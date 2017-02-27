/*
 * $RCSfile: SendMail.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
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

/**
 * Sends the Email
 *
 * @author   CentraView, LLC
 */
public class SendMail extends Action
{

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = "closeWindow";
    try
    {
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualId = userobjectd.getIndividualID();
      ListPreference listpreference = userobjectd.getListPreference("Email");
      MailMessage mailMessage = new MailMessage();
      String composeType = (String) request.getParameter("composeType");

      if (composeType != null && composeType.equals("Proposal")){
		 mailMessage.setContentType("HTML");
	  }
      DynaActionForm dynaForm = (DynaActionForm)form;

      String accountid = (String)dynaForm.get("AccountID");
      if (request.getParameter("id") != null)
      {
        mailMessage.setMessageID(Integer.parseInt(request.getParameter("id")));
      } //end of if statement (request.getParameter("id") != null)

      mailMessage.setAccountID(Integer.parseInt(accountid));

      FolderList fl = (FolderList)session.getAttribute("folderlist");
      Set listkey = fl.keySet();
      Iterator it = listkey.iterator();

      String smtpserver = "";
      while (it.hasNext())
      {
        AccountDetail ad1 = (AccountDetail)fl.get(it.next());
        if (Integer.parseInt(accountid) == ad1.getAccountid())
        {
          smtpserver = ad1.getSmtpserver();
          mailMessage.setFolder(ad1.getFolderIDFromName("Drafts", "SYSTEM"));
        } //end of if statement (Integer.parseInt(accountid) == ad1.getAccountid())
      } //end of while loop (it.hasNext())

      mailMessage.setSmtpserver(smtpserver);

      String mailFrom = (String)dynaForm.get("composeFrom");
      mailMessage.setMailFrom(mailFrom);

      //to
      ArrayList toarray = new ArrayList();
      String to = (String)dynaForm.get("composeTo");
      StringTokenizer st = new StringTokenizer(to, ",");
      while (st.hasMoreTokens())
      {
        toarray.add(new MailAddress(st.nextToken()));
      } //end of while loop (st.hasMoreTokens())
      mailMessage.setTo(toarray);

      //cc
      ArrayList ccarray = new ArrayList();
      String cc = (String)dynaForm.get("composeCc");
      StringTokenizer stcc = new StringTokenizer(cc, ",");
      while (stcc.hasMoreTokens())
      {
        ccarray.add(new MailAddress(stcc.nextToken()));
      } //end of while loop (stcc.hasMoreTokens())
      mailMessage.setCc(ccarray);

      //bcc
      ArrayList bccarray = new ArrayList();
      String bcc = (String)dynaForm.get("composeBcc");

      StringTokenizer stbcc = new StringTokenizer(bcc, ",");
      while (stbcc.hasMoreTokens())
      {
        bccarray.add(new MailAddress(stbcc.nextToken()));
      } //end of while loop (stbcc.hasMoreTokens())
      mailMessage.setBcc(bccarray);

      //message subject
      String subject = (String)dynaForm.get("composeSubject");
      mailMessage.setSubject(subject);

      //message body
      String body = (String)dynaForm.get("composeMessage");
      mailMessage.setBody(body);

      //message Date
      long l = (new java.util.Date()).getTime();
      //mailMessage.setMessageDate( new java.sql.Timestamp( l  ) ) ;

      //Attchment
      HashMap attchmentids = (HashMap)session.getAttribute("AttachfileList");
      mailMessage.setAttachFileIDs(attchmentids);

      session.removeAttribute("AttachfileList");

      EmailFacadeHome aa = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)aa.create();
      remote.setDataSource(dataSource);
      boolean result = remote.sendMailMessage(individualId, mailMessage);

      if (result)
      {
        forward = "closeWindow";
      }else{
        //forward = "error";
      }
    }catch(Exception e){
      System.out.println("[Exception][SendMail.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return(mapping.findForward(forward));
  } //end of execute method
  
} //end of SendMail class

