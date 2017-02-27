/*
 * $RCSfile: GetRuleDetailsHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:50 $ - $Author: mking_cv $
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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.common.CVUtility;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.settings.Settings;

//	/**
//	 * @version 	1.0 Date  7/23/2003
//	 * @author Vivek T
/*	This handler is used for Get Rule Details Handler */

public class GetRuleDetailsHandler extends Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();


    try
    {
      String foldername = "";
      String ruleid = request.getParameter("rowId");
      int ruleID = Integer.parseInt(ruleid);
      EmailFacadeHome cfh = (EmailFacadeHome)CVUtility.getHomeObject("com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
      EmailFacade remote = (EmailFacade)cfh.create();
      remote.setDataSource(dataSource);

      RuleDetails ruledetails = remote.getRuleDetails(ruleID);
      DynaActionForm dynaForm = (DynaActionForm)form;
      dynaForm.set("RuleID", ruledetails.getRuleID());
      dynaForm.set("AccountID", ruledetails.getAccountID());
      dynaForm.set("name", ruledetails.getName());
      dynaForm.set("description", ruledetails.getDescription());
      dynaForm.set("enabled", ruledetails.getEnabled());
      dynaForm.set("colA", ruledetails.getcolA());
      dynaForm.set("colB", ruledetails.getcolB());
      dynaForm.set("colC", ruledetails.getcolC());
      dynaForm.set("colD", ruledetails.getcolD());
      dynaForm.set("movemessageto", ruledetails.getMovemessageto());
      dynaForm.set("movemessagetofolder", ruledetails.getMovemessagetofolder());
      dynaForm.set("markasread", ruledetails.getMarkasread());
      dynaForm.set("deletemessage", ruledetails.getDeleteMessage());
      HttpSession session = request.getSession(true);
      FolderList fl = (FolderList)session.getAttribute("folderlist");
      AccountDetail ad = (AccountDetail)fl.get(new Integer(ruledetails.getAccountID()));

      ArrayList al = ad.getFolderList();
      Folder f;
      if ((ruledetails.getMovemessagetofolder() != null) && (!ruledetails.getMovemessagetofolder().equals("")))
      {
        for (int i = 0; i < al.size(); i++)
        {
          f = (Folder)al.get(i);
          int folderid = f.getFolderid();
          int movemessagetofolder = Integer.parseInt(ruledetails.getMovemessagetofolder().toString());
          if (folderid == movemessagetofolder)
          {
            foldername = f.getFoldername();
            request.setAttribute("foldername", foldername);
            break;
          }
        }
      }

    }
    catch (Exception e)
    {
      System.out.println("[Exception][GetRuleDetailsHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward("displayeditruleemail"));
  }

  public void clearForm(DynaActionForm form, ActionMapping mapping)
  {
    form.initialize(mapping);
  }
}
