/*
 * $RCSfile: ResourceVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:38 $ - $Author: mking_cv $
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


package com.centraview.activity.helper;

import java.io.Serializable;

public class ResourceVO implements Serializable
{
  //	this Collection will contain collection of Resource Objects
  private int resourceId;
  private String resourceName;
  private String resourceDetails;
  public ResourceVO()
  {
    super();
  }
  public ResourceVO(int id)
  {
    this.resourceId = id;
  }
  public ResourceVO(int id, String name)
  {
    this.resourceId = id;
    this.resourceName = name;
  }
  public int getResourceId()
  {
    return this.resourceId;
  }
  public void setResourceId(int resourceId)
  {
    this.resourceId = resourceId;
  }
  public String getResourceName()
  {
    return this.resourceName;
  }
  public void setResourceName(String resourceName)
  {
    this.resourceName = resourceName;
  }
  public String getResourceDetail()
  {
    return this.resourceDetails;
  }
  public void setResourceDetail(String resourceDetails)
  {
    this.resourceDetails = resourceDetails;
  }
}