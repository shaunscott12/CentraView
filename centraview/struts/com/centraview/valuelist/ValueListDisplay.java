/*
 * $RCSfile: ValueListDisplay.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:10 $ - $Author: mking_cv $
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

package com.centraview.valuelist;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ValueListDisplay implements Serializable
{
  private ArrayList buttonObjects;
  private boolean checkboxes;
  private boolean radio;
  private boolean icon;
  private boolean sortable;
  private boolean pagingBar;
  private boolean link;
  private boolean attachmentIcon;
  private boolean priority;
  private boolean skipRPP = false;
  private boolean skipHeader = false;
  private boolean downloadIcon;
  private boolean relatedInfo = false;
  private boolean radioToCheckbox = false;
  
  /**
   * @param buttonObjects
   * @param checkboxes
   * @param icon
   */
  public ValueListDisplay(ArrayList buttonObjects, boolean checkboxes, boolean icon)
  {
    this.buttonObjects = buttonObjects;
    this.checkboxes = checkboxes;
    this.icon = icon;
  }
  /**
   * Construct a ValueListDisplay Object setting all fields.
   * @param buttonObjects an ArrayList of button objects to be rendered on the
   *          top left of the paging bar area.
   * @param checkboxes Should the checkboxes column be displayed?
   * @param radio Should a radion column be displayed? radio and checkboxes are
   *          mutually exclusive. If radio and checkboxes are both set to true
   *          then radio will be coerced to false. Don't rely on it, set your
   *          flags correctly!
   * @param icon Should the document icon be displayed to the left of the row.
   *          These icons will be the next column after the checkbox/radio
   *          column.
   * @param sortable should the header be links with
   *          onClick="goTo('Sort.do?...');" ?
   * @param pagingBar should the paging bar be rendered at all? This is the
   *          entire big yellowish box around the actual table details.
   * @param link should the list details be decorated with links?
   */
  public ValueListDisplay(ArrayList buttonObjects, boolean checkboxes, boolean radio, boolean icon, boolean sortable, boolean pagingBar, boolean link)
  {
    this.buttonObjects = buttonObjects;
    this.checkboxes = checkboxes;
    // coerce radio based on the value of checkboxes. 
    this.radio = checkboxes ? false : radio;
    this.icon = icon;
    this.sortable = sortable;
    this.pagingBar = pagingBar;
    this.link = link;
  }
  /**
   * @return Returns the buttonObjects.
   */
  public final ArrayList getButtonObjects()
  {
    return buttonObjects;
  }
  /**
   * @param buttonObjects The buttonObjects to set.
   */
  public final void setButtonObjects(ArrayList buttonObjects)
  {
    this.buttonObjects = buttonObjects;
  }
  /**
   * @return Returns the checkboxes.
   */
  public final boolean isCheckboxes()
  {
    return checkboxes;
  }
  /**
   * @param checkboxes The checkboxes to set.
   */
  public final void setCheckboxes(boolean checkboxes)
  {
    this.checkboxes = checkboxes;
  }
  /**
   * @return Returns the icon.
   */
  public final boolean isIcon()
  {
    return icon;
  }
  /**
   * @param icon The icon to set.
   */
  public final void setIcon(boolean icon)
  {
    this.icon = icon;
  }
  /**
   * @return Returns the sortable.
   */
  public final boolean isSortable()
  {
    return sortable;
  }
  /**
   * @param sortable The sortable to set.
   */
  public final void setSortable(boolean sortable)
  {
    this.sortable = sortable;
  }
  /**
   * @return Returns the pagingBar.
   */
  public final boolean isPagingBar()
  {
    return pagingBar;
  }
  /**
   * @param pagingBar The pagingBar to set.
   */
  public final void setPagingBar(boolean pagingBar)
  {
    this.pagingBar = pagingBar;
  }
  /**
   * @return Returns the radio.
   */
  public final boolean isRadio()
  {
    return radio;
  }
  /**
   * @param radio The radio to set.
   */
  public final void setRadio(boolean radio)
  {
    this.radio = radio;
  }
  /**
   * @return Returns the link.
   */
  public final boolean isLink()
  {
    return link;
  }
  /**
   * @param link Set link.
   */
  public final void setLink(boolean link)
  {
    this.link = link;
  }
  /**
   * @return Returns the attachmentIcon.
   */
  public final boolean isAttachmentIcon()
  {
    return attachmentIcon;
  }
  /**
   * @param link Set attachmentIcon.
   */
  public final void setAttachmentIcon(boolean attachmentIcon)
  {
    this.attachmentIcon = attachmentIcon;
  }  
  /**
   * @return Returns the priority.
   */
  public final boolean isPriority()
  {
    return priority;
  }
  /**
   * @param link Set priority.
   */
  public final void setPriority(boolean priority)
  {
    this.priority = priority;
  }   
  public final boolean isSkipRPP()
  {
    return skipRPP;
  }
  public final void setSkipRPP(boolean skipRPP)
  {
    this.skipRPP = skipRPP;
  }
  public final boolean isSkipHeader()
  {
    return skipHeader;
  }
  public final void setSkipHeader(boolean skipHeader)
  {
    this.skipHeader = skipHeader;
  }
  /**
   * @return Returns the saveIcon.
   */
  public boolean isDownloadIcon() {
    return downloadIcon;
  }
  /**
   * @param saveIcon The saveIcon to set.
   */
  public void setDownloadIcon(boolean downloadIcon) {
    this.downloadIcon = downloadIcon;
  }
  
  public final boolean isRelatedInfo()
  {
    return relatedInfo;
  }
  public final void setRelatedInfo(boolean relatedInfo)
  {
    this.relatedInfo = relatedInfo;
  }

  public final boolean isRadioToCheckBox()
  {
    return radioToCheckbox;
  }
  public final void setRadioToCheckBox(boolean radioToCheckbox)
  {
    this.radioToCheckbox = radioToCheckbox;
  }
}
