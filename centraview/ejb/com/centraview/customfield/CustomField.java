/*
 * $RCSfile: CustomField.java,v $    $Revision: 1.3 $  $Date: 2005/05/24 20:19:40 $ - $Author: mking_cv $
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


package com.centraview.customfield;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import javax.ejb.EJBObject;

import com.centraview.common.CustomFieldList;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface CustomField extends EJBObject
{
  public TreeMap getCustomFieldData(String recordType) throws java.rmi.RemoteException;
  public Collection getCustomFieldImportData(String recordType) throws java.rmi.RemoteException;
  public TreeMap getCustomFieldData(String recordType, int recordID) throws java.rmi.RemoteException;
  public CustomFieldVO getCustomField(int customfieldID, int recordID) throws java.rmi.RemoteException;
  public void addCustomField(CustomFieldVO cfdata) throws java.rmi.RemoteException;
  public void updateCustomField(CustomFieldVO cfdata) throws java.rmi.RemoteException;
  public CustomFieldList getCustomFieldList(String recordType, int recordID) throws java.rmi.RemoteException;
  public CustomFieldList getCustomFieldList(int userID, HashMap hm) throws java.rmi.RemoteException;
  public void addNewCustomField(CustomFieldVO cfData) throws java.rmi.RemoteException;
  public void deleteCustomField(int userID, int customFieldID) throws java.rmi.RemoteException;
  public CustomFieldVO getCustomField(int customFieldID) throws java.rmi.RemoteException;
  public void updateNewCustomField(CustomFieldVO cfData) throws java.rmi.RemoteException;
  public HashMap getCustomFieldValue() throws java.rmi.RemoteException;
  public void addCustomFieldValue(int customFieldID, String fieldValue) throws java.rmi.RemoteException;
  public ValueListVO getCustomFieldsValueList(int individualId, ValueListParameters parameters) throws java.rmi.RemoteException;
  public HashMap getFieldModuleInfo(int individualId, int customFieldId) throws java.rmi.RemoteException;

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;
}
