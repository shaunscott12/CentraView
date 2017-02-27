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
import com.centraview.contact.individual.IndividualVO;
import com.centraview.contact.individual.IndividualVOX;
import com.centraview.settings.Settings;

/**
 * this class will get http request from Indivividualdetails_C JSP then get get
 * form and get IndividualVO from VOX then call updateIndividual method
 */
public class EditIndividualHandler extends Action {

  private static Logger logger = Logger.getLogger(EditIndividualHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    DynaActionForm individualForm = (DynaActionForm)form;
    // The individualVOX takes the form and converts it to a VO
    IndividualVO individualVO = new IndividualVOX(form, request, dataSource);
    HttpSession session = request.getSession(true);
    UserObject userobject = (UserObject)session.getAttribute("userobject");
    int individualId = userobject.getIndividualID();
    individualVO.setModifiedBy(individualId);
    ContactFacade contactFacade = null;
    try {
      contactFacade = (ContactFacade)CVUtility.setupEJB("ContactFacade",
          "com.centraview.contact.contactfacade.ContactFacadeHome", dataSource);
    } catch (Exception e) {
      throw new ServletException(e);
    }
    ActionForward forward = null;
    StringBuffer path = new StringBuffer();
    // Decide if we are updating an existing or creating a new individual
    if (individualVO.getContactID() < 1) {
      String marketingListSession = (String)session.getAttribute("dbid");
      int marketingList = 1;
      try {
        marketingList = Integer.valueOf(marketingListSession).intValue();
      } catch (NumberFormatException nfe) {}
      individualVO.setList(marketingList);
      individualVO.setCreatedBy(individualId);
      int newIndividualId = 0;
      try {
        newIndividualId = contactFacade.createIndividual(individualVO, individualId);
      } catch (Exception e) {
        logger.error("[execute] Exception thrown.", e);
        throw new ServletException(e);
      }
      if (request.getParameter("new") != null) {
        path.append(mapping.findForward("newIndividual").getPath());
        path.append(individualVO.getList());
        path.append("&entityNo=");
        path.append(individualVO.getEntityID());
        path.append("&entityName=");
        path.append(individualVO.getEntityName());
        forward = new ActionForward(path.toString(), true);
      } else {
        path.append(mapping.findForward("viewIndividual").getPath());
        path.append(newIndividualId);
        path.append("&closeWindow=false");
        forward = new ActionForward(path.toString(), true);
      }
    } else {
      try {
        contactFacade.updateIndividual(individualVO, individualId);
      } catch (Exception e) {
        logger.error("[execute] Exception thrown.", e);
        throw new ServletException(e);
      }
      path.append(mapping.findForward("viewIndividual").getPath());
      path.append(individualVO.getContactID());
      path.append("&closeWindow=");
      path.append(individualForm.get("closeWindow"));
      forward = new ActionForward(path.toString(), true);
    }
    return forward;
  }
}