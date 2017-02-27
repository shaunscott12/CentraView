/*
 * $RCSfile: AttachFileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:05 $ - $Author: mking_cv $
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

package com.centraview.mail;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileVO;
import com.centraview.file.CvFolderVO;
import com.centraview.settings.Settings;

public class AttachFileHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession(true);
      FileForm fileform = (FileForm)form;
      if ((request.getParameter("From") != null) && (request.getParameter("From").equals("Local")))
      {

        UserObject userobjectd = (UserObject) session.getAttribute("userobject");
        int userid = userobjectd.getIndividualID();

        FormFile ff = (FormFile) fileform.getFile();
        String fileName = ff.getFileName();
        InputStream im = ff.getInputStream();

        CvFileFacade cvfile = new CvFileFacade();
        CvFileVO flvo = new CvFileVO();
        flvo.setTitle(fileName); //file name

        Calendar c = Calendar.getInstance();
        java.util.Date dt = c.getTime();
        DateFormat df = new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss");
        String dateStamp = df.format(dt);

        flvo.setName("attachment_" + dateStamp +"_"+ fileName);
        flvo.setCreatedBy(userid);
        flvo.setOwner(userid);
        flvo.setAuthorId(userid);
        flvo.setFileSize((float) ff.getFileSize());

        CvFolderVO homeFolder = cvfile.getHomeFolder(userid, dataSource);
        int fileId = cvfile.addFile(userid, homeFolder.getFolderId(), flvo, im, dataSource);
        ArrayList fileList = new ArrayList();
        fileList.add(new DDNameValue(fileId+"#"+fileName,fileName));

				String[] files = fileform.getFileList();
				if(files != null){
					for(int i=0; i<files.length;i++){
						String fileKeyName = files[i];
						if(fileKeyName != null){
							int indexOfHash = fileKeyName.indexOf("#");
							if (indexOfHash != -1){
								int lenString = fileKeyName.length();
								String fileID = fileKeyName.substring(0,indexOfHash);
								String fileName1 = fileKeyName.substring(indexOfHash+1,lenString);
								fileList.add(new DDNameValue(fileID+"#"+fileName1,fileName1));
							}
						}
					}
				}
        request.setAttribute("fileList",fileList);
      } else {
        String fileId = "";
        String fileName = "";

        if (request.getParameter("fileId") != null) {
          fileId = request.getParameter("fileId");
        }

        if (request.getParameter("fileName") != null) {
          fileName = request.getParameter("fileName");
        }
         
        ArrayList fileList = new ArrayList();
        if (fileId != null && !fileId.equals("") && fileName != null && !fileName.equals(""))
          fileList.add(new DDNameValue(fileId+"#"+fileName,fileName));

				String[] files = (String[])fileform.getFileList();
				if(files != null){
					for(int i=0; i<files.length;i++){
						String fileKeyName = files[i];
						if(fileKeyName != null){
							int indexOfHash = fileKeyName.indexOf("#");
							if (indexOfHash != -1){
								int lenString = fileKeyName.length();
								String fileID = fileKeyName.substring(0,indexOfHash);
								String fileName1 = fileKeyName.substring(indexOfHash+1,lenString);
								
								fileList.add(new DDNameValue(fileID+"#"+fileName1,fileName1));
							}
						}
					}
				}
        request.setAttribute("fileList", fileList);
      }
    }catch(Exception e){
      System.out.println("[Exception][AttachmentHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward(".view.email.attachmentlookup"));
  }

}

