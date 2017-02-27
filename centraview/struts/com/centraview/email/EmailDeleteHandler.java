/*
 * $RCSfile: EmailDeleteHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.ListGenerator;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;

/**
 * Insert the type's description here.
 * Creation date: (17/07/2003 )
 * @author: Vivek T
 */
public class EmailDeleteHandler extends org.apache.struts.action.Action
{


  /**
  This method is overridden from Action Class
  */

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String listAction = null;
    try
    {
      int trashfolderId = 0;
      int trashdeleteflag = 0;
      int sourcefolderid = 0;
      HttpSession session = request.getSession(true);

      DisplayList sessionDL = (DisplayList) session.getAttribute("displaylist");
      DisplayList displaylist = (DisplayList)request.getAttribute("displaylist");
      
//      System.out.println("displaylist"+displaylist);


      long reqListID = -1;
      String listIdStr = (String) request.getParameter("listId");

      if (listIdStr != null)
      {
	     reqListID = Long.parseLong(listIdStr);
      }

//      System.out.println("reqListID"+reqListID);


    long sessionListID = -1;

    if (sessionDL != null)
    {
      sessionListID = sessionDL.getListID();
    }

      ListGenerator LG = ListGenerator.getListGenerator(dataSource);
      DisplayList reqDL = LG.getDisplayList(reqListID);
      
    if ((sessionDL != null) && (reqListID == sessionListID))
    {
      int startat = sessionDL.getStartAT();
      int endat = sessionDL.getEndAT();
      
  //    System.out.println("startat DELETE >"+startat);
//      System.out.println("endatDELETE >"+endat);

      reqDL.setStartAT(startat);
      reqDL.setEndAT(endat);
    
    }
    



      request.setAttribute("displaylist", reqDL);
      request.setAttribute("deleteFlag", "true");
      
//      System.out.println("displaylist"+displaylist);

      String sid = (String)request.getParameter("folderid");

      if (sid != null && !sid.equals(""))
      {
        try
        {
          sourcefolderid = Integer.parseInt(sid);
        }
        catch (NumberFormatException e)
        {}
      }

      int accountid = 0;

      String aid = (String)request.getParameter("accountid");
      if (aid != null && !aid.equals(""))
      {
        try
        {
          accountid = Integer.parseInt(aid);
        }
        catch (NumberFormatException e)
        {
          System.out.println("[Exception][EmailDeleteHandler.execute] Exception Thrown: " + e);
        }
      }

      String messageID[] = request.getParameterValues("rowId");
      
      if (messageID == null){
         messageID = new String[1];
         messageID[0] = request.getParameter("rowId");
      }

      listAction = request.getParameter("listAction");
      
      System.out.println("Next"+listAction);
      
      String rowId = null;

      
      FolderList fl = (FolderList)session.getAttribute("folderlist");
      AccountDetail ad = (AccountDetail)fl.get(new Integer(accountid));
      ArrayList al = ad.getFolderList();
      Folder f;

      for (int i = 0; i < al.size(); i++)
      {
        f = (Folder)al.get(i);

        if (f.getFoldername().equalsIgnoreCase("trash") && f.getFtype().equalsIgnoreCase("system"))
        {
          trashfolderId = f.getFolderid();
          break;
        }
      }

      EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)cfh.create();
      remote.setDataSource(dataSource);
      remote.emailDelete(sourcefolderid, trashfolderId, messageID);

    }
    catch (Exception e)
    {
      System.out.println("[Exception][EmailDeleteHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }

    if (listAction != null && listAction.equals("next"))
    {
      return (mapping.findForward("displayViewMail"));
    }


    String listType = (String)request.getParameter("listType");
    String listFor = (String)request.getParameter("listFor");

    if (listType == null)
    {
      listType = "";
    }

    if (listType.equals("Individual") && listFor != null)
    {
      return (mapping.findForward("deleteRelatedEmail"));
    }
    else
    {
      return (mapping.findForward("displaymail"));
    }
  } // end execute() method
}