/*
 * $RCSfile: EmailListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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
package com.centraview.email;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;

/**
 * Insert the type's description here. Creation date: (14/07/2003 )
 *
 * @author   Shirish D
 */
public class EmailListHandler extends org.apache.struts.action.Action
{

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();


    try
    {
      HttpSession session = request.getSession(true);
      session.setAttribute("highlightmodule", "email");

      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      ListPreference listpreference = userobjectd.getListPreference("Email");

      DisplayList displaylistSession = (DisplayList)session.getAttribute("displaylist");
      DisplayList displaylist = (DisplayList)request.getAttribute("displaylist");

      String deleteFlag = (String)request.getAttribute("deleteFlag");

//      System.out.println("displaylist"+displaylist);
      // Folder List

      EmailFacadeHome aa = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)aa.create();
      remote.setDataSource(dataSource);
      int individualID = userobjectd.getIndividualID();
      FolderList fl = (FolderList)remote.getFolderList(individualID);

      String[] maps = mapping.findForwards();
      if (fl == null)
      {
        return (mapping.findForward("displaynomailaccount"));
      }
      else if (fl.size() == 0)
      {
        return (mapping.findForward("displaynomailaccount"));
      }

      session.setAttribute("folderlist", fl);

      int folderid = fl.getDefaultFolder();

      System.out.println("folderid"+folderid);

      if (request.getParameter("folderid") != null)
      {
        folderid = Integer.parseInt(request.getParameter("folderid"));
      }
      if (request.getParameter("folderid") == null)
      {
		  System.out.println("request.getAttribute(folderid)--->"+request.getAttribute("folderid"));
		 if(request.getAttribute("folderid") != null){
    		folderid = Integer.parseInt((String)request.getAttribute("folderid"));
		 }
      }


      //End Folder List
      session.setAttribute("folderid", "" + folderid);

      EmailList DL = null;
      if (displaylist == null)
      {
        ListGenerator lg = ListGenerator.getListGenerator(dataSource);
        DL = (EmailList)lg.getEmailList(individualID, 1, listpreference.getRecordsPerPage(), "", listpreference.getSortElement(),'D', folderid, true);
	  }
      else
      {
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

        if ((displaylistSession.getListID() == displaylist.getListID())
          && (displaylist.getDirtyFlag() == false)
          && (searchSession.equals(searchrequest))
          && (displaylist.getStartAT() >= displaylistSession.getBeginIndex())
          && (displaylist.getEndAT() <= displaylistSession.getEndIndex())
          && (displaylist.getSortMember().equals(displaylistSession.getSortMember())
          && (displaylist.getSortType() == (displaylistSession.getSortType()))
          && deleteFlag == null))
        {
          DL = (EmailList)displaylistSession;
        }
        else
        {
          ListGenerator lg = ListGenerator.getListGenerator(dataSource);
          DL = (EmailList)lg.getEmailList(individualID, displaylist, folderid, true);
        }
      }

      Set listkey = DL.keySet();
      Iterator it = listkey.iterator();
      while (it.hasNext())
      {
        String str = (String)it.next();
        ListElement ele = (ListElement)DL.get(str);
        StringMember sm = (StringMember)ele.get("Subject");

        if ((request.getParameter("folderName") != null) && (request.getParameter("folderName")).equals("Drafts"))
        {
          sm.setRequestURL("openPopup('ComposeEmailHandler.do?action=Drafts&rowId=" + ele.getElementID() + "')");
        }

        else
        {
          sm.setRequestURL("goTo('ViewMailHandler.do?rowId=" + ele.getElementID() + "&listId=" + DL.getListID() + "&index=" + str + "&folderid=" + folderid + "')");
        }
        sm = (StringMember)ele.get("To");

        String ComposeTo = sm.getDisplayString();

        ComposeTo = ComposeTo.replaceAll("\""," ");
        ComposeTo = ComposeTo.replaceAll("'"," ");
		ComposeTo = ComposeTo.trim();

        sm.setRequestURL("openPopup('ComposeEmailHandler.do?composeTo=" + ComposeTo + "')");

        sm = (StringMember)ele.get("From");
        String ComposeFrom = sm.getDisplayString();
        ComposeFrom = ComposeFrom.replaceAll("\""," ");
		ComposeFrom = ComposeFrom.replaceAll("'"," ");
		ComposeFrom = ComposeFrom.trim();

        sm.setRequestURL("openPopup('ComposeEmailHandler.do?composeTo=" + ComposeFrom + "')");

      }
      session.setAttribute("displaylist", DL);
      request.setAttribute("displaylist", DL);
      request.setAttribute("list", "Email");
    }
    catch (Exception e)
    {
      System.out.println("[Exception][EmailListHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward("showemaillist"));
  }
}
