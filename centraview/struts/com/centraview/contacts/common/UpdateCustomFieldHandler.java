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

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
import com.centraview.settings.Settings;

public final class UpdateCustomFieldHandler extends Action {
  private static Logger logger = Logger.getLogger(UpdateCustomFieldHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String saveandclose = request.getParameter("saveandclose");
    String cancel = request.getParameter("Cancel");

    String returnStatus = ".view.error";
    if ((saveandclose != null)) {
      returnStatus = perform("saveandclose", request);
      request.setAttribute("closeWindow", "true");
    } else if ((cancel != null)) {
      returnStatus = "cancel";
    }
    request.setAttribute("refreshParent", "true");
    return (mapping.findForward(returnStatus));
  }

  public String perform(String returnStatus, HttpServletRequest request)
      throws CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String status = returnStatus;
    CustomFieldVO custVo = new CustomFieldVO();
    String fieldidStr = request.getParameter("fieldid");

    HttpSession session = request.getSession(true);

    UserObject userobject = (UserObject)session.getAttribute("userobject");
    int individualID = userobject.getIndividualID();

    if (fieldidStr == null) {
      fieldidStr = "0";
    }
    int fieldId = Integer.parseInt(fieldidStr);
    String fieldType = request.getParameter("fieldType");
    String value = request.getParameter("text");

    if (value == null) {
      value = "";
    }

    int recordID = 0;
    if (request.getParameterValues("recordID") != null) {
      recordID = Integer.parseInt(request.getParameter("recordID"));
    }

    custVo.setValue(value);
    custVo.setFieldType(fieldType);
    custVo.setFieldID(fieldId);
    custVo.setRecordID(recordID);

    CustomFieldHome cfh = (CustomFieldHome)CVUtility.getHomeObject(
        "com.centraview.customfield.CustomFieldHome", "CustomField");

    try {
      CustomField remote = cfh.create();
      remote.setDataSource(dataSource);

      remote.updateCustomField(custVo);

      CustomFieldVO cfvo = remote.getCustomField(fieldId);

      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);

      cfvo.setRecordID(recordID);
      remoteContactFacade.updateModifiedBy(cfvo, individualID);
    } catch (Exception e) {
      logger.error("[Exception] UpdateCustomFieldHandler.perform  ", e);
    }
    return status;
  }

}
