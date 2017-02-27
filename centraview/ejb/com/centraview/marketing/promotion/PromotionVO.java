/*
 * $RCSfile: PromotionVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:42 $ - $Author: mking_cv $
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

package com.centraview.marketing.promotion;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Vector;

import com.centraview.marketing.ItemLines;


/*
 * This is Promotion Value Object which represent the Address Data..
 *
 * @author 
 * @version 1.0
 */

public class PromotionVO implements Serializable
{
	private int promotionid ; 
	private String name ;
	private String description ; 
	private Timestamp startdate ; 
	private Timestamp enddate ; 
	private String status;
	private String notes;
	private ItemLines itemlines ; 
	private Vector customfield;
	
	/**
	* This method returns customfield
	*/
	public Vector getCustomfield()
	{
		return this.customfield;
	}
	
	/**
	* This method sets the customfield 
	*/

	public void setCustomfield(Vector customfield)
	{
		this.customfield = customfield;
	}
	
	/**
	* This method returns description of promotion
	*/
	
	public String getDescription()
	{
		return this.description;
	}

	/**
	* This method sets description of promotion
	*/
	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	* This method returns enddate
	*/	
	public Timestamp getEnddate()
	{
		return this.enddate;
	}
	
	/**
	* This method sets enddate
	*/	
	public void setEnddate(Timestamp enddate)
	{
		this.enddate = enddate;
	}
	
	/**
	* This method returns LineItems
	*/
	public ItemLines getItemlines()
	{
		return this.itemlines;
	}

	/**
	* This method sets LineItems
	*/
	public void setItemlines(ItemLines ItemLines)
	{
		this.itemlines = ItemLines;
	}

	/**
	* This method returns name field 
	*/
	public String getName()
	{
		return this.name;
	}

	
	/**
	* This method sets name field 
	*/
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	This method returns notes
	*/
	public String getNotes()
	{
		return this.notes;
	}
	
	/**
	this method sets the notes
	*/
	public void setNotes(String notes)
	{
		this.notes = notes;
	}

	/**
	This method returns the start date 
	*/
	public Timestamp getStartdate()
	{
		return this.startdate;
	}
	
	/**
	This method sets  the start date 
	*/
	public void setStartdate( Timestamp startdate )
	{
		this.startdate = startdate;
	}

	/**
	This method returns  the status 
	*/
	public String getStatus()
	{
		return this.status;
	}
	
	/**
	This method sets the status 
	*/
	public void setStatus(String status)
	{
		this.status = status;
	}

	
	public int getPromotionid()
	{
		return this.promotionid;
	}

	public void setPromotionid(int promotionid)
	{
		this.promotionid = promotionid;
	}
}





 