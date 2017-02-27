/*
 * $RCSfile: SaveSettingsHandler.java,v $    $Revision: 1.2 $  $Date: 2005/06/09 14:11:39 $ - $Author: mking_cv $
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
package com.centraview.administration.modulesettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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

import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.UserObject;
import com.centraview.projects.common.ProjectsHelperList;
import com.centraview.settings.Settings;

public class SaveSettingsHandler extends Action
{
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = ".view.administration.field_settings";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  private static final String FORWARD_save = ".view.administration.field_settings";
  private static final String FORWARD_saveandclose = ".view.administration.module_settings";

  private static Logger logger = Logger.getLogger(SaveSettingsHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject("com.centraview.administration.applicationsettings.AppSettingsHome", "AppSettings");

    try {
      String returnStatus = "";
      String button = request.getParameter("buttonpress").toString();
      String setting = request.getParameter("setting").toString();
      String submodule = request.getParameter("submodule").toString();
      request.setAttribute("typeofsubmodule", submodule);

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int IndividualId = userObject.getIndividualID();
      DynaActionForm dynaform = (DynaActionForm) form;

      String[] optionValues = (String[]) dynaform.get("valueList");
      String[] optionIDs = (String[]) dynaform.get("valueIds");
      ArrayList vecOption = new ArrayList();

      //To Make sure we don't go out of bounds.
      int arrayLength = (optionIDs.length < optionValues.length) ? optionIDs.length : optionValues.length;

      for (int i = 0; i < arrayLength; i++) {
        DDNameValue tempDDNameValue =
        new DDNameValue(Integer.parseInt(optionIDs[i]), optionValues[i]);
        vecOption.add(tempDDNameValue);
      }
      AppSettings appRemote = appHome.create();
      appRemote.setDataSource(dataSource);
      appRemote.addMasterDataSettings(setting, (Collection) vecOption);

      GlobalMasterLists.refreshGlobalMasterList(dataSource);
      ProjectsHelperList.refreshProjectsHelperList();

      if (button.equals("save")) {
        returnStatus = "save";
        FORWARD_final = FORWARD_save;
      } else {
        returnStatus = "saveandclose";
        request.setAttribute("typeofmodule", AdministrationConstantKeys.MODULESETTINGS);
        FORWARD_final = FORWARD_saveandclose;
      }
    } catch (Exception e) {
      logger.error("[Exception] SaveSettingsHandler.Execute Handler ", e);
    }
    return mapping.findForward(FORWARD_final);
  }
}
