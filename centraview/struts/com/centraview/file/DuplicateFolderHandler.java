/*
 * $RCSfile: DuplicateFolderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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

public class DuplicateFolderHandler extends org.apache.struts.action.Action {
    
    // Local Forwards
    private static final String FORWARD_DUPLICATEFOLDER = ".view.files.duplicatefolder";
	private static  String FORWARD_FINAL = ".view.files.duplicatefolder";

	/**
	 *
	 *
	 * @param   mapping  
	 * @param   form  
	 * @param   request  
	 * @param   response  
	 * @return    ActionForward 
	 * @exception   Exception  
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
    	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		HttpSession session=request.getSession(true);
		
		UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );//get the user object
		int individualID = userobjectd.getIndividualID();//get the individualId of the user logged in
		
		request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
		request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
		request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.DUPLICATE);
		request.setAttribute(FileConstantKeys.WINDOWID, "1");
		
		int rowID=0;
		
		if (request.getParameter("rowId")!=null)
		{
			rowID=Integer.parseInt((String)request.getParameter("rowId"));	//This is the folderId which has been requested to be duplicated
		}
		
		UserObject userObject = (UserObject)session.getAttribute("userobject");
		CvFileFacade cvf = new CvFileFacade();
		CvFolderVO fdvo = cvf.getFolder(individualID,rowID,dataSource);//get the original values of the folder
		FolderForm folderForm = (FolderForm)form;
		folderForm.setFoldername(fdvo.getName());
		folderForm.setFolderId(new Integer(fdvo.getFolderId()).toString());
		folderForm.setSubfoldername(fdvo.getFullPath(null,false));
		folderForm.setParentId(new Integer(fdvo.getParent()).toString());//set the parent id
		FORWARD_FINAL = FORWARD_DUPLICATEFOLDER;
		request.setAttribute(FileConstantKeys.FFID, new Integer(rowID));
		return mapping.findForward(FORWARD_FINAL);
    }
}


