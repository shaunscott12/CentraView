/*
 * $RCSfile: CategoryForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:09 $ - $Author: mcallist $
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

package com.centraview.support.knowledgebase;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class CategoryForm extends org.apache.struts.action.ActionForm {

  /*
   * Stores category
   */
  private String catId;
  /*
   * Stores categoryname
   */
  private String categoryname;

  /*
   * Stores parentcategory
   */
  private String parentcategory;
  private int parentcatid;
  /*
   * Stores owner
   */
  private String owner;

  /*
   * Stores owner
   */
  private String ownerid;

  /*
   * Stores created
   */
  private String created;
  /*
   * Stores modified
   */
  private String modified;

  /*
   * Stores status
   */
  private String status;

  /*
   * Stores publishToCustomerView
   */
  private String publishToCustomerView;

  private String title;

  private String detail;

  private String category;

  /*
   * Returns catid string @return catid String
   */
  public String getCatId()
  {
    return this.catId;
  }

  /*
   * Set threadid string @param threadid String
   */
  public void setCatId(String catId)
  {
    this.catId = catId;
  }

  public String getCategoryname()
  {
    return this.categoryname;
  }

  public void setCategoryname(String categoryname)
  {
    this.categoryname = categoryname;
  }

  public String getCreated()
  {
    return this.created;
  }

  public void setCreated(String created)
  {
    this.created = created;
  }

  public String getModified()
  {
    return this.modified;
  }

  public void setModified(String modified)
  {
    this.modified = modified;
  }

  public String getOwner()
  {
    return this.owner;
  }

  public void setOwner(String owner)
  {
    this.owner = owner;
  }

  public String getParentcategory()
  {
    return this.parentcategory;
  }

  public void setParentcategory(String parentcategory)
  {
    this.parentcategory = parentcategory;
  }

  public int getParentcatid()
  {
    return this.parentcatid;
  }

  public void setParentcatid(int parentcategory)
  {
    this.parentcatid = parentcategory;
  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    ActionErrors errors = new ActionErrors();
    try {
      if (this.getCategoryname() == null || this.getCategoryname().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.generalRequiredField",
            "Category Name"));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return errors;
  }

  public String getOwnerid()
  {
    return this.ownerid;
  }

  public void setOwnerid(String ownerid)
  {
    this.ownerid = ownerid;
  }

  public String getStatus()
  {
    return this.status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }

  public String getPublishToCustomerView()
  {
    return this.publishToCustomerView;
  }

  public void setPublishToCustomerView(String publishToCustomerView)
  {
    this.publishToCustomerView = publishToCustomerView;
  }

  /**
   * @return Returns the title.
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * @param title The title to set.
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   * @return Returns the detail.
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * @param detail The detail to set.
   */
  public void setDetail(String detail)
  {
    this.detail = detail;
  }

  /**
   * @return Returns the category.
   */
  public String getCategory()
  {
    return category;
  }

  /**
   * @param category The category to set.
   */
  public void setCategory(String category)
  {
    this.category = category;
  }
}
