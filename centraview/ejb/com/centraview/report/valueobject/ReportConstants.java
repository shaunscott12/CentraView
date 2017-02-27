/*
 * $RCSfile: ReportConstants.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:03 $ - $Author: mking_cv $
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

package com.centraview.report.valueobject;

/**
 * <p>Title:  ReportConstants </p>
 *
 * <p>Description: This file is used for defining constants
 *  for report module. </p>
 */

public class ReportConstants
{
  /**************************************************************************
   *
   *	Constants - Report types values.
   *
   **************************************************************************/
  /** Constant - Standard report type code. */
  public static final int STANDARD_REPORT_CODE = 1;
  /** Constant - Ad Hoc report type code. */
  public static final int ADHOC_REPORT_CODE = 2;

  /**************************************************************************
   *
   *	Constants - List types values.
   *
   **************************************************************************/
  /** Constant - Standard report list type . */
  public static final String STANDARD_REPORT_LIST = "StandardReport";
  /** Constant - Ad Hoc report type list code. */
  public static final String ADHOC_REPORT_LIST = "AdHocReport";

  /**************************************************************************
   *
   *	Constants - Sort order types values.
   *
   **************************************************************************/
  /** Constant - Sort Order ASC. */
  public static final char SORTORDER_TYPE_A = 'A';
  /** Constant - Sort Order DESC. */
  public static final char SORTORDER_TYPE_D = 'D';
  /** Constant - Sort Order ASC. */
  public static final String SORTORDER_ASC = "ASC";
  /** Constant - Sort Order DESC. */
  public static final String SORTORDER_DESC = "DESC";

  /**************************************************************************
   *
   *	Constants - Module ID values.
   *
   **************************************************************************/
  /** Constant - Entity module id. */
  public static final int ENTITY_MODULE_ID = 14;
  /** Constant - Individual module id. */
  public static final int INDIVIDUAL_MODULE_ID = 15;
  /** Constant - Activities module id. */
  public static final int ACTIVITIES_MODULE_ID = 3;
  /** Constant - Opportunity module id. */
  public static final int OPPORTUNITY_MODULE_ID = 30;
  /** Constant - Proposal module id. */
  public static final int PROPOSAL_MODULE_ID = 31;
  /** Constant - Marketing module id. */
  public static final int PROMOTION_MODULE_ID = 33;
  /** Constant - Project module id. */
  public static final int PROJECT_MODULE_ID = 36;
  /** Constant - Task module id. */
  public static final int TASK_MODULE_ID = 37;
  /** Constant - TimeSlip module id. */
  public static final int TIMESLIP_MODULE_ID = 38;
  /** Constant - Support module id. */
  public static final int TICKET_MODULE_ID = 39;
  /** Constant - Accounting module id. */
  public static final int ACCOUNTING_ORDER_ID = 42;
  public static final int ACCOUNTING_INVENTORY_ID = 48;
  /** Constant - HR module id. */
  public static final int TIMESHEET_MODULE_ID = 52;

  /**************************************************************************
   *
   *	Constants - wether tables and fields appearance.
   *
   **************************************************************************/
  public static final String APPEARANCE_TOP = "TOP";
  public static final String APPEARANCE_BOTTOM = "BOTTOM";
  public static final String APPEARANCE_BOTH = "BOTH";
}