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
import com.centraview.common.Constants;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;

/*
 * This handler is used for Duplicate Entity
 */
public final class DuplicateEntityHandler extends Action {

  private static Logger logger = Logger.getLogger(DuplicateEntityHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";

    ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");

    try {
      EntityVO entityVO = new EntityVO();

      String rowId[] = null;

      if (request.getParameterValues("selectId") != null) {
        rowId = request.getParameterValues("selectId");
      } else {
        rowId[0] = new String(request.getParameter(Constants.PARAMID));
      }

      ContactFacade remote = aa.create();
      remote.setDataSource(dataSource);

      entityVO = remote.getEntity(Integer.parseInt(rowId[0]));
      DynaActionForm dynaForm = (DynaActionForm)form;

      dynaForm.set("entityName", entityVO.getName());

      Collection mocList = entityVO.getMOC();
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
          dynaForm.set("mocId" + count, new Integer(moc.getMocID()).toString());
          count++;
        }
      }

      AddressVO primaryAdd = entityVO.getPrimaryAddress();
      IndividualVO individualVO = entityVO.getIndividualVO();

      if (individualVO.getFirstName() != null) {
        dynaForm.set("pcFirstName", individualVO.getFirstName());
      }

      if (individualVO.getMiddleName() != null) {
        dynaForm.set("pcMiddleInitial", individualVO.getMiddleName());
      }

      if (individualVO.getLastName() != null) {
        dynaForm.set("pcLastName", individualVO.getLastName());
      }

      if (individualVO.getTitle() != null) {
        dynaForm.set("pcTitle", individualVO.getTitle());
      }

      if (primaryAdd != null) {
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

        if (primaryAdd.getWebsite() != null) {
          dynaForm.set("website", primaryAdd.getWebsite());
        }

        dynaForm.set("addressId", new Integer(primaryAdd.getAddressID()));
      }

      if (entityVO.getSourceName() != null) {
        dynaForm.set("sourceName", entityVO.getSourceName());
      }

      if (entityVO.getSource() > 0) {
        dynaForm.set("sourceId", new Integer(entityVO.getSource()));
      }

      request.setAttribute("marketingListId", new Integer(entityVO.getList()));

      dynaForm.set("pcIndividualId", new Integer(individualVO.getContactID()));
      dynaForm.set("entityId", new Integer(entityVO.getContactID()));
      dynaForm.set("accountManagerId", new Integer(entityVO.getAccManager()));
      dynaForm.set("accountManagerName", entityVO.getAcctMgrName());
      dynaForm.set("accountTeamId", new Integer(entityVO.getAccTeam()));
      dynaForm.set("accountTeamName", entityVO.getAcctTeamName());
      request.setAttribute(Constants.ENTITYVO, entityVO);
      returnStatus = "success";
    } catch (Exception e) {
      logger.error("[Exception] DuplicateEntityHandler.Execute Handler ", e);
    }
    return (mapping.findForward(returnStatus));
  }

}
