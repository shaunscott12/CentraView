/*
 * $RCSfile: SaveHrExpenseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:02 $ - $Author: mking_cv $
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

package com.centraview.hr.expenses;

import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserPrefererences;
import com.centraview.hr.helper.ExpenseFormVO;
import com.centraview.hr.helper.ExpenseFormVOX;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.settings.Settings;

public class SaveHrExpenseHandler extends org.apache.struts.action.Action
{

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_savenew = ".view.hr.savenew.expenseform";
  private static final String FORWARD_saveclose = ".view.hr.expenseform.list";
  private static final String FORWARD_save = ".view.hr.save.expenseform";

  private static String FORWARD_final = FORWARD_savenew;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession(true);

      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.EXPENSE);

      Enumeration enmattr = request.getAttributeNames();

      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.EXPENSE);

      request.setAttribute("body", "EDIT");

      com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject)session.getAttribute("userobject"); //get the user object
      int individualID = userobjectd.getIndividualID();

      UserPrefererences userPref = userobjectd.getUserPref();

      String dateFormat = userPref.getDateFormat();

      dateFormat = "M/d/yyyy";
      String timeZone = userPref.getTimeZone();
      if (timeZone == null)
        timeZone = "EST";
      GregorianCalendar gCal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
      SimpleDateFormat dForm = new SimpleDateFormat(dateFormat);
      dForm.setCalendar(gCal);

      String typeOfSave = null;

      if (request.getParameter("typeOfSave") != null)
        typeOfSave = (String)request.getParameter("typeOfSave");

      com.centraview.hr.expenses.HrExpenseForm hrexpenseForm = (com.centraview.hr.expenses.HrExpenseForm)session.getAttribute("HrExpenseForm");
      int expenseFormID = 0;

      Long expenseFormIDLong = new Long(0);

      if (hrexpenseForm != null)
      {
        //	String expenseFormIDString =(String)hrexpenseForm.getExpenseFormID();
        expenseFormID = hrexpenseForm.getExpenseFormID();
        //get("expenseFormID");
      }

      ExpenseFormVO expenseformVo = null;

      //Put all item to ItemLines
      if (expenseFormID == 0)
      {
        hrexpenseForm.convertItemLines();
      }
      ExpenseFormVOX vox = new ExpenseFormVOX(userobjectd, hrexpenseForm);

      ExpenseFormVO expenseFormVO = vox.getVO();

      HrFacadeHome hm = (HrFacadeHome)CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
      HrFacade remote = (HrFacade)hm.create();
      remote.setDataSource(dataSource);
      if (expenseFormID > 0 && request.getParameter("DUPLICATE") != null)
      {
        hrexpenseForm.convertItemLines();
        expenseFormVO = remote.createExpense(expenseFormVO, individualID);
        hrexpenseForm.setExpenseFormID((new Integer(expenseFormVO.getExpenseFormID())).intValue());
        //set("expenseFormID",(new Integer(expenseFormVO.getExpenseFormID())).toString());
        java.sql.Timestamp date = expenseFormVO.getCreatedDate();
        if (date != null)
        {
          hrexpenseForm.setCreatedDate(date.toString());
        }
        hrexpenseForm.setItemLines(expenseFormVO.getHrExpenseLines());
        request.setAttribute("HrExpenseLines", expenseFormVO.getHrExpenseLines());
      }
      else if (expenseFormID > 0)
      {
        expenseFormVO.setExpenseFormID(expenseFormID);
        remote.updateExpense(expenseFormVO, individualID);

        expenseFormVO = remote.getExpenseFormVO(expenseFormID);

        //Set all elements in form.
        hrexpenseForm.setExpenseFormID(expenseFormID);

        java.sql.Timestamp date = expenseFormVO.getCreatedDate();

        //set values on hrexpenseForm
        hrexpenseForm.setCreatedDate(date.toString());
        //set("CreatedDate",dForm.format(date));
        //expenseForm.setCreatedDate(date);
        hrexpenseForm.setExpenseFormID(expenseFormID);

        hrexpenseForm.setFormDescription(expenseFormVO.getDescription());

        hrexpenseForm.setEmployee(expenseFormVO.getEmployee());
        hrexpenseForm.setEmployeeID(expenseFormVO.getEmployeeId());
        hrexpenseForm.setReportto(expenseFormVO.getReportingTo());
        hrexpenseForm.setReporttoID(expenseFormVO.getReportingId());
        hrexpenseForm.setStatus(expenseFormVO.getStatus());

        hrexpenseForm.setNotes(expenseFormVO.getNotes());

        if (expenseFormVO.getFrom() != null)
        {
          java.sql.Date fromDate = expenseFormVO.getFrom();
          String sfromDate = fromDate.toString();
          Vector fromDateSplitUp = getDate(sfromDate);

          hrexpenseForm.setFromyear((String)fromDateSplitUp.get(0));
          hrexpenseForm.setFrommonth((String)fromDateSplitUp.get(1));
          hrexpenseForm.setFromday((String)fromDateSplitUp.get(2));
        }

        if (expenseFormVO.getTo() != null)
        {
          java.sql.Date toDate = expenseFormVO.getTo();
          String stoDate = toDate.toString();
          //String[] sTokenizedDate = getDate(stoDate);
          Vector toDateSplitUp = getDate(stoDate);
          hrexpenseForm.setToyear((String)toDateSplitUp.get(0));
          hrexpenseForm.setTomonth((String)toDateSplitUp.get(1));
          hrexpenseForm.setToday((String)toDateSplitUp.get(2));
        }

        hrexpenseForm.setItemLines(expenseFormVO.getHrExpenseLines());

        //	@@@@@COMMENNTED ON 30/10/03
        //hrexpenseForm.setItemLines(expenseFormVO.getHrExpenseLines());

        request.setAttribute("HrExpenseLines", expenseFormVO.getHrExpenseLines());

      }

      else if (expenseFormID == 0)
      {
        expenseFormVO = remote.createExpense(expenseFormVO, individualID);

        hrexpenseForm.setExpenseFormID((new Integer(expenseFormVO.getExpenseFormID())).intValue());
        //set("expenseFormID",(new Integer(expenseFormVO.getExpenseFormID())).toString());

        java.sql.Timestamp date = expenseFormVO.getCreatedDate();
        if (date != null)
        {
          hrexpenseForm.setCreatedDate(date.toString());
        }
      }

      if (typeOfSave != null)
      {
        if (typeOfSave.equals("savenew") || typeOfSave.equals("saveandnew"))
        {
          // forward to jsp page
          FORWARD_final = FORWARD_savenew;

          request.setAttribute("body", "EDIT");

          request.setAttribute("HrExpenseForm", new HrExpenseForm());
          session.setAttribute("HrExpenseForm", new HrExpenseForm());
          //changed now request.setAttribute("hrexpenseform",new HrExpenseForm());

        }
        if (typeOfSave.equals("saveandclose"))
        {
          // forward to jsp page
          request.setAttribute("HrExpenseForm", hrexpenseForm);
          session.setAttribute("HrExpenseForm", hrexpenseForm);
          request.setAttribute("ExpenseFormVO", expenseFormVO);
          session.setAttribute("ExpenseFormVO", expenseFormVO);
          FORWARD_final = FORWARD_saveclose;

          request.setAttribute("body", "list");
        }

        if (typeOfSave.equals("save"))
        {

          // forward to jsp page
          FORWARD_final = FORWARD_save;

          request.setAttribute("body", "EDIT");

          if (expenseFormID > 0)
          {
            hrexpenseForm.setCreatedDate(expenseFormVO.getCreatedDate().toString());
            //set("CreatedDate",(expenseFormVO.getCreatedDate()).toString());
          }
          session.setAttribute("HrExpenseForm", hrexpenseForm);
          request.setAttribute("HrExpenseform", hrexpenseForm);
          request.setAttribute("ExpenseFormVO", expenseFormVO);
          session.setAttribute("ExpenseFormVO", expenseFormVO);
        }

      }

    }
    catch (Exception e)
    {
      System.out.println("[Exception][SaveHrExpenseHandler.execute] Exception Thrown: " + e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }
  private Vector getDate(String sfromDate)
  {
    StringTokenizer stok = new StringTokenizer(sfromDate, "-");
    String[] sTokens = new String[] {};
    int i = 0;
    Vector vecDate = new Vector();
    while (stok.hasMoreTokens())
    {
      String sToken = stok.nextToken();
      vecDate.add(sToken);
    }
    return vecDate;
  }
}
