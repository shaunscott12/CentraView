/*
 * $RCSfile: ReportContentPK.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:00 $ - $Author: mking_cv $
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
 * <p>Title: ReportContent<PK/p>
 *
 * <p>Description: 	Entity Bean primary key class that represents the REPORTCONTENT
 *	table.
 * </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public class ReportContentPK implements Serializable
{
  public int reportContentId;
  public String dataSource;
  public ReportContentPK()
  {}

  public ReportContentPK(int reportContentId, String ds)
  {
    this.reportContentId = reportContentId;
    this.dataSource = ds;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!(obj instanceof ReportContentPK))
    {
      return false;
    }
    ReportContentPK that = (ReportContentPK)obj;
    if (that.reportContentId != this.reportContentId && !this.dataSource.equals(that.dataSource))
    {
      return false;
    }
    return true;
  }

  public int hashCode()
  {
    // using hash algorithm from _Java Enterprise Best Practices_
    int result = 0;
    result = result * 31 + dataSource.hashCode();
    result = result * 31 + this.reportContentId;
    return result;
  }

  public String getDataSource()
  {
    return dataSource;
  }

  public int getReportContentId()
  {
    return reportContentId;
  }

  public void setDataSource(String string)
  {
    dataSource = string;
  }

  public void setReportContentId(int i)
  {
    reportContentId = i;
  }

}