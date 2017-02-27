/*
 * $RCSfile: OrderVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:30 $ - $Author: mking_cv $
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


package com.centraview.account.order;

import com.centraview.account.common.ItemLines;

public class OrderVO implements java.io.Serializable
{
  /**  Stores Customer id
   */
  private int customerIdValue;
  private String customerName;


  /** Stores Address to which this Order has been Billed */
  private String billToAdd;
  private int billToAddIdValue;

  /** Stores Address where this Order is to be Shipped */
  private String shipToAdd;
  private int shipToAddIdValue;

  /** Stores Current Status of the Order */
  private String status;

  private int statusIdValue;
  private String statusId;

  /** Stores Order Number of this Order */
  private int orderIdValue;

  /** Stores Purchase Order No for this Order */
  private String po;

  /** Stores Terms (if any ) for this Order */
  private String terms;

  private int termsIdValue;

  /** Stores Individual who is Account Manager for this Order */
  private String acctMgr;

  private int acctMgrIdValue;

  /** Stores Name of the Project related with this Order */
  private String project;

  private int projectIdValue;

  /** Stores Additional Notes for this Order */
  private String notes;


  private java.sql.Date orderDate;

  private ItemLines itemLines;

  private boolean invoiceIsGenerated;

  /*
  *  Stores jurisdictionID
  */
  private Integer jurisdictionID;

  // take Form and fill 'this' data with Form's values
  public OrderVO(OrderForm orderForm)
  {
    this.setCustomerIdValue(orderForm.getCustomerIdValue());
    this.setCustomerName(orderForm.getCustomerName());
    this.setAcctMgr(orderForm.getAcctMgr());
    this.setAcctMgrIdValue(orderForm.getAcctMgrIdValue());
    this.setBillToAdd(orderForm.getBillToAdd());
    this.setBillToAddIdValue(orderForm.getBillToAddIdValue());
    this.setCustomerIdValue(orderForm.getCustomerIdValue());
    this.setCustomerName(orderForm.getCustomerName());
    this.setItemLines(orderForm.getItemLines());
    this.setNotes(orderForm.getNotes());
    this.setOrderDate(orderForm.getOrderDate());
    this.setOrderIdValue(orderForm.getOrderIdValue());
    this.setPo(orderForm.getPo());
    this.setProject(orderForm.getProject());
    this.setProjectIdValue(orderForm.getProjectIdValue());
    this.setShipToAdd(orderForm.getShipToAdd());
    this.setShipToAddIdValue(orderForm.getShipToAddIdValue());
    this.setStatus(orderForm.getStatus());
    this.setStatusIdValue(orderForm.getStatusIdValue());
    this.setTerms(orderForm.getTerms());
    this.setTermsIdValue(orderForm.getTermsIdValue());
    this.setInvoiceIsGenerated(orderForm.getInvoiceIsGenerated());
  }

  public OrderVO getVO()
  {
    return this;
  }

  public String getAcctMgr()
  {
    return this.acctMgr;
  }

  public void setAcctMgr(String acctMgr)
  {
    this.acctMgr = acctMgr;
  }


  public int getAcctMgrIdValue()
  {
    return this.acctMgrIdValue;
  }

  public void setAcctMgrIdValue(int acctMgrIdValue)
  {
    this.acctMgrIdValue = acctMgrIdValue;
  }


  public String getBillToAdd()
  {
    return this.billToAdd;
  }

  public void setBillToAdd(String billToAdd)
  {
    this.billToAdd = billToAdd;
  }


  public int getBillToAddIdValue()
  {
    return this.billToAddIdValue;
  }

  public void setBillToAddIdValue(int billToAddIdValue)
  {
    this.billToAddIdValue = billToAddIdValue;
  }


  public int getCustomerIdValue()
  {
    return this.customerIdValue;
  }

  public void setCustomerIdValue(int customerIdValue)
  {
    this.customerIdValue = customerIdValue;
  }


  public String getCustomerName()
  {
    return this.customerName;
  }

  public void setCustomerName(String customerName)
  {
    this.customerName = customerName;
  }


  public ItemLines getItemLines()
  {
    return this.itemLines;
  }

  public void setItemLines(ItemLines itemLines)
  {
    this.itemLines = itemLines;
  }


  public String getNotes()
  {
    return this.notes;
  }

  public void setNotes(String notes)
  {
    this.notes = notes;
  }


  public java.sql.Date getOrderDate()
  {
    return this.orderDate;
  }

  public void setOrderDate(java.sql.Date orderDate)
  {
    this.orderDate = orderDate;
  }


  public int getOrderIdValue()
  {
    return this.orderIdValue;
  }

  public void setOrderIdValue(int orderIdValue)
  {
    this.orderIdValue = orderIdValue;
  }


  public String getPo()
  {
    return this.po;
  }

  public void setPo(String po)
  {
    this.po = po;
  }


  public String getProject()
  {
    return this.project;
  }

  public void setProject(String project)
  {
    this.project = project;
  }


  public int getProjectIdValue()
  {
    return this.projectIdValue;
  }

  public void setProjectIdValue(int projectIdValue)
  {
    this.projectIdValue = projectIdValue;
  }


  public String getShipToAdd()
  {
    return this.shipToAdd;
  }

  public void setShipToAdd(String shipToAdd)
  {
    this.shipToAdd = shipToAdd;
  }


  public int getShipToAddIdValue()
  {
    return this.shipToAddIdValue;
  }

  public void setShipToAddIdValue(int shipToAddIdValue)
  {
    this.shipToAddIdValue = shipToAddIdValue;
  }


  public String getStatus()
  {
    return this.status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }


  public String getStatusId()
  {
    return this.statusId;
  }

  public void setStatusId(String statusId)
  {
    this.statusId = statusId;
  }


  public int getStatusIdValue()
  {
    return this.statusIdValue;
  }

  public void setStatusIdValue(int statusIdValue)
  {
    this.statusIdValue = statusIdValue;
  }


  public String getTerms()
  {
    return this.terms;
  }

  public void setTerms(String terms)
  {
    this.terms = terms;
  }


  public int getTermsIdValue()
  {
    return this.termsIdValue;
  }

  public void setTermsIdValue(int termsIdValue)
  {
    this.termsIdValue = termsIdValue;
  }


  public boolean getInvoiceIsGenerated()
  {
    return this.invoiceIsGenerated;
  }

  public void setInvoiceIsGenerated(boolean invoiceIsGenerated)
  {
    this.invoiceIsGenerated = invoiceIsGenerated;
  }

  /**
   * @return The jurisdiction ID.
   */
  public Integer getJurisdictionID()
  {
    return this.jurisdictionID;
  }

  /**
   * Set the Jurisdiction ID
   *
   * @param jurisdictionID
   */
  public void setJurisdictionID(Integer jurisdictionID)
  {
    this.jurisdictionID = jurisdictionID;
  }
}


