/*
 * $RCSfile: TemplateComposeHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:09 $ - $Author: mking_cv $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * First step in using a Template to compose a new email message;
 * simply sets a request attribute "composeFromTemplate" to true
 * then forwards execution control to /mail/EditDraft.do which will
 * populate an emailForm bean and forward control to /mail/Compose.do.
 * EditDraft.do will check the "composeFromTemplate" attribute and
 * tell Compose.do to NOT delete the Template after sending, whereas
 * a Draft would have been deleted.
 */
public class TemplateComposeHandler extends org.apache.struts.action.Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    request.setAttribute("composeFromTemplate", new Boolean(true));
    return(mapping.findForward("editDraft"));
  }
}
