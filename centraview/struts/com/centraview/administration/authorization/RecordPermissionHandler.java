/*
 * $RCSfile: RecordPermissionHandler.java,v $    $Revision: 1.3 $  $Date: 2005/07/25 13:36:10 $ - $Author: mcallist $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
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
import org.apache.struts.util.LabelValueBean;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.helper.ContactHelper;
import com.centraview.contact.helper.ContactHelperHome;
import com.centraview.file.FileConstantKeys;
import com.centraview.settings.Settings;

public class RecordPermissionHandler extends Action
{
  private static Logger logger = Logger.getLogger(RecordPermissionHandler.class);
  // TODO clean up these silly forwards.
  public static final String GLOBAL_FORWARD_failure = "failure";
  public static final String PERMISSION_PAGE = ".view.permission";
  private static final String FORWARD_proposals = PERMISSION_PAGE;
  private static final String FORWARD_projects = PERMISSION_PAGE;
  private static final String FORWARD_group = PERMISSION_PAGE;
  private static final String FORWARD_salesedit = PERMISSION_PAGE;
  private static final String FORWARD_timeslips = PERMISSION_PAGE;
  private static final String FORWARD_tasks = PERMISSION_PAGE;
  private static final String FORWARD_knowledgebase = PERMISSION_PAGE;
  private static final String FORWARD_entity = PERMISSION_PAGE;
  private static final String FORWARD_individual = PERMISSION_PAGE;
  private static final String FORWARD_displaynewnoteedit = PERMISSION_PAGE;
  private static final String FORWARD_fileedit = PERMISSION_PAGE;
  private static final String FORWARD_displaynewnote = "permissionnote";
  private static final String FORWARD_project = "projectpermission";
  private static final String FORWARD_file = "filepermission";
  private static final String FORWARD_folder = "folderpermission";
  private static final String FORWARD_sales = "salepermission";
  private static final String FORWARD_timeslip = "timeslips_permission";
  private static final String FORWARD_task = "task_permission";
  private static final String FORWARD_knowledgebasecategory = "knowledgebasecategory";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");
    try {
      HttpSession session = request.getSession(false);
      HashMap recordPermissions = new HashMap();
      RecordPermissionForm permissionForm = (RecordPermissionForm)form;
      Vector vecview = new Vector();
      Vector vecmodify = new Vector();
      Vector vecdelete = new Vector();
      Collection colview = new ArrayList();
      Collection colmodify = new ArrayList();
      Collection coldelete = new ArrayList();

      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int uid = userobjectd.getIndividualID();

      ModuleFieldRightMatrix rightsMatrix = userobjectd.getUserPref().getModuleAuthorizationMatrix();
      String modulename = "";
      if (request.getParameter("modulename") != null) {
        modulename = request.getParameter("modulename");
        request.setAttribute("modulename", modulename);
      } else {
        if (session.getAttribute("modulename") != null) {
          modulename = (String)session.getAttribute("modulename");
          session.setAttribute("modulename", modulename);
        }
      }

      int moduleID = permissionForm.getModuleID();
      String dbModuleName = new String("");

      if (moduleID != 0) {
        TreeMap mrights = rightsMatrix.getModuleRights();
        HashMap mything = (HashMap)mrights.get(new Integer(moduleID));
        dbModuleName = (String)mything.get("name");
      }

      if (((modulename == null) || modulename.equals("")) && (!dbModuleName.equals(""))) {
        modulename = dbModuleName;
      }
      int rowID = 0;
      if (request.getParameter("rowID") != null) {
        rowID = Integer.parseInt(request.getParameter("rowID"));
      } else if (request.getAttribute("rowID") != null) {
        rowID = Integer.parseInt((String)request.getAttribute("rowID"));
      } else {
        if (session.getAttribute("rowID") != null) {
          rowID = Integer.parseInt((String)session.getAttribute("rowID"));
        }
      }

      String permission = "";

      if (modulename != null && modulename.equals("ListManager")) {
        permission = "ListMember";
        request.setAttribute("permission", permission);
      }

      if (modulename != null && modulename.equals("MarketingList")) {
        request.setAttribute("permission", permission);
      }

      ContactHelperHome home = (ContactHelperHome)CVUtility.getHomeObject("com.centraview.contact.helper.ContactHelperHome", "ContactHelper");
      ContactHelper remote = home.create();
      remote.setDataSource(dataSource);

      Authorization authRemote = authHome.create();
      authRemote.setDataSource(dataSource);
      
      HashMap hmget = authRemote.getOwner(modulename, rowID);
      String sowner = hmget.get("name").toString();
      
      if (!(sowner == null) || (sowner.length() == 0)) {
        request.setAttribute("ownerName", sowner);
      } else {
        request.setAttribute("ownerName", "");
      }
      
      String sid = hmget.get("id").toString();
      
      if (!(sid.equals("null") || (sid.length() == 0))) {
        request.setAttribute("id", sid);
      } else {
        request.setAttribute("id", "");
      }


      recordPermissions = authRemote.getRecordPermission(modulename, rowID);

      if (recordPermissions == null) {
        recordPermissions = authRemote.getDefaultRecordPermission(uid);
      }


      if (! authRemote.getRecordFromPublic(modulename, rowID).equalsIgnoreCase("Yes")) {
        if (recordPermissions != null) {
          vecview = (Vector)recordPermissions.get("VIEW");
          vecmodify = (Vector)recordPermissions.get("UPDATE");
          vecdelete = (Vector)recordPermissions.get("DELETE");
        }
      } else {
        request.setAttribute("isPublic", "Yes");
      }

      ContactFacadeHome facadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade cfremote = facadeHome.create();
      cfremote.setDataSource(dataSource);
      
      ArrayList employeeList = (ArrayList) cfremote.getEmployeeListDisplay();
      if (employeeList != null && employeeList.size() != 0) {
        Iterator i = employeeList.iterator();
        while (i.hasNext()) {
	      	Object employeeObject = i.next();
          
	      	if (employeeObject instanceof LabelValueBean) {
		      	LabelValueBean employee = (LabelValueBean) employeeObject;
		        Long employeeId = new Long(employee.getValue());
		        if (vecview.contains(employeeId)) {
		          i.remove();
		          colview.add(employee);
		        } else if (vecmodify.contains(employeeId)) {
		          i.remove();
		          colmodify.add(employee);
		        } else if (vecdelete.contains(employeeId)) {
		          i.remove();
			      coldelete.add(employee);
		        }
	      	}
	      }
      }
      permissionForm.setEmployeeList(employeeList);

      request.setAttribute("colview", colview);
      request.setAttribute("colmodify", colmodify);
      request.setAttribute("coldelete", coldelete);

      request.setAttribute("view", vecview);
      request.setAttribute("modify", vecmodify);
      request.setAttribute("delete", vecdelete);

      request.setAttribute("permissionform", permissionForm);
      request.setAttribute("modulename", modulename);
      request.setAttribute("rowID", String.valueOf(rowID));
      request.setAttribute("rowId", String.valueOf(rowID));

      if (modulename != null) {
        if (modulename.equals("Notes")) {
          FORWARD_final = FORWARD_displaynewnote;
          if (request.getParameter("permissionnoteedit") != null) {
            if (request.getParameter("permissionnoteedit").equals("1")) {
              FORWARD_final = FORWARD_displaynewnoteedit;
            }
          }
        }

        if (modulename.equals("File")) {
          FORWARD_final = FORWARD_file;

          if (request.getParameter("filepermissionedit") != null) {
            if (request.getParameter("filepermissionedit").equals("1")) {
              FORWARD_final = FORWARD_fileedit;
            }
          }

          request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
        }

        if (modulename.equals("Marketing")) {
          FORWARD_final = FORWARD_group;
          request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
        }

        if (modulename.equals("CVFolder")) {
          FORWARD_final = FORWARD_folder;
          request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
          request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
        }

        if (modulename.equals("Sales")) {
          FORWARD_final = FORWARD_sales;

          if (request.getParameter("salepermissionedit") != null) {
            if (request.getParameter("salepermissionedit").equals("1")) {
              FORWARD_final = FORWARD_salesedit;
            }
          }
        }

        if (modulename.equals("Projects")) {
          if (session.getAttribute("isnewproject") != null) {
            session.removeAttribute("isnewproject");
            FORWARD_final = FORWARD_project;
          } else {
            FORWARD_final = FORWARD_projects;
          }
        }

        if (modulename.equals("Opportunities")) {
          FORWARD_final = FORWARD_proposals;
        }

        if (modulename.equals("Proposals")) {
          FORWARD_final = FORWARD_proposals;
        }

        if (modulename.equals("Time Slips")) {
          if (session.getAttribute("isnewtimeslips") != null) {
            session.removeAttribute("isnewtimeslips");
            FORWARD_final = FORWARD_timeslip;
          } else {
            FORWARD_final = FORWARD_timeslips;
          }
        }

        if (modulename.equals("Tasks")) {
          if (session.getAttribute("isnewtask") != null) {
            session.removeAttribute("isnewtask");
            FORWARD_final = FORWARD_task;
          } else {
            FORWARD_final = FORWARD_tasks;
          }
        }

        if (modulename.equals("Entity")) {
          FORWARD_final = FORWARD_entity;
        }

        if (modulename.equals("ListManager")) {
          FORWARD_final = PERMISSION_PAGE;
        }

        if (modulename.equals("MarketingList")) {
          FORWARD_final = PERMISSION_PAGE;
        }

        if (modulename.equals("Individual")) {
          FORWARD_final = FORWARD_individual;
        }

        if (modulename.equals("Ticket")) {
          FORWARD_final = PERMISSION_PAGE;
        }

        if (modulename.equals("FAQ")) {
          FORWARD_final = PERMISSION_PAGE;
        }

        if (modulename.equals("Knowledgebase")) {
          FORWARD_final = FORWARD_knowledgebase;
          if (session.getAttribute("Type") != null) {
            if ((session.getAttribute("Type").equals("Category"))) {
              FORWARD_final = FORWARD_knowledgebasecategory;
            }
          }
          request.setAttribute("DETAILPRESS", "No");
        }

        if (modulename.equals("knowledgecategory")) {
          FORWARD_final = FORWARD_knowledgebasecategory;
        }

        if (modulename.equals("Group")) {
          FORWARD_final = FORWARD_group;
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] RecordPermissionHandler.Execute Handler ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
