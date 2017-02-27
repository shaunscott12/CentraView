/*
 * $RCSfile: DeleteTimeSlipHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:03 $ - $Author: mking_cv $
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

package com.centraview.hr.timesheet;

import java.io.IOException;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.settings.Settings;

public class DeleteTimeSlipHandler extends org.apache.struts.action.Action
{

  private com.centraview.common.UserObject userobjectd;
  private org.apache.struts.validator.DynaValidatorForm dynaValidatorForm;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";
    HttpSession session = request.getSession(true);
    if (session.getAttribute("highlightmodule") != null)
      session.setAttribute("highlightmodule", "hr");

    userobjectd = (com.centraview.common.UserObject)session.getAttribute("userobject");
    HrFacadeHome aa = (HrFacadeHome)CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
    try
    {
      HrFacade remote = (HrFacade)aa.create();
      remote.setDataSource(dataSource);
      int indvID = userobjectd.getIndividualID();
      String timeslipIds = (String)request.getParameter("timeslipIds");
      if (timeslipIds != null)
      {
          remote.deleteTimeSlip(indvID,timeslipIds);
      }
      Integer iTimeSheetId = (Integer)request.getAttribute("timesheetID");
      request.setAttribute("timesheetID", iTimeSheetId);
      returnStatus = ".view.success";
    }
    catch (Exception e)
    {
      returnStatus = "failure";
      System.out.println("[Exception][DeleteTimeSlipHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return mapping.findForward(returnStatus);
  }
}