/*
 * $RCSfile: DisplayNewCustomFieldHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:39 $ - $Author: mking_cv $
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

package com.centraview.administration.customfield;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;

public class DisplayNewCustomFieldHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    try {
      DynaActionForm customFieldForm = (DynaActionForm) form;

      String action = (String) request.getParameter("actionType");
      String submodule = (String) request.getParameter("module");

      request.setAttribute(AdministrationConstantKeys.TYPEOFSUBMODULE, AdministrationConstantKeys.CUSTOMFIELD);
      request.setAttribute("body", AdministrationConstantKeys.ADD);

      request.setAttribute("actionType", action);
      request.setAttribute("typeofmodule", AdministrationConstantKeys.MODULESETTINGS);
      request.setAttribute("typeofsubmodule", submodule);
      request.setAttribute("module", submodule);
      
      customFieldForm.set("fieldname", "");
      customFieldForm.set("layerSwitch", "SCALAR");
      request.setAttribute("Dyna", customFieldForm);

    } catch (Exception e) {
      System.out.println("[Exception] DisplayNewCustomFieldHandler.execute: " + e.toString());
    }
    return (mapping.findForward(".view.administration.new_custom_field"));
  }
}
