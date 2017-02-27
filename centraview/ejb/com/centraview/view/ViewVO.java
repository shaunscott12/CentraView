/*
 * $RCSfile: ViewVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:18 $ - $Author: mking_cv $
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

package com.centraview.view;

import java.util.Vector;

import com.centraview.common.CVAudit;

public class ViewVO extends CVAudit
{
  private int viewId;
  private String listType;
  private String viewName;
  private String sortMember;
  private String sortType;
  private int searchId;
  private String searchType;
  private int noOfRecord;
  private Vector viewColumnVO;
  private Vector searchIdName;
  private String defaultView;

  public String getListType()
  {
    return this.listType;
  }

  public void setListType(String listType)
  {
    this.listType = listType;
  }


  public int getNoOfRecord()
  {
    return this.noOfRecord;
  }

  public void setNoOfRecord(int noOfRecord)
  {
    this.noOfRecord = noOfRecord;
  }


  public int getSearchId()
  {
    return this.searchId;
  }

  public void setSearchId(int searchId)
  {
    this.searchId = searchId;
  }


  public String getSortMember()
  {
    return this.sortMember;
  }

  public void setSortMember(String sortMember)
  {
    this.sortMember = sortMember;
  }


  public String getSortType()
  {
    return this.sortType;
  }

  public void setSortType(String sortType)
  {
    this.sortType = sortType;
  }


  public Vector getViewColumnVO()
  {
    return this.viewColumnVO;
  }

  public void setViewColumnVO(Vector viewColumnVO)
  {
    this.viewColumnVO = viewColumnVO;
  }


  public int getViewId()
  {
    return this.viewId;
  }

  public void setViewId(int viewId)
  {
    this.viewId = viewId;
  }


  public String getViewName()
  {
    return this.viewName;
  }

  public void setViewName(String viewName)
  {
    this.viewName = viewName;
  }


  public String getSearchType()
  {
    return this.searchType;
  }

  public void setSearchType(String searchType)
  {
    this.searchType = searchType;
  }


  public Vector getSearchIdName()
  {
    return this.searchIdName;
  }

  public void setSearchIdName(Vector searchIdName)
  {
    this.searchIdName = searchIdName;
  }


  public String getDefaultView()
  {
    return this.defaultView;
  }

  public void setDefaultView(String defaultView)
  {
    this.defaultView = defaultView;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer("ViewVO = \n");
    sb.append("  - viewId = [" + viewId + "]\n");
    sb.append("  - listType = [" + listType + "]\n");
    sb.append("  - viewName = [" + viewName + "]\n");
    sb.append("  - sortMember = [" + sortMember + "]\n");
    sb.append("  - sortType = [" + sortType + "]\n");
    sb.append("  - searchId = [" + searchId + "]\n");
    sb.append("  - searchType = [" + searchType + "]\n");
    sb.append("  - noOfRecord = [" + noOfRecord + "]\n");
    sb.append("  - viewColumnVO = [" + viewColumnVO + "]\n");
    sb.append("  - searchIdName = [" + searchIdName + "]\n");
    sb.append("  - defaultView = [" + defaultView + "]\n");
    return sb.toString();
  }

}

