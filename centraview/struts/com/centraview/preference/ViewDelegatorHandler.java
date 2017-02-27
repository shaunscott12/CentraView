/*
 * $RCSfile: ViewDelegatorHandler.java,v $    $Revision: 1.3 $  $Date: 2005/09/01 15:31:04 $ - $Author: mcallist $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.DDNameValue;
import com.centraview.common.IndividualList;
import com.centraview.common.IndividualListElement;
import com.centraview.common.IntMember;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.preference.common.AdminConstantKeys;
import com.centraview.settings.Settings;

public class ViewDelegatorHandler extends Action
{

  public static String globalForward = ".view.preference.mail.delegation_settings";
  public static String successForward = ".view.preference.mail.delegation_settings";

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HashMap hm = new HashMap();

      Vector vecView = new Vector();
      Vector vecAll = new Vector();
      Vector vecSchedule = new Vector();
      Vector vecSend = new Vector();

      Collection colview = new ArrayList();
      Collection colAll = new ArrayList();
      Collection colschedule = new ArrayList();
      Collection colsend = new ArrayList();

      String typeofModule = "";

      HttpSession session = request.getSession(true);
      com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject)session.getAttribute("userobject"); //get the user object
      int individualId = userobjectd.getIndividualID();

      ListGenerator lg = ListGenerator.getListGenerator(dataSource);

      String moduleName = "";
      typeofModule = (String)request.getParameter("TYPEOFMODULE");

      if (typeofModule == null) {
        typeofModule = (String)request.getAttribute("TYPEOFMODULE");
      }
      if (typeofModule == null) {
          typeofModule = Constants.EMAILMODULE;
      }      

      if (typeofModule != null) {
        if (typeofModule.equals(Constants.ACTIVITYMODULE)) {
          moduleName = Constants.ACTIVITYMODULE;
        } else if (typeofModule.equals(Constants.EMAILMODULE)) {
          moduleName = Constants.EMAILMODULE;
        }
      }
      
      PreferenceHome prefHome = (PreferenceHome)CVUtility.getHomeObject("com.centraview.preference.PreferenceHome", "Preference");
      Preference prefRemote = (Preference)prefHome.create();
      prefRemote.setDataSource(dataSource);
      
      if (moduleName.equals(Constants.ACTIVITYMODULE)) {
    	hm = prefRemote.getUserDelegation(individualId, moduleName);
        vecView = (Vector)hm.get(Constants.VIEW);
        vecSchedule = (Vector)hm.get(Constants.SCHEDULEACTIVITY);
        vecAll = (Vector)hm.get(Constants.VIEWSCHEDULEACTIVITY);
      } else if (moduleName.equals(Constants.EMAILMODULE)) {
    	Vector emailVecSend = prefRemote.getEmailDelegation(individualId);
        vecView = (Vector)hm.get(Constants.VIEW);
        vecSend = emailVecSend;
        vecAll = (Vector)hm.get(Constants.VIEWSENDEMAIL);
      }

      ContactFacadeHome facadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade cfremote = (ContactFacade) facadeHome.create();
      cfremote.setDataSource(dataSource);

      // get the records from the database
      Collection sqlResults = cfremote.getEmployeeListCollection(individualId);

      // now create a DisplayList out of those records
      IndividualList employeeList = new IndividualList();

      // ok, now that we have the results from the database,
      // we need to generate a DisplayList object to pass
      // to the List View code (List.jsp)
      Iterator iter = sqlResults.iterator();

      while (iter.hasNext()) {
        HashMap sqlRow = (HashMap)iter.next();

        String individualName = (String)sqlRow.get("Name");
        int individualID = ((Number)sqlRow.get("IndividualID")).intValue();

        IntMember individualIDfield = new IntMember("IndividualID", individualID, 10, "", 'T', false, 10);

        StringMember nameField = new StringMember("Name", individualName, 10, "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.INDIVIDUAL + "&rowId=" + individualID, 'T', true);

        if (moduleName.equals(Constants.ACTIVITYMODULE)) {
          if ((vecView != null) && vecView.contains(new Integer(individualID))) {
            colview.add(new DDNameValue("" + individualID, nameField.getDisplayString()));
          } else if ((vecSchedule != null) && vecSchedule.contains(new Integer(individualID))) {
            colschedule.add(new DDNameValue("" + individualID, nameField.getDisplayString()));
          } else if ((vecAll != null) && vecAll.contains(new Integer(individualID))) {
            colAll.add(new DDNameValue("" + individualID, nameField.getDisplayString()));
          }
        } else if (moduleName.equals(Constants.EMAILMODULE)) {
          if ((vecView != null) && vecView.contains(new Integer(individualID))) {
            colview.add(new DDNameValue("" + individualID, nameField.getDisplayString()));
          } else if ((vecSend != null) && vecSend.contains(new Integer(individualID))) {
            colsend.add(new DDNameValue("" + individualID, nameField.getDisplayString()));
          } else if ((vecAll != null)  && vecAll.contains(new Integer(individualID))) {
            colAll.add(new DDNameValue("" + individualID, nameField.getDisplayString()));
          }
        }
        
        IndividualListElement ele = new IndividualListElement(individualID);
        ele.put("IndividualID", individualIDfield);
        ele.put("Name", nameField);
        
        employeeList.put(individualName + individualID, ele);
      }

      
      String searchStr = request.getParameter("search");

      if (searchStr != null && (searchStr.trim()).length() > 0) {
        searchStr = searchStr.trim();
        employeeList.setSearchString(searchStr);
        employeeList.search();
      }

      employeeList.setTotalNoOfRecords(employeeList.size());

      request.setAttribute("employeelist", employeeList);
      request.setAttribute("list", "Individual");
      request.setAttribute("typeofmodule", typeofModule);

      if (moduleName.equals(Constants.ACTIVITYMODULE)) {
        request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "ACTIVITYDELEGATION");
        request.setAttribute("colview", colview);
        request.setAttribute("colschedule", colschedule);
        request.setAttribute("colviewschedule", colAll);

        request.setAttribute(Constants.VIEW, vecView);
        request.setAttribute(Constants.SCHEDULEACTIVITY, vecSchedule);
        request.setAttribute(Constants.VIEWSCHEDULEACTIVITY, vecAll);
        successForward = ".view.preference.calendar.delegation_settings";
      } else if (moduleName.equals(Constants.EMAILMODULE)) {
        request.setAttribute(AdminConstantKeys.PREFERENCEPAGE, "EMAILDELEGATION");
        request.setAttribute("colview", (colview != null) ? colview : new ArrayList());
        request.setAttribute("colsend", (colsend != null) ? colsend : new ArrayList());
        request.setAttribute("colAllsend", (colAll != null) ? colAll : new ArrayList());

        request.setAttribute(Constants.VIEW, vecView);
        request.setAttribute(Constants.SENDEMAIL, vecSend);
        request.setAttribute(Constants.VIEWSENDEMAIL, vecAll);
        successForward = ".view.preference.mail.delegation_settings";
      }
      globalForward = successForward;
    } catch (Exception e) {
      System.out.println("[Exception][ViewDelegatorHandler.execute] Exception Thrown: " + e);
      System.out.println("Error in ViewDelegatorHandler " + e);
      e.printStackTrace();
    }
    return mapping.findForward(globalForward);
  }
}
