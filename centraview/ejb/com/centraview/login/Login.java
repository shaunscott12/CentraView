/*
 * $RCSfile: Login.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:29 $ - $Author: mking_cv $
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


package com.centraview.login;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.common.UserObject;

/*
 * @author  Shilpa Patil,Himansu Biswal
 * @version 1.0
 * Date 18/07/2003
 */
public interface Login extends EJBObject
{
    public HashMap authenticateUser(String userName, String password) throws RemoteException;
	/**
     * return hashmap with data for sending email
     * with forgotten password
     * @param userName String
     * @param email String
     * @throws RemoteException
     * @return HashMap
     */
    public HashMap getForgottenPassword(String userName, String email)   throws RemoteException;
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws RemoteException;
	// For the Customer View
	public UserObject  getUserObject(int individualId,String firstName,String lastName,String userType) throws java.rmi.RemoteException;
	public HashMap authenticateUserCustomer(String userName, String password)   throws java.rmi.RemoteException;

}