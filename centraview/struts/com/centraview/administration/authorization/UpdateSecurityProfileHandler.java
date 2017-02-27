/*
 * $RCSfile: UpdateSecurityProfileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:37 $ - $Author: mking_cv $
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

package com.centraview.administration.authorization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class UpdateSecurityProfileHandler extends org.apache.struts.action.Action
{

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_updateprofile = "updateprofile";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // we should get the moduleFieldRightMatrix from the EJB layer and just update it based on what we have
    // changed on the JSP, not generate our own from scratch.
    try {
      DynaActionForm securityProfileBean = (DynaActionForm)form;
      String profileIdString = (String)securityProfileBean.get("profileid");
      int profileId = (profileIdString.equals("")) ? 0 : Integer.parseInt(profileIdString);
      // Do stuff for new security profile if 0!
      String profileName = (String)securityProfileBean.get("profilename");

      AuthorizationHome authEJBHome = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");
      Authorization authEJB = (Authorization)authEJBHome.create();
      authEJB.setDataSource(dataSource);

      // gets mfrm from the EJB with the default rights of passed int.
      ModuleFieldRightMatrix mfrm = authEJB.getBlankFieldRightMatrix(ModuleFieldRightMatrix.NONE_RIGHT);

      String[] moduleArray = (String[])securityProfileBean.get("moduleright");

      // Go through the moduleArray if the moduleName is in there then we should update
      // the mfrm to give permissions.
      for (int i = 0; i < moduleArray.length; i++) {
        String moduleName = moduleArray[i];
        mfrm.setVisibleModule(moduleName);
      }

      // Step through the field rights and update them from the formbean.
      HashMap fieldRightMap = mfrm.getFieldRightsMap();
      Set fieldHashKey = fieldRightMap.keySet();
      Iterator moduleIter = fieldHashKey.iterator();
      while (moduleIter.hasNext()) {
        String moduleName = (String)moduleIter.next();
        HashMap fields = (HashMap)fieldRightMap.get(moduleName);
        Set fieldKey = fields.keySet();
        Iterator fieldIter = fieldKey.iterator();
        while (fieldIter.hasNext()) {
          String fieldName = (String)fieldIter.next();
          int fieldRights = mfrm.getFieldRight(moduleName, fieldName);
          String formBeanKey = moduleName + "_" + fieldName;
          String fieldRightString = (String)securityProfileBean.get(formBeanKey);
          int fieldRight = (fieldRightString != null) ? Integer.parseInt(fieldRightString) : ModuleFieldRightMatrix.NONE_RIGHT;
          mfrm.setFieldRight(moduleName, fieldName, fieldRight);
        }
      }

      if (profileId != 0) {
        authEJB.updateSecurityProfile(profileId, profileName, mfrm);
      } else {
        profileId = authEJB.addSecurityProfile(profileName, mfrm);
      }
      String action = (String)request.getParameter("action");
      request.setAttribute("profileId", Integer.toString(profileId));
      
      FORWARD_final = action;
    } catch (Exception e) {
      System.out.println("[Exception][UpdateSecurityProfileHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
