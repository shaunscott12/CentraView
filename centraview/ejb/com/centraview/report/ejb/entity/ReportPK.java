/*
 * $RCSfile: ReportPK.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:02 $ - $Author: mking_cv $
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

package com.centraview.report.ejb.entity;

import java.io.Serializable;

/**
 * <p>Title: ReportPK</p>
 *
 * <p>Description: 	Entity Bean primary key class that represents the REPORT
 *	table.
 * </p>
 */
public class ReportPK implements Serializable 
{
  public int reportId;
  public String dataSource;
  public ReportPK() {}

  public ReportPK(int reportId, String ds) 
  {
    this.reportId = reportId;
    this.dataSource = ds;
  }

  public boolean equals(Object obj) 
  {
    if (this == obj) {
      return true;
    }
    if (! (obj instanceof ReportPK)) {
      return false;
    }
    ReportPK that = (ReportPK) obj;
    if (that.reportId != this.reportId && !that.dataSource.equals(this.dataSource)) {
      return false;
    }
    return true;
  }

  public int hashCode() 
  {
    // using hash algorithm from _Java Enterprise Best Practices_
    int result = 0;
    result = result * 31 + dataSource.hashCode();
    result = result * 31 + this.reportId;
    return result;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("ReportPK:{reportId: ");
    sb.append(reportId);
    sb.append(", dataSource: ");
    sb.append(dataSource);
    return sb.toString();
  }
}
