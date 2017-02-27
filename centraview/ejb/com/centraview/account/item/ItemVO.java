/*
 * $RCSfile: ItemVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:29 $ - $Author: mking_cv $
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

import com.centraview.common.CVAudit;
import com.centraview.contact.entity.EntityVO;

public class ItemVO extends CVAudit
{
  private int itemId;
  private String itemName;
  private int itemTypeId;
  private String itemDesc;
  private int glAccountId;
  // private GLAccountVO glAccountVO;
  private int taxClassId;
  // private TaxClassVO taxClassVO;
  private int subItemOfId;
  private double price;
  private double cost;
  private String linkToInventory;
  private int qtyOnHand;
  private int qtyOnOrder;
  private int qtyOnBackOrder;
  private String sku;  
  private int manufacturerid;
  private EntityVO manufacturerVO;
  private int vendorid;
  private EntityVO vendorVO;
  private String externalID;
  private String parentExternalID;

  public void setParentExternalID(String parentExternalID)
        {
    this.parentExternalID = parentExternalID;
        }

  public String getParentExternalID()
        {
    return this.parentExternalID;
        }
  
  public int getGlAccountId()
  {
    return this.glAccountId;
  }

  public void setGlAccountId(int glAccountId)
  {
    this.glAccountId = glAccountId;
  }

  
  public String getItemDesc()
  {
    return this.itemDesc;
  }

  public void setItemDesc(String itemDesc)
  {
    this.itemDesc = itemDesc;
  }

  
  public int getItemId()
  {
    return this.itemId;
  }

  public void setItemId(int itemId)
  {
    this.itemId = itemId;
  }

  
  public int getItemTypeId()
  {
    return this.itemTypeId;
  }

  public void setItemTypeId(int itemTypeId)
  {
    this.itemTypeId = itemTypeId;
  }

  
  public String getLinkToInventory()
  {
    return this.linkToInventory;
  }

  public void setLinkToInventory(String linkToInventory)
  {
    this.linkToInventory = linkToInventory;
  }
  
  public int getQtyOnBackOrder()
  {
    return this.qtyOnBackOrder;
  }

  public void setQtyOnBackOrder(int qtyOnBackOrder)
  {
    this.qtyOnBackOrder = qtyOnBackOrder;
  }

  
  public int getQtyOnHand()
  {
    return this.qtyOnHand;
  }

  public void setQtyOnHand(int qtyOnHand)
  {
    this.qtyOnHand = qtyOnHand;
  }

  
  public int getQtyOnOrder()
  {
    return this.qtyOnOrder;
  }

  public void setQtyOnOrder(int qtyOnOrder)
  {
    this.qtyOnOrder = qtyOnOrder;
  }

  
  public String getSku()
  {
    return this.sku;
  }

  public void setSku(String sku)
  {
    this.sku = sku;
  }

  
  public int getSubItemOfId()
  {
    return this.subItemOfId;
  }

  public void setSubItemOfId(int subItemOfId)
  {
    this.subItemOfId = subItemOfId;
  }

  
  public int getTaxClassId()
  {
    return this.taxClassId;
  }

  public void setTaxClassId(int taxClassId)
  {
    this.taxClassId = taxClassId;
  }

  
  public int getManufacturerid()
  {
    return this.manufacturerid;
  }

  public void setManufacturerid(int manufacturerid)
  {
    this.manufacturerid = manufacturerid;
  }

  
  public EntityVO getManufacturerVO()
  {
    return this.manufacturerVO;
  }

  public void setManufacturerVO(EntityVO manufacturerVO)
  {
    this.manufacturerVO = manufacturerVO;
  }

  
  public int getVendorid()
  {
    return this.vendorid;
  }

  public void setVendorid(int vendorid)
  {
    this.vendorid = vendorid;
  }

  
  public EntityVO getVendorVO()
  {
    return this.vendorVO;
  }

  public void setVendorVO(EntityVO vendorVO)
  {
    this.vendorVO = vendorVO;
  }

  
  public String getItemName()
  {
    return this.itemName;
  }

  public void setItemName(String itemName)
  {
    this.itemName = itemName;
  }

  
  public double getPrice()
  {
    return this.price;
  }

  public void setPrice(double price)
  {
    this.price = price;
  }

  
  public double getCost()
  {
    return this.cost;
  }

  public void setCost(double cost)
  {
    this.cost = cost;
  }

  
  public String getExternalID()
  {
    return this.externalID;
  }

  public void setExternalID(String externalID)
  {
    this.externalID = externalID;
  }
}
