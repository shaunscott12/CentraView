/*
 * $RCSfile: TimeSlip.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:52 $ - $Author: mking_cv $
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


package com.centraview.projects.timeslip;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.projects.helper.TimeSlipVO;

public interface TimeSlip extends EJBObject
{
  public int addTimeSlip(int userId, TimeSlipVO tsvo) throws RemoteException;
  public TimeSlipVO getTimeSlip(int timeSlipId,int userId) throws RemoteException;
  public void updateTimeSlip(int userId, TimeSlipVO tsvo) throws RemoteException;
  public int deleteTimeSlip(int timeSlipId) throws RemoteException,AuthorizationFailedException;
  public void duplicateTimeSlip(int userId, int timeSlipId) throws RemoteException;	
  
  /**
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 * @author Kevin McAllister <kevin@centraview.com>
	 */
	public void setDataSource(String ds) throws RemoteException;	
}
