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
 * Original Code created by CentraView are Copyright (c) 2004 CentraView,
 * LLC; All Rights Reserved.  The terms "CentraView" and the CentraView
 * logos are trademarks and service marks of CentraView, LLC.
 */

package com.centraview.contacts.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.Globals;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.customfield.CustomField;
import com.centraview.settings.Settings;

public class NewEntityHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(NewEntityHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    DynaActionForm entityForm = (DynaActionForm)form;
    // moc Types.
    entityForm.set("mocTypeList", Globals.MOC_TYPE);
    // Get the custom field stuff and set it on the form.
    CustomField customField = null;
    try {
      customField = (CustomField)CVUtility.setupEJB("CustomField",
          "com.centraview.customfield.CustomFieldHome", dataSource);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    // Only 8 custom fields fit on the detail screen so we have to pull only the
    // first 8 out.
    TreeMap customFieldMap = customField.getCustomFieldData("Entity");
    Collection customFieldValues = customFieldMap.values();
    int arraySize = customFieldValues.size() > 4 ? 4 : customFieldValues.size();
    CustomFieldVO[] fieldArray = new CustomFieldVO[arraySize];
    Iterator i = customFieldValues.iterator();
    int count = 0;
    while (i.hasNext() && count < 4) {
      fieldArray[count++] = (CustomFieldVO)i.next();
    }
    entityForm.set("customFields", fieldArray);
    // marketingListId passed on the parameter otherwise I will default to the
    // default list (1)
    int marketingListId = request.getParameter("marketingListId") == null ? 1 : Integer
        .parseInt(request.getParameter("marketingListId"));
    entityForm.set("marketingListId", new Integer(marketingListId));
    return (mapping.findForward(".view.contact.new_entity"));
  }
}
