/*
 * $RCSfile: PopulateFileForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:56 $ - $Author: mking_cv $
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

package com.centraview.file;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;

import com.centraview.common.DDNameValue;

/*
 *	Used to maintain user input data while verticate navigation
 */
public class PopulateFileForm {

	/*
	 *	Constructor
	 */
    public PopulateFileForm() {
    }

	/*
	 *	Set (Update) the user input data from form of previous tab into hashmap
	 */
	public void setForm (HttpServletRequest request, HttpServletResponse response, ActionForm form)
	{
		try
		{
			System.out.println("PopulateFileForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap fileHashMap = null;
			// file form from session
			FileForm sessionForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(FileConstantKeys.WINDOWID);
			String previousTab = (String) request.getParameter(FileConstantKeys.CURRENTTAB);

      String operationType = (String)request.getParameter(FileConstantKeys.TYPEOFOPERATION);

	  if (operationType != null)
      {
        if (operationType.equals(FileConstantKeys.ADD))
        {
          // hash map for add folder
          fileHashMap = (HashMap) session.getAttribute(FileConstantKeys.NEWFFHASHMAP);
          // get form from hashmap
          sessionForm = (FileForm) fileHashMap.get(windowId);
        }else if(operationType.equals(FileConstantKeys.EDIT)){
          // hash map for edit activity
          fileHashMap = (HashMap) session.getAttribute(FileConstantKeys.NEWFFHASHMAP);
          // get form from hashmap
          sessionForm = (FileForm) fileHashMap.get(windowId);
        }
      }

			// get previous form values
			FileForm previousForm = (FileForm) form;

			// set activity id if present
			if (previousForm.getFileId() != null)
				sessionForm.setFileId(previousForm.getFileId());

			// set form for previous permission values
			if (FileConstantKeys.DETAIL.equals(previousTab))
			{
				sessionForm.setFile(previousForm.getFile());
				sessionForm.setUploadfoldername(previousForm.getUploadfoldername());
				sessionForm.setUploadfolderid(previousForm.getUploadfolderid());
				// vector of other uploaded folder name
				sessionForm.setOtheruploadfoldername(previousForm.getOtheruploadfoldername());
				sessionForm.setOtheruploadfoldernamevec(previousForm.getOtheruploadfoldernamevec());
				sessionForm.setTitle(previousForm.getTitle());
				sessionForm.setDescription(previousForm.getDescription());
				sessionForm.setAuthorname(previousForm.getAuthorname());
				sessionForm.setAuthorid(previousForm.getAuthorid());
				sessionForm.setEntityname(previousForm.getEntityname());
				sessionForm.setEntityid(previousForm.getEntityid());
				sessionForm.setAttachname(previousForm.getAttachname());
				sessionForm.setAttachid(previousForm.getAttachid());
				sessionForm.setRecordname(previousForm.getRecordname());
				sessionForm.setRecordnamevec(previousForm.getRecordnamevec());
				sessionForm.setRecordid(previousForm.getRecordid());
				sessionForm.setAccess(previousForm.getAccess());
				sessionForm.setCustomerview(previousForm.getCustomerview());
				sessionForm.setFileversion(previousForm.getFileversion());
				sessionForm.setIndividualid(previousForm.getIndividualid());
				sessionForm.setIndividualname(previousForm.getIndividualname());
			}
			else if (FileConstantKeys.PERMISSION.equals(previousTab))
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

			// set the form back in session
			if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.ADD))
			{
				fileHashMap.put(windowId, sessionForm);
				session.setAttribute(FileConstantKeys.NEWFFHASHMAP, fileHashMap);
			}
			else if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				fileHashMap.put(windowId, sessionForm);
				session.setAttribute(FileConstantKeys.EDITFFHASHMAP, fileHashMap);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("PopulateFolderForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
		}
	}

	/*
	 *	Get the user input data from hashmap
	 */
	public ActionForm getForm (HttpServletRequest request, HttpServletResponse response, ActionForm form, String currentTab)
	{
		try
		{
			System.out.println("PopulateFileForm::getForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap fileHashMap = null;
			// form from session
			FileForm sessionForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(FileConstantKeys.WINDOWID);

			if (request.getParameter(FileConstantKeys.TYPEOFOPERATION) != null && (request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.ADD))
			{
				// hash map for add activity
				fileHashMap = (HashMap) session.getAttribute(FileConstantKeys.NEWFFHASHMAP);
				// get form from hashmap
				sessionForm = (FileForm) fileHashMap.get(windowId);
			}
			else if (request.getParameter(FileConstantKeys.TYPEOFOPERATION) != null && (request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				// hash map for edit activity
				fileHashMap = (HashMap) session.getAttribute(FileConstantKeys.EDITFFHASHMAP);
				// get form from hashmap
				sessionForm = (FileForm) fileHashMap.get(windowId);
			}

			// get previous form values
			FileForm currentForm = (FileForm) form;

			// set activity id if present
			if (sessionForm.getFileId() != null)
				currentForm.setFileId(sessionForm.getFileId());

			// set form for previous tab values
			// set detail
			currentForm.setFile(sessionForm.getFile());
			currentForm.setUploadfoldername(sessionForm.getUploadfoldername());
			currentForm.setUploadfolderid(sessionForm.getUploadfolderid());
			currentForm.setOtheruploadfoldername(sessionForm.getOtheruploadfoldername());
			currentForm.setOtheruploadfoldernamevec(sessionForm.getOtheruploadfoldernamevec());
			currentForm.setTitle(sessionForm.getTitle());
			currentForm.setDescription(sessionForm.getDescription());
			currentForm.setAuthorname(sessionForm.getAuthorname());
			currentForm.setAuthorid(sessionForm.getAuthorid());
			currentForm.setEntityname(sessionForm.getEntityname());
			currentForm.setEntityid(sessionForm.getEntityid());
			currentForm.setAttachname(sessionForm.getAttachname());
			currentForm.setAttachid(sessionForm.getAttachid());
			currentForm.setRecordname(sessionForm.getRecordname());
			currentForm.setRecordnamevec(sessionForm.getRecordnamevec());
			currentForm.setRecordid(sessionForm.getRecordid());
			currentForm.setAccess(sessionForm.getAccess());
			currentForm.setCustomerview(sessionForm.getCustomerview());
			currentForm.setFileversion(sessionForm.getFileversion());
			currentForm.setIndividualid(sessionForm.getIndividualid());
			currentForm.setIndividualname(sessionForm.getIndividualname());

			// set permission
			currentForm.setAllindividual(sessionForm.getAllindividual());
			currentForm.setDeletepermission(sessionForm.getDeletepermission());
			currentForm.setDeletepermissionvec(sessionForm.getDeletepermissionvec());
			currentForm.setModifypermission(sessionForm.getModifypermission());
			currentForm.setModifypermissionvec(sessionForm.getModifypermissionvec());
			currentForm.setViewpermission(sessionForm.getViewpermission());
			currentForm.setViewpermissionvec(sessionForm.getViewpermissionvec());

			// update request
			request.setAttribute(FileConstantKeys.TYPEOFFILE,FileConstantKeys.FILE);
			request.setAttribute(FileConstantKeys.CURRENTTAB, currentTab);
			request.setAttribute(FileConstantKeys.TYPEOFOPERATION, request.getParameter(FileConstantKeys.TYPEOFOPERATION));
			request.setAttribute(FileConstantKeys.WINDOWID,request.getParameter(FileConstantKeys.WINDOWID));
			request.setAttribute("fileform", form);

			System.out.println("PopulateFileForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
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
			System.out.println("PopulateFileForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");

			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap fileHashMap = null;
			// form from session
			FileForm resetForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(FileConstantKeys.WINDOWID);

			if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.ADD))
			{
				// hash map for add activity
				fileHashMap = (HashMap) session.getAttribute(FileConstantKeys.NEWFFHASHMAP);
				// get form from hashmap
				resetForm = (FileForm) fileHashMap.get(windowId);
			}
			else if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				// hash map for edit activity
				fileHashMap = (HashMap) session.getAttribute(FileConstantKeys.EDITFFHASHMAP);
				// get form from hashmap
				resetForm = (FileForm) fileHashMap.get(windowId);
			}

			resetForm.setFile(null);
			resetForm.setFileId("");
			resetForm.setUploadfoldername("");
			resetForm.setUploadfolderid("");
			resetForm.setOtheruploadfoldername(null);
			resetForm.setOtheruploadfoldernamevec(null);
			resetForm.setTitle("");
			resetForm.setDescription("");
			resetForm.setAuthorname("");
			resetForm.setAuthorid("");
			resetForm.setEntityname("");
			resetForm.setEntityid("");
			resetForm.setAttachname("");
			resetForm.setAttachid("");
			resetForm.setRecordname("");
			resetForm.setRecordnamevec(null);
			resetForm.setRecordid("");
			resetForm.setAccess("");
			resetForm.setCustomerview("");
			resetForm.setFileversion("");

			resetForm.setAllindividual(null);
			resetForm.setDeletepermission(null);
			resetForm.setDeletepermissionvec(null);
			resetForm.setModifypermission(null);
			resetForm.setModifypermissionvec(null);
			resetForm.setViewpermission(null);
			resetForm.setViewpermissionvec(null);

			// set the form back in session
			if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.ADD))
			{
				fileHashMap.put(windowId, resetForm);
				session.setAttribute(FileConstantKeys.NEWFFHASHMAP, fileHashMap);
			}
			else if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				fileHashMap.put(windowId, resetForm);
				session.setAttribute(FileConstantKeys.EDITFFHASHMAP, fileHashMap);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			request.setAttribute("fileform", form);
			System.out.println("PopulateFileForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
		}
	}
}
