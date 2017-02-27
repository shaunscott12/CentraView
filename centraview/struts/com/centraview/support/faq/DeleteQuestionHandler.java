/*
 * $RCSfile: DeleteQuestionHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:47 $ - $Author: mking_cv $
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

package com.centraview.support.faq;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

public class DeleteQuestionHandler extends Action
{
  /** Global Forwards for exception handling.   */
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_question = "displayquestion";

  /** Redirect constant.   */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  static int counter = 0;

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      DynaActionForm faqForm = (DynaActionForm) form;
      String[] qids = request.getParameterValues("rowId");
      SupportFacadeHome sfh = (SupportFacadeHome) CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome",
          "SupportFacade");
      SupportFacade remote = (SupportFacade) sfh.create();
      remote.setDataSource(dataSource);
      int qid = 0;

      for (int i = 0; i < qids.length; i++)
      {
        qid = Integer.parseInt(qids[i]);
        remote.deleteQuestion(individualID, qid);
      }

      request.setAttribute("refreshWindow", "true");
      FORWARD_final = FORWARD_question;
    }
    catch (Exception e)
    {
      System.out.println("[Exception] DeleteQuestionHandler.execute: " + e.toString());
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
