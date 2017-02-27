/*
 * $RCSfile: ProjectListsLocal.java,v $    $Revision: 1.2 $  $Date: 2005/09/08 20:38:18 $ - $Author: mcallist $
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

package com.centraview.projects.projectlist;

import java.util.HashMap;

import javax.ejb.EJBLocalObject;

import com.centraview.common.ProjectList;
import com.centraview.common.TimeSlipList;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface ProjectListsLocal extends EJBLocalObject {
  public ProjectList getProjectList(int userID, HashMap info);

  public TimeSlipList getAllTimeSlipList(int userID, HashMap info);

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId);

  public TimeSlipList getTimeSlipListForProject(int userId, int projectId, HashMap info);

  /**
   * @author Kevin McAllister <kevin@centraview.com> Allows the client to set
   *         the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

  public ValueListVO getTaskValueList(int individualID, ValueListParameters parameters);

  /**
   * Returns a ValueListVO representing a list of Project records, based on the
   * <code>parameters</code> argument which limits results.
   */
  public ValueListVO getProjectValueList(int individualId, ValueListParameters parameters);

  /**
   * Returns a ValueListVO representing a list of Project records, based on the
   * <code>parameters</code> argument which limits results.
   */
  public ValueListVO getTimeslipsValueList(int individualId, ValueListParameters parameters);

}
