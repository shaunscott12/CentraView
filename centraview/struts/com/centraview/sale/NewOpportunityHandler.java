/*
 * $RCSfile: NewOpportunityHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:39 $ - $Author: mking_cv $
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

package com.centraview.sale;

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
import com.centraview.common.DDNameValue;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * NewOpportunityHandler
 * Used to display new opportunity page
 */
public class NewOpportunityHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = ".view.error";
  private static final String FORWARD_newopportunity = ".view.sales.new_opportunity";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    try {
      request.setAttribute("Detail", "Detail");
      
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualId = userObject.getIndividualID() ;

      String firstName = (userObject.getfirstName() == null) ? "" : userObject.getfirstName();
      String lastName = (userObject.getlastName() == null) ? "" : userObject.getlastName();
      String individualName = firstName + " " + lastName;

      HashMap opportunityHashMap = null;

      String newWindowId = "1";
      OpportunityForm opportunityform = null;

      if ((request.getAttribute("NewForm") != null) && ((String) request.getAttribute("NewForm")).equals("New")) {
        opportunityform = new OpportunityForm();
      }else{
        opportunityform = (OpportunityForm) request.getAttribute("opportunityform");
      }

      opportunityform.setAcctmgrid(String.valueOf(individualId));
      opportunityform.setAcctmgrname(individualName);
      
      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector salesStatusList = (Vector) gml.get("AllSaleStatus");
      Iterator it = salesStatusList.iterator();
      
      while (it.hasNext()) {
        DDNameValue thisStatus = (DDNameValue) it.next();
        if ((thisStatus.getName()).equalsIgnoreCase("Pending")) {
          opportunityform.setStatusid(Integer.toString(thisStatus.getId()));
          opportunityform.setStatusname(thisStatus.getName());
          break;
        }
      }

      request.setAttribute("opportunityform", opportunityform);
      request.setAttribute(SaleConstantKeys.CURRENTTAB, SaleConstantKeys.DETAIL);
      request.setAttribute(SaleConstantKeys.TYPEOFOPERATION, SaleConstantKeys.ADD);
      request.setAttribute(SaleConstantKeys.WINDOWID, newWindowId);
      request.setAttribute("NewForm", "New");
      FORWARD_final = FORWARD_newopportunity;
    }catch(Exception e){
      System.out.println("[Exception] NewOpportunityHandler.execute: " + e.toString());
      FORWARD_final = GLOBAL_FORWARD_failure;
    }

    return mapping.findForward(FORWARD_final);
  }
  
}

