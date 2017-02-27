/*
 * $RCSfile: SaveDraftHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import java.util.Date;

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
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileVO;
import com.centraview.settings.Settings;

/**
 * Takes input from the email compose screen, and saves the details as a message
 * in the Drafts <strong>or</strong> Templates folder for later use as a Draft
 * or Template message. The only difference between a Draft and a Template (for
 * our purposes here) is the location in which they are saved.
 */
public class SaveDraftHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(SaveDraftHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID(); // logged in user

    ActionMessages allErrors = new ActionMessages();
    String forward = "displayComposeForm";

    ActionMessages messages = new ActionMessages();

    // "composeMailForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

    // Populate the Attachments In-Case. If we face any problem while sending
    // email.
    // Populating the ArrayList with the DDNameValue.
    ArrayList attachments = new ArrayList();
    ArrayList attachmentFileIDs = new ArrayList();
    String[] tempAttachmentMap = (String[])emailForm.get("attachments");

    if (tempAttachmentMap != null && tempAttachmentMap.length > 0) {
      for (int i = 0; i < tempAttachmentMap.length; i++) {
        String fileKeyName = tempAttachmentMap[i];
        if (fileKeyName != null) {
          int indexOfHash = fileKeyName.indexOf("#");
          if (indexOfHash != -1) {
            int lenString = fileKeyName.length();
            String fileID = fileKeyName.substring(0, indexOfHash);
            String fileName = fileKeyName.substring(indexOfHash + 1, lenString);
            attachments.add(new DDNameValue(fileID + "#" + fileName, fileName));
            attachmentFileIDs.add(new Integer(fileID));
          }
        }
      }
    }
    // Setting the Attachments to form.
    emailForm.set("attachmentList", attachments);

    try {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = home.create();
      remote.setDataSource(dataSource);

      // create a MailMessageVO
      MailMessageVO messageVO = new MailMessageVO();

      Integer messageID = new Integer(0);

      messageVO.setMessageID(messageID.intValue());

      Integer accountID = (Integer)emailForm.get("accountID");
      messageVO.setEmailAccountID(accountID.intValue());

      // IMPORTANT: Because we use this same handler to do the save "Template"
      // functionality, note the logic below. Here, we're getting the folder ID
      // for the folder where we are going to store the message. Instead of
      // writing
      // a second handler for Save Template that did almost the same exact thing
      // as this handler, I created a SaveTemplateHandler that just sets one
      // Boolean
      // request attribute named "saveTemplate". If that attribute is set to
      // true
      // here, then we'll get the folderID of the Templates folder instead of
      // the
      // Drafts folder. That is the ONLY difference between Draft and Template.
      String folderName = MailFolderVO.DRAFTS_FOLDER_NAME;
      Boolean saveTemplate = (Boolean)request.getAttribute("saveTemplate");
      if (saveTemplate != null && saveTemplate.booleanValue() == true) {
        messageID = (Integer)emailForm.get("templateID");
        folderName = MailFolderVO.TEMPLATES_FOLDER_NAME;
      } else {
        messageID = (Integer)emailForm.get("savedDraftID");
      }
      messageVO.setMessageID(messageID.intValue());
      MailFolderVO draftsFolder = remote.getEmailFolderByName(accountID.intValue(), folderName);
      messageVO.setEmailFolderID(draftsFolder.getFolderID());

      MailAccountVO accountVO = remote.getMailAccountVO(accountID.intValue());
      if (accountVO == null) {
        accountVO = new MailAccountVO();
      }

      InternetAddress fromAddress = new InternetAddress(accountVO.getEmailAddress(), accountVO
          .getAccountName());
      messageVO.setFromAddress(fromAddress.toString());

      messageVO.setReplyTo(accountVO.getReplyToAddress());

      String subject = (String)emailForm.get("subject");
      messageVO.setSubject(subject);

      String body = (String)emailForm.get("body");
      messageVO.setBody(body);

      Boolean composeInHTML = (Boolean)emailForm.get("composeInHTML");
      if (composeInHTML.booleanValue()) {
        messageVO.setContentType(MailMessageVO.PLAIN_TEXT_TYPE);
      } else {
        messageVO.setContentType(MailMessageVO.HTML_TEXT_TYPE);
      }

      // To: is *NOT* a required field here
      ArrayList toList = this.parseAddresses((String)emailForm.get("to"));
      messageVO.setToList(toList);

      ArrayList ccList = this.parseAddresses((String)emailForm.get("cc"));
      messageVO.setCcList(ccList);

      ArrayList bccList = this.parseAddresses((String)emailForm.get("bcc"));
      messageVO.setBccList(bccList);

      Date receivedDate = new Date();
      messageVO.setReceivedDate(receivedDate);

      // Handle attachments - the attachment handler puts a ArrayList
      // This ArrayList contains the list of attached fileIDs.
      // So we'll get that ArrayList, iterate through it, creating a
      // CvFileVO object for each attachment, then add that file VO
      // to the messageVO object which will take care of the rest in
      // the EJB layer. Note that in the future, we'll want to make
      // this better by not saving the attachment list on the session.
      // So if you intend to modify this code, please consult the rest
      // of the team to see if it makes sense to make that change now.
      // Also, if you make a change here, you must make sure the
      // attachment handling code in ForwardHandler reflects your changes.
      if (attachmentFileIDs != null && attachmentFileIDs.size() > 0) {
        CvFileFacade fileRemote = new CvFileFacade();

        for (int i = 0; i < attachmentFileIDs.size(); i++) {
          int fileID = ((Integer)attachmentFileIDs.get(i)).intValue();
          // get the CvFileVO from the EJB layer
          CvFileVO fileVO = fileRemote.getFile(individualID, fileID, dataSource);
          if (fileVO != null) {
            // add this attachment to the messageVO
            messageVO.addAttachedFiles(fileVO);
          }
        } // end while (attachIter.hasNext())
      } // end if (attachmentMap != null && attachmentMap.size() > 0)

      remote.saveDraft(individualID, messageVO);
      // **ATTENTION** Note the "tricky" logic here. We're using the action
      // errors framework to send a *SUCCESS* message to the user!!!
      // We're going to add a message to the error list *only* if there
      // were no errors up to this point. In all cases, we're going to save
      // the error messages and forward back to the Compose.do handler.
      if (allErrors.isEmpty()) {
        messages.add(ActionErrors.GLOBAL_MESSAGE, new ActionMessage("error.freeForm",
            "Message saved in " + folderName + " folder."));
        saveMessages(request, messages);
      }
      saveErrors(request, allErrors);

      request.setAttribute("saveDraft", new Boolean(true));
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }
    return (mapping.findForward(forward));
  }

  /**
   * Takes the String value of the To:, Cc:, or Bcc: field on the email compose
   * screen, and parses it into separate addresses, adding each String address
   * to an ArrayList which is returned.
   * @param unparsedString The raw String value of the form field from the email
   *          compose screen.
   * @return ArrayList of String addresses after parsing.
   */
  private ArrayList parseAddresses(String unparsedString)
  {
    ArrayList addressList = new ArrayList();
    if (unparsedString != null && unparsedString.length() > 0) {
      String[] rawAddresses = unparsedString.split(",");
      for (int i = 0; i < rawAddresses.length; i++) {
        if (rawAddresses[i] != null && rawAddresses[i].length() > 0) {
          addressList.add(rawAddresses[i].trim());
        }
      }
    }
    return (addressList);
  } // end parseAddresses(String) method

} // end class definition

