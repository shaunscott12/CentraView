/*
 * $RCSfile: TaskForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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
package com.centraview.projects.task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.common.Validation;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.projects.helper.TaskVO;


public class TaskForm extends org.apache.struts.action.ActionForm
{
	private String 	title = "";
	private String 	description = "";
	private String 	project = "";
	private String 	projectid = "";
	private String 	parentTask = "";
	private String 	parenttaskid = "";
	private String 	milestone  = "No";
	private String 	sendAlert = "No";
	private String 	alertTypeAlert = "";
	private String 	alertTypeEmail = "";
	private String[] 	sendTo = {};
	private String 	manager = "";
	private String 	managerID = "";
	private String 	startday = "";
	private String 	startmonth = "";
	private String 	startyear = "";
	private String 	endday  = "";
	private String 	endmonth  = "";
	private String 	endyear = "";
	private String[] 	assignedTo = {};
	private String 	text1 = "";
	private String 	text2  = "";
	private String 	text3  = "";
	private String	status = "";
	private String 	percentComplete = "";
	private Collection assignedtoCol;
	private Collection sendtoCol;
	private Collection statusCol;
	private String created;
	private String modified;
	private String taskid;
	private String modifiedOn;
	private String createdOn;
	private String createdbyid;
	private String modifiedbyid;
	private LinkedHashMap crumbs;
	private String selectedStatus="";
	private String assignedTodummy;
	private String sendtodummy;

	/**
	 * Returns the alertTypeAlert.
	 * @return String
	 */
	public String getAlertTypeAlert() {
		return alertTypeAlert;
	}

	/**
	 * Returns the alertTypeEmail.
	 * @return String
	 */
	public String getAlertTypeEmail() {
		return alertTypeEmail;
	}

	/**
	 * Returns the assignedTo.
	 * @return String[]
	 */
	public String[] getAssignedTo() {
		return assignedTo;
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
	 * @return String
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
	 * Returns the manager.
	 * @return String
	 */
	public String getManager() {
		return manager;
	}

	/**
	 * Returns the managerID.
	 * @return String
	 */
	public String getManagerID() {
		return managerID;
	}

	/**
	 * Returns the milestone.
	 * @return String
	 */
	public String getMilestone() {
		return milestone;
	}

	/**
	 * Returns the parentTask.
	 * @return String
	 */
	public String getParentTask() {
		return parentTask;
	}

	/**
	 * Returns the parenttaskid.
	 * @return String
	 */
	public String getParenttaskid() {
		return parenttaskid;
	}

	/**
	 * Returns the project.
	 * @return String
	 */
	public String getProject() {
		return project;
	}

	/**
	 * Returns the projectid.
	 * @return String
	 */
	public String getProjectid() {
		return projectid;
	}

	/**
	 * Returns the sendAlert.
	 * @return String
	 */
	public String getSendAlert() {
		return sendAlert;
	}

	/**
	 * Returns the sendTo.
	 * @return String[]
	 */
	public String[] getSendTo() {
		return sendTo;
	}

	/**
	 * Returns the startday.
	 * @return String
	 */
	public String getStartday() {
		return startday;
	}

	/**
	 * Returns the startmonth.
	 * @return String
	 */
	public String getStartmonth() {
		return startmonth;
	}

	/**
	 * Returns the startyear.
	 * @return String
	 */
	public String getStartyear() {
		return startyear;
	}

	/**
	 * Returns the title.
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the alertTypeAlert.
	 * @param alertTypeAlert The alertTypeAlert to set
	 */
	public void setAlertTypeAlert(String alertTypeAlert) {
		this.alertTypeAlert = alertTypeAlert;
	}

	/**
	 * Sets the alertTypeEmail.
	 * @param alertTypeEmail The alertTypeEmail to set
	 */
	public void setAlertTypeEmail(String alertTypeEmail) {
		this.alertTypeEmail = alertTypeEmail;
	}

	/**
	 * Sets the assignedTo.
	 * @param assignedTo The assignedTo to set
	 */
	public void setAssignedTo(String[] assignedTo) {
		this.assignedTo = assignedTo;
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
	public void setManagerID(String managerID) {
		this.managerID = managerID;
	}

	/**
	 * Sets the milestone.
	 * @param milestone The milestone to set
	 */
	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}

	/**
	 * Sets the parentTask.
	 * @param parentTask The parentTask to set
	 */
	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}

	/**
	 * Sets the parenttaskid.
	 * @param parenttaskid The parenttaskid to set
	 */
	public void setParenttaskid(String parenttaskid) {
		this.parenttaskid = parenttaskid;
	}

	/**
	 * Sets the project.
	 * @param project The project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * Sets the projectid.
	 * @param projectid The projectid to set
	 */
	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}

	/**
	 * Sets the sendAlert.
	 * @param sendAlert The sendAlert to set
	 */
	public void setSendAlert(String sendAlert) {
		this.sendAlert = sendAlert;
	}

	/**
	 * Sets the sendTo.
	 * @param sendTo The sendTo to set
	 */
	public void setSendTo(String[] sendTo) {
		this.sendTo = sendTo;
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
	 * Sets the title.
	 * @param title The title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the percentComplete.
	 * @return String
	 */
	public String getPercentComplete() {
		return percentComplete;
	}

	/**
	 * Sets the percentComplete.
	 * @param percentComplete The percentComplete to set
	 */
	public void setPercentComplete(String percentComplete) {
		this.percentComplete = percentComplete;
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

	public void initialize()
	{
		setTitle("");
		setDescription("");
		setProject("");
		setProjectid("");
		setParentTask("");
		setParenttaskid("");
		setMilestone("No");
		setSendAlert("No");
		setAlertTypeAlert("");
		setAlertTypeEmail("");
		setSendTo(new String[]{	});
		setManager("") ;
		setManagerID("");
		setStartday("");
		setStartmonth("");
		setStartyear("");
		setEndday("");
		setEndmonth("");
		setEndyear("");
		setAssignedTo(new String[]{});
		setText1("");
		setText2("");
		setText3("");
	}

	/**
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1)
	{
		super.reset(arg0, arg1);
		initialize();
	}

	/**
	 * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1)
	{
		String mgrID = this.getManagerID();
		ActionErrors errors = super.validate(arg0, arg1);
		if(errors == null)
			errors = new ActionErrors();

		try
		{
			Validation validation = new Validation();

      if (getTitle() == null || getTitle().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Title"));
      }
      
      if (getProject() == null || getProject().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Project"));
      }
      
      if (getManager() == null || getManager().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Manager"));
      }


			if ((getEndyear() != null && getEndyear().length() != 0) && (getStartyear() == null || getStartyear().length() == 0)) {
        if (getStartyear() == null || getStartyear().trim().length() <= 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Start Date"));
        }
			}

			if ((getStartyear() != null && getStartyear().length() != 0) ||	(getStartmonth() != null && getStartmonth().length() != 0) || (getStartday() != null && getStartday().length() != 0)) {
        //validation.checkForDate("label.projects.task.startdate", getStartyear(), getStartmonth(), getStartday(), "error.application.date", "", errors);
      }
      
      if ((getEndyear() != null && getEndyear().length() != 0) ||	(getEndmonth() != null && getEndmonth().length() != 0) ||	(getEndday() != null && getEndday().length() != 0))	{
        //validation.checkForDate("label.projects.task.duedate", getEndyear(), getEndmonth(), getEndday(),"error.application.date", "",	errors);
      }
			if ((getStartyear() != null && getStartyear().length() != 0) & (getEndyear() != null && getEndyear().length() != 0))	{
				//validation.checkForDateComparison("label.projects.task.startdate", getStartyear(),getStartmonth(), getStartday(), "label.projects.task.duedate", getEndyear(), getEndmonth(), getEndday(),"error.application.datecomparison", "", errors);
      }
		} catch(Exception exception) {
			exception.printStackTrace();
		}

		HttpSession session = arg1.getSession(true);
		com.centraview.common.UserObject  userobjectd = (com.centraview.common.UserObject)session.getAttribute( "userobject" );

		arg1.setAttribute("taskid",getTaskid());
		arg1.setAttribute("setstatus",getSelectedStatus());
		TaskVO oldTaskVO = (TaskVO)session.getAttribute("taskForm"+getTaskid());

		if (oldTaskVO != null)
		{

			arg1.setAttribute("statusid",oldTaskVO.getStat());
			arg1.setAttribute("projecttaskcount",new Long(oldTaskVO.getProjectTaskCount()));

			IndividualVO ivo = oldTaskVO.getCreatedByVO();

			if (ivo != null)
			{
				setCreated(ivo.getFirstName()+" " + ivo.getLastName());
				String createdbyname = ivo.getFirstName()+" " + ivo.getLastName();
				arg1.setAttribute("createdbyname", createdbyname);

				DateFormat df = new SimpleDateFormat("dd/MM/yyyy - h:mm a") ;
				String createdon= df.format(oldTaskVO.getCreatedOn());
				arg1.setAttribute("createdon" , createdon);
				arg1.setAttribute("createdby" ,""+oldTaskVO.getCreatedBy());
			}




			ivo = oldTaskVO.getModifiedByVO();
			if (ivo != null)
			{
				setModified(ivo.getFirstName()+" " + ivo.getLastName());
				String modifiedbyname = ivo.getFirstName()+" " + ivo.getLastName();
				arg1.setAttribute("modifiedbyname", modifiedbyname);

				DateFormat df = new SimpleDateFormat("dd/MM/yyyy - h:mm a") ;
				String modified= "";
				if(oldTaskVO.getModifiedOn() != null)
				 modified= df.format(oldTaskVO.getModifiedOn());

				arg1.setAttribute("modifiedon" , modified);
				arg1.setAttribute("modifiedby" , ""+oldTaskVO.getModifiedBy());
			}

			String []AssignedTo = getAssignedTo();
			String []SendTo = getSendTo();

			String assignedtodummy = getAssignedTodummy();
			StringTokenizer tokenizer = new StringTokenizer(assignedtodummy,",");
			String token = "";
			String elm="";
			HashMap hmmm = new HashMap();
			while (tokenizer.hasMoreTokens())
			{
				token = (String)tokenizer.nextElement();
				elm = (String)tokenizer.nextElement();
				hmmm.put(new Integer(token),elm);
			}

			arg1.setAttribute("assignedTo",hmmm);
			String sendd = getSendtodummy();
			tokenizer = new StringTokenizer(sendd,",");

			HashMap hmmm1 = new HashMap();
			while (tokenizer.hasMoreTokens())
			{
				token = (String)tokenizer.nextElement();
				elm = (String)tokenizer.nextElement();
				hmmm1.put(new Long(token),elm);
			}


			arg1.setAttribute("sendTo",hmmm1);

			setManager(oldTaskVO.getIndividualName());
			setManagerID("" + oldTaskVO.getIndividualID());

			crumbs = oldTaskVO.getCrumbs();
			taskid = oldTaskVO.getTaskid();
			session.setAttribute("crumbs"+taskid,crumbs);
		}
		else
		{

			String milesto = getMilestone();
			arg1.setAttribute("milestone",milesto);
			String []AssignedTo = getAssignedTo();
			String []SendTo = getSendTo();

			String assignedtodummy = getAssignedTodummy();
			StringTokenizer tokenizer = null;
			String token = "";
			String elm="";

			if (assignedtodummy != null)
			{
				tokenizer = new StringTokenizer(assignedtodummy,",");
				HashMap hmmm = new HashMap();
				while (tokenizer.hasMoreTokens())
				{
					token = (String)tokenizer.nextElement();
					elm = (String)tokenizer.nextElement();
					hmmm.put(new Integer(token),elm);
				}
				arg1.setAttribute("assignedTo",hmmm);
			}

			String sendd = getSendtodummy();
			if (sendd != null)
			{
				tokenizer = new StringTokenizer(sendd,",");
				HashMap hmmm1 = new HashMap();
				while (tokenizer.hasMoreTokens())
				{
					token = (String)tokenizer.nextElement();
					elm = (String)tokenizer.nextElement();
					hmmm1.put(new Long(token),elm);
				}
				arg1.setAttribute("sendTo",hmmm1);
			}
		}

		this.setManagerID(mgrID);
		return errors;

	}

	public Collection getAssignedtoCol()
	{
		return this.assignedtoCol;
	}

	public void setAssignedtoCol(Collection assignedtoCol)
	{
		this.assignedtoCol = assignedtoCol;
	}



	public String getCreated()
	{
	return this.created;
	}

	public void setCreated(String created)
	{
	this.created = created;
	}


	public String getModified()
	{
	return this.modified;
	}

	public void setModified(String modified)
	{
	this.modified = modified;
	}


	public String getTaskid()
	{
		return this.taskid;
	}

	public void setTaskid(String taskid)
	{
		this.taskid = taskid;
	}


	public Collection getSendtoCol()
	{
	return this.sendtoCol;
	}

	public void setSendtoCol(Collection sendtoCol)
	{
	this.sendtoCol = sendtoCol;
	}


	public Collection getStatusCol()
	{
	return this.statusCol;
	}

	public void setStatusCol(Collection statusCol)
	{
	this.statusCol = statusCol;
	}


	public String getStatus()
	{
	return this.status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}


	public String getSelectedStatus()
	{
	return this.selectedStatus;
	}

	public void setSelectedStatus(String selectedStatus)
	{
	this.selectedStatus = selectedStatus;
	}


	public LinkedHashMap getCrumbs()
	{
	return this.crumbs;
	}

	public void setCrumbs(LinkedHashMap crumbs)
	{
	this.crumbs = crumbs;
	}

	public void setForm(TaskVO taskVO)
	{
	}

	public String getModifiedbyid()
	{
	return this.modifiedbyid;
	}

	public void setModifiedbyid(String modifiedbyid)
	{
	this.modifiedbyid = modifiedbyid;
	}
	
	public String getCreatedbyid()
	{
	return this.createdbyid;
	}

	public void setCreatedbyid(String createdbyid)
	{
	this.createdbyid = createdbyid;
	}
	

	public String getAssignedTodummy()
	{
	return this.assignedTodummy;
	}

	public void setAssignedTodummy(String assignedTodummy)
	{
	this.assignedTodummy = assignedTodummy;
	}


	public String getSendtodummy()
	{
	return this.sendtodummy;
	}

	public void setSendtodummy(String sendtodummy)
	{
	this.sendtodummy = sendtodummy;
	}

	public String getCreatedOn()
	{
		return this.createdOn;
	}
	
	public void setCreatedOn(String createdOn)
	{
		this.createdOn = createdOn;
	}	

	public String getModifiedOn()
	{
		return this.modifiedOn;
	}

	public void setModifiedOn(String modifiedOn)
	{
		this.modifiedOn = modifiedOn;
	}
}













