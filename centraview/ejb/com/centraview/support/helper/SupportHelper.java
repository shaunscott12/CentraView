/*
 * $RCSfile: SupportHelper.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:10 $ - $Author: mking_cv $
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


package com.centraview.support.helper;

import java.util.Vector;

import javax.ejb.EJBObject;

public interface SupportHelper extends EJBObject
{
  public Vector getAllStatus() throws java.rmi.RemoteException;
	public Vector getAllPriorities() throws java.rmi.RemoteException;
	public Vector getAllTicketIDs() throws java.rmi.RemoteException;
  
  /**
   * Returns the "supportEmailCheckInterval" value from the
   * "systemsettings" table in the database, which defines
   * how often the system should check for new messages on
   * ALL of the defined support email accounts.
   * @return int The number in minutes between checks of the
   *         support email accounts. A value of zero indicates
   *         that support accounts should never be checked.
   */
  public int getSupportEmailCheckInterval() throws java.rmi.RemoteException;

  /**
   * Updates the "supportEmailCheckInterval" value in the
   * "systemsettings" table in the database, which defines
   * how often the system should check for new messages on
   * ALL of the defined support email accounts.
   * @param newInterval The number in minutes between checks of the
   *         support email accounts. A value of zero indicates
   *         that support accounts should never be checked.
   * @return int The number of records updated - should always be zero or 1.
   */
  public int setSupportEmailCheckInterval(int interval) throws java.rmi.RemoteException;


	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds) throws java.rmi.RemoteException;
}

