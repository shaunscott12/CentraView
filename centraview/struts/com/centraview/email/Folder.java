/*
 * $RCSfile: Folder.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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
public class Folder implements Serializable
{
 private String foldername;
 private String parentname;
 int folderid ;
 int parentfolderid;
 String ftype;
 int unreadMessage ;
 int allMessage ;



  public Folder( String foldername ,int folderid , int parentfolderid ,String ftype ,int all , int unread , String parentname)
  {
	this.foldername = foldername;
	this.parentname = parentname;
	this.folderid = folderid;
    this.parentfolderid = parentfolderid;
	this.ftype = ftype;
	this.allMessage = all;
	this.unreadMessage = unread;

  }





	public int getFolderid()
	{
		return this.folderid;
	}

	public void setFolderid(int folderid)
	{
		this.folderid = folderid;
	}


	public String getParentname()
	{
		return this.parentname;
	}

	public void setParentname(String parentname)
	{
		this.parentname = parentname;
	}


	public String getFoldername()
	{
		return this.foldername;
	}

	public void setFoldername(String foldername)
	{
		this.foldername = foldername;
	}


	public int getParentfolderid()
	{
		return this.parentfolderid;
	}

	public void setParentfolderid(int parentfolderid)
	{
		this.parentfolderid = parentfolderid;
	}


	public String getFtype()
	{
		return this.ftype;
	}

	public void setFtype(String ftype)
	{
		this.ftype = ftype;
	}


	public int getAllMessage()
	{
		return this.allMessage;
	}

	public void setAllMessage(int all)
	{
		this.allMessage = all;
	}


	public int getUnreadMessage()
	{
		return this.unreadMessage;
	}

	public void setUnreadMessage(int unreadMessage)
	{
		this.unreadMessage = unreadMessage;
	}
}