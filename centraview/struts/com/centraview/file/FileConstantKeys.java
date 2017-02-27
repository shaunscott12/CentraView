/*
 * $RCSfile: FileConstantKeys.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:53 $ - $Author: mking_cv $
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
 *	This file is used for defining constants
 * 	for file module.
 *	@author : Ashwin Nagar
 *
 */

// package name
package com.centraview.file;

public class FileConstantKeys {
	/*
	 *	Indicate type of file attribute.
	 *	File attribute can be file or folder
	 */
    public static final String TYPEOFFILE = "TYPEOFFILE";
	/*
	 *	Indicate file string
	 */
    public static final String FILE = "FILE";
	/*
	 *	Indicate folder string
	 */
    public static final String FOLDER = "FOLDER";
	/*
	 *	Indicate type of operation
	 *	Operation can be of add, edit, delete
	 */
	public static final String TYPEOFOPERATION = "TYPEOFOPERATION";
	/*
	 *	Indicate add string
	 */
	public static final String ADD = "ADD";
	/*
	 *	Indicate edit string
	 */
	public static final String EDIT = "EDIT";
	/*
	 *	Indicate id attached to particular open window
	 *	Window id is used in case multiple windows are opened
	 */
	public static final String WINDOWID = "WINDOWID";
	/*
	 *	Indicate a hashmap to store formbean containing user input data
	 *	during add operation of file / folder.
	 */
	public static final String NEWFFHASHMAP = "NEWFFHASHMAP";
	/*
	 *	Indicate a hashmap to store formbean containing user input data
	 *	during edit operation of fle / folder.
	 */
	public static final String EDITFFHASHMAP = "EDITFFHASHMAP";
	/*
	 *	Indicate all string
	 */
	public static final String ALL = "ALL";
	/*
	 *	Indicate my string
	 */
	public static final String MY = "MY";
	/*
	 *	Indicate tab on user is present.
	 *	Tab under file / folder contains Details and Permission tabs
	 */
	public static final String CURRENTTAB = "CURRENTTAB";
	/*
	 *	Indicate detail string tab
	 */
	public static final String DETAIL = "DETAIL";
	/*
	 *	Indicate permission string tab
	 */
	public static final String PERMISSION = "PERMISSION";
	/*
	 *	Indicate file / folder id
	 */
	public static final String FFID = "FFID";

	public static final String DUPLICATE = "DUPLICATE";

	public static final String TYPEOFFILELIST = "TYPEOFFILELIST";

	public static final String DEFAULTACCESS = "PRIVATE";

	public static final String DEFAULTCUSTOMERVIEW = "NO";

	public static final String DEFAULTCOMPANYNEWS = "NO";
}
