/*
 * $RCSfile: ReportListElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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

import com.centraview.common.ListElement;
import com.centraview.common.ListElementMember;

/**
 * <p>Title:  ReportListElement </p>
 *
 * <p>Description: This class stores all columns for each row of Report. </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */

public class ReportListElement extends ListElement
{

    private int itsElementID;

    /**
     * ReportListElement
     *
     * @param ID int
     */
    public ReportListElement(int ID)
    {
        itsElementID = ID;
    }

    /**
     * addMember
     *
     * @param ID int
     * @param lem ListElementMember
     */
    public void addMember(int ID, ListElementMember lem)
    {
        put(new Integer(ID), lem);
    }

    /**
     * getMember
     *
     * @param memberName String
     * @return ListElementMember
     */
    public ListElementMember getMember(String memberName)
    {
        return null;
    }

    /**
     * setListAuth
     *
     * @param value char
     */
    public void setListAuth(char value)
    {
        super.auth = value;
    }

    /**
     * getListAuth
     *
     * @return char
     */
    public char getListAuth()
    {
        return auth;
    }

    /**
     * getElementID
     *
     * @return int
     */
    public int getElementID()
    {
        return itsElementID;
    }
}
