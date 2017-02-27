/*
 * $RCSfile: PopulateThreadForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:52 $ - $Author: mking_cv $
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
 *	Populate Thread Form
 *  @author : Sandip Wadkar
 *  @date : 29 Aug. 03  
 */

package com.centraview.support.ticket;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;

public class PopulateThreadForm {

	public PopulateThreadForm() {
		// TODO: Write constructor body
	}

	public void setForm (HttpServletRequest request, HttpServletResponse response, ActionForm form) {
		try {
			System.out.println("PopulateThreadForm:1:setForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap threadHashMap = null;
			// file form from session
			ThreadForm sessionForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(ThreadConstantKeys.WINDOWID);
			System.out.println("1");			
			if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.ADD)) {
				// hash map for add folder
				threadHashMap = (HashMap) session.getAttribute(ThreadConstantKeys.NEWTHREADHASHMAP);
				// get form from hashmap
				sessionForm = (ThreadForm) threadHashMap.get(windowId);
			}
			else if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.EDIT)) {
				// hash map for edit activity
				threadHashMap = (HashMap) session.getAttribute(ThreadConstantKeys.EDITTHREADHASHMAP);
				// get form from hashmap
				sessionForm = (ThreadForm) threadHashMap.get(windowId);
			}

			// get previous form values	
			ThreadForm previousForm = (ThreadForm) form;

			// set activity id if present
			if (previousForm.getThreadId() != null)
				sessionForm.setThreadId(previousForm.getThreadId());			

			// set form for previous permission values
			if (ThreadConstantKeys.DETAIL.equals(""))//(previousTab))	
			{
				sessionForm.setTitle(previousForm.getTitle());
				sessionForm.setDetail(previousForm.getDetail());
				sessionForm.setPriorityId(previousForm.getPriorityId());
				sessionForm.setPriorityName(previousForm.getPriorityName());
			}

			// set the form back in session
			if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.ADD)) {			
				threadHashMap.put(windowId, sessionForm);
				session.setAttribute(ThreadConstantKeys.NEWTHREADHASHMAP, threadHashMap);
			}
			else if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.EDIT)) {
				threadHashMap.put(windowId, sessionForm);
				session.setAttribute(ThreadConstantKeys.EDITTHREADHASHMAP, threadHashMap);
			}
		}
		catch (Exception e) {
			e.printStackTrace();		
		}
		finally {
			System.out.println("PopulateThreadForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
		}
	}

	public ActionForm getForm (HttpServletRequest request, HttpServletResponse response, ActionForm form, String currentTab) {
		try {
			System.out.println("PopulateNoteForm::getForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap threadHashMap = null;
			// form from session
			ThreadForm sessionForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(ThreadConstantKeys.WINDOWID);

			if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.ADD)) {
				// hash map for add activity
				threadHashMap = (HashMap) session.getAttribute(ThreadConstantKeys.NEWTHREADHASHMAP);
				// get form from hashmap
				sessionForm = (ThreadForm) threadHashMap.get(windowId);
			}
			else if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.EDIT)) {
				// hash map for edit activity
				threadHashMap = (HashMap) session.getAttribute(ThreadConstantKeys.EDITTHREADHASHMAP);
				// get form from hashmap
				sessionForm = (ThreadForm) threadHashMap.get(windowId);
			}

			// get previous form values	
			ThreadForm currentForm = (ThreadForm) form;

			// set activity id if present
			if (sessionForm.getThreadId() != null)
				currentForm.setThreadId(sessionForm.getThreadId());			

			// set form for previous tab values
			// set detail
			currentForm.setTitle(sessionForm.getTitle());
			currentForm.setDetail(sessionForm.getDetail());
			currentForm.setPriorityId(sessionForm.getPriorityId());
			currentForm.setPriorityName(sessionForm.getPriorityName());



			// update request
			request.setAttribute(ThreadConstantKeys.CURRENTTAB, currentTab);			
			request.setAttribute(ThreadConstantKeys.TYPEOFOPERATION, request.getParameter(ThreadConstantKeys.TYPEOFOPERATION));
			request.setAttribute(ThreadConstantKeys.WINDOWID,request.getParameter(ThreadConstantKeys.WINDOWID));
			request.setAttribute("fileform", form);							

			System.out.println("PopulateThreadForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
		}
		catch (Exception e) {
			e.printStackTrace();		
		}
		return form;
	}

	// reset form
	public void resetForm (HttpServletRequest request, HttpServletResponse response, ActionForm form) {
		try {
			System.out.println("PopulateNoteForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");	

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap threadHashMap = null;
			// form from session
			ThreadForm resetForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(ThreadConstantKeys.WINDOWID);

			if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.ADD)) {
				// hash map for add activity
				threadHashMap = (HashMap) session.getAttribute(ThreadConstantKeys.NEWTHREADHASHMAP);
				// get form from hashmap
				resetForm = (ThreadForm) threadHashMap.get(windowId);
			}
			else if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.EDIT)) {
				// hash map for edit activity
				threadHashMap = (HashMap) session.getAttribute(ThreadConstantKeys.EDITTHREADHASHMAP);
				// get form from hashmap
				resetForm = (ThreadForm) threadHashMap.get(windowId);
			}

			resetForm = (ThreadForm) form;

			resetForm.setThreadId("0");
			resetForm.setTitle("");
			resetForm.setDetail("");
			resetForm.setPriorityId(0);
			resetForm.setPriorityName("");


			resetForm.setCreatedby("");
			resetForm.setCreateddate("");
			resetForm.setModifieddate("");

			// set the form back in session
			if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.ADD)) {			
				threadHashMap.put(windowId, resetForm);
				session.setAttribute(ThreadConstantKeys.NEWTHREADHASHMAP, threadHashMap);
			}
			else if ((request.getParameter(ThreadConstantKeys.TYPEOFOPERATION).toString()).equals(ThreadConstantKeys.EDIT)) {
				threadHashMap.put(windowId, resetForm);
				session.setAttribute(ThreadConstantKeys.EDITTHREADHASHMAP, threadHashMap);
			}		

		}
		catch (Exception e) {
			e.printStackTrace();
		}	
		finally {
			request.setAttribute("Threadform", form);									
			System.out.println("PopulateThreadForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");		
		}
	}
}
