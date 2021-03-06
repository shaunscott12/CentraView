/*
 * $RCSfile: SendSuggestionHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

package com.centraview.hr.suggestion;

import java.io.IOException;
import java.util.ArrayList;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.administration.emailsettings.EmailSettings;
import com.centraview.administration.emailsettings.EmailSettingsHome;
import com.centraview.administration.emailsettings.EmailTemplateForm;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.hr.common.HrConstantKeys;
import com.centraview.mail.MailMessageVO;
import com.centraview.settings.Settings;

public class SendSuggestionHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(SendSuggestionHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
  		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		EmailSettingsHome emailSettingsHome = (EmailSettingsHome)CVUtility.getHomeObject("com.centraview.administration.emailsettings.EmailSettingsHome","EmailSettings");
		try
		{
			HttpSession session = request.getSession(true);
			UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );
			int individualID = userobjectd.getIndividualID();

			DynaActionForm dynaForm = (DynaActionForm)form;
			String body = (String) dynaForm.get( "suggestion" );

			EmailSettings emailSettingsRemote = (EmailSettings)emailSettingsHome.create();
			emailSettingsRemote.setDataSource(dataSource);

			// Its a predefined Template for the replying message for the newly created Suggestion
			EmailTemplateForm suggestionTemplateForm = emailSettingsRemote.getEmailTemplate(AdministrationConstantKeys.EMAIL_TEMPLATE_SUGGESTIONBOX);
			String toAddress = suggestionTemplateForm.getToAddress();
			String fromAddress = suggestionTemplateForm.getFromAddress();
			String replyTo = suggestionTemplateForm.getReplyTo();
			String subject = suggestionTemplateForm.getSubject();

			MailMessageVO mailMessageVO = new MailMessageVO();
			ArrayList toList = new ArrayList();
			toList.add(toAddress);
			mailMessageVO.setToList(toList);
			mailMessageVO.setFromAddress(fromAddress);
			mailMessageVO.setReplyTo(replyTo);
			mailMessageVO.setHeaders("Suggestion");
			mailMessageVO.setSubject(subject);
			mailMessageVO.setBody(body);
			mailMessageVO.setContentType(MailMessageVO.PLAIN_TEXT_TYPE);

			boolean sendFlag = emailSettingsRemote.simpleMessage(individualID,mailMessageVO);
			request.setAttribute(HrConstantKeys.TYPEOFSUBMODULE,HrConstantKeys.SUGGESTION);

		}//end of try block
		catch(Exception e)
		{
			logger.error("[Exception] SendSuggestionHandler.Execute Handler ", e);
			ActionErrors allErrors = new ActionErrors();
			allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "Error occured while sending the suggestion. Please check the suggestion template."));
			saveErrors(request, allErrors);			
		}//end of catch block
		return mapping.findForward(".view.hr.suggestionbox");
	}// end of exceute method
}// end of class

