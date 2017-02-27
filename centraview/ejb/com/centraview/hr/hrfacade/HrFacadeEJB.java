/*
 * $RCSfile: HrFacadeEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:25 $ - $Author: mking_cv $
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


package com.centraview.hr.hrfacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.common.EmployeeList;
import com.centraview.common.ExpenseFormList;
import com.centraview.common.TimeSheetList;
import com.centraview.hr.helper.ExpenseFormVO;
import com.centraview.hr.helper.TimeSheetVO;
import com.centraview.hr.hremployee.EmployeeLocal;
import com.centraview.hr.hremployee.EmployeeLocalHome;
import com.centraview.hr.hrexpenses.ExpenseFormPK;
import com.centraview.hr.hrexpenses.HrExpensesLocal;
import com.centraview.hr.hrexpenses.HrExpensesLocalHome;
import com.centraview.hr.hrlist.HrListsLocal;
import com.centraview.hr.hrlist.HrListsLocalHome;
import com.centraview.hr.timesheet.TimeSheetLocal;
import com.centraview.hr.timesheet.TimeSheetLocalHome;
import com.centraview.projects.helper.TimeSlipVO;
import com.centraview.projects.timeslip.TimeSlipLocal;
import com.centraview.projects.timeslip.TimeSlipLocalHome;

public class HrFacadeEJB implements SessionBean
{
	protected javax.ejb.SessionContext ctx;
	protected Context environment;
	private String dataSource = "MySqlDS";

	public void setSessionContext(SessionContext ctx)
	{
		this.ctx = ctx;
	}

	public void ejbActivate()   { }
	public void ejbPassivate()   { }
	public void ejbRemove()   { }
	public void ejbCreate()  { }

	public EmployeeList getEmployeeDetailList(int userID, HashMap info) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("EmployeeList",userID, this.dataSource))
			throw new AuthorizationFailedException("EmployeeList - getEmployeeDetailList");

		EmployeeList employeeList = null;
		try
		{
			InitialContext ic 			= CVUtility.getInitialContext();
			HrListsLocalHome home 		=(HrListsLocalHome)ic.lookup("local/HrLists");
			HrListsLocal remote 		=(HrListsLocal)home.create();
			remote.setDataSource(this.dataSource);
			employeeList				= (EmployeeList)remote.getEmployeeDetailList(userID,info);
		}
		catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.getEmployeeDetailList] Exception Thrown: "+e);
			e.printStackTrace();
		}

		return employeeList;

	}



	public ArrayList getEmployeeIds()
	{
		ArrayList employeeList = null;
		try
		{
			InitialContext ic 			= CVUtility.getInitialContext();
			EmployeeLocalHome home 		=(EmployeeLocalHome)ic.lookup("local/Employee");
			EmployeeLocal local 		 =(EmployeeLocal)home.create();
			local.setDataSource(this.dataSource);
			employeeList = (ArrayList)local.getEmployeeIds();
		}
		catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.getEmployeeIds] Exception Thrown: "+e);
			e.printStackTrace();
		}

		return employeeList;
	}

	public TimeSheetList getTimeSheetList(int userID, HashMap info) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("Time Sheets",userID, this.dataSource))
			throw new AuthorizationFailedException("TimeSheetList - getTimeSheetList");

		TimeSheetList timesheetlist = null;
		try
		{
			InitialContext ic 			= CVUtility.getInitialContext();
			HrListsLocalHome home 		=(HrListsLocalHome)ic.lookup("local/HrLists");
			HrListsLocal remote 		=(HrListsLocal)home.create();
			remote.setDataSource(this.dataSource);
			timesheetlist				= remote.getTimeSheetList(userID,info);
		}
		catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.getTimeSheetList] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return timesheetlist;
	}

	public ExpenseFormList getExpenseFormList(int userID, HashMap info) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("ExpenseForms",userID, this.dataSource))
			throw new AuthorizationFailedException("ExpenseForms - getExpenseFormList");

		ExpenseFormList expenseformlist = null;
		try
		{
			InitialContext ic 			= CVUtility.getInitialContext();
			HrListsLocalHome home 		=(HrListsLocalHome)ic.lookup("local/HrLists");
			HrListsLocal remote 		=(HrListsLocal)home.create();
			remote.setDataSource(this.dataSource);
			expenseformlist				= remote.getExpenseFormList(userID,info);
		}
		catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.getExpenseFormList] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return expenseformlist;
	}


	//For the Employee List Starts Here
	 public int addEmployee(int userId , int individualID) throws AuthorizationFailedException
	 {
 		if(!CVUtility.isModuleVisible("EmployeeList",userId, this.dataSource))
 			throw new AuthorizationFailedException("EmployeeList - addEmployee");

		int empId = 0;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			EmployeeLocalHome home = (EmployeeLocalHome)ic.lookup("local/Employee");
			EmployeeLocal remote = (EmployeeLocal) home.create();
			remote.setDataSource(this.dataSource);

			empId = remote.addEmployee( userId,  individualID);

		}catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.addEmployee] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return empId;
	 }

	//public void updateEmployee(int employeeId, EmployeeVO pvo);

	public void deleteEmployee( int individualID) throws AuthorizationFailedException
	{
	  	if(!CVUtility.isModuleVisible("EmployeeList",individualID, this.dataSource))
	  		throw new AuthorizationFailedException("EmployeeList - deleteEmployee");

	  	try
		{
			InitialContext ic = CVUtility.getInitialContext();
			EmployeeLocalHome home = (EmployeeLocalHome)ic.lookup("local/Employee");
			EmployeeLocal remote = (EmployeeLocal) home.create();
			remote.setDataSource(this.dataSource);

			remote.deleteEmployee(individualID);

		}catch(CreateException ce)
		{
			throw new EJBException(ce);
		}catch(NamingException ce)
		{
			throw new EJBException(ce);
		}
		return;
	}



	public int getTimeSheetID()
	{
		int  timesheetID = 0;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			TimeSheetLocalHome home = (TimeSheetLocalHome)ic.lookup("local/TimeSheet");
			TimeSheetLocal remote = (TimeSheetLocal) home.create();
			remote.setDataSource(this.dataSource);


			timesheetID = remote.getTimeSheetID();

		}catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.getTimeSheetID] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return timesheetID;
	}




	public int addTimeSheet(int userId , TimeSheetVO tsvo) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("Time Sheets",userId, this.dataSource))
			throw new AuthorizationFailedException("Time Sheets - addTimeSheet");

		int timesheetID = 0;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			TimeSheetLocalHome home = (TimeSheetLocalHome)ic.lookup("local/TimeSheet");
			TimeSheetLocal remote = (TimeSheetLocal) home.create();
			remote.setDataSource(this.dataSource);


			timesheetID = remote.addTimeSheet(userId,tsvo);

		}catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.addTimeSheet] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return timesheetID;
	}


	public TimeSheetVO getTimeSheet(int timesheetID)
	{
		TimeSheetVO tvo = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			TimeSheetLocalHome home = (TimeSheetLocalHome)ic.lookup("local/TimeSheet");
			TimeSheetLocal remote = (TimeSheetLocal) home.create();
			remote.setDataSource(this.dataSource);


			tvo = remote.getTimeSheet(timesheetID);

		}catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.getTimeSheet] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return tvo;
	}
	public void updateTimeSheet(int individualID, TimeSheetVO tsvo)
	{
		int timesheetID = 0;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			TimeSheetLocalHome home = (TimeSheetLocalHome)ic.lookup("local/TimeSheet");
			TimeSheetLocal remote = (TimeSheetLocal) home.create();
			remote.setDataSource(this.dataSource);

			remote.updateTimeSheet(individualID,tsvo);

		}catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.updateTimeSheet] Exception Thrown: "+e);
			e.printStackTrace();
		}

	}




	public void deleteTimeSheet(int individualID , int timesheetID) throws AuthorizationFailedException
	{
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			TimeSheetLocalHome home = (TimeSheetLocalHome)ic.lookup("local/TimeSheet");
			TimeSheetLocal remote = (TimeSheetLocal) home.create();
			remote.setDataSource(this.dataSource);


			remote.deleteTimeSheet(individualID,timesheetID);

		}catch(CreateException ce)
		{
			throw new EJBException(ce);
		}catch(NamingException ce)
		{
			throw new EJBException(ce);
		}
		return;
	}


	public Collection getStatusInfo()
	{
		Collection col = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			TimeSheetLocalHome home = (TimeSheetLocalHome)ic.lookup("local/TimeSheet");
			TimeSheetLocal remote = (TimeSheetLocal) home.create();
			remote.setDataSource(this.dataSource);


			col = remote.getStatusInfo();
		}catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.getStatusInfo] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return col;
	}



	public int addTimeSlip(int indvId, TimeSlipVO tsvo) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("Time Sheets",indvId, this.dataSource))
			throw new AuthorizationFailedException("Time Sheets - addTimeSlip");

		int timeslipID = 0;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			TimeSlipLocalHome home = (TimeSlipLocalHome)ic.lookup("local/TimeSlip");
			TimeSlipLocal remote = (TimeSlipLocal) home.create();
			remote.setDataSource(this.dataSource);
			timeslipID = remote.addTimeSlip(indvId,tsvo);

		}catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.addTimeSlip] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return timeslipID;
	}

	public void deleteTimeSlip(int individualID , String timeSlipIds) throws AuthorizationFailedException
	{
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			TimeSheetLocalHome home = (TimeSheetLocalHome)ic.lookup("local/TimeSheet");
			TimeSheetLocal remote = (TimeSheetLocal) home.create();
			remote.setDataSource(this.dataSource);
			remote.deleteTimeSlip(individualID,timeSlipIds);
		}catch(CreateException ce)
		{
			throw new EJBException(ce);
		}catch(NamingException ce)
		{
			throw new EJBException(ce);
		}
	}

	//for Expense starts here
	//public void addExpenses(int userId, ExpenseFormVO ExFormvo)

	public ExpenseFormVO createExpense(ExpenseFormVO expenseFormVO, int userID) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("ExpenseForms",userID, this.dataSource))
			throw new AuthorizationFailedException("ExpenseForms - createExpense");

		try
		{
			InitialContext ic = CVUtility.getInitialContext();

			HrExpensesLocalHome home = (HrExpensesLocalHome)ic.lookup("local/HrExpense");
			HrExpensesLocal hrexpense = (HrExpensesLocal) home.create(expenseFormVO,userID,this.dataSource);
			hrexpense.setDataSource(this.dataSource);
			expenseFormVO = hrexpense.getExpenseFormVO();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return expenseFormVO;

	}


	public ExpenseFormVO getExpenseFormVO(int ExpenseFormID)
	{
		ExpenseFormVO expenseFormVO = null;
			try
			{
				InitialContext ic = CVUtility.getInitialContext();
				HrExpensesLocalHome home = (HrExpensesLocalHome)ic.lookup("local/HrExpense");
				HrExpensesLocal hrexpense = home.findByPrimaryKey(new ExpenseFormPK(ExpenseFormID,this.dataSource));
				hrexpense.setDataSource(this.dataSource);
				expenseFormVO = hrexpense.getExpenseFormVO();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return expenseFormVO;
		}


	public void updateExpense(ExpenseFormVO expenseFormVO,int userID) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("ExpenseForms",userID, this.dataSource))
			throw new AuthorizationFailedException("ExpenseForms - getTimeSheetList");


		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			HrExpensesLocalHome home = (HrExpensesLocalHome)ic.lookup("local/HrExpense");
			HrExpensesLocal hrexpense = home.findByPrimaryKey(new ExpenseFormPK(expenseFormVO.getExpenseFormID(),this.dataSource));
			hrexpense.setDataSource(this.dataSource);
			hrexpense.setHrExpenseVO(expenseFormVO,userID);
		}
		catch(Exception e)
		{
			System.out.println("[Exception][HrFacadeEJB.updateExpense] Exception Thrown: "+e);
			e.printStackTrace();
		}
	}

	public void deleteExpense (int expenseFormID) throws NamingException
	{
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			HrExpensesLocalHome home = (HrExpensesLocalHome)ic.lookup("local/HrExpense");
			HrExpensesLocal hrexpense = home.findByPrimaryKey(new ExpenseFormPK(expenseFormID,this.dataSource));
			hrexpense.setDataSource(this.dataSource);
			//hrexpense.remove();
			hrexpense.deleteExpenseForm(expenseFormID);
		}catch(FinderException fe)
		{
			System.out.println("[Exception][HrFacadeEJB.deleteExpense] Exception Thrown: "+fe);
		}
	}

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

}


