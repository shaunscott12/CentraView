/*
 * $RCSfile: MarketingListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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

package com.centraview.marketing ;

import java.io.IOException;
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

import com.centraview.common.CVUtility;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Handles list action for display of entity list.
 * @author CentraView, LLC.
 */
public class MarketingListHandler extends org.apache.struts.action.Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession(true);

      if (session.getAttribute("highlightmodule") != null)
      {
				session.setAttribute("highlightmodule", "marketing");
      }

      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int userid = userobjectd.getIndividualID();
      int individualId = userobjectd.getIndividualID();
      ListPreference listpreference = userobjectd.getListPreference("Marketing");

      MarketingList displaylistSession = null;
      MarketingList displaylist = null;

	  // After performing the logic in the DeleteHanlder, we are generat a new request for the list
	  // So we will not be carrying the old error. So that we will try to collect the error from the Session variable
	  // Then destory it after getting the Session value
	  if (session.getAttribute("listErrorMessage") != null)
	  {
		  ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
		  saveErrors(request, allErrors);
		  session.removeAttribute("listErrorMessage");
	  }//end of if (session.getAttribute("listErrorMessage") != null)
	  
      try
      {
        displaylistSession = (MarketingList)session.getAttribute("displaylist");
      }catch(ClassCastException e){
        displaylistSession = null;
      }

      try
      {
        displaylist = (MarketingList)request.getAttribute("displaylist");
      }catch(ClassCastException e){
        displaylist = null;
      }


      MarketingList DL = null;

      if (displaylist == null)
      {
        ListGenerator lg = ListGenerator.getListGenerator(dataSource);
        int records = listpreference.getRecordsPerPage();
        String sortelement = listpreference.getSortElement();
        DL = (MarketingList)lg.getMarketingList(individualId, 1, records, "", sortelement);
      }else {
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

        if (((displaylistSession.getListID() == displaylist.getListID()) &&
            (displaylist.getDirtyFlag() == false) &&
            (displaylist.getStartAT() >= displaylistSession.getBeginIndex()) &&
            (displaylist.getEndAT() <= displaylistSession.getEndIndex()) &&
            (displaylist.getSortMember().equals(displaylistSession.getSortMember())) &&
            (displaylist.getSortType() == (displaylistSession.getSortType())) &&
            (searchSession.equals(searchrequest)))|| displaylist.getAdvanceSearchFlag() == true)
        {
		  DL = (MarketingList) displaylistSession;
          request.setAttribute("displaylist" , displaylistSession );
        }else{
          ListGenerator lg = ListGenerator.getListGenerator(dataSource);
          DL = (MarketingList)lg.getMarketingList(individualId, displaylist);
        }
      }

      Set listkey = DL.keySet();
      Iterator it =  listkey.iterator();

      while (it.hasNext())
      {
        String str = (String)it.next();
        ListElement ele = (ListElement)DL.get(str);
        StringMember sm = (StringMember)ele.get("title");
        sm.setRequestURL("goTo('ViewListHandler.do?rowId=" + ele.getElementID() + "&listId=" + DL.getListID() + "')");
      }

      DL.setPrimaryMember("title");
      if (request.getParameter("columnname") != null)
      {
        DL.setSortMember(request.getParameter("columnname"));
      }

      request.setAttribute("showDuplicateButton", new Boolean(false));

      session.setAttribute( "displaylist" , DL );
      request.setAttribute("displaylist" , DL );
      request.setAttribute("list" , "Marketing" );
      request.setAttribute("TypeOfOperation","List Manager");
    }catch(Exception e){
      System.out.println("[Exception][MarketingListHandler] Exception thrown in execute(): " + e);
      e.printStackTrace();
      return(mapping.findForward("failure"));
    }
    return(mapping.findForward("displayMarketingList"));
  }   // end execute()

}

