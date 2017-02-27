/*
 * $RCSfile: ReportFacadeLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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

package com.centraview.report.ejb.session;

import javax.ejb.EJBLocalObject;

import com.centraview.advancedsearch.SearchVO;
import com.centraview.report.valueobject.ReportResultVO;
import com.centraview.report.valueobject.ReportVO;
import com.centraview.report.valueobject.SelectVO;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * <p>Title: ReportFacade</p>
 *
 * <p>Description: Session Bean local interface.
 * </p>
 */

public interface ReportFacadeLocal extends EJBLocalObject
{
  public ValueListVO getStandardReportList(int individualId, ValueListParameters parameters);
  public ValueListVO getAdHocReportList(int individualId, ValueListParameters parameters);
  public void delete(int[] reportIds);
  public ReportVO getStandardReport(int userId, int reportId);
  public int updateStandardReport(int userId, ReportVO reportVO, boolean saveName);
  public SelectVO getAdHocPageData(int moduleId);
  public int addAdHocReport(int userId, ReportVO reportVO);
  public ReportVO getAdHocReport(int userId, int reportId);
  public int updateAdHocReport(int userId, ReportVO reportVO);
  public ReportResultVO getAdHocReportResult(int userId, int reportId, SearchVO search);
  public ReportResultVO getStandardReportResult(int userId, int reportId, SearchVO search);
  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

}