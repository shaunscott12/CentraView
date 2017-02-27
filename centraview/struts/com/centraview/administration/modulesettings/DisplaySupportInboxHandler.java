/*
 * $RCSfile: DisplaySupportInboxHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:45 $ - $Author: mking_cv $
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

package com.centraview.administration.modulesettings;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.ContactHelper;
import com.centraview.contact.helper.ContactHelperHome;
import com.centraview.mail.Mail;
import com.centraview.mail.MailHome;
import com.centraview.settings.Settings;
import com.centraview.support.helper.SupportHelper;
import com.centraview.support.helper.SupportHelperHome;

public class DisplaySupportInboxHandler extends Action
{
  public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      DynaActionForm dynaForm = (DynaActionForm)form;
      
      String submodule = request.getParameter("sourcefor");
      String setting = request.getParameter("setting");
      
      if (submodule == null) {
        submodule = "";
      }
      
      HttpSession session = request.getSession(true);
      
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      
      MailHome mailHome = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail mailRemote = (Mail)mailHome.create();
      mailRemote.setDataSource(dataSource);

      Vector accountList = (Vector)mailRemote.getAccountList();
      
      AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject("com.centraview.administration.applicationsettings.AppSettingsHome","AppSettings");
      AppSettings appRemote = (AppSettings)appHome.create();
      appRemote.setDataSource(dataSource);
      
      Vector supportMailIds = (Vector)appRemote.getSupportMailId();
      
      try {
        SupportHelperHome supportHome = (SupportHelperHome)CVUtility.getHomeObject("com.centraview.support.helper.SupportHelperHome", "SupportHelper");
        SupportHelper supportRemote = (SupportHelper)supportHome.create();
        supportRemote.setDataSource(dataSource);
        int supportInterval = supportRemote.getSupportEmailCheckInterval();
        dynaForm.set("emailCheckInterval", new Integer(supportInterval));
      } catch (Exception e) {
        dynaForm.set("emailCheckInterval", new Integer(0));
      }


      String ownerid = appRemote.getApplicationSettings("DEFAULTOWNER");
      if (ownerid.length() == 0 || ownerid == null) {
        ownerid = "";
      }
      String ownerName="";
      if (ownerid != null && !ownerid.equals("")) {
        ContactHelperHome contactHome = (ContactHelperHome)CVUtility.getHomeObject("com.centraview.contact.helper.ContactHelperHome", "ContactHelper");
        ContactHelper contactRemote =  contactHome.create();
        contactRemote.setDataSource(dataSource);
        ownerName = contactRemote.getIndividualName(Integer.parseInt(ownerid));
      }

      int sizeOfAccountList = accountList.size();
      int sizeOfSupportAccountList = accountList.size();
      Vector supportSendAccountList = new Vector();
      int i = 0;
      boolean removed = false;
      while (i < sizeOfAccountList) {
        removed = false;
        DDNameValue ddEmailAccountInfo = (DDNameValue) accountList.get(i);
        int accountID = ddEmailAccountInfo.getId();
        if (supportMailIds != null && supportMailIds.contains(String.valueOf(accountID))) {
          supportSendAccountList.add(ddEmailAccountInfo);
          accountList.remove(i);
          sizeOfAccountList--;
          removed = true;
        }
        if (!removed) {
          i++;
        }
      }

      String searchStr = request.getParameter("search");

      if (searchStr != null && !searchStr.equals("")){
        if (accountList != null) {
          int accountSize = accountList.size();
          i = 0;
          while (i < accountSize) {
            DDNameValue tempAccountListInfo = (DDNameValue) accountList.elementAt(i);
            int emailAccountID = tempAccountListInfo.getId();
            String emailAddress = tempAccountListInfo.getName();
            
            String tempEmailAddress = emailAddress.toUpperCase();
            String tempSearchEmailAddress = searchStr.toUpperCase();
            int occuranceVALUESearch = (tempEmailAddress).indexOf(tempSearchEmailAddress);
            if (occuranceVALUESearch == -1) {
              accountList.remove(i);
              accountSize --;
            } else {
              i++;
            }
          }
        }
      }
      
      request.setAttribute("employee", ownerName);
      request.setAttribute("employeeID", ownerid );
      request.setAttribute("emailAccountList", accountList);
      request.setAttribute("colsend", supportSendAccountList);
      request.setAttribute("typeofmodule",AdministrationConstantKeys.MODULESETTINGS);
      request.setAttribute("typeofsubmodule",submodule);
      request.setAttribute("settingfor",setting);
      
    } catch (Exception e) {
      System.out.println("[Exception][DisplaySupportInboxHandler.execute] Exception Thrown: "+e);
      e.printStackTrace();
    }
    return mapping.findForward(".view.administration.support_inbox");
  }

}

