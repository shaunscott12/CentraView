/*
 * $RCSfile: NewFileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:55 $ - $Author: mking_cv $
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

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class NewFileHandler extends org.apache.struts.action.Action
{

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_newfile = ".view.files.newfile";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public NewFileHandler()
  {}

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try
    {
      // get session
      HttpSession session = request.getSession();
      // hash map for add activity
      HashMap fileHashMap = null;
      // to identify multiple opened window
      String newWindowId = "1";
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int userId = userObject.getIndividualID();
      String folderId;
      // Related Info frames, and any file list that calls this handler needs to set this parameter so
      // The target directory becomes the users default rather than trying to get the CurrentFolder from
      // the display list.
      boolean fromRelatedInfo = request.getParameter("BottomFile")!=null && request.getParameter("BottomFile").equals("true");
      if (fromRelatedInfo)
      {
        folderId = "" + (userObject.getUserPref()).getDefaultFolderID();
      }
      else
      {
        folderId = request.getParameter("folderId");
      }
	
      if(request.getParameter("employeeHandBookFlag") != null && request.getParameter("employeeHandBookFlag").equals("true")){
	      CvFileHome cfh = (CvFileHome)CVUtility.getHomeObject("com.centraview.file.CvFileHome", "CvFile");
	      CvFile remote = (CvFile)cfh.create();
	      remote.setDataSource(dataSource);
		  CvFolderVO emlFld = remote.getFolderByName(userId, 2, CvFileFacade.CV_EMPLOYEE_HANDBOOK_FOLDER_NAME);
		  folderId = ""+emlFld.getFolderId();
      }

      // check if present in session else create new
      // Warum?
      // What Fer?
      // as Nancy Kerrigan said: "WHY!!!!???!"
      if (session.getAttribute(FileConstantKeys.NEWFFHASHMAP) == null || fromRelatedInfo)
      {
        fileHashMap = new HashMap();
        // assign window id to activity form bean and store in hashmap
        fileHashMap.put(newWindowId, form);
        session.setAttribute(FileConstantKeys.NEWFFHASHMAP, fileHashMap);
      }
      else
      { // TODO I don't think this branch ever needs to execute.  We should throw out this whole cacheing of the form
        // bean on the session, not sure what gain it provides.
        // populate form bean for previous sub-activity
        PopulateFileForm populateForm = new PopulateFileForm();
        populateForm.setForm(request, response, form);
        form = populateForm.getForm(request, response, form, FileConstantKeys.CURRENTTAB);
		if(((FileForm)form).getUploadfolderid()!=null && !((FileForm)form).getUploadfolderid().equals("")){
			folderId = ((FileForm)form).getUploadfolderid();
		}
        
        CvFileFacade cvf = new CvFileFacade();
        CvFolderVO fdvo = cvf.getFolder(userId, new Integer(folderId).intValue(), dataSource);

        ((FileForm)form).setUploadfoldername(fdvo.getFullPath(null, false));
        ((FileForm)form).setUploadfolderid(new Integer(fdvo.getParent()).toString());
      }

      CvFileFacade cvf = new CvFileFacade();
      CvFolderVO fdvo = cvf.getFolder(userId, new Integer(folderId).intValue(), dataSource);

      ((FileForm)form).setUploadfoldername(fdvo.getFullPath(null, false));
      ((FileForm)form).setUploadfolderid(new Integer(fdvo.getFolderId()).toString());
      ((FileForm)form).setCustomerview(FileConstantKeys.DEFAULTCUSTOMERVIEW);
      ((FileForm)form).setCompanynews(FileConstantKeys.DEFAULTCOMPANYNEWS);
      ((FileForm)form).setStartday("");
      ((FileForm)form).setStartmonth("");
      ((FileForm)form).setStartyear("");
      ((FileForm)form).setEndyear("");
      ((FileForm)form).setEndmonth("");
      ((FileForm)form).setEndday("");


	  String projectName  = (String) request.getParameter("ProjectTitle");
	  String projectID = (String) request.getParameter("ProjectID");
	  if (projectName != null && projectID != null && !projectID.equals("")){
	 	((FileForm)form).setRelatedFieldID(Integer.parseInt(projectID));
		((FileForm)form).setRelatedFieldValue(projectName);
		((FileForm)form).setRelatedTypeID(36);
		((FileForm)form).setRelatedTypeValue("Project");
	  }
      // set request
      request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
      request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
      request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.ADD);
      request.setAttribute(FileConstantKeys.WINDOWID, newWindowId);

      FORWARD_final = FORWARD_newfile;
    }
    catch (Exception e)
    {
      System.out.println("[Exception][NewFileHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}