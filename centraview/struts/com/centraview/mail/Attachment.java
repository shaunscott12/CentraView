/*
 * $RCSfile: Attachment.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:05 $ - $Author: mking_cv $
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

import java.io.Serializable;

public class Attachment implements Serializable
{
  private int attachmentID;
  private int messageID ;
  private int fileID ;
  private String fileName;
  private String filePath;
  
  public Attachment(int attachmentID, int messageID, int fileID, String fileName)
  {
    this.attachmentID = attachmentID ;
    this.messageID = messageID;
    this.fileID  = fileID;
    this.fileName = fileName;
  }
  
  public int getAttachmentID()
  {
    return this.attachmentID;
  }
	
	public void setAttachmentID(int attachmentID)
	{
		this.attachmentID = attachmentID;
	}

	public int getFileID()
	{
		return this.fileID;
	}

	public void setFileID(int fileID)
	{
		this.fileID = fileID;
	}

	public String getFileName()
	{
		return this.fileName;
	}
	
	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public String getFilePath()
	{
		return this.filePath;
	}
	
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}

	public int getMessageID()
	{
		return this.messageID;
	}
	
	public void setMessageID(int messageID)
	{
		this.messageID = messageID;
	}

}

