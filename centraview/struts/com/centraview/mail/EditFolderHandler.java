/*
 * $RCSfile: EditFolderHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
import com.centraview.settings.Settings;

public class EditFolderHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(EditFolderHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = "showFolderDetails";
    
    // "mailFolderForm", defined in struts-config-email.xml
    DynaActionForm folderForm = (DynaActionForm)form;

    try
    {
      // get the folder ID from the form bean
      Integer folderID = (Integer)folderForm.get("folderID");
      
      // now, check the folder ID on the form...
      if (folderID == null || folderID.intValue() <= 0 )
      {
        // if folder ID is not set on the form, then there is
        // no point in continuing forward. Show user the door. :-)
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Folder ID"));
        return(mapping.findForward(forward));
      }
      
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);

      MailFolderVO oldFolderVO = remote.getEmailFolder(folderID.intValue());

      // currently, we're only allowing Name and Parent to be updated
      oldFolderVO.setFolderName((String)folderForm.get("folderName"));
      oldFolderVO.setParentID(((Integer)folderForm.get("parentID")).intValue());

      int rowsAffected = remote.editFolder(individualID, oldFolderVO);

      folderForm.set("closeWindow", new Boolean(true));
    }catch(Exception e){
      logger.error("[Exception][ViewFolderHandler] Exception thrown in execute()", e);
		}
    return(mapping.findForward(forward));
  }   // end execute() method
  
}   // end class definition

