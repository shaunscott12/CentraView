/*
 * $RCSfile: EditAccountHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.mail.Mail;
import com.centraview.mail.MailAccountVO;
import com.centraview.mail.MailHome;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;


/**
 * Handles the request for EditEmailAccount.do which simply
 * displays the details of a given email account, allowing
 * the user to update the fields and save the information.
 */
public class EditAccountHandler extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "EMAIL");

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.preference.mail.view_account";
    String errorForward = ".view.preference.mail.view_account";

    // "mailAccountForm" defined in struts-config-preference.xml
    DynaActionForm accountForm = (DynaActionForm)form;
    
    try {
      // get the account ID from the form bean
      Integer accountID = (Integer)accountForm.get("accountID");
      
      // now, check the account ID on the form...
      if (accountID == null || accountID.intValue() <= 0) {
        // if account ID is not set on the form, then there is
        // no point in continuing forward. Show user the door. :-)
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Account ID"));
        return mapping.findForward(forward);
      }

      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      MailAccountVO accountVO = remote.getMailAccountVO(accountID.intValue());

      accountForm.set("accountName", accountVO.getAccountName());
      accountForm.set("emailAddress", accountVO.getEmailAddress());
      accountForm.set("replyTo", accountVO.getReplyToAddress());
      accountForm.set("serverType", accountVO.getAccountType());
      accountForm.set("mailServer", accountVO.getMailServer());
      accountForm.set("smtpServer", accountVO.getSmtpServer());
      accountForm.set("authenticationRequiredForSMTP", new Boolean(accountVO.isAuthenticationRequiredForSMTP()));
      accountForm.set("username", accountVO.getLogin());
      accountForm.set("password", accountVO.getPassword());
      accountForm.set("port", new Integer(accountVO.getSmtpPort()));
      accountForm.set("leaveOnServer", new Boolean(accountVO.isLeaveMessagesOnServer()));
      accountForm.set("signature", accountVO.getSignature());
    } catch (Exception e) {
      System.out.println("[Exception][EditAccountHandler] Exception thrown in execute(): " + e);
      e.printStackTrace();
      forward = errorForward;
    }
    return mapping.findForward(forward);
  }   // end execute() method

}   // end class definition

