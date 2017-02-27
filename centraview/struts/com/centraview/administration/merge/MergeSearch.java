/*
 * $RCSfile: MergeSearch.java,v $    $Revision: 1.2 $  $Date: 2005/07/12 20:53:50 $ - $Author: mcallist $
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
import java.util.ArrayList;
import java.util.HashMap;

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
import org.apache.struts.validator.DynaValidatorForm;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

/**
 * This MergeSearchHandler will contact the EJB layer and get the SearchCriteriaVO
 * It will set up the dropdowns that will be displayed on the screen.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 * 
 */
public class MergeSearch extends Action
{
  private static Logger logger = Logger.getLogger(MergeSearch.class);
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    HttpSession session = request.getSession(true);
    // remove the searchresults from the session, and the Details Forms
    session.removeAttribute("mergeSearchResult");
    session.removeAttribute("mergeEntityDetails");
    session.removeAttribute("mergeIndividualDetails");
    
    DynaValidatorForm mergeSearchForm = (DynaValidatorForm)form;
    // clean up always unless "useSessionForm" is set.
    if (request.getParameter("useSessionForm") == null)
    {
      mergeSearchForm.initialize(mapping);
	  session.removeAttribute("mergedIndividual");
	  session.removeAttribute("mergedEntity");
	  session.removeAttribute("mergeIdArray");
	  session.removeAttribute("mergeResult");     
    }
    
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // Set up the collections for the drop downs, and stick them on the request.
    ArrayList mergeType = new ArrayList();
    mergeType.add(new DDNameValue("1", "Entities"));
    mergeType.add(new DDNameValue("2", "Individuals"));

    // The search domain list conists of a 0 value for all records
    // And listid values for the marketing lists.
    ArrayList searchDomain = new ArrayList();
    searchDomain.add(new DDNameValue("0", "All Records"));

    // get marketing lists from the EJB
    ArrayList marketingLists = null;
	ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    try
    {
      ContactFacade remoteContactFacade = contactFacadeHome.create();
      remoteContactFacade.setDataSource(dataSource);
      marketingLists = (ArrayList)remoteContactFacade.getAllMarketingList();
    } catch (Exception e) {
		logger.error("[Exception] MergeSearch.Execute Handler ", e);
      	throw new ServletException(e);
    }
    // this should never be null worst case it should be an empty arraylist.
    for(int i = 0; i < marketingLists.size(); i++)
    {
      HashMap currentList = (HashMap)marketingLists.get(i);
      searchDomain.add(new DDNameValue(((Number)currentList.get("listid")).toString(), (String)currentList.get("title")));
    }

    // Get the searchCriteria from the EJB, needs to come from EJB layer for
    // custom fields, otherwise we could just instantiate it.
    SearchCriteriaVO searchCriteria = null;
	MergeHome mergeHome = (MergeHome)CVUtility.getHomeObject("com.centraview.administration.merge.MergeHome", "Merge");
    try
    {
      Merge remoteMerge = mergeHome.create();
      remoteMerge.setDataSource(dataSource);
      searchCriteria = remoteMerge.getSearchCriteriaVO();
    }
    catch (Exception e)
    {
		logger.error("[Exception] MergeSearch.Execute Handler ", e);
      	throw new ServletException(e);
    }
    
    // Check the request parameter to see what type of field list we need to show.
    // Default to entities = 1.  (Individuals = 2)
    String mergeTypeParam = request.getParameter("mergeType");
    String selectedMergeType = mergeTypeParam != null ? mergeTypeParam : "1";
    ArrayList rawFieldList = null;
    if (selectedMergeType.equals("1")) 
    {
      rawFieldList = searchCriteria.getEntityFieldList();
    } else {
      rawFieldList = searchCriteria.getIndividualFieldList();
    } // end if(selectedMergeType.equals("1"))
    // Set the mergeType on the form-bean
    mergeSearchForm.set("mergeType", selectedMergeType);
    // We are now prepared to DDNameValue-up the fieldList
    ArrayList fieldList = new ArrayList();
    for (int i = 0; i < rawFieldList.size(); i++)
    {
      MergeField field = (MergeField)rawFieldList.get(i);
      fieldList.add(new DDNameValue(Integer.toString(i),field.getDescription()));
    } // end while fieldIterator.hasNext()
    
    ArrayList rawSearchType = SearchCriteriaVO.getSearchType();
    // DDNameValue it UP!
    ArrayList searchType = new ArrayList();
    for (int i = 0; i < rawSearchType.size(); i++)
    {
      searchType.add(new DDNameValue(Integer.toString(i), (String)rawSearchType.get(i)));
    }
    
    // We have to grow the Array of SearchCriteriaLines on the formbean
    // based on the property bean property addRow
    SearchCriteriaLine [] currentLine = (SearchCriteriaLine[])mergeSearchForm.get("criteriaLine");
    // see if addrow was set to true.
    String addrow = (String)mergeSearchForm.get("addRow");
    if (addrow.equals("true")) // the Add Row button was clicked.
    {
      SearchCriteriaLine [] tmpCriteria = new SearchCriteriaLine[currentLine.length + 1];
      for (int i =0; i  < tmpCriteria.length; i++)
      {
        tmpCriteria[i] = new SearchCriteriaLine();
      } // end for(...) populating tmpCriteria Array
      System.arraycopy(currentLine, 0, tmpCriteria, 0, currentLine.length);
      mergeSearchForm.set("criteriaLine",tmpCriteria);
      mergeSearchForm.set("addRow", "false");
    } // end if(addrow.equals("true")

    // We have all the collections we need, now throw them on the request
    // Then the struts JSP tags should be able to handle them.
    mergeSearchForm.set("mergeTypeCollection", mergeType);
    mergeSearchForm.set("searchDomainCollection", searchDomain);
    mergeSearchForm.set("fieldListCollection", fieldList);
    mergeSearchForm.set("searchTypeCollection", searchType);
    
    // the merge_c.jsp includes different body sections based on the following attribute.
    request.setAttribute("body","mergesearch");

    return (mapping.findForward(".view.administration.merge_search"));
  } // end execute(...)
}
