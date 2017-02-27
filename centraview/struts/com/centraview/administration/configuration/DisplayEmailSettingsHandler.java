/*
 * $RCSfile: DisplayEmailSettingsHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.emailsettings.EmailSettings;
import com.centraview.administration.emailsettings.EmailSettingsHome;
import com.centraview.administration.emailsettings.EmailSettingsVO;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class DisplayEmailSettingsHandler extends Action
{
  private static Logger logger = Logger.getLogger(DisplayEmailSettingsHandler.class);

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_page = ".view.administration.email_settings";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;


  public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    EmailSettingsHome emailSettingHome = (EmailSettingsHome)CVUtility.getHomeObject("com.centraview.administration.emailsettings.EmailSettingsHome","EmailSettings");
    
    try {
      DynaActionForm dynaForm = (DynaActionForm)form;

      EmailSettings emailSettingRemote = (EmailSettings)emailSettingHome.create();
      emailSettingRemote.setDataSource(dataSource);

      EmailSettingsVO emailSettingVO = emailSettingRemote.getEmailSettings();

      dynaForm = setForm(dynaForm,emailSettingVO);
      
      request.setAttribute("emailSettingsForm", dynaForm);

      FORWARD_final = FORWARD_page;
    } catch(Exception e) {
      logger.error("[Exception] DisplayEmailSettingsHandler.Execute Handler ", e);
    }
    return (mapping.findForward(FORWARD_final));
  }   // end exceute() method

  private DynaActionForm setForm(DynaActionForm form, EmailSettingsVO emailSettingVO)
  {
    form.set("smtpserver", emailSettingVO.getSmtpServer());
    form.set("username", emailSettingVO.getUsername());
    form.set("password", emailSettingVO.getPassword());
    form.set("authentication", new Boolean(emailSettingVO.getAuthentication()));
    int port = emailSettingVO.getSmtpPort();
    
    if (port == 0 || port == -1) {
      port = 25;
    }
    
    form.set("port", new Integer(port));
    form.set("emailTemplateList", emailSettingVO.getEmailTemplateList());
    return form;
  }   // end setForm() method

}

