/*
 * $RCSfile: ExpenseFormVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:24 $ - $Author: mking_cv $
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


package com.centraview.hr.helper;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import com.centraview.hr.expenses.HrExpenseLines;

public class ExpenseFormVO implements Serializable
{
  private int expenseFormId;
  private int expenseId;
  private int reportingId;
  private String reportingto;
  private int employeeId;
  private String employee;
  private int createdby;
  private String creatorName;
  private Timestamp createdDate;
  private int modifiedby;
  private Timestamp modifiedDate;
  private String modifiedByName;
  private String description = new String();
  private String notes = new String();
  private String status;
  private Date from = null;
  private Date to = null;
  private float amount;
  private HrExpenseLines hrexpenseLines;

  public void setExpenseFormID(int newExpenseFormID)
  {
    this.expenseFormId = newExpenseFormID;
  }

  public int getExpenseFormID()
  {
    return this.expenseFormId;
  }

  public void setExpenseID(int newExpenseID)
  {
    this.expenseId = newExpenseID;
  }

  public int getExpenseID()
  {
    return this.expenseId;
  }

  public void setReportingId(int newReportingID)
  {
    this.reportingId = newReportingID;
  }

  public int getReportingId()
  {
    return this.reportingId;
  }

  public void setReportingTo(String newReportingTo)
  {
    this.reportingto = newReportingTo;
  }

  public String getReportingTo()
  {
    return this.reportingto;
  }

  public void setEmployeeId(int newEmployeeID)
  {
    this.employeeId = newEmployeeID;
  }

  public int getEmployeeId()
  {
    return this.employeeId;
  }

  public void setEmployee(String newEmployee)
  {
    this.employee = newEmployee;
  }

  public String getEmployee()
  {
    return this.employee;
  }

  public void setCreatedBy(int newCreatedBy)
  {
    this.createdby = newCreatedBy;
  }

  public int getCreatedBy()
  {
    return this.createdby;
  }

  public void setModifiedBy(int newModifiedBy)
  {
    this.modifiedby = newModifiedBy;
  }

  public int getModifiedBy()
  {
    return this.modifiedby;
  }

  public void setDescription(String newDescription)
  {
    this.description = newDescription;
  }

  public String getDescription()
  {
    return this.description;
  }

  public void setNotes(String newNotes)
  {
    this.notes = newNotes;
  }

  public String getNotes()
  {
    return this.notes;
  }

  public void setFrom(Date newFrom)
  {
    this.from = newFrom;
  }

  public Date getFrom()
  {
    return this.from;
  }

  public void setTo(Date newTo)
  {
    this.to = newTo;
  }

  public Date getTo()
  {
    return this.to;
  }

  public void setCreatedDate(Timestamp newCreatedDate)
  {
    this.createdDate = newCreatedDate;
  }

  public Timestamp getCreatedDate()
  {
    return this.createdDate;
  }

  public void setModifiedDate(Timestamp newModifiedDate)
  {
    this.modifiedDate = newModifiedDate;
  }

  public Timestamp getModifiedDate()
  {
    return this.modifiedDate;
  }

  public float getAmount()
  {
    return this.amount;
  }

  public void setAmount(float newAmount)
  {
    this.amount = newAmount;
  }

  public void setHrExpenseLines(HrExpenseLines newHrExpenseLines)
  {
    this.hrexpenseLines = newHrExpenseLines;
  }

  public HrExpenseLines getHrExpenseLines()
  {
    return this.hrexpenseLines;
  }

  public ExpenseFormVO getVO()
  {
    return this;
  }

  public String getModifiedByName()
  {
    return this.modifiedByName;
  }

  public void setModifiedByName(String newModifiedByName)
  {
    this.modifiedByName = newModifiedByName;
  }

  public String getCreatorName()
  {
    return this.creatorName;
  }

  public void setCreatorName(String newCreatorName)
  {
    this.creatorName = newCreatorName;
  }

  public String getStatus()
  {
    return this.status;
  }

  public void setStatus(String newStatus)
  {
    this.status = newStatus;
  }
}   // end class definition

