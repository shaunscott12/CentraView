/*
 * $RCSfile: CommonViewMessageHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.mail.common;

import java.util.ArrayList;
import java.util.Iterator;

import javax.mail.internet.InternetAddress;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.mail.Mail;
import com.centraview.mail.MailHome;
import com.centraview.mail.MailMessageVO;
import com.centraview.settings.Settings;

/**
 * Class is used for view the message which are related to particular individual
 * Individual can see the message which are composed by him.
 *
 * @author Naresh Patel <npatel@centraview.com>
 */
public class CommonViewMessageHandler extends org.apache.struts.action.Action
{

  private static Logger logger = Logger.getLogger(CommonViewMessageHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = "displayMessage";

    // "emailMessageForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

    MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
    
    try
    {
      // get the message ID from the form bean
      Integer messageID = (Integer)emailForm.get("messageID");

      // now, check the message ID on the form...
      if (messageID == null || messageID.intValue() <= 0 )
      {
        // if Message ID is not set on the form, then there is
        // no point in continuing forward. Show user the door. :-)
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Message ID"));
        return(mapping.findForward(forward));
      }

  
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      MailMessageVO messageVO = remote.getEmailMessageVO(individualID, messageID.intValue());

      emailForm.set("accountID", new Integer(messageVO.getEmailAccountID()));
      Integer folderID = new Integer(messageVO.getEmailFolderID());
      emailForm.set("folderID", folderID);
      
      emailForm.set("private",(String)messageVO.getPrivate());
      String fromAddress = (String)messageVO.getFromAddress();
      if (fromAddress != null && CVUtility.isEmailAddressValid(fromAddress))
      {
        emailForm.set("from", new InternetAddress(fromAddress));
      }else{
		InternetAddress blankfromAddress = new InternetAddress();
		blankfromAddress.setAddress("");
      	emailForm.set("from", blankfromAddress);
      }

      // the the To: list. Make sure it contains InternetAddress objects
      ArrayList toListVO = (ArrayList)messageVO.getToList();
      ArrayList toList = new ArrayList();
      Iterator toIter = toListVO.iterator();
      while (toIter.hasNext())
      {
        toList.add((InternetAddress)toIter.next());
      }
      emailForm.set("toList", toList);

      // the the cc: list. Make sure it contains InternetAddress objects
      ArrayList ccListVO = (ArrayList)messageVO.getCcList();
      ArrayList ccList = new ArrayList();
      Iterator ccIter = ccListVO.iterator();
      while (ccIter.hasNext())
      {
        ccList.add((InternetAddress)ccIter.next());
      }
      emailForm.set("ccList", ccList);

      ArrayList bccList = new ArrayList();
      emailForm.set("bccList", bccList);

      emailForm.set("messageDate", messageVO.getReceivedDate());

      emailForm.set("subject", messageVO.getSubject());

      ArrayList attachmentList = new ArrayList();
      emailForm.set("attachmentList", messageVO.getAttachedFiles());


      String body = (String)messageVO.getBody();

      // figure out if the message's Content-Type: is text/plain
      // If so, replace newlines with <br>'s.
      String contentType = (String)messageVO.getContentType();
      if (contentType != null && contentType.equals(MailMessageVO.PLAIN_TEXT_TYPE))
      {
        // we need to make it pretty
        body = body.replaceAll("\r", "");
        body = body.replaceAll("\n", "<br>\n");
      }

      emailForm.set("body", body);

      String headers = (String)messageVO.getHeaders();

    }catch(Exception e){
    	logger.debug("[Exception][CommonViewMessageHandler] Exception thrown in execute(): " + e);
	}
    return(mapping.findForward(forward));
  }   // end execute() method
}   // end class definition

