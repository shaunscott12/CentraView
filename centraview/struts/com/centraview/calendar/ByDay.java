/*
 * $RCSfile: ByDay.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:51 $ - $Author: mking_cv $
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

package com.centraview.calendar;

/***

By night one way, BYDAY another...

Represents a day of the week and OPTIONALLY which count of that day of the week.

For values of count:
	-N means the Nth last given week day in the unit of measure
	 0 means all given week days in the unit of measure
	+N means the Nth first given week day in the unit of measure


If you think this should be a bean, I encourage you to suck on one.

***/

public class ByDay
{
  public ByDay(int count, String weekday)
  {
    this.count = count;
    this.weekday = weekday;
  }


  public ByDay(String descriptor)
  {
    //--- get the week
    weekday = descriptor.substring(descriptor.length()-2);
    if(!Constants.weekDayValues.contains(weekday)){
      throw new IllegalArgumentException("Error: "+weekday+" is not a valid weekday! Use capitolized two letter codes only");
    }

    //--- get the count
    if(descriptor.length() > 2){
      count = Integer.parseInt( descriptor.substring(0,descriptor.length()-2));
      if ( count>53 || count<-53 ){
        throw new IllegalArgumentException("Error: You have asked for an illegal number of weekdays");
      }
    }
  }

  public int     count = 0;
  public String  weekday = null;

  public String toString()
  {
    return "["+count+":"+weekday+"]";
  }
}




