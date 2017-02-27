/*
 * $RCSfile: TheSearchItem.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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
 * <p>Title:  TheSearcItem </p>
 *
 * <p>Description: This is SearchItemValue Object which represent the
 * selected table/field data for search criteria on View/New/Edit report screens.
 * </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public class TheSearchItem implements Serializable
{
    private int   tableId;         // Table ID
    private int   fieldId;         // Field ID
    private int   conditionId;     // Condition(Expression) ID
    private String criteriaValue;  // Search criteria value
    private String andOr;          // AND or OR value

    /**
     * TheSearchItem
     */
    public TheSearchItem()
    {
    }

    /**
     * getAndOr
     *
     * @return String
     */
    public String getAndOr() {
        return andOr;
    }

    /**
     * setAndOr
     *
     * @param andOr String
     */
    public void setAndOr(String andOr) {
        this.andOr = andOr;
    }

    /**
     * setConditionId
     *
     * @param conditionId int
     */
    public void setConditionId(int conditionId) {
        this.conditionId = conditionId;
    }

    /**
     * getConditionId
     *
     * @return int
     */
    public int getConditionId() {
        return conditionId;
    }

    /**
     * getCriteriaValue
     *
     * @return String
     */
    public String getCriteriaValue() {
        return criteriaValue;
    }

    /**
     * setCriteriaValue
     *
     * @param criteriaValue String
     */
    public void setCriteriaValue(String criteriaValue) {
        this.criteriaValue = criteriaValue;
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
     * getTableId
     *
     * @return int
     */
    public int getTableId() {
        return tableId;
    }

    /**
     * setTableId
     *
     * @param tableId int
     */
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}
