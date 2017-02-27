/*
 * $RCSfile: View.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:16 $ - $Author: mking_cv $
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

package com.centraview.view;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;


/*
 * @author  Ashwin Nagar
 * @version 1.0
 */
public interface View extends EJBObject
{
	public int addView(int userId, ViewVO nvo) throws RemoteException;
	public ViewVO getView(int userId, int viewId) throws RemoteException;
	public void updateView(int userId, ViewVO nvo) throws RemoteException;
	public Vector getAllColumns(String strListType) throws RemoteException;
	public Vector getOwnerSearch (int ownerId, String listType) throws RemoteException;
	public Vector getViewComboData (int userId, String primaryTableName) throws RemoteException;
	public Vector getViewInfo(int userId, int viewId) throws RemoteException;
	public int getSystemDefaultView(String listType) throws RemoteException;
	public void updateUserDefaultView(int viewId, int userId, String sortType, String sortElement, int recordPerPage, String listType) throws RemoteException;
	public void deleteUserView(int viewId) throws RemoteException;
	public Vector getUserDefaultColumns (int viewId, String listType, int userId) throws RemoteException;
	public void deleteView(int viewId) throws RemoteException;
	public int getUserDefaultView(int userId, String listType) throws RemoteException;
	public void updateUserDefaultViewSort(int viewId, String listType, String sortColumn, String sortOrder) throws RemoteException;
	public void updateUserDefaultViewRRP(int viewId, String listType, int recordPerPage) throws RemoteException;

	/**
	* Get the Default View of the ListType and Its associated View in a form of vector
	*
	* @param listTypeList  listTypeList its a collection of the List Type
	*                      for which we must have to find the defaultView and the List of all the view which are created under that listType
	*
	* @return defaultViewMap  defaultViewMap its a map of two things. They are
	*                         key Object is DefaultViewID will contain the information of the viewID
	*					                key Object is ViewList will contain a Vector (ViewID and View Name)
	*
	*/
	public HashMap getDefaultViews(ArrayList listType) throws RemoteException;

	/**
	* Set the Default View of the Module to new selected ViewID.
	*
	* @param defaultViews  defaultViews its a collection of the key as List Type (Module name)
	*												and Value will be the newly Set View ID
	*/
	public void setDefaultView(HashMap defaultViews) throws RemoteException;

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws RemoteException;
}
