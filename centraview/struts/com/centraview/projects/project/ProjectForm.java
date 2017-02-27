/*
 * $RCSfile: ProjectForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:08 $ - $Author: mcallist $
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

package com.centraview.projects.project;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.CVUtility;
import com.centraview.common.Validation;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.settings.Settings;

public class ProjectForm extends ActionForm
{
	private String title;
	private String description;
	private int entityid;
	private int projectid;
	private String entity;
	private String contact;
	private int contactID;
	private int managerID;
	private String manager;
	private String team;
	private int teamID;
	private String startday;
	private String startmonth;
	private String startyear;
	private String endday;
	private String endmonth;
	private String endyear;
	private String status;
	private int statusID;
	private String budhr = "0";
	private int usedHours;
	private int creator;
	private String creatorName;
	private String createdOn;
	private int modifier;
	private String modifierName;
	private String modifiedOn;
	private String entityLookupButton;
	private String managerLookupButton;
	private String teamLookupButton;
	private String contactLookupButton;
	private String 	text1 = "";
	private String 	text2  = "";
	private String 	text3  = "";
	private int available;

	/*
	*	Stores jurisdictionVec
	*/
	private Vector projectStatusVec;
	
	/**
	 * Returns the budhr.
	 * @return int
	 */
	public String getBudhr() {
		return budhr;
	}

	/**
	 * Returns the contact.
	 * @return String
	 */
	public String getContact() {
		return contact;
	}

	public int getContactID()
	{
		return this.contactID;
	}

	public void setContactID(int contactID)
	{
		this.contactID = contactID;
	}

	public int getProjectid()
	{
		return this.projectid;
	}

	public void setProjectid(int projectid)
	{
		this.projectid = projectid;
	}

	/**
	 * Returns the description.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the endday.
	 * @return int
	 */
	public String getEndday() {
		return endday;
	}

	/**
	 * Returns the endmonth.
	 * @return String
	 */
	public String getEndmonth() {
		return endmonth;
	}

	/**
	 * Returns the endyear.
	 * @return String
	 */
	public String getEndyear() {
		return endyear;
	}

	/**
	 * Returns the entity.
	 * @return String
	 */
	public String getEntity() {
		return entity;
	}

	/**
	 * Returns the entityid.
	 * @return String
	 */
	public int getEntityid() {
		return entityid;
	}

	/**
	 * Returns the manager.
	 * @return String
	 */
	public String getManager() {
		return manager;
	}

	/**
	 * Returns the managerID.
	 * @return int
	 */
	public int getManagerID() {
		return managerID;
	}

	/**
	 * Returns the startday.
	 * @return int
	 */
	public String getStartday() {
		return startday;
	}

	/**
	 * Returns the startmonth.
	 * @return int
	 */
	public String getStartmonth() {
		return startmonth;
	}

	/**
	 * Returns the startyear.
	 * @return int
	 */
	public String getStartyear() {
		return startyear;
	}

	/**
	 * Returns the status.
	 * @return int
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Returns the statusID.
	 * @return int
	 */
	public int getStatusID() {
		return statusID;
	}

	/**
	 * Returns the team.
	 * @return String
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * Returns the status.
	 * @return int
	 */
	public int getTeamID() {
		return teamID;
	}


	/**
	 * Returns the title.
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the budhr.
	 * @param budhr The budhr to set
	 */
	public void setBudhr(String budhr) {
		this.budhr = budhr;
	}

	/**
	 * Sets the contact.
	 * @param contact The contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * Sets the description.
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the endday.
	 * @param endday The endday to set
	 */
	public void setEndday(String endday) {
		this.endday = endday;
	}

	/**
	 * Sets the endmonth.
	 * @param endmonth The endmonth to set
	 */
	public void setEndmonth(String endmonth) {
		this.endmonth = endmonth;
	}

	/**
	 * Sets the endyear.
	 * @param endyear The endyear to set
	 */
	public void setEndyear(String endyear) {
		this.endyear = endyear;
	}

	/**
	 * Sets the entity.
	 * @param entity The entity to set
	 */
	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * Sets the entityid.
	 * @param entityid The entityid to set
	 */
	public void setEntityid(int entityid) {
		this.entityid = entityid;
	}

	/**
	 * Sets the manager.
	 * @param manager The manager to set
	 */
	public void setManager(String manager) {
		this.manager = manager;
	}

	/**
	 * Sets the managerID.
	 * @param managerID The managerID to set
	 */
	public void setManagerID(int managerID) {
		this.managerID = managerID;
	}

	/**
	 * Sets the startday.
	 * @param startday The startday to set
	 */
	public void setStartday(String startday) {
		this.startday = startday;
	}

	/**
	 * Sets the startmonth.
	 * @param startmonth The startmonth to set
	 */
	public void setStartmonth(String startmonth) {
		this.startmonth = startmonth;
	}

	/**
	 * Sets the startyear.
	 * @param startyear The startyear to set
	 */
	public void setStartyear(String startyear) {
		this.startyear = startyear;
	}

	/**
	 * Sets the status.
	 * @param status The status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Sets the statusID.
	 * @param statusID The statusID to set
	 */
	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}

	/**
	 * Sets the team.
	 * @param team The team to set
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * Sets the team.
	 * @param team The team to set
	 */
	public void setTeamID(int teamid) {
		this.teamID = teamid;
	}

	/**
	 * Sets the title.
	 * @param title The title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public int getModifier()
	{
		return this.modifier;
	}

	public void setModifier(int modifier)
	{
		this.modifier = modifier;
	}

	public String getModifierName()
	{
		return this.modifierName;
	}

	public void setModifierName(String modifierName)
	{
		this.modifierName = modifierName;
	}

	public String getModifiedOn()
	{
		return this.modifiedOn;
	}

	public void setModifiedOn(String modifiedOn)
	{
		this.modifiedOn = modifiedOn;
	}

	public int getCreator()
	{
	return this.creator;
	}


	public void setCreator(int creator)
	{
		this.creator = creator;
	}


	public String getCreatorName()
	{
		return this.creatorName;
	}


	public void setCreatorName(String creatorName)
	{
		this.creatorName = creatorName;
	}

	public String getCreatedOn()
	{
		return this.createdOn;
	}

	public void setCreatedOn(String createdOn)
	{
		this.createdOn = createdOn;
	}

	/**
	 * Returns the contactLookupButton.
	 * @return String
	 */
	public String getContactLookupButton() {
		return contactLookupButton;
	}

	/**
	 * Returns the entityLookupButton.
	 * @return String
	 */
	public String getEntityLookupButton() {
		return entityLookupButton;
	}

	/**
	 * Returns the managerLookupButton.
	 * @return String
	 */
	public String getManagerLookupButton() {
		return managerLookupButton;
	}

	/**
	 * Returns the teamLookupButton.
	 * @return String
	 */
	public String getTeamLookupButton() {
		return teamLookupButton;
	}


	/**
	 * Sets the contactLookupButton.
	 * @param contactLookupButton The contactLookupButton to set
	 */
	public void setContactLookupButton(String contactLookupButton) {
		this.contactLookupButton = contactLookupButton;
	}

	/**
	 * Sets the entityLookupButton.
	 * @param entityLookupButton The entityLookupButton to set
	 */
	public void setEntityLookupButton(String entityLookupButton) {
		this.entityLookupButton = entityLookupButton;
	}

	/**
	 * Sets the managerLookupButton.
	 * @param managerLookupButton The managerLookupButton to set
	 */
	public void setManagerLookupButton(String managerLookupButton) {
		this.managerLookupButton = managerLookupButton;
	}

	/**
	 * Sets the teamLookupButton.
	 * @param teamLookupButton The teamLookupButton to set
	 */
	public void setTeamLookupButton(String teamLookupButton) {
		this.teamLookupButton = teamLookupButton;
	}

	/**
	 * Returns the text1.
	 * @return String
	 */
	public String getText1() {
		return text1;
	}

	/**
	 * Returns the text2.
	 * @return String
	 */
	public String getText2() {
		return text2;
	}

	/**
	 * Returns the text3.
	 * @return String
	 */
	public String getText3() {
		return text3;
	}

	/**
	 * Sets the text1.
	 * @param text1 The text1 to set
	 */
	public void setText1(String text1) {
		this.text1 = text1;
	}

	/**
	 * Returns the usedHours.
	 * @return int
	 */
	public int getUsedHours() {
		return usedHours;
	}

	/**
	 * Sets the text2.
	 * @param text2 The text2 to set
	 */
	public void setText2(String text2) {
		this.text2 = text2;
	}

	/**
	 * Sets the text3.
	 * @param text3 The text3 to set
	 */
	public void setText3(String text3) {
		this.text3 = text3;
	}

	/**
	 * Sets the usedHours.
	 * @param usedHours The usedHours to set
	 */
	public void setUsedHours(int usedHours) {
		this.usedHours = usedHours;
	}


	/*	Validation is dome at the time of save and edit.
	 * 	Validation is not done at the time of vertical navigation.
	 * 	Validation is done on following user input
	 *	1. Required fileds
	 *	2. Maxlength
	 *	3. Integer
	 *	4. Date
	 *	5. Time
	 *	6. Date comparison
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{

		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();
		try{
			String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
			ProjectFacadeHome phh = (ProjectFacadeHome)CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");
			ProjectFacade remote = (ProjectFacade) phh.create();
			remote.setDataSource(dataSource);
			Vector statusCol = remote.getProjectStatusList();
			this.setProjectStatusVec(statusCol);
		}
		catch (Exception e)
		{
			System.out.println("[Exception][ProjectForm.Validate] Exception Thrown: " + e);
		}
		try
		{
			// initialize validation
			Validation validation = new Validation();

      if (getTitle() == null || getTitle().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Title"));
      }

			//validation.checkForInteger ("label.projects.task.budgetedhours", getBudhr(), "error.application.integer", "", errors);

			if (
				 (startday != null && startday.length() != 0) ||
				 (getStartyear() != null && getStartyear().length() != 0) ||
				 (getStartmonth() != null && getStartmonth().length() != 0)
			   )
			{
        //validation.checkForDate("label.projects.task.startdate", getStartyear(), getStartmonth(), getStartday(), "error.application.date", "", errors);
			}
			if (
				 (endday != null && endday.length() !=0) ||
				 (getEndyear() != null && getEndyear().length() !=0) ||
				 (getEndmonth() != null && getEndmonth().length() !=0)
			   )
			{
        //validation.checkForDate("label.projects.task.enddate", getEndyear(), getEndmonth(), getEndday(), "error.application.date", "", errors);
			}

			if (
					(getEndyear() != null && getEndyear().length() != 0) &&
					(getStartyear() == null || getStartyear().length() == 0)
				)
			{
        //validation.checkForRequired ("label.projects.task.startdate", getStartyear(), "error.projects.task.startdate", "", errors);
			}

			if (startday != null && startday.length() != 0 && endday != null && endday.length() !=0) {
        //validation.checkForDateComparison ("label.projects.task.startdate", getStartyear(), getStartmonth(),getStartday(), "label.projects.task.enddate", getEndyear(), getEndmonth(), getEndday(), "error.application.datecomparison", "", errors);
      }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return errors;
	}

	public int getAvailable()
	{
		return this.available;
	}

	public void setAvailable(int available)
	{
		this.available = available;
	}

	/**
	 * @return The Project Status Vector.
	 */
	public Vector getProjectStatusVec()
	{
	  return this.projectStatusVec;
	}

	/**
	 * Set the Project Status Vector
	 *
	 * @param projectStatusVec
	 */
	public void setProjectStatusVec(Vector projectStatusVec)
	{
	  this.projectStatusVec = projectStatusVec;
	}	
}
