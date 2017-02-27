/*
 * $RCSfile: SaveNewFileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveNewFileHandler extends org.apache.struts.action.Action {

    /*
	 *	Global Forwards for exception handling
	 */
    public static final String GLOBAL_FORWARD_failure = "failure";

    /*
     *	Local Forwards for redirecting to jsp addedit_folder_c
     */
    private static final String FORWARD_savefile = ".view.files.newfile";

	/*
	 *	Redirect constant
	 */
    private static String FORWARD_final = GLOBAL_FORWARD_failure;

	/*
	 *	Executes initialization of required parameters and open window for entry of note
	 *	returns ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
    	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		HttpSession session = request.getSession();
		int rowID=0;
	    try
	    {
			// call ejb to insert record
			// initialize file vo
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );
			String timeZone = userobjectd.getUserPref().getTimeZone();
			CvFileVO fileVO = new CvFileVO();

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

			// get file
			FormFile fileUpload = ((FileForm)form).getFile();
			// set file vo
			fileVO.setName(fileUpload.getFileName());
			fileVO.setFileSize(fileUpload.getFileSize());
			fileVO.setPhysicalFolder(Integer.parseInt(((FileForm)form).getUploadfolderid()));
			fileVO.setTitle(((FileForm)form).getTitle());
			fileVO.setDescription(((FileForm)form).getDescription());

			if(((FileForm)form).getAuthorid() != null && ((FileForm)form).getAuthorid().length() > 0)
				fileVO.setAuthorId(Integer.parseInt(((FileForm)form).getAuthorid()));

			if(((FileForm)form).getEntityid() != null && ((FileForm)form).getEntityid().length() > 0)
				fileVO.setRelateEntity(Integer.parseInt(((FileForm)form).getEntityid()));

			if(((FileForm)form).getIndividualid() != null && ((FileForm)form).getIndividualid().length() > 0)
				fileVO.setRelateIndividual(Integer.parseInt(((FileForm)form).getIndividualid()));

			fileVO.setRelatedFieldID(((FileForm)form).getRelatedFieldID());
			fileVO.setRelatedFieldValue(((FileForm)form).getRelatedFieldValue());
			fileVO.setRelatedTypeID(((FileForm)form).getRelatedTypeID());
			fileVO.setRelatedTypeValue(((FileForm)form).getRelatedTypeValue());
			fileVO.setVisibility(((FileForm)form).getAccess());
			fileVO.setCustomerView(((FileForm)form).getCustomerview());
			fileVO.setCompanyNews(((FileForm)form).getCompanynews());

			int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
			fileVO.setCreatedBy(userId);
			fileVO.setOwner(userId);
			// call to file facade
			CvFileFacade fileFacade = new CvFileFacade();

			// set request back to jsp
	    	request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
	    	request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
	    	request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.ADD);
	    	request.setAttribute(FileConstantKeys.WINDOWID, "1");

			if (request.getParameter("closeornew").equals("close"))
			{
				request.setAttribute("closeWindow","true");
			}
			else
			{
				request.setAttribute("closeWindow","false");
			}

	    	FORWARD_final = FORWARD_savefile;

			try
			{
			 int recordID=fileFacade.addFile(userId, fileVO.getPhysicalFolder(), fileVO, fileUpload.getInputStream(), dataSource);
			 rowID=recordID;
			}
			catch(CvFileException e)
			{
				System.out.println("[Exception][SaveNewFileHandler.execute] Exception Thrown: "+e);
				ActionErrors allErrors = new ActionErrors();
				allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "File With This Name Already Exists in this path"));
				saveErrors(request, allErrors);
				request.setAttribute("closeWindow","false");
			}

			if (!(request.getParameter("closeornew").equals("close")) && (request.getAttribute("ExistFileError") == null))
			{
				((FileForm)form).setTitle("");
				((FileForm)form).setDescription("");
				((FileForm)form).setEntityname("");
				((FileForm)form).setAuthorname("");
				((FileForm)form).setIndividualname("");
				((FileForm)form).setCustomerview(FileConstantKeys.DEFAULTCUSTOMERVIEW);
				((FileForm)form).setAccess(FileConstantKeys.DEFAULTACCESS);
                ((FileForm)form).setStartday("");
                ((FileForm)form).setStartmonth("");
                ((FileForm)form).setStartyear("");
                ((FileForm)form).setEndday("");
                ((FileForm)form).setEndmonth("");
                ((FileForm)form).setEndyear("");
			}
			request.setAttribute("refreshWindow", "true");
	    }
		 catch (Exception e)
	    {
			System.out.println("[Exception][SaveNewFileHandler.execute] Exception Thrown: "+e);
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
