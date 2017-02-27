/*
 * $RCSfile: DisplayEditCustomFieldHandler.java,v $    $Revision: 1.4 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
import com.centraview.settings.Settings;

public class DisplayEditCustomFieldHandler extends org.apache.struts.action.Action
{
  private static final String forward = ".view.administration.view_custom_field";
  private static Logger logger = Logger.getLogger(DisplayEditCustomFieldHandler.class);
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualId = userObject.getIndividualID();    // logged in user

      DynaActionForm customFieldForm = (DynaActionForm)form;

      ActionErrors allErrors = new ActionErrors();

      // get the field ID from the form
      Integer customFieldId = (Integer)customFieldForm.get("fieldid");
      if (customFieldId == null && customFieldId.intValue() <= 0) {
        // if it's not a valid integer, show user an error
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Field ID"));
        saveErrors(request, allErrors);
        return(mapping.findForward(forward));
      }
     
      CustomFieldVO customFieldVO = new CustomFieldVO();  
      
      CustomFieldHome customfieldHome = (CustomFieldHome)CVUtility.getHomeObject("com.centraview.customfield.CustomFieldHome","CustomField");
      CustomField customfieldRemote = customfieldHome.create();
      customfieldRemote.setDataSource(dataSource);
      
      // get the custom field data from the EJB layer
      customFieldVO = customfieldRemote.getCustomField(customFieldId.intValue());

      customFieldForm.set("fieldid", customFieldId);
      customFieldForm.set("fieldname", customFieldVO.getLabel());
      customFieldForm.set("layerSwitch", customFieldVO.getFieldType());
      customFieldForm.set("recordTypeId", new Integer(customFieldVO.getRecordTypeID()));

      HashMap moduleInfo = customfieldRemote.getFieldModuleInfo(individualId, customFieldId.intValue());
      customFieldForm.set("module", moduleInfo.get("moduleName"));
      customFieldForm.set("recordtype", moduleInfo.get("recordType"));
      request.setAttribute("valuelist", customFieldVO.getOptionValues());
      request.setAttribute("valueidslist", customFieldVO.getOptionValuesIds());
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }
    return (mapping.findForward(forward));
  }   // end execute() method
  
}   // end class definition

