/*
 * $RCSfile: Attachment.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:48 $ - $Author: mking_cv $
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
This class is used for storing attachment Data
*/


public class Attachment implements Serializable
{

   /**
   Members of this class
   */
   private int AttachmentID;
   private int MessageID ;
   private int fileID ;
   private String filename;
   private String filepath;

   /**
   constructor
   */
   public Attachment( int AttachmentID,int MessageID,int FileID, String FileName  )
   {
	  this.AttachmentID =AttachmentID ;
	  this.MessageID = MessageID;
	  this.fileID  = FileID ;
	  this.filename = FileName;
   }


	/** get Attachment ID
	*/
	
	public int getAttachmentID()
	{
		return this.AttachmentID;
	}
	
	/** set Attachment ID
	*/
	
	public void setAttachmentID(int AttachmentID)
	{
		this.AttachmentID = AttachmentID;
	}

	/** get file ID
	*/
	public int getFileID()
	{
		return this.fileID;
	}

	/** set file ID
	*/
	public void setFileID(int fileID)
	{
		this.fileID = fileID;
	}

	/** returns file name
	*/
	public String getFilename()
	{
		return this.filename;
	}
	
	/** set file name
	*/
	public void setFilename(String filename)
	{
		this.filename = filename;
	}

	/** get file path
	*/
	public String getFilepath()
	{
		return this.filepath;
	}
	
	/** set file path
	*/
	public void setFilepath(String filepath)
	{
		this.filepath = filepath;
	}

	/** get message id
	*/	
	public int getMessageID()
	{
		return this.MessageID;
	}
	
	/** set message id
	*/	
	public void setMessageID(int MessageID)
	{
		this.MessageID = MessageID;
	}
}
