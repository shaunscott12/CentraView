/*
 * $RCSfile: EditDraftHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import java.util.Iterator;

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
import com.centraview.file.CvFileVO;
import com.centraview.settings.Settings;

/**
 * This class handles the request for EditDraft.do, which
 * will get the details of a saved draft message, populate
 * a DynaActionForm bean ("composeMailForm"), with the details
 * of the original message, and forward Compose.do
 * @author CentraView, LLC.
 * @since v1.10
 */
public class EditDraftHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(EditDraftHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = "displayComposeForm";
    String errorForward = "errorOccurred";

    // "composeMailForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

    try {
      // get the message ID from the form bean
      Integer messageID = (Integer)emailForm.get("messageID");

      // now, check the message ID on the form...
      if (messageID == null || messageID.intValue() <= 0 ) {
        // if Message ID is not set on the form, then there is
        // no point in continuing forward. Show user the door. :-)
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Message ID"));
        return(mapping.findForward(errorForward));
      }

      // This tells the Send.do handler to delete the
      // draft after it has been sent
      Boolean composeFromTemplate = (Boolean)request.getAttribute("composeFromTemplate");
      if (composeFromTemplate == null || composeFromTemplate.booleanValue() == false) {
        emailForm.set("savedDraftID", messageID);
      }

      if (composeFromTemplate == null || composeFromTemplate.booleanValue() == true) {
        emailForm.set("loadTemplate", messageID);
        emailForm.set("templateID", messageID);
      }

      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      MailMessageVO messageVO = remote.getEmailMessageVO(individualID, messageID.intValue());

      // handle recipients (To:)
      ArrayList toList = (ArrayList)messageVO.getToList();
      String to = this.appendAddresses(toList);
      emailForm.set("to", to);

      // handle recipients (Cc:)
      ArrayList ccList = (ArrayList)messageVO.getCcList();
      String cc = this.appendAddresses(ccList);
      emailForm.set("cc", cc);

      // handle recipients (Bcc:)
      ArrayList bccList = (ArrayList)messageVO.getBccList();
      String bcc = this.appendAddresses(bccList);
      emailForm.set("bcc", bcc);

      // set the subject on the form bean. Don't forget to prepend "Fw: "
      String subject = (String)messageVO.getSubject();
      emailForm.set("subject", subject);

      String body = messageVO.getBody();
      emailForm.set("body", body);

      // Handle attachments - the attachment handler puts a HashMap
      // object on the session as an attribute named "AttachfileList".
      // This hashmap contains the list of attached files, with the
      // key being the fileID and the value being the filename.
      // So we'll have to replicate that behavior here, getting the
      // ArrayList of attachments from the MailMessageVO, looping
      // through that list, and adding an entry to a HashMap for each
      // attached file. Then, stick that HashMap onto the session as
      // an attribute named "AttachfileList". PLEASE NOTE: we are going
      // to want to change this in the future. So if you intend to modify
      // this code, please consult the rest of the team to see if it makes
      // sense to make that change now. Also, if you make a change here,
      // you must make sure the attachment handling code in SendHandler
      // reflects your changes.
      ArrayList attachmentList = (ArrayList)messageVO.getAttachedFiles();
      ArrayList attachmentFileIDs = new ArrayList();
      Iterator iter = attachmentList.iterator();
      ArrayList attachments = new ArrayList();
      int i = 0;
      while (iter.hasNext()) {
        CvFileVO fileVO = (CvFileVO)iter.next();
        int fileID = fileVO.getFileId();
        String fileName = fileVO.getName();
        attachments.add(new DDNameValue(fileID+"#"+fileName,fileName));
      }

      //Setting the Attachments to form.
      emailForm.set("attachmentList", attachments);
    } catch (Exception e) {
      System.out.println("[Exception][EditDraftHandler] Exception thrown in execute(): " + e.toString());
      e.printStackTrace();
    }
    return(mapping.findForward(forward));
  }   // end execute() method

  /**
   * Takes an ArrayList if InternetAddress objects, and
   * converts it into a comma-delimited String represenation
   * of those addresses.
   * @param addressList ArrayList of InternetAddress objects.
   * @return Comma-delimited String of addresses.
   */
  private String appendAddresses(ArrayList addressList)
  {
    StringBuffer addresses = new StringBuffer();
    Iterator iter = addressList.iterator();
    while (iter.hasNext()) {
      InternetAddress address = (InternetAddress)iter.next();
      addresses.append(address.toString());
      if (iter.hasNext()) {
        addresses.append(", ");
      }
    }
    return(addresses.toString());
  }   // end appendAddresses(ArrayList)


}   // end class definition

