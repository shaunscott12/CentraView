/*
 * $RCSfile: CustomFieldData.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:56 $ - $Author: mking_cv $
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

import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.centraview.customfield.CustomField;
import com.centraview.customfield.CustomFieldHome;

/**
 *
 * @author CentraView, LLC.
 */
public class CustomFieldData
{
  private static Logger logger = Logger.getLogger(CustomFieldData.class);
  /**
   *  This method returns CustomField collection for RecordType
   *  This is generally used for Add time.
   *
   * @param   recordType  Record Type
   * @return
   */
  public static TreeMap getCustomField(String recordType, String dataSource)
  {
    TreeMap hmap = null;
    try
    {
      CustomFieldHome cfh = (CustomFieldHome) 
        CVUtility.getHomeObject("com.centraview.customfield.CustomFieldHome", "CustomField");
      CustomField remote = (CustomField) cfh.create();
      remote.setDataSource(dataSource);
      hmap = remote.getCustomFieldData(recordType);
    }
    catch (Exception e)
    {
      logger.error("[getCustomField] Exception thrown.", e);
    }
    return hmap;
  } // end of getCustomField(String recordType)

  /**
   *  This method returns CustomField collection for RecordType
   *  This is generally used for Add time.
   *
   * @param   recordType  Record Type
   * @return
   */
  public static TreeMap getCustomField(String recordType, int recordID, String dataSource)
  {
    TreeMap hmap = null;
    try
    {
      CustomFieldHome cfh = (CustomFieldHome) 
        CVUtility.getHomeObject("com.centraview.customfield.CustomFieldHome", "CustomField");
      CustomField remote = (CustomField) cfh.create();
      remote.setDataSource(dataSource);
      hmap = remote.getCustomFieldData(recordType, recordID);
    }
    catch (Exception e)
    {
      logger.error("[getCustomField] Exception thrown.", e);
    }
    return hmap;
  } // end of getCustomField(String recordType,int recordID)
}
