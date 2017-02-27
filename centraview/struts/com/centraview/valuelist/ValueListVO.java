/*
 * $RCSfile: ValueListVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:11 $ - $Author: mking_cv $
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
import java.util.List;

/**
 * This class contains the details of a list. It contains two fields the list
 * itself and an Object that describes the metadata about the list, (the paging,
 * column, filter information) It is created calling the ValueListEJB.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ValueListVO implements Serializable
{
  /**
   * The actual list, Expected to be a list of ValueListRows, giving us a 2D array to
   * represent the list
   */
  private List list;
  /** The list metadata */
  private ValueListParameters parameters;
  /** Some view layer metadata */
  private ValueListDisplay display;

  /** A field to identify wheather its a lookup or not information */
  private boolean lookup;

  /** A field to identify wheather its a MoveTo is needed or not*/
  private boolean moveToFlag;
  
  /** A field to identify which lookup we are processing */
  private String lookupType = "";
  
  /** A will build the parameters and concat with the sorting url */
  private String currentPageParameters;

  /** A will build the parameters and concat with the Icon Link url */
  private String currentLinkParameters;
  
  /**
   * Create a valueListVO initialized with the valuelist and the parameters used
   * to generate the list.
   * @param list The valuelist itself as a List which hopefully each row of the
   *          list is a list
   * @param parameters The parameters that were used to request this list.
   */
  public ValueListVO(List list, ValueListParameters parameters)
  {
    this.list = list;
    this.parameters = parameters;
    this.lookupType = "";
  }
  
  /**
   * @return Returns the list.
   */
  public final List getList()
  {
    return list;
  }
  /**
   * @param list The list to set.
   */
  public final void setList(List list)
  {
    this.list = list;
  }
  /**
   * @return Returns the parameters.
   */
  public final ValueListParameters getParameters()
  {
    return parameters;
  }
  /**
   * @param parameters The parameters to set.
   */
  public final void setParameters(ValueListParameters parameters)
  {
    this.parameters = parameters;
  }
  /**
   * @return Returns the display.
   */
  public final ValueListDisplay getDisplay()
  {
    return display;
  }
  /**
   * @param display The display to set.
   */
  public final void setDisplay(ValueListDisplay display)
  {
    this.display = display;
  }

  /**
   * @return Returns the lookupFlag.
   */
  public final boolean isLookup()
  {
	return lookup;
  }
  
  /**
   * @param lookupFlag The lookupFlag to set.
   */
  public final void setLookup(boolean lookup)
  {
	this.lookup = lookup;
  }  
  /**
   * @return Returns the lookupType.
   */
  public final String getLookupType()
  {
	return lookupType;
  }
  
  /**
   * @param lookupType The lookupType to set.
   */
  public final void setLookupType(String lookupType)
  {
	this.lookupType = lookupType;
  }   
  
  /**
   * @param currentPageParameters The currentPageParameters to set.
   */
  public final void setCurrentPageParameters(String currentPageParameters)
  {
    this.currentPageParameters = currentPageParameters;
  }
  
  /**
   * @return Returns the currentPageParameters.
   */
  public String getCurrentPageParameters() {
  	return this.currentPageParameters;
  } 
  
  /**
   * @return Returns the moveToFlag.
   */
  public final boolean isMoveTo()
  {
	return moveToFlag;
}
  /**
   * @param moveToFlag The moveToFlag to set.
   */
  public final void setMoveTo(boolean moveToFlag)
  {
	this.moveToFlag = moveToFlag;
  }  

  /**
   * @param currentLinkParameters The currentLinkParameters to set.
   */
  public final void setCurrentLinkParameters(String currentLinkParameters)
  {
    this.currentLinkParameters = currentLinkParameters;
  }
  
  /**
   * @return Returns the currentLinkParameters.
   */
  public String getCurrentLinkParameters() {
  	return this.currentLinkParameters;
  }

  public String toString() 
  {
    StringBuffer sb = new StringBuffer("\nValueListVO:\n");
    sb.append(" - list = [" + list + "]\n");
    sb.append(" - parameters = [" + parameters + "]\n");
    sb.append(" - display = [" + display + "]\n");
    sb.append(" - lookup = [" + lookup + "]\n");
    sb.append(" - moveToFlag = [" + moveToFlag + "]\n");
    sb.append(" - lookupType = [" + lookupType + "]\n");
    sb.append(" - currentPageParameters = [" + currentPageParameters + "]\n");
    sb.append(" - currentLinkParameters = [" + currentLinkParameters + "]\n");
    return sb.toString();
  }

}
