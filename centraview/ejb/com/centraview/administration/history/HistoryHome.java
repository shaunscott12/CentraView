/*
 * $RCSfile: HistoryHome.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:45 $ - $Author: mking_cv $
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

/*
 * HistoryHome.java
 *
 * @version  1.0    
 * 
 */

package com.centraview.administration.history;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;

/**
 *  Home Interface  for History EJB
 *  
 * @author praveen
 */

public interface  HistoryHome extends EJBHome
{
	/**
	 * Called by the client to create an object of File. It requires a matching pair in
     * the bean class, i.e. ejbCreate().
	 * @return The object reference of History
	 * @throws RemoteException
	 * @throws CreateException
	 */
	public History create() throws RemoteException,CreateException;
	
	
	
	
}
