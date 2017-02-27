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

package com.centraview.contacts.common;

import java.io.IOException;

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

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.AddressVOX;
import com.centraview.settings.Settings;

public final class AddAddressHandler extends Action {
  private static Logger logger = Logger.getLogger(AddAddressHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String saveandclose = request.getParameter("saveandclose");
    String saveandnew = request.getParameter("saveandnew");
    String cancel = request.getParameter("Cancel");

    String returnStatus = "failure";

    if ((saveandclose != null)) {
      returnStatus = perform("saveandclose", form, request);
      request.setAttribute("closeWindow", "true");
    } else if ((saveandnew != null)) {
      returnStatus = perform("saveandnew", form, request);
      clearForm((DynaActionForm)form, mapping);
    } else if ((cancel != null)) {
      returnStatus = "cancel";
    }
    request.setAttribute("refreshParent", "true");
    return (mapping.findForward(returnStatus));
  }

  public String perform(String returnStatus, ActionForm form, HttpServletRequest request)
      throws CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String status = returnStatus;
    AddressVOX addressVOX = new AddressVOX(form);
    HttpSession session = request.getSession();
    AddressVO addVO = addressVOX.getVO();

    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualID = userObject.getIndividualID();

    if (addVO != null) {
      addVO.setAddressType(1);
    }

    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");

    try {
      ContactFacade remote = aa.create();
      remote.setDataSource(dataSource);

      int contactId = 0;
      try {
        contactId = Integer.parseInt((String)((DynaActionForm)form).get("recordID"));
      } catch (NumberFormatException nfe) {
        // carry on, we already set a default value
      }

      String listFor = (String)((DynaActionForm)form).get("listFor");
      int contactType = 1;
      if (listFor != null && listFor.equals("Entity")) {
        contactType = 1;
      } else if (listFor != null && listFor.equals("Individual")) {
        contactType = 2;
      }

      remote.createAddress(contactId, contactType, addVO, individualID);
    } catch (Exception e) {
      logger.error("[Exception] AddAddressHandler.Execute Handler ", e);
    }
    return status;
  }

  public void clearForm(DynaActionForm form, ActionMapping mapping)
  {
    form.initialize(mapping);
  }

}
