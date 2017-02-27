/*
 * $RCSfile: FileForm.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

public class FileForm extends org.apache.struts.action.ActionForm {

  /** Stores file id */
  private String fileId;

  /** Stores file object */
  private FormFile file;

  /** Stores folder name where file is uploaded */
  private String uploadfoldername;

  /** Stores folder id where file is uploaded */
  private String uploadfolderid;

  /** Stores other folder names where file is uploaded */
  private String[] otheruploadfoldername;

  /** Stores other folder names in vector to display in list box on jsp */
  private Vector otheruploadfoldernamevec;

  /** Stores title */
  private String title;

  /** Stores description */
  private String description;

  /** Stores author name */
  private String authorname;

  /** Stores author id */
  private String authorid;

  /** Stores entity name */
  private String entityname;

  /** Stores entity id */
  private String entityid;

  /** Stores attach name */
  private String attachname;

  /** Stores attach id */
  private String attachid;

  /** Stores record name */
  private String recordname;

  /** Stores record name in vector to display in list box on jsp */
  private Vector recordnamevec;

  /** Stores record id */
  private String recordid;

  /** Stores access */
  private String access = FileConstantKeys.DEFAULTACCESS;

  /** Stores customer view */
  private String customerview = FileConstantKeys.DEFAULTCUSTOMERVIEW;

  /** Stores file version */
  private String fileversion;

  /** Stores all individual information */
  private String[] allindividual;

  /** Stores individual having view permission */
  private String[] viewpermission;

  /** Stores individual having modify permission */
  private String[] modifypermission;

  /** Stores individual having delete permission */
  private String[] deletepermission;

  /**
   * Stores individual having view permission in vector to display in list box
   * on jsp
   */
  private Vector viewpermissionvec;

  /**
   * Stores individual having modify permission in vector to display in list box
   * on jsp
   */
  private Vector modifypermissionvec;

  /**
   * Stores individual having delete permission in vector to display in list box
   * on jsp
   */
  private Vector deletepermissionvec;

  /** Stores owner id */
  private String ownerid;

  /** Stores owner name */
  private String ownername;

  /** Stores created name */
  private String created;

  /** Stores modified name */
  private String modified;

  /** Stores location id */
  private String locationid;

  /** Stores location name */
  private String locationname;

  /** Stores file info */
  private String fileInfo;

  /** Stores individual id */
  private String individualid;

  /** Stores individual name */
  private String individualname;

  /** Stores start day */
  private String startday;

  /** Stores start month */
  private String startmonth;

  /** Stores start year */
  private String startyear;

  /** Stores end day */
  private String endday;

  /** Stores end month */
  private String endmonth;

  /** Stores end year */
  private String endyear;

  /** used in case of duplicate only */
  private String filename;

  private String employeeHandBookFlag = "false";

  /** Stores company news */
  private String companynews = FileConstantKeys.DEFAULTCOMPANYNEWS;

  /** The ID of the type of the item related to this file upload. */
  protected int relatedTypeID;

  /** The value of the type of the item related to this file upload. */
  protected String relatedTypeValue;

  /** The ID of the item related to this file upload. */
  protected int relatedFieldID;

  /** The value of the item related to this file upload. */
  protected String relatedFieldValue;

  public FileForm() {}

  public void reset(ActionMapping actionMapping, HttpServletRequest request) {}

  public String getCompanynews() {
    return (this.companynews).trim();
  }

  public void setCompanynews(String companynews) {
    this.companynews = companynews;
  }

  public String getAccess() {
    return this.access;
  }

  public void setAccess(String access) {
    this.access = access;
  }

  public String[] getAllindividual() {
    return this.allindividual;
  }

  public void setAllindividual(String[] allindividual) {
    this.allindividual = allindividual;
  }

  public String getAuthorid() {
    return this.authorid;
  }

  public void setAuthorid(String authorid) {
    this.authorid = authorid;
  }

  public String getAuthorname() {
    return this.authorname;
  }

  public void setAuthorname(String authorname) {
    this.authorname = authorname;
  }

  public String getCustomerview() {
    return this.customerview;
  }

  public void setCustomerview(String customerview) {
    this.customerview = customerview;
  }

  public String[] getDeletepermission() {
    return this.deletepermission;
  }

  public void setDeletepermission(String[] deletepermission) {
    this.deletepermission = deletepermission;
  }

  public Vector getDeletepermissionvec() {
    return this.deletepermissionvec;
  }

  public void setDeletepermissionvec(Vector deletepermissionvec) {
    this.deletepermissionvec = deletepermissionvec;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEntityid() {
    return this.entityid;
  }

  public void setEntityid(String entityid) {
    this.entityid = entityid;
  }

  public String getEntityname() {
    return this.entityname;
  }

  public void setEntityname(String entityname) {
    this.entityname = entityname;
  }

  public FormFile getFile() {
    return this.file;
  }

  public void setFile(FormFile file) {
    this.file = file;
  }

  public String[] getModifypermission() {
    return this.modifypermission;
  }

  public void setModifypermission(String[] modifypermission) {
    this.modifypermission = modifypermission;
  }

  public Vector getModifypermissionvec() {
    return this.modifypermissionvec;
  }

  public void setModifypermissionvec(Vector modifypermissionvec) {
    this.modifypermissionvec = modifypermissionvec;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUploadfolderid() {
    return this.uploadfolderid;
  }

  public void setUploadfolderid(String uploadfolderid) {
    this.uploadfolderid = uploadfolderid;
  }

  public String getUploadfoldername() {
    return this.uploadfoldername;
  }

  public void setUploadfoldername(String uploadfoldername) {
    this.uploadfoldername = uploadfoldername;
  }

  public String[] getViewpermission() {
    return this.viewpermission;
  }

  public void setViewpermission(String[] viewpermission) {
    this.viewpermission = viewpermission;
  }

  public Vector getViewpermissionvec() {
    return this.viewpermissionvec;
  }

  public void setViewpermissionvec(Vector viewpermissionvec) {
    this.viewpermissionvec = viewpermissionvec;
  }

  public String getAttachid() {
    return this.attachid;
  }

  public void setAttachid(String attachid) {
    this.attachid = attachid;
  }

  public String getAttachname() {
    return this.attachname;
  }

  public void setAttachname(String attachname) {
    this.attachname = attachname;
  }

  public String getRecordid() {
    return this.recordid;
  }

  public void setRecordid(String recordid) {
    this.recordid = recordid;
  }

  public String getRecordname() {
    return this.recordname;
  }

  public void setRecordname(String recordname) {
    this.recordname = recordname;
  }

  public Vector getRecordnamevec() {
    return this.recordnamevec;
  }

  public void setRecordnamevec(Vector recordnamevec) {
    this.recordnamevec = recordnamevec;
  }

  public String[] getOtheruploadfoldername() {
    return this.otheruploadfoldername;
  }

  public void setOtheruploadfoldername(String[] otheruploadfoldername) {
    this.otheruploadfoldername = otheruploadfoldername;
  }

  public Vector getOtheruploadfoldernamevec() {
    return this.otheruploadfoldernamevec;
  }

  public void setOtheruploadfoldernamevec(Vector otheruploadfoldernamevec) {
    this.otheruploadfoldernamevec = otheruploadfoldernamevec;
  }

  public String getFileversion() {
    return this.fileversion;
  }

  public void setFileversion(String fileversion) {
    this.fileversion = fileversion;
  }

  public String getFileId() {
    return this.fileId;
  }

  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

  public String getOwnerid() {
    return this.ownerid;
  }

  public void setOwnerid(String ownerid) {
    this.ownerid = ownerid;
  }

  public String getOwnername() {
    return this.ownername;
  }

  public void setOwnername(String ownername) {
    this.ownername = ownername;
  }

  public String getModified() {
    return this.modified;
  }

  public void setModified(String modified) {
    this.modified = modified;
  }

  public String getCreated() {
    return this.created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getLocationid() {
    return this.locationid;
  }

  public void setLocationid(String locationid) {
    this.locationid = locationid;
  }

  public String getLocationname() {
    return this.locationname;
  }

  public void setLocationname(String locationname) {
    this.locationname = locationname;
  }

  public String getFileInfo() {
    return this.fileInfo;
  }

  public void setFileInfo(String fileInfo) {
    this.fileInfo = fileInfo;
  }

  public String getIndividualid() {
    return this.individualid;
  }

  public void setIndividualid(String individualid) {
    this.individualid = individualid;
  }

  public String getIndividualname() {
    return this.individualname;
  }

  public void setIndividualname(String individualname) {
    this.individualname = individualname;
  }

  /**
   * Validates user input data
   * 
   * @param mapping
   *          ActionMapping
   * @param request
   *          HttpServletRequest
   * @return ActionErrors
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    // initialize new actionerror object
    ActionErrors errors = new ActionErrors();
    try {
      // file name
      FormFile file = this.getFile();

      String fileName = "";

      HttpSession session = request.getSession(true);
      String fEmployee = (String) session.getAttribute("FromEmployee");

      if (request.getParameter(FileConstantKeys.TYPEOFOPERATION).equals(FileConstantKeys.EDIT)) {
        fileName = this.getFileInfo();
        if (fileName == null || fileName.trim().length() == 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "File Name"));
        }
      } else if (file != null) {
        fileName = file.getFileName();
        if (fileName == null || fileName.trim().length() == 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "File Name"));
        }
      } else {
        if (fEmployee != null && !fEmployee.equals("EmployeeHandbook")) {
          if (fileName == null || fileName.trim().length() == 0) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "File Name"));
          }
        }
      }

      if (fEmployee != null && !fEmployee.equals("EmployeeHandbook")) {
        if (this.getUploadfoldername() == null || this.getUploadfoldername().trim().length() == 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Folder Name"));
        }
      }

      // title
      if (this.getTitle() == null || this.getTitle().trim().length() == 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Title"));
      }

      // description
      if (this.getDescription() == null || this.getDescription().trim().length() == 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Description"));
      }

      // redirect to jsp if errors present
      if (!errors.isEmpty()) {
        request.setAttribute("bodycontent", "editdetailfile");
      }

      if (request.getParameter(FileConstantKeys.TYPEOFOPERATION) != null
          && request.getParameter(FileConstantKeys.TYPEOFOPERATION).equals("EDIT")) {
        request.setAttribute(FileConstantKeys.FFID, request.getParameter(FileConstantKeys.FFID));
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FILE);
      request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
      request.setAttribute(FileConstantKeys.TYPEOFOPERATION, request.getParameter(FileConstantKeys.TYPEOFOPERATION));
      request.setAttribute(FileConstantKeys.WINDOWID, request.getParameter(FileConstantKeys.WINDOWID));
    }

    return errors;
  }

  public String getFilename() {
    return this.filename;
  }

  // Iq added used for duplicate only
  public void setFilename(String filename) {
    this.filename = filename;
  }

  /**
   * Sets the RelatedTypeValue for this file.
   * 
   * @param relatedTypeValue
   *          The new RelatedTypeValue for this file.
   */
  public void setRelatedTypeValue(String relatedTypeValue) {
    this.relatedTypeValue = relatedTypeValue;
  } // end of setRelatedTypeValue method

  /**
   * @return The RelatedTypeValue for this file.
   */
  public String getRelatedTypeValue() {
    return this.relatedTypeValue;
  } // end of getRelatedTypeValue method

  /**
   * Sets the RelatedTypeID for this file.
   * 
   * @param relatedTypeID
   *          The new RelatedTypeID for this file.
   */
  public void setRelatedTypeID(int relatedTypeID) {
    this.relatedTypeID = relatedTypeID;
  } // end of setProjectID method

  /**
   * @return The RelatedTypeID for this file.
   */
  public int getRelatedTypeID() {
    return this.relatedTypeID;
  } // end of getRelatedTypeID method

  /**
   * Sets the RelatedFieldValue for this file.
   * 
   * @param relatedFieldValue
   *          The new RelatedFieldValue for this file.
   */
  public void setRelatedFieldValue(String relatedFieldValue) {
    this.relatedFieldValue = relatedFieldValue;
  } // end of setRelatedFieldValue method

  /**
   * @return The RelatedFieldValue for this file.
   */
  public String getRelatedFieldValue() {
    return this.relatedFieldValue;
  } // end of getRelatedFieldValue method

  /**
   * Sets the RelatedFieldID for this file.
   * 
   * @param relatedFieldID
   *          The new RelatedFieldID for this file.
   */
  public void setRelatedFieldID(int relatedFieldID) {
    this.relatedFieldID = relatedFieldID;
  } // end of setRelatedFieldID method

  /**
   * @return The RelatedFieldID for this file.
   */
  public int getRelatedFieldID() {
    return this.relatedFieldID;
  } // end of getRelatedFieldID method

  /**
   * getEndday
   * 
   * @return String
   */
  public String getEndday() {
    return this.endday;
  }

  /**
   * setEndday
   * 
   * @param endday
   *          String
   */
  public void setEndday(String endday) {
    this.endday = endday;
  }

  /**
   * setEndmonth
   * 
   * @param endmonth
   *          String
   */
  public void setEndmonth(String endmonth) {
    this.endmonth = endmonth;
  }

  /**
   * getEndmonth
   * 
   * @return String
   */
  public String getEndmonth() {
    return this.endmonth;
  }

  /**
   * setEndyear
   * 
   * @param endyear
   *          String
   */
  public void setEndyear(String endyear) {
    this.endyear = endyear;
  }

  /**
   * getEndyear
   * 
   * @return String
   */
  public String getEndyear() {
    return this.endyear;
  }

  /**
   * getStartday
   * 
   * @return String
   */
  public String getStartday() {
    return this.startday;
  }

  /**
   * setStartday
   * 
   * @param startday
   *          String
   */
  public void setStartday(String startday) {
    this.startday = startday;
  }

  /**
   * setStartmonth
   * 
   * @param startmonth
   *          String
   */
  public void setStartmonth(String startmonth) {
    this.startmonth = startmonth;
  }

  /**
   * getStartmonth
   * 
   * @return String
   */
  public String getStartmonth() {
    return this.startmonth;
  }

  /**
   * getStartyear
   * 
   * @return String
   */
  public String getStartyear() {
    return this.startyear;
  }

  /**
   * setStartyear
   * 
   * @param startyear
   *          String
   */
  public void setStartyear(String startyear) {
    this.startyear = startyear;
  }

  /**
   * getEmployeeHandBookFlag
   * 
   * @return String
   */
  public String getEmployeeHandBookFlag() {
    return this.employeeHandBookFlag;
  }

  /**
   * setEmployeeHandBookFlag
   * 
   * @param employeeHandBookFlag
   *          String
   */
  public void setEmployeeHandBookFlag(String employeeHandBookFlag) {
    this.employeeHandBookFlag = employeeHandBookFlag;
  }

}
