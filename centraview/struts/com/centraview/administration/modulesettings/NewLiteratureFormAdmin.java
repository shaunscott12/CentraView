/*
 * $RCSfile: NewLiteratureFormAdmin.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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
/**
 * @author praveen
 * Date 31 st Dec 2003 
 */

package com.centraview.administration.modulesettings;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import com.centraview.administration.common.AdministrationConstantKeys;

/**
 * This Form class written to set the values that required in Saving New Literature 
 */

public class NewLiteratureFormAdmin extends org.apache.struts.action.ActionForm {

	//------------------------------------------------------- Variables
	//To store name
	private String fileName = null;
	//to store the file
	private FormFile file = null;
	//to store the  FileID 
	private int fileID = 0;
	//To store the LiteratureName
	private String literatureName = null;
	//To store the literatureID if any
	private int literatureID = 0;
	

	//	===================================================== Methods

	/**
	 * Return the File
	 * @return file
	 */
	public FormFile getFile() {
		return file;
	}
	/**
	 * Sets the file
	 * @param file
	 */
	public void setFile(FormFile file) {
		this.file = file;
	}
	
	/**
	 * Returns the FileID 
	 * @return the FileID
	 */
	public int getFileID() {
		return fileID;
	}

	/**
	 * Sets the fileID
	 * @param fileID
	 */
	public void setFileID(int fileID) {
		this.fileID = fileID;
	}

	/**
	 * Rretuns the fileName
	 * @return fileName
	 */
	public String getFileName() {
		return fileName ;
	}

	/**
	 * Returns the LiteratureName
	 * @return the Literature Name
	 */
	public String getLiteratureName() {
		return literatureName ;
	}

	/**
	 * Sets the fileName
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Sets the Literature Name
	 * @param literatureName 
	 */
	public void setLiteratureName(String literatureName ) {
		this.literatureName = literatureName ;
	}
	
	/**
	 * Returns the LiteratureID
	 * @return literatureID
	 */
	public int getLiteratureID() {
		return literatureID;
	}

	/**
	 * Sets the literatureID 
	 * @param literatureID
	 */
	public void setLiteratureID(int literatureID) {
		this.literatureID = literatureID;
	}

	
	//------------------------------------------------------------------------ validate method 
	 /**
	   * Validate method to validate the password and confirm password
	   * @param ActionMapping mapping
	   * @param HttpServletRequest request
	   * @return ActionErrors
	   */
	  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	  {
	  	  System.out.println("In validate");
		  // initialize new ActionError object
		  ActionErrors errors = new ActionErrors();
		  try 
		  {
			
			  request.setAttribute("typeofmodule",AdministrationConstantKeys.MODULESETTINGS);
			  request.setAttribute("typeofsubmodule","marketing");
			
			  //If Name is not properly selected throw the error
			  if ((literatureName == null) || !(literatureName .length()>0)) {
				  errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Literature"));
			  }
			
			  //Set the attributes
		  }
		  catch (Exception e)	
		  {
			  e.printStackTrace();
		  }
		  // retutn the error object
		  return errors;	
	  } // end of validate
	  
	

	
}
