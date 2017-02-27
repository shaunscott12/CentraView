/*
 * $RCSfile: TimeSlipDBVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:47 $ - $Author: mking_cv $
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

package com.centraview.projects.helper;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/*
 * This is TimeSlip Value Object which represent the TimeSlip Data.
 * This Class implements Serializable Interface.
 *
 * @author  
 * @version 1.0
 */
public class TimeSlipDBVO implements Serializable
{
	private int timeSlipID;
	private int taskID;
	private int projectID;
	private int ticketID;
	private String ticket;
	private String description;
	private String projectTitle;
	private String taskTitle;
	private java.sql.Date date;
	private Time start;
	private Time end;
	private int owner ;
	private int creator;
	private int modifiedBy;
	private Timestamp created;
	private Timestamp modified;
	private float breakTime;
	private float hours;

	public TimeSlipDBVO()
	{
	}

	public int getTimeSlipID()
	{
		return timeSlipID;
	}

	public void setTimeSlipID(int timeSlipID)
	{
		this.timeSlipID = timeSlipID;
	}

	public String getTicket()
	{
		return ticket;
	}

	public void setTicket(String ticket)
	{
		this.ticket = ticket;
	}

	public int getTaskID()
	{
		return taskID;
	}

	public void setTaskID(int taskID)
	{
		this.taskID = taskID;
	}

	public int getProjectID()
	{
		return projectID;
	}

	public void setProjectID(int projectID)
	{
		this.projectID = projectID;
	}

	public int getTicketID()
	{
		return ticketID;
	}

	public void setTicketID(int ticketID)
	{
		this.ticketID = ticketID;
	}


	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public java.sql.Date getDate()
	{
		return date;
	}

	public void setDate(java.sql.Date date)
	{
		this.date = date;
	}

	public String getProjectTitle()
	{
		return projectTitle;
	}

	public void setProjectTitle(String projectTitle)
	{
		this.projectTitle = projectTitle;
	}

	public String getTaskTitle()
	{
		return taskTitle;
	}

	public void setTaskTitle(String taskTitle)
	{
		this.taskTitle = taskTitle;
	}

	public Time getStart()
	{
		return start;
	}

	public void setStart(Time start)
	{
		this.start = start;
	}

	public Time getEnd()
	{
		return end;
	}

	public void setEnd(Time end)
	{
		this.end = end;
	}

	public float getBreakTime()
	{
		return breakTime;
	}

	public void setBreakTime(float breakTime)
	{
		this.breakTime = breakTime;
	}

	/**
	 * @return  The Owner
	 */
	public int getOwner()
	{
		return this.owner;
	}


	/**
	 *	Set owner ID
	 *
	 * @param   owner  OwnerID
	 */
	public void setOwner(int owner)
	{
		this.owner = owner;
	}

	/**
	 * @return  The creator
	 */
	public int getCreator()
	{
		return creator;
	}

	/**
	 *	Set owner ID
	 *
	 * @param   owner  OwnerID
	 */
	public void setCreator(int creator)
	{
		this.creator = creator;
	}

			/**
	 * @return   The modifiedBy ID.
	 */
	public int getModifiedBy()
	{
		return this.modifiedBy;
	}


	/**
	 *	Set ModifiedByID
	 *
	 * @param   modifiedBy  ModifiedBy ID.
	 */
	public void setModifiedBy(int modifiedBy)
	{
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModified()
	{
		return modified;
	}

	public void setModified(Timestamp modified)
	{
		this.modified = modified;
	}

	public Timestamp getCreated()
	{
		return created;
	}

	public void setCreated(Timestamp created)
	{
		this.created = created;
	}

	/**
	 * @return  The  TimeSlip value Object.
	 */
	public TimeSlipDBVO getVO()
	{
		return this;
	}


	public float getHours()
	{
	return this.hours;
	}

	public void setHours(float hours)
	{
	this.hours = hours;
	}
}