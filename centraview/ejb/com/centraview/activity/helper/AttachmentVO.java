/*
 * $RCSfile: AttachmentVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:37 $ - $Author: mking_cv $
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
import java.util.ArrayList;

public class AttachmentVO implements Serializable
{
  private ArrayList allAttachments;
  private int recordTypeID;
  private int recordsID;
  /**
   * @roseuid 3ED76B2501E1
   */
  public AttachmentVO(ArrayList allAttachments)
  {
    this.allAttachments = allAttachments;
  }
  /**
   * @return int
   * @roseuid 3ED76B400213
   */
  public ArrayList getAllAttachments()
  {
    return this.allAttachments;
  }
  /**
   * @roseuid 3ED76D1E0197
   */
  public void setAllAttachments(ArrayList allAttachments)
  {
    this.allAttachments = allAttachments;
  }
  public int getRecordTypeID()
  {
    return recordTypeID;
  }
  public int getRecordsID()
  {
    return recordsID;
  }
}