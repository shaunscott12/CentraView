/*
 * $RCSfile: PTDisplayHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/01 21:05:13 $ - $Author: mcallist $
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

package com.centraview.printtemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class PTDisplayHandler extends org.apache.struts.action.Action {

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_printtemplate = "display";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public PTDisplayHandler() {}

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      DynaActionForm dynaform = (DynaActionForm) form;
      int ptdetailid = 0;
      String strptdetailid = (String) dynaform.get("ptdetailid");
      ptdetailid = Integer.parseInt(strptdetailid);

      // Collect the Template information on basis of the passed templateID
      PrintTemplateVO ptVO = new PrintTemplateVO();
      PrintTemplateHome PTHome = (PrintTemplateHome) CVUtility.getHomeObject("com.centraview.printtemplate.PrintTemplateHome", "Printtemplate");
      PrintTemplate PTRemote = PTHome.create();
      PTRemote.setDataSource(dataSource);
      ptVO = PTRemote.getPrintTemplate(ptdetailid);

      if (ptVO.getPtname() != null)
        dynaform.set("templatetitle", ptVO.getPtname());
      dynaform.set("pttextarea", ptVO.getPtData());
      if (ptVO.getPtsubject() != null) {
        dynaform.set("templatesubject", ptVO.getPtsubject());
      }

      dynaform.set("artifactid", "" + ptVO.getArtifactId());
      dynaform.set("ptcategoryid", "" + ptVO.getPtcategoryId());

      request.setAttribute("typeofoperation", "Edit");
      request.setAttribute("body", "main");

      FORWARD_final = FORWARD_printtemplate;
    } catch (Exception e) {
      System.out.println("[Exception][PTDisplayHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}