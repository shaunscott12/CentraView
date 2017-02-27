/*
 * $RCSfile: ExpenseFormVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:24 $ - $Author: mking_cv $
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

import org.apache.struts.action.ActionForm;

import com.centraview.common.UserObject;
import com.centraview.hr.expenses.HrExpenseForm;

public class ExpenseFormVOX extends ExpenseFormVO
{
  private ExpenseFormVO objExpenseFormVO = null;

	public ExpenseFormVOX(UserObject objUserObject, ActionForm AexpenseForm)
	{
		HrExpenseForm expenseForm = (HrExpenseForm)AexpenseForm;

		this.setExpenseFormID(expenseForm.getExpenseFormID());
		this.setReportingId(expenseForm.getReporttoID());
		this.setReportingTo(expenseForm.getReportingTo());
		this.setEmployeeId(expenseForm.getEmployeeID());
		this.setEmployee(expenseForm.getEmployee());

		int individualId = objUserObject.getIndividualID();

		this.setCreatedBy(individualId);
		this.setModifiedBy(individualId);
		this.setStatus(expenseForm.getStatus());

		String sCreatedDate = expenseForm.getCreatedDate();

		java.sql.Date fromDate = new java.sql.Date(Integer.parseInt(expenseForm.getFromyear()), Integer.parseInt(expenseForm.getFrommonth())-1, Integer.parseInt(expenseForm.getFromday()));
		this.setFrom(fromDate);

		java.sql.Date toDate = new java.sql.Date(Integer.parseInt(expenseForm.getToyear()), Integer.parseInt(expenseForm.getTomonth())-1, Integer.parseInt(expenseForm.getToday()));
		this.setTo(toDate);

		this.setDescription(expenseForm.getFormDescription());
		this.setAmount(expenseForm.getAmount());
		this.setNotes(expenseForm.getNotes());
		this.setHrExpenseLines(expenseForm.getItemLines());
	}   // end ExpenseFormVOX(UserObject,ActionForm) method

  public ExpenseFormVO getExpenseFormVO()
  {
    return super.getVO();
  }

}   // end class definition

