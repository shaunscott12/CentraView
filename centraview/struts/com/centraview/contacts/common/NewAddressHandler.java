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

package com.centraview.contacts.common;

import java.io.IOException;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public final class NewAddressHandler extends Action
{
  private static Logger logger = Logger.getLogger(NewAddressHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    String returnStatus = ".view.contacts.view_related_address";
    DynaActionForm dynaform = (DynaActionForm) form;
    AccountHelperHome home = (AccountHelperHome)
    CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome", "AccountHelper");
    
    try {
      AccountHelper remote = home.create();
      remote.setDataSource(dataSource);
      
      Vector taxJurisdiction = remote.getTaxJurisdiction();
      
      dynaform.set("jurisdictionVec",taxJurisdiction);
      dynaform.set("recordID", request.getParameter("recordID"));
      dynaform.set("recordName", request.getParameter("recordName"));
      dynaform.set("listType", request.getParameter("listType"));
      dynaform.set("listFor", request.getParameter("listFor"));
      dynaform.set("operation", "add");
    }catch(Exception e){
      logger.error("[Exception] NewAddressHandler.Execute Handler ", e);
      returnStatus = "failure";
    }
    return (mapping.findForward(returnStatus));
  }

}

