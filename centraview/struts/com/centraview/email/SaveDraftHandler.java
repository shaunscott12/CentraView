/*
 * $RCSfile: SaveDraftHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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
package com.centraview.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.file.CvFileFacade;
import com.centraview.settings.Settings;

public class SaveDraftHandler extends org.apache.struts.action.Action
{



  /**
  This method is overridden from Action Class
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {

      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualId = userobjectd.getIndividualID();
      ListPreference listpreference = userobjectd.getListPreference("Email");
      MailMessage mailmessagae = new MailMessage();

      DynaActionForm dynaForm = (DynaActionForm)form;

      String accountid = (String)dynaForm.get("AccountID");
      HashMap attchmentids = (HashMap)session.getAttribute("AttachfileList");

      int id = Integer.parseInt(accountid);

      /** code added for query change **/
      FolderList fl = (FolderList)session.getAttribute("folderlist");
      Set listkey = fl.keySet();
      Iterator it = listkey.iterator();
      int accountID;
      AccountDetail ad1 = null;
      while (it.hasNext())
      {
        ad1 = (AccountDetail)fl.get(it.next());
        accountID = ad1.getAccountid();
        if (id == accountID)
          break;

      }
      int folderidfordraft = ad1.getFolderIDFromName("Drafts", "SYSTEM");
      mailmessagae.setFolder(folderidfordraft);
      // up to here
      mailmessagae.setAccountID(Integer.parseInt(accountid));
      String mailFrom = (String)dynaForm.get("composeFrom");

      mailmessagae.setMailFrom(mailFrom);

      //to
      ArrayList toarray = new ArrayList();
      String to = (String)dynaForm.get("composeTo");

      StringTokenizer st = new StringTokenizer(to, ",");
      while (st.hasMoreTokens())
      {
        toarray.add(new MailAddress(st.nextToken()));
      }
      mailmessagae.setTo(toarray);

      //cc
      ArrayList ccarray = new ArrayList();
      String cc = (String)dynaForm.get("composeCc");

      StringTokenizer stcc = new StringTokenizer(cc, ",");
      while (stcc.hasMoreTokens())
      {
        ccarray.add(new MailAddress(stcc.nextToken()));
      }
      mailmessagae.setCc(ccarray);

      //bcc
      ArrayList bccarray = new ArrayList();
      String bcc = (String)dynaForm.get("composeBcc");

      StringTokenizer stbcc = new StringTokenizer(bcc, ",");
      while (stbcc.hasMoreTokens())
      {
        bccarray.add(new MailAddress(stbcc.nextToken()));
      }
      mailmessagae.setBcc(bccarray);

      //message subject
      String subject = (String)dynaForm.get("composeSubject");
      mailmessagae.setSubject(subject);

      //message body
      String body = (String)dynaForm.get("composeMessage");
      mailmessagae.setBody(body);

      //message Date
      long l = (new java.util.Date()).getTime();
      mailmessagae.setMessageDate(new java.sql.Timestamp(l));

      mailmessagae.setAttachFileIDs(attchmentids);

      EmailFacadeHome facade = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)facade.create();
      remote.setDataSource(dataSource);
      int messageid = remote.saveDraft(individualId, mailmessagae);

    if (attchmentids != null){
      Set s = attchmentids.keySet();
      Iterator iter = s.iterator();
      int fileid[] = new int[attchmentids.size()];
      int x = 0;
      while (iter.hasNext())
      {
      fileid[x++] = Integer.parseInt((String)iter.next());
      }

      CvFileFacade cvfile = new CvFileFacade();
      if (attchmentids != null)
      {
      for (int i = 0; i < attchmentids.size(); i++)
      {
        cvfile.commitEmailAttachment(individualId, fileid[i], dataSource);
      }
      }
    }

      request.setAttribute("messageid", Integer.toString(messageid));
      request.setAttribute("action", "Drafts");
      request.setAttribute("rowId", Integer.toString(messageid));

    }
    catch (Exception e)
    {
      System.out.println("[Exception][SaveDraftHandler.execute] Exception Thrown: " + e);
      //here delete the message object from server if
      //attachment is not saved properly
      e.printStackTrace();
    }

    return (mapping.findForward("displaycomposeemail"));

  }

}
