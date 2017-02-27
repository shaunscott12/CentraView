/*
 * $RCSfile: Resource.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 21:57:04 $ - $Author: mcallist $
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
package com.centraview.activity;

/**
 * class Resource
 */

public class Resource {

  // variable id for id of the resource
  private int id;
  // variable name for name of the resource
  private String name = null;

  /**
   * constructor with arguments id and name
   * @param id
   * @param name
   */
  Resource(int id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * method getId
   * @return id
   */
  public int getId()
  {
    return this.id;
  }

  /**
   * method getName
   * @return name
   */
  public String getName()
  {
    return this.name;
  }

}
