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
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.ContactVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;

/**
 * This handler is used for updating the order of an entities MOC.
 * @author Ryan Grier <ryan@centraview.com>
 */
public final class UpdateContactChangeMOCHandler extends Action {
  private static Logger logger = Logger.getLogger(UpdateContactChangeMOCHandler.class);

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";
    String saveandclose = request.getParameter("saveandclose");
    boolean closeWindow = (saveandclose == null) ? false : true;

    HttpSession session = request.getSession(true);
    UserObject userobject = (UserObject)session.getAttribute("userobject");

    int individualID = 0;
    if (userobject != null) {
      individualID = userobject.getIndividualID();
    }
    DynaActionForm dynaForm = (DynaActionForm)form;
    int contactID = Integer.parseInt((String)dynaForm.get("ContactID"));
    boolean isEntity = ((String)dynaForm.get("EntityType")).equalsIgnoreCase("TRUE");
    try {
      ContactVO contactObject = new ContactVO();
      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");

      ContactFacade remote = aa.create();
      remote.setDataSource(dataSource);

      if (isEntity) {
        EntityVO entityVO = remote.getEntity(contactID);
        contactObject = entityVO;
        dynaForm.set("name", entityVO.getName());
      } else {
        IndividualVO individualVO = remote.getIndividual(contactID);
        contactObject = individualVO;
        dynaForm.set("name", individualVO.getFirstName() + " " + individualVO.getLastName());
      }
      request.setAttribute("rowId", new String[] { Integer.toString(contactID) });
      String[] primaryEmailAddress = (String[])dynaForm.get("primaryEmailAddress");
      String[] primaryPhoneNumbers = (String[])dynaForm.get("primaryPhoneNumbers");
      // Putting the phone numbers in an arraylist because it's a bit easier to
      // deal with.
      ArrayList phoneNumbers = new ArrayList();
      for (int i = 0; i < primaryPhoneNumbers.length; i++) {
        phoneNumbers.add(i, primaryPhoneNumbers[i]);
      }

      Iterator mocIterator = contactObject.getMOC().iterator();
      while (mocIterator.hasNext()) {
        MethodOfContactVO methodOfContact = (MethodOfContactVO)mocIterator.next();
        if (methodOfContact.getMocType() == 1) // this is for email
        {
          if (methodOfContact.getMocID() == Integer.parseInt(primaryEmailAddress[0])) {
            methodOfContact.setIsPrimary("YES");
          } else {
            methodOfContact.setIsPrimary("NO");
          }
          remote.updateMOC(methodOfContact, contactID, individualID);
        } else {
          int index = phoneNumbers.indexOf(Integer.toString(methodOfContact.getMocID()));
          if (index != -1) {
            if (index < 3) {
              methodOfContact.setIsPrimary("YES");
            } else {
              methodOfContact.setIsPrimary("NO");
            }
            methodOfContact.setMocOrder(Integer.toString(index));
            remote.updateMOC(methodOfContact, contactID, individualID);
          }
        }
      }
      if (closeWindow) {
        request.setAttribute("closeWindow", "true");
        request.setAttribute("refreshWindow", "true");
      }
      if (isEntity) {
        returnStatus = "viewEntity";
      } else {
        returnStatus = "viewIndividual";
      }
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }
    // Now we need to redirect to the ViewEntityChangeMOCHandler class.
    ActionForward returnForward = mapping.findForward(returnStatus);
    StringBuffer newPathBuffer = new StringBuffer();
    newPathBuffer.append(returnForward.getPath());
    newPathBuffer.append("?rowId=");
    newPathBuffer.append(Integer.toString(contactID));
    ActionForward newActionForward = new ActionForward();
    newActionForward.setPath(newPathBuffer.toString());
    newActionForward.setRedirect(returnForward.getRedirect());
    newActionForward.setName(returnForward.getName());
    return (newActionForward);
  } // end of execute method
} // end of UpdateEntityChangeMOCHandler class
