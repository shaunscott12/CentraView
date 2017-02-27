/*
 * $RCSfile: SaveNewOrderHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:22 $ - $Author: mking_cv $
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
package com.centraview.account.order;

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

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * SaveNewOrderHandler.java
 * This file is used for Saving New Order Details
 *
 * @author Sandeep Joshi
 */
public final class SaveNewOrderHandler extends Action
{

  private static Logger logger = Logger.getLogger(SaveNewOrderHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String btnClicked = (String) request.getParameter("btnClicked");

    HttpSession session = request.getSession();
    UserObject userobjectd = (UserObject) session.getAttribute("userobject");
    int individualID = userobjectd.getIndividualID();

    OrderForm oform = (OrderForm) form;
    OrderForm tempform = null;
    oform.convertFormbeanToValueObject("add");

    oform.convertArrayToItemLines();

    String returnStatus = "failure";

    if (btnClicked != null && btnClicked.equals("save"))
    {
      tempform = saveNewOrder(request, response, oform, individualID, dataSource);
      request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, "ShowOrder");
      request.setAttribute("body", AccountConstantKeys.EDIT);
      returnStatus = "save";
    }//end of if (btnClicked != null && btnClicked.equals("save"))
    else if (btnClicked != null && btnClicked.equals("saveClose"))
    {
      tempform = saveNewOrder(request, response, oform, individualID, dataSource);
      request.setAttribute("body", "list");
      returnStatus = "saveclose";
    }//end of else if (btnClicked != null && btnClicked.equals("saveClose"))
    else if (btnClicked != null && btnClicked.equals("saveNew"))
    {
      tempform = saveNewOrder(request, response, oform, individualID, dataSource);
      request.setAttribute("body", AccountConstantKeys.ADD);
      request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, null);
      returnStatus = "savenew";
    }//end of else if (btnClicked != null && btnClicked.equals("saveNew"))

    //form = tempform;
    request.setAttribute("orderform", tempform); // ToDo: check if only above line works or not

    return (mapping.findForward(returnStatus));
  }

  public OrderForm saveNewOrder(HttpServletRequest req,
    HttpServletResponse res, OrderForm of, int individualID, String dataSource) throws CommunicationException, NamingException
  {
    AccountFacadeHome accountFacadeHome = (AccountFacadeHome)
        CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome", "AccountFacade");
    try
    {
		    AccountFacade remote = (AccountFacade) accountFacadeHome.create();
		    remote.setDataSource(dataSource);
			int jurisdictionID = 0;
			int billingAddressID = 0;
			if(of.getJurisdictionID() != null && !((of.getJurisdictionID()).equals(""))){
				jurisdictionID = (of.getJurisdictionID()).intValue();
			}//end of if(of.getJurisdictionID() != null && !((of.getJurisdictionID()).equals("")))
			billingAddressID = of.getBillToAddIdValue();
			if(billingAddressID != 0 && jurisdictionID != 0){
				remote.setJurisdictionForAddress(billingAddressID,jurisdictionID);
			}//end of if(billingAddressID != 0 && jurisdictionID != 0)

			return (remote.createOrder(of, individualID)); // public OrderForm createOrder(...
    }
    catch (Exception e)
    {
	  logger.error("[Exception] SaveNewOrderHandler.Execute Handler ", e);
      return of;
    }
  }
}
