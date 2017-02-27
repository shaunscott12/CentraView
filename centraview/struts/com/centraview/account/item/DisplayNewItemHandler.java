/*
 * $RCSfile: DisplayNewItemHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:18 $ - $Author: mking_cv $
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


package com.centraview.account.item;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;

public class DisplayNewItemHandler  extends org.apache.struts.action.Action
 {
    
    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";
    
    // Local Forwards
    private static final String FORWARD_newitem = ".view.accounting.item_detail";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;
   
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
    	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		try 
		{
			ItemForm itemForm = (ItemForm) form;
			if(request.getParameter("actionType") == null){
				itemForm = ItemForm.clearForm(itemForm);
			}
			ItemHome itemHome = (ItemHome)CVUtility.getHomeObject("com.centraview.account.item.ItemHome","Item");
	   		Item itemRemote = itemHome.create();
	   		itemRemote.setDataSource(dataSource);
			Vector itemTypeVec = itemRemote.getItemType();
			Vector glAcntVec = itemRemote.getGLAccountType();
			Vector taxClassVec = itemRemote.getTaxClassType();
			itemForm.setItemtypevec(itemTypeVec);
			itemForm.setGlaccountvec(glAcntVec);
			itemForm.setTaxclassvec(taxClassVec);
								 	
			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ITEM);
			request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.ADD );
			request.setAttribute("itemform", itemForm);
			// forward to jsp page	
			FORWARD_final = FORWARD_newitem;
			
		} catch (Exception e)	
		{
			System.out.println("[Exception][DisplayNewItemHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
    	return (mapping.findForward(FORWARD_final));
    }
}
