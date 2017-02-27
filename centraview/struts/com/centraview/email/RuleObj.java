/*
 * $RCSfile: RuleObj.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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
package com.centraview.email;

public class RuleObj
{
  public  int join;
  public  int field;
  public  int condition;
  public  String criteria;

	 public RuleObj()
	 {
	 }



	public int getCondition()
	{
		return this.condition;
	}

	public void setCondition(int condition)
	{
		this.condition = condition;
	}


	public String getCriteria()
	{
		return this.criteria;
	}

	public void setCriteria(String criteria)
	{
		this.criteria = criteria;
	}


	public int getField()
	{
		return this.field;
	}

	public void setField(int field)
	{
		this.field = field;
	}


	public int getJoin()
	{
		return this.join;
	}

	public void setJoin(int join)
	{
		this.join = join;
	}
}