/*
 * $RCSfile: Ticket.java,v $    $Revision: 1.2 $  $Date: 2005/08/04 15:22:48 $ - $Author: mcallist $
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
 * Ticket.java
 *
 * @author   Iqbal Khan
 * @version  1.0
 *
 */
package com.centraview.support.ticket;

import java.rmi.RemoteException;
import java.util.Vector;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;

/**
 * This is remote interface which is called from client
 */

public interface Ticket extends EJBObject {
  public int addTicket(int userId, TicketVO tvo) throws RemoteException, TicketException, AuthorizationFailedException;
  public TicketVO getTicketBasic(int userId, int ticketId) throws RemoteException, TicketException;
  public TicketVO getTicketBasicRelations(int userId, int ticketId) throws RemoteException, TicketException, AuthorizationFailedException;
  public void updateTicket(int userId, TicketVO tvo) throws RemoteException, TicketException, AuthorizationFailedException;
  public void deleteTicket(int userId, int ticketId) throws RemoteException, AuthorizationFailedException;
  public int duplicateTicket(int userId, TicketVO tv) throws RemoteException, TicketException;
  public int addThread(int userId, ThreadVO thvo) throws RemoteException, TicketException;
  public void updateThread(int userId, ThreadVO thvo) throws RemoteException, TicketException;
  public ThreadVO getThread(int userId, int ThreadId) throws RemoteException, TicketException;
  public Vector getAllThreadForTicket(int userId, int ticketId) throws RemoteException, TicketException;
  public void closeTicket(int userId, int ticketId) throws RemoteException, TicketException;
  public void reopenTicket(int userId, int ticketId) throws RemoteException, TicketException;
  public void addTicketLink(int ticketId, int[] linkId, int linkType) throws RemoteException;
  /**
   * @author Kevin McAllister <kevin@centraview.com> Allows the client to set
   *         the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;
  /**
   * This method returns Ticket Name Of the Ticket
   * @param TicketID The TicketID to collect the Ticket Title
   * @return TicketName The TicketName
   */
  public String getTicketName(int TicketID) throws RemoteException;
}
