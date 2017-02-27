/*
 * $RCSfile: ForgotPasswordHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:06 $ - $Author: mking_cv $
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

package com.centraview.user;

import java.io.IOException;
import java.util.HashMap;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.administration.emailsettings.EmailSettings;
import com.centraview.administration.emailsettings.EmailSettingsHome;
import com.centraview.administration.emailsettings.EmailTemplateForm;
import com.centraview.administration.user.User;
import com.centraview.administration.user.UserHome;
import com.centraview.common.CVUtility;
import com.centraview.login.Login;
import com.centraview.login.LoginHome;
import com.centraview.mail.MailMessageVO;
import com.centraview.settings.Settings;

public class ForgotPasswordHandler extends org.apache.struts.action.Action
{
	private static Logger logger = Logger.getLogger(ForgotPasswordHandler.class);

	//local variables
	private String emailFrom;
	private String userPassword;
	private String firstName;
	private String lastName;
	private String forgotPasswordMesssage;

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	 throws IOException, ServletException, CommunicationException, NamingException
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		String formUserName = "";
		String formEmail = "";

		String action = request.getParameter("action"); 
		if (action != null && action.equals("display")) {
		  request.setAttribute("action", "display");
		  return(mapping.findForward(".view.forgot"));
		}
		
		LoginHome lh = (LoginHome)CVUtility.getHomeObject("com.centraview.login.LoginHome","Login");
		try
		{
			ForgotPasswordForm fpf = (ForgotPasswordForm)form;
			formUserName = (String)fpf.getUsername();
			formEmail = (String)fpf.getEmail();

			HttpSession session = request.getSession(true);
			
			// EJB
			Login remote = (Login)lh.create();
			remote.setDataSource(dataSource);
			HashMap resultHashMap = remote.getForgottenPassword(formUserName, formEmail);
			String rand = "";
			int userid = 0;
			int individualID = 0;

			if ((String)resultHashMap.get("noemail") ==  null) {
			  if (resultHashMap.get("status") != null && ((String) resultHashMap.get("status")).equals("forgottenpassword.success")) {
			    firstName = resultHashMap.get("firstName").toString();
					lastName = resultHashMap.get("lastName").toString();
					userid = ((Integer)resultHashMap.get("userid")).intValue();

					individualID = ((Integer)resultHashMap.get("individualID")).intValue();
					PasswordGenerator pg = new PasswordGenerator();
					rand  = pg.generateIt();
					forgotPasswordMesssage = firstName + " " + lastName + "\n" +
					"Here is the login information you requested for your\n" +
					"CentraView account:\n" + "Username: " + formUserName + "\n" +
					"Password: " + rand + "\n" +
					"You can use the information above to log in to the \n" +
					"application. We suggest you change your password as\n" +
					"soon as you log in, and save it in a safe location";

					UserHome ab = (UserHome) CVUtility.getHomeObject(
					"com.centraview.administration.user.UserHome", "User");
					User remoteUser = (User) ab.create();
					remoteUser.setDataSource(dataSource);
					remoteUser.setNewPassword(individualID, rand);

					EmailSettingsHome emailSettingsHome = (EmailSettingsHome)CVUtility.getHomeObject("com.centraview.administration.emailsettings.EmailSettingsHome","EmailSettings");
					EmailSettings emailSettingsRemote = (EmailSettings)emailSettingsHome.create();
					emailSettingsRemote.setDataSource(dataSource);

					// Its a predefined Template for the replying message for the Forgot Password
					EmailTemplateForm forgotPasswordTemplateForm = emailSettingsRemote.getEmailTemplate(AdministrationConstantKeys.EMAIL_TEMPLATE_FORGOT_PASSWORD);
					String fromAddress = forgotPasswordTemplateForm.getFromAddress();
					String replyTo = forgotPasswordTemplateForm.getReplyTo();
					String emailSubject = forgotPasswordTemplateForm.getSubject();

					/* 
					 * Special case.  Until the app is properly configured, i.e. forgot
					 * password template setup, smtp relay setup, none of this will work.
					 */
					
					MailMessageVO mailMessage = new MailMessageVO();
					mailMessage.setHeaders("Forgotten Password");
					mailMessage.setContentType("text/plain");
					mailMessage.addToAddress(formEmail);
					mailMessage.setFromAddress(fromAddress);
					mailMessage.setReplyTo(replyTo);
					mailMessage.setSubject(emailSubject);
					mailMessage.setBody(forgotPasswordMesssage);
					boolean sendFlag = emailSettingsRemote.simpleMessage(individualID,mailMessage);
					request.setAttribute("action", "succeed");
				}	else {
					request.setAttribute("action", "fail");
				}
			} else {
				request.setAttribute("noemail","1");
				request.setAttribute("action", "fail");
			}
		}
		catch (Exception e) {
		  request.setAttribute("noemail","1");
			request.setAttribute("action", "fail");
		  e.printStackTrace();
		}
		return mapping.findForward(".view.forgot");
	}

}
