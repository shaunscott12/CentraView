/*
 * $RCSfile: MergeResultBean.java,v $    $Revision: 1.2 $  $Date: 2005/07/26 20:19:18 $ - $Author: mcallist $
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
package com.centraview.administration.merge;

import java.io.Serializable;

/**
 * This Object will hold statistics for the current merge
 * session.  Showing what we have actually done during the merge.
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class MergeResultBean implements Serializable {
  private int recordsPurged = 0;
  private int mergesCompleted = 0;
  
  /**
   * @return Returns the mergesCompleted.
   */
  public int getMergesCompleted() {
    return mergesCompleted;
  }
  /**
   * @return Returns the recordsPurged.
   */
  public int getRecordsPurged() {
    return recordsPurged;
  }
  /**
   * @param mergesCompleted The mergesCompleted to set.
   */
  public void setMergesCompleted(int mergesCompleted) {
    this.mergesCompleted = mergesCompleted;
  }
  /**
   * @param recordsPurged The recordsPurged to set.
   */
  public void setRecordsPurged(int recordsPurged) {
    this.recordsPurged = recordsPurged;
  }
}
