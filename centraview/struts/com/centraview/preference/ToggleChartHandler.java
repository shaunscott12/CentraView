/*
 * $RCSfile: ToggleChartHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:24 $ - $Author: mking_cv $
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

package com.centraview.preference;

import java.util.Vector;

import javax.servlet.ServletException;
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
import com.centraview.common.UserPrefererences;
import com.centraview.settings.Settings;

/**
 * This action simply takes a type and toggles a preference for the current user
 * to either show or not show the charts.  It will toggle it both on the session
 * userpreference object and in the database.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ToggleChartHandler extends Action
{
  private static Logger logger = Logger.getLogger(ToggleChartHandler.class);
  private static String TICKET = "ticket";
  private static String SALES = "sales";
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession();
    UserObject user = (UserObject)session.getAttribute("userobject");
    int individualId = user.getIndividualID();
    UserPrefererences preferences = user.getUserPref();
    String type = request.getParameter("type");
    PreferenceVO preferenceVO = new PreferenceVO();

    StringBuffer returnPath = new StringBuffer(mapping.findForward(type).getPath());

	String listScope = request.getParameter("listScope");
	if (listScope == null ){
		listScope = "my";
	}
	if (listScope != null && listScope.length() > 0)
	{
		returnPath.append("?listScope="+listScope);
	}

    if (type != null && type.equals(TICKET))
    {
      preferences.setShowTicketCharts(!preferences.isShowTicketCharts());
      String preferenceValue = (preferences.isShowTicketCharts())?"Yes":"No";
      preferenceVO.setTag("");
      preferenceVO.setPreferenceName("showticketcharts");
      preferenceVO.setPreferenceValue(preferenceValue);
    } else if (type != null && type.equals(SALES)){
	    preferences.setShowSalesCharts(!preferences.isShowSalesCharts());
      String preferenceValue = (preferences.isShowSalesCharts())?"Yes":"No";
      preferenceVO.setTag("");
      preferenceVO.setPreferenceName("showsalescharts");
      preferenceVO.setPreferenceValue(preferenceValue);
    }

    try
    {
      Vector preferenceVector = new Vector();
      preferenceVector.add(preferenceVO);
      Preference preferenceEJB = (Preference)CVUtility.setupEJB("Preference", "com.centraview.preference.PreferenceHome", dataSource);
      preferenceEJB.updateUserPreference(individualId, preferenceVector);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    return(new ActionForward(returnPath.toString(), true));
	}
}
