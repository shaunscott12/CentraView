/*
 * $RCSfile: OrderDBVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:29 $ - $Author: mking_cv $
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

public class OrderDBVO implements java.io.Serializable
{
  /**  Stores Customer id
   */
  private int customerIdValue;

  /** Stores Address to which this Order has been Billed */
  private int billToAddIdValue;

  /** Stores Address where this Order is to be Shipped */
  private int shipToAddIdValue;

  /** Stores Current Status of the Order */
  private int statusIdValue;

  /** Stores Order Number of this Order */
  private int orderIdValue;

  /** Stores Purchase Order No for this Order */
  private String po;

  /** Stores Terms (if any ) for this Order */
  private int termsIdValue;

  /** Stores Individual who is Account Manager for this Order */
  private int acctMgrIdValue;

  /** Stores Name of the Project related with this Order */
  private int projectIdValue;

  /** Stores Additional Notes for this Order */
  private String notes;

  private java.sql.Date orderDate;

  private ItemLines itemLines;

  public OrderDBVO()
  {
    // just plain constructor
  }

  public OrderDBVO(OrderForm orderForm)
  {
    this.setCustomerIdValue(orderForm.getCustomerIdValue());
    this.setAcctMgrIdValue(orderForm.getAcctMgrIdValue());
    this.setBillToAddIdValue(orderForm.getBillToAddIdValue());
    this.setCustomerIdValue(orderForm.getCustomerIdValue());
    this.setItemLines(orderForm.getItemLines());
    this.setNotes(orderForm.getNotes());
    this.setOrderDate(orderForm.getOrderDate());
    this.setOrderIdValue(orderForm.getOrderIdValue());
    this.setPo(orderForm.getPo());
    this.setProjectIdValue(orderForm.getProjectIdValue());
    this.setShipToAddIdValue(orderForm.getShipToAddIdValue());
    this.setStatusIdValue(orderForm.getStatusIdValue());
    this.setTermsIdValue(orderForm.getTermsIdValue());
  }

  /** return the Account Manager ID
   *
   *
   * @return int account Manager id
   */
  public int getAcctMgrIdValue()
  {
    return this.acctMgrIdValue;
  }


  /** sets Id for account Manager
   *
   *
   * @param   acctMgrIdValue  int Id
   */
  public void setAcctMgrIdValue(int acctMgrIdValue)
  {
    this.acctMgrIdValue = acctMgrIdValue;
  }



  /** returns bill address id
   *
   *
   * @return int id of bill address
   */
  public int getBillToAddIdValue()
  {
    return this.billToAddIdValue;
  }


  /**sets the id of bill address
   *
   *
   * @param   billToAddIdValue  int value of Id
   */
  public void setBillToAddIdValue(int billToAddIdValue)
  {
    this.billToAddIdValue = billToAddIdValue;
  }



  /** returns customer id of customer of this order
   *
   *
   * @return int id of customer
   */
  public int getCustomerIdValue()
  {
    return this.customerIdValue;
  }


  /** sets the id of customer of this order
   *
   *
   * @param   customerIdValue int value of customer id
   */
  public void setCustomerIdValue(int customerIdValue)
  {
    this.customerIdValue = customerIdValue;
  }



  /** returns ItemLines object having one line for each item in order
   *
   *
   * @return ItemLines object
   */
  public ItemLines getItemLines()
  {
    return this.itemLines;
  }


  /** sets the itemlines object for this order
   *
   *
   * @param   itemLines  object
   */
  public void setItemLines(ItemLines itemLines)
  {
    this.itemLines = itemLines;
  }



  /** reuturns notes/description for this order
   *
   *
   * @return String notes
   */
  public String getNotes()
  {
    return this.notes;
  }


  /** sets the notes for this order
   *
   *
   * @param   notes  String description
   */
  public void setNotes(String notes)
  {
    this.notes = notes;
  }



  /** returns the Date of this order
   *
   *
   * @return java.sql.Date of this order
   */
  public java.sql.Date getOrderDate()
  {
    return this.orderDate;
  }


  /**sets the date of this order
   *
   *
   * @param   orderDate  date of this order
   */
  public void setOrderDate(java.sql.Date orderDate)
  {
    this.orderDate = orderDate;
  }



  /** returns id of this order
   *
   *
   * @return int id of order
   */
  public int getOrderIdValue()
  {
    return this.orderIdValue;
  }


  /** sets the id of this order
   *
   *
   * @param   orderIdValue int value
   */
  public void setOrderIdValue(int orderIdValue)
  {
    this.orderIdValue = orderIdValue;
  }



  /** returns po number of this order
   *
   *
   * @return String po
   */
  public String getPo()
  {
    return this.po;
  }


  /** sets the po for this order
   *
   *
   * @param   po  String value
   */
  public void setPo(String po)
  {
    this.po = po;
  }



  /**
   *
   *
   * @return
   */
  public int getProjectIdValue()
  {
    return this.projectIdValue;
  }


  /**
   *
   *
   * @param   projectIdValue
   */
  public void setProjectIdValue(int projectIdValue)
  {
    this.projectIdValue = projectIdValue;
  }



  /**
   *
   *
   * @return
   */
  public int getShipToAddIdValue()
  {
    return this.shipToAddIdValue;
  }


  /**
   *
   *
   * @param   shipToAddIdValue
   */
  public void setShipToAddIdValue(int shipToAddIdValue)
  {
    this.shipToAddIdValue = shipToAddIdValue;
  }



  /**
   *
   *
   * @return
   */
  public int getStatusIdValue()
  {
    return this.statusIdValue;
  }


  /**
   *
   *
   * @param   statusIdValue
   */
  public void setStatusIdValue(int statusIdValue)
  {
    this.statusIdValue = statusIdValue;
  }


  /**
   *
   *
   * @return
   */
  public int getTermsIdValue()
  {
    return this.termsIdValue;
  }


  /**
   *
   *
   * @param   termsIdValue
   */
  public void setTermsIdValue(int termsIdValue)
  {
    this.termsIdValue = termsIdValue;
  }
}


