/*
 * $RCSfile: PurchaseOrderVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:32 $ - $Author: mking_cv $
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


public class PurchaseOrderVOX extends  PurchaseOrderVO
{

  public PurchaseOrderVOX(PurchaseOrderForm form)
  {

    if(form.getAccountmanagerid() != null && !((form.getAccountmanagerid()).equals("")))
      setAccountManagerId(Integer.parseInt(form.getAccountmanagerid()));

    if(form.getBilltoID() != null && !((form.getBilltoID()).equals("")))
      setBillToId(Integer.parseInt(form.getBilltoID()));

    if(form.getVendorId() != null && !((form.getVendorId()).equals("")))
      setVendorId(Integer.parseInt(form.getVendorId()));

    setPurchaseOrderDate(form.getPurchaseOrderDate());

    if(form.getPurchaseOrderid() != null && !((form.getPurchaseOrderid()).equals("")))
      setPurchaseOrderId(Integer.parseInt(form.getPurchaseOrderid()));

    setItemLines(form.getItemLines());
    setNotes(form.getNotes());


    if(form.getShiptoID() != null && !((form.getShiptoID()).equals("")))
      setShipToId(Integer.parseInt(form.getShiptoID()));

    if(form.getStatusid() != null && !((form.getStatusid()).equals("")))
      setStatusId(Integer.parseInt(form.getStatusid()));

    if(form.getTermid() != null && !((form.getTermid()).equals("")))
      setTermId(Integer.parseInt(form.getTermid()));

    setNotes(form.getNotes());

    int month = 0;
    if(form.getMonth() != null && !((form.getMonth()).equals("")))
      month = Integer.parseInt(form.getMonth());

    int date = 0;
    if(form.getDate() != null && !((form.getDate()).equals("")))
      date = Integer.parseInt(form.getDate());

    int year = 0;
    if(form.getYear() != null && !((form.getYear()).equals("")))
      year = Integer.parseInt(form.getYear());
    java.sql.Date invDate = new java.sql.Date(year,month-1,date);
    setPurchaseOrderDate(invDate);

    if(form.getModifiedBy() != 0 )
      this.setModifiedBy(form.getModifiedBy());
  }

  public PurchaseOrderVO getVO()
  {
    return super.getVO();
  }
}














