/*
 * $RCSfile: GobalReplacePerform.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:41 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.centraview.com/opensource/license.html
 * 
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The Original Code is: CentraView Open Source. 
 * 
 * The developer of the Original Code is CentraView.  Portions of the
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
 */

package com.centraview.administration.globalreplace;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * @author Naresh Patel <npatel@centraview.com>
 */
public class GobalReplacePerform extends Action
{
	private static Logger logger = Logger.getLogger(GobalReplacePerform.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException,NamingException
  {
		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		GlobalReplace globalReplace = null;
		//intialize the GlobalReplaceEJB
		GlobalReplaceHome globalReplaceHome = (GlobalReplaceHome)CVUtility.getHomeObject("com.centraview.administration.globalreplace.GlobalReplaceHome", "GlobalReplace");
		try
		{
			globalReplace = globalReplaceHome.create();
			globalReplace.setDataSource(dataSource);
		}
		catch (Exception e)
		{
			logger.error("[Exception] GobalReplacePerform.Execute Handler ", e);
		}

		try{
			request.setAttribute("typeofmodule", AdministrationConstantKeys.DATAADMINISTRATION);
			request.setAttribute("typeoflist", AdministrationConstantKeys.GLOBALREPLACE);
			request.setAttribute("typeofsubmodule", AdministrationConstantKeys.GLOBALREPLACE);
			request.setAttribute("body","Next");
			HttpSession session = request.getSession(true);

			// Need the userobject so we know who we are.
			UserObject userObject = (UserObject)session.getAttribute("userobject");
			int individualId = userObject.getIndividualID();

			HashMap replaceInfo = new HashMap();
			replaceInfo.put("individualID", new Integer(individualId));

			DynaActionForm globalReplaceForm = (DynaActionForm)form;

			String replaceTableIDString = (String)globalReplaceForm.get("replaceTableID");;

			int replaceTableID = 0;
			int moduleId = 14;

			//Parse the Selected TableID which will contain the information of tableID, primaryTableName
			// With primary Table get the moduleID
			if(replaceTableIDString != null){
				StringTokenizer tableInfo = new StringTokenizer(replaceTableIDString, "#");
				String tableIDString = null;
				String primaryTableName = null;
				if(tableInfo != null){
					while (tableInfo.hasMoreTokens())
					{
						tableIDString = (String) tableInfo.nextToken();
						primaryTableName = (String) tableInfo.nextToken();
					}//end of while (tableInfo.hasMoreTokens())
					if(tableIDString != null && primaryTableName != null && !tableIDString.equals("") && !primaryTableName.equals("")){
						replaceTableID =  Integer.parseInt(tableIDString);
						String moduleIdString = null;
						try {
							moduleIdString = AdvancedSearchUtil.getModuleId(primaryTableName,dataSource);
						} catch (Exception e) {
							logger.error("[Exception] GobalReplacePerform.Execute Handler ", e);
						}
						if(moduleIdString != null){
							moduleId = Integer.parseInt(moduleIdString);
						}//end of if(moduleIdString != null)
					}//end of if(tableIDString != null && primaryTableName != null && !tableIDString.equals("") && !primaryTableName.equals(""))
				}//end of if(tableInfo != null)
			}//end of if(replaceTableIDString != null)

			// make sure the one we finally decided on, is on the form
			replaceInfo.put("tableID", new Integer(replaceTableID));

			// Parse the selected FieldID.
			String replaceFieldIDString = (String)globalReplaceForm.get("replaceFieldID");
			int fieldTableID = 0;
			int replaceFieldID = 0;
			int replaceFieldType = 0;
			if(replaceFieldIDString != null){
				StringTokenizer fieldInfo = new StringTokenizer(replaceFieldIDString, "*");
				String fieldIDString = null;
				String tableIDString = null;
				String fieldTypeString = null;
				if(fieldInfo != null){
					while (fieldInfo.hasMoreTokens())
					{
						tableIDString = (String) fieldInfo.nextToken();
						fieldIDString = (String) fieldInfo.nextToken();
						fieldTypeString = (String) fieldInfo.nextToken();
					}//end of while (fieldInfo.hasMoreTokens())
					if(fieldIDString != null && fieldTypeString != null && !fieldIDString.equals("") && !fieldTypeString.equals("")){
						fieldTableID = Integer.parseInt(tableIDString);
						replaceFieldID = Integer.parseInt(fieldIDString);
						replaceFieldType =  Integer.parseInt(fieldTypeString);
					}//end of if(fieldIDString != null && fieldTypeString != null && !fieldIDString.equals("") && !fieldTypeString.equals(""))
				}//end of if(fieldInfo != null)
			}//end of if(replaceFieldIDString != null)

			//We are going to replace the selected field by the replaceValue
			String replaceValue = (String)globalReplaceForm.get("replaceValue");

			//Frame the actual Value which is going to be replace, which should be should back to user.
			String actualValue = replaceValue;

			// If its a Phone Type contact Extension to the actualvalue and replacing value
			if(replaceFieldType == GobalReplaceConstantKeys.FIELD_TYPE_PHONE){
				String replaceExt = (String)globalReplaceForm.get("replaceExt");
				replaceValue = replaceValue +" EXT "+ replaceExt;
				actualValue = replaceValue;
			}

			if(replaceFieldType == GobalReplaceConstantKeys.FIELD_TYPE_MULTIPLE){
				String replaceID = (String)globalReplaceForm.get("replaceID");
				replaceValue = replaceID;
			}
			// If the field type is lookup then pass the id to the EJB and pass the Actual value for the id to sucess page
			if(replaceFieldType != GobalReplaceConstantKeys.FIELD_TYPE_PHONE && replaceFieldType != 0 && replaceFieldType != GobalReplaceConstantKeys.FIELD_TYPE_MULTIPLE){
				String replaceID = (String)globalReplaceForm.get("replaceID");
				replaceValue = replaceID;
			}

			replaceInfo.put("fieldValue", replaceValue);
			replaceInfo.put("fieldInfo", replaceFieldIDString);


			globalReplaceForm.set("replaceValue",replaceValue);

			String fieldName = (String)globalReplaceForm.get("fieldName");
			replaceInfo.put("fieldName", fieldName);

			String searchType = (String)globalReplaceForm.get("searchType");

			SearchVO searchObject = (SearchVO) session.getAttribute("searchObject");

			replaceInfo.put("searchVO", searchObject);
			request.setAttribute("displayListFlag","true");

			//Collection of HashMap with its setted value to EJB and Perform GlobalReplace
			boolean updateFlag = globalReplace.performGlobalReplace(replaceInfo);
			String failureOrSuccess = "Success! you have successfully replaced the follwoing field:";
			if(!updateFlag){
				failureOrSuccess = "Failed! updated some records or failed to update all searched records for the follwoing field:";
			}
			globalReplaceForm.set("failureOrSuccess",failureOrSuccess);
			session.removeAttribute("searchObject");
			globalReplaceForm.set("actualValue",actualValue);
		}
		catch (Exception e)
		{
			logger.error("[Exception] GobalReplacePerform.Execute Handler ", e);
		}
		return (mapping.findForward("showGlobalReplace"));
  } //end of execute method

}
