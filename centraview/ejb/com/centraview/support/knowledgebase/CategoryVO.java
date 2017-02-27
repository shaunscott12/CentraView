/*
 * $RCSfile: CategoryVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:10 $ - $Author: mking_cv $
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

package com.centraview.support.knowledgebase;
import java.io.Serializable;

import com.centraview.common.CVAudit;
/*
 * @author Sandip Wadkar
 * @version 1.0
 */

public class CategoryVO extends CVAudit implements Serializable
{
	private int catid;
	private String title;
	private int parent;

	/*
	*	Stores status
	*/
	private String status;

	/*
	*	Stores publishToCustomerView
	*/
	private String publishToCustomerView;

	/**
	 *
	 * @return  The id of Category
	 */
	public int getCatid()
	{
		return this.catid;
	}

	/**
	 *	Set The id of Category
	 *
	 * @param   catid
	 */
	public void setCatid(int catid)
	{
		this.catid = catid;
	}

	/**
	 *
	 * @return  The title of Category
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 *	Set The title of Category
	 *
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 *
	 * @return  The parent id of Category
	 */
	public int getParent()
	{
		return this.parent;
	}

	/**
	 *	Set The parent id of Category
	 *
	 * @param  parent
	 */
	public void setParent(int parent)
	{
		this.parent = parent;
	}


	/**
	 *
	 *
	 * @return The Category ValeObject
	 */
	public CategoryVO getVO()
	{
		return this;
	}

	public CategoryVO()
	{
	super();
	//System.out.println("\nIn Category VO constructor\n");
	}

	/**
	 * get the status
	 *
	 * @return The Category status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 *	Set The status of Category
	 *
	 * @param   status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * get the publishToCustomerView
	 *
	 * @return The Category publishToCustomerView
	 */
	public String getPublishToCustomerView() {
		return this.publishToCustomerView;
	}

	/**
	 *	Set The publishToCustomerView of Category
	 *
	 * @param   publishToCustomerView
	 */
	public void setPublishToCustomerView(String publishToCustomerView) {
		this.publishToCustomerView = publishToCustomerView;
	}

}