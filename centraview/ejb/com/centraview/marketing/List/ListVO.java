/*
 * $RCSfile: ListVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:38 $ - $Author: mking_cv $
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


package com.centraview.marketing.List;

import java.io.Serializable;

public class ListVO implements Serializable
{
	private int listID;
	private int ownerID;
	private String description;
	private String title;
	private String created;
	private String modified;
	private String ownerName;

	/**
	 *
	 * @return   The listID
	 */
	public int getListID()
	{
		return this.listID;
	}


	/**
	 *	Set the listID
	 *
	 * @param   listID  The new listID
	 */
	public void setListID(int listID)
	{
		this.listID = listID;
	}

	/**
	 *
	 * @return   The owner ID
	 */
	public int getOwnerID()
	{
		return this.ownerID;
	}


	/**
	 *	Set the Owner ID
	 *
	 * @param   ownerId  The new owner ID
	 */
	public void setOwnerID(int ownerID)
	{
		this.ownerID = ownerID;
	}

	/**
	* @return Description of List.
	*/
	public String getDescription()
	{
		return this.description;
	}

	/**
	* Set the Description of List.
	* @param description.
	*/
	public void setDescription(String description)
	{
		this.description = description;
	}


	/**
	* @return title of List.
	*/
	public String getTitle()
	{
		return this.title;
	}

	/**
	* Set the title  of List.
	* @param   title.
	*/
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return Created By of List.
	 */
	public String getCreated()
	{
		return this.created;
	}

	/**
	 * Set the Created By  of List.
	 * @param   created.
	 */
	public void setCreated(String created)
	{
		this.created = created;
	}

	/**
	 * @return Modified By of List.
	 */
	public String getModified()
	{
		return this.modified;
	}

	/**
	 * Set the Modified By  of List.
	 * @param   modified.
	 */
	public void setModified(String modified)
	{
		this.modified = modified;
	}

	/**
	 * @return Owner Name of List.
	 */
	public String getOwnerName()
	{
		return this.ownerName;
	}

	/**
	 * Set the Owner Name of List.
	 * @param   ownerName  First Name of List.
	 */
	public void setOwnerName(String ownerName)
	{
		this.ownerName = ownerName;
	}
}
