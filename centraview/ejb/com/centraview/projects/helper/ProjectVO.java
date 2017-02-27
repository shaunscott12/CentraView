/*
 * $RCSfile: ProjectVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:46 $ - $Author: mking_cv $
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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Vector;
/*
 * This is Project Value Object which represent the Project Data. 
 * This Class implements Serializable Interface.
 *
 * @author  Vaijayanti Vaidya
 * @version 1.0
 */
public class ProjectVO implements Serializable
{
	private int projectID;
	private int entityID;
	private String entityName;
	
	private int contactID;
	private String contactName;
	
	private int groupID;
	private String groupName;
	
	private int statusID;
	
	private String title;
	private String description;
	private StatusVO statusVO;
	private Date start;
	private Date end;
	private int budgetedHours;
	private float usedHours;
	private Vector customFields; // collection of customfieldVO
	private int owner ;
	private String ownerName;
	private int creator;
	private String creatorName;
	private int modifiedBy;
	private String modifierName;
	private Timestamp created;
	private Timestamp modified;
	public String selectedStatus;
	private LinkedHashMap stat;
	private String manager;
	private int managerID;
	
	
	public ProjectVO() 
	{
		this.customFields = new Vector();
	}
	
	public int getProjectID()
	{
		return projectID;
	}

	public void setProjectID(int projectID)
	{
		this.projectID = projectID;
	}

	public int getEntityID()
	{
		return entityID;
	}

	public void setEntityID(int entityID)
	{
		this.entityID = entityID;
	}

	public String getEntityName()
	{
		return entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}
	
	public int getContactID()
	{
		return contactID;
	}

	public void setContactID(int contactID)
	{
		this.contactID = contactID;
	}

	public String getContactName()
	{
		return contactName;
	}

	public void setContactName(String contactName)
	{
		this.contactName = contactName;
	}
	
	public int getGroupID()
	{
		return groupID;
	}

	public void setGroupID(int groupID)
	{
		this.groupID = groupID;
	}

	public String getGroupName()
	{
		return groupName;
	}

	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}


	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Date getStart()
	{
		return start;
	}

	public void setStart(Date start)
	{
		this.start = start;
	}

	public Date getEnd()
	{
		return end;
	}

	public void setEnd(Date end)
	{
		this.end = end;
	}

	public int getBudgetedHours()
	{
		return budgetedHours;
	}

	public void setBudgetedHours(int budgetedHours)
	{
		this.budgetedHours = budgetedHours;
	}

	public float getUsedHours()
	{
		return usedHours;
	}

	public void setUsedHours(float usedHours)
	{
		this.usedHours = usedHours;
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
	 * @return  The Owner   
	 */
	public String getOwnerName()
	{
		return this.ownerName;
	}


	/**
	 *	Set owner 
	 *
	 * @param   owner  OwnerID
	 */
	public void setOwnerName(String owner)
	{
		this.ownerName = owner;
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
	 * @return  The creator name  
	 */
	public String getCreatorName()
	{
		return creatorName;
	}

	/**
	 *	Set creator Name 
	 *
	 * @param   creator Name 
	 */
	public void setCreatorName(String creatorName)
	{
		this.creatorName = creatorName;
	}

	/**
	 * @return   The Collection of CustomFields.  
	 */
	public Vector getCustomField()
	{
		return this.customFields;
	}

	/**
	 *	Set the Collection of CustomFields.
	 *
	 * @param   customFields  Collection of CustomFields
	 */
	public void setCustomField(CustomFieldVO customField)
	{
		this.customFields.add(customField);
	}

	/**
	 *	Set the Collection of CustomFieldVO.
	 *
	 * @param   customFields  Collection of CustomFields
	 */

	public void setCustomFieldVOs(Vector vec)
	{
		this.customFields = vec;
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

	/**
	* @return   The Modifier Name .  
	*/
	public String getModifierName()
	{
		return this.modifierName;
	}

	/**
	* Set ModifierName
	*
	* @param   modifierName  Modifier Name.
	*/
	public void setModifierName(String modifiedBy)
	{
		this.modifierName = modifiedBy;
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
	
	public int getStatusID()
	{
		return statusID;
	}
	public void setStatusID(int statusID)
	{
		this.statusID = statusID;
	}
	/**
	 * @return  The Project value Object.   
	 */
	public ProjectVO getVO()
	{
		return this; 
	}
	
	public String getSelectedStatus()
	{
	return this.selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus)
	{
	this.selectedStatus = selectedStatus;
	}
	
	public LinkedHashMap getStat()
	{
		return this.stat;
	}

	public void setStat(LinkedHashMap stat)
	{
		this.stat = stat;
	}
	public void setStat(int id,String name)
	{
		if(this.stat == null)
			this.stat = new LinkedHashMap();
		this.stat.put(new Integer(id),name);		
	}
	public String getManager() {
		return manager;
	}

	public int getManagerID() {
		return managerID;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public void setManagerID(int managerID) 
	{
		this.managerID = managerID;
	}
}									
