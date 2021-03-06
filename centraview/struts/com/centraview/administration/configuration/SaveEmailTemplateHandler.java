/*
 * $RCSfile: SaveEmailTemplateHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.administration.configuration;

import java.io.IOException;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.administration.emailsettings.EmailSettings;
import com.centraview.administration.emailsettings.EmailSettingsHome;
import com.centraview.administration.emailsettings.EmailTemplateForm;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class SaveEmailTemplateHandler extends Action
{
	private static Logger logger = Logger.getLogger(SaveEmailTemplateHandler.class);

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_save = ".forward.administration.view_email_template";
  private static final String FORWARD_error = ".view.administration.view_email_template";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    EmailSettingsHome emailSettingsHome = (EmailSettingsHome)CVUtility.getHomeObject("com.centraview.administration.emailsettings.EmailSettingsHome", "EmailSettings");
    
    try {
			EmailTemplateForm emailTemplateForm = (EmailTemplateForm) form;
			ActionErrors allErrors = new ActionErrors();

			boolean requiredToAddress = emailTemplateForm.getRequiredToAddress();
			boolean requiredFromAddress = emailTemplateForm.getRequiredFromAddress();
			boolean requiredSubject = emailTemplateForm.getRequiredSubject();
			boolean requiredBody = emailTemplateForm.getRequiredBody();

			String toAddress = emailTemplateForm.getToAddress();
			String fromAddress = emailTemplateForm.getFromAddress();
			String replyTo = emailTemplateForm.getReplyTo();
			String subject = emailTemplateForm.getSubject();
			String body = emailTemplateForm.getBody();

			if (replyTo != null) {
				if (replyTo.equals("")) {
					emailTemplateForm.setReplyTo(null);
					replyTo = null;
				} else {
          if (! CVUtility.isEmailAddressValid(replyTo)) {
            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.email.invalidAddress", "ReplyTo"));
          }
				}
			}

			if (requiredToAddress) {
        if (toAddress == null || toAddress.length() <= 0) {
          allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "To"));
        }
        if (toAddress != null) {
          if (! CVUtility.isEmailAddressValid(toAddress)) {
            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.email.invalidAddress", "To"));
          }
        }
      }

			if (requiredFromAddress) {
        if (fromAddress == null || fromAddress.length() <= 0) {
          allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "From"));
        }
        if (fromAddress != null) {
          if (! CVUtility.isEmailAddressValid(fromAddress)){
            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.email.invalidAddress", "From"));
          }
        }
      }
      
      if (requiredSubject) {
        if (subject == null || subject.length() <= 0) {
          allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Subject"));
        }
      }
      
      if (requiredBody) {
        if (body == null || body.length() <= 0) {
          allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Body"));
        }
      }
      
      if (! allErrors.isEmpty()) {
        saveErrors(request, allErrors);
        return(mapping.findForward(FORWARD_error));
      }
      
      EmailSettings emailSettingsRemote = (EmailSettings) emailSettingsHome.create();
      emailSettingsRemote.setDataSource(dataSource);
      emailSettingsRemote.updateEmailTemplate(emailTemplateForm);
      
      FORWARD_final = FORWARD_save;
    } catch (Exception e) {
      logger.error("[Exception] SaveEmailTemplateHandler.Execute Handler ", e);
    }
    return (mapping.findForward(FORWARD_final));
  }
}
