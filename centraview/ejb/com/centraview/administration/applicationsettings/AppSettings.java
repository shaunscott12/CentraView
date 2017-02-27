/*
 * $RCSfile: AppSettings.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:39 $ - $Author: mking_cv $
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

package com.centraview.administration.applicationsettings;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;

/**
 * This is  remote interface which is called from client
 *
 * @author   Sandip Wadkar
 */
public interface AppSettings extends EJBObject
{
  public void addMasterDataSettings(String setting, Collection sourceCollection)
    throws RemoteException;

  public Vector viewMasterDataSettings(String setting)
    throws RemoteException;

  public Vector getRecordType(String module)
    throws RemoteException;

  public void addSupportMailId(Vector ids)
    throws RemoteException;

  public Vector getSupportMailId()
    throws RemoteException;

  public void exportTable(String filePath, String tabname)
    throws RemoteException;

  /**
   * Get emailids from suggestionbox table for support.
   *
   * @return Vector
   */
  public Vector getAllSupportEmailIds()
    throws RemoteException;

  //By Atul
  public String getApplicationSettings(String msname)
    throws RemoteException;

  public void updateApplicationSettings(String msname, String msvalue)
    throws RemoteException;

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds)
    throws RemoteException;

  public void updateCalendarSettings(String start, String end,
    String workingdays)
    throws RemoteException;

  public HashMap getCalendarSettings()
    throws RemoteException;

}
