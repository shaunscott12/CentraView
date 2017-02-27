/*
 * $RCSfile: ThreadVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:13 $ - $Author: mking_cv $
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


package com.centraview.support.ticket;

import com.centraview.support.SupportVO;

public class ThreadVO extends SupportVO
{
  public static final String TT_INTERNAL = "INTERNAL";
  public static final String TT_EXTERNAL = "EXTERNAL";
  
  private String threadType;
  private int priorityId;
  private String priorityName;
  private int ticketId; 
  
  public ThreadVO()
  {
    super();
  }
  
  
  public int getPriorityId()
  {
    return this.priorityId;
  }

  public void setPriorityId(int priorityId)
  {
    this.priorityId = priorityId;
  }

  public String getPriorityName()
  {
    return this.priorityName;
  }

  public void setPriorityName(String priorityName)
  {
    this.priorityName = priorityName;
  }

  public String getThreadType()
  {
    return this.threadType;
  }

  public void setThreadType(String threadType)
  {
    if (threadType == null || !(threadType.equals(TT_INTERNAL) || threadType.equals(TT_EXTERNAL))) {
      this.threadType = TT_INTERNAL;
    } else {
      this.threadType = threadType;
    }
  }

  public int getTicketId()
  {
    return this.ticketId;
  }

  public void setTicketId(int ticketId)
  {
    this.ticketId = ticketId;
  }
  
}

