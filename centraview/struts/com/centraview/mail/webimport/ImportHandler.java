/*
 * $RCSfile: ImportHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:07 $ - $Author: mcallist $
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

package com.centraview.mail.webimport;

import java.util.ArrayList;

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
import com.centraview.mail.MailImport;
import com.centraview.mail.MailImportHome;
import com.centraview.mail.MailMessageVO;
import com.centraview.settings.Settings;

public class ImportHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(ImportHandler.class);
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = "showImportForm";

    // "webformImportForm" defined in struts-config-email.xml
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

      // get the import type from the form bean
      String importType = (String)emailForm.get("importType");
      if (importType == null || importType.length() < 1)
      {
        // if import type is not set on the form, then there is
        // no point in continuing forward. Show user the door. :-)
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Import Type"));
        return(mapping.findForward(forward));
      }
      
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      MailMessageVO messageVO = remote.getEmailMessageVO(individualID, messageID.intValue());

      String body = (String)messageVO.getBody();

      MailImportHome importHome = (MailImportHome)CVUtility.getHomeObject("com.centraview.mail.MailImportHome", "MailImport");
      MailImport importRemote = (MailImport)importHome.create();
      importRemote.setDataSource(dataSource);
      
      ArrayList bodyFields = importRemote.parseMessageBody(body, importType);

      emailForm.set("importFields", bodyFields);

    }catch(Exception e){
		logger.error("[Exception] ImportHandler.Execute Handler ", e);
      	e.printStackTrace();
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

