/*
 * $RCSfile: InvoiceListHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.customer.accounting;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.centraview.common.CVUtility;
import com.centraview.common.ListPreference;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ActionUtil;
import com.centraview.valuelist.ValueList;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListDisplay;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class InvoiceListHandler extends Action
{
  private static Logger logger = Logger.getLogger(InvoiceListHandler.class);
  
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    String forward = ".view.customer.invoice_list";
    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualID = userObject.getIndividualID(); // logged in user
    int entityID = userObject.getEntityId(); // logged in user's entityID
    ActionErrors allErrors = new ActionErrors();
    try {
      ListPreference listPrefs = userObject.getListPreference("InvoiceHistory");
      ValueListParameters listParameters = ActionUtil.valueListParametersSetUp(listPrefs, request, ValueListConstants.CUSTOMER_INVOICE_LIST_TYPE, ValueListConstants.customerInvoiceViewMap, false);
      String filter = "SELECT invoice.InvoiceID FROM invoice WHERE invoice.CustomerID = " + entityID;
      listParameters.setFilter(filter);
      ValueList valueList = (ValueList)CVUtility.setupEJB("ValueList", "com.centraview.valuelist.ValueListHome", dataSource);
      ValueListVO listObject = valueList.getValueList(individualID, listParameters);
      ValueListDisplay displayParameters = new ValueListDisplay(new ArrayList(), false, false, true, true, true, true);
      listObject.setDisplay(displayParameters);
      request.setAttribute("valueList", listObject);
    } catch (Exception e) {
      logger.error("[execute] Exception thrown.", e);
      allErrors.add("error.unknownError", new ActionMessage("error.unknownError"));
    }
    if (!allErrors.isEmpty()) {
      saveErrors(request, allErrors);
    }
    return (mapping.findForward(forward));
  } // end execute() method
} // end class definition
