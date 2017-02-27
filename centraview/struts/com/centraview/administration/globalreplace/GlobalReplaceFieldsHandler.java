/*
 * $RCSfile: GlobalReplaceFieldsHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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

package com.centraview.administration.globalreplace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

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

import com.centraview.advancedsearch.AdvancedSearch;
import com.centraview.advancedsearch.AdvancedSearchHome;
import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Collects the table information and search which should apply on the table.
 * We must build the searchVO according the criteria which was selected by the user.
 * We will execute the advance search by passing the searchVO, then it will return the displaylist.
 * @author Naresh Patel <npatel@centraview.com>
 */
public class GlobalReplaceFieldsHandler extends Action
{
  // TODO I have removed the DisplayList (non-valuelist) Advanced Search call.  So this is broken.
  private static Logger logger = Logger.getLogger(GlobalReplaceFieldsHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException,NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession(true);

      // Need the userobject so we know who we are.
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualId = userObject.getIndividualID();

      DynaActionForm globalReplaceForm = (DynaActionForm)form;

      //Collect the Table information 
      String replaceTableIDString = (String)globalReplaceForm.get("replaceTableID");
      int replaceTableID = 0;
      int moduleId = 14;
      if (replaceTableIDString != null) {
        StringTokenizer tableInfo = new StringTokenizer(replaceTableIDString, "#");
        String tableIDString = null;
        String primaryTableName = null;
        if (tableInfo != null) {
          while (tableInfo.hasMoreTokens()) {
            tableIDString = tableInfo.nextToken();
            primaryTableName = tableInfo.nextToken();
          }
          
          if (tableIDString != null && primaryTableName != null && !tableIDString.equals("") && !primaryTableName.equals("")) {
            replaceTableID =  Integer.parseInt(tableIDString);
            String moduleIdString = AdvancedSearchUtil.getModuleId(primaryTableName, dataSource);
            if (moduleIdString != null) {
              moduleId = Integer.parseInt(moduleIdString);
            }
          }
        }
      }

      //intialize the Global Search EJB
      GlobalReplace globalReplace = null;
      GlobalReplaceHome globalReplaceHome = (GlobalReplaceHome)CVUtility.getHomeObject("com.centraview.administration.globalreplace.GlobalReplaceHome", "GlobalReplace");
      try {
        globalReplace = globalReplaceHome.create();
        globalReplace.setDataSource(dataSource);
      } catch (Exception e) {
        logger.error("[Exception] GlobalReplaceHandler.Execute Handler ", e);
      }

      // We must get the fields which are related to the selected table
      Vector primaryReplaceFieldsList = globalReplace.getReplaceTableFields(replaceTableID);
      globalReplaceForm.set("replaceFieldVec",primaryReplaceFieldsList);

      // Get the information of the Selected Field by the User
      // parse out of the field Type (lookup, text, select, etc.,)
      String replaceFieldIDString = (String)globalReplaceForm.get("replaceFieldID");
      int fieldTableID = 0;
      int replaceFieldID = 0;
      int replaceFieldType = 0;
      if (replaceFieldIDString != null) {
        StringTokenizer fieldInfo = new StringTokenizer(replaceFieldIDString, "*");
        String fieldIDString = null;
        String tableIDString = null;
        String fieldTypeString = null;
        if (fieldInfo != null) {
          while (fieldInfo.hasMoreTokens()) {
            tableIDString = fieldInfo.nextToken();
            fieldIDString = fieldInfo.nextToken();
            fieldTypeString = fieldInfo.nextToken();
          }
          
          if (fieldIDString != null && fieldTypeString != null && !fieldIDString.equals("") && !fieldTypeString.equals("")) {
            fieldTableID = Integer.parseInt(tableIDString);
            replaceFieldID = Integer.parseInt(fieldIDString);
            replaceFieldType =  Integer.parseInt(fieldTypeString);
          }
        }
      }

      //If its a mulitple Selection type
      if (replaceFieldType == GlobalReplaceConstantKeys.FIELD_TYPE_MULTIPLE) {
        Vector replaceVec = globalReplace.getFieldValues(fieldTableID,replaceFieldID);
        globalReplaceForm.set("replaceVec",replaceVec);
      }



      // START HERE?

      String marketingListID = (String)globalReplaceForm.get("listID");
      String displayListFlag = (String) request.getAttribute("displayListFlag");
      DisplayList displaylistRequest = (DisplayList)request.getAttribute("displaylist");
      
      if ((displayListFlag != null && displayListFlag.equals("true")) || displaylistRequest == null ) {
        // Get Advance Search result by applying the Search criteria
        AdvancedSearch remoteAdvancedSearch = null;
        AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
        try {
          remoteAdvancedSearch = advancedSearchHome.create();
          remoteAdvancedSearch.setDataSource(dataSource);
        } catch (Exception e) {
          logger.error("[Exception] GlobalReplaceHandler.Execute Handler ", e);
        }
        
        // If the user selecte the New Search then build Search VO.
        // Else pass the searchId to the EJB and get the searchVO.
        SearchVO searchObject = new SearchVO();
        String searchType = (String)globalReplaceForm.get("searchType");
        if (searchType != null && searchType.equals("New Search")) {
          searchObject.setModuleID(moduleId);
          searchObject.setName("New Search");
          List searchCriteria = Arrays.asList((SearchCriteriaVO[])globalReplaceForm.get("searchCriteria"));
          searchObject.setSearchCriteria(searchCriteria);
        } else {
          String savedSearchID = (String)globalReplaceForm.get("savedSearchID");
          if (!(savedSearchID.equals("0") || savedSearchID.equals(""))) {
            searchObject = remoteAdvancedSearch.getSavedSearch(Integer.parseInt(savedSearchID),"ADVANCE",null);
          }
        }

        // Add an extra search Criteria on User selected criteria
        // The Criteria is defined so that user is categorizing the search should be applied to 
        // Marketing List 1 or 2 etc.,
        // If the user selects he wants to apply to all Records then we will not add the criteria
        if (!(marketingListID.equals("0") || marketingListID.equals(""))) {
          int fieldID = globalReplace.getSearchFieldID(replaceTableID);
          if (fieldID != -1) {
            SearchCriteriaVO searchCriteriaVO = new SearchCriteriaVO();
            searchCriteriaVO.setTableID(replaceTableID+"");
            searchCriteriaVO.setFieldID(replaceTableID+"");
            searchCriteriaVO.setConditionID((SearchVO.EQUALS_INTEGER).toString());
            searchCriteriaVO.setExpressionType(GlobalReplaceConstantKeys.SEARCH_AND_OR);
            searchCriteriaVO.setValue(marketingListID);
            searchObject.addSearchCriteria(searchCriteriaVO);
          }
        }

        session.setAttribute("searchObject",searchObject);
        ArrayList resultsIDs = new ArrayList();
        resultsIDs.addAll(remoteAdvancedSearch.performSearch(individualId,searchObject));
        request.setAttribute("displayListFlag","false");
      }

      globalReplaceForm.set("replaceValue","");
      globalReplaceForm.set("replaceID","");
      globalReplaceForm.set("replaceExt","");
      globalReplaceForm.set("fieldType", replaceFieldType + "");

    } catch (Exception e) {
      logger.error("[Exception] GlobalReplaceHandler.Execute Handler ", e);
    }
    return mapping.findForward(".view.administration.global_replace.fields");
  } //end of execute method
}
