/*
 * $RCSfile: RecurrenceVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:37 $ - $Author: mking_cv $
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RecurrenceVO implements Serializable
{
	public static final String ARF_DAY = "DAY";
	public static final String ARF_WEEK = "WEEK";
	public static final String ARF_MONTH = "MONTH";
	public static final String ARF_YEAR = "YEAR";

	private Date startDate;
	private Date until;
	private String timePeriod;
	private int every;
	private int on;
	private int recurrenceId;

	private ArrayList recurrenceExcept;

    HashMap hmrecurrence = new HashMap();


	public RecurrenceVO()
	{
	}


	public RecurrenceVO(Date startDate, Date until, String timePeriod, int every, int on)
  {
    this.startDate = startDate;
    this.until = until;
    this.timePeriod = timePeriod;
    this.every = every ;
    this.on = on;

    this.fillRecurrenceHashMap();
  }   // end constructor

  String freq ;

  public void fillRecurrenceHashMap()
  {
    try
    {
      if (timePeriod.equals(ARF_DAY))
      {
        hmrecurrence.put("FREQUENCY" , ARF_DAY);

        // If you figure out what the hell "17" is
        // you MUST document it here. That goes for
        // ANYONE that edits this code.
        if (on == 17)
        {
          hmrecurrence.put("ON" ,"1");
        }else{
          hmrecurrence.put("ON" ,"0");
        }
        hmrecurrence.put("EVERY", "" + every);
      }

      if (timePeriod.equals(ARF_WEEK))
      {
        hmrecurrence.put("FREQUENCY", ARF_WEEK);
        String str = "";

        if ((on & 0x01) > 0){ str = str + "0,"; }
        if ((on & 0x02) > 0){ str = str + "1,"; }
        if ((on & 0x04) > 0){ str = str + "2,"; }
        if ((on & 0x08) > 0){ str = str + "3,"; }
        if ((on & 0x10) > 0){ str = str + "4,"; }
        if ((on & 0x20) > 0){ str = str + "5,"; }
        if ((on & 0x40) > 0){ str = str + "6";  }

        hmrecurrence.put("ON" ,str);

        hmrecurrence.put("EVERY", "" + every);
      }

      if (timePeriod.equals(ARF_MONTH))
      {
        hmrecurrence.put("FREQUENCY" , ARF_MONTH);
        String str = "";

        if (on >0 && on <=31)
        {
          str = "0,"+on;
        }else{
          str = "1,"+(((int)(on/10))-3)+","+((int)((double)(on)%(double)(10)));
        }
        hmrecurrence.put("ON" , str);
        hmrecurrence.put("EVERY" ,every+""); //""
      }

      if (timePeriod.equals(ARF_YEAR))
      {
        hmrecurrence.put("FREQUENCY" , ARF_YEAR);
        String str = "";

        if (on >0 && on <=31)
        {
          str = "0,"+on;
        }else{
          str = "1,"+(((int)(on/10))-3)+","+((int)((double)(on)%(double)(10)));
        }
        hmrecurrence.put("ON" , str);
        hmrecurrence.put("EVERY" ,""+every);
      }
    }catch(Exception e){
      System.out.println("[Exception][RecurrenceVO] Exception thrown in fillRecurrenceHashMap(): " + e);
      //e.printStackTrace();
    }
  }    // end fillRecurrenceHashMap

  public HashMap getRecurrenceHM()
  {
    return(hmrecurrence);
  }

  public void hashMapfromHandler(HashMap hashmap)
  {
    try
    {
      if (((Boolean)hashmap.get("IsRecurrence")).booleanValue())
      {
        freq = (String)hashmap.get("FREQUENCY");

        if (freq.equals(ARF_DAY))
        {
          on = (Integer.parseInt((String)hashmap.get("ON")));

          // If you figure out what the hell "17" or "65" are,
          // you MUST document it here. That goes for
          // ANYONE that edits this code.
          if (on == 1)
          {
            on = 17;
          }else{
            on = 65;
          }

          every = (Integer.parseInt((String)hashmap.get("EVERY")));
        }

        if (freq.equals(ARF_WEEK))
        {
          int tgt = 0;
          int i = 0;
          String onstring = (String)hashmap.get("ON");

          i = onstring.indexOf('0');
          if (i != -1)
          {
            tgt = tgt | 0x01;
          }

          i = onstring.indexOf('1');
          if (i != -1)
          {
            tgt = tgt | 0x02 ;
          }

          i = onstring.indexOf('2');
          if (i != -1)
          {
            tgt = tgt | 0x04;
          }

          i = onstring.indexOf('3');
          if (i != -1)
          {
            tgt = tgt | 0x08;
          }

          i = onstring.indexOf('4');
          if (i != -1)
          {
            tgt = tgt | 0x10;
          }

          i = onstring.indexOf('5');
          if (i != -1)
          {
            tgt = tgt | 0x20;
          }

          i = onstring.indexOf('6');
          if (i != -1)
          {
            tgt = tgt | 0x40 ;
          }

          on = tgt;
          every = (Integer.parseInt((String)hashmap.get("EVERY")));
        }   // end if (freq.equals(ARF_WEEK))

        if (freq.equals(ARF_MONTH))
        {
          String onstring = (String)hashmap.get("ON");

          if (Integer.parseInt(onstring.substring(0, 1)) == 0)
          {
            on = Integer.parseInt(onstring.substring(2, 4));
          }

          if (Integer.parseInt(onstring.substring(0,1)) == 1)
          {
            int d1 = Integer.parseInt(onstring.substring(2, 3)) * 10;
            int d2 = Integer.parseInt(onstring.substring(4, 5));
            on = 32 + d1 + d2;
          }
          every = (Integer.parseInt((String)hashmap.get("EVERY")));
        }

        if (freq.equals(ARF_YEAR))
        {
          String onstring = (String)hashmap.get("ON");

          if (Integer.parseInt(onstring.substring(0, 1)) == 0)
          {
            on =  Integer.parseInt(onstring.substring(2,4));
          }

          if (Integer.parseInt(onstring.substring(0, 1)) == 1)
          {
            int d1 = Integer.parseInt(onstring.substring(2, 3)) * 10;
            int d2 = Integer.parseInt(onstring.substring(4, 5));
            on = 32 + d1 + d2;
          }
          every = (Integer.parseInt((String)hashmap.get("EVERY")));
        }
      }   // end if (((Boolean)hashmap.get("IsRecurrence")).booleanValue())
    }catch(Exception e){
      System.out.println("[Exception][RecurrenceVO] Exception thrown in hashMapfromHandler: " + e);
      //e.printStackTrace();
    }
  }   // end hashMapfromHandler() nethod


	public void setEvery(int every)
	{
		this.every = every;
	}

	public int getEvery()
	{
		return(this.every);
	}

	public void setOn(int on)
	{
		this.on = on;
	}

	public int getOn()
	{
		return(this.on);
	}

	public void setStartDate( Date stDate)
	{
	 	this.startDate = stDate;
	}

	public Date getStartDate()
	{
    return(this.startDate);
	}

	public void setUntil(Date until)
	{
		this.until = until ;
	}

	public Date getUntil()
	{
    return(this.until);
	}

	public void setTimePeriod(String freq)
	{
		this.timePeriod = freq;
	}

	public String getTimePeriod()
	{
    return(this.timePeriod);
	}

	public ArrayList getRecurrenceExcept()
	{
		return(this.recurrenceExcept);
	}

	public void setRecurrenceExcept(Date rexDt)
	{
		if (rexDt != null)
		{
			if (this.recurrenceExcept == null)
      {
				this.recurrenceExcept = new ArrayList();
      }

			this.recurrenceExcept.add(rexDt);
		}
	}

	public int getRecurrenceId()
	{
		return this.recurrenceId;
	}

	public String getRecurrenceRule(HashMap mapRecurrence)
	{
		ArrayList weekDayValues = new ArrayList();
		weekDayValues.add("");
		weekDayValues.add("MO");
		weekDayValues.add("TU");
		weekDayValues.add("WE");
		weekDayValues.add("TH");
		weekDayValues.add("FR");
		weekDayValues.add("SA");
		weekDayValues.add("SU");

		ArrayList monthWeekValues = new ArrayList();
		monthWeekValues.add("MO");
		monthWeekValues.add("TU");
		monthWeekValues.add("WE");
		monthWeekValues.add("TH");
		monthWeekValues.add("FR");
		monthWeekValues.add("SA");
		monthWeekValues.add("SU");

		String recurrenceRule = "";
		String frequency = (String) mapRecurrence.get("FREQUENCY");
		String every = (String) mapRecurrence.get("EVERY");
		String on = (String) mapRecurrence.get("ON");

		if (frequency.equals("DAY"))
		{
			if (on != null && on.equals("0") )
			{
			   recurrenceRule = "FREQ=DAILY;INTERVAL="+every;
			}else{
			   recurrenceRule = "FREQ=DAILY;INTERVAL="+every+";BYDAY=MO,TU,WE,TH,FR";
          	}

		}
		else if (frequency.equals("WEEK"))
		{
			StringTokenizer st = new StringTokenizer(on, ",");
			String weekOn = ";BYDAY=";
			while (st.hasMoreTokens())
			{
				int keyValue = Integer.parseInt(st.nextToken());
				weekOn = weekOn + weekDayValues.get(keyValue+1)+",";
			}

			if (weekOn.equals(";BYDAY=")){
				weekOn = "";
			}
			else{
				int weekLen = weekOn.length();
				weekOn = weekOn.substring(0,(weekLen-1));
			}
			if(every != null && every.equals("1")){
			  recurrenceRule = "FREQ=DAILY;INTERVAL="+every+weekOn;
			}
			else{
			  recurrenceRule = "FREQ=WEEKLY;INTERVAL="+every+""+weekOn;
			}
		}
		else if (frequency.equals("MONTH"))
		{
			String onstring = on;
			//System.out.println("MONTH");
			//System.out.println("onstring"+onstring);

			int inton = Integer.parseInt(onstring.substring(0, 1));

			if (inton == 0)
			{
				recurrenceRule = "FREQ=MONTHLY;BYMONTHDAY="+every;
			}

			if (inton == 1)
			{
				int onDay = Integer.parseInt((onstring.substring(2, 3))); //first second etc
				int onWeekDay = Integer.parseInt((onstring.substring(4, 5))); //sun,mon etc
				if (onDay == 5){
					onDay = -1;
				}
				String monthOn = ";BYDAY=" + onDay + monthWeekValues.get(onWeekDay);
			    recurrenceRule = "FREQ=MONTHLY"+monthOn;
			}
		}
		else if (frequency.equals("YEAR"))
		{
			String onstring = on;
			//System.out.println("YEAR");
			//System.out.println("onstring"+onstring);

			int inton = Integer.parseInt(onstring.substring(0, 1));
			int everyMonth = Integer.parseInt(every);
			if (inton == 0)
			{
				int yearMonthly =
					Integer.parseInt(onstring.substring(2, onstring.length()));

				recurrenceRule = "FREQ=YEARLY;BYMONTHDAY="+yearMonthly+";BYMONTH="+(everyMonth+1);
			}

			if (inton == 1)
			{
				int onDay = Integer.parseInt((onstring.substring(2, 3))); //first second etc
				int onWeekDay = Integer.parseInt((onstring.substring(4, 5))); //sun,mon etc
				if (onDay == 5){
					onDay = -1;
				}
				String monthOn = ";BYDAY=" + onDay + monthWeekValues.get(onWeekDay);
				//+";BYMONTH"+(yearMonthly+1)
				recurrenceRule = "FREQ=YEARLY;BYMONTH="+(everyMonth+1)+monthOn;
			}
		}
		return recurrenceRule;
	}

	public void setRecurrenceId(int recurrenceId)
	{
		this.recurrenceId = recurrenceId;
	}

}   // end class definition

