/*
 * $RCSfile: DownLoadFileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:23 $ - $Author: mking_cv $
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

package com.centraview.preference;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import com.centraview.common.UserObject;



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
		System.out.println( "   In DownLoadFileHandler for preferences synchronization ");
 		MessageResources messages = MessageResources.getMessageResources("ApplicationResources");

		HttpSession session = request.getSession(true);	

		int individualID=((UserObject)session.getAttribute("userobject")).getIndividualID();
 
		String fileFullPath = new String();

		String fileName = new String();

		System.out.println("fileFullPath----------"+ fileFullPath);
		System.out.println("fileName----------"+ fileName);

		request.setAttribute("Path",fileFullPath);
		request.setAttribute("RealName",fileName);
		
		return ( mapping.findForward("downloadfile"));

	}
	
	
}
