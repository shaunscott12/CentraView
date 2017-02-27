/*
 * $RCSfile: TemplateListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/02 14:46:35 $ - $Author: mcallist $
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

package com.centraview.administration.modulesettings.templates;

import java.io.IOException;
import java.util.ArrayList;

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

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This Action class will get a list of Mail Merge Templates from
 * the database.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class TemplateListHandler extends Action
{
  private static Logger logger = Logger.getLogger(TemplateListHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, CommunicationException, IOException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    UserObject user = (UserObject)session.getAttribute("userobject");
    int individualId = user.getIndividualID();
    // Handle Paging, etc
    ListPreference listPreference = user.getListPreference("Template");
    ValueListParameters listParameters = ActionUtil.valueListParametersSetUp(listPreference, request, ValueListConstants.TEMPLATE_LIST_TYPE, ValueListConstants.templateViewMap, true);
    // Get the list from the database.
    ValueListHome valueListHome = (ValueListHome) CVUtility.getHomeObject("com.centraview.valuelist.ValueListHome","ValueList");
    ValueList valueList = null;
    try
    {
      valueList = valueListHome.create();
    } catch (CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    valueList.setDataSource(dataSource);
    ValueListVO listObject = valueList.getValueList(individualId, listParameters);
    // Got the list now set the display preferences on the ValueListDisplay Object.
    ArrayList buttonList = new ArrayList();
    buttonList.add(new Button("New Template", "newTemplate", "c_goTo('/administration/new_template.do');", false));
    buttonList.add(new Button("Delete", "deleteTemplate", "vl_deleteList();", false));
    ValueListDisplay displayParameters = new ValueListDisplay(buttonList, true, false, true, true, true, true);
    listObject.setDisplay(displayParameters);
    request.setAttribute("valueList", listObject);
    TemplateUtil.setNavigation(request, "list");
    return mapping.findForward(".view.administration.template_list");
  }
}
