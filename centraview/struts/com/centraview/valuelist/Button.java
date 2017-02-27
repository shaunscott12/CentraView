/*
 * $RCSfile: Button.java,v $    $Revision: 1.2 $  $Date: 2005/09/21 18:11:10 $ - $Author: mcallist $
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
import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.centraview.common.CVUtility;

/**
 * Represents a button to the ValueListTag custom tag.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class Button implements Serializable
{
  private String label;
  private String name;
  private String onClick;
  private boolean disabled;
  private String resourceKey;
  private Locale locale;
  
  /**
   * @param label
   * @param name
   * @param onClick
   * @param disabled
   */
  public Button(String label, String name, String onClick, boolean disabled)
  {
    this.label = label;
    this.name = name;
    this.onClick = onClick;
    this.disabled = disabled;
  }

  public Button(String label, String name, String onClick, boolean disabled, String resourceKey, Locale locale) 
  {
    this(label, name, onClick, disabled);
    this.resourceKey = resourceKey;
    this.locale = locale;
  }
  
  /**
   * @return Returns the disabled.
   */
  public final boolean isDisabled()
  {
    return disabled;
  }
  /**
   * @param disabled The disabled to set.
   */
  public final void setDisabled(boolean disabled)
  {
    this.disabled = disabled;
  }
  /**
   * If locale and the resourceKey are not empty and the resource bundle gives
   * a reasonable response then return the label from the resource bundle.
   * Otherwise reuturn the hardcoded label.
   * @return Returns the label.
   */
  public final String getLabel()
  {
    String label = this.label;
    if (CVUtility.notEmpty(locale) && CVUtility.notEmpty(resourceKey)) {
      MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
      label = messages.getMessage(this.locale, this.resourceKey);
    }
    return label;
  }
  /**
   * @param label The label to set.
   */
  public final void setLabel(String label)
  {
    this.label = label;
  }
  /**
   * @return Returns the name.
   */
  public final String getName()
  {
    return name;
  }
  /**
   * @param name The name to set.
   */
  public final void setName(String name)
  {
    this.name = name;
  }
  /**
   * @return Returns the onClick.
   */
  public final String getOnClick()
  {
    return onClick;
  }
  /**
   * @param onClick The onClick to set.
   */
  public final void setOnClick(String onClick)
  {
    this.onClick = onClick;
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
  
  
}
