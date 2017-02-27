/*
 * $RCSfile: AttachmentActivityHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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

package com.centraview.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class AttachmentActivityHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(AttachmentActivityHandler.class);
  public static final String GLOBAL_FORWARD_failure = ".view.error";
  private static final String FORWARD_attachment = ".view.activities.attachments";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    try {
      // populate form bean for previous sub-activity
      PopulateForm populateForm = new PopulateForm();
      // set the form elements
      populateForm.setForm(request, form);
      // set form with respect to new opening page
      form = populateForm.getForm(request, form, ConstantKeys.ATTACHMENT);

      if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.APPOINTMENT)) {
        request.setAttribute("actionName", "title.contact.appointment");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.CALL)) {
        request.setAttribute("actionName", "title.contact.call");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.MEETING)) {
        request.setAttribute("actionName", "title.contact.meeting");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.NEXTACTION)) {
        request.setAttribute("actionName", "title.contact.nextaction");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.TASK)) {
        request.setAttribute("actionName", "title.contact.task");
      } else if (request.getParameter(ConstantKeys.TYPEOFACTIVITY).equals(ConstantKeys.TODO)) {
        request.setAttribute("actionName", "title.contact.todo");
      }

      request.setAttribute(ConstantKeys.TYPEOFSUBACTIVITY, ConstantKeys.ATTACHMENT);
      request.setAttribute(ConstantKeys.TYPEOFACTIVITY, request
          .getParameter(ConstantKeys.TYPEOFACTIVITY));
      request.setAttribute(ConstantKeys.TYPEOFOPERATION, request
          .getParameter(ConstantKeys.TYPEOFOPERATION));
      FORWARD_final = FORWARD_attachment;
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }

}
