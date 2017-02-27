/*
 * $RCSfile: GlobalReplace.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:44 $ - $Author: mking_cv $
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


package com.centraview.administration.globalreplace;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;

/**
 * This interface is the remote interface
 * for the Global REplace EJB. All of the
 * public available (remotely) methods are defined
 * in this interface.
 *
 * @author Naresh Patel <npatel@centraview.com>
 */
public interface GlobalReplace extends EJBObject {

	/**
	 * Returns the list of primary table on which
	 * we are going to perform Global Replace. The data is
	 * returned in a Vector with bean object contains tableID & TableName.
	 * <br>
	 *
	 * @return A Vector with bean object contains tableID & TableName.
	 */
	public Vector getPrimaryReplaceTables() throws RemoteException;

	/**
	 * Returns the list of Fields in the concern table. The data is
	 * returned in a Vector with bean object contains
	 * 1) ID contains two information tableID and fieldId seperated by "*"
	 * 2) Name is the FieldName.
	 * <br>
	 *
	 * @return A Vector with bean object contains
	 * 1) ID contains two information tableID and fieldId seperated by "*"
	 * 2) Name is the FieldName.
	 */
	public Vector getReplaceTableFields(int replaceTableID) throws RemoteException;

	/**
	 * Returns the list of Field's Value. We are going to Replace the selected
	 * field with one selected field's value. The data is
	 * returned in a Vector with bean object contains fieldValueID & fieldValue.
	 * <br>
	 *
	 * @param tableID The ID of the table in Database.
	 * @param fieldID The ID of the table's field.
	 * 				  We will collect the values associated to that field.
	 *
	 * @return A Vector with bean object contains fieldValueID & fieldValue.
	 */
	public Vector getFieldValues(int tableID, int fieldID) throws RemoteException;

	/**
	 * We will first collect the table information. Then we will apply the searchVO and
	 * get the List of records which need to be updated.
	 * We must also check wheather the field is occuring on the table. If not then get the
	 * RelationShip Query.
	 * Finally we will build the update Query.
	 * for example: Method Of contact for that entity/ Individual is not occuring then we will
	 * 				Add a new entry for that method of contact
	 *
	 *
	 * @param replaceInfo The replaceInfo its a collection of information
	 * 		1) individualID The individualID of the user asking for the Replace.
	 *      2) tableID  The tableID on which we are going to perform the replace.
	 *      3) searchVO  The searchVO list of criteria, we will use to build up a list of records which will be affected by this replace.
	 *		4) fieldInfo The fieldInfo contains three information seperated by the "*" TableID/FieldID/FieldType
	 *		5) fieldValue The value which will be replaced on the selected Field.
	 *		6) fieldName Name of the Field which is getting Replaced.
	 *
	 * @return updateFlag if any thing goes wrong then it will return false else it will return true.
	 */
	public boolean performGlobalReplace(HashMap replaceInfo) throws RemoteException;

	/**
	 * Returns the field ID for the Field Name "list" which belongings
	 * to the Table.
	 *
	 * @param tableID to identify the table
	 */
	public int getSearchFieldID(int tableID) throws RemoteException;

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws RemoteException;


}
