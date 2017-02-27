/*
 * $RCSfile: DuplicateFileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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



public class DuplicateFileHandler extends org.apache.struts.action.Action {

    // Global Forwards
    public static final String GLOBAL_FORWARD_FAILURE = "failure";
    
    // Local Forwards
    private static final String FORWARD_DUPLICATEFILE = ".view.files.duplicatefile";
    private static String FORWARD_FINAL = GLOBAL_FORWARD_FAILURE;
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
        try 
        {
        	// set request
        	request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
        	request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
        	request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.DUPLICATE);
        	request.setAttribute(FileConstantKeys.WINDOWID, "1");
		
			// get file id from request
			int fileId = 0;
			if (request.getParameter("rowId") != null)
				fileId = Integer.parseInt(request.getParameter("rowId"));
			
			HttpSession session = request.getSession();
			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
			// get the data from db thru ejb
			CvFileFacade fileFacade = new CvFileFacade();
			CvFileVO fileVO = fileFacade.getFile(userId, fileId, dataSource); 
			
			// set the form bean data from VO
			request.setAttribute(FileConstantKeys.FFID, ""+fileId);

			((FileForm)form).setFileId(""+fileVO.getFileId());
			// iq added
			((FileForm)form).setFilename(fileVO.getName());
			//
			((FileForm)form).setTitle(fileVO.getTitle());
			((FileForm)form).setFileInfo(fileVO.getName());//set file info
			((FileForm)form).setDescription(fileVO.getDescription());//set the description 
			((FileForm)form).setAccess(fileVO.getVisibility());
			((FileForm)form).setCustomerview(fileVO.getCustomerView());
			((FileForm)form).setUploadfolderid(""+fileVO.getPhysicalFolder());//sets physical folder
			if (fileVO.getPhysicalFolderVO() !=null) {
				((FileForm)form).setUploadfoldername(fileVO.getPhysicalFolderVO().getName());
			}
			
			((FileForm)form).setOwnerid(""+fileVO.getOwner());
			if (fileVO.getOwnerVO() != null) {
				((FileForm)form).setOwnername(fileVO.getOwnerVO().getFirstName() + " " + fileVO.getOwnerVO().getLastName());
			}
			
			((FileForm)form).setAuthorid(""+fileVO.getAuthorId());
			if (fileVO.getAuthorVO() != null) {
				((FileForm)form).setAuthorname(fileVO.getAuthorVO().getFirstName() + " " + fileVO.getAuthorVO().getLastName());
			}
			
			((FileForm)form).setEntityid(""+fileVO.getRelateEntity());
			if(fileVO.getRelateEntityVO() != null)
				((FileForm)form).setEntityname(fileVO.getRelateEntityVO().getName());
			
			((FileForm)form).setIndividualid(""+fileVO.getRelateIndividual());
			if(fileVO.getRelateIndividualVO() != null)
				((FileForm)form).setIndividualname(fileVO.getRelateIndividualVO().getFirstName() + " " + fileVO.getRelateIndividualVO().getLastName());

			if (fileVO.getCreatedOn() != null){
				String createdDate=		getDateName(fileVO.getModifiedOn().getMonth())+" "+fileVO.getModifiedOn().getDate()+", "+(fileVO.getModifiedOn().getYear()+1900);
				((FileForm)form).setCreated(createdDate);
				}
			if (fileVO.getModifiedOn() != null){
				String modifiedDate=		getDateName(fileVO.getModifiedOn().getMonth())+" "+fileVO.getModifiedOn().getDate()+", "+(fileVO.getModifiedOn().getYear()+1900);
				((FileForm)form).setModified(modifiedDate);
			}
	
			request.setAttribute("fileform", form);
        	FORWARD_FINAL = FORWARD_DUPLICATEFILE;
        } catch (Exception e)	
        {
        	System.out.println("[Exception][DuplicateFileHandler.execute] Exception Thrown: "+e);
        	e.printStackTrace();
        	FORWARD_FINAL = GLOBAL_FORWARD_FAILURE;		
        }
        return mapping.findForward(FORWARD_FINAL);
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
