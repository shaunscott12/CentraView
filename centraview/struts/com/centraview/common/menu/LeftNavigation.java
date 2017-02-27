/*
 * $RCSfile: LeftNavigation.java,v $    $Revision: 1.4 $  $Date: 2005/09/20 20:22:21 $ - $Author: mcallist $
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
import java.util.Locale;

import org.apache.struts.util.MessageResources;

/**
 * @author CentraView, LLC <info@centraview.com>
 */
public class LeftNavigation
{
  private String leftHeader;
  private String resourceKey;
  private ArrayList elements;
  private Locale locale = new Locale("en");
  private MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
  /**
   * @param elements
   */
  public LeftNavigation(String leftHeader, ArrayList elements)
  {
    this.leftHeader = leftHeader;
    this.elements = elements;
  }
  public LeftNavigation(String leftHeader, ArrayList elements, String resourceKey)
  {
    this(leftHeader, elements);
    this.resourceKey = resourceKey;
  }
  /**
   * @return Returns the elements.
   */
  public final ArrayList getElements()
  {
    return elements;
  }
  /**
   * @param elements The elements to set.
   */
  public final void setElements(ArrayList elements)
  {
    this.elements = elements;
  }
  public final String getLeftHeader()
  {
    return leftHeader;
  }
  public final void setLeftHeader(String leftHeader)
  {
    this.leftHeader = leftHeader;
  }
  /**
   * Iterate the elements list and set all elements to
   * isSelected = false recursively.
   */
  public void clearSelection()
  {
    for (int i = 0; i < this.elements.size(); i++)
    {
      NestedMenuItem element = (NestedMenuItem)elements.get(i);
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
    NestedMenuItem element = (NestedMenuItem)this.elements.get(selectItem);
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
      NestedMenuItem element = (NestedMenuItem)this.elements.get(selectItems[i]);
      element.setIsSelected(true);
    }
  }
  public Locale getLocale()
  {
    return this.locale;
  }
  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }
  
  public String getResourceKey()
  {
    return this.resourceKey;
  }
  public void setResourceKey(String resourceKey)
  {
    this.resourceKey = resourceKey;
  }
  /**
   * return the appropriate label from the resource bundle
   * if the resourceKey is set and the locale is set.
   * Otherwise provide the leftHeader string.
   * @return the text representation.
   */
  public String getLabel()
  {
    String label;
    if (this.resourceKey != null && this.resourceKey.length() > 0 
        && this.locale != null && this.messages != null) {
      label = messages.getMessage(locale, this.resourceKey);
    } else {
      label = this.leftHeader;
    }
    return label;
  }
}