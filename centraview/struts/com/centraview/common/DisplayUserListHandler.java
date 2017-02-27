/*
 * $RCSfile: DisplayUserListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:59 $ - $Author: mking_cv $
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
/*
 * DisplayUserListHandler is display list from view page
 *
 */

package com.centraview.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.settings.Settings;

public class DisplayUserListHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(DisplayUserListHandler.class);
  /**
   * Global Forwards for exception handling
   */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /**
   * Redirect constant
   */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
   * Executes initialization of required parameters and open window for entry of
   * note returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      long listId = 0;
      if (request.getParameter("listId") != null)
        listId = Long.parseLong((String)request.getParameter("listId"));
      // get listgenerator instance
      ListGenerator listGenerator = ListGenerator.getListGenerator(dataSource);
      // get displaylist
      DisplayList displayList = listGenerator.getDisplayList(listId);
      // get the listtype
      String listType = displayList.getListType();
      // set in request the display list
      request.setAttribute("displaylist", displayList);
      // set listid
      request.setAttribute("listId", "" + listId);
      // forward to list handler as per listtype
      FORWARD_final = "cancel" + listType;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}