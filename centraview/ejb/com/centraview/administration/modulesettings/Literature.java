/*
 * $RCSfile: Literature.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:47 $ - $Author: mking_cv $
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
 * Literature.java
 *
 * @author   Pravin Kadam
 * @version  1.0    
 * 
 */
package com.centraview.administration.modulesettings;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
* This is  remote interface which is called from client
*/

public interface Literature extends EJBObject
{
	public int addLiterature(int userId,String title, int fileID, String description)throws LiteratureException,RemoteException;
	public void deleteLiterature(int userID,int literatureID) throws LiteratureException,RemoteException;
	public LiteratureVO selectLiterature(int literatureID) throws LiteratureException,RemoteException;
	public void updateLiterature(int userid, String title, int fileID,int literatureID)throws LiteratureException,RemoteException;
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws RemoteException;
	
}

                                                                                                                                                                                                                                                                                                                                                                                      










