/*
 * $RCSfile: KBListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.customer.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.common.menu.MenuBean;
import com.centraview.common.menu.MenuItem;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.knowledgebase.KnowledgebaseList;

public class KBListHandler extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = "customerKBList";

    // set up the left nav menu
    ArrayList menuList = new ArrayList();

    MenuItem ticketMenuItem = new MenuItem("Tickets", "TicketList.do", false);
    menuList.add(ticketMenuItem);
    MenuItem faqMenuItem = new MenuItem("FAQ", "FAQList.do", false);
    menuList.add(faqMenuItem);
    MenuItem kbMenuItem = new MenuItem("Knowledgebase", "KBList.do", true);
    menuList.add(kbMenuItem);

    request.setAttribute("cvMenu", new MenuBean("Support", menuList));

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getIndividualID();    // logged in user's entityID

    // "customerKBListForm" defined in cv-struts-config.xml
    DynaActionForm kbForm = (DynaActionForm)form;

    ActionErrors allErrors = new ActionErrors();

    try
    {
      Integer formCategoryID = (Integer)kbForm.get("categoryID");
      int categoryID = 0;

      if (formCategoryID == null)
      {
        categoryID = 1;
      }else{
        categoryID = formCategoryID.intValue();
      }

      ListPreference listPrefs = userObject.getListPreference("Knowledgebase");

      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      int records = listPrefs.getRecordsPerPage();
      String sortElement = listPrefs.getSortElement();
      boolean customerViewFlag = true;
      KnowledgebaseList kbList = (KnowledgebaseList)lg.getKnowledgebaseList(individualID, 1, records, "", sortElement, categoryID,customerViewFlag);

      Set listkey = kbList.keySet();
      Iterator iter = listkey.iterator();
      while (iter.hasNext())
      {
        String key = (String)iter.next();
        ListElement row = (ListElement)kbList.get(key);

        int elementID = row.getElementID();

        StringMember categoryField = (StringMember)row.get("CatKB");
        String type = categoryField.getDisplayString();

        StringMember nameField = (StringMember)row.get("Name");

        if (type.equals(SupportConstantKeys.CATEGORY))
        {
          // if the list element is a category, then we need to link to KBList.do
          nameField.setRequestURL("KBList.do?categoryID=" + elementID);
          categoryField.setRequestURL("KBList.do?categoryID=" + elementID);
        }else if (type.equals(SupportConstantKeys.KBELEMENT)){
          // if the list element is a KB entry, then we need to link to ViewKB.do
          nameField.setRequestURL("ViewKB.do?kbID=" + elementID);
          categoryField.setRequestURL("ViewKB.do?kbID=" + elementID);
        }
      }   // end while(iter.hasNext())

      kbList.setCurrentCategoryID(categoryID);
      session.setAttribute("displaylist", kbList);
      request.setAttribute("displaylist", kbList);
    }catch(Exception e){
      System.out.println("[Exception][CV KBListHandler] Exception thrown in execute(): " + e);
      allErrors.add("error.unknownError", new ActionMessage("error.unknownError"));
    }

    if (! allErrors.isEmpty())
    {
      saveErrors(request, allErrors);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

