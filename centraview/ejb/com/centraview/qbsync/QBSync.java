/*
 * $RCSfile: QBSync.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:53 $ - $Author: mking_cv $
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

package com.centraview.qbsync;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import javax.ejb.EJBObject;
/**
* This is  remote interface which is called from Sync client
*/

public interface QBSync extends EJBObject
{
  public void initializeLog4j(String path) throws RemoteException;
  public ArrayList beginSynchronisation(int indId, Date lastSyncDate, String moduleName,ArrayList qbVOs, String flag) throws RemoteException;
  public boolean updateExternalIDs(HashMap hm, String modulename) throws RemoteException;
  public Collection getList(String moduleName, Timestamp lastSyncDate, String flag, String syncDatePrKeyField[]) throws RemoteException;
  public HashMap chooseNewAndModify(ArrayList arlQB, String modulename, Date lastSDate) throws RemoteException;
  public ArrayList getObjects(String objName,HashMap params, int indId) throws RemoteException;
  public void updateObjects(String objName, ArrayList VOs,int indId) throws RemoteException;
  public void deleteObjects(String objName,ArrayList VOs,int indId) throws RemoteException;
  public int getCVidForExtid(String objName, String ExtId ) throws RemoteException;
  public String getExtidforCVid(String objName,int CVid) throws RemoteException;
  public void setDataSource(String ds) throws RemoteException;
}
