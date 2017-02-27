/*
 * $RCSfile: HrExpenseForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import com.centraview.common.Validation;

public class HrExpenseForm extends org.apache.struts.action.ActionForm {
  private static Logger logger = Logger.getLogger(HrExpenseForm.class);
  protected static MessageResources messages = MessageResources
      .getMessageResources("ApplicationResources");
  private int ExpenseFormID;
  private String reportto = "";
  private int reporttoID = 0;
  private String reporttoLookupButton = "";
  private String employee = "";
  private int employeeID = 0;
  private String employeeLookupButton = "";
  private String frommonth = "";
  private String fromday = "";
  private String fromyear = "";
  private String tomonth = "";
  private String today = "";
  private String toyear = "";
  private String formDescription = "";
  private String notes = "";
  private int createdby = 0;
  private int modifiedby = 0;
  private String createdDate;
  private String modifiedDate;
  private float amount = 0;
  private String reportingTo = "";
  private String status;

  private String[] priceeach;
  private String[] priceExtended;
  private String[] sku;
  private String[] itemid;
  private String[] item;
  private String[] description;
  private String[] quantity;
  private String[] typeID;
  private String[] reference;
  private String[] referenceId;
  private String[] unitprice;
  private String[] totalprice;
  private String[] lineid;
  private String[] expenseId;
  private String[] linestatus;

  private HrExpenseLines itemLines;

  private String theitemid;
  private String removeID;

  public HrExpenseForm() {

  }

  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    // initialize new actionerror object
    ActionErrors errors = new ActionErrors();
    // cache the form data
    convertItemLines();

    try {
      // initialize validation
      Validation validation = new Validation();

      validation.checkForRequired("label.hrs.hr.ReportingTo", reportto,
          "error.application.required", errors);

      validation.checkForRequired("label.hrs.hr.Employee", getEmployee(),
          "error.application.required", errors);

      validation.checkForRequired("label.hrs.hr.From", getFromday(), "error.application.required",
          errors);
      validation.checkForRequired("label.hrs.hr.To", getToday(), "error.application.required",
          errors);

      if ((getFromday() != null && getFromday().length() != 0)
          || (getFromyear() != null && (getFromyear()).length() != 0)
          || (getFrommonth() != null && (getFrommonth()).length() != 0)) {
        validation.checkForDate("label.hrs.hr.From", getFromyear(), getFrommonth(), getFromday(),
            "error.application.date", errors);
      }
      if ((getToday() != null && getToday().length() != 0)
          || (getToyear() != null && getToyear().length() != 0)
          || (getTomonth() != null && getTomonth().length() != 0)) {
        validation.checkForDate("label.hrs.hr.To", getToyear(), getTomonth(), getToday(),
            "error.application.date", errors);
      }

      if ((getFromday()) != null
          && (getFromday().length() != 0 && (getToday()) != null && (getToday()).length() != 0))
        validation.checkForDateComparison("label.hrs.hr.From", (getFromyear()), (getFrommonth()),
            (getFromday()), "label.hrs.hr.To", (getToyear()), (getTomonth()), (getToday()),
            "error.application.datecomparison", errors);
      boolean itemPresent = false;
      int counter = 0;

      if (this.itemid != null) {

        for (int i = 0; i < this.linestatus.length; i++) {
          if (this.linestatus[i] != null && this.linestatus[i].equalsIgnoreCase("Deleted")) {
            counter++;
          }
        }

        if (this.itemid.length > 0 && this.linestatus.length != counter) {
          itemPresent = true;
        }
      }

      if (itemPresent == false) {
        ActionMessage error = new ActionMessage("error.application.required", messages
            .getMessage("error.account.expense.itemsrequired"));
        errors.add("error.application.required", error);
      }

      HttpSession session = request.getSession(true);

      if (errors != null) {
        request.setAttribute("body", "EDIT");
        request.setAttribute("HrExpenseForm", this);
        session.setAttribute("HrExpenseForm", this);
      }

    } catch (Exception e) {
      logger.error("[validate]: Exception", e);
    }
    return errors;
  }

  public void convertItemLines()
  {
    itemLines = new HrExpenseLines();

    if (itemid != null) {
      for (int i = 0; i < itemid.length; i++) {
        HrExpenseLineElement ie = new HrExpenseLineElement();

        com.centraview.common.IntMember LineId = new com.centraview.common.IntMember("LineId",
            Integer.parseInt(lineid[i]), 'D', "", 'T', false, 20);

        com.centraview.common.IntMember ExpenseItemId = new com.centraview.common.IntMember(
            "ExpenseItemId", Integer.parseInt(itemid[i]), 'D', "", 'T', false, 20);

        com.centraview.common.StringMember SKU = new com.centraview.common.StringMember("SKU",
            sku[i], 'D', "", 'T', false);

        com.centraview.common.StringMember ItemDescription = new com.centraview.common.StringMember(
            "ItemDescription", description[i], 'D', "", 'T', false);

        int iTypeid = Integer.parseInt(typeID[i]);
        com.centraview.common.IntMember typeId = new com.centraview.common.IntMember("typeID",
            Integer.parseInt(typeID[i]), 'D', "", 'T', false, 20);

        com.centraview.common.StringMember Reference = new com.centraview.common.StringMember(
            "Reference", reference[i], 'D', "", 'T', false);

        com.centraview.common.IntMember ReferenceId = new com.centraview.common.IntMember(
            "ReferenceId", 100, 'D', "", 'T', false, 20);

        if ((iTypeid >= 1) && (iTypeid <= 3)) {
          ReferenceId = new com.centraview.common.IntMember("ReferenceId", Integer
              .parseInt(referenceId[i]), 'D', "", 'T', false, 20);
        } else {
          // reference[i] = "0";
          ReferenceId = new com.centraview.common.IntMember("ReferenceId", 100, 'D', "", 'T',
              false, 20);
        }

        com.centraview.common.FloatMember Quantity = new com.centraview.common.FloatMember(
            "Quantity", new Float(quantity[i]), 'D', "0", 'T', false, 20);

        String price = removeSeparator(priceeach[i]);
        com.centraview.common.FloatMember PriceEach = new com.centraview.common.FloatMember(
            "PriceEach", new Float(price), 'D', "", 'T', false, 20);

        String priceExt = removeSeparator(priceExtended[i]);

        com.centraview.common.FloatMember PriceExtended = new com.centraview.common.FloatMember(
            "PriceExtended", new Float(priceExt), 'D', "", 'T', false, 20);

        if (LineId != null)
          ie.put("LineId", LineId);

        if (ExpenseItemId != null)
          ie.put("ExpenseItemID", ExpenseItemId);

        if (SKU != null)
          ie.put("SKU", SKU); // sku is the item description
        if (Reference != null)
          ie.put("Reference", Reference);

        if (Quantity != null)
          ie.put("Quantity", Quantity);

        if (typeId != null)
          ie.put("ReferenceType", typeId);

        if (ReferenceId != null)
          ie.put("ReferenceId", ReferenceId);

        if (PriceEach != null)
          ie.put("PriceEach", PriceEach);

        if (PriceExtended != null)
          ie.put("PriceExtended", PriceExtended);

        if (ItemDescription != null)
          ie.put("Description", ItemDescription);

        String sstatus = linestatus[i];
        if (sstatus == null)
          sstatus = "New";
        ie.setLineStatus(sstatus);

        itemLines.put(new Integer(i), ie);

      }// end of for
    }
  }

  public int getExpenseFormID()
  {
    return this.ExpenseFormID;
  }

  public void setExpenseFormID(int expenseFormID)
  {
    this.ExpenseFormID = expenseFormID;
  }

  public HrExpenseLines getItemLines()
  {
    return this.itemLines;
  }

  public void setItemLines(HrExpenseLines itemLines)
  {
    this.itemLines = itemLines;
  }

  public String getReportto()
  {
    return this.reportto;
  }

  public void setReportto(String reportto)
  {
    this.reportto = reportto;
  }

  public String[] getUnitprice()
  {
    return this.unitprice;
  }

  public void setUnitprice(String[] unitprice)
  {
    this.unitprice = unitprice;
  }

  public String[] getTypeID()
  {
    return this.typeID;
  }

  public void setTypeID(String[] typeID)
  {
    this.typeID = typeID;
  }

  public String getToyear()
  {
    return this.toyear;
  }

  public void setToyear(String toyear)
  {
    this.toyear = toyear;
  }

  public String[] getTotalprice()
  {
    return this.totalprice;
  }

  public void setTotalprice(String[] totalprice)
  {
    this.totalprice = totalprice;
  }

  public String getTomonth()
  {
    return this.tomonth;
  }

  public void setTomonth(String tomonth)
  {
    this.tomonth = tomonth;
  }

  public String getToday()
  {
    return this.today;
  }

  public void setToday(String today)
  {
    this.today = today;
  }

  public String getTheitemid()
  {
    return this.theitemid;
  }

  public void setTheitemid(String theitemid)
  {
    this.theitemid = theitemid;
  }

  public String[] getSku()
  {
    return this.sku;
  }

  public void setSku(String[] sku)
  {
    this.sku = sku;
  }

  public String getReporttoLookupButton()
  {
    return this.reporttoLookupButton;
  }

  public void setReporttoLookupButton(String reporttoLookupButton)
  {
    this.reporttoLookupButton = reporttoLookupButton;
  }

  public int getReporttoID()
  {
    return this.reporttoID;
  }

  public void setReporttoID(int reporttoID)
  {
    this.reporttoID = reporttoID;
  }

  public String getRemoveID()
  {
    return this.removeID;
  }

  public void setRemoveID(String removeID)
  {
    this.removeID = removeID;
  }

  public String[] getReference()
  {
    return this.reference;
  }

  public void setReference(String[] reference)
  {
    for (int iCount = 0; iCount < reference.length; iCount++) {}
    this.reference = reference;
  }

  public String[] getQuantity()
  {
    return this.quantity;
  }

  public void setQuantity(String[] quantity)
  {
    this.quantity = quantity;
  }

  public String[] getPriceExtended()
  {
    return this.priceExtended;
  }

  public void setPriceExtended(String[] priceExtended)
  {
    this.priceExtended = priceExtended;
  }

  public String[] getPriceeach()
  {
    return this.priceeach;
  }

  public void setPriceeach(String[] priceeach)
  {
    this.priceeach = priceeach;
  }

  public String getNotes()
  {
    return this.notes;
  }

  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  public String getModifiedDate()
  {
    return this.modifiedDate;
  }

  public void setModifiedDate(String modifiedDate)
  {
    this.modifiedDate = modifiedDate;
  }

  public int getModifiedby()
  {
    return this.modifiedby;
  }

  public void setModifiedby(int modifiedby)
  {
    this.modifiedby = modifiedby;
  }

  public String[] getLinestatus()
  {
    return this.linestatus;
  }

  public void setLinestatus(String[] linestatus)
  {
    this.linestatus = linestatus;
  }

  public String[] getLineid()
  {
    return this.lineid;
  }

  public void setLineid(String[] lineid)
  {
    this.lineid = lineid;
  }

  public String[] getItemid()
  {
    return this.itemid;
  }

  public void setItemid(String[] itemid)
  {
    this.itemid = itemid;
  }

  public String[] getItem()
  {
    return this.item;
  }

  public void setItem(String[] item)
  {
    this.item = item;
  }

  public String getFromyear()
  {
    return this.fromyear;
  }

  public void setFromyear(String fromyear)
  {
    this.fromyear = fromyear;
  }

  public String getFrommonth()
  {
    return this.frommonth;
  }

  public void setFrommonth(String frommonth)
  {
    this.frommonth = frommonth;
  }

  public String getFromday()
  {
    return this.fromday;
  }

  public void setFromday(String fromday)
  {
    this.fromday = fromday;
  }

  public String[] getExpenseId()
  {
    return this.expenseId;
  }

  public void setExpenseId(String[] expenseId)
  {
    this.expenseId = expenseId;
  }

  /*
   * public String[] getExpenseFormId() { return this.expenseFormId; } public
   * void setExpenseFormId(String[] expenseFormId) { this.expenseFormId =
   * expenseFormId; }
   */

  public String getEmployeeLookupButton()
  {
    return this.employeeLookupButton;
  }

  public void setEmployeeLookupButton(String employeeLookupButton)
  {
    this.employeeLookupButton = employeeLookupButton;
  }

  public int getEmployeeID()
  {
    return this.employeeID;
  }

  public void setEmployeeID(int employeeID)
  {
    this.employeeID = employeeID;
  }

  public String getEmployee()
  {
    return this.employee;
  }

  public void setEmployee(String employee)
  {
    this.employee = employee;
  }

  public String getFormDescription()
  {
    return this.formDescription;
  }

  public void setFormDescription(String description1)
  {
    this.formDescription = description1;
  }

  public String[] getDescription()
  {
    return this.description;
  }

  public void setDescription(String[] description)
  {
    this.description = description;

  }

  public String getCreatedDate()
  {
    return this.createdDate;
  }

  public void setCreatedDate(String createdDate)
  {
    this.createdDate = createdDate;
  }

  public int getCreatedby()
  {
    return this.createdby;
  }

  public void setCreatedby(int createdby)
  {
    this.createdby = createdby;
  }

  public float getAmount()
  {
    return this.amount;
  }

  public void setAmount(float amount)
  {
    this.amount = amount;
  }

  public String getReportingTo()
  {
    return this.reportingTo;
  }

  public void setReportingTo(String reportingTo)
  {
    this.reportingTo = reportingTo;
  }

  public String[] getReferenceId()
  {

    return this.referenceId;
  }

  public void setReferenceId(String[] referenceId)
  {
    for (int iIndex = 0; iIndex < referenceId.length; iIndex++) {}
    this.referenceId = referenceId;
  }

  /*
   * this is a temp solution to convert "1,330.00" to "1333.00";
   */
  private String removeSeparator(String str)
  {
    String ret = "";
    int ind = str.indexOf(",");
    if (ind > 0) {
      ret = str.substring(0, ind) + removeSeparator(str.substring(ind + 1));
    } else
      ret = str;
    return ret;
  }

  public String getStatus()
  {
    return this.status;
  }

  public void setStatus(String status)
  {
    this.status = status;
  }
}
