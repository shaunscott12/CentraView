/*
 * $RCSfile: ValueList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:16 $ - $Author: mking_cv $
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


package com.centraview.valuelist;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 * ValueList interface provides remote access to retrieve
 * lists of data, by passing in a list request (ViewParameters)
 * and a ValueListPage is returned.
 *
 * @author Kevin McAllister <kevin@centraview.com>
 */
public interface ValueList extends EJBObject
{
  /**
   * Allows the client to set the private dataSource.
   *
   * @param ds The cannonical JNDI name of the datasource.
   *
   * @throws RemoteException This exception is defined in the
   * method signature to provide backward compatibility
   * for applications written for the EJB 1.0 specification.
   * Enterprise beans written for the EJB 1.1 specification
   * should throw the javax.ejb.EJBException instead of this
   * exception. Enterprise beans written for the EJB2.0 and
   * higher specifications must throw the javax.ejb.EJBException
   * instead of this exception.
   */
  public void setDataSource(String ds) throws RemoteException;
  /**
   * This method dispatches the work of building the list
   * to the appropriate EJB which will return a list. 
   * @param individualId
   * @param parameters
   * @return a ValueListVO
   */
  public ValueListVO getValueList(int individualId, ValueListParameters parameters) throws RemoteException;

} //end of ValueList interface
