/*
 * $RCSfile: ReportAction.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:38 $ - $Author: mking_cv $
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
 
package com.centraview.report.web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.centraview.advancedsearch.AdvancedSearch;
import com.centraview.advancedsearch.AdvancedSearchHome;
import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.report.ejb.session.ReportFacade;
import com.centraview.report.ejb.session.SessionBeanFactory;
import com.centraview.report.valueobject.ReportConstants;
import com.centraview.report.valueobject.ReportContentVO;
import com.centraview.report.valueobject.ReportVO;
import com.centraview.report.valueobject.TheSearchItem;
import com.centraview.settings.Settings;

/**
 * <code>ReportAction</code> base class for all Handlers of report module.
 * Contains all common method.
 */
public class ReportAction extends Action
{
  private static Logger logger = Logger.getLogger(ReportAction.class);
  /** Accessor method - get the <I>module id</I> */
  protected synchronized int getModuleId(HttpServletRequest request) throws ServletException
  {
    int moduleId;
    String strId;
    try {
      strId = getId("moduleId", request);
      try {
        moduleId = Integer.parseInt(strId);
      } catch (NumberFormatException nfe) {
        moduleId = ReportConstants.ENTITY_MODULE_ID;
      }
    } catch (Exception ex) {
      throw new ServletException(ex);
    }
    return moduleId;
  }

  /**	Accessor method - gets the <I>Id</I>.
   *   Used to pass in a generic parameter
   */
  public int getTheId(String whatId, HttpServletRequest request) throws ServletException
  {
    int templateId;
    String strId;
    try {
      strId = getId(whatId, request);
      try {
        templateId = Integer.parseInt(strId);
      } catch (NumberFormatException nfe) {
        templateId = 0;
      }
    } catch (Exception ex) {
      logger.error("[getTheId] Exception thrown.", ex);
      throw new ServletException(ex);
    }
    return templateId;
  }

  /** Helper method - gets the value for a given ID. First looks in
   *  request parameters, then request attributes, and finally the Session. 
   */
  protected String getId(String key, HttpServletRequest request)
  {
    String strID = request.getParameter(key);
    if (strID != null && strID.length() != 0) {
      return strID;
    }
    strID = (String)request.getAttribute(key);
    if (strID != null && strID.length() != 0) {
      return strID;
    }
    return (String)request.getSession().getAttribute(key);
  }

  /**
   * Helper method - gets the ReportVO object from AdHocReportForm
   * 
   * @param timeZone String
   * @param form ActionForm
   * @return ReportVO
   */
  protected ReportVO getAdHocReportVOFromForm(String timeZone, DynaActionForm form)
  {
    ReportVO reportVO = new ReportVO();
    reportVO.setModuleId(((Integer)form.get("moduleId")).intValue());
    reportVO.setName((String)form.get("name"));
    reportVO.setDescription((String)form.get("description"));
    reportVO.setReportId(((Integer)form.get("reportId")).intValue());
    reportVO.setSelectedFields(getSelectedFieldsWithNames((String)form.get("contentFields"), (String)form.get("contentOrders"), (String)form.get("contentFieldNames")));
    reportVO.setSearchFields(getSearchFields((String)form.get("searchFields")));
    return reportVO;
  }

  protected SearchVO getSearchVOFromForm(ActionForm form, HttpServletRequest request) throws IOException, ServletException
  {
    // The SearchVO we need should only consist of an array of searchCriteria
    // and a moduleid.
    DynaActionForm advancedSearchForm = (DynaActionForm)form;
    SearchVO searchVO = new SearchVO();
    int moduleId = ((Integer)advancedSearchForm.get("moduleId")).intValue();
    searchVO.setModuleID(moduleId);
    List searchCriteriaList = Arrays.asList((SearchCriteriaVO[])advancedSearchForm.get("searchCriteria"));
    ArrayList searchCriteria = new ArrayList(searchCriteriaList);
    // before simply setting the criteria on the VO check and make sure we really have any.
    for (int i = 0; i < searchCriteria.size(); i++) {
      SearchCriteriaVO currentCriteria = (SearchCriteriaVO)searchCriteria.get(i);
      if (currentCriteria.getTableID().equals("") || currentCriteria.getFieldID().equals("") || currentCriteria.getValue().equals("")) {
        searchCriteria.remove(i);
      }
    }
    if (searchCriteria.size() > 0) {
      searchVO.setSearchCriteria(searchCriteria);
    }
    return searchVO;
  }

  /**
   * Helper method - gets the AdHocReportForm from ReportVO object
   *
   * @param reportVO ReportVO
   * @return ActionForm
   */
  protected ActionForm getAdHocReportFormFromReportVO(ReportVO reportVO, ActionForm form, HttpServletRequest request) throws RemoteException
  {

    HttpSession session = request.getSession(true);
    // Need the userobject so we know who we are.
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    DynaActionForm reportForm = (DynaActionForm)form;
    int moduleId = reportVO.getModuleId();

    reportForm.set("description", reportVO.getDescription());
    reportForm.set("moduleId", new Integer(moduleId));
    reportForm.set("name", reportVO.getName());
    reportForm.set("reportId", new Integer(reportVO.getReportId()));
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    // Get Saved searches from the EJB
    AdvancedSearch remoteAdvancedSearch = null;
    try {
      AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
      remoteAdvancedSearch = advancedSearchHome.create();
      remoteAdvancedSearch.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[getAdHocReportFormFromReportVO] Exception thrown.", e);
    }
    HashMap allFields = (HashMap)reportForm.get("allFields");
    // Only repopulate all the lists if it is empty
    if (allFields == null || allFields.isEmpty()) {
      // Build dem drop downs
      HashMap tableMap = remoteAdvancedSearch.getSearchTablesForModule(individualId, moduleId);
      HashMap conditionMap = SearchVO.getConditionOptions();
      // Build an ArrayList of LabelValueBeans
      ArrayList tableList = AdvancedSearchUtil.buildSelectOptionList(tableMap);
      ArrayList conditionList = AdvancedSearchUtil.buildSelectOptionList(conditionMap);
      reportForm.set("tableList", tableList);
      reportForm.set("conditionList", conditionList);

      // Get all the appropriate field lists and stick them on the formbean
      // The fieldList (ArrayList of LabelValueBean) will be stored in a
      // HashMap with the key being the (Number)tableId
      TreeSet keySet = new TreeSet(tableMap.keySet());
      Iterator keyIterator = keySet.iterator();
      allFields = new HashMap();
      while (keyIterator.hasNext()) {
        Number key = (Number)keyIterator.next();
        // iterate the tables and get all the field lists
        // stick them in a hashmap of arraylists on the form bean.
        HashMap tableFields = remoteAdvancedSearch.getSearchFieldsForTable(individualId, key.intValue(), moduleId);
        ArrayList tableFieldList = AdvancedSearchUtil.buildSelectOptionList(tableFields);
        allFields.put(key, tableFieldList);
      } // end while(keyIterator.hasNext())
      reportForm.set("allFields", allFields);
    } // end if (allFields == null || allFields.isEmpty())
    return reportForm;
  }

  /**
   * Helper method - gets the StandardReportVO object from StandardReportForm
   *
   * @param timeZone String
   * @param form ActionForm
   * @return ReportVO
   */
  protected ReportVO getStandardReportVOFromForm(String timeZone, ActionForm form)
  {

    DynaActionForm reportForm = (DynaActionForm)form;
    ReportVO reportVO = new ReportVO();
    reportVO.setModuleId(((Integer)reportForm.get("moduleId")).intValue());
    reportVO.setName((String)reportForm.get("name"));
    reportVO.setDescription((String)reportForm.get("description"));
    reportVO.setReportId(((Integer)reportForm.get("reportId")).intValue());
    if (((String)reportForm.get("startday")) != null && !((String)reportForm.get("startday")).equals(""))
      reportVO.setFrom(getDate((String)reportForm.get("startday"), (String)reportForm.get("startmonth"), (String)reportForm.get("startyear"), timeZone));
    if (((String)reportForm.get("endday")) != null && !((String)reportForm.get("endday")).equals(""))
      reportVO.setTo(getDate((String)reportForm.get("endday"), (String)reportForm.get("endmonth"), (String)reportForm.get("endyear"), timeZone));
    reportVO.setSearchFields(getSearchFields((String)reportForm.get("searchFields")));
    return reportVO;
  }

  /**
   * Helper method - gets the StandardReportForm from StandardReportVO object
   *
   * @param reportVO ReportVO
   * @return ActionForm
   */
  protected ActionForm getStandardReportFormFromReportVO(ReportVO reportVO, ActionForm form, HttpServletRequest request) throws RemoteException
  {
    DynaActionForm reportForm = (DynaActionForm)form;
    int moduleId = reportVO.getModuleId();
    HttpSession session = request.getSession(true);
    // Need the userobject so we know who we are.
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    reportForm.set("description", reportVO.getDescription());
    reportForm.set("moduleId", new Integer(moduleId));
    reportForm.set("name", reportVO.getName());
    reportForm.set("reportId", new Integer(reportVO.getReportId()));
    if (reportVO.getFrom() != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(reportVO.getFrom());
      reportForm.set("startday", String.valueOf(calendar.get(Calendar.DATE)));
      reportForm.set("startmonth", String.valueOf(calendar.get(Calendar.MONTH) + 1));
      reportForm.set("startyear", String.valueOf(calendar.get(Calendar.YEAR)));
    }
    if (reportVO.getTo() != null) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(reportVO.getTo());
      reportForm.set("endday", String.valueOf(calendar.get(Calendar.DATE)));
      reportForm.set("endmonth", String.valueOf(calendar.get(Calendar.MONTH) + 1));
      reportForm.set("endyear", String.valueOf(calendar.get(Calendar.YEAR)));
    }
    // fields for the search part of things.
    reportForm.set("ownerId", (String.valueOf(individualId)));
    SearchCriteriaVO[] searchCriteria = (SearchCriteriaVO[])reportForm.get("searchCriteria");
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    // We need some advanced Search stuff from the EJB layer
    AdvancedSearch remoteAdvancedSearch = null;
    try {
      AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
      remoteAdvancedSearch = advancedSearchHome.create();
      remoteAdvancedSearch.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[getStandardReportFormFromReportVO] Exception thrown.", e);
    }
    HashMap allFields = (HashMap)reportForm.get("allFields");
    // Only repopulate all the lists if it is empty
    if (allFields == null || allFields.isEmpty()) {
      // Build dem drop downs
      HashMap tableMap = remoteAdvancedSearch.getSearchTablesForModule(individualId, moduleId);
      HashMap conditionMap = SearchVO.getConditionOptions();
      // Build an ArrayList of LabelValueBeans
      ArrayList tableList = AdvancedSearchUtil.buildSelectOptionList(tableMap);
      ArrayList conditionList = AdvancedSearchUtil.buildSelectOptionList(conditionMap);
      // Get all the appropriate field lists and stick them on the formbean
      // The fieldList (ArrayList of LabelValueBean) will be stored in a
      // HashMap with the key being the (Number)tableId
      TreeSet keySet = new TreeSet(tableMap.keySet());
      Iterator keyIterator = keySet.iterator();
      allFields = new HashMap();
      while (keyIterator.hasNext()) {
        Number key = (Number)keyIterator.next();
        // iterate the tables and get all the field lists
        // stick them in a hashmap of arraylists on the form bean.
        HashMap tableFields = remoteAdvancedSearch.getSearchFieldsForTable(individualId, key.intValue(), moduleId);
        ArrayList tableFieldList = AdvancedSearchUtil.buildSelectOptionList(tableFields);
        allFields.put(key, tableFieldList);
      } // end while(keyIterator.hasNext())
      reportForm.set("allFields", allFields);
      reportForm.set("tableList", tableList);
      reportForm.set("conditionList", conditionList);
    } // end if (allFields == null || allFields.isEmpty())
    reportForm.set("searchCriteria", searchCriteria);
    return reportForm;
  }

  /**
   * Helper method - gets the Selected fields from form data
   *
   * @param contentFields String
   * @param contentOrders String
   * @return ArrayList
   */
  protected ArrayList getSelectedFields(String contentFields, String contentOrders)
  {

    int[] fieldIds = null;
    String[] sortOs = null;
    if (contentOrders != null && contentOrders.length() > 0) {
      StringTokenizer tok = new StringTokenizer(contentOrders, ";");
      int fieldLength = tok.countTokens();
      fieldIds = new int[fieldLength];
      sortOs = new String[fieldLength];
      int i = 0;
      while (tok.hasMoreTokens()) {
        String tmp = tok.nextToken();
        String ad = tmp.substring(0, 1);
        String id = tmp.substring(1, tmp.length());
        if (ad.equals("D")) {
          ad = ReportConstants.SORTORDER_DESC;
        } else {
          ad = ReportConstants.SORTORDER_ASC;
        }
        StringTokenizer tok2 = new StringTokenizer(id, ":");
        String tableId = tok2.nextToken();
        String fieldId = tok2.nextToken();
        fieldIds[i] = Integer.parseInt(fieldId);
        sortOs[i++] = ad;
      }
    } // end if (contentOrders != null && contentOrders.length() > 0)

    String sort = null;
    Byte sortSeq = null;
    int fieldId = 0;
    int tableId = 0;
    ArrayList selectedFields = new ArrayList();

    // The contentFields values have a semicolon seperated list
    // of colon separated Ids Where the first ID is the parent or table id
    // and the second is the field id.
    if (contentFields != null && contentFields.length() > 0) {
      int i = 0;
      StringTokenizer tok = new StringTokenizer(contentFields, ";");
      while (tok.hasMoreTokens()) {
        sort = null;
        sortSeq = null;
        StringTokenizer tok2 = new StringTokenizer(tok.nextToken(), ":");
        tableId = Integer.parseInt(tok2.nextToken());
        fieldId = Integer.parseInt(tok2.nextToken());
        // hopefully this sort stuff still works after adding the parent
        // ID in above.
        if (fieldIds != null) {
          for (i = 0; i < fieldIds.length; i++) {
            if (fieldIds[i] == fieldId) {
              sort = sortOs[i];
              sortSeq = Byte.valueOf("" + (i + 1));
              break;
            }
          }
        }
        selectedFields.add(new ReportContentVO(fieldId, tableId, null, sortSeq, sort));
      }
    }
    return selectedFields;
  }

  /**
   * Helper method - gets the Selected fields from form data
   * There are two multiple select boxes that we will be pulling data from
   * The Selected Fields and the sort order. 
   * @param contentFields String the list returned from the struts layer of the selected fields.
   * @param contentOrders String the list returned from the struts layer of the sort order selection, ascending or decending.
   * @return ArrayList
   */
  protected ArrayList getSelectedFieldsWithNames(String contentFields, String contentOrders, String contentFieldNames)
  {
    int[] fieldIds = null;
    String[] sortOs = null;
    if (contentOrders != null && contentOrders.length() > 0) {
      StringTokenizer tok = new StringTokenizer(contentOrders, ";");
      int fieldLength = tok.countTokens();
      fieldIds = new int[fieldLength];
      sortOs = new String[fieldLength];
      int i = 0;
      while (tok.hasMoreTokens()) {
        String tmp = tok.nextToken();
        String ad = tmp.substring(0, 1);
        String id = tmp.substring(1, tmp.length());
        if (ad.equals("D"))
          ad = ReportConstants.SORTORDER_DESC;
        else
          ad = ReportConstants.SORTORDER_ASC;
        StringTokenizer tok2 = new StringTokenizer(id, ":");
        String tableId = tok2.nextToken();
        String fieldId = tok2.nextToken();
        fieldIds[i] = Integer.parseInt(fieldId);
        sortOs[i++] = ad;
      }
    }

    String sort = null;
    Byte sortSeq = null;
    int fieldId = 0;
    int tableId = 0;
    String fieldName = "";
    ArrayList selectedFields = new ArrayList();

    // The contentFields values have a semicolon seperated list
    // of colon separated Ids Where the first ID is the parent or table id
    // and the second is the field id.
    if (contentFields != null && contentFields.length() > 0) {
      int i = 0;
      StringTokenizer tok = new StringTokenizer(contentFields, ";");
      StringTokenizer nameTok = new StringTokenizer(contentFieldNames, ";");
      while (tok.hasMoreTokens()) {
        sort = null;
        sortSeq = null;
        StringTokenizer tok2 = new StringTokenizer(tok.nextToken(), ":");
        tableId = Integer.parseInt(tok2.nextToken());
        fieldId = Integer.parseInt(tok2.nextToken());
        fieldName = nameTok.nextToken();
        // Any way go through the sortIds found and see if the current 
        // field is part of the sorting rules.
        if (fieldIds != null) {
          for (i = 0; i < fieldIds.length; i++) {
            if (fieldIds[i] == fieldId) {
              sort = sortOs[i];
              sortSeq = Byte.valueOf(String.valueOf((i + 1)));
              break;
            }
          }
        }
        selectedFields.add(new ReportContentVO(fieldId, tableId, fieldName, sortSeq, sort));
      }
    }
    return selectedFields;
  }

  /**
   * Helper method - gets the Selected search criteria fields from form data
   *
   * @param searchFields String
   * @return ArrayList
   */
  protected ArrayList getSearchFields(String searchFields)
  {
    ArrayList searches = new ArrayList();
    TheSearchItem searchItem = null;
    if (searchFields != null && searchFields.length() > 0) {
      StringTokenizer tok = new StringTokenizer(searchFields, ";");
      int tokCount = 0;
      int row = 0;
      while (tok.hasMoreTokens()) {
        String tmp = tok.nextToken();
        row = tokCount % 5;
        switch (row) {
          case 0 :
            // allocate new row of search
            if (searchItem != null) {
              searches.add(searchItem);
            }
            searchItem = new TheSearchItem();
            // assign joinId
            searchItem.setAndOr(tmp);
            break;
          case 1 :
            // assign tableId
            searchItem.setTableId(Integer.parseInt(tmp));
            break;
          case 2 :
            // assign fieldId
            searchItem.setFieldId(Integer.parseInt(tmp));
            break;
          case 3 :
            // assign conditionId
            searchItem.setConditionId(Integer.parseInt(tmp));
            break;
          case 4 :
            // assign value
            searchItem.setCriteriaValue(tmp);
            break;
        }
        ++tokCount;
      }
    }
    // last searchItem dont added yet
    if (searchItem != null && searchItem.getConditionId() > -1) {
      if (null == searchItem.getCriteriaValue())
        searchItem.setCriteriaValue("");
      searches.add(searchItem);
    }
    return searches;
  }

  /**
   * Helper method - gets the Date form data
   *
   * @param day String
   * @param month String
   * @param year String
   * @param timeZone String
   * @return Date
   */
  protected java.sql.Date getDate(String day, String month, String year, String timeZone)
  {
    int startday = Integer.parseInt(day);
    int startmonth = Integer.parseInt(month) - 1;
    int startyear = Integer.parseInt(year);
    GregorianCalendar calendar = new GregorianCalendar(startyear, startmonth, startday);
    return CVUtility.convertTimeZone(new java.sql.Date(calendar.getTimeInMillis()), java.util.TimeZone.getTimeZone(timeZone), java.util.TimeZone.getTimeZone("GMT"));
  }

  /**
   * Helper method - gets the ReportFacade
   *
   * @return ReportFacade
   */
  protected ReportFacade getReportFacade()
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    ReportFacade remote = null;
    try {
      remote = SessionBeanFactory.getReportFacade();
      remote.setDataSource(dataSource);
    } catch (Exception e) {
      logger.error("[getReportFacade] Exception thrown.", e);
    }
    return remote;
  }

  public static String doCSVCorrect(String tmp)
  {
    String theString = tmp;
    if (theString.indexOf("\"") > 0) {
      theString = theString.replaceAll("\"", "\"\"");
      theString = "\"" + theString + "\"";
    } else if (theString.indexOf("\r") > 0) {
      theString = "\"" + theString + "\"";
    } else if (theString.indexOf("\n") > 0) {
      theString = "\"" + theString + "\"";
    } else if (theString.indexOf(",") > 0) {
      theString = "\"" + theString + "\"";
    } else if (theString.startsWith(" ") || theString.startsWith("\t") || theString.endsWith(" ") || theString.endsWith("\t")) {
      theString = "\"" + theString + "\"";
    }
    return theString;
  }
}