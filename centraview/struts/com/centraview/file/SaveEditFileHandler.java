/*
 * $RCSfile: SaveEditFileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class SaveEditFileHandler extends org.apache.struts.action.Action {

    public static final String GLOBAL_FORWARD_failure = "failure";
    private static final String FORWARD_editsavefile = ".view.files.editfile";
    private static final String FORWARD_editsaveclosefile = ".view.files.list";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;


	/*
	 *	Executes initialization of required parameters and open window for entry of note
	 *	returns ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
    	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    	HttpSession session = request.getSession();
	    try
	    {

            com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );
            String timeZone =  userobjectd.getUserPref().getTimeZone();

			if (request.getParameter("saveType").equals("close"))
			{
				request.setAttribute("bodycontent", null);
				FORWARD_final = FORWARD_editsaveclosefile;
				return mapping.findForward(FORWARD_final);
			}

	    	// call ejb to insert record
	    	// initialize file vo
	    	CvFileVO fileVO = new CvFileVO();

			// set the VO from form bean data
			((FileForm)form).setFileId(request.getParameter(FileConstantKeys.FFID));
			if (((FileForm)form).getCompanynews().equals("YES"))
			{
			  String startDay = ((FileForm)form).getStartday();
			  String startMonth = ((FileForm)form).getStartmonth();
			  String startYear = ((FileForm)form).getStartyear();
			  String endMonth = ((FileForm)form).getEndmonth();
			  String endDay = ((FileForm)form).getEndday();
			  String endYear = ((FileForm)form).getEndyear();

			  if (startDay != null && startMonth != null && startYear != null
			  	  && startDay.length() != 0 && startMonth.length() != 0 && startYear.length() != 0 )
			  {
			  	   fileVO.setFrom(getTimestamp(startDay,startMonth,startYear,timeZone));
			  }
			  if (endDay != null && endMonth != null && endYear != null
			  	  && endDay.length() != 0 && endMonth.length() != 0 && endYear.length() != 0 )
			  {
				   fileVO.setTo(getTimestamp(endDay,endMonth,endYear,timeZone));
			  }
			}

			fileVO.setTitle(((FileForm)form).getTitle());
			fileVO.setFileId(Integer.parseInt(((FileForm)form).getFileId()));
			fileVO.setName(((FileForm)form).getFileInfo());
			fileVO.setPhysicalFolder(Integer.parseInt(((FileForm)form).getUploadfolderid()));
			fileVO.setDescription(((FileForm)form).getDescription());
			fileVO.setCompanyNews(((FileForm)form).getCompanynews());
			if(((FileForm)form).getAuthorid() != null && ((FileForm)form).getAuthorid().length() > 0)
				fileVO.setAuthorId(Integer.parseInt(((FileForm)form).getAuthorid()));


			fileVO.setVisibility(((FileForm)form).getAccess());
			fileVO.setVersion(((FileForm)form).getFileversion());

			if(((FileForm)form).getEntityid() != null && ((FileForm)form).getEntityid().length() > 0)
				fileVO.setRelateEntity(Integer.parseInt(((FileForm)form).getEntityid()));

			if(((FileForm)form).getIndividualid() != null && ((FileForm)form).getIndividualid().length() > 0)
				fileVO.setRelateIndividual(Integer.parseInt(((FileForm)form).getIndividualid()));

			//Set the RelatedField Information:
			fileVO.setRelatedFieldID(((FileForm)form).getRelatedFieldID());
			fileVO.setRelatedFieldValue(((FileForm)form).getRelatedFieldValue());
			fileVO.setRelatedTypeID(((FileForm)form).getRelatedTypeID());
			fileVO.setRelatedTypeValue(((FileForm)form).getRelatedTypeValue());

			String[] otherFolder = (String[])(((FileForm)form).getOtheruploadfoldername());
			// iterate the array of string to set foldervo
			int[] otherFolderId = null;
			if (otherFolder != null)
			{
				int sizeOfOtherFolder = otherFolder.length;
					// iterate the array of string to set foldervo
				otherFolderId = new int[sizeOfOtherFolder];
				for( int i=0 ; i < sizeOfOtherFolder ; i ++ )
				{
					try
					{
						int indexOfHash = otherFolder[i].indexOf("#");
						if ( indexOfHash > 0){
							String folderId = otherFolder[i].substring(0, indexOfHash);
							String folderName = otherFolder[i].substring(indexOfHash+1, otherFolder[i].length());
							int intFolderId = Integer.parseInt(folderId);
							CvFolderVO folderVO = new CvFolderVO();
							folderVO.setFolderId(Integer.parseInt(folderId));
							folderVO.setName(folderName);
							fileVO.setVirtualFolderVO(folderVO);
							otherFolderId[i] = intFolderId;
						}
					} catch (Exception e) {
						System.out.println("[Exception][SaveEditFileHandler.execute] Exception Thrown: "+e);
						e.printStackTrace();
					}
				}
			}

			// update record to db

			int userId = userobjectd.getIndividualID();
			fileVO.setModifiedBy(userId);
			// call to file facade
			CvFileFacade fileFacade = new CvFileFacade();
			FormFile fileUpload = ((FileForm)form).getFile();

			if (fileUpload != null && !(fileUpload.equals("")))
			{
				fileVO.setName(fileUpload.getFileName());
				fileVO.setFileSize(fileUpload.getFileSize());
			}

			request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
			request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
			request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.EDIT);
			request.setAttribute(FileConstantKeys.WINDOWID, "1");
			request.setAttribute(FileConstantKeys.FFID, ""+fileVO.getFileId());

			if (request.getParameter("saveType").equals("save"))
				{
					request.setAttribute("bodycontent", "editdetailfile");
					FORWARD_final = FORWARD_editsavefile;
				}
				else // if (request.getParameter("saveType").equals("saveclose"))
				{
					request.setAttribute("bodycontent", null);
					FORWARD_final = FORWARD_editsaveclosefile;
				}
			try
			{
				fileFacade.updateFile(userId, otherFolderId, fileVO,fileUpload.getInputStream(), dataSource);

				if(fileUpload!=null && fileUpload.getFileName()!=null && (!fileUpload.getFileName().trim().equals("")) )	//CW: update the filename field to the new file name
				{

					((FileForm)form).setFileInfo(fileUpload.getFileName());
				}
			}
			catch(CvFileException e)
			{
				System.out.println("[Exception][SaveEditFileHandler.execute] Exception Thrown: "+e);
				ActionErrors allErrors = new ActionErrors();
				allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "This File Already Exists"));
				saveErrors(request, allErrors);
				FORWARD_final= FORWARD_editsavefile;
				request.setAttribute("bodycontent", "editdetailfile");
			}
	    }
		catch (Exception e)
	    {
	    	System.out.println("[Exception][SaveEditFileHandler.execute] Exception Thrown: "+e);
	    	e.printStackTrace();
	    	FORWARD_final = GLOBAL_FORWARD_failure;
	    }
        return mapping.findForward(FORWARD_final);
    }
    /**
     * Helper method - gets the Timestamp form data
     *
     * @param day String
     * @param month String
     * @param year String
     * @param timeZone String
     * @return Date
     */
    protected Timestamp getTimestamp(String day, String month, String year, String timeZone) {

        int startday   = Integer.parseInt(day);
        int startmonth = Integer.parseInt(month) - 1;
        int startyear  = Integer.parseInt(year);
        GregorianCalendar gc = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
        gc.set(startyear,startmonth,startday,0,0,0);
        return new  Timestamp(gc.getTimeInMillis());
    }

}
