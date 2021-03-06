/*
 * $RCSfile: DisplayEmailTemplateHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.administration.emailsettings.EmailSettings;
import com.centraview.administration.emailsettings.EmailSettingsHome;
import com.centraview.administration.emailsettings.EmailTemplateForm;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class DisplayEmailTemplateHandler extends Action
{

  private static Logger logger = Logger.getLogger(DisplayEmailTemplateHandler.class);

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_page = ".view.administration.view_email_template";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    EmailSettingsHome emailSettingHome = (EmailSettingsHome)CVUtility.getHomeObject("com.centraview.administration.emailsettings.EmailSettingsHome","EmailSettings");
    try {
      HttpSession session = request.getSession();

      EmailSettings emailSettingRemote = (EmailSettings)emailSettingHome.create();
      emailSettingRemote.setDataSource(dataSource);

      String tempTemplateID = request.getParameter("templateID");
      if (tempTemplateID != null && !tempTemplateID.equals("")) {
        int templateID = Integer.parseInt(tempTemplateID);
        EmailTemplateForm emailTemplateForm = emailSettingRemote.getEmailTemplate(templateID);
        request.setAttribute( "emailTemplateForm" , emailTemplateForm);
      }

      FORWARD_final = FORWARD_page;
    } catch (Exception e) {
      logger.error("[Exception] DisplayEmailTemplateHandler.Execute Handler ", e);
    }
    return (mapping.findForward(FORWARD_final));
  }
}
