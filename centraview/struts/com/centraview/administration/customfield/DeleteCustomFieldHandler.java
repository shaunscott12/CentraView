/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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
 * Original Code created by CentraView are Copyright (c) 2003 - 2005 CentraView,
 * LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
 */
package com.centraview.administration.customfield;

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

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
import com.centraview.settings.Settings;

/**
 * Action to delete the custom field and return to the list of custom fields for
 * the appropriate module.
 * @author mcallist
 */
public class DeleteCustomFieldHandler extends Action {
    private static Logger logger = Logger.getLogger(DeleteCustomFieldHandler.class);
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
      // Get the customFieldId from the action form.
      DynaActionForm customFieldForm = (DynaActionForm)form;
      Integer customFieldId = (Integer)customFieldForm.get("fieldid");
      // Call the EJB to do the dirty work
      String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualId = userObject.getIndividualID();
      try {
        CustomFieldHome customfieldHome = (CustomFieldHome)CVUtility.getHomeObject("com.centraview.customfield.CustomFieldHome","CustomField");
        CustomField customField = customfieldHome.create();
        customField.setDataSource(dataSource);
        customField.deleteCustomField(individualId, customFieldId.intValue());
      } catch (Exception e) {
        logger.error("[execute]: Failed trying to delete Custom Field ID: "+customFieldId, e);
        throw new ServletException(e);
      }
      // Figure out what module we are in and adjust the forward appropriately
      String moduleName = (String)customFieldForm.get("module");
      ActionForward listForward = mapping.findForward("customFieldList");
      StringBuffer forwardPath = new StringBuffer();
      forwardPath.append(listForward.getPath());
      forwardPath.append("?module=");
      forwardPath.append(moduleName);
      ActionForward actionForward = new ActionForward("customFieldList", forwardPath.toString(), listForward.getRedirect());
      return actionForward;
    }
}
