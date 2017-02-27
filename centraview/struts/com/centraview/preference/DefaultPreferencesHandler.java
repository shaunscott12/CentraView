/*
 * $RCSfile: DefaultPreferencesHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:04 $ - $Author: mcallist $
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

package com.centraview.preference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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

import com.centraview.administration.authorization.Authorization;
import com.centraview.administration.authorization.AuthorizationHome;
import com.centraview.administration.authorization.RecordPermissionForm;
import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.DDNameValue;
import com.centraview.common.IndividualList;
import com.centraview.common.IndividualListElement;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

public class DefaultPreferencesHandler extends Action
{
	private static Logger logger = Logger.getLogger(DefaultPreferencesHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		AuthorizationHome authHome = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");
    try {
      HttpSession session = request.getSession(false);

      HashMap hm = new HashMap();

      RecordPermissionForm permissionForm = (RecordPermissionForm)form;

      if (permissionForm == null) {
        permissionForm = new RecordPermissionForm();
      }
      permissionForm.setModify("");
      permissionForm.setDeleten("");
      permissionForm.setView("");

      Vector vecview = new Vector();
      Vector vecmodify = new Vector();
      Vector vecdelete = new Vector();

      Collection colview = new ArrayList();
      Collection colmodify = new ArrayList();
      Collection coldelete = new ArrayList();

      int uid = Integer.parseInt(request.getParameter("contactID").toString());
      request.setAttribute("userID",new Integer(uid));

      Authorization authRemote = (Authorization)authHome.create();

      hm = authRemote.getDefaultPermissions(uid);

      if (! authRemote.getUserDefaultPermission(uid).equalsIgnoreCase("Yes")) {
        if (hm != null) {
          vecview = (Vector)hm.get("VIEW");
          vecmodify = (Vector)hm.get("UPDATE");
          vecdelete = (Vector)hm.get("DELETE");
        }
      }else{
        request.setAttribute("isPublic","Yes");
      }


      ContactFacadeHome facadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade cfremote = (ContactFacade)facadeHome.create();
      cfremote.setDataSource(dataSource);

      // get the records from the database
      Collection sqlResults = cfremote.getUserListCollection(uid);

      // now create a DisplayList out of those records
      IndividualList employeeList = new IndividualList();

      // ok, now that we have the results from the database,
      // we need to generate a DisplayList object to pass
      // to the List View code (List.jsp)
      Iterator iter = sqlResults.iterator();

      while (iter.hasNext()) {
        HashMap sqlRow = (HashMap)iter.next();

        String individualName = (String)sqlRow.get("Name");
        int individualID  = ((Number)sqlRow.get("IndividualID")).intValue();

        IntMember individualIDfield = new IntMember("IndividualID", individualID, 10, "", 'T', false, 10);
        StringMember nameField  = new StringMember("Name", individualName, 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.INDIVIDUAL  + "&rowId=" + individualID, 'T', true);

        if (vecview != null && vecview.contains(new Long(individualID))) {
          colview.add(new DDNameValue(""+individualID, nameField.getDisplayString()));
        }else if (vecmodify != null && vecmodify.contains(new Long(individualID))){
          colmodify.add(new DDNameValue(""+individualID, nameField.getDisplayString()));
        }else if (vecdelete != null && vecdelete.contains(new Long(individualID))){
          coldelete.add(new DDNameValue(""+individualID, nameField.getDisplayString()));
        }

        IndividualListElement ele = new IndividualListElement(individualID);
        ele.put("IndividualID", individualIDfield);
        ele.put("Name", nameField);

        employeeList.put(individualName + individualID, ele);
      }   // end while(iter.hasNext())

      request.setAttribute("employeelist", employeeList);

      request.setAttribute("colview", colview);
      request.setAttribute("colmodify", colmodify);
      request.setAttribute("coldelete", coldelete);

      request.setAttribute("view", vecview);
      request.setAttribute("modify", vecmodify);
      request.setAttribute("delete", vecdelete);

      request.setAttribute("permissionform", permissionForm);
      request.setAttribute("typeofmodule", AdministrationConstantKeys.USERADMINISTRATION);
      request.setAttribute("typeofsubmodule", AdministrationConstantKeys.USERADMINISTRATION_USER);

      request.setAttribute(AdminConstantKeys.PREFERENCEPAGE,"DEFAULTPREFERENCES");
    }catch(Exception e){
      logger.error("[Exception] DefaultPreferencesHandler.Execute Handler ", e);
    }
    return mapping.findForward(".view.administration.default_preferences");
  }   // end execute() method

}   // end class definition

