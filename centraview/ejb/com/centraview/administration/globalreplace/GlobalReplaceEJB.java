/*
 * $RCSfile: GlobalReplaceEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:44 $ - $Author: mking_cv $
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.advancedsearch.AdvancedSearchLocal;
import com.centraview.advancedsearch.AdvancedSearchLocalHome;
import com.centraview.advancedsearch.SearchVO;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;

/**
 *
 * @author Naresh Patel <npatel@centraview.com>
 */
public class GlobalReplaceEJB implements SessionBean
{
	/** The SessionContext of this SessionBean. */
	protected SessionContext ctx;

	/** The batchQuery of this Session. */
	private ArrayList batchQuery = null;

	/** the logger for this class */
	private static Logger logger = Logger.getLogger(GlobalReplaceEJB.class);
	
	/** The JNDI/DataSource name this EJB will be using. */
	private String dataSource = "MySqlDS";

	/**
	 * Set the associated session context. The container calls
	 * this method after the instance creation.<p>
	 *
	 * The enterprise Bean instance should store the reference
	 * to the context object in an instance variable. <p>
	 *
	 * This method is called with no transaction context.
	 *
	 * @param ctx A SessionContext interface for the instance.
	 *
	 * @throws EJBException Thrown by the method to indicate a
	 * failure caused by a system-level error.
	 *
	 */
	public void setSessionContext(SessionContext ctx) throws EJBException
	{
	  this.ctx = ctx;
	} //end of setSessionContext method

	/**
	 * The activate method is called when the instance is
	 * activated from its "passive" state. The instance
	 * should acquire any resource that it has released
	 * earlier in the ejbPassivate() method. <p>
	 *
	 * This method is called with no transaction context.
	 *
	 * @throws EJBException Thrown by the method to indicate a
	 * failure caused by a system-level error.
	 *
	 * instead of this exception.
	 */
	public void ejbActivate() throws EJBException
	{
	  //Not Implemented
	} //end of ejbActivate method

	/**
	 * A container invokes this method before it ends the
	 * life of the session object. This happens as a result
	 * of a client's invoking a remove operation, or when a
	 * container decides to terminate the session object after
	 * a timeout. <p>
	 *
	 * This method is called with no transaction context.
	 *
	 * @throws EJBException Thrown by the method to indicate a
	 * failure caused by a system-level error.
	 *
	 */
	public void ejbRemove() throws EJBException
	{
	  //Not Implemented
	} //end of ejbRemove method

	/**
	 * The passivate method is called before the instance
	 * enters the "passive" state. The instance should
	 * release any resources that it can re-acquire later
	 * in the ejbActivate() method. <p>
	 *
	 * After the passivate method completes, the instance
	 * must be in a state that allows the container to use the
	 * Java Serialization protocol to externalize and store
	 * away the instance's state. <p>
	 *
	 * This method is called with no transaction context.
	 *
	 * @throws EJBException Thrown by the method to indicate a
	 * failure caused by a system-level error.
	 *
	 */
	public void ejbPassivate() throws EJBException
	{
	  //Not Implemented
	} //end of ejbPassivate method

	/**
	 * The required ejbCreate() method.
	 *
	 * @throws CreateException An instance of the EJB could not
	 * be created.
	 */
	public void ejbCreate() throws CreateException
	{
	  //Not Implemented
	} //end of ejbCreate method

	/**
	 * This simply sets the target datasource to be used
	 * for DB interaction.
	 *
	 * @param ds A string that contains the cannonical JNDI
	 * name of the datasource
	 */
	public void setDataSource(String ds)
	{
	  this.dataSource = ds;
	} //end of setDataSource method

	/**
	 * Returns the field ID for the Field Name "list" which belongings
	 * to the Table.
	 *
	 * @param tableID to identify the table
	 */
	public int getSearchFieldID(int tableID)
	{
		int searchFieldID = -1;
		CVDal cvdal = new CVDal(this.dataSource);
		Vector fieldsValueList = new Vector();
		try
		{
		  String sqlQuery = "Select SearchFieldID from searchfield where SearchtableID = ? and FieldName = ?";
		  cvdal.setSqlQuery(sqlQuery);
		  cvdal.setInt(1,tableID);
		  cvdal.setString(2,GlobalReplaceConstantKeys.SEARCH_MARKETING_FIELD_NAME);

		  Collection resultsCollection = cvdal.executeQuery();
		  if (resultsCollection != null)
		  {
			Iterator resultsIterator = resultsCollection.iterator();
			while (resultsIterator.hasNext())
			{
			  HashMap resultsHashMap = (HashMap) resultsIterator.next();
			  searchFieldID = ((Number) resultsHashMap.get("SearchFieldID")).intValue();
			} //end of while statement (resultsIterator.hasNext())
		  } //end of if statement (resultsCollection != null)
	  } //end of try block
	  catch (Exception e)
	  {
		logger.error("[Exception] GlobalReplaceEJB.getSearchFieldID:", e);
	  } //end of catch block (Exception)
	  finally
	  {
		cvdal.setSqlQueryToNull();
		cvdal.destroy();
		cvdal = null;
	  } //end of finally block

	  return searchFieldID;
	}//end of getSearchFieldID

	/**
	 * Returns the list of primary table on which
	 * we are going to perform Global Replace. The data is
	 * returned in a Vector with bean object contains tableID & TableName.
	 * <br>
	 *
	 * @return A Vector with bean object contains tableID & TableName.
	 */
	public Vector getPrimaryReplaceTables()
	{
	  CVDal cvdal = new CVDal(this.dataSource);
	  Vector replaceTablesList = new Vector();
	  try
	  {
	  	//We will get the list of table id, for which we can preform the Global Replace
		String sqlQuery = "SELECT SearchTableID,DisplayName,TableName " +			" FROM searchtable WHERE IsOnGlobalReplaceTable = 'Y' ";
		cvdal.setSqlQuery(sqlQuery);
		Collection resultsCollection = cvdal.executeQuery();
		if (resultsCollection != null)
		{
		  Iterator resultsIterator = resultsCollection.iterator();
		  while (resultsIterator.hasNext())
		  {
			HashMap resultsHashMap = (HashMap) resultsIterator.next();
			Number tableID = (Number) resultsHashMap.get("SearchTableID");
			String tableDisplayName = (String) resultsHashMap.get("DisplayName");
			String tableName = (String) resultsHashMap.get("TableName");
			if(tableID != null && tableName != null && !tableName.equals("") + tableDisplayName != null && !tableDisplayName.equals("")){
				String keyValue = tableID.toString() + "#" + tableName;
				DDNameValue tableInfo = new DDNameValue(keyValue,tableDisplayName);
				replaceTablesList.add(tableInfo);
			}
		  } //end of while statement (resultsIterator.hasNext())
		} //end of if statement (resultsCollection != null)
	  } //end of try block
	  catch (Exception e)
	  {
		logger.error("[Exception] GlobalReplaceEJB.getPrimaryReplaceTables:", e);
	  } //end of catch block (Exception)
	  finally
	  {
		cvdal.setSqlQueryToNull();
		cvdal.destroy();
		cvdal = null;
	  } //end of finally block

	  return replaceTablesList;
	} //end of getPrimaryReplaceTables method

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
	public Vector getReplaceTableFields(int replaceTableID)
	{
	  CVDal cvdal = new CVDal(this.dataSource);
	  Vector replaceFieldsList = new Vector();
	  boolean flagMethodOfContact = false;
	  boolean flagCustomFields = false;
	  StringBuffer tableIDs = new StringBuffer();
	  tableIDs.append(replaceTableID+"");
	  try
	  {
		//We will get the list of fields which are associate to the table which is selected.
		String sqlQuery = "SELECT RelateTableID,IsRelateTable " +
			" FROM globalreplacerelate WHERE SearchTableID = ? ";
		cvdal.setSqlQuery(sqlQuery);
		cvdal.setInt(1,replaceTableID);
		Collection resultsCollection = cvdal.executeQuery();
		if (resultsCollection != null)
		{
		  Iterator resultsIterator = resultsCollection.iterator();
		  while (resultsIterator.hasNext())
		  {
			HashMap resultsHashMap = (HashMap) resultsIterator.next();
			Number relateTableID = (Number) resultsHashMap.get("RelateTableID");
			int tableID = relateTableID.intValue();
			String isRelateTable = (String) resultsHashMap.get("IsRelateTable");
			if(tableID != 0 && isRelateTable != null && isRelateTable.equals("Y")){
				tableIDs.append("," + tableID);
			}else if(tableID != 0){
				if(tableID == GlobalReplaceConstantKeys.CUSTOM_FIELD_TABLEID ){
					flagCustomFields = true;
				}
			}
		  } //end of while statement (resultsIterator.hasNext())
		} //end of if statement (resultsCollection != null)

		cvdal.setSqlQueryToNull();
		cvdal.clearParameters();
		sqlQuery = "SELECT SearchTableID,SearchFieldID,DisplayName,FieldType " +
			" FROM searchfield WHERE SearchTableID in ("+tableIDs.toString()+")  and IsGlobalReplaceField ='Y'";
		cvdal.setSqlQuery(sqlQuery);
		Collection fieldCollections = cvdal.executeQuery();
		if (fieldCollections != null)
		{
		  Iterator fieldsIterator = fieldCollections.iterator();
		  while (fieldsIterator.hasNext())
		  {
				HashMap fieldsHashMap = (HashMap) fieldsIterator.next();
				Number tableID = (Number) fieldsHashMap.get("SearchTableID");
				Number fieldID = (Number) fieldsHashMap.get("SearchFieldID");
				Number fieldType = (Number) fieldsHashMap.get("FieldType");
				String fieldDisplayName = (String) fieldsHashMap.get("DisplayName");
				String keyValue = tableID.toString() + "*" + fieldID.toString()+ "*" + fieldType.toString();
				DDNameValue tableInfo = new DDNameValue(keyValue,fieldDisplayName);
				replaceFieldsList.add(tableInfo);
		  } //end of while statement (fieldsIterator.hasNext())
		} //end of if statement (fieldCollections != null)

		// If we found the tableId for the customfield associated to the selected table.
		// We must have to get all the fields associated to the table.
		if(flagCustomFields){
			cvdal.setSqlQueryToNull();
			cvdal.clearParameters();

			sqlQuery = "SELECT CustomFieldID , Name, FieldType FROM" +					   " customfield where RecordType = ?;";
			cvdal.setSqlQuery(sqlQuery);
			cvdal.setInt(1,replaceTableID);
			fieldCollections = cvdal.executeQuery();
			if (fieldCollections != null)
			{
			  Iterator fieldsIterator = fieldCollections.iterator();
			  while (fieldsIterator.hasNext())
			  {
				HashMap fieldsHashMap = (HashMap) fieldsIterator.next();
				Number customFieldID = (Number) fieldsHashMap.get("CustomFieldID");
				String fieldDisplayName = (String) fieldsHashMap.get("Name");
				String fieldType = (String) fieldsHashMap.get("FieldType");
				int scalarOrMultiple = 0;
				if(fieldType != null && fieldType.equals("MULTIPLE")){
					scalarOrMultiple = GlobalReplaceConstantKeys.FIELD_TYPE_MULTIPLE;
				}
				String keyValue = GlobalReplaceConstantKeys.CUSTOM_FIELD_TABLEID  + "*" + customFieldID.toString() + "*" + scalarOrMultiple;
				DDNameValue tableInfo = new DDNameValue(keyValue,fieldDisplayName);
				replaceFieldsList.add(tableInfo);
			  } //end of while statement (fieldsIterator.hasNext())
			} //end of if statement (fieldCollections != null)
		}
	  } //end of try block
	  catch (Exception e)
	  {
		logger.error("[Exception] GlobalReplaceEJB.getReplaceTableFields:", e);
	  } //end of catch block (Exception)
	  finally
	  {
		cvdal.setSqlQueryToNull();
		cvdal.destroy();
		cvdal = null;
	  } //end of finally block

	  return replaceFieldsList;
	} //end of getReplaceTableFields method

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
	public Vector getFieldValues(int tableID, int fieldID)
	{
	  CVDal cvdal = new CVDal(this.dataSource);
	  Vector fieldsValueList = new Vector();
	  try
	  {
	  	if(tableID == GlobalReplaceConstantKeys.CUSTOM_FIELD_TABLEID ){
			String sqlQuery = "SELECT ValueID , Value " +
				" FROM customfieldvalue WHERE CustomFieldID = ? ";
			cvdal.setSqlQuery(sqlQuery);
			cvdal.setInt(1,fieldID);

			Collection resultsCollection = cvdal.executeQuery();
			if (resultsCollection != null)
			{
			  Iterator resultsIterator = resultsCollection.iterator();
			  while (resultsIterator.hasNext())
			  {
				HashMap resultsHashMap = (HashMap) resultsIterator.next();
				Number valueID = (Number) resultsHashMap.get("ValueID");
				String value = (String) resultsHashMap.get("Value");
				if(valueID != null && value != null && !value.equals("")){
					String keyValue = valueID.toString();
					DDNameValue tableInfo = new DDNameValue(keyValue,value);
					fieldsValueList.add(tableInfo);
				}
			  } //end of while statement (resultsIterator.hasNext())
			} //end of if statement (resultsCollection != null)
	  	}//end of if(tableID == GlobalReplaceConstantKeys.CUSTOM_FIELD_TABLEID )
	  	else{
	  		// We must have to put a unique entry for the tableID and fieldId in the globalreplacerelate table.
	  		// which should have the fieldname and tablename.
	  		// fieldname should be like this for Example StatusID as ValueID and StatusTitle As Value
			String sqlQuery = "SELECT fieldName,tableName " +
				" FROM Globalreplacevalue WHERE tableID = ? and fieldID= ?";
			cvdal.setSqlQuery(sqlQuery);
			cvdal.setInt(1,tableID);
			cvdal.setInt(2,fieldID);
			String fieldName = null;
			String tableName = null;
			Collection resultsCollection = cvdal.executeQuery();
			if (resultsCollection != null)
			{
			  Iterator resultsIterator = resultsCollection.iterator();
			  while (resultsIterator.hasNext())
			  {
				HashMap resultsHashMap = (HashMap) resultsIterator.next();
				fieldName = (String) resultsHashMap.get("fieldName");
				tableName = (String) resultsHashMap.get("tableName");
			  } //end of while statement (resultsIterator.hasNext())
			} //end of if statement (resultsCollection != null)
			if(fieldName != null && tableName != null){
				sqlQuery = "SELECT " + fieldName + " FROM " + tableName;
				cvdal.setSqlQuery(sqlQuery);
				resultsCollection = cvdal.executeQuery();
				if (resultsCollection != null)
				{
				  Iterator resultsIterator = resultsCollection.iterator();
				  while (resultsIterator.hasNext())
				  {
					HashMap resultsHashMap = (HashMap) resultsIterator.next();
					Number valueID = (Number) resultsHashMap.get("ValueID");
					String value = (String) resultsHashMap.get("Value");
					if(valueID != null && value != null && !value.equals("")){
						String keyValue = valueID.toString();
						DDNameValue tableInfo = new DDNameValue(keyValue,value);
						fieldsValueList.add(tableInfo);
					}//end of if(valueID != null && value != null && !value.equals(""))
				  } //end of while statement (resultsIterator.hasNext())
				} //end of if statement (resultsCollection != null)
			}//end of if(fieldName != null && tableName != null)
	  	}//end of else for if(tableID == GlobalReplaceConstantKeys.CUSTOM_FIELD_TABLEID )
	  } //end of try block
	  catch (Exception e)
	  {
		logger.error("[Exception] GlobalReplaceEJB.getFieldValues:", e);
	  } //end of catch block (Exception)
	  finally
	  {
		cvdal.setSqlQueryToNull();
		cvdal.destroy();
		cvdal = null;
	  } //end of finally block

	  return fieldsValueList;
	} //end of getFieldValues method

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
	public boolean performGlobalReplace(HashMap replaceInfo)
	{
	  boolean updateFlag = true;
	  CVDal cvdal = new CVDal(this.dataSource);
	  Collection allTables = new ArrayList();
	  batchQuery = new ArrayList();

	  try {

		  int individualID = ((Integer) replaceInfo.get("individualID")).intValue();
		  Integer primaryTableID = (Integer) replaceInfo.get("tableID");
		  SearchVO searchVO = (SearchVO) replaceInfo.get("searchVO");
		  String fieldInfo = (String) replaceInfo.get("fieldInfo");
		  String fieldValue = (String) replaceInfo.get("fieldValue");
		  String fieldName = (String) replaceInfo.get("fieldName");

		  InitialContext ic = CVUtility.getInitialContext();
		  AdvancedSearchLocalHome home = (AdvancedSearchLocalHome) ic.lookup("local/AdvancedSearch");
		  AdvancedSearchLocal remote = (AdvancedSearchLocal) home.create();
		  remote.setDataSource(dataSource);
		  ArrayList resultsIDs = new ArrayList();
		  resultsIDs.addAll(remote.performSearch(individualID,searchVO));

		  //Parse out the field Information which are fieldtableid, fieldID and fieldType
		  // Field type as its own idenfication
		  // for example  "6" is a phone type field. "8" is a multiple selection field type
		  int fieldTableID = 0;
		  int replaceFieldID = 0;
		  int replaceFieldType = 0;
		  if(fieldInfo != null){
			  StringTokenizer fieldToken = new StringTokenizer(fieldInfo, "*");
			  String fieldIDString = null;
			  String tableIDString = null;
			  String fieldTypeString = null;
			  if(fieldToken != null){
				  while (fieldToken.hasMoreTokens())
				  {
					  tableIDString = (String) fieldToken.nextToken();
					  fieldIDString = (String) fieldToken.nextToken();
					  fieldTypeString = (String) fieldToken.nextToken();
				  }//end of while (fieldInfo.hasMoreTokens())
				  if(fieldIDString != null && fieldTypeString != null && !fieldIDString.equals("") && !fieldTypeString.equals("")){
					  fieldTableID = Integer.parseInt(tableIDString);
					  replaceFieldID = Integer.parseInt(fieldIDString);
					  replaceFieldType =  Integer.parseInt(fieldTypeString);
				  }//end of if(fieldIDString != null && fieldTypeString != null && !fieldIDString.equals("") && !fieldTypeString.equals(""))
			  }//end of if(fieldToken != null)
		  }//end of if(fieldInfo != null)


		String primaryTable = null;
		String primaryKey = null;
		String relateTable = null;
		int queryTableID = 0;
		String displayName = null;
		String relateKey = null;
		String relationShipQuery = null;
		String isOnGlobalReplaceTable = null;
		String realTableName = null;
		String updateField = null;
		String subRelationShipQuery = null;

		//Get the Primary Table for this module.
		String mainTableQuery = "SELECT TablePrimaryKey,  TableName FROM "+
					"searchtable where SearchTableID = ?";
		cvdal.setSqlQueryToNull();
		cvdal.clearParameters();
		cvdal.setSqlQuery(mainTableQuery);
		cvdal.setInt(1, primaryTableID.intValue());
		Collection mainTableResults = cvdal.executeQuery();
		cvdal.setSqlQueryToNull();
		if (mainTableResults != null)
		{
		  Iterator mainTableIterator = mainTableResults.iterator();
		  //Only get the first one.
		  if (mainTableIterator.hasNext())
		  {
			HashMap mainTableHashMap = (HashMap) mainTableIterator.next();
			primaryTable = (String) mainTableHashMap.get("TableName");
			primaryKey = (String) mainTableHashMap.get("TablePrimaryKey");
			if (!allTables.contains(primaryTable))
			{
			  allTables.add(primaryTable);
			} //end of if statement (!tables.contains(tableName))
		  } //end of while loop (mainTableIterator.hasNext())
		} //end of if statement (mainTableResults != null)
		//end of Get the Primary Table for this module.

		//Get the Primary Table for this module.
		mainTableQuery = "SELECT sf.RealFieldName, sf.SearchTableID , sf.DisplayName , st.TableName, sf.RelationshipQuery, " +					" sf.IsOnGlobalReplaceTable, sf.RealTableName, sf.SubRelationshipQuery FROM searchtable st,searchfield sf where sf.SearchTableID "+
					" = st.SearchTableID and sf.searchFieldID = ?";
		cvdal.setSqlQueryToNull();
		cvdal.clearParameters();
		cvdal.setSqlQuery(mainTableQuery);
		cvdal.setInt(1, replaceFieldID);
		mainTableResults = cvdal.executeQuery();
		cvdal.setSqlQueryToNull();
		if (mainTableResults != null)
		{
		  Iterator mainTableIterator = mainTableResults.iterator();
		  //Only get the first one.
		  if (mainTableIterator.hasNext())
		  {
			HashMap mainTableHashMap = (HashMap) mainTableIterator.next();
			relateTable = (String) mainTableHashMap.get("TableName");
			relateKey = (String) mainTableHashMap.get("RealFieldName");
			queryTableID = ((Number) mainTableHashMap.get("SearchTableID")).intValue();
			displayName = (String) mainTableHashMap.get("DisplayName");
			relationShipQuery = (String)mainTableHashMap.get("RelationshipQuery");
			isOnGlobalReplaceTable = (String)mainTableHashMap.get("IsOnGlobalReplaceTable");
			realTableName = (String)mainTableHashMap.get("RealTableName");
			subRelationShipQuery = (String)mainTableHashMap.get("SubRelationshipQuery");
			
			if (!allTables.contains(primaryTable))
			{
			  allTables.add(primaryTable);
			} //end of if statement (!tables.contains(tableName))
		  } //end of while loop (mainTableIterator.hasNext())
		} //end of if statement (mainTableResults != null)
		//end of Get the Primary Table for this module.
		
		// Update the Custom Field Table with its values.
		if(fieldTableID == GlobalReplaceConstantKeys.CUSTOM_FIELD_TABLEID ){
			for (int i = 0; i < resultsIDs.size(); i++)
			{
				int recordID =((Number)resultsIDs.get(i)).intValue();
				if(replaceFieldType == GlobalReplaceConstantKeys.FIELD_TYPE_MULTIPLE){
					String finalTableQuery = "UPDATE customfieldmultiple SET ValueID = ? WHERE " +					" CustomFieldID = ? AND RecordID in (?) ";
					cvdal.setSqlQueryToNull();
					cvdal.clearParameters();
					cvdal.setSqlQuery(finalTableQuery);
					cvdal.setString(1, fieldValue);
					cvdal.setInt(2, replaceFieldID);
					cvdal.setInt(3, recordID);
					int rowsAffected = cvdal.executeUpdate();
					if(rowsAffected == 0){
						batchQuery.add("insert into customfieldmultiple " +
						"(customfieldid,recordid,valueid) values " +
						"(" + replaceFieldID + "," + recordID + "," + fieldValue +
						")");
					}//end of if(rowsAffected == 0)
				}//end of if(replaceFieldType == GlobalReplaceConstantKeys.FIELD_TYPE_MULTIPLE)
				else{
					String finalTableQuery = "UPDATE customfieldscalar SET Value = ? "
								+" cf.CustomFieldID = ? AND RecordID in (?) ";
					cvdal.setSqlQueryToNull();
					cvdal.clearParameters();
					cvdal.setSqlQuery(finalTableQuery);
					cvdal.setString(1, fieldValue);
					cvdal.setInt(2, replaceFieldID);
					cvdal.setInt(3, recordID);
					int rowsAffected = cvdal.executeUpdate();
					if(rowsAffected == 0){
						batchQuery.add("insert into customfieldscalar " +
						"(customfieldid,recordid,value) values " + "(" +
						replaceFieldID + "," + recordID + ",'" +
						fieldValue + "')");
					}//end of if(rowsAffected == 0)
				}//end of else for if(replaceFieldType == GlobalReplaceConstantKeys.FIELD_TYPE_MULTIPLE)
			}//end of for (int i = 0; i < resultsIDs.size(); i++)
		}else {
			updateField = relateTable + "." + relateKey;
			String thisRelationship = "";

			//We must check wheather the field is occuring in the table.
			// If it not occuring in the table then  we must have to get the alias table name &
			// collect the Relationship to that table build a relationship information.
			if (isOnGlobalReplaceTable != null && isOnGlobalReplaceTable.equals("N")){

				//We must have to do this because the tablename is attached to the field name.
				updateField = relateKey;

				if (!allTables.contains(realTableName))
				{
					allTables.add(realTableName);
				} //end of if (!allTables.contains(realTableName))

				// alias determination
				Collection alias = new ArrayList();
				StringTokenizer aliasCommaTokens = new StringTokenizer(realTableName, ",");
				while(aliasCommaTokens.hasMoreTokens()){
					String aliasRealTable = aliasCommaTokens.nextToken();
					StringTokenizer aliasTokens = new StringTokenizer(aliasRealTable, " ");
					String tempTableName = aliasTokens.nextToken();
					if(aliasTokens.hasMoreTokens())
					{
						alias.add(aliasTokens.nextToken());
					}//end of if(aliasTokens.hasMoreTokens())
				}//end of while(aliasCommaTokens.hasMoreTokens())

				if (relationShipQuery != null)
				{
					thisRelationship = relationShipQuery;
					StringTokenizer relationshipTokens = new StringTokenizer(relationShipQuery, " ");
					while (relationshipTokens.hasMoreTokens())
					{
					  String thisToken = relationshipTokens.nextToken();
					  int index = thisToken.indexOf(".");
					  if (index > -1)
					  {
						String tableName = thisToken.substring(0, index);
						//Incase if you added the new line to the Query then before
						//check for the occurance. We will eliminate the new line return character.
						tableName = tableName.replaceAll("\n","");
						if ((!allTables.contains(tableName)) && (!alias.contains(tableName)))
						{
						  allTables.add(tableName);
						} //end of if statement (!tables.contains(tableName))
					  } //end of if statement (index > -1)
					} //end of while loop (relationshipTokens.hasMoreTokens())
				  } //end of if statement (thisRelationship != null)
				  
				  if(subRelationShipQuery != null && !subRelationShipQuery.equals("")){
					thisRelationship += subRelationShipQuery;
				  }
			}//end of if (isOnTable != null && isOnTable.equals("N"))


			// We must have to identify the relation of the table
			// incase for some reason if the record is not occuring for the table then
			// we must have add new record for that related table.
			int index = updateField.indexOf(".");
			int insertType = 0;

			if (index > -1)
			{
				int lenField = updateField.length();
				String tableName = updateField.substring(0, index);
				if (tableName != null && tableName.startsWith("moc")){
					insertType = GlobalReplaceConstantKeys.METHOD_OF_CONTACT_TABLEID;
				}
				if (tableName != null && tableName.startsWith("address")){
					insertType = GlobalReplaceConstantKeys.ADDRESS_TABLEID;
					fieldName = updateField.substring(index+1,lenField);
				}

			} //end of if statement (index > -1)

			//building the final query
			StringBuffer selectQuery = new StringBuffer();
			selectQuery.append("UPDATE ");
			Iterator thisTableIterator = allTables.iterator();
			while (thisTableIterator.hasNext())
			{
			  String currentTable = (String) thisTableIterator.next();
			  selectQuery.append(currentTable);
			  if (thisTableIterator.hasNext())
			  {
				selectQuery.append(", ");
			  } //end of if statement (thisTableIterator.hasNext())
			} //end of while loop (thisTableIterator.hasNext())
			selectQuery.append(" SET " + updateField + " = ? ");
			selectQuery.append(" WHERE ");
			if (thisRelationship != null && thisRelationship.length() > 0)
			{
				selectQuery.append(thisRelationship);
				selectQuery.append(" AND ");
			} //end of if statement (thisRelationship != null ...
			selectQuery.append( primaryTable + "." + primaryKey + " in ( ? )");

			for (int i = 0; i < resultsIDs.size(); i++)
			{
				int recordID =((Number)resultsIDs.get(i)).intValue();
				cvdal.setSqlQueryToNull();
				cvdal.clearParameters();
				cvdal.setSqlQuery(selectQuery.toString());
				cvdal.setString(1,fieldValue);
				cvdal.setInt(2,recordID);
				int rowsAffected = cvdal.executeUpdate();
				if(rowsAffected == 0){
					HashMap fieldDetails = new HashMap();
					fieldDetails.put("contactID",new Integer(recordID));
					fieldDetails.put("contactType",primaryTableID);
					fieldDetails.put("insertType",new Integer(insertType));
					fieldDetails.put("fieldName",fieldName);
					fieldDetails.put("fieldValue",fieldValue);
					this.insertGlobalReplaceRecord(fieldDetails,cvdal);
				}
			}//end of for (int i = 0; i < resultsIDs.size(); i++)
		}//end of else block

		// In-case if we are updating the individual's List then we must have to update the entity's List ID
		// same thing for entity.
		if(displayName != null && displayName.equals("Marketing List") && queryTableID != 0){
			
			//intialize the ContactHelperLocal
			ContactHelperLocalHome contactHelperLocalHome = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal contactHelperLocal =  contactHelperLocalHome.create();
			contactHelperLocal.setDataSource(this.dataSource);

			ArrayList recordIDs= new ArrayList();			
			for (int i = 0; i < resultsIDs.size(); i++)
			{
				int recordID =((Number)resultsIDs.get(i)).intValue();
				// If we are processing the Individual then we must have to get 
				// Entity ID. So that we can move the entity as well as all associated individual 
				// to the new List selected by the user. 
				if(queryTableID == 2){
					recordID = contactHelperLocal.getEntityIDForIndividual(recordID);
				}//end of if(queryTableID == 2)
				recordIDs.add(recordID+"");
			}//end of for (int i = 0; i < resultsIDs.size(); i++)
			String entityIDs[] = new String[recordIDs.size()];
			for(int i = 0; i < recordIDs.size(); i++){
				entityIDs[i]= (String) recordIDs.get(i);
			}//end of for(int i = 0; i < recordIDs.size(); i++)
			int marketingListID = Integer.parseInt(fieldValue); 
			if(marketingListID != 0 && entityIDs.length != 0  ){
				// Reason for adminIndividualID to set as -1. We know that admin is a super user 
				// he can move any thing. thats why its hard coded to -1 
				int adminIndividualID = -1;
				contactHelperLocal.entityMove(adminIndividualID, marketingListID, entityIDs );
			}//end of if(marketingListID != 0 && entityIDs.length != 0  )			
		}//end of if(displayName != null && displayName.equals("Marketing List") && queryTableID != 0)

		// inserting the batched query to the database.
		int[] batchResult = cvdal.batchProcess(batchQuery);

	  } //end of try block
	  catch (Exception e)
	  {
		logger.error("[Exception] GlobalReplaceEJB.performGlobalReplace:", e);
		updateFlag = false;
	  } //end of catch block (Exception)
	  finally
	  {
		cvdal.destroy();
		cvdal = null;
	  } //end of finally block
	  return updateFlag;
	} //end of performGlobalReplace method

	/**
	 * The Record which weren't affected when we tried to update the fields information for a record. For those records we will insert new row in there corresponding related tables.
	 * for Example:Method Of contact for that entity/Individual Phone is not occuring then we will Add a new entry for that method of contact
	 *
	 *
	 * @param fieldInfo The replaceInfo its a collection of information
	 * 		1) contactID The contactID identification of the record which is getting created.
	 *      2) contactType  the type will define either entity/individual
	 *      3) insertType  The unique way to identify wheather we will be updating the method of contact/Address ..etc.,
	 *		4) fieldValue The value which will be replaced on the selected Field.
	 *		5) fieldName Name of the Field which is getting Replaced.
	 *
	 * @return void
	 */
	private void insertGlobalReplaceRecord(HashMap fieldInfo,CVDal cvdal)
	{
		int contactID = ((Integer) fieldInfo.get("contactID")).intValue();
		int contactType = ((Integer) fieldInfo.get("contactType")).intValue();
		int insertType = ((Integer) fieldInfo.get("insertType")).intValue();
		String fieldName = (String) fieldInfo.get("fieldName");
		String fieldValue = (String) fieldInfo.get("fieldValue");

		try{
			if(insertType == GlobalReplaceConstantKeys.ADDRESS_TABLEID){
				try
				{
				  cvdal.setSqlQuery("insert into address (AddressType,"+fieldName+" )values (?,?)");
				  cvdal.setInt(1, 1);
				  cvdal.setString(2, fieldValue);
				  cvdal.executeUpdate();
				  int addressID = cvdal.getAutoGeneratedKey();
				  cvdal.clearParameters();
				  if(addressID != 0){
					  batchQuery.add("insert into addressrelate(Address,ContactType,Contact,"
						+ "AddressType,IsPrimary) values("
						+ addressID + ","+contactType+"," + contactID + ",1,'YES')");
				  }
				}
				catch (Exception e)
				{
					logger.error("[Exception] GlobalReplaceEJB.insertGlobalReplaceRecord:", e);
				}
			}
			if(insertType == GlobalReplaceConstantKeys.METHOD_OF_CONTACT_TABLEID){
				try
				{
					int mocTypeID = -1;
					String sqlQuery = "SELECT MOCTypeID " +
						" FROM moctype WHERE upper(Name) = upper(?) ";
					cvdal.setSqlQuery(sqlQuery);
					cvdal.setString(1,fieldName);
					Collection resultsCollection = cvdal.executeQuery();
					if (resultsCollection != null)
					{
					  Iterator resultsIterator = resultsCollection.iterator();
					  while (resultsIterator.hasNext())
					  {
						HashMap resultsHashMap = (HashMap) resultsIterator.next();
						mocTypeID = ((Number) resultsHashMap.get("MOCTypeID")).intValue();
					  }
					}
					if (mocTypeID != -1){
						cvdal.setSql("contact.addmoc");
						cvdal.setInt(1, mocTypeID);
						cvdal.setString(2, fieldValue);
						cvdal.setString(3, ""); //moc.getNote()
						cvdal.setString(4, "");
						cvdal.setString(5, null); //moc.getMocOrder()
						cvdal.executeUpdate();
						int mocID = cvdal.getAutoGeneratedKey();
						cvdal.clearParameters();
						if (mocID != 0)
						{
							batchQuery.add("insert into mocrelate(MOCID,ContactType," +
							"ContactID,isPrimary) values(" + mocID + ","+contactType+"," +
							contactID + ",'YES')");
						}
					}
				}
				catch (Exception e)
				{
					logger.error("[Exception] GlobalReplaceEJB.insertGlobalReplaceRecord:", e);
				}
			}
		} //end of try block
		catch (Exception e)
		{
			logger.error("[Exception] GlobalReplaceEJB.insertGlobalReplaceRecord:", e);			
		} //end of catch block (Exception)
	}//end of insertGlobalReplaceRecord
}
 //End of Class

