/*
 * $RCSfile: EJBHomeCache.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:48 $ - $Author: mking_cv $
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

package com.centraview.ejb;

import java.util.HashMap;


/**
 * A singleton whose sole existance is to cache EJBHome Objects in
 * a private HashMap keyed by the Class object of the EJB it refers to.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public final class EJBHomeCache
{
  /**
   * The singleton instance.
   */
  private static final EJBHomeCache _instance = new EJBHomeCache();

  /**
   * The HashMap that will cache the JNDI lookups.
   */
  private HashMap homeMap; 
  
  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Declare constructor private so others can't create this.
   * Could make it protected to allow subclassing, but I doubt that 
   * matters much here.
   * 
   * Don't use lazy loading since it will be a wasted check every time
   * this thing better be setup.
   */
  private EJBHomeCache() 
  {
    homeMap = new HashMap();
  }

  static public synchronized final EJBHomeCache getInstance()
  {
    return _instance;
  }
  
  /**
   * Returns Object stored in the private HashMap keyed by
   * homeClass.
   * @param homeClass
   * @return Object
   */
  public Object getHome(Class homeClass) 
  {
    return homeMap.get(homeClass);
  }
  
  /**
   * Stores Object keyed by homeClass in a private HashMap.
   * @param homeClass
   * @param homeObject
   */
  public void putHome(Class homeClass, Object homeObject)
  {
    homeMap.put(homeClass, homeObject);
  }
  
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append("EJBHomeCache HasHMap: ");
    buffer.append(homeMap);
    return buffer.toString();
  }
  
}
