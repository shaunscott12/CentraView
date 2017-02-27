/*
 * $RCSfile$    $Revision$  $Date$ - $Author$
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

package com.centraview.common;

/**
 * This class defines the common String and number constants used in the
 * Web Application tier.
 * 
 * Pursuing the white through the denial of magic words and numbers!
 */
public final class Constants {
  // Struts Action class Constants
  /** the default struts action forward name */
  public static final String FORWARD_DEFAULT = "default";
  public static final String FORWARD_FAILURE = "failure";
  /** Generic error message when the struts action has no recourse */
  public static final String ACTION_INVALID = "No valid forward configured for this action";
  
  public static final String ENTITYVO = "entityVO";
  public static final String ENTITY = "entity";
  public static final String TYPEOFCONTACT = "typeOfContact";
  public static final String TYPEOFOPERATION = "typeofoperation";
  public static final String ISPRIMARYYES = "yes";
  public static final String INDIVIDUAL = "individual";
  public static final String GROUP = "group";
  public static final String PARAMID = "rowId";

  // EH = Email Header
  public static final String EH_PREFIX = "X-CV-";
  public static final int EH_PREFIX_LENGTH = 5;
  public static final String EH_HEADER_DELIMETER = "::";
  public static final String EH_KEYVALUE_DELEMETER = "=";

  // for any service to happen through enail this header is required
  public static final String EH_SERVICE_KEY = "X-CV-SERVICE";
  public static final String EH_SERVICE_VALUE_ACTIVITY_INVITATION = "ActivityInvitation";
  public static final String EH_SERVICE_VALUE_CREATEINDIVIDUAL = "CreateIndividual";
  public static final String EH_SERVICE_VALUE_INDIVIDUALIMPORTED = "IndividualImported";

  // Activity Email invidation confirmation
  // by default none
  public static final String EH_ATTENDEE_STATUS_KEY = "X-CV-CurrentAttendeeStatus";
  public static final String EH_ATTENDEE_STATUS_VALUE_NONE = "None";
  public static final String EH_ATTENDEE_STATUS_VALUE_ACCEPT = "ACCEPTED";
  public static final String EH_ATTENDEE_STATUS_VALUE_DECLINE = "DECLINED";
  public static final String EH_ATTENDEE_STATUS_VALUE_TENTATIVELYACCEPT = "TENTATIVELY ACCEPTED";

  public static final String EH_ACTIVITYID_KEY = "ActivityId";

  public static final String FIELD_SEPARATOR = " , ";
  public static final String PRIMARY_FIELD_SEPARATOR = "::";

  public static final String WHERE_CLAUSE_SEPARATOR = " and ";
  // end email invitation
  // END email header constants

  public static final String CV_ATTIC = "CV_ATTIC";
  public static final String CV_GARBAGE = "CV_GARBAGE";

  // Loggin related constants
  // *** Never use this key other than to storing the uesr counter in the
  // ServletContex.
  public static final String LOGGED_ON_USER_COUNT = "loggedOnUserCount";
  // License Key status
  public static final String LICENSE_VALID = "valid";
  public static final String LICENSE_INVALID = "invalid";
  public static final String LICENSE_TRIAL = "trial";
  public static final String LICENSE_REMAINING_DAYS = "remaininglicensedays";

  // License related error messages
  public static final String LICENSE_INVALID_ERROR = "error.license.invalid";
  public static final String LICENSE_EXPIRED_ERROR = "error.license.expired";
  public static final String LICENSE_EXPIRED_WARNING = "LicenseExpireWarning";
  public static final String EXCEEDED_NO_OF_USER_ERROR = "error.license.noofusersexceed";

  public static final String TYPEOFMODULE = "";

  public static final String ACTIVITYMODULE = "Activities";
  public static final String EMAILMODULE = "Email";
  /**
   * The EMAILTYPEFLAG object determines format of the email. It can be either
   * plain text or html format. This flag is set in administration - preferences
   * module and is reflected accordingly in E-Mail module.
   */
  public static final String EMAILTYPEFLAG = "true";

  public static final String VIEW = "VIEW";
  public static final String SENDEMAIL = "SEND";
  public static final String SCHEDULEACTIVITY = "SCHEDULE";
  public static final String VIEWSCHEDULEACTIVITY = "VIEWSCHEDULE";
  public static final String VIEWSENDEMAIL = "VIEWSEND";

  // This values should be same as that of in CV Database(Module Table)
  public static final int EntityModuleID = 14;
  public static final int IndividualModuleID = 15;
  public static final int ProjectModuleID = 36;

  // reason of doing this was if we change the name of the method of contact's
  // type
  // Still we will not be able to scale the code perfectly.
  // So thats why I thought Its a standard value we can define the unique name
  // So that If any change to database.
  public static final int MOC_EMAIL = 1;
  public static final int MOC_FAX = 2;
  public static final int MOC_MOBILE = 3;
  public static final int MOC_MAIN = 4;
  public static final int MOC_HOME = 5;
  public static final int MOC_OTHER = 6;
  public static final int MOC_PAGER = 7;
  public static final int MOC_WORK = 8;

  // This values should be same as that of in CV Database(HistoryType Table)
  public static final int DELETED = 1;
  public static final int CREATED = 2;
  public static final int RESTORED = 3;
  public static final int UPDATED = 4;
  public static final int COMPLETE_ACTIVITY = 5;

  /** the librarary Directory CV_ROOT/CVFS_SYSTEM/lib */
  public final static String CENTRAVIEW_LIBRARY = System.getProperty("file.separator", "/") + "CV_ROOT" + System.getProperty("file.separator", "/")
      + "CVFS_SYSTEM" + System.getProperty("file.separator", "/") + "lib";
  public final static String CUSTOMERLOGO_PATH = System.getProperty("file.separator", "/") + "CV_ROOT" + System.getProperty("file.separator", "/")
      + "CVFS_SYSTEM" + System.getProperty("file.separator", "/") + "logo" + System.getProperty("file.separator", "/");
}