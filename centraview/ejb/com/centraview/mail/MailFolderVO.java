/*
 * $RCSfile: MailFolderVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:36 $ - $Author: mking_cv $
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

package com.centraview.mail;

import java.io.Serializable;

/**
 * This class represents and email folder object.
 * @author Ryan Grier <ryan@centraview.com>
 */
public class MailFolderVO implements Serializable
{
  /** The Folder ID of this Folder. */
  private int folderID;

  /** The Parent Folder ID of this Folder. */
  private int parentID;

  /** The Email Account ID which owns this Folder. */
  private int emailAccountID;

  /** The name of this folder. */
  private String folderName;

  /** The full name of this folder. */
  private String folderFullName;

  /** The type of this folder, is either System or User. */
  private String folderType;

  /** The default Inbox Folder name. */
  public static final String DEFAULT_FOLDER_NAME = "Inbox";

  /** The default Sent Folder name. */
  public static final String SENT_FOLDER_NAME = "Sent";

  /** The default Drafts Folder name. */
  public static final String DRAFTS_FOLDER_NAME = "Drafts";

  /** The default Trash Folder name. */
  public static final String TRASH_FOLDER_NAME = "Trash";

  /** The default Templates Folder name. */
  public static final String TEMPLATES_FOLDER_NAME = "Templates";

  /** The System type folder name. */
  public static final String SYSTEM_FOLDER_TYPE = "SYSTEM";

  /** The User type folder name. */
  public static final String USER_FOLDER_TYPE = "USER";

  /** The totalMessage folder total number of message in Folder. */
  private int totalMessage;

  /** The countReadMessage type count of message in Folder. */
  private int countReadMessage;

  /** Indicates whether this folder contains sub-folders or not. */
  private boolean subFolders;

  public int getEmailAccountID()
  {
    return this.emailAccountID;
  }

  public void setEmailAccountID(int emailAccountID)
  {
    this.emailAccountID = emailAccountID;
  }

  public int getFolderID()
  {
    return this.folderID;
  }

  public void setFolderID(int folderID)
  {
    this.folderID = folderID;
  }

  public String getFolderName()
  {
    return this.folderName;
  }

  public void setFolderName(String folderName)
  {
    this.folderName = folderName;
  }

  public String getFolderFullName()
  {
    return this.folderFullName;
  }

  public void setFolderFullName(String folderFullName)
  {
    this.folderFullName = folderFullName;
  }

  public String getFolderType()
  {
    return this.folderType;
  }

  public void setFolderType(String folderType)
  {
    this.folderType = folderType;
  }

  public boolean isSystemFolder()
  {
    return this.folderType.equalsIgnoreCase(MailFolderVO.SYSTEM_FOLDER_TYPE);
  }

  public int getParentID()
  {
    return this.parentID;
  }

  public void setParentID(int parentID)
  {
    this.parentID = parentID;
  }


  public Integer getCountReadMessage()
  {
    return new Integer(this.countReadMessage);
  }

  public void setCountReadMessage(int countReadMessage)
  {
    this.countReadMessage = countReadMessage;
  }


  public Integer getTotalMessage()
  {
    return new Integer(this.totalMessage);
  }

  public void setTotalMessage(int totalMessage)
  {
    this.totalMessage = totalMessage;
  }

  public void setSubFolders(boolean hasSubFolders)
  {
    this.subFolders = hasSubFolders;
  }

  public boolean hasSubFolders()
  {
    return(this.subFolders);
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer("MailFolderVO = [\n");
    sb.append("    folderID = " + folderID + "\n");
    sb.append("    parentID = " + parentID + "\n");
    sb.append("    emailAccountID = " + emailAccountID + "\n");
    sb.append("    folderName = " + folderName + "\n");
    sb.append("    folderFullName = " + folderFullName + "\n");
    sb.append("    folderType = " + folderType + "\n");
    sb.append("    totalMessage = " + totalMessage + "\n");
    sb.append("    countReadMessage = " + countReadMessage + "\n");
    sb.append("]\n");
    return(sb.toString());  
  }
}
  

