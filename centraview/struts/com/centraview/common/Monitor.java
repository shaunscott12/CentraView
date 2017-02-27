/*
 * $RCSfile: Monitor.java,v $    $Revision: 1.3 $  $Date: 2005/09/02 12:00:07 $ - $Author: mcallist $
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
package com.centraview.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.settings.Settings;

/**
 * This Action class is used to monitor CentraView Basically a HTTP request will
 * go to http://hostname:port/monitor.do And a simple HTML page will be returned
 * that provides information. It should ensure that it is possible to talk to
 * the EJB layer and the database.
 */
public class Monitor extends Action {
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String monitorMessage = "UNKNOWN";
    ContactFacadeHome contactFacadeHome = null;
    try {
      long startTime = System.currentTimeMillis();
      contactFacadeHome = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade contactRemote = contactFacadeHome.create();
      contactRemote.setDataSource(dataSource);
      EntityVO entityVO = contactRemote.getEntity(1);
      long endTime = System.currentTimeMillis();
      // How long it took to retreive data in milliseconds
      long msDataRetreival = (endTime - startTime);
      if (entityVO == null) {
        monitorMessage = "FAILED: Record Not Found";
      } else if (entityVO != null && entityVO.getName().equals("")) {
        monitorMessage = "FAILED: Default Entity Name is Empty";
      } else {
        monitorMessage = "PASSED: Entity data retreived in " + msDataRetreival + " milliseconds";
      }
    } catch (Exception e) {
      if (contactFacadeHome == null) {
        monitorMessage = "FAILED: We probably were unable to get a connection to the JNDI Server";
      } else {
        monitorMessage = "FAILED: We probably were unable to get a connection to the EJB Server";
      }
    }
    request.setAttribute("MonitorMessage", monitorMessage);
    return mapping.findForward(Constants.FORWARD_DEFAULT);
  }
} // end of class Monitor
