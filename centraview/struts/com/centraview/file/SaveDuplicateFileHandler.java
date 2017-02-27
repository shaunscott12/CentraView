/*
 * $RCSfile: SaveDuplicateFileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:56 $ - $Author: mking_cv $
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
import com.centraview.settings.Settings;

public class SaveDuplicateFileHandler extends org.apache.struts.action.Action {

    // Global Forwards
    public static final String GLOBAL_FORWARD_FAILURE = "failure";
    
    // Local Forwards
    private static final String GLOBAL_FORWARD = ".view.files.duplicatefile";
    private static String FORWARD_FINAL = GLOBAL_FORWARD_FAILURE;
 
	/**
	 * This is overridden method  from Action class
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
	    try 
	    {
			HttpSession session=request.getSession(true);
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );
            int individualID = userobjectd.getIndividualID();//get the IndividualId of the user logged in 
			int curFileID=(Integer.parseInt(request.getParameter("rowId")));
			int folderID=Integer.parseInt(((FileForm)form).getUploadfolderid());
	    	
			// call ejb to insert record
			// initialize file vo
			CvFileVO fileVO = new CvFileVO();
			
			fileVO.setFileId(curFileID);
			fileVO.setPhysicalFolder(folderID);
			fileVO.setTitle(((FileForm)form).getTitle());
			fileVO.setDescription(((FileForm)form).getDescription());
			fileVO.setVisibility(((FileForm)form).getAccess());
			fileVO.setCustomerView(((FileForm)form).getCustomerview());
			fileVO.setCreatedBy(individualID);
			fileVO.setOwner(individualID);
			if(((FileForm)form).getAuthorid() != null && ((FileForm)form).getAuthorid().length() > 0)
				fileVO.setAuthorId(Integer.parseInt(((FileForm)form).getAuthorid()));
				
			if(((FileForm)form).getEntityid() != null && ((FileForm)form).getEntityid().length() > 0)
				fileVO.setRelateEntity(Integer.parseInt(((FileForm)form).getEntityid()));
				
			if(((FileForm)form).getIndividualid() != null && ((FileForm)form).getIndividualid().length() > 0)
				fileVO.setRelateIndividual(Integer.parseInt(((FileForm)form).getIndividualid()));
			
			// call to file facade
			CvFileFacade fileFacade = new CvFileFacade();
			fileFacade.duplicateFile(individualID,fileVO,folderID, dataSource); //duplicates the file record

	    	// set request back to jsp
	    	request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
	    	request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
	    	request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.DUPLICATE);
	    	request.setAttribute(FileConstantKeys.WINDOWID, "1");
	    	request.setAttribute(FileConstantKeys.FFID, new Integer(curFileID));
	    	
			//get the button value which user has clicked on form
			if (request.getParameter("closeornew").equals("close"))	
			{
				request.setAttribute("closeWindow","true");
			}
			else	
			{
				request.setAttribute("closeWindow","false");			
			}			
			request.setAttribute("refreshWindow", "true");
	    	FORWARD_FINAL = GLOBAL_FORWARD;
	    } catch (Exception e)	
	    {
	    	e.printStackTrace();
	    	FORWARD_FINAL = GLOBAL_FORWARD_FAILURE;		
	    }
        return mapping.findForward(FORWARD_FINAL);
    }
}
