/*
 * $RCSfile: RelatedCustomFieldList.java,v $    $Revision: 1.2 $  $Date: 2005/08/01 19:57:38 $ - $Author: mcallist $
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ValueListConstants;

/**
 * This class is the Struts Action class handler for all Related Info
 * screens where the listType is "Address". The class will generate
 * the proper DisplayList and set a request attribute. Control will then
 * transfer to the View layer.
 */
public class RelatedCustomFieldList extends org.apache.struts.action.Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String viewType = "CustomField";
    String forwardPage = "relatedInfoBottom"; // by default, this is the page to forward to
    // get the variables needed from the request object, DO NOT
    // get these values from anywhere else. These values were set
    // by RelatedInfoListHandler, so check there if you need to
    // modify anything. These values should not change in this class.
    String listFor = (String)request.getAttribute("listFor");
    Integer recordID = (Integer)request.getAttribute("recordID");
    int contactType = 0;
    if (listFor.equals("Entity")) {
      contactType = 1;
    } else if (listFor.equals("Opportunity")) {
      contactType = 30;
    } else if (listFor.equals("Project")) {
      contactType = 48;
    } else {
      contactType = 2;
    }
    String filter = "SELECT customfieldId FROM customfield WHERE recordType = " + contactType;
    // Buttons
    ArrayList buttonList = (ArrayList)request.getAttribute("buttonList");
    // now, get the data from the EJB layer and put it on the request
    RelatedInfoUtil.relatedInfoSetup(request, dataSource, viewType, ValueListConstants.CUSTOM_FIELD_RI_LIST_TYPE, ValueListConstants.customFieldRIViewMap, filter.toString(), buttonList, recordID.intValue());
    return (mapping.findForward(forwardPage));
  } // end execute() method
} // end class definition

