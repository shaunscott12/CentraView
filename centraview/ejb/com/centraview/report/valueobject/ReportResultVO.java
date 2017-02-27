/*
 * $RCSfile: ReportResultVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * <p>Title:  ReportResultVO </p>
 *
 * <p>Description: This is ReportResultValue Object which represent the
 * Report Results data. </p>
 */

public class ReportResultVO implements Serializable
{
    private int reportId;           // Report ID
    private int moduleId;           // Module ID
    private String name;            // Name of Report
    private String dateRange;       // Date range string
    private String searchCriteria;  // Search criteria string
    private ArrayList titles;       // Header of result table
    private ArrayList results;      // List of result table elements

    /**
     * ReportResultVO
     */
    public ReportResultVO()
    {
        titles = new ArrayList();
        results = new ArrayList();
    }

    /**
     * getTitles
     *
     * @return ArrayList
     */
    public ArrayList getTitles() {
        return titles;
    }

    /**
     * setTitles
     *
     * @param titles ArrayList
     */
    public void setTitles(ArrayList titles) {
        this.titles = titles;
    }

    /**
     * getResults
     *
     * @return ArrayList
     */
    public ArrayList getResults() {
        return results;
    }

    /**
     * setResults
     *
     * @param results ArrayList
     */
    public void setResults(ArrayList results) {
        this.results = results;
    }

    /**
     * getModuleId
     *
     * @return int
     */
    public int getModuleId() {
        return moduleId;
    }

    /**
     * setModuleId
     *
     * @param moduleId int
     */
    public void setModuleId(int moduleId) {
        this.moduleId = moduleId;
    }

    /**
     * setName
     *
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getName
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * getReportId
     *
     * @return int
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * setReportId
     *
     * @param reportId int
     */
    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    /**
     * setSearchCriteria
     *
     * @param searchCriteria String
     */
    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    /**
     * getSearchCriteria
     *
     * @return String
     */
    public String getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * setDateRange
     *
     * @param dateRange String
     */
    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    /**
     * setDateRange
     *
     * @param dateFrom java.sql.Date
     * @param dateTo   java.sql.Date
     */
    public void setDateRange(java.sql.Date dateFrom,java.sql.Date dateTo) {

        if (dateFrom != null && dateTo!=null) {

            SimpleDateFormat sdf = new SimpleDateFormat();
            this.dateRange = sdf.format(dateFrom) + " to " + sdf.format(dateTo);
    }

    }

    /**
     * getDateRange
     *
     * @return String
     */
    public String getDateRange() {
        return dateRange;
    }

}
