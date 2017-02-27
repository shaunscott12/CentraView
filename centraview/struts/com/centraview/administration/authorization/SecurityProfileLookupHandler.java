/*
 * $RCSfile: SecurityProfileLookupHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:36 $ - $Author: mking_cv $
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

package com.centraview.administration.authorization;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public final class SecurityProfileLookupHandler extends Action
{
  private static Logger logger = Logger.getLogger(SecurityProfileLookupHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException
  {
    
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    ValueListParameters listParameters = null;
    if (request.getAttribute("listParameters") != null) {
      listParameters = (ValueListParameters)request.getAttribute("listParameters");
    } else {
      listParameters = new ValueListParameters(ValueListConstants.SECURITY_PROFILE_LIST_TYPE, 100, 1);
      // Sorting
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.securityProfileListViewMap.get("ProfileName");
      listParameters.setSortColumn(sortField.getQueryIndex());
      listParameters.setSortDirection("ASC");
    }
    ArrayList columns = new ArrayList();
    columns.add(ValueListConstants.securityProfileListViewMap.get("ProfileName"));
    listParameters.setColumns(columns);
    ValueList valueList = null;
    try {
      valueList = (ValueList)CVUtility.setupEJB("ValueList", "com.centraview.valuelist.ValueListHome", dataSource);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    ValueListVO listObject = valueList.getValueList(0, listParameters);
    ArrayList buttonList = new ArrayList();
    buttonList.add(new Button("Select", "select", "lu_addSelectedItemsToParent();", false));
    ValueListDisplay displayParameters = new ValueListDisplay(buttonList, false, false);
    // hack alert: radio is false checkboxes is true but we are relying on the "lookupType" listObject attribute
    // to print the right kinda checkboxes.
    displayParameters.setRadio(false);
    displayParameters.setCheckboxes(true);
    displayParameters.setPagingBar(true);
    listObject.setLookup(true);
    listObject.setLookupType("SecurityProfile");
    listObject.setDisplay(displayParameters);
    request.setAttribute("dynamicTitle", "Security Profile");
    request.setAttribute("lookupType", "Security Profile");
    request.setAttribute("hideSearch", new Boolean(true));
    request.setAttribute("hideMarketingList", new Boolean(true));
    request.setAttribute("customHeader", "Lookup: Security Profile");
    request.setAttribute("valueList", listObject);
    return (mapping.findForward(".view.lookup"));
  }
}
