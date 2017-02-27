/*
 * $RCSfile: LiteratureVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:47 $ - $Author: mking_cv $
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
 * Created on Jan 3, 2004
 * 
 * 
 *  
 */
package com.centraview.administration.modulesettings;

import java.io.Serializable;

/**
 * @author praveen
 *
 * This class will store the Literature Information
 * 
 */
public class LiteratureVO implements Serializable {
	
	//----------------------------------------- variables
	
	//To store the literature ID
	private int literatureID = 0;
	//To store the Literature Title	
	private String literatureTitle = null;
	//To store the FileID 
	private int fileID = 0;
	//To store the File Name
	private String fileName = null;
		
	
	//---------------------------------------------- Methods
	 
	/**
	 * Returns the FileID
	 * @return fileID
	 */
	public int getFileID() {
		return fileID;
	}

	/**
	 * Retuns the FileName associated with Literature Name
	 * @return FileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Returns teh LiteratureID
	 * @return LiteratureID
	 */
	public int getLiteratureID() {
		return literatureID;
	}

	/**
	 * Returns the Literature Title 
	 * @return Literature Title
	 */
	public String getLiteratureTitle() {
		return literatureTitle;
	}

	/**
	 * Returns the FileID
	 * @param fileID
	 */
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}

	/**
	 * Sets the FileName.
	 * @param FileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Sets the literatureID
	 * @param literatureID
	 */
	public void setLiteratureID(int literatureID) {
		this.literatureID = literatureID;
	}

	/**
	 * Sets the literatureTitle
	 * @param literatureTitle
	 * 
	 */
	public void setLiteratureTitle(String literatureTitle) {
		this.literatureTitle = literatureTitle;
	}
	
}
