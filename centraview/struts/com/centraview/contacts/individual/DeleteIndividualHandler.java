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

/**
 * this class will get http request from JSP with individualid then call
 * deleteIndividual method to delete individual
 */

public class DeleteIndividualHandler extends Action {
  private static Logger logger = Logger.getLogger(DeleteIndividualHandler.class);

  /**
   * Creation date: (6/15/2003 11:59:03 AM)
   * @return org.apache.struts.action.ActionForward
   * @param param org.apache.struts.action.ActionMapping
   * @param param org.apache.struts.action.ActionForm
   * @param param org.apache.struts.action.ActionForm
   * @param param javax.servlet.http.HttpServletRequest
   * @param param javax.servlet.http.HttpServletResponse
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";

    HttpSession session = request.getSession(true);
    UserObject userobject = (UserObject)session.getAttribute("userobject");

    int indvID = 0;
    if (userobject != null)
      indvID = userobject.getIndividualID();

    ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    String[] rowId = request.getParameterValues("rowId");
    returnStatus = "deletesuccess";
    try {
      ContactFacade remote = cfh.create();
      remote.setDataSource(dataSource);
      if (rowId != null) {
        for (int i = 0; i < rowId.length; i++) {
          remote.deleteIndividual(Integer.parseInt(rowId[i]), indvID);
        }// end of for (int i = 0; i < rowId.length; i++)
      }// end of if (rowId != null)
    }// end of try block
    catch (Exception e) {
      logger.error("[Exception] DeleteIndividualHandler.Execute Handler ", e);
    }// end of catch block
    return (mapping.findForward(returnStatus));
  }// end of execute method
}// end of class
