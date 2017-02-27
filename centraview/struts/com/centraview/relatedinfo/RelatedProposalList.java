/*
 * $RCSfile: RelatedProposalList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:36 $ - $Author: mking_cv $
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

package com.centraview.relatedinfo;

import java.io.IOException;
import java.util.ArrayList;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ValueListConstants;

/**
 * This class is the Struts Action class handler for all Related Info
 * screens where the listType is "Opportunity". The class will generate
 * the proper DisplayList and set a request attribute. Control will then
 * transfer to the View layer.
 */
public class RelatedProposalList extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(RelatedProposalList.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String viewType = "Proposal";
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    // by default, this is the page to forward to
    String forwardPage = "relatedInfoBottom";
    // get the variables needed from the request object, DO NOT
    // get these values from anywhere else. These values were set
    // by RelatedInfoListHandler, so check there if you need to
    // modify anything. These values should not change in this class.
    String listType = (String)request.getAttribute("listType");
    if (listType.equals("BottomOpportunity")) {
      listType = "Opportunity";
    }
    String listFor = (String)request.getAttribute("listFor");
    Integer recordID = (Integer)request.getAttribute("recordID");
    String filter = "";
    if (listFor.equals("Entity")) {
      filter = "SELECT ProposalID FROM proposal p LEFT JOIN opportunity o ON (p.OpportunityID=o.OpportunityID) WHERE o.EntityID=" + recordID;
    } else if (listFor.equals("Opportunity")) {
      filter = "SELECT ProposalID FROM proposal WHERE OpportunityID=" + recordID;
    } else if (listFor.equals("Individual")) {
      filter = "SELECT ProposalID FROM proposal WHERE individualid=" + recordID;
    }
    // Buttons
    ArrayList buttonList = (ArrayList)request.getAttribute("buttonList");
    // now, get the data from the EJB layer and put it on the request
    RelatedInfoUtil.relatedInfoSetup(request, dataSource, viewType, ValueListConstants.PROPOSALS_LIST_TYPE, ValueListConstants.proposalsViewMap, filter.toString(), buttonList, 0);

    return (mapping.findForward(forwardPage));
  } // end execute() method
} // end class definition