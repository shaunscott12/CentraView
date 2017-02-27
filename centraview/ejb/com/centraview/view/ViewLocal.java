/*
 * $RCSfile: ViewLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:18 $ - $Author: mking_cv $
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

package com.centraview.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBLocalObject;

public interface ViewLocal extends EJBLocalObject
{
  public int addView(int userId, ViewVO nvo);
  public ViewVO getView(int userId, int viewId);
  public void updateView(int userId, ViewVO nvo);
  public Vector getAllColumns(String strListType);
  public Vector getOwnerSearch (int ownerId, String listType);
  public Vector getViewComboData (int userId, String primaryTableName);
  public Vector getViewInfo(int userId, int viewId);
  public int getSystemDefaultView(String listType);
  public void updateUserDefaultView(int viewId, int userId, String sortType, String sortElement, int recordPerPage, String listType);
  public void deleteUserView(int viewId);
  public Vector getUserDefaultColumns (int viewId, String listType, int userId);
  public void deleteView(int viewId);
  public int getUserDefaultView(int userId, String listType);
  public void updateUserDefaultViewSort(int viewId, String listType, String sortColumn, String sortOrder);
  public void updateUserDefaultViewRRP(int viewId, String listType, int recordPerPage);
  public HashMap getDefaultViews(ArrayList listType);
  public void setDefaultView(HashMap defaultViews);
  public void setDataSource(String ds);
}

