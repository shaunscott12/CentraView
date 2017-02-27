/*
 * $RCSfile: EventDetails.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:12 $ - $Author: mking_cv $
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
package com.centraview.marketing;

import java.sql.Timestamp;
import java.util.ArrayList;

public class EventDetails implements java.io.Serializable
{
	private int eventid;
	private String name = null;
	private String detail = null;
	private String whoshouldattend;
	private String formember;
	private int maxattendees;
	private int moderatorid;
	private String moderatorname;
	private Timestamp startdate = null;
	private Timestamp enddate = null;
	private int creator;
	private String individualid[] = null;
	private String individualname[] = null;
	private Timestamp createddate = null;
	private Timestamp modifieddate = null;
	private ArrayList attachedFiles;

	public EventDetails()
	{
	}

	public int getMaxattendees()
	{
		return this.maxattendees;
	}

	public void setMaxattendees(int maxattendees)
	{
		this.maxattendees = maxattendees;
	}

	public int getCreator()
	{
		return this.creator;
	}

	public void setCreator(int creator)
	{
		this.creator = creator;
	}

	public Timestamp getStartdate()
	{
		return this.startdate;
	}

	public void setStartdate(Timestamp startdate)
	{
		this.startdate = startdate;
	}

	public Timestamp getEnddate()
	{
		return this.enddate;
	}

	public void setEnddate(Timestamp enddate)
	{
		this.enddate = enddate;
	}

	public int getModeratorid()
	{
		return this.moderatorid;
	}

	public void setModeratorid(int moderatorid)
	{
		this.moderatorid = moderatorid;
	}


	public String getDetail()
	{
		return this.detail;
	}

	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	public int getEventid()
	{
		return this.eventid;
	}

	public void setEventid(int eventid)
	{
		this.eventid = eventid;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getWhoshouldattend()
	{
		return this.whoshouldattend;
	}

	public void setWhoshouldattend(String whoshouldattend)
	{
		this.whoshouldattend = whoshouldattend;
	}


	public String getFormember()
	{
		return this.formember;
	}

	public void setFormember(String formember)
	{
		this.formember = formember;
	}

	public String getModeratorname()
	{
		return this.moderatorname;
	}

	public void setModeratorname(String moderatorname)
	{
		this.moderatorname = moderatorname;
	}

	public String[] getIndividualid()
	{
		return this.individualid;
	}

	public void setIndividualid(String[] individualid)
	{
		this.individualid = individualid;
	}

	public String[] getindividualname()
	{
		return this.individualname;
	}

	public void setIndividualname(String[] individualname)
	{
		this.individualname = individualname;
	}

	public Timestamp getCreateddate()
	{
		return this.createddate;
	}

	public void setCreateddate(Timestamp createddate)
	{
		this.createddate = createddate;
	}

	public Timestamp getModifieddate()
	{
		return this.modifieddate;
	}

	public void setModifieddate(Timestamp modifieddate)
	{
		this.modifieddate = modifieddate;
	}

	public ArrayList getAttachedFiles()
	{
		return this.attachedFiles;
	}

	public void setAttachedFiles(ArrayList attachedFiles)
	{
		this.attachedFiles = attachedFiles;
	}

}

