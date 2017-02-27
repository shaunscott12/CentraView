/*
 * $RCSfile: CvFileLocal.java,v $    $Revision: 1.2 $  $Date: 2005/09/23 11:05:00 $ - $Author: mcallist $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This is remote interface which is called from client
 */

public interface CvFileLocal extends EJBLocalObject {
  public FileList getAllFiles(int userID, HashMap hm);

  public int addFolder(int userId, CvFolderVO fdvo) throws CvFileException,
      AuthorizationFailedException;

  public CvFolderVO getFolder(int userId, int folderId) throws CvFileException,
      AuthorizationFailedException;

  public CvFolderVO getFolderByName(int userId, int parentFolderId, String folderName)
      throws CvFileException;

  public void updateFolder(int userId, CvFolderVO fdvo) throws CvFileException,
      AuthorizationFailedException;

  public void deleteFolder(int userId, int folderId) throws CvFileException,
      AuthorizationFailedException;

  public void commitEmailAttachment(int fileId, String newName) throws CvFileException;

  public int addFile(int userId, int folderId, CvFileVO fdvo) throws CvFileException,
      AuthorizationFailedException;

  public CvFileVO getFile(int userId, int folderId) throws CvFileException,
      AuthorizationFailedException;

  public CvFileVO getFileBasic(int userId, int folderId) throws CvFileException,
      AuthorizationFailedException;

  public void updateFile(int userId, int[] folderId, CvFileVO fdvo) throws CvFileException,
      AuthorizationFailedException;

  public void deleteFile(int userId, int fileId, int currFolderID) throws CvFileException,
      AuthorizationFailedException;

  public CvFolderVO getHomeFolder(int userID) throws CvFileException, AuthorizationFailedException;

  public Vector getFolderRootPath(int userID, int folderID);

  public ArrayList getUserFolders(int userID) throws CvFileException;

  public void duplicateFolder(int userID, CvFolderVO fdvo, int curfolderID, int curParentID)
      throws CvFileException, AuthorizationFailedException;

  public void duplicateFile(int userID, int fileID, int folderID) throws CvFileException,
      AuthorizationFailedException;

  public ArrayList getAllFolders(int userId, boolean systemIncludeFlag) throws CvFileException;

  public FileList getAllTicketFiles(int userID, int ticketID);

  /**
   * Gets File Input Stream. we will pass the fileID and get folderPath and
   * fileName We will process the image to a byte array and return it.
   * @param individualId Identification for the individual who is logged in.
   * @param fileId Identification of the file.
   * @param dataSource A string that contains the cannonical JNDI name of the
   *          datasource
   * @return HashMap It will return two things array of byte & fileName.
   */
  public HashMap getFileInputStream(int individualId, int fileId, String dataSource);

  /**
   * @author Kevin McAllister <kevin@centraview.com> Allows the client to set
   *         the private dataSource
   * @param ds The cannonical JNDI name of the datasource.
   */
  public void setDataSource(String ds);

  public ValueListVO getFileValueList(int individualID, ValueListParameters parameters);
}
