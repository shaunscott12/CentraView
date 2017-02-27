/*
 * $RCSfile: SaveLicenseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:39 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the License"); you may not use this file except in
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
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.license.License;
import com.centraview.license.LicenseHome;
import com.centraview.license.LicenseUtil;
import com.centraview.license.LicenseVO;
import com.centraview.settings.Settings;

public class SaveLicenseHandler extends Action
{
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_save = "savelicense";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  
  private static Logger logger = Logger.getLogger(SaveLicenseHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      DynaActionForm dynaForm = (DynaActionForm) form;
      HttpSession session = request.getSession();
      String typeOfSave = "saveclose";
      if (request.getParameter("buttonpress") != null)
      {
        typeOfSave = request.getParameter("buttonpress");
      }

      LicenseHome lh = (LicenseHome)CVUtility.getHomeObject("com.centraview.license.LicenseHome", "License");
      License remote = (License) lh.create();
      remote.setDataSource(dataSource);

      LicenseVO licenseVO = remote.getPrimaryLicense();

      if (typeOfSave.equals("save"))
      {
        if (dynaForm.get("licenseKey") != null && !((String)dynaForm.get("licenseKey")).equalsIgnoreCase(""))
        {
          licenseVO.setLicenseKey((String) dynaForm.get("licenseKey"));
          remote.updateLicense(licenseVO);
          LicenseUtil.fetchLicenseFile(dataSource);
        } //end of if statement (dynaForm.get("licenseKey") != null ...
        FORWARD_final = FORWARD_save;
      } //end of if statement (typeOfSave.equals("save"))
      else if (typeOfSave.equals("verify"))
      {
        LicenseUtil.fetchLicenseFile(dataSource);
        FORWARD_final = FORWARD_save;
      } //end of else if (typeOfSave.equals("verify"))
      
      request.setAttribute(AdministrationConstantKeys.TYPEOFMODULE,
        AdministrationConstantKeys.LICENSE);
                
      request.setAttribute("typeofmodule", AdministrationConstantKeys.CONFIGURATION);
      request.setAttribute("typeofsubmodule", AdministrationConstantKeys.LICENSE);

      request.setAttribute("dyna", dynaForm);
      request.setAttribute(AdministrationConstantKeys.TYPEOFMODULE,
        AdministrationConstantKeys.LICENSE);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }

    return (mapping.findForward(FORWARD_final));
  }
}
