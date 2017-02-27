/*
 * $RCSfile: SelectVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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
import java.util.ArrayList;

/**
 * <p>Title:  SelectVO </p>
 *
 * <p>Description: This is SelectValue Object which represent the
 * initial data for select boxes on View/New/Edit report screens.</p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public class SelectVO implements Serializable
{
    ArrayList topTables;      // List of TheTable object for top available tables selectbox

    /**
     * SelectVO
     */
    public SelectVO()
    {
        topTables = new ArrayList();
    }

    /**
     * addTopTables
     *
     * @param item TheTable
     */
    public void addTopTables(TheTable item) {
        topTables.add(item);
    }

    /**
     * getTopTables
     *
     * @param i int
     * @return TheTable
     */
    public TheTable getTopTables(int i ) {
        return (TheTable)topTables.get(i);
    }

    /**
     * sizeTopTables
     *
     * @return int
     */
    public int sizeTopTables() {
        return topTables.size();
    }

    /**
     * setTopTables
     *
     * @param topTables ArrayList
     */
    public void setTopTables(ArrayList topTables) {
        this.topTables = topTables;
    }

    /**
     * getTopTables
     *
     * @return ArrayList
     */
    public ArrayList getTopTables() {
        return topTables;
    }
}
