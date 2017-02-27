/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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

package com.centraview.contacts.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * A dispatch handler which checks the "operation" parameter
 * of the form bean, and decides whether to forward to
 * /contacts/edit_contact_method.do or /contacts/add_contact_method.do.
 */
public class SaveContactMethodHandler extends org.apache.struts.action.Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    DynaActionForm dynaForm = (DynaActionForm)form;
    String operation = (String)dynaForm.get("operation");

    String forward = "edit_contact_method";
    if (operation != null && operation.equals("edit")) {
      forward = "edit_contact_method";
    }else if (operation != null && operation.equals("add")){
      forward = "add_contact_method";
    }
    return mapping.findForward(forward);
  }

}

