/*
 * $RCSfile: PrintTemplateVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:46 $ - $Author: mking_cv $
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

package com.centraview.printtemplate;

import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVAudit;

/**
 * PrintTemplateVO.java
 * This class stores the properties of PrintTemplate
 */
public class PrintTemplateVO extends CVAudit
{
  public static final String PRINT = "PRINT";
  public static final String EMAIL = "EMAIL";
  public static final String FAX = "FAX";
  private int ptcategoryId;
  private String catName;
  private int artifactId;
  private int parentId;
  private String catType;
  private int ptdetailId;
  private String artifactName;
  private String isDefault;
  private String ptData;
  private String ptname;
  private int userid;
  private String ptsubject;

  public PrintTemplateVO()
  {
    super();
  }
  
  /**
   * Populates the VO with the data from the passed in form.
   * Coded to work with the printTemplateForm in struts-administration.xml
   * @param form
   */
  public PrintTemplateVO(DynaActionForm form)
  {
    this.ptcategoryId = ((Integer)form.get("categoryId")).intValue();
    this.isDefault = (String)form.get("isDefault");
    this.ptData = (String)form.get("templateData");
    this.ptname = (String)form.get("templateName");
    this.userid = ((Integer)form.get("userId")).intValue();
    this.artifactId = ((Integer)form.get("artifactId")).intValue();
    this.ptsubject = (String)form.get("subject");
    this.ptdetailId = ((Integer)form.get("id")).intValue();
  }
  /**
   * gets the ArtifactId
   * @return  int
   */
  public int getArtifactId()
  {
    return this.artifactId;
  }

  /**
   * set ArtifactId
   * @param   artifactId
   */
  public void setArtifactId(int artifactId)
  {
    this.artifactId = artifactId;
  }

  /**
   * gets the ArtifactName
   * @return  string
   */
  public String getArtifactName()
  {
    return this.artifactName;
  }

  /**
   * set ArtifactName
   * @param   artifactName
   */
  public void setArtifactName(String artifactName)
  {
    this.artifactName = artifactName;
  }

  /**
   * gets the CatName
   * @return  string
   */
  public String getCatName()
  {
    return this.catName;
  }

  /**
   * set catName
   * @param   catName
   */
  public void setCatName(String catName)
  {
    this.catName = catName;
  }

  /**
   * gets the CatType
   * @return  string
   */
  public String getCatType()
  {
    return this.catType;
  }

  /**
   * set catType
   * @param   catType
   */
  public void setCatType(String catType)
  {
    if (!(catType.equals(PRINT) || catType.equals(EMAIL) || catType.equals(FAX)))
    {
      this.catType = PRINT;
    }
    else
    {
      this.catType = catType;
    }
  }

  /**
   * gets the IsDefault
   * @return  string
   */
  public String getIsDefault()
  {
    return this.isDefault;
  }

  /**
   * set isDefault
   * @param   isDefault
   */
  public void setIsDefault(String isDefault)
  {
    this.isDefault = isDefault;
  }

  /**
   * gets the ParentId
   * @return  string
   */
  public int getParentId()
  {
    return this.parentId;
  }

  /**
   * set parentId
   * @param   parentId
   */
  public void setParentId(int parentId)
  {
    this.parentId = parentId;
  }

  /**
   * gets the PtcategoryId
   * @return  string
   */
  public int getPtcategoryId()
  {
    return this.ptcategoryId;
  }

  /**
   * set ptcategoryId
   * @param   ptcategoryId
   */
  public void setPtcategoryId(int ptcategoryId)
  {
    this.ptcategoryId = ptcategoryId;
  }

  /**
   * gets the PtData
   * @return  string
   */
  public String getPtData()
  {
    return this.ptData;
  }

  /**
   * set ptData
   * @param   ptData
   */
  public void setPtData(String ptData)
  {
    this.ptData = ptData;
  }

  /**
   * gets the PtdetailId
   * @return  string
   */
  public int getPtdetailId()
  {
    return this.ptdetailId;
  }

  /**
   * set ptdetailId
   * @param   ptdetailId
   */
  public void setPtdetailId(int ptdetailId)
  {
    this.ptdetailId = ptdetailId;
  }

  /**
   * gets the Ptname
   * @return  string
   */
  public String getPtname()
  {
    return this.ptname;
  }

  /**
   * set ptname
   * @param   ptname
   */
  public void setPtname(String ptname)
  {
    this.ptname = ptname;
  }

  /**
   * gets the Userid
   * @return  int
   */
  public int getUserid()
  {
    return this.userid;
  }

  /**
   * set Userid
   * @param   Userid
   */
  public void setUserid(int userid)
  {
    this.userid = userid;
  }

  public String getPtsubject()
  {
    return this.ptsubject;
  }

  public void setPtsubject(String ptsubject)
  {
    this.ptsubject = ptsubject;
  }
  
  /**
   * Populates the passed in form with the appropriate fields.
   * Coded to work with the printTemplateForm in struts-administration.xml
   * @param form
   */
  public void populateFormBean(DynaActionForm form)
  {
    form.set("id", new Integer(this.ptdetailId));
    form.set("categoryId", new Integer(this.ptcategoryId));
    form.set("isDefault", this.isDefault);
    form.set("templateData", this.ptData);
    form.set("templateName", this.ptname);
    form.set("userId", new Integer(this.userid));
    form.set("artifactId", new Integer(this.artifactId));
    form.set("subject", this.ptsubject);
  }
}
