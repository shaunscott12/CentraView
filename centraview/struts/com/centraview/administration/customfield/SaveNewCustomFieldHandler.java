/*
 * $RCSfile: SaveNewCustomFieldHandler.java,v $    $Revision: 1.2 $  $Date: 2005/06/14 21:11:43 $ - $Author: mking_cv $
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
package com.centraview.administration.customfield;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
import com.centraview.settings.Settings;

/**
 * SaveNewCustomFieldHandler.java
 *
 * @author CentraView, LLC.
 */
public class SaveNewCustomFieldHandler extends Action
{
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_savenew = "savenew";
  private static final String FORWARD_saveclose = "saveclose";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;


  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String typeOfSave = "saveclose";
    String module = "";
    String tableName = "";
    
    try
    {
      HttpSession session = request.getSession();
      int IndividualId = ((UserObject) session.getAttribute("userobject")).getIndividualID();
      
      if (request.getParameter("buttonpress") != null)
      {
        typeOfSave = request.getParameter("buttonpress");
      }

      DynaActionForm cf = (DynaActionForm) form;
      module = request.getParameter("module").toString();
      tableName = request.getParameter("table").toString();

      int tableId = 0;
      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      HashMap hmp = (HashMap) gml.get("Tables");

      if (hmp != null)
      {
        Long lngObj = (Long) hmp.get(tableName);

        if (lngObj != null)
        {
          tableId = lngObj.intValue();
        }
      }

      CustomFieldVO customfieldVO = new CustomFieldVO();
      customfieldVO.setLabel(cf.get("fieldname").toString());
      customfieldVO.setRecordType(tableName);
      customfieldVO.setRecordTypeID(tableId);
      customfieldVO.setFieldType((String) cf.get("layerSwitch"));

      if ((cf.get("layerSwitch")).equals(CustomFieldVO.MULTIPLE))
      {
        String[] optionValues = (String[]) cf.get("valuelist");
        Vector vecOption = new Vector();

        for (int i = 0; i < optionValues.length; i++)
        {
          vecOption.addElement(optionValues[i]);
        }

        customfieldVO.setOptionValues(vecOption);
      }

      CustomFieldHome customfieldHome = (CustomFieldHome) CVUtility
        .getHomeObject("com.centraview.customfield.CustomFieldHome", "CustomField");
      CustomField customfieldRemote = (CustomField) customfieldHome.create();
      customfieldRemote.setDataSource(dataSource);
      customfieldRemote.addNewCustomField(customfieldVO);

      if (typeOfSave.equals("savenew"))
      {
        request.setAttribute("typeofmodule", AdministrationConstantKeys.MODULESETTINGS);
        request.setAttribute("typeofsubmodule", module);
        request.setAttribute("module", module);
        FORWARD_final = FORWARD_savenew;
      }
      else if (typeOfSave.equals("saveclose"))
      {
        request.setAttribute("typeofmodule", AdministrationConstantKeys.MODULESETTINGS);
        request.setAttribute("typeofsubmodule", module);
        request.setAttribute("module", module);
        FORWARD_final = FORWARD_saveclose;
      }
      //Reset the Form
      cf.reset(mapping, request);
    }
    catch (Exception e)
    {
      System.out.println("[Exception] SaveNewCustomFieldHandler.execute: " + e.toString());
      //e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    
    ActionForward returnForward = mapping.findForward(FORWARD_final);
    StringBuffer newPathBuffer = new StringBuffer();
    newPathBuffer.append(returnForward.getPath());
    if (typeOfSave.equals("savenew"))
    {
      newPathBuffer.append("?actionType=new&module=" + module);
    } //end of if statement (saveandclose == null)

    if (typeOfSave.equals("saveclose"))
    {
      newPathBuffer.append("?module=" + module);
    } //end of if statement (saveandclose == null)
    
    ActionForward newActionForward = new ActionForward();
    newActionForward.setPath(newPathBuffer.toString());
    newActionForward.setRedirect(returnForward.getRedirect());
    newActionForward.setContextRelative(returnForward.getContextRelative());
    newActionForward.setName(returnForward.getName());
    //System.out.println("[DEBUG] [SaveNewCustomField]: Forwarding to :" + newActionForward.getPath());
    //System.out.println("[DEBUG] [SaveNewCustomField]: Exiting .");
    return (newActionForward);    
  }
}
