/*
 * $RCSfile: Report.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:00 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;

import javax.ejb.EJBObject;

/**
 * <p>Title: Report</p>
 *
 * <p>Description: 	Entity Bean remote interface that represents the REPORT
 *	table.
 * </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public interface Report
    extends EJBObject {

  public void setDescription(String description) throws RemoteException;

  public String getDescription() throws RemoteException;

  public void setModifiedBy(Integer modifiedBy) throws RemoteException;

  public Integer getModifiedBy() throws RemoteException;

  public void setModifiedOn(Timestamp modifiedOn) throws RemoteException;

  public Timestamp getModifiedOn() throws RemoteException;

  public void setModuleId(int moduleId) throws RemoteException;

  public int getModuleId() throws RemoteException;

  public int getReportId() throws RemoteException;

  public void setName(String name) throws RemoteException;

  public String getName() throws RemoteException;

  public void setReportTypeId(int reportTypeId) throws RemoteException;

  public int getReportTypeId() throws RemoteException;

  public void setReportURL(String reportURL) throws RemoteException;

  public String getReportURL() throws RemoteException;

  public void setCreatedBy(int createdBy) throws RemoteException;

  public int getCreatedBy() throws RemoteException;

  public void setCreatedOn(Timestamp createdOn) throws RemoteException;

  public Timestamp getCreatedOn() throws RemoteException;

  public void setDateFrom(Date dateFrom) throws RemoteException;

  public Date getDateFrom() throws RemoteException;

  public void setDateTo(Date dateTo) throws RemoteException;

  public Date getDateTo() throws RemoteException;

}
