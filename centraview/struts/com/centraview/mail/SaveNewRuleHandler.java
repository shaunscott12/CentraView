/*
 * $RCSfile: SaveNewRuleHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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

import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveNewRuleHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = "showRulesList";
    String errorForward = "errorOccurred";

    // "ruleForm", defined in struts-config-email.xml
    DynaActionForm ruleForm = (DynaActionForm)form;

    Integer accountID = new Integer(0);

    try {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      RuleVO ruleVO = new RuleVO();

      // name - required field
      String ruleName = (String)ruleForm.get("name");
      if (ruleName == null || ruleName.length() <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Name"));
      }else{
        ruleVO.setRuleName(ruleName);
      }

      // description - required field
      String description = (String)ruleForm.get("description");
      if (description == null || description.length() <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Description"));
      }else{
        ruleVO.setDescription(description);
      }

      // enabled - required field, but default to true if it's not given (shouldn't ever happen)
      Boolean enabled = (Boolean)ruleForm.get("enabled");
      if (enabled == null) {
        enabled = new Boolean(true);
      }else{
        ruleVO.setEnabled(enabled.booleanValue());
      }

      // accountID - required field. But user can't fix the error if it's not supplied :-(
      accountID = (Integer)ruleForm.get("accountID");
      if (accountID == null || accountID.intValue() <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Account ID"));
      }else{
        ruleVO.setAccountID(accountID.intValue());
      }

      // moveMessage - if true, then folderID is required. Default to false.
      Boolean moveMessage = (Boolean)ruleForm.get("moveMessage");
      if (moveMessage == null) {
        moveMessage = new Boolean(false);
      }else{
        ruleVO.setMoveMessage(moveMessage.booleanValue());
      }

      // folderID = required only if moveMessage == true
      Integer folderID = (Integer)ruleForm.get("folderID");
      if (moveMessage.booleanValue() == true) {
        if (folderID == null || folderID.intValue() <= 0) {
          allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You must choose a destination when moving a message."));
        }else{
          ruleVO.setFolderID(folderID.intValue());
        }
      }

      // markMessageRead - Default to false.
      Boolean markMessageRead = (Boolean)ruleForm.get("markMessageRead");
      if (markMessageRead == null) {
        markMessageRead = new Boolean(false);
      }else{
        ruleVO.setMarkMessageRead(markMessageRead.booleanValue());
      }

      // deleteMessage - Default to false.
      Boolean deleteMessage = (Boolean)ruleForm.get("deleteMessage");
      if (deleteMessage == null) {
        deleteMessage = new Boolean(false);
      }else{
        ruleVO.setDeleteMessage(deleteMessage.booleanValue());
      }

      // At least one of moveMessage OR deleteMessage OR markMessageRead MUST be selected
      if (deleteMessage.booleanValue() == false && moveMessage.booleanValue() == false && markMessageRead.booleanValue() == false) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You must select at least one Action."));
      }

      // search criteria - get the List of SearchCriteriaVO's from the form
      // and check to see if there is at least ONE valid criteria. If not,
      // throw an error.
      List searchCriteria = Arrays.asList((SearchCriteriaVO[])ruleForm.get("searchCriteria"));
      if (searchCriteria == null || searchCriteria.isEmpty()) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You must supply at least one Criteria,"));
      }else{
        Iterator iter = searchCriteria.iterator();
        int count = 1;
        while (iter.hasNext()) {
          SearchCriteriaVO criteria = (SearchCriteriaVO)iter.next();
          if (criteria == null || criteria.getValue() == null || (criteria.getValue()).length() <= 0) {
            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "The Criteria field cannot be blank. (line " + count + ")"));
          }else{
            RuleCriteriaVO ruleCriteriaVO = new RuleCriteriaVO();
            ruleCriteriaVO.setOrderID(count);
            ruleCriteriaVO.setExpressionType(criteria.getExpressionType());

            try{
              ruleCriteriaVO.setFieldID(Integer.parseInt(criteria.getFieldID()));
            }catch(NumberFormatException nfe){
              ruleCriteriaVO.setFieldID(-1);
            }

            try{
              ruleCriteriaVO.setConditionID(Integer.parseInt(criteria.getConditionID()));
            }catch(NumberFormatException nfe){
              ruleCriteriaVO.setConditionID(4);
            }

            ruleCriteriaVO.setValue(criteria.getValue());
            ruleVO.addRuleCriteria(ruleCriteriaVO);
          }
          count++;
        }
      }

      // if there are any errors at this point, then don't attempt to add the
      // rule to the database, just give the user the errors
      if (! allErrors.isEmpty()) {
        this.saveErrors(request, allErrors);
        return(mapping.findForward(errorForward));
      }

      // if everything's cool, then add the rule to the database...
      int newRuleID = remote.addRule(individualID, ruleVO);
      session.removeAttribute("ruleForm");
    }catch(Exception e){
      System.out.println("[Exception][ViewFolderHandler] Exception thrown in execute(): " + e);
      // TODO: remove stack trace
      e.printStackTrace();
      forward = errorForward;
		}

    StringBuffer returnPath = new StringBuffer(mapping.findForward(forward).getPath());
    if (accountID != null && accountID.intValue() > 0) {
      returnPath.append("?accountID="+accountID.intValue());
    }
    return(new ActionForward(returnPath.toString(), true));
  }   // end execute() method

}   // end class definition

