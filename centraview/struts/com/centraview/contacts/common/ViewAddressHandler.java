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
import java.util.Vector;

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

import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.AddressVO;
import com.centraview.settings.Settings;

public final class ViewAddressHandler extends Action {
  private static Logger logger = Logger.getLogger(ViewAddressHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = ".view.error";
    String rowId[] = null;

    AccountHelperHome accountHome = (AccountHelperHome)CVUtility.getHomeObject(
        "com.centraview.account.helper.AccountHelperHome", "AccountHelper");
    ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");

    try {
      if (request.getParameterValues("rowId") != null) {
        rowId = request.getParameterValues("rowId");
      } else {
        rowId[0] = new String(request.getParameter(Constants.PARAMID));
      }

      DynaActionForm dynaForm = (DynaActionForm)form;

      AddressVO avo = null;

      ContactFacade remote = cfh.create();
      remote.setDataSource(dataSource);

      avo = remote.getAddress(Integer.parseInt(rowId[0]));

      if (avo != null) {
        dynaForm.set("addressid", rowId[0]);

        if (avo.getStreet1() != null) {
          dynaForm.set("street1", avo.getStreet1());
        }

        if (avo.getStreet2() != null) {
          dynaForm.set("street2", avo.getStreet2());
        }

        if (avo.getCity() != null) {
          dynaForm.set("city", avo.getCity());
        }

        if (avo.getStateName() != null) {
          dynaForm.set("state", avo.getStateName());
        }

        if (avo.getZip() != null) {
          dynaForm.set("zipcode", avo.getZip());
        }

        if (avo.getCountryName() != null) {
          dynaForm.set("country", avo.getCountryName());
        }

        if (avo.getIsPrimary() != null) {
          dynaForm.set("isPrimary", avo.getIsPrimary());
        }

        dynaForm.set("jurisdictionID", avo.getJurisdictionID() + "");
      }

      AccountHelper accountRemote = accountHome.create();
      accountRemote.setDataSource(dataSource);

      Vector taxJurisdiction = accountRemote.getTaxJurisdiction();

      dynaForm.set("jurisdictionVec", taxJurisdiction);
      dynaForm.set("operation", "edit");

      returnStatus = ".view.contacts.view_related_address";
    } catch (Exception e) {
      logger.error("[Exception] ViewAddressHandler.Execute Handler ", e);
      returnStatus = ".view.error";
    }
    return mapping.findForward(returnStatus);
  }

}
