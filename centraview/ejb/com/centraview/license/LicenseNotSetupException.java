/*
 * $RCSfile: LicenseNotSetupException.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:29 $ - $Author: mking_cv $
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

/**
 * This is an exception class which gets called if 
 * LicenseInstanceVO.updateLicenseInformation or
 * LicenseInstanceVO.setLicenseToInvalid has not
 * been sucessfully run yet. This exception is here
 * to ensure that the LicenseInstanceVO singleton
 * has been filled with data from a license file.
 *
 * @see com.centraview.license.LicenseInstanceVO#updateLicenseInformation(java.util.Properties)
 * @see com.centraview.license.LicenseInstanceVO#setLicenseToInvalid()
 *
 * @author Ryan Grier <ryan@centraview.com>
 */
public class LicenseNotSetupException extends java.lang.Exception
{
  public static final String EXCEPTION_MESSAGE =
    "The License Object has not been setup correctly, please call updateLicenseInformation";

  /**
   * Creates a new LicenseNotSetupException with the default
   * message <code>LicenseNotSetupException.EXCEPTION_MESSAGE</code>
   */
  public LicenseNotSetupException()
  {
    super(EXCEPTION_MESSAGE);
  } //end of LicenseNotSetupException Constructor
} //end of LicenseNotSetupException class