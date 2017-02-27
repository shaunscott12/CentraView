/*
 * $RCSfile: ViewMessageHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.mail.internet.InternetAddress;
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
import com.centraview.settings.Settings;

public class ViewMessageHandler extends org.apache.struts.action.Action
{

  private static Logger logger = Logger.getLogger(ViewMessageHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.email.view_message";

    // "emailMessageForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

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

      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      MailMessageVO messageVO = remote.getEmailMessageVO(individualID, messageID.intValue());

      emailForm.set("accountID", new Integer(messageVO.getEmailAccountID()));
      Integer folderID = new Integer(messageVO.getEmailFolderID());
      emailForm.set("folderID", folderID);
      
      // now when we go back to FolderList.do, we'll be in the folder that this message is in
      session.setAttribute("currentMailFolder", folderID);
      emailForm.set("private",(String)messageVO.getPrivate());
      String fromAddress = (String)messageVO.getFromAddress();
      if (fromAddress != null && !fromAddress.equals("") && CVUtility.isEmailAddressValid(fromAddress))
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
      	Object toListObject = toIter.next(); 
      	if (toListObject instanceof InternetAddress){
      		toList.add((InternetAddress) toListObject);
      	}
      	if (toListObject instanceof String){
      		toList.add((String) toListObject);
      	}
      }
      emailForm.set("toList", toList);
      request.setAttribute("toSize", new Integer(toList.size()));

      // the the cc: list. Make sure it contains InternetAddress objects
      ArrayList ccListVO = (ArrayList)messageVO.getCcList();
      ArrayList ccList = new ArrayList();
      Iterator ccIter = ccListVO.iterator();
      while (ccIter.hasNext())
      {
      	Object ccListObject = ccIter.next(); 
      	if (ccListObject instanceof InternetAddress){
      		ccList.add((InternetAddress) ccListObject);
      	}
      	if (ccListObject instanceof String){
      		ccList.add((String) ccListObject);
      	}      	
      }
      emailForm.set("ccList", ccList);
      request.setAttribute("ccSize", new Integer(ccList.size()));

      ArrayList bccList = new ArrayList();
      emailForm.set("bccList", bccList);

      emailForm.set("messageDate", messageVO.getReceivedDate());

      emailForm.set("subject", messageVO.getSubject());

      emailForm.set("attachmentList", messageVO.getAttachedFiles());
      request.setAttribute("attachmentSize", new Integer(messageVO.getAttachedFiles().size()));

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

      HashMap folderList = remote.getFolderList(messageVO.getEmailAccountID());
      emailForm.set("folderList", folderList);

      HashMap prevNextMap = remote.getPreviousNextLinks(individualID, messageID.intValue());
      emailForm.set("previousMessage", (Integer)prevNextMap.get("previousMessage"));
      emailForm.set("nextMessage", (Integer)prevNextMap.get("nextMessage"));

      String headers = (String)messageVO.getHeaders();
      String webformType = this.isMessageWebformImport(headers);

      if (webformType.equals("Contact"))
      {
        emailForm.set("showImportContactButton", new Boolean(true));
      }

      if (! messageVO.isMessageRead())
      {
        // if the message is not marked as read, mark it read now
        remote.markMessageRead(individualID, messageID.intValue());
      }

    }catch(Exception e){
    	logger.debug("[Exception][ViewMessageHandler] Exception thrown in execute(): " + e);
    	e.printStackTrace();
	}
    return(mapping.findForward(forward));
  }   // end execute() method

  /**
   * Returns the type of webform which this message is recieved from
   * or "NONE" by parsing the headers of the message.
   * @param headers The entire String headers of the email message.
   * @return String The type of webform which is being imported, or 
   *         "NONE" if this message is not from a webform. Currently,
   *         the only valid type is "Contact", but this can change
   *         in the future as we add features.
   */
  private String isMessageWebformImport(String headers)
  {
    String webformType = "NONE";
    StringTokenizer tokenizer = new StringTokenizer(headers, System.getProperty("line.separator", "\n"));
    while (tokenizer.hasMoreTokens())
    {
      String currentHeader = (String)tokenizer.nextToken();
      if (currentHeader.matches(".*X-CentraView-Webform: .*"))
      {
        webformType = (currentHeader.substring(21)).trim();
      }
    }
    return webformType;
  }

}   // end class definition

