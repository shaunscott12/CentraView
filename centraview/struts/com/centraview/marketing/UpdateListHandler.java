/*
 * $RCSfile: UpdateListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:19 $ - $Author: mking_cv $
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
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.marketing.List.ListVO;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;


/**
 * Insert the type's description here.
 * Creation date: (6/4/2003 11:48:26 AM)
 * @author shirishd

 */
public class UpdateListHandler extends Action
{
  private static Logger logger = Logger.getLogger(UpdateListHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String returnStatus = "";
    String button = request.getParameter("save_close");
    String button1 = request.getParameter("save");

    DynaActionForm dynaForm = (DynaActionForm) form;
    HttpSession session = request.getSession(false);
    UserObject userobjectd = (UserObject) session.getAttribute("userobject");
    int individualID = userobjectd.getIndividualID();

    if (button != null)
    {
      returnStatus = ".view.marketing.listmanager";
    }
    else
    {
      returnStatus = ".view.marketing.editlistmanager";
    }

    this.updateList(form, individualID);

    ListGenerator lg = ListGenerator.getListGenerator(dataSource);
    lg.makeListDirty("Marketing");
    dynaForm.initialize(mapping);

    return (mapping.findForward(returnStatus));
  }

  public void updateList(ActionForm form, int individualID) throws CommunicationException, NamingException
  {
    DynaActionForm dynaform = (DynaActionForm) form;
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
	MarketingFacadeHome aa = (MarketingFacadeHome) CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");    
	try
    {

      MarketingFacade remote = (MarketingFacade) aa.create();
      remote.setDataSource(dataSource);
      ListVO listVO = new ListVO();
      listVO.setTitle((String)dynaform.get("listname").toString());
      listVO.setDescription((String)dynaform.get("listdescription").toString());
      if(dynaform.get("listid") != null)
      {
		int listID = Integer.parseInt((String) dynaform.get("listid").toString());
		listVO.setListID(listID);
      }
      remote.updateList(individualID, listVO);
    }
    catch (Exception e)
    {
      logger.error("[Exception] UpdateListHandler.Execute Handler ", e);
    }
  }
}
