/*
 * $RCSfile: ProjectViewHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:29 $ - $Author: mking_cv $
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
package com.centraview.projects.project;

import java.io.IOException;
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

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.ProjectList;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * @author Shekhar Chiplunkar
 */
public class ProjectViewHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String returnStatus = "failure";
    try
    {
      HttpSession session = request.getSession(true);

      if (session.getAttribute("highlightmodule") != null)
      {
        session.setAttribute("highlightmodule", "project");
      }

	  // After performing the logic in the DeleteHanlder, we are generat a new request for the list
	  // So we will not be carrying the old error. So that we will try to collect the error from the Session variable
	  // Then destory it after getting the Session value
	  if (session.getAttribute("listErrorMessage") != null)
	  {
		  ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
		  saveErrors(request, allErrors);
		  session.removeAttribute("listErrorMessage");
	  }//end of if (session.getAttribute("listErrorMessage") != null)
	  
      UserObject userobjectd =(UserObject) session.getAttribute("userobject");
      ListPreference listpreference = userobjectd.getListPreference("Project");

      DisplayList displaylistSession = (DisplayList) session.getAttribute("displaylist");
      DisplayList displaylist = (DisplayList) request.getAttribute("displaylist");
      ProjectList DL = null;

      if (displaylist == null)
      {
        ListGenerator lg = ListGenerator.getListGenerator(dataSource);
        DL = (ProjectList) lg.getProjectList(userobjectd.getIndividualID(), 1,
            listpreference.getRecordsPerPage(), "", listpreference.getSortElement());
      } //end of if statement (displaylist == null)
      else
      {
        String searchSession = displaylistSession.getSearchString();
        String searchrequest = displaylist.getSearchString();

        if (searchSession == null)
        {
          searchSession = "";
        } //end of if statement (searchSession == null)

        if (searchrequest == null)
        {
          searchrequest = "";
        } //end of if statement (searchrequest == null)

        if (((displaylistSession.getListID() == displaylist.getListID())
            && (displaylist.getDirtyFlag() == false)
            && (displaylist.getStartAT() >= displaylistSession.getBeginIndex())
            && (displaylist.getEndAT() <= displaylistSession.getEndIndex())
            && (displaylist.getSortMember().equals(displaylistSession.getSortMember())
            && (displaylist.getSortType() == (displaylistSession.getSortType()))
            && (searchSession.equals(searchrequest))))
            || displaylist.getAdvanceSearchFlag() == true)
        {
          DL = (ProjectList) displaylistSession;
          request.setAttribute("displaylist", displaylistSession);
        }
        else
        {
          ListGenerator lg = ListGenerator.getListGenerator(dataSource);

          DL =(ProjectList) lg.getProjectList( userobjectd.getIndividualID(),displaylist);

          // DL =(ProjectList) lg.getProjectList(userobjectd.getIndividualID(), 1,
           //   listpreference.getRecordsPerPage(), "", listpreference.getSortElement());
        }
      } //end of else statement (displaylist == null)

      this.setLinks(DL);
      session.setAttribute("displaylist", DL);
      request.setAttribute("displaylist", DL);
      request.setAttribute("list", "Project");
      returnStatus = "showprojectlist";
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception] ProjectViewHandler.execute: " + e.toString());
      //e.printStackTrace();
    } //end of catch block (Exception)
    return mapping.findForward(returnStatus);
  } //end of execute method

  /**
   * This function sets links on members
   *
   * @param DL
   */
  public void setLinks(DisplayList DL)
  {
    Set listkey = DL.keySet();
    Iterator it = listkey.iterator();

    while (it.hasNext())
    {
      String str = (String) it.next();
      StringMember sm = null;
      ListElement ele = (ListElement) DL.get(str);
      sm = (StringMember) ele.get("Name");
      sm.setRequestURL("openPopup('ViewProjectDetail.do?rowId="
        + ele.getElementID() + "&listId=" + DL.getListID() + "')");

      sm = (StringMember) ele.get("Entity");

      IntMember im = (IntMember) ele.get("EntityID");
      sm.setRequestURL("openPopup('ViewEntityDetail.do?rowId="
        + ((Integer) im.getMemberValue()).intValue() + "&listId="
        + DL.getListID() + "')");
    } //end of while loop (it.hasNext())
  } //end of method setLinks
} //end of ProjectViewHandler class
