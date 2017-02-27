/*
 * $RCSfile: NewHRExpenseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:02 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.item.ItemList;
import com.centraview.common.CVUtility;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.common.UserPrefererences;
import com.centraview.hr.helper.ExpenseFormVO;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.settings.Settings;

public class NewHRExpenseHandler extends org.apache.struts.action.Action
{

  static int counter = 0;
  HrExpenseForm hrexpenseForm;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String title = request.getParameter("title");
    try
    {
      HttpSession session = request.getSession(true);
      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.EXPENSE);

      request.setAttribute("body", "EDIT");

      hrexpenseForm = (HrExpenseForm)session.getAttribute("HrExpenseForm");

      HrExpenseLines hrExpenseLines = null;

      hrexpenseForm.convertItemLines();

      com.centraview.common.UserObject userobjectd = (com.centraview.common.UserObject)session.getAttribute("userobject"); //get the user object
      int individualID = userobjectd.getIndividualID();

      int expenseFormID = 0;

      UserPrefererences userPref = userobjectd.getUserPref();
      String dateFormat = userPref.getDateFormat();

      dateFormat = "M/d/yyyy";
      String timeZone = userPref.getTimeZone();
      if (timeZone == null)
        timeZone = "EST";
      GregorianCalendar gCal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
      SimpleDateFormat dForm = new SimpleDateFormat(dateFormat);
      dForm.setCalendar(gCal);

      String actionType = request.getParameter("actionType");
      
      if (request.getParameter("expenseFormID") != null)
      {
        String sExpenseFormID = (String)request.getParameter("expenseFormID");

        if ((sExpenseFormID.trim()).equals(""))
        {
          expenseFormID = 0;
        }
        else
        {
          Long expenseFormIDLong = new Long((String)request.getParameter("expenseFormID"));
          expenseFormID = expenseFormIDLong.intValue();
        }
      }
      else
      {
        //why this? directly it is 0 expenseFormID = Integer.parseInt((String)request.getParameter("expenseFormID"));
        expenseFormID = 0;
      }

      /*
      	ADD item
      */
      if (request.getParameter(AccountConstantKeys.TYPEOFOPERATION) != null && request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals("ADDITEM")) //if_2
      {

        if (expenseFormID == 0)
        {
          hrExpenseLines = hrexpenseForm.getItemLines();
          if (hrExpenseLines == null)
          {
            hrExpenseLines = new HrExpenseLines();
          }
        }
        else
        {
          hrExpenseLines = hrexpenseForm.getItemLines(); //(HrExpenseLines)session.getAttribute("hrExpenseLines");
        }

        String newItemID = request.getParameter("theitemid");
        ItemList IL = null; //see if u need to write something different.

        ListGenerator lg = ListGenerator.getListGenerator(dataSource); //get the List Generator object for Listing
        //IL = (ItemList )lg.getItemList( 1 , 1, 10 , "" , "ItemID");//called when the request for the list is for first time
        IL = (ItemList)lg.getItemList(individualID, 1, 10, "", "ItemID"); //called when the request for the list is for first time
        StringTokenizer st;
        String token, nextItr;
        if (newItemID != null)
        {
          st = new StringTokenizer(newItemID, ",");
          counter = hrExpenseLines.size();

          while (st.hasMoreTokens())
          {
            token = (String)st.nextToken();
            int intToken = Integer.parseInt(token);
						Iterator itr = IL.keySet().iterator();
            while (itr.hasNext())
            {
              nextItr = (String)itr.next();
              ListElement ile = (ListElement)IL.get(nextItr);
              IntMember smid = (IntMember)ile.get("ItemID");
              Integer listItemid = (Integer)smid.getMemberValue();

              if (listItemid.intValue() == intToken)
              {
                StringMember smName = (StringMember)ile.get("Name"); // name = description
                String name = (String)smName.getMemberValue();
                StringMember smSku = (StringMember)ile.get("SKU");
                String sku = (String)smSku.getMemberValue();
                //							DoubleMember dmprice  = (DoubleMember) ile.get("Price");
                FloatMember dmprice = (FloatMember)ile.get("Price");
                float price = ((Float)dmprice.getMemberValue()).floatValue();
                int id = ile.getElementID();

                IntMember LineId = new IntMember("LineId", 11, 'D', "", 'T', false, 20);
                IntMember ItemId = new IntMember("ItemId", id, 'D', "", 'T', false, 20);
                StringMember SKU = new StringMember("SKU", sku, 'D', "", 'T', false);
                StringMember Description = new StringMember("Description", name, 'D', "", 'T', false);
                FloatMember Quantity = new FloatMember("Quantity", new Float(1.0f), 'D', "", 'T', false, 20);
                FloatMember PriceEach = new FloatMember("PriceEach", new Float(price), 'D', "", 'T', false, 20);
                FloatMember PriceExtended = new FloatMember("PriceExtended", new Float(0.0f), 'D', "", 'T', false, 20);

                FloatMember Amount = new FloatMember("Amount", new Float(0.0f), 'D', "", 'T', false, 20);
                StringMember Reference = new StringMember("Reference", new String(), 'D', "", 'T', false);

                IntMember ReferenceType = new IntMember("ReferenceType", 100, 'D', "", 'T', false, 20);
                IntMember ReferenceId = new IntMember("ReferenseId", 100, 'D', "", 'T', false, 20);

                HrExpenseLineElement ie = new HrExpenseLineElement(11);

                ie.put("LineId", LineId);
                ie.put("ExpenseItemID", ItemId);
                ie.put("SKU", SKU);
                ie.put("Description", Description);
                ie.put("Quantity", Quantity);
                ie.put("PriceEach", PriceEach);
                ie.put("PriceExtended", PriceExtended);

                //ie.put( "Project" , new Integer(110) ) ;
                //ie.put( "Ticket" , new Integer(100) ) ;
                //ie.put( "Opportunity" , new Integer(100) );

                //fields other than item list
                ie.put("Reference", Reference);
                ie.put("ReferenceType", ReferenceType);
                ie.put("ReferenseId", ReferenceId);
                ie.put("Amount", Amount);

                ie.setLineStatus("New");
                counter += 1;
                hrExpenseLines.put(new Integer(counter), ie);
                //hrExpenseLines.put(""+counter, ie);
                //cw break;
              } //end if ( listItemid.intValue() == intToken )
            } //end of while (itr.hasNext())
          } //end of while (st.hasMoreTokens())
        } //end of if (newItemID != null)
        hrexpenseForm.setItemLines(hrExpenseLines);
        //request.setAttribute("HrExpenseLines",hrExpenseLines);
        request.setAttribute("HrExpenseForm", hrexpenseForm);
        session.setAttribute("HrExpenseForm", hrexpenseForm);
        //session.setAttribute("hrExpenseLines",hrExpenseLines);//cw
      } //if_2
      else if (
        request.getParameter(AccountConstantKeys.TYPEOFOPERATION) != null
          && request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals(AccountConstantKeys.REMOVEITEM)) //if_2
      {
        if (expenseFormID == 0)
        {
          hrExpenseLines = hrexpenseForm.getItemLines();
          if (hrExpenseLines == null)
          {
            hrExpenseLines = new HrExpenseLines();
          }
        }
        else
        {
          hrExpenseLines = hrexpenseForm.getItemLines(); //(HrExpenseLines)session.getAttribute("hrExpenseLines");
        }
        String newItemID = request.getParameter("theitemid");

        StringTokenizer st;
        String token, nextItr;

        if (newItemID != null)
        {
          st = new StringTokenizer(newItemID, ",");
          counter = hrExpenseLines.size();
          while (st.hasMoreTokens())
          {

            token = (String)st.nextToken();

            if (expenseFormID == 0)
              hrExpenseLines.remove(new Integer(token));
            else
            {
              HrExpenseLineElement ele = (HrExpenseLineElement)hrExpenseLines.get(new Integer(token));
              if (ele != null)
                ele.setLineStatus("Deleted");
            }

          }
        }
        hrexpenseForm.setItemLines(hrExpenseLines);
        request.setAttribute("HrExpenseForm", hrexpenseForm);
        session.setAttribute("HrExpenseForm", hrexpenseForm);
        //session.setAttribute("hrExpenseLines",hrExpenseLines);//cw
      } //if_2
      else if (expenseFormID == 0 && actionType != null)
      {
      	hrexpenseForm = new HrExpenseForm(); //cw important so everytime a new form is created instead of getting from session

        hrexpenseForm.setEmployeeID(individualID);
        hrexpenseForm.setEmployee(userobjectd.getfirstName() + " " + userobjectd.getlastName());

        //for employee id

        request.setAttribute("HrExpenseForm", hrexpenseForm);
        session.setAttribute("HrExpenseForm", hrexpenseForm);

        request.setAttribute("ExpenseFormVO", null);
        session.setAttribute("ExpenseFormVO", null);
      

      }
      
      else if ((request.getParameter("expenseFormID") != null))
      {
        HrFacadeHome hm = (HrFacadeHome)CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");
        HrFacade remote = (HrFacade)hm.create();
        remote.setDataSource(dataSource);

        ExpenseFormVO expenseFormVO = remote.getExpenseFormVO(expenseFormID);
        this.hrexpenseForm.setExpenseFormID(expenseFormID);

        this.hrexpenseForm.setFormDescription(expenseFormVO.getDescription());

        this.hrexpenseForm.setEmployee(expenseFormVO.getEmployee());
        this.hrexpenseForm.setEmployeeID(expenseFormVO.getEmployeeId());
        this.hrexpenseForm.setReportto(expenseFormVO.getReportingTo());
        this.hrexpenseForm.setReporttoID(expenseFormVO.getReportingId());
        this.hrexpenseForm.setNotes(expenseFormVO.getNotes());
        this.hrexpenseForm.setStatus(expenseFormVO.getStatus());

        if (expenseFormVO.getFrom() != null)
        {
          java.sql.Date fromDate = expenseFormVO.getFrom();
          String sfromDate = fromDate.toString();
          Vector fromDateSplitUp = getDate(sfromDate);

          this.hrexpenseForm.setFromyear((String)fromDateSplitUp.get(0));
          this.hrexpenseForm.setFrommonth((String)fromDateSplitUp.get(1));
          this.hrexpenseForm.setFromday((String)fromDateSplitUp.get(2));
        }
        if (expenseFormVO.getTo() != null)
        {
          java.sql.Date toDate = expenseFormVO.getTo();
          String stoDate = toDate.toString();
          //String[] sTokenizedDate = getDate(stoDate);
          Vector toDateSplitUp = getDate(stoDate);
          this.hrexpenseForm.setToyear((String)toDateSplitUp.get(0));
          this.hrexpenseForm.setTomonth((String)toDateSplitUp.get(1));
          this.hrexpenseForm.setToday((String)toDateSplitUp.get(2));
        }

        this.hrexpenseForm.setItemLines(expenseFormVO.getHrExpenseLines());

        //request.setAttribute("expenseform",expenseForm);
        request.setAttribute("ExpenseFormVO", expenseFormVO);
        session.setAttribute("ExpenseFormVO", expenseFormVO);

        request.setAttribute("HrExpenseForm", this.hrexpenseForm);
        session.setAttribute("HrExpenseForm", this.hrexpenseForm);

        request.setAttribute("DTD", "DTD");

      }

      //HttpSession session = request.getSession(true);

      if (session.getAttribute("highlightmodule") != null)
        session.setAttribute("highlightmodule", "hr");
    }
    catch (Exception e)
    {
      System.out.println("[Exception][NewHRExpenseHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      return mapping.findForward("failure");
    }

    return mapping.findForward(".view.hr.expenseform.detail");
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
