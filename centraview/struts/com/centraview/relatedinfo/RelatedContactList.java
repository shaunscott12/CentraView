/*
 * $RCSfile: RelatedContactList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:35 $ - $Author: mking_cv $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ValueListConstants;

/**
 * This class is the Struts Action class handler for all Related Info
 * screens where the listType is "Contacts". The class will generate
 * the proper DisplayList and set a request attribute. Control will then
 * transfer to the RelatedInfoList_c.jsp file for display on the View layer.
 * 
 * This seems to appear only in Project
 */
public class RelatedContactList extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(RelatedContactList.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String forwardPage = "relatedInfoBottom"; // by default, this is the page to forward to
    String viewType = "BottomContacts";
    // get the variables needed from the request object, DO NOT
    // get these values from anywhere else. These values were set
    // by RelatedInfoListHandler, so check there if you need to
    // modify anything. These values should not change in this class.
    String listFor = (String)request.getAttribute("listFor");
    Integer recordId = (Integer)request.getAttribute("recordID");
    // Filter
    StringBuffer filter = new StringBuffer();
    filter.append("SELECT owner AS individualId FROM project WHERE projectid = "); 
    filter.append(recordId);
    filter.append(" UNION SELECT recordid AS individualId FROM projectlink where recordtypeid = 15 AND projectlink.projectid = ");
    filter.append(recordId);
    filter.append(" UNION SELECT manager AS individualId FROM project WHERE projectid = ");
    filter.append(recordId);
    filter.append(" UNION SELECT ta.AssignedTo AS individualId FROM task AS tk, taskassigned AS ta WHERE ta.TaskID=tk.ActivityID and tk.ProjectID = ");
    filter.append(recordId);
    // Buttons
    ArrayList buttonList = (ArrayList)request.getAttribute("buttonList");
    // now, get the data from the EJB layer and put it on the request
    RelatedInfoUtil.relatedInfoSetup(request, dataSource, viewType, ValueListConstants.INDIVIDUAL_LIST_TYPE, ValueListConstants.individualViewMap, filter.toString(), buttonList, 0);
    return (mapping.findForward(forwardPage));
  } // end execute() method
} // end class definition