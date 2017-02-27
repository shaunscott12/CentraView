/*
 * $RCSfile: TicketLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:14 $ - $Author: mking_cv $
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
 * TicketLocal.java
 *
 * @author   Iqbal Khan
 * @version  1.0
 *
 */
package com.centraview.support.ticket;




import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;
/**
* This is  remote interface which is called from client
*/

public interface TicketLocal extends EJBLocalObject
{
	public int addTicket(int userId, TicketVO tvo) throws TicketException, AuthorizationFailedException;
	public TicketVO getTicketBasic(int userId, int ticketId) throws  TicketException;
	public TicketVO getTicketBasicRelations(int userId, int ticketId) throws TicketException, AuthorizationFailedException;
	//public TicketVO getTicketFull(int userId, int ticketId) throws TicketException;
	public void updateTicket(int userId, TicketVO tvo) throws TicketException, AuthorizationFailedException;
	public void deleteTicket(int userId, int ticketId) throws  AuthorizationFailedException;
	public int duplicateTicket( int userId, TicketVO tv) throws TicketException;
	public int addThread(int userId, ThreadVO thvo) throws TicketException;
	public void updateThread(int userId, ThreadVO thvo) throws TicketException;
	//public void deleteThread(int userId, int threadId) throws TicketException;
	public ThreadVO getThread(int userId, int ThreadId) throws TicketException;
	public Vector getAllThreadForTicket(int userId, int ticketId) throws TicketException;

	public void closeTicket(int userId, int ticketId) throws  TicketException;
	public void reopenTicket(int userId, int ticketId) throws TicketException;

	public void addTicketLink(int ticketId,int[] linkId,int linkType);
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);

  /**
   * This method returns Ticket Name Of the Ticket
   *
   * @param TicketID The TicketID to collect the Ticket Title
   *
   * @return TicketName The TicketName
   */
  public String getTicketName(int TicketID);
}












