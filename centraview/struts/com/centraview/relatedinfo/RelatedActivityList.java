/*
 * $RCSfile: RelatedActivityList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:35 $ - $Author: mking_cv $
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
 * screens where the listType is "Activity". The class will generate
 * the proper DisplayList and set a request attribute. Control will then
 * transfer to the RelatedInfoList_c.jsp file for display on the View layer.
 * @author CentraView, LLC.
 */
public class RelatedActivityList extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(RelatedActivityList.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String forwardPage = "relatedInfoBottom"; // by default, this is the page to forward to
    String viewType = "MultiActivity";
    // get the variables needed from the request object, DO NOT
    // get these values from anywhere else. These values were set
    // by RelatedInfoListHandler, so check there if you need to
    // modify anything. These values should not change in this class.
    String listType   = (String)request.getAttribute("listType");
    String listFor = (String)request.getAttribute("listFor");
    Integer recordId = (Integer)request.getAttribute("recordID");
    // Filter
    boolean completed = false;
    if (listType.equals("CompletedActivity"))
    {
      completed = true;
    }
    int contactType = 0;
    if (listFor.equals("Entity"))
    {
      contactType = 1;
    } else if (listFor.equals("Opportunity")) {
      contactType = 30;
    } else {
      // Means we are an Individual listFor
      contactType = 2;
    }
    String filter;
    if (listFor.equals("Opportunity"))
    {
      filter = "SELECT a.ActivityID FROM activity a LEFT JOIN activitylink al ON (a.activityid = al.ActivityID) WHERE al.RecordTypeID=" + contactType + " AND al.RecordID=" + recordId;
    }else if (listFor.equals("Project")){
      filter = "SELECT a.ActivityID FROM activity a LEFT JOIN activitylink al ON (a.activityid = al.ActivityID) WHERE al.RecordTypeID=36 AND al.RecordID=" + recordId;
    }else{
      filter = "SELECT a.ActivityID FROM activity a LEFT JOIN activitylink al ON (a.activityid = al.ActivityID) WHERE al.RecordTypeID=" + contactType + " AND al.RecordID=" + recordId;
    }
    if (completed)
    {
      filter += " AND a.Status = 2";
    }else{
      filter += " AND a.Status != 2";
    }
    // Buttons
    ArrayList buttonList = (ArrayList)request.getAttribute("buttonList");
    // now, get the data from the EJB layer and put it on the request
    RelatedInfoUtil.relatedInfoSetup(request, dataSource, viewType, ValueListConstants.ACTIVITY_LIST_TYPE, ValueListConstants.activityViewMap, filter.toString(), buttonList, 0);
    return (mapping.findForward(forwardPage));
  } // end execute() method
} // end class
