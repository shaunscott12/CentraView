/*
 * $RCSfile: PrivateMessageHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:08 $ - $Author: mking_cv $
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

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Handles the request served from the Private button on the Email List
 * screen & also at Viewing Message Screen, make all selected messages to private or public on basis of "YES/NO"
 */
public class PrivateMessageHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(PrivateMessageHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualID = userObject.getIndividualID();    // logged in user

    String forward = "emailList";

    MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
    DynaActionForm emailForm = (DynaActionForm)form;
    String folderID = (String)request.getParameter("folderID");
    
    StringBuffer returnParameter = new StringBuffer();
    if (folderID != null && folderID.length() > 0) {
      returnParameter.append("?folderID="+folderID);
    }
    
    try {
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      String rowId[] = request.getParameterValues("rowId");
      String messageID = (String)request.getParameter("messageID");
      String privateType = (String)request.getParameter("privateType");
      
      String messageIDs = "";

      // If we are setting the rowId then we are making the request from ViewMessage Screen
      // Else we are making request from the Email List Page
      if (messageID != null || rowId == null){
        rowId = new String[1];
        rowId[0] = messageID;
        forward = "viewMessage";
        returnParameter.append("&messageID="+messageID);
      }

      for (int i=0; i<rowId.length; i++) {
        if (rowId[i] != null && !rowId[i].equals("")) {
          if ((i+1) != rowId.length) {
            messageIDs += rowId[i] + ",";
          } else {
            messageIDs += rowId[i];
          }
        }
      }
      
      if (privateType != null && messageIDs != null && messageIDs.length() != 0) {
        boolean flag = remote.privateMessage(individualID, messageIDs, privateType);
      }
    } catch (Exception e) {
      logger.debug("[Exception][PrivateMessageHandler] Exception thrown in execute(): " + e);
    }
    
    StringBuffer returnPath = new StringBuffer(mapping.findForward(forward).getPath());
    returnPath.append(returnParameter.toString());
    
    return(new ActionForward(returnPath.toString(), true));
  }   // end execute() method
  
}   // end class definition

