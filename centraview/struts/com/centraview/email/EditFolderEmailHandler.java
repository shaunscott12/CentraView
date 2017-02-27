/*
 * $RCSfile: EditFolderEmailHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:49 $ - $Author: mking_cv $
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
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;

/**
 * NewFolderEmailHandler.java
 * Creation date: (18/07/2003 )
 * @author: Sunita
 */
public class EditFolderEmailHandler extends org.apache.struts.action.Action
{

  /**
  This method is overridden from Action Class
  */

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      if (request.getParameter("closeornew") != null)
      {
        if (request.getParameter("closeornew").equals("new"))
        {
          request.setAttribute("refreshWindow", "true");
          request.setAttribute("closeWindow", "false");
        }
        else if (request.getParameter("closeornew").equals("close"))
        {
          request.setAttribute("closeWindow", "true");
          request.setAttribute("refreshWindow", "false");
        }
      }
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualId = userobjectd.getIndividualID();

      DynaActionForm dynaForm = (DynaActionForm)form;
      EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)cfh.create();
      remote.setDataSource(dataSource);

      HashMap hmFolder = new HashMap();
      String str = (String)dynaForm.get("accountid");
      String folderid = (String)dynaForm.get("folderid");
      String foldername = (String)dynaForm.get("foldername");
      String parentid = (String)dynaForm.get("parentid");

      hmFolder.put("AccountID", new Integer(Integer.parseInt(str)));
      hmFolder.put("parentid", new Integer(Integer.parseInt(parentid)));
      hmFolder.put("foldername", foldername);
      hmFolder.put("folderid", new Integer(Integer.parseInt(folderid)));

      int i = remote.editFolder(individualId, hmFolder);
      // TODO do something with this return value?
      dynaForm.initialize(mapping);
    }
    catch (Exception e)
    {
      System.out.println("[Exception][EditFolderEmailHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward("displaynewfolderemail"));
  }
}