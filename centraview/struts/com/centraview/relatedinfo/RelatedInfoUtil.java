/*
 * $RCSfile: RelatedInfoUtil.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:36 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * @author CentraView, LLC <info@centraview.com>
 */
public class RelatedInfoUtil
{
  private static Logger logger = Logger.getLogger(RelatedInfoUtil.class);
  public static void relatedInfoSetup(HttpServletRequest request, String dataSource, String viewType, int valueListType, HashMap viewMap, String filter, ArrayList buttonList, int extraId) throws ServletException, RemoteException
  {
    // now, get the data from the EJB layer
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();
    ListPreference listPreference = userObject.getListPreference(viewType);
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    ValueListParameters listParameters = null;
    if (request.getAttribute("listParameters") != null) {
      listParameters = (ValueListParameters)request.getAttribute("listParameters");
    } else {
      listParameters = new ValueListParameters(valueListType, listPreference.getRecordsPerPage(), 1);
      // Sorting
      FieldDescriptor sortField = (FieldDescriptor)viewMap.get(listPreference.getSortElement());
      if (sortField != null) {
        listParameters.setSortColumn(sortField.getQueryIndex());
      } else {
        listParameters.setSortColumn(1);  // probably Id.
      }
      if (listPreference.getsortOrder()) {
        listParameters.setSortDirection("ASC");
      } else {
        listParameters.setSortDirection("DESC");
      }
    }
    // Filter
    listParameters.setFilter(filter);
    // Columns
    ArrayList columns = new ArrayList();
    Vector viewColumns = view.getColumns();
    ActionUtil.mapOldView(columns, viewColumns, valueListType);
    listParameters.setColumns(columns);
    // extraId is used for custom field.
    listParameters.setExtraId(extraId);
    // EJB
    ValueList valueList = null;
    try {
      valueList = (ValueList)CVUtility.setupEJB("ValueList", "com.centraview.valuelist.ValueListHome", dataSource);
    } catch (Exception e) {
      logger.error("[relatedInfoSetup] Exception thrown.", e);
      throw new ServletException(e);
    }
    // List!
    ValueListVO listObject = valueList.getValueList(individualId, listParameters);
    // Display Junk
    ValueListDisplay displayParameters = new ValueListDisplay(buttonList, true, false, true, true, true, true);
    displayParameters.setSkipRPP(true);
    displayParameters.setRelatedInfo(true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("valueList", listObject);
  }
}
