/*
 * $RCSfile: HistoryEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:45 $ - $Author: mking_cv $
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


package com.centraview.administration.history;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;

/**
 *
 * @author praveen
 *
 * This is the EJB class for History
 * The Logic for methods defined in Remote interface
 * is defined in this class
 */
public class HistoryEJB implements SessionBean {
	protected SessionContext ctx;
	private String dataSource = "MySqlDS";
	/**
	 *
	 */
	public void setSessionContext(SessionContext ctx) {
		this.ctx = ctx;
	}

	/**
	 *
	 *
	 */
	public void ejbCreate() {
	}

	/**
	 *
	 *
	 */
	public void ejbRemove() {
	}

	/**
	 *
	 *
	 */
	public void ejbActivate() {
	}

	/**
	 *
	 *
	 */
	public void ejbPassivate() {
	}

	public boolean isDeletedRecord(
		String recordType,
		int recordID,
		String deleteHistoryType,
		String restoreHistoryType) {

		boolean recordDeleted = false;
		Timestamp currentTime = null;
		Timestamp requiredTime = null;
		String operation = null;

		// Create the instance of CVDal
		CVDal cvdl = new CVDal(dataSource);

		//set the SQL Qyuery as well as RecordTypeID and RecordID
		cvdl.setSql("history.seleterecord");
		cvdl.setString(1, recordType);
		cvdl.setInt(2, recordID);
		cvdl.setString(3, deleteHistoryType);
		cvdl.setString(4, restoreHistoryType);

		//Execute the query and get the output
		Collection col = cvdl.executeQuery();

		Iterator it = col.iterator();

		// The return values will be either 0, 1 or 2 rows only
		// As query is order by operation field of contact history type
		// We have maintained the order of deleted and restored values

		while (it.hasNext()) {
			HashMap hm = (HashMap) it.next();
			currentTime = (Timestamp) hm.get("historydate");
			if ((String) hm.get("historytype") != null) {

				// For first time when it retrived
				if (requiredTime == null) {
					requiredTime = currentTime;
					operation = (String) hm.get("historytype");
				} //End of Inner IF
				else if (requiredTime.before(currentTime)) {
					requiredTime = currentTime;
					operation = (String) hm.get("historytype");
				}
			} //End of outer IF

			//Now check whether last operation is of delete type

			if (operation.equals(deleteHistoryType))
				recordDeleted = true;
			else
				recordDeleted = false;

		}
		cvdl.destroy();
		cvdl = null;

		//Finally return the recordDeleted boolean
		return recordDeleted;

	} //End of isDeletedRecord Method

	// Add new History record
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
	public void addHistoryRecord(HashMap historyInfo) {

			// Create the instance of CVDal
			CVDal cvdl = new CVDal(dataSource);

			int recordID = ((Number) historyInfo.get("recordID")).intValue();
			int recordTypeID = ((Number) historyInfo.get("recordTypeID")).intValue();
			int operation = ((Number) historyInfo.get("operation")).intValue();
			int individualID = ((Number) historyInfo.get("individualID")).intValue();
			int referenceActivityID = ((Number) historyInfo.get("referenceActivityID")).intValue();			
			String recordName = (String) historyInfo.get("recordName");
			//Set sql query and all respective fields

			cvdl.setSql("history.insertrecord");
			cvdl.setInt(1, recordID);
			cvdl.setInt(2, recordTypeID);
			cvdl.setString(3, recordName);			
			cvdl.setInt(4, operation);
			cvdl.setInt(5, individualID);
			cvdl.setInt(6, referenceActivityID);

			//It will add the current Timestamp value as well as will generate new historyRecord

			//Update the values in the database
			cvdl.executeUpdate();
			cvdl.destroy();
			cvdl = null;



	}
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

	public boolean deleteHistoryRecord(int operation, int recordTypeID, int recordID){
		boolean recordDeleted = true;
		try{
			// Create the instance of CVDal
			CVDal cvdl = new CVDal(dataSource);

			//set the SQL Qyuery as well as RecordTypeID and RecordID
			cvdl.setSql("history.deleterecord");
			cvdl.setInt(1, operation);
			cvdl.setInt(2, recordTypeID);
			cvdl.setInt(3, recordID);
			cvdl.executeUpdate();
			cvdl.destroy();
			cvdl = null;
		}
		catch(Exception e){
			recordDeleted = false;
		}
		//Finally return the recordDeleted boolean
		return recordDeleted;

	} //End of isDeletedRecord Method

} //End of Class
