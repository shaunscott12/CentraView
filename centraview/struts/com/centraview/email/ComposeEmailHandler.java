/*
 * $RCSfile: ComposeEmailHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:49 $ - $Author: mking_cv $
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.email.getmail.MailAddress;
import com.centraview.settings.Settings;

/**
 * Insert the type's description here. Creation date: (16/07/2003 )
 *
 * @author   Sunita
 */
public class ComposeEmailHandler extends Action
{

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      ListPreference listpreference = userobjectd.getListPreference("Email");

      String rowId = null;
      rowId = request.getParameter("rowId");

      if (rowId == null)
      {
        rowId = (String)request.getAttribute("rowId");
      }

      //int AccountID = request.getParameter("accountId");
      //String accountID = "1";
      String accountID = (String)request.getParameter("accountId");
      DynaActionForm dynaForm = (DynaActionForm)form;
      dynaForm.set("AccountID", accountID);

      String strAction = request.getParameter("action");
      if (strAction == null)
      {
        strAction = (String)request.getAttribute("action");
      }

      if (strAction == null)
      {
        return (mapping.findForward("displaycomposeemail"));
      }

      if (strAction.equals("compose"))
      {
        if (request.getParameter("to") != null)
        {
          dynaForm.set("composeTo", request.getParameter("to"));
        }

        HashMap hm1 = (HashMap)session.getAttribute("AttachfileList");
        if (hm1 != null)
        {
          hm1.clear();
          session.setAttribute("AttachfileList", hm1);
        }
      }
      else
      {

        MailMessage mailmessage = null;
        EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
        EmailFacade remote = (EmailFacade)cfh.create();
        remote.setDataSource(dataSource);

        HashMap hm = new HashMap();

        hm.put("MessageID", new Integer(Integer.parseInt(rowId)));

        //the userid is hardcoded to be changed later
        mailmessage = remote.getMailMessage(userobjectd.getIndividualID(), hm);

        // get all the addresses in to field in buffer
        ArrayList to = mailmessage.getTo();
        StringBuffer bufferTo = new StringBuffer();
        if (to.size() > 0)
        {
          for (int i = 0; i < to.size(); i++)
          {
            MailAddress ma = (MailAddress)to.get(i);

            if (i == to.size() - 1)
            {
              bufferTo.append(ma.getAddress());
            }
            else
            {
              bufferTo.append(ma.getAddress() + ",");
            }
          }
        }

        // get all the addresses in cc field in buffer
        ArrayList cc = mailmessage.getCc();
        StringBuffer bufferCc = new StringBuffer();
        if (cc.size() > 0)
        {
          for (int i = 0; i < cc.size(); i++)
          {
            MailAddress ma = (MailAddress)cc.get(i);

            if (i == cc.size() - 1)
            {
              bufferCc.append(ma.getAddress());
            }
            else
            {
              bufferCc.append(ma.getAddress() + ",");
            }
          }
        }

        // get all the addresses in bcc field in buffer
        ArrayList bcc = mailmessage.getBcc();
        StringBuffer bufferBcc = new StringBuffer();
        if (bcc.size() > 0)
        {
          for (int i = 0; i < bcc.size(); i++)
          {
            MailAddress ma = (MailAddress)bcc.get(i);

            if (i == bcc.size() - 1)
            {
              bufferBcc.append(ma.getAddress());
            }
            else
            {
              bufferBcc.append(ma.getAddress() + ",");
            }
          }
        }

        //From
        String bodyText = "";
        String subject = "";
        String mailFrom = "";
        String OriginalMailText = "";
        String messageDate = "";

        // From
        if (mailmessage.getMailFrom() != null)
        {
          mailFrom = mailmessage.getMailFrom();
        }

        //subject
        if (mailmessage.getSubject() != null)
        {
          subject = mailmessage.getSubject();
        }

        // body
        if (mailmessage.getBody() != null)
        {
          bodyText = mailmessage.getBody();
        }

        if (mailmessage.getMessageDate() != null)
        {
          messageDate = (String) (mailmessage.getMessageDate()).toString();
        }

        OriginalMailText = "\n --- Original Message --- ";
        OriginalMailText = OriginalMailText + "\nFrom: " + mailFrom;
        OriginalMailText = OriginalMailText + "\nTo: " + bufferTo.toString();
        OriginalMailText = OriginalMailText + "\nCc: " + bufferCc.toString();
        OriginalMailText = OriginalMailText + "\nSent: " + messageDate;
        OriginalMailText = OriginalMailText + "\nSubject: " + subject;

        dynaForm.set("MessageID", "1");

        if (strAction.equals("reply"))
        {
          dynaForm.set("composeTo", mailFrom);
          dynaForm.set("composeSubject", "Re: " + subject);
          dynaForm.set("composeMessage", OriginalMailText + "\n" + bodyText);
        }
        else if (strAction.equals("replyAll"))
        {
          dynaForm.set("composeTo", mailFrom + "," + bufferTo.toString());
          dynaForm.set("composeCc", bufferCc.toString());
          dynaForm.set("composeSubject", "Re: " + subject);
          dynaForm.set("composeMessage", OriginalMailText + "\n" + bodyText);
        }
        else if (strAction.equals("forward"))
        {
          //attachment
          if (mailmessage.getAttachmentID() != null)
          {
            ArrayList attchment = mailmessage.getAttachmentID();
            request.setAttribute("Attchment", attchment);
          }
          //End Add by shirish

          dynaForm.set("composeSubject", "Fw: " + subject);
          dynaForm.set("composeMessage", OriginalMailText + "\n" + bodyText);
        }
        else if (strAction.equals("Drafts"))
        {
          dynaForm.set("composeTo", bufferTo.toString());
          dynaForm.set("composeCc", bufferCc.toString());
          dynaForm.set("composeBcc", bufferBcc.toString());
          dynaForm.set("composeSubject", subject);
          dynaForm.set("composeMessage", bodyText);
          //Add by shirish modify linesh
          //attachment
          if (mailmessage.getAttachmentID() != null)
          {
            ArrayList attchment = mailmessage.getAttachmentID();
            request.setAttribute("Attchment", attchment);
          }
          //End Add by shirish
        }
      }
    }
    catch (Exception e)
    {
      System.out.println("[Exception][ComposeEmailHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward("displaycomposeemail"));
  }
}
