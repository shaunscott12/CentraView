/*
 * $RCSfile: ViewKbHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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
import com.centraview.common.UserObject;
import com.centraview.common.menu.MenuBean;
import com.centraview.common.menu.MenuItem;
import com.centraview.settings.Settings;
import com.centraview.support.knowledgebase.KnowledgeVO;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 * Handles the request for the Customer View kb Details
 * screen. Displays kb only, no modification allowed.
 */
public class ViewKbHandler extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = "customerKbDetail";
    // set up the left nav menu
    ArrayList menuList = new ArrayList();
    menuList.add(new MenuItem("Tickets", "TicketList.do", false));
    menuList.add(new MenuItem("FAQ", "FAQList.do", false));
    menuList.add(new MenuItem("Knowledgebase", "KBList.do", true));
    request.setAttribute("cvMenu", new MenuBean("Support", menuList));

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // entityID of the logged-in user's entity
    
    ActionErrors allErrors = new ActionErrors();

    // "customerKbForm", defined in cv-struts-config.xml
    DynaActionForm kbForm = (DynaActionForm)form;

    try
    {
      // get the kb ID from the form bean
      Integer formKbID = (Integer)kbForm.get("kbID");
      // create an int to hold the kb ID value
      int kbID = 0;

      // now, check the kb ID on the form...
      if (formKbID == null)
      {
        // if kb ID is not set on the form, then there is
        // no point in continuing forward. Show the user an
        // error and quit.
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Knowledgebase ID"));
        return(mapping.findForward(forward));
      }else{
        // if kb ID is set on the form properly, then set
        // the int representation for use in the code below
        kbID = formKbID.intValue();
      }
      
      SupportFacadeHome supportFacade = (SupportFacadeHome)CVUtility.getHomeObject("com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");
      SupportFacade remote = (SupportFacade)supportFacade.create();
      remote.setDataSource(dataSource);

      KnowledgeVO kbVO = remote.getKB(individualID, kbID);
      
      kbForm.set("title", kbVO.getTitle());
      kbForm.set("detail", kbVO.getDetail());

System.out.println("\n\n\nkbForm = [" + kbForm + "]\n\n\n");      


    }catch(Exception e){
      System.out.println("[Exception][CV ViewEventHandler] Exception thrown in execute(): " + e);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

