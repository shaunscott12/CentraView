/*
 * $RCSfile: GroupVO.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:22:46 $ - $Author: mcallist $
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

package com.centraview.contact.group;

import java.io.Serializable;
import java.util.Date;

public class GroupVO implements Serializable {
  private int groupID;
  private String groupName;
  private String description;
  private Date createdate;
  private Date modifydate;
  private int owner;

  /**
   * @return The Description of Group
   */
  public String getDescription()
  {
    return this.description;
  }

  /**
   * Set the Description of Group.
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * @return The GroupID
   */
  public int getGroupID()
  {
    return this.groupID;
  }

  public void setGroupID(int groupID)
  {
    this.groupID = groupID;
  }

  /**
   * @return The GroupName
   */
  public String getGroupName()
  {
    return this.groupName;
  }

  public void setGroupName(String groupName)
  {
    this.groupName = groupName;
  }

  /**
   * @return The Group ValeObject
   */
  public GroupVO getVO()
  {
    return this;
  }

  public Date getCreatedate()
  {
    return this.createdate;
  }

  public void setCreatedate(Date creatdate)
  {
    this.createdate = creatdate;
  }

  public Date getModifydate()
  {
    return this.modifydate;
  }

  public void setModifydate(Date modifydate)
  {
    this.modifydate = modifydate;
  }

  public int getOwner()
  {
    return this.owner;
  }

  public void setOwner(int owner)
  {
    this.owner = owner;
  }
  
  public String toString()
  {
    StringBuffer buffer = new StringBuffer("GroupVO: {");
    buffer.append("ID: "+groupID);
    buffer.append(", name: "+groupName);
    buffer.append(", desc: "+description);
    buffer.append(", created: "+createdate);
    buffer.append(", modified: "+modifydate);
    buffer.append(", ownerID: "+owner);
    return buffer.toString();
  }
  
}
