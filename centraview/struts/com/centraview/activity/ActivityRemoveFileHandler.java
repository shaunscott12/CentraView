/*
 * $RCSfile: ActivityRemoveFileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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

import java.io.IOException;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.DDNameValue;

public class ActivityRemoveFileHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(ActivityRemoveFileHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException
  {
    try {
      String fileIdName = request.getParameter("listSelectedFileId");

      Vector fileListVec = new Vector();
      String[] fileListArray = ((ActivityForm)form).getActivityFilesList();

      if (fileListArray != null) {
        int sizeOfListFile = fileListArray.length;

        for (int i = 0; i < sizeOfListFile; i++) {
          int indexOfHash = fileListArray[i].indexOf("#");
          String id = fileListArray[i].substring(0, indexOfHash);
          String name = fileListArray[i].substring(indexOfHash + 1, fileListArray[i].length());
          DDNameValue ddFileIdName = new DDNameValue(id + "#" + name, name);

          if (!(fileIdName.equals(ddFileIdName.getStrid()))) {
            fileListVec.add(ddFileIdName);
          }
        }
      }

      ((ActivityForm)form).setActivityFilesListVec(fileListVec);

      int fileListSize = fileListVec.size();
      fileListArray = new String[fileListSize];

      for (int i = 0; i < fileListSize; i++) {
        DDNameValue idName = (DDNameValue)fileListVec.get(i);
        fileListArray[i] = idName.getStrid();
      }

      ((ActivityForm)form).setActivityFilesList(fileListArray);

      // populate form bean for previous sub-activity
      PopulateForm populateForm = new PopulateForm();
      // set the form elements
      populateForm.setForm(request, form);
      // set form with respect to new opening page
      form = populateForm.getForm(request, form, ConstantKeys.AVAILABILITY);

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

      request.setAttribute(ConstantKeys.TYPEOFACTIVITY, request
          .getParameter(ConstantKeys.TYPEOFACTIVITY));
      request.setAttribute(ConstantKeys.TYPEOFSUBACTIVITY, ConstantKeys.ATTACHMENT);
      request.setAttribute(ConstantKeys.TYPEOFOPERATION, request
          .getParameter(ConstantKeys.TYPEOFOPERATION));

    } catch (Exception e) {
      logger.error("[Exception] ActivityRemoveFileHandler.Execute Handler ", e);
    }
    return (mapping.findForward(".view.activities.attachments"));
  }

}
