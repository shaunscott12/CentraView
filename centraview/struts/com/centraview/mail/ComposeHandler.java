/*
 * $RCSfile: ComposeHandler.java,v $    $Revision: 1.2 $  $Date: 2005/06/28 14:32:21 $ - $Author: mking_cv $
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

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.settings.Settings;

public class ComposeHandler extends org.apache.struts.action.Action
{

  private static Logger logger = Logger.getLogger(ComposeHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.email.compose";

    // "composeMailForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

    try {
      // figure out if we're 1. showing the FCKEditor and 2. setting content type == HTML
      UserPrefererences userPref= userObject.getUserPref();
      boolean composeInHTML = false;
      if ((userPref.getContentType()).equals("HTML")) {
        composeInHTML = true;
      }
      emailForm.set("composeInHTML", new Boolean(composeInHTML));

      Mail remote = (Mail)CVUtility.setupEJB("Mail", "com.centraview.mail.MailHome", dataSource);

      ArrayList accountIDList = remote.getUserAccountList(individualID);

      // also adding delegated accounts  
      accountIDList.addAll(remote.getDelegatedAccountList(individualID));

      ArrayList accountList = new ArrayList();    // this, we're sending to the form
      if (accountIDList.size() > 0) {
        MailUtils mailUtils = new MailUtils();
        // get the details of each account
        Iterator iter = accountIDList.iterator();
        while (iter.hasNext()) {
          Number accountID = (Number)iter.next();
          MailAccountVO accountVO = remote.getMailAccountVO(accountID.intValue());
          HashMap accountDetails = new HashMap();
          accountDetails.put("accountID", new Integer(accountID.intValue()));
          accountDetails.put("accountName", new InternetAddress(accountVO.getEmailAddress(), mailUtils.stripInvalidCharsFromName(accountVO.getIndividualName())));
          accountList.add(accountDetails);
        }
      }
      emailForm.set("accountList", accountList);

      int defaultAccountID = remote.getDefaultAccountID(individualID);

      ArrayList templateList = remote.getTemplateList(individualID, defaultAccountID);
      emailForm.set("templateList", templateList);

      if (defaultAccountID > 0) {
        MailAccountVO accountVO = remote.getMailAccountVO(defaultAccountID);
        StringBuffer body = new StringBuffer((String)emailForm.get("body"));
        String signature = (String)accountVO.getSignature();
        if (signature != null && signature.length() > 0) {
          signature = "\n\n-- \n" + signature;
          if (composeInHTML) {
            signature = signature.replaceAll("\n","<BR>");
          }
          
          // Don't append the signature if we're using a saved template
          // (it already has the signature appended).
          Boolean composeFromTemplate = (Boolean)request.getAttribute("composeFromTemplate");
          Boolean saveTemplate = (Boolean)request.getAttribute("saveTemplate");
          Boolean saveDraft = (Boolean)request.getAttribute("saveDraft");
          if (composeFromTemplate == null || composeFromTemplate.booleanValue() == false) {
            if (saveTemplate == null || saveTemplate.booleanValue() == false) {
              if (saveDraft == null || saveDraft.booleanValue() == false) {
                body.append(signature);
              }
            }
          }
        }
        emailForm.set("body", body.toString());
      }

    }catch(Exception e){
      System.out.println("[Exception][ComposeHandler] Exception thrown in execute(): " + e.toString());
      e.printStackTrace();
    }
    return(mapping.findForward(forward));
  }   // end execute() method


}   // end class definition

