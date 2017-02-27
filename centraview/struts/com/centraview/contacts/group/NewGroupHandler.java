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

package com.centraview.contacts.group;

import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * @author CentraView, LLC <info@centraview.com>
 */
public class NewGroupHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception 
  {
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    // Just set up an empty list with the right columns
    ListPreference listPreference = userObject.getListPreference("Individual");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    ValueListParameters listParameters = new ValueListParameters(ValueListConstants.INDIVIDUAL_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
    FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.groupViewMap.get(listPreference.getSortElement());
    listParameters.setSortColumn(sortField.getQueryIndex());
    if (listPreference.getsortOrder()) {
      listParameters.setSortDirection("ASC");
    } else {
      listParameters.setSortDirection("DESC");
    }
    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();
    ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.INDIVIDUAL_LIST_TYPE);
    listParameters.setColumns(columns);
    listParameters.setTotalRecords(0);
    ValueListVO listObject = new ValueListVO(new ArrayList(), listParameters);
    ArrayList buttonList = new ArrayList();
    buttonList.add(new Button("Add Members", "button3", "saveToAddMembers();", false));
    buttonList.add(new Button("Delete", "delete", "javascript:null(0);", false));
    ValueListDisplay displayParameters = new ValueListDisplay(buttonList, true, true);
    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("valueList", listObject);
    return mapping.findForward(".view.contact.group_detail");
  }
}
