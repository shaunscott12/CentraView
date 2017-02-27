/*
 * $RCSfile: InvoicePK.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:29 $ - $Author: mking_cv $
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

import java.io.Serializable;

public class InvoicePK implements Serializable
{
  // the primary key will consist of both dataSource and InvoiceId
  // This way there can be many Invoice Entity Beans with the same InvoiceId
  // but from different dataSources
    private int invoiceId;
    private String dataSource;

    public int hashCode()
    {
    // On suggestion just cat the key integer and the dataSource
    // string together and return the hashcode of the resultant string.
    String aggregate = this.invoiceId + this.dataSource;
    return (aggregate.hashCode());
    }

    public boolean equals(Object obj)
    {
    if(obj == null || !(obj instanceof InvoicePK))
      return false;
    else if ( (((InvoicePK)obj).getId() == this.invoiceId) && ((InvoicePK)obj).getDataSource() == this.dataSource)
      return true;
    else
      return false;
    }

    public String toString()
    {
    return ("InvoicePK: InvoiceID: " + this.invoiceId + ", dataSource: " + this.dataSource);
    }

    public InvoicePK(int pkId, String ds)
    {
    this.invoiceId = pkId;
    this.dataSource = ds;
    }

  public int getId()
  {
    return this.invoiceId;
  }

  public String getDataSource()
  {
    return this.dataSource;
  }

  public InvoicePK()
    {

    }
}
