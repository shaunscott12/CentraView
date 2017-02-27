/*
 * $RCSfile: ViewFaqHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.customer.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListElement;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.faq.Faq;
import com.centraview.support.faq.FaqHome;
import com.centraview.support.faq.FaqVO;
import com.centraview.support.faq.QuestionList;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 * Handles the request for the Customer View FAQ Details
 * screen. Displays FAQ only, no modification allowed.
 */
public class ViewFaqHandler extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = ".view.customer.view_faq";

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // entityID of the logged-in user's entity
    
    ActionErrors allErrors = new ActionErrors();

    // "customerFaqForm", defined in cv-struts-config.xml
    DynaActionForm faqForm = (DynaActionForm)form;

    try {
      // get the event ID from the form bean
      Integer formFaqID = (Integer)faqForm.get("faqID");
      // create an int to hold the faq ID value
      int faqID = 0;

      // now, check the faq ID on the form...
      if (formFaqID == null) {
        // if faq ID is not set on the form, then there is
        // no point in continuing forward. Show the user an
        // error and quit.
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "FAQ ID"));
        return(mapping.findForward(forward));
      }else{
        // if faq ID is set on the form properly, then set
        // the int representation for use in the code below
        faqID = formFaqID.intValue();
      }
      
      FaqHome home = (FaqHome)CVUtility.getHomeObject("com.centraview.support.faq.FaqHome", "Faq");
      Faq remote = home.create();
      remote.setDataSource(dataSource);
      
      FaqVO faqDetail = remote.getFaq(individualID, faqID);

      faqForm.set("title", faqDetail.getTitle());
      
      SupportFacadeHome supFacade = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade supRemote = supFacade.create();
      supRemote.setDataSource(dataSource);
      
      QuestionList questionList = supRemote.getQuestionList(individualID, faqID);

      ArrayList qList = new ArrayList();
      
      Set listkey = questionList.keySet();
      Iterator iter = listkey.iterator();
      while (iter.hasNext()) {
        String key = (String)iter.next();
        ListElement row = (ListElement)questionList.get(key);

        HashMap questionInfo = new HashMap();
        
        StringMember questionField = (StringMember)row.get("Question");
        StringMember answerField = (StringMember)row.get("Answer");
        
        questionInfo.put("questionID", new Integer(row.getElementID()));
        questionInfo.put("question", (String)questionField.getMemberValue());
        questionInfo.put("answer", (String)answerField.getMemberValue());
        qList.add(questionInfo);
      }

      faqForm.set("questionList", qList);

    }catch(Exception e){
      System.out.println("[Exception][CV ViewEventHandler] Exception thrown in execute(): " + e);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

