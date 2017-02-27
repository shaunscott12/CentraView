/*
 * $RCSfile: EmailListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:06 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.DisplayList;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.email.EmailList;
import com.centraview.settings.Settings;

public class EmailListHandler extends org.apache.struts.action.Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user

    session.setAttribute("highlightmodule", "email");

    ActionErrors allErrors = new ActionErrors();
    String forward = "emailList";

    // "emailListForm", defined in struts-config-email.xml
    DynaActionForm emailListForm = (DynaActionForm)form;

    try
    {
      // check for a valid email account
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail mailRemote = (Mail)home.create();
      mailRemote.setDataSource(dataSource);

      int numberAccounts = mailRemote.getNumberOfAccountsForUser(individualID);

      if (numberAccounts < 1)
      {
        // if the user has less than 1 mail account
        // set up, then show them the door
        return(mapping.findForward("displaynomailaccount"));
      }

      int currentAccountID = 0;
      MailFolderVO folderVO = new MailFolderVO();
      Integer folderID = (Integer)emailListForm.get("folderID");
      if (folderID == null || folderID.intValue() <= 0 )
      {
        // if "folderID" is not specified, then get the user's
        // default folder, which should be Inbox for their
        // default account
        // get the user's default account (we already know they have at least one)
        int defaultAccountID = mailRemote.getDefaultAccountID(individualID);
        folderVO = mailRemote.getPrimaryEmailFolder(defaultAccountID);
      }else{
        folderVO = mailRemote.getEmailFolder(folderID.intValue());
      }
      folderID = new Integer(folderVO.getFolderID());
      String folderName = folderVO.getFolderName();
      currentAccountID = folderVO.getEmailAccountID();

      HashMap folderList = mailRemote.getFolderList(currentAccountID);

      // now, set the folderID back to the form bean, so we
      // can access it in the JSPs via Struts beans
      emailListForm.set("folderID", folderID);

      MailAccountVO accountVO = mailRemote.getMailAccountVO(currentAccountID);
      emailListForm.set("accountID", new Integer(currentAccountID));
      emailListForm.set("accountType", (String)accountVO.getAccountType());

      emailListForm.set("folderList", folderList);
      emailListForm.set("folderType", folderVO.getFolderType());

      // now, we need to set some stuff up for the folder bar which
      // shows where the user is located within the folder hierarchy
      ArrayList folderPathList = mailRemote.getFolderFullPath(folderID.intValue());
      emailListForm.set("folderPathList", folderPathList);

      ListPreference listPrefs = userObject.getListPreference("Email");

      // TODO: make sure searchString is taken care of
      String searchString = request.getParameter("searchTextBox");
      if (searchString == null)
      {
        searchString = "";
      }else{
        searchString = "SIMPLE :" + searchString;
      }

      // Paging stuff - we are ALWAYS getting the EmailList object populated from
      // the EJB layer. However, we are chaging the startAt and endAt parameters
      // passed to the ListGenerator when we get that list. By default, startAt is
      // set to 1 and endAt is set to records per page;
      int startAt = 1;
      int endAt = listPrefs.getRecordsPerPage();
      char sortType = 'D';

      // but if NextPageHandler or PreviousPageHandler have been called, then we
      // get the DisplayList that either of those handlers put on the request,
      // and get the startAt and endAt parameters from that object (BUT THROW
      // AWAY THE ACTUAL LIST)
      DisplayList sessionEmailList = (DisplayList)request.getAttribute("displaylist");

      if (sessionEmailList != null)
      {
        startAt = sessionEmailList.getStartAT();
        endAt = sessionEmailList.getEndAT();
        sortType = sessionEmailList.getSortType();
      }

      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      EmailList emailList = null;
      
      // If we are setting the Advance Search Flag to True means we already processed the List with Advance Search condition. So we don't need to get the list once again.
      if (sessionEmailList != null && sessionEmailList.getAdvanceSearchFlag() == true)
      {
        emailList = (EmailList) sessionEmailList;
      }else{
        emailList = (EmailList)lg.getEmailList(individualID, startAt, endAt, searchString, listPrefs.getSortElement(), sortType, folderID.intValue(),true);
      }

      Set listkey = emailList.keySet();
      Iterator it = listkey.iterator();
      while (it.hasNext())
      {
        String str = (String)it.next();
        ListElement ele = (ListElement)emailList.get(str);

        StringMember sm = (StringMember)ele.get("Subject");
        if (folderName != null && folderName.equals(MailFolderVO.DRAFTS_FOLDER_NAME))
        {
          sm.setRequestURL("openWindows('" + request.getContextPath() + "/mail/EditDraft.do?messageID=" + ele.getElementID() + "', 720, 585, '')");
        }else if (folderName != null && folderName.equals(MailFolderVO.TEMPLATES_FOLDER_NAME)){
          sm.setRequestURL("openWindows('" + request.getContextPath() + "/mail/TemplateCompose.do?messageID=" + ele.getElementID() + "', 720, 585, '')");
        }else{
          sm.setRequestURL("goTo('" + request.getContextPath() + "/mail/ViewMessage.do?messageID=" + ele.getElementID() + "');");
        }

        sm = (StringMember)ele.get("To");
        String ComposeTo = sm.getDisplayString();
        ComposeTo = ComposeTo.replaceAll("\""," ");
        ComposeTo = ComposeTo.replaceAll("'"," ");
        ComposeTo = ComposeTo.trim();
        sm.setRequestURL("openWindows('" + request.getContextPath() + "/mail/Compose.do?to=" + ComposeTo + "', 720, 585, '')");

        sm = (StringMember)ele.get("From");
        String ComposeFrom = sm.getDisplayString();
        ComposeFrom = ComposeFrom.replaceAll("\""," ");
        ComposeFrom = ComposeFrom.replaceAll("'"," ");
        ComposeFrom = ComposeFrom.trim();
        sm.setRequestURL("openWindows('" + request.getContextPath() + "/mail/Compose.do?to=" + ComposeFrom + "', 720, 585, '')");
      }
      session.setAttribute("currentMailFolder", folderID);
      session.setAttribute("displaylist", emailList);
      request.setAttribute("displaylist", emailList);
      request.setAttribute("list", "Email");
    }catch(Exception e){
      System.out.println("[Exception][EmailListHandler.execute] Exception Thrown: " + e);
      // TODO: remove stack trace
      e.printStackTrace();
    }
    return (mapping.findForward("showemaillist"));
  }   // end execute() method

}

