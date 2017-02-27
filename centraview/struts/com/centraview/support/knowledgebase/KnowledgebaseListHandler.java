/*
 * $RCSfile: KnowledgebaseListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:50 $ - $Author: mking_cv $
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

package com.centraview.support.knowledgebase;

import java.util.Iterator;
import java.util.Set;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;

/**
 * KnowledgebaseListHandler.java
 */
public class KnowledgebaseListHandler extends Action
{
  private static final String FORWARD_listkb = "listknowledgebase";

  /**
   * Fetches the details of the list and forwards the request to the jsp to
   * display
   * 
   * @param mapping
   * @param form
   * @param request
   * @param response
   * @return ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";

    int requestCategoryID = 1;
    HttpSession session = request.getSession(true);
    UserObject userobjectd = (UserObject)session.getAttribute("userobject");
    int individualID = userobjectd.getIndividualID();

    ListPreference listpreference = userobjectd.getListPreference("Knowledgebase");
    KnowledgebaseList displaylistSession = null;
    try
    {
      displaylistSession = (KnowledgebaseList)session.getAttribute("displaylist");
      //System.out.println(" displaylistSession "+displaylistSession);
    } catch (Exception e) {
      System.out.println("[Exception] KnowledgebaseListHandler.execute: " + e.toString());
    }

    // After performing the logic in the DeleteHanlder, we are generat a new
    // request for the list
    // So we will not be carrying the old error. So that we will try to collect
    // the error from the Session variable
    // Then destory it after getting the Session value
    if (session.getAttribute("listErrorMessage") != null)
    {
      ActionErrors allErrors = (ActionErrors)session.getAttribute("listErrorMessage");
      saveErrors(request, allErrors);
      session.removeAttribute("listErrorMessage");
    }

    KnowledgebaseList displaylist = null;
    try
    {
      displaylist = (KnowledgebaseList)request.getAttribute("displaylist");
    } catch (Exception e) {
      System.out.println("[Exception] KnowledgebaseListHandler.execute: " + e.toString());
    }

    if (request.getParameter("rowId") != null)
    {
      String rowID = (String)request.getParameter("rowId");
      int indexRowID = rowID.indexOf("#");
      if (indexRowID == -1)
      {
        requestCategoryID = (Integer.parseInt((String)request.getParameter("rowId")));
      }
      else if (displaylistSession != null) {
        requestCategoryID = displaylistSession.getCurrentCategoryID();
      } else {
        requestCategoryID = Integer.parseInt(rowID.substring(0, indexRowID));
      }
    } else if (displaylistSession != null) {
      requestCategoryID = displaylistSession.getCurrentCategoryID();
    }

    KnowledgebaseList DL = null;
    if (displaylist == null)
    {
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      int records = listpreference.getRecordsPerPage();
      String sortelement = listpreference.getSortElement();
      // If we are not coming from customer View List handler than we must have
      // to set the as false.
      // Otherwise true.
      boolean customerViewFlag = false;
      DL = (KnowledgebaseList)lg.getKnowledgebaseList(individualID, 1, records, "", sortelement, requestCategoryID, customerViewFlag);
      DL = setLinksfunction(DL);
      DL.setCurrentCategoryID(requestCategoryID);
    } else {
      String searchSession = displaylistSession.getSearchString();
      String searchrequest = displaylist.getSearchString();
      if (searchSession == null)
      {
        searchSession = "";
      }
      if (searchrequest == null)
      {
        searchrequest = "";
      }

      if (((displaylistSession.getCurrentCategoryID() == displaylist.getCurrentCategoryID()) && (displaylistSession.getListID() == displaylist.getListID()) && (displaylist.getDirtyFlag() == false) && (displaylist.getStartAT() >= displaylistSession.getBeginIndex()) && (displaylist.getEndAT() <= displaylistSession.getEndIndex())
          && (displaylist.getSortMember().equals(displaylistSession.getSortMember())) && (displaylist.getSortType() == (displaylistSession.getSortType()) && (searchSession.equals(searchrequest))))
          || displaylist.getAdvanceSearchFlag() == true)
      {
        DL = (KnowledgebaseList)displaylistSession;
        request.setAttribute("displaylist", displaylistSession);
      } else {
        ListGenerator lg = ListGenerator.getListGenerator(dataSource);
        DL = (KnowledgebaseList)lg.getKnowledgebaseList(individualID, requestCategoryID, displaylist);
        DL.setCurrentCategoryID(requestCategoryID);
      }
      DL = setLinksfunction(DL);
    }
    session.setAttribute("displaylist", DL);
    request.setAttribute("displaylist", DL);
    request.setAttribute(SupportConstantKeys.TYPEOFSUBMODULE, "Knowledgebase");
    returnStatus = FORWARD_listkb;

    return (mapping.findForward(returnStatus));
  }

  /**
   * This function sets links on members
   * 
   * @param DL
   * @return KnowledgebaseList object
   */
  public KnowledgebaseList setLinksfunction(KnowledgebaseList DL)
  {
    Set listkey = DL.keySet();
    Iterator it = listkey.iterator();
    while (it.hasNext())
    {
      String str = (String)it.next();
      StringMember smFF = null;
      StringMember sm = null;
      ListElement ele = (ListElement)DL.get(str);
      int elementID = ele.getElementID();
      smFF = (StringMember)ele.get("CatKB");
      sm = (StringMember)ele.get("Name");
      if ((smFF.getDisplayString()).equals(SupportConstantKeys.CATEGORY))
      {
        sm.setRequestURL("ListKnowledgebase.do?rowId=" + elementID + "&listId=" + DL.getListID());
        smFF.setRequestURL("ListKnowledgebase.do?rowId=" + elementID + "&listId=" + DL.getListID());
      } else if ((smFF.getDisplayString()).equals(SupportConstantKeys.KBELEMENT)) {
        sm.setRequestURL("/centraview/ViewKnowledgeBase.do?rowId=" + elementID + "&listId=" + DL.getListID());
        smFF.setRequestURL("ListKnowledgebase.do?rowId=" + elementID + "&listId=" + DL.getListID());
      }
    }
    return DL;
  }
}
