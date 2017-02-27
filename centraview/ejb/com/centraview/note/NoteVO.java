/*
 * $RCSfile: NoteVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:44 $ - $Author: mking_cv $
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

import com.centraview.common.CVAudit;

/**
 * This class stores the properties of Note
 *
 * @author   Iqbal Khan
 */
public class NoteVO extends CVAudit
{
  public static final String NP_HIGH = "HIGH";
  public static final String NP_MEDIUM = "MEDIUM";
  public static final String NP_LOW = "LOW";
  private int noteId;
  private String title;
  private String detail;
  private String priority;
  private int relateEntity;
  private String relateEntityName;
  private int relateIndividual;
  private String relateIndividualName;
  private int projectId;

  /**
   * Constructor
   *
   */
  public NoteVO()
  {
    super();
  }

  /**
   * gets the NoteID
   *
   * @return  int
   */
  public int getNoteId()
  {
    return this.noteId;
  }

  /**
   * set Note ID
   *
   * @param   noteId
   */
  public void setNoteId(int noteId)
  {
    this.noteId = noteId;
  }

  /**
   * gets detail
   *
   * @return     String
   */
  public String getDetail()
  {
    return this.detail;
  }

  /**
   * sets detail
   *
   * @param   detail
   */
  public void setDetail(String detail)
  {
    this.detail = detail;
  }

  /**
   * gets the priority
   *
   * @return String
   */
  public String getPriority()
  {
    return this.priority;
  }

  /**
   * sets the priority
   *
   * @param   priority
   */
  public void setPriority(String priority)
  {
    if (!(priority.equals(NP_HIGH) || priority.equals(NP_LOW)
        || priority.equals(NP_MEDIUM)))
    {
      this.priority = NP_MEDIUM;
    }
    else
    {
      this.priority = priority;
    }
  }

  /**
   * gets the title
   *
   * @return  String
   */
  public String getTitle()
  {
    return this.title;
  }

  /**
   * sets the title
   *
   * @param   title
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   * gets the RelateEntity
   *
   * @return  int
   */
  public int getRelateEntity()
  {
    return this.relateEntity;
  }

  /**
   * sets the RelateEntity
   *
   * @param   relateEntity
   */
  public void setRelateEntity(int relateEntity)
  {
    this.relateEntity = relateEntity;
  }

  /**
   * gets the String
   *
   * @return   String
   */
  public String getRelateEntityName()
  {
    return this.relateEntityName;
  }

  /**
   * sets the relateEntityName
   *
   * @param   relateEntityName
   */
  public void setRelateEntityName(String relateEntityName)
  {
    this.relateEntityName = relateEntityName;
  }

  /**
   * gets the RelateIndividual
   *
   * @return int
   */
  public int getRelateIndividual()
  {
    return this.relateIndividual;
  }

  /**
   * sets the RelateIndividual
   *
   * @param   relateIndividual
   */
  public void setRelateIndividual(int relateIndividual)
  {
    this.relateIndividual = relateIndividual;
  }

  /**
   * get the String
   *
   * @return    String
   */
  public String getRelateIndividualName()
  {
    return this.relateIndividualName;
  }

  /**
   * sets relateIndividualName
   *
   * @param   relateIndividualName
   */
  public void setRelateIndividualName(String relateIndividualName)
  {
    this.relateIndividualName = relateIndividualName;
  }
  /**
   * gets the project Id
   *
   * @return  int
   */
  public int getProjectId()
  {
    return this.projectId;
  }

  /**
   * set project ID
   *
   * @param   projectId
   */
  public void setProjectId(int projectId)
  {
    this.projectId = projectId;
  }

}
