/*
 * $RCSfile: LocationLookupHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/13 22:00:15 $ - $Author: mcallist $
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
import java.util.ArrayList;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.settings.Settings;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class LocationLookupHandler extends Action {

  private static Logger logger = Logger.getLogger(LocationLookupHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {

    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    ValueListParameters listParameters = null;
    if (request.getAttribute("listParameters") != null) {
      listParameters = (ValueListParameters)request.getAttribute("listParameters");
    } else {
      listParameters = new ValueListParameters(ValueListConstants.LOCATION_LIST_TYPE, 100, 1);
      // Sorting
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.locationViewMap
          .get("location");
      listParameters.setSortColumn(sortField.getQueryIndex());
      listParameters.setSortDirection("ASC");
    }
    ArrayList columns = new ArrayList();
    columns.add(ValueListConstants.locationViewMap.get("location"));
    listParameters.setColumns(columns);
    ValueList valueList = null;
    try {
      valueList = (ValueList)CVUtility.setupEJB("ValueList",
          "com.centraview.valuelist.ValueListHome", dataSource);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    // there are no permissions for sources.
    ValueListVO listObject = valueList.getValueList(0, listParameters);
    ArrayList buttonList = new ArrayList();
    buttonList.add(new Button("Select", "select", "lu_selectList('Location');", false));
    ValueListDisplay displayParameters = new ValueListDisplay(buttonList, false, false);
    displayParameters.setRadio(true);
    displayParameters.setPagingBar(true);
    listObject.setLookup(true);
    listObject.setLookupType("lookup"); // is this line needed?
    listObject.setDisplay(displayParameters);
    request.setAttribute("dynamicTitle", "Location");
    request.setAttribute("lookupType", "Location");
    request.setAttribute("hideSearch", new Boolean(true));
    request.setAttribute("hideMarketingList", new Boolean(true));
    request.setAttribute("customHeader", "Lookup: Location");
    request.setAttribute("valueList", listObject);
    return (mapping.findForward(".view.lookup"));
  }
}
