/*
 * $RCSfile: OrderLookupHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:11 $ - $Author: mking_cv $
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
package com.centraview.account.common;
/**
File Name:	OrderLookupHandler.java
Purpose :	Shows List of Orders to Select from
Author :	Sandeep Joshi
Date :		27 August 2003
Change History:  By		Version		Date		Purpose
*/

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.order.OrderList;
import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.settings.Settings;

/**
* this class will get http request from AddIndividual JSP then do
* the corresponding get data from database.
*/

public final class OrderLookupHandler extends Action
{
// --------------------------------------------------------- Public Methods
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param req The HTTP request we are processing
     * @param res The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
   public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest req,HttpServletResponse res) throws IOException, ServletException	
{
  String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

		String returnStatus ="";
		try
		{
			HttpSession session = req.getSession();
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();
			
			System.out.println("san-OrderLookupHandler-getting OrderList from LG ...");
			ListGenerator lg = ListGenerator.getListGenerator(dataSource);
			OrderList DL = ( OrderList )lg.getOrderList(individualID , 0,0 , "" ,"OrderID");


			/* Commented - by Sandeep
			   Search Related Code to be Added afterwords*/
			/*
			String searchStr = req.getParameter("simpleSearch");
			String saveSearchStr = req.getParameter("savedSearch");

			System.out.println(" searchStr in OrderLookupHandler "+searchStr);
			System.out.println(" saveSearchStr  in OrderLookupHandler "+saveSearchStr );

			if(searchStr != null && (searchStr.trim()).length() > 0)
			{
				System.out.println(" In simple Order Lookup search ");
				searchStr = searchStr.trim();
				DL.setSearchString(searchStr);
				DL.search();
			}
			else if (saveSearchStr != null && (saveSearchStr.trim()).length() > 0)
			{
				System.out.println(" In Advance Order Lookup search ");
				String primaryMembetType = DL.getPrimaryMemberType();
				String primaryTable 	 = DL.getPrimaryTable();

				DataDictionary dd = new DataDictionary();
				String filterQuery = dd.getAdvanceQuery(saveSearchStr,primaryMembetType,primaryTable);

				DL.setSearchString("ADVANCE:"+filterQuery);
				DL = ( OrderList )lg.getOrderList(DL);
			}
			*/


			DL.setTotalNoOfRecords( DL.size() );

			System.out.println("san-OrderLookupHandler-got the DL, size = "+DL.size());

			Set listkey = DL.keySet();
			Iterator it =  listkey.iterator();
			/*while( it.hasNext() )
			{
				String str = ( String )it.next();
				System.out.println(str);
				StringMember sm=null;
				ListElement ele  = ( ListElement )DL.get( str );
				sm = ( StringMember )ele.get("Name" );
				sm.setRequestURL("openPopup('ViewOrderDetail.do?rowId="+ele.getElementID()+"&listId="+DL.getListID()+"')");
				sm = ( StringMember )ele.get("PrimaryContact" );
				IntMember im = (IntMember) ele.get("IndividualID");
				sm.setRequestURL("openPopup('ViewIndividualDetail.do?rowId="+((Integer)im.getMemberValue()).intValue()+"&listId="+DL.getListID()+"')");
				System.out.println("san-OrderLookupHandler-end of
			}
			*/



			req.setAttribute("orderlookuplist" , DL );
			req.setAttribute("list" , "order" );


			returnStatus = "orderlookup";

		}catch(Exception e)
		{
			returnStatus = "failure";
		}
		return mapping.findForward(returnStatus);
   }

}
