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

import javax.naming.CommunicationException;
import javax.naming.NamingException;
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
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;

public final class DuplicateIndividualHandler extends Action {
  private static Logger logger = Logger.getLogger(DuplicateIndividualHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";

    ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");

    try {
      Integer rowId = new Integer(request.getParameter("selectId"));

      DynaActionForm dynaForm = (DynaActionForm)form;

      IndividualVO individualVO = null;
      AddressVO primaryAdd = null;

      ContactFacade remote = cfh.create();
      remote.setDataSource(dataSource);
      individualVO = remote.getIndividual(rowId.intValue());

      if (individualVO.getFirstName() != null) {
        dynaForm.set("firstName", individualVO.getFirstName());
        dynaForm.set("lastName", individualVO.getLastName());
        dynaForm.set("middleInitial", individualVO.getMiddleName());
      }

      if (individualVO.getEntityID() != 0) {
        dynaForm.set("entityId", new Integer(individualVO.getEntityID()));
      }

      if (individualVO.getTitle() != null) {
        dynaForm.set("title", individualVO.getTitle());
      }

      if (individualVO.getExternalID() != null) {
        dynaForm.set("id2", individualVO.getExternalID());
      }

      if (individualVO.getSourceName() != null) {
        dynaForm.set("sourceName", individualVO.getSourceName());
      }

      if (individualVO.getSource() > 0) {
        dynaForm.set("sourceId", new Integer(individualVO.getSource()));
      }

      if (individualVO.getContactID() != 0) {
        dynaForm.set("individualId", new Integer(individualVO.getContactID()));
      }

      request.setAttribute("marketingListId", new Integer(individualVO.getList()));
      Collection mocList = individualVO.getMOC();
      Iterator iterator = mocList.iterator();
      int count = 1;

      while (iterator.hasNext()) {
        MethodOfContactVO moc = (MethodOfContactVO)iterator.next();
        if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) {
          // this is for email
          dynaForm.set("email", moc.getContent());
          dynaForm.set("emailId", new Integer(moc.getMocID()).toString());
        } else if (count < 4 && moc.getMocType() != 1) {
          dynaForm.set("mocType" + count, new Integer(moc.getMocType()).toString());
          String mocContent = moc.getContent();
          String mocContentValue = "";
          String mocContentExt = "";
          if (mocContent.indexOf("EXT") != -1) {
            String tempContent = mocContent;
            mocContentValue = tempContent.substring(0, tempContent.indexOf("EXT"));
            mocContentExt = tempContent.substring(tempContent.indexOf("EXT") + 3, tempContent
                .length());
          } else {
            mocContentValue = mocContent;
          }

          dynaForm.set("mocContent" + count, mocContentValue);
          dynaForm.set("mocExt" + count, mocContentExt);
          dynaForm.set("mocId" + count, String.valueOf(moc.getMocID()));
          count++;
        }
      }

      primaryAdd = individualVO.getPrimaryAddress();
      if (primaryAdd != null) {
        if (primaryAdd.getAddressID() != 0) {
          dynaForm.set("addressId", new Integer(primaryAdd.getAddressID()));
        }
        if (primaryAdd.getStreet1() != null) {
          dynaForm.set("street1", primaryAdd.getStreet1());
        }
        if (primaryAdd.getStreet2() != null) {
          dynaForm.set("street2", primaryAdd.getStreet2());
        }
        if (primaryAdd.getCity() != null) {
          dynaForm.set("city", primaryAdd.getCity());
        }
        if (primaryAdd.getStateName() != null) {
          dynaForm.set("state", primaryAdd.getStateName());
        }
        if (primaryAdd.getZip() != null) {
          dynaForm.set("zip", primaryAdd.getZip());
        }
        if (primaryAdd.getCountryName() != null) {
          dynaForm.set("country", primaryAdd.getCountryName());
        }
      } // end of if (primaryAdd != null)
      returnStatus = "success";
    } catch (Exception e) {
      logger.error("[Exception] DuplicateIndividualHandler.Execute Handler ", e);
      returnStatus = "failure";
    }
    return mapping.findForward(returnStatus);
  }

}
