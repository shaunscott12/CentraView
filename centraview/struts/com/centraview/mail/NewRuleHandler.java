/*
 * $RCSfile: NewRuleHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import java.util.TreeSet;

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
import com.centraview.advancedsearch.SearchCriteriaVO;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class NewRuleHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.email.rules.new";

    // "ruleForm", defined in struts-config-email.xml
    DynaActionForm ruleForm = (DynaActionForm)form;

    try {
      // get the account ID from the form bean
      Integer accountID = (Integer)ruleForm.get("accountID");

      // now, check the account ID on the form...
      if (accountID == null || accountID.intValue() <= 0 ) {
        // if account ID is not set on the form, then there is
        // no point in continuing forward. Show user the door. :-)
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Account ID"));
        return(mapping.findForward(forward));
      }

      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      SearchCriteriaVO [] searchCriteria = (SearchCriteriaVO [])ruleForm.get("searchCriteria");

      HashMap conditionMap = SearchVO.getConditionOptions();
      ArrayList conditionList = this.buildSelectOptionList(conditionMap);
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
      ArrayList tableFieldList = this.buildSelectOptionList(tableFields);
      ruleForm.set("fieldList", tableFieldList);

      // See if we should add a row.
      String addRow = (String)ruleForm.get("addRow");
      if(addRow.equals("true")) {
        searchCriteria = this.addRow(searchCriteria);
        ruleForm.set("addRow","false");
      }

      // see if we should delete a row
      String removeRow = (String)ruleForm.get("removeRow");
      if(!removeRow.equals("false")) {
        searchCriteria = this.removeRow(searchCriteria, removeRow);
        ruleForm.set("removeRow","false");
      }

      ruleForm.set("searchCriteria", searchCriteria);

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

    }catch(Exception e){
      System.out.println("[Exception][ViewFolderHandler] Exception thrown in execute(): " + e);
      // TODO: remove stack trace
      e.printStackTrace();
		}
    return(mapping.findForward(forward));
  }   // end execute() method

  /**
   * Takes a hashmap iterates the keys and builds an ArrayList
   * of LabelValueBeans to use in an optioncollection.
   * @param map
   * @return ArrayList of <code>LabelValueBean</code>
   * Where the Label is the Value from the HashMap
   * and the Value is the corresponding Key from the
   * HashMap.
   */
  private ArrayList buildSelectOptionList(HashMap map)
  {
    ArrayList optionList = new ArrayList();
    TreeSet keySet = new TreeSet(map.keySet());
    Iterator keyIterator = keySet.iterator();
    // prepend an empty 0 label to the top of each OptionList
    optionList.add(new LabelValueBean("-- Select --", "0"));
    while(keyIterator.hasNext())
    {
      Number key = (Number)keyIterator.next();
      String value = (String)map.get(key);
      LabelValueBean option = new LabelValueBean(value, key.toString());
      optionList.add(option);
    }
    return optionList;
  } // end buildSelectOptionList(HashMap map)

  private SearchCriteriaVO[] addRow(SearchCriteriaVO[] currentCriteria)
  {
    SearchCriteriaVO [] tmpCriteria = new SearchCriteriaVO[currentCriteria.length + 1];
    // populate the newest one, copy in the rest.
    tmpCriteria[tmpCriteria.length-1] = new SearchCriteriaVO();
    System.arraycopy(currentCriteria, 0, tmpCriteria, 0, currentCriteria.length);
    return tmpCriteria;
  } // end addRow()

  private SearchCriteriaVO[] removeRow(SearchCriteriaVO[] currentCriteria, String rowIndexString)
  {
    int rowIndex = Integer.parseInt(rowIndexString);
    SearchCriteriaVO [] tmpCriteria = new SearchCriteriaVO[currentCriteria.length - 1];

    // squeeze all the elements down.
    int numberMoved = currentCriteria.length - rowIndex - 1;
    if (numberMoved > 0)
    {
        System.arraycopy(currentCriteria, rowIndex+1, currentCriteria, rowIndex, numberMoved);
    }
    // Then copy length-1 elements the squeezed down array into a more
    // appropriately sized array.
    System.arraycopy(currentCriteria, 0, tmpCriteria, 0, tmpCriteria.length);
    return tmpCriteria;
  } // end removeRow()
}   // end class definition

