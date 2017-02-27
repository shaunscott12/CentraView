/*
 * $RCSfile: FieldDescriptor.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:07 $ - $Author: mking_cv $
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

/**
 * The object defined by this class stores the information about a field/column
 * in a ValueList. It is necessary to store this as a java object rather than a
 * simple primative to support the complexities introduced by custom fields.
 * Also it allows us to store a resource key to externalize the strings, in the
 * hope of one day being able to change the name of stuff.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class FieldDescriptor implements Serializable
{
  /** Name for debugging or fallback display if externalResourceKey fails */
  private String fieldName;
  /**
   * fieldId is a index for the query row list, or in customField it represents
   * the custom field Id.
   */
  private int queryIndex;
  /** If this field is a customfield this will be true otherwise no */
  private boolean customField;
  /** Mapping to the ApplicationResources.properties file, for display */
  private String externalResourceKey;
  
  /**
   * @param fieldName
   * @param queryIndex
   * @param customField
   * @param externalResourceKey
   */
  public FieldDescriptor(String fieldName, int queryIndex, boolean customField, String externalResourceKey)
  {
    super();
    this.fieldName = fieldName;
    this.queryIndex = queryIndex;
    this.customField = customField;
    this.externalResourceKey = externalResourceKey;
  }
  
  /**
   * @return Returns the customField.
   */
  public final boolean isCustomField()
  {
    return customField;
  }
  /**
   * @param customField The customField to set.
   */
  public final void setCustomField(boolean customField)
  {
    this.customField = customField;
  }
  /**
   * @return Returns the externalResourceKey.
   */
  public final String getExternalResourceKey()
  {
    return externalResourceKey;
  }
  /**
   * @param externalResourceKey The externalResourceKey to set.
   */
  public final void setExternalResourceKey(String externalResourceKey)
  {
    this.externalResourceKey = externalResourceKey;
  }
  /**
   * @return Returns the fieldId.
   */
  public final int getQueryIndex()
  {
    return queryIndex;
  }
  /**
   * @param queryIndex The queryIndex to set.
   */
  public final void setQueryIndex(int queryIndex)
  {
    this.queryIndex = queryIndex;
  }
  /**
   * @return Returns the fieldName.
   */
  public final String getFieldName()
  {
    return fieldName;
  }
  /**
   * @param fieldName The fieldName to set.
   */
  public final void setFieldName(String fieldName)
  {
    this.fieldName = fieldName;
  }
}
