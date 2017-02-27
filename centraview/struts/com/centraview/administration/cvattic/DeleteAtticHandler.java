/*
 * $RCSfile: DeleteAtticHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

package com.centraview.administration.cvattic;

import java.io.IOException;

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
import com.centraview.common.Constants;
import com.centraview.common.UserObject;
import com.centraview.cvattic.CvAttic;
import com.centraview.settings.Settings;

public class DeleteAtticHandler extends Action
{
  private static Logger logger = Logger.getLogger(DeleteAtticHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject"); //get the user object
    int individualId = user.getIndividualID();

    String rowId[] = request.getParameterValues("rowId");
    
    CvAttic remote = null;
    try {
      remote = (CvAttic)CVUtility.setupEJB("CvAttic", "com.centraview.cvattic.CvAtticHome", dataSource);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown: ", e);
      throw new ServletException(e);
    }


    for (int i = 0; i < rowId.length; i++) {
      if (rowId[i] != null && ! rowId[i].equals("")) {
         int elementId = Integer.parseInt(rowId[i]);

          try {
            logger.debug("Moving attic record with recordId = [" + elementId + "] back to Garbage...");
            remote.delete(individualId, elementId, Constants.CV_ATTIC);
            logger.debug("...attic record moved.");            
          } catch (Exception e) {
            logger.error("Exception thrown: ", e);
          }
       }
    }

    return mapping.findForward(".forward.administration.attic_list");
    
  }

}

