/*
 * $RCSfile: Note.java,v $    $Revision: 1.2 $  $Date: 2005/05/23 18:07:24 $ - $Author: mking_cv $
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


package com.centraview.note;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface Note extends EJBObject
{
  
  public int addNote(int userId, NoteVO nvo) throws NoteException, RemoteException,AuthorizationFailedException;
  public NoteVO getNote(int userId, int noteId) throws NoteException, RemoteException,AuthorizationFailedException;
  public void updateNote(int userId, NoteVO nvo) throws NoteException, RemoteException,AuthorizationFailedException;
  public void deleteNote(int userId, int noteId) throws NoteException, RemoteException, AuthorizationFailedException;
  public NoteList getNoteList(int individualID, HashMap hm) throws RemoteException,AuthorizationFailedException;
  public NoteList getEntityNoteList(int entityId, HashMap hashmap) throws RemoteException;
  public ValueListVO getNoteValueList(int individualID, ValueListParameters parameters) throws RemoteException;

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * Allows the client to set the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;

}

