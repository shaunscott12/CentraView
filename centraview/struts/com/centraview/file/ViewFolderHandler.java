/*
 * $RCSfile: ViewFolderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:58 $ - $Author: mking_cv $
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

	/**
	 *  Fetches the details of the folder and
	 *  forwards the request to the jsp to view
	 * @param   mapping  ActionMapping
	 * @param   form  ActionForm
	 * @param   request  HttpServletRequest
	 * @param   response  HttpServletResponse
	 * @return   ActionForward
	 * @exception   Exception
	 */


public class ViewFolderHandler extends org.apache.struts.action.Action {

    // Local Forwards
	private static  String FORWARD_final = ".view.files.editfolder";


	/**
	 *  Fetches the details of the folder and
	 *  forwards the request to the jsp to display
	 * @param   mapping  ActionMapping
	 * @param   form  ActionForm
	 * @param   request  HttpServletRequest
	 * @param   response  HttpServletResponse
	 * @return   ActionForward
	 * @exception   Exception
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
    	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
		request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
		request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.EDIT);
		request.setAttribute(FileConstantKeys.WINDOWID, "1");

		request.setAttribute("actionName","Edit Folder");

		HttpSession session = request.getSession();


		String folderId = request.getParameter("folderId");

		FolderForm folderForm = (FolderForm)form;

		UserObject userObject = (UserObject)session.getAttribute("userobject");
		int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

		CvFileFacade cvf = new CvFileFacade();
		CvFolderVO fdvo = cvf.getFolder(userId,new Integer(folderId).intValue(), dataSource);


		folderForm.setFoldername(fdvo.getName());
		folderForm.setDescription(fdvo.getDescription());
		folderForm.setFolderId(new Integer(fdvo.getFolderId()).toString());
		if (fdvo.getCreatedByVO() !=null)
		folderForm.setOwner(fdvo.getCreatedByVO().getFirstName() + " " + fdvo.getCreatedByVO().getLastName());
		folderForm.setOwnerId(new Integer(fdvo.getCreatedBy()).toString());

		if (fdvo.getCreatedOn() !=null) {
			String createdDate=		getDateName(fdvo.getCreatedOn().getMonth())+" "+fdvo.getCreatedOn().getDate()+", "+(fdvo.getCreatedOn().getYear()+1900);
			folderForm.setCreatedBy(createdDate);
		}
		if (fdvo.getModifiedOn() !=null) {
			String modifiedDate=		getDateName(fdvo.getModifiedOn().getMonth())+" "+fdvo.getModifiedOn().getDate()+", "+(fdvo.getModifiedOn().getYear()+1900);
			folderForm.setModifiedBy(modifiedDate);
		}

		if(folderForm.getParentId() != null && !(folderForm.getParentId().equals(""))){
			CvFolderVO tempFolderVO = cvf.getFolder(userId,new Integer(folderForm.getParentId()).intValue(), dataSource);
			folderForm.setSubfoldername(tempFolderVO.getFullPath(null,false));
			folderForm.setParentId(new Integer(tempFolderVO.getFolderId()).toString());
		}else{
			folderForm.setSubfoldername(fdvo.getFullPath(null,false));
			folderForm.setParentId(new Integer(fdvo.getParent()).toString());
		}

		int fldrID = Integer.parseInt(folderId);
		request.setAttribute("RECORDOPERATIONRIGHT",new Integer(CVUtility.getRecordPermission(userId, "CVFolder", fldrID, dataSource)));
        return mapping.findForward(FORWARD_final);
  }

  public String getDateName (int monthNumber) // This method is used to quickly return the proper name of a month
  {
	String strReturn = "";
	switch (monthNumber)
		{
	case 0:
		strReturn = "Jan";
		break;
	case 1:
		strReturn = "Feb";
		break;
	case 2:
		strReturn = "Mar";
		break;
	case 3:
		strReturn = "Apr";
		break;
	case 4:
		strReturn = "May";
		break;
	case 5:
		strReturn = "June";
		break;
	case 6:
		strReturn = "July";
		break;
	case 7:
		strReturn = "Aug";
		break;
	case 8:
		strReturn = "Sep";
		break;
	case 9:
		strReturn = "Oct";
		break;
	case 10:
		strReturn = "Nov";
		break;
	case 11:
		strReturn = "Dec";
		break;
	}
	return strReturn;
  }
}