/*
 * $RCSfile: CvFile.java,v $    $Revision: 1.2 $  $Date: 2005/07/12 18:35:50 $ - $Author: mcallist $
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

package com.centraview.file;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBObject;

import com.centraview.common.AuthorizationFailedException;

/**
 * This is remote interface which is called from client
 */

public interface CvFile extends EJBObject {
  public FileList getAllFiles(int userID, HashMap hm) throws RemoteException;
  public int addFolder(int userId, CvFolderVO fdvo) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public CvFolderVO getHomeFolder(int userID) throws RemoteException, CvFileException, AuthorizationFailedException;
  public CvFolderVO getFolder(int userId, int folderId) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public CvFolderVO getFolderByName(int userId, int parentFolderId, String folderName) throws RemoteException,
      CvFileException;
  public void updateFolder(int userId, CvFolderVO fdvo) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public void deleteFolder(int userId, int folderId) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public void commitEmailAttachment(int fileId, String newName) throws RemoteException, CvFileException;
  public FileListElement getParentFolder(int individualId, int folderId, int parentId) throws RemoteException,
      CvFileException;
  public int addFile(int userId, int folderId, CvFileVO fdvo) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public CvFileVO getFile(int userId, int folderId) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public CvFileVO getFileBasic(int userId, int fileId) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public void updateFile(int userId, int[] folderId, CvFileVO fdvo) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public void deleteFile(int userId, int fileId, int currfolderID) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public Vector getFolderRootPath(int userID, int folderID) throws RemoteException, CvFileException;
  public ArrayList getUserFolders(int userID) throws RemoteException, CvFileException;
  public void duplicateFolder(int userID, CvFolderVO fdvo, int curfolderID, int curParentID) throws RemoteException,
      CvFileException, AuthorizationFailedException;
  public void duplicateFile(int userID, int fileID, int folderID) throws RemoteException, CvFileException,
      AuthorizationFailedException;
  public ArrayList getAllFolders(int userId, boolean systemIncludeFlag) throws RemoteException, CvFileException;
  public FileList getAllTicketFiles(int userID, int ticketID) throws RemoteException;
  public FileList getEntityFiles(int entityId, int userID, HashMap hm) throws RemoteException;
  /**
   * @author Kevin McAllister <kevin@centraview.com> Allows the client to set
   *         the private dataSource
   * @param ds
   *          The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds) throws RemoteException;
  public ArrayList getCompanyNews() throws RemoteException;
  public FileList getAllCustomerFiles(int userID, HashMap hm) throws RemoteException;
  /**
   * Gets File Input Stream. we will pass the fileID and get folderPath and
   * fileName We will process the image to a byte array and return it.
   * 
   * @param individualId
   *          Identification for the individual who is logged in.
   * @param fileId
   *          Identification of the file.
   * @param dataSource
   *          A string that contains the cannonical JNDI name of the datasource
   * @return HashMap It will return two things array of byte & fileName.
   */
  public HashMap getFileInputStream(int individualId, int fileId, String dataSource) throws RemoteException;
  /**
   * calls the CvFile EJB to get the public folder id.
   * @return Integer folder id or null if something goes wrong.
   */
  public Integer getPublicFolderId() throws RemoteException;
  /**
   * This method returns All The public folders
   * @return ArrayList list of CvFolderVO objects
   */
  public ArrayList getPublicFolders() throws RemoteException;
}
