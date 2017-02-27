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

package com.centraview.contacts.individual;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.Globals;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.contacts.entity.NewEntityHandler;
import com.centraview.customfield.CustomField;
import com.centraview.settings.Settings;

public final class IndividualNewHandler extends Action {
  private static Logger logger = Logger.getLogger(NewEntityHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    final String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    DynaActionForm individualForm = (DynaActionForm)form;
    int entityId = 0;
    try {
      entityId = request.getParameter("entityNo") == null ? 0 : (Integer.valueOf(request
          .getParameter("entityNo"))).intValue();
    } catch (NumberFormatException e) {} // use default (zero)
    String entityName = request.getParameter("entityName") == null ? "" : (String)request
        .getParameter("entityName");
    String marketingListIdParam = request.getParameter("marketingListId");
    Integer marketingListId = marketingListIdParam != null ? new Integer(marketingListIdParam)
        : new Integer(1);

    if (marketingListIdParam == null && entityId > 0) {
      // We have an entity Id, and there wasn't a ListId passed in, we should
      // ask the database
      // What ListId to use. It will default to 1.
      // Get the marketingList for the parent EntityId and set our parameter
      // accordingly.
      try {
        ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject(
            "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
        ContactFacade contactFacadeRemote = contactFacadeHome.create();
        contactFacadeRemote.setDataSource(dataSource);
        marketingListId = contactFacadeRemote.getEntityMarketingList(entityId);
      } catch (Exception e) {
        // If something goes wrong, it's okay we will default to marketingList 1
        logger.error("[Exception] execute(): ", e);
      }
    }
    // moc Types.
    individualForm.set("mocTypeList", Globals.MOC_TYPE);
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
    TreeMap customFieldMap = customField.getCustomFieldData("Individual");
    Collection customFieldValues = customFieldMap.values();
    int arraySize = customFieldValues.size() > 8 ? 8 : customFieldValues.size();
    CustomFieldVO[] fieldArray = new CustomFieldVO[arraySize];
    Iterator i = customFieldValues.iterator();
    int count = 0;
    while (i.hasNext() && count < 8) {
      fieldArray[count++] = (CustomFieldVO)i.next();
    }
    individualForm.set("customFields", fieldArray);
    individualForm.set("marketingListId", marketingListId);
    individualForm.set("entityId", new Integer(entityId));
    individualForm.set("entityName", entityName);
    return (mapping.findForward(".view.contact.new_individual"));
  }
}