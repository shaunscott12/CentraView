/*
 * $RCSfile: ExpenseVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:24 $ - $Author: mking_cv $
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


package com.centraview.account.expense;

import com.centraview.account.common.ItemLines;
import com.centraview.common.CVAudit;

public class ExpenseVO extends CVAudit
{

  /**  Elements for Expense **/

  private String externalID;

  private int expenseID;

  private int glAccountIDValue;

  private float amount;

  private String title = "";

  private String expenseDescription;
  private String notes;

  private String entity;
  private int entityIDValue;

  private java.sql.Date dateEntered;

  private int statusIDValue;
  private String status;

  private int employeeIDValue;
  private String employee;

  private int projectIDValue;
  private String project;

  private int opportunityIDValue;
  private String opportunity;

  private int supportTicketIDValue;
  private String supportTicket;

  /**  Form elements for Expense ends here**/

  /**  Elements for Items **/
  private ItemLines itemLines ;

  /**  Elements for Items ends here**/

  public ExpenseVO()
  {
   itemLines = new ItemLines();
  }

  public float getAmount()
  {
    return this.amount;
  }

  public void setAmount(float amount)
  {
    this.amount = amount;
  }


  public java.sql.Date getDateEntered()
  {
    return this.dateEntered;
  }

  public void setDateEntered( java.sql.Date dateEntered)
  {
    this.dateEntered = dateEntered;
  }


  public int getEmployeeIDValue()
  {
    return this.employeeIDValue;
  }

  public void setEmployeeIDValue(int employeeIDValue)
  {
    this.employeeIDValue = employeeIDValue;
  }


  public int getEntityIDValue()
  {
    return this.entityIDValue;
  }

  public void setEntityIDValue(int entityIDValue)
  {
    this.entityIDValue = entityIDValue;
  }


  public String getExpenseDescription()
  {
    return this.expenseDescription;
  }

  public void setExpenseDescription(String expenseDescription)
  {
    this.expenseDescription = expenseDescription;
  }


  public int getExpenseID()
  {
    return this.expenseID;
  }

  public void setExpenseID(int expenseID)
  {
    this.expenseID = expenseID;
  }


  public int getGlAccountIDValue()
  {
    return this.glAccountIDValue;
  }

  public void setGlAccountIDValue(int glAccountIDValue)
  {
    this.glAccountIDValue = glAccountIDValue;
  }


  public ItemLines getItemLines()
  {
    return this.itemLines;
  }

  public void setItemLines(ItemLines itemLines)
  {
    this.itemLines = itemLines;
  }


  public int getOpportunityIDValue()
  {
    return this.opportunityIDValue;
  }

  public void setOpportunityIDValue(int opportunityIDValue)
  {
    this.opportunityIDValue = opportunityIDValue;
  }


  public int getProjectIDValue()
  {
    return this.projectIDValue;
  }

  public void setProjectIDValue(int projectIDValue)
  {
    this.projectIDValue = projectIDValue;
  }


  public int getStatusIDValue()
  {
    return this.statusIDValue;
  }

  public void setStatusIDValue(int statusIDValue)
  {
    this.statusIDValue = statusIDValue;
  }


  public int getSupportTicketIDValue()
  {
    return this.supportTicketIDValue;
  }

  public void setSupportTicketIDValue(int supportTicketIDValue)
  {
    this.supportTicketIDValue = supportTicketIDValue;
  }


  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }


  public String getEntity()
  {
    return this.entity;
  }

  public void setEntity(String entity)
  {
    this.entity = entity;
  }


  public String getOpportunity()
  {
    return this.opportunity;
  }

  public void setOpportunity(String opportunity)
  {
    this.opportunity = opportunity;
  }


  public String getProject()
  {
    return this.project;
  }

  public void setProject(String project)
  {
    this.project = project;
  }


  public String getEmployee()
  {
    return this.employee;
  }

  public void setEmployee(String employee)
  {
    this.employee = employee;
  }


  public String getSupportTicket()
  {
    return this.supportTicket;
  }

  public void setSupportTicket(String supportTicket)
  {
    this.supportTicket = supportTicket;
  }


  public String getStatus()
  {
    return this.status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }
  public ExpenseVO getVO()
  {
    return this;
  }


  public String getNotes()
  {
    return this.notes;
  }

  public void setNotes(String notes)
  {
    this.notes = notes;
  }


  public String getExternalID()
  {
    return this.externalID;
  }

  public void setExternalID(String externalID)
  {
    this.externalID = externalID;
  }

}














