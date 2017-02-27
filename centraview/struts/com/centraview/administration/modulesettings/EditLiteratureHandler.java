/*
 * $RCSfile: EditLiteratureHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:46 $ - $Author: mking_cv $
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

package com.centraview.administration.modulesettings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class EditLiteratureHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      String literatureID = null;

      if (request.getParameter("rowId") != null) {
        literatureID = request.getParameter("rowId");
      }

      if (request.getAttribute("rowId") != null) {
        literatureID = (String) request.getAttribute("rowId");
      }

      HttpSession session = request.getSession(true);
      NewLiteratureFormAdmin literatureForm = new NewLiteratureFormAdmin();

      UserObject userobjectd = (UserObject) session.getAttribute("userobject");
      int userid = userobjectd.getIndividualID();

      //Get the rmote object of Literature EJB
      LiteratureHome literatureHome = (LiteratureHome) CVUtility.getHomeObject("com.centraview.administration.modulesettings.LiteratureHome", "LiteratureAdmin");
      Literature remote = literatureHome.create();
      remote.setDataSource(dataSource);

      //Get the information related to the Literature and then delete
      LiteratureVO literatureVO = remote.selectLiterature(Integer.parseInt(literatureID));
      literatureForm.setLiteratureName(literatureVO.getLiteratureTitle());
      literatureForm.setLiteratureID(literatureVO.getLiteratureID());
      literatureForm.setFileID(literatureVO.getFileID());

      request.setAttribute("fileName", literatureVO.getFileName() + "");
      request.setAttribute("fileId", literatureVO.getFileID() + "");
      request.setAttribute("typeofmodule", AdministrationConstantKeys.MODULESETTINGS);
      request.setAttribute("typeofsubmodule", "Marketing");
      request.setAttribute("newliteratureformadmin", literatureForm);
    } catch (Exception e) {
      System.out.println("[Exception][EditLiteratureHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward(".view.administration.view_literature"));
  }
}
