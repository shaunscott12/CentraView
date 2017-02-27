/*
 * $RCSfile: ViewNoteHandler.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:32:13 $ - $Author: mcallist $
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

package com.centraview.note;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class ViewNoteHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(ViewNoteHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ViewNoteHandler() {}

  /**
   * Executes initialization of required parameters and open window for entry of
   * note returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // initialization of required parameter
    try {
      HttpSession session = request.getSession();
      // get file id from request
      int noteId = 0;
      if (request.getParameter("rowId") != null) {
        noteId = Integer.parseInt(request.getParameter("rowId"));
      }
      int individualId = ((UserObject) session.getAttribute("userobject")).getIndividualID();
      request.setAttribute("RECORDOPERATIONRIGHT", new Integer(CVUtility.getRecordPermission(individualId, "Notes",
          noteId, dataSource)));
      // get the data from db thru ejb
      NoteHome noteHome = (NoteHome) CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
      Note noteRemote = noteHome.create();
      noteRemote.setDataSource(dataSource);
      NoteVO noteVO = noteRemote.getNote(individualId, noteId);
      // set the form bean data from VO
      NoteForm noteForm = (NoteForm) form;

      noteForm.setNoteId(String.valueOf(noteVO.getNoteId()));
      noteForm.setTitle(noteVO.getTitle());
      noteForm.setDetail(noteVO.getDetail());
      noteForm.setCreatedid(String.valueOf(noteVO.getCreatedBy()));
      noteForm.setCreatedby(noteVO.getCreatedByVO().getFirstName() + " " + noteVO.getCreatedByVO().getLastName());

      DateFormat df = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss a");
      Timestamp createdTimestamp = noteVO.getCreatedOn();
      String createdDateString = "";
      if (createdTimestamp != null) {
        Date valueDate = new Date(createdTimestamp.getTime());
        createdDateString = df.format(valueDate);
      }
      noteForm.setCreateddate(createdDateString);

      Timestamp modifiedTimestamp = noteVO.getModifiedOn();
      String modyfiedDateString = "";
      if (modifiedTimestamp != null) {
        Date valueDate = new Date(modifiedTimestamp.getTime());
        modyfiedDateString = df.format(valueDate);
      }
      noteForm.setModifieddate(modyfiedDateString);

      if (noteVO.getRelateEntity() > 0) {
        noteForm.setEntityid(String.valueOf(noteVO.getRelateEntity()));
      }

      if (noteVO.getRelateEntityName() != null) {
        noteForm.setEntityname(noteVO.getRelateEntityName());
      }

      if (noteVO.getRelateIndividual() > 0) {
        noteForm.setIndividualid(String.valueOf(noteVO.getRelateIndividual()));
      }

      if (noteVO.getRelateIndividualName() != null) {
        noteForm.setIndividualname(noteVO.getRelateIndividualName());
      }

      if (request.getParameter("bottomFrame") != null && request.getParameter("bottomFrame").equals("true")) {
        FORWARD_final = ".view.notes.relatednote";
      } else {
        boolean listScopeAllFlag = false;
        if (request.getParameter("listScope") != null && request.getParameter("listScope").equalsIgnoreCase("all")) {
          listScopeAllFlag = true;
        }
        // if edit
        FORWARD_final = ".view.notes.editnote.my";
        if (listScopeAllFlag) {
          FORWARD_final = ".view.notes.editnote.all";
        }
        // if duplicate
        //FORWARD_final = ".view.notes.duplicatenote";
      }
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  } // end execute() method
}