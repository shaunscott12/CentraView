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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.group.GroupVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * @author: CentraView, LLC.
 */
public class ViewGroupHandler extends Action {
  private static Logger logger = Logger.getLogger(ViewGroupHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    int individualId = 0;
    try {
      ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
          "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = cfh.create();
      remote.setDataSource(dataSource);
      individualId = userObject.getIndividualID();
      String rowId[] = null;
      String row = null;
      if (request.getParameterValues("rowIdParent") != null) {
        rowId = request.getParameterValues("rowIdParent");
      } else if (request.getParameterValues("rowId") != null) {
        rowId = request.getParameterValues("rowId");
      } else if (request.getParameter("groupId") != null) {
        row = request.getParameter("groupId");
      } else if (session.getAttribute("groupid") != null) {
        row = (String)session.getAttribute("groupid");
        session.removeAttribute("groupid");
      } else {
        row = (String)request.getAttribute("groupid");
      }
      if (rowId != null) {
        row = rowId[0];
      }
      // After performing the logic in the DeleteHanlder, we are generat a new
      // request for the list
      // So we will not be carrying the old error. So that we will try to
      // collect the error from the Session variable
      // Then destory it after getting the Session value
      if (session.getAttribute("listErrorMessage") != null) {
        ActionMessages allErrors = (ActionMessages)session.getAttribute("listErrorMessage");
        saveErrors(request, allErrors);
        session.removeAttribute("listErrorMessage");
      }
      GroupVO groupVO = remote.getGroup(individualId, Integer.parseInt(row));
      DynaActionForm dynaForm = (DynaActionForm)form;
      IndividualVO ivo = remote.getIndividual(groupVO.getOwner());
      if (ivo != null) {
        dynaForm.set("ownerName", new String(ivo.getFirstName() + " " + ivo.getLastName()));
      }
      ivo = null;
      dynaForm.set("groupname", groupVO.getGroupName());
      dynaForm.set("groupdescription", groupVO.getDescription());
      dynaForm.set("groupid", (new Integer(groupVO.getGroupID())).toString());
      Date createdDate = groupVO.getCreatedate();
      Date modifiedDate = groupVO.getModifydate();
      // TODO common date formatter
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy h:mm a");
      dynaForm.set("create", simpleDateFormat.format(createdDate));
      dynaForm.set("modify", simpleDateFormat.format(modifiedDate));
      dynaForm.set("owner", (new Integer(groupVO.getOwner())).toString());

      ListPreference listPreference = userObject.getListPreference("Individual");
      ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
      ValueListParameters listParameters = (ValueListParameters)request
          .getAttribute("listParameters");
      if (listParameters == null) {
        listParameters = new ValueListParameters(ValueListConstants.GROUP_MEMBER_LIST_TYPE,
            listPreference.getRecordsPerPage(), 1);
        FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.individualViewMap
            .get(listPreference.getSortElement());
        listParameters.setSortColumn(sortField.getQueryIndex());
        if (listPreference.getsortOrder()) {
          listParameters.setSortDirection("ASC");
        } else {
          listParameters.setSortDirection("DESC");
        }
      }
      Vector viewColumns = view.getColumns();
      ArrayList columns = new ArrayList();
      ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.GROUP_MEMBER_LIST_TYPE);
      listParameters.setColumns(columns);
      String filter = "SELECT DISTINCT member.childId AS individualId FROM member WHERE member.groupId = "
          + row;
      listParameters.setFilter(filter);
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
      listObject.setCurrentPageParameters(ValueListConstants.AMP + "groupId=" + row);
      ArrayList buttonList = new ArrayList();
      buttonList.add(new Button("Add Members", "button3", "c_lookup('attendee_lookup');", false));
      buttonList.add(new Button("Delete", "delete", "removeMember();", false));
      ValueListDisplay displayParameters = new ValueListDisplay(buttonList, true, true);
      displayParameters.setSortable(true);
      displayParameters.setPagingBar(true);
      displayParameters.setLink(true);
      listObject.setDisplay(displayParameters);
      request.setAttribute("valueList", listObject);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }
    return (mapping.findForward(".view.contact.group_detail"));
  }
}
