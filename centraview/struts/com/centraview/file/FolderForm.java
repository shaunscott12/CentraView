/*
 * $RCSfile: FolderForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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
 * FolderForm.java
 *
 * @author   Diwakar Purighalla  
 * @version  1.0    
 * 
 */
package com.centraview.file;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/*
 * Class gets and sets the user input data during adding or editing of file.
 */
public class FolderForm extends org.apache.struts.action.ActionForm {

  private String folderId;
  private String foldername;
  private String subfoldername;
  private String access = "PRIVATE";
  private String description;
  private String[] allindividual;
  private String[] viewpermission;
  private String[] modifypermission;
  private String[] deletepermission;
  private Vector viewpermissionvec;
  private Vector modifypermissionvec;
  private Vector deletepermissionvec;
  private String owner;
  private String ownerId;
  private String createdBy;
  private String modifiedBy;
  private String parentId;

  public FolderForm() {}

  /**
   * @param actionMapping
   * @param request
   */
  public void reset(ActionMapping actionMapping, HttpServletRequest request)
  {}

  /**
   * Get the access
   * @return String
   */
  public String getAccess()
  {
    return this.access;
  }

  /**
   * set Access
   * @param access
   */
  public void setAccess(String access)
  {
    this.access = access;
  }

  /**
   * Get the individuals
   * @return String[]
   */
  public String[] getAllindividual()
  {
    return this.allindividual;
  }

  /**
   * Set the individuals
   * @param allindividual
   */
  public void setAllindividual(String[] allindividual)
  {
    this.allindividual = allindividual;
  }

  /**
   * Get Delete Permissions
   * @return String[]
   */
  public String[] getDeletepermission()
  {
    return this.deletepermission;
  }

  /**
   * Set Delete Permissions
   * @param deletepermission
   */
  public void setDeletepermission(String[] deletepermission)
  {
    this.deletepermission = deletepermission;
  }

  /**
   * Get Delete Permission Vector
   * @return Vector
   */
  public Vector getDeletepermissionvec()
  {
    return this.deletepermissionvec;
  }

  /**
   * Set Delete Permission Vector
   * @param deletepermissionvec
   */
  public void setDeletepermissionvec(Vector deletepermissionvec)
  {
    this.deletepermissionvec = deletepermissionvec;
  }

  /**
   * Get the Description
   * @return Description
   */
  public String getDescription()
  {
    return this.description;
  }

  /**
   * Set Description
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Get the Foldername
   * @return String FolderName
   */
  public String getFoldername()
  {
    return this.foldername;
  }

  /**
   * Set The Foldername
   * @param foldername
   */
  public void setFoldername(String foldername)
  {
    this.foldername = foldername;
  }

  /**
   * Get the Modify permissions
   * @return String[] permissions
   */
  public String[] getModifypermission()
  {
    return this.modifypermission;
  }

  /**
   * Set the Modify permissions
   * @param String[] modifypermission
   */
  public void setModifypermission(String[] modifypermission)
  {
    this.modifypermission = modifypermission;
  }

  /**
   * Get the Modify permissions vector
   * @return Vector Permissions
   */
  public Vector getModifypermissionvec()
  {
    return this.modifypermissionvec;
  }

  /**
   * Set the Modify permissions
   * @param Vector Modify Permissions
   */
  public void setModifypermissionvec(Vector modifypermissionvec)
  {
    this.modifypermissionvec = modifypermissionvec;
  }

  /**
   * Get the Sub folder name
   * @return String foldername
   */
  public String getSubfoldername()
  {
    return this.subfoldername;
  }

  /**
   * Set Sub folder name
   * @param String sub foldername
   */
  public void setSubfoldername(String subfoldername)
  {
    this.subfoldername = subfoldername;
  }

  /**
   * Get View permissions
   * @return String[] Permissions
   */
  public String[] getViewpermission()
  {
    return this.viewpermission;
  }

  /**
   * Set the View Permissions
   * @param String[] view permissions
   */
  public void setViewpermission(String[] viewpermission)
  {
    this.viewpermission = viewpermission;
  }

  /**
   * Get The View permissions
   * @return Vector View Permissions
   */
  public Vector getViewpermissionvec()
  {
    return this.viewpermissionvec;
  }

  /**
   * Set View Permissions
   * @param Vector View Permissions
   */
  public void setViewpermissionvec(Vector viewpermissionvec)
  {
    this.viewpermissionvec = viewpermissionvec;
  }

  /**
   * Get The Folder Id
   * @return String FolderID
   */
  public String getFolderId()
  {
    return this.folderId;
  }

  /**
   * Set Folder ID
   * @param String Folder Id
   */
  public void setFolderId(String folderId)
  {
    this.folderId = folderId;
  }

  /**
   * Get Modified By
   * @return String Modified By
   */
  public String getModifiedBy()
  {
    return this.modifiedBy;
  }

  /**
   * Set Modified By
   * @param String Modified By
   */
  public void setModifiedBy(String modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  }

  /**
   * Get Created By
   * @return String Created By
   */
  public String getCreatedBy()
  {
    return this.createdBy;
  }

  /**
   * Set Created By
   * @param String CreatedBy
   */
  public void setCreatedBy(String createdBy)
  {
    this.createdBy = createdBy;
  }

  /**
   * Get The Owner
   * @return String Owner
   */
  public String getOwner()
  {
    return this.owner;
  }

  /**
   * Set The Owner
   * @param String Owner
   */
  public void setOwner(String owner)
  {
    this.owner = owner;
  }

  /**
   * Get The Parent ID
   * @return String Parent ID
   */
  public String getParentId()
  {
    return this.parentId;
  }

  /**
   * Set The Parent ID
   * @param String ParentId
   */
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }

  /**
   * Get the Owner ID
   * @return String OwnerID
   */
  public String getOwnerId()
  {
    return this.ownerId;
  }

  /**
   * Set the Owner ID
   * @param String OwnerId
   */
  public void setOwnerId(String ownerId)
  {
    this.ownerId = ownerId;
  }

  /*
   * Validates user input data @param mapping ActionMapping @param request
   * HttpServletRequest @return errors ActionErrors
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    ActionErrors errors = new ActionErrors();

    try {

      if (request.getParameter(FileConstantKeys.TYPEOFOPERATION).equals(FileConstantKeys.ADD)) {
        if (this.getFoldername() == null || this.getFoldername().trim().length() == 0) {
          errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
              "error.general.requiredField", "Description"));
        }
      }

      String folderName = this.getFoldername();

      if (folderName.indexOf("\\") >= 0 || folderName.indexOf("/") >= 0
          || folderName.indexOf(":") >= 0 || folderName.indexOf("*") >= 0
          || folderName.indexOf("?") >= 0 || folderName.indexOf("<") >= 0
          || folderName.indexOf(">") >= 0 || folderName.indexOf("|") >= 0
          || folderName.indexOf("\"") >= 0) {
        ActionMessage error = new ActionMessage("error.foldername.required", "");
        errors.add("error.foldername.required", error);
      }

      if (errors != null) {
        request.setAttribute(FileConstantKeys.TYPEOFFILE, FileConstantKeys.FOLDER);
        request.setAttribute(FileConstantKeys.CURRENTTAB, FileConstantKeys.DETAIL);
        request.setAttribute(FileConstantKeys.TYPEOFOPERATION, request
            .getParameter(FileConstantKeys.TYPEOFOPERATION));
        request.setAttribute(FileConstantKeys.WINDOWID, request
            .getParameter(FileConstantKeys.WINDOWID));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return errors;
  }

}
