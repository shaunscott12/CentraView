/*
 * $RCSfile: ExpenseListDeleteHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * @author Naresh Patel <npatel@centraview.com>
 */
public class ExpenseListDeleteHandler extends Action
{
  private static Logger logger = Logger.getLogger(ExpenseListDeleteHandler.class);
  public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    HttpSession session = request.getSession(true);
    int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
    ArrayList deleteLog = new ArrayList();
    String rowId[] = request.getParameterValues("rowId");
    AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
    try
    {
      AccountFacade remote = accountFacadeHome.create();
      remote.setDataSource(dataSource);
      for (int i=0; i<rowId.length; i++)
      {
        if(rowId[i] != null && !rowId[i].equals(""))
        {
          int elementID = Integer.parseInt(rowId[i]);
          try
          {
            remote.deleteExpense(elementID, individualId);
          } catch(AuthorizationFailedException ae) {
            String errorMessage = ae.getExceptionDescription();
            deleteLog.add(errorMessage);
          } catch(RemoteException re) {
            logger.error("[execute] Exception thrown.", re);
          }
        } //end of if(rowId[i] != null && !rowId[i].equals(""))
      } //end of for (int i=0; i<rowId.length; i++)
    } catch(CreateException e) {
      logger.error("[execute] Exception thrown.", e);
      throw new CommunicationException(e.getMessage());
    }
    if (!deleteLog.isEmpty())
    {
      ActionErrors allErrors = new ActionErrors();
      allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.freeForm", "You do not have permission to delete one or more of the records you selected."));
      session.setAttribute("listErrorMessage", allErrors);
    }
    ActionForward forward = new ActionForward(request.getParameter("currentPage"), true);
    return forward;
  }
}
