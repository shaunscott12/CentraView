/*
 * $RCSfile: ViewFolderHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

public class ViewFolderHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.email.editfolder";
    
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

      MailFolderVO folderVO = remote.getEmailFolder(folderID.intValue());

      folderForm.set("folderName", folderVO.getFolderName());
      folderForm.set("folderType", folderVO.getFolderType());
      folderForm.set("totalMessages", folderVO.getTotalMessage());
      folderForm.set("hasSubFolders", new Boolean(folderVO.hasSubFolders()));

      int parentID = folderVO.getParentID();
      
      // get the parentID from the request. If it is set, that means
      // that the user has selected a new parent folder, and the page
      // has been refreshed. So we need to generate the full path based
      // on the given parentID, and not the parentID from the database.
      Integer formParentID = (Integer)folderForm.get("parentID");
      
      if (formParentID != null && formParentID.intValue() > 0)
      {
        parentID = formParentID.intValue();
      }
      
      // generate the fullPath to the parent folder
      ArrayList fullPathList = (ArrayList)remote.getFolderFullPath(parentID);
      StringBuffer fullPath = new StringBuffer("");
      if (fullPathList != null)
      {
        Iterator pathIter = fullPathList.iterator();
        while (pathIter.hasNext())
        {
          HashMap folderInfo = (HashMap)pathIter.next();
          fullPath.append("/" + (String)folderInfo.get("name"));
        }
      }
      folderForm.set("fullPath", fullPath.toString());
      folderForm.set("parentID", new Integer(parentID));

    }catch(Exception e){
      System.out.println("[Exception][ViewFolderHandler] Exception thrown in execute(): " + e);
      e.printStackTrace();
		}
    return(mapping.findForward(forward));
  }
}

