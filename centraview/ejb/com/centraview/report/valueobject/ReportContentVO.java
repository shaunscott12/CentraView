/*
 * $RCSfile: ReportContentVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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

/**
 * <p>Title:  ReportContentVO </p>
 *
 * <p>Description: This is ReportContentValue Object which represent the
 * Report Content data. </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public class ReportContentVO implements Serializable
{
    private int tableId;       // Table ID
    private int fieldId;       // Field ID
    private String name;       // Full Field Name
    private Byte sortSeq;      // Sequence od sort
    private String sortOrder;  // Sort order

    /**
     * ReportContentVO
     *
     * @param fieldId int
     * @param name String
     * @param sortSeq Byte
     * @param sortOrder String
     */
    public ReportContentVO(int fieldId, int tableId, String name, Byte sortSeq, String sortOrder)
    {
        this.fieldId = fieldId;
        this.tableId = tableId;
        this.name = name;
        this.sortSeq = sortSeq;
        this.sortOrder = sortOrder;
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
     * setFieldId
     *
     * @param fieldId int
     */
    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    /**
     * getFieldId
     *
     * @return int
     */
    public int getFieldId() {
        return fieldId;
    }

    /**
     * getSortOrder
     *
     * @return String
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * setSortOrder
     *
     * @param sortOrder String
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * getSortSeq
     *
     * @return Byte
     */
    public Byte getSortSeq() {
        return sortSeq;
    }

    /**
     * setSortSeq
     *
     * @param sortSeq Byte
     */
    public void setSortSeq(Byte sortSeq) {
        this.sortSeq = sortSeq;
    }
    
    /**
     * @return Returns the tableId.
     */
    public int getTableId()
    {
      return this.tableId;
    }
    
    /**
     * @param tableId The tableId to set.
     */
    public void setTableId(int tableId)
    {
      this.tableId = tableId;
    }
    
    public String toString()
    {
      String string = "ReportContentVO: {tableid: "+tableId+", fieldId: "+fieldId+", name: "+name+"}";
      return string;
    }
    
}