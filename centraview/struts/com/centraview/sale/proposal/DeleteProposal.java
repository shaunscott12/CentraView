/*
 * $RCSfile: DeleteProposal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:44 $ - $Author: mking_cv $
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

import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

/**
 * DeleteProposal
 *  Used to delete proposal information.
 *
 * @author  Sandip
 */
public class DeleteProposal extends Action
{

  
  /** Global Forwards for exception handling.   */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /** Local Forwards for redirecting to jsp.   */
  private static final String FORWARD_Proposal = ".view.proposal.deleteproposal";

  /** Redirect constant.   */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /** Default Constructor.   */
  public DeleteProposal()
  {
    //empty
  }

  /**
   *  Executes initialization of required parameters and open window for entry of note
   *  returns ActionForward
   * @throws NamingException
   * @throws IOException, ServletException,CommunicationException
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    SaleFacadeHome aa = (SaleFacadeHome) CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");
    try
    {
      HttpSession session = request.getSession(true);
      int proposalid = 0;
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualId = userObject.getIndividualID();
      ProposalListForm pf = (ProposalListForm) form;
      proposalid = Integer.parseInt(pf.getProposalid());

      SaleFacade remote = (SaleFacade) aa.create();
      remote.setDataSource(dataSource);

      boolean b = remote.deleteProposal(individualId, proposalid);
      FORWARD_final = FORWARD_Proposal;
    }
    catch (Exception e)
    {
      System.out.println("[Exception] DeleteProposal.execute: " + e.toString());
      //e.printStackTrace();
    }
    return mapping.findForward(FORWARD_final);
  }
}
