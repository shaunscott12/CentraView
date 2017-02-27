/*
 * $RCSfile: SucessPrintTemplateHandler.java,v $    $Revision: 1.4 $  $Date: 2005/10/24 13:39:11 $ - $Author: mcallist $
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

package com.centraview.printtemplate;

import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.file.CvFile;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileHome;
import com.centraview.file.CvFileVO;
import com.centraview.file.CvFolderVO;
import com.centraview.mail.MailMessageVO;
import com.centraview.settings.Settings;

public class SucessPrintTemplateHandler extends Action {
  private static Logger logger = Logger.getLogger(SucessPrintTemplateHandler.class);
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";
  // Local Forwards
  private static final String FORWARD_newtemplate = ".view.marketing.mailmerge";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      DynaActionForm dynaform = (DynaActionForm)form;

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

      String mergeType = (String)session.getAttribute("mergeType");
      ArrayList ptContent = (ArrayList)dynaform.get("toPrint");
      String ptfrom = (String)dynaform.get("templatefrom");
      ArrayList emailList = (ArrayList)dynaform.get("emailList");
      String ptSubject = (String)dynaform.get("templatesubject");

      String Message[] = new String[3];
      if (ptContent != null && ptContent.size() != 0) {
        if (mergeType.equals("EMAIL")) {
          // Populate the Attachments In-Case. If we face any problem while
          // sending email.
          // Populating the ArrayList with the DDNameValue.
          ArrayList attachmentFileIDs = new ArrayList();
          ArrayList tempAttachmentMap = (ArrayList)dynaform.get("attachmentList");
          if (tempAttachmentMap != null && tempAttachmentMap.size() > 0) {
            for (int i = 0; i < tempAttachmentMap.size(); i++) {
              DDNameValue fileKeyInfo = (DDNameValue)tempAttachmentMap.get(i);
              String fileKeyName = fileKeyInfo.getStrid();

              if (fileKeyName != null) {
                int indexOfHash = fileKeyName.indexOf("#");
                if (indexOfHash != -1) {
                  String fileID = fileKeyName.substring(0, indexOfHash);
                  attachmentFileIDs.add(new Integer(fileID));
                }
              }
            }
          }

          // create a MailMessageVO
          MailMessageVO messageVO = new MailMessageVO();

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
            }
          }

          // Send Email to all the Individual with the updated information of
          // the Template
          PrintTemplateHome PTHome = (PrintTemplateHome)CVUtility.getHomeObject(
              "com.centraview.printtemplate.PrintTemplateHome", "Printtemplate");
          PrintTemplate PTRemote = PTHome.create();
          PTRemote.setDataSource(dataSource);

          if (ptfrom != null && !ptfrom.equals("") && !ptfrom.equals("null")) {
            int accountID = Integer.parseInt(ptfrom);
            Message[0] = PTRemote.sendPTEmail(individualID, accountID, emailList, ptSubject,
                ptContent, messageVO);
          }
        } else {
          // generate the Previewed Template into a File.
          try {
            CvFileFacade cvf = new CvFileFacade();

            CvFileHome homeFile = (CvFileHome)CVUtility.getHomeObject(
                "com.centraview.file.CvFileHome", "CvFile");
            CvFile remoteFile = homeFile.create();
            remoteFile.setDataSource(dataSource);

            CvFolderVO homeFld = remoteFile.getHomeFolder(individualID);

            int rn = (new Random()).nextInt();
            Calendar c = Calendar.getInstance();
            java.util.Date dt = c.getTime();
            DateFormat df = new SimpleDateFormat("MM_dd_yyyy");
            String dateStamp = df.format(dt);

            CvFileVO flvo = new CvFileVO();
            flvo.setTitle("Print Template_" + rn);
            flvo.setName("Template_" + "_" + rn + "_" + dateStamp + ".html");
            Message[1] = "Template_" + "_" + rn + "_" + dateStamp + ".html";

            flvo.setPhysicalFolder(homeFld.getFolderId());
            flvo.setIsTemporary("NO");
            flvo.setOwner(individualID);
            flvo.setPhysical(CvFileVO.FP_PHYSICAL);
            StringBuffer documentContent = new StringBuffer();

            for (int i = 0; i < ptContent.size(); i++) {
              documentContent.append(ptContent.get(i));
              if (i != (ptContent.size() - 1)) {
                documentContent.append("<br><<Page Break>><br>");
              }
            }
            ByteArrayInputStream inputStream = new ByteArrayInputStream(documentContent.toString()
                .getBytes());
            // Add the File to the Files Module so that we can download it...
            int fileId = cvf.addFile(individualID, homeFld.getFolderId(), flvo, inputStream,
                dataSource);
            Message[2] = String.valueOf(fileId);
          } catch (Exception e) {
            logger.error("[execute] Exception thrown.", e);
          }
        } // end of else block for if (mergeType.equals("EMAIL"))
      } // end if (ptContent != null && ptContent.size() != 0)
      session.removeAttribute("mergetype");
      request.setAttribute("Message", Message);
      FORWARD_final = FORWARD_newtemplate;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
