/*
 * $RCSfile: NoteForm.java,v $    $Revision: 1.3 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class NoteForm extends org.apache.struts.action.ActionForm {
  private static Logger logger = Logger.getLogger(NoteForm.class);
  private String noteId = "0";
  private String view;
  private String modify;
  private String deleten;
  private String title = "";
  private String detail = "";
  private String entityid = "";
  private String entityname = "";
  private String individualid = "0";
  private String individualname = "";
  private String[] allindividual = null;
  private String[] viewpermission = null;
  private String[] modifypermission = null;
  private String[] deletepermission = null;
  private Vector viewpermissionvec = null;
  private Vector modifypermissionvec = null;
  private Vector deletepermissionvec = null;
  private String createdid = "";
  private String createdby = "";
  private String createddate = "";
  private String modifieddate = "";
  private String projectId = "0";
  private String listScope = "my";

  /*
   * Validates user input data @param mapping ActionMapping @param request
   * HttpServletRequest @return errors ActionErrors
   */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {

    // initialize new actionerror object
    ActionErrors errors = new ActionErrors();

    try {
      String tempTitle = this.getTitle();
      if (tempTitle == null || tempTitle.trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Title"));
      }

      if (errors != null) {
        request.setAttribute("noteform", this);
      }
    } catch (Exception e) {
      logger.error("[validate]: Exception", e);
    }
    return errors;
  }

  /*
   * Returns string array of individual @return allindividual String[]
   */
  public String[] getAllindividual()
  {
    return this.allindividual;
  }

  /*
   * Set allindividual string array @param allindividual String[]
   */
  public void setAllindividual(String[] allindividual)
  {
    this.allindividual = allindividual;
  }

  /*
   * Returns createdby @return createdby String
   */
  public String getCreatedby()
  {
    return this.createdby;
  }

  /*
   * Set created by @param createdby String
   */
  public void setCreatedby(String createdby)
  {
    this.createdby = createdby;
  }

  /*
   * Returns createddate @return createddate String
   */
  public String getCreateddate()
  {
    return this.createddate;
  }

  /*
   * Set createddate @param createddate String
   */
  public void setCreateddate(String createddate)
  {
    this.createddate = createddate;
  }

  /*
   * Returns deletepermission string array @return deletepermission String[]
   */
  public String[] getDeletepermission()
  {
    return this.deletepermission;
  }

  /*
   * Set deletepermission string array @param deletepermission String[]
   */
  public void setDeletepermission(String[] deletepermission)
  {
    this.deletepermission = deletepermission;
  }

  /*
   * Returns deletepermissionvec vector @return deletepermissionvec Vector
   */
  public Vector getDeletepermissionvec()
  {
    return this.deletepermissionvec;
  }

  /*
   * Set deletepermissionvec vector @param deletepermissionvec Vector
   */
  public void setDeletepermissionvec(Vector deletepermissionvec)
  {
    this.deletepermissionvec = deletepermissionvec;
  }

  /*
   * Returns detail @return detail String
   */
  public String getDetail()
  {
    return this.detail;
  }

  /*
   * Set detail @param detail String
   */
  public void setDetail(String detail)
  {
    this.detail = detail;
  }

  /*
   * Returns entityid @return entityid String
   */
  public String getEntityid()
  {
    return this.entityid;
  }

  /*
   * Set entityid @param entityid String
   */
  public void setEntityid(String entityid)
  {
    this.entityid = entityid;
  }

  /*
   * Returns entityname @return entityname String
   */
  public String getEntityname()
  {
    return this.entityname;
  }

  /*
   * Set entityname @param entityname String
   */
  public void setEntityname(String entityname)
  {
    this.entityname = entityname;
  }

  /*
   * Returns individualid @return individualid String
   */
  public String getIndividualid()
  {
    return this.individualid;
  }

  /*
   * Set individualid @param individualid String
   */
  public void setIndividualid(String individualid)
  {
    this.individualid = individualid;
  }

  /*
   * Returns individualname @return individualname String
   */
  public String getIndividualname()
  {
    return this.individualname;
  }

  /*
   * Set individualname @param individualname String
   */
  public void setIndividualname(String individualname)
  {
    this.individualname = individualname;
  }

  /*
   * Returns modifieddate @return modifieddate String
   */
  public String getModifieddate()
  {
    return this.modifieddate;
  }

  /*
   * Set modifieddate @param modifieddate String
   */
  public void setModifieddate(String modifieddate)
  {
    this.modifieddate = modifieddate;
  }

  /*
   * Returns modifypermission string array @return modifypermission String[]
   */
  public String[] getModifypermission()
  {
    return this.modifypermission;
  }

  /*
   * Set modifypermission string array @param modifypermission String[]
   */
  public void setModifypermission(String[] modifypermission)
  {
    this.modifypermission = modifypermission;
  }

  /*
   * Returns modifypermissionvec vector @return modifypermissionvec Vector
   */
  public Vector getModifypermissionvec()
  {
    return this.modifypermissionvec;
  }

  /*
   * Set modifypermissionvec vector @param modifypermissionvec Vector
   */
  public void setModifypermissionvec(Vector modifypermissionvec)
  {
    this.modifypermissionvec = modifypermissionvec;
  }

  /*
   * Returns noteid string @return noteid String
   */
  public String getNoteId()
  {
    return this.noteId;
  }

  /*
   * Set noteid string @param noteid String
   */
  public void setNoteId(String noteId)
  {
    this.noteId = noteId;
  }

  /*
   * Return title string @return title String
   */
  public String getTitle()
  {
    return this.title;
  }

  /*
   * Set title string @param title String
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /*
   * Return viewpermission string array @return viewpermission String[]
   */
  public String[] getViewpermission()
  {
    return this.viewpermission;
  }

  /*
   * Set viewpermission string array @param viewpermission String[]
   */
  public void setViewpermission(String[] viewpermission)
  {
    this.viewpermission = viewpermission;
  }

  /*
   * Return viewpermissionvec vector @return viewpermissionvec Vector
   */
  public Vector getViewpermissionvec()
  {
    return this.viewpermissionvec;
  }

  /*
   * Set viewpermissionvec vector @param viewpermissionvec Vector
   */
  public void setViewpermissionvec(Vector viewpermissionvec)
  {
    this.viewpermissionvec = viewpermissionvec;
  }

  /*
   * Return createdid string @return createdid String
   */
  public String getCreatedid()
  {
    return this.createdid;
  }

  /*
   * Set createdid string @param createdid String
   */
  public void setCreatedid(String createdid)
  {
    this.createdid = createdid;
  }

  public String getDeleten()
  {
    return this.deleten;
  }

  public void setDeleten(String deleten)
  {
    this.deleten = deleten;
  }

  public String getView()
  {
    return this.view;
  }

  public void setView(String view)
  {
    this.view = view;
  }

  public String getModify()
  {
    return this.modify;
  }

  public void setModify(String modify)
  {
    this.modify = modify;
  }

  /**
   * gets the project Id
   * @return int
   */
  public String getProjectId()
  {
    return this.projectId;
  }

  /**
   * set project ID
   * @param projectId
   */
  public void setProjectId(String projectId)
  {
    this.projectId = projectId;
  }

  /**
   * gets the listScope
   * @return listScope
   */
  public String getListScope()
  {
    return this.listScope;
  }

  /**
   * set listScope
   * @param listScope
   */
  public void setListScope(String listScope)
  {
    this.listScope = listScope;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer("");
    sb.append("noteId = [" + noteId + "]\n");
    sb.append("view = [" + view + "]\n");
    sb.append("modify = [" + modify + "]\n");
    sb.append("deleten = [" + deleten + "]\n");
    sb.append("title = [" + title + "]\n");
    sb.append("detail = [" + detail + "]\n");
    sb.append("entityid = [" + entityid + "]\n");
    sb.append("entityname = [" + entityname + "]\n");
    sb.append("individualid = [" + individualid + "]\n");
    sb.append("individualname = [" + individualname + "]\n");
    sb.append("allindividual = [" + allindividual + "]\n");
    sb.append("viewpermission  = [" + viewpermission + "]\n");
    sb.append("modifypermission  = [" + modifypermission + "]\n");
    sb.append("deletepermission  = [" + deletepermission + "]\n");
    sb.append("viewpermissionvec  = [" + viewpermissionvec + "]\n");
    sb.append("modifypermissionvec  = [" + modifypermissionvec + "]\n");
    sb.append("deletepermissionvec  = [" + deletepermissionvec + "]\n");
    sb.append("createdid = [" + createdid + "]\n");
    sb.append("createdby = [" + createdby + "]\n");
    sb.append("createddate = [" + createddate + "]\n");
    sb.append("modifieddate = [" + modifieddate + "]\n");
    sb.append("projectId = [" + projectId + "]\n");
    sb.append("listScope = [" + listScope + "]\n");
    return sb.toString();
  }
}
