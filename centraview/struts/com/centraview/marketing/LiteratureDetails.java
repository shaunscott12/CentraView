/*
 * $RCSfile: LiteratureDetails.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:14 $ - $Author: mking_cv $
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
/**
 *	LiteratureDetails.java
 *	Author Sunita
 * 	Date : 21/08/2003
 *	version :
 */

package com.centraview.marketing;

import java.sql.Timestamp;
/**
 *	class LiteratureDetails
 */
public class LiteratureDetails implements java.io.Serializable
{
	private String title = null;
	private String detail = null;
	private int assignedtoid;
	private int entityid;
	private int individualid;
	private String assignedtoname;
	private String entityname;
	private String individualname;
	private int statusid;
	private int deliveryid;
	private Timestamp duebydate = null;
	private int creator;
	private int ownerid;
	private String literatureId = null;
	private String literatureName = null;

 	/**
	 * constructor LiteratureDetails with no arguments
	 */
	public LiteratureDetails()
	{
	}

	/**
	 * method getAssignedtoid
	 * @return assignedtoid
	 */
	public int getAssignedtoid()
	{
		return this.assignedtoid;
	}

	/**
	 * method setAssignedtoid
	 * @param assignedtoid
	 */
	public void setAssignedtoid(int assignedtoid)
	{
		this.assignedtoid = assignedtoid;
	}

	/**
	 * method getCreator
	 * @return creator
	 */
	public int getCreator()
	{
		return this.creator;
	}

	/**
	 * method setCreator
	 * @param creator
	 */
	public void setCreator(int creator)
	{
		this.creator = creator;
	}

	/**
	 * method getDeliveryid
	 * @return deliveryid
	 */
	public int getDeliveryid()
	{
		return this.deliveryid;
	}

	/**
	 * method setDeliveryid
	 * @param deliveryid
	 */
	public void setDeliveryid(int deliveryid)
	{
		this.deliveryid = deliveryid;
	}

	/**
	 * method getDuebydate
	 * @return duebydate
	 */
	public Timestamp getDuebydate()
	{
		return this.duebydate;
	}

	/**
	 * method setDuebydate
	 * @param duebydate
	 */
	public void setDuebydate(Timestamp duebydate)
	{
		this.duebydate = duebydate;
	}

	/**
	 * method getEntityid
	 * @return entityid
	 */
	public int getEntityid()
	{
		return this.entityid;
	}

	/**
	 * method setEntityid
	 * @param entityid
	 */
	public void setEntityid(int entityid)
	{
		this.entityid = entityid;
	}

	/**
	 * method getIndividualid
	 * @return individualid
	 */
	public int getIndividualid()
	{
		return this.individualid;
	}

	/**
	 * method setIndividualid
	 * @param individualid
	 */
	public void setIndividualid(int individualid)
	{
		this.individualid = individualid;
	}

	/**
	 * method getOwnerid
	 * @return ownerid
	 */
	public int getOwnerid()
	{
		return this.ownerid;
	}

	/**
	 * method setOwnerid
	 * @param ownerid
	 */
	public void setOwnerid(int ownerid)
	{
		this.ownerid = ownerid;
	}

	/**
	 * method getStatusid
	 * @return statusid
	 */
	public int getStatusid()
	{
		return this.statusid;
	}

	/**
	 * method setStatusid
	 * @param statusid
	 */
	public void setStatusid(int statusid)
	{
		this.statusid = statusid;
	}

	/**
	 * method getDetail
	 * @return detail
	 */
	public String getDetail()
	{
		return this.detail;
	}

	/**
	 * method setDetail
	 * @param Detail
	 */
	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	/**
	 * method getLiteratureId
	 * @return literatureId
	 */
	public String getLiteratureId()
	{
		return this.literatureId;
	}

	/**
	 * method setLiteratureId
	 * @param literatureId
	 */
	public void setLiteratureId(String literatureId)
	{
		this.literatureId = literatureId;
	}

	/**
	 * method getLiteratureName
	 * @return literatureName
	 */
	public String getLiteratureName()
	{
		return this.literatureName;
	}

	/**
	 * method setLiteratureName
	 * @param literatureName
	 */
	public void setLiteratureName(String literatureName)
	{
		this.literatureName = literatureName;
	}

	/**
	 * method getTitle
	 * @return title
	 */
	public String getTitle()
	{
		return this.title;
	}

	/**
	 * method setTitle
	 * @param title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * method getAssignedtoname
	 * @return assignedtoname
	 */
	public String getAssignedtoname()
	{
		return this.assignedtoname;
	}

	/**
	 * method setAssignedtoname
	 * @param assignedtoname
	 */
	public void setAssignedtoname(String assignedtoname)
	{
		this.assignedtoname = assignedtoname;
	}

	/**
	 * method getEntityname
	 * @return entityname
	 */
	public String getEntityname()
	{
		return this.entityname;
	}

	/**
	 * method setEntityname
	 * @param entityname
	 */
	public void setEntityname(String entityname)
	{
		this.entityname = entityname;
	}

	/**
	 * method getIndividualname
	 * @return individualname
	 */
	public String getIndividualname()
	{
		return this.individualname;
	}

	/**
	 * method setIndividualname
	 * @param individualname
	 */
	public void setIndividualname(String individualname)
	{
		this.individualname = individualname;
	}
}
