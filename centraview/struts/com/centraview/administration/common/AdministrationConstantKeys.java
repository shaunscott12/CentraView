/*
 * $RCSfile: AdministrationConstantKeys.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:37 $ - $Author: mking_cv $
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

package com.centraview.administration.common;


public class AdministrationConstantKeys
{
	public static final String TYPEOFMODULE = "TYPEOFMODULE";
	public static final String TYPEOFSUBMODULE = "TYPEOFSUBMODULE";
	public static final String TYPEOFOPERATION = "TYPEOFOPERATION";
	public static final String ADD = "ADD";
	public static final String EDIT = "EDIT";
	public static final String LIST = "LIST";

	public static final String USERADMINISTRATION = "useradministration";
	public static final String CONFIGURATION = "configuration";
	public static final String DATAADMINISTRATION = "dataadministration";
	public static final String MODULESETTINGS = "modulesettings";
	public static final String WORKFLOW = "workflow";
	public static final String VIEWS = "views";
	public static final String CUSTOMFIELD = "customfields";
	public static final String SAVEDSEARCH = "savedsearches";
	public static final String ADMINISTRATION = "CONTACTS";
	public static final String CUSTOMVIEW = "customviews";

	public static final String USERADMINISTRATION_USER = "users";
	public static final String USERADMINISTRATION_SECURITY = "securityprofiles";

	public static final String GARBAGE="GARBAGE";
	public static final String ATTIC="ATTIC";
	public static final String MERGEPURGE="MERGEPURGE";
	public static final String EXPORT="EXPORT";
	public static final String LOGS="LOGS";
	public static final String HISTORY="HISTORY";
	public static final String GLOBALREPLACE="GLOBALREPLACE";
	public static final String CUSTOMERLOGO="CUSTOMERLOGO";
	public static final String LICENSE="LICENSE";
	public static final String AUTHSETTINGS="AUTHSETTINGS";
	public static final String EMAILSETTINGS="EMAILSETTINGS";


	//This values should be same as that of the values in
	//in History type table IN DataBase ..
	public static final String COMPLETE_ACTIVITY = "CompleteActivity";
	public static final String DELETED = "Deleted";
	public static final String RESTORED = "Restored";

	// The value of following variables should be same as that of
	// respective value in Module table
	public static final String INDIVIDUAL = "Individual";
	public static final String ENTITY = "Entity";
	public static final String PROJECTS = "Projects";

	public static final String SERVERSETTINGS="ServerSettings";
	public static final String LITERATURE="literature";

  // These values define the primary key value for the
  // records in the "systememailsettings" table in the
  // database. They should be used when trying to retrieve
  // those particular records from that table.
  public static final int SYSTEM_EMAIL_CUST_PROFILE_REQUEST = 1;
  public static final int SYSTEM_EMAIL_FORGOT_PASSWORD_REQUEST = 2;

	//Email Template Constant Key Values
	public static final int EMAIL_TEMPLATE_SUPPORTTICKET = 1;
	public static final int EMAIL_TEMPLATE_SUPPORTTHREAD = 2;
	public static final int EMAIL_TEMPLATE_SUPPORTERROR = 3;
	public static final int EMAIL_TEMPLATE_ACTIVITES = 4;
	public static final int EMAIL_TEMPLATE_TASK = 5;
	public static final int EMAIL_TEMPLATE_FORGOT_PASSWORD = 6;
	public static final int EMAIL_TEMPLATE_SUGGESTIONBOX = 7;
	public static final int EMAIL_TEMPLATE_CUST_PROFILE = 8;
	public static final int EMAIL_TEMPLATE_USER_PROFILE = 9;

}
