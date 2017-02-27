/*
 * $RCSfile: CommonSendHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
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
import com.centraview.mail.Mail;
import com.centraview.mail.MailHome;
import com.centraview.mail.MailMessageVO;
import com.centraview.settings.Settings;

/**
 * @since 1.0.15
 * @author Naresh Patel <npatel@centraview.com>
 */

public class CommonSendHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(CommonSendHandler.class);
  /**
   * Process the specified HTTP request, and create the corresponding HTTP
   * response (or forward to another web component that will create it). Return
   * an <code>ActionForward</code> instance describing where and how control
   * should be forwarded, or <code>null</code> if the response has already
   * been completed.
   * @param mapping The ActionMapping used to select this instance
   * @param form The optional ActionForm bean for this request (if any)
   * @param req The HTTP request we are processing
   * @param res The HTTP response we are creating
   * @exception IOException if an input/output error occurs
   * @exception ServletException if a servlet exception occurs
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID(); // logged in user

    ActionMessages allErrors = new ActionMessages();
    String forward = "closeWindow";
    String errorForward = "errorOccurred";

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
      }// end of while (attachIter.hasNext())
    }// end of if (tempAttachmentMap != null && tempAttachmentMap.size() > 0)

    // Setting the Attachments to form.
    emailForm.set("attachmentList", attachments);

    try {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = home.create();
      remote.setDataSource(dataSource);

      // create a MailMessageVO
      MailMessageVO messageVO = new MailMessageVO();

      String from = (String)emailForm.get("from");
      messageVO.setFromAddress(from);
      String replyTo = (String)emailForm.get("replyTo");
      messageVO.setReplyTo(replyTo);

      String subject = (String)emailForm.get("subject");
      if (subject == null || subject.equals("")) {
        messageVO.setSubject("[No Subject] ");
      } else {
        messageVO.setSubject(subject);
      }

      String body = (String)emailForm.get("body");
      if (body == null) {
        body = "";
      }
      messageVO.setBody(body);

      Boolean composeInHTML = (Boolean)emailForm.get("composeInHTML");
      if (composeInHTML.booleanValue()) {
        messageVO.setContentType(MailMessageVO.HTML_TEXT_TYPE);
      } else {
        messageVO.setContentType(MailMessageVO.PLAIN_TEXT_TYPE);
      }

      ArrayList toList = this.parseAddresses((String)emailForm.get("to"));
      if (toList.size() <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            "error.general.requiredField", "To:"));
      } else {
        messageVO.setToList(toList);
      }

      ArrayList ccList = this.parseAddresses((String)emailForm.get("cc"));
      messageVO.setCcList(ccList);

      ArrayList bccList = this.parseAddresses((String)emailForm.get("bcc"));
      messageVO.setBccList(bccList);

      // TODO: figure out what headers to set when sending mail
      messageVO.setHeaders("");

      messageVO.setReceivedDate(new java.util.Date());

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

      if (!allErrors.isEmpty()) {
        // we encountered error above and built a list of messages,
        // so save the errors to be shown to the users, and quit
        saveErrors(request, allErrors);
        return (mapping.findForward(errorForward));
      }

      try {
        // send the message
        remote.simpleMessage(individualID, messageVO);
        session.removeAttribute("AttachfileList");
      } catch (SendFailedException sfe) {

        String errorMessage = sfe.getMessage();

        // For any specific error catch and tagged inside the <error> </error>.
        // we will parse and Display to the user.
        int startOfError = errorMessage.indexOf("<error>");
        int endOfError = errorMessage.indexOf("</error>");
        if (startOfError >= 0 && endOfError >= 0) {
          String finalParsedMessage = errorMessage.substring((startOfError + 7), endOfError);
          allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm",
              "Error while sending mail. " + finalParsedMessage));
        }// end of if (startOfError >= 0 && endOfError >= 0)
        else {
          // For any specific error catch and tagged inside the <failed>
          // </failed>.
          // it basically containg the information like successfully send
          // message to Individual List
          // and failed to send to particular Individual.
          // pass the control to errorDisplaying Page.
          int startOfFailed = errorMessage.indexOf("<failed>");
          int endOfFailed = errorMessage.indexOf("</failed>");
          if (startOfFailed >= 0 && endOfFailed >= 0) {
            String finalParsedMessage = errorMessage.substring((startOfFailed + 8), endOfFailed);
            request.setAttribute("failedMessage", finalParsedMessage);
            errorForward = "errorPage";
          }// end of if (startOfFailed >= 0 && endOfFailed >= 0)
          else {
            allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm",
                "Error while sending mail. Please check the recipients for invalid addresses."));
          }// end of else
        }// end of else
        saveErrors(request, allErrors);
        return (mapping.findForward(errorForward));
      }
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      // For any other error occured while sending mail.
      allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.unknownError",
          "error.unknownError"));
      saveErrors(request, allErrors);
      return (mapping.findForward(errorForward));
    }
    return (mapping.findForward(forward));
  } // end execute() method

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
    try {
      unparsedString = unparsedString.replaceAll(";", ",");
      InternetAddress[] rawAddresses = InternetAddress.parse(unparsedString);
      for (int i = 0; i < rawAddresses.length; i++) {
        addressList.add(rawAddresses[i].toString());
      }
    } catch (Exception e) {
      logger.error("[parseAddresses]: Exception", e);
    }
    return (addressList);
  } // end parseAddresses(String) method

} // end class definition

