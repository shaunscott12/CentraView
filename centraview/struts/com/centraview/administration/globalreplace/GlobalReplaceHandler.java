/*
 * $RCSfile: GlobalReplaceHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:41 $ - $Author: mking_cv $
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

package com.centraview.administration.globalreplace;

import java.io.IOException;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Forwards control to the /administration/global_replace_search.do
 * URI. Also removes "globalReplaceForm" from the session.
 * @author Naresh Patel <npatel@centraview.com>
 */
public class GlobalReplaceHandler extends Action
{
  private static Logger logger = Logger.getLogger(GlobalReplaceHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException,NamingException
  {
    // initalize the GlobalReplace Form
    HttpSession session = request.getSession(true);
    session.removeAttribute("globalReplaceForm");
    return mapping.findForward(".forward.administration.global_replace.search");
  }

}

