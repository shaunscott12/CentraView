/*
 * $RCSfile: TheItem.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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
 * <p>Title:  TheItem </p>
 *
 * <p>Description: This is ItemValue Object which represent the
 * table/field data for select boxes on View/New/Edit report screens
 * or selected tables and fields on these screens.</p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public class TheItem implements Serializable
{
    private int parentId;     // ID of a parent (if it is a field this will be the tableid) unused for tables.
    private int   id;         // ID of table or field
    private String fullName;  // Full name of table or field

    /**
     * TheItem
     *
     * @param id int
     * @param name String
     * @param fullName String
     */
    public TheItem(int id, String fullName)
    {
        this.id = id;
        if ( fullName != null )
            this.fullName = fullName;
        else
            this.fullName = "";
    }

    /**
     * TheItem
     */
    public TheItem()
    {
        this.fullName = "";
    }

    /**
     * getId
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * setId
     *
     * @param id int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getFullName
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * setFullName
     *
     * @param fullName String
     */
    public void setFullName(String fullName) {
        if ( fullName != null )
            this.fullName = fullName;
        else
            this.fullName = "";
    }
    
    /**
     * @return Returns the parentId.
     */
    public int getParentId()
    {
      return this.parentId;
    }
    
    /**
     * @param parentId The parentId to set.
     */
    public void setParentId(int parentId)
    {
      this.parentId = parentId;
    }
}
