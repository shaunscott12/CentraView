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
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.settings.Settings;

public final class ViewMOCHandler extends Action {
  private static Logger logger = Logger.getLogger(ViewMOCHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = ".view.error";

    try {
      MethodOfContactVO mcontactVO = new MethodOfContactVO();

      String rowId[] = null;

      if (request.getParameterValues("rowId") != null) {
        rowId = request.getParameterValues("rowId");
      } else {
        rowId[0] = new String(request.getParameter(Constants.PARAMID));
      }

      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = aa.create();
      remote.setDataSource(dataSource);

      mcontactVO = remote.getMOC(Integer.parseInt(rowId[0]));

      DynaActionForm dynaForm = (DynaActionForm)form;
      dynaForm.set("select", new Integer(mcontactVO.getMocType()).toString());
      dynaForm.set("text3", mcontactVO.getContent());
      dynaForm.set("mocid", new Integer(mcontactVO.getMocID()).toString());
      dynaForm.set("text4", mcontactVO.getNote());
      dynaForm.set("syncas", mcontactVO.getSyncAs());

      returnStatus = ".view.contacts.new_contact_method";
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }
    return mapping.findForward(returnStatus);
  }
}
