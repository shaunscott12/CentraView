/*
 * $RCSfile: EditRuleHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

public class EditRuleHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    //String forward = "showRuleDetails";
    String forward = "showRulesList";

    // "ruleForm", defined in struts-config-email.xml
    DynaActionForm ruleForm = (DynaActionForm)form;

    Integer accountID = (Integer)ruleForm.get("accountID");

    try
    {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      Integer ruleID = (Integer)ruleForm.get("ruleID");
      if (ruleID == null || ruleID.intValue() <= 0)
      {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Rule ID"));
      }



      RuleVO oldRuleVO = remote.getRule(ruleID.intValue());
      RuleVO newRuleVO = new RuleVO();

      newRuleVO.setRuleID(ruleID.intValue());
      newRuleVO.setAccountID(accountID.intValue());

      // name - required field
      String ruleName = (String)ruleForm.get("name");
      if (ruleName == null || ruleName.length() <= 0)
      {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Name"));
      }else{
        newRuleVO.setRuleName(ruleName);
      }

      // description - required field
      String description = (String)ruleForm.get("description");
      if (description == null || description.length() <= 0)
      {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Description"));
      }else{
        newRuleVO.setDescription(description);
      }

      // enabled - required field, but default to true if it's not given (shouldn't ever happen)
      Boolean enabled = (Boolean)ruleForm.get("enabled");
      if (enabled == null)
      {
        enabled = new Boolean(true);
      }else{
        newRuleVO.setEnabled(enabled.booleanValue());
      }

      // moveMessage - if true, then folderID is required. Default to false.
      // HACK!!!! Because we're using checkboxes on a DynaActionForm that we
      // are sticking onto the session, we needed to do the following hack:
      // I created a hidden HTML input field on the JSP. It is initially set
      // to the value of "moveMessage" from the database. When the user checks
      // or unchecks the checkbox, then the hidden field is either set to true
      // or false (respectively). Therefore, in this handler we only care about
      // the value of the HIDDEN field, and don't even bother to check the value
      // of the "moveMessage" property of the form bean. This same hack is
      // also used for "markMessageRead and" "deleteMessage"
      String moveMessageParam = (String)request.getParameter("moveMessageHidden");
      boolean moveMessage = false;
      if (moveMessageParam != null && moveMessageParam.equals("true"))
      {
        newRuleVO.setMoveMessage(true);
        moveMessage = true;
      }else{
        newRuleVO.setMoveMessage(false);
      }

      // folderID = required only if moveMessage == true
      Integer folderID = (Integer)ruleForm.get("folderID");
      if (moveMessage == true && (folderID == null || folderID.intValue() <= 0))
      {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You must choose a destination when moving a message."));
      }else{
        newRuleVO.setFolderID(folderID.intValue());
      }

      // markMessageRead - Default to false. NOTE THE HACK (explained above)!!!
      String markMessageReadParam = (String)request.getParameter("markMessageReadHidden");
      boolean markMessageRead = false;
      if (markMessageReadParam != null && markMessageReadParam.equals("true"))
      {
        newRuleVO.setMarkMessageRead(true);
        markMessageRead = true;
      }else{
        newRuleVO.setMarkMessageRead(false);
      }

      // deleteMessage - Default to false. NOTE THE HACK (explained above)!!!
      String deleteMessageParam = (String)request.getParameter("deleteMessageHidden");
      boolean deleteMessage = false;
      if (deleteMessageParam != null && deleteMessageParam.equals("true"))
      {
        newRuleVO.setDeleteMessage(true);
        deleteMessage = true;
      }else{
        newRuleVO.setDeleteMessage(false);
      }

      // At least one of moveMessage OR deleteMessage OR markMessageRead MUST be selected
      if (deleteMessage == false && moveMessage == false && markMessageRead == false)
      {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You must select at least one Action."));
      }


      // search criteria - get the List of SearchCriteriaVO's from the form
      // and check to see if there is at least ONE valid criteria. If not,
      // throw an error.
      List searchCriteria = Arrays.asList((SearchCriteriaVO[])ruleForm.get("searchCriteria"));
      if (searchCriteria == null || searchCriteria.isEmpty())
      {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You must supply at least one Criteria,"));
      }else{
        Iterator iter = searchCriteria.iterator();
        int count = 1;
        while (iter.hasNext())
        {
          SearchCriteriaVO criteria = (SearchCriteriaVO)iter.next();
          if (criteria == null || criteria.getValue() == null || (criteria.getValue()).length() <= 0)
          {
            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "The Criteria field cannot be blank. (line " + count + ")"));
          }else{
            RuleCriteriaVO ruleCriteriaVO = new RuleCriteriaVO();
            ruleCriteriaVO.setRuleID(ruleID.intValue());
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
            newRuleVO.addRuleCriteria(ruleCriteriaVO);
          }
          count++;
        }
      }

      if (! allErrors.isEmpty())
      {
        this.saveErrors(request, allErrors);
        return(mapping.findForward("errorOccurred"));
      }

      // THIS IS WHERE WE CALL THE EJB TO UPDATE THE RULE
      int numberChanged = remote.editRule(individualID, newRuleVO);
	  session.removeAttribute("ruleForm");
    }catch(Exception e){
      System.out.println("[Exception][EditRuleHandler] Exception thrown in execute(): " + e);
      // TODO: remove stack trace
      e.printStackTrace();
		}

    StringBuffer returnPath = new StringBuffer(mapping.findForward(forward).getPath());
    if (accountID != null && accountID.intValue() > 0)
    {
      returnPath.append("?accountID="+accountID.intValue());
    }
    return(new ActionForward(returnPath.toString(), true));

  }   // end execute() method

}   // end class definition

