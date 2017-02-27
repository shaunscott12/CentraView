/*
 * $RCSfile: CustomerLogo.java,v $    $Revision: 1.2 $  $Date: 2005/07/14 16:38:57 $ - $Author: mcallist $
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
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;
import com.centraview.settings.SiteInfo;

/**
 * The customer logo servlet reads in the image pointed to by the SiteInfo object
 * and streams the data to the client.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class CustomerLogo extends HttpServlet
{
  
  private static Logger logger = Logger.getLogger(CustomerLogo.class);
  /** 1x1 transparent gif of last resort (43 well earned bytes) */
  private static final byte[] spacerGif = new byte[] {
      (byte)0x47, (byte)0x49, (byte)0x46, (byte)0x38, (byte)0x39, (byte)0x61, (byte)0x01, 
      (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x80, (byte)0xFF, (byte)0x00, (byte)0xC0, 
      (byte)0xC0, (byte)0xC0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x21, (byte)0xF9, 
      (byte)0x04, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x2C, 
      (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, 
      (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x02, (byte)0x44, (byte)0x01, (byte)0x00, 
      (byte)0x3B };
  /**
   * This service method will read in the image pointed to by the SiteInfo object
   * set the mime type on the output stream and stream out the image.
   */
  protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    ServletContext servlet = super.getServletContext();
    SiteInfo siteInfo = Settings.getInstance().getSiteInfo(CVUtility.getHostName(servlet));
    File customerLogo = siteInfo.getCustomerLogo();
    byte[] buffer = null;
    // if size is still zero later, we will write out a hardcoded image.
    int size = 0;
    if (siteInfo.isCustomerLogoDirty())
    {
      // Catch the IOException on this FileInputStream constructor and
      // on the read method so we can set size to zero to fall back.
      DataInputStream customerLogoStream = null;
      FileInputStream fileInputStream = null;
      try 
      {
        fileInputStream = new FileInputStream(customerLogo);
        customerLogoStream = new DataInputStream(fileInputStream);
        long length = customerLogo.length();
        // make sure the long isn't bigger than maximum int value.
        if (length <= Integer.MAX_VALUE)
        {
          buffer = new byte[(int)length];
          customerLogoStream.readFully(buffer);
          // Cache the data.
          siteInfo.setCustomerLogoData(buffer);
          siteInfo.setCustomerLogoDirty(false);
          size = buffer.length;
        }
      } catch (IOException e) {
        logger.error("[service] IOException Reading CustomerLogoFile, falling back to safe default.  "+e.toString());
        size = 0;
      } finally {
        try {
          customerLogoStream.close();
          fileInputStream.close();
        } catch (Exception e) {}
      }
    } else {
      buffer = siteInfo.getCustomerLogoData();
      size = buffer.length;
    }
    if (size > 0)
    {
      response.setContentType(this.getMimeType(customerLogo.getName()));
    } else {
      response.setContentType("image/gif");
      buffer = spacerGif;
    }
    // send the output.
    ServletOutputStream outStream = response.getOutputStream();
    BufferedOutputStream bufferedOutStream = new BufferedOutputStream(outStream);
    bufferedOutStream.write(buffer, 0, buffer.length);
    bufferedOutStream.flush();
    bufferedOutStream.close();
  }
  
  /**
   * guess the mime type of the image based on the extention of
   * the logoname passed in.  Currently handles png,gif,jpg,jpeg.
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
}
;