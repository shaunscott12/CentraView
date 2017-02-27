/*
 * $RCSfile: MenuItem.java,v $    $Revision: 1.5 $  $Date: 2005/08/17 18:32:54 $ - $Author: mcallist $
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

import java.util.Locale;

import org.apache.struts.util.MessageResources;



/**
 * This class represents one instance of the main menu
 * on the lefthand side of the screen. You can define
 * the main Title, and multiple menu and sub-menu items.
 */
public class MenuItem extends Object implements Cloneable
{
  public String title;
  public String URL;
  public boolean isSelected;
  public int id;
  private String resourceKey;
  private Locale locale;
  private MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
  
  public MenuItem()
  { 
    // by default, set the title equal to empty String.
    // This ensures no null pointer if something
    // goes wrong or the title is not set explicitly.
    this.title = "";
    // by default, set the URL equal to empty String.
    // This ensures no null pointer if something
    // goes wrong or the URL is not set explicitly.
    this.URL = "";
    // by default, set isSelected equal to false.
    // This ensures no null pointer if something
    // goes wrong or isSelected is not set explicitly.
    this.isSelected = false;
    this.resourceKey = "";
    locale = new Locale("en");
  }

  /**
   * Creates a new MenuItem with all values initialized
   * based on the parameters passed in. This can be used
   * to create a MenuItem in one statement instead of
   * creating the item and setting all three properties.
   * @param newTitle String representation of the Title for this MenuItem
   * @param newURL String representation of the value for the "href" element 
   * of the link of this MenuItem
   * @param newIsSelected boolean value for the isSelected property
   * of this MenuUtemBean
   */
  public MenuItem(String newTitle, String newURL, boolean newIsSelected)
  { 
    super();
    if (newTitle == null){ newTitle = new String(""); }
    this.title = newTitle;

    if (newURL == null){ newURL = new String(""); }
    this.URL = newURL;

    this.isSelected = newIsSelected;
  }   // end second constructor

  public MenuItem(String newTitle, String newURL, int id)
  {
    this(newTitle, newURL, false);
    this.id = id;
  }
  /**
   * constructor for i18n, takes a resource key that should exist in 
   * the ApplicationResources.properties
   * @param newTitle
   * @param newURL
   * @param id
   * @param resourceKey
   */
  public MenuItem(String newTitle, String newURL, int id, String resourceKey) {
    this(newTitle, newURL, id);
    this.resourceKey = resourceKey;
  }
  
  public String getTitle()
  {
    return(this.title);
  }

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
  }   // end setTitle() method

  public String getURL()
  {
    return(this.URL);
  }   // end getURL() method

  public void setURL(String newURL)
  {
    if (newURL == null)
    {
      newURL = new String("");
    }
    this.URL = newURL;
  }   // end setURL() method

  public boolean getIsSelected()
  {
    return(this.isSelected);
  }   // end setIsSelected() method

  public void setIsSelected(boolean newIsSelected)
  {
    this.isSelected = newIsSelected;
  }   // end setIsSelected() method

  /**
   * @return Returns the id.
   */
  public final int getId()
  {
    return id;
  }
  /**
   * @param id The id to set.
   */
  public final void setId(int id)
  {
    this.id = id;
  }
  public Object clone()
  {
    Object cloned = null;
    try
    {
      cloned = super.clone();
    } catch (CloneNotSupportedException e) {}
    return cloned;
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
   * Given a locale use the resource bundle to pull out
   * the right label assuming we have a resourceKey defined.
   * @return the label either the title attribute or the
   * label from the resource bundle.
   */
  public String getLabel() 
  {
    String label;
    if (this.resourceKey != null && this.resourceKey.length() > 0 
        && this.locale != null && this.messages != null) {
      label = messages.getMessage(this.resourceKey, locale);
    } else {
      label = this.title;
    }
    return label;
  }

  public void setLocale(Locale locale)
  {
    this.locale = locale;
  }
  
}

