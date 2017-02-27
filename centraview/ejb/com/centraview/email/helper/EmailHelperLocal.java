/*
 * $RCSfile: EmailHelperLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:16 $ - $Author: mking_cv $
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


package com.centraview.email.helper;

import java.util.HashMap;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;

/**
 * The local interface for other EmailHelperEJB
 */
public interface EmailHelperLocal extends EJBLocalObject
{
  /**
   * Get IndividualID from mocrelate table for given email address
   * @return int - individualID that matches the given email address
   */
	public int getIndividualID(String emailAddress);
  
  /**
   * Get FolderID from emailfolder table for given a/c id and folder type
   * @return int - folderID that matches the given accountID
   */
	public int getFolderIDForAccount(int accountId, String folderName, String folderType);

	/**
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 * @author Kevin McAllister <kevin@centraview.com>
	 */
	public void setDataSource(String ds);	
  
  /**
   * Returns a HashMap representation of the information needed
   * to send a particular system email (ie: Forgot Password email)
   * for the given System Email ID.
   * @param systemEmailID The ID of the system email which we need
   * information for. Use one of the correct constants found in
   * com.centraview.administration.common.AdminConstantKeys
   */
  public HashMap getSystemEmailInfo(int systemEmailID) throws EJBException;

}
