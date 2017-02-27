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
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;
import com.centraview.settings.Settings;

public class ViewCustomFieldHandler extends Action {
  private static Logger logger = Logger.getLogger(ViewCustomFieldHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = ".view.error";
    String rowId[] = null;

    try {
      HttpSession session = request.getSession();

      if (request.getParameterValues("rowId") != null) {
        rowId = request.getParameterValues("rowId");
      } else {
        rowId[0] = new String(request.getParameter(Constants.PARAMID));
      }

      int recordID = 0;
      if (request.getParameterValues("recordID") != null) {
        recordID = Integer.parseInt(request.getParameter("recordID"));
      }

      CustomFieldVO cfvo = null;

      CustomFieldHome cfh = (CustomFieldHome)CVUtility.getHomeObject(
          "com.centraview.customfield.CustomFieldHome", "CustomField");
      CustomField remote = cfh.create();
      remote.setDataSource(dataSource);

      int userId = 0;
      if (session.getAttribute("userobject") != null) {
        userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      }

      request.setAttribute("RECORDOPERATIONRIGHT", new Integer(CVUtility.getRecordPermission(
          userId, "CustomFields", recordID, dataSource)));

      cfvo = remote.getCustomField(Integer.parseInt(rowId[0]), recordID);
      cfvo.setRecordID(recordID);

      request.setAttribute("CustomFieldVO", cfvo);
      returnStatus = ".view.contacts.view_custom_field";
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      returnStatus = ".view.error";
    }
    return mapping.findForward(returnStatus);
  }

}
