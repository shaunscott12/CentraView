/*
 * $RCSfile: DisplayQuestionHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

public class DisplayQuestionHandler extends Action
{
  /** Global Forwards for exception handling. */
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_newfaq = ".view.support.faq.question.new";
  private static final String FORWARD_editfaq = ".view.support.faq.question.edit";

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
      DynaActionForm qForm = (DynaActionForm) form;
      QuestionVO QVO = null;

      if (request.getParameter(Constants.TYPEOFOPERATION).equals(SupportConstantKeys.EDIT)) {
        SupportFacadeHome sfh = (SupportFacadeHome) CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome",
            "SupportFacade");
        SupportFacade remote = (SupportFacade) sfh.create();
        remote.setDataSource(dataSource);
        QVO = remote.getQuestion(individualID, Integer.parseInt(request.getParameter("rowId")));
        qForm.set("title", QVO.getQuestion());
        qForm.set("answer", QVO.getAnswer());
        qForm.set("faqid", new Integer(QVO.getFaqId()).toString());
        FORWARD_final = FORWARD_editfaq;
      } else {
        qForm.set("faqid", request.getParameter("faqid"));
        FORWARD_final = FORWARD_newfaq;
      }
    }
    catch (Exception e) {
      System.out.println("[Exception] DisplayQuestionHandler.execute: " + e.toString());
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
