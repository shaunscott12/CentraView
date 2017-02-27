/*
 * $RCSfile: DuplicateHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/06 19:04:29 $ - $Author: mcallist $
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
 
package com.centraview.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class DuplicateHandler extends org.apache.struts.action.Action 
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException   
  {
    if (request.getParameter(Constants.TYPEOFCONTACT).toString().equals(Constants.ENTITY)) {
      return (mapping.findForward("DuplicateEntity"));
    } else if  (request.getParameter(Constants.TYPEOFCONTACT).toString().equals(Constants.INDIVIDUAL)) {
      return (mapping.findForward("DuplicateIndividual"));
    } else if  (request.getParameter(Constants.TYPEOFCONTACT).toString().equals(Constants.GROUP)) {
      return (mapping.findForward("DuplicateGroup"));
    }      
    throw new ServletException(Constants.ACTION_INVALID);
  }
  
}
