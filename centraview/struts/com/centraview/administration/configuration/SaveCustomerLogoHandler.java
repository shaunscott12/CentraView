/*
 * $RCSfile: SaveCustomerLogoHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.settings.Settings;
import com.centraview.settings.SiteInfo;

public class SaveCustomerLogoHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(SaveCustomerLogoHandler.class);
  public static final String GLOBAL_FORWARD_failure = ".view.administration.customer_logo";
  private static final String FORWARD_save = ".forward.administration.customer_logo";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    SiteInfo siteInfo = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext()));
    String dataSource = siteInfo.getDataSource();
    String returnStatus = "";

    try {
      String typeOfSave = "save";
      if (request.getParameter("buttonpress") != null) {
        typeOfSave = request.getParameter("buttonpress");
        String fileName = "";
        File fsRoot = CVUtility.getCentraViewFileSystemRoot(dataSource);
        if (typeOfSave != null && typeOfSave.equals("save")) {
          FormFile fileUpload = ((CustomerLogoForm)form).getFile();
          File logoFile = new File(fsRoot.getAbsolutePath() + Constants.CUSTOMERLOGO_PATH + fileUpload.getFileName());
          FileOutputStream fos = new FileOutputStream(logoFile);
          InputStream fis = fileUpload.getInputStream();
          try {
            int i = 0;
            while ((i = fis.read()) != -1) {
              fos.write(i);
            }
            siteInfo.setCustomerLogo(logoFile);
            siteInfo.setCustomerLogoDirty(true);
          } catch (IOException e) {
            logger.error("[execute] Exception thrown.", e);
          } finally {
            fis.close();
            fos.close();
          }
          fileName = fileUpload.getFileName();
        } else if (typeOfSave != null && typeOfSave.equals("delete")) {
          fileName = "logo_customer.gif";
          File logoFile = new File(fsRoot.getAbsolutePath() + Constants.CUSTOMERLOGO_PATH + fileName);
          siteInfo.setCustomerLogo(logoFile);
          siteInfo.setCustomerLogoDirty(true);
        }
        AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject("com.centraview.administration.applicationsettings.AppSettingsHome", "AppSettings");
        AppSettings appRemote = (AppSettings)appHome.create();
        appRemote.updateApplicationSettings("CUSTOMERLOGO", fileName);
      }
      FORWARD_final = FORWARD_save;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
      ActionErrors allErrors = new ActionErrors();
      allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "The following error has occured: " + e.getMessage()));
      saveErrors(request, allErrors);
    }
    return (mapping.findForward(FORWARD_final));
  }
}
