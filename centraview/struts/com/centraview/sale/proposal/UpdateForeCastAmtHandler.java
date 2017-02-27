/*
 * $RCSfile: UpdateForeCastAmtHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:46 $ - $Author: mking_cv $
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

package com.centraview.sale.proposal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

public class UpdateForeCastAmtHandler extends Action
{
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = ".view.proposal.editproposal";

    HttpSession session = request.getSession();

    ProposalListForm proposallistform = (ProposalListForm) form;
    String sEventid = null;
    String sFormEventid = proposallistform.getProposalid();
    String sListEventid = (String) request.getParameter("eventid");

    if (sListEventid == null)
    {
      sEventid = sFormEventid;
    }
    else
    {
      sEventid = sListEventid;
    }

    if (sFormEventid == null)
    {
      sEventid = sListEventid;
    }
    else
    {
      sEventid = sFormEventid;
    }

    proposallistform.convertItemLines();

    UserObject userobjectd = (UserObject) session.getAttribute("userobject");
    int userid = Integer.parseInt(sEventid);
    String sForecastinc = proposallistform.getForecastinc();

    request.setAttribute("returnStatus", returnStatus);

    this.updateList(userid, proposallistform);
    session.removeAttribute("proposallistform");

    return (mapping.findForward(returnStatus));
  }

  public void updateList(int userid,
    ProposalListForm proposallistform)
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try
    {
      SaleFacadeHome sfh = (SaleFacadeHome) CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome",
          "SaleFacade");
      SaleFacade remote = (SaleFacade) sfh.create();
      remote.setDataSource(dataSource);
      remote.updateProposal(userid, proposallistform);
    }
    catch (Exception e)
    {
      System.out.println("[Exception] UpdateForeCastAmtHandler.updateList: " + e.toString());
      //System.out.println(e);
    }

    //System.out.println(status);
  }
}
