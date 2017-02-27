/*
 * $RCSfile: FileForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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
 * FolderForm.java
 */

package com.centraview.email;

import org.apache.struts.upload.FormFile;

/**
This class is used for getting file from Struts 
to webserver
*/
public class FileForm extends org.apache.struts.action.ActionForm {


    /** member variable for FormFile
	*/
	private FormFile file;

	
	/** get member variable for FormFile
	*/

	public FormFile getFile()
	{
		return this.file;
	}
	
	/** set member variable for FormFile
	*/
	public void setFile(FormFile file)
	{
		System.out.println( " FormFile ");
		this.file = file;
	}



}

