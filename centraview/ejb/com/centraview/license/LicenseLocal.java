/*
 * $RCSfile: LicenseLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:29 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the License"); you may not use this file except in
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

package com.centraview.license;
 
import javax.ejb.EJBLocalObject;

/**
 * This provides the Local 
 * interface for the License EJB.
 *
 * @author Ryan Grier <ryan@centraview.com>
 * @version 1.0
 */
public interface LicenseLocal extends EJBLocalObject
{
	
	/** 
	 * Will create a new License instance
	 * in the database and return the 
	 * newly created primary key.
	 *
	 * @param licenseDetail The LicenseVO to be created.
	 *
	 * @return The new ID of the LicenseVO.
	 */
	public int createLicense (LicenseVO licenseDetail);
	
	/** 
	 * Returns the LicenseVO based on the 
	 * Primary License ID passed to the method.
	 * Will return null if the license
	 * does not exist.
	 *
	 * @param licenseID The License ID of the LicenseVO wanted.
	 *
	 * @return The LicenseVO if it exists, null of it doesn't.
	 */
	public LicenseVO getLicenseDetails (int licenseID);
	
	/** 
	 * Updates an existing LicenseVO object. 
	 *
	 * @param licenseDetail The LicenseVO to be created. The
	 * LicenseID must be set in this object.
	 */
	public void updateLicense(LicenseVO licenseDetail);
	
	/** 
	 * Deleted the license object based on the 
	 * Primary License ID passed to the method.
	 *
	 * @param licenseID The License ID of the LicenseVO 
	 * to be deleted.
	 */
	public void deleteLicense (int licenseID);
  
	/** 
	 * Returns the Primary LicenseVO.
	 * There should only be one LicenseVO, but
	 * this will return the first one.
	 * Will return null if the license
	 * does not exist.
	 *
	 * @return The LicenseVO if it exists, null of it doesn't.
	 */
	public LicenseVO getPrimaryLicense();
  
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);
  
} //end of LicenseLocal interface