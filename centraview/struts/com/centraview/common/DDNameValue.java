/*
 * $RCSfile: DDNameValue.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:56 $ - $Author: mking_cv $
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

import java.io.Serializable;

/**
 * @author CentraView, LLC.
 */
public class DDNameValue implements Serializable
{
  private int id;
  private String strid;
  private String name;
  private String tag;

  public DDNameValue(int id, String name) 
  {
    this.id = id;
    this.name = name;
  }

  public DDNameValue(String strid, String name) 
  {
    this.strid = strid;
    this.name = name;
  }

  public DDNameValue(String strid, String name, String tag) 
  {
    this.strid = strid;
    this.name = name;
    this.tag = tag;
  }

  public String getStrid()
  {
    return this.strid;
  }

  public void setStrid(String strid)
  {
    this.strid = strid;
  }

  public int getId()
  {
    return this.id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public String getTag()
  {
    return this.tag;
  }

  public void setTag(String tag)
  {
    this.tag = tag;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("{");
    sb.append("ID:" + this.getId() + ",");
    sb.append("StringID:" + this.getStrid() + ",");
    sb.append("Name:" + this.getName() + ",");
    sb.append("Tag:" + this.getTag());
    sb.append("}");
    return sb.toString();
  } //end of toString method
}