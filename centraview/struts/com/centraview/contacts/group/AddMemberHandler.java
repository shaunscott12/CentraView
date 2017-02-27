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
import java.util.StringTokenizer;

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
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

public final class AddMemberHandler extends Action
{
  private static Logger logger = Logger.getLogger(AddMemberHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    int groupId = 0;
    try {
      HttpSession session = request.getSession(true);
      String selectedRowId = request.getParameter("memberId");
      StringTokenizer parseSelectedId = new StringTokenizer(selectedRowId, ",");
      int countOfRowId = parseSelectedId.countTokens();
      int[] memberIds = new int[countOfRowId];
      int i = 0;
      while (parseSelectedId.hasMoreTokens()) {
        memberIds[i] = Integer.parseInt(parseSelectedId.nextToken());
        i++;
      }
      groupId = Integer.parseInt(request.getParameter("groupId"));
      UserObject userobject = (UserObject)session.getAttribute("userobject");
      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = aa.create();
      remote.setDataSource(dataSource);
      remote.addContactToGroup(userobject.getIndividualID(), groupId, memberIds);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    StringBuffer path = new StringBuffer(mapping.findForward("viewGroup").getPath());
    path.append(groupId);
    return new ActionForward(path.toString(), true);
  }
}
