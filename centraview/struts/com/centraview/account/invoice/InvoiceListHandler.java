/*
 * $RCSfile: InvoiceListHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:17 $ - $Author: mking_cv $
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

package com.centraview.account.invoice;

import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.ListPreference;
import com.centraview.common.StringMember;
import com.centraview.settings.Settings;

public class InvoiceListHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(InvoiceListHandler.class);
  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_showinvoicelist = "showinvoicelist";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    long entityID = 0;
    long orderID = 0;
    try
    {

      HttpSession session = request.getSession(true);
      session.setAttribute("highlightmodule", "account");
      String entityidStr = request.getParameter("EntityID");
      String orderidStr = request.getParameter("OrderID");

      if (entityidStr != null && !entityidStr.equals(""))
        entityID = Long.parseLong(entityidStr);

				// After performing the logic in the DeleteHanlder, we are generat a new request for the list
				// So we will not be carrying the old error. So that we will try to collect the error from the Session variable
				// Then destory it after getting the Session value
				if (session.getAttribute("listErrorMessage") != null)
				{
					ActionErrors allErrors = (ActionErrors) session.getAttribute("listErrorMessage");
					saveErrors(request, allErrors);
					session.removeAttribute("listErrorMessage");
				}//end of if (session.getAttribute("listErrorMessage") != null)
				
      if (orderidStr != null && !orderidStr.equals(""))
        orderID = Long.parseLong(orderidStr);

      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);
      request.setAttribute("body", "list");

      com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject)session.getAttribute("userobject");//get
                                                                                                                          // the
                                                                                                                          // user
                                                                                                                          // object
      int individualID = userobjectd.getIndividualID();
      ListPreference listpreference = userobjectd.getListPreference("InvoiceHistory");

      InvoiceList displaylistSession = null;
      InvoiceList displaylist = null;

      try
      {
        displaylistSession = (InvoiceList)session.getAttribute("displaylist");//gets
                                                                              // the
                                                                              // list
                                                                              // from
                                                                              // session
      } catch (Exception e)
      {
        displaylistSession = null;
      }
      try
      {
        displaylist = (InvoiceList)request.getAttribute("displaylist");//gets
                                                                       // the
                                                                       // list
                                                                       // from
                                                                       // request
      } catch (Exception e)
      {
        displaylist = null;
      }

      InvoiceList DL = null;
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);

      if (displaylist == null)
      {
        int records = listpreference.getRecordsPerPage();//gets the initial
                                                         // record per page to
                                                         // be displayed from
                                                         // listPreference
        String sortelement = listpreference.getSortElement();//gets the initial
                                                             // sort element
                                                             // from
                                                             // listPreference
        DL = null;
        DL = (InvoiceList)lg.getInvoiceList(individualID, 1, records, "", sortelement, entityID, orderID);//called
                                                                                                          // when
                                                                                                          // the
                                                                                                          // request
                                                                                                          // for
                                                                                                          // the
                                                                                                          // list
                                                                                                          // is
                                                                                                          // for
                                                                                                          // first
                                                                                                          // time

        if (entityidStr != null)
          DL.setEntityID(entityID);

        if (orderidStr != null)
          DL.setOrderID(orderID);

        DL = setLinksfunction(DL);
      } else
      //if(displaylistSession !=null)
      {
        String searchSession = displaylistSession.getSearchString();
        String searchrequest = displaylist.getSearchString();
        if (searchSession == null)
          searchSession = "";
        if (searchrequest == null)
          searchrequest = "";

        if (((displaylistSession.getListID() == displaylist.getListID()) && (displaylist.getDirtyFlag() == false) && (displaylist.getStartAT() >= displaylistSession.getBeginIndex()) && (displaylist.getEndAT() <= displaylistSession.getEndIndex()) && (displaylist.getSortMember().equals(displaylistSession.getSortMember())) && (displaylist
            .getSortType() == (displaylistSession.getSortType()) && (searchSession.equals(searchrequest))))
            || displaylist.getAdvanceSearchFlag() == true)
        {
          entityID = displaylistSession.getEntityID();
          orderID = displaylistSession.getOrderID();
          DL = (InvoiceList)displaylistSession;
          DL.setEntityID(entityID);
          DL.setOrderID(orderID);
        } else
        {
          entityID = displaylistSession.getEntityID();
          orderID = displaylistSession.getOrderID();

          displaylist.setEntityID(entityID);
          displaylist.setOrderID(orderID);

          DL = (InvoiceList)lg.getInvoiceList(individualID, displaylist);
          DL.setEntityID(entityID);
          DL.setOrderID(orderID);
        }
        DL = setLinksfunction(DL);

      }
      session.setAttribute("displaylist", DL);
      request.setAttribute("displaylist", DL);

      // forward to jsp page
      FORWARD_final = FORWARD_showinvoicelist;
    } catch (Exception e)
    {
      logger.error("[execute] Exception thrown.", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }

  /**
   * @ uses ListElement and various member classes This function sets links on
   * members @ returns InventoryList object
   */

  public InvoiceList setLinksfunction(InvoiceList DL)
  {

    String url = "";

    Set listkey = DL.keySet();
    Iterator it = listkey.iterator();
    while (it.hasNext())
    {
      try
      {
        String str = (String)it.next();

        ListElement ele = (ListElement)DL.get(str);

        //sets the URL for this member
        IntMember invoice = (IntMember)ele.get("InvoiceID");
        invoice.setRequestURL("goTo('DisplayEditInvoice.do?rowId=" + ele.getElementID() + "&listId=" + DL.getListID() + "&" + AccountConstantKeys.TYPEOFOPERATION + "=ShowInvoice')");

        //sets the URL for this member
        IntMember order = (IntMember)ele.get("Order");
        order.setRequestURL("goTo('DisplayEditOrder.do?rowId=" + ele.getElementID() + "&listId=" + DL.getListID() + "')");

        //sets the URL for this member
        StringMember cust = (StringMember)ele.get("CustomerID");
        url = "openPopup('ViewEntityDetail.do?rowId=" + ((IntMember)ele.get("CustID")).getMemberValue() + "')";
        cust.setRequestURL(url);

        //sets the URL for this member
        StringMember creator = (StringMember)ele.get("Creator");
        url = "openPopup('ViewEntityDetail.do?rowId=" + ((IntMember)ele.get("CreatorID")).getMemberValue() + "')";
        creator.setRequestURL(url);

      } catch (Exception e)
      {
        logger.error("[setLinksfunction] Exception thrown.", e);
      }

    }
    return DL;
  }
}