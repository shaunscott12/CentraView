/*
 * $RCSfile: SearchObject.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:16 $ - $Author: mking_cv $
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

public class SearchObject implements java.io.Serializable 
{
	private int intSearchId;
	private int intTableId;
	private int intModuleId;
	
	private int intOwner;
	private int intCreatedBy;	
	private int intModifiedBy;
	
	private String strSearchName;
	private String strSearchString;
		
		
	public int getIntModuleId()
	{
		return this.intModuleId;
	}

	public void setIntModuleId(int intModuleId)
	{
		this.intModuleId = intModuleId;
	}

	
	public int getIntSearchId()
	{
		return this.intSearchId;
	}

	public void setIntSearchId(int intSearchId)
	{
		this.intSearchId = intSearchId;
	}

	
	public int getIntTableId()
	{
		return this.intTableId;
	}

	public void setIntTableId(int intTableId)
	{
		this.intTableId = intTableId;
	}


	public String getStrSearchString()
	{
		return this.strSearchString;
	}

	public void setStrSearchString(String strSearchString)
	{
		this.strSearchString = strSearchString;
	}

	
	public String getStrSearchName()
	{
		return this.strSearchName;
	}

	public void setStrSearchName(String strSearchName)
	{
		this.strSearchName = strSearchName;
	}

	
	public int getIntOwner()
	{
		return this.intOwner;
	}

	public void setIntOwner(int intOwner)
	{
		this.intOwner = intOwner;
	}

	
	public int getIntModifiedBy()
	{
		return this.intModifiedBy;
	}

	public void setIntModifiedBy(int intModifiedBy)
	{
		this.intModifiedBy = intModifiedBy;
	}

	
	public int getIntCreatedBy()
	{
		return this.intCreatedBy;
	}

	public void setIntCreatedBy(int intCreatedBy)
	{
		this.intCreatedBy = intCreatedBy;
	}
}
