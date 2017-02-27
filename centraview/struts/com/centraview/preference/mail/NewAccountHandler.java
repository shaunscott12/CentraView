/*
 * $RCSfile: NewAccountHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/02 20:14:50 $ - $Author: mcallist $
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

package com.centraview.preference.mail;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.preference.common.AdminConstantKeys;


/**
 * Handles the request for NewEmailAccount.do which simply
 * displays a blank form for the user to enter email account
 * information in order to create a new email account.
 */
public class NewAccountHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "NEWEMAIL");
    // default "port" to 25 unless it's given
    DynaActionForm accountForm = (DynaActionForm)form;
    Integer port = (Integer)accountForm.get("port");
    if (port == null || port.intValue() <= 0) {
      port = new Integer(25);
    }
    accountForm.set("port", port);
    return(mapping.findForward(".view.preference.mail.new_account"));
  }   // end execute() method
}   // end class definition

