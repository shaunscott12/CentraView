/*
 * $RCSfile: ListView.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:12 $ - $Author: mking_cv $
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

import java.util.Vector;

public class ListView implements java.io.Serializable
{

  private String ListType;
  private String viewName;
  private String owner;
  private int viewID;
  private Vector columnNames;
  private int searchID;

  public ListView(int ID)
  {
    this.viewID = ID;
    this.columnNames = new Vector();
  }

  public Vector getColumns()
  {
    return columnNames;
  }

  public void addColumnName(String columnName)
  {
    columnNames.addElement(columnName);
  }

  public void removeColumnName(String columnName)
  {
    columnNames.remove(columnName);
  }

  public String getListType()
  {
    return ListType;
  }

  public int getViewID()
  {
    return viewID;
  }

  public int getSearchID()
  {
    return searchID;
  }

  // set Methods
  public void setListType(String value)
  {
    ListType = value;
  }

  public void setViewID(int value)
  {
    viewID = value;
  }

  public void setSearchID(int value)
  {
    searchID = value;
  }

  public String getViewName()
  {
    return this.viewName;
  }

  public void setViewName(String viewName)
  {
    this.viewName = viewName;
  }
}

