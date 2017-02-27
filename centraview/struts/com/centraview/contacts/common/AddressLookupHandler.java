/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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

package com.centraview.contacts.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * The class called by IndividualList.do which will retreive the list from the
 * EJB layer and pass it to the display layer.
 * @author CentraView, LLC.
 */
public class AddressLookupHandler extends Action {
  private static Logger logger = Logger.getLogger(AddressLookupHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String final_Forward = ".view.contacts.addreslookup";
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);

    int contactID = 0;
    int contactType = 1;
    String contactIDStr = request.getParameter("contactID");
    if ((contactIDStr != null) && !contactIDStr.equals("")) {
      contactID = Integer.parseInt(contactIDStr);
    }

    String contactTypeStr = request.getParameter("contactType");

    if ((contactTypeStr != null) && !contactTypeStr.equals("")) {
      contactType = Integer.parseInt(contactTypeStr);
    }

    String filter = "SELECT address AS addressId FROM addressrelate WHERE contactType = "
        + contactType + " AND contact = " + contactID;

    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request
        .getAttribute("listParameters");
    if (requestListParameters == null) // build up new Parameters
    {
      listParameters = new ValueListParameters(ValueListConstants.ADDRESSLOOKUP_LIST_TYPE, 100, 1);
    } else { // paging or sorting or something, use the parameters from the
              // request.
      listParameters = requestListParameters;
    }

    listParameters.setSortColumn(2);
    listParameters.setSortDirection("ASC");
    ArrayList columns = new ArrayList();

    // end hack for search in lookups
    Vector lookupViewColumns = new Vector();
    lookupViewColumns.add("Address");
    ActionUtil.mapOldView(columns, lookupViewColumns, ValueListConstants.ADDRESSLOOKUP_LIST_TYPE);
    listParameters.setRecordsPerPage(100);

    // start hack for search in lookups
    request.setAttribute("showSearch", new Boolean(true));

    if (request.getParameter("filter") != null) {
      String searchString = request.getParameter("filter");
      filter += " AND (AddressID LIKE '%" + searchString + "%' OR AddressType LIKE '%"
          + searchString + "%'" + " OR Street1 LIKE '%" + searchString + "%' OR Street2 LIKE '%"
          + searchString + "%'" + " OR City LIKE '%" + searchString + "%' OR state LIKE '%"
          + searchString + "%'" + " OR Zip LIKE '%" + searchString + "%' OR country LIKE '%"
          + searchString + "%'" + " OR Website LIKE '%" + searchString
          + "%' OR jurisdictionID LIKE '%" + searchString + "%') ";
    }
    listParameters.setColumns(columns);
    listParameters.setFilter(filter);

    // Get the list!
    ValueListHome valueListHome = (ValueListHome)CVUtility.getHomeObject(
        "com.centraview.valuelist.ValueListHome", "ValueList");
    ValueList valueList = null;
    try {
      valueList = valueListHome.create();
    } catch (CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    valueList.setDataSource(dataSource);
    ValueListVO listObject = valueList.getValueList(individualId, listParameters);
    ArrayList buttonList = new ArrayList();

    ValueListDisplay displayParameters = null;
    displayParameters = new ValueListDisplay(buttonList, false, false);

    StringBuffer parameterValues = new StringBuffer();
    parameterValues.append(ValueListConstants.AMP);
    parameterValues.append("contactID=" + contactID);
    parameterValues.append(ValueListConstants.AMP);
    parameterValues.append("contactType=" + contactType);
    listObject.setCurrentPageParameters(parameterValues.toString());
    listObject.setLookup(true);
    listObject.setLookupType("address");

    request.setAttribute("hideMarketingList", new Boolean(true));

    // When we select the lookup then we should add the Select button to the
    // valueList
    buttonList.add(new Button("Select", "select", "lu_selectList('address');", false));
    request.setAttribute("dynamicTitle", "Address");
    request.setAttribute("lookupType", "Address");
    displayParameters.setRadio(true);

    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("valueList", listObject);

    // For the searchBar
    request.setAttribute("listType", "address");
    request.setAttribute("showSearch", new Boolean(false));
    session.setAttribute("highlightmodule", "contact");

    return mapping.findForward(final_Forward);
  }
}
