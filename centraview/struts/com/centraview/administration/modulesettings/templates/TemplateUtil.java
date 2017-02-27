/*
 * $RCSfile: TemplateUtil.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:48 $ - $Author: mking_cv $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.centraview.administration.common.AdministrationConstantKeys;

/**
 * Class to hold static method for crap that is done all the time in
 * templates.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class TemplateUtil
{
  /**
   * Just set the tab bar tab to be on, and the left nav stuff.
   * @param request
   */
  static public void setNavigation(HttpServletRequest request, String bodySelection)
  {
    HttpSession session = request.getSession(true);
    // make sure the right tab is highlighted.
    session.setAttribute("highlightmodule", "admin");
    // Used to set the left nav stuff in the Administration module.
    request.setAttribute("typeofmodule", AdministrationConstantKeys.MODULESETTINGS);
    request.setAttribute("typeofsubmodule", "TemplateManagement");
    // Tell the template container to put the list body in.
    request.setAttribute("bodySelection", bodySelection);
  }
}
