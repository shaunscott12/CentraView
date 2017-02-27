/*
 * $RCSfile: RelatedEmailHistory.java,v $    $Revision: 1.3 $  $Date: 2005/07/25 13:41:40 $ - $Author: mcallist $
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
package com.centraview.relatedinfo;

import java.io.IOException;
import java.util.ArrayList;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ValueListConstants;

/**
* This class is the Struts Action class handler for all Related Info
* screens where the listType is "Activity". The class will generate
* the proper DisplayList and set a request attribute. Control will then
* transfer to the RelatedInfoList_c.jsp file for display on the View layer.
*
*/
public class RelatedEmailHistory extends org.apache.struts.action.Action
{

  private static Logger logger = Logger.getLogger(RelatedEmailHistory.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String viewType = "Email";
    String forwardPage = "relatedInfoBottom"; // by default, this is the page to
                                              // forward to
    // get the variables needed from the request object, DO NOT
    // get these values from anywhere else. These values were set
    // by RelatedInfoListHandler, so check there if you need to
    // modify anything. These values should not change in this class.
    String listType = (String)request.getAttribute("listType");
    String listFor = (String)request.getAttribute("listFor");
    Integer recordID = (Integer)request.getAttribute("recordID");
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject");
    int individualId = user.getIndividualID();
    int contactType = 0;
    if (listFor.equals("Entity"))
    {
      contactType = 1;
    } else {
      contactType = 2;
    }
    // The filter string is very complex we need to get it from the EJB layer.
    EmailFacadeHome facadeHome = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
    String filter = "";
	try
    {
      EmailFacade emailFacade = facadeHome.create();
      emailFacade.setDataSource(dataSource);
      filter = emailFacade.getRelatedEmailList(individualId, recordID.intValue(), contactType);
    } catch (Exception e) {
      logger.error("[Exception] RelatedEmailHistory.Execute Handler ", e);
      throw new ServletException(e);
    }
    // Buttons
    ArrayList buttonList = (ArrayList)request.getAttribute("buttonList");
    // now, get the data from the EJB layer and put it on the request
    RelatedInfoUtil.relatedInfoSetup(request, dataSource, viewType, ValueListConstants.EMAIL_LIST_TYPE, ValueListConstants.emailViewMap, filter.toString(), buttonList, recordID.intValue());
    return (mapping.findForward(forwardPage));
  } // end execute() method
} // end class definition

