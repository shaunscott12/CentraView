/*
 * $RCSfile: CalendarList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:51 $ - $Author: mking_cv $
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

package com.centraview.calendar;
import java.util.HashMap;
import java.util.TreeMap;
public class CalendarList extends TreeMap
{
  private HashMap maxIndices;
  private TreeMap tdIndices;
  private String searchString;
  private String powerstring;

  public CalendarList()
  {
    maxIndices = new HashMap();
    tdIndices = new TreeMap();
  }

  public HashMap getMaxIndicesHashMap()
  {
    return maxIndices;
  }

  public TreeMap getTDIndicesTreeMap()
  {
    return tdIndices;
  }

  public String getSearchString()
  {
    return searchString;
  }

  public void setSearchString(String value)
  {
    searchString = value;
  }

  public String getPowerstring()
  {
    return this.powerstring;
  }

  public void setPowerstring(String powerstring)
  {
    this.powerstring = powerstring;
  }
}