/*
 * $RCSfile: InvoiceDBVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:27 $ - $Author: mking_cv $
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


package com.centraview.account.invoice;

import com.centraview.account.common.ItemLines;

public class InvoiceDBVO
{
  private int customerId; 
  private int invoiceId; 
  private int orderId; 
  private int billToId;
  private int shipToId;
  private int statusId;
  private String externalId;
  private String po;
  private int termId;
  private int accountManagerId;
    private int projectId;
  private String notes;     
  private java.sql.Date invoiceDate;
  private ItemLines itemLines ;

  public InvoiceDBVO()
  {
    //plain constructor
  }
  
  public InvoiceDBVO(InvoiceVO inv)
  {
    this.setCustomerId(inv.getCustomerId());
    this.setInvoiceId(inv.getInvoiceId());
    this.setOrderId(inv.getOrderId());
    this.setBillToId(inv.getBillToId());
    this.setShipToId(inv.getShipToId());
    this.setStatusId(inv.getStatusId());
    this.setExternalId(inv.getExternalId());
    this.setPo(inv.getPo());
    this.setTermId(inv.getTermId());
    this.setAccountManagerId(inv.getAccountManagerId());
    this.setProjectId(inv.getProjectId());
    this.setNotes(inv.getNotes());
    this.setInvoiceDate(inv.getInvoiceDate());
    this.setItemLines(inv.getItemLines());
  }
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
   * @return    Customer ID 
   */
  public int getCustomerId()
  {
    return this.customerId;
  }


  /**
   *  set CustomerID
   *
   * @param   customerId  
   */
  public void setCustomerId(int customerId)
  {
    this.customerId = customerId;
  }

  

  /**
   *  
   *
   * @return    External ID 
   */
  public String getExternalId()
  {
    return this.externalId;
  }


  /**
   *  set External ID
   *
   * @param   externalId  
   */
  public void setExternalId(String externalId)
  {
    this.externalId = externalId;
  }

  

  /**
   *
   *
   * @return     Invoice Date
   */
  public java.sql.Date getInvoiceDate()
  {
    return this.invoiceDate;
  }


  /**
   *  set Invoice Date
   *  
   * @param   invoiceDate  
   */
  public void setInvoiceDate(java.sql.Date invoiceDate)
  {
    this.invoiceDate = invoiceDate;
  }

  

  /**
   *
   *
   * @return     InvoiceID
   */
  public int getInvoiceId()
  {
    return this.invoiceId;
  }


  /**
   *  set InvoiceID
   *
   * @param   invoiceId  
   */
  public void setInvoiceId(int invoiceId)
  {
    this.invoiceId = invoiceId;
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
   * @return   OrderID  
   */
  public int getOrderId()
  {
    return this.orderId;
  }


  /**
   *  set OrderID
   *
   * @param   orderId  
   */
  public void setOrderId(int orderId)
  {
    this.orderId = orderId;
  }

  

  /**
   *
   *
   * @return     PO Number
   */
  public String getPo()
  {
    return this.po;
  }


  /**
   *  set PO Number
   *
   * @param   po  
   */
  public void setPo(String po)
  {
    this.po = po;
  }

  

  /**
   *
   *
   * @return     Project ID
   */
  public int getProjectId()
  {
    return this.projectId;
  }


  /**
   *  set Project ID
   *
   * @param   projectId  
   */
  public void setProjectId(int projectId)
  {
    this.projectId = projectId;
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

}
