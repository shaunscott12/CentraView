/*
 * $RCSfile: ListPreference.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:12 $ - $Author: mking_cv $
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
package com.centraview.common;

import java.io.Serializable;
import java.util.HashMap;

public class ListPreference implements Serializable
{
  private int recordsPerPage;
  private String sortElement;
  private int defaultView;
  private HashMap viewHashMap;
  private boolean sortOrder;
  private String lpName;

  public ListPreference(String value)
  {
    this.lpName = value;
    this.viewHashMap = new HashMap();
  }

  public ListView getListView(String viewID)
  {
    return (ListView) viewHashMap.get(viewID);
  }

  public String getLPName()
  {
    return lpName;
  }

  public void addListView(ListView lv)
  {
    viewHashMap.put(lv.getViewID() + "", lv);
  }

  public int getRecordsPerPage()
  {
    return recordsPerPage;
  }

  public String getSortElement()
  {
    return sortElement;
  }

  public int getDefaultView()
  {
    return defaultView;
  }

  public boolean getsortOrder()
  {
    return sortOrder;
  }

  //set UserObject
  public void setRecordsPerPage(int value)
  {
    recordsPerPage = value;
  }

  public void setSortElement(String value)
  {
    sortElement = value;
  }

  public void setDefaultView(int value)
  {
    defaultView = value;
  }

  public void setSortOrder(boolean value)
  {
    sortOrder = value;
  }

  public HashMap getViewHashMap()
  {
    return this.viewHashMap;
  }

  public void setViewHashMap(HashMap viewHashMap)
  {
    this.viewHashMap = viewHashMap;
  }
}
