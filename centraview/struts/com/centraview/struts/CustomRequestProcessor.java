/*
 * $RCSfile: CustomRequestProcessor.java,v $    $Revision: 1.2 $  $Date: 2005/08/29 20:21:24 $ - $Author: mcallist $
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

package com.centraview.struts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.tiles.TilesRequestProcessor;

import com.centraview.common.CVUtility;
import com.centraview.common.Globals;
import com.centraview.common.UIAttributes;

/**
 * A simple RequestProcessor to grab the UIAttributes for the
 * view that is about to be displayed and make them available on the
 * Request.
 * @author CentraView, LLC <info@centraview.com>
 */
public class CustomRequestProcessor extends TilesRequestProcessor
{
  private static Logger logger = Logger.getLogger(CustomRequestProcessor.class);
  protected void processForwardConfig(HttpServletRequest request, HttpServletResponse response, ForwardConfig forward) throws IOException, ServletException {
    // For this thread set the MDC for logging (I hope this is inexpensive):
    String host = CVUtility.getHostName(servlet.getServletContext());
    // use log4j Mapped diagnostic context to uniquely identify
    // each threads log entries by host
    MDC.put("HOSTNAME", host);
    // Set up the UI attributes
    String key = null;
    UIAttributes attributes = null;
    if (forward != null) {
      key = forward.getName();
      attributes = (UIAttributes)Globals.UIATTRIBUTES.get(key);
      if (logger.isDebugEnabled() && System.getProperty(Globals.DEBUG) != null) {
        logger.debug("[processForwardConfig] forward name: " + key);
      }
    }
    if (attributes == null) {
      attributes = Globals.DEFAULT_ATTRIBUTES;
    }
    request.setAttribute("uiAttributes", attributes);
    super.processForwardConfig(request, response, forward);
  }
}