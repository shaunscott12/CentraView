/*
 * $RCSfile: ReportVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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
import java.sql.Date;
import java.util.ArrayList;


/**
 * <p>Title:  ReportVO </p>
 *
 * <p>Description: This is ReportValue Object which represent the
 * Report data. </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */


public class ReportVO implements Serializable
{
    private int reportId;             // Report ID
    private int moduleId;             // Module ID
    private String name;              // Report Name
    private String description;       // Report Description
    private Date from;                // Start date
    private Date to;                  // End date
    private ArrayList selectedFields;  // Selected field (TheItem object) list
    private ArrayList searchFields;   // Selected field (TheSearchItem object) list for search criteria
    private SelectVO select;          // initial data for select boxes

    /**
     * ReportVO
     */
    public ReportVO()
    {
        selectedFields = new ArrayList();
        searchFields = new ArrayList();
    }

    /**
     * getDescription
     *
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * setDescription
     *
     * @param description String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * setFrom
     *
     * @param from Date
     */
    public void setFrom(Date from) {
        this.from = from;
    }

    /**
     * getFrom
     *
     * @return Date
     */
    public Date getFrom() {
        return from;
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
     * setName
     *
     * @param name String
     */
    public void setName(String name) {
        this.name = name;
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
     * getReportId
     *
     * @return int
     */
    public int getReportId() {
        return reportId;
    }

    /**
     * getSearchFields
     *
     * @return ArrayList
     */
    public ArrayList getSearchFields() {
        return searchFields;
    }

    /**
     * setSearchFields
     *
     * @param searchFields ArrayList
     */
    public void setSearchFields(ArrayList searchFields) {
        this.searchFields = searchFields;
    }

    /**
     * setSelect
     *
     * @param select SelectVO
     */
    public void setSelect(SelectVO select) {
        this.select = select;
    }

    /**
     * getSelect
     *
     * @return SelectVO
     */
    public SelectVO getSelect() {
        return select;
    }

    /**
     * getSeletedFields
     *
     * @return ArrayList
     */
    public ArrayList getSelectedFields() {
        return selectedFields;
    }

    /**
     * setSeletedFields
     *
     * @param seletedFields ArrayList
     */
    public void setSelectedFields(ArrayList seletedFields) {
        this.selectedFields = seletedFields;
    }

    /**
     * getTo
     *
     * @return Date
     */
    public Date getTo() {
        return to;
    }

    /**
     * setTo
     *
     * @param to Date
     */
    public void setTo(Date to) {
        this.to = to;
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
    
    public String toString()
    {
      String string = "ReportVO: {reportId: "+reportId+", moduleId: "+moduleId+", "+
                      "name: "+name+", description: "+description+", selectedFields: "+selectedFields+"}";
      return string;
    }

}
