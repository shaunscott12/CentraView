/*
 * $RCSfile: PTEditHandler.java,v $    $Revision: 1.2 $  $Date: 2005/08/01 21:05:13 $ - $Author: mcallist $
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

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;

import com.centraview.advancedsearch.AdvancedSearchUtil;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.mail.Mail;
import com.centraview.mail.MailHome;
import com.centraview.settings.Settings;

/**
 *
 */
public class PTEditHandler extends Action {
  private static Logger logger = Logger.getLogger(PTEditHandler.class);

  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_printtemplate = ".view.marketing.mailmerge";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    session.removeAttribute("printtemplate");
    UserObject userObject = (UserObject) session.getAttribute("userobject");
    int individualID = userObject.getIndividualID();
    try {
      DynaActionForm dynaform = (DynaActionForm) form;
      dynaform.reset(mapping, request);
      dynaform.set("templateName", "");
      dynaform.set("id", "");
      dynaform.set("savedsearch", "");
      dynaform.set("templateData", "");
      dynaform.set("file", "");
      dynaform.set("radio", "");
      dynaform.set("artifactname", "");
      dynaform.set("categoryId", "");
      dynaform.set("artifactid", "");
      dynaform.set("individualId", "");
      dynaform.set("entityId", "");
      dynaform.set("ename", "");
      dynaform.set("individualName", "");
      dynaform.set("mergetype", "PRINT");
      dynaform.set("savedsearch1", "");
      dynaform.set("entitysavedsearch", "");
      dynaform.set("specificentity", "");
      dynaform.set("accountList", null);
      dynaform.set("templateList", null);
      dynaform.set("templatefrom", "");
      dynaform.set("templatereplyto", "");
      dynaform.set("templatesubject", "");
      dynaform.set("toPrint", null);
      dynaform.set("emailList", null);
      dynaform.set("toIndividuals", null);
      dynaform.set("selectedEntityId", "");
      dynaform.set("individualId", "");
      dynaform.set("attachmentList", new ArrayList());

      String artifactName = request.getParameter("artifactName");

      if (artifactName == null) {
        artifactName = "entity";
      }
      dynaform.set("artifactname", artifactName);

      String entityId = request.getParameter("entityId");
      String entityName = request.getParameter("entityName");

      String indvId = request.getParameter("indvId");
      String indvName = request.getParameter("indvName");

      if (entityId != null && entityName != null) {
        dynaform.set("entityId", entityId);
        dynaform.set("ename", entityName);
      }

      if (indvId != null && indvName != null) {
        dynaform.set("individualName", indvName);
        dynaform.set("individualId", indvId);
      }
      // Check wheather the User is have any email account set up already.
      // if user as more then one accout we will set the value to true. so that
      // it will not show EMAIL
      // else it will be false
      MailHome home = (MailHome) CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail remote = home.create();
      remote.setDataSource(dataSource);

      int numberOfAccounts = remote.getNumberOfAccountsForUser(individualID);
      boolean emailFlag = false;
      if (numberOfAccounts <= 0) {
        emailFlag = true;
      }
      request.setAttribute("emailDisableFlag", new Boolean(emailFlag));

      // Get Saved searches from the EJB
      ArrayList entitySearchList = AdvancedSearchUtil.getSavedSearchList(individualID, 14, dataSource);
      entitySearchList.add(0, new LabelValueBean("Select", ""));
      ArrayList individualSearchList = AdvancedSearchUtil.getSavedSearchList(individualID, 15, dataSource);
      individualSearchList.add(0, new LabelValueBean("Select", ""));

      request.setAttribute("entitySearchList", entitySearchList);
      request.setAttribute("individualSearchList", individualSearchList);

      request.setAttribute("typeofoperation", "Edit");
      request.setAttribute("body", "view");

      FORWARD_final = FORWARD_printtemplate;
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    return mapping.findForward(FORWARD_final);
  } // end execute() method
} // end class definition

