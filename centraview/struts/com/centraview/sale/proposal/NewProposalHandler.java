/*
 * $RCSfile: NewProposalHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:44 $ - $Author: mking_cv $
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
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

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

import com.centraview.account.accountfacade.AccountFacade;
import com.centraview.account.accountfacade.AccountFacadeHome;
import com.centraview.account.item.ItemList;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.FloatMember;
import com.centraview.common.GlobalMasterLists;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

/**
 * This handler is used to display new proposal
 */
public class NewProposalHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_newproposal = ".view.proposal.new_proposal";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  static int counter = 0;
  private static Logger logger = Logger.getLogger(NewProposalHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    
    SaleFacadeHome sfh = (SaleFacadeHome)CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");
    
    try{
      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

      request.setAttribute("newProposal", new Boolean(true));
      ProposalListForm proposalListForm = (ProposalListForm)form;
      
      String oppId = request.getParameter("oppId");
      String oppTitle = request.getParameter("oppTitle");
      
      if ((oppId == null) && (oppTitle == null)) {
        oppId = (String)request.getAttribute("oppId");
        oppTitle = (String)request.getAttribute("oppTitle");
      }
      
      if ((oppId != null) && (oppTitle != null)) {
        proposalListForm.setOpportunityid(oppId);
        proposalListForm.setOpportunity(oppTitle);
        SaleFacade remote = (SaleFacade)sfh.create();
        remote.setDataSource(dataSource);
        
        int opportunityId = (new Integer(oppId)).intValue();
        OpportunityVO oppVO = remote.getOpportunity(individualID, opportunityId);
        proposalListForm.setIndividualid(String.valueOf(oppVO.getIndividualID()));
        proposalListForm.setIndividual(oppVO.getIndividualname());
        proposalListForm.setEntityID(oppVO.getEntityID());
        proposalListForm.setEntity(oppVO.getEntityname());
      }

      AccountFacadeHome accountFacadeHome = (AccountFacadeHome)CVUtility.getHomeObject("com.centraview.account.accountfacade.AccountFacadeHome","AccountFacade");
      AccountFacade accountFacade = (AccountFacade)accountFacadeHome.create();
      accountFacade.setDataSource(dataSource);
      
      Vector taxJurisdiction = accountFacade.getTaxJurisdiction();
      proposalListForm.setJurisdictionVec(taxJurisdiction);
      proposalListForm.convertItemLines();

      ItemLines itemLines = null;

      request.setAttribute("body", ProposalConstantKeys.ADD);

      String typeOfOperation = (String)request.getParameter(ProposalConstantKeys.TYPEOFOPERATION);
      String action = (String)request.getParameter("action");
      
      if ((typeOfOperation != null) && typeOfOperation.equals("REMOVEITEM")) {
        String removeIDs = request.getParameter("removeID");
        StringTokenizer st;
        Iterator itr;
        Vector removeKeys = new Vector();
        
        itemLines = proposalListForm.getItemLines();
        
        if (itemLines != null) {
          st = new StringTokenizer(removeIDs, ",");
          
          while (st.hasMoreTokens()) {
            String str = st.nextToken();
            int removeToken = Integer.parseInt(str);
            
            itr = itemLines.keySet().iterator();
            
            while (itr.hasNext()) {
              Object obj = itr.next();
              ItemElement ILE = (ItemElement)itemLines.get(obj);
              IntMember ItemId = (IntMember)ILE.get("ItemId");
              Integer currItemId = (Integer)ItemId.getMemberValue();
              
              if (currItemId.intValue() == removeToken) {
                String status = ILE.getLineStatus();
                
                if (status.equals("Active")) {
                  ILE.setLineStatus("Deleted");
                }else if (status.equals("New")){
                  removeKeys.add(obj);
                }
              }
            }   // end while(itr.hasNext())
          }   // end while(st.hasMoreTokens())

          for (int i = 0; i < removeKeys.size(); i++) {
            itemLines.remove(removeKeys.get(i));
          }
        }
        
        itemLines.calculate();
        proposalListForm.setItemLines(itemLines);
      }else if ((typeOfOperation != null) && typeOfOperation.equals("ADDITEM")){
        String newItemID = request.getParameter("theitemid");
        ItemList IL = null ;
        ListGenerator lg = ListGenerator.getListGenerator(dataSource);
        IL = (ItemList)lg.getItemList(individualID, 1, 10, "", "ItemID");

        StringTokenizer st;
        String token, nextItr;

        if (newItemID != null) {
          st = new StringTokenizer(newItemID, ",");
          itemLines = proposalListForm.getItemLines();
          
          if (itemLines == null) {
            itemLines = new ItemLines();
          }
          
          int counter = itemLines.size();
          
          while (st.hasMoreTokens()) {
            token = (String)st.nextToken();
            int intToken = Integer.parseInt(token);
            
            Iterator itr = IL.keySet().iterator();
            
            while (itr.hasNext()) {
              nextItr = (String)itr.next();
              ListElement ile = (ListElement)IL.get(nextItr);
              IntMember smid = (IntMember)ile.get("ItemID");
              Integer listItemid = (Integer)smid.getMemberValue();
              
              if (listItemid.intValue() == intToken) {
                StringMember smName = (StringMember)ile.get("Name"); // name = description
                String name = (String) smName.getMemberValue();
                StringMember smSku = (StringMember)ile.get("SKU");
                String sku = (String)smSku.getMemberValue();
                FloatMember dmprice = (FloatMember)ile.get("Price");
                float price = Float.parseFloat((dmprice.getMemberValue()).toString());
                int id = ile.getElementID();

                IntMember LineId = new IntMember("LineId", 0, 'D', "", 'T', false, 20);
                IntMember ItemId = new IntMember("ItemId", id, 'D', "", 'T', false, 20);
                IntMember Quantity = new IntMember("Quantity", 1, 'D', "", 'T', false, 20);
                FloatMember PriceEach = new FloatMember("Price", new Float(price), 'D', "", 'T', false, 20);
                StringMember SKU = new StringMember("SKU", sku, 'D', "", 'T', false);
                StringMember Description = new StringMember("Description", name, 'D', "", 'T', false);
                FloatMember PriceExtended = new FloatMember("PriceExtended", new Float(0.0f), 'D', "", 'T', false, 20);
                FloatMember TaxAmount = new FloatMember("TaxAmount", new Float(0.0f), 'D', "", 'T', false, 20);

                ItemElement ie = new ItemElement(0);
                ie.put("LineId", LineId);
                ie.put("ItemId", ItemId);
                ie.put("SKU", SKU);
                ie.put("Description", Description);
                ie.put("Quantity", Quantity);
                ie.put("Price", PriceEach);
                ie.put("PriceExtended", PriceExtended);
                ie.put("TaxAmount", TaxAmount);

                ie.setLineStatus("New");
                counter += 1;
                itemLines.put(new Integer(counter), ie);
                break;
              }   // end if (listItemid.intValue() == intToken)
            }   // end while(itr.hasNext())
          }   // end while(st.hasMoreTokens())
          itemLines.calculate();
          proposalListForm.setItemLines(itemLines);
        }   // end if (newItemID != null)
      }
      
      GlobalMasterLists gml = GlobalMasterLists.getGlobalMasterLists(dataSource);
      Vector salesStatusList = (Vector)gml.get("AllSaleProbability");
      Iterator it = salesStatusList.iterator();
      
      while (it.hasNext()) {
        DDNameValue thisStatus = (DDNameValue) it.next();
        if ((thisStatus.getName()).equalsIgnoreCase("100%")) {
          proposalListForm.setProbability(Integer.toString(thisStatus.getId()));
          break;
        }
      }
      
      proposalListForm.setAttachFileValues(new Vector());
      FORWARD_final = FORWARD_newproposal;
    }catch (Exception e){
      logger.error("[Exception] NewProposalHandler.Execute Handler ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }   // end execute() method

}   // end class definition
