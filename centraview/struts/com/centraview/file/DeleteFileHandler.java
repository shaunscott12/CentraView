/*
 * $RCSfile: DeleteFileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
 * DeleteFileHandler.java
 *
 * This class handles the request for deleting file or folder
 * Creation date: 23 July 2003
 * @author: Amit Gandhe
 */

package com.centraview.file;

import java.util.ArrayList;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
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
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;


public class DeleteFileHandler extends org.apache.struts.action.Action
{

	/**
	* This method gets called when request comes to DeleteFileHandler
	*
	* @param   mapping
	* @param   form
	* @param   request
	* @param   response
	* @return  ActionForward
	* @exception   Exception
	*/
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws CommunicationException, NamingException
	{
		String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		String rowId[]=null;
		String listType=null;

		String FORWARD_NAME="";
		ActionErrors allErrors = new ActionErrors();
		
		HttpSession session = request.getSession(true);

		rowId =request.getParameterValues("rowId");//request can be to delete mutiple rows
		listType=request.getParameter("listType");//get the list type in this case it better be File
		String listId = request.getParameter("listId");//get its ID,  for some reason.

		long idd = 0;

		if(listId != null)
			idd = Long.parseLong( listId  );

		ListGenerator lg = ListGenerator.getListGenerator(dataSource);


		FileList displayList = (FileList)lg.getDisplayList(idd);//get the FileList from LG
		FileList displayListSession = ( FileList )session.getAttribute( "displaylist") ;


		if((displayListSession!=null)&&(displayListSession.getListID()==idd))
			displayListSession.setDirtyFlag(true);

		UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );
		int individualID = userobjectd.getIndividualID();//get the individualId of the user logged in
		displayList.setDataSource(dataSource);
		ArrayList deleteLog = displayList.deleteElement(individualID, rowId);//deletes the element
		displayList.setDirtyFlag(true);

		if(deleteLog != null && !deleteLog.isEmpty()){
			allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You do not have permission to delete one or more of the records you selected."));
			session.setAttribute("listErrorMessage", allErrors);
		}

		request.setAttribute( "displaylist" , displayList);
		// If listFor is set on the request we must assume we came from the relatedInfo area
		String listFor = (String)request.getParameter("listFor");
		if(listFor !=null && !listFor.equals(""))
		{
			FORWARD_NAME = "deleteRelatedFile";
		} else {
			FORWARD_NAME = "deleteFile";
		}
		return ( mapping.findForward(FORWARD_NAME) );
	}
}