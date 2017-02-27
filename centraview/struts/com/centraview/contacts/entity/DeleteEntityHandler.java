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
import com.centraview.settings.Settings;

public final class DeleteEntityHandler extends Action {

  private static Logger logger = Logger.getLogger(DeleteEntityHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession(true);
    int individualID = ((UserObject)session.getAttribute("userobject")).getIndividualID();

    String returnStatus = "deletesuccess";

    String rowId[] = request.getParameterValues("rowId");

    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    try {
      ContactFacade remote = aa.create();
      remote.setDataSource(dataSource);
      if (rowId != null) {
        for (int i = 0; i < rowId.length; i++) {
          remote.deleteEntity(Integer.parseInt(rowId[i]), individualID);
        }// for (int i=0;i<rowId.length;i++)
      }
    } catch (Exception e) {
      logger.error("[Exception] DeleteEntityHandler.Execute Handler ", e);
    }
    return (mapping.findForward(returnStatus));
  }// end of execute method
}// end of class DeleteEntityHandler
