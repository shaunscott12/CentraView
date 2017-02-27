/*
 * $RCSfile: AdvancedSearchLocalHome.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:51 $ - $Author: mking_cv $
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

package com.centraview.advancedsearch;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * This interface is the lcoal home interface
 * for the Advanced Search EJB. It provides the
 * method for creating an instance of the 
 * local interface.
 * 
 * @author Ryan Grier <ryan@centraview.com>
 */
public interface AdvancedSearchLocalHome extends EJBLocalHome
{
  /**
   * This method returns a instance of the local interface
   * of the Advanced Search EJB.
   * 
   * @return An instance of the AdvancedSearchLocal.
   * 
   * @throws CreateException The instance could not be
   * created.
   * 
   */
  public AdvancedSearchLocal create() throws CreateException;
} //end of AdvancedSearchLocalHome interface