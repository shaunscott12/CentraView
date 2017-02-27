/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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

package com.centraview.home;

/**
 * This object holds some properties about a thing to be displayed on the home
 * page This could probably be made generic, but right now, simplest thing that
 * works.
 * @author mcallist
 */
public class HomeDisplayItem {
  /** the items title */
  private String title;
  /** url typically for wrapping around the title */
  private String url;
  /** title to be displayed on OverLib javascript hover */
  private String olTitle;
  /** title to be displayed on OverLib javascript hover */
  private String olDescription;
  /** the caption to be displayed in the top area of the javascript hover */
  private String olCaption;
  /** icon name associated with this item */
  private String icon;
  /** an additional string to be displayed */
  private String subTitle;
  /** the title of the related record the type of record is usually given
   * by context
   */
  private String relatedTitle;
  /** the id of the related record */
  private int relatedId;

  /**
   * Default constructor
   */
  public HomeDisplayItem() {
    super();
  }

  /**
   * convenience Constructor that takes all the fields at once
   */
  public HomeDisplayItem(String title, String url, String olTitle, String olDescription, String subTitle,
      String olCaption, String icon) {
    super();

    this.icon = icon;
    this.olDescription = olDescription;
    this.olTitle = olTitle;
    this.olCaption = olCaption;
    this.subTitle = subTitle;
    this.title = title;
    this.url = url;
  }

  public String getIcon()
  {
    return this.icon;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  public String getOlDescription()
  {
    return this.olDescription;
  }

  public void setOlDescription(String olDescription)
  {
    this.olDescription = olDescription;
  }

  public String getOlTitle()
  {
    return this.olTitle;
  }

  public void setOlTitle(String olTitle)
  {
    this.olTitle = olTitle;
  }

  public String getOlCaption()
  {
    return this.olCaption;
  }

  public void setOlCaption(String olCaption)
  {
    this.olCaption = olCaption;
  }

  public String getSubTitle()
  {
    return this.subTitle;
  }

  public void setSubTitle(String subTitle)
  {
    this.subTitle = subTitle;
  }

  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getUrl()
  {
    return this.url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getRelatedTitle()
  {
    return this.relatedTitle;
  }

  public void setRelatedTitle(String relatedTitle)
  {
    this.relatedTitle = relatedTitle;
  }

  public int getRelatedId()
  {
    return this.relatedId;
  }

  public void setRelatedId(int relatedId)
  {
    this.relatedId = relatedId;
  }

}
