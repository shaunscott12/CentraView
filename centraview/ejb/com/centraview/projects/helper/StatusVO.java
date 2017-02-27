/*
 * $RCSfile: StatusVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:47 $ - $Author: mking_cv $
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

package com.centraview.projects.helper;

import java.io.Serializable;

/**
 * This is Project Value Object which represent the Project Data.
 */
public class StatusVO implements Serializable
{
  private int statusID;
  private String title;
  private String description;

  public StatusVO() {}

  /**
   * StatusVO parameterised constructor
   * @param statusID int Status ID of project status
   * @param title String title of project.
   * @param description String description of project.
   */
  public StatusVO(int statusID, String title, String description)
  {
    this.statusID = statusID;
    this.title = title;
    this.description = description;
  }

  /**
   * Returns StatusID of project
   * @return statusID int
   */
  public int getStatusID()
  {
    return statusID;
  }

  /**
   * Sets StatusID of project
   * @param statusID int
   */
  public void setStatusID(int statusID)
  {
    this.statusID = statusID;
  }

  /**
   * Returns StatusID of project
   * @return title String
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Sets title of project
   * @param title String
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /** 
   * Returns Description of project
   * @return description String
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Sets Description of project
   * @param description String
   */
  public void setDescription(String description)
  {
    this.description = description;
  }
}

