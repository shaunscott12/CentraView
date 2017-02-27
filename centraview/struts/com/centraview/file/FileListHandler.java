/*
 * $RCSfile: FileListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:55 $ - $Author: mking_cv $
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


import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;




public class FileListHandler extends Action
{
	 private static final String FORWARD_listfile = "listfile";
	 String fileTypeRequest  =  FileConstantKeys.MY;


	/**
	 * Fetches the details of the list and
	 *  forwards the request to the jsp to display
	 *
	 * @param   mapping
	 * @param   form
	 * @param   request
	 * @param   response
	 * @return   ActionForward
	 */


	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)	
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();


		try
		{
			fileTypeRequest  =  FileConstantKeys.MY;

			//int requestfolderID=1;
	 		//int currfolderID=7;

			HttpSession session = request.getSession(true);


			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );
			int individualID = userobjectd.getIndividualID();

			int requestfolderID=(userobjectd.getUserPref()).getDefaultFolderID();



			// After performing the logic in the DeleteHanlder, we are generat a new request for the list
			// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
			// Then destory it after getting the Session value
			if (session.getAttribute("listErrorMessage") != null)
			{
				ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
				saveErrors(request, allErrors);
				session.removeAttribute("listErrorMessage");
			}//end of if (session.getAttribute("listErrorMessage") != null)



			if (request.getParameter("folderId")!=null)
			{
				String ID=(String)request.getParameter("folderId");
				if (ID.indexOf("#")==-1)//Parsing required when request is from a form
					requestfolderID=(Integer.parseInt((String)request.getParameter("folderId")));
				else
				{
					requestfolderID=Integer.parseInt(ID.substring(0,ID.indexOf("#")));	//This is the folderId which has been requested and now list needs to be generated using this folderID
				}
				request.setAttribute("folderId" , requestfolderID+"" );
			}

			/*if (session.getAttribute("filefolderid")!=null)
				currfolderID=(((Integer)session.getAttribute("filefolderid")).intValue());//This the current folderID whose listing is shown*/


		 	fileTypeRequest  = (String)request.getParameter(FileConstantKeys.TYPEOFFILELIST) ;//Check which is the requested listing all or my
		 	request.setAttribute("fileTypeRequest" , fileTypeRequest );

			if (session.getAttribute("highlightmodule") != null)
				session.setAttribute("highlightmodule", "file");



			/*
			Hardcoded for the time being will be decided who is the user(administrator or normal user)

			This parameter decides whether to include the system folders in the list or not
			*/
			boolean systemIncludeFlag=false;



            ListPreference listpreference= userobjectd.getListPreference("File");


			FileList displaylistSession=null;

			try
			{
				displaylistSession = ( FileList )session.getAttribute( "displaylist") ;
			}
			catch(Exception e)
			{
				System.out.println("[Exception][FileListHandler.execute] Exception Thrown: "+e);
			}

			FileList displaylist=null;

			try
			{
				 displaylist = ( FileList )request.getAttribute( "displaylist") ;
			}
			catch(Exception e)
			{
				System.out.println("[Exception][FileListHandler.execute] Exception Thrown: "+e);
			}

			if ( fileTypeRequest == null )
			{
				fileTypeRequest = FileConstantKeys.MY;
			}

			FileList DL = null ;

			if( displaylist == null )
         	{

				 com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);
				 int records = listpreference.getRecordsPerPage();
				 String sortelement = listpreference.getSortElement();
				 DL = (FileList )lg.getFileList( individualID , 1, records , "" ,sortelement, fileTypeRequest,requestfolderID ,systemIncludeFlag );

       			 request.setAttribute(FileConstantKeys.TYPEOFFILELIST,DL.getFileTypeRequest());

				 DL = setLinksfunction( DL );
				 session.setAttribute( "displaylist" , DL );
				 request.setAttribute("displaylist" , DL );

				 //session.setAttribute("filefolderid" , new Integer(requestfolderID ));//sets the requestFolderID to CurrentfolderID
				 //currfolderID=requestfolderID;
				 DL.setCurrentFolderID(requestfolderID);
			}
			else
			{

				String searchSession = displaylistSession.getSearchString();
				String searchrequest = displaylist.getSearchString();
				if(searchSession == null)
					searchSession = "";
				if(searchrequest == null)
					searchrequest = "";

				// if (( displaylistSession.getFileTypeRequest().equals(  displaylist.getFileTypeRequest() ) ) && (displaylistSession.getListID() == displaylist.getListID() ) && ( displaylist.getDirtyFlag() == false ) && ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) && ( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) && (displaylist.getSortMember().equals(displaylistSession.getSortMember() ) ) && (displaylist.getSortType()==(displaylistSession.getSortType()) ) )
				 if ((( displaylistSession.getFileTypeRequest().equals(  displaylist.getFileTypeRequest() ) ) && (displaylistSession.getListID() == displaylist.getListID() ) && ( displaylist.getDirtyFlag() == false ) && ( displaylist.getStartAT() >= displaylistSession.getBeginIndex() ) && ( displaylist.getEndAT()<=displaylistSession.getEndIndex() ) && (displaylist.getSortMember().equals(displaylistSession.getSortMember() ) ) && (displaylist.getSortType()==(displaylistSession.getSortType()) && (searchSession.equals(searchrequest))  ) ) || displaylist.getAdvanceSearchFlag() == true)
				 {
					DL = ( FileList )displaylistSession;
					request.setAttribute(FileConstantKeys.TYPEOFFILELIST,displaylist.getFileTypeRequest());

				 }else
				 {
					ListGenerator lg = ListGenerator.getListGenerator(dataSource);
					DL = ( FileList )lg.getFileList( individualID ,displaylistSession.getCurrentFolderID(), displaylist );
					request.setAttribute(FileConstantKeys.TYPEOFFILELIST,DL.getFileTypeRequest());
					DL.setCurrentFolderID(DL.getCurrentFolderID());
				 }
				 DL = setLinksfunction( DL );
 				 session.setAttribute( "displaylist" , DL );
				 request.setAttribute("displaylist" , DL );
			}
			request.setAttribute("showNewButton",new Boolean(false));
 		}
 		catch (Exception e)
		{
			e.printStackTrace();
			return (mapping.findForward("failure"));
		}
    	return ( mapping.findForward(FORWARD_listfile));
	}



	/**
	 * This function sets links on members
	 *
	 * @param   DL
	 * @return  FileList object
	 */
	public FileList setLinksfunction( FileList DL )
	{

		Set listkey = DL.keySet();
		Iterator it =  listkey.iterator();
		/*while( it.hasNext() )
		{
			String str = ( String )it.next();
			StringMember sm = null;
			ListElement ele  = ( ListElement )DL.get( str );
			sm = ( StringMember )ele.get("Name" );
			sm.setRequestURL("openPopup('ViewScheduleActivity.do?rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");
			sm = ( StringMember )ele.get("CreatedBY" );



			IntMember im = ( IntMember )ele.get("FolderID" );
			Integer value = (Integer)im.getMemberValue();
			int IndividualID = value.intValue();
			sm.setRequestURL( "openPopup('ViewScheduleActivity.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");

		}*/
		while( it.hasNext() )
		{
			String str = ( String )it.next();
			StringMember smFF = null;
			StringMember sm = null;
			ListElement ele  = ( ListElement )DL.get( str );
			int elementID=ele.getElementID();


			smFF=(StringMember)ele.get("FileFolder");

			sm = ( StringMember )ele.get("Name" );

			int fol=1;//delete afterwards

			IntMember im = ( IntMember )ele.get("FolderID" );

			if ((smFF.getDisplayString()).equals(FileConstantKeys.FILE))
			{
				sm.setRequestURL("ViewFile.do?rowId="+elementID+"&listId="+DL.getListID()+"&TYPEOFFILELIST="+fileTypeRequest);
				//smFF.setRequestURL("ListFile.do?rowId="+elementID+"&listId="+DL.getListID()+"&Type="+fol);
				smFF.setRequestURL("ListFile.do?rowId="+elementID+"&listId="+DL.getListID()+"&TYPEOFFILELIST="+fileTypeRequest);

			}
			else if ((smFF.getDisplayString()).equals(FileConstantKeys.FOLDER))
			{
				//sm.setRequestURL("ListFile.do?rowId="+elementID+"&listId="+DL.getListID()+"&Type="+fol);
				//smFF.setRequestURL("ListFile.do?rowId="+elementID+"&listId="+DL.getListID()+"&Type="+fol);

				sm.setRequestURL("ListFile.do?folderId="+elementID+"&listId="+DL.getListID()+"&TYPEOFFILELIST="+fileTypeRequest);
				smFF.setRequestURL("ListFile.do?folderId="+elementID+"&listId="+DL.getListID()+"&TYPEOFFILELIST="+fileTypeRequest);
			}

			sm = ( StringMember )ele.get("CreatedBy" );

			IntMember imIndvID = ( IntMember )ele.get("IndividualID" );

			sm.setRequestURL( "javascript:openPopup('ViewIndividualDetail.do?rowId="+((Integer)imIndvID.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");


			Integer value = (Integer)im.getMemberValue();
			int IndividualID = value.intValue();


		}

		return DL;

	}
}
