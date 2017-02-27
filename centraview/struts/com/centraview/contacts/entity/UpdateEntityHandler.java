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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.entity.EntityVOX;
import com.centraview.settings.Settings;

public final class UpdateEntityHandler extends Action {
  private static Logger logger = Logger.getLogger(UpdateEntityHandler.class);

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession(true);
    UserObject userobject = (UserObject)session.getAttribute("userobject");
    int individualId = userobject.getIndividualID();

    ActionMessages allErrors = new ActionMessages();

    DynaActionForm entityForm = (DynaActionForm)form;
    EntityVO entityVO = new EntityVOX(form, request, dataSource);

    String entityName = entityVO.getName();
    if (entityName == null || entityName.length() <= 0) {
      allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField",
          "Entity Name"));
    }

    entityVO.setModifiedBy(individualId);

    ContactFacade contactFacade = null;
    try {
      contactFacade = (ContactFacade)CVUtility.setupEJB("ContactFacade",
          "com.centraview.contact.contactfacade.ContactFacadeHome", dataSource);
    } catch (Exception e) {
      throw new ServletException(e);
    }

    ActionForward forward = null;
    StringBuffer path = new StringBuffer();
    // Decide if we are updating an existing or creating a new
    if (entityVO.getContactID() < 1) {
      // set the creator
      entityVO.setCreatedBy(individualId);
      // if necessary the account manager
      if (entityVO.getAccManager() < 1) {
        entityVO.setAccManager(individualId);
      }
      String marketingListSession = (String)session.getAttribute("dbid");
      int marketingList = 1;
      try {
        marketingList = Integer.valueOf(marketingListSession).intValue();
      } catch (NumberFormatException nfe) {}

      entityVO.setList(marketingList);

      if (!allErrors.isEmpty()) {
        // if there were any validation errors, show the
        // new entity form again, with error messages...
        this.saveErrors(request, allErrors);
        return mapping.findForward(".view.contact.new_entity");
      }

      int newEntityId = contactFacade.createEntity(entityVO, individualId);

      if (request.getParameter("new") != null) {
        path.append(mapping.findForward("newEntity").getPath());
        path.append(entityVO.getList());
        forward = new ActionForward(path.toString(), true);
      } else {
        path.append(mapping.findForward("viewEntity").getPath());
        path.append(newEntityId);
        path.append("&closeWindow=false");
        forward = new ActionForward(path.toString(), true);
      }
    } else {
      try {
        contactFacade.updateEntity(entityVO, individualId);
      } catch (Exception e) {
        logger.error("[execute] Exception thrown.", e);
        throw new ServletException(e);
      }
      path.append(mapping.findForward("viewEntity").getPath());
      path.append(entityVO.getContactID());
      path.append("&closeWindow=");
      path.append(entityForm.get("closeWindow"));
      forward = new ActionForward(path.toString(), true);
    }
    return forward;
  } // end of execute method
} // end of UpdateEntityHandler class
