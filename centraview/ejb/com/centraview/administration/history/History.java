/*
 * $RCSfile: History.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:45 $ - $Author: mking_cv $
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
 * History.java
 *
 * @version  1.0
 *
 */
package com.centraview.administration.history;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.EJBObject;

/**
 * Remote Interface for History Stateless Session EJB
 * @author praveen
 *
 */

public interface History extends EJBObject {
	//	----------------------------------------------- Methods

	/**
	 * This method check whether record exists in respective table
	 *
	 * @param RecordID The RecordID
	 * @param RecordType The RecordType e.g. Individual, Entity
	 * @param deleteHistoryType String value of Deleted HistoryType
	 * @param restoreHistoryType String value of Restored HistoryType
	 * @return TRUE if Record is deleted
	 * @throws RemoteException
	 */
	public boolean isDeletedRecord(
		String recordType,
		int recordID,
		String deleteHistoryType,
		String restoreHistoryType)
		throws RemoteException;

	/**
	 *
	 * This method add new history record in Database
	 *
	 * @param HashMap Its a collection of historyInfo like 
	 * <ui> recordID The Record ID from Individual or Entity Table
	 * <ui> recordTypeID The RecordType is either Entity or Individual
	 * <ui> recordName The RecordName is the name of record which we are inserting.
	 * <ui> operation Operation is deleted, created , restored, modified or complete activity
	 * <ui> individualID The the Individual who create this history record
	 * <ui> referenceActivityID Refereces to the Activity ID for that record
	 *
	 */
	public void addHistoryRecord(HashMap historyInfo) throws RemoteException;

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws RemoteException;

	/**
	 * This method delete RecordID from ContactHistory Table
	 *
	 * @param operation The operation
	 * @param recordTypeID The recordTypeID
	 * @param RecordID The RecordID
	 * @return TRUE if Record is deleted
	 */
	public boolean deleteHistoryRecord(int operation, int recordTypeID, int recordID) throws RemoteException;

}
