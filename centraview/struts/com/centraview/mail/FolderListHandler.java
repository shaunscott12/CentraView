/*
 * $RCSfile: FolderListHandler.java,v $    $Revision: 1.3 $  $Date: 2005/07/25 13:40:59 $ - $Author: mcallist $
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

import java.util.ArrayList;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class FolderListHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(FolderListHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
      throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession();
    UserObject userObject = (UserObject) session.getAttribute("userobject");
    int individualID = userObject.getIndividualID(); // logged in user
    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.email.folder_list";
    try {
      MailHome home = (MailHome) CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = home.create();
      remote.setDataSource(dataSource);
      TreeMap folderList = new TreeMap();
      ArrayList accountList = remote.getUserAccountList(individualID);
      // also adding delegated accounts
      accountList.addAll(remote.getDelegatedAccountList(individualID));

      for (int i = 0; i < accountList.size(); i++) {
        int accountID = ((Number) accountList.get(i)).intValue();

        MailAccountVO mailAccountVO = remote.getMailAccountVO(accountID);

        if (mailAccountVO != null) {
          String accountName = mailAccountVO.getAccountName();
          ArrayList accountFolderList = remote.getFolderIDs(accountID);
          ArrayList folderVOCollection = new ArrayList();
          for (int j = 0; j < accountFolderList.size(); j++) {
            int folderID = ((Number) accountFolderList.get(j)).intValue();
            MailFolderVO folderVO = remote.getEmailFolder(folderID);
            folderVOCollection.add(folderVO);
          }
          folderList.put(accountID + "*" + accountName, folderVOCollection);
        }
      }
      request.setAttribute("folderList", folderList);
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }
    return (mapping.findForward(forward));
  }

}
