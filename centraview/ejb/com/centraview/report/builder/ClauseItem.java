/*
 * $RCSfile: ClauseItem.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:55 $ - $Author: mking_cv $
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

package com.centraview.report.builder;

/**
 *
 * <p>Title: ClauseItem class </p>
 * <p>Description: Class for representation of standard report single where clause item</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Kalmychkov Alexi
 * @version 1.0
 * @date 01/13/04
 */

public class ClauseItem {
    private String andOr;
    private String fieldName;
    private String clause;
    private String preValue;
    private String value;
    private String postValue;

    public ClauseItem(String andOr,
                      String fieldName,
                      String clause,
                      String preValue,
                      String value,
                      String postValue) {

        this.andOr     = andOr;
        this.fieldName = fieldName;
        this.clause    = clause;
        this.preValue  = preValue;
        this.value     = value;
        this.postValue = postValue;

    }
    public String getAndOr() {
        return andOr;
    }
    public String getClause() {
        return clause;
    }
    public String getFieldName() {
        return fieldName;
    }
    public String getPostValue() {
        return postValue;
    }
    public String getPreValue() {
        return preValue;
    }
    public String getValue() {
        return value;
    }
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

}
