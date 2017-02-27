/*
 * $RCSfile: UpdateProposal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:46 $ - $Author: mking_cv $
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

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

/**
 * Insert the type's description here.
 * Creation date: (6/4/2003 11:48:26 AM)
 *
 */
public class UpdateProposal extends Action
{

  private static Logger logger = Logger.getLogger(UpdateProposal.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String returnStatus = ".view.proposal.editproposal";
    String button = request.getParameter("save_close");
    String button1 = request.getParameter("save_new");
    String button2 = request.getParameter("save");
    String orderFlag = request.getParameter("orderFlag");

    String saveLineItems = request.getParameter("save_line_items");


    String status = "changes Group updated in the database";
    //DynaActionForm dynaForm = (DynaActionForm)form;
    HttpSession session = request.getSession();

    //ProposalListForm  proposallistform  = (ProposalListForm)session.getAttribute("proposallistform");
    ProposalListForm  proposallistform  = (ProposalListForm)form;
    String sEventid = null;
    String sFormEventid = proposallistform.getProposalid();
    String sListEventid = (String) request.getParameter("eventid");
    if (sListEventid == null)
    {
      sEventid = sFormEventid;
    } //end of if statement (sListEventid == null)
    else
    {
      sEventid = sListEventid;
    } //end of else statement (sListEventid == null)

    if(sFormEventid == null)
    {
      sEventid = sListEventid;
    } //end of if statement (sFormEventid == null)
    else
    {
      sEventid = sFormEventid;
    } //end of else statement (sFormEventid == null)


    proposallistform.convertItemLines();

    ItemLines itemLines = (ItemLines) proposallistform.getItemLines();

    UserObject userobjectd = (UserObject) session.getAttribute("userobject");
    int individualID = userobjectd.getIndividualID();

    if (button != null)
    {
      request.setAttribute("saveandclose" , "saveandclose");
    } //end of if statement (button != null)
    else if (button1 != null)
    {
      returnStatus = ".view.proposal.newproposal";
      request.setAttribute("returnStatus", returnStatus);
      request.setAttribute("oppTitle", proposallistform.getOpportunity());
      request.setAttribute("oppId", proposallistform.getOpportunityid());
      request.removeAttribute("proposallistform");
    } //end of else if statement (button1 != null)
    else if (button2 != null || saveLineItems != null)
    {
      request.setAttribute("returnStatus", returnStatus);
    } //end of else if statement (button2 != null || saveLineItems != null)

	if(orderFlag != null && orderFlag.equals("yes")){
		returnStatus = ".view.accounting.ordergenerate";
	}

    this.updateList(status, individualID, proposallistform);
    session.removeAttribute("proposallistform");
    ListGenerator lg = ListGenerator.getListGenerator(dataSource);
    lg.makeListDirty("Proposal");
    //dynaForm.initialize(mapping);
    return (mapping.findForward(returnStatus));
  } //end of execute method


  public void updateList(String status, int individualID, ProposalListForm proposallistform) throws CommunicationException,NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    //DynaActionForm dynaform = (DynaActionForm)form;
    SaleFacadeHome sfh = (SaleFacadeHome)
        CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");
    try
    {
      SaleFacade remote =(SaleFacade) sfh.create();
      remote.setDataSource(dataSource);

			int billingAddressID = 0;
			int jurisdictionID = 0;
			if(proposallistform.getBillingaddressid() != null && !((proposallistform.getBillingaddressid()).equals(""))){
				billingAddressID = Integer.parseInt(proposallistform.getBillingaddressid());
			}//end of if(proposallistform.getBillingaddressid() != null && !((proposallistform.getBillingaddressid()).equals("")))
			jurisdictionID = proposallistform.getJurisdictionID();
			if(billingAddressID != 0 && jurisdictionID != 0){
				remote.setJurisdictionForAddress(billingAddressID,jurisdictionID);
			}//end of if(billingAddressID != 0 && jurisdictionID != 0)


      remote.updateProposal(individualID, proposallistform);
    } //end of try block
    catch(Exception e)
    {
      logger.error("[Exception] UpdateProposal.updateList ", e);
    } //end of catch block (Exception)
  } //end of updateList method
} //end of UpdateProposal class
