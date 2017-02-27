/*
 * $RCSfile: ExpenseVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:24 $ - $Author: mking_cv $
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


public class ExpenseVOX extends  ExpenseVO
{

  public ExpenseVOX(ExpenseForm expenseForm)
  {
    this.setExpenseID(expenseForm.getExpenseID());
    this.setGlAccountIDValue(expenseForm.getGlAccountIDValue());
    this.setAmount(expenseForm.getAmount());
    this.setTitle(expenseForm.getTitle());
    this.setExpenseDescription(expenseForm.getExpenseDescription());
    this.setEntityIDValue(expenseForm.getEntityIDValue());
    this.setStatusIDValue(expenseForm.getStatusIDValue());
    this.setEmployeeIDValue(expenseForm.getEmployeeIDValue());
    this.setProjectIDValue(expenseForm.getProjectIDValue());
    this.setOpportunityIDValue(expenseForm.getOpportunityIDValue());
    this.setSupportTicketIDValue(expenseForm.getSupportTicketIDValue());
    this.setNotes(expenseForm.getNotes());
    this.setItemLines(expenseForm.getItemLines());
  }

  public ExpenseVO getVO()
  {
    return super.getVO();
  }
}














