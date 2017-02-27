/*
 * $RCSfile: DuplicateInventoryHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:15 $ - $Author: mking_cv $
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
import com.centraview.account.item.ItemVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserPrefererences;
import com.centraview.contact.entity.EntityVO;
import com.centraview.settings.Settings;

public class DuplicateInventoryHandler  extends org.apache.struts.action.Action
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
						

			int invID = Integer.parseInt((String)request.getParameter("rowId"));
				
			InventoryForm invenForm =  new InventoryForm();
			InventoryVO inventoryVO = null;
			
			InventoryHome hm = (InventoryHome)CVUtility.getHomeObject("com.centraview.account.inventory.InventoryHome","Inventory");
			Inventory remote =(Inventory)hm.create();
			remote.setDataSource(dataSource);

			inventoryVO = remote.getInventory( individualID ,invID );
			
			ItemVO itemVO = inventoryVO.getItemVO();
			
			if (itemVO != null)
			{
				invenForm.setItemName(itemVO.getItemName());
				String itemid = new String();
				itemid = String.valueOf(itemVO.getItemId());
				invenForm.setItemID(itemid);
			}
			
			EntityVO entityVO = null;
			
			entityVO = inventoryVO.getVendorVO();
			if (entityVO != null)
			{
				invenForm.setVendorName(entityVO.getName());
				invenForm.setVendorID(String.valueOf(entityVO.getContactID()));
			}
			
			entityVO = inventoryVO.getSoldToVO();
			if (entityVO != null)
			{
				invenForm.setSoldToName(entityVO.getName());
				invenForm.setSoldToID(String.valueOf(entityVO.getContactID()));
			}
		
			invenForm.setIdentifier(inventoryVO.getStrIdentifier());
			invenForm.setLocationName(inventoryVO.getStrLocation());
			invenForm.setLocationID(String.valueOf(inventoryVO.getIntLocationID()));
			
			entityVO = inventoryVO.getManufacturerVO();
			if (entityVO != null)
			{
				invenForm.setManufacturer(entityVO.getName());
				invenForm.setManufactureID(String.valueOf(entityVO.getContactID()));
			}
							
			invenForm.setDescription(inventoryVO.getStrDescription());
			
			java.sql.Timestamp timeStamp = (java.sql.Timestamp)inventoryVO.getModified();
			java.sql.Date date = (java.sql.Date)inventoryVO.getCreated();
							
			if(inventoryVO.getCreated()!= null)				
			invenForm.setCreated(dForm.format(date));
			
			if(inventoryVO.getModified() != null)
			invenForm.setModified(dForm.format(timeStamp));
			
			if(inventoryVO.getStrStatus() != null)
			invenForm.setStatus(inventoryVO.getStrStatus());								
			
			invenForm.setStatusID(String.valueOf(inventoryVO.getIntStatusID()));								
			
			//To set all IDs to int value
			invenForm.convertFormbeanToValueObject();
							
			invenForm.setInventoryID(0);
			request.setAttribute("inventoryform",invenForm);
							
			// forward to jsp page
			FORWARD_final = FORWARD_editinventory;

		} catch (Exception e)
		{
			System.out.println("[Exception][DuplicateInventoryHandler.execute] Exception Thrown: "+e);
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
    	return (mapping.findForward(FORWARD_final));
    }
}
