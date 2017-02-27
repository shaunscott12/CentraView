/*
 * $RCSfile: NewTemplateHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:48 $ - $Author: mking_cv $
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
import com.centraview.settings.Settings;

/**
 * The execute method of this action class simply sets the list of PrintTemplate
 * types on the form bean and forwards to a blank template details page.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class NewTemplateHandler extends Action
{
  private static Logger logger = Logger.getLogger(NewTemplateHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    Collection templateCategories = null;
    try {
      // get the categories from the Database.  
      // (this action would be really simple and non-resource-intensive if we threw these in the GML)
      templateCategories = (Collection)CVUtility.invokeEJBMethod("Printtemplate", "com.centraview.printtemplate.PrintTemplateHome", "getCategories", null, dataSource);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    ((DynaActionForm)form).set("typeList", templateCategories);
    TemplateUtil.setNavigation(request, "detail");
    return mapping.findForward(".view.administration.new_template");
  }
}
