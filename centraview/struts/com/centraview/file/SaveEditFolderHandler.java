/*
 * $RCSfile: SaveEditFolderHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

public class SaveEditFolderHandler extends org.apache.struts.action.Action {

    // Local Forwards
    private static final String FORWARD_editfolder = ".view.files.saveeditfolder";

	private static String FORWARD_final = FORWARD_editfolder;

	/**
	 *  Updates the edited details
	 *   of the folder to the database and
	 * @param   mapping  ActionMapping
	 * @param   form  ActionForm
	 * @param   request  HttpServletRequest
	 * @param   response  HttpServletResponse
	 * @return   ActionForward
	 * @exception   Exception
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
		request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
		request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.EDIT);
		request.setAttribute(FileConstantKeys.WINDOWID, "1");

		try {

			String parentId = (String)request.getParameter("parentId");
			String folderId = (String)request.getParameter("folderId");

			FolderForm folderForm = (FolderForm)form;

			CvFolderVO fdvo = new CvFolderVO();
			CvFileFacade cvf = new CvFileFacade();

			fdvo.setName(folderForm.getFoldername());
			fdvo.setParent(new Integer(parentId).intValue());
			fdvo.setDescription(folderForm.getDescription());
			fdvo.setFolderId(new Integer(folderId).intValue());


			HttpSession session = request.getSession();
			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
			fdvo.setCreatedBy(userId);
			fdvo.setOwner(userId);
			// check for close or new
			String closeornew = (String)request.getParameter("closeornew");
			String saveandclose = null;
			String saveandnew = null;
			if (closeornew.equals("close"))
			{
				saveandclose = "saveandclose";
			}
			else if (closeornew.equals("new"))
			{
				saveandnew = "saveandnew";
			}

			// set refresh and closewindow flag
			if (saveandclose != null)
				request.setAttribute("closeWindow", "true");
			
			request.setAttribute("refreshWindow", "true");
			try
			{
				cvf.updateFolder(userId,fdvo,dataSource);
			}
			catch(CvFileException e)
			{
				System.out.println("[Exception][SaveEditFolderHandler.execute] Exception Thrown: "+e);
				e.printStackTrace();
				request.setAttribute("closeWindow", "false");
				saveandclose = "";
				saveandnew = null;
				ActionErrors allErrors = new ActionErrors();
				allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "The Folder with This Name Already Exists On this Location"));
				saveErrors(request, allErrors);
				request.setAttribute("closeWindow","false");
			}

			if (saveandnew != null) {
				folderForm.setDescription("");
				folderForm.setFoldername("");
				folderForm.setSubfoldername("");
				folderForm.setCreatedBy("");
				folderForm.setModifiedBy("");
				folderForm.setOwner("");
				FORWARD_final = ".view.files.savenewfolder";
			}

		} catch(Exception e)
		{
			System.out.println("[Exception][SaveEditFolderHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
		}

		return ( mapping.findForward(FORWARD_final));
    }
}
