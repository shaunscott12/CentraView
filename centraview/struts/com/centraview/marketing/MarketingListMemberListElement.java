/*
 * $RCSfile: MarketingListMemberListElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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
package com.centraview.marketing;

import com.centraview.common.ListElement;
import com.centraview.common.ListElementMember;

/**
 * This class will contain the list elements of the MarketingListMemberList.
 *
 * @author   Ryan Grier <ryan@centraview.com>
 * @see      com.centraview.marketing.MarketingListMemberList
 */
public class MarketingListMemberListElement extends ListElement
{
  /** The MarketingListMemberListElement ID.  */
  private int listElementID;

  /**
   * Constructs a new MarketingListMemberListElement with ID of listElementID
   *
   * @param listElementID  The List Element ID of the new MarketingListMemberListElement
   */
  public MarketingListMemberListElement(int listElementID)
  {
    this.listElementID = listElementID;
  } //end of MarketingListMemberListElement constructor

  /**
   * Returns the List Element ID.
   *
   * @return   The list element ID.
   */
  public int getElementID()
  {
    return this.listElementID;
  } //end of getElementID method

  /**
   * Adds a new member to a row.
   *
   * @param ID                 Where to add the new member.
   * @param listElementMember  The new member to be added.
   */
  public void addMember(int ID, ListElementMember listElementMember)
  {
    put(new Integer(ID), listElementMember);
  } //end of addMember method

  /**
   * Returns the ListElementMember requested. Although it currently returns
   * NULL.
   *
   * @param memberName  It doesn't matter, it returns NULL
   * @return            Current returns NULL.
   */
  public ListElementMember getMember(String memberName)
  {
    return null;
  } //end of getMember method.

  /**
   * Returns the list authorization.
   *
   * @return   The list authorization.
   */
  public char getListAuth()
  {
    return this.auth;
  } //end of getListAuth method

  /**
   * Sets the list authorization.
   *
   * @param value  The list authorization value.
   */
  public void setListAuth(char value)
  {
    super.auth = value;
  } //end of setListAuth method
} //end of MarketingListMemberListElement class