/*
 * $RCSfile: GenericListElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:00 $ - $Author: mking_cv $
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
 * This is a GenericListElement, because for some reason there were many specific ListElement
 * subclasses created with all the same functionallity.  So I have created this with the goal of
 * eventually moving all references to the other ListElements to use this one.  In that way
 * we can ease maintenance, eventually.
 * 
 * This was specifically created to support the Merge / Purge lists, but it shouldn't matter.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 * 
 */
public class GenericListElement extends ListElement
{
  /**
   * The unique database Id for what this element represents.
   */
  private int elementId;

  public GenericListElement(int id)
  {
    this.elementId = id;
  }

  /**
   * ListElement extends HashMap so this calls put()
   * using an Integer ID for the key to the HashMap.
   * @see com.centraview.common.ListElement#addMember(int, com.centraview.common.ListElementMember)
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public void addMember(int ID, ListElementMember lem)
  {
    put(new Integer(ID), lem);
  }

  /**
   * For some reason all the other implementations of this ListMember return null on
   * this method.  It seems, sadly, not to be used.
   * @return NULL always, always returns null.  It's like a NullPointerException factory.
   * @see com.centraview.common.ListElement#getMember(java.lang.String)
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public ListElementMember getMember(String memberName)
  {
    return null;
  }

  /**
   * This returns the ID of this ListElement.
   * @see com.centraview.common.ListElement#getElementID()
   */
  public int getElementID()
  {
    return elementId;
  }

}
