/*
 * $RCSfile: AddVendorHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:26 $ - $Author: mking_cv $
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
package com.centraview.account.vendor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

public class AddVendorHandler extends org.apache.struts.action.Action 
{
    
    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";
    
    // Local Forwards
    private static final String FORWARD_showvendorlist = "showvendorlist";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;
    

	    
    public AddVendorHandler() {
    }
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
     	try 
		{
			HttpSession session = request.getSession(true);			
			int IndividualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

			session.setAttribute("highlightmodule", "account");
			int entityId = 0;
			String entityIdStr = (String)request.getParameter("SelectedEntityId");			
			if(entityIdStr != null && !entityIdStr.equals(""))
				entityId = Integer.parseInt(entityIdStr);
			com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );

			AccountHelperHome hm = (AccountHelperHome)CVUtility.getHomeObject("com.centraview.account.helper.AccountHelperHome","AccountHelper");
			AccountHelper remote =(AccountHelper)hm.create();
			remote.setDataSource(dataSource);
			remote.addVendor(entityId,IndividualId);

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.VENDOR);
			request.setAttribute("body", "list");
		
			// forward to jsp page	
			FORWARD_final = FORWARD_showvendorlist;
		 	
		} catch (Exception e)	
		{
			System.out.println("[Exception][AddVendorHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
    	return (mapping.findForward(FORWARD_final));
    }
}
