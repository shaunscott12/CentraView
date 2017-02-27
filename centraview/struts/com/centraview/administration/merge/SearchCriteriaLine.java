/*
 * $RCSfile: SearchCriteriaLine.java,v $    $Revision: 1.2 $  $Date: 2005/07/26 20:19:18 $ - $Author: mcallist $
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
 * The search CriteriaLine is used in the form-bean for the search
 * Basically it will consist of 3 String fields to hold the index on the
 * Select drop-downs and one to hold the value for the line.
 * I will define the form bean to use an Array of these for the lines.
 * In this way I can use an elegant iteration, and let the handler grow and
 * shrink the table rather than relying on ghetto javascript, like is used in 
 * Email Rules and Advanced Search.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 * 
 */
public class SearchCriteriaLine implements Serializable
{
  private String fieldIndex = "0";
  private String searchTypeIndex = "0";
  private String matchValue;
  
  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * @return
   */
  public String getFieldIndex()
  {
    return fieldIndex;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * @return
   */
  public String getMatchValue()
  {
    return matchValue;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * @return
   */
  public String getSearchTypeIndex()
  {
    return searchTypeIndex;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * @param string
   */
  public void setFieldIndex(String string)
  {
    fieldIndex = string;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * @param string
   */
  public void setMatchValue(String string)
  {
    matchValue = string;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * @param string
   */
  public void setSearchTypeIndex(String string)
  {
    searchTypeIndex = string;
  }

  public String toString()
  {
    StringBuffer string = new StringBuffer();
    string.append("SeachCriteriaLine: {fieldIndex: ");
    string.append(fieldIndex);
    string.append(", searchTypeIndex: ");
    string.append(searchTypeIndex);
    string.append(", matchValue: \"");
    string.append(matchValue);
    string.append("\"}");
    return string.toString(); 
  }
}
