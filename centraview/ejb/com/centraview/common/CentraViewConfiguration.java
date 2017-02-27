/*
 * $RCSfile: CentraViewConfiguration.java,v $    $Revision: 1.17 $  $Date: 2005/10/31 13:25:04 $ - $Author: mcallist $
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


package com.centraview.common;


/**
 * Class to access some of CentraView's server configuration information.
 * @author CentraView, LLC.
 */
public class CentraViewConfiguration 
{
  /** The Version of CentraView software. */
  private final static String CENTRAVIEW_VERSION = "2.1.4";
  private final static String CENTRAVIEW_BUILD   = "47";
  private final static String CENTRAVIEW_CODENAME = "Dot";

  /**
   * Returns the current version of the CentraView software package.
   * @return The current version of CentraView.
   */
  public final static String getVersion()
  {
    return CENTRAVIEW_VERSION;
  } //end of getVersion method

  /**
   * Returns the current build number of the CentraView software package.
   * @return String representation of the build number,
   */

  public final static String getBuild()
  {
    return(CENTRAVIEW_BUILD);
  } // end getBuild() method
  
  public static final String getCodeName()
  {
    return CENTRAVIEW_CODENAME;
  }
} // end CentraViewConfiguration class

