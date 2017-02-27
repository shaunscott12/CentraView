/*
 * $RCSfile: ViewMailHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
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
import com.centraview.common.Constants;
import com.centraview.common.DisplayList;
import com.centraview.common.ListElement;
import com.centraview.common.ListElementMember;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.email.getmail.MailAddress;
import com.centraview.settings.Settings;

/**
 *
 */
public class ViewMailHandler extends Action
{

  private static Logger logger = Logger.getLogger(ViewMailHandler.class);


  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
  	throws IOException, ServletException, CommunicationException, NamingException 
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

	EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
    try
    {
      DynaActionForm dynaForm = (DynaActionForm)form;

      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualID = userobjectd.getIndividualID();
      ListPreference listpreference = userobjectd.getListPreference("Email");
      String rowId = null;


      rowId = request.getParameter("rowId");
      int messageID = Integer.parseInt(rowId);

      String listAction = request.getParameter("listAction");

      String folderid = request.getParameter("folderid");
      if (folderid == null)
      {
        folderid = (String)session.getAttribute("folderid");
      }
      else
      {
        session.setAttribute("folderid", folderid);
      }


      String indexfrompage = request.getParameter("index");
     System.out.println("indexfrompage"+indexfrompage);

      if (listAction != null && listAction.equals("next"))
      {
		  System.out.println("messageID"+messageID);
	         messageID = messageID - 1 ;
	         int index = 0;
	         if (indexfrompage != null){
				 index = Integer.parseInt(indexfrompage);
				 indexfrompage = (index - 1)+"";
			 }
	         rowId = messageID + "";
      }

      System.out.println("indexfrompage"+indexfrompage);
      DisplayList displaylistSession = (DisplayList)session.getAttribute("displaylist");

      int index = 0;
      boolean flag = false;

      // THIS IS UTTER SILLINESS. WHY ARE WE LOOPING THROUGH A SESSION ATTRIBUTE
      // WITH A GENERIC NAME LIKE "displaylist", WHICH COULD HAVE BEEN SET
      // BY *ANY* HANDLER, WHEN WE *ALREADY HAVE* THE KEY TO THE MESSAGE?!??!
      // I'VE INTRODUCED A HACK HERE, TO ACCOUNT FOR CASES IN WHICH WE'RE TRYING
      // TO VIEW A MESSAGE DETAILS, BUT WE HAVEN'T COME FROM THE EMAIL LIST SCREEN.
      if (displaylistSession != null && (displaylistSession.getListType()).equals("Email"))
      {
        Set listkey = displaylistSession.keySet();
        Iterator it = listkey.iterator();

        while (it.hasNext())
        {
          index++;
          String str = (String)it.next();
          ListElement ele = (ListElement)displaylistSession.get(str);
          ListElementMember sm = (ListElementMember)ele.get("MessageID");
          Integer id = (Integer)sm.getMemberValue();
          int messageid = id.intValue();

          if (index == (Integer.parseInt(indexfrompage)))
          {
            flag = true;
            messageID = messageid;
            break;
          }
        } // end while(it.hasNext())
      }
      else
      {
        flag = true;
      }
/*
      if (listAction != null && listAction.equals("next"))
      {
		  System.out.println("messageID"+messageID);
	         messageID = messageID - 1 ;
	         rowId = messageID + "";
      }

*/
      System.out.println("messageID after"+messageID);

      if (indexfrompage != null)
      {
        dynaForm.set("index", indexfrompage);
      }
      else
      {
        dynaForm.set("index", Integer.toString(index));
      }

      if (flag == true)
      {
        MailMessage mailmessage = null;
        EmailFacade remote = (EmailFacade)cfh.create();

        HashMap hm = new HashMap();
        hm.put("MessageID", new Integer(messageID));
        mailmessage = remote.getMailMessage(individualID, hm);
        remote.setDataSource(dataSource);

        String mailIdList[] = { rowId };

        remote.emailMarkasRead(Integer.parseInt(folderid), 1, mailIdList);

        ArrayList to = mailmessage.getTo();
        ArrayList cc = mailmessage.getCc();

        String arrayTO[] = new String[to.size()];
        for (int i = 0; i < arrayTO.length; i++)
        {
          MailAddress ma = (MailAddress)to.get(i);
          arrayTO[i] = ma.getAddress();
        }

        String arrayCC[] = new String[cc.size()];
        for (int j = 0; j < arrayCC.length; j++)
        {
          com.centraview.email.getmail.MailAddress macc = (MailAddress)cc.get(j);
          arrayCC[j] = macc.getAddress();
        }

        dynaForm.set("MessageID", Integer.toString(messageID));

        //From
        if (mailmessage.getMailFrom() != null)
        {
          dynaForm.set("from", mailmessage.getMailFrom());
        }
        // To
        if (arrayTO.length != 0)
        {
          dynaForm.set("to", arrayTO);
        }

        // CC
        if (arrayCC.length != 0)
        {
          dynaForm.set("cc", arrayCC);
        }

        //date
        if (mailmessage.getMessageDate() != null)
        {
          dynaForm.set("date", mailmessage.getMessageDate());
        }

        //subject
        if (mailmessage.getSubject() != null)
        {
          dynaForm.set("subject", mailmessage.getSubject());
        }

System.out.println("mailmessage.getSubject()"+mailmessage.getSubject());
        // body
        if (mailmessage.getBody() != null)
        {
          dynaForm.set("message", mailmessage.getBody());
        }
        if (mailmessage.getHeadersHM() != null)
        {
          HashMap hmHeader = (HashMap)mailmessage.getHeadersHM();
          String headerKey = (String)hmHeader.get(Constants.EH_SERVICE_KEY);
          if (headerKey != null)
          {
            String headerValue = null;
            if (headerKey.equals(Constants.EH_SERVICE_VALUE_ACTIVITY_INVITATION))
            {
              headerValue = (String)hmHeader.get(Constants.EH_ATTENDEE_STATUS_KEY);
              request.setAttribute("header", headerValue);
              String activityId = (String)hmHeader.get(Constants.EH_ACTIVITYID_KEY);
              request.setAttribute("activityId", activityId);

            }
            else if (headerKey.equals(Constants.EH_SERVICE_VALUE_CREATEINDIVIDUAL))
            {
              headerValue = (String)hmHeader.get(headerKey);
              headerValue = Constants.EH_SERVICE_VALUE_CREATEINDIVIDUAL;
              request.setAttribute("header", headerValue);
            }
            else if (headerKey.equals(Constants.EH_SERVICE_VALUE_INDIVIDUALIMPORTED))
            {
              headerValue = Constants.EH_SERVICE_VALUE_INDIVIDUALIMPORTED;
              request.setAttribute("header", headerValue);
            }
          }

        }

        request.setAttribute("emailForm",dynaForm);

        ArrayList attchment = mailmessage.getAttachmentID();

        request.setAttribute("Attchment", attchment);
      }
      else
      {
        request.setAttribute("folderid", folderid);
        //	request.setAttribute("accountid" , "0" );
        return (mapping.findForward("showemaillist"));
      }

    }
    catch (Exception e)
    {
	  logger.error("[Exception] ViewMailHandler.Execute Handler ", e);
    }

    return (mapping.findForward("displaymail"));
  }
}
