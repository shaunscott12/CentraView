/*
 * $RCSfile: ReportHome.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:00 $ - $Author: mking_cv $
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

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * <p>Title: ReportHome</p>
 *
 * <p>Description: 	Entity Bean home interface that represents the REPORT
 *	table.
 * </p>
 */

public interface ReportHome
    extends EJBHome {

  public Report findByPrimaryKey(ReportPK pk) throws FinderException, RemoteException;
  public Report create(Timestamp createdOn, Date dateFrom, Date dateTo,
                       String description, Integer modifiedBy,
                       Timestamp modifiedOn, int moduleId, String name,
                       int reportTypeId, String reportURL, String ds) throws
      CreateException, RemoteException;
}
