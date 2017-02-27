/*
 * $RCSfile: ForwardHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
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
import com.centraview.common.UserPrefererences;
import com.centraview.file.CvFileException;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileVO;
import com.centraview.file.CvFolderVO;
import com.centraview.settings.Settings;

/**
 * This class handles the request sent from the "Forward"
 * button on the email message detail screen. This will
 * populate a DynaActionForm bean ("composeMailForm"),
 * with the details of the original message, and forward
 * to com.centraview.mail.ComposeHandler.
 * @author CentraView, LLC.
 * @since v1.10
 */
public class ForwardHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(ForwardHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, IOException, ServletException, CommunicationException, NamingException
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

    MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");

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

      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      MailMessageVO messageVO = remote.getEmailMessageVO(individualID, messageID.intValue());

      // set the subject on the form bean. Don't forget to prepend "Fw: "
      String prevSubject = (String)messageVO.getSubject();
      String newSubject = "";
      if (prevSubject != null && prevSubject.length() > 0) {
        if ((prevSubject.toLowerCase()).startsWith("fw: ")) {
          // If the subject already has "Fw:" pre-pended, do not add another "Fw:"
          newSubject = prevSubject;
        }else{
          newSubject = "Fw: " + prevSubject;
        }
      }
      emailForm.set("subject", newSubject);

      String linebreak = "\n";
      UserPrefererences userPref= userObject.getUserPref();
      if ((userPref.getContentType()).equals("HTML")) {
        linebreak = "<br>\n";
      }


      StringBuffer body = new StringBuffer();
      body.append(linebreak + linebreak + "---- Original Message: ----" + linebreak);
      body.append("From: " + messageVO.getFromAddress() + linebreak);
      body.append("Sent: " + messageVO.getReceivedDate() + linebreak);
      body.append("Subject: " + messageVO.getSubject() + linebreak + linebreak);

      // if the original message is an HTML message, we can only quote
      // it if the user is using IE (with HTMLArea). Otherwise, we need
      // to attach the original message as an attachment to the new message
      boolean saveOrigBodyAsAttachment = false;
      String originalBody = (String)messageVO.getBody();
      originalBody = originalBody.replaceAll("\r", "");
      originalBody = originalBody.replaceAll("\n", linebreak);
      
      body.append(originalBody);

      String messageBody = body.toString();
      emailForm.set("body", messageBody);


      // Handle attachments - the attachment handler puts a ArrayList
      // This ArrayList contains the list of attached fileIDs.
      // So we'll get that ArrayList, iterate through it, creating a      // key being the fileID and the value being the filename.
      // So we'll have to replicate that behavior here, getting the
      // ArrayList of attachments from the MailMessageVO, looping
      // through that list, and adding an entry to a HashMap for each
      // attached file. Then Process the HashMap and get the FileID and File Title
      // an attribute named "attachmentList". PLEASE NOTE: we are going
      // to want to change this in the future. So if you intend to modify
      // this code, please consult the rest of the team to see if it makes
      // sense to make that change now. Also, if you make a change here,
      // you must make sure the attachment handling code in SendHandler
      // reflects your changes.
      ArrayList attachmentList = (ArrayList)messageVO.getAttachedFiles();

      Iterator iter = attachmentList.iterator();
      ArrayList attachmentMap = new ArrayList();
      int i = 0;
      while (iter.hasNext()) {
        CvFileVO fileVO = (CvFileVO)iter.next();
        int fileID = fileVO.getFileId();
        String fileName = fileVO.getTitle();
        attachmentMap.add(new DDNameValue(fileID+"#"+fileName,fileName));
      }

      if (saveOrigBodyAsAttachment) {
        CvFolderVO attachmentFolderVO = (CvFolderVO)remote.getAttachmentFolder(individualID);
        int attachmentFolderID = attachmentFolderVO.getFolderId();
        int newFileID = -1;

        // somehow create a file using CvFileFacade, and get a CvFileVO from that.
        CvFileVO fileVO = new CvFileVO();
        SimpleDateFormat df = new SimpleDateFormat("MMMM_dd_yyyy_hh_mm_ss_S");
        String prependDate = df.format(new Date());
        fileVO.setName("OriginalMessage_#" + messageID.intValue() + "_" + prependDate + ".html");
        fileVO.setTitle("OriginalMessage_#" + messageID.intValue());
        fileVO.setDescription("");
        fileVO.setFileSize(0.0f);   // float
        fileVO.setVersion("1.0");
        fileVO.setStatus("PUBLISHED");
        fileVO.setVisibility(CvFileVO.FV_PRIVATE);
        fileVO.setPhysical(CvFileVO.FP_PHYSICAL);
        fileVO.setPhysicalFolder(attachmentFolderID);
        fileVO.setAuthorId(individualID);
        fileVO.setIsTemporary(CvFileVO.FIT_YES);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(originalBody.getBytes());

        try {
          CvFileFacade fileFacade = new CvFileFacade();
          newFileID = fileFacade.addFile(individualID, attachmentFolderID, fileVO, inputStream, dataSource);
        }catch(CvFileException cfe){
          // I guess do nothing
        }

        if (newFileID > 0) {
          // Add the CvFileVO to attachmentList, and the following line for the attachmentMap
          fileVO.setFileId(newFileID);

          String FileName= "OriginalMessage_" + messageID.intValue();
          attachmentMap.add(new DDNameValue(newFileID+"#"+FileName,FileName));
        }
      }   // end if (saveOrigBodyAsAttachment)

      if (! attachmentMap.isEmpty()) {
        emailForm.set("attachmentList", attachmentMap);
      }
    }catch(Exception e){
      logger.error("[Exception] ForwardHandler.Execute Handler ", e);
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

