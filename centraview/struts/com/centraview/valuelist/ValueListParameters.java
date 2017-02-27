/*
 * $RCSfile: ValueListParameters.java,v $    $Revision: 1.3 $  $Date: 2005/08/01 20:02:17 $ - $Author: mcallist $
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
 * This object contains the metadata about a ValueListVO
 * it includes paging, view columns, sort and filter
 * information.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ValueListParameters implements Serializable
{
  /** the id of the list type */
  private int valueListType;
  private int recordsPerPage;
  private int currentPage;
  private int totalRecords;
  /** list ValueListfieldDescriptors in the order they are to be displayed */
  private List columns;
  /** filter is a valid SQL query to select the primary key's of the desired listmembers */
  private String filter;
  /** The column to sort the results by */
  private int sortColumn;
  /** The direction of the sort, ascending or descending */
  private String sortDirection;
  /** A field to store debugging time information */
  private long debugTime;
  
  private int folderID;
  
  private int accountID;
  
  private int categoryID;
  
  /** 
   * An extra id field that is to be used for any single general parameter that 
   * needs to be passed into the actual query and can't be accomplished in the filter.
   * It was placed initially to be used by the related custom field list.
   */
  private int extraId;

  /** 
   * An extra String field that is to be used for any single general parameter that 
   * needs to be passed into the actual query and can't be accomplished in the filter.
   * It was placed initially to be used by the related custom field list.
   */
  private String extraString;
  
  /**
   * Constructor sets the type, recordsPerPage and curentPage.
   * Other more specific parameters can be set via the various setters.
   * @param valueListType An int referring to a field, definitions can be found in
   *  com.centraview.valuelist.ValueListConstants.
   * @param recordsPerPage the number of records on a single page.
   * @param currentPage the page requested from the number of records.
   */
  public ValueListParameters(int valueListType, int recordsPerPage, int currentPage)
  {
    this.valueListType = valueListType;
    this.recordsPerPage = recordsPerPage;
    this.currentPage = currentPage;
  }
  
  /**
   * @return Returns the columns.
   */
  public final List getColumns()
  {
    return columns;
  }
  /**
   * @param columns The columns to set.
   */
  public final void setColumns(List columns)
  {
    this.columns = columns;
  }
  /**
   * @return Returns the currentPage.
   */
  public final int getCurrentPage()
  {
    return currentPage;
  }
  /**
   * @param currentPage The currentPage to set.
   */
  public final void setCurrentPage(int currentPage)
  {
    this.currentPage = currentPage;
  }
  /**
   * @return Returns the totalRecords.
   */
  public final int getTotalRecords()
  {
    return totalRecords;
  }
  /**
   * @param totalRecords The totalRecords to set.
   */
  public final void setTotalRecords(int totalRecords)
  {
    this.totalRecords = totalRecords;
  }
  /**
   * @return Returns the filter.
   */
  public final String getFilter()
  {
    return filter;
  }
  /**
   * @param filter The filter to set.
   */
  public final void setFilter(String filter)
  {
    this.filter = filter;
  }
  /**
   * @return Returns the recordsPerPage.
   */
  public final int getRecordsPerPage()
  {
    return recordsPerPage;
  }
  /**
   * @param recordsPerPage The recordsPerPage to set.
   */
  public final void setRecordsPerPage(int recordsPerPage)
  {
    this.recordsPerPage = recordsPerPage;
  }
  /**
   * @return Returns the valueListType.
   */
  public final int getValueListType()
  {
    return valueListType;
  }
  /**
   * @param valueListType The valueListType to set.
   */
  public final void setValueListType(int valueListType)
  {
    this.valueListType = valueListType;
  }
  /**
   * @return Returns the sortColumn.
   */
  public final int getSortColumn()
  {
    return sortColumn;
  }
  /**
   * @param sortColumn The sortColumn to set.
   */
  public final void setSortColumn(int sortColumn)
  {
    this.sortColumn = sortColumn;
  }
  /**
   * @return Returns the sortDirection.
   */
  public final String getSortDirection()
  {
    return sortDirection;
  }
  /**
   * @param sortDirection The sortDirection to set.
   */
  public final void setSortDirection(String sortDirection)
  {
    this.sortDirection = sortDirection;
  }
  /**
   * Calculates the parameter passed to the MySQL limit command based on the
   * current page and the records per page.
   * @return
   */
  public final String getLimitParam()
  {
    if (this.recordsPerPage > 0)
    {
      int skip = this.recordsPerPage * (this.currentPage - 1);
      if (this.totalRecords > 0)
      {
        while(skip >= totalRecords)
        {
          skip = skip = this.recordsPerPage;
          this.currentPage--;
        }
      }
      return " LIMIT " + skip + ", " + this.recordsPerPage;
    }
    return "";
  }
  
  /**
   * @return Returns the debugTime.
   */
  public final long getDebugTime()
  {
    return debugTime;
  }
  /**
   * @param debugTime The debugTime to set.
   */
  public final void setDebugTime(long debugTime)
  {
    this.debugTime = debugTime;
  }

  /**
   * @param folderID The folderID to set.
   */
  public final void setFolderID(int folderID)
  {
    this.folderID = folderID;
  }
  
  /**
   * @return Returns the folderID.
   */
  public final int getFolderID()
  {
    return folderID;
  }
  
  /**
   * @param accountID The accountID to set.
   */
  public final void setAccountID(int accountID)
  {
    this.accountID = accountID;
  }
  
  /**
   * @return Returns the accountID.
   */
  public int getAccountID() {
  	return this.accountID;
  }  
  
  /**
   * @return Returns the categoryID.
   */
  public int getCategoryID() {
    return categoryID;
  }
  
  /**
   * @param categoryID The categoryID to set.
   */
  public void setCategoryID(int categoryID) {
    this.categoryID = categoryID;
  }
  
  public final int getExtraId()
  {
    return extraId;
  }
  public final void setExtraId(int extraId)
  {
    this.extraId = extraId;
  }
  public final String getExtraString()
  {
    return extraString;
  }
  public final void setExtraString(String extraString)
  {
    this.extraString = extraString;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer("\nValueListParameters: \n");
    sb.append(" - valueListType = [" + valueListType + "]\n");
    sb.append(" - recordsPerPage = [" + recordsPerPage + "]\n");
    sb.append(" - currentPage = [" + currentPage + "]\n");
    sb.append(" - totalRecords = [" + totalRecords + "]\n");
    sb.append(" - columns = [" + columns + "]\n");
    sb.append(" - filter = [" + filter + "]\n");
    sb.append(" - sortColumn = [" + sortColumn + "]\n");
    sb.append(" - sortDirection = [" + sortDirection + "]\n");
    sb.append(" - debugTime = [" + debugTime + "]\n");
    return sb.toString();
  }


}
