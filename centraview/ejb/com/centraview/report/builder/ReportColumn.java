/*
 * $RCSfile: ReportColumn.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:57 $ - $Author: mking_cv $
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

package com.centraview.report.builder;

/**
 *
 * <p>Title: ReportColumn class </p>
 * <p>Description: Class for representation of standard report column</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class ReportColumn {
  String colName = "";
  boolean visible = true;

  public ReportColumn(String name,boolean visible) {
    this.colName = name;
    this.visible = visible;
  }

  public String getColName() {
    return colName;
  }
  public boolean isVisible() {
    return visible;
  }
  public void setColName(String colName) {
    this.colName = colName;
  }
  public void setVisible(boolean visible) {
    this.visible = visible;
  }
}
