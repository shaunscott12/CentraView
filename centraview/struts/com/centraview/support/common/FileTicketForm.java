/*
 * $RCSfile: FileTicketForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:46 $ - $Author: mking_cv $
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
 *	FileForm.java
 * 	@author Himansu Sekhar
 * 	All fields related to activity are defined in this class.
 *  Validation is done at the time of save and edit of activity.
 */

package com.centraview.support.common;

import java.util.Vector;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;
 

public class FileTicketForm extends ActionForm{

	// attachment	
	private String fileNameFromCent;
	private String fileId;
	private FormFile file;
	private String[] filelist;
	private Vector filelistvec;

	public FormFile getFile() {
		return this.file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}


	public String getFileId() {
		return this.fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}


	public String getFileNameFromCent() {
		return this.fileNameFromCent;
	}

	public void setFileNameFromCent(String fileNameFromCent) {
		this.fileNameFromCent = fileNameFromCent;
	}


	public String[] getFilelist() {
		return this.filelist;
	}

	public void setFilelist(String[] filelist) {
		this.filelist = filelist;
	}


	public Vector getFilelistvec() {
		return this.filelistvec;
	}

	public void setFilelistvec(Vector filelistvec) {
		this.filelistvec = filelistvec;
	}
}
