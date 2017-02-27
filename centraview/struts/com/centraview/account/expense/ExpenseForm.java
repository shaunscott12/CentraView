/*
 * $RCSfile: ExpenseForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import com.centraview.account.common.ItemElement;
import com.centraview.account.common.ItemLines;
import com.centraview.common.DDNameValue;
import com.centraview.common.FloatMember;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.common.Validation;

public class ExpenseForm extends org.apache.struts.action.ActionForm {
  protected static MessageResources messages = MessageResources
      .getMessageResources("ApplicationResources");

  /** HTML form elements for Expense * */
  // As hidden param
  private int expenseID;

  private Vector glAccountVec;
  private String glAccountID;
  private int glAccountIDValue;

  private float amount;
  private String strAmount;

  private String title;

  private String expenseDescription;

  private String entity;
  private String entityID;
  private int entityIDValue;

  private String dateEntered;

  private String statusID;
  private String status;
  private int statusIDValue;
  private Vector statusVec;

  private String employee;
  private String employeeID;
  private int employeeIDValue;

  private String project;
  private String projectID;
  private int projectIDValue;

  private String opportunity;
  private String opportunityID;
  private int opportunityIDValue;

  private String supportTicket;
  private String supportTicketID;
  private int supportTicketIDValue;

  /** HTML form elements for Expense ends here* */

  /** HTML form elements for Items * */

  private String notes;
  private String[] priceeach;
  private String[] priceExtended;
  private String[] sku;
  private String[] itemid;
  private String[] item;
  private String[] description;
  private String[] quantity;
  private String[] unitprice;
  private String[] totalprice;
  private String[] unittax;
  private String[] taxrate;
  private String[] orderquantity;
  private String[] pendingquantity;
  private String[] lineid;
  private String[] linestatus;
  private ItemLines itemLines;

  // Added to get selected item ids.
  private String theitemid;
  private String removeID;

  /** HTML form elements for Items ends here* */

  public void convertFormbeanToValueObject()
  {
    try {

      if (glAccountID != null && glAccountID.length() != 0)
        this.glAccountIDValue = Integer.parseInt(glAccountID);

      if (entityID != null && entityID.length() != 0)
        this.entityIDValue = Integer.parseInt(entityID);

      if (employeeID != null && employeeID.length() != 0)
        this.employeeIDValue = Integer.parseInt(employeeID);

      if (projectID != null && projectID.length() != 0)
        this.projectIDValue = Integer.parseInt(projectID);

      if (opportunityID != null && opportunityID.length() != 0)
        this.opportunityIDValue = Integer.parseInt(opportunityID);

      if (supportTicketID != null && supportTicketID.length() != 0)
        this.supportTicketIDValue = Integer.parseInt(supportTicketID);

      if (statusID != null && statusID.length() != 0)
        this.statusIDValue = Integer.parseInt(statusID);

      if (strAmount != null && strAmount.length() != 0)
        this.amount = Float.parseFloat(strAmount);

    } catch (Exception e) {
      System.out.println("Error while converting strings to int" + e);
    }

  }

  /* convertItemLines method added for Item Lines */
  public void convertItemLines()
  {
    itemLines = new ItemLines();

    if (itemid != null) {
      for (int i = 0; i < itemid.length; i++) {

        ItemElement ie = new ItemElement();
        IntMember LineId = new IntMember("LineId", Integer.parseInt(lineid[i]), 'D', "", 'T',
            false, 20);
        IntMember ItemId = new IntMember("ItemId", Integer.parseInt(itemid[i]), 'D', "", 'T',
            false, 20);
        IntMember Quantity = new IntMember("Quantity", Integer.parseInt(quantity[i]), 'D', "", 'T',
            false, 20);
        priceeach[i] = priceeach[i].replaceAll(",", "");
        FloatMember PriceEach = new FloatMember("Price", new Float(priceeach[i]), 'D', "", 'T',
            false, 20);
        StringMember SKU = new StringMember("SKU", sku[i], 'D', "", 'T', false);
        StringMember Description = new StringMember("Description", description[i], 'D', "", 'T',
            false);
        if (priceExtended[i] != null) {
          priceExtended[i] = priceExtended[i].replaceAll(",", "");
        } else {
          priceExtended[i] = "";
        }
        FloatMember PriceExtended = new FloatMember("PriceExtended", new Float(priceExtended[i]),
            'D', "", 'T', false, 20);

        ie.put("LineId", LineId);
        ie.put("ItemId", ItemId);
        ie.put("SKU", SKU);
        ie.put("Description", Description);
        ie.put("Quantity", Quantity);
        ie.put("Price", PriceEach);
        ie.put("PriceExtended", PriceExtended);

        String status = linestatus[i];
        if (status == null)
          status = "New";
        ie.setLineStatus(status);

        itemLines.put(new Integer(i), ie);

      }// end of for
    }
  }

  public float getAmount()
  {
    return this.amount;
  }

  public void setAmount(float amount)
  {
    this.amount = amount;
  }

  public String getDateEntered()
  {
    return this.dateEntered;
  }

  public void setDateEntered(String dateEntered)
  {
    this.dateEntered = dateEntered;
  }

  public String[] getDescription()
  {
    return this.description;
  }

  public void setDescription(String[] description)
  {
    this.description = description;
  }

  public String getEmployee()
  {
    return this.employee;
  }

  public void setEmployee(String employee)
  {
    this.employee = employee;
  }

  public String getEmployeeID()
  {
    return this.employeeID;
  }

  public void setEmployeeID(String employeeID)
  {
    this.employeeID = employeeID;
  }

  public int getEmployeeIDValue()
  {
    return this.employeeIDValue;
  }

  public void setEmployeeIDValue(int employeeIDValue)
  {
    this.employeeIDValue = employeeIDValue;
  }

  public String getEntity()
  {
    return this.entity;
  }

  public void setEntity(String entity)
  {
    this.entity = entity;
  }

  public String getEntityID()
  {
    return this.entityID;
  }

  public void setEntityID(String entityID)
  {
    this.entityID = entityID;
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

  public String getGlAccountID()
  {
    return this.glAccountID;
  }

  public void setGlAccountID(String glAccountID)
  {
    this.glAccountID = glAccountID;
  }

  public int getGlAccountIDValue()
  {
    return this.glAccountIDValue;
  }

  public void setGlAccountIDValue(int glAccountIDValue)
  {
    this.glAccountIDValue = glAccountIDValue;
  }

  public Vector getGlAccountVec(String dataSource)
  {
    GlobalMasterLists gml = null;
    gml = GlobalMasterLists.getGlobalMasterLists(dataSource);

    Vector vec = (Vector)gml.get("GLAccounts");
    glAccountVec = vec;
    return glAccountVec;
  }

  public void setGlAccountVec(Vector glAccountVec)
  {
    this.glAccountVec = glAccountVec;
  }

  public String[] getItem()
  {
    return this.item;
  }

  public void setItem(String[] item)
  {
    this.item = item;
  }

  public String[] getItemid()
  {
    return this.itemid;
  }

  public void setItemid(String[] itemid)
  {
    this.itemid = itemid;
  }

  public ItemLines getItemLines()
  {
    return this.itemLines;
  }

  public void setItemLines(ItemLines itemLines)
  {
    this.itemLines = itemLines;
  }

  public String[] getLineid()
  {
    return this.lineid;
  }

  public void setLineid(String[] lineid)
  {
    this.lineid = lineid;
  }

  public String[] getLinestatus()
  {
    return this.linestatus;
  }

  public void setLinestatus(String[] linestatus)
  {
    this.linestatus = linestatus;
  }

  public String getNotes()
  {
    return this.notes;
  }

  public void setNotes(String notes)
  {
    this.notes = notes;
  }

  public String getOpportunity()
  {
    return this.opportunity;
  }

  public void setOpportunity(String opportunity)
  {
    this.opportunity = opportunity;
  }

  public String getOpportunityID()
  {
    return this.opportunityID;
  }

  public void setOpportunityID(String opportunityID)
  {
    this.opportunityID = opportunityID;
  }

  public int getOpportunityIDValue()
  {
    return this.opportunityIDValue;
  }

  public void setOpportunityIDValue(int opportunityIDValue)
  {
    this.opportunityIDValue = opportunityIDValue;
  }

  public String[] getOrderquantity()
  {
    return this.orderquantity;
  }

  public void setOrderquantity(String[] orderquantity)
  {
    this.orderquantity = orderquantity;
  }

  public String[] getPendingquantity()
  {
    return this.pendingquantity;
  }

  public void setPendingquantity(String[] pendingquantity)
  {
    this.pendingquantity = pendingquantity;
  }

  public String[] getPriceeach()
  {
    return this.priceeach;
  }

  public void setPriceeach(String[] priceeach)
  {
    this.priceeach = priceeach;
  }

  public String[] getPriceExtended()
  {
    return this.priceExtended;
  }

  public void setPriceExtended(String[] priceExtended)
  {
    this.priceExtended = priceExtended;
  }

  public String getProject()
  {
    return this.project;
  }

  public void setProject(String project)
  {
    this.project = project;
  }

  public String getProjectID()
  {
    return this.projectID;
  }

  public void setProjectID(String projectID)
  {
    this.projectID = projectID;
  }

  public int getProjectIDValue()
  {
    return this.projectIDValue;
  }

  public void setProjectIDValue(int projectIDValue)
  {
    this.projectIDValue = projectIDValue;
  }

  public String[] getQuantity()
  {
    return this.quantity;
  }

  public void setQuantity(String[] quantity)
  {
    this.quantity = quantity;
  }

  public String[] getSku()
  {
    return this.sku;
  }

  public void setSku(String[] sku)
  {
    this.sku = sku;
  }

  public String getStatusID()
  {
    return this.statusID;
  }

  public void setStatusID(String statusID)
  {
    this.statusID = statusID;
  }

  public int getStatusIDValue()
  {
    return this.statusIDValue;
  }

  public void setStatusIDValue(int statusIDValue)
  {
    this.statusIDValue = statusIDValue;
  }

  public Vector getStatusVec()
  {
    Vector vec = new Vector();

    vec.add(new DDNameValue(1, "Paid"));
    vec.add(new DDNameValue(2, "Unpaid"));
    vec.add(new DDNameValue(3, "Reimbursed"));

    statusVec = vec;
    return statusVec;
  }

  public void setStatusVec(Vector statusVec)
  {
    this.statusVec = statusVec;
  }

  public String getSupportTicket()
  {
    return this.supportTicket;
  }

  public void setSupportTicket(String supportTicket)
  {
    this.supportTicket = supportTicket;
  }

  public String getSupportTicketID()
  {
    return this.supportTicketID;
  }

  public void setSupportTicketID(String supportTicketID)
  {
    this.supportTicketID = supportTicketID;
  }

  public int getSupportTicketIDValue()
  {
    return this.supportTicketIDValue;
  }

  public void setSupportTicketIDValue(int supportTicketIDValue)
  {
    this.supportTicketIDValue = supportTicketIDValue;
  }

  public String[] getTaxrate()
  {
    return this.taxrate;
  }

  public void setTaxrate(String[] taxrate)
  {
    this.taxrate = taxrate;
  }

  public String getTitle()
  {
    return this.title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String[] getTotalprice()
  {
    return this.totalprice;
  }

  public void setTotalprice(String[] totalprice)
  {
    this.totalprice = totalprice;
  }

  public String[] getUnitprice()
  {
    return this.unitprice;
  }

  public void setUnitprice(String[] unitprice)
  {
    this.unitprice = unitprice;
  }

  public String[] getUnittax()
  {
    return this.unittax;
  }

  public void setUnittax(String[] unittax)
  {
    this.unittax = unittax;
  }

  public String getRemoveID()
  {
    return this.removeID;
  }

  public void setRemoveID(String removeID)
  {
    this.removeID = removeID;
  }

  public String getTheitemid()
  {
    return this.theitemid;
  }

  public void setTheitemid(String theitemid)
  {
    this.theitemid = theitemid;
  }

  public String getStrAmount()
  {
    return this.strAmount;
  }

  public void setStrAmount(String strAmount)
  {
    this.strAmount = strAmount;
  }

  /* For Form Validation */
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
  {
    ActionErrors errors = new ActionErrors();

    // cache the form data
    convertItemLines();

    try {
      Validation validation = new Validation();
      validation.checkForMaxlength("label.account.expense.description", this
          .getExpenseDescription(), "error.application.maxlength", errors, 40);
      validation.checkForRequired("label.account.expense.entity", this.getEntity(),
          "error.application.required", errors);
      validation.checkForRequired("label.account.expense.employee", this.getEmployee(),
          "error.application.required", errors);
      // validation.checkForRequired("label.account.expense.project",this.getProject(),"error.application.required","",errors);

      boolean itemPresent = false;
      int counter = 0;

      if (this.itemid != null) {
        for (int i = 0; i < this.linestatus.length; i++) {
          if (this.linestatus[i] != null && this.linestatus[i].equalsIgnoreCase("Deleted")) {
            counter++;
          }
        }

        if (this.linestatus.length > 0 && this.linestatus.length != counter) {
          itemPresent = true;
        }
      }

      if (itemPresent == false) {
        ActionMessage error = new ActionMessage("error.application.required", messages
            .getMessage("error.account.expense.itemsrequired"));
        errors.add("error.application.required", error);
      }

      if (errors != null && errors.size() > 0) {

        request.setAttribute("TYPEOFSUBMODULE", "ORDER");
        request.setAttribute("body", "EDIT");
        request.setAttribute("expenseform", this);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return errors;
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
