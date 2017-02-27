/*
 * $RCSfile: ViewRuleHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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

import com.centraview.advancedsearch.AdvancedSearch;
import com.centraview.advancedsearch.AdvancedSearchHome;
import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class ViewRuleHandler extends org.apache.struts.action.Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.email.rules.view";

    // "ruleForm", defined in struts-config-email.xml
    DynaActionForm ruleForm = (DynaActionForm)form;

    try {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      Integer ruleID = (Integer)ruleForm.get("ruleID");
      if (ruleID == null || ruleID.intValue() <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Rule ID"));
      }

      RuleVO ruleVO = remote.getRule(ruleID.intValue());

      if (ruleVO == null) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.noInfoReturned", "Rule information"));
      }

      Integer accountID = new Integer(ruleVO.getAccountID());
      ruleForm.set("accountID", accountID);
      ruleForm.set("name", ruleVO.getRuleName());
      ruleForm.set("description", ruleVO.getDescription());
      ruleForm.set("enabled", new Boolean(ruleVO.isEnabled()));
      ruleForm.set("moveMessage", new Boolean(ruleVO.isMoveMessage()));
      ruleForm.set("folderID", new Integer(ruleVO.getFolderID()));
      ruleForm.set("markMessageRead", new Boolean(ruleVO.isMarkMessageRead()));
      ruleForm.set("deleteMessage", new Boolean(ruleVO.isDeleteMessage()));

      ArrayList criteriaList = ruleVO.getRuleCriteria();
      if (criteriaList != null) {
        SearchCriteriaVO [] searchCriteria = new SearchCriteriaVO[criteriaList.size()];
        Iterator crIter = criteriaList.iterator();
        while (crIter.hasNext()) {
          RuleCriteriaVO criteriaVO = (RuleCriteriaVO)crIter.next();
          SearchCriteriaVO searchCriteriaVO = new SearchCriteriaVO();
          searchCriteriaVO.setExpressionType(criteriaVO.getExpressionType());
          searchCriteriaVO.setFieldID(String.valueOf(criteriaVO.getFieldID()));
          searchCriteriaVO.setConditionID(String.valueOf(criteriaVO.getConditionID()));
          searchCriteriaVO.setValue(criteriaVO.getValue());
          searchCriteria[criteriaVO.getOrderID() - 1] = searchCriteriaVO;
        }

        AdvancedSearchUtil util = new AdvancedSearchUtil();
        // See if we should add a row.
        String addRow = (String)ruleForm.get("addRow");
        if (addRow.equals("true")) {
          searchCriteria = util.addRow(searchCriteria);
          ruleForm.set("addRow", "false");
        }

        // see if we should delete a row
        String removeRow = (String)ruleForm.get("removeRow");
        if (! removeRow.equals("false")) {
          searchCriteria = util.removeRow(searchCriteria, removeRow);
          ruleForm.set("removeRow", "false");
        }
        ruleForm.set("searchCriteria", searchCriteria);
      }


      HashMap conditionMap = SearchVO.getConditionOptions();
      ArrayList conditionList = AdvancedSearchUtil.buildSelectOptionList(conditionMap);
      ruleForm.set("conditionList", conditionList);

      // Get Saved search EJB
      AdvancedSearch remoteAdvancedSearch = null;
      try {
        AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome)CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome", "AdvancedSearch");
        remoteAdvancedSearch = advancedSearchHome.create();
        remoteAdvancedSearch.setDataSource(dataSource);
      }catch(Exception e){
        System.out.println("[Exception][SearchForm.execute] Exception Thrown getting EJB connection: " + e);
        throw new ServletException(e);
      }

      final int emailTableID = 33;
      final int emailModuleID = 79;

      HashMap tableFields = remoteAdvancedSearch.getSearchFieldsForTable(individualID, emailTableID, emailModuleID);
      ArrayList tableFieldList = AdvancedSearchUtil.buildSelectOptionList(tableFields);
      ruleForm.set("fieldList", tableFieldList);

      // get the user's list of folders for this account
      HashMap folderList = remote.getFolderList(accountID.intValue());

      // remove the "root" folder from the list
      Set keySet = folderList.keySet();
      Iterator iter = keySet.iterator();
      while (iter.hasNext()) {
        Number key = (Number)iter.next();
        String value = (String)folderList.get(key);
        if (value.equals("root")) {
          iter.remove();
        }
      }
      ruleForm.set("folderList", folderList);


      if (! allErrors.isEmpty()) {
        this.saveErrors(request, allErrors);
      }

    }catch(Exception e){
      System.out.println("[Exception][ViewFolderHandler] Exception thrown in execute(): " + e);
      //e.printStackTrace();
		}
    return(mapping.findForward(forward));

  }   // end execute() method

}   // end class definition

