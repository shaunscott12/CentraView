/*
 * $RCSfile: DeleteLiteratureHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/02 14:46:15 $ - $Author: mcallist $
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
package com.centraview.administration.modulesettings;

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
import com.centraview.settings.Settings;

/**
 * This class does DeleteLiteratureHandler file.
 * @author CentraView, LLC.
 */
public class DeleteLiteratureHandler extends Action {
  private static Logger logger = Logger.getLogger(DeleteLiteratureHandler.class);
  /**
   * This is a overridden method from action class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      String[] literatureID = null;
      HttpSession session = request.getSession(true);
      UserObject userobjectd = (UserObject) session.getAttribute("userobject");
      int individualID = userobjectd.getIndividualID();
      // Get the rmote object of Literature EJB
      LiteratureHome literatureHome = (LiteratureHome) CVUtility.getHomeObject("com.centraview.administration.modulesettings.LiteratureHome",
          "LiteratureAdmin");
      Literature remote = literatureHome.create();
      remote.setDataSource(dataSource);
      literatureID = request.getParameterValues("rowId");
      // Create the instance of CVFileFacade
      CvFileFacade cvfile = new CvFileFacade();
      for (int i = 0; i < literatureID.length; i++) {
        // Get the information related to the Literature and then delete
        LiteratureVO literatureVO = remote.selectLiterature(Integer.parseInt(literatureID[i]));
        cvfile.deleteLiterature(individualID, literatureVO.getFileID(), dataSource);
        // Delete the entry from the Literature Table....
        remote.deleteLiterature(individualID, Integer.parseInt(literatureID[i]));
      }
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
    }
    return (mapping.findForward("literaturelist"));
  }
}
