/*
 * $RCSfile: SendHandler.java,v $    $Revision: 1.4 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

public class SendHandler extends org.apache.struts.action.Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = "closeWindow";
    String errorForward = "errorOccurred";

    // "composeMailForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

		// Populate the Attachments In-Case. If we face any problem while sending email.
		// Populating the ArrayList with the DDNameValue.
		ArrayList attachments = new ArrayList();
		ArrayList attachmentFileIDs = new ArrayList();
		String[] tempAttachmentMap = (String[])emailForm.get("attachments");
		if (tempAttachmentMap != null && tempAttachmentMap.length > 0) {
			for (int i=0; i<tempAttachmentMap.length; i++) {
				String fileKeyName = tempAttachmentMap[i];
				if (fileKeyName != null) {
          int indexOfHash = fileKeyName.indexOf("#");
          if (indexOfHash != -1) {
            int lenString = fileKeyName.length();
            String fileID = fileKeyName.substring(0,indexOfHash);
            String fileName = fileKeyName.substring(indexOfHash+1,lenString);
            attachments.add(new DDNameValue(fileID+"#"+fileName,fileName));
            attachmentFileIDs.add(new Integer(fileID));
					}
				}
			}
		}

		//Setting the Attachments to form.
		emailForm.set("attachmentList", attachments);

		try {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      // get the form input and validate

      // create a MailMessageVO
      MailMessageVO messageVO = new MailMessageVO();

      Integer accountID = (Integer)emailForm.get("accountID");
      if (accountID == null || accountID.intValue() <= 0) {
        accountID = new Integer(remote.getDefaultAccountID(individualID));
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.pleaseSelect", "From: (email account)"));
      }
      messageVO.setEmailAccountID(accountID.intValue());

      MailAccountVO accountVO = remote.getMailAccountVO(accountID.intValue());
      if (accountVO == null) {
        accountVO = new MailAccountVO();
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "Could not retreive account information. Please select an account."));
      }

      MailUtils mailUtils = new MailUtils();

      InternetAddress fromAddress = new InternetAddress(accountVO.getEmailAddress(), mailUtils.stripInvalidCharsFromName(accountVO.getIndividualName()));
      messageVO.setFromAddress(fromAddress.toString());

      messageVO.setReplyTo(accountVO.getReplyToAddress());


      String subject = (String)emailForm.get("subject");
      if (subject == null || subject.equals("")) {
        messageVO.setSubject("[No Subject] ");
      }else{
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
      }else{
        messageVO.setContentType(MailMessageVO.PLAIN_TEXT_TYPE);
      }

      ArrayList toList = this.parseAddresses((String)emailForm.get("to"));
      if (toList.size() <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "To:"));
      }else{
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
        CvFileFacade fileRemote  = new CvFileFacade();
        
        for (int i = 0; i < attachmentFileIDs.size(); i++) {
          int fileID = ((Integer) attachmentFileIDs.get(i)).intValue();
          // get the CvFileVO from the EJB layer
          CvFileVO fileVO = fileRemote.getFile(individualID, fileID, dataSource);
          if (fileVO != null) {
            // add this attachment to the messageVO
            messageVO.addAttachedFiles(fileVO);
          }
        }   // end while (attachIter.hasNext())
      }   // end if (attachmentMap != null && attachmentMap.size() > 0)


      if (! allErrors.isEmpty()) {
        // we encountered error above and built a list of messages,
        // so save the errors to be shown to the users, and quit
        saveErrors(request, allErrors);
        return(mapping.findForward(errorForward));
      }

      try {
        // send the message
        boolean messageSent = remote.sendMessage(accountVO, messageVO);

        if (messageSent) {
          // check to see if we just successfully delivered a saved draft
          // message. If so, then we need to delete the saved draft from
          // the database.
          Integer savedDraftID = (Integer)emailForm.get("savedDraftID");
          if (savedDraftID.intValue() > 0) {
            remote.deleteMessage(savedDraftID.intValue(), individualID);
          }
        }
      }catch(SendFailedException sfe){
				String errorMessage = sfe.getMessage();
				int startOfError = errorMessage.indexOf("<error>");
				int endOfError = errorMessage.indexOf("</error>");
				if (startOfError >= 0 && endOfError >= 0){
					// For any specific error catch and tagged inside the <error> </error>. we will parse and Display to the user.
					String finalParsedMessage = errorMessage.substring((startOfError+7),endOfError);
					allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "Error while sending mail. "+finalParsedMessage));
				} else{
	        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "Error while sending mail. Please check the recipients for invalid addresses."));
				}
        saveErrors(request, allErrors);
        return(mapping.findForward(errorForward));
      }
    }catch(Exception e){
      System.out.println("[Exception][SendHandler] Exception thrown in execute(): " + e.toString());
      e.printStackTrace();
      allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.unknownError", "error.unknownError"));
      saveErrors(request, allErrors);
      return(mapping.findForward(errorForward));
    }
    return(mapping.findForward(forward));
  }

  /**
   * Takes the String value of the To:, Cc:, or Bcc: field on
   * the email compose screen, and parses it into separate
   * addresses, adding each String address to an ArrayList
   * which is returned.
   * @param unparsedString The raw String value of the form
   *        field from the email compose screen.
   * @return ArrayList of String addresses after parsing.
   */
  private ArrayList parseAddresses(String unparsedString)
  {
    ArrayList addressList = new ArrayList();
    try {
      unparsedString = unparsedString.replaceAll(";",",");
      InternetAddress tempAddress = new InternetAddress();
      InternetAddress[] rawAddresses =  InternetAddress.parse(unparsedString);
      for (int i=0; i<rawAddresses.length; i++) {
        addressList.add(rawAddresses[i].toString());
      }
    }catch(Exception e){
      e.printStackTrace();
    }
    return(addressList);
  }
}

