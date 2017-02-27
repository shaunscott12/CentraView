/*
 * $RCSfile: EditListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:12 $ - $Author: mking_cv $
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
package com.centraview.marketing;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.settings.Settings;

/**
 * Insert the type's description here.
 * Creation date: (6/17/2003 11:58:44 AM)
 * @author: shirish d
 * @deprecated This class isn't even finished.
 */
public class EditListHandler extends Action
{


  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    //System.out.println("List:EditListHandler");

    String returnStatus = "";
    String button = request.getParameter("save_close");
    String button1 = request.getParameter("save_new");
    //System.out.println("button " + button + " button1 " + button1);

    String status = "changes Group updated in the database";
    DynaActionForm dynaForm = (DynaActionForm) form;

    if (button != null)
    {
      returnStatus = "saveandclose";
    }
    else
    {
      returnStatus = "successandnew";
    }

    // this.updateGroup(form , status);
    ListGenerator lg = ListGenerator.getListGenerator(dataSource);
    lg.makeListDirty("Marketing");
    dynaForm.initialize(mapping);

    return (mapping.findForward(returnStatus));
  }

  public void updateList(ActionForm form, String status)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    DynaActionForm dynaform = (DynaActionForm) form;

    /*
    MarketingFacadeHome aa = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome","MarketingFacade");
    MarketingFacade remote =(MarketingFacade)aa.create();
    */
    try
    {
       //System.out.println("Trying to  HOME IN HANDLER"+groupVO.getGroupName());
       ContactFacadeHome cfh = (ContactFacadeHome) CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome",
           "ContactFacade");
       ContactFacade remote = (ContactFacade) cfh.create();
       remote.setDataSource(dataSource);
       //System.out.println("GOT HOME IN HANDLER");
      // listid = remote.updateList( userID , form );
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Marketing");
    }
    catch (Exception e)
    {
      System.out.println("[Exception] EditListHandler.updateList: " + e.toString());
      //System.out.println(e);
    }

    //System.out.println(status);
  }
}
