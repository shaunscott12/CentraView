/*
 * $RCSfile: PurchaseOrderVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:32 $ - $Author: mking_cv $
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


package com.centraview.account.purchaseorder;

import com.centraview.account.common.ItemLines;
import com.centraview.common.CVAudit;

public class PurchaseOrderVO extends CVAudit
{
  private String externalID;

  private int vendorId;
  private String vendorName;

  private int purchaseOrderId;

  private int billToId;
  private String  billToAddress;

  private int shipToId;
  private String shipToAddress;

  private int statusId;
  private String statusName;

  private int termId;
  private String term;

  private int accountManagerId;
  private String accountManagerName;

  private String notes = "";

  private java.sql.Date purchaseorderDate;
  private ItemLines itemLines ;

  private int modifiedBy;
  /**
   *
   *
   * @return     AccountManager ID
   */
  public int getAccountManagerId()
  {
    return this.accountManagerId;
  }

  /**
   *  set AccountManager ID
   *
   * @param   accountManagerId
   */
  public void setAccountManagerId(int accountManagerId)
  {
    this.accountManagerId = accountManagerId;
  }

  /**
   *
   *
   * @return     Bill Address ID
   */
  public int getBillToId()
  {
    return this.billToId;
  }

  /**
   *  set Bill Address ID
   *
   * @param   billToId
   */
  public void setBillToId(int billToId)
  {
    this.billToId = billToId;
  }

  /**
   *
   *
   * @return    Vendor ID
   */
  public int getVendorId()
  {
    return this.vendorId;
  }

  /**
   *  set Vendor ID
   *
   * @param   vendorId
   */
  public void setVendorId(int vendorId)
  {
    this.vendorId = vendorId;
  }

  /**
   *
   *
   * @return     PurchaseOrder Date
   */
  public java.sql.Date getPurchaseOrderDate()
  {
    return this.purchaseorderDate;
  }

  /**
   *  set PurchaseOrder Date
   *
   * @param   purchaseorderDate
   */
  public void setPurchaseOrderDate(java.sql.Date purchaseorderDate)
  {
    this.purchaseorderDate = purchaseorderDate;
  }

  /**
   *
   *
   * @return     PurchaseOrderID
   */
  public int getPurchaseOrderId()
  {
    return this.purchaseOrderId;
  }

  /**
   *  set PurchaseOrderID
   *
   * @param   purchaseOrderId
   */
  public void setPurchaseOrderId(int purchaseOrderId)
  {
    this.purchaseOrderId = purchaseOrderId;
  }

  /**
   *
   *
   * @return    ItemLines
   */
  public ItemLines getItemLines()
  {
    return this.itemLines;
  }

  /**
   *  set ItemLines
   *
   * @param   itemLines
   */
  public void setItemLines(ItemLines itemLines)
  {
    this.itemLines = itemLines;
  }

  /**
   *
   *
   * @return     Notes
   */
  public String getNotes()
  {
    return this.notes;
  }


  /**
   *  set Notes
   *
   * @param   notes
   */
  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  /**
   *
   *
   * @return     Ship Address ID
   */
  public int getShipToId()
  {
    return this.shipToId;
  }

  /**
   *  set Ship Address ID
   *
   * @param   shipToId
   */
  public void setShipToId(int shipToId)
  {
    this.shipToId = shipToId;
  }

  /**
   *
   *
   * @return     Status ID
   */
  public int getStatusId()
  {
    return this.statusId;
  }

  /**
   *  set Status ID
   *
   * @param   statusId
   */
  public void setStatusId(int statusId)
  {
    this.statusId = statusId;
  }

  /**
   *
   *
   * @return     Terms ID
   */
  public int getTermId()
  {
    return this.termId;
  }

  /**
   *  set Terms ID
   *
   * @param   termId
   */
  public void setTermId(int termId)
  {
    this.termId = termId;
  }

  public String getAccountManagerName()
  {
    return this.accountManagerName;
  }

  /**
   *
   *
   * @param   accountManagerName
   */
  public void setAccountManagerName(String accountManagerName)
  {
    this.accountManagerName = accountManagerName;
  }

  /**
   *
   *
   * @return
   */
  public String getBillToAddress()
  {
    return this.billToAddress;
  }

  /**
   *
   *
   * @param   billToAddress
   */
  public void setBillToAddress(String billToAddress)
  {
    this.billToAddress = billToAddress;
  }

  /**
   *
   *
   * @return
   */
  public String getVendorName()
  {
    return this.vendorName;
  }

  /**
   *
   *
   * @param   customerName
   */
  public void setVendorName(String vendorName)
  {
    this.vendorName = vendorName;
  }

  /**
   *
   *
   * @return
   */
  public String getShipToAddress()
  {
    return this.shipToAddress;
  }

  /**
   *
   *
   * @param   shipToAddress
   */
  public void setShipToAddress(String shipToAddress)
  {
    this.shipToAddress = shipToAddress;
  }

  /**
   *
   *
   * @return
   */
  public String getStatusName()
  {
    return this.statusName;
  }

  /**
   *
   *
   * @param   statusName
   */
  public void setStatusName(String statusName)
  {
    this.statusName = statusName;
  }

  /**
   *
   *
   * @return
   */
  public String getTerm()
  {
    return this.term;
  }

  /**
   *
   *
   * @param   term
   */
  public void setTerm(String term)
  {
    this.term = term;
  }

  public PurchaseOrderVO getVO()
  {
    return this;
  }

  public String getExternalID()
  {
    return this.externalID;
  }

  public void setExternalID(String externalID)
  {
    this.externalID = externalID;
  }

  /**
   * Sets the modifiedBy for this PurchaseOrder.
   * @param modifiedBy The new modifiedBy for this PurchaseOrder.
   */
  public void setModifiedBy(int modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  } //end of setModifiedBy method

  /**
   * @return The modifiedBy for this PurchaseOrder.
   */
  public int getModifiedBy()
  {
    return this.modifiedBy;
  } //end of getModifiedBy method
}














