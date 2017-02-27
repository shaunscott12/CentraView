/*
 * $RCSfile: CvFileException.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:22 $ - $Author: mking_cv $
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
 * CvFileException.java
 *
 * @author   Iqbal Khan
 * @version  1.0    
 * 
 */




package com.centraview.file;




public class CvFileException extends java.lang.Exception
{
	private int exceptionId;
	private String exceptionDescription;
	
	public static final int OTHER_EXCEPTION = 0;
	public static final int INSERT_FAILED = 1;
	public static final int UPDATE_FAILED = 2;
	public static final int GET_FAILED = 3;
	public static final int DELETE_FAILED = 4;
	public static final int INVALID_DATA = 5;
	public static final int PARENT_NOT_FOUND = 6;
	public static final int COULD_NOT_CREATE_DIRECTORY = 7;
	public static final int COULD_NOT_CREATE_FILE = 8;
	public static final int COULD_NOT_RENAME_FILE = 9;



	/**
	 * constructor
	 *
	 * @param   exceptionId  
	 * @param   exceptionDescrription  
	 */
	public CvFileException(int exceptionId,String exceptionDescription)
	{
		this.exceptionDescription = exceptionDescription;
		this.exceptionId = exceptionId;
	}
	

	/**
	 * get the description of exception
	 *
	 * @return   String  
	 */
	public String getExceptionDescription()
	{
		return this.exceptionDescription;
	}
	


	/**
	 * sets the description 
	 *
	 * @param   exceptionDescription  
	 */
	public void setExceptionDescription(String exceptionDescription)
	{
		this.exceptionDescription = exceptionDescription;
	}

	

	/**
	 * get exception id
	 *
	 * @return    int 
	 */
	public int getExceptionId()
	{
		return this.exceptionId;
	}


	/**
	 * set the exception ID
	 *
	 * @param   exceptionId  
	 */
	public void setExceptionId(int exceptionId)
	{
		this.exceptionId = exceptionId;
	}
  
  public String toString()
  {
    String string = "CVFileException: "+this.exceptionDescription;
    return string;
  }
}
