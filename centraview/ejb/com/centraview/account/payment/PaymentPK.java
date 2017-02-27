/*
 * $RCSfile: PaymentPK.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:29 $ - $Author: mking_cv $
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


package com.centraview.account.payment;

import java.io.Serializable;

public class PaymentPK implements Serializable
{
  // the primary key will consist of both dataSource and paymentID
  // This way there can be many Payment Entity Beans with the same paymentID
  // but from different dataSources
    private int paymentID;
  private String dataSource;

    public int hashCode()
    {
    // On suggestion just cat the key integer and the dataSource
    // string together and return the hashcode of the resultant string.
    String aggregate = this.paymentID + this.dataSource;
    return (aggregate.hashCode());
    }

    public boolean equals(Object obj)
    {
    if(obj == null || !(obj instanceof PaymentPK))
      return false;
    else if ( (((PaymentPK)obj).getId() == this.paymentID) && ((PaymentPK)obj).getDataSource() == this.dataSource)
      return true;
    else
      return false;
    }

    public java.lang.String toString()
    {
    return ("PaymentPK: PaymentID: " + this.paymentID + ", dataSource: " + this.dataSource);
    }

    public PaymentPK(int pkId, String ds)
    {
    this.paymentID = pkId;
    this.dataSource = ds;
    }

  public int getId()
  {
    return this.paymentID;
  }

  public String getDataSource()
  {
    return this.dataSource;
  }

  public PaymentPK()
    {
  }
}
