/*
 * $RCSfile: SaveTemplateHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:48 $ - $Author: mking_cv $
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
import com.centraview.printtemplate.PrintTemplateVO;
import com.centraview.settings.Settings;

/**
 * Action class to simply take the submitted form bean and either create a new
 * template or save changes to an existing one.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class SaveTemplateHandler extends Action
{
  private static Logger logger = Logger.getLogger(SaveTemplateHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    // We will be forwarding to the detail screen so get the forward from the struts
    // config and then we can append the templateId dynamically.
    String forward = mapping.findForward(".view.administration.view_template").getPath();
    // parameterize it up.
    PrintTemplateVO templateVO = new PrintTemplateVO((DynaActionForm)form);
    Object[] parameter = {templateVO};
    try {
      // Check ID if it is zero we are creating a new one, otherwise just updating an existing
      if (templateVO.getPtdetailId() == 0) {
        // create new
        Integer newTemplateId = (Integer)CVUtility.invokeEJBMethod("Printtemplate", "com.centraview.printtemplate.PrintTemplateHome", "newTemplate", parameter, dataSource);
        forward += String.valueOf(newTemplateId);
      } else {
        // update
        CVUtility.invokeEJBMethod("Printtemplate", "com.centraview.printtemplate.PrintTemplateHome", "updateTemplate", parameter, dataSource);
        forward += String.valueOf(templateVO.getPtdetailId());
      }
    } catch (Exception e) {
      logger.error("[execute] Exception executing EJB call, refer (hopefully) to above CVUtility stacktrace.");
      throw new ServletException(e);
    }
    // redirect to the new page, avoid the always dreaded "repost error."
    return new ActionForward(forward, true);
  } // end execute(...)
}
