/*
 * $RCSfile: TicketDBVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:13 $ - $Author: mking_cv $
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


import java.sql.Timestamp;
import java.util.Vector;

import com.centraview.contact.entity.EntityVO;
import com.centraview.support.SupportVO;


public class TicketDBVO extends SupportVO
{

	public static final String TK_OCSTATUS_OPEN = "OPEN";
	public static final String TK_OCSTATUS_CLOSE = "CLOSE";

	private String subject;
	private String details;

	private int statusId;
	private String statusName;
	private int priorityId;
	private String priorityName;

	private Timestamp closeDate;

	private int managerId;
	private int assignedToId;
	private int refEntityId;
	private int refIndividualId;

	private String managerName;
	private String assignedToName;
	private EntityVO entityVO;
	private String refIndividualName;

	private Vector customField = null;

	private Vector threadVO = null;

	// IQ - desines is a ticket is open or closed
	// cannot be tracked with status column cause that is database dependent value
	// while ocstatus is hard value
	private String ocStatus = TK_OCSTATUS_OPEN;

	/**
	 * Constructor
	 *
	 */
	public TicketDBVO()
	{
		super();
	}



	public int getAssignedToId()
	{
		return this.assignedToId;
	}

	public void setAssignedToId(int assignedToId)
	{
		this.assignedToId = assignedToId;
	}



	public Timestamp getCloseDate()
	{
		return this.closeDate;
	}

	public void setCloseDate(Timestamp closeDate)
	{
		this.closeDate = closeDate;
	}


	public Vector getCustomField()
	{
		return this.customField;
	}
/*
	public void setCustomField(CustomFieldVO customField)
	{
		if(this.customField == null)
			this.customField = new Vector();

		this.customField.add(customField);
	}
*/

	public void setCustomField(Vector customFieldVector)
	{
		this.customField = customFieldVector;

	}


	public int getManagerId()
	{
		return this.managerId;
	}

	public void setManagerId(int managerId)
	{
		this.managerId = managerId;
	}


	public int getPriorityId()
	{
		return this.priorityId;
	}

	public void setPriorityId(int priorityId)
	{
		this.priorityId = priorityId;
	}


	public int getRefEntityId()
	{
		return this.refEntityId;
	}

	public void setRefEntityId(int refEntityId)
	{
		this.refEntityId = refEntityId;
	}


	public int getRefIndividualId()
	{
		return this.refIndividualId;
	}

	public void setRefIndividualId(int refIndividualId)
	{
		this.refIndividualId = refIndividualId;
	}


	public int getStatusId()
	{
		return this.statusId;
	}

	public String getSubject()
	{
		return subject;

	}
	public String getDetails()
	{
		return details;

	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}

	public void setStatusId(int statusId)
	{
		this.statusId = statusId;
	}


	public String getAssignedToName()
	{
		return this.assignedToName;
	}

	public void setAssignedToName(String assignedToName)
	{
		this.assignedToName = assignedToName;
	}


	public String getManagerName()
	{
		return this.managerName;
	}

	public void setManagerName(String managerName)
	{
		this.managerName = managerName;
	}


	public EntityVO getEntityVO()
	{
		return this.entityVO;
	}

	public void setEntityVO(EntityVO entityVO)
	{
		this.entityVO = entityVO;
	}


	public String getRefIndividualName()
	{
		return this.refIndividualName;
	}

	public void setRefIndividualName(String refIndividualName)
	{
		this.refIndividualName = refIndividualName;
	}


	public Vector getThreadVO()
	{
		return this.threadVO;
	}

	public void setThreadVO(ThreadVO threadVO)
	{

		if(this.threadVO == null)
			this.threadVO = new Vector();

		this.threadVO.add(threadVO);
	}


	public String getStatusName()
	{
		return this.statusName;
	}

	public void setStatusName(String statusName)
	{
		this.statusName = statusName;
	}


	public String getPriorityName()
	{
		return this.priorityName;
	}

	public void setPriorityName(String priorityName)
	{
		this.priorityName = priorityName;
	}


	public String getOCStatus()
	{
		return this.ocStatus;
	}

	public void setOCStatus(String ocStatus)
	{
		if(!(ocStatus.equals(TK_OCSTATUS_OPEN) || ocStatus.equals(TK_OCSTATUS_CLOSE)))
			this.ocStatus = TK_OCSTATUS_OPEN;
		else
			this.ocStatus = ocStatus;
	}
}
