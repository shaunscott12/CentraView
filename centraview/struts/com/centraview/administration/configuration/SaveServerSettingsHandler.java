/*
 * $RCSfile: SaveServerSettingsHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.serversettings.ServerSettings;
import com.centraview.administration.serversettings.ServerSettingsHome;
import com.centraview.administration.serversettings.ServerSettingsVOX;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class SaveServerSettingsHandler extends Action
{
  private static Logger logger = Logger.getLogger(SaveServerSettingsHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_save = ".forward.administration.server_settings";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    ServerSettingsHome sh = (ServerSettingsHome)CVUtility.getHomeObject("com.centraview.administration.serversettings.ServerSettingsHome", "ServerSettings");
    
    try {
      DynaActionForm dynaForm = (DynaActionForm)form;
      HttpSession session = request.getSession();
      ServerSettings remote = (ServerSettings)sh.create();
      remote.setDataSource(dataSource);
      
      ServerSettingsVOX svo = new ServerSettingsVOX(dynaForm);

      remote.updateServerSettings(svo);
      FORWARD_final = FORWARD_save;
    } catch (Exception e) {
      logger.error("[Exception] SaveServerSettingsHandler.Execute Handler ", e);
    }
    return (mapping.findForward(FORWARD_final));
  }
}
