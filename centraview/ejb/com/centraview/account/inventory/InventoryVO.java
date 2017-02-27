/*
 * $RCSfile: InventoryVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:27 $ - $Author: mking_cv $
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

import java.util.Vector;

import com.centraview.account.item.ItemVO;
import com.centraview.common.CVAudit;
import com.centraview.contact.entity.EntityVO;

public class InventoryVO extends CVAudit
{

  private int inventoryID;
  
  private ItemVO itemVO;
  
  private String strIdentifier;
  
  private String strLocation;
  private int intLocationID;
  
  private EntityVO manufacturerVO;
  
  private EntityVO vendorVO;
    
  private String strDescription;
  
  private String strStatus;
  private int intStatusID;
  
  private EntityVO soldToVO;  
  
  private java.sql.Date created ;
  private java.sql.Timestamp modified;
  
  private int qty;
  
  private Vector customFieldsVec;
  
  
  public int getIntLocationID()
  {
    return this.intLocationID;
  }

  public void setIntLocationID(int intLocationID)
  {
    this.intLocationID = intLocationID;
  }

  
  public int getIntStatusID()
  {
    return this.intStatusID;
  }

  public void setIntStatusID(int intStatusID)
  {
    this.intStatusID = intStatusID;
  }

  
  public ItemVO getItemVO()
  {
    return this.itemVO;
  }

  public void setItemVO(ItemVO itemVO)
  {
    this.itemVO = itemVO;
  }

  
  public EntityVO getSoldToVO()
  {
    return this.soldToVO;
  }

  public void setSoldToVO(EntityVO soldToVO)
  {
    this.soldToVO = soldToVO;
  }

  
  public String getStrDescription()
  {
    return this.strDescription;
  }

  public void setStrDescription(String strDescription)
  {
    this.strDescription = strDescription;
  }

  
  public String getStrIdentifier()
  {
    return this.strIdentifier;
  }

  public void setStrIdentifier(String strIdentifier)
  {
    this.strIdentifier = strIdentifier;
  }

  
  public String getStrLocation()
  {
    return this.strLocation;
  }

  public void setStrLocation(String strLocation)
  {
    this.strLocation = strLocation;
  }

  public String getStrStatus()
  {
    return this.strStatus;
  }

  public void setStrStatus(String strStatus)
  {
    this.strStatus = strStatus;
  }

  
  public EntityVO getVendorVO()
  {
    return this.vendorVO;
  }

  public void setVendorVO(EntityVO vendorVO)
  {
    this.vendorVO = vendorVO;
  }

  public java.sql.Date getCreated()
  {
    return this.created;
  }

  public void setCreated(java.sql.Date created)
  {
    this.created = created;
  }

  
  public java.sql.Timestamp getModified()
  {
    return this.modified;
  }

  public void setModified(java.sql.Timestamp modified)
  {
    if(modified != null)
    this.modified = modified;
  }

  
  public int getQty()
  {
    return this.qty;
  }

  public void setQty(int qty)
  {
    this.qty = qty;
  }

  
  public int getInventoryID()
  {
    return this.inventoryID;
  }

  public void setInventoryID(int inventoryID)
  {
    this.inventoryID = inventoryID;
  }

  
  public EntityVO getManufacturerVO()
  {
    return this.manufacturerVO;
  }

  public void setManufacturerVO(EntityVO manufacturerVO)
  {
    this.manufacturerVO = manufacturerVO;
  }

  
  public Vector getCustomFieldsVec()
  {
    return this.customFieldsVec;
  }

  public void setCustomFieldsVec(Vector customFieldsVec)
  {
    this.customFieldsVec = customFieldsVec;
  }
}    
