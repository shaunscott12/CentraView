/*
 * $RCSfile: EmailValueListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/28 17:22:36 $ - $Author: mcallist $
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.CreateException;
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
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.ListPreference;
import com.centraview.common.ListView;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * The Class is used to process the Email Message List The EJB layer and pass it
 * to the display layer.
 * @author Naresh Patel <npatel@centraview.com>
 */
public class EmailValueListHandler extends Action {
  private static Logger logger = Logger.getLogger(EmailValueListHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String finalForward = ".view.email.valuelist";
    final String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    GlobalMasterLists globalMasterLists = GlobalMasterLists.getGlobalMasterLists(dataSource);

    HashMap moduleList = new HashMap();
    if (globalMasterLists.get("moduleList") != null) {
      moduleList = (HashMap)globalMasterLists.get("moduleList");
    }

    HttpSession session = request.getSession(true);

    // Check the session for an existing error message (possibly from the delete
    // handler)
    ActionMessages allErrors = (ActionMessages)session.getAttribute("listErrorMessage");

    if (allErrors != null) {
      saveErrors(request, allErrors);
      session.removeAttribute("listErrorMessage");
    }

    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();

    long start = 0L;
    if (logger.isDebugEnabled()) {
      start = System.currentTimeMillis();
    }

    DynaActionForm emailListForm = (DynaActionForm)form;
    Integer folderID = (Integer)emailListForm.get("folderID");
    int currentAccountID = 0;

    MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
    try {
      // check for a valid email account
      Mail mailRemote = home.create();
      mailRemote.setDataSource(dataSource);

      int numberAccounts = mailRemote.getNumberOfAccountsForUser(individualId);

      if (numberAccounts < 1) {
        // if the user has less than 1 mail account
        // set up, then show them the door
        return (mapping.findForward(".view.email.setup"));
      }

      MailFolderVO folderVO;
      if (folderID != null) {
        folderVO = MailUtil.getFolderVO(folderID.intValue(), individualId, dataSource);
      } else {
        folderVO = MailUtil.getFolderVO(0, individualId, dataSource);
      }

      folderID = new Integer(folderVO.getFolderID());
      String folderName = folderVO.getFolderName();
      currentAccountID = folderVO.getEmailAccountID();
      HashMap folderList = mailRemote.getFolderList(currentAccountID);

      MailAccountVO accountVO = mailRemote.getMailAccountVO(currentAccountID);
      emailListForm.set("accountID", new Integer(currentAccountID));

      String accountType = accountVO.getAccountType();
      emailListForm.set("accountType", accountType);

      // This approach dirties the session, try to clean it up a bit.
      Enumeration nameEnum = session.getAttributeNames();
      while (nameEnum.hasMoreElements()) {
        String element = (String)nameEnum.nextElement();
        Object o = session.getAttribute(element);
        if (o instanceof BackgroundMailCheck) {
          if (!((BackgroundMailCheck)o).isAlive()) {
            session.removeAttribute("element");
          }
        }
      }

      /*
       * We check mail every time a user clicks on an IMAP folder. Thread
       * sychronization is taken care of in the EJB layer. We create a unique
       * session variable for each folder so each page knows if it should be
       * refreshing.
       */
      // We create the condition where the page continues to check because on
      // every refresh it doesn't
      // that it just checked. So it does again and again and again... "checked"
      // is to alleviate that.
      String clicked = request.getParameter("clicked");
      String checked = request.getParameter("checked");
      if ((checked == null || !checked.equals("true"))
          && (clicked != null && clicked.equals("true"))) {
        if (accountType.equals(MailAccountVO.IMAP_TYPE)) {
          // First check mailDaemon which will be for first timers
          BackgroundMailCheck mailDaemon = (BackgroundMailCheck)session.getAttribute("mailDaemon");
          if (mailDaemon == null || !mailDaemon.isAlive()) {
            // Then check the indivdual folder daemon
            String daemonName = folderName + "MailCheck";
            mailDaemon = (BackgroundMailCheck)session.getAttribute(daemonName);
            if (mailDaemon == null || !mailDaemon.isAlive()) {
              mailDaemon = new BackgroundMailCheck(daemonName + individualId, individualId,
                  dataSource, folderID.intValue());
              mailDaemon.start();
              session.setAttribute(daemonName, mailDaemon);
              request.setAttribute("checked", "true");
            }
          }
        }
      }
      // We must have to add the FolderName to the request
      // We will use this value from the tag lib.
      // To Identify wheather which folder we are processing
      request.setAttribute("folderName", folderName);

      // populate the moveTO button in Tag Lib
      // we will use this folderList
      request.setAttribute("folderList", folderList);
      request.setAttribute("folderID", folderID);

      emailListForm.set("folderList", folderList);
      emailListForm.set("folderType", folderVO.getFolderType());
      emailListForm.set("folderID", folderID);

      // now, we need to set some stuff up for the folder bar which
      // shows where the user is located within the folder hierarchy
      ArrayList folderPathList = mailRemote.getFolderFullPath(folderID.intValue());
      emailListForm.set("folderPathList", folderPathList);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
    }

    ListPreference listPreference = userObject.getListPreference("Email");
    ListView view = listPreference.getListView(String.valueOf(listPreference.getDefaultView()));
    ValueListParameters listParameters = null;
    ValueListParameters requestListParameters = (ValueListParameters)request
        .getAttribute("listParameters");

    if (requestListParameters == null) {
      // build up new Parameters
      listParameters = new ValueListParameters(ValueListConstants.EMAIL_LIST_TYPE, listPreference
          .getRecordsPerPage(), 1);
    } else {
      // paging or sorting or something, use the parameters from the request.
      listParameters = requestListParameters;
    }

    if (listParameters.getSortColumn() == 0) {
      FieldDescriptor sortField = (FieldDescriptor)ValueListConstants.emailViewMap
          .get(listPreference.getSortElement());
      listParameters.setSortColumn(sortField.getQueryIndex());

      if (listPreference.getsortOrder()) {
        listParameters.setSortDirection("ASC");
      } else {
        listParameters.setSortDirection("DESC");
      }
    }

    String filter = null;
    String filterParameter = request.getParameter("filter");

    if (filterParameter != null) {
      filter = (String)session.getAttribute("listFilter");
      request.setAttribute("appliedSearch", filterParameter);
    } else {
      session.removeAttribute("listFilter");
    }
    listParameters.setFilter(filter);

    Vector viewColumns = view.getColumns();
    ArrayList columns = new ArrayList();

    ActionUtil.mapOldView(columns, viewColumns, ValueListConstants.EMAIL_LIST_TYPE);
    listParameters.setColumns(columns);
    listParameters.setFolderID(folderID.intValue());
    // Get the list!
    ValueListHome valueListHome = (ValueListHome)CVUtility.getHomeObject(
        "com.centraview.valuelist.ValueListHome", "ValueList");
    ValueList valueList = null;

    try {
      valueList = valueListHome.create();
    } catch (CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    valueList.setDataSource(dataSource);
    ValueListVO listObject = valueList.getValueList(individualId, listParameters);
    ArrayList buttonList = new ArrayList();

    ValueListDisplay displayParameters = null;
    buttonList.add(new Button("Delete", "delete", "vl_deleteList();", false));
    buttonList.add(new Button("Mark Read", "read", "vl_readList();", false));
    buttonList.add(new Button("Mark UnRead", "unread", "vl_unReadList();", false));
    buttonList.add(new Button("Private", "private", "vl_privateList();", false));
    buttonList.add(new Button("Rules", "rule", "vl_ruleList(" + currentAccountID + ");", false));
    displayParameters = new ValueListDisplay(buttonList, true, true);

    listObject.setMoveTo(true);

    displayParameters.setSortable(true);
    displayParameters.setPagingBar(true);

    displayParameters.setLink(true);
    // Add the Attachment Column
    displayParameters.setAttachmentIcon(true);
    // Add the Priority Column
    displayParameters.setPriority(true);
    listObject.setDisplay(displayParameters);
    // Stick the list on the request and then the custom list tag will handle
    // it.
    if (logger.isDebugEnabled()) {
      long debugTime = (System.currentTimeMillis() - start);
      logger.debug("[execute] End to End: " + debugTime + " ms");
      listObject.getParameters().setDebugTime(debugTime);
    }

    request.setAttribute("valueList", listObject);
    request.setAttribute("showComposeButton", new Boolean(true));
    request.setAttribute("newButtonValue", "Compose");
    session.setAttribute("currentMailFolder", folderID);

    // For the searchBar
    String moduleID = (String)moduleList.get("Email");
    request.setAttribute("moduleId", moduleID);
    request.setAttribute("listType", "Email");
    return mapping.findForward(finalForward);
  }

}
