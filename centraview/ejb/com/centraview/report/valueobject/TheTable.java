/*
 * $RCSfile: TheTable.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:04 $ - $Author: mking_cv $
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

/**
 * <p>Title:  TheTable extends TheItem </p>
 *
 * <p>Description: This is TableValue Object which represent the
 * table data for select boxes on View/New/Edit report screens.
 * </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public class TheTable extends TheItem
{
    ArrayList fields;       // List of TheItem objects stored table fields data.

    /**
     * TheTable
     *
     * @param id int
     * @param name String
     * @param fullName String
     */
    public TheTable(int id, String fullName)
    {
        super(id, fullName);
        fields = new ArrayList();
    }

    /**
     * TheTable
     */
    public TheTable()
    {
        super();
        fields = new ArrayList();
    }

    /**
     * addItem
     *
     * @param item TheItem
     */
    public void addItem(TheItem item)
    {
        fields.add(item);
    }

    /**
     * getItem
     *
     * @param i int
     * @return TheItem
     */
    public TheItem getItem(int i )
    {
        return (TheItem)fields.get(i);
    }

    /**
     * size
     *
     * @return int
     */
    public int size() {
        return fields.size();
    }

    /**
     * getFields
     *
     * @return ArrayList
     */
    public ArrayList getFields() {
        return fields;
    }

    /**
     * setFields
     *
     * @param fields ArrayList
     */
    public void setFields(ArrayList fields) {
        this.fields = fields;
    }

}
