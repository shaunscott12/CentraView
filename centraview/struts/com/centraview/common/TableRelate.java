/*
 * $RCSfile: TableRelate.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:17 $ - $Author: mking_cv $
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
package com.centraview.common;


public class TableRelate implements java.io.Serializable
{
	private String table1;
	private String field1;	
	private String field2;		
	private String caluse;
	private String othertablename;			
					
	

	public TableRelate(String table1,String field1,String field2,String caluse,String othertablename)
	{
		this.table1 = table1;
		this.field1 = field1;	
		this.field2 = field2;
		this.caluse = caluse;		
		this.othertablename = othertablename;				
	}
	public String getCaluse()
	{
		return this.caluse;
	}

	public void setCaluse(String caluse)
	{
		this.caluse = caluse;
	}

	
	public String getField1()
	{
		return this.field1;
	}

	public void setField1(String field1)
	{
		this.field1 = field1;
	}

	
	public String getTable1()
	{
		return this.table1;
	}

	public void setTable1(String table1)
	{
		this.table1 = table1;
	}

	
	public String getField2()
	{
		return this.field2;
	}

	public void setField2(String field2)
	{
		this.field2 = field2;
	}

	
	public String getOthertablename()
	{
		return this.othertablename;
	}

	public void setOthertablename(String othertablename)
	{
		this.othertablename = othertablename;
	}
}












