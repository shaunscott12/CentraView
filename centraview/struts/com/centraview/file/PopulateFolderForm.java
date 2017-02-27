/*
 * $RCSfile: PopulateFolderForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:56 $ - $Author: mking_cv $
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

public class PopulateFolderForm {

    public PopulateFolderForm() {
        // TODO: Write constructor body
    }
	
	public void setForm (HttpServletRequest request, HttpServletResponse response, ActionForm form)	
	{
		try
		{
			System.out.println("PopulateFolderForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");
					
			// get from session the form associated with windowid
			HttpSession session = request.getSession();
		
			// hashMap
			HashMap folderHashMap = null;
			// form from session
			FolderForm sessionForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(FileConstantKeys.WINDOWID);
			String previousTab = (String) request.getParameter(FileConstantKeys.CURRENTTAB);
			
			if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.ADD))
			{
				// hash map for add folder
				folderHashMap = (HashMap) session.getAttribute(FileConstantKeys.NEWFFHASHMAP);
				// get form from hashmap
				sessionForm = (FolderForm) folderHashMap.get(windowId);
			}
			else if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				// hash map for edit activity
				folderHashMap = (HashMap) session.getAttribute(FileConstantKeys.NEWFFHASHMAP);
				// get form from hashmap
				sessionForm = (FolderForm) folderHashMap.get(windowId);
			}
			
			// get previous form values	
			FolderForm previousForm = (FolderForm) form;
			
			// set activity id if present
			if (previousForm.getFolderId() != null)
				sessionForm.setFolderId(previousForm.getFolderId());			
			
			// set form for previous permission values
			if (FileConstantKeys.DETAIL.equals(previousTab))	
			{
				sessionForm.setFoldername(previousForm.getFoldername());
				sessionForm.setSubfoldername(previousForm.getSubfoldername());
				sessionForm.setAccess(previousForm.getAccess());
				sessionForm.setDescription(previousForm.getDescription());
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
				folderHashMap.put(windowId, sessionForm);
				session.setAttribute(FileConstantKeys.NEWFFHASHMAP, folderHashMap);
			}
			else if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				folderHashMap.put(windowId, sessionForm);
				session.setAttribute(FileConstantKeys.EDITFFHASHMAP, folderHashMap);
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

	public ActionForm getForm (HttpServletRequest request, HttpServletResponse response, ActionForm form, String currentTab)	
	{
		try
		{
			System.out.println("PopulateFolderForm::getForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");
					
			// get from session the form associated with windowid
			HttpSession session = request.getSession();
		
			// hashMap
			HashMap folderHashMap = null;
			// form from session
			FolderForm sessionForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(FileConstantKeys.WINDOWID);
		
			if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.ADD))
			{
				// hash map for add activity
				folderHashMap = (HashMap) session.getAttribute(FileConstantKeys.NEWFFHASHMAP);
				// get form from hashmap
				sessionForm = (FolderForm) folderHashMap.get(windowId);
			}
			else if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				// hash map for edit activity
				folderHashMap = (HashMap) session.getAttribute(FileConstantKeys.EDITFFHASHMAP);
				// get form from hashmap
				sessionForm = (FolderForm) folderHashMap.get(windowId);
			}
			
			// get previous form values	
			FolderForm currentForm = (FolderForm) form;

			// set activity id if present
			if (sessionForm.getFolderId() != null)
				currentForm.setFolderId(sessionForm.getFolderId());			
							
			// set form for previous tab values
			// set detail
			currentForm.setAccess(sessionForm.getAccess());
			currentForm.setDescription(sessionForm.getDescription());
			currentForm.setFolderId(sessionForm.getFolderId());
			currentForm.setFoldername(sessionForm.getFoldername());
			currentForm.setSubfoldername(sessionForm.getSubfoldername());
			// set permission
			currentForm.setAllindividual(sessionForm.getAllindividual());
			currentForm.setDeletepermission(sessionForm.getDeletepermission());
			currentForm.setDeletepermissionvec(sessionForm.getDeletepermissionvec());
			currentForm.setModifypermission(sessionForm.getModifypermission());
			currentForm.setModifypermissionvec(sessionForm.getModifypermissionvec());
			currentForm.setViewpermission(sessionForm.getViewpermission());
			currentForm.setViewpermissionvec(sessionForm.getViewpermissionvec());
		
			// update request
			request.setAttribute(FileConstantKeys.TYPEOFFILE,FileConstantKeys.FOLDER);
			request.setAttribute(FileConstantKeys.CURRENTTAB, currentTab);			
			request.setAttribute(FileConstantKeys.TYPEOFOPERATION, request.getParameter(FileConstantKeys.TYPEOFOPERATION));
			request.setAttribute(FileConstantKeys.WINDOWID,request.getParameter(FileConstantKeys.WINDOWID));
			request.setAttribute("folderform", form);							

			System.out.println("PopulateFolderForm::setForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");
		}
		catch (Exception e)
		{
			e.printStackTrace();		
		}
		return form;
	}

	// reset form
	public void resetForm (HttpServletRequest request, HttpServletResponse response, ActionForm form)	
	{
		try
		{
			System.out.println("PopulateFolderForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");	
			
			// get from session the form associated with windowid
			HttpSession session = request.getSession();

			// hashMap
			HashMap folderHashMap = null;
			// form from session
			FolderForm resetForm = null;

			// get from request windowid
			String windowId = (String) request.getParameter(FileConstantKeys.WINDOWID);
		
			if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.ADD))
			{
				// hash map for add activity
				folderHashMap = (HashMap) session.getAttribute(FileConstantKeys.NEWFFHASHMAP);
				// get form from hashmap
				resetForm = (FolderForm) folderHashMap.get(windowId);
			}
			else if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				// hash map for edit activity
				folderHashMap = (HashMap) session.getAttribute(FileConstantKeys.EDITFFHASHMAP);
				// get form from hashmap
				resetForm = (FolderForm) folderHashMap.get(windowId);
			}

			resetForm.setAccess("");
			resetForm.setAllindividual(null);
			resetForm.setDeletepermission(null);
			resetForm.setDeletepermissionvec(null);
			resetForm.setDescription("");
			resetForm.setFolderId("");
			resetForm.setFoldername("");
			resetForm.setModifypermission(null);
			resetForm.setModifypermissionvec(null);
			resetForm.setSubfoldername("");
			resetForm.setViewpermission(null);
			resetForm.setViewpermissionvec(null);

			// set the form back in session
			if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.ADD))
			{			
				folderHashMap.put(windowId, resetForm);
				session.setAttribute(FileConstantKeys.NEWFFHASHMAP, folderHashMap);
			}
			else if ((request.getParameter(FileConstantKeys.TYPEOFOPERATION).toString()).equals(FileConstantKeys.EDIT))
			{
				folderHashMap.put(windowId, resetForm);
				session.setAttribute(FileConstantKeys.EDITFFHASHMAP, folderHashMap);
			}		
			
		}
		catch (Exception e)	
		{
			e.printStackTrace();
		}	
		finally	
		{
			request.setAttribute("folderform", form);									
			System.out.println("PopulateFolderForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::exit");		
		}
	}
}
