/*
 * $RCSfile: HrLists.java,v $    $Revision: 1.2 $  $Date: 2005/05/23 18:07:23 $ - $Author: mking_cv $
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


package com.centraview.hr.hrlist;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.EmployeeList;
import com.centraview.common.ExpenseFormList;
import com.centraview.common.TimeSheetList;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface HrLists extends EJBObject
{
	public EmployeeList getEmployeeDetailList(int userID, HashMap info) throws RemoteException, AuthorizationFailedException;
	public TimeSheetList getTimeSheetList(int userID, HashMap info) throws RemoteException, AuthorizationFailedException;
	public ExpenseFormList getExpenseFormList(int userID, HashMap info) throws RemoteException, AuthorizationFailedException;
	public ValueListVO getExpenseFormValueList(int individualID, ValueListParameters parameters) throws RemoteException;	
	public ValueListVO getTimeSheetValueList(int individualID, ValueListParameters parameters) throws RemoteException;	
  
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws RemoteException;
}

