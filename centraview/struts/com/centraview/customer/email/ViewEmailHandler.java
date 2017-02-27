/*
 * $RCSfile: ViewEmailHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.customer.email;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

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
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.email.MailMessage;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.email.getmail.MailAddress;
import com.centraview.settings.Settings;

/**
 * Handles the request for viewing the details of
 * a particular email in Customer View.
 */
public class ViewEmailHandler extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = ".view.customer.view_email";

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();

    try {
      EmailFacadeHome aa = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)aa.create();
      remote.setDataSource(dataSource);

      // "customerEmailForm" defined in cv-struts-config.xml
      DynaActionForm emailForm = (DynaActionForm)form;

      // get the messageID from the form bean 
      Integer messageID = (Integer)emailForm.get("emailID");

      // now, realize the silliness here. Let's create a HashMap
      // with one element. Stick the messageID in that element.
      // Then pass that HashMap to the EJB getMailMessage() method.
      // Why would we just pass the messageID to the method directly?!?
      HashMap hm = new HashMap();
      hm.put("MessageID", messageID);

      // get the message from the EJB layer
      MailMessage message = remote.getMailMessage(individualID, hm);

      // get all the To: addresses from the message
      // loop through, getting each address as a String,
      // add to a second ArrayList which gets set on the form bean
      ArrayList toList = (ArrayList)message.getTo();
      ArrayList formToList = new ArrayList();
      for (int i = 0; i < toList.size(); i++) {
        MailAddress ma = (MailAddress)toList.get(i);
        formToList.add((String)ma.getAddress());
      }
      emailForm.set("toList", formToList);
      
      // get all the Cc: addresses from the message
      // loop through, getting each address as a String,
      // add to a second ArrayList which gets set on the form bean
      ArrayList ccList = (ArrayList)message.getCc();
      ArrayList formCcList = new ArrayList();
      for (int j = 0; j < ccList.size(); j++) {
        MailAddress macc = (MailAddress)ccList.get(j);
        formCcList.add((String)macc.getAddress());
      }
      emailForm.set("ccList", formCcList);

      // set the From: field on the form bean
      emailForm.set("from", message.getMailFrom());
      
      // set the Date: field on the form bean
      Timestamp messageTimestamp = (Timestamp)message.getMessageDate();
      SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
      String date = dateFormatter.format(messageTimestamp);
      emailForm.set("date", date);
      
      // set the Subject: field on the form bean
      emailForm.set("subject", message.getSubject());
      
      // set the Body: field on the form bean
      emailForm.set("body", message.getBody());
      
    }catch(Exception e){
      System.out.println("[Exception][CV ViewEmailHandler] Exception thrown in execute(): " + e);
      allErrors.add("error.unknownError", new ActionMessage("error.unknownError"));
    }
    
    if (! allErrors.isEmpty()) {
      saveErrors(request, allErrors);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

