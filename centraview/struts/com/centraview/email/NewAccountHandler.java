/*
 * $RCSfile: NewAccountHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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

package com.centraview.email;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;

public class NewAccountHandler extends org.apache.struts.action.Action
{


  /**
  This method is overridden from Action Class
  */

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {

      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualId = userobjectd.getIndividualID();
      ListPreference listpreference = userobjectd.getListPreference("Email");

      //account object created
      MailAccount mailaccount = new MailAccount();
      DynaActionForm dynaForm = (DynaActionForm)form;

      String address = (String)dynaForm.get("address");
      mailaccount.setAddress(address);

      String leaveonserver = (String)dynaForm.get("leaveonserver");
      mailaccount.setLeaveonserver(leaveonserver);

      String login = (String)dynaForm.get("username");
      mailaccount.setLogin(login);

      String mailserver = (String)dynaForm.get("mailserver");
      mailaccount.setMailserver(mailserver);

      String name = (String)dynaForm.get("name");
      mailaccount.setName(name);

      mailaccount.setOwner(individualId);

      String password = (String)dynaForm.get("password");
      mailaccount.setPassword(password);

      String replyto = (String)dynaForm.get("replyto");
      mailaccount.setReplyto(replyto);

      String servertype = (String)dynaForm.get("servertype");
      mailaccount.setServertype(servertype);

      String signature = (String)dynaForm.get("signature");
      mailaccount.setSignature(signature);

      String smtpserver = (String)dynaForm.get("smtpserver");
      mailaccount.setSmtpserver(smtpserver);

      EmailFacadeHome facade = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)facade.create();
      remote.setDataSource(dataSource);

      int newAccountID = remote.createNewEmailAccount(individualId, mailaccount);

      if (newAccountID != 0)
      {
        return(mapping.findForward("success"));
      }

    }
    catch (Exception e)
    {
      //here delete the message object from server if
      //attachment is not saved properly
      System.out.println("[Exception][NewAccountHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward("displaynewemailaccount"));
  }
}
