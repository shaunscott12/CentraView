/*
 * $RCSfile: SessionBeanFactory.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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

 
package com.centraview.report.ejb.session;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.NamingException;

import com.centraview.common.CVUtility;

/**********************************************************************************
 *
 *   Session Bean factory.
 *
 *   @version 1.0
 *   @date 12/28/03
 ****************************************************************************/

public class SessionBeanFactory
{
  public static Context context = null;
  /***********************************************************************************
   *
   *	Constants - JNDI names
   *
   ***********************************************************************************/

  /** Constant - JNDI name prefix for session beans. */
  public static final String JNDI_PREFIX_SESSION = "report/ejb/session";

  /** Constant - full name for the ReportFacadeHome */
  public static final String NAME_REPORT_FACADE_HOME = "com.centraview.report.ejb.session.ReportFacadeHome";

  /** Constant - JNDI name for the ReportFacade session */
  public static final String JNDI_NAME_REPORT_FACADE_SESSION = JNDI_PREFIX_SESSION + "/ReportFacade";

  public SessionBeanFactory(Context ejbNamingContext)
  {
    context = ejbNamingContext;
  }

  /***********************************************************************************
   *
   *	Factory methods
   *
   ***********************************************************************************/
  /** 
   * Factory method - gets a reference to the ReportFacede
   *  home interface.
   */
  public static ReportFacadeHome getReportFacadeHome() throws NamingException
  {
    return (ReportFacadeHome)CVUtility.getHomeObject(NAME_REPORT_FACADE_HOME, JNDI_NAME_REPORT_FACADE_SESSION);
  }

  /** 
   * Factory method - methods that use Home interface methods 
   * to generate a Remote interface reference
   */
  public static ReportFacade getReportFacade() throws NamingException, RemoteException, CreateException
  {
    ReportFacadeHome reportFacadeHome = getReportFacadeHome();
    return reportFacadeHome.create();
  }
}