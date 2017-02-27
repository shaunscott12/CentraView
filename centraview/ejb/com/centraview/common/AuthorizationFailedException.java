/*
 * $RCSfile: AuthorizationFailedException.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:53 $ - $Author: mking_cv $
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
 * NoteException.java
 *
 * @author   Iqbal Khan
 * @version  1.0
 *
 */
package com.centraview.common;

public class AuthorizationFailedException extends Exception
{
	private int exceptionId;
	private String exceptionDescription;

	/**
	 * Constructor
	 *
	 * @param   exceptionId
	 * @param   exceptionDescrription
	 */
	public AuthorizationFailedException(int exceptionId,String exceptionDescription)
	{
		this.exceptionDescription = exceptionDescription;
		this.exceptionId = exceptionId;
	}

	/**
	 * Constructor
	 *
	 * @param   exceptionId
	 * @param   exceptionDescrription
	 */
	public AuthorizationFailedException(String exceptionDescription)
	{
		this.exceptionDescription = exceptionDescription;
		System.out.println("[Authorization Exception] " + exceptionDescription);
	}

	/**
	 * gets the Exception Description
	 *
	 * @return String
	 */


	public String getExceptionDescription()
	{
		return this.exceptionDescription;
	}



	/**
	 * sets the Exception description
	 *
	 * @param   exceptionDescription
	 */
	public void setExceptionDescription(String exceptionDescription)
	{
		this.exceptionDescription = exceptionDescription;
	}



	/**
	 * gets the Exception Id
	 *
	 * @return  int
	 */
	public int getExceptionId()
	{
		return this.exceptionId;
	}


	/**
	 * sets the exception Id
	 *
	 * @param   exceptionId
	 */
	public void setExceptionId(int exceptionId)
	{
		this.exceptionId = exceptionId;
	}
}
