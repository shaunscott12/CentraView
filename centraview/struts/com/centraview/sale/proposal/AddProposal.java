/*
 * $RCSfile: AddProposal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:43 $ - $Author: mking_cv $
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
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

public class AddProposal extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(AddProposal.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    ProposalListForm proposallistform;
    HttpSession session = request.getSession();

    proposallistform = (ProposalListForm) form;
    proposallistform.convertItemLines();

    String button = request.getParameter("save_close");
    String addItem = request.getParameter("additem");
    String button1 = request.getParameter("save_new");
    String button2 = request.getParameter("save");
    String addmeberbutton = request.getParameter("button3");

    UserObject userObject = (UserObject) session.getAttribute("userobject");
    int individualID = userObject.getIndividualID();

    if (button1 != null) //save & new
    {
      this.saveList(individualID, proposallistform);
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Proposal");
      request.setAttribute("saveandclose", "saveandclose");
      session.removeAttribute("proposallistform");
      return (mapping.findForward(".view.proposal.newproposal"));
    }
    else if (button != null) //Save & Close
    {
      this.saveList(individualID, proposallistform);
      // code to update list to reflect the changes
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Proposal");
      request.setAttribute("saveandnew", "saveandnew");
      session.removeAttribute("proposallistform");
      return (mapping.findForward(".view.proposal.newproposal"));
    }
    else
    {
      if (button2 == null)
      {
        request.setAttribute("openAddItem", new Boolean(true));
      }

      int listid = this.saveList(individualID, proposallistform);

      // code to update list to reflect the changes
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Proposal");
      request.setAttribute("save", "save");
      session.removeAttribute("proposallistform");
      proposallistform.setProposalid("" + listid);
      request.setAttribute("proposallistform", proposallistform);
      ActionForward am = new ActionForward(mapping.findForward(".view.proposal.editproposal").getPath() + "?eventid=" + listid);
      return am;
    }
  }

  public int saveList(int individualID, ProposalListForm proposallistform) throws CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    int listid = 0;
		SaleFacadeHome sfh = (SaleFacadeHome)
			CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");
    try
    {
      SaleFacade remote = (SaleFacade) sfh.create();
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

      listid = remote.addProposal(individualID, proposallistform);
    }
    catch (Exception e)
    {
      logger.error("[Exception] AddProposal.saveList  ", e);
    }
    return listid;
  }
}
