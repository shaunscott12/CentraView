/*
 * $RCSfile: SaveLiteratureHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:47 $ - $Author: mking_cv $
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

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileVO;
import com.centraview.settings.Settings;

public class SaveLiteratureHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String FINAL_FORWARD = ".view.adminstration.view_literature";

    if ((request.getParameter("closeorsave") != null) && request.getParameter("closeorsave").equals("save")) {
      FINAL_FORWARD = ".view.adminstration.view_literature";
    } else {
      FINAL_FORWARD = ".view.adminstration.list_literature";
    }

    try {
      HttpSession session = request.getSession(true);
      NewLiteratureFormAdmin literatureForm = (NewLiteratureFormAdmin) form;
      UserObject userobjectd = (UserObject) session.getAttribute("userobject");
      int individualID = userobjectd.getIndividualID();
      FormFile ff = (FormFile) literatureForm.getFile();
      String strf = ff.getFileName();
      InputStream im = ff.getInputStream();

      //Add file first
      CvFileFacade cvfile = new CvFileFacade();
      CvFileVO flvo = new CvFileVO();
      flvo.setTitle("Literature");
      flvo.setName(strf);
      flvo.setCreatedBy(individualID);

      int fileID = cvfile.addLiterature(individualID, flvo, im, dataSource);

      //Add entry in Literature table
      LiteratureHome literatureHome = (LiteratureHome)CVUtility.getHomeObject("com.centraview.administration.modulesettings.LiteratureHome", "LiteratureAdmin");
      Literature remote = literatureHome.create();
      remote.setDataSource(dataSource);

      int newLiteratureID = remote.addLiterature(individualID, literatureForm.getLiteratureName(), fileID, "Literature" + fileID);

      //Set the request attribute here
      request.setAttribute("rowId", Integer.toString(newLiteratureID));
    } catch (Exception e) {
      System.out.println("[Exception][SaveLiteratureHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }

    return (mapping.findForward(FINAL_FORWARD));
  }
}
