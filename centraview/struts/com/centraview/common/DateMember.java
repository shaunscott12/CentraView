/*
 * $RCSfile: DateMember.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:57 $ - $Author: mking_cv $
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
package com.centraview.common ;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;

public class DateMember extends ListElementMember implements Serializable
{
	private int auth ;
	private String requestURL ;
	private char displayType ;
	private String graphicResourceURL ;
	private boolean  linkEnabled ;
	private int  displayWidth ;
	protected Date memberValue ;
	private String memberType ;

	private String timeZone = "EST" ;
	private String dateFormat = "MMM d, yyyy hh:mm:ss a" ;
	private String onlyDateFormat = "MMM d, yyyy";

	private boolean applyDate = true;

	public DateMember (String memberType, Date memberValue, int auth, String requestURL, char displayType, boolean linkEnabled, int displayWidth, String timezoneid)
  {
    this.auth = auth ;
    this.requestURL = requestURL;
    this.displayType = displayType; // Graphics  or Text
    this.linkEnabled = linkEnabled;
    this.displayWidth = displayWidth ;
    this.memberType = memberType ; //column name
    this.memberValue = memberValue;
  }
  
  public Date applyTimeZone(Date ds, String timezoneid)
  {
    TimeZone tz = TimeZone.getTimeZone(timezoneid);
    long ls = ds.getTime();
    int startoffset = tz.getOffset(ls);
    ls = ls + startoffset;
    ds = new Date(ls);
    return ds;
  }
  
  public String getMemberType()
  {
    return this.memberType;
  }
  
  public String getDataType()
  {
    return "string";
  }
  
	public Object getMemberValue()
	{
		return this.memberValue;
	}
  
  public void setApplyDate(boolean applyDate)
	{
    this.applyDate = applyDate;
  }
  
  public String getRequestURL()
  {
    return this.requestURL;
  }
  
  public void setRequestURL(String str)
  {
    this.requestURL = str;
  }
  
	public int getDisplayWidth()
  {
    return this.displayWidth;
  }
  
  public char getDisplayType()
  {
    return this.displayType;
  }
  
  public String getGraphicResourceURL()
  {
    return this.graphicResourceURL;
  }
  
  public void setGraphicResourceURL(String str)
  {
    this.graphicResourceURL = str;
  }
  
  public boolean getLinkEnabled()
  {
    return this.linkEnabled;
  }
  
  public void setLinkEnabled(boolean b)
  {
    this.linkEnabled = b;
  }
  
  public int getAuth()
  {
    return auth;
  }
  
  public void setAuth(int auth)
  {
    this.auth = auth;
  }
  
  public String  getDisplayString()
  {
    if (auth == ModuleFieldRightMatrix.NONE_RIGHT)
    {
      return "";
    }
    
    if (memberValue != null)
    {
      GregorianCalendar greg = null;
      boolean isTimestamp = false;    // used to determine if a mySQL TIMESTAMP value was
                                      // passed in, in the form of a java.sql.Date object
      try
      {
        greg = new GregorianCalendar(((Date)this.getMemberValue()).getYear(),((Date)this.getMemberValue()).getMonth(),((Date)this.getMemberValue()).getDate(),((Date)this.getMemberValue()).getHours(),((Date)this.getMemberValue()).getMinutes());
      }catch(Exception e){
        // mking is responsible for this code.
				// I'm making an assumption here, that if an exception was thrown above, then
				// we must have been passed a mySQL TIMESTAMP value in the form of a java.sql.Date
				// object. In that case, let's set the isTimestamp flag to true, and create an
				// empty GregorianCalendar object. Then populate that object via the setTime() method.
				isTimestamp = true;
				greg = new GregorianCalendar();
				try
				{
					// populate GregorianCalendar with the memberValue, which is a java.sql.Date object
					greg.setTime(memberValue);
				}catch(Exception err){
					System.out.println("[DateMember] DateMember.getDisplayString() threw Exception:");
					//err.printStackTrace();
				}
				System.out.println("[DateMember] DateMember.getDisplayString() threw Exception:");
				//e.printStackTrace();
			}

			if (! isTimestamp)
			{
				// if we didn't set the isTimestamp flag to true in the above block,
				// then continue with the previous code, setting time zone
				greg.setTimeZone(TimeZone.getTimeZone("EST"));

				greg.get(Calendar.HOUR_OF_DAY);

				greg.setTimeZone(TimeZone.getTimeZone(this.getTimeZone()));

				((Date)this.getMemberValue()).setYear(greg.get(Calendar.YEAR));
				((Date)this.getMemberValue()).setMonth(greg.get(Calendar.MONTH));
				((Date)this.getMemberValue()).setDate(greg.get(Calendar.DATE));
				((Date)this.getMemberValue()).setHours(greg.get(Calendar.HOUR_OF_DAY));
				((Date)this.getMemberValue()).setMinutes(greg.get(Calendar.MINUTE));

				DateFormat df = null;

				// When you set the applyDate to false then
				// it will only display the Date and it will not consider the Time
        if (applyDate)
        {
          df = new SimpleDateFormat(this.getDateFormat());
        }else{
          df = new SimpleDateFormat(this.onlyDateFormat);
				}
        return df.format(this.memberValue);
			}else{
				// if this is a timestamp, then just return a string representation
				return(memberValue.toString());
			}   // end if (! isTimestamp)
		}else{
			return "";
		}   // end if (memberValue != null)
	}
  
  public  String getSortString()
  {
    if (memberValue != null && !memberValue.equals(""))
    {
      return String.valueOf(memberValue.getTime());
    }else{
      return "";
    }
  }
  
  public String getTimeZone()
  {
    return this.timeZone;
  }
  
  public void setTimeZone(String timeZone)
  {
    this.timeZone = timeZone;
  }
  
  public String getDateFormat()
  {
    return this.dateFormat;
  }
  
  public void setDateFormat(String dateFormat)
  {
    this.dateFormat = dateFormat;
  }

  /**
   * Over-rides the Object.toString() method, which makes for
   * debug statements which are easier to read and give the
   * developer useful information.
   * @return String representation of this DateMember object
   */
  public String toString()
  {
    StringBuffer toReturn = new StringBuffer("");
    toReturn.append("[" + this.memberValue + "]");
    return(toReturn.toString());
  }   // end toString() method

}

