/*
 * $RCSfile: ItemForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.Validation;
import com.centraview.settings.Settings;

public class ItemForm extends ActionForm
{
	private String itemid;
	private String itemname;
	private String itemtypeid;
	private Vector itemtypevec;
	private String itemdesc;		
	private String glaccountid;
	private Vector glaccountvec;
	private String taxclassid;
	private Vector taxclassvec;
	private String subitemid;
	private String subitemname;
	private String price;
	private String cost;
	private String linktoinventory;
	private String qtyonhand;
	private String qtyonorder;
	private String qtyonbackorder;
	private String sku;
	private String created;
	private String modified;			
	private String manufacturerid;
	private String manufacturername;
	private String vendorid;
	private String vendorname;


	// message property file
	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
	
	public String getCost()
	{
		return this.cost;
	}

	public void setCost(String cost)
	{
		this.cost = cost;
	}

	
	public String getCreated()
	{
		return this.created;
	}

	public void setCreated(String created)
	{
		this.created = created;
	}

	
	public String getGlaccountid()
	{
		return this.glaccountid;
	}

	public void setGlaccountid(String glaccountid)
	{
		this.glaccountid = glaccountid;
	}

	
	public Vector getGlaccountvec()
	{
		return this.glaccountvec;
	}

	public void setGlaccountvec(Vector glaccountvec)
	{
		this.glaccountvec = glaccountvec;
	}

	
	public String getItemdesc()
	{
		return this.itemdesc;
	}

	public void setItemdesc(String itemdesc)
	{
		this.itemdesc = itemdesc;
	}

	
	public String getItemid()
	{
		return this.itemid;
	}

	public void setItemid(String itemid)
	{
		this.itemid = itemid;
	}

	
	public String getItemname()
	{
		return this.itemname;
	}

	public void setItemname(String itemname)
	{
		this.itemname = itemname;
	}

	
	public String getItemtypeid()
	{
		return this.itemtypeid;
	}

	public void setItemtypeid(String itemtypeid)
	{
		this.itemtypeid = itemtypeid;
	}

	
	public Vector getItemtypevec()
	{
		return this.itemtypevec;
	}

	public void setItemtypevec(Vector itemtypevec)
	{
		this.itemtypevec = itemtypevec;
	}

	
	public String getLinktoinventory()
	{
		return this.linktoinventory;
	}

	public void setLinktoinventory(String linktoinventory)
	{
		this.linktoinventory = linktoinventory;
	}

	
	public String getModified()
	{
		return this.modified;
	}

	public void setModified(String modified)
	{
		this.modified = modified;
	}

	
	public String getPrice()
	{
		return this.price;
	}

	public void setPrice(String price)
	{
		this.price = price;
	}

	
	public String getQtyonbackorder()
	{
		return this.qtyonbackorder;
	}

	public void setQtyonbackorder(String qtyonbackorder)
	{
		this.qtyonbackorder = qtyonbackorder;
	}

	
	public String getQtyonhand()
	{
		return this.qtyonhand;
	}

	public void setQtyonhand(String qtyonhand)
	{
		this.qtyonhand = qtyonhand;
	}

	
	public String getQtyonorder()
	{
		return this.qtyonorder;
	}

	public void setQtyonorder(String qtyonorder)
	{
		this.qtyonorder = qtyonorder;
	}

	
	public String getSku()
	{
		return this.sku;
	}

	public void setSku(String sku)
	{
		this.sku = sku;
	}

	
	public String getSubitemid()
	{
		return this.subitemid;
	}

	public void setSubitemid(String subitemid)
	{
		this.subitemid = subitemid;
	}

	
	public String getTaxclassid()
	{
		return this.taxclassid;
	}

	public void setTaxclassid(String taxclassid)
	{
		this.taxclassid = taxclassid;
	}

	
	public Vector getTaxclassvec()
	{
		return this.taxclassvec;
	}

	public void setTaxclassvec(Vector taxclassvec)
	{
		this.taxclassvec = taxclassvec;
	}

	
	public String getManufacturerid()
	{
		return this.manufacturerid;
	}

	public void setManufacturerid(String manufacturerid)
	{
		this.manufacturerid = manufacturerid;
	}

	
	public String getManufacturername()
	{
		return this.manufacturername;
	}

	public void setManufacturername(String manufacturername)
	{
		this.manufacturername = manufacturername;
	}

	
	public String getVendorid()
	{
		return this.vendorid;
	}

	public void setVendorid(String vendorid)
	{
		this.vendorid = vendorid;
	}

	
	public String getVendorname()
	{
		return this.vendorname;
	}

	public void setVendorname(String vendorname)
	{
		this.vendorname = vendorname;
	}

	
	public String getSubitemname()
	{
		return this.subitemname;
	}

	public void setSubitemname(String subitemname)
	{
		this.subitemname = subitemname;
	}
	
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request) 
	{
	    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(request.getSession().getServletContext())).getDataSource();
		ActionErrors errors = new ActionErrors();

		try 
		{
			if (request.getParameter("typeofsave") != null && (!request.getParameter("typeofsave").equals("cancel")))	
			{
				ItemHome itemHome = (ItemHome)CVUtility.getHomeObject("com.centraview.account.item.ItemHome","Item");
				Item itemRemote = itemHome.create();
				itemRemote.setDataSource(dataSource);

				Vector itemTypeVec = itemRemote.getItemType();
				Vector glAcntVec = itemRemote.getGLAccountType();
				Vector taxClassVec = itemRemote.getTaxClassType();
        
				this.setItemtypevec(itemTypeVec);
				this.setGlaccountvec(glAcntVec);
				this.setTaxclassvec(taxClassVec);
			
				Validation validation = new Validation();	

				// sku field
        if (this.getSku() == null || this.getSku().trim().length() <= 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "SKU"));
        }
				
				// item name field
        if (this.getItemname() == null || this.getItemname().trim().length() <= 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Item Name"));
        }
       
        // TODO: message properties are not valid - fix them
        /*
        if (!this.getQtyonhand().equals("")) {
          validation.checkForInteger("label.account.item.qtyonhand", this.getQtyonhand(), "error.application.integer", "",errors);
        }
        
        if (!this.getQtyonorder().equals("")) {
          validation.checkForInteger("label.account.item.qtyonorder", this.getQtyonorder(), "error.application.integer", "",errors);
        }
        if (! this.getQtyonbackorder().equals("")) {
          validation.checkForInteger("label.account.item.qtyonbackorder", this.getQtyonbackorder(), "error.application.integer", "",errors);
        }
        */

				// check for duplicate sku code
				ItemVO itemVO = itemRemote.checkSKUCode(this.getSku());
				
				if (itemVO.getItemId() != 0 && (!((""+itemVO.getItemId()).equals(this.getItemid())))) {
					errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "The SKU you enter already exists."));
				}
			}	
			//    update request
      if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals(AccountConstantKeys.EDIT))
        request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.EDIT);
      else
        request.setAttribute(AccountConstantKeys.TYPEOFOPERATION, AccountConstantKeys.ADD);
    
      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.ITEM);
      request.setAttribute("itemform", this);
		}
		catch (Exception e)	
		{
      System.out.println("[Exception] ItemForm.validate: " + e.toString());
			//e.printStackTrace();
		}
    return errors;
	}
	
	public static ItemForm clearForm(ItemForm itemForm)	
	{
		itemForm.setCost("");
		itemForm.setCreated("");
		itemForm.setGlaccountid("");
		itemForm.setItemdesc("");
		itemForm.setItemid("");
		itemForm.setItemname("");
		itemForm.setItemtypeid("");
		itemForm.setLinktoinventory("");
		itemForm.setManufacturerid("");
		itemForm.setManufacturername("");
		itemForm.setModified("");
		itemForm.setPrice("");
		itemForm.setQtyonbackorder("");
		itemForm.setQtyonhand("");	
		itemForm.setQtyonorder("");			
		itemForm.setSku("");
		itemForm.setSubitemid("");
		itemForm.setSubitemname("");
		itemForm.setTaxclassid("");
		itemForm.setVendorid("");						
		itemForm.setVendorname("");								
		return itemForm;
	}
	
}
