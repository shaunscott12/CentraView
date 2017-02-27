/*
 * $RCSfile: MergeSearchResults.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:44 $ - $Author: mking_cv $
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

package com.centraview.administration.merge;

import java.io.IOException;

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
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

/**
 * The merge-search form will forward here when the form is finally submitted.
 * This class will Squeeze the form-bean into the SearchCriteriaVO and submit it
 * to the EJB.  The EJB in turn will hand us the results.  Which we can display the first
 * set, and a summary of the other sets.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 * 
 */
public class MergeSearchResults extends Action
{
  private static Logger logger = Logger.getLogger(MergeSearchResults.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    DynaValidatorForm mergeSearchForm = (DynaValidatorForm)form;
    SearchCriteriaVO searchCriteria = this.populateSearchCriteria(mergeSearchForm);
    // Well lets get the groupings already.
    MergeSearchResultVO searchResult = null;
    
    MergeHome mergeHome = (MergeHome)CVUtility.getHomeObject("com.centraview.administration.merge.MergeHome", "Merge");
    try {
      Merge remoteMerge = mergeHome.create();
      remoteMerge.setDataSource(dataSource);
      searchResult = remoteMerge.performSearch(searchCriteria);
    } catch (Exception e) {
      logger.error("[Exception] MergeSearchResults.Execute Handler ", e);
      // We probably should return a nicer error at this point.
      throw new ServletException(e);
    }
    // now that we have the searchResult stick it on the session
    // and then forward to the displaysearchresult handler
    HttpSession session = request.getSession(true);
    session.setAttribute("mergeSearchResult", searchResult);
    
    return mapping.findForward(".view.administration.merge_search_results");
  }
  
  private SearchCriteriaVO populateSearchCriteria(DynaActionForm mergeSearchForm)
  {
    // here we don't need to get it from the EJB because the field lists don't need to be populated
    // completely with custom fields, etc.
    SearchCriteriaVO searchCriteria = new SearchCriteriaVO();
    int mergeType = Integer.parseInt((String)mergeSearchForm.get("mergeType"));
    searchCriteria.setType(mergeType);
    int searchDomain = Integer.parseInt((String)mergeSearchForm.get("searchDomain"));
    searchCriteria.setSearchDomain(searchDomain);
    int threshhold = Integer.parseInt((String)mergeSearchForm.get("threshhold"));
    searchCriteria.setThreshhold(threshhold);
    SearchCriteriaLine [] lines = (SearchCriteriaLine[])mergeSearchForm.get("criteriaLine");
    for (int i=0; i < lines.length; i++) {
      SearchCriteriaLine criterion = lines[i];
      int fieldIndex = Integer.parseInt(criterion.getFieldIndex());
      int searchType = Integer.parseInt(criterion.getSearchTypeIndex());
      int score = Integer.parseInt(criterion.getMatchValue());
      searchCriteria.addFieldCriterion(fieldIndex, searchType, score);
    }
    return searchCriteria;
  } // end populateSearchCriteria()
  
}

