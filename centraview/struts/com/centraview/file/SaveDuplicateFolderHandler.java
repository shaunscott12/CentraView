/*
 * $RCSfile: SaveDuplicateFolderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:57 $ - $Author: mking_cv $
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

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;


public class SaveDuplicateFolderHandler extends Action
{
	// Global Forwards
	public static String  GLOBAL_FORWARD_FAILURE="failure";
	
	public static String GLOBAL_FORWARD="saveduplicatefolder";
	
	//// Local Forwards
	public static String GLOBAL_FORWARD_FINAL=GLOBAL_FORWARD_FAILURE;
	


	/**
	 * This is overridden method  from Action class
	 *
	 * @param   mapping  
	 * @param   form  
	 * @param   request  
	 * @param   response  
	 * @return    ActionForward 
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		try
		{
		
			HttpSession session=request.getSession(true);
			
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object 
            int individualID = userobjectd.getIndividualID();//get the individualId of the user logged in
			
		
			FolderForm folderForm=(FolderForm)form;
			
			CvFolderVO fdvo = new CvFolderVO();
			CvFileFacade  cvf=new CvFileFacade();
			
			int curFolderID=(Integer.parseInt(request.getParameter("folderId")));// gets the ID of file or folder which user has selected
			
			CvFolderVO fdExistingVO = cvf.getFolder(individualID,curFolderID,dataSource);//get the original values using the current ID
			
			fdExistingVO.setName(folderForm.getFoldername());//sets the name on the form
			
			int curParentID=Integer.parseInt(request.getParameter("parentId"));
			fdExistingVO.setParent(curParentID);//set the current parent selected from folder lookup
			
			fdExistingVO.setOwner(individualID);//set the current individualID
			
			cvf.duplicateFolder(individualID,fdExistingVO,curFolderID,curParentID, dataSource);//calls the duplicate folder and duplicates the record

			request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
			request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
			request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.DUPLICATE);
			request.setAttribute(FileConstantKeys.WINDOWID, "1");
			
			request.setAttribute(FileConstantKeys.FFID, new Integer(curFolderID));

			String closeornew = (String)request.getParameter("closeornew");//gets the parameter whether (cancel) or (save and close) or (save and new) is clicked
			
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
			
			if (saveandnew != null) {
				folderForm.setDescription("");
				folderForm.setFoldername("");
				folderForm.setSubfoldername("");
			}

			GLOBAL_FORWARD_FINAL = GLOBAL_FORWARD;	
		}
		catch (Exception e)	
	    {
			System.out.println("[Exception][SaveDuplicateFolderHandler.execute] Exception Thrown: "+e);
	    	e.printStackTrace();
	    	GLOBAL_FORWARD_FINAL = GLOBAL_FORWARD_FAILURE;		
	    }
        return mapping.findForward(GLOBAL_FORWARD_FINAL);
	}
}
