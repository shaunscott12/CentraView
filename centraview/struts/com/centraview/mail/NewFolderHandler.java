/*
 * $RCSfile: NewFolderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:07 $ - $Author: mking_cv $
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
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class NewFolderHandler extends org.apache.struts.action.Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    
    ActionErrors allErrors = new ActionErrors();
    String forward = ".view.email.newfolder";
    String errorForward = "errorOccurred";
    
    // "newMailFolderForm", defined in struts-config-email.xml
    DynaActionForm emailForm = (DynaActionForm)form;

    try
    {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = (Mail)home.create();
      remote.setDataSource(dataSource);
      
      // get the parent ID from the form bean
      Integer parentID = (Integer)emailForm.get("parentID");
      
      // now, check the parent ID on the form...
      if (parentID == null || parentID.intValue() <= 0)
      {
        int defaultAccountID = remote.getDefaultAccountID(individualID);
        MailFolderVO folderVO = remote.getPrimaryEmailFolder(defaultAccountID);
        parentID = new Integer(folderVO.getFolderID());
        emailForm.set("parentID", parentID);
        emailForm.set("accountID", new Integer(defaultAccountID));
      }

      // generate the fullPath to the parent folder
      ArrayList fullPathList = (ArrayList)remote.getFolderFullPath(parentID.intValue());
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
      emailForm.set("fullPath", fullPath.toString());

    }catch(Exception e){
      System.out.println("[Exception][NewFolderHandler] Exception thrown in execute(): " + e);
      e.printStackTrace();
    }
    return (mapping.findForward(forward));
  }
  
}

