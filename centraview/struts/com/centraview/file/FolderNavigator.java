/*
 * $RCSfile: FolderNavigator.java,v $    $Revision: 1.2 $  $Date: 2005/07/12 18:39:01 $ - $Author: mcallist $
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

package com.centraview.file;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class FolderNavigator extends org.apache.struts.action.Action {
  private static final String FORWARD_lookupfolder = ".view.files.folderlist";

  /**
   * Gets the Folders and other information required to render the File left nav
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession();
    UserObject userObject = (UserObject) session.getAttribute("userobject");
    CvFileFacade cvf = new CvFileFacade();
    ArrayList userFolderList = cvf.getUserFolders(userObject.getIndividualID(), dataSource);
    ArrayList publicFolderList = cvf.getPublicFolders(dataSource);
    // Get the id of the public folder and set it on the request
    Integer publicFolderId = cvf.getPublicFolderId(dataSource);
    boolean systemIncludeFlag = (userObject.getUserType().equals("ADMINISTRATOR") ? true : false);
    ArrayList allFolderList = cvf.getAllFolders(userObject.getIndividualID(), systemIncludeFlag, dataSource);
    request.setAttribute("userfolderlist", userFolderList);
    request.setAttribute("publicFolderId", publicFolderId);
    request.setAttribute("allfolderlist", allFolderList);
    request.setAttribute("publicFolderList", publicFolderList);
    String folderId = request.getParameter("folderId");
    String fileTypeRequest = request.getParameter("fileTypeRequest");
    request.setAttribute("folderId", folderId);
    request.setAttribute("fileTypeRequest", fileTypeRequest);
    if (request.getParameter("typeofoperation") != null) {
      request.setAttribute("typeofoperation", request.getParameter("typeofoperation"));
    }
    return mapping.findForward(FORWARD_lookupfolder);
  }
}
