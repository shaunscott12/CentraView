/*
 * $RCSfile: LocationListElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:12 $ - $Author: mking_cv $
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

/**
 * ItemListElement.java
 *
 * @version 	1.0 Date  29 Jan 2004
 * @author Ilia Kirillov
 */

/**
This class stores all columns for each row of Location

*/

public class LocationListElement extends ListElement
{
  private int itsElementID;
  /**
   * constructor
   *
   * @param   ID
   */
  public LocationListElement(int ID)
  {
    itsElementID = ID;
  }

  /**
   *
   * add member to row
   * @param   ID
   * @param   lem
   */
  public void addMember(int ID, ListElementMember lem)
  {
    put(new Integer(ID), lem);
  }

  /**
   * this method returns  row member
   *
   * @param   memberName
   * @return
   */
  public ListElementMember getMember(String memberName)
  {
    return null;
  }

  /**
   * this method sets List authrization
   *
   * @param   value
   */
  public void setListAuth(char value)
  {
    super.auth = value;
  }

  /**
   * this method gets List authrization
   *
   * @return
   */
  public char getListAuth()
  {
    return auth;
  }

  /**
   * gets List element ID
   *
   * @return
   */
  public int getElementID()
  {
    return itsElementID;
  }
} //ItemListElement class
