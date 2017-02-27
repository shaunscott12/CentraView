/*
 * $RCSfile: DeleteFolderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:49 $ - $Author: mking_cv $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;


/**
 * Insert the type's description here. Creation date: (17/07/2003 )
 *
 * @author   Vivek T
 */
public class DeleteFolderHandler extends Action
{

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      int trashfolderId    = 0;
      int trashdeleteflag  = 0;

      request.setAttribute("closeWindow", "true");
      request.setAttribute("refreshWindow", "false");

      int sourcefolderid   = 0;
      int parentFolderId   = 0;
      String sid           = (String) request.getParameter("folderid");
      if (sid != null && !sid.equals(""))
      {
        try
        {
          sourcefolderid = Integer.parseInt(sid);
        } //end of try block
        catch (NumberFormatException e)
        {
          //ignored
        } //end of catch block (NumberFormatException)
      } //end of if statement (sid != null && !sid.equals(""))

      int accountid        = 0;
      String aid           = (String) request.getParameter("accountid");
      if (aid != null && !aid.equals(""))
      {
        try
        {
          accountid = Integer.parseInt(aid);
        } //end of try block
        catch (NumberFormatException e)
        {
          //ignored
        } //end of catch block (NumberFormatException)
      } //end of if statement (aid != null && !aid.equals(""))
      //	String messageID[] = request.getParameterValues("rowId");

      HttpSession session  = request.getSession(true);
      FolderList fl        = (FolderList) session.getAttribute("folderlist");
      AccountDetail ad     = (AccountDetail) fl.get(new Integer(accountid));
      ArrayList al         = ad.getFolderList();
      Folder f;
      for (int i = 0; i < al.size(); i++)
      {
        f = (Folder) al.get(i);

        if (f.getFoldername().equalsIgnoreCase("trash")
           && f.getFtype().equalsIgnoreCase("system"))
        {
          trashfolderId = f.getFolderid();
          parentFolderId = fl.getDefaultFolder();
          break;
        } //end of if statement (f.getFoldername().equalsIgnoreCase("trash") && f.getFtype().equalsIgnoreCase("system"))
      } //end of for loop (int i = 0; i < al.size(); i++)

      EmailFacadeHome cfh  = (EmailFacadeHome) CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote   = (EmailFacade) cfh.create();
      remote.setDataSource(dataSource);
      remote.removeFolder(sourcefolderid, trashfolderId);

      //Added by Ryan Grier - 10.08.2003
      //Build the parent view address
      //This will be passed to the jsp page and 
      //the opener window will be redirected to this URL.
      //NOTE: This isn't the nicest way to do this, but it works for now :(
      StringBuffer nextUrl = new StringBuffer();
      nextUrl.append(request.getContextPath());
      nextUrl.append(mapping.findForward("showemaillist").getPath());
      nextUrl.append("?accountid=" + Integer.toString(accountid));
      nextUrl.append("&folderid=" + Integer.toString(parentFolderId));
      
      request.setAttribute("viewParent", "true");
      request.setAttribute("parentListUrl", nextUrl.toString());
      //End of Build the parent view address
      //End of Added by Ryan Grier - 10.08.2003
    }
    catch (Exception e)
    {
      System.out.println("[Exception][DeleteFolderHandler.execute] Exception Thrown: "+e);
      e.printStackTrace();
    } //end of catch block (Exception)
    return (mapping.findForward("displaynewfolderemail"));
  } //end of execute method
} //end of DeleteFolderHandler class