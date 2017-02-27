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
import java.util.ArrayList;
import java.util.Iterator;

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
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.ContactVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;

/**
 * This handler is used when Viewing the Contact Change MOC Order page.
 * @author Ryan Grier <ryan@centraview.com>
 */
public final class ViewContactChangeMOCHandler extends Action {
  private static Logger logger = Logger.getLogger(ViewContactChangeMOCHandler.class);

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";
    String rowID = null;
    boolean isEntity = false;
    try {
      ContactVO contactObject = new ContactVO();
      rowID = request.getParameter("rowId");
      isEntity = (request.getParameter("type") == null) ? false : true;

      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");

      ContactFacade remote = aa.create();
      remote.setDataSource(dataSource);
      DynaActionForm dynaForm = (DynaActionForm)form;
      String recordType = "";
      if (isEntity) {
        recordType = "Entity";
        EntityVO entityVO = remote.getEntity(Integer.parseInt(rowID));
        contactObject = entityVO;
        dynaForm.set("name", entityVO.getName());
        request.setAttribute("name", entityVO.getName());
      } else {
        recordType = "Individual";
        IndividualVO individualVO = remote.getIndividual(Integer.parseInt(rowID));
        contactObject = individualVO;
        dynaForm.set("name", individualVO.getFirstName() + " " + individualVO.getLastName());
        request
            .setAttribute("name", individualVO.getFirstName() + " " + individualVO.getLastName());
      }

      dynaForm.set("ContactID", Integer.toString(contactObject.getContactID()));
      dynaForm.set("EntityType", Boolean.toString(isEntity));

      request.setAttribute("EntityType", Boolean.toString(isEntity));
      request.setAttribute("closeWindow", request.getAttribute("closeWindow"));
      request.setAttribute("refreshWindow", request.getAttribute("refreshWindow"));

      Iterator mocIterator = contactObject.getMOC().iterator();
      ArrayList phoneNumbers = new ArrayList();
      ArrayList emailAddresses = new ArrayList();
      String primaryEmailAddress = new String();
      while (mocIterator.hasNext()) {
        MethodOfContactVO methodOfContact = (MethodOfContactVO)mocIterator.next();
        if (methodOfContact.getMocType() == 1) // this is for email
        {
          // If the email address is a default email address,
          // add it at the begining of the list.
          if ((methodOfContact.getIsPrimary()).equalsIgnoreCase("YES")) {
            emailAddresses.add(0, methodOfContact);
            primaryEmailAddress = methodOfContact.getContent();
          } else {
            emailAddresses.add(methodOfContact);
          }
        } else {// phone number
          if (methodOfContact.getMocOrder() != null
              && phoneNumbers.size() >= Integer.parseInt(methodOfContact.getMocOrder())) {
            phoneNumbers.add(Integer.parseInt(methodOfContact.getMocOrder()), methodOfContact);
          } else {
            phoneNumbers.add(methodOfContact);
          }
        }
      }

      dynaForm.set("phoneMOCOrder", phoneNumbers);
      dynaForm.set("emailMOCOrder", emailAddresses);
      dynaForm.set("primaryEmailAddress", new String[] { primaryEmailAddress });
      returnStatus = ".view.contact.change_moc";
      request.setAttribute("recordType", recordType);
      request.setAttribute("recordName", request.getParameter("recordName"));
      request.setAttribute("recordId", rowID);
      request.setAttribute("dynamicTitle", request.getParameter("recordName"));
      request.setAttribute("parentId", request.getParameter("parentId"));
      request.setAttribute("parentName", request.getParameter("parentName"));
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }
    return (mapping.findForward(returnStatus));
  } // end of execute method
} // end of ViewContactChangeMOCHandler class
