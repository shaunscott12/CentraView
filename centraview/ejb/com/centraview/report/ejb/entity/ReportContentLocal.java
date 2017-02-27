/*
 * $RCSfile: ReportContentLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:00 $ - $Author: mking_cv $
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

import javax.ejb.EJBLocalObject;

/**
 * <p>Title: ReportContent</p>
 *
 * <p>Description: 	Entity Bean local interface that represents the REPORTCONTENT
 *	table.
 * </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public interface ReportContentLocal
    extends EJBLocalObject {

  public void setFieldId(int fieldId);

  public int getFieldId();

  public int getReportContentId();

  public void setReportId(int reportId);

  public int getReportId();

  public void setSequenceNumber(short sequenceNumber);

  public short getSequenceNumber();

  public void setSortOrder(String sortOrder);

  public String getSortOrder();

  public void setSortOrderSequence(Byte sortOrderSequence);

  public Byte getSortOrderSequence();

  public int getTableId();
  
  public void setTableId(int tableId);

}
