/*
 * $RCSfile: ProjectVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:46 $ - $Author: mking_cv $
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


import org.apache.struts.action.ActionForm;

import com.centraview.common.CVUtility;
import com.centraview.projects.project.ProjectForm;


/*
 * @author  Vaijayanti Vaidya
 * @version 1.0
 */

public class ProjectVOX extends ProjectVO
{

	/**
	 *	Constructor handling ActionForm
	 *
	 * @param   form  ActionForm
	 */
	public ProjectVOX(String TimeZone, ActionForm form)
	{
		ProjectForm projectForm = (ProjectForm)form;		
			
		//get all form values in local variable
		//to be removed after debug and change for long
		String title 				= projectForm.getTitle();
		String description			= projectForm.getDescription();
		String entity 				= projectForm.getEntity();		
		String contact 				= projectForm.getContact();
		String manager 				= projectForm.getManager();
		String team 					= projectForm.getTeam();
		/*
		String customField1 		= (String)dynaForm.get("CustomField1");
		String customField2 		= (String)dynaForm.get("CustomField2");
		String customField3 		= (String)dynaForm.get("CustomField3");
		*/
		//int Status 					= projectForm.getStatus();
		int budhr 					= Integer.parseInt(projectForm.getBudhr());
		if (projectForm.getStartday() != null && !projectForm.getStartday().equals(""))
		{
			int startday 				= Integer.parseInt(projectForm.getStartday());
			int startmonth 				= Integer.parseInt(projectForm.getStartmonth()) - 1;
			int startyear 				= Integer.parseInt(projectForm.getStartyear());
			setStart
			(
				CVUtility.convertTimeZone
				(
					new java.sql.Date(startyear,startmonth,startday),
					java.util.TimeZone.getTimeZone(TimeZone),
					java.util.TimeZone.getTimeZone("EST")					
				)				
			);
		}
		if (projectForm.getEndday() != null && !projectForm.getEndday().equals(""))
		{
			int endday 					= Integer.parseInt(projectForm.getEndday());
			int endmonth 				= Integer.parseInt(projectForm.getEndmonth()) - 1;
			int endyear 				= Integer.parseInt(projectForm.getEndyear());	
			setEnd
			(
				CVUtility.convertTimeZone
				(
					new java.sql.Date(endyear,endmonth,endday),
					java.util.TimeZone.getTimeZone(TimeZone),
					java.util.TimeZone.getTimeZone("EST")	
				)
			);
		}
		int projectid 				= projectForm.getProjectid();	
				
		
		setTitle(title);
		setDescription(description);
		
		/*if(status.equals(""))
			status="-1";
			*/
	//	setStatusID(Status);
		setProjectID(projectid);
		
		/*
		if(startyear != 0)	
			setStart(new java.sql.Date(startyear,startmonth,startday));
		if(endyear != 0)		
			setEnd(new java.sql.Date(endyear,endmonth,endday));
		*/
		if(!entity.equals(""))	
		{
			setEntityID(projectForm.getEntityid());		
		}
		if(budhr != 0)
			setBudgetedHours(budhr);				

		if(!manager.equals(""))
		{
			setManagerID(projectForm.getManagerID());
		}
		if(!projectForm.getStatus().equals(""))	
			setStatusID(Integer.parseInt(projectForm.getStatus()));
		if(projectForm.getTeamID()!=0)	
			setGroupID(projectForm.getTeamID());
		if(projectForm.getContactID()!=0)	
			setContactID(projectForm.getContactID());
		
		
		   
				
		
	}	
	
	/**
	 *
	 *
	 * @return  The CustomField value Object.   
	 */
	public ProjectVO getVO()
	{
		return super.getVO();
	}
}
