/*
 * $RCSfile: PopulateLiteratureForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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
 *	Populate Literature Form
 *	@author Sunita
 */

package com.centraview.marketing;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;

/*
 *	Used to maintain user input data while verticate navigation
 */
public class PopulateLiteratureForm {

	/*
	 *	Constructor
	 */
    public PopulateLiteratureForm() {
    }

	/*
	 *	Set (Update) the user input data from form of previous tab into hashmap
	 */
	public void setForm (HttpServletRequest request, HttpServletResponse response, ActionForm form)
	{
		try
		{
			//System.out.println("PopulateLiteratureForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap literatureHashMap = null;
			// literature form from session
			LiteratureForm sessionForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(MarketingConstantKeys.WINDOWID);
			String previousTab = (String) request.getParameter(MarketingConstantKeys.CURRENTTAB);

			if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.ADD))
			{
				// hash map for add literature
				literatureHashMap = (HashMap) session.getAttribute(MarketingConstantKeys.NEWMLHASHMAP);
				// get form from hashmap
				sessionForm = (LiteratureForm) literatureHashMap.get(windowId);
			}
			else if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.EDIT))
			{
				// hash map for edit literature
				literatureHashMap = (HashMap) session.getAttribute(MarketingConstantKeys.NEWMLHASHMAP);
				// get form from hashmap
				sessionForm = (LiteratureForm) literatureHashMap.get(windowId);
			}

			// get previous form values
			LiteratureForm previousForm = (LiteratureForm) form;


			// set form for previous permission values
			if (MarketingConstantKeys.DETAIL.equals(previousTab))
			{
				sessionForm.setTitle(previousForm.getTitle());
				sessionForm.setDetail(previousForm.getDetail());
				sessionForm.setEntityname(previousForm.getEntityname());
				sessionForm.setEntityid(previousForm.getEntityid());
				sessionForm.setIndividualid(previousForm.getIndividualid());
				sessionForm.setIndividualname(previousForm.getIndividualname());
				sessionForm.setAssignedtoname(previousForm.getAssignedtoname());
				sessionForm.setAssignedtoid(previousForm.getAssignedtoid());
				sessionForm.setDeliverymethodid(previousForm.getDeliverymethodid());
				sessionForm.setDeliverymethodname(previousForm.getDeliverymethodname());
				sessionForm.setStatusid(previousForm.getStatusid());
				sessionForm.setStatusname(previousForm.getStatusname());
				sessionForm.setDuebymonth(previousForm.getDuebymonth());
				sessionForm.setDuebyday(previousForm.getDuebyday());
				sessionForm.setDuebytime(previousForm.getDuebytime());
				sessionForm.setDuebyyear(previousForm.getDuebyyear());
				sessionForm.setLiteraturenamevec(previousForm.getLiteraturenamevec());
				sessionForm.setLiteraturename(previousForm.getLiteraturename());
			}
/*			else if (MarketingConstantKeys.PERMISSION.equals(previousTab))
			{
				// view permission
				String[] viewPermissionS = previousForm.getViewpermission();
				Vector viewPermisionV = new Vector();
				DDNameValue viewPermissionListBox = null;
				String idName = "";
				String displayName = "";
				// if user have selected records
				if (previousForm.getViewpermission() != null)
				{
					int sizeOfListBox = viewPermissionS.length;
					for (int i=0;i<sizeOfListBox;i++)
					{
						int indexOfHash = viewPermissionS[i].indexOf("#");
						idName = viewPermissionS[i].substring(0, indexOfHash);
						displayName = viewPermissionS[i].substring(indexOfHash+1, viewPermissionS[i].length());
						viewPermissionListBox = new DDNameValue(viewPermissionS[i], displayName);
						viewPermisionV.add(viewPermissionListBox);
					}
					previousForm.setViewpermissionvec(viewPermisionV);
				}
				// if user have not selected records
				else
				{
					previousForm.setViewpermissionvec(new Vector());
				}

				// modify permission
				String[] modifyPermissionS = previousForm.getViewpermission();
				Vector modifyPermisionV = new Vector();
				DDNameValue modifyPermissionListBox = null;
				idName = "";
				displayName = "";
				// if user have selected records
				if (previousForm.getViewpermission() != null)
				{
					int sizeOfListBox = modifyPermissionS.length;
					for (int i=0;i<sizeOfListBox;i++)
					{
						int indexOfHash = modifyPermissionS[i].indexOf("#");
						idName = modifyPermissionS[i].substring(0, indexOfHash);
						displayName = modifyPermissionS[i].substring(indexOfHash+1, modifyPermissionS[i].length());
						modifyPermissionListBox = new DDNameValue(modifyPermissionS[i], displayName);
						modifyPermisionV.add(modifyPermissionListBox);
					}
					previousForm.setViewpermissionvec(modifyPermisionV);
				}
				// if user have not selected records
				else
				{
					previousForm.setViewpermissionvec(new Vector());
				}

				// delete permission
				String[] deletePermissionS = previousForm.getViewpermission();
				Vector deletePermisionV = new Vector();
				DDNameValue deletePermissionListBox = null;
				idName = "";
				displayName = "";
				// if user have selected records
				if (previousForm.getViewpermission() != null)
				{
					int sizeOfListBox = deletePermissionS.length;
					for (int i=0;i<sizeOfListBox;i++)
					{
						int indexOfHash = deletePermissionS[i].indexOf("#");
						idName = deletePermissionS[i].substring(0, indexOfHash);
						displayName = deletePermissionS[i].substring(indexOfHash+1, deletePermissionS[i].length());
						deletePermissionListBox = new DDNameValue(deletePermissionS[i], displayName);
						deletePermisionV.add(deletePermissionListBox);
					}
					previousForm.setViewpermissionvec(deletePermisionV);
				}
				// if user have not selected records
				else
				{
					previousForm.setViewpermissionvec(new Vector());
				}
				sessionForm.setAllindividual(previousForm.getAllindividual());
				sessionForm.setViewpermission(previousForm.getViewpermission());
				sessionForm.setViewpermissionvec(previousForm.getViewpermissionvec());
				sessionForm.setModifypermission(previousForm.getModifypermission());
				sessionForm.setModifypermissionvec(previousForm.getModifypermissionvec());
				sessionForm.setDeletepermission(previousForm.getDeletepermission());
				sessionForm.setDeletepermissionvec(previousForm.getDeletepermissionvec());
			}
*/
			// set the form back in session
			if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.ADD))
			{
				literatureHashMap.put(windowId, sessionForm);
				session.setAttribute(MarketingConstantKeys.NEWMLHASHMAP, literatureHashMap);
			}
			else if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.EDIT))
			{
				literatureHashMap.put(windowId, sessionForm);
				session.setAttribute(MarketingConstantKeys.EDITMLHASHMAP, literatureHashMap);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("PopulateLiteratureForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
		}
	}

	/*
	 *	Get the user input data from hashmap
	 */
	public ActionForm getForm (HttpServletRequest request, HttpServletResponse response, ActionForm form, String currentTab)
	{
		try
		{
			//System.out.println("PopulateLiteratureForm::getForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap literatureHashMap = null;
			// form from session
			LiteratureForm sessionForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(MarketingConstantKeys.WINDOWID);

			if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.ADD))
			{
				// hash map for add activity
				literatureHashMap = (HashMap) session.getAttribute(MarketingConstantKeys.NEWMLHASHMAP);
				// get form from hashmap
				sessionForm = (LiteratureForm) literatureHashMap.get(windowId);
			}
			else if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.EDIT))
			{
				// hash map for edit activity
				literatureHashMap = (HashMap) session.getAttribute(MarketingConstantKeys.EDITMLHASHMAP);
				// get form from hashmap
				sessionForm = (LiteratureForm) literatureHashMap.get(windowId);
			}

			// get previous form values
			LiteratureForm currentForm = (LiteratureForm) form;

			// set form for previous tab values
			// set detail

			currentForm.setTitle(sessionForm.getTitle());
			currentForm.setDetail(sessionForm.getDetail());
			currentForm.setEntityname(sessionForm.getEntityname());
			currentForm.setEntityid(sessionForm.getEntityid());
			currentForm.setIndividualid(sessionForm.getIndividualid());
			currentForm.setIndividualname(sessionForm.getIndividualname());
			currentForm.setAssignedtoname(sessionForm.getAssignedtoname());
			currentForm.setAssignedtoid(sessionForm.getAssignedtoid());
			currentForm.setDeliverymethodid(sessionForm.getDeliverymethodid());
			currentForm.setDeliverymethodname(sessionForm.getDeliverymethodname());
			currentForm.setStatusid(sessionForm.getStatusid());
			currentForm.setStatusname(sessionForm.getStatusname());
			currentForm.setDuebymonth(sessionForm.getDuebymonth());
			currentForm.setDuebyday(sessionForm.getDuebyday());
			currentForm.setDuebytime(sessionForm.getDuebytime());
			currentForm.setDuebyyear(sessionForm.getDuebyyear());
			currentForm.setLiteraturenamevec(sessionForm.getLiteraturenamevec());
			currentForm.setLiteraturename(sessionForm.getLiteraturename());

			// update request
			request.setAttribute(MarketingConstantKeys.CURRENTTAB, currentTab);
			request.setAttribute(MarketingConstantKeys.TYPEOFOPERATION, request.getParameter(MarketingConstantKeys.TYPEOFOPERATION));
			request.setAttribute(MarketingConstantKeys.WINDOWID,request.getParameter(MarketingConstantKeys.WINDOWID));
			request.setAttribute("literatureform", form);

			//System.out.println("PopulateLiteratureForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return form;
	}

	/*
	 *	Reset user input data from form into hashmap
	 */
	public void resetForm (HttpServletRequest request, HttpServletResponse response, ActionForm form)
	{
		try
		{
			//System.out.println("PopulateLiteratureForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap literatureHashMap = null;
			// form from session
			LiteratureForm resetForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(MarketingConstantKeys.WINDOWID);

			if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.ADD))
			{
				// hash map for add activity
				literatureHashMap = (HashMap) session.getAttribute(MarketingConstantKeys.NEWMLHASHMAP);
				// get form from hashmap
				resetForm = (LiteratureForm) literatureHashMap.get(windowId);
			}
			else if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.EDIT))
			{
				// hash map for edit activity
				literatureHashMap = (HashMap) session.getAttribute(MarketingConstantKeys.EDITMLHASHMAP);
				// get form from hashmap
				resetForm = (LiteratureForm) literatureHashMap.get(windowId);
			}

			resetForm.setTitle("");
			resetForm.setDetail("");
			resetForm.setEntityname("");
			resetForm.setEntityid("");
			resetForm.setIndividualid("");
			resetForm.setIndividualname("");
			resetForm.setAssignedtoname("");
			resetForm.setAssignedtoid("");
			resetForm.setDeliverymethodid("");
			resetForm.setDeliverymethodname("");
			resetForm.setStatusid("");
			resetForm.setStatusname("");
			resetForm.setDuebymonth("");
			resetForm.setDuebyday("");
			resetForm.setDuebytime("");
			resetForm.setDuebyyear("");
			resetForm.setLiteraturenamevec(null);
			resetForm.setLiteraturename(null);

/*			resetForm.setAllindividual(null);
			resetForm.setDeletepermission(null);
			resetForm.setDeletepermissionvec(null);
			resetForm.setModifypermission(null);
			resetForm.setModifypermissionvec(null);
			resetForm.setViewpermission(null);
			resetForm.setViewpermissionvec(null);
*/
			// set the form back in session
			if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.ADD))
			{
				literatureHashMap.put(windowId, resetForm);
				session.setAttribute(MarketingConstantKeys.NEWMLHASHMAP, literatureHashMap);
			}
			else if ((request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).toString()).equals(MarketingConstantKeys.EDIT))
			{
				literatureHashMap.put(windowId, resetForm);
				session.setAttribute(MarketingConstantKeys.EDITMLHASHMAP, literatureHashMap);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			request.setAttribute("literatureform", form);
			//System.out.println("PopulateLiteratureForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
		}
	}
}
