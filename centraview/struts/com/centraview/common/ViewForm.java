/*
 * $RCSfile: ViewForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.settings.Settings;
import com.centraview.view.View;
import com.centraview.view.ViewHome;

public class ViewForm extends ActionForm
{
  private static Logger logger = Logger.getLogger(ViewForm.class);
  private String viewId;
  private String listType;
  private String viewName;
  private String sortMember;
  private String sortType = "A";
  private String searchId;
  private String searchType = "A";
  private String noOfRecord;
  private String createdId;
  private String ownerId;
  private Vector searchIdName;
  private Vector availableColumnVec;
  private Vector selectedColumnVec;
  private String availableColumn[];
  private String selectedColumn[];
  private Vector sortMemberVec;
  private String defaultSystemView;

  public String getListType()
  {
    return this.listType;
  }

  public void setListType(String listType)
  {
    this.listType = listType;
  }

  public String getNoOfRecord()
  {
    return this.noOfRecord;
  }

  public void setNoOfRecord(String noOfRecord)
  {
    this.noOfRecord = noOfRecord;
  }

  public String getSearchId()
  {
    return this.searchId;
  }

  public void setSearchId(String searchId)
  {
    this.searchId = searchId;
  }

  public String getSortMember()
  {
    return this.sortMember;
  }

  public void setSortMember(String sortMember)
  {
    this.sortMember = sortMember;
  }

  public String getSortType()
  {
    return this.sortType;
  }

  public void setSortType(String sortType)
  {
    this.sortType = sortType;
  }

  public String getViewId()
  {
    return this.viewId;
  }

  public void setViewId(String viewId)
  {
    this.viewId = viewId;
  }

  public String getViewName()
  {
    return this.viewName;
  }

  public void setViewName(String viewName)
  {
    this.viewName = viewName;
  }

  public String getSearchType()
  {
    return this.searchType;
  }

  public void setSearchType(String searchType)
  {
    this.searchType = searchType;
  }

  public String getOwnerId()
  {
    return this.ownerId;
  }

  public void setOwnerId(String ownerId)
  {
    this.ownerId = ownerId;
  }

  public String getCreatedId()
  {
    return this.createdId;
  }

  public void setCreatedId(String createdId)
  {
    this.createdId = createdId;
  }

  public Vector getSearchIdName()
  {
    return this.searchIdName;
  }

  public void setSearchIdName(Vector searchIdName)
  {
    this.searchIdName = searchIdName;
  }

  /**
   * Validates user input data @param mapping ActionMapping @param request
   * HttpServletRequest @return errors ActionErrors
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    // initialize new actionerror object
    ActionErrors errors = new ActionErrors();

    try {
      // initialize validation
      Validation validation = new Validation();

      // title
      if (this.getViewName() == null || this.getViewName().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredFied", "View Name"));
      }

      if (errors != null) {
        request.setAttribute("TYPEOFOPERATION", request.getParameter("TYPEOFOPERATION"));

        // get userid from session
        HttpSession session = request.getSession();
        int individualID = ((UserObject)session.getAttribute("userobject")).getIndividualID();

        // get data from database
        ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome", "View");
        View remote = (View)viewHome.create();
        remote.setDataSource(dataSource);

        String listType = (String)request.getParameter("listType");

        Vector selectedColumn = new Vector();
        if (this.getSelectedColumn() != null) {
          String[] selectedCol = this.getSelectedColumn();
          for (int i = 0; i < selectedCol.length; i++) {
            DDNameValue ddSelectedCol = new DDNameValue(selectedCol[i], selectedCol[i]);
            selectedColumn.add(ddSelectedCol);
          }
        }
        this.setSelectedColumnVec(selectedColumn);

        Vector vecColumn = (Vector)remote.getAllColumns(listType);

        Vector sortMemberVec = new Vector(vecColumn);
        this.setSortMemberVec(sortMemberVec);

        int sizeOfAvailableList = vecColumn.size();
        int sizeOfSelectedList = selectedColumn.size();
        int i = 0;
        int j = 0;
        String idOfAvailableList = "";
        String idOfSelectedList = "";
        DDNameValue ddAvailableListInfo = null;
        DDNameValue ddSelectedListInfo = null;
        boolean removed = false;
        while (i < sizeOfAvailableList) {
          removed = false;
          ddAvailableListInfo = (DDNameValue)vecColumn.get(i);
          idOfAvailableList = ddAvailableListInfo.getStrid();
          while (j < sizeOfSelectedList) {
            ddSelectedListInfo = (DDNameValue)selectedColumn.get(j);
            idOfSelectedList = ddSelectedListInfo.getStrid();
            if (idOfAvailableList.equals(idOfSelectedList)) {
              vecColumn.remove(i);
              removed = true;
              sizeOfAvailableList--;
              break;
            } else {
              j++;
            }
            ddSelectedListInfo = null;
          }
          j = 0;
          if (!removed)
            i++;
          ddAvailableListInfo = null;
          removed = false;
        }
        this.setAvailableColumnVec(vecColumn);
        Vector searchVector = (Vector)remote.getOwnerSearch(individualID, request.getParameter("listType").toString());
        this.setSearchIdName(searchVector);
        request.setAttribute("listId", request.getParameter("listId"));
        request.setAttribute("viewform", this);
        request.setAttribute("TYPEOFOPERATION", request.getParameter("TYPEOFOPERATION"));
        request.setAttribute("primaryTableName", request.getParameter("primarytablename"));
      }
    } catch (Exception e) {
      logger.error("[validate] Exception thrown.", e);
    }
    return errors;
  }

  public String[] getAvailableColumn()
  {
    return this.availableColumn;
  }

  public void setAvailableColumn(String[] availableColumn)
  {
    this.availableColumn = availableColumn;
  }

  public Vector getAvailableColumnVec()
  {
    return this.availableColumnVec;
  }

  public void setAvailableColumnVec(Vector availableColumnVec)
  {
    this.availableColumnVec = availableColumnVec;
  }

  public String[] getSelectedColumn()
  {
    return this.selectedColumn;
  }

  public void setSelectedColumn(String[] selectedColumn)
  {
    this.selectedColumn = selectedColumn;
  }

  public Vector getSelectedColumnVec()
  {
    return this.selectedColumnVec;
  }

  public void setSelectedColumnVec(Vector selectedColumnVec)
  {
    this.selectedColumnVec = selectedColumnVec;
  }

  public Vector getSortMemberVec()
  {
    return this.sortMemberVec;
  }

  public void setSortMemberVec(Vector sortMemberVec)
  {
    this.sortMemberVec = sortMemberVec;
  }

  public String getDefaultSystemView()
  {
    return this.defaultSystemView;
  }

  public void setDefaultSystemView(String defaultSystemView)
  {
    this.defaultSystemView = defaultSystemView;
  }

  /**
   * To clear form data
   */
  public static ViewForm clearForm(ViewForm viewForm)
  {
    viewForm.setCreatedId("");
    viewForm.setDefaultSystemView("");
    viewForm.setListType("");
    viewForm.setNoOfRecord("");
    viewForm.setOwnerId("");
    viewForm.setSearchId("");
    viewForm.setSearchType("A");
    viewForm.setSortMember("");
    viewForm.setSortType("A");
    viewForm.setViewId("");
    viewForm.setViewName("");
    viewForm.setSelectedColumnVec(new Vector());
    return viewForm;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer("");
    sb.append(" - viewId = [" + viewId + "]\n");
    sb.append(" - listType = [" + listType + "]\n");
    sb.append(" - viewName = [" + viewName + "]\n");
    sb.append(" - sortMember = [" + sortMember + "]\n");
    sb.append(" - sortType = [" + sortType + "]\n");
    sb.append(" - searchId = [" + searchId + "]\n");
    sb.append(" - searchType = [" + searchType + "]\n");
    sb.append(" - noOfRecord = [" + noOfRecord + "]\n");
    sb.append(" - createdId = [" + createdId + "]\n");
    sb.append(" - ownerId = [" + ownerId + "]\n");
    sb.append(" - searchIdName = [" + searchIdName + "]\n");
    sb.append(" - availableColumnVec = [" + availableColumnVec + "]\n");
    sb.append(" - selectedColumnVec = [" + selectedColumnVec + "]\n");
    sb.append(" - availableColumn = [" + availableColumn + "]\n");
    sb.append(" - selectedColumn = [" + selectedColumn + "]\n");
    sb.append(" - sortMemberVec = [" + sortMemberVec + "]\n");
    sb.append(" - defaultSystemView = [" + defaultSystemView + "]\n");
    return sb.toString();
  }
}
