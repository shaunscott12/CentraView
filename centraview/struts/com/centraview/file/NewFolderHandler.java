/*
 * $RCSfile: NewFolderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:56 $ - $Author: mking_cv $
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class NewFolderHandler extends org.apache.struts.action.Action {

    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_newfolder = ".view.files.newfolder";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;



	/**
	 * Forwards to create newfolder jsp
	 *
	 * @param   mapping  ActionMapping
	 * @param   form  ActionForm
	 * @param   request  HttpServletRequest
	 * @param   response  HttpServletResponse
	 * @return     ActionForward
	 * @exception   Exception
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

	    try
	    {
			// set request
			request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
			request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
			request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.ADD);
			request.setAttribute(FileConstantKeys.WINDOWID, "1");
			request.setAttribute("actionName","New Folder");
			HttpSession session	= request.getSession();
			UserObject userObject = (UserObject)session.getAttribute("userobject");

			FolderForm folderForm = (FolderForm)form;

			String folderId = request.getParameter("folderId");

			if(folderForm.getParentId() != null && !(folderForm.getParentId().equals(""))){
				folderId = folderForm.getParentId();
			}

			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
			CvFileFacade cvf = new CvFileFacade();
			CvFolderVO fdvo = cvf.getFolder(userId,new Integer(folderId).intValue(), dataSource);

			folderForm.setSubfoldername(fdvo.getFullPath(null,false));
			folderForm.setParentId(new Integer(fdvo.getFolderId()).toString());

			FORWARD_final = FORWARD_newfolder;
	    } catch (Exception e)
	    {
	    	System.out.println("[Exception][NewFolderHandler.execute] Exception Thrown: "+e);
	    	e.printStackTrace();
	    	FORWARD_final = GLOBAL_FORWARD_failure;
	    }
        return mapping.findForward(FORWARD_final);
    }
}
