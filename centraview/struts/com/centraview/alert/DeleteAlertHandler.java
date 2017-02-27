/*
 * $RCSfile: DeleteAlertHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/02 14:46:35 $ - $Author: mcallist $
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

package com.centraview.alert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class DeleteAlertHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(DeleteAlertHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String forward = Constants.FORWARD_FAILURE;
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      HttpSession session = request.getSession(true);
      String alertIDs[] = request.getParameterValues("IdsToDelete");
      UserObject userobjectd = (UserObject) session.getAttribute("userobject");
      int userid = userobjectd.getIndividualID();
      if (alertIDs.length > 0) {
        AlertHome ah = (AlertHome) CVUtility.getHomeObject("com.centraview.alert.AlertHome", "Alert");
        Alert remote = ah.create();
        remote.setDataSource(dataSource);
        int activityID;
        for (int i = 0; i < alertIDs.length; i++) {
          activityID = Integer.parseInt(alertIDs[i]);
          remote.deleteAlert(activityID, userid);
        }
      }
      forward = Constants.FORWARD_DEFAULT;
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      forward = Constants.FORWARD_FAILURE;
    }
    return (mapping.findForward(forward));
  }
}
