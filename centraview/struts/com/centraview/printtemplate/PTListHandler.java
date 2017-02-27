/*
 * $RCSfile: PTListHandler.java,v $    $Revision: 1.3 $  $Date: 2005/08/01 21:05:13 $ - $Author: mcallist $
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.mail.internet.InternetAddress;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
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

import com.centraview.advancedsearch.AdvancedSearch;
import com.centraview.advancedsearch.AdvancedSearchHome;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.mail.Mail;
import com.centraview.mail.MailAccountVO;
import com.centraview.mail.MailHome;
import com.centraview.settings.Settings;

/**
 * @author CentraView, LLC.
 */
public class PTListHandler extends Action {
  private static Logger logger = Logger.getLogger(PTListHandler.class);
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";
  // Local Forwards
  private static final String FORWARD_newtemplate = ".view.marketing.mailmerge";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException,
      ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    AdvancedSearchHome advancedSearchHome = (AdvancedSearchHome) CVUtility.getHomeObject("com.centraview.advancedsearch.AdvancedSearchHome",
        "AdvancedSearch");
    PrintTemplateHome PTHome = (PrintTemplateHome) CVUtility.getHomeObject("com.centraview.printtemplate.PrintTemplateHome", "Printtemplate");
    MailHome mailHome = (MailHome) CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
    try {
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      DynaActionForm dynaform = (DynaActionForm) form;
      String mergeType = (String) dynaform.get("mergetype");

      String actionType = null;
      if (request.getParameter("actionType") != null) {
        actionType = request.getParameter("actionType");
      }

      int categoryId = this.getCategoryIdFromType(mergeType);
      PrintTemplate PTRemote = PTHome.create();
      PTRemote.setDataSource(dataSource);
      AdvancedSearch remoteAdvancedSearch = advancedSearchHome.create();
      remoteAdvancedSearch.setDataSource(dataSource);
      String savedsearch1 = "";
      String specificentity = "";
      String entitysavedsearch = "";
      if (mergeType.equals("EMAIL")) {
        // collect the Account List and set it to the dynaActionForm.
        Mail mailRemote = mailHome.create();
        mailRemote.setDataSource(dataSource);
        ArrayList accountIDList = mailRemote.getUserAccountList(individualID);
        // also adding delegated accounts
        accountIDList.addAll(mailRemote.getDelegatedAccountList(individualID));
        ArrayList accountList = new ArrayList(); // this, we're sending to the
                                                  // form
        if (accountIDList.size() > 0) {
          // get the details of each account
          Iterator iter = accountIDList.iterator();
          while (iter.hasNext()) {
            Number accountID = (Number) iter.next();
            MailAccountVO accountVO = mailRemote.getMailAccountVO(accountID.intValue());
            InternetAddress address = new InternetAddress(accountVO.getEmailAddress(), accountVO.getAccountName());
            LabelValueBean accountDetails = new LabelValueBean(address.toString(), accountID.toString());
            accountList.add(accountDetails);
          }
        }
        dynaform.set("accountList", accountList);
      } // if (mergeType.equals("EMAIL"))
      entitysavedsearch = (String) dynaform.get("entitysavedsearch");
      if (entitysavedsearch.equals("ENTITY")) {
        savedsearch1 = (String) dynaform.get("savedsearch1");
      }
      if (entitysavedsearch.equals("SPECIFICENTITY")) {
        specificentity = (String) dynaform.get("specificentity");
      }
      // So if this is an email or print merge basically we need to gather up
      // a whole buttload of information to figure out which individuals will
      // be getting the message
      if (specificentity.equals("SPECIFICPRIMARY") && entitysavedsearch.equals("SPECIFICENTITY")) {
        // get the primary contact for this entity, they are the target
        // recipient
        String entityid = (String) dynaform.get("selectedEntityId");
        ArrayList entityIdCollection = new ArrayList();
        entityIdCollection.add(entityid);
        Collection contactID = PTRemote.getContactsForEntity(entityIdCollection, true);
        Iterator contactIdIterator = contactID.iterator();
        HashMap individualIds = new HashMap();
        if (contactIdIterator.hasNext()) {
          Number individualId = (Number) contactIdIterator.next();
          individualIds.put(individualId.toString(), individualId.toString());
        }
        dynaform.set("toIndividuals", individualIds);
      } else if (specificentity.equals("SPECIFICALL") && entitysavedsearch.equals("SPECIFICENTITY")) {
        // All individuals associated with the selected entity are
        // to be recipients
        String entityid = (String) dynaform.get("selectedEntityId");
        ArrayList entityIdCollection = new ArrayList();
        entityIdCollection.add(entityid);
        Collection contactID = PTRemote.getContactsForEntity(entityIdCollection, false);
        Iterator contactIdIterator = contactID.iterator();
        HashMap individualIds = new HashMap();
        while (contactIdIterator.hasNext()) {
          Number individualId = (Number) contactIdIterator.next();
          individualIds.put(individualId.toString(), individualId.toString());
        }
        dynaform.set("toIndividuals", individualIds);
      } else if (specificentity.equals("SPECIFICCONTACT") && entitysavedsearch.equals("SPECIFICENTITY")) {
        // specific
        String individualid = (String) dynaform.get("individualId");
        HashMap individualIds = new HashMap();
        individualIds.put(individualid, individualid);
        dynaform.set("toIndividuals", individualIds);
      } else if (entitysavedsearch.equals("INDIVIDUAL")) {
        String individualSearchId = (String) dynaform.get("individualSearchId");
        ArrayList results = new ArrayList();
        results.addAll(remoteAdvancedSearch.performSearch(individualID, Integer.parseInt(individualSearchId), "ADVANCE", null));
        HashMap individualIds = new HashMap();
        for (int i = 0; i < results.size(); i++) {
          Number resultId = (Number) results.get(i);
          // I stuck the key and value in to this hashmap as the same string
          // as that is how the rest of print templates is written and I
          // didn't have time to make it more reasonable.
          individualIds.put(resultId.toString(), resultId.toString());
        }
        dynaform.set("toIndividuals", individualIds);
      } else if (entitysavedsearch.equals("ENTITY") && savedsearch1.equals("PRIMARY")) {
        // this is when an entity saved search is selected, and we need to
        // get the individualid's of the primary contacts for the found set
        // of entities.
        String entitySearchId = (String) dynaform.get("entityId");
        ArrayList results = new ArrayList();
        results.addAll(remoteAdvancedSearch.performSearch(individualID, Integer.parseInt(entitySearchId), "ADVANCE", null));
        // results contains our collection of entity ids.
        Collection contactID = PTRemote.getContactsForEntity(results, true);
        Iterator contactIdIterator = contactID.iterator();
        HashMap individualIds = new HashMap();
        while (contactIdIterator.hasNext()) {
          Number individualId = (Number) contactIdIterator.next();
          individualIds.put(individualId.toString(), individualId.toString());
        }
        dynaform.set("toIndividuals", individualIds);
      } else if (entitysavedsearch.equals("ENTITY") && savedsearch1.equals("ALL")) {
        // This is exactly the same as above, except we are getting all
        // individuals instead of just primary contacts.
        String entitySearchId = (String) dynaform.get("entityId");
        ArrayList results = new ArrayList();
        results.addAll(remoteAdvancedSearch.performSearch(individualID, Integer.parseInt(entitySearchId), "ADVANCE", null));
        // results contains our collection of entity ids.
        Collection contactID = PTRemote.getContactsForEntity(results, false);
        Iterator contactIdIterator = contactID.iterator();
        HashMap individualIds = new HashMap();
        while (contactIdIterator.hasNext()) {
          Number individualId = (Number) contactIdIterator.next();
          individualIds.put(individualId.toString(), individualId.toString());
        }
        dynaform.set("toIndividuals", individualIds);
      } // end else if (entitysavedsearch.equals("ENTITY") &&
      request.setAttribute("body", "new");
      // now we finished getting the buttload of info.
      // So get the templates.
      Collection templateList = PTRemote.getallPrintTemplate(individualID, categoryId);
      session.setAttribute("mergeType", mergeType);
      dynaform.set("templateList", templateList);
      PrintTemplateVO ptVO = new PrintTemplateVO();
      String id = (String) dynaform.get("id");

      Integer templateId = null;
      // If the id is set on the form bean get the selected template.
      // Otherwise get the default.
      if (id != null && !id.equals("")) {
        try {
          templateId = Integer.valueOf(id);
        } catch (Exception e) {}
      }
      // anyway somehow we got the "Default" print template. So put it in the
      // bean
      if (templateId == null) {
        try {
          ptVO = PTRemote.getDefaultPrintTemplate(individualID, categoryId);
        } catch (Exception e) {
          logger.error("[Exception] PTListHandler.Execute Handler ", e);
          throw new ServletException(e);
        }
      } else {
        try {
          ptVO = PTRemote.getPrintTemplate(templateId.intValue());
        } catch (Exception e) {
          logger.error("[execute] Exception thrown.", e);
          throw new ServletException(e);
        }
      }
      dynaform.set("artifactname", ptVO.getArtifactName());
      dynaform.set("artifactid", String.valueOf(ptVO.getArtifactId()));
      if (actionType == null) {
        dynaform.set("templateData", ptVO.getPtData());
      }
      dynaform.set("templateName", ptVO.getPtname());
      dynaform.set("id", String.valueOf(ptVO.getPtdetailId()));
      dynaform.set("categoryId", String.valueOf(ptVO.getPtcategoryId()));
      if (ptVO.getPtsubject() != null) {
        dynaform.set("templatesubject", ptVO.getPtsubject());
      }
      FORWARD_final = FORWARD_newtemplate;
    } catch (Exception e) {
      logger.error("[Exception] PTListHandler.Execute Handler ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    MailMergeUtil.requestSetup(request, "/jsp/marketing/mailmerge/template_detail.jsp");
    return mapping.findForward(FORWARD_final);
  } // end execute

  private int getCategoryIdFromType(String mergeType)
  {
    int categoryId = 0;
    if (mergeType.equals("PRINT")) {
      categoryId = 1;
    } else if (mergeType.equals("EMAIL")) {
      categoryId = 2;
    } else if (mergeType.equals("PROPOSAL")) {
      categoryId = 3;
    } else if (mergeType.equals("TICKET")) {
      categoryId = 4;
    }
    return categoryId;
  }
} // end action class
