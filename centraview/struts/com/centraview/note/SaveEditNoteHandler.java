/*
 * $RCSfile: SaveEditNoteHandler.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:32:13 $ - $Author: mcallist $
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

public class SaveEditNoteHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(SaveEditNoteHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_saveeditnote = ".view.notes.saveeditnote";
  private static final String FORWARD_notelist = ".view.notes.notelist";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public SaveEditNoteHandler() {}

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
      String bottomFrame = request.getParameter("bottomFrame");

      if (request.getParameter("saveType").equals("close")) {
        request.setAttribute("bodycontent", null);
        FORWARD_final = FORWARD_notelist;
        if (bottomFrame != null && bottomFrame.equals("true")) {
          FORWARD_final = ".view.notes.relatednote";
        }
        return mapping.findForward(FORWARD_final);
      }
      HttpSession session = request.getSession();
      int individualId = ((UserObject) session.getAttribute("userobject")).getIndividualID();
      NoteVO noteVO = new NoteVO();
      noteVO.setTitle(((NoteForm) form).getTitle());
      noteVO.setDetail(((NoteForm) form).getDetail());

      int indId = 0;
      if (((NoteForm) form).getIndividualid() != null && ((NoteForm) form).getIndividualid().length() > 0) {
        indId = Integer.parseInt(((NoteForm) form).getIndividualid());
      }
      noteVO.setRelateIndividual(indId);

      int entId = 0;
      if (((NoteForm) form).getEntityid() != null && ((NoteForm) form).getEntityid().length() > 0) {
        entId = Integer.parseInt(((NoteForm) form).getEntityid());
      }
      noteVO.setRelateEntity(entId);
      int noteID = 0;
      noteVO.setNoteId(noteID);
      noteVO.setCreatedBy(individualId);
      noteVO.setOwner(individualId);

      // pass vo to ejb layer
      NoteHome noteHome = (NoteHome) CVUtility.getHomeObject("com.centraview.note.NoteHome", "Note");
      Note noteRemote = noteHome.create();
      noteRemote.setDataSource(dataSource);
      noteRemote.updateNote(individualId, noteVO);

      // set attribute back to jsp pages
      if (request.getParameter("saveType").equals("save")) {
        request.setAttribute("bodycontent", "editnote");
        FORWARD_final = FORWARD_saveeditnote;
      } else if (request.getParameter("saveType").equals("saveclose")) {
        request.setAttribute("bodycontent", null);
        FORWARD_final = FORWARD_notelist;
      }

      if (bottomFrame != null && bottomFrame.equals("true")) {
        FORWARD_final = ".view.notes.relatednote";
      }

    }
    // exception handling
    catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    // forward the request as per forward_final value
    return mapping.findForward(FORWARD_final);
  }
}
