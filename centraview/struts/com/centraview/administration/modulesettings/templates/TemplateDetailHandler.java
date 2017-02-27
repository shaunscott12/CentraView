/*
 * $RCSfile: TemplateDetailHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:48 $ - $Author: mking_cv $
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

package com.centraview.administration.modulesettings.templates;

import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.printtemplate.PrintTemplate;
import com.centraview.printtemplate.PrintTemplateVO;
import com.centraview.settings.Settings;

/**
 * Class to retrieve the details of a Print Template from the database.
 * for display on the screen.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class TemplateDetailHandler extends Action
{
  private static Logger logger = Logger.getLogger(TemplateDetailHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    // The ID must be passed in the templateId parameter.
    int templateId = Integer.parseInt(request.getParameter("templateId"));
    // Get the detail from the EJB world ...
    PrintTemplateVO templateVO = null;
    Collection templateCategories = null;
    try {
      PrintTemplate remoteEJB = (PrintTemplate)CVUtility.setupEJB("Printtemplate", "com.centraview.printtemplate.PrintTemplateHome", dataSource);
      templateVO = remoteEJB.getPrintTemplate(templateId);
      templateCategories = remoteEJB.getCategories();
    } catch (Exception e) {
      throw new ServletException(e);
    }
    // ... and populate the DynaActionForm
    DynaActionForm templateForm = (DynaActionForm)form;
    templateVO.populateFormBean(templateForm);
    // Stick the template types (categories) list on the formbean.
    templateForm.set("typeList", templateCategories);
    TemplateUtil.setNavigation(request, "detail");
    // forward to the display screen
    return mapping.findForward(".view.administration.view_template");
  }
}
