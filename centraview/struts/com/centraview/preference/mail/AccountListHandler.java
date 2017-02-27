/*
 * $RCSfile: AccountListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:25 $ - $Author: mking_cv $
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

package com.centraview.preference.mail;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.mail.Mail;
import com.centraview.mail.MailAccountVO;
import com.centraview.mail.MailHome;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

/**
 * Handles the request for /preference/mail/AccountList.do which
 * displays a list of email accounts configured for the logged-in
 * user.
 */
public class AccountListHandler extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "EMAIL");

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.preference.mail.account_list";

    // "mailAccountListform" defined in struts-config-preference.xml
    DynaActionForm accountForm = (DynaActionForm)form;
    
    try {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      ArrayList accountList = new ArrayList();

      ArrayList accountIDList = remote.getUserAccountList(individualID);
      Iterator iter = accountIDList.iterator();
      
      while (iter.hasNext()) {
        Number accountID = (Number)iter.next();
        
        if (accountID != null && accountID.intValue() > 0) {
          MailAccountVO accountVO = remote.getMailAccountVO(accountID.intValue());
          accountList.add(accountVO);
        }
      }
      
      accountForm.set("accountList", accountList);
    } catch (Exception e) {
      System.out.println("[Exception][AccountListHandler] Exception thrown in execute(): " + e);
      e.printStackTrace();
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

