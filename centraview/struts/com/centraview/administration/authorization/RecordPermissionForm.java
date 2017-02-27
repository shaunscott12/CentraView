/*
 * $RCSfile: RecordPermissionForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:35 $ - $Author: mking_cv $
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

package com.centraview.administration.authorization;

import java.util.Collection;

public class RecordPermissionForm extends org.apache.struts.action.ActionForm
{
  private String noteId;
  private String view;
  private String modify;
  /** there are *awesome* javascript reasons why this is deleten and not delete */
  private String deleten;
  private int moduleID = 0;
  private Collection employeeList;
  private String employeeSelect = "";

  public String getDeleten()
  {
    return this.deleten;
  }

  public void setDeleten(String deleten)
  {
    this.deleten = deleten;
  }

  public String getModify()
  {
    return this.modify;
  }

  public void setModify(String modify)
  {
    this.modify = modify;
  }

  public String getNoteId()
  {
    return this.noteId;
  }

  public void setNoteId(String noteId)
  {
    this.noteId = noteId;
  }

  public String getView()
  {
    return this.view;
  }

  public void setView(String view)
  {
    this.view = view;
  }

  public void setModuleID(int moduleID)
  {
    this.moduleID = moduleID;
  }

  public int getModuleID()
  {
    return (this.moduleID);
  }
  public final Collection getEmployeeList()
  {
    return employeeList;
  }
  public final void setEmployeeList(Collection employeeList)
  {
    this.employeeList = employeeList;
  }
  public final String getEmployeeSelect()
  {
    return employeeSelect;
  }
  public final void setEmployeeSelect(String employeeSelect)
  {
    this.employeeSelect = employeeSelect;
  }
}