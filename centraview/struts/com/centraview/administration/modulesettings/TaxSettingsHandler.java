/*
 * $RCSfile: TaxSettingsHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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
import java.util.HashMap;
import java.util.Vector;

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

import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class TaxSettingsHandler extends Action
{
  private static Logger logger = Logger.getLogger(TaxSettingsHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    DynaActionForm dynaform = (DynaActionForm)form;
    HttpSession session = request.getSession();

    /*
    String submodule = (request.getParameter("sourcefor") != null) ? (String)request.getParameter("sourcefor") : (String)request.getAttribute("typeofsubmodule");
    String setting = (String)request.getParameter("setting");

    request.setAttribute("typeofmodule", AdministrationConstantKeys.MODULESETTINGS);
    request.setAttribute("typeofsubmodule", submodule);
    request.setAttribute("settingfor", setting);
    */

      AccountHelperHome home = (AccountHelperHome)CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome", "AccountHelper");
      try {
        AccountHelper remote = home.create();
        remote.setDataSource(dataSource);
        Vector taxClass = remote.getTaxClasses();
        Vector taxJurisdiction = remote.getTaxJurisdiction();
        HashMap taxMatrix = remote.getTaxMartix();
        dynaform.set("taxClassVec",taxClass);
        dynaform.set("taxJurisdictionsVec",taxJurisdiction);
        dynaform.set("taxMatrix",taxMatrix);
        dynaform.set("taxClassValue","");
        dynaform.set("taxJurisdictionsValue","");
        dynaform.set("taxClassID","");
        dynaform.set("taxJurisdictionsID","");
        request.setAttribute("masterdataform", dynaform);
      } catch (Exception e) {
        logger.error("[Exception] TaxSettingsHandler.Execute Handler ", e);
      }

    return mapping.findForward(".view.administration.tax_settings");
  }
}
