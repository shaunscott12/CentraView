/*
 * $RCSfile: ViewColumnVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:16 $ - $Author: mking_cv $
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

import java.io.Serializable;

public class ViewColumnVO implements Serializable
{
  private int viewId;
  private String columnName;
  private int columnOrder;

  public String getColumnName()
  {
    return this.columnName;
  }

  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }

  public int getColumnOrder()
  {
    return this.columnOrder;
  }

  public void setColumnOrder(int columnOrder)
  {
    this.columnOrder = columnOrder;
  }

  public int getViewId()
  {
    return this.viewId;
  }

  public void setViewId(int viewId)
  {
    this.viewId = viewId;
  }
  
}

