/*
 * $RCSfile: FileValueListAction.java,v $    $Revision: 1.2 $  $Date: 2005/07/12 18:38:43 $ - $Author: mcallist $
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

package com.centraview.file;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
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
 * File lists. Created: Oct 27, 2004
 * 
 * @author CentraView, LLC.
 */
public class FileValueListAction extends Action {
  private static Logger logger = Logger.getLogger(FileValueListAction.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException {
    String finalForward = ".view.files.list";
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    UserObject userObject = (UserObject) session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    // Check the session for an existing error message (possibly from the delete
    // handler)
    ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
    if (allErrors != null) {
      saveErrors(request, allErrors);
      session.removeAttribute("listErrorMessage");
    }

    // Check wheather the actionType is it Lookup or not
    String actionType = request.getParameter("actionType");

    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);
    HashMap moduleList = new HashMap();
    if (globalMasterLists.get("moduleList") != null) {
      moduleList = (HashMap) globalMasterLists.get("moduleList");
    }

    ListPreference listPreference = userObject.getListPreference("File");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));

    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters) request.getAttribute("listParameters");
    if (requestListParameters == null) { // build up new Parameters
      listParameters = new ValueListParameters(ValueListConstants.FILE_LIST_TYPE, listPreference.getRecordsPerPage(), 1);
    } else { // paging or sorting or something, use the parameters from the
              // request.
      listParameters = requestListParameters;
    }
    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor) ValueListConstants.fileViewMap.get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());
      if (listPreference.getsortOrder())
        listParameters.setSortDirection("ASC");
      else
        listParameters.setSortDirection("DESC");
    }

    int defaultFolder = userObject.getUserPref().getDefaultFolderID();
    int folderID = defaultFolder;
    String param = request.getParameter("folderId");
    if (param != null) {
      if (param.indexOf("*") == -1) {
        folderID = Integer.parseInt(param);
      } else {
        folderID = Integer.parseInt(param.substring(0, param.indexOf("*")));
      }
    }
    listParameters.setFolderID(folderID);
    request.setAttribute("folderId", String.valueOf(folderID));

    String scope = request.getParameter(FileConstantKeys.TYPEOFFILELIST);
    if (scope != null && !scope.equals("")) {
      request.setAttribute("fileTypeRequest", scope);
      request.setAttribute(FileConstantKeys.TYPEOFFILELIST, scope);
    } else {
      scope = "MY";
      request.setAttribute("fileTypeRequest", "MY");
      request.setAttribute(FileConstantKeys.TYPEOFFILELIST, "MY");
    }

    // Search handling
    String filter = null;
    String filterParameter = request.getParameter("filter");
    if (filterParameter != null) {
      filter = (String) session.getAttribute("listFilter");
      if (scope.equals("MY")) {
        filter += " AND Owner = " + individualId;
        session.setAttribute("listFilter", filter);
      }
      request.setAttribute("appliedSearch", filterParameter);
    } else {
      // TODO Need to handle folder access too.
      if (scope.equals("MY"))
        filter = "SELECT FileID FROM cvfile WHERE Owner = " + individualId;
      session.removeAttribute("listFilter");
    }
    listParameters.setFilter(filter);

    // Build up the breadcrumbs on the folder bar.
    // the crumbs will be the complete FolderRootPath except in the
    // cases where you are looking at ALL, MY or PUBLIC then we will manually get rid of 
    // the leading things.
    try {
      Vector vec = null;
      int rootId = 0;
      ArrayList remove = new ArrayList();
      CvFileFacade fileFacade = new CvFileFacade();
      vec = fileFacade.getFolderRootPath(individualId, folderID, dataSource);
      if (vec != null) {
        Collections.reverse(vec);
        if (scope.equals("ALL")) {
          Iterator iter = vec.iterator();
          while (iter.hasNext()) {
            DDNameValue current = (DDNameValue) iter.next();
            if (current.getName().equals("CV_ROOT")) {
              remove.add(current);
            } else if (current.getName().equals("CVFS_ROOT")) {
              rootId = current.getId();
              remove.add(current);
            }
          }
          iter = remove.iterator();
          while (iter.hasNext()) {
            vec.remove(iter.next());
          }
          vec.add(0, new DDNameValue(rootId, "All Files"));
        } else if (scope.equals("MY")) {
          Iterator iter = vec.iterator();
          while (iter.hasNext()) {
            DDNameValue current = (DDNameValue) iter.next();
            if (current.getName().equals("CV_ROOT")) {
              remove.add(current);
            } else if (current.getName().equals("CVFS_ROOT")) {
              remove.add(current);
            } else if (current.getName().equals("CVFS_USER")) {
              remove.add(current);
            } else if (current.getName().equals(userObject.getLoginName())) {
              rootId = current.getId();
              remove.add(current);
            }
          }
          iter = remove.iterator();
          while (iter.hasNext()) {
            vec.remove(iter.next());
          }
          vec.add(0, new DDNameValue(rootId, "My Files"));
        } else if (scope.equals("PUBLIC")) {
          Iterator iter = vec.iterator();
          while (iter.hasNext()) {
            DDNameValue current = (DDNameValue) iter.next();
            if (current.getName().equals("CV_ROOT")) {
              remove.add(current);
            } else if (current.getName().equals("CVFS_ROOT")) {
              remove.add(current);
            } else if (current.getName().equals("Public Folders")) {
              rootId = current.getId();
              remove.add(current);
            }
          }
          iter = remove.iterator();
          while (iter.hasNext()) {
            vec.remove(iter.next());
          }
          vec.add(0, new DDNameValue(rootId, "Public Folders"));
        }
        request.setAttribute("breadCrumbs", vec);
      }
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }

    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();
    // We the Action Type is Entity's Lookup then we must have
    // to set the columns which we are going to look
    if (actionType != null && actionType.equals("lookup")) {
      Vector lookupViewColumns = new Vector();
      lookupViewColumns.add("Name");
      ActionUtil.mapOldView(columns, lookupViewColumns, ValueListConstants.FILE_LIST_TYPE);
      listParameters.setRecordsPerPage(100);
      finalForward = ".view.files.filelookup";
    } else {
      ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.FILE_LIST_TYPE);
    }
    listParameters.setColumns(columns);

    // Get the list!
    ValueListHome valueListHome = (ValueListHome) CVUtility.getHomeObject("com.centraview.valuelist.ValueListHome", "ValueList");
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

    if (actionType != null && actionType.equals("lookup")) {
      // When we select the lookup then we should add the Select button to the
      // valueList
      buttonList.add(new Button("Select", "select", "lu_selectList('File');", false));
      displayParameters = new ValueListDisplay(buttonList, false, false);
      displayParameters.setRadio(true);
      listObject.setLookup(true);
      listObject.setLookupType(actionType);
      request.setAttribute("dynamicTitle", "File");
      request.setAttribute("lookupType", "File");
    } else {
      buttonList.add(new Button("View", "view", "vl_viewList();", false));
      buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
      buttonList.add(new Button("Duplicate", "duplicate", "vl_duplicateList();", false));
      displayParameters = new ValueListDisplay(buttonList, true, true);
    }
    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);
    displayParameters.setLink(true);
    // Add the download file column
    displayParameters.setDownloadIcon(true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("valueList", listObject);

    // For the searchBar
    String moduleID = (String) moduleList.get("File");
    request.setAttribute("moduleId", moduleID);
    request.setAttribute("listType", "File");

    return mapping.findForward(finalForward);
  }
}
