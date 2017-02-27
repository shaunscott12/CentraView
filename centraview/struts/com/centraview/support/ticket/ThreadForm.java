/*
 * $RCSfile: ThreadForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
 *	This file is used for storing user input data
 * 	during adding or editing of Thread.
 *	@author : Sandip Wadkar
 *  @date : 29 Aug. 03
 *
 **/

package com.centraview.support.ticket;

// java import package
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.Validation;


public class ThreadForm extends org.apache.struts.action.ActionForm {

	/*
	 *	Stores thread id
	 */
	private String threadId;
	/*
	 *	Stores title
	 */
	private String title;
	/*
	 *	Stores detail
	 */
	private String detail;
	/*
	 *	Stores priorityid
	 */
	private int priorityid;
	/*
	 *	Stores priority name
	 */
	private String priorityname;
	/*
	 *	Stores created id
	 */
	private String createdid;	
	/*
	 *	Stores created by info
	 */
	private String createdby;
	/*
	 *	Stores created date
	 */
	private String createddate;
	/*
	 *	Stores modified date
	 */
	private String modifieddate;

	/*
	 *	Validates user input data
	 *	@param mapping ActionMapping
	 *	@param request HttpServletRequest
	 *	@return errors ActionErrors
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request) {

		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();

		try {
			// initialize validation
			System.out.println("inside Validation 833333333333333333");
			Validation validation = new Validation();	

			// title
      if (this.getTitle() == null || this.getTitle().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Title"));
      }

			if (errors != null) {
				request.setAttribute("bodycontent", "editthread");
				request.setAttribute(ThreadConstantKeys.CURRENTTAB, ThreadConstantKeys.DETAIL);
				request.setAttribute(ThreadConstantKeys.TYPEOFOPERATION, request.getParameter(ThreadConstantKeys.TYPEOFOPERATION));
				request.setAttribute("threadform", this);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return errors;	
	}


	/*	
	 *	Returns createdby
	 *	@return createdby String	
	 */
	public String getCreatedby() {
		return this.createdby;
	}

	/*	
	 *	Set created by
	 *	@param createdby String	
	 */
	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	/*	
	 *	Returns createddate
	 *	@return createddate String	
	 */
	public String getCreateddate() {
		return this.createddate;
	}

	/*	
	 *	Set createddate
	 *	@param createddate String	
	 */
	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}



	/*	
	 *	Returns detail
	 *	@return detail String	
	 */
	public String getDetail() {
		return this.detail;
	}

	/*	
	 *	Set detail
	 *	@param detail String	
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/*	
	 *	Returns priorityId
	 *	@return priorityId String	
	 */
	public int getPriorityId() {
		return this.priorityid;
	}

	/*	
	 *	Set priorityId
	 *	@param priorityId String	
	 */
	public void setPriorityId(int priorityId) {
		this.priorityid = priorityId;
	}

	/*	
	 *	Returns priorityname
	 *	@return priorityname String	
	 */
	public String getPriorityName() {
		return this.priorityname;
	}

	/*	
	 *	Set priorityname
	 *	@param priorityname String	
	 */
	public void setPriorityName(String priorityName) {
		this.priorityname = priorityName;
	}


	/*	
	 *	Returns modifieddate
	 *	@return modifieddate String	
	 */
	public String getModifieddate() {
		return this.modifieddate;
	}

	/*	
	 *	Set modifieddate
	 *	@param modifieddate String	
	 */
	public void setModifieddate(String modifieddate) {
		this.modifieddate = modifieddate;
	}


	/*	
	 *	Returns threadid string
	 *	@return threadid String	
	 */
	public String getThreadId() {
		return this.threadId;
	}

	/*	
	 *	Set threadid string
	 *	@param threadid String	
	 */
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	/*	
	 *	Return title string
	 *	@return title String	
	 */
	public String getTitle() {
		return this.title;
	}

	/*	
	 *	Set title string
	 *	@param title String	
	 */
	public void setTitle(String title) {
		this.title = title;
	}


	/*	
	 *	Return createdid string
	 *	@return createdid String	
	 */
	public String getCreatedid() {
		return this.createdid;
	}

	/*	
	 *	Set createdid string
	 *	@param createdid String	
	 */
	public void setCreatedid(String createdid) {
		this.createdid = createdid;
	}
}














