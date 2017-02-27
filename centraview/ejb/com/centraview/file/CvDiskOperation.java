/*
 * $RCSfile: CvDiskOperation.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:20 $ - $Author: mking_cv $
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Vector;

import com.centraview.common.DDNameValue;

public class CvDiskOperation
{

  //one slash is escape character
  private static String PATH_SEPERATOR = "/";

  private String workingLocation;
  private Vector workingDirectory;
  private String fullWorkingPath;
  private String currFolderName;

  private Vector newWorkingVector;//variable for setting new Working vector
  private String newWorkingPath;//variable for setting new Full Path
  private String newWorkingLocation;//variable for setting new Location

  private String newFolderName = "";//variable for setting new generated folder
  // name
  private String newFileName = "";//variable for setting new generated File
  // name

  private InputStream inStream;

  /**
   * Constructor
   * 
   * @param workingLocation
   * @param workingDirectory
   * @exception CvFileException
   */
  public CvDiskOperation(String workingLocation, Vector workingDirectory) throws CvFileException
  {
    this.fullWorkingPath = getWorkingLocation(workingLocation, workingDirectory);
  }

  /**
   * Constructor
   * 
   * @param currFolderName
   * @param workingLocation
   * @param workingDirectory
   * @exception CvFileException
   */
  public CvDiskOperation(String currFolderName, String workingLocation, Vector workingDirectory) throws CvFileException
  {
    this(workingLocation, workingDirectory);

    this.currFolderName = currFolderName;
  }

  /**
   * gets the Full Working Path
   * 
   * @return Full Working path(Directory Location)
   */
  public String getWorkingFullpath()
  {
    return this.fullWorkingPath;
  }

  public void setWorkingFullpath(String fullWorkingPath)
  {
    this.fullWorkingPath = fullWorkingPath;
  }

  /**
   * gets the Location(Initial Directory)
   * 
   * @param workingLocation
   * @param workingDirectory
   * @return Initial Directory
   */
  private String getWorkingLocation(String workingLocation, Vector workingDirectory)
  {
    String tmpFp = null;
    try {

      if (workingLocation == null)
        throw new CvFileException(CvFileException.INVALID_DATA, "Working Location is null");

      this.workingLocation = workingLocation;

      if (workingDirectory == null)
        throw new CvFileException(CvFileException.INVALID_DATA, "Working Directory is null");

      this.workingDirectory = workingDirectory;

      if (workingLocation.indexOf("\\") >= 0)
        PATH_SEPERATOR = "/";
      else if (workingLocation.indexOf("/") >= 0)
        PATH_SEPERATOR = "/";
      else {
        throw new CvFileException(CvFileException.INVALID_DATA, "Location not set properly");
      }

      Iterator it = this.workingDirectory.iterator();
      tmpFp = new String();

      while (it.hasNext()) {
        tmpFp = ((DDNameValue)it.next()).getName() + PATH_SEPERATOR + tmpFp;
      }

      tmpFp = this.workingLocation + PATH_SEPERATOR + tmpFp;
    } catch (Exception e) {
      System.out.println("Error in  getWorkingLocation in CvDiskOperation ");
      //e.printStackTrace();
    }

    return tmpFp;
  }

  /**
   * Creates a new Directory using the dirName
   * 
   * @param dirName
   * @exception CvFileException
   */
  public void createDirectory(String dirName) throws CvFileException
  {
    // check if working dir and location is set
    if (dirName == null) {
      throw new CvFileException(CvFileException.INVALID_DATA, "Directory Name not provided");
    }

    File fl = new File(this.fullWorkingPath + dirName);

    try {
      if (!fl.mkdir()) {
        throw new CvFileException(CvFileException.COULD_NOT_CREATE_DIRECTORY, "Could not create Directory");
      }
    } catch (Exception e) {
      System.out.println("[Exception][CvDiskOperation] Exception thrown in createDirectory(): " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Could not create Directory");
    } finally {
      fl = null;
    }
  } // end createDirectory() method

  /**
   * Creates a new File using fileName and its content in the form of
   * InputStream object
   */
  public void createFile(String fileName, InputStream fis) throws CvFileException
  {
    // check if working dir and location is set
    OutputStream fos;
    File fl;
    try {
      if (fis == null || fis.available() < 0) {
        return;
      }
      fl = new File(this.fullWorkingPath + fileName);

      if (!fl.createNewFile()) {
        throw new CvFileException(CvFileException.COULD_NOT_CREATE_DIRECTORY, "Could not create file");
      }

      fos = new FileOutputStream(fl);

      try {
        int i = 0;
        while ((i = fis.read()) != -1) {
          fos.write(i);
        }
      } catch (Exception e) {
        fis.close();
        fos.close();
        System.out.println("[Exception] CvDiskOperation.createFile: " + e.toString());
        e.printStackTrace();
        throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Other exception");
      }
      fos.close();
      fis.close();
    } catch (Exception e) {
      System.out.println("[Exception] CvDiskOperation.createFile: " + e.toString());
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Other exception");
    } finally {
      fl = null;
      //fos.close();
    }
  } // end createFile()

  /**
   * 
   * 
   * @return String
   */
  private String makeFullPath()
  {
    return null;
    // make the full path from hashmap
  }

  /**
   * Deletes the file using the full path and file name
   * 
   * @param fullFilePath
   * @param fileName
   * @return @exception CvFileException
   */
  public boolean deleteFile(String fullFilePath, String fileName) throws CvFileException
  {
    boolean isOperationComplete = false;

    File fDel = null;
    try {

      fDel = new File(fullFilePath + fileName);

      if (fDel.exists()) {
        fDel.delete();
        isOperationComplete = true;
      } else
        isOperationComplete = false;

    } catch (Exception e) {
      System.out.println("[Exception] CvDiskOperation.deleteFile: " + e.toString());
      //e.printStackTrace();
      //System.out.println(" Error in deleteFile in CvDiskOperation ");
    } finally {
      fDel = null;

    }
    return isOperationComplete;
  }

  /**
   * Deletes the Parent folder from Directory and calls deleteRecursiveFolder
   * for deleting the subdirectory
   * 
   * @return @exception CvFileException
   */
  public boolean deleteFolder() throws CvFileException
  {
    //System.out.println(this.fullWorkingPath);
    boolean isOperationComplete = false;

    File filDelete = null;
    try {
      String workingPath = this.fullWorkingPath;//gets the current full working
      // path

      filDelete = new File(this.fullWorkingPath);

      if (filDelete.exists())
        isOperationComplete = deleteRecursiveFolder(this.fullWorkingPath);//call
      // for
      // deleting
      // the
      // subdirectories
      else
        return isOperationComplete;

      String curDelFolderWorkPath = (workingPath).substring(0, workingPath.indexOf(currFolderName));//deletes
      // the
      // Parent
      // folder

      if (!filDelete.delete())
        throw new CvFileException(CvFileException.DELETE_FAILED, "Could not Delete Last Directory");

      isOperationComplete = true;

    } catch (Exception e) {
      System.out.println("[Exception] CvDiskOperation.deleteFolder: " + e.toString());
      //e.printStackTrace();
    } finally {
      filDelete = null;
    }

    return isOperationComplete;
  }

  /**
   * called by deleteFolder deletes the subdirectories
   * 
   * @param curWorkPath
   * @return
   */
  private boolean deleteRecursiveFolder(String curWorkPath)
  {

    boolean isOperationComplete = false;

    String delFilePath = curWorkPath;//store the current working path

    File filDelete = null;
    try {

      filDelete = new File(delFilePath);

      if (filDelete.exists()) {

        File[] strFiles = filDelete.listFiles();//lists the files and folders
        // of current directory

        if (strFiles != null) {

          for (int i = 0; i < strFiles.length; i++) {
            if (strFiles[i].isFile()) {
              try {
                if (!deleteFile(delFilePath, strFiles[i].getName()))//if a file
                  // is found
                  // delete
                  // the file
                  return isOperationComplete;
              } catch (Exception e) {}
            } else if (strFiles[i].isDirectory()) {
              curWorkPath = delFilePath + strFiles[i].getName() + PATH_SEPERATOR;
              //curWorkPath is used for setting the sub folder directory path

              if (deleteRecursiveFolder(curWorkPath))//call the same fuction
                // recursively
                strFiles[i].delete();//deletes the current folder
              else
                return isOperationComplete;
            }
          }
        }
      } else
        throw new CvFileException(CvFileException.DELETE_FAILED, "Could not Delete Directory");

      isOperationComplete = true;
    } catch (Exception e) {
      System.out.println(" Error in  deleteRecursiveFolder");
      e.printStackTrace();
    } finally {
      filDelete = null;
    }

    return isOperationComplete;

  }

  /**
   * rename a file/folder with new name and/or location
   * 
   * @param oldFileName
   * @param newFileName
   * @exception CvFileException
   */
  public void renameFile(String oldFileName, String newFileName) throws CvFileException
  {
    File oldFile = null;
    File newFile = null;
    try {

      oldFile = new File(this.fullWorkingPath + oldFileName);

      if (!oldFile.exists())
        throw new CvFileException(CvFileException.COULD_NOT_CREATE_FILE, "File does not exists");

      newFile = new File(this.fullWorkingPath + newFileName);
      oldFile.renameTo(newFile);
    } catch (Exception e) {
      e.printStackTrace();
      throw new CvFileException(CvFileException.COULD_NOT_CREATE_FILE, "Unknown error");
    }

    finally {
      oldFile = null;
      newFile = null;
    }

  }

  /*
   * copies a file from current location to the new location
   *  
   */
  public boolean duplicateFile(String origFileName, String newFileLocation, Vector newFileParentFolder) throws CvFileException
  {

    boolean isOperationComplete = false;

    File oldFile = null;

    try {

      oldFile = new File(this.fullWorkingPath + origFileName);//original file
      // from where the
      // file is to be
      // duplicated

      CvDiskOperation newCvLocation = new CvDiskOperation(newFileLocation, newFileParentFolder);
      FileInputStream fis = new FileInputStream(oldFile);

      String fileName = origFileName;
      String newFileName = "";

      int counter = 1;

      /*
       * if file with the current name already exists then loop to create a new
       * name
       */
      while (newFileName.equals("")) {

        if (newCvLocation.isExists(newFileLocation, newFileParentFolder, fileName)) {
          fileName = "Copy " + counter + " of " + origFileName;

        } else {
          newFileName = fileName;
          setNewFileName(newFileName);//sets the generated name of the folder
        }

        counter++;
      }

      newCvLocation.createFile(newFileName, fis);//creates a file

      isOperationComplete = true;

    } catch (Exception e) {
      System.out.println("Error in duplicateFolder in CvDiskoperation ");
      e.printStackTrace();
    } finally {
      oldFile = null;

    }

    return isOperationComplete;
  }

  /*
   * copies a file from current location to the new location
   * 
   * used when modifying a file
   *  
   */
  public boolean duplicateFile(String origFileName, String newFileLocation, Vector newFileParentFolder, InputStream fis) throws CvFileException
  {

    boolean isOperationComplete = false;

    File oldFile = null;

    try {

      oldFile = new File(this.fullWorkingPath + origFileName);
      //original file from where the file is to be duplicated

      CvDiskOperation newCvLocation = new CvDiskOperation(newFileLocation, newFileParentFolder);
      //new file location where the file needs to be created ifexists

      String fileName = origFileName;
      String newFileName = "";

      int counter = 1;

      /*
       * if file with the current name already exists then loop to create a new
       * name
       */
      while (newFileName.equals("")) {

        if (newCvLocation.isExists(newFileLocation, newFileParentFolder, fileName)) {
          fileName = "Copy " + counter + " of " + origFileName;

        } else {
          newFileName = fileName;
          setNewFileName(newFileName);//sets the generated name of the folder
        }

        counter++;
      }

      newCvLocation.createFile(newFileName, fis);//creates a file

      isOperationComplete = true;

    } catch (Exception e) {
      System.out.println("Error in duplicateFolder in CvDiskoperation ");
      e.printStackTrace();
    } finally {
      oldFile = null;

    }

    return isOperationComplete;
  }

  /**
   * duplicates the folder and its subfolders
   * 
   * @param newWorkingLocation
   * @param newWorkingVector
   * @param newFolderName
   * @return
   */
  public boolean duplicateFolder(String newWorkingLocation, Vector newWorkingVector, String newFolderName)
  {

    boolean isOperationComplete = false;

    File filDuplicate = null;
    try {

      int counter = 1;

      CvDiskOperation newCvLocation = new CvDiskOperation(newWorkingLocation, newWorkingVector);

      String folderName = newFolderName;
      /*
       * loops till it finds a name which is not present on the disk
       */
      while (getNewFolderName().equals("")) {

        if (newCvLocation.isExists(newWorkingLocation, newWorkingVector, folderName)) {
          folderName = "Copy " + counter + " of " + newFolderName;

        } else
          setNewFolderName(folderName);//sets a new folder name

        counter++;
      }

      if (!(getNewFolderName().equals(""))) {

        this.newWorkingVector = newWorkingVector;//stores a new vector
        this.newWorkingLocation = newWorkingLocation;//stores a new work
        // location

        newWorkingPath = getWorkingLocation(newWorkingLocation, newWorkingVector);
        // gets the new working location of the folder to be duplicated
        filDuplicate = new File(newWorkingPath + PATH_SEPERATOR + getNewFolderName());

        if (!filDuplicate.mkdir())// creates the folder which needs to be
          // duplicated
          throw new CvFileException(CvFileException.COULD_NOT_CREATE_DIRECTORY, "Could not create Directory");

        newWorkingPath = newWorkingPath + PATH_SEPERATOR + getNewFolderName() + PATH_SEPERATOR;

        if (!duplicateRecursiveFolder(this.fullWorkingPath, newWorkingPath))
          // duplicates the sub folders
          return isOperationComplete;

        filDuplicate = null;
      }
      isOperationComplete = true;

    } catch (Exception e) {
      System.out.println("Error in duplicateFolder in CvDiskoperation ");
      e.printStackTrace();
    } finally {
      filDuplicate = null;
    }

    return isOperationComplete;
  }

  /**
   * called by duplicate folder for creating sub folders
   * 
   * @param curWorkPath
   * @param newWorkPath
   * @return
   */
  private boolean duplicateRecursiveFolder(String curWorkPath, String newWorkPath)
  {
    boolean isOperationComplete = false;

    File filDuplicate = null;

    String curWorkDir = curWorkPath;//store the original path FROM where
    // copying is to be done(source)
    String newWorkDir = newWorkPath;//store the original path where the folder
    // needs to be Copies(Destination)

    try {
      filDuplicate = new File(curWorkDir);

      if (filDuplicate.exists()) {
        File[] strFiles = filDuplicate.listFiles();

        for (int i = 0; i < strFiles.length; i++) {

          if (strFiles[i].isDirectory()) {
            curWorkPath = curWorkDir + strFiles[i].getName() + PATH_SEPERATOR;

            File createDir = new File(newWorkDir + strFiles[i].getName());

            if (!createDir.mkdir())
              throw new CvFileException(CvFileException.COULD_NOT_CREATE_DIRECTORY, "Could not create Directory");
            else {
              newWorkPath = newWorkDir + strFiles[i].getName() + PATH_SEPERATOR;
              //                  call the recursive folders (source,destination)
              if (!duplicateRecursiveFolder(curWorkPath, newWorkPath))
                return isOperationComplete;
            }
          } else if (strFiles[i].isFile()) {

            File oldFile = new File(curWorkDir + strFiles[i].getName());

            this.fullWorkingPath = newWorkDir;

            FileInputStream fis = new FileInputStream(oldFile);

            createFile(strFiles[i].getName(), fis);//create a new file
          }
        }
      } else
        throw new CvFileException(CvFileException.COULD_NOT_CREATE_DIRECTORY, "Could not Duplicate Directory");

      filDuplicate = null;
      isOperationComplete = true;
    } catch (Exception e) {
      System.out.println(" Error in  duplicateRecursiveFolder");
      e.printStackTrace();
    } finally {
      filDuplicate = null;
    }

    return isOperationComplete;
  }

  /**
   * Checks if the File/Folder already exists with that name in that location
   * 
   * @param newWorkingLocation
   * @param newWorkingPath
   * @param newFolderName
   * @return boolean
   */
  public boolean isExists(String newWorkingLocation, Vector newWorkingPath, String newFolderName)
  {
    boolean existFlag = false;

    String newWorkingLoc = getWorkingLocation(newWorkingLocation, newWorkingPath);

    File filDuplicate = new File(newWorkingLoc + PATH_SEPERATOR + newFolderName);

    if (filDuplicate.exists()) {

      existFlag = true;

    } else
      existFlag = false;

    filDuplicate = null;

    return existFlag;
  }

  /**
   * gets the generated folder name
   * 
   * @return new Folder Name
   */
  public String getNewFolderName()
  {
    return newFolderName;
  }

  /**
   * sets a new folder name
   * 
   * @param newFolderName
   */
  public void setNewFolderName(String newFolderName)
  {
    this.newFolderName = newFolderName;
  }

  /**
   * gets the generated file name
   * 
   * @return new File Name
   */
  public String getNewFileName()
  {
    return newFileName;
  }

  /**
   * sets the new file name
   * 
   * @param newFileName
   */
  public void setNewFileName(String newFileName)
  {
    this.newFileName = newFileName;
  }

  /**
   * Modifies a File
   * 
   * @param oldFileName
   * @param newFileName
   * @param newWorkLocation
   * @param newWorkPath
   * @param newIs
   * @return boolean
   */
  public boolean modifyFile(String oldFileName, String newFileName, String newWorkLocation, Vector newWorkPath, InputStream newIs) throws CvFileException
  {
    boolean isOperationComplete = false;
    try {
      if (newIs.available() > 0) {
        CvDiskOperation cdo = new CvDiskOperation(newWorkLocation, newWorkPath);
        //         if the name and location has not changed delete the file and create a
        // new file with new contents
        if ((oldFileName.equals(newFileName)) && (getWorkingFullpath().equals(cdo.getWorkingFullpath()))) {
          if (deleteFile(getWorkingFullpath(), oldFileName))
            cdo.createFile(oldFileName, newIs);
          else
            throw new CvFileException(CvFileException.DELETE_FAILED, "Could not delete file");

        } else if (!oldFileName.equals(newFileName) && (getWorkingFullpath().equals(cdo.getWorkingFullpath()))) {
          //         if the name has changed and location has not changed delete the
          // file and create a new file with new contents and new name
          if (deleteFile(getWorkingFullpath(), oldFileName))
            cdo.createFile(newFileName, newIs);
          else
            throw new CvFileException(CvFileException.DELETE_FAILED, "Could not delete file");
        } else if (oldFileName.equals(newFileName) && !(getWorkingFullpath().equals(cdo.getWorkingFullpath()))) {
          //         if the name has not changed and location has changed delete the
          // file and create a new file with new contents and new location
          cdo.deleteFile(cdo.getWorkingFullpath(), newFileName);
          cdo.createFile(oldFileName, newIs);

          deleteFile(getWorkingFullpath(), oldFileName);
        } else if (!oldFileName.equals(newFileName) && !(getWorkingFullpath().equals(cdo.getWorkingFullpath()))) {
          // if the name and location both are changed delete the file and create a new file with new contents, new name and new location
          deleteFile(getWorkingFullpath(), oldFileName);

          cdo.deleteFile(cdo.getWorkingFullpath(), newFileName);

          cdo.createFile(newFileName, newIs);
        }
      } else
        return isOperationComplete = false;

      isOperationComplete = true;
    } catch (Exception e) {
      System.out.println("Error in modifying File " + e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Other exception");
    }
    return isOperationComplete;

  }

  public boolean modifyFolder(String oldFolderName, String newFolderName, String newWorkLocation, Vector newWorkPath) throws CvFileException
  {

    boolean isOperationComplete = false;
    try {

      CvDiskOperation cdo = new CvDiskOperation(newWorkLocation, newWorkPath);

      cdo.setWorkingFullpath(cdo.getWorkingFullpath() + newFolderName + PATH_SEPERATOR);
      // if the name and location has not changed rename the folder
      if ((!oldFolderName.equals(newFolderName)) && (getWorkingFullpath().equals(cdo.getWorkingFullpath()))) {
        cdo.renameFile(oldFolderName, newFolderName);
      } else if (oldFolderName.equals(newFolderName) && (!(getWorkingFullpath().equals(cdo.getWorkingFullpath())))) {
        // if the name has changed but location has not changed duplicate the
        // folder and delete that folder
        if (cdo.isExists(newWorkLocation, newWorkPath, newFolderName))
          throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Folder Already Exists ");

        duplicateFolder(newWorkLocation, newWorkPath, oldFolderName);
        deleteFolder();
      } else if (!oldFolderName.equals(newFolderName) && !(getWorkingFullpath().equals(cdo.getWorkingFullpath()))) {
        // if the name and location both are changed duplicate  the folder to new location and delete that folder
        if (cdo.isExists(newWorkLocation, newWorkPath, newFolderName))
          throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Folder Already Exists ");

        duplicateFolder(newWorkLocation, newWorkPath, newFolderName);
        deleteFolder();

      }

      isOperationComplete = true;
    } catch (Exception e) {
      System.out.println("Error in modifying Folder " + e);
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Other exception");
    }
    return isOperationComplete;
  }

  public InputStream getInStream()
  {

    return this.inStream;
  }

  public void setInStream(String fileName)
  {
    try {

      File fl = new File(fullWorkingPath + fileName);

      FileInputStream fileInStream = new FileInputStream(fl);
      this.inStream = fileInStream;
    } catch (Exception e) {
      System.out.println(" Error in getting InputStream " + e);
    }
  }
}
