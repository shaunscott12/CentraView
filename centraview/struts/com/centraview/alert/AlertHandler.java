/*
 * $RCSfile: AlertHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/06 16:45:49 $ - $Author: mcallist $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * AlertHandler.java Handler-Servlet for Alert Action.
 * @author Shilpa Patil
 */
public class AlertHandler extends Action {
  private static Logger logger = Logger.getLogger(AlertHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_success = "success";

  private static String FORWARD_final;
  private static final int MAX_CHARS_IN_TITLE = 55;
  public final int REFRESHCOUNT = 5;
  private ArrayList alertsArl;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    FORWARD_final = FORWARD_success;

    try {
      HttpSession session = request.getSession(true);

      if (session.getAttribute("LastAccessedAlert") != null) {
        alertsArl = (ArrayList) session.getAttribute("AlertDetails");

        Integer temp = (Integer) session.getAttribute("LastAccessedAlert");
        int count = temp.intValue();
        int remainder = count % REFRESHCOUNT;

        // After particular no of requests, Hashmap should be updated
        // from bean.
        if (remainder == 0) // get fresh data from DB
        {
          // call to ejb which will return latest hm.
          // Set this hm back to session
          getAlertList(session, dataSource);
          session.setAttribute("AlertDetails", alertsArl);
        }

        int noOfAlerts = alertsArl.size();

        // If count is greater than no of alerts then reset the counter.
        if (count > (noOfAlerts - 1)) {
          // Reset the count.
          getAlertList(session, dataSource);
          session.setAttribute("LastAccessedAlert", new Integer(0));
        } else {
          count++;
          session.setAttribute("LastAccessedAlert", new Integer(count));
        }
      } else // If its a 1st request
      {
        getAlertList(session, dataSource);
        session.setAttribute("AlertDetails", alertsArl);

        // Take 1st entry of HashMap
        session.setAttribute("LastAccessedAlert", new Integer(0));
      }
    } catch (Exception e) {
      System.out.println("[Exception] AlertHandler.execute: " + e.toString());
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }

  private void getAlertList(HttpSession session, String dataSource)
  {
    try {
      if (alertsArl != null) {
        alertsArl.clear();
      } else {
        alertsArl = new ArrayList();
      }

      HashMap hmConf = new HashMap();

      // this code takes care of adding an alert if the license is expired
      if ((session.getAttribute("Alert_LicenseExpired") != null)
          && ((String) session.getAttribute("Alert_LicenseExpired")).equals("true")) {
        alertsArl.add("Licence has expired!! please contact CentraView");
      }

      // get the userid fron the userobject
      UserObject userobjectd = (UserObject) session.getAttribute("userobject");
      int userid = userobjectd.getIndividualID();

      hmConf.put("UserID", new Integer(userid));

      // fetch the alerts from DB
      AlertHome ah = (AlertHome) CVUtility.getHomeObject("com.centraview.alert.AlertHome", "Alert");
      Alert remote = ah.create();
      remote.setDataSource(dataSource);
      Collection rs = remote.getAlertList(hmConf);

      if (rs != null) {
        Iterator it = rs.iterator();

        String Title = "";
        String AlertType = "";
        alertsArl.clear();

        while (it.hasNext()) {
          HashMap hm = (HashMap) it.next();
          Title = (String) hm.get("title");
          AlertType = (String) hm.get("alerttype");

          if (Title.length() > MAX_CHARS_IN_TITLE) {
            Title = Title.substring(0, MAX_CHARS_IN_TITLE) + "...";
          }

          if (AlertType.equals("ALERT")) {
            alertsArl.add(hm);
          }
        }
      }
    } catch (Exception e) {
      logger.error("[getAlertList]: Exception", e);
    }
  }
}
