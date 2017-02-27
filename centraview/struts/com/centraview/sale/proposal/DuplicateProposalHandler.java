/*
 * $RCSfile: DuplicateProposalHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:44 $ - $Author: mking_cv $
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

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

/**
 * This handler is used to view proposal.
 *
 * @author sandip wadkar
 */
public class DuplicateProposalHandler extends Action
{


  /** Global Forwards for exception handling.  */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /** To forward to jsp viewproposal_sale.jsp.   */
  private static final String FORWARD_viewproposal = ".view.proposal.duplicateproposal";

  /** Redirect constant.   */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /**
   *  Executes initialization of required parameters and open window for entry of proposal
   *  returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // initialization of required parameter
    try
    {
      int counter = 0;
      ItemLines itemLines = null;

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();
      int row = Integer.parseInt((String) request.getParameter("eventid"));

      ProposalListForm proposallistform = (ProposalListForm) form;
      proposallistform.convertItemLines();

      SaleFacadeHome sfh = (SaleFacadeHome) CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome",
          "SaleFacade");
      SaleFacade remote = (SaleFacade) sfh.create();
      remote.setDataSource(dataSource);

      HashMap hm = (HashMap) remote.viewProposal(individualID, row, proposallistform);
      itemLines = (ItemLines) hm.get("itemLines");
      java.util.Set listkey = itemLines.keySet();
      java.util.Iterator it = listkey.iterator();

      int opportunityID = Integer.parseInt(proposallistform.getOpportunityid());

	  OpportunityVO opportunityVO	= remote.getOpportunity(individualID, opportunityID);
	  if(opportunityVO != null){
		proposallistform.setOpportunity(opportunityVO.getTitle());
		proposallistform.setEntityID(opportunityVO.getEntityID());
		proposallistform.setEntity(opportunityVO.getEntityname());
	  }
		
	  AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
	  AccountFacade accountFacade =(AccountFacade)accountFacadeHome.create();
	  accountFacade.setDataSource(dataSource);
	  Vector taxJurisdiction = accountFacade.getTaxJurisdiction();


      proposallistform = (ProposalListForm) hm.get("dyna");
      proposallistform.setJurisdictionVec(taxJurisdiction);
      proposallistform.setItemLines(itemLines);

      request.setAttribute("proposallistform", proposallistform);
      request.setAttribute("ItemLines", itemLines);
      FORWARD_final = FORWARD_viewproposal;

    }
    catch (Exception e)
    {
      System.out.println("[Exception] DuplicateProposalHandler.execute: " + e.toString());
      FORWARD_final = GLOBAL_FORWARD_failure;
    }

    // forward the request as per forward_final value
    return mapping.findForward(FORWARD_final);
  }
}
