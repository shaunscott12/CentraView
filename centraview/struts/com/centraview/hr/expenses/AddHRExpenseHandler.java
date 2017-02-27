/*
 * $RCSfile: AddHRExpenseHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:00 $ - $Author: mking_cv $
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

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.hr.helper.ExpenseFormVO;
import com.centraview.hr.helper.ExpenseFormVOX;
import com.centraview.hr.hrfacade.HrFacade;
import com.centraview.hr.hrfacade.HrFacadeHome;
import com.centraview.settings.Settings;


/**
 * This Handler handles form request for new Expenses.
 *
 */
public final class AddHRExpenseHandler extends Action
{

  
  String currentTZ = "";
  UserObject createExpense = null;
  UserObject userobject = null;

  /**
  * Callback Execute method
  * @param mapping ActionMapping
  * @param form ActionForm
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException,NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    DynaValidatorForm dynaForm = (DynaValidatorForm) form;
    String saveandclose = (String) request.getParameter("saveandclose");
    String saveandnew = (String) request.getParameter("saveandnew");
    String save = (String) request.getParameter("save");

    if (save != null)
    {
      saveandnew = "";
    }

    String addItem = (String) dynaForm.get("additem");
    String returnStatus = "failure";
    HttpSession session = request.getSession(true);
    userobject = (UserObject) session.getAttribute("userobject");
    currentTZ = userobject.getUserPref().getTimeZone();

    int indvID = 0;

    if (userobject != null)
    {
      indvID = userobject.getIndividualID();
    }

    if ((saveandclose != null))
    {
      if (saveForm(indvID, dynaForm, request, response))
      {
        returnStatus = "success";
      }

      request.setAttribute("closeWindow", "true");
      request.setAttribute("refreshWindow", "true");
    }
    else if ((saveandnew != null))
    {
      if (saveForm(indvID, dynaForm, request, response))
      {
        returnStatus = "successNew";
      }

      //clearForm(dynaForm , mapping);
      //Clears the DynaActionForm
      if (save == null)
      {
        dynaForm.reset(mapping, request);
      }

      request.setAttribute("refreshWindow", "true");
    }
    else if (addItem != null)
    {
      returnStatus = "addItem";
    }

    return (mapping.findForward(returnStatus));
  }

  public boolean saveForm(int indvID, DynaValidatorForm dynaform,
    HttpServletRequest request, HttpServletResponse response) throws CommunicationException,NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    ExpenseFormVOX expenseFormVOX = new ExpenseFormVOX(userobject, dynaform);
    ExpenseFormVO expenseFormVO = expenseFormVOX.getExpenseFormVO();

    //		//ProjectFacadeHome pfh = (ProjectFacadeHome)CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome","ProjectFacade");
    HrFacadeHome hfh = (HrFacadeHome) 
      CVUtility.getHomeObject("com.centraview.hr.hrfacade.HrFacadeHome", "HrFacade");

    try
    {
      HrFacade remote = (HrFacade) hfh.create();
      remote.setDataSource(dataSource);
      remote.createExpense(expenseFormVO, indvID);

      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("hr");

      return true;
    }
    catch (Exception e)
    {
      System.out.println("[Exception] AddHRExpenseHandler.saveForm: " + e.toString());
      //e.printStackTrace();
    }

    return false;
  }
}
