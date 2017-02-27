/*
 * $RCSfile: Delegator.java,v $    $Revision: 1.3 $  $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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

import java.util.Vector;

import org.apache.log4j.Logger;

import com.centraview.preference.Preference;
import com.centraview.preference.PreferenceHome;

/**
 * @author CentraView, LLC.
 */
public class Delegator {
  private static Logger logger = Logger.getLogger(Delegator.class);

  public static Vector getDelegatorIds(int userID, String typeofModule, String typeofoperation, String dataSource) {
    String moduleName = "";
    Vector indIdVec = new Vector();

    try {
      PreferenceHome hm = (PreferenceHome) CVUtility.getHomeObject("com.centraview.preference.PreferenceHome",
          "Preference");
      Preference remote = hm.create();
      remote.setDataSource(dataSource);

      if (typeofModule.equals(Constants.ACTIVITYMODULE)) {
        moduleName = Constants.ACTIVITYMODULE;
      } else if (typeofModule.equals(Constants.ACTIVITYMODULE)) {
        moduleName = Constants.EMAILMODULE;
      }

      indIdVec = remote.getDelegatorIDs(userID, moduleName, typeofoperation);
      indIdVec.addElement((new Integer(userID)).toString());
    } catch (Exception e) {
      logger.error("[getDelegatorIds] Exception thrown.", e);
    }
    return indIdVec;
  }

  public static Vector getCalendarDelegatorIds(int userID, String typeofModule, String typeofoperation,
      String dataSource) {
    String moduleName = "";
    Vector indIdVec = new Vector();

    try {
      PreferenceHome hm = (PreferenceHome) CVUtility.getHomeObject("com.centraview.preference.PreferenceHome",
          "Preference");
      Preference remote = hm.create();
      remote.setDataSource(dataSource);

      // FIXME Delegation of Email through getCalendarDelegatorIds??
      if (typeofModule.equals(Constants.ACTIVITYMODULE)) {
        moduleName = Constants.ACTIVITYMODULE;
      } else if (typeofModule.equals(Constants.ACTIVITYMODULE)) {
        moduleName = Constants.EMAILMODULE;
      }

      indIdVec = remote.getCalendarDelegatorIds(userID, moduleName, typeofoperation);
      if (moduleName.equals(Constants.EMAILMODULE)) {
        indIdVec.addElement((new Integer(userID)).toString());
      }
    } catch (Exception e) {
      logger.error("[getCalendarDelegatorIds] Exception thrown.", e);
    }
    return indIdVec;
  }
}
