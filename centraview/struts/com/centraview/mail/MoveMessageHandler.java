/*
 * $RCSfile: MoveMessageHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:07 $ - $Author: mking_cv $
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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * Handles the request served from the Move To button on the Email List
 * screen, moving all selected messages to the folder which was selected.
 */
public class MoveMessageHandler extends org.apache.struts.action.Action
{
  
  private static Logger logger = Logger.getLogger(MoveMessageHandler.class);
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    
    int individualID = userObject.getIndividualID();
    
    ActionErrors allErrors = new ActionErrors();
    String forward = "mailList";
    String errorForward = "errorOccurred";
    
    DynaActionForm emailForm = (DynaActionForm)form;
    Integer folderID = (Integer)emailForm.get("folderID");
    
    MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
    try
    {
      
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);
      
      String rowId[] = request.getParameterValues("rowId");
      Integer newFolderID = (Integer)emailForm.get("newFolderID");
      
      for (int i = 0; i < rowId.length; i++) {
        if (rowId[i] != null && !rowId[i].equals("")) {
          Integer messageID = Integer.valueOf(rowId[i]);
          remote.moveMessageToFolder(individualID, messageID.intValue(), newFolderID.intValue());
        }
      }
    }catch(Exception e){
      logger.debug("[Exception][MoveMessageHandler] Exception thrown in execute(): " + e);
    }
    
    StringBuffer returnPath = new StringBuffer(mapping.findForward(forward).getPath());
    if (folderID != null && folderID.intValue() > 0) {
      returnPath.append("?folderID="+folderID);
    }
    return(new ActionForward(returnPath.toString(), true));
  }
}

