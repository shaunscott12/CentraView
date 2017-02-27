/*
 * $RCSfile: TimeSheetVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:24 $ - $Author: mking_cv $
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

/*
 * This is TimeSheet Value Object which represent the TimeSheet Data. 
 * This Class implements Serializable Interface.
 *
 * @author  Nilesh Ghorpade
 * @version 1.0
 */

package com.centraview.hr.helper;


import java.io.Serializable;

import com.centraview.common.TimeSlipList;


/*
*NOTE: The Creator Is Same as the Employee field in the Form NewTimeSheetForm
	   Initially the ModifiedByName and the CreatorName are the  same.
	   Also the created and the Modified date are the same initially.		
*/




public class TimeSheetVO implements Serializable
{
	private		int 		timesheetID;
	private		int 		modifiedBy;
	private		int 		owner;//individualID	
	private		int 		creator;//userId
	private		int 		status;
	private		int 		reportingToId;
	private		String 		description;
	private		String		notes;
	private		java.sql.Timestamp	createdDate;
	private		java.sql.Timestamp	modifiedDate;
	private		java.sql.Date	fromDate;
	private		java.sql.Date	toDate;
	private		TimeSlipList timeslipList;
	private		float		totalDuration;
	private		String		reportingTo;
	private		String 		creatorName;
	private		String		modifiedbyName;
	private		String		ownerName;
	

	public void setOwnerName(String AownerName)
	{
		ownerName = AownerName;
	}
	
	public String getOwnerName()
	{
		return ownerName;
	}
	
	public void setReportingToId(int iReportingToId)
	{
		reportingToId = iReportingToId;
	}
	
	public int getReportingToId()
	{
		return reportingToId;
	}
	
	
	public void setModifiedByName(String sModifiedBy)
	{
		modifiedbyName = sModifiedBy;
	}
	
	public String getModifiedByName()
	{
		return modifiedbyName;
	}
	
	
	public void setReportingTo(String sReportingTo)
	{
		reportingTo = sReportingTo;
	}
	
	public String getReportingTo()
	{
		return reportingTo;
	}
	
	public void setCreatorName(String sEmployee)
	{
		creatorName = sEmployee;
	}
	
	public String getCreatorName()
	{
		return creatorName;
	}
	
	
	public void setTimesheetID(int AtimesheetID)
	{
		timesheetID = AtimesheetID;
	}
	
	public int getTimesheetID()
	{
		return timesheetID;
	}
	
	
	public void setModifiedBy(int AmodifiedBy)
	{
		modifiedBy = AmodifiedBy;
	}
	
	public int getModifiedBy()
	{
		return modifiedBy;
	}
	
	
	public void setOwner(int Aowner)
	{
		owner = Aowner;
	}
	
	public int getOwner()
	{
		return owner;
	}
	
	
	public void setCreator(int Acreator)
	{
		creator  = Acreator;
	}
	
	public int getCreator()
	{
		return creator;
	}
	
	
	public void setStatus(int Astatus)
	{
		status  = Astatus;
	}
	
	public int getStatus()
	{
		return status;
	}
	
	public void setDescription(String Adescription)
	{
		description = Adescription;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	
	public void setNotes(String Anotes)
	{
		notes = Anotes;
	}
	
	public String getNotes()
	{
		return notes;
	}
	
	public void setCreatedDate(java.sql.Timestamp AcreatedDate)
	{
		createdDate = AcreatedDate;
	}
	
	public java.sql.Timestamp getCreatedDate()
	{
		return createdDate;
	}
	
	public void setModifiedDate(java.sql.Timestamp AmodifiedDate)
	{
		modifiedDate = AmodifiedDate;
	}
	
	
	public java.sql.Timestamp getModifiedDate()
	{
		return modifiedDate;
	}
	
	public void setFromDate(java.sql.Date AfromDate)
	{
		System.out.println("\n\n IN THE setFromDate of TOMESHEETVO AfromDate" + AfromDate);
		fromDate = AfromDate;
		System.out.println("\n\n IN THE setFromDate of TOMESHEETVO AfromDate" + fromDate);
	}
	
	public java.sql.Date getFromDate()
	{
		return fromDate;
	}
	
	public void setToDate(java.sql.Date AtoDate)
	{
		toDate = AtoDate;
	}
	
	public java.sql.Date getToDate()
	{
		return toDate;
	}
	
	public void setTimeSlipList(TimeSlipList ATimeslipList)
	{
		timeslipList = ATimeslipList;
	}
	
	public TimeSlipList getTimeSlipList()
	{
		return timeslipList;
	}
	
	public void setTotalDuration(float AtotalDuration)
	{
		totalDuration = AtotalDuration;
	}
	
	public float getTotalDuration()
	{
		return totalDuration;
	}
	
}
