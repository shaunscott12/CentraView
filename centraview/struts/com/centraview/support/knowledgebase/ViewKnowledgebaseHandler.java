/*
 * $RCSfile: ViewKnowledgebaseHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/21 17:42:05 $ - $Author: mcallist $
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

package com.centraview.support.knowledgebase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.support.common.SupportConstantKeys;
import com.centraview.support.supportfacade.SupportFacade;
import com.centraview.support.supportfacade.SupportFacadeHome;

/**
 * This handler Handles View Knowledgebase.
 */
public class ViewKnowledgebaseHandler extends Action {
  private static String FORWARD_final = "failure";

  private static Logger logger = Logger.getLogger(ViewKnowledgebaseHandler.class);

  /**
   * This method is overridden from Action Class
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualID = userObject.getIndividualID();

    KnowledgeBaseForm kbForm = (KnowledgeBaseForm)form;

    SupportFacadeHome supportFacade = (SupportFacadeHome)CVUtility.getHomeObject(
        "com.centraview.support.supportfacade.SupportFacadeHome", "SupportFacade");

    request.setAttribute(SupportConstantKeys.TYPEOFSUBMODULE, "Knowledgebase");
    KnowledgeVO kVO = new KnowledgeVO();
    SupportFacade remote = null;
    int kbId = -1;
    try {
      String rowId = request.getParameter("rowId");
      if (rowId != null) {
        if (rowId.indexOf("*") > -1) {
          String elements[] = rowId.split("\\*");
          if (elements.length != 2)
            throw new Exception("No row selected.");
          kbId = Integer.parseInt(elements[0]);
        } else {
          kbId = Integer.parseInt(rowId);
        }
      } else {
        throw new Exception("No row selected.");
      }

      remote = supportFacade.create();
      remote.setDataSource(dataSource);
      kVO = remote.getKB(individualID, kbId);
      ArrayList categoryList = remote.getAllCategory(individualID);
      request.setAttribute("CATEGORYVOARRAY", categoryList);
    } catch (Exception e) {
      logger.error("[Exception] [ViewKnowledgebaseHandler.execute Calling SupportFacade]  ", e);
    }
    int ownerID = 0;
    if (kVO != null) {
      kbForm.setTitle(kVO.getTitle());
      kbForm.setDetail(kVO.getDetail().replaceAll("\n","<br/>"));
      kbForm.setCategory(new Integer(kVO.getCatid()).toString());
      kbForm.setRowId(new Integer(kbId).toString());
      kbForm.setParentcategory(new Integer(kVO.getParent()).toString());
      kbForm.setStatus(kVO.getStatus());
      kbForm.setPublishToCustomerView(kVO.getPublishToCustomerView());
      ownerID = kVO.getOwner();

      // Condition if the record status is Publish then Only Owner can update
      // it.
      // Other user can't update it.
      // thats why we will disable the Edit button
      boolean editFlag = false;
      if (kVO.getStatus() != null && kVO.getStatus().equals("PUBLISH")) {
        AuthorizationHome homeAuthorization = (AuthorizationHome)CVUtility.getHomeObject(
            "com.centraview.administration.authorization.AuthorizationHome", "Authorization");
        try {
          Authorization remoteAuthorization = homeAuthorization.create();
          remoteAuthorization.setDataSource(dataSource);
          editFlag = remoteAuthorization.canPerformRecordOperation(individualID, "KnowledgeBase",
              kbId, ModuleFieldRightMatrix.UPDATE_RIGHT);
        } catch (Exception e) {
          logger.error("[Exception] [ViewKnowledgebaseHandler.execute Calling Authorization]  ", e);
        }
      } else if (individualID == ownerID) {
        editFlag = true;
      }
      request.setAttribute("showEditRecordButton", new Boolean(editFlag));
    }
    request.setAttribute("showPermissionButton", new Boolean("true"));
    String show = (String)request.getAttribute("show");
    String typeOfOperation = (String)request.getAttribute(Constants.TYPEOFOPERATION);
    if (typeOfOperation == null)
      typeOfOperation = request.getParameter(Constants.TYPEOFOPERATION);

    if (typeOfOperation != null) {
      if (typeOfOperation.equalsIgnoreCase("editkb")) {
        FORWARD_final = ".view.support.knowledgebase.edit";
        if (show != null) {
          kbForm.setTitle("");
          kbForm.setDetail("");
        }
        // Get all categories and recurively process them for there parent/child
        // relationships.
        ArrayList flatList = remote.getAllCategory(individualID);
        ArrayList categoryList = new ArrayList();

        categoryList.add(new DDNameValue(1, "Knowledgebase"));
        Iterator iter = flatList.iterator();
        while (iter.hasNext()) {
          CategoryVO catVO = (CategoryVO)iter.next();
          if (catVO.getParent() == 1)
            KBUtil.processCategory(flatList, catVO, categoryList, 0);
        }
        request.setAttribute("CATEGORYVOARRAY", categoryList);
        request.setAttribute("DETAILPRESS", "YES");
      }
    } else {
      FORWARD_final = ".view.support.knowledgebase.view";
    }
    request.setAttribute("kbform", kbForm);
    return (mapping.findForward(FORWARD_final));
  }
}
