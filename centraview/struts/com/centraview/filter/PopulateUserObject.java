/*
 * $RCSfile: PopulateUserObject.java,v $    $Revision: 1.4 $  $Date: 2005/09/01 17:55:12 $ - $Author: mcallist $
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

package com.centraview.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.centraview.common.UserObject;

public class PopulateUserObject implements Filter
{
  // Collection of URLs for which we do not require a valid logged-in session
  private static String[] validUrls;
  static
  {
    PopulateUserObject.validUrls = new String[7];
    PopulateUserObject.validUrls[0] = "/CustomerLogin.do";
    PopulateUserObject.validUrls[1] = "/customer_login.do";
    PopulateUserObject.validUrls[2] = "/monitor.do";
    PopulateUserObject.validUrls[3] = "/show_terms.do";
    PopulateUserObject.validUrls[4] = "/start.do";
    PopulateUserObject.validUrls[5] = "/forgot.do";
    PopulateUserObject.validUrls[6] = "/customer/login.do";
  }

  /**
   * Check if user object resides in session on each request.
   * If not present, will forward to login JSP.
   * @param request   The ServletRequest object passed in from the container.
   * @param response  The ServletResponse object passed in from the container.
   * @param chain     The Filterchain object passed in from the container.
   * @return          void
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException
  {
    HttpServletRequest req = (HttpServletRequest)request;
    HttpServletResponse res = (HttpServletResponse)response;

    // Check to see if we are currently trying to log in.  If so, then let the request go.
    if ((request.getParameter("username") == null) && (request.getParameter("password") == null)) {
      // well we aren't trying to login in an obvious way, so check the session for a null UserObject
      HttpSession session = req.getSession(true);
      if ((UserObject)session.getAttribute("userobject") == null) {
        // Apparently we don't have a userObject.  Is this request trying to go to
        // a screen which doesn't require a valid logged-in user with a session?
        String servletPath = req.getServletPath();

        boolean isValidUrl = false;
        for (int i = 0; i < PopulateUserObject.validUrls.length; i++) {
          if (servletPath.equals(PopulateUserObject.validUrls[i])) {
            isValidUrl = true;
          }
        }

        if (! isValidUrl) {
          // nope.  Go Directly to Jail. Do not pass Go. Do not collect $200.
          req.getRequestDispatcher(res.encodeURL("/start.do")).forward(req, res);
        }
      } else if (session.getAttribute("expiredLicense") != null) {
        // okay, so we have a non null userobject on the session, BUT the license is expired
        // So the admin may be trying to dance without paying the piper.  But the chisler
        // didn't count on this filter.  He had better be trying to view or save the license and
        // that is all, or else there will be a repeat of that business that occurred in Hamelin.
        String requestURL = req.getServletPath();
        // SaveLicense.do or DisplayLicense.do or logout.do
        if (!(requestURL.matches("^/\\S+License.do$") || requestURL.matches("^/logout.do$"))) {
          // get back there and pay me!
          req.getRequestDispatcher(res.encodeURL("/DisplayLicense.do")).forward(req, res);
        }
      }
    }
    chain.doFilter(req, res);
  }   // end doFilter()

  public void init(FilterConfig filterconfig)	throws ServletException { }

  public void destroy() { }

}   // end class definition

