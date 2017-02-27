/*
 * $RCSfile: DownLoadFileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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
 
/**
 * DownLoadFileHandler.java
 *
 * This class handles the request for deleting file or folder
 * Creation date: 23 July 2003
 * @author: Amit Gandhe
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

public class DownLoadFileHandler extends org.apache.struts.action.Action
{



	/**
	 * This method gets called when request comes to DownLoadFileHandler
	 *
	 * @param   mapping  
	 * @param   form  
	 * @param   request  
	 * @param   response  
	 * @return  ActionForward   
	 * @exception   Exception  
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		
		int fileID=0;
		
		String FORWARD_NAME="";
		HttpSession session = request.getSession(true);	
		int individualID=((UserObject)session.getAttribute("userobject")).getIndividualID();

		String typeOfOperation = (String) request.getParameter("typeOfOperation");
		String strFileID = request.getParameter("fileid");
		if(strFileID != null && strFileID.length() != 0){
			fileID =Integer.parseInt(strFileID);//get the ID of the File
		}//end of if(strFileID != null && strFileID.length() != 0)
		if(typeOfOperation != null && typeOfOperation.equals("RelatedEmail")){
			// If we are trying to download a file from the RelatedEmail Screen
			// They we know user might not have the permission to access the emailAttachment folder 
			// If we set the individual Id to -13 then it won't perform the authorization.
			individualID = -13;
		}//end of if(typeOfOperation != null && typeOfOperation.equals("RelatedEmail"))
		if(fileID != 0){
			CvFileFacade cvf=new CvFileFacade();
			CvFileVO cvfVO=new CvFileVO();
			cvfVO=cvf.getFile(individualID,fileID, dataSource);
			
			String fileName = cvfVO.getName();
			  
			String requestFileName = (String)request.getParameter("filename");
			if (requestFileName != null && ! requestFileName.equals(""))
			{
			    request.setAttribute("requestFileName", requestFileName);
			}//end of if (requestFileName != null && ! requestFileName.equals(""))
			else{
			    request.setAttribute("requestFileName", "");
			}//end of else for if (requestFileName != null && ! requestFileName.equals(""))
			
			CvFolderVO cvFdVO=cvfVO.getPhysicalFolderVO();
			String fileFullPath=cvFdVO.getFullPath(null,true);
			fileFullPath += fileName;
			request.setAttribute("Path",fileFullPath);
			request.setAttribute("RealName",fileName);
		}//end of if(fileID != 0)
		
		return ( mapping.findForward("downloadfile"));
	}//end of excute
}//end of class

