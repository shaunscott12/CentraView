/*
 * $RCSfile: ViewFileHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.customer.files;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.menu.MenuBean;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFileVO;
import com.centraview.settings.Settings;

/**
 * Handles the request for the Customer View file Details
 * screen. Displays file only, no modification allowed.
 */
public class ViewFileHandler extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String forward = "customerFileDetail";
    // set up the left nav menu
    request.setAttribute("cvMenu", new MenuBean("Files", new ArrayList()));

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    int individualID = userObject.getIndividualID();    // logged in user
    int entityID = userObject.getEntityId();    // entityID of the logged-in user's entity
    
    ActionErrors allErrors = new ActionErrors();

    // "customerFileForm", defined in cv-struts-config.xml
    DynaActionForm fileForm = (DynaActionForm)form;

    try
    {
      // get the file ID from the form bean
      Integer formFileID = (Integer)fileForm.get("fileID");
      // create an int to hold the file ID value
      int fileID = 0;

      // now, check the file ID on the form...
      if (formFileID == null)
      {
        // if file ID is not set on the form, then there is
        // no point in continuing forward. Show the user an
        // error and quit.
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Knowledgebase ID"));
        return(mapping.findForward(forward));
      }else{
        // if file ID is set on the form properly, then set
        // the int representation for use in the code below
        fileID = formFileID.intValue();
      }
      
      CvFileFacade fileFacade = new CvFileFacade();
      CvFileVO fileVO = fileFacade.getFile(individualID, fileID, dataSource);

      fileForm.set("title", fileVO.getTitle());
      fileForm.set("fileName", fileVO.getName());
      fileForm.set("description", fileVO.getDescription());
      
      if (fileVO.getAuthorVO() != null)
      {
        fileForm.set("author", fileVO.getAuthorVO().getFirstName() + " " + fileVO.getAuthorVO().getLastName());
      }else{
        fileForm.set("author", "");
      }

      fileForm.set("created", fileVO.getCreatedOn());
      fileForm.set("modified", fileVO.getModifiedOn());
      fileForm.set("version", fileVO.getVersion());

      System.out.println("\n\n\nfileForm = [" + fileForm + "]\n\n\n");

    }catch(Exception e){
      System.out.println("[Exception][CV ViewFileHandler] Exception thrown in execute(): " + e);
      e.printStackTrace();
    }
    return(mapping.findForward(forward));
  }   // end execute() method

}   // end class definition

