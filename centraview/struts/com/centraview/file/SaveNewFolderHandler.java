/*
 * $RCSfile: SaveNewFolderHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveNewFolderHandler extends org.apache.struts.action.Action {
    
    // Local Forwards
    private static final String FORWARD_newfolder = ".view.files.savenewfolder";
    
    private static String FORWARD_final = FORWARD_newfolder;
  	
	/**
	 *  Adds a New Folder to the 
	 *  selected parent Folder
	 * @param   mapping  ActionMapping
	 * @param   form  ActionForm
	 * @param   request  HttpServletRequest
	 * @param   response  HttpServletResponse
	 * @return   ActionForward  
	 * @exception   Exception  
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		// TODO: Write method body
		int rowID=0;
		request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
		request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
		request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.ADD);
		request.setAttribute(FileConstantKeys.WINDOWID, "1");
		
		String parentId = request.getParameter("parentId");
		HttpSession session = request.getSession();
		
		try {
			FolderForm folderForm = (FolderForm)form;
			CvFolderVO fdvo = new CvFolderVO();
			fdvo.setName(folderForm.getFoldername());
			fdvo.setParent(Integer.parseInt(parentId));
			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
			fdvo.setCreatedBy(userId);
			// hardcoded  to be changed
			fdvo.setLocationId(1);
			fdvo.setVisibility(folderForm.getAccess());
			
			// check for close or new
			String closeornew = (String)request.getParameter("closeornew");
			String saveandclose = null;
			String saveandnew = null;
			if (closeornew.equals("close"))
				saveandclose = "saveandclose";
			else if (closeornew.equals("new"))
				saveandnew = "saveandnew";

			// set refresh and closewindow flag 
			if (saveandclose != null)
				request.setAttribute("closeWindow", "true");

			request.setAttribute("refreshWindow", "true");
			
			CvFileFacade cvf = new CvFileFacade();
			try
			{
				int recordID=cvf.addFolder(userId,fdvo,dataSource);
				rowID=recordID;
			}
			catch(CvFileException e)
			{
				request.setAttribute("closeWindow", "false");
				saveandclose = "";
				saveandnew = null;
				ActionErrors allErrors = new ActionErrors();
				allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "The Folder with This Name Already Exists On this Location"));
				saveErrors(request, allErrors);
			}
			
			if (saveandnew != null) {
				folderForm.setDescription("");
				folderForm.setFoldername("");
				folderForm.setSubfoldername("");
			}
		}catch(Exception e) {
			System.out.println(" in  ececute of SaveNewFolderHandler " +e.toString());
		}
		return ( mapping.findForward(FORWARD_final));
    }
	
	
}
