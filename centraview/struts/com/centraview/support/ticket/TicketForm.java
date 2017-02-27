/*
 * $RCSfile: TicketForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
 *	This file is used for storing user input data
 * 	during adding or editing of file.
 *	@author : Prasanta Sinha
 *
 */

package com.centraview.support.ticket;

// java import package
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.Validation;

public class TicketForm extends org.apache.struts.action.ActionForm {

	/*
	 *	Stores subject
	 */
	private String id;
	/*
	 *	Stores subject
	 */
	private String subject;

	private String managerid;
	/*
	 *	Stores detail
	 */
	private String detail;
	/*
	 *	Stores status
	 */
	private String status;
	/*
	 *	Stores priority
	 */
	private String priority;

	/*
	 *	Stores entiry
	 */
	private String entityname;
	private String entityid;
	/*
	 *	Stores contact
	 */
	private String contact;
	private String contactid;
	/*
	 *	Stores managername
	 */
	private String managername;

	/*
	 *	Stores assignedto
	 */
	private String assignedto;

	private String assignedtoid;
	/*
	 *	Stores modified
	 */
	private String modified;

	private String phone;

	private String email;

	private String address;
	private String website;

	public String getId()
	{
		return this.id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getSubject()
	{
		return this.subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}


	public String getDetail()
	{
		return this.detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}


	public String getStatus()
	{
		return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getPriority()
	{
		return this.priority;
	}

	public void setPriority(String priority)
	{
		this.priority = priority;
	}

	public String getEntityname()
	{
		return this.entityname;
	}

	public void setEntityname(String entityname)
	{
		this.entityname = entityname;
	}


	public String getContact()
	{
		return this.contact;
	}

	public void setContact(String contact)
	{
		this.contact = contact;
	}

	public String getManagername()
	{
		return this.managername;
	}

	public void setManagername(String managername)
	{
		this.managername = managername;
	}

	public String getAssignedto()
	{
		return this.assignedto;
	}

	public void setAssignedto(String assignedto)
	{
		this.assignedto = assignedto;
	}

   /*
	 *	Validates user input data
	 *	@param mapping ActionMapping
	 *	@param request HttpServletRequest
	 *	@return errors ActionErrors
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();

		try {
			// initialize validation
			Validation validation = new Validation();

			// subject
      if (this.getSubject() == null || this.getSubject().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Subject"));
      }

			// status
      if (this.getStatus() == null || this.getStatus().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Status"));
      }

			//priority
      if (this.getPriority() == null || this.getPriority().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Priority"));
      }

			if (errors != null) {
				request.setAttribute("ticketform", this);
			}

			request.setAttribute("ticketform", this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errors;
	}



	public String getPhone()
	{
		if (this.phone == null || this.phone.equals("null")){
			this.phone = "";
		}
		return this.phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}


	public String getEmail()
	{
		if (this.email == null || this.email.equals("null")){
			this.email = "";
		}
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}


	public String getAssignedtoid()
	{
		return this.assignedtoid;
	}

	public void setAssignedtoid(String assignedtoid)
	{
		this.assignedtoid = assignedtoid;
	}


	public String getContactid()
	{
		return this.contactid;
	}

	public void setContactid(String contactid)
	{
		this.contactid = contactid;
	}


	public String getEntityid()
	{
		return this.entityid;
	}

	public void setEntityid(String entityid)
	{
		this.entityid = entityid;
	}


	public String getManagerid()
	{
		return this.managerid;
	}

	public void setManagerid(String managerid)
	{
		this.managerid = managerid;
	}

	public String getAddress()
	{
		if (this.address == null){
			this.address = "";
		}
		this.address = this.address.replaceAll("\n","<br>");
		return this.address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getWebsite()
	{
		return this.website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}
}
