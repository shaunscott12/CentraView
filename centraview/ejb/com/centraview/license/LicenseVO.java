/*
 * $RCSfile: LicenseVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:29 $ - $Author: mking_cv $
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

import java.io.Serializable;
import java.util.Date;

/**
 * This class is a Value Object
 * for the License EJB. It will hold
 * any values returned from the EJB 
 *
 * @author Ryan Grier <ryan@centraview.com>
 * @version 1.0
 */
public class LicenseVO implements Serializable
{
	/** The Primary Key of the license. */
	private int licenseID;
	
	/** The date the license key was last verified. */
	private Date lastVerified;
	
	/** The 16 character CentraView License Key. */
	private String licenseKey;
	
	/** The Base64 encoded SHA1 of the license information. */
	private String licenseVerification;

	/**
	 * Returns the License's Primary Key
	 * from the database.
	 *
	 * @return The Primary License Id.
	 */
	public int getLicenseID()
	{
		return this.licenseID;
	} //end of getLicenseID method
	
	/**
	 * Sets the License's Primary Key
	 * from the database.
	 *
	 * @param licenseId The Primary License Id.
	 */
	public void setLicenseID(int licenseId)
	{
		this.licenseID = licenseId;
	} //end of setLicenseID method
	
	/**
	 * Returns the License's Last Verified
	 * Date from the database.
	 *
	 * @return The last date the license was verified.
	 */
	public Date getLastVerified()
	{
		return this.lastVerified;
	} //end of getLastVerified method
	
	/**
	 * Sets the License's Last Verified
	 * Date from the database.
	 *
	 * @param lastVerified The last date the license was verified.
	 */
	public void setLastVerified(Date lastVerified)
	{
		this.lastVerified = lastVerified;
	} //end of setLastVerified method
	
	/**
	 * Returns the License's 16 character license
	 * key from the database.
	 *
	 * @return The 16 character CentraView license key.
	 */
	public String getLicenseKey()
	{
		return this.licenseKey;
	} //end of getLicenseKey method
	
	/**
	 * Sets the License's 16 character license
	 * key from the database.
	 *
	 * @param licenseKey The 16 character CentraView license key.
	 */
	public void setLicenseKey(String licenseKey)
	{
		this.licenseKey = licenseKey;
	} //end of setLicenseKey method
	
	/**
	 * Returns the Base64 encoded SHA1 verification from 
	 * the central CentraView server.
	 *
	 * @return The Base64 encoded SHA message from CentraView
	 */
	public String getLicenseVerification()
	{
		return this.licenseVerification;
	} //end of getLicenseKey method
	
	/**
	 * Sets the Base64 encoded SHA1 verification from 
	 * the central CentraView server.
	 *
	 * @param licenseVerification 
	 * The Base64 encoded SHA message from CentraView.
	 */
	public void setLicenseVerification(String licenseVerification)
	{
		this.licenseVerification = licenseVerification;
	} //end of setLicenseKey method
	
} //end of LicenseVO class