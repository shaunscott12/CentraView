/*
 * $RCSfile: FileLookupHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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
* FileLookupHandler.java
*
*
* Creation date: 11 August 2003
* @author: Sunita
*/


package com.centraview.email;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.file.CvFileFacade;
import com.centraview.file.FileConstantKeys;
import com.centraview.file.FileList;
import com.centraview.file.FileListElement;
import com.centraview.settings.Settings;

public class FileLookupHandler extends Action
{

	private static final String FORWARD_filelookup = "displayFileLookup";


	private static Logger logger = Logger.getLogger(FileLookupHandler.class);
	/**
	* Fetches the details of the list and
	*  forwards the request to the jsp to display
	*
	* @param   mapping
	* @param   form
	* @param   request
	* @param   response
	* @return  ActionForward
	*/
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{
	  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		try
		{
			HttpSession session = request.getSession(true);
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );
			int individualID = userobjectd.getIndividualID();

			int strFolderId = -1;
			if (request.getParameter("folderId") != null)
			{
				strFolderId = Integer.parseInt(request.getParameter("folderId").toString());
			}
			else {
				strFolderId = userobjectd.getUserPref().getDefaultFolderID();
			}

			ListPreference listpreference= userobjectd.getListPreference("File");
			FileList displaylistSession=null;


			FileList DL = new FileList() ;
			CvFileFacade fileFacade = new CvFileFacade();
			FileListElement fle = fileFacade.getParentFolder(individualID, strFolderId, dataSource);

			if (fle != null) {
				StringBuffer stringbuffer = new StringBuffer("00000000000");
				stringbuffer.setLength(11);
				String s3 = (new Integer(DL.getBeginIndex())).toString();
				stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
				String s4 = stringbuffer.toString();
				DL.put(s4, fle);

				DL.setTotalNoOfRecords(DL.size() + 1);
				DL.setListType("File");
				DL.setBeginIndex(DL.getBeginIndex());
				DL.setEndIndex(DL.size());
			}

			com.centraview.common.ListGenerator lg = com.centraview.common.ListGenerator.getListGenerator(dataSource);
			boolean systemIncludeFlag = (userobjectd.getUserType().equals("ADMINISTRATOR") ? true : false);
			DL.putAll((FileList )lg.getFileList(individualID,1,10,"","",FileConstantKeys.MY,strFolderId,systemIncludeFlag));;

			String searchStr = request.getParameter("simpleSearch");
			if(searchStr != null && (searchStr.trim()).length() > 0)
			{
				searchStr = searchStr.trim();
				DL.setSearchString("SIMPLE: "+searchStr);
				DL.search();
			}
			request.setAttribute("displaylist" , DL );

		}
		catch (Exception e)
		{
			logger.error("[Exception] FileLookupHandler.Execute Handler ", e);
			return (mapping.findForward("failure"));
		}
		return ( mapping.findForward(FORWARD_filelookup));
	}
}
