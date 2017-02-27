/*
 * $RCSfile: GobalReplaceSearchHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.advancedsearch.AdvancedSearch;
import com.centraview.advancedsearch.AdvancedSearchHome;
import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

/**
 * @author Naresh Patel <npatel@centraview.com>
 */
public class GobalReplaceSearchHandler extends Action
{
  private static Logger logger = Logger.getLogger(GobalReplaceSearchHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException,NamingException
  {
		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		ActionErrors allErrors = new ActionErrors();
		HttpSession session = request.getSession(true);
		String returnStatus = "showGlobalReplace";

		// Need the userobject so we know who is logged in.
		UserObject userObject = (UserObject)session.getAttribute("userobject");
		int individualId = userObject.getIndividualID();

		request.setAttribute("typeofmodule", AdministrationConstantKeys.DATAADMINISTRATION);
		request.setAttribute("typeoflist", AdministrationConstantKeys.GLOBALREPLACE);
		request.setAttribute("typeofsubmodule", AdministrationConstantKeys.GLOBALREPLACE);

		DynaActionForm globalReplaceForm = (DynaActionForm)form;

		// check the parameters to see if a particular searchId was passed in
		String searchIdParameter = (String)request.getAttribute("searchId");
		String selectedSearchId = searchIdParameter != null ? searchIdParameter : "0";

		try{
			// replaceTableID should be set on the request by the Dispatch.
			String replaceTableIDString = (String)request.getAttribute("replaceTableID");

			int replaceTableID = 0;
			int moduleId = 14;
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
						int tableID =  Integer.parseInt(tableIDString);
						String moduleIdString = AdvancedSearchUtil.getModuleId(primaryTableName, dataSource);
						if(moduleIdString != null){
							moduleId = Integer.parseInt(moduleIdString);
						}//end of if(moduleIdString != null)
					}//end of if(tableIDString != null && primaryTableName != null && !tableIDString.equals("") && !primaryTableName.equals(""))
				}//end of if(tableInfo != null)
			}//end of if(replaceTableIDString != null)

			globalReplaceForm.set("moduleId", new Integer(moduleId));

			ContactFacadeHome cfh = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
			try
			{
				ContactFacade remote = (ContactFacade) cfh.create();
				remote.setDataSource(dataSource);
				Vector listVec = remote.getDBList(individualId);
				globalReplaceForm.set("listVec",listVec);
			}
			catch (Exception e)
			{
				logger.error("[Exception] GobalReplaceSearchHandler.Execute Handler ", e);
			}

			// Get Saved searches from the EJB
			GlobalReplace globalReplace = null;
			GlobalReplaceHome globalReplaceHome = (GlobalReplaceHome)CVUtility.getHomeObject("com.centraview.administration.globalreplace.GlobalReplaceHome", "GlobalReplace");
			try
			{
				globalReplace = globalReplaceHome.create();
				globalReplace.setDataSource(dataSource);
			}
			catch (Exception e)
			{
				logger.error("[Exception] GobalReplaceSearchHandler.Execute Handler ", e);
			}

			Vector primaryReplaceTableList = globalReplace.getPrimaryReplaceTables();
			globalReplaceForm.set("replaceTableVec",primaryReplaceTableList);

			// intialize the Advance Search EJB
			AdvancedSearch remoteAdvancedSearch = null;
			AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
			try
			{
				remoteAdvancedSearch = advancedSearchHome.create();
				remoteAdvancedSearch.setDataSource(dataSource);
			}
			catch (Exception e)
			{
				logger.error("[Exception] GobalReplaceSearchHandler.Execute Handler ", e);
			}

			HashMap savedSearchMap = remoteAdvancedSearch.getSavedSearchList(individualId, moduleId);
			Vector savedSearchVec = new Vector();
			if(savedSearchMap != null){
				Set savedSearchIds = savedSearchMap.keySet();
				Iterator idIterator = savedSearchIds.iterator();
				while (idIterator.hasNext())
				{
					Number searchId = (Number)idIterator.next();
					String searchName = (String)savedSearchMap.get(searchId);
					savedSearchVec.add(new DDNameValue(searchId.toString(),searchName));
				}
			}
			globalReplaceForm.set("savedSearchVec", savedSearchVec);

			HashMap allFields = (HashMap)globalReplaceForm.get("allFields");
			// Only repopulate all the lists if it is empty
			if (allFields == null || allFields.isEmpty())
			{
				// Build dem drop downs
				HashMap tableMap = remoteAdvancedSearch.getSearchTablesForModule(individualId, moduleId);
				HashMap conditionMap = SearchVO.getConditionOptions();
				// Build an ArrayList of LabelValueBeans
				ArrayList tableList = AdvancedSearchUtil.buildSelectOptionList(tableMap);
				ArrayList conditionList = AdvancedSearchUtil.buildSelectOptionList(conditionMap);
				globalReplaceForm.set("tableList", tableList);
				globalReplaceForm.set("conditionList", conditionList);

				// Get all the appropriate field lists and stick them on the formbean
				// The fieldList (ArrayList of LabelValueBean) will be stored in a
				// HashMap with the key being the (Number)tableId
				TreeSet keySet = new TreeSet(tableMap.keySet());
				Iterator keyIterator = keySet.iterator();
				allFields = new HashMap();
				while(keyIterator.hasNext())
				{
					Number key = (Number)keyIterator.next();
					// iterate the tables and get all the field lists
					// stick them in a hashmap of arraylists on the form bean.
					HashMap tableFields = remoteAdvancedSearch.getSearchFieldsForTable(individualId, key.intValue(), moduleId);
					ArrayList tableFieldList = AdvancedSearchUtil.buildSelectOptionList(tableFields);
					allFields.put(key, tableFieldList);
				} // end while(keyIterator.hasNext())
				globalReplaceForm.set("allFields",allFields);
			} // end if (allFields == null || allFields.isEmpty())

			// if we have a SearchVO then populate the formbean with its contents.
			// Otherwise we will show a blank form.  A clean slate.  A new beginning.
			SearchCriteriaVO [] searchCriteria = (SearchCriteriaVO [])globalReplaceForm.get("searchCriteria");

			// Add the Selected Line from the criteria list
			String addRow = (String)globalReplaceForm.get("addRow");
			if(addRow.equals("true"))
			{
				searchCriteria = AdvancedSearchUtil.addRow(searchCriteria);
				globalReplaceForm.set("addRow","false");
			}

			// Remove the Selected Line from the criteria list
			String removeRow = (String)globalReplaceForm.get("removeRow");
			if(!removeRow.equals("false"))
			{
				searchCriteria = AdvancedSearchUtil.removeRow(searchCriteria, removeRow);
				globalReplaceForm.set("removeRow","false");
			}

			globalReplaceForm.set("searchCriteria", searchCriteria);

			String actionType = (String)globalReplaceForm.get("actionType");
			
			// If actionType is Fields then check wheather the user selecte the New Search.
			// then Error Out the user saying he must select the values for one line atleast.
			if(actionType != null && actionType.equals("Fields")){
				returnStatus = "showGlobalReplaceFields";
				String searchType = (String)globalReplaceForm.get("searchType");
				if(searchType != null && searchType.equals("New Search")){
					if(searchCriteria.length == 1){
						SearchCriteriaVO  searchCriteriaVO = searchCriteria[0];
						String tableID = searchCriteriaVO.getTableID();
						String fieldID = searchCriteriaVO.getFieldID();
						String conditionID = searchCriteriaVO.getConditionID();
						if(tableID != null && tableID.equals("0")){
							allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Record Type"));
						}//end of if(tableID != null && tableID.equals("0"))
						if(fieldID != null && fieldID.equals("0")){
							allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Field"));
						}//end of if(fieldID != null && fieldID.equals("0"))
						if(conditionID != null && conditionID.equals("0")){
							allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Condition"));
						}//end of if(conditionID != null && conditionID.equals("0"))
						if(!allErrors.isEmpty()){
							saveErrors(request, allErrors);
							returnStatus = "showGlobalReplace";
						}//end of if(!allErrors.isEmpty())
					}//end of if(searchCriteria.length == 1)
				}//end of if(searchType != null && searchType.equals("New Search"))
			}//end of if(actionType != null && actionType.equals("Fields"))
		}
		catch (Exception e)
		{
			logger.error("[Exception] GobalReplaceSearchHandler.Execute Handler ", e);
		}
		return (mapping.findForward(returnStatus));
  } //end of execute method
}
