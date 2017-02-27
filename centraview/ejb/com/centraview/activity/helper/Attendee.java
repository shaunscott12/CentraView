/*
 * $RCSfile: Attendee.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:37 $ - $Author: mking_cv $
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

public class Attendee implements Serializable
{
  private int individualid;
  private int entityid;
  /*
   * option == 1 required
   * == 0 optional
   */
  private byte option;
  /**
   @roseuid 3EFC315C0224
   */
  public Attendee(int indId, int entId, byte option)
  {
    this.individualid = indId;
    this.entityid = entId;
    this.option = option;
  }
  /**
   @param attendee
   @roseuid 3ED6F98D0372
   */
  public int getAttendeeID()
  {
    return individualid;
  }
  /**
   @return java.util.Collection
   @roseuid 3ED6F99F02D8
   */
  public int getEntityId()
  {
    return entityid;
  }
  /*
   * get option flag if its required or optional.
   */
  public byte getOption()
  {
    return option;
  }
  /*
   * get option flag if its required or optional.
   */
  public void setOption(byte flag)
  {
    this.option = flag;
  }
}