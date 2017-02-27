/*
 * $RCSfile: KnowledgeBaseForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.Validation;

public class KnowledgeBaseForm extends org.apache.struts.action.ActionForm {

	/*
	*	Stores title
	*/
	private String title;

	/*
	*	Stores detail
	*/
	private String detail;
	/*
	*	Stores category
	*/
	private String category;
	/*
	*	Stores categoryname
	*/
	private String categoryname;

	private String rowId;

	/*
	*	Stores parentcategory
	*/
	private String parentcategory;
	/*
	*	Stores owner
	*/
	private String owner;
	/*
	*	Stores created
	*/
	private String created;
	/*
	*	Stores modified
	*/
	private String modified;

	/*
	*	Stores status
	*/
	private String status;

	/*
	*	Stores publishToCustomerView
	*/
	private String publishToCustomerView;

	/*
	*	Validates user input data
	*	@param mapping ActionMapping
	*	@param request HttpServletRequest
	*	@return ActionErrors
	*/
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{

		ActionErrors errors = new ActionErrors();

		try
		{
			// initialize validation
			Validation validation = new Validation();

      if (this.getTitle() == null || this.getTitle().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Title"));
      }

			request.setAttribute("kbform", this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return errors;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	public String getCategoryname() {
		return this.categoryname;
	}

	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}


	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}


	public String getDetail() {
		return this.detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}


	public String getModified() {
		return this.modified;
	}

	public void setModified(String modified) {
		this.modified = modified;
	}


	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}


	public String getParentcategory() {
		return this.parentcategory;
	}

	public void setParentcategory(String parentcategory) {
		this.parentcategory = parentcategory;
	}


	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	public String getRowId() {
		return this.rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPublishToCustomerView() {
		return this.publishToCustomerView;
	}

	public void setPublishToCustomerView(String publishToCustomerView) {
		this.publishToCustomerView = publishToCustomerView;
	}

}














