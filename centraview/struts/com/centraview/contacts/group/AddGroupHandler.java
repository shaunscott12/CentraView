/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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

package com.centraview.contacts.group;

import java.io.IOException;

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

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

/**
 * @author: CentraView, LLC.
 */
public class AddGroupHandler extends Action {
  private static Logger logger = Logger.getLogger(AddGroupHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    HttpSession session = request.getSession(true);
    UserObject userobjectd = (UserObject)session.getAttribute("userobject");
    int individualId = userobjectd.getIndividualID();
    String saveAndClose = request.getParameter("save_close");
    String addmeberbutton = request.getParameter("button3");
    int groupid = this.saveGroup(form, individualId);
    DynaActionForm dynaForm = (DynaActionForm)form;
    dynaForm.initialize(mapping);
    if (addmeberbutton != null) {
      StringBuffer path = new StringBuffer();
      path.append(mapping.findForward("addmember").getPath());
      path.append("?rowId=");
      path.append(groupid);
      path.append("&launchAddMember=true");
      return new ActionForward(path.toString(), true);
    }
    if (saveAndClose == null) { // save and new
      return (mapping.findForward("success1"));
    }
    return (mapping.findForward("success2"));
  }

  public int saveGroup(ActionForm form, int individualId) throws CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    com.centraview.contact.group.GroupVOX group = new com.centraview.contact.group.GroupVOX(form,
        individualId);
    com.centraview.contact.group.GroupVO groupVO = group.getVO();
    ContactFacadeHome cfh = (ContactFacadeHome)CVUtility.getHomeObject(
        "com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
    int groupid = 0;
    try {
      ContactFacade remote = cfh.create();
      remote.setDataSource(dataSource);
      if (group.getGroupID() < 1) {
        groupid = remote.createGroup(individualId, groupVO);
      } else {
        remote.updateGroup(individualId, groupVO);
      }
    } catch (Exception e) {
      logger.error("[saveGroup] Exception thrown.", e);
    }
    return groupid;
  }
} // end of AddGroupHandler class
