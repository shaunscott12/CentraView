/*
 * $RCSfile: CvFileEJB.java,v $    $Revision: 1.9 $  $Date: 2005/10/13 13:05:05 $ - $Author: mcallist $
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.DDNameValue;
import com.centraview.common.DateMember;
import com.centraview.common.EJBUtil;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.contact.entity.EntityLocal;
import com.centraview.contact.entity.EntityLocalHome;
import com.centraview.contact.entity.EntityPK;
import com.centraview.contact.individual.IndividualLocal;
import com.centraview.contact.individual.IndividualLocalHome;
import com.centraview.contact.individual.IndividualPK;
import com.centraview.cvattic.CvAtticLocal;
import com.centraview.cvattic.CvAtticLocalHome;
import com.centraview.projects.project.ProjectLocal;
import com.centraview.projects.project.ProjectLocalHome;
import com.centraview.sale.opportunity.OpportunityLocal;
import com.centraview.sale.opportunity.OpportunityLocalHome;
import com.centraview.sale.proposal.ProposalHome;
import com.centraview.sale.proposal.ProposalLocal;
import com.centraview.support.ticket.TicketLocal;
import com.centraview.support.ticket.TicketLocalHome;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public class CvFileEJB implements SessionBean
{
  private static final int DEFAULT_LOCATION = 1;
  private int transactionID = 0;
  protected SessionContext sessionContext;
  private String dataSource = "";
  private static Logger logger = Logger.getLogger(CvFileEJB.class);

  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  public void ejbCreate(){}
  public void ejbRemove(){}
  public void ejbActivate(){}
  public void ejbPassivate(){}

  public FileListElement getParentFolder(int individualId, int folderId, int parentId)
  {
    FileListElement filelistelement = null;

    if (parentId != 0 )
    {
      IntMember intFolderID = new IntMember("ID", folderId, 10, "", 'T', false, 10);
      StringMember strName = new StringMember("Name", "Parent Folder", 10, "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
      ModuleFieldRightMatrix mfrmx = CVUtility.getUserModuleRight("File", individualId, false, this.dataSource);
      StringMember strDescription = new StringMember("Description", "The Description", mfrmx.getFieldRight("File", "description"), "", 'T', false);
      StringMember strCreatedBY = new StringMember("CreatedBy", "", 10, "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
      StringMember strFileFolder = new StringMember("FileFolder", "FOLDER", 10, "", 'T', false);
      IntMember intIndividualID = new IntMember("IndividualID", individualId, 10, "", 'T', false, 10);
      StringMember strFolderName = new StringMember("FolderName", "Parent Folder", 10, "", 'T', false);

      int parentFolderId = parentId;
      IntMember intParentFolderId = new IntMember("ParentFolderId", parentFolderId, 10, "", 'T', false, 10);  // So we can have the parentFolderId for the file

      filelistelement = new FileListElement(folderId);

      filelistelement.put("FolderID", intFolderID);
      filelistelement.put("Name", strName);
      filelistelement.put("Description", strDescription);
      filelistelement.put("CreatedBy", strCreatedBY);
      filelistelement.put("Created", null);
      filelistelement.put("Updated", null);
      filelistelement.put("FileFolder", strFileFolder);
      filelistelement.put("IndividualID", intIndividualID);
      filelistelement.put("FolderName", strFolderName);
      filelistelement.put("ParentID", intParentFolderId);
    }
    return filelistelement;
  }   // end getParentFolder(int,int,int) method

  public FileList getAllFiles(int individualId, HashMap hashmap)
  {
    Integer intStart = (Integer)hashmap.get("startATparam");
    Integer intEnd = (Integer)hashmap.get("EndAtparam");
    String strSearch = (String)hashmap.get("searchString");
    String strSortMem = (String)hashmap.get("sortmem");
    Character chrSortType = (Character)hashmap.get("sortType");
    String strFileTypeReq = (String)hashmap.get("fileTypeRequest");

    Boolean systemincl = (Boolean)hashmap.get("SystemIncludeFlag");
    Integer intfolderID = (Integer)hashmap.get("curFolderID");

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int folderID = intfolderID.intValue();

    String strOriginal = strSearch;

    boolean boolIncludeSystem = false;

    if (systemincl != null)
    {
      boolIncludeSystem = systemincl.booleanValue();
    }

    int beginIndex = 0;

    FileList filelist = new FileList();
    filelist.setSortMember(strSortMem);

    Vector vec = new Vector();
    CVDal cvdl = new CVDal(this.dataSource);
    Collection colList = null;

    try
    {
      // this method creates the temporary table "fileaccess", and "folderaccess" that we will do our selects against, in this way
      // we can be sure that the rest of the code uses only records the Individual is allowed to work see.
      // This relies on these TEMPORARY tables sticking around at least throughout the life of this instance of CVDal.
      CVUtility.getAllAccessibleRecords("File", "fileaccess", "cvfile", "FileID", "Owner", "(cvfile.owner=" + individualId + " or cvfile.visibility='PUBLIC')", individualId, cvdl);
      CVUtility.getAllAccessibleRecords("CVFolder", "folderaccess", "cvfolder", "FolderID", "owner", "(cvfolder.owner=" + individualId + " and cvfolder.IsSystem='FALSE') or (cvfolder.visibility='PUBLIC')", individualId, cvdl);

      // We are doing an advanced search, or as with relatedinfo stuff selecting a subset,
      // first create a temporary table  RelatedInfo currently uses this for getting the files related to an Individual.
      if (strSearch != null && strSearch.startsWith("ADVANCE:"))
      {
        strSearch = strSearch.substring(8);

        String str = "CREATE TEMPORARY TABLE filelistSearch " + strSearch;
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();

        if (strFileTypeReq.equals(FileConstantKeys.ALL)) // FileTypeReq is MY or ALL  so this is if selecting from ALL
        {
          String strQuery = "";
          String sortType = "ASC";

          if (charSort == 'A')
          {
            sortType = "ASC";
          }else{
            sortType = "DESC";
          }

          strQuery =  // This query grabs ALL files
            "select 'FILE' as FileFolder,cvfl.Name as FolderName,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.folderid AS Parent from cvfile fil ,individual indv,cvfilefolder fld,cvfolder cvfl,filelistSearch fSearch, fileaccess fla where indv.individualid=fil.owner and cvfl.folderid=fld.folderid and fld.fileid=fil.fileid and fla.fileid=fil.fileid and fSearch.fileid=fil.fileid and fld.folderid != 6 order by '"
            + strSortMem
            + "' "
            + sortType;

          cvdl.setSqlQuery(strQuery);
          colList = cvdl.executeQuery();
          cvdl.setSqlQueryToNull();
        }else{
          // Selecting MY files.
          String strQuery = "";
          String sortType = "ASC";

          if (charSort == 'A')
          {
            sortType = "ASC";
          }else{
            sortType = "DESC";
          }

          strQuery = // This query gets all files where ownerid of the file = the IndividualId, for some reason this query has some
                     // differences from the ALL query above, these should probably be investigated.
            "select 'FILE' as FileFolder,cvfl.Name as FolderName,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY', fld.folderid AS Parent from cvfile fil ,individual indv,cvfilefolder fld,cvfolder cvfl,filelistSearch fSearch  where indv.individualid=fil.owner and fld.fileid=fil.fileid and fil.owner=? and cvfl.folderid=fld.folderid and fSearch.fileid=fil.fileid and fld.folderid != 6 order by '"
            + strSortMem
            + "' "
            + sortType;

          cvdl.setSqlQuery(strQuery);
          cvdl.setInt(1, individualId);
          colList = cvdl.executeQuery();
        }
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE filelistSearch");  // get rid of the no longer necessary temp table.
        cvdl.executeUpdate();
      }else{
        // There is no search to be done, so just getallfiles
        if (strFileTypeReq.equals(FileConstantKeys.MY) && folderID == -1)  // MY files and for somereason folderID == -1 ?
        {
          // I think this means include SYSTEM files/folders.
          // I think this is hardcoded to never go into this branch.
          // TODO: validate this is a dead branch and cull it.
          cvdl.setSql("file.getuserfiles");
          cvdl.setInt(1, individualId);
          cvdl.setInt(2, individualId);

          colList = cvdl.executeQuery();
        }else if (strFileTypeReq.equals(FileConstantKeys.ALL)){
          String sortType = "ASC";
          if (charSort == 'A')
          {
            sortType = "ASC";
          }else{
            sortType = "DESC";
          }

          cvdl.setDynamicQuery("file.allfilefolderssystem", sortType, strSortMem);

          cvdl.setInt(1, folderID);
          cvdl.setInt(2, folderID);
          colList = cvdl.executeQuery();
        }else if (strFileTypeReq.equals(FileConstantKeys.MY)){
          // So this is just MY files which are NOT in System Folders.
          String sortType = "ASC";
          if (charSort == 'A')
          {
            sortType = "ASC";
          }else{
            sortType = "DESC";
          }
          cvdl.setDynamicQuery("file.myfilefoldersnonsystem", sortType, strSortMem);
          cvdl.setInt(1, folderID);
          cvdl.setInt(2, individualId);
          cvdl.setInt(3, folderID);
          cvdl.setInt(4, individualId);

          colList = cvdl.executeQuery();
        }else if (strFileTypeReq.equals("INDV")){
          // get Individual Files ??? I thought it was MY or ALL.
          // TODO: this appears to be a dead branch.  I can't find references
          // in the code anywhere If you see the printout in code the remove
          // this task, otherwise we should cull the branch eventually
          cvdl.setSql("file.getindividualfiles");
          cvdl.setInt(1, individualId);
          cvdl.setInt(2, individualId);
          colList = cvdl.executeQuery();
        }
      }

      // Okay so we now have our results, clean up the access TEMP tables.
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("drop table folderaccess");
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("drop table fileaccess");
      cvdl.executeUpdate();
    }finally{
      cvdl.destroy();
      cvdl = null;
    }

    // Build the DisplayList
    Iterator it = colList.iterator();

    int i = 0;

    if (colList != null)
    {
      ModuleFieldRightMatrix mfrmx = CVUtility.getUserModuleRight("File", individualId, false, dataSource);
      while (it.hasNext())
      {
        i++;
        HashMap hm = (HashMap)it.next();
        int FolderID = ((Long)hm.get("ID")).intValue();

        try
        {
          // Often this code has hardcoded field auth values of 10 which is DELETE or full privileges.
          // FolderID is not a good name here.  It is actually the ID of the file/folder in the Element.
          IntMember intFolderID = new IntMember("ID", FolderID, 10, "", 'T', false, 10);
          StringMember strName = new StringMember("Name", (String)hm.get("Name"), 10, "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
          StringMember strDescription = null;

          if ((hm.get("Description") != null))
          {
            // If we have Description create the member, including the field rights.
            strDescription = new StringMember("Description", (String)hm.get("Description"), mfrmx.getFieldRight("File", "description"), "", 'T', false);
          }else{
            strDescription = new StringMember("Description", null, mfrmx.getFieldRight("File", "description"), "", 'T', false);
          }

          StringMember strCreatedBY = new StringMember("CreatedBy", (String)hm.get("Created BY"), 10, "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
          StringMember strFileFolder = new StringMember("FileFolder", (String)hm.get("FileFolder"), 10, "", 'T', false);
          IntMember intIndividualID = new IntMember("IndividualID", ((Long)hm.get("individualid")).intValue(), 10, "", 'T', false, 10);

          StringMember strFolderName = null;
          IntMember intParentFolderId = null;
          int parentFolderId = (hm.get("Parent") != null) ? ((Number)hm.get("Parent")).intValue() : 0;

          // Populate the folderName member correctly, if its an advanced search then we alredy have folderName, so just set it directly.
          if (strOriginal != null && strOriginal.startsWith("ADVANCE:"))
          {
            strFolderName = new StringMember("FolderName", (String)hm.get("FolderName"), 10, "", 'T', false);
            intParentFolderId = new IntMember("ParentFolderId", parentFolderId, 10, "", 'T', false, 10);  // So we can have the parentFolderId for the file
          }else if (folderID != -1){
            // If it wasn't advanced search, and we aren't a system folder then get the name from the folderVO, we have the ID.
            // BTW this is very inefficient, because we may do many many more queries and create folderVOs just to get the name, when we could have
            strFolderName = new StringMember("FolderName", (String) ((getFolder(individualId, folderID)).getName()), 10, "", 'T', false);
            intParentFolderId = new IntMember("ParentFolderId", parentFolderId, 10, "", 'T', false, 10);  // So we can have the parentFolderId for the file
            // If folderID and parentFolderId are not equivalent here, then we have some issues.
          }else if (folderID == -1){
            // If it is a System folder (system = TRUE in database)
            strFolderName = new StringMember("FolderName", null, 10, "", 'T', false);
            intParentFolderId = new IntMember("ParentFolderId", parentFolderId, 10, "", 'T', false, 10);
          }

          Timestamp dtCreated = null;
          Timestamp dtUpdated = null;
          DateMember dmCreated = null;
          DateMember dmUpdated = null;
          String timezoneid = "EST";

          if ((hm.get("Created") != null))
          {
            dtCreated = (Timestamp)hm.get("Created");
            dmCreated = new DateMember("Created", dtCreated, 10, "", 'T', false, 1, timezoneid);
          }else{
            dmCreated = new DateMember("Created", null, 10, "", 'T', false, 1, timezoneid);
          }

          if ((hm.get("Updated") != null))
          {
            dtUpdated = (Timestamp)hm.get("Updated");
            dmUpdated = new DateMember("Updated", dtUpdated, 10, "", 'T', false, 1, timezoneid);
          }else{
            dmUpdated = new DateMember("Updated", null, 10, "", 'T', false, 1, timezoneid);
          }

          StringMember title = null;
          if ((hm.get("NameTitle") != null))
          {
            title = new StringMember("NameTitle", (String)hm.get("NameTitle"), 10, "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
          }

          FileListElement filelistelement = new FileListElement(FolderID);

          filelistelement.put("FolderID", intFolderID);
          filelistelement.put("Name", strName);
          filelistelement.put("Description", strDescription);
          filelistelement.put("CreatedBy", strCreatedBY);
          filelistelement.put("Created", dmCreated);
          filelistelement.put("Updated", dmUpdated);
          filelistelement.put("FileFolder", strFileFolder);
          filelistelement.put("IndividualID", intIndividualID);
          filelistelement.put("FolderName", strFolderName);
          filelistelement.put("ParentID", intParentFolderId);
          filelistelement.put("NameTitle", title);

          // gah ???
          // What is wrong with using an Integer or a Long as the index.
          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);
          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
          String s4 = stringbuffer.toString();

          filelist.put(s4, filelistelement);
        }catch (Exception e){
          logger.error("[getAllFiles] Exception thrown.", e);
        }
      }   // end while(it.hasNext())
    }   // end if (colList != null)

    if (folderID != -1 && (strSearch == null || strSearch.equals("")))
    {
      Vector rootPath = getFolderRootPath(individualId, folderID);
      filelist.setDirectoryStructure(rootPath);
    }

    filelist.setTotalNoOfRecords(filelist.size());
    filelist.setListType("File");
    filelist.setBeginIndex(beginIndex);
    filelist.setFileTypeRequest(strFileTypeReq);
    filelist.setEndIndex(filelist.size());

    return filelist;
  }   // end getAllFiles(int,HashMap) method

  /**
   * Adds a new folder
   */
  public int addFolder(int individualId, CvFolderVO fdvo) throws CvFileException, AuthorizationFailedException
  {

    if (!CVUtility.isModuleVisible("File", individualId, this.dataSource)) {
      throw new AuthorizationFailedException("File - addFolder");
    }
    int folderID = 0;
    try {
      if (fdvo == null) {
        throw new CvFileException(CvFileException.INVALID_DATA, "Cannot add folder. FolderVo is empty.");
      }

      if (fdvo.getName() == null || fdvo.getName().length() == 0) {
        throw new CvFileException(CvFileException.INVALID_DATA, "Name is Empty");
      }

      if (fdvo.getIsSystem() == null) {
        fdvo.setIsSystem(CvFolderVO.FS_SYSTEM_NO);
      }

      if (fdvo.getVisibility() == null) {
        fdvo.setVisibility(CvFolderVO.FDV_PRIVATE);
      }

      if (fdvo.getOwner() == 0) {
        fdvo.setOwner(individualId);
      }

      // allow attaching to root here. will be used in admin
      if (fdvo.getParent() <= 0) {
        fdvo.setParent(0); // root folder
      } else if (!folderExist(fdvo.getParent())) {
        throw new CvFileException(CvFileException.PARENT_NOT_FOUND, "Parent Not found for new folder: Parent id = " + fdvo.getParent());
      }

      if (fdvo.getLocationId() == 0) {
        fdvo.setLocationId(DEFAULT_LOCATION);
      }

      CVDal cvdl = new CVDal(dataSource);
      try {
        cvdl.setSql("file.insertfolder");
        cvdl.setString(1, fdvo.getName());
        cvdl.setString(2, fdvo.getDescription());
        cvdl.setInt(3, fdvo.getParent());
        cvdl.setInt(4, fdvo.getLocationId());
        cvdl.setInt(5, fdvo.getOwner());
        cvdl.setString(6, fdvo.getIsSystem());
        cvdl.setString(7, fdvo.getVisibility());
        cvdl.setInt(8, individualId);
        cvdl.setInt(9, individualId);
        cvdl.executeUpdate();
        folderID = cvdl.getAutoGeneratedKey();
      } finally {
        cvdl.destroy();
        cvdl = null;
      }
      // call the authorization EJB to set the record public if warranted
      if (fdvo.getVisibility().equals("PUBLIC")) {
        InitialContext ic = CVUtility.getInitialContext();
        AuthorizationLocalHome home = (AuthorizationLocalHome)ic.lookup("local/Authorization");
        AuthorizationLocal remote = home.create();
        remote.setDataSource(dataSource);
        remote.setRecordToPublic("76", folderID);  // 76 is the folder "module" id.
      }
    } catch (Exception e) {
      logger.error("[addFolder] Exception thrown.", e);
      throw new CvFileException(CvFileException.INSERT_FAILED, "Failed in file ejb while adding folder");
    }
    return folderID;
  } // end addFolder() method


  /**
   * checks whether folder exists
   * 
   * @param folderId
   * @return
   * @exception CvFileException
   */
  public boolean folderExist(int folderId) throws CvFileException
  {
    CVDal cvdl = new CVDal(dataSource);
    Collection col = null;
    try {
      cvdl.setSql("file.getfolder");
      cvdl.setInt(1, folderId);
      col = cvdl.executeQuery();
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    if (col == null)
      return false;
    else
      return true;
  }

  /**
   * Get the the home folder based on individualId (owner)
   * @param individualId The UserID (owner) of the folder
   * @return A CvFolderVO object with the necessary details.
   * @exception CvFileException
   */
  public CvFolderVO getHomeFolder(int userID) throws CvFileException, AuthorizationFailedException
  {
    int homeFolderID = getHomeFolderID(userID);
    return this.getFolder(userID, homeFolderID);
  }

  private int getHomeFolderID(int userID) throws CvFileException
  {
    int homeFolderID;
    CVDal cvdl = new CVDal(this.dataSource);

    try
    {
      cvdl.setSql("file.gethomefolder");
      cvdl.setInt(1,userID);
      Collection col = cvdl.executeQuery();

      if (col == null)
      {
        throw new CvFileException(CvFileException.GET_FAILED,"Could not find home folder for owner : " + userID);

      }
      Iterator it = col.iterator();

      HashMap hm = (HashMap)it.next();
      homeFolderID = Integer.parseInt(((String)hm.get("HomeFolderID")));
    }catch(Exception e){
      logger.error("[getHomeFolderID] Exception thrown.  for userId = "+userID, e);
      throw new CvFileException(CvFileException.GET_FAILED,"Could not find home folder for user: " + userID);
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    return homeFolderID;
  }   // end getHomeFolderID() method


  /**
   * Det the details of the folder
   * @param individualId The UserID (owner) of the folder.
   * @param folderId  The Folder ID.
   * @return A CvFolderVO object with the necessary details.
   * @throws CvFileException
   */
  public CvFolderVO getFolder(int individualId, int folderId) throws CvFileException, AuthorizationFailedException
  {
    // I've added this check to see if the individualID is
    // deemed a System-wide operation. Kevin and I have
    // decided that individualID -13 will be used for system-wide
    // operations. -1 is a bad option because it's popular for
    // Java applications to return -1 (for an int) if an error.
    if (individualId != -13)
    {
      if (!CVUtility.canPerformRecordOperation(individualId,"CVFolder",folderId,ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource))
      {
        throw new AuthorizationFailedException("File - getFolder");
      }
    }

    CvFolderVO fdvo;

    CVDal cvdl = new CVDal(this.dataSource);

    try
    {
      cvdl.setSql("file.getfolder");
      cvdl.setInt(1,folderId);
      Collection col = cvdl.executeQuery();

      if (col == null)
      {
        throw new CvFileException(CvFileException.GET_FAILED,"Could not find Folder: " + folderId);
      }

      Iterator it = col.iterator();

      if (! it.hasNext())
      {
        throw new CvFileException(CvFileException.GET_FAILED,"Could not find Folder: " + folderId);
      }

      HashMap hm = (HashMap)it.next();

      fdvo = new CvFolderVO();
      fdvo.setFolderId(((Long)hm.get("FolderID")).intValue());
      fdvo.setName((String)hm.get("Name"));
      fdvo.setDescription((String)hm.get("Description"));
      fdvo.setParent(((Long)hm.get("Parent")).intValue());

      if (hm.get("LocationID")!= null)
      {
        fdvo.setLocationId(((Long)hm.get("LocationID")).intValue());
      }

      if (hm.get("Detail")!= null)
      {
        fdvo.setLocationName((String)hm.get("Detail"));
      }

      fdvo.setFullPathVec(getFolderRootPath(individualId,folderId));

      if (hm.get("IsSystem")!= null)
      {
        fdvo.setIsSystem((String)hm.get("IsSystem"));
      }

      if (hm.get("visibility")!= null)
      {
        fdvo.setVisibility((String)hm.get("visibility"));
      }

      if (hm.get("owner")!= null)
      {
        fdvo.setOwner(((Long)hm.get("owner")).intValue());
      }

      if (hm.get("CreatedBy")!= null)
      {
        fdvo.setCreatedBy(((Long)hm.get("CreatedBy")).intValue());
      }

      if (hm.get("ModifiedBy")!= null)
      {
        fdvo.setModifiedBy(((Long)hm.get("ModifiedBy")).intValue());
      }

      fdvo.setCreatedOn((Timestamp)hm.get("CreatedOn"));
      fdvo.setModifiedOn((Timestamp)hm.get("ModifiedOn"));
      fdvo.fillAuditDetails(this.dataSource);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return fdvo;
  } //end of getFolder method

  /**
   * Updates the details of the folder
   *
   * @param   individualId
   * @param   fdvo
   * @exception   CvFileException
   */
  public void updateFolder(int individualId, CvFolderVO fdvo) throws CvFileException, AuthorizationFailedException
  {
    if(!CVUtility.canPerformRecordOperation(individualId,"CVFolder",fdvo.getFolderId(),ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource) ) {
      throw new AuthorizationFailedException("File - updateFolder");
    }
    CVDal cvdl = new CVDal(dataSource);
    try
    {
      if (fdvo == null) {
        throw new CvFileException(CvFileException.INVALID_DATA,"Cannot update folder. FolderVo is empty.");
      }
      if (fdvo.getFolderId() <= 7) {
        // if we are trying to update one of the system folders, don't.
        logger.error("Trying to update folder id: "+fdvo.getFolderId()+", named: "+fdvo.getName());
        throw new CvFileException(CvFileException.INVALID_DATA, "Can't update folder with ID less than 7.");
      }
      if(fdvo.getName() == null || fdvo.getName().length() == 0)
        throw new CvFileException(CvFileException.INVALID_DATA,"Name is Empty");
      if (fdvo.getParent() <= 0)
        fdvo.setParent(0); // root folder
      else if(!folderExist(fdvo.getParent()))
        throw new CvFileException(CvFileException.PARENT_NOT_FOUND,"Parent Not found for update folder: Parent id = " + fdvo.getParent());
      cvdl.setSql("file.updatefolder");
      cvdl.setString(1,fdvo.getName());
      cvdl.setString(2,fdvo.getDescription());
      cvdl.setInt(3,fdvo.getParent());
      cvdl.setInt(4,individualId);
      cvdl.setString(5,fdvo.getVisibility());
      cvdl.setInt(6,fdvo.getFolderId());
      int voOwner = fdvo.getOwner();
      if (voOwner != 0) {
        cvdl.setInt(7,voOwner);
      } else {
        cvdl.setInt(7,individualId);
      }
      cvdl.executeUpdate();
      // call the authorization EJB to set the record public if warranted
      if (fdvo.getVisibility().equals("PUBLIC")) {
        InitialContext ic = CVUtility.getInitialContext();
        AuthorizationLocalHome home = (AuthorizationLocalHome)ic.lookup("local/Authorization");
        AuthorizationLocal remote = home.create();
        remote.setDataSource(dataSource);
        remote.setRecordToPublic("76", fdvo.getFolderId());  // 76 is the folder "module" id.
      }
    } catch (CvFileException fe) {
      // just rethrow
      throw fe;
    } catch(Exception e) {
      logger.error("[updateFolder] Exception thrown.", e);
      throw new CvFileException(CvFileException.INSERT_FAILED,"Failed in file ejb while updating folder");
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  }


//File Methods

  /**
   * Adds a new file
   * @param   individualId
   * @param   folderId
   * @param   flvo
   * @exception   CvFileException
   */
  public int addFile(int individualId, int folderId, CvFileVO flvo) throws CvFileException, AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("File", individualId, this.dataSource)) {
      throw new AuthorizationFailedException("File - addFile");
    }
    int fileId = 0;
    CVDal cvdl = new CVDal(dataSource);
    try {
      if (flvo == null) {
        throw new CvFileException(CvFileException.INVALID_DATA, "Cannot add file. File is empty.");
      }
      if (flvo.getName() == null || flvo.getName().length() == 0) {
        throw new CvFileException(CvFileException.INVALID_DATA, "File Name is Empty");
      }
      if (!folderExist(folderId)) {
        throw new CvFileException(CvFileException.PARENT_NOT_FOUND, "Folder Not found for new File: " + folderId);
      }
      if (flvo.getVersion() == null) {
        flvo.setVersion(CvFileVO.DEFAULT_VERSION);
      }
      if (flvo.getVisibility() == null) {
        flvo.setVisibility(CvFileVO.FV_PRIVATE);
      }
      if (flvo.getStatus() == null) {
        flvo.setStatus(CvFileVO.FS_DRAFT);
      }
      if (flvo.getPhysical() == null || flvo.getPhysical() != CvFileVO.FP_PHYSICAL || flvo.getPhysical() != CvFileVO.FP_VIRTUAL) {
        flvo.setPhysical(CvFileVO.FP_PHYSICAL);
      }
      if (flvo.getIsTemporary() == null) {
        flvo.setIsTemporary(CvFileVO.FIT_NO);
      }
      if (flvo.getCustomerView() == null) {
        flvo.setCustomerView(CvFileVO.FCV_NO);
      }
      cvdl.setSql("file.insertfile");
      cvdl.setString(1, flvo.getTitle());
      cvdl.setString(2, flvo.getDescription());
      cvdl.setString(3, flvo.getName());
      cvdl.setInt(4, flvo.getOwner());
      cvdl.setFloat(5, flvo.getFileSize());
      cvdl.setString(6, flvo.getVersion());
      cvdl.setString(7, flvo.getStatus());
      cvdl.setString(8, flvo.getVisibility());
      cvdl.setInt(9, flvo.getAuthorId());
      cvdl.setInt(10, flvo.getRelateEntity());
      cvdl.setInt(11, flvo.getRelateIndividual());
      cvdl.setString(12, flvo.getIsTemporary());
      cvdl.setString(13, flvo.getCustomerView());
      cvdl.setInt(14, flvo.getCreatedBy());
      cvdl.setInt(15, flvo.getCreatedBy());
      cvdl.executeUpdate();

      fileId = cvdl.getAutoGeneratedKey();

      cvdl.setSqlQueryToNull();
      cvdl.setSql("file.insertfilefolder");
      cvdl.setInt(1, fileId);
      cvdl.setInt(2, folderId);
      cvdl.setString(3, flvo.getPhysical());
      cvdl.executeUpdate();

      if (flvo.getCompanyNews() != null && flvo.getCompanyNews().equals("YES")) {
        cvdl.setSqlQueryToNull();
        cvdl.setSql("companynews.insertnews");
        cvdl.setInt(1, fileId);
        cvdl.setRealTimestamp(2, flvo.getFrom());
        cvdl.setRealTimestamp(3, flvo.getTo());
        cvdl.executeUpdate();
      }
      this.addFileLink(fileId, flvo, cvdl);
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
      AuthorizationLocal authorizationLocal = authorizationHome.create();
      authorizationLocal.setDataSource(dataSource);
      authorizationLocal.saveCurrentDefaultPermission("File", fileId, individualId);
    } catch (Exception e) {
      logger.error("[addFile] Exception thrown.", e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return fileId;
  } // end addFile() method

  /**
   * Returns a CvFileVO with the details of the requested fileId.
   * @param individualId
   * @param fileId
   * @return
   * @exception CvFileException
   */
  public CvFileVO getFile(int individualId, int fileId) throws CvFileException, AuthorizationFailedException
  {
    boolean userAuthorized = true;
    // I've added this check to see if the individualID is
    // deemed a System-wide operation. Kevin and I have
    // decided that individualID -13 will be used for system-wide
    // operations. -1 is a bad option because it's popular for
    // Java applications to return -1 (for an int) if an error.
    if (individualId != -13) {
      if (!CVUtility.canPerformRecordOperation(individualId, "File", fileId, ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource)) {
        // we can't throw an Auth failed exception yet, because canPerformRecordOperation
        // doesn't know if this file was marked as "Public" (on the record itself, not in
        // the publicrecords table) by the owner. So let's set a flag and check later after
        // we get the file info from the DB...
        userAuthorized = false;
      }
    }

    CvFileVO flvo = new CvFileVO();

    CVDal cvdl = new CVDal(dataSource);
    
    try {
      cvdl.setSql("file.getfile");
      cvdl.setInt(1, fileId);
      Collection col = cvdl.executeQuery();
      cvdl.setSqlQueryToNull();

      cvdl.setSql("companynews.getnew");
      cvdl.setInt(1, fileId);
      Collection ncol = cvdl.executeQuery();
      Iterator nit = ncol.iterator();

      if (!nit.hasNext()) {
        flvo.setCompanyNews("NO");
      } else {
        flvo.setCompanyNews("YES");
        HashMap nhm = (HashMap)nit.next();
        flvo.setFrom(((Timestamp)nhm.get("DateFrom")));
        flvo.setTo(((Timestamp)nhm.get("DateTo")));
      }
      
      // gets all records for virtual links only (referencetype=VIRTUAL)
      cvdl.setSqlQueryToNull();
      
      cvdl.setSql("file.getvirtualfilefolder");
      cvdl.setInt(1, fileId);
      Collection ffcol = cvdl.executeQuery();

      if (col == null) {
        throw new CvFileException(CvFileException.GET_FAILED, "Could not find File : " + fileId);
      }

      Iterator it = col.iterator();

      if (!it.hasNext()) {
        throw new CvFileException(CvFileException.GET_FAILED, "Could not find File : " + fileId);
      }

      HashMap hm = (HashMap)it.next();

      String visibility = (String)hm.get("visibility");
      if (! userAuthorized && (visibility == null || !visibility.equals("PUBLIC"))) {
        // if the record is not public, AND the authorization check
        // failed at the top of this method, NOW throw an AuthFailed exception
        throw new AuthorizationFailedException("File - getFile");
      }
      
      flvo.setFileId(((Number)hm.get("FileID")).intValue());
      flvo.setTitle((String)hm.get("Title"));
      flvo.setName((String)hm.get("Name"));
      flvo.setDescription((String)hm.get("Description"));
      
      if (hm.get("Owner") != null) {
        flvo.setOwner(((Number)hm.get("Owner")).intValue());
      }

      if (hm.get("FileSize") != null) {
        flvo.setFileSize(Float.parseFloat((hm.get("FileSize")).toString()));
      }

      flvo.setVersion((String)hm.get("Version"));
      flvo.setStatus((String)hm.get("Status"));
      flvo.setVisibility((String)hm.get("visibility"));
      flvo.setPhysical((String)hm.get("referencetype"));
      flvo.setCustomerView((String)hm.get("CustomerView"));

      if (hm.get("Author") != null) {
        flvo.setAuthorId(((Number)hm.get("Author")).intValue());
      }

      int folderId = ((Number)hm.get("FolderID")).intValue();

      try {
        flvo.setPhysicalFolderVO(getFolder(-13, folderId));   // -13 == system user
      } catch (AuthorizationFailedException afe) {
        // don't worry about the AFE here.. we're allowed to show the
        // user which folder the file is in if they're allowed to view
        // this file, which we've already checked for.
      }
      flvo.setPhysicalFolder(folderId);

      if (hm.get("Creator") != null) {
        flvo.setCreatedBy(((Number)hm.get("Creator")).intValue());
      }

      flvo.setCreatedOn((Timestamp)hm.get("Created"));

      if (hm.get("UpdatedBy") != null) {
        flvo.setModifiedBy(((Number)hm.get("UpdatedBy")).intValue());
      }

      flvo.setModifiedOn((Timestamp)hm.get("Updated"));
      flvo.fillAuditDetails(this.dataSource);

      if (hm.get("RelateEntity") != null) {
        flvo.setRelateEntity(((Number)hm.get("RelateEntity")).intValue());
      }

      if (hm.get("RelateIndividual") != null) {
        flvo.setRelateIndividual(((Number)hm.get("RelateIndividual")).intValue());
      }

      //Get Relations
      InitialContext ic = CVUtility.getInitialContext();

      //for Author
      if (flvo.getAuthorId() > 0) {
        try {
          //InitialContext ic = CVUtility.getInitialContext();
          IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");
          IndividualLocal remote = home.findByPrimaryKey(new IndividualPK(flvo.getAuthorId(), this.dataSource));
          flvo.setAuthorVO(remote.getIndividualVOBasic());
        } catch (Exception e) {
          // do nothing if not found
        }
      }

      //for RelateEntity
      if (flvo.getRelateEntity() > 0) {
        try {
          EntityLocalHome home = (EntityLocalHome)ic.lookup("local/Entity");
          EntityLocal remote = home.findByPrimaryKey(new EntityPK(flvo.getRelateEntity(), this.dataSource));
          flvo.setRelateEntityVO(remote.getEntityVOBasic());
        } catch (Exception e) {
          // do nothing if not found
        }
      }

      //for RelateIndividual
      if (flvo.getRelateIndividual() > 0) {
        try {
          IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");
          IndividualLocal remote = home.findByPrimaryKey(new IndividualPK(flvo.getRelateIndividual(), this.dataSource));
          flvo.setRelateIndividualVO(remote.getIndividualVOBasic());
        } catch (Exception e) {
          // do nothing if not found
        }
      }

      this.getFileLink(fileId, flvo, cvdl, individualId);

      // virtual file folder relation
      if (ffcol != null) {
        it = ffcol.iterator();
        while (it.hasNext()) {
          CvFolderVO fdvo = new CvFolderVO();
          HashMap fdhm = (HashMap)it.next();
          int vFolderId = ((Long)fdhm.get("FolderID")).intValue();
          flvo.setVirtualFolderVO(getFolder(individualId, vFolderId));
        }
      }
    } catch (Exception e) {
      logger.error("[getFile] Exception thrown.", e);
      e.printStackTrace();
      throw new CvFileException(CvFileException.INSERT_FAILED, "Failed in file ejb while adding File");
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return flvo;
  }   // end getFile() method



  /**
   * gets the details of the file. Is very lightweight. does not fill references
   * like physicalvo, createdby,modifiedby,ownervo virtual folder vo etc.
   * 
   * @param individualId
   * @param fileId
   * @return
   * @exception CvFileException
   */
  public CvFileVO getFileBasic(int individualId, int fileId) throws CvFileException, AuthorizationFailedException
  {
    if (! CVUtility.canPerformRecordOperation(individualId,"File", fileId, ModuleFieldRightMatrix.VIEW_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException("File - getFileBasic");
    }
    
    CvFileVO flvo;
    CVDal cvdl = new CVDal(dataSource);
    
    try {
      cvdl.setSql("file.getfile");
      cvdl.setInt(1,fileId);
      Collection col = cvdl.executeQuery();
      
      if (col == null) {
        throw new CvFileException(CvFileException.GET_FAILED,"Could not find File : " + fileId);
      }

      Iterator it = col.iterator();

      if (! it.hasNext()) {
        throw new CvFileException(CvFileException.GET_FAILED,"Could not find File : " + fileId);
      }

      HashMap hm = (HashMap)it.next();

      flvo = new CvFileVO();

      flvo.setFileId(((Long)hm.get("FileID")).intValue());
      flvo.setTitle((String)hm.get("Title"));
      flvo.setName((String)hm.get("Name"));
      flvo.setDescription((String)hm.get("Description"));
      
      if (hm.get("Owner") != null) {
        flvo.setOwner(((Long)hm.get("Owner")).intValue());
      }

      if (hm.get("FileSize") != null) {
        flvo.setFileSize(((Number)hm.get("FileSize")).floatValue());
      }

      flvo.setVersion((String)hm.get("Version"));
      flvo.setStatus((String)hm.get("Status"));
      flvo.setVisibility((String)hm.get("visibility"));
      flvo.setPhysical((String)hm.get("referencetype"));

      if (hm.get("Author") != null) {
        flvo.setAuthorId(((Long)hm.get("Author")).intValue());
      }
      
      int folderId = ((Long)hm.get("FolderID")).intValue();
      flvo.setPhysicalFolderVO(getFolder(individualId,folderId));
      
      if (hm.get("Creator") != null) {
        flvo.setCreatedBy(((Long)hm.get("Creator")).intValue());
      }
      
      flvo.setCreatedOn((Timestamp)hm.get("Created"));
      
      if (hm.get("UpdatedBy") != null) {
        flvo.setModifiedBy(((Long)hm.get("UpdatedBy")).intValue());
      }
      
      flvo.setCreatedOn((Timestamp)hm.get("Updated"));
      
      if (hm.get("RelateEntity") != null) {
        flvo.setRelateEntity(((Long)hm.get("RelateEntity")).intValue());
      }
      
      if (hm.get("RelateIndividual") != null) {
        flvo.setRelateIndividual(((Long)hm.get("RelateIndividual")).intValue());
      }

    } catch(Exception e) {
      logger.error("[getFileBasic] Exception thrown.", e);
      throw new CvFileException(CvFileException.INSERT_FAILED,"Failed in file ejb while adding File");
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return flvo;
  }



  /**
   * update values of the file
   * 
   * 
   * @param individualId
   * @param virtualFolderId
   * @param flvo
   * @exception CvFileException
   */
  public void updateFile(int individualId, int[] virtualFolderId, CvFileVO flvo) throws CvFileException, AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(individualId, "File", flvo.getFileId(), ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("File - updateFile");

    CVDal cvdl = new CVDal(dataSource);
    try {

      if (flvo == null)
        throw new CvFileException(CvFileException.INVALID_DATA, "Cannot update file. File is empty");

      if (flvo.getName() == null || flvo.getName().length() == 0)
        throw new CvFileException(CvFileException.INVALID_DATA, "File Name is Empty");

      if (flvo.getPhysicalFolder() == 0)
        throw new CvFileException(CvFileException.INVALID_DATA, "Physical location not set. File cannot be copied to the root");

      if (virtualFolderId != null) {
        for (int i = 0; i < virtualFolderId.length; i++) {
          // vextra check to see if folders exist.
          // dont add for folders which do not exist,
          // add for other
          if (!folderExist(virtualFolderId[i]))
            virtualFolderId[i] = 0; // do not add to root
        }
      }

      if (flvo.getVersion() == null)
        flvo.setVersion(CvFileVO.DEFAULT_VERSION);

      if (flvo.getVisibility() == null)
        flvo.setVisibility(CvFileVO.FV_PRIVATE);

      if (flvo.getStatus() == null)
        flvo.setStatus(CvFileVO.FS_DRAFT);

      cvdl.setSql("file.updatefile");
      cvdl.setString(1, flvo.getTitle());
      cvdl.setString(2, flvo.getDescription());
      cvdl.setString(3, flvo.getName());
      cvdl.setFloat(4, flvo.getFileSize());
      cvdl.setString(5, flvo.getVersion());
      cvdl.setString(6, flvo.getStatus());
      cvdl.setString(7, flvo.getVisibility());
      cvdl.setInt(8, flvo.getAuthorId());
      cvdl.setInt(9, flvo.getModifiedBy());
      cvdl.setInt(10, flvo.getRelateEntity());
      cvdl.setInt(11, flvo.getRelateIndividual());
      cvdl.setString(12, flvo.getCustomerView());
      cvdl.setInt(13, flvo.getFileId());
      cvdl.executeUpdate();

      if (flvo.getCompanyNews() != null && flvo.getCompanyNews().equals("YES")) {
        cvdl.setSqlQueryToNull();
        cvdl.setSql("companynews.getnews");
        Collection ncol = cvdl.executeQuery();
        Iterator nit = ncol.iterator();
        if (nit.hasNext()) {
          cvdl.setSqlQueryToNull();
          cvdl.setSql("companynews.updatenews");
          cvdl.setRealTimestamp(1, flvo.getFrom());
          cvdl.setRealTimestamp(2, flvo.getTo());
          cvdl.setInt(3, flvo.getFileId());
          cvdl.executeUpdate();
        } else {
          cvdl.setSqlQueryToNull();
          cvdl.setSql("companynews.insertnews");
          cvdl.setInt(1, flvo.getFileId());
          cvdl.setRealTimestamp(2, flvo.getFrom());
          cvdl.setRealTimestamp(3, flvo.getTo());
          cvdl.executeUpdate();
        }
      } else {
        cvdl.setSqlQueryToNull();
        cvdl.setSql("companynews.deletenews");
        cvdl.setInt(1, flvo.getFileId());
        cvdl.executeUpdate();
      }

      cvdl.setSqlQueryToNull();
      insertFileFolder(flvo.getFileId(), flvo.getPhysicalFolder(), virtualFolderId, cvdl);

      this.addFileLink(flvo.getFileId(), flvo, cvdl);
    } catch (Exception e) {
      logger.error("[updateFile] Exception thrown.", e);
      throw new CvFileException(CvFileException.INSERT_FAILED, "Failed in file ejb while adding File");
    } finally {
      cvdl.destroy();
      cvdl = null;
    }

  }



  /**
   * 
   * 
   * @param fileId
   * @param physicalFolderId
   * @param VirtualFolderId
   * @param cvdl
   * @return boolean
   */
  private boolean insertFileFolder(int fileId, int physicalFolderId, int[] VirtualFolderId,CVDal cvdl)
  {
    try
    {
      // delete all folders and then add again
      cvdl.setSqlQueryToNull();
      cvdl.setSql("file.deleteallfilefolder");
      cvdl.setInt(1,fileId);
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();

      cvdl.setSql("file.insertfilefolder");

      if (physicalFolderId > 0)
      {
        cvdl.setInt(1,fileId);
        cvdl.setInt(2,physicalFolderId);
        cvdl.setString(3,CvFileVO.FP_PHYSICAL);
        cvdl.executeUpdate();
      }

      if (VirtualFolderId != null)
      {
        for(int i=0;i<VirtualFolderId.length;i++)
        {
          if (VirtualFolderId[i] > 0) // do not add to root
          {
            cvdl.clearParameters();
            cvdl.setInt(1,fileId);
            cvdl.setInt(2,VirtualFolderId[i]);
            cvdl.setString(3,CvFileVO.FP_VIRTUAL);
            cvdl.executeUpdate();
          }
        }
      }
    } catch(Exception e) {
      logger.error("[insertFileFolder] Exception thrown.", e);
      return false;
    }
    return true;
  }


  /**
   * This method deletes the folder and its subfolders and file
   *
   * @param   individualId
   * @param   folderId
   */
  public void deleteFolder(int individualId, int folderId) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(individualId, "CVFolder", folderId, ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException("File - deleteFolder");
    }
    CVDal dl = new CVDal(dataSource);
    try {
      removeFolderFiles(individualId, folderId, dl);// removes all the files of
                                                    // that folder
      HashMap hmFolder = new HashMap();
      hmFolder.put("folderid", (new Integer(folderId)).toString());
      sendToAttic(individualId, transactionID, "cvfolder", hmFolder);
      dl.setSqlQueryToNull();
      dl.setSql("file.deletefilefolder"); //deletes references of folder from
                                          // cvfilefolder table
      dl.setInt(1, folderId);
      dl.executeUpdate();
      dl.setSqlQueryToNull();
      dl.setSql("file.getsubfolders");//gets all the subfolders of current folder
      dl.setInt(1, folderId);
      Collection col = dl.executeQuery();
      Iterator iter = col.iterator();
      while (iter.hasNext()) {
        HashMap hm = (HashMap)iter.next();
        int curFolderID = ((Long)hm.get("folderid")).intValue();//get the ID of
                                                                // the sub-folder
        deleteFolder(individualId, curFolderID);//recursive function
      }
      // deletes the actual folder
      dl.setSqlQueryToNull();
      dl.setSql("file.deletefolder");
      dl.setInt(1, folderId);
      dl.executeUpdate();
      // call the authorization EJB to set the record public if warranted
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome home = (AuthorizationLocalHome)ic.lookup("local/Authorization");
      AuthorizationLocal remote = home.create();
      remote.setDataSource(dataSource);
      remote.deleteRecordFromPublic("76", folderId);
    } catch (Exception e) {
      logger.error("[deleteFolder] Exception thrown.", e);
      throw new EJBException(e);
    } finally {
      dl.destroy();
    }
  }

  public void deleteFile(int individualId, int fileId,int currFolderID) throws  AuthorizationFailedException
  {
    if(!CVUtility.canPerformRecordOperation(individualId,"File",fileId,ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource) )
      throw new AuthorizationFailedException("File - deleteFile");

    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("file.getfile");
      dl.setInt(1,fileId);
      Collection colFile=dl.executeQuery();
  
  
      String strTitle="";
      int intOwner=0;
  
      if (colFile.size()>0)
      {
        Iterator iter=colFile.iterator();
  
        while (iter.hasNext())
        {
          HashMap hm=(HashMap)iter.next();
  
          if(hm.get("Title")!=null)
            strTitle=(String)hm.get("Title");
  
          if(hm.get("Owner")!=null)
            intOwner=((Long)hm.get("Owner")).intValue();
        }
      }
  
      HashMap hmDetails=new HashMap();
      hmDetails.put("title",strTitle);
      hmDetails.put("owner",new Integer(intOwner));
      hmDetails.put("module",new Integer(6));
      hmDetails.put("recordtype",new Integer(33));
  
      transactionID=getAtticTransactionID(individualId,Constants.CV_ATTIC,hmDetails);
  
      HashMap hmfile=new HashMap();
      hmfile.put("fileid",(new Integer(fileId)).toString());
      sendToAttic(individualId,transactionID,"cvfile",hmfile);
  
      dl.setSqlQueryToNull();
      dl.setSql( "file.deletephysicalfile"); //deletes physical file
      dl.setInt(1,fileId);
      dl.executeUpdate();
  
      HashMap hmfilefolder=new HashMap();
      hmfilefolder.put("fileid",(new Integer(fileId)).toString());
      hmfilefolder.put("folderid",(new Integer(currFolderID)).toString());
      sendToAttic(individualId,transactionID,"cvfilefolder",hmfilefolder);
  
      dl.setSqlQueryToNull();
      dl.setSql( "file.deleteallfilefolder"); //deletes entry from CvFileFolder Table
      dl.setInt(1,fileId);
      dl.executeUpdate();
      this.deleteFileLink(fileId,dl);
    } finally {
      dl.destroy();
    }
  }

  /**
   * This method removes the entries from CvFileFolder Table
   *
   * @param   folderId
   * @param   dl
   */
  private void removeFolderFiles(int userID, int folderId, CVDal dl) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userID, "CVFolder", folderId, ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("File - removeFolderFiles");
    dl.setSqlQueryToNull();
    dl.setSql("file.selectfoldlerfiles");
    dl.setInt(1, folderId);
    Collection col = dl.executeQuery();
    Iterator iter = col.iterator();
    String referenceType = "";
    int fileId = 0;
    dl.setSqlQueryToNull();
    dl.setSql("file.getfolder");
    dl.setInt(1, folderId);
    Collection colFolder = dl.executeQuery();
    String strTitle = "";
    int intOwner = 0;
    if (colFolder.size() > 0) {
      Iterator iterFol = colFolder.iterator();
      while (iterFol.hasNext()) {
        HashMap hm = (HashMap)iterFol.next();
        if (hm.get("Description") != null)
          strTitle = (String)hm.get("Description");
        if (hm.get("owner") != null)
          intOwner = ((Long)hm.get("owner")).intValue();
      }
    }

    HashMap hmDetails = new HashMap();
    hmDetails.put("title", strTitle);
    hmDetails.put("owner", new Integer(intOwner));
    hmDetails.put("module", new Integer(6));
    hmDetails.put("recordtype", new Integer(36));

    transactionID = getAtticTransactionID(userID, Constants.CV_ATTIC, hmDetails);

    while (iter.hasNext()) {
      HashMap hm = (HashMap)iter.next();
      referenceType = ((String)hm.get("referenceType"));
      fileId = ((Long)hm.get("fileid")).intValue();

      if ((referenceType != null) && (referenceType.equals(CvFileVO.FP_PHYSICAL))) {
        HashMap hmfilefolder = new HashMap();
        hmfilefolder.put("fileid", (new Integer(fileId)).toString());
        hmfilefolder.put("folderid", (new Integer(folderId)).toString());
        sendToAttic(userID, transactionID, "cvfilefolder", hmfilefolder);

        dl.setSqlQueryToNull();
        dl.setSql("file.deleteallvirtualfiles");
        dl.setInt(1, fileId);
        dl.executeUpdate();

        HashMap hmfile = new HashMap();
        hmfile.put("fileid", (new Integer(fileId)).toString());
        sendToAttic(userID, transactionID, "cvfile", hmfile);

        dl.setSqlQueryToNull();
        dl.setSql("file.deletephysicalfile");
        dl.setInt(1, fileId);
        dl.executeUpdate();
      } else if ((referenceType != null) && (referenceType.equals(CvFileVO.FP_VIRTUAL))) {
        HashMap hmfilefolder = new HashMap();
        hmfilefolder.put("fileid", (new Integer(fileId)).toString());
        hmfilefolder.put("folderid", (new Integer(folderId)).toString());
        sendToAttic(userID, transactionID, "cvfilefolder", hmfilefolder);
        dl.setSqlQueryToNull();
        dl.setSql("file.deletevirtualfile");
        dl.setInt(1, fileId);
        dl.setInt(2, folderId);
        dl.executeUpdate();
      }
    }
  }

  /**
   * 
   * This method returns All The Folders of This userID
   * Except the public folders
   * @param individualId
   * @return ArrayList object (arrlistFolders)
   */
  public ArrayList getUserFolders(int individualId)
  {
    CVDal dl = new CVDal(dataSource);
    ArrayList arrlistFolders = new ArrayList();
    Integer publicFolderId = this.getPublicFolderId();
    try {
      // selects all the folders owned by individual
      // EXCEPT folders where the public folder is the
      // parent
      // FIXME make this search up the parent hierarchy to exclude all
      // subfolders of "public folders".  OR remove that check.
      // My Folders can be all of your folders even public ones (confusing)
      dl.setSql("file.getuserfolders"); 
      dl.setInt(1, individualId);
      dl.setInt(2, publicFolderId.intValue());
      Collection colID = dl.executeQuery();
      Iterator it = colID.iterator();
      int ID = 0;
      String name = "";
      int parent = 0;
      String parentname = "";
      while (it.hasNext()) {
        CvFolderVO fldVO = new CvFolderVO();
        HashMap hm = (HashMap)it.next();
        ID = ((Long)hm.get("folderid")).intValue();
        name = (String)hm.get("name");
        parent = ((Long)hm.get("parent")).intValue();
        parentname = ((String)hm.get("parentname"));
        fldVO.setName(name);
        fldVO.setFolderId(ID);
        fldVO.setParent(parent);
        fldVO.setParentName(parentname);
        arrlistFolders.add(fldVO);
      }
    } finally {
      dl.destroy();
    }
    return arrlistFolders;
  }

  /**
   * This method returns All The public folders
   * @return ArrayList list of CvFolderVO objects
   */
  public ArrayList getPublicFolders()
  {
    // FIXME make it return the whole hierarchy below,
    // not just 1st generation children
    CVDal dl = new CVDal(dataSource);
    ArrayList publicFolderList = new ArrayList();
    Integer publicFolderId = this.getPublicFolderId();
    try {
      // selects all the folders that are somehow under the public folder
      dl.setSql("file.getpublicfolders");
      dl.setInt(1, publicFolderId.intValue());
      Collection rs = dl.executeQuery();
      Iterator it = rs.iterator();
      while (it.hasNext()) {
        CvFolderVO folder = new CvFolderVO();
        HashMap record = (HashMap)it.next();
        int id = ((Number)record.get("folderid")).intValue();
        String name = (String)record.get("name");
        int parentId = ((Number)record.get("parent")).intValue();
        String parentName = ((String)record.get("parentname"));
        folder.setName(name);
        folder.setFolderId(id);
        folder.setParent(parentId);
        folder.setParentName(parentName);
        publicFolderList.add(folder);
      }
    } finally {
      dl.destroy();
    }
    return publicFolderList;
  }
  /**
   * This method returns All The Root(Parent) Folders of This FolderID
   * in the form of DDNameValue object packaged in a Vector
   *
   * @param   userID
   * @param   folderID
   * @return   vector (vecDDNameValue)
   */
  public Vector getFolderRootPath(int userID, int folderID)
  {
    CVDal dl = new CVDal(dataSource);
    Vector vecDDNameValue = new Vector();
    try {
      int folID = folderID;
      dl.setSql("file.getcurfoldername");
      dl.setInt(1, folID);
      Collection colID = dl.executeQuery();
      if (colID.size() > 0) {
        Iterator itCur = colID.iterator();
        HashMap hmCur = (HashMap)itCur.next();
        DDNameValue curnameValueObj = new DDNameValue(String.valueOf(folID) + "#" + (String)hmCur.get("name"), (String)hmCur.get("name"));
        vecDDNameValue.addElement(curnameValueObj);
        if (((String)hmCur.get("system")).equals("TRUE")) {
          // 1 when it is a system folders should be included
          curnameValueObj.setId(folID);
        } else {
          // 2 when only is non-system folders are included
          curnameValueObj.setId(folID);
        }
        while (folID != 0) {
          dl.setSqlQueryToNull();
          dl.setSql("file.getparentinfo");
          dl.setInt(1, folID);
          Collection colParID = dl.executeQuery();
          Iterator iter = colParID.iterator();
          int parID = 0;
          String parName = "";
          String system = "";
          while (iter.hasNext()) {
            HashMap hm = (HashMap)iter.next();
            parID = ((Long)hm.get("parentid")).intValue();
            parName = (String)hm.get("parentname");
            system = (String)hm.get("system");
          }
          folID = parID;
          if (parID != 0) {
            DDNameValue thisnameValueObj = new DDNameValue(String.valueOf(parID) + "#" + parName, parName);
            if (system.equals("TRUE")) {
              vecDDNameValue.addElement(thisnameValueObj);
              thisnameValueObj.setId(parID);
            } else {
              vecDDNameValue.addElement(thisnameValueObj);
              thisnameValueObj.setId(parID);
            }
          }
        }
      }
    } finally {
      dl.destroy();
    }
    return vecDDNameValue;
  }

  /**
   * This method is called when duplicateFolder is requested This method calls a
   * method addFolderFiles to add the entries inCvFileFolder table
   * 
   * @param userID
   * @param fdvo
   * @param folderID
   * @param ParentID
   */
  public void duplicateFolder(int userID, CvFolderVO fdvo, int folderID, int ParentID) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userID, "CVFolder", folderID, ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("File - duplicateFolder");

    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("file.insertfolder");
      dl.setString(1, fdvo.getName());
      dl.setString(2, fdvo.getDescription());
      dl.setInt(3, fdvo.getParent());
      dl.setInt(4, fdvo.getLocationId());
      dl.setInt(5, userID);
      dl.setString(6, fdvo.getIsSystem());
      dl.setString(7, fdvo.getVisibility());
      dl.setInt(8, userID);
      dl.setInt(9, userID);
      dl.executeUpdate();
      int newParentId = dl.getAutoGeneratedKey();
      addFolderFiles(folderID, newParentId, dl);// adds all the files of that
                                                // folder
      dl.setSqlQueryToNull();
      dl.setSql("file.getsubfolderdetails");//gets all the subfolders of
                                            // current folder
      dl.setInt(1, folderID);
      Collection col = dl.executeQuery();
      Iterator iter = col.iterator();
      while (iter.hasNext()) {
        HashMap hm = (HashMap)iter.next();
        int curFolderID = ((Long)hm.get("folderid")).intValue();
        int curParentID = ((Long)hm.get("parent")).intValue();
        fdvo.setName((String)hm.get("name"));
        fdvo.setDescription((String)hm.get("description"));
        fdvo.setParent(newParentId);
        duplicateFolder(userID, fdvo, curFolderID, curParentID);
      }
    } finally {
      dl.destroy();
    }
  }

  /**
   * This method is called when duplicate file is requested
   * This method adds the entries in cvFileFolder table
   * @deprecated This doesn't do Anything!
   * @param   userID
   * @param   fileID
   * @param   folderID
   */
  public void duplicateFile(int userID,int fileID,int folderID) throws  AuthorizationFailedException
  {
    if(!CVUtility.canPerformRecordOperation(userID,"CVFolder",fileID,ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource) )
      throw new AuthorizationFailedException("File - duplicateFile");
  }


  /**
   * This method is called by duplicateFolder method
   *
   * @param   folderID
   * @param   newParentID
   * @param   dl
   */


  private void addFolderFiles(int folderID, int newParentID, CVDal dl)
  {
    dl.setSqlQueryToNull();
    dl.setSql("file.selectallfiles");//gets all the files of current folder
    dl.setInt(1, folderID);
    Collection col = dl.executeQuery();

    Iterator iter = col.iterator();

    while (iter.hasNext()) {
      HashMap hm = (HashMap)iter.next();

      int fileID = ((Long)hm.get("fileid")).intValue();

      dl.setSqlQueryToNull();
      dl.setSql("file.insertfilefolder");
      dl.setInt(1, fileID);
      dl.setInt(2, newParentID);
      dl.setString(3, CvFileVO.FP_VIRTUAL);
      dl.executeUpdate();
    }
  }

  /**
   * set IsTemporary = NO
   * 
   * @param fileId
   * @param status
   * @exception CvFileException
   */
  public void commitEmailAttachment(int fileId, String newName) throws CvFileException
  {

    CVDal cvdl = new CVDal(dataSource);
    try {
      cvdl.setSql("file.commitemailattachment");
      cvdl.setString(1, newName);
      cvdl.setInt(2, fileId);
      cvdl.executeUpdate();
    } catch (Exception e) {
      logger.error("[commitEmailAttachment] Exception thrown.", e);
      throw new CvFileException(CvFileException.OTHER_EXCEPTION, "Failed while commiting File : " + fileId);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
  }

  /**
   * this method returns all the folders in CvFolder Table
   *
   * @return   ArrayList
   */
  public ArrayList getAllFolders(int individualId, boolean systemIncludeFlag)
  {
    CVDal dl = new CVDal(dataSource);
    CVUtility.getAllAccessibleRecords("CVFolder", "folderaccess", "cvfolder", "FolderID", "owner", "(cvfolder.owner=" + individualId + " and cvfolder.IsSystem='FALSE') or (cvfolder.visibility='PUBLIC')", individualId, dl);
    ArrayList arrlistFolders = new ArrayList();
    try {
      dl.setSql("file.getallfolders"); //selects all the folders of the owner
                                       // including system folders
      Collection colID = dl.executeQuery();
      Iterator it = colID.iterator();
      dl.setSqlQueryToNull();
      dl.setSqlQuery("drop table folderaccess");
      dl.executeUpdate();
      int ID = 0;
      String name = "";
      String parentname = "";
      int parent = 0;

      while (it.hasNext()) {
        CvFolderVO fldVO = new CvFolderVO();
        HashMap hm = (HashMap)it.next();
        ID = ((Long)hm.get("folderid")).intValue();
        name = (String)hm.get("name");
        parent = ((Long)hm.get("parent")).intValue();
        parentname = (String)hm.get("parentname");
        fldVO.setName(name);
        fldVO.setFolderId(ID);
        fldVO.setParent(parent);
        fldVO.setParentName(parentname);
        arrlistFolders.add(fldVO);
      }
    } finally {
      dl.destroy();
    }
    return arrlistFolders;
  }

  /**
   * Returns a CvFolderVO object which is populated from the database based on
   * the given folderName parameter.
   * @param individualId The individualID of the user asking for this folder.
   * @param parentFolderId Parent folders id
   * @param folderName The name of the folder we're looking for
   * @return CvFolderVO object representing the folder which was asked for.
   * @throws CvFileException
   */
  public CvFolderVO getFolderByName(int individualId, int parentFolderId, String folderName) throws CvFileException
  {
    if (folderName == null) {
      throw new CvFileException(CvFileException.INVALID_DATA, "Folder name not provided");
    }
    CvFolderVO fdvo;
    CVDal cvdl = new CVDal(dataSource);
    try {
      cvdl.setSql("file.getfolderbyname");
      cvdl.setString(1, folderName);
      cvdl.setInt(2, parentFolderId);

      Collection col = cvdl.executeQuery();

      if (col == null) {
        throw new CvFileException(CvFileException.GET_FAILED, "Could not find Folder by name: " + folderName);
      }
      Iterator it = col.iterator();
      if (!it.hasNext()) {
        throw new CvFileException(CvFileException.GET_FAILED, "Could not find Folder by name: " + folderName);
      }

      HashMap hm = (HashMap)it.next();

      fdvo = new CvFolderVO();
      int folderId = ((Long)hm.get("FolderID")).intValue();
      fdvo.setFolderId(folderId);
      fdvo.setName((String)hm.get("Name"));
      fdvo.setDescription((String)hm.get("Description"));
      fdvo.setParent(((Long)hm.get("Parent")).intValue());

      fdvo.setFullPathVec(getFolderRootPath(individualId, folderId));
      if (hm.get("LocationID") != null)
        fdvo.setLocationId(((Long)hm.get("LocationID")).intValue());
      if (hm.get("Detail") != null) // incase location detail is null.
        fdvo.setLocationName((String)hm.get("Detail"));
      if (hm.get("CreatedBy") != null)
        fdvo.setCreatedBy(((Long)hm.get("CreatedBy")).intValue());
      if (hm.get("ModifiedBy") != null)
        fdvo.setModifiedBy(((Long)hm.get("ModifiedBy")).intValue());
      fdvo.setCreatedOn((Timestamp)hm.get("CreatedOn"));
      fdvo.setModifiedOn((Timestamp)hm.get("ModifiedOn"));
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return fdvo;
  }

  public FileList getAllTicketFiles(int userID, int ticketID)
  {
    FileList fileList = new FileList();
    CVDal cvdl = new CVDal(dataSource);
    Collection colList = null;
    try {
      cvdl.setSql("support.ticket.filelist");
      cvdl.setInt(1, ticketID);
      colList = cvdl.executeQuery();
    } finally {
      cvdl.destroy();
    }

    if (colList != null) {
      Iterator it = colList.iterator();
      int i = 0;

      while (it.hasNext()) {
        i++;
        HashMap hm = (HashMap)it.next();
        int fileID = ((Number)hm.get("fileid")).intValue();

        try {
          IntMember intFileID = new IntMember("FileID", fileID, 10, "", 'T', false, 10);

          StringMember strName = new StringMember("Name", (String)hm.get("name"), 10, "javascript:c_goTo('/files/file_download.do?fileid=" + fileID + "');", 'T', true);

          StringMember strDescription = null;
          if ((hm.get("description") != null)) {
            strDescription = new StringMember("Description", (String)hm.get("description"), 10, "", 'T', false);
          } else {
            strDescription = new StringMember("Description", null, 10, "", 'T', false);
          }

          int individualID = ((Long)hm.get("individualid")).intValue();
          StringMember strCreatedBy = new StringMember("CreatedBy", (String)hm.get("createdby"), 10, "javascript:c_openPopup('/contacts/view_individual.do?rowId=" + individualID + "')", 'T', true);

          IntMember intIndividualID = new IntMember("IndividualID", individualID, 10, "", 'T', false, 10);

          Timestamp dtCreated = null;
          DateMember dmCreated = null;
          String timezoneid = "EST";

          if ((hm.get("created") != null)) {
            dtCreated = (Timestamp)hm.get("created");
            dmCreated = new DateMember("Created", dtCreated, 10, "", 'T', false, 1, timezoneid);
          } else {
            dmCreated = new DateMember("Created", null, 10, "", 'T', false, 1, timezoneid);
          }

          Timestamp dtUpdated = null;
          DateMember dmUpdated = null;

          if ((hm.get("updated") != null)) {
            dtUpdated = (Timestamp)hm.get("updated");
            dmUpdated = new DateMember("Updated", dtUpdated, 10, "", 'T', false, 1, timezoneid);
          } else {
            dmUpdated = new DateMember("Updated", null, 10, "", 'T', false, 1, timezoneid);
          }

          FileListElement filelistelement = new FileListElement(fileID);

          filelistelement.put("FileID", intFileID);
          filelistelement.put("Description", strDescription);
          filelistelement.put("Name", strName);
          filelistelement.put("Created", dmCreated);
          filelistelement.put("Updated", dmUpdated);
          filelistelement.put("CreatedBy", strCreatedBy);
          filelistelement.put("IndividualID", intIndividualID);

          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);
          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
          String s4 = stringbuffer.toString();
          fileList.put(s4, filelistelement);
        } catch (Exception e) {
          logger.error("[getAllTicketFiles] Exception thrown.", e);
        }
      }
    }
    return fileList;
  } // end getAllTicketFiles() method

  private void sendToAttic(int userID, int transactionID, String recordType, HashMap primaryMembers)
  {
    try {
      InitialContext ctx = CVUtility.getInitialContext();
      CvAtticLocalHome home = (CvAtticLocalHome)ctx.lookup("local/CvAttic");
      CvAtticLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.dumpData(userID, transactionID, recordType, primaryMembers);
    } catch (Exception e) {
      logger.error("[sendToAttic] Exception thrown.", e);
    }
  }

  private int getAtticTransactionID(int userID, String dumpType, HashMap hm)
  {
    int transID = 0;
    try {

      InitialContext ctx = CVUtility.getInitialContext();
      CvAtticLocalHome home = (CvAtticLocalHome)ctx.lookup("local/CvAttic");

      CvAtticLocal remote = home.create();
      remote.setDataSource(this.dataSource);

      transID = remote.getAtticTransactionID(userID, dumpType, hm);
    } catch (Exception e) {
      logger.error("[getAtticTransactionID] Exception thrown.", e);
    }
    return transID;
  }

  /**
   * This method is used for File Listing by Entity
   *
   * @param   entityId
   * @param   userID
   * @param   hashmap
   * @return  FileList Object
   */
  public FileList getEntityFiles(int entityId, int userID, HashMap hashmap)
  {
    Integer intStart = (Integer)hashmap.get("startATparam");
    Integer intEnd = (Integer)hashmap.get("EndAtparam");
    String strSearch = (String)hashmap.get("searchString");
    String strSortMem = (String)hashmap.get("sortmem");
    Character chrSortType = (Character)hashmap.get("sortType");
    String strFileTypeReq = (String)hashmap.get("fileTypeRequest");
    Boolean systemincl = (Boolean)hashmap.get("SystemIncludeFlag");
    Integer intfolderID = (Integer)hashmap.get("curFolderID");
    char charSort = chrSortType.charValue();
    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();
    int folderID = intfolderID.intValue();
    String strOriginal = strSearch;
    boolean boolIncludeSystem = false;
    if (systemincl != null)
      boolIncludeSystem = systemincl.booleanValue();
    int beginIndex = 0;
    FileList filelist = new FileList();
    filelist.setSortMember(strSortMem);
    Vector vec = new Vector();
    CVDal cvdl = new CVDal(dataSource);
    Collection colList = null;
    try {
      String strQuery = "";
      String sortType = "ASC";
      if (charSort == 'A') {
        sortType = " ASC";
      } else {
        sortType = " DESC";
      }
      cvdl.setSqlQueryToNull();
      strQuery = "select 'FILE' as FileFolder,cvfl.Name as FolderName,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from cvfile fil ,individual indv,cvfilefolder fld,cvfolder cvfl where indv.individualid=fil.owner and cvfl.folderid=fld.folderid and fld.fileid=fil.fileid and (fil.RelateEntity=?) order by '"
          + strSortMem + "' " + sortType;
      cvdl.setSqlQuery(strQuery);
      cvdl.setInt(1, entityId);
      colList = cvdl.executeQuery();
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    Iterator it = colList.iterator();
    int i = 0;
    if (colList != null) {
      while (it.hasNext()) {
        i++;
        HashMap hm = (HashMap)it.next();
        int FolderID = ((Long)hm.get("ID")).intValue();
        try {
          IntMember intFolderID = new IntMember("ID", FolderID, 10, "", 'T', false, 10);
          StringMember strName = new StringMember("Name", (String)hm.get("Name"), 10, "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
          StringMember strDescription = null;
          if ((hm.get("Description") != null))
            strDescription = new StringMember("Description", (String)hm.get("Description"), 10, "", 'T', false);
          else
            strDescription = new StringMember("Description", null, 10, "", 'T', false);
          StringMember strCreatedBY = new StringMember("CreatedBy", (String)hm.get("Created BY"), 10, "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
          StringMember strFileFolder = new StringMember("FileFolder", (String)hm.get("FileFolder"), 10, "", 'T', false);
          IntMember intIndividualID = new IntMember("IndividualID", ((Long)hm.get("individualid")).intValue(), 10, "", 'T', false, 10);
          StringMember strFolderName = null;
          if (strOriginal != null && strOriginal.startsWith("ADVANCE:")) {
            strFolderName = new StringMember("FolderName", (String)hm.get("FolderName"), 10, "", 'T', false);
          } else if (folderID != -1) {
            strFolderName = new StringMember("FolderName", ((getFolder(userID, folderID)).getName()), 10, "", 'T', false);
          } else if (folderID == -1) {
            strFolderName = new StringMember("FolderName", null, 10, "", 'T', false);
          }
          Timestamp dtCreated = null;
          Timestamp dtUpdated = null;
          DateMember dmCreated = null;
          DateMember dmUpdated = null;
          String timezoneid = "EST";
          if ((hm.get("Created") != null)) {
            dtCreated = (Timestamp)hm.get("Created");
            dmCreated = new DateMember("Created", dtCreated, 10, "", 'T', false, 1, timezoneid);
          } else {
            dmCreated = new DateMember("Created", null, 10, "", 'T', false, 1, timezoneid);
          }
          if ((hm.get("Updated") != null)) {
            dtUpdated = (Timestamp)hm.get("Updated");
            dmUpdated = new DateMember("Updated", dtUpdated, 10, "", 'T', false, 1, timezoneid);
          } else {
            dmUpdated = new DateMember("Updated", null, 10, "", 'T', false, 1, timezoneid);
          }
          FileListElement filelistelement = new FileListElement(FolderID);
          filelistelement.put("FolderID", intFolderID);
          filelistelement.put("Name", strName);
          filelistelement.put("Description", strDescription);
          filelistelement.put("CreatedBy", strCreatedBY);
          filelistelement.put("Created", dmCreated);
          filelistelement.put("Updated", dmUpdated);
          filelistelement.put("FileFolder", strFileFolder);
          filelistelement.put("IndividualID", intIndividualID);
          filelistelement.put("FolderName", strFolderName);
          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);
          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
          String s4 = stringbuffer.toString();
          filelist.put(s4, filelistelement);
        } catch (Exception e) {
          logger.error("[getEntityFiles] Exception thrown.", e);
        }
      }
    }
    if (folderID != -1 && (strSearch == null || strSearch.equals(""))) {
      Vector rootPath = getFolderRootPath(userID, folderID);
      filelist.setDirectoryStructure(rootPath);
    }
    filelist.setTotalNoOfRecords(filelist.size());
    filelist.setListType("File");
    filelist.setBeginIndex(beginIndex);
    filelist.setFileTypeRequest(strFileTypeReq);
    filelist.setEndIndex(filelist.size());
    return filelist;
  }

  public FileList getAllCustomerFiles(int userID, HashMap hashmap)
  {
    Integer intStart = (Integer)hashmap.get("startATparam");
    Integer intEnd = (Integer)hashmap.get("EndAtparam");
    String strSearch = (String)hashmap.get("searchString");
    String strSortMem = (String)hashmap.get("sortmem");
    Character chrSortType = (Character)hashmap.get("sortType");
    String strFileTypeReq = (String)hashmap.get("fileTypeRequest");
    Boolean systemincl = (Boolean)hashmap.get("SystemIncludeFlag");
    Integer intfolderID = (Integer)hashmap.get("curFolderID");
    char charSort = chrSortType.charValue();
    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();
    int folderID = intfolderID.intValue();
    String strOriginal = strSearch;
    boolean boolIncludeSystem = false;
    if (systemincl != null)
      boolIncludeSystem = systemincl.booleanValue();
    int beginIndex = 0;
    FileList filelist = new FileList();
    filelist.setSortMember(strSortMem);
    Vector vec = new Vector();
    CVDal cvdl = new CVDal(dataSource);
    Collection colList = null;
    try {
      if (strSearch != null && strSearch.startsWith("ADVANCE:")) {
        strSearch = strSearch.substring(8);
        String str = "create TEMPORARY TABLE filelistSearch " + strSearch;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        if (strFileTypeReq.equals(FileConstantKeys.ALL)) {
          String strQuery = "";
          String sortType = "ASC";
          if (charSort == 'A')
            sortType = "ASC";
          else
            sortType = "DESC";
          if (boolIncludeSystem) {
            strQuery = "select 'FILE' as FileFolder,cvfl.Name as FolderName,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from cvfile fil ,individual indv,cvfilefolder fld,cvfolder cvfl,filelistSearch fSearch where indv.individualid=fil.owner and cvfl.folderid=fld.folderid and fld.fileid=fil.fileid  and fSearch.fileid=fil.fileid order by '"
                + strSortMem + "' " + sortType;
            cvdl.setSqlQuery(strQuery);
          } else {
            strQuery = "select 'FILE' as FileFolder,cvfl.Name as FolderName,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from cvfile fil ,individual indv,cvfilefolder fld,cvfolder cvfl,filelistSearch fSearch where indv.individualid=fil.owner and cvfl.folderid=fld.folderid and fld.fileid=fil.fileid and (fil.visibility='PUBLIC' or fil.owner=?) and fSearch.fileid=fil.fileid order by '"
                + strSortMem + "' " + sortType;
            cvdl.setSqlQuery(strQuery);
            cvdl.setInt(1, userID);
          }
          colList = cvdl.executeQuery();
        } else {
          String strQuery = "";
          String sortType = "ASC";
          if (charSort == 'A')
            sortType = "ASC";
          else
            sortType = "DESC";
          if (boolIncludeSystem)
            strQuery = "select 'FILE' as FileFolder,cvfl.Name as FolderName,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from cvfile fil ,individual indv,cvfilefolder fld,cvfolder cvfl,filelistSearch fSearch  where indv.individualid=fil.owner and fld.fileid=fil.fileid and fil.owner=? and cvfl.folderid=fld.folderid and fSearch.fileid=fil.fileid order by '"
                + strSortMem + "' " + sortType;
          else
            strQuery = "select 'FILE' as FileFolder,cvfl.Name as FolderName,indv.individualid as individualid,fil.fileid ID,fil.name Name,fil.description Description,fil.created Created,fil.updated Updated,concat(indv.FirstName,'',indv.LastName) 'Created BY' from cvfile fil ,individual indv,cvfilefolder fld,cvfolder cvfl,filelistSearch fSearch  where indv.individualid=fil.owner and fld.fileid=fil.fileid and fil.owner=? and cvfl.folderid=fld.folderid and fSearch.fileid=fil.fileid order by '"
                + strSortMem + "' " + sortType;
          cvdl.setSqlQueryToNull();
          cvdl.setSqlQuery(strQuery);
          cvdl.setInt(1, userID);
          colList = cvdl.executeQuery();
        }
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE filelistSearch");
        cvdl.executeUpdate();
      } else {
        cvdl.setSqlQueryToNull();
        if (strFileTypeReq.equals(FileConstantKeys.MY) && folderID == -1) {
          cvdl.setSql("customerview.file.getuserfiles");
          cvdl.setInt(1, userID);
          cvdl.setInt(2, userID);
          colList = cvdl.executeQuery();
        } else if (strFileTypeReq.equals(FileConstantKeys.ALL)) {
          String sortType = "ASC";
          if (charSort == 'A')
            sortType = "ASC";
          else
            sortType = "DESC";
          if (boolIncludeSystem)
            cvdl.setDynamicQuery("file.allfilefolderssystem", sortType, strSortMem);
          else
            cvdl.setDynamicQuery("file.allfilefoldersnonsystem", sortType, strSortMem);
          cvdl.setInt(1, folderID);
          cvdl.setInt(2, folderID);
          colList = cvdl.executeQuery();
        } else if (strFileTypeReq.equals(FileConstantKeys.MY)) {
          String sortType = "ASC";
          if (charSort == 'A')
            sortType = "ASC";
          else
            sortType = "DESC";
          if (boolIncludeSystem)
            cvdl.setDynamicQuery("file.myfilefolderssystem", sortType, strSortMem);
          else
            cvdl.setDynamicQuery("file.myfilefoldersnonsystem", sortType, strSortMem);
          cvdl.setInt(1, folderID);
          cvdl.setInt(2, userID);
          cvdl.setInt(3, folderID);
          cvdl.setInt(4, userID);
          colList = cvdl.executeQuery();
        } else if (strFileTypeReq.equals("INDV")) {
          cvdl.setSql("file.getindividualfiles");
          cvdl.setInt(1, userID);
          cvdl.setInt(2, userID);
          colList = cvdl.executeQuery();
        }
      }
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    int i = 0;
    if (colList != null) {
      Iterator it = colList.iterator();
      while (it.hasNext()) {
        i++;
        HashMap hm = (HashMap)it.next();
        int FolderID = ((Long)hm.get("ID")).intValue();
        try {
          IntMember intFolderID = new IntMember("ID", FolderID, 'r', "", 'T', false, 10);
          StringMember strName = new StringMember("Name", (String)hm.get("Name"), 'r', "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
          StringMember strDescription = null;
          if ((hm.get("Description") != null))
            strDescription = new StringMember("Description", (String)hm.get("Description"), 'r', "", 'T', false);
          else
            strDescription = new StringMember("Description", null, 'r', "", 'T', false);
          StringMember strCreatedBY = new StringMember("CreatedBy", (String)hm.get("Created BY"), 'r', "/centraview/ViewHandler.do?typeOfContact=entity&rowId=1", 'T', true);
          StringMember strFileFolder = new StringMember("FileFolder", (String)hm.get("FileFolder"), 'r', "", 'T', false);
          IntMember intIndividualID = new IntMember("IndividualID", ((Long)hm.get("individualid")).intValue(), 'r', "", 'T', false, 10);
          StringMember strFolderName = null;
          if (strOriginal != null && strOriginal.startsWith("ADVANCE:")) {
            strFolderName = new StringMember("FolderName", (String)hm.get("FolderName"), 'r', "", 'T', false);
          } else if (folderID != -1) {
            strFolderName = new StringMember("FolderName", (String)((getFolder(userID, folderID)).getName()), 'r', "", 'T', false);
          } else if (folderID == -1)
            strFolderName = new StringMember("FolderName", null, 'r', "", 'T', false);
          Timestamp dtCreated = null;
          Timestamp dtUpdated = null;
          DateMember dmCreated = null;
          DateMember dmUpdated = null;
          String timezoneid = "EST";
          if ((hm.get("Created") != null)) {
            dtCreated = (Timestamp)hm.get("Created");
            dmCreated = new DateMember("Created", dtCreated, 'r', "", 'T', false, 1, timezoneid);
          } else {
            dmCreated = new DateMember("Created", null, 'r', "", 'T', false, 1, timezoneid);
          }
          if ((hm.get("Updated") != null)) {
            dtUpdated = (Timestamp)hm.get("Updated");
            dmUpdated = new DateMember("Updated", dtUpdated, 'r', "", 'T', false, 1, timezoneid);
          } else {
            dmUpdated = new DateMember("Updated", null, 'r', "", 'T', false, 1, timezoneid);
          }
          FileListElement filelistelement = new FileListElement(FolderID);
          filelistelement.put("FolderID", intFolderID);
          filelistelement.put("Name", strName);
          filelistelement.put("Description", strDescription);
          filelistelement.put("CreatedBy", strCreatedBY);
          filelistelement.put("Created", dmCreated);
          filelistelement.put("Updated", dmUpdated);
          filelistelement.put("FileFolder", strFileFolder);
          filelistelement.put("IndividualID", intIndividualID);
          filelistelement.put("FolderName", strFolderName);
          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);
          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(), stringbuffer.length(), s3);
          String s4 = stringbuffer.toString();
          filelist.put(s4, filelistelement);
        } catch (Exception e) {
          logger.error("[getAllCustomerFiles] Exception thrown.", e);
        }
      }
    }
    if (folderID != -1 && (strSearch == null || strSearch.equals(""))) {
      Vector rootPath = getFolderRootPath(userID, folderID);
      filelist.setDirectoryStructure(rootPath);
    }
    filelist.setTotalNoOfRecords(filelist.size());
    filelist.setListType("File");
    filelist.setBeginIndex(beginIndex);
    filelist.setFileTypeRequest(strFileTypeReq);
    filelist.setEndIndex(filelist.size());
    return filelist;
  }

  // for the customer view [END]
  public ArrayList getCompanyNews()
  {
    ArrayList companyList = new ArrayList();
    CVDal cvdl = new CVDal(dataSource);
    try {
      Collection colList = null;
      cvdl.setSql("companynews.getnews");
      colList = cvdl.executeQuery();
      Iterator it = colList.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        companyList.add(new CompanyNewsVO(((Number)hm.get("FileID")).intValue(), (String)hm.get("Description"), (String)hm.get("Title")));
      }
    } catch (NumberFormatException e) {
      logger.error("[getCompanyNews] Exception thrown.", e);
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return companyList;
  }


  /**
   * This method deletes the existing File Links in the system and then adds all
   * of the links associated with the FileVO.
   * 
   * @param fileId The ID of the File ID.
   * @param fileVO The FileVO object with the new File Links.
   * @param dl The Database Connection.
   */
  private void addFileLink(int fileId, CvFileVO fileVO, CVDal dl)
  {
    try{
      if (fileVO != null)
      {
        int relatedFieldID = fileVO.getRelatedFieldID();
        // first delete all then add new
        deleteFileLink(fileId, dl);
        //Add the related field.
        if (relatedFieldID > 0)
        {
          dl.setSqlQueryToNull();
          dl.setSqlQuery("insert into cvfilelink (FileID,RecordTypeID,RecordID) values(?,?,?)");
          dl.setInt(1, fileId);
          dl.setInt(2, fileVO.getRelatedTypeID());
          dl.setInt(3, relatedFieldID);
          dl.executeUpdate();
        } //end of if statement (relatedFieldID > 0)
      } //end of if statement (fileVO == null)
      dl.setSqlQueryToNull();
    }catch(Exception e){
      logger.error("[addFileLink] Exception thrown.", e);
    }
  } //end of addFileLink method

  /**
   * Deleted all of the FileLinks associated with this File.
   *
   * @param FileId The FileID of the File links to clear
   * @param dl The Database Connection.
   */
  private void deleteFileLink(int FileId, CVDal dl)
  {
    try{
      dl.setSqlQueryToNull();
      dl.setSqlQuery("delete from cvfilelink where FileID=?");
      dl.setInt(1, FileId);
      dl.executeUpdate();
      dl.setSqlQueryToNull();
    }catch(Exception e){
      logger.error("[deleteFileLink] Exception thrown.", e);
    }
  } //end of deleteFileLink method

  /**
   * Fills in the FileLinks for an File. This method fills in the
   * following links:
   * <ul>
   * <li> Project
   * <li> Opportunity
   * <li> Ticket
   * <li> Proposal
   * </ul>
   *
   * @param FileId The File ID to get the File Links for.
   * @param flvo The FileVO to fill in.
   * @param dl The connection to the database.
   * @param individualID The individualID
   */
  private void getFileLink(int FileId, CvFileVO flvo, CVDal dl,int individualID)
  {
   try{
    dl.setSqlQueryToNull();
    dl.setSqlQuery("select * from cvfilelink where FileID=?");
    dl.setInt(1, FileId);
    Collection col = dl.executeQuery();
    if (col != null)
    {
      Iterator it = col.iterator();
      int recordType = 0;
      int linkId = 0;
      InitialContext ic = CVUtility.getInitialContext();
      while (it.hasNext())
      {
        HashMap hm = (HashMap) it.next();
        if (hm.get("RecordTypeID") != null)
        {
          recordType = ((Long) hm.get("RecordTypeID")).intValue();
        } //end of if statement (hm.get("RecordTypeID") != null)
        if (hm.get("RecordID") != null)
        {
          linkId = ((Long) hm.get("RecordID")).intValue();
          switch (recordType)
          {
            case 30: // Opportunity - from module table
              try
              {
                OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
                OpportunityLocal local = home.create();
                local.setDataSource(this.dataSource);
                String opportunityName = local.getOpportunityName(linkId);
                flvo.setRelatedFieldID(linkId);
                flvo.setRelatedFieldValue(opportunityName);
                flvo.setRelatedTypeID(30);
                flvo.setRelatedTypeValue("Opportunity");
              } catch (Exception e) {
                logger.error("[getFileLink] Exception thrown.", e);
                //do nothing
              }
              break;
            case 31: // Proposal - from module table
              try
              {
                ProposalHome home = (ProposalHome)ic.lookup("local/Proposal");
                ProposalLocal remote =  home.create();
                remote.setDataSource(this.dataSource);
                String proposalTitle = remote.getProposalName(linkId);
                flvo.setRelatedFieldID(linkId);
                flvo.setRelatedFieldValue(proposalTitle);
                flvo.setRelatedTypeID(31);
                flvo.setRelatedTypeValue("Project");
              } catch (Exception e) {
                logger.error("[getFileLink] Exception thrown.", e);
              } //end of catch block (Exception)
              break;

            case 36: // Projects - from module table
              try
              {
                ProjectLocalHome home = (ProjectLocalHome)ic.lookup("local/Project");
                ProjectLocal remote = home.create();
                remote.setDataSource(this.dataSource);
                String projectTitle = remote.getProjectName(linkId);
                flvo.setRelatedFieldID(linkId);
                flvo.setRelatedFieldValue(projectTitle);
                flvo.setRelatedTypeID(36);
                flvo.setRelatedTypeValue("Project");
              } catch (Exception e) {
                logger.error("[getFileLink] Exception thrown.", e);
              } //end of catch block (Exception)
              break;
            case 39: // Ticket - from module table
              try
              {
                TicketLocalHome home = (TicketLocalHome)ic.lookup("local/Ticket");
                TicketLocal remote = home.create();
                remote.setDataSource(this.dataSource);
                String ticketSubject = remote.getTicketName(linkId);
                flvo.setRelatedFieldID(linkId);
                flvo.setRelatedFieldValue(ticketSubject);
                flvo.setRelatedTypeID(39);
                flvo.setRelatedTypeValue("Ticket");
              } catch (Exception e) {
                logger.error("[getFileLink] Exception thrown.", e);
              } //end of catch block (Exception)
              break;
          } //end of switch statement (recordType)
        } //end of if statement (hm.get("RecordID") != null)
      } //end of while loop (it.hasNext())
    }
    dl.setSqlQueryToNull();
   }catch(Exception e){
     logger.error("[getFileLink] Exception thrown.", e);
   }
  } //end of getFileLink method

	/**
	 * Gets File Input Stream.
	 * we will pass the fileID and get folderPath and fileName
	 * We will process the image to a byte array and return it.
	 *
	 * @param individualId  Identification for the individual who is logged in.
	 * @param fileId  Identification of the file.
	 * @param dataSource A string that contains the cannonical JNDI name of the datasource
	 * @return HashMap It will return two things array of byte & fileName.
	 */
  public HashMap getFileInputStream(int individualId, int fileId, String dataSource)
  {
    HashMap imageInfo = new HashMap();
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSqlQuery("SELECT folderid FROM cvfilefolder WHERE fileid=?");
      dl.setInt(1, fileId);
      byte[] buffer = null;
      String fileName = "";

      int folderID = 0;
      Collection col = dl.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        if (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          folderID = ((Number)hm.get("folderid")).intValue();
        }
      }
      dl.setSqlQueryToNull();
      dl.setSqlQuery("SELECT name FROM cvfile WHERE fileid=?");
      dl.setInt(1, fileId);
      col = null;
      col = dl.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        if (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          fileName = (String)hm.get("name");
        }
      }
      FileInputStream fileInputStream = null;
      if (folderID > 0) {
        try {
          // We know only the owner will have the permission to access the
          // folder.
          // If some other user wants to access then we will bye-pass the
          // authentication
          // We have to do this coz Releted Email's Individual will not have the
          // access to Folder.
          CvFolderVO cvfCurFolVO = this.getFolder(-13, folderID);
          CvDiskOperation cdo = new CvDiskOperation(cvfCurFolVO.getLocationName(), cvfCurFolVO.getFullPathVec());
          String fullPath = cdo.getWorkingFullpath();
          File imageFile = new File(fullPath + "/" + fileName);

          // Catch the IOException on this FileInputStream constructor and
          // on the read method so we can collect the buffer.

          fileInputStream = new FileInputStream(imageFile);
          buffer = new byte[fileInputStream.available()];
          if (buffer != null) {
            for (int i = 0, n = fileInputStream.available(); i < n; i++) {
              buffer[i] = (byte)fileInputStream.read();
            }
          }
        } catch (CvFileException e) {
          logger.error("[CVFileEJB] Exception thrown in getFileInputStream.", e);
        } catch (AuthorizationFailedException e) {
          logger.error("[CVFileEJB] Exception thrown in getFileInputStream.", e);
        } catch (FileNotFoundException e) {
          logger.error("[CVFileEJB] Exception thrown in getFileInputStream.", e);
        } catch (IOException e) {
          logger.error("[CVFileEJB] Exception thrown in getFileInputStream.", e);
        } finally {
          try {
            fileInputStream.close();
          } catch (Exception e) {
            logger.error("[CVFileEJB] Exception thrown in getFileInputStream.", e);
          }
        }
      }
      imageInfo.put("imageBuffer", buffer);
      imageInfo.put("fileName", fileName);
    } finally {
      dl.destroy();
      dl = null;
    }
    return imageInfo;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }
  
  /**
   * Returns a ValueListVO representing a list of file records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getFileValueList(int individualID, ValueListParameters parameters)
  {
    ArrayList list = new ArrayList();
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      boolean permissionSwitch = individualID < 1 ? false : true;
      boolean applyFilter = false;
      String filter = parameters.getFilter();
      if (filter != null && filter.length() > 0) {
        String str = "CREATE TABLE filelistfilter " + filter;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        applyFilter = true;
      }
      int numberOfRecords = 0;
      // TODO Check permissions on folders too.
      if (applyFilter) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "filelistfilter", individualID, 6, "cvfile", "FileId", "Owner", null, permissionSwitch);
      } else if (permissionSwitch) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 6, "cvfile", "FileId", "Owner", null, permissionSwitch);
      }
      parameters.setTotalRecords(numberOfRecords);

      String query = this.buildFileListQuery(applyFilter, permissionSwitch, parameters);
      cvdl.setSqlQuery(query);
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();

      if (applyFilter) {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE filelistfilter");
        cvdl.executeUpdate();
      }
      if (applyFilter || permissionSwitch) {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE listfilter");
        cvdl.executeUpdate();
      }
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return new ValueListVO(list, parameters);
  }
  
  private String buildFileListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
  {
    int folderID = parameters.getFolderID();
    String select = 
      "SELECT f.FolderID AS Id, 'FOLDER' AS Type, i.IndividualID, f.Name, f.Description, f.CreatedOn AS " +
      "Created, f.ModifiedOn AS Updated, CONCAT(i.FirstName, ' ', i.LastName) AS 'CreatedBy', f.Parent, " +
      "f.Name AS Title ";
    StringBuffer from = new StringBuffer("FROM individual i, cvfolder f ");
    StringBuffer where = new StringBuffer("WHERE i.IndividualID = f.Owner AND IsSystem = 'FALSE' ");
    if (folderID > -1)
      where.append("AND f.Parent = " + folderID + " ");
    StringBuffer query = new StringBuffer();
    query.append(select);
    query.append(from);
    query.append(where);
    query.append("UNION ");
    select = 
      "SELECT file.FileID AS Id, 'FILE' AS Type, i.IndividualID, file.Name, file.Description, file.Created AS " +
      "Created, file.Updated AS Updated, CONCAT(i.FirstName, ' ', i.LastName) AS 'CreatedBy', ff.FolderID " +
      "AS Parent, file.Title "; 
    
    from = new StringBuffer("FROM cvfile file, individual i, cvfilefolder ff ");
    where = new StringBuffer("WHERE i.IndividualID = file.owner AND ff.FileID = file.FileID ");
    if (folderID > -1)
      where.append("AND ff.FolderID = " + folderID + " ");
    
    String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + 
        " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();
    
    // If a filter is applied we need to do an additional join against the
    // temporary entity list filter table.
    if (applyFilter || permissionSwitch) {
      from.append(", listfilter AS lf ");
      where.append("AND file.FileID = lf.FileID ");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    query.append(select);
    query.append(from);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }
  /**
   * calls the CvFile EJB to get the public folder id.
   * @return Integer folder id or null if something goes wrong.
   */
  public Integer getPublicFolderId() {
    Integer id = null;
    CVDal cvdl = new CVDal(this.dataSource);
    try {
      String query = "SELECT folderId FROM cvfolder WHERE name = ? AND leftNav = 1";
      cvdl.setSqlQuery(query);
      cvdl.setString(1, "Public Folders");
      Collection rs = cvdl.executeQuery();
      Iterator i = rs.iterator();
      while (i.hasNext()) {
        HashMap record = (HashMap)i.next();
        int intId = ((Number)record.get("folderId")).intValue();
        id = new Integer(intId);
      }
    } catch (Exception e) {
      logger.error("[getPublicFolderId]: Exception", e);
      throw new EJBException(e);
    } finally {
      cvdl.destroy();
    }
    return id;
  }
}
