/*
 * $RCSfile: MenuBean.java,v $    $Revision: 1.2 $  $Date: 2005/06/10 17:52:31 $ - $Author: mking_cv $
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
 * This class represents one instance of the main menu
 * on the lefthand side of the screen. You can define
 * the main Title, and multiple menu and sub-menu items.
 */
public class MenuBean extends Object
{
  public String title;
  public ArrayList menuItems;

  public MenuBean()
  { 
    // by default, set the title equal to "Home".
    // This ensures no null pointer if something
    // goes wrong or the title is not set explicitly.
    this.title = "Home";

    // by default, set the menuItems member to
    // an empty array list. This ensures no null
    // pointer if something goes wrong or the menu
    // Items are not set explicitly. Note that it
    // it possible to have no menu items.
    this.menuItems = new ArrayList();
  }   // end constructor

  /**
   * Creates a new MenuBean object will all properties
   * initialized to valid values. This can be used to
   * create a MenuBean object with one statement instead
   * of creating the object, then setting each property
   * with the accessor methods.
   * @param newTitle String representation of the Title
   * of this MenuBean.
   * @param newMenuItems ArrayList containing MenuItem
   * objects that represent the selections on this menu.
   */
  public MenuBean(String newTitle, ArrayList newMenuItems)
  {
    if (newTitle == null){ newTitle = new String(""); }
    this.title = newTitle;

    if (newMenuItems == null){ newMenuItems = new ArrayList(); }
    this.menuItems = newMenuItems;
  }   // end second constructor

  public String getTitle()
  {
    return(this.title);
  }   // end getTitle()

  public void setTitle(String newTitle)
  {
    // if the newTitle parameter is null, set
    // newTitle equal to an empty *but initialized*
    // String object to avoid null pointer errors.
    if (newTitle == null)
    {
      newTitle = new String("");
    }
    this.title = newTitle;
  }   // end setTitle();

  public ArrayList getMenuItems()
  {
    return(this.menuItems);
  }   // end getMenuItems()

  public void setMenuItems(ArrayList newMenuItems)
  {
    // if the newMenuItems parameter is null, set
    // newMenuItems equal to an empty *but initialized*
    // ArrayList object to avoid null pointer errors.
    if (newMenuItems == null)
    {
      newMenuItems = new ArrayList();
    }
    this.menuItems = newMenuItems;
  }   // end setMenuItems()

}   // end class definition

