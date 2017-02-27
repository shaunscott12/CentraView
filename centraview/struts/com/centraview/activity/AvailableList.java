/*
 * $RCSfile: AvailableList.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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
package com.centraview.activity;

/**
 * This array of boolean tells whether an individual is available for each hour of the day.
 */
public class AvailableList {
  int userID;
  String userName;
  boolean available[] = new boolean[24];

  public AvailableList(int id, String name) {
    this.userID = id;
    this.userName = name;
  }

  public boolean[] getAvailable()
  {
    return this.available;
  }

  public void setAvailable(boolean[] available)
  {
    this.available = available;
  }

  public int getUserID()
  {
    return this.userID;
  }

  public void setUserID(int userID)
  {
    this.userID = userID;
  }

  public String getUserName()
  {
    return this.userName;
  }

  public void setUserName(String userName)
  {
    this.userName = userName;
  }
}
