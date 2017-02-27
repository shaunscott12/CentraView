/*
 * $RCSfile: FileList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:54 $ - $Author: mking_cv $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.DisplayList;
import com.centraview.common.EntityListElement;

/**
 * This class stores the properties of the file list
 *
 *
 * Creation date: 17 July 2003
 * @author: Amit Gandhe
 */
public class FileList extends DisplayList
{
	private static Logger logger = Logger.getLogger(FileList.class);

	/**
	 *
	 * Constructor
	 */
	public FileList()
	{
		columnMap=new HashMap();
		columnMap.put("Name",new Integer(5));
		columnMap.put("Description",new Integer(10));
		columnMap.put("Created",new Integer(45));
		columnMap.put("Updated",new Integer(45));
		columnMap.put("CreatedBy",new Integer(10));
		setPrimaryMember("Name");
	}


	/**
	 * gets the dirty flag
	 *
	 * @return    boolean
	 */
	public boolean getDirtyFlag()
    {
        return dirtyFlag;
    }


	/**
	 * sets the Dirty Flag
	 *
	 * @param   flag
	 */
    public void setDirtyFlag(boolean flag)
    {
        dirtyFlag = flag;
    }


	/**
	 * gets the StartAt
	 *
	 * @return     int
	 */
    public int getStartAT()
    {
        return startAT;
    }


	/**
	 * gets the End At
	 *
	 * @return  int
	 */
    public int getEndAT()
    {
        return endAT;
    }


	/**
	 * gets The Begin Index
	 *
	 * @return     int
	 */
    public int getBeginIndex()
    {
        return beginIndex;
    }


	/**
	 *
	 * gets the End Index
	 * @return   int
	 */
    public int getEndIndex()
    {
        return endIndex;
    }


	/**
	 * sets the RecordsPerpage
	 *
	 * @param   i
	 */
    public void setRecordsPerPage(int i)
    {
        recordsPerPage = i;
    }


	/**
	 * sets the TotalNoOfrecords
	 *
	 * @param   i
	 */
    public void setTotalNoOfRecords(int i)
    {
        totalNoOfRecords = i;
    }


	/**
	 * sets the List Id
	 *
	 * @param   l
	 */
    public void setListID(long l)
    {
        super.ListID = l;
    }


	/**
	 * gets the ColumnMap
	 *
	 * @return   HashMap
	 */
    public HashMap getColumnMap()
    {
        return columnMap;
    }


	/**
	 * sets the sort member
	 *
	 * @param   s
	 */
    public void setSortMember(String s)
    {
        sortMember = s;
    }


	/**
	 * sets the list type
	 *
	 * @param   s
	 */


    public void setListType(String s)
    {
        super.listType = s;
    }


	/**
	 * sets the Search String
	 *
	 * @return    String
	 */
    public String getSearchString()
    {
        return searchString;
    }


	/**
	 * sets the Search String
	 *
	 * @param   s
	 */


    public void setSearchString(String s)
    {
        searchString = s;
    }

	/**
	 * gets the getListType
	 *
	 * @return   String
	 */

    public String getListType()
    {
        return listType;
    }


	/**
	 * gets the PrimaryMember type
	 *
	 * @return     String
	 */
    public String getPrimaryMemberType()
    {
        return PrimaryMemberType;
    }


	/**
	 * sets the Primary Member Type
	 *
	 * @param   s
	 */
    public void setPrimaryMemberType(String s)
    {
        PrimaryMemberType = s;
    }


	/**
	 * gets the Total No of Records
	 *
	 * @return     int
	 */
    public int getTotalNoofRecords()
    {
        return totalNoofRecords;
    }


	/**
	 * gets Primary Table
	 *
	 * @return    String
	 */
    public String getPrimaryTable()
    {
        return primaryTable;
    }


	/**
	 * gets the Sort Member
	 *
	 * @return   String
	 */
    public String getSortMember()
    {
        return sortMember;
    }

	public void deleteElement(int i, String foo)
	{
	   // must implement abstract method from super class.
	}

	/**
	 *
	 * This method calls the methods of CvFileFacade depending upon whether it is a file or folder
	 * @param   userID
	 * @param   key
	 * @param   typeOfDoc
	 * @param   currFolderID
	 */
	public void deleteElement(int userID,String key,String typeOfDoc)
    {
		int elementID = Integer.parseInt(key);

		CvFileFacade cvFFacade=new CvFileFacade();

		try
		{
			if (typeOfDoc.equals("FILE"))
			{
				cvFFacade.deleteFile(userID, elementID, this.dataSource);
			}
			else if (typeOfDoc.equals("FOLDER"))
			{
				System.out.println("FOLDER");
				cvFFacade.deleteFolder(userID, elementID, this.dataSource);
			}
		}
		catch(Exception e )
		{
			logger.error("[Exception] FileList.deleteElement( int indvID, String key )", e);
		}
		this.setDirtyFlag(true);
    }

	/**
	  * We will process the rowId. Incase if we don't have the right to DELETE a record then it will raise the AuthorizationException.
	  * We will catch the Exception and Log the Description of the Exception.
	  *
	  * @param individualID  ID for the Individual who is try to delete the record.
	  * @param recordID[] A String array of the recordID which we are try to delete it from database.
	  * @return resultDeleteLog A Collection of the Error Message while deleting a particular record.
	  */
	public ArrayList deleteElement(int individualID, String recordID[]) throws CommunicationException,NamingException
	{
		ArrayList resultDeleteLog = new ArrayList();
		CvFileFacade cvFFacade=new CvFileFacade();
		//call to EJB server
		for (int i=0; i<recordID.length; i++)
		{
			if(recordID[i] != null && !recordID[i].equals("")){

				String fileID = null;
				String typeOfDoc = null;
				if (recordID[i].indexOf("#") == -1) // if -1 then the rowId doesn't have # and we should assume id = rowId
				{
					fileID = recordID[i];
					typeOfDoc = "FILE";
				}
				else
				{
					fileID=recordID[i].substring(0,recordID[i].indexOf("#"));//parse the string to get the ID and Type(File or folder)
					typeOfDoc=recordID[i].substring(recordID[i].indexOf("#")+1);
				}
								
				if(fileID != null && !fileID.equals("")){
					int elementID = Integer.parseInt(fileID);
					try{
						
						if (typeOfDoc.equals("FILE"))
						{
							cvFFacade.deleteFile(individualID, elementID, this.dataSource);
						}
						else if (typeOfDoc.equals("FOLDER"))
						{
							System.out.println("FOLDER");
							cvFFacade.deleteFolder(individualID, elementID, this.dataSource);
						}
					}//end of try block
					catch(AuthorizationFailedException ae){
						String errorMessage = ae.getExceptionDescription();
						resultDeleteLog.add(errorMessage);
					}//end of catch block
				}//end of if(fileID != null && !fileID.equals(""))
			}//end of if(recordID[i] != null && !recordID[i].equals(""))
			
		}//end of for (int i=0; i<recordID.length; i++)
		this.setDirtyFlag(true);
		return resultDeleteLog;
	}// end of deleteElement
	
	/**
	 * Duplicate Element
	 *
	 */
    public void duplicateElement()
    {
    }


	/**
	 * sets Primary Member
	 *
	 * @param   s
	 */
    public void setPrimaryMember(String s)
    {
        primaryMember = s;
    }


	/**
	 * sets the Primary Table
	 *
	 * @param   s
	 */
    public void setPrimaryTable(String s)
    {
        primaryTable = s;
    }


	/**
	 * adds an Entity Element
	 *
	 * @param   entitylistelement
	 */
    public void addEntityElement(EntityListElement entitylistelement)
    {
    }


	/**
	 * gets the EntityElement
	 *
	 * @param   s
	 */
    public void getEntityElement(String s)
    {
    }


	/**
	 * sets the Start At
	 *
	 * @param   i
	 */
    public void setStartAT(int i)
    {
        startAT = i;
    }


	/**
	 * sets the End AT
	 *
	 * @param   i
	 */
    public void setEndAT(int i)
    {
        endAT = i;
    }


	/**
	 * sets the Begin Index
	 *
	 * @param   i
	 */
    public void setBeginIndex(int i)
    {
        beginIndex = i;
    }


	/**
	 * sets the End Index
	 *
	 * @param   i
	 */
    public void setEndIndex(int i)
    {
        endIndex = i;
    }


	/**
	 * gets the Primary Member
	 *
	 * @return     String
	 */
    public String getPrimaryMember()
    {
        return primaryMember;
    }


	/**
	 *  sets the FileTypeRequest(my or all)
	 *
	 * @param   s
	 */
    public void setFileTypeRequest(String s)
    {
        fileTypeRequest = s;
    }


	/**
	 *
	 * gets the FileTypeRequest(my or all)
	 * @return     String
	 */
    public String getFileTypeRequest()
    {
        System.out.println("fileTypeRequest " + fileTypeRequest);
        return fileTypeRequest;
    }


	/**
	 * sets the vecDirectory vector
	 *
	 * @param   vec
	 */
	public void setDirectoryStructure(Vector vec)
	{
		this.vecDirectory=vec;

	}

	/**
	 * gets the Directory Structure
	 *
	 * @return   Vector
	 */

	public Vector getDirectoryStructure()
	{
		return vecDirectory;
	}

	/**
	 * sets whether to include system folders or not
	 *
	 *
	 */
	public void setSystemIncludeFlag(boolean value)
	{
		this.includeSystem=value;
	}

	 /**
	 * gets whether to include system folders or not
	 *
	 *
	 */

	public boolean getSystemIncludeFlag()
	{
		return includeSystem;
	}

	public void setCurrentFolderID(int curFolderID)
	{
		this.curFolderID=curFolderID;

	}
	public int getCurrentFolderID()
	{
		return this.curFolderID;

	}

	public void setFolderName(String folderName)
	{
		this.folderName=folderName;
	}

	public String getFolderName()
	{
		return this.folderName;
	}

	public Vector getAllColumns()
	{
		Vector vecColumns=new Vector();

		vecColumns.addElement("Name");
		vecColumns.addElement("Description");
		vecColumns.addElement("Created");
		vecColumns.addElement("Updated");
		vecColumns.addElement("CreatedBy");

		return vecColumns;

	}

    private String sortMember;
    private String primaryMember;
    private String primaryTable="cvfile";
    private String PrimaryMemberType="fileid";
    private int totalNoofRecords;
    private int beginIndex;
    private int endIndex;
    private int startAT;
    private int endAT;
    protected static boolean dirtyFlag = false;
    private String fileTypeRequest;
	private Vector vecDirectory=new Vector();
	private boolean includeSystem=false;
	private int curFolderID;
	private String folderName;
}