/*
 * $RCSfile: SendSimpleMail.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:16 $ - $Author: mking_cv $
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

package com.centraview.common;

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

import com.centraview.email.AccountDetail;
import com.centraview.email.FolderList;
import com.centraview.email.MailAddress;
import com.centraview.email.MailMessage;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;

/**
 * This class intends to send email for Marketing module and Group module.
 *
 * @author   Chris Wang
 */
public class SendSimpleMail extends Action
{

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

	String eventid = "";
	try
	{
	  HttpSession session            = request.getSession(true);
	  UserObject userobjectd         = (UserObject) session.getAttribute("userobject");
	  int individualID = userobjectd.getIndividualID();
	  
	  DynaActionForm dynaForm        = (DynaActionForm) form;
	  eventid = (String)dynaForm.get("eventid");
	  String strAction = request.getParameter("action");		  
	  
	  if(strAction !=null && !strAction.equalsIgnoreCase("cancel"))
	  {
		MailMessage mailmessage  = new MailMessage();
		HashMap hm               = new HashMap();
		  String mailTo = (String)dynaForm.get("composeTo");
		  String mailFrom = (String)dynaForm.get("composeFrom");
		  String subject = (String)dynaForm.get("composeSubject");
		  String body = (String)dynaForm.get("composeMessage");
		  String replyTo = (String)dynaForm.get("composeReplyTo");

		  mailmessage.setMailFrom(mailFrom);
		  mailmessage.setSubject(subject);
		  mailmessage.setBody(body);
		  mailmessage.setReplyTo(replyTo);
	
		  String accountid               = (String) dynaForm.get("accountID");
		  mailmessage.setAccountID(Integer.parseInt(accountid));
	
		  FolderList fl                  = (FolderList) session.getAttribute("folderlist");
		  Set listkey                    = fl.keySet();
		  Iterator it                    = listkey.iterator();
	
		  String smtpserver              = "";
		  while (it.hasNext())
		  {
			AccountDetail ad1  = (AccountDetail) fl.get(it.next());
			if (Integer.parseInt(accountid) == ad1.getAccountid())
			{
			  smtpserver = ad1.getSmtpserver();
			  mailmessage.setFolder(ad1.getFolderIDFromName("Sent", "SYSTEM"));
			} //end of if statement (Integer.parseInt(accountid) == ad1.getAccountid())
		  } //end of while loop (it.hasNext())
		  mailmessage.setSmtpserver(smtpserver);
	
		  //message Date
		  long l                         = (new java.util.Date()).getTime();
		  mailmessage.setMessageDate( new java.sql.Timestamp( l  ) ) ;
	
		  //Attchment
		  HashMap attchmentids           = (HashMap) session.getAttribute("AttachfileList");
		  mailmessage.setAttachFileIDs(attchmentids);
	
		  session.removeAttribute("AttachfileList");
		  EmailFacadeHome aa             = (EmailFacadeHome) CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
		  EmailFacade remote             = (EmailFacade) aa.create();
		  remote.setDataSource(dataSource);
		  
		  //to
		  StringTokenizer st             = new StringTokenizer(mailTo, ",");
		  while (st.hasMoreTokens())
		  {
		  	String addr = st.nextToken();
			ArrayList al = new ArrayList();
			if(addr!=null)
			{
				al.add(new MailAddress(addr));
				mailmessage.setTo(al);
				remote.sendMailMessage(individualID, mailmessage);
			}
		  } //end of while loop (st.hasMoreTokens())
	  	}//end of if(strAction !=null..)
	} //end of try block
	catch (Exception e)
	{
	  System.out.println("[Exception][SendSimpleMail.execute] Exception Thrown: "+e);
	  e.printStackTrace();
	} //end of catch block (Exception)

	ActionForward af = new ActionForward();
	af.setRedirect(true);
	af.setPath("/ViewEvent.do?TYPEOFOPERATION=EDIT&FromAttendee=NO&eventid="+ eventid);
	return (af);
  } //end of execute method
} //end of SendSimpleMail class
