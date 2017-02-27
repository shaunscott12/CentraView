/*
 * $RCSfile: EmailMarkasreadHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;

/**
 * This handler is used for marking email as read.
 * Creation date: (17/07/2003 )
 * @author: Vivek T
 */
public class EmailMarkasreadHandler extends org.apache.struts.action.Action
{


  /**
  This method is overridden from Action Class
  */

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    int smarkFlag = 0;
    try
    {
      boolean markflag = false;
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualId = userobjectd.getIndividualID();

      String rowId = null;

      int destfolderid = 0;

      int sourcefolderid = 0;
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
      String sflag = (String)request.getParameter("readflag");
      if (sflag != null && !sflag.equals(""))
      {
        try
        {
          smarkFlag = Integer.parseInt(sflag);
        }
        catch (NumberFormatException e)
        {}
      }
      String messageID[] = request.getParameterValues("rowId");

      EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)cfh.create();
      remote.setDataSource(dataSource);
      remote.emailMarkasRead(sourcefolderid, smarkFlag, messageID);
    }
    catch (Exception e)
    {
      System.out.println("[Exception][EmailMarkasreadHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward("displaymail"));
  }
}