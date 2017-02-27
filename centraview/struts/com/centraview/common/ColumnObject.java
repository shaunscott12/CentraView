/*
 * $RCSfile: ColumnObject.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:55 $ - $Author: mking_cv $
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
/*
----------------------------------------------------------------------------
Date  : 04-06-2003

Author: .

LastUpdated Date :

-----------------------------------------------------------------------------
*/

package com.centraview.common ;


public class ColumnObject
{

private String columnName ;
private int  displayWidth  ;




public ColumnObject( String columnName  , int  displayWidth  )
{
	this.columnName = columnName;
	this.displayWidth =  displayWidth ;
}


public String getColumnName()
{
	return columnName ;
}


public void setColumnName( String value )
{
	columnName = value ;
}


public int getColumnWidth()
{
	return displayWidth ;
}


public void setColumnWidth( int value )
{
	displayWidth = value ;
}






}