/*
 * $RCSfile: KnowledgeBase.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:10 $ - $Author: mking_cv $
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

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.EJBObject;

/*
 * @author  Vivek Temkar
 * @version 1.0
 */

public interface KnowledgeBase extends EJBObject
{

    // Inserting category
	public int  insertCategory (int userId,CategoryVO catinfo) throws RemoteException;
	
	// Updating category
	public void updateCategory (int userId,CategoryVO catinfo) throws RemoteException, KBException;
	
	// Deleting catrgory
	public int deleteCategory (int userId, int categoryId) throws RemoteException;
		
	// Getting categoryVo
	public CategoryVO getCategory (int userId, int catId) throws RemoteException;

	
	// get an array list of CategoryVO	
	public ArrayList getAllCategory  (int userId)  throws RemoteException;
	
	// Inserting KB
	public int  insertKB (int userId,KnowledgeVO kbinfo) throws RemoteException;
	
	// Updating KB
	public void updateKB (int userId,KnowledgeVO kbinfo) throws RemoteException,KBException;
	
	// Deleting KB
	public void deleteKB (int userId,int knId) throws RemoteException;
	
	// Deleting all KB
	public void deleteAllKB (int userId) throws RemoteException;

	// Getting KnowledgeVo
	public KnowledgeVO getKB (int userId,int kbId) throws RemoteException;
	
	public int duplicateCategory(int userId, CategoryVO catVO) throws RemoteException;
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws RemoteException;	
}