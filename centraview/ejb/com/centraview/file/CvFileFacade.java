/*
 * $RCSfile: CvFileFacade.java,v $    $Revision: 1.5 $  $Date: 2005/10/13 13:05:05 $ - $Author: mcallist $
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

import java.io.InputStream;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVAudit;
import com.centraview.common.CVUtility;

public class CvFileFacade extends CVAudit {
  private static Logger logger = Logger.getLogger(CvFileFacade.class);

  private static final String EMAIL_ATTACH_FOLDER_NAME = "CV_EMAIL_DEFAULT_FOLDER";

  private static final String CV_DEFAULT_FOLDER_NAME = "CV_FILE_DEFAULT_FOLDER";

  public static final String CV_EMPLOYEE_HANDBOOK_FOLDER_NAME = "CV_EMPLOYEEHANDBOOK_DEFAULT_FOLDER";

  private static CvFileHome fileHome;

  static {
    Object home = null;
    Class homeClass = null;
    try {
      home = CVUtility.getInitialContext().lookup("CvFile");
      homeClass = Class.forName("com.centraview.file.CvFileHome");
    } catch (NamingException e) {
      System.out.println("[NamingException][CvFileFacade.<init>] JNDI lookups are not working, most things won't work: " + e);
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      System.out.println("[Exception][CvFileFacade.<init>] CvFileHome is not on the ClassPath: " + e);
      e.printStackTrace();
    }
    fileHome = (CvFileHome) javax.rmi.PortableRemoteObject.narrow(home, homeClass);
  }

  /**
   *
   *
   */
  public CvFileFacade() {
    super();
  }

  /**
   * To return the home folder object based on userid
   * @param userID
   * @return
   * @throws CvFileException
   */
  public CvFolderVO getHomeFolder(int userID, String dataSource) throws CvFileException
  {
    CvFolderVO homeFolder = null;
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      homeFolder = remote.getHomeFolder(userID);
    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.getHomeFolder] Home Directory problem, Exception Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.getHomeFolder] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in FileFacade.getHomeFolder(userID)");
    }
    return homeFolder;
  }

  /**
   * Adds a new Folder
   * @param individualId
   * @param fldvo
   * @exception CvFileException
   */
  public int addFolder(int individualId, CvFolderVO fldvo, String dataSource) throws CvFileException
  {
    String folderName = fldvo.getName();
    int parent = fldvo.getParent();
    int folderID = 0;
    if (folderName == null) {
      throw new CvFileException(CvFileException.INVALID_DATA, "FolderName not provided");
    }
    try {
      CvFile fr = fileHome.create();
      fr.setDataSource(dataSource);
      CvFolderVO fdvo;
      Vector rootPath;

      if (parent == 0) {
        // add to the default root directory if no parent specified
        fdvo = fr.getFolderByName(individualId, 0, CV_DEFAULT_FOLDER_NAME);
        // because getFolderByName does not return the full path
        rootPath = fr.getFolderRootPath(individualId, fdvo.getFolderId());
      } else {
        fdvo = fr.getFolder(individualId, parent);
        rootPath = fdvo.getFullPathVec();
      }
      CvDiskOperation cdo = new CvDiskOperation(fdvo.getLocationName(), rootPath);
      cdo.createDirectory(fldvo.getName());
      folderID = fr.addFolder(individualId, fldvo);
    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.addFolder] CvFileException Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      logger.error("[addFolder]: Exception", e);
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding folder");
    }
    return folderID;
  }

  /*
   * Gets the Folder Contents
   */
  public CvFolderVO getFolder(int individualId, int folderId, String dataSource) throws CvFileException
  {
    try {
      CvFile fr = fileHome.create();
      fr.setDataSource(dataSource);
      return fr.getFolder(individualId, folderId);
    } catch (CvFileException cve) {
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.getFolder] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while getting folderVO");
    }
  }

  public void updateFolder(int individualId, CvFolderVO fldvo, String dataSource) throws CvFileException
  {
    try {
      CvFile fr = fileHome.create();
      fr.setDataSource(dataSource);

      CvFolderVO orgFolder = fr.getFolder(individualId, fldvo.getFolderId());
      CvFolderVO newFolder = fr.getFolder(individualId, fldvo.getParent());

      CvDiskOperation cdo = new CvDiskOperation(orgFolder.getName(), orgFolder.getLocationName(), orgFolder.getFullPathVec());

      if (cdo.modifyFolder(orgFolder.getName(), fldvo.getName(), newFolder.getLocationName(), newFolder.getFullPathVec())) {
        if (!(cdo.getNewFolderName().equals(""))) {
          fldvo.setName(cdo.getNewFolderName());
        }
        fr.updateFolder(individualId, fldvo);
      }

    } catch (CvFileException cve) {
      logger.error("[updateFolder]: Exception", cve);
      throw cve;
    } catch (Exception e) {
      logger.error("[updateFolder]: Exception", e);
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while updating folder");
    }
  }

  /**
   * Adds file Only to the DataBase.
   * @param individualId
   * @param folderId
   * @param flvo
   * @return
   * @exception CvFileException
   */
  public int addFile(int individualId, int folderId, CvFileVO flvo, String dataSource) throws CvFileException
  {
    try {
      CvFile fr = fileHome.create();
      fr.setDataSource(dataSource);
      return fr.addFile(individualId, folderId, flvo);
    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.addFile] Exception Thrown: " + cve);
      cve.printStackTrace();
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.addFile] Exception Thrown: " + e);
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }
  }

  /**
   * add a new File on Disk as well as in the database
   * @param individualId
   * @param folderId
   * @param flvo
   * @return
   * @exception CvFileException
   */
  public int addFile(int individualId, int folderId, CvFileVO flvo, InputStream fis, String dataSource) throws CvFileException
  {
    int resultValue = -1;
    try {
      CvFile fr = fileHome.create();
      fr.setDataSource(dataSource);
      CvFolderVO fdvo = fr.getFolder(individualId, folderId);
      String workingLocation = fdvo.getLocationName();
      Vector workingDirectory = fdvo.getFullPathVec();
      CvDiskOperation cdo = new CvDiskOperation(workingLocation, workingDirectory);
      cdo.createFile(flvo.getName(), fis);
      resultValue = fr.addFile(individualId, folderId, flvo);
    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.addFile] CvFileException Thrown: " + cve);
      cve.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.addFile] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }
    return resultValue;
  }

  /**
   * Gets File Information
   * @param individualId
   * @param fileId
   * @param
   * @return CvFileVO
   * @exception CvFileException
   */
  public CvFileVO getFile(int individualId, int fileId, String dataSource) throws CvFileException
  {
    try {
      CvFile fr = fileHome.create();
      fr.setDataSource(dataSource);
      return fr.getFile(individualId, fileId);
    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.getFile] CvFileException Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.getFile] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }
  }

  /*
   * Modifies a File
   */
  public void updateFile(int individualId, int[] folderId, CvFileVO flvo, InputStream modFile, String dataSource) throws CvFileException
  {
    try {
      String newFileName;
      String newFileLocation;
      Vector newFilePathVec;

      CvFile fr = fileHome.create();
      fr.setDataSource(dataSource);

      CvFileVO orgFile = fr.getFile(individualId, flvo.getFileId());

      if (modFile.available() > 0) {

        if ((orgFile.getName() != flvo.getName()) && !(flvo.getName().equals("")))
          newFileName = flvo.getName();
        else
          newFileName = orgFile.getName();

      } else {
        newFileName = orgFile.getName();
      }
      flvo.setName(newFileName);

      if (orgFile.getPhysicalFolder() != flvo.getPhysicalFolder()) {
        CvFolderVO fldvo = fr.getFolder(individualId, flvo.getPhysicalFolder());
        newFileLocation = fldvo.getLocationName();
        newFilePathVec = fldvo.getFullPathVec();
      } else {
        newFileLocation = orgFile.getPhysicalFolderVO().getLocationName();
        newFilePathVec = orgFile.getPhysicalFolderVO().getFullPathVec();
      }

      CvDiskOperation cdo = new CvDiskOperation(orgFile.getPhysicalFolderVO().getLocationName(), orgFile.getPhysicalFolderVO().getFullPathVec());

      if (modFile.available() <= 0 && (orgFile.getPhysicalFolder() != flvo.getPhysicalFolder())) {
        cdo.setInStream(newFileName);
        cdo.modifyFile(orgFile.getName(), newFileName, newFileLocation, newFilePathVec, cdo.getInStream());
      } else {
        cdo.modifyFile(orgFile.getName(), newFileName, newFileLocation, newFilePathVec, modFile);
      }
      fr.updateFile(individualId, folderId, flvo);
    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.updateFile] CvFileException Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.updateFile] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }
  }

  /**
   * Delete the Folder from Disk as well as Folder
   * @param Id
   * @param folderId
   */
  public void deleteFolder(int Id, int folderId, String dataSource) throws AuthorizationFailedException
  {
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      CvFolderVO cvfVO = remote.getFolder(Id, folderId);
      CvDiskOperation cdo = new CvDiskOperation(cvfVO.getName(), cvfVO.getLocationName(), cvfVO.getFullPathVec());

      if (cdo.deleteFolder())
        remote.deleteFolder(Id, folderId);
    } catch (RemoteException re) {
      throw new EJBException(re);
    } catch (CvFileException re) {
      throw new EJBException(re);
    } catch (CreateException re) {
      throw new EJBException(re);
    }
  }

  /**
   * Delete a file From the Database as well as from the Disk
   * @param Id
   * @param fileId
   * @param currfolderID
   */
  public void deleteFile(int Id, int fileId, String dataSource) throws AuthorizationFailedException
  {
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      CvFileVO cvfVO = remote.getFile(Id, fileId);
      int parentFolderId = cvfVO.getPhysicalFolder();

      String fileName = cvfVO.getName();
      CvFolderVO flVO = cvfVO.getPhysicalFolderVO();

      CvDiskOperation cdo = new CvDiskOperation(flVO.getLocationName(), flVO.getFullPathVec());
      String worlLoc = cdo.getWorkingFullpath();

      if (cdo.deleteFile(worlLoc, fileName))
        remote.deleteFile(Id, fileId, parentFolderId);
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.deleteFile] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }

  /**
   * Delete a file From the Database as well as from the Disk
   * @param Id
   * @param fileId
   * @param currfolderID
   */
  public void deleteFile(int Id, int fileId, int currentFolderId, String dataSource) throws AuthorizationFailedException, CreateException,
      NamingException, CvFileException
  {

    InitialContext ic = CVUtility.getInitialContext();
    CvFileLocalHome home = (CvFileLocalHome) ic.lookup("local/CvFile");
    CvFileLocal remote = home.create();
    remote.setDataSource(dataSource);

    CvFileVO cvfVO = remote.getFile(Id, fileId);

    String fileName = cvfVO.getName();
    CvFolderVO flVO = cvfVO.getPhysicalFolderVO();

    CvDiskOperation cdo = new CvDiskOperation(flVO.getLocationName(), flVO.getFullPathVec());
    String worlLoc = cdo.getWorkingFullpath();

    if (cdo.deleteFile(worlLoc, fileName))
      remote.deleteFile(Id, fileId, currentFolderId);

  }

  /**
   * This method calls the getFolderRootPath method of CvFile Bean
   * @param userID
   * @param folderID
   * @return Vector(vecFullPath)
   */
  public Vector getFolderRootPath(int userID, int folderID, String dataSource)
  {
    Vector vecFullPath = new Vector();

    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      vecFullPath = remote.getFolderRootPath(userID, folderID);
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.getFolderRootPath] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return vecFullPath;

  }

  public FileListElement getParentFolder(int individualId, int folderId, String dataSource)
  {

    FileListElement filelistelement = null;
    try {

      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      int parentId = this.getFolder(individualId, folderId, dataSource).getParent();
      int parentParentId = this.getFolder(individualId, parentId, dataSource).getParent();
      filelistelement = remote.getParentFolder(individualId, parentId, parentParentId);
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.getAllFiles] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return filelistelement;
  }

  /**
   * //This method calls the getAllFiles method of the CvFileEJB
   * @param userID
   * @param folderID
   * @param hm
   * @return
   */
  public FileList getAllFiles(int userID, HashMap hm, String dataSource)
  {
    FileList returnDL = null;
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      returnDL = remote.getAllFiles(userID, hm);
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.getAllFiles] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return returnDL;
  }

  /**
   * @param individualId
   * @param flvo
   * @param attachFile
   * @return int
   * @exception CvFileException
   */
  public int addActivityAttachment(int individualId, CvFileVO flvo, InputStream attachFile, String dataSource) throws CvFileException
  {
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      CvFolderVO homeFld = remote.getHomeFolder(individualId);
      flvo.setTitle(flvo.getName());

      int rn = (new Random()).nextInt();
      // TEMPORARY-EMAIL-ATTACHMENT
      flvo.setName("TEA" + "_" + individualId + "_" + rn + "_" + flvo.getName());
      flvo.setPhysicalFolder(homeFld.getFolderId());
      flvo.setIsTemporary("YES");
      flvo.setPhysical(CvFileVO.FP_PHYSICAL);

      return addFile(individualId, homeFld.getFolderId(), flvo, attachFile, dataSource);

    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.addActivityAttachment] Exception Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.addActivityAttachment] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }
  }

  /**
   * @param individualId
   * @param flvo
   * @param attachFile
   * @return int
   * @exception CvFileException
   */
  public int addEmailAttachment(int individualId, CvFileVO flvo, InputStream attachFile, String dataSource) throws CvFileException
  {

    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      CvFolderVO emlFld = remote.getFolderByName(individualId, 2, EMAIL_ATTACH_FOLDER_NAME);

      flvo.setTitle(flvo.getName());

      int rn = (new Random()).nextInt();
      // TEMPORARY-EMAIL-ATTACHMENT
      flvo.setName("TEA" + "_" + individualId + "_" + rn + "_" + flvo.getName());
      flvo.setPhysicalFolder(emlFld.getFolderId());
      flvo.setIsTemporary("YES");
      flvo.setOwner(individualId);
      flvo.setPhysical(CvFileVO.FP_PHYSICAL);

      return addFile(individualId, emlFld.getFolderId(), flvo, attachFile, dataSource);

    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.addEmailAttachment] Exception Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.addEmailAttachment] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }

  }

  /**
   * This method deletes attached file
   * @param individualId
   * @param fileId - Id of File which is to be deleted
   * @return
   * @exception CvFileException - Other Exception Type, gets throwed if any
   *              error occurs in deleting file.
   */
  public void deleteEmailAttachment(int individualId, int fileId, String dataSource) throws CvFileException, AuthorizationFailedException,
      NamingException, CreateException
  {
    InitialContext ic = CVUtility.getInitialContext();
    CvFileLocalHome home = (CvFileLocalHome) ic.lookup("local/CvFile");
    CvFileLocal remote = home.create();
    remote.setDataSource(dataSource);
    CvFolderVO emlFld = remote.getFolderByName(individualId, 2, EMAIL_ATTACH_FOLDER_NAME);
    deleteFile(individualId, fileId, emlFld.getFolderId(), dataSource);
  }

  /**
   * Call getFile. Made this method just incase the getEmail attachment needs
   * any ore processing like stripping the ID from the name etc.
   * @param individualId
   * @param fileId - File ID that needs to be fetched
   * @return
   * @exception CvFileException
   */
  public CvFileVO getEmailAttachment(int individualId, int fileId, String dataSource) throws CvFileException
  {
    return getFile(individualId, fileId, dataSource);
  }

  public void commitEmailAttachment(int individualId, int fileId, String dataSource) throws CvFileException
  {
    try {
      CvFile fr = fileHome.create();
      fr.setDataSource(dataSource);
      CvFileVO flvo = fr.getFileBasic(individualId, fileId);

      String workingLocation = flvo.getPhysicalFolderVO().getLocationName();
      Vector workingDirecdory = flvo.getPhysicalFolderVO().getFullPathVec();

      String oldName = flvo.getName();

      Calendar c = Calendar.getInstance();
      java.util.Date dt = c.getTime();
      DateFormat df = new SimpleDateFormat("MM_dd_yyyy_hh_mm_ss");
      String dateStamp = df.format(dt);

      // String newName = fileId + "_" + flvo.getTitle();

      String newName = "attachment_" + dateStamp + "_" + flvo.getTitle();

      CvDiskOperation cdo = new CvDiskOperation(workingLocation, workingDirecdory);
      cdo.renameFile(oldName, newName);

      fr.commitEmailAttachment(fileId, newName);

    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.commitEmailAttachment] CvFileException Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.commitEmailAttachment] Exception Thrown: " + e);
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }
  }

  /**
   * This method calls the CvFile EJB to get a list of folders owned by the
   * user.
   * @param userID
   * @return ArrayList (arrlistFolders)
   */
  public ArrayList getUserFolders(int userID, String dataSource)
  {
    ArrayList arrlistFolders = new ArrayList();
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      arrlistFolders = remote.getUserFolders(userID);
    } catch (Exception e) {
      logger.error("[getUserFolders]: Exception", e);
    }
    return arrlistFolders;
  }

  /**
   * This method calls the CvFile EJB to get a list of folders in the public
   * folders directory
   * @return ArrayList of CvFolderVOs
   */
  public ArrayList getPublicFolders(String dataSource)
  {
    ArrayList publicFolderList = new ArrayList();
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      publicFolderList = remote.getPublicFolders();
    } catch (Exception e) {
      logger.error("[getPublicFolders]: Exception", e);
    }
    return publicFolderList;
  }

  /**
   * This method calls the duplicateFolder of CvFile Bean
   * @param userID
   * @param folderID
   */
  public void duplicateFolder(int userID, CvFolderVO fdvo, int curfolderID, int curParentID, String dataSource)
  {
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      CvFolderVO cvfCurFolVO = remote.getFolder(userID, curfolderID);
      CvFolderVO cvfNewFolVO = remote.getFolder(userID, fdvo.getParent());

      CvDiskOperation cdo = new CvDiskOperation(cvfCurFolVO.getName(), cvfCurFolVO.getLocationName(), cvfCurFolVO.getFullPathVec());
      boolean isOperationComplete = cdo.duplicateFolder(cvfNewFolVO.getLocationName(), cvfNewFolVO.getFullPathVec(), fdvo.getName());

      if (!(cdo.getNewFolderName()).equals("") && (isOperationComplete == true)) {
        fdvo.setName(cdo.getNewFolderName());
        remote.duplicateFolder(userID, fdvo, curfolderID, curParentID);
      }

    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.duplicateFolder] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }

  /**
   * This method calls the duplicateFile of CvFile Bean
   * @param userID
   * @param fileID
   */
  public int duplicateFile(int userID, CvFileVO fileVO, int folderID, String dataSource)
  {
    int newFileID = 0;
    try {
      if (fileVO == null)
        throw new CvFileException(CvFileException.INVALID_DATA, "Could not duplicate File");

      int fileID = fileVO.getFileId();

      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      CvFileVO orgFile = remote.getFile(userID, fileID);
      CvFolderVO orgFolder = orgFile.getPhysicalFolderVO();
      CvFolderVO newFolder = remote.getFolder(userID, folderID);

      CvDiskOperation cdo = new CvDiskOperation(orgFolder.getLocationName(), orgFolder.getFullPathVec());
      cdo.duplicateFile(orgFile.getName(), newFolder.getLocationName(), newFolder.getFullPathVec());

      orgFile.setName(cdo.getNewFileName());
      if (fileVO.getTitle() != null && fileVO.getTitle().length() > 0)
        orgFile.setTitle(fileVO.getTitle());

      if (fileVO.getDescription() != null && fileVO.getTitle().length() > 0)
        orgFile.setDescription(fileVO.getDescription());

      if (fileVO.getAuthorId() > 0)
        orgFile.setAuthorId(fileVO.getAuthorId());

      if (fileVO.getRelateEntity() > 0)
        orgFile.setRelateEntity(fileVO.getRelateEntity());

      if (fileVO.getRelateIndividual() > 0)
        orgFile.setRelateIndividual(fileVO.getRelateIndividual());

      // will be set to default in the EJB
      orgFile.setOwner(userID);
      orgFile.setVersion(null);

      orgFile.setVisibility(fileVO.getVisibility());
      orgFile.setCustomerView(fileVO.getCustomerView());

      orgFile.setPhysicalFolder(fileVO.getPhysicalFolder());

      newFileID = remote.addFile(userID, folderID, orgFile);
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.duplicateFile] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return newFileID;
  }

  /**
   * This method calls the getAllFolders method of CvFile Bean
   * @param userID
   * @return ArrayList (arrlistFolders)
   */
  public ArrayList getAllFolders(int individualId, boolean systemIncludeFlag, String dataSource)
  {
    ArrayList arrlistFolders = new ArrayList();
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      arrlistFolders = remote.getAllFolders(individualId, systemIncludeFlag);
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.getAllFolders] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return arrlistFolders;
  }

  /**
   * @param individualId
   * @param flvo
   * @param attachFile
   * @return int
   * @exception CvFileException
   */
  public int addEmployeeHandbook(int individualId, CvFileVO flvo, InputStream attachFile, String dataSource) throws CvFileException
  {

    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      CvFolderVO emlFld = remote.getFolderByName(individualId, 2, CV_EMPLOYEE_HANDBOOK_FOLDER_NAME);

      flvo.setTitle(flvo.getName());

      int rn = (new Random()).nextInt();
      flvo.setName(flvo.getName());
      flvo.setPhysicalFolder(emlFld.getFolderId());
      flvo.setIsTemporary("NO");
      flvo.setPhysical(CvFileVO.FP_PHYSICAL);
      return addFile(individualId, emlFld.getFolderId(), flvo, attachFile, dataSource);

    } catch (CvFileException cve) {
      System.out.println("[Exception][CvFileFacade.addEmployeeHandbook] CvFileException Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.addEmployeeHandbook] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }

  }

  // For the Customer View
  public FileList getAllCustomerFiles(int userID, HashMap hm, String dataSource)
  {
    FileList returnDL = null;

    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      returnDL = remote.getAllCustomerFiles(userID, hm);
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.getAllCustomerFiles] Exception Thrown: " + e);
      e.printStackTrace();
    }

    return returnDL;
  }

  /**
   * @param individualId
   * @param flvo
   * @param attachFile
   * @return int
   * @exception CvFileException
   */
  public int addLiterature(int individualId, CvFileVO flvo, InputStream uploadFile, String dataSource) throws CvFileException
  {
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);

      CvFolderVO cfvLiterature = remote.getFolderByName(individualId, 3, CV_DEFAULT_FOLDER_NAME);

      flvo.setTitle(flvo.getName());

      int rn = (new Random()).nextInt();
      // TEMPORARY-EMAIL-ATTACHMENT
      flvo.setName("TEA" + "_" + individualId + "_" + rn + "_" + flvo.getName());
      flvo.setPhysicalFolder(cfvLiterature.getFolderId());
      flvo.setIsTemporary("NO");
      flvo.setAuthorId(individualId);
      flvo.setOwner(individualId);
      flvo.setPhysical(CvFileVO.FP_PHYSICAL);
      System.out.println("userid , uploadfile,folderid : " + individualId + " , " + cfvLiterature.getFolderId() + " , " + uploadFile);
      return addFile(individualId, cfvLiterature.getFolderId(), flvo, uploadFile, dataSource);

    } catch (CvFileException cve) {
      System.out.println("[CvFileException][CvFileFacade.addLiterature] Exception Thrown: " + cve);
      throw cve;
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.addLiterature] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in File Facade while adding File");
    }
  }

  // Delete Literature

  /**
   * This method deletes uploaded lLiterature
   * @param individualId
   * @param fileId - Id of File which is to be deleted
   * @return
   * @exception CvFileException - Other Exception Type, gets throwed if any
   *              error occurs in deleting file.
   */
  public void deleteLiterature(int individualId, int fileId, String dataSource) throws CvFileException
  {
    try {

      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      CvFolderVO emlFld = remote.getFolderByName(individualId, 3, CV_DEFAULT_FOLDER_NAME);

      deleteFile(individualId, fileId, emlFld.getFolderId(), dataSource);
    } catch (Exception e) {
      System.out.println("[Exception][CvFileFacade.deleteLiterature] Exception Thrown: " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in delete Literature function");
    }
  }

  /**
   * Add a user folder, however if the folder name exists either change the folder
   * @param userid
   * @param userName
   * @param dataSource
   * @return
   * @throws CvFileException
   */
  public int addFolder(int userid, String userName, String dataSource) throws CvFileException
  {
    int folderID = 0;
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      CvFolderVO cvroot = remote.getFolderByName(userid, 0, "CV_ROOT");
      CvFolderVO cvfsroot = remote.getFolderByName(userid, cvroot.getFolderId(), "CVFS_ROOT");
      CvFolderVO cvfsuser = remote.getFolderByName(userid, cvfsroot.getFolderId(), "CVFS_USER");
      try {
        CvFolderVO checkUserName = remote.getFolderByName(userid, cvfsuser.getFolderId(), userName);
        if (checkUserName != null) {
          // append a date time string to the end of the folder name.
          Calendar calendar = Calendar.getInstance();
          SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
          userName += df.format(calendar.getTime()); 
        }
      } catch (CvFileException fe) {
        // if we get an exception it means the folder doesn't exist so we are cool.
      }
      CvFolderVO cvVo = new CvFolderVO();
      cvVo.setParent(cvfsuser.getFolderId());
      cvVo.setName(userName);
      folderID = addFolder(userid, cvVo, dataSource);
    } catch (Exception e) {
      logger.error("[addFolder]: Exception", e);
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed in Create USER FOLDER");
    }
    return folderID;
  }

  /**
   * Gets File Input Stream. we will pass the fileID and get folderPath and
   * fileName We will process the image to a byte array and return it.
   * @param individualId Identification for the individual who is logged in.
   * @param fileId Identification of the file.
   * @param dataSource A string that contains the cannonical JNDI name of the
   *          datasource
   * @return HashMap It will return two things array of byte & fileName.
   */
  public HashMap getFileInputStream(int individualId, int fileId, String dataSource)
  {
    HashMap imageInfo = new HashMap();
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      imageInfo = remote.getFileInputStream(individualId, fileId, dataSource);
    } catch (Exception e) {
      logger.error("[getFileInputStream]: Exception", e);
    }
    return imageInfo;
  }

  /**
   * calls the CvFile EJB to get the public folder id.
   * @return Integer folder id or null if something goes wrong.
   */
  public Integer getPublicFolderId(String dataSource)
  {
    Integer id = null;
    try {
      CvFile remote = fileHome.create();
      remote.setDataSource(dataSource);
      id = remote.getPublicFolderId();
    } catch (Exception e) {
      logger.error("[getPublicFolderId]: Exception", e);
    }
    return id;
  }
}
