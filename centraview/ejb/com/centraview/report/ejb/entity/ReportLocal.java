/*
 * $RCSfile: ReportLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:00 $ - $Author: mking_cv $
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

import java.sql.Date;
import java.sql.Timestamp;

import javax.ejb.EJBLocalObject;

/**
 * <p>Title: ReportLocal</p>
 *
 * <p>Description: 	Entity Bean local interface that represents the REPORT
 *	table.
 * </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public interface ReportLocal
    extends EJBLocalObject {
  public void setDescription(String description);

  public String getDescription();

  public void setModifiedBy(Integer modifiedBy);

  public Integer getModifiedBy();

  public void setModifiedOn(Timestamp modifiedOn);

  public Timestamp getModifiedOn();

  public void setModuleId(int moduleId);

  public int getModuleId();

  public int getReportId();

  public void setName(String name);

  public String getName();

  public void setReportTypeId(int reportTypeId);

  public int getReportTypeId();

  public void setReportURL(String reportURL);

  public String getReportURL();

  public void setCreatedBy(int createdBy);

  public int getCreatedBy();

  public void setCreatedOn(Timestamp createdOn);

  public Timestamp getCreatedOn();

  public void setDateFrom(Date dateFrom);

  public Date getDateFrom();

  public void setDateTo(Date dateTo);

  public Date getDateTo();

}
