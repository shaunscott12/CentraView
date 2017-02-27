/*
 * $RCSfile: ActivityNewDispatch.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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

package com.centraview.activity;

import java.io.IOException;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * What does this do... Created: Sep 21, 2004
 * @author CentraView, LLC.
 */
public class ActivityNewDispatch extends Action {
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String subScope = request.getParameter("subScope");

    ActionForward forward = null;
    if (subScope.equals("ForecastSales"))
      forward = new ActionForward("/NewOpportunity.do");
    else if (subScope.equals("LiteratureFulfillment"))
      forward = new ActionForward("/NewLiterature.do");
    else if (subScope.equals("ActivityTask"))
      forward = new ActionForward("/NewProjectTask.do");
    else if (subScope.equals("Meeting"))
      forward = new ActionForward("/activities/activity_dispatch.do?" + ConstantKeys.TYPEOFACTIVITY
          + "=" + ConstantKeys.MEETING);
    else if (subScope.equals("Call"))
      forward = new ActionForward("/activities/activity_dispatch.do?" + ConstantKeys.TYPEOFACTIVITY
          + "=" + ConstantKeys.CALL);
    else if (subScope.equals("NextAction"))
      forward = new ActionForward("/activities/activity_dispatch.do?" + ConstantKeys.TYPEOFACTIVITY
          + "=" + ConstantKeys.NEXTACTION);
    else if (subScope.equals("ToDo"))
      forward = new ActionForward("/activities/activity_dispatch.do?" + ConstantKeys.TYPEOFACTIVITY
          + "=" + ConstantKeys.TODO);
    else
      forward = new ActionForward("/activities/activity_dispatch.do?" + ConstantKeys.TYPEOFACTIVITY
          + "=" + ConstantKeys.APPOINTMENT);
    return forward;
  }
}
