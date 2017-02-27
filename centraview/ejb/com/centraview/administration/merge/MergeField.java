/*
 * $RCSfile: MergeField.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:46 $ - $Author: mking_cv $
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


package com.centraview.administration.merge;

import java.io.Serializable;

/**
 * MergeField is a simple object that has an Id, a Descriptive String
 * And some SQL information that maps a field to the appropriate string.
 * It is used exclusively in the SearchCriteriaVO.  Basically you end up with a user 
 * friendly field name and a query that will give you that field after you set the ID 
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 * 
 */
public class MergeField implements Serializable
{
  // Shown to the user.
  private String description = null;
  private String query = null;
  
  /**
   * Constructor that populates the MergeField internal Values.
   * The private variables.  Basically you end up with a user friendly field name
   * and a query that will give you that field after you set the ID
   * 
   * @param fieldId
   * @param description
   * @param query
   */
  public MergeField(String description, String query)
  {
    this.description = description;
    this.query = query;
  }

  /**
   * get the userfriedly field description to be shown to the user
   * for picking the selection.
   * 
   * @author Kevin McAllister <kevin@centraview.com>
   * @return the description a String
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * get the basic query that corresponds to the field.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return the query a String
   */
  public String getQuery()
  {
    return query;
  }
  
  public String toString()
  {
    String string = "MergeField: {description: "+this.description+", query: "+this.query+"}";
    return string;
  }

}
