/*
 * $RCSfile: EntityHomeFactory.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:00 $ - $Author: mking_cv $
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

package com.centraview.report.ejb.entity;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**********************************************************************************
 *
 *	Entity Bean home interface factory.
 *
 *	@version 1.0
 *	@date 12/26/03
 *
 **********************************************************************************/

public class EntityHomeFactory {
    /***********************************************************************************
     *
     *	Constants - JNDI names
     *
     ***********************************************************************************/

    /** Constant - JNDI name prefix for entity beans. */
    public static final String JNDI_PREFIX_ENTITY = "report/ejb/entity";


    /** Constant - JNDI name for the Report entity */
    public static final String JNDI_NAME_REPORT = JNDI_PREFIX_ENTITY + "/ReportLocal";

    /** Constant - JNDI name for the ReportType entity */
    public static final String JNDI_NAME_REPORT_TYPE = JNDI_PREFIX_ENTITY + "/ReportTypeLocal";

    /** Constant - JNDI name for the ReportContent entity */
    public static final String JNDI_NAME_REPORT_CONTENT = JNDI_PREFIX_ENTITY + "/ReportContentLocal";

    /** Constant - JNDI name for the ReportSearchCriteria entity */
    public static final String JNDI_NAME_REPORT_SEARCH_CRITERIA = JNDI_PREFIX_ENTITY + "/ReportSearchCriteriaLocal";

    /** Constant - JNDI name for the ReportSearchExpression entity */
    public static final String JNDI_NAME_REPORT_SEARCH_EXPRESSION = JNDI_PREFIX_ENTITY + "/ReportSearchExpressionLocal";

    /***********************************************************************************
     *
     *	Factory methods
     *
     ***********************************************************************************/


    /** Factory method - gets a reference to the Report home interface.
     */
    public static ReportLocalHome getReportLocalHome()
    throws NamingException {
        Context context = new InitialContext();
        ReportLocalHome home = (ReportLocalHome)context.lookup(JNDI_NAME_REPORT);
        return home;
    }

    /** Factory method - gets a reference to the ReportContent home interface.
     */
    public static ReportContentLocalHome getReportContentLocalHome()
    throws NamingException {
        Context context = new InitialContext();
        ReportContentLocalHome home = (ReportContentLocalHome)context.lookup(JNDI_NAME_REPORT_CONTENT);
        return home;
    }

    /** Factory method - gets a reference to the ReportType home interface.
     */
    public static ReportTypeLocalHome getReportTypeLocalHome()
    throws NamingException {
        Context context = new InitialContext();
        ReportTypeLocalHome home = (ReportTypeLocalHome)context.lookup(JNDI_NAME_REPORT_TYPE);
        return home;
    }

    /*************************************************************************************
     *
     *	Helper methods
     *
     *************************************************************************************/

    /** Helper method - gets the EJB home interface based on name. */
    public static Object getHomeInterface(String name, Class homeInterface)
            throws NamingException {
        Context context = new InitialContext();
        Object ref = context.lookup(name);

        if (null == ref)
            return null;

        return PortableRemoteObject.narrow(ref, homeInterface);
    }
}
