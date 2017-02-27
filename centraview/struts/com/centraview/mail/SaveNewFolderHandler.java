/*
 * $RCSfile: SaveNewFolderHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SaveNewFolderHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = "closeWindow";
    String errorForward = "errorOccurred";
    
    // "newMailFolderForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

    try
    {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);
      
      // TODO: validate input;

      MailFolderVO newFolder = new MailFolderVO();

      Integer parentID = (Integer)emailForm.get("parentID");
      Integer accountID = (Integer)emailForm.get("accountID");
      String folderName = (String)emailForm.get("folderName");
      
      
      newFolder.setParentID(parentID.intValue());
      newFolder.setEmailAccountID(accountID.intValue());
      newFolder.setFolderName(folderName);
      newFolder.setFolderType(MailFolderVO.USER_FOLDER_TYPE);

      int newFolderID = remote.addEmailFolder(newFolder, true);

      if (newFolderID < 1)
      {
        forward = errorForward;
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "The email folder could not be created. Please make sure all fields are filled in and try again. Also make sure you have privileges to create folders on your email server."));
        this.saveErrors(request, allErrors);
      }else{
        emailForm.set("closeWindow", new Boolean(true));
      }
      
    }catch(Exception e){
      System.out.println("[Exception][SaveNewFolderHandler] Exception thrown in execute(): " + e);
      // TODO: remove stack trace
      e.printStackTrace();
    }
    return (mapping.findForward(forward));
  }
  
}

