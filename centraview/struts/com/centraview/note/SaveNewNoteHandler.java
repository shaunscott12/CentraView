/*
 * $RCSfile: SaveNewNoteHandler.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:32:13 $ - $Author: mcallist $
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

public class SaveNewNoteHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(SaveNewNoteHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_savenewnote = ".view.notes.savenewnote";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      HttpSession session = request.getSession();
      int individualId = ((UserObject) session.getAttribute("userobject")).getIndividualID();
      NoteVO noteVO = new NoteVO();
      noteVO.setTitle(((NoteForm)form).getTitle());
      noteVO.setDetail(((NoteForm)form).getDetail());
      int relateIndividualId = 0;
      if (((NoteForm)form).getIndividualid() != null && ((NoteForm) form).getIndividualid().length() > 0) {
        relateIndividualId = Integer.parseInt(((NoteForm)form).getIndividualid());
      }
      noteVO.setRelateIndividual(relateIndividualId);
      int relateEntityId = 0;
      if (((NoteForm) form).getEntityid() != null && ((NoteForm) form).getEntityid().length() > 0) {
        relateEntityId = Integer.parseInt(((NoteForm) form).getEntityid());
      }
      noteVO.setRelateEntity(relateEntityId);
      noteVO.setCreatedBy(individualId);
      noteVO.setOwner(individualId);

      int projectId = 0;
      if (request.getParameter("listFor") != null && request.getParameter("listFor").equals("Project")
          && request.getParameter("recordID") != null) {
        projectId = Integer.parseInt(request.getParameter("recordID").toString());
      }
      noteVO.setProjectId(projectId);

      // pass vo to ejb layer
      NoteHome noteHome = (NoteHome) CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
      Note noteRemote = noteHome.create();
      noteRemote.setDataSource(dataSource);
      noteRemote.addNote(individualId, noteVO);
      if (request.getParameter("closeornew").equals("close")) {
        request.setAttribute("closeWindow", "true");
      }
      request.setAttribute("refreshWindow", "true");

      String bottomFrame = request.getParameter("bottomFrame");
      if (bottomFrame != null && bottomFrame.equals("true")) {
        FORWARD_final = ".view.notes.relatednote";
      } else {
        FORWARD_final = FORWARD_savenewnote;
      }
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  } // end execute() method
} // end class definition