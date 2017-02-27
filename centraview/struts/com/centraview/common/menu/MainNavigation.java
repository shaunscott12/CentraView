/*
 * $RCSfile: MainNavigation.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:19 $ - $Author: mking_cv $
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
 * @author CentraView, LLC <info@centraview.com>
 */
public class MainNavigation
{
  private ArrayList elements;
  /**
   * @param elements
   */
  public MainNavigation(ArrayList elements)
  {
    this.elements = elements;
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
  
  /**
   * Iterate the items list and set all elements to
   * isSelected = false recursively.
   */
  public void clearSelection()
  {
    for (int i = 0; i < this.elements.size(); i++)
    {
      MenuItem element = (MenuItem)this.elements.get(i);
      element.setIsSelected(false);
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
    MenuItem element = (MenuItem)this.elements.get(selectItem);
    element.setIsSelected(true);
  }
}
