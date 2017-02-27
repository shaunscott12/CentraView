/*
 * $RCSfile: DeleteUserHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/27 18:15:58 $ - $Author: mcallist $
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

package com.centraview.administration.user;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.file.CvFileFacade;
import com.centraview.file.CvFolderVO;
import com.centraview.settings.Settings;



public class DeleteUserHandler extends Action
{
  private static Logger logger = Logger.getLogger(DeleteUserHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject"); //get the user object
    int individualId = user.getIndividualID();

    String rowId[] = request.getParameterValues("rowId");

    User remote = null;
    try {
      remote = (User)CVUtility.setupEJB("User", "com.centraview.administration.user.UserHome", dataSource);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown: ", e);
      throw new ServletException(e);
    }


    for (int i = 0; i < rowId.length; i++) {
      if (rowId[i] != null && ! rowId[i].equals("")) {
         int elementID = Integer.parseInt(rowId[i]);

          // get individualID for user we are deleting
          int deleteIndividualId = remote.getIndividualIdForUser(elementID);
          logger.debug("Got individual ID for user: [" + deleteIndividualId + "]");

          try {
            CvFileFacade cvf = new CvFileFacade();
            CvFolderVO folder = cvf.getHomeFolder(deleteIndividualId, dataSource);
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String yyyymmdd = sdf.format(new java.util.Date(System.currentTimeMillis()));
            
            folder.setName(folder.getName() + "." + yyyymmdd + "." + "deleted");
            folder.setOwner(individualId);
            cvf.updateFolder(individualId, folder, dataSource);
          } catch (Exception e) {
            logger.error("Exception thrown: ", e);
          }
          
          try {
            logger.debug("Deleting user with individualId = [" + deleteIndividualId + "]...");
            remote.deleteUser(deleteIndividualId);
            logger.debug("...user deleted.");            
          } catch (Exception e) {
            logger.error("Exception thrown: ", e);
          }
       }
    }

    return mapping.findForward(".forward.administration.user_list");
    
  }

}

