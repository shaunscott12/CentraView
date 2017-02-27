/*
 * $RCSfile: UIAttributes.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:18 $ - $Author: mking_cv $
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

import com.centraview.common.menu.LeftNavigation;
import com.centraview.common.menu.MainNavigation;

/**
 * @author CentraView, LLC <info@centraview.com>
 */
public class UIAttributes
{
  private String pageTitle;
  private MainNavigation mainNav;
  private int mainSelected;
  private LeftNavigation leftNav;
  private int[] leftSelected = new int[2];
  /**
   * @param pageTitle
   * @param leftHeader
   * @param mainNav
   * @param mainSelected
   * @param leftNav
   * @param leftSelected
   */
  public UIAttributes(String pageTitle, MainNavigation mainNav, int mainSelected, LeftNavigation leftNav, int[] leftSelected)
  {
    this.pageTitle = pageTitle;
    this.mainNav = mainNav;
    this.mainSelected = mainSelected;
    this.leftNav = leftNav;
    this.leftSelected = leftSelected;
  }
  public UIAttributes(String pageTitle, int mainSelected, LeftNavigation leftNav, int[] leftSelected)
  {
    this(pageTitle, mainSelected, leftNav);
    this.leftSelected = leftSelected;
  }
  public UIAttributes(String pageTitle, int mainSelected, LeftNavigation leftNav)
  {
    this.pageTitle = pageTitle;
    this.mainNav = Globals.DEFAULT_TABS;
    this.mainSelected = mainSelected;
    this.leftNav = leftNav;
  }
  /**
   * @return Returns the leftNav.
   */
  public final LeftNavigation getLeftNav()
  {
    return leftNav;
  }
  /**
   * @param leftNav The leftNav to set.
   */
  public final void setLeftNav(LeftNavigation leftNav)
  {
    this.leftNav = leftNav;
  }
  /**
   * @return Returns the mainNav.
   */
  public final MainNavigation getMainNav()
  {
    return mainNav;
  }
  /**
   * @param mainNav The mainNav to set.
   */
  public final void setMainNav(MainNavigation mainNav)
  {
    this.mainNav = mainNav;
  }
  /**
   * @return Returns the pageTitle.
   */
  public final String getPageTitle()
  {
    return pageTitle;
  }
  /**
   * @param pageTitle The pageTitle to set.
   */
  public final void setPageTitle(String pageTitle)
  {
    this.pageTitle = pageTitle;
  }
  /**
   * @return Returns the leftSelected.
   */
  public final int[] getLeftSelected()
  {
    return leftSelected;
  }
  /**
   * @param leftSelected The leftSelected to set.
   */
  public final void setLeftSelected(int[] leftSelected)
  {
    this.leftSelected = leftSelected;
  }
  /**
   * @return Returns the mainSelected.
   */
  public final int getMainSelected()
  {
    return mainSelected;
  }
  /**
   * @param mainSelected The mainSelected to set.
   */
  public final void setMainSelected(int mainSelected)
  {
    this.mainSelected = mainSelected;
  }
}
