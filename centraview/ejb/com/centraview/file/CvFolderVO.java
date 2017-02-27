/*
 * $RCSfile: CvFolderVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:24 $ - $Author: mking_cv $
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

package com.centraview.file;

import java.util.Iterator;
import java.util.Vector;

import com.centraview.common.CVAudit;
import com.centraview.common.DDNameValue;

/**
 * This class stores the value object of Folders
 *
 * @author   Iqbal Khan
 * @version  $Revision: 1.1.1.1 $
 */
public class CvFolderVO extends CVAudit
{
  public static final int DEFAULT_LOCATION = 1;
  public static final String FS_SYSTEM_YES = "TRUE";
  public static final String FS_SYSTEM_NO = "FALSE";
  public static final String FDV_PRIVATE = "PRIVATE";
  public static final String FDV_PUBLIC = "PUBLIC";
  
  public static final String EMAIL_ATTACHMENT_FOLDER = "EMAIL_ATTACHMENTS";

  // set in setLocationName else blank
  public static String PATH_SEPERATOR = "";
  private int folderId;
  private String name;
  private String parentname;
  private int parent;
  private String description;
  private String fullPath;
  private Vector fullPathVec;
  private int locationId;
  private String locationName;
  private String isSystem;
  private String visibility;

  /**
   * Constructor
   *
   */
  public CvFolderVO()
  {
    super();
  }

  /**
   * gets the description
   *
   * @return     String
   */
  public String getDescription()
  {
    return this.description;
  }

  /**
   * sets the description
   *
   * @param   description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * gets ParentName
   *
   * @return    String
   */
  public String getParentName()
  {
    return this.parentname;
  }

  /**
   * sets ParentName
   *
   * @param   parentname
   */
  public void setParentName(String parentname)
  {
    this.parentname = parentname;
  }

  /**
   * gets the FolderId
   *
   * @return   int
   */
  public int getFolderId()
  {
    return this.folderId;
  }

  /**
   * sets the folderid
   *
   * @param   folderId
   */
  public void setFolderId(int folderId)
  {
    this.folderId = folderId;
  }

  /**
   * gets the name
   *
   * @return  String
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * sets the name
   *
   * @param   name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * gets the parent
   *
   * @return  int
   */
  public int getParent()
  {
    return this.parent;
  }

  /**
   *
   * sets the parent
   * @param   parent
   */
  public void setParent(int parent)
  {
    this.parent = parent;
  }

  /**
   * returns the Full Path of the folder (concatenated)
   *
   * @return  String
   * @param   pathSeperator
   * @param   includeLocation    if true prefix location with the full path
   *
   */
  public String getFullPath(String pathSeperator, boolean includeLocation)
  {
    if (this.fullPath != null)
    {
      return this.fullPath;
    }

    if (this.fullPathVec == null)
    {
      return "";
    }

    if ((pathSeperator == null) || (pathSeperator.length() == 0))
    {
      pathSeperator = CvFolderVO.PATH_SEPERATOR;
    }

    if (this.locationName.indexOf("\\") >= 0)
    {
      pathSeperator = "\\";
    }
    else if (this.locationName.indexOf("/") >= 0)
    {
      pathSeperator = "/";
    }

    //if(pathSeperator == null || pathSeperator.length() == 0);
    //	pathSeperator = "\\";
    Iterator it = this.fullPathVec.iterator();
    String tmpFp = new String();

    while (it.hasNext())
    {
      //tmpFp = tmpFp.concat(pathSeperator).concat(((DDNameValue)it.next()).getName());
      tmpFp = ((DDNameValue) it.next()).getName() + pathSeperator + tmpFp;
    }

    if (includeLocation && (this.locationName != null))
    {
      tmpFp = this.locationName + pathSeperator + tmpFp;
    }

    //tmpFp.concat(this.locationName);
    this.fullPath = tmpFp;

    return this.fullPath;
  }

  /**
   *
   * sets the Full path
   * @param   fullPath
   */

  //public void setFullPath(String fullPath)
  //{
  //	this.fullPath = fullPath;
  //}

  /**
   * gets the locationId
   *
   * @return   int
   */
  public int getLocationId()
  {
    return this.locationId;
  }

  /**
   * sets the locationId
   *
   * @param   locationId
   */
  public void setLocationId(int locationId)
  {
    this.locationId = locationId;
  }

  /**
   * gets the locationName
   *
   * @return   String
   */
  public String getLocationName()
  {
    return this.locationName;
  }

  /**
   * sets the Location name
   *
   * @param   locationName
   */
  public void setLocationName(String locationName)
  {
    this.locationName = locationName;

    if (this.locationName.indexOf("\\") >= 0)
    {
      PATH_SEPERATOR = "\\";
    }
    else if (this.locationName.indexOf("/") >= 0)
    {
      PATH_SEPERATOR = "/";
    }
  }

  /**
   * returns a vector having DDNameValue object
   * one element for each folder till the root folder
   * see getFullPath() also
   *
   * @param
   */
  public Vector getFullPathVec()
  {
    return this.fullPathVec;
  }

  /**
   * sets a vector having DDNameValue object
   * one element for each folder till the root folder.
   *
   * @param    fullPathVec
   */
  public void setFullPathVec(Vector fullPathVec)
  {
    this.fullPathVec = fullPathVec;
  }

  public String getIsSystem()
  {
    return this.isSystem;
  }

  public void setIsSystem(String isSystem)
  {
    if (!(isSystem.equals(FS_SYSTEM_YES) || isSystem.equals(FS_SYSTEM_NO)))
    {
      this.isSystem = FS_SYSTEM_NO;
    }
    else
    {
      this.isSystem = isSystem;
    }
  }

  public String getVisibility()
  {
    return this.visibility;
  }

  public void setVisibility(String visibility)
  {
    if ((visibility == null)
        || !(visibility.equals(FDV_PRIVATE) || visibility.equals(FDV_PUBLIC)))
    {
      this.visibility = FDV_PRIVATE;
    }
    else
    {
      this.visibility = visibility;
    }
  }
}
