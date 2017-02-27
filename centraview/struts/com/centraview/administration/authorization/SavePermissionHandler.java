/*
 * $RCSfile: SavePermissionHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:36 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

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
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class SavePermissionHandler extends Action
{
  private static Logger logger = Logger.getLogger(SavePermissionHandler.class);
  public static String globalForward = "failure";
  public static String saveandclose = "saveandclose";
  public static String saveandnew = "saveandnew";
  private static String FORWARD_sale = "salepermission";
  private static String FORWARD_saveandclose = "saveandclose";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(false);
    String modulename = "";

    if (request.getParameter("modulename") != null) {
      modulename = (String) request.getParameter("modulename");
    } else {
      if (session.getAttribute("modulename") != null) {
        modulename = (String) session.getAttribute("modulename");
      }
    }

    AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");

    try {
      UserObject userObj = (UserObject) session.getAttribute("userobject");

      int uid = userObj.getIndividualID();
      String action = request.getParameter("closeornew");
      int rowId = 0;

      if ((String) request.getParameter("rowID") != null) {
        rowId = Integer.parseInt((String) request.getParameter("rowID"));
      } else {
        if ((String) session.getAttribute("rowID") != null) {
          rowId = Integer.parseInt((String) session.getAttribute("rowID"));
        }
      }

      request.setAttribute("rowID", rowId + "");

      String permission = request.getParameter("permission");

      if (permission == null) {
        permission = "";
      }

      int ownerId = 0;
      if (request.getParameter("textid") != null) {
        ownerId = Integer.parseInt(request.getParameter("textid").toString());
      }

      String[] viewSelect = new String[100];
      String[] modifySelect = new String[100];
      String[] deleteSelect = new String[100];

      Vector view = new Vector();
      Vector modify = new Vector();
      Vector delete = new Vector();

      int[] arrView = null;
      int[] arrModify = null;
      int[] arrDelete = null;

      if ((String[])request.getParameterValues("view") != null) {
        viewSelect = (String[])request.getParameterValues("view");

        if (viewSelect != null) {
          for (int i = 0; i < viewSelect.length; i++) {
            view.addElement(new Integer(viewSelect[i]));
          }

          arrView = new int[view.size()];

          Iterator it = view.iterator();
          int j = 0;

          while (it.hasNext()) {
            arrView[j] = ((Integer)it.next()).intValue();
            j = j + 1;
          }
        }
      }

      if ((String[]) request.getParameterValues("modify") != null) {
        modifySelect = (String[]) request.getParameterValues("modify");

        if (modifySelect != null) {
          for (int i = 0; i < modifySelect.length; i++) {
            modify.addElement(new Integer(modifySelect[i]));
          }

          Iterator it = modify.iterator();
          int j = 0;
          arrModify = new int[modify.size()];

          while (it.hasNext()) {
            arrModify[j] = ((Integer) it.next()).intValue();
            j = j + 1;
          }
        }
      }
      
      
      delete.addElement(new Integer(ownerId));    // owner always has delete privs
      if ((String[]) request.getParameterValues("deleten") != null) {
        deleteSelect = (String[]) request.getParameterValues("deleten");
        if (deleteSelect != null) {
          for (int i = 0; i < deleteSelect.length; i++) {
            delete.addElement(new Integer(deleteSelect[i]));
          }

          Iterator it = delete.iterator();
          int j = 0;
          arrDelete = new int[delete.size()];

          while (it.hasNext()) {
            arrDelete[j] = ((Integer) it.next()).intValue();
            j = j + 1;
          }
        }
      } else {
        arrDelete = new int[1];
        arrDelete[0] = ownerId;
      }

      Authorization authRemote = (Authorization)authHome.create();
      authRemote.setDataSource(dataSource);

      int flag = 0;

      if (request.getParameter("isPublic") != null) {
        flag = -1;
      }

      if (permission.equals("ListMember") || modulename.equals("ListManager")) {
        authRemote.saveMarketingRecordPermission(modulename, rowId, arrView, arrModify, arrDelete, flag);
        if (ownerId != 0) {
          authRemote.updateMarketingRecordOwner("Entity", rowId, ownerId);
          authRemote.updateMarketingRecordOwner("Individual", rowId, ownerId);
        }
      }else{
        authRemote.saveRecordPermission(uid,flag, modulename, rowId, arrView, arrModify,arrDelete);
      }

      if (ownerId != 0) {
        authRemote.updateOwner(modulename, rowId, ownerId);
      }
    }catch(Exception e){
        logger.error("[Exception] SavePermissionHandler.Execute Handler ", e);
    }

    //String moduleName = new String(session.getAttribute("moduleName") + "");
    if (modulename != null) {
      if (modulename.equals("File")) {
         String action = "";
         action = request.getParameter("TYPEOFACTIVITY").toString();
         String closeornew = request.getParameter("closeornew").toString();

         if ((action.equals("BUTTON")) && (closeornew.equals("close"))) {
           globalForward = FORWARD_saveandclose;
         } else {
           globalForward = FORWARD_saveandclose;
         }
      }

      if (modulename.equals("CVFolder")) {
        globalForward = FORWARD_saveandclose;
      }

      if (modulename.equals("Notes")) {
        globalForward = FORWARD_saveandclose;
      }

      if (modulename.equals("Sales")) {
        globalForward = FORWARD_sale;
      }

      if (modulename.equals("ListManager")) {
        request.setAttribute("modulename", modulename);
        globalForward = FORWARD_saveandclose;
      }

      if (modulename.equals("MarketingList")) {
        request.setAttribute("modulename", modulename);
        globalForward = FORWARD_saveandclose;
      }

      if (modulename.equals("Tasks")) {
        request.setAttribute("modulename", modulename);
        globalForward = "taskpermission";
      }

      if (modulename.equals("Time Slips")) {
        request.setAttribute("modulename", modulename);
        globalForward = "timeslipspermission";
      }

      if (modulename.equals("Ticket")) {
        globalForward = "ticketpermission";
      }

      if (modulename.equals("FAQ")) {
        globalForward = "faqpermission";
      }

      if (modulename.equals("Knowledgebase")) {
        request.setAttribute("modulename", modulename);
        globalForward = "knowledgebasepermission";
      }



      if (modulename.equals("Projects")) {
        String action = "";
        action = request.getParameter("TYPEOFACTIVITY").toString();

        if (!(action.equals("BUTTON"))) {
          globalForward = FORWARD_saveandclose;
        } else {
          globalForward = FORWARD_saveandclose;
        }
      }

      if (modulename.equals("Proposals")) {
        globalForward = FORWARD_saveandclose;
      }

      if (modulename.equals("Opportunities")) {
        globalForward = FORWARD_saveandclose;
      }

      if (modulename.equals("Entity")) {
        globalForward = FORWARD_saveandclose;
      }

      if (modulename.equals("Individual")) {
        globalForward = FORWARD_saveandclose;
      }

      if (modulename.equals("Group")) {
        globalForward = FORWARD_saveandclose;
      }
    }


    return (mapping.findForward(globalForward));
  }
   // end execute() method
}
 // end class definition
