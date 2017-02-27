/*
 * $RCSfile: EditCustomViewHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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
 
package com.centraview.administration.modulesettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import com.centraview.advancedsearch.AdvancedSearch;
import com.centraview.advancedsearch.AdvancedSearchHome;
import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.view.View;
import com.centraview.view.ViewHome;
import com.centraview.view.ViewVO;

public class EditCustomViewHandler extends org.apache.struts.action.Action 
{
  public static final String forward = ".view.administration.edit_custom_view";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws Exception 
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();    // logged in user

    DynaActionForm viewForm = (DynaActionForm)form;   // "customViewForm" defined in administration.xml

    ActionErrors allErrors = new ActionErrors();
    
    Integer viewId = (Integer)viewForm.get("viewId");
    if (viewId == null || viewId.intValue() <= 0) {
      allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Custom View ID"));
      return(mapping.findForward(forward));
    }
    
    // get data from database
    ViewHome viewHome = (ViewHome)CVUtility.getHomeObject("com.centraview.view.ViewHome","View");
    View remote = null;
    try {
      remote = (View)viewHome.create();
    } catch (CreateException e) {
      System.out.println("[execute] Exception thrown: " + e);
      throw new ServletException(e);
    }
    remote.setDataSource(dataSource);
    
    ViewVO viewVO = remote.getView(individualId, viewId.intValue());

    if (viewVO != null) {
      viewForm.set("viewName", viewVO.getViewName());
      viewForm.set("moduleName", "");
      viewForm.set("recordType", viewVO.getListType());
      viewForm.set("sortColumn", viewVO.getSortMember());
      viewForm.set("sortDirection", viewVO.getSortType());
      try {
        viewForm.set("defaultSearch", new Integer(viewVO.getSearchId()));
      } catch (NumberFormatException nfe) {
        viewForm.set("defaultSearch", new Integer(0));
      }
      try {
        viewForm.set("recordsPerPage", new Integer(viewVO.getNoOfRecord()));
      } catch (NumberFormatException nfe) {
        viewForm.set("recordsPerPage", new Integer(10));
      }
      viewForm.set("defaultSystemView", viewVO.getDefaultView());

      try {
        viewForm.set("defaultSearch", new Integer(viewVO.getSearchId()));
      } catch (NumberFormatException nfe) {
        viewForm.set("defaultSearch", new Integer(0));
      }
    }

    String listType = viewVO.getListType();

                                              // holds fields that should not appear
    ArrayList unionFields = new ArrayList();  // in available fields list because they
                                              // already appear in selected fields list

    // selectedFields - the list of fields selected for this view
    ArrayList selectedFields = new ArrayList();
    Vector selectedFieldsDb = (Vector)viewVO.getViewColumnVO();
    Iterator sfIter = selectedFieldsDb.iterator();
    while (sfIter.hasNext()) {
      DDNameValue field = (DDNameValue)sfIter.next();
      HashMap fieldMap = new HashMap();
      // someone made a decision long before me, that both the value
      // and display name properties for the HTML select widget should
      // be the name of the field. So I'm not changing it. -MK 12/29/2004
      String fieldName = (String)field.getName();
      fieldMap.put("fieldId", fieldName);
      fieldMap.put("fieldName", fieldName);
      selectedFields.add(fieldMap);
      unionFields.add(fieldName);
    }
    viewForm.set("selectedFields", selectedFields);

    // availableFields - the list of fields available in this module
    ArrayList availableFields = new ArrayList();    // collection we send to JSP via form bean
    ArrayList allFields = new ArrayList();    // collection of all available fields
    Vector availableFieldsDb = (Vector)remote.getAllColumns(listType);    // collection from DB
    Iterator afIter = availableFieldsDb.iterator();
    while (afIter.hasNext()) {
      DDNameValue field = (DDNameValue)afIter.next();
      HashMap fieldMap = new HashMap();
      // someone made a decision long before me, that both the value
      // and display name properties for the HTML select widget should
      // be the name of the field. So I'm not changing it. -MK 12/29/2004
      fieldMap.put("fieldId", field.getName());
      fieldMap.put("fieldName", field.getName());
      if (! unionFields.contains(field.getName())) {
        // only add the field if it's not in the selectedFields list
        availableFields.add(fieldMap);
      }
      // used for the sort fields list
      allFields.add(fieldMap);
    }
    viewForm.set("availableFields", availableFields);
    viewForm.set("allFields", allFields);

    // get the list of available searches
    AdvancedSearchHome searchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome","AdvancedSearch");
    AdvancedSearch searchRemote = null;
    try {
      searchRemote = (AdvancedSearch)searchHome.create();
    } catch (CreateException e) {
      System.out.println("[execute] Exception thrown: " + e);
      throw new ServletException(e);
    }
    searchRemote.setDataSource(dataSource);

    String moduleIdString = AdvancedSearchUtil.getModuleId(listType, dataSource);

    int moduleId = 0;
    try {
      moduleId = Integer.parseInt(moduleIdString);
    } catch (NumberFormatException nfe) {
      // already set default value...
    }

    HashMap searchListDb = searchRemote.getSavedSearchList(individualId, moduleId);

    ArrayList searchList = new ArrayList();
    
    // the EJB returns a HashMap of key/value pairs, each key being
    // a searchID, and value being the name. Not the best way to send
    // this data to the JSP. So here, I'll iterate the HashMap, and create
    // an ArrayList of LabelValueBeans
    Set keys = searchListDb.keySet();
    Iterator keyIter = keys.iterator();
    while (keyIter.hasNext()) {
      Number key = (Number)keyIter.next();
      String value = (String)searchListDb.get(key);
      LabelValueBean searchPair = new LabelValueBean(value, String.valueOf(key));
      searchList.add(searchPair);
    }

    viewForm.set("searchList", searchList);

    return mapping.findForward(forward);      
  }   // end execute() method
  
}   // end DisplayEditViewHandler class

