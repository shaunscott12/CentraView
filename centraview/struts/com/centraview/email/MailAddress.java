/*
 * $RCSfile: MailAddress.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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
import java.io.Serializable;

/**
This class stores Accout Information
*/

public class MailAddress implements Serializable
{
 
	private String name;
	private int  id ;
	private String address ;
	private boolean isGroup = false ;	

	/**
	constructor
	*/
	public MailAddress( String str ) 
	{
		this.address = str;
	}

	/**
	returns address 
	*/
	public String getAddress()
	{
		return this.address;
	}
	
	/**
	sets address 
	*/
	public void setAddress(String address)
	{
		this.address = address;
	}

	/**
	returns AccountID 
	*/
	public int getId()
	{
		return this.id;
	}
	
	/**
	sets AccountID 
	*/
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	returns TRUE if the address is groups address
	*/

	public boolean getIsGroup()
	{
		return this.isGroup;
	}
	
	/**
	SETS  TRUE if the address is groups address
	*/
	public void setIsGroup(boolean isGroup)
	{
		this.isGroup = isGroup;
	}
	
	/**
	* returns name 
	*/

	public String getName()
	{
		return this.name;
	}
	
	/**
	* sets name 
	*/
	public void setName(String name)
	{
		this.name = name;
	}
}
