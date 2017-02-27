/*
 * $RCSfile: RulesListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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


import java.util.Iterator;
import java.util.Set;

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

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.email.RuleList;
import com.centraview.settings.Settings;

public class RulesListHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = "showRulesList";

    // "rulesListForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

	// After performing the logic in the DeleteHanlder, we are generat a new request for the list
	// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
	// Then destory it after getting the Session value
	if (session.getAttribute("listErrorMessage") != null)
	{
		allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
		saveErrors(request, allErrors);
		session.removeAttribute("listErrorMessage");
	}//end of if (session.getAttribute("listErrorMessage") != null)
	
    try
    {
      // get rid of any existing filled-out rulesForm objects
      // that might still be on the session
      session.setAttribute("ruleForm", null);
      // get the account ID from the form bean
      Integer accountID = (Integer)emailForm.get("accountID");

      // now, check the account ID on the form...
      if (accountID == null || accountID.intValue() <= 0 )
      {
        // if accountID was not passed on the form, then
        // try to get the user's default email account ID
        MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
        Mail mailRemote = (Mail)home.create();
        mailRemote.setDataSource(dataSource);
        int defaultAccountID = mailRemote.getDefaultAccountID(individualID);
        if (defaultAccountID > 0)
        {
          accountID = new Integer(defaultAccountID);
        }else{
          // if the user doesn't have a default email account ID,
          // than we can only throw an error :-(
          allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "AccountID"));
          this.saveErrors(request, allErrors);
          accountID = new Integer(0);
        }
      }

      ListPreference listPrefs = userObject.getListPreference("Rule");

      // Paging stuff - we are ALWAYS getting the RuleList object populated from
      // the EJB layer. However, we are chaging the startAt and endAt parameters
      // passed to the ListGenerator when we get that list. By default, startAt is
      // set to 1 and endAt is set to records per page;
      int startAt = 1;
      int endAt = listPrefs.getRecordsPerPage();
      char sortType = 'A';

      // but if NextPageHandler or PreviousPageHandler have been called, then we
      // get the DisplayList that either of those handlers put on the request,
      // and get the startAt and endAt parameters from that object (BUT THROW
      // AWAY THE ACTUAL LIST)
      DisplayList sessionRulesList = (DisplayList)request.getAttribute("displaylist");
      if (sessionRulesList != null)
      {
        startAt = sessionRulesList.getStartAT();
        endAt = sessionRulesList.getEndAT();
        sortType = sessionRulesList.getSortType();
      }

      // now get the list from the ListGenerator
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      String searchString = "";

	  RuleList rulesList = null;
      // If we are setting the Advance Search Flag to True means we already processed the List with Advance Search condition. So we don't need to get the list once again.
	  if (sessionRulesList != null && sessionRulesList.getAdvanceSearchFlag() == true)
	  {
		  rulesList = (RuleList) sessionRulesList;
      }
      else{
      	  rulesList = (RuleList)lg.getRuleList(individualID, accountID.intValue(), startAt, endAt, searchString, listPrefs.getSortElement(),sortType);
	  }

      Set keySet = rulesList.keySet();
      Iterator it =  keySet.iterator();
      while (it.hasNext())
      {
        try
        {
          String key = (String)it.next();
          ListElement ele = (ListElement)rulesList.get(key);
          StringMember nameField = (StringMember)ele.get("Name");
          nameField.setRequestURL("goTo('" + request.getContextPath() + "/mail/ViewRule.do?ruleID=" + ele.getElementID() + "')");
        }catch(Exception e){ }
      }

      session.setAttribute("displaylist", rulesList);
      request.setAttribute("displaylist", rulesList);
      request.setAttribute("showAdvancedSearch", new Boolean(false));
    }catch(Exception e){
      System.out.println("[Exception][RulesListHandler] Exception thrown in execute(): " + e);
      e.printStackTrace();
		}
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

