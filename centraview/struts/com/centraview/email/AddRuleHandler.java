/*
 * $RCSfile: AddRuleHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:48 $ - $Author: mking_cv $
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
package com.centraview.email;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;

/**
 * This handler is used when Adding Rule.
 *
 * @author Vivek T
 */
public final class AddRuleHandler extends Action
{

  
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = "failure";
    String forwardTo = "failure";
    String saveandclose = "";
    String saveandnew = "";
    String cancel = "";

    try
    {
      if (request.getParameter("saveandclose") != null)
      {
        saveandclose = (String) request.getParameter("saveandclose");
        returnStatus = perform("saveandclose", form, request);
        request.setAttribute("closeWindow", "true");
        forwardTo = "displaylistruleemail";
      }
      else if (request.getParameter("saveandnew") != null)
      {
        saveandnew = (String) request.getParameter("saveandnew");
        returnStatus = perform("saveandnew", form, request);
        clearForm((DynaActionForm) form, mapping);
        forwardTo = "displayruleemail";
      }
      else if (request.getParameter("Cancel") != null)
      {
        cancel = (String) request.getParameter("Cancel");
        returnStatus = "cancel";
        forwardTo = "displaylistruleemail";
      }
    }
    catch (Exception exe)
    {
      System.out.println("[Exception] AddRuleHandler.execute: " + exe.toString());
      forwardTo = "failure";
      //exe.printStackTrace();
    }

    return (mapping.findForward(forwardTo));
  }

  public String perform(String returnStatus, ActionForm form,
    HttpServletRequest request)
  {
    String status = returnStatus;
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try
    {
      EmailFacadeHome cfh = (EmailFacadeHome) 
        CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade) cfh.create();
      remote.setDataSource(dataSource);

      DynaActionForm dynaForm = (DynaActionForm) form;

      String accountid = (String) dynaForm.get("AccountID");
      String name = (String) dynaForm.get("name");
      String desc = (String) dynaForm.get("description");
      String enable = (String) dynaForm.get("enabled");
      String movemessto = (String) dynaForm.get("movemessageto");
      String movemesstofolder = (String) dynaForm.get("movemessagetofolder");
      String deletemessage = (String) dynaForm.get("deletemessage");
      String markasread = (String) dynaForm.get("markasread");
      String[] join = (String[]) dynaForm.get("colA");
      String[] field = (String[]) dynaForm.get("colB");
      String[] condition = (String[]) dynaForm.get("colC");
      String[] criteria = (String[]) dynaForm.get("colD");

      HashMap hm = new HashMap();
      hm.put("RuleName", name);
      hm.put("Description", desc);
      hm.put("EnabledStatus", enable);
      hm.put("AccountID", accountid);

      hm.put("Join", join);
      hm.put("Field", field);
      hm.put("Condition", condition);
      hm.put("Criteria", criteria);

      hm.put("ActionMoveMessage", movemessto);
      hm.put("MoveFolderId", movemesstofolder);
      hm.put("ActionDeleteMessage", deletemessage);
      hm.put("ActionMarkasRead", markasread);
      remote.addRule(hm);
    }
    catch (Exception e)
    {
      System.out.println("[Exception] AddRuleHandler.perform: " + e.toString());
      //e.printStackTrace();
    }
    return status;
  }

  public void clearForm(DynaActionForm form, ActionMapping mapping)
  {
    form.initialize(mapping);
  }
}
