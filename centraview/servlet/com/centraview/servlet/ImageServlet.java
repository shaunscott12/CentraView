/*
 * $RCSfile: ImageServlet.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:11 $ - $Author: mking_cv $
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

package com.centraview.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.file.CvFileFacade;
import com.centraview.settings.Settings;

/**
 * We will collect the File Id from the ViewMessage.jsp Page and serve the request
 * We will get the image buffer and write it to ServletOutputStream.
 *
 * @author Naresh Patel <npatel@centraview.com>
 */
public class ImageServlet extends HttpServlet
{

  private static Logger logger = Logger.getLogger(ImageServlet.class);

  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
  	try{
	    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(this.getServletContext())).getDataSource();

	    HttpSession session = request.getSession(true);
	    UserObject userObject = (UserObject)session.getAttribute("userobject");
	    int individualID = userObject.getIndividualID();

	    String fileId = request.getParameter("fileID");
	    int fileID = 0;
	    if(fileId != null && !fileId.equals("")&& !fileId.equals("null")){
	    	fileID = Integer.parseInt(fileId);
	    }//end of if(fileId != null && !fileId.equals("")&& !fileId.equals("null"))
	    if(fileID > 0){
			CvFileFacade fileFacade = new CvFileFacade();

			HashMap imageInfo = fileFacade.getFileInputStream(individualID, fileID, dataSource);
			byte[] buffer = (byte[])imageInfo.get("imageBuffer");
			String fileName = (String)imageInfo.get("fileName");
			int size = 0;
			if(buffer != null){
				size = buffer.length;
			}
		    if (size > 0)
		    {
		      response.setContentType(this.getMimeType(fileName));
		    }
		    
		    // send the output.
		    ServletOutputStream outStream = response.getOutputStream();
		    BufferedOutputStream bufferedOutStream = new BufferedOutputStream(outStream);
		    bufferedOutStream.write(buffer, 0, buffer.length);
		    bufferedOutStream.flush();
		    bufferedOutStream.close();
	    }//end of if(fileID > 0)
  	}
  	catch(Exception e){
  		logger.error("[ImageServlet] Exception thrown in service(): ", e);
  	}
  }//end of service
  
  /**
   * guess the mime type of the image based on the extention of
   * the imageType passed in.  Currently handles png,gif,jpg,jpeg.
   * @param logoName the full name of the customer logo
   * @return a guessed mime type in a string.
   */
  private String getMimeType(String logoName)
  {
    String lowerLogoName = logoName.toLowerCase();
    String mimeType = null;
    if (lowerLogoName.matches(".*\\.gif$"))
    {
      mimeType = "image/gif";
    } else if (lowerLogoName.matches(".*\\.png$")) {
      mimeType = "image/png";
    } else if (lowerLogoName.matches(".*\\.jpg$") || lowerLogoName.matches(".*\\.jpeg$")) {
      mimeType = "image/jpeg";
    }
    return mimeType;
  }  
}//end of class ImageServlet
