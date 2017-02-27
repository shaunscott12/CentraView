/*
 * $RCSfile: SourceVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:59 $ - $Author: mking_cv $
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

package com.centraview.common.source;

import java.io.Serializable;

/**
 * @author 
 */
public class SourceVO implements Serializable
{
  private int sourceId;
  private String name;

  /**
   *
   *  Return the sourceid of source
   * @return sourceid
   */
  public int getSourceId()
  {
    return this.sourceId;
  }

  /**
   *  Set the Source.sourceId ID
   *
   * @param   sourceId  The new sourceId.
   */
  public void setSourceId(int sourceId)
  {
    this.sourceId = sourceId;
  }

  /**
   *
   *
   * @return The Value of Source
   */
  public String getName()
  {
    return this.name;
  }

  /**
   *  Set the Value of Source
   *
   * @param  value  Value of Source
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   *  This method returns SourceVO Object
   *
   * @return     SourceVO
   */
  public SourceVO getVO()
  {
    return this;
  }
}
