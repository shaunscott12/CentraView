/*
 * $RCSfile: KnowledgeVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:11 $ - $Author: mking_cv $
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

public class KnowledgeVO extends CVAudit implements Serializable
{
	private int kbid, catid;
	private String title;
	private String detail;
	private CategoryVO categoryDetails;
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
	 * @return  The id of Knowledge base
	 */
	public int getKbid()
	{
		return this.kbid;
	}

	/**
	 *	Set The id of Knowledge base
	 *
	 * @param   kbid
	 */
	public void setKbid(int kbid)
	{
		this.kbid = kbid;
	}

	/**
	 *
	 * @return  The title of Knowledge base
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 *	Set The title of Knowledge base
	 *
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 *
	 * @return  The detail of Knowledge base
	 */
	public String getDetail()
	{
		return this.detail;
	}

	/**
	 *	Set The detail of Knowledge base
	 *
	 * @param   detail
	 */
	public void setDetail(String detail)
	{
		this.detail = detail;
	}


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
	 * @return  The category of Knowledge base
	 */
	public CategoryVO getCategory()
	{
		return this.categoryDetails;
	}
	/**
	 *	Set The category of Knowledge base
	 *
	 * @param   category
	 */
	public void setCategory(CategoryVO categoryDetails)
	{
		this.categoryDetails = categoryDetails;
	}
	/**
	 *
	 *
	 * @return The Knowledge ValeObject
	 */
	public KnowledgeVO getVO()
	{
		return this;
	}


	/**
	 * get the parent
	 *
	 * @return The Knowledge ValeObject
	 */

	public int getParent()
	{
		return this.parent;
	}

	/**
	 *	Set The parent of Knowledge base
	 *
	 * @param   parent
	 */
	public void setParent(int parent)
	{
		this.parent = parent;
	}

	/**
	 * get the status
	 *
	 * @return The Knowledge status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 *	Set The status of Knowledge base
	 *
	 * @param   status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * get the publishToCustomerView
	 *
	 * @return The Knowledge publishToCustomerView
	 */
	public String getPublishToCustomerView() {
		return this.publishToCustomerView;
	}

	/**
	 *	Set The publishToCustomerView of Knowledge base
	 *
	 * @param   publishToCustomerView
	 */
	public void setPublishToCustomerView(String publishToCustomerView) {
		this.publishToCustomerView = publishToCustomerView;
	}
}
