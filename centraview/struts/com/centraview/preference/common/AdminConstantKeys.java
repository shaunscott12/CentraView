/*
 * $RCSfile: AdminConstantKeys.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:24 $ - $Author: mking_cv $
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

package com.centraview.preference.common;

public class AdminConstantKeys 
{
  // Operation can be of add, edit, delete
  public static final String TYPEOFOPERATION = "TYPEOFOPERATION";
	public static final String ADD = "ADD";	
	public static final String EDIT = "EDIT";	
	public static final String DUPLICATE = "DUPLICATE";		
  
  // Indicate id attached to particular open window
  // Window id is used in case multiple windows are opened
	public static final String WINDOWID = "WINDOWID";
  
  // indicate which jsp page to be loaded 
  // with respect to navigation.
	public static final String PREFERENCEPAGE = "USERPROFILE";
  
	public static final String EMAIL = "Email";		
	public static final String TODAYSCALENDAR = "Today's Calendar ";		
	public static final String UNSCHEDULEDACTIVITIES="Unscheduled Activities";
	public static final String SCHEDULEDOPPORTUNUITIES="Scheduled Opportunities";		
	public static final String PROJECTTASKS = "Project Tasks";		
	public static final String SUPPORTTICKETS = "Support Tickets";		
	public static final String COMPANYNEWS = "Company News";		
  public static final String DEFAULTPREFERENCES = "DefaultPreferences";
	
}

