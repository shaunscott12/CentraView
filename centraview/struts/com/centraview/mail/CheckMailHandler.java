/*
 * $RCSfile: CheckMailHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/28 17:23:01 $ - $Author: mcallist $
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

package com.centraview.mail;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class CheckMailHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(CheckMailHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws RuntimeException, Exception
  {
    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");

      int individualID = userObject.getIndividualID(); // logged in user
      String forward = "emailList";
      DynaActionForm emailListForm = (DynaActionForm)form;
      Integer folderID = (Integer)emailListForm.get("folderID");
      if (CVUtility.empty(folderID)) {
        try {
          folderID = new Integer(request.getParameter("folderID"));
        } catch (NumberFormatException nfe) {}
      }

      // First see if we are currently checking mail.
      BackgroundMailCheck mailDaemon = (BackgroundMailCheck)session.getAttribute("mailDaemon");
      if (mailDaemon == null || !mailDaemon.isAlive()) {
        String dataSource = Settings.getInstance().getSiteInfo(
            CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        if (folderID != null && folderID.intValue() != 0) {
          MailFolderVO folderVO = MailUtil.getFolderVO(folderID.intValue(), individualID,
              dataSource);
          String daemonName = folderVO.getFolderName() + "MailCheck";
          mailDaemon = (BackgroundMailCheck)session.getAttribute(daemonName);
          if (mailDaemon == null || !mailDaemon.isAlive()) {
            mailDaemon = new BackgroundMailCheck("mailDaemon" + individualID, individualID,
                dataSource, folderID.intValue());
            session.setAttribute(daemonName, mailDaemon);
            mailDaemon.start();
          }
        } else {
          mailDaemon = new BackgroundMailCheck("mailDaemon" + individualID, individualID,
              dataSource, 0);
          session.setAttribute("mailDaemon", mailDaemon);
          mailDaemon.start();
        }
      }
      StringBuffer returnPath = new StringBuffer();
      returnPath.append(mapping.findForward(forward).getPath());
      if (folderID != null) {
        returnPath.append("?folderID=" + folderID);
      }
      // give the mail check at a few hundred milliseconds in case it doesn't
      // take to long.
      try {
        // 350 ms is probably a good threshhold.
        Thread.sleep(350);
      } catch (InterruptedException e) {}
      return (new ActionForward(returnPath.toString(), true));
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }
    return null;
  }
}
