/*
 * $RCSfile: FileForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:07 $ - $Author: mking_cv $
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

package com.centraview.mail;

import org.apache.struts.upload.FormFile;

/**
 * This class is used for getting file from Struts
 * to webserver
 */
public class FileForm extends org.apache.struts.action.ActionForm
{
	private FormFile file;
	private String[] fileList = null;
	private String fileNameFromCent;

	public FormFile getFile()
	{
		return this.file;
	}

	public void setFile(FormFile file)
	{
		this.file = file;
	}

	public String[] getFileList()
	{
		return this.fileList;
	}

	public void setFileList(String[] fileList)
	{
		this.fileList = fileList;
	}

	public String getFileNameFromCent() {
	  return(this.fileNameFromCent);
	}
	
	public void setfileNameFromCent(String fileNameFromCent) {
	  this.fileNameFromCent = fileNameFromCent;
	}
}

