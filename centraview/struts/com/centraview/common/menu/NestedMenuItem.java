/*
 * $RCSfile: NestedMenuItem.java,v $    $Revision: 1.2 $  $Date: 2005/08/16 21:20:39 $ - $Author: mcallist $
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

package com.centraview.common.menu;

import java.util.ArrayList;

/**
 * This class is an extention of MenuItem, which can only
 * handle primary navigation on a left nav.  This has been extended
 * to handle an arbitrary depth of navigation, however the real problem
 * then becomes the aestetic treatment of it.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class NestedMenuItem extends MenuItem implements Cloneable 
{
  /**
   * This will hold a collection of HierarchicalMenuItems
   * @author mcallist
   */
  private ArrayList items = new ArrayList();
  
  /**
   * @param newTitle
   * @param newURL
   * @param newIsSelected
   */
  public NestedMenuItem(String newTitle, String newURL, int id) 
  {
    super(newTitle, newURL, id);
  }
  public NestedMenuItem(String newTitle, String newURL, boolean newIsSelected)
  {
    super(newTitle, newURL, newIsSelected);
  }
  public NestedMenuItem(String title, String URL, int id, String resourceKey) {
    super(title, URL, id, resourceKey);
  }
  
  /**
   * @return Returns the items.
   */
  public ArrayList getItems() 
  {
    return items;
  }
  
  /**
   * @param items The items to set.
   */
  public void setItems(ArrayList items) 
  {
    this.items = items;
  }
  
  public String toString() 
  {
    String string = "Title: " + this.title + ", URL: " + this.URL + 
      ", isSelected: " + this.isSelected +  ", items: " + items.toString();
    return string;
  }
  
  /**
   * Iterate the items list and set all elements to
   * isSelected = false recursively.
   */
  public void clearSelection()
  {
    for (int i = 0; i < items.size(); i++)
    {
      NestedMenuItem element = (NestedMenuItem)items.get(i);
      element.setIsSelected(false);
      element.clearSelection();
    }
  }
  
  /**
   * This method will select the selectItem-th element after clearing
   * all other selections first.
   * @param selectItem
   */
  public void mutexSelect(int selectItem)
  {
    this.clearSelection();
    NestedMenuItem element = (NestedMenuItem)this.items.get(selectItem);
    element.setIsSelected(true);
  }
  
  /**
   * This method takes an array of integers which are the
   * indexes of the menu items to select.  So if you have a menu
   * with 3 top level items each with 3 second level items and you want to show that
   * the third sub item of the second item is selected you would call this with the 
   * int array {2, 3}.  It would clear the Select from everything else and dig down.
   * to select each other item.
   * 
   * There is nothing programatically to stop a programmer from hosing himself based
   * on NoSuchElement exceptions.  So just be careful.
   * 
   * If it turns into a problem add logic in here to default to something.
   * 
   * @param selectItems
   */
  public void deepSelect(int[] selectItems)
  {
    this.clearSelection();
    for (int i = 0; i < selectItems.length; i++)
    {
      NestedMenuItem element = (NestedMenuItem)this.items.get(selectItems[i]);
      element.setIsSelected(true);
    }
  }
  
  public Object clone()
  {
    NestedMenuItem copy = (NestedMenuItem)super.clone();
    ArrayList listCopy = new ArrayList();
    for (int i = 0; i < this.items.size(); i++)
    {
      NestedMenuItem element = (NestedMenuItem)this.items.get(i);
      listCopy.add(element.clone());
    }
    copy.setItems(listCopy);
    return copy;
  }
  
  public void addItem(MenuItem item)
  {
    this.items.add(item);
  }
}
