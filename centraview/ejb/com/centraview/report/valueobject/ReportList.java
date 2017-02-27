/*
 * $RCSfile: ReportList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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

package com.centraview.report.valueobject;

import java.util.ArrayList;
import java.util.HashMap;

import com.centraview.common.DisplayList;
import com.centraview.common.EntityListElement;

/**
 * <p>Title:  ReportList </p>
 *
 * <p>Description: This class is for storing the Report List. </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */


public class ReportList extends DisplayList
{
    private String sortMember;
    private String primaryMember;
    private String primaryTable;
    private String PrimaryMemberType;
    private String searchString;
    private int totalNoofRecords;
    private int beginIndex;
    private int endIndex;
    private int startAT;
    private int endAT;
    protected static boolean dirtyFlag = false;
    private int reportType;
    private int moduleId;

    /**
     * ReportList
     */
    public ReportList()
    {
        columnMap = new HashMap();
        columnMap.put("ReportID", new Integer(10));
        columnMap.put("Name", new Integer(100));
        columnMap.put("Description", new Integer(255));
        setPrimaryMember("ReportID");
    }

    /**
     * getDirtyFlag
     *
     * @return boolean
     */
    public boolean getDirtyFlag()
    {
        return dirtyFlag;
    }

    /**
     * setDirtyFlag
     *
     * @param value boolean
     */
    public void setDirtyFlag(boolean value)
    {
        dirtyFlag = value;
    }

    /**
     * getStartAT
     *
     * @return int
     */
    public int getStartAT()
    {
        return startAT;
    }

    /**
     * getEndAT
     *
     * @return int
     */
    public int getEndAT()
    {
        return endAT;
    }

    /**
     * getBeginIndex
     *
     * @return int
     */
    public int getBeginIndex()
    {
        return beginIndex;
    }

    /**
     * getEndIndex
     *
     * @return int
     */
    public int getEndIndex()
    {
        return endIndex;
    }

    /**
     * setRecordsPerPage
     *
     * @param value int
     */
    public void setRecordsPerPage(int value)
    {
        recordsPerPage = value;
    }

    /**
     * setTotalNoOfRecords
     *
     * @param value int
     */
    public void setTotalNoOfRecords(int value)
    {
        totalNoOfRecords = value;
    }

    /**
     * setListID
     *
     * @param value long
     */
    public void setListID(long value)
    {
        super.ListID = value;
    }

    /**
     * getColumnMap
     *
     * @return HashMap
     */
    public HashMap getColumnMap()
    {
        return columnMap;
    }

    /**
     * setSortMember
     *
     * @param value String
     */
    public void setSortMember(String value)
    {
        sortMember = value;
    }

    /**
     * setListType
     *
     * @param value String
     */
    public void setListType(String value)
    {
        super.listType = value;
    }

    /**
     * getSearchString
     *
     * @return String
     */
    public String getSearchString()
    {
        return searchString;
    }

    /**
     * setSearchString
     *
     * @param value String
     */
    public void setSearchString(String value)
    {
        searchString = value;
    }

    /**
     * getListType
     *
     * @return String
     */
    public String getListType()
    {
        return listType;
    }

    /**
     * getPrimaryMemberType
     *
     * @return String
     */
    public String getPrimaryMemberType()
    {
        return PrimaryMemberType;
    }

    /**
     * setPrimaryMemberType
     *
     * @param value String
     */
    public void setPrimaryMemberType(String value)
    {
        PrimaryMemberType = value;
    }

    /**
     * getTotalNoofRecords
     *
     * @return int
     */
    public int getTotalNoofRecords()
    {
        return totalNoofRecords;
    }

    /**
     * getPrimaryTable
     *
     * @return String
     */
    public String getPrimaryTable()
    {
        return primaryTable;
    }

    /**
     * getSortMember
     *
     * @return String
     */
    public String getSortMember()
    {
        return sortMember;
    }

    /**
     * deleteElement
     *
     * @param s String
     */
    public void deleteElement(String s)
    {
    }

    /**
     * addElement
     *
     * @param ID String
     * @param value EntityListElement
     */
    public void addElement(String ID, EntityListElement value)
    {
        put(ID, value);
    }

    /**
     * duplicateElement
     */
    public void duplicateElement()
    {
    }

    /**
     * setPrimaryMember
     *
     * @param value String
     */
    public void setPrimaryMember(String value)
    {
        primaryMember = value;
    }

    /**
     * setPrimaryTable
     *
     * @param value String
     */
    public void setPrimaryTable(String value)
    {
        primaryTable = value;
    }

    /**
     * addEntityElement
     *
     * @param entitylistelement EntityListElement
     */
    public void addEntityElement(EntityListElement entitylistelement)
    {
    }

    /**
     * getEntityElement
     *
     * @param s String
     */
    public void getEntityElement(String s)
    {
    }

    /**
     * setStartAT
     *
     * @param startAT int
     */
    public void setStartAT(int startAT)
    {
        this.startAT = startAT;
    }

    /**
     * setEndAT
     *
     * @param EndAt int
     */
    public void setEndAT(int EndAt)
    {
        endAT = EndAt;
    }

    /**
     * setBeginIndex
     *
     * @param beginIndex int
     */
    public void setBeginIndex(int beginIndex)
    {
        this.beginIndex = beginIndex;
    }

    /**
     * setEndIndex
     *
     * @param endIndex int
     */
    public void setEndIndex(int endIndex)
    {
        this.endIndex = endIndex;
    }

    /**
     * getPrimaryMember
     *
     * @return String
     */
    public String getPrimaryMember()
    {
        return primaryMember;
    }

    /**
     * setReportType
     *
     * @param value int
     */
    public void setReportType(int value)
    {
        reportType = value;
    }

    /**
     * getReportType
     *
     * @return int
     */
    public int getReportType()
    {
        return reportType;
    }

    /**
     * setModuleId
     *
     * @param value int
     */
    public void setModuleId(int value)
    {
        moduleId = value;
    }

    /**
     * getModuleId
     *
     * @return int
     */
    public int getModuleId()
    {
        return moduleId;
    }

	/* (non-Javadoc)
	 * @see com.centraview.common.DisplayList#deleteElement(int, java.lang.String)
	 */
    public void deleteElement(int indvID, String key)
    {
    // TODO Auto-generated method stub

    }

	public ArrayList deleteElement( int indvID, String rowId[] ) 
	{
		ArrayList resultDeleteLog = new ArrayList();
		return resultDeleteLog;
	}//end of deleteElement( int indvID, String rowId[] ) throws CommunicationException,NamingException


}
