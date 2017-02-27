/*
 * $RCSfile: ActivityListLocal.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:11 $ - $Author: mcallist $
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

package com.centraview.activity.activitylist;

import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This interface ActivityListLocal is local interface for other EJBs
 */
public interface ActivityListLocal extends EJBLocalObject {
  /**
   * this method returns Vector
   */
  public Vector getAllResources();

  /**
   * @author Kevin McAllister <kevin@centraview.com>Allows the client to set the
   *         private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

  public ValueListVO getActivityValueList(int individualID, ValueListParameters parameters);
}