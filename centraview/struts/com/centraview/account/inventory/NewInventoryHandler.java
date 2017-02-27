/*
 * $RCSfile: NewInventoryHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:16 $ - $Author: mking_cv $
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


package com.centraview.account.inventory;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserPrefererences;
import com.centraview.settings.Settings;

public class NewInventoryHandler  extends org.apache.struts.action.Action
 {

    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_editinventory = ".view.accounting.inventory_detail";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;



    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
	
		try
		{
			HttpSession session = request.getSession(true);
			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVENTORY);

			request.setAttribute("body", "EDIT");
			
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );//get the user object
			int individualID = userobjectd.getIndividualID();
			
			UserPrefererences userPref = userobjectd.getUserPref();
			String dateFormat	= userPref.getDateFormat();
				
			dateFormat= "MMMMMMMMM dd, yyyy";
			String timeZone		= userPref.getTimeZone();
			if(timeZone == null)
			timeZone = "EST";
				
			GregorianCalendar gCal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
			SimpleDateFormat dForm = new SimpleDateFormat(dateFormat);
			dForm.setCalendar(gCal);
						
			// forward to jsp page
			FORWARD_final = FORWARD_editinventory;

		} catch (Exception e)
		{
			System.out.println("[Exception][NewInventoryHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
    	return (mapping.findForward(FORWARD_final));
    }
}
