/*
 * $RCSfile: ViewFileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/05/12 20:15:28 $ - $Author: mking_cv $
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

import java.util.Calendar;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class ViewFileHandler extends org.apache.struts.action.Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  
  private static final String FORWARD_viewfile = ".view.files.editfile";

  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      // set request
      request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
      request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
      request.setAttribute(FileConstantKeys.TYPEOFOPERATION, FileConstantKeys.EDIT);
      request.setAttribute(FileConstantKeys.WINDOWID, "1");
      // set to display the view detail in same home page template format
      request.setAttribute("bodycontent", "editdetailfile");
      
      String fileTypeRequest  = (String)request.getParameter("TYPEOFFILELIST") ;//Check which is the requested listing all or my
      if ( fileTypeRequest == null ) {
        fileTypeRequest = "MY";
      }
      request.setAttribute("fileTypeRequest" , fileTypeRequest );
      
      // get file id from request
      int fileId = 0;
      if (request.getParameter("rowId") != null) {
        fileId = Integer.parseInt(request.getParameter("rowId"));
      }
      
      HttpSession session = request.getSession();
      int userId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      // get the data from db thru ejb
      CvFileFacade fileFacade = new CvFileFacade();
      
      CvFileVO fileVO = fileFacade.getFile(userId, fileId, dataSource);
      // set the form bean data from VO
      if (fileVO.getCompanyNews().equals("YES")) {
        Calendar fromcalendar =Calendar.getInstance();
        Calendar tocalendar = Calendar.getInstance();
        fromcalendar.setTime(fileVO.getFrom());
        tocalendar.setTime(fileVO.getTo());
        
        ((FileForm)form).setCompanynews("YES");
        ((FileForm)form).setStartday("" + fromcalendar.get(5));
        ((FileForm)form).setStartmonth("" + (fromcalendar.get(2) + 1));
        ((FileForm)form).setStartyear("" + fromcalendar.get(1));
        ((FileForm)form).setEndday("" + tocalendar.get(5));
        ((FileForm)form).setEndmonth("" + (tocalendar.get(2) + 1));
        ((FileForm)form).setEndyear("" + tocalendar.get(1));
      } else {
        ((FileForm)form).setCompanynews("NO");
        ((FileForm)form).setStartday("");
        ((FileForm)form).setStartmonth("");
        ((FileForm)form).setStartyear("");
        ((FileForm)form).setEndday("");
        ((FileForm)form).setEndmonth("");
        ((FileForm)form).setEndyear("");
      }
      request.setAttribute(FileConstantKeys.FFID, ""+fileId);
      ((FileForm)form).setTitle(fileVO.getTitle());
      ((FileForm)form).setFileId(""+fileVO.getFileId());
      
      ((FileForm)form).setFileInfo(fileVO.getName());
      
      String folderID = fileVO.getPhysicalFolder()+"";
      
      if (((FileForm)form).getUploadfolderid()!=null && !((FileForm)form).getUploadfolderid().equals("")) {
        String folderId = ((FileForm)form).getUploadfolderid();
        CvFolderVO tempFolderVO = fileFacade.getFolder(userId, new Integer(folderId).intValue(), dataSource);
        ((FileForm)form).setUploadfoldername(tempFolderVO.getFullPath(null, false));
        ((FileForm)form).setUploadfolderid(new Integer(tempFolderVO.getFolderId()).toString());
      } else {
        ((FileForm)form).setUploadfolderid(""+fileVO.getPhysicalFolder());
        ((FileForm)form).setUploadfoldername(fileVO.getPhysicalFolderVO().getName());
      }
      
      ((FileForm)form).setDescription(fileVO.getDescription());
      ((FileForm)form).setCustomerview(fileVO.getCustomerView());
      
      if (fileVO.getOwner() > 0) {
        ((FileForm)form).setOwnerid(""+fileVO.getOwner());
      }
      
      if (fileVO.getOwnerVO() != null) {
        ((FileForm)form).setOwnername(fileVO.getOwnerVO().getFirstName() + " " + fileVO.getOwnerVO().getLastName());
      }
      
      
      if (fileVO.getAuthorId() > 0) {
        ((FileForm)form).setAuthorid(""+fileVO.getAuthorId());
      }
      
      if (fileVO.getAuthorVO() != null) {
        ((FileForm)form).setAuthorname(fileVO.getAuthorVO().getFirstName() + " " + fileVO.getAuthorVO().getLastName());
      }
      
      
      ((FileForm)form).setAccess(fileVO.getVisibility());
      
      
      if (fileVO.getCreatedOn() != null) {
        String createdDate=		getDateName(fileVO.getModifiedOn().getMonth())+" "+fileVO.getModifiedOn().getDate()+", "+(fileVO.getModifiedOn().getYear()+1900);
        ((FileForm)form).setCreated(createdDate);
      }
      
      if (fileVO.getModifiedOn() != null) {
        String modifiedDate=		getDateName(fileVO.getModifiedOn().getMonth())+" "+fileVO.getModifiedOn().getDate()+", "+(fileVO.getModifiedOn().getYear()+1900);
        ((FileForm)form).setModified(modifiedDate);
      }
      
      ((FileForm)form).setRelatedFieldID(fileVO.getRelatedFieldID());
      ((FileForm)form).setRelatedFieldValue(fileVO.getRelatedFieldValue());
      ((FileForm)form).setRelatedTypeID(fileVO.getRelatedTypeID());
      ((FileForm)form).setRelatedTypeValue(fileVO.getRelatedTypeValue());
      ((FileForm)form).setFileversion(fileVO.getVersion());
      
      if (fileVO.getRelateEntity() > 0) {
        ((FileForm)form).setEntityid(""+fileVO.getRelateEntity());
      }
      
      if (fileVO.getRelateEntityVO() != null) {
        ((FileForm)form).setEntityname(fileVO.getRelateEntityVO().getName());
      }
      
      // individual vo
      if (fileVO.getRelateIndividual() > 0) {
        ((FileForm)form).setIndividualid(""+fileVO.getRelateIndividual());
      }
      
      if (fileVO.getRelateIndividualVO() != null) {
        ((FileForm)form).setIndividualname(fileVO.getRelateIndividualVO().getFirstName() + " " + fileVO.getRelateIndividualVO().getLastName());
      }
      
      
      // get virtual folder
      Vector otherFolderVec = new Vector();
      if (fileVO.getVirtualFolderVO() != null) {
        for (int i=0;i<fileVO.getVirtualFolderVO().size();i++) {
          CvFolderVO folderVO = new CvFolderVO();
          folderVO = (CvFolderVO) fileVO.getVirtualFolderVO().get(i);
          
          DDNameValue otherFolderDDNameValue = new DDNameValue(""+folderVO.getFolderId() + "#" + folderVO.getName(), folderVO.getName());
          otherFolderVec.add(otherFolderDDNameValue);
        }
      }
      ((FileForm)form).setOtheruploadfoldernamevec(otherFolderVec);
      
      request.setAttribute("RECORDOPERATIONRIGHT",new Integer(CVUtility.getRecordPermission(userId, "File", fileId, dataSource)));
      
      request.setAttribute("folderId", folderID);
      request.setAttribute("fileform", form);
      
      FORWARD_final = FORWARD_viewfile;
    } catch (Exception e) {
      System.out.println("[Exception][ViewFileHandler.execute] Exception Thrown: "+e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
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
