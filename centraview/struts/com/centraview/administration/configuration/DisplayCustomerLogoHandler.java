/*
 * $RCSfile: DisplayCustomerLogoHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;


public class DisplayCustomerLogoHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(DisplayCustomerLogoHandler.class);

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_customerlogo = ".view.administration.customer_logo";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    try {
      CustomerLogoForm custform = (CustomerLogoForm)form;
      
      AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject("com.centraview.administration.applicationsettings.AppSettingsHome", "AppSettings");
      AppSettings appRemote = (AppSettings)appHome.create();
      appRemote.setDataSource(dataSource);
      
      String cust = appRemote.getApplicationSettings("CUSTOMERLOGO");
      
      custform.setCurrentfile(cust);
      request.setAttribute("Cust", cust);
      FORWARD_final = FORWARD_customerlogo;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }
}
