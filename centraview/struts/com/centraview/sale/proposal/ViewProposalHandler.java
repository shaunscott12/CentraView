/*
 * $RCSfile: ViewProposalHandler.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:43:21 $ - $Author: mcallist $
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
import java.text.DecimalFormat;
import java.util.HashMap;
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
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.email.emailfacade.EmailFacade;
import com.centraview.email.emailfacade.EmailFacadeHome;
import com.centraview.sale.OpportunityForm;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

public class ViewProposalHandler extends Action {

  /** Global Forwards for exception handling */
  public static final String GLOBAL_FORWARD_failure = "failure";

  /** To forward to jsp viewproposal_sale.jsp */
  private static final String FORWARD_viewproposal = ".view.proposal.editproposal";

  /** Redirect constant */
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  /** The Currency Amount Format. */
  private DecimalFormat currencyFormat = new DecimalFormat("###,###,##0.00");
  private static Logger logger = Logger.getLogger(ViewProposalHandler.class);

  /**
   * Executes initialization of required parameters and open window for entry of
   * proposal returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException, NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    // initialization of required parameter
    SaleFacadeHome sfh = (SaleFacadeHome) CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome",
        "SaleFacade");
    AccountFacadeHome accountFacadeHome = (AccountFacadeHome) CVUtility.getHomeObject(
        "com.centraview.account.accountfacade.AccountFacadeHome", "AccountFacade");
    EmailFacadeHome emailFacade = (EmailFacadeHome) CVUtility.getHomeObject(
        "com.centraview.email.emailfacade.EmailFacadeHome", "EmailFacade");
    try {
      Boolean openAddItem = (request.getAttribute("openAddItem") == null) ? new Boolean(false) : (Boolean) request
          .getAttribute("openAddItem");
      request.setAttribute("openAddItem", openAddItem);

      int counter = 0;
      int row = 0;
      ItemLines itemLines = null;
      String viewOperation = (request.getParameter(ProposalConstantKeys.TYPEOFOPERATION) == null) ? "" : request
          .getParameter(ProposalConstantKeys.TYPEOFOPERATION);

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject) session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

      ProposalListForm proposallistform = (ProposalListForm) form;
      proposallistform.convertItemLines();
      try {
        row = Integer.parseInt(request.getParameter("eventid"));
      } catch (Exception ex) {
        row = Integer.parseInt(proposallistform.getProposalid());
      }

      AccountFacade accountFacade = accountFacadeHome.create();
      accountFacade.setDataSource(dataSource);
      Vector taxJurisdiction = accountFacade.getTaxJurisdiction();
      proposallistform.setJurisdictionVec(taxJurisdiction);

      EmailFacade remoteEmail = emailFacade.create();
      remoteEmail.setDataSource(dataSource);
      String emailDisableFlag = "";

      boolean emailFlag = remoteEmail.checkEmailAccount(individualID);

      if (!emailFlag) {
        emailDisableFlag = "disabled= true";
      }
      request.setAttribute("emailDisableFlag", emailDisableFlag);

      if (!(viewOperation.equalsIgnoreCase(ProposalConstantKeys.REMOVEITEM) || viewOperation
          .equalsIgnoreCase(ProposalConstantKeys.ADDITEM))) {

        SaleFacade remote = sfh.create();
        remote.setDataSource(dataSource);

        HashMap hm = remote.viewProposal(individualID, row, proposallistform);

        itemLines = (ItemLines) hm.get("itemLines");
        proposallistform = (ProposalListForm) hm.get("dyna");
        proposallistform.setItemLines((ItemLines) hm.get("itemLines"));
        int iOpportunityId;
        iOpportunityId = Integer.parseInt(proposallistform.getOpportunityid());

        // **************** set the opportunity form bean start *************
        OpportunityVO opportunityVO = remote.getOpportunity(individualID, iOpportunityId);
        OpportunityForm opportunityForm = new OpportunityForm();

        if (opportunityVO != null) {
          opportunityForm.setEntityid("" + opportunityVO.getEntityID());
          opportunityForm.setTitle(opportunityVO.getTitle());
          opportunityForm.setDescription(opportunityVO.getDescription());
          opportunityForm.setEntityname(opportunityVO.getEntityname());
          opportunityForm.setOpportunityid(new Integer(opportunityVO.getOpportunityID()).toString());
          opportunityForm.setStatusname(opportunityVO.getStatus());
          opportunityForm.setStagename(opportunityVO.getStage());
          opportunityForm.setOpportunitytypename(opportunityVO.getOpportunityType());
          opportunityForm.setForecastedamount(currencyFormat.format(new Float(opportunityVO.getForecastedAmount())));
          opportunityForm.setAcctmgrname(opportunityVO.getManagerName());
        } // end of if statement (opportunityVO != null)
        request.setAttribute("opportunityForm", opportunityForm);
        session.setAttribute("opportunityForm", opportunityForm);
        // **************** set the opportunity form bean end *************
      } // end of if statement
      // (!(viewOperation.equalsIgnoreCase(ProposalConstantKeys.REMOVEITEM)
      // ...
      else if (viewOperation.equalsIgnoreCase(ProposalConstantKeys.REMOVEITEM)) {
        String removeIDs = request.getParameter("removeID");
        StringTokenizer st;
        Iterator itr;
        Vector removeKeys = new Vector();

        itemLines = proposallistform.getItemLines();
        if (itemLines != null) {
          st = new StringTokenizer(removeIDs, ",");
          while (st.hasMoreTokens()) {
            String str = st.nextToken();
            int removeToken = Integer.parseInt(str);

            itr = itemLines.keySet().iterator();
            while (itr.hasNext()) {
              Object obj = itr.next();
              ItemElement ILE = (ItemElement) itemLines.get(obj);
              IntMember ItemId = (IntMember) ILE.get("ItemId");
              Integer currItemId = (Integer) ItemId.getMemberValue();
              if (currItemId.intValue() == removeToken) {
                String status = ILE.getLineStatus();
                if (status.equals("Active")) {
                  ILE.setLineStatus("Deleted");
                } // end of if statement (status.equals("Active"))
                else if (status.equals("New")) {
                  removeKeys.add(obj);
                } // end of else if statement (status.equals("New"))
              } // end of if statement (currItemId.intValue() == removeToken)
            } // end of while loop (itr.hasNext())
          } // end of while loop (st.hasMoreTokens())

          for (int i = 0; i < removeKeys.size(); i++) {
            itemLines.remove(removeKeys.get(i));
          } // end of for loop (int i=0; i<removeKeys.size(); i++)
          itemLines.calculate();
        } // end of if statement (itemLines != null)
        proposallistform.setItemLines(itemLines);

        OpportunityForm opportunityForm = (OpportunityForm) session.getAttribute("opportunityForm");
        request.setAttribute("opportunityForm", opportunityForm);
        session.setAttribute("opportunityForm", opportunityForm);

      } // end of else if statement
      // (viewOperation.equalsIgnoreCase(ProposalConstantKeys.REMOVEITEM))
      else if (viewOperation.equalsIgnoreCase(ProposalConstantKeys.ADDITEM)) {

        String newItemID = request.getParameter("theitemid");
        ItemList IL = null;
        ListGenerator lg = ListGenerator.getListGenerator(dataSource);
        IL = lg.getItemList(individualID, 1, 10, "", "ItemID");
        StringTokenizer st;
        String token, nextItr;

        if (newItemID != null) {
          st = new StringTokenizer(newItemID, ",");
          itemLines = proposallistform.getItemLines();

          if (itemLines == null)
            itemLines = new ItemLines();
          counter = itemLines.size();
          while (st.hasMoreTokens()) {
            token = st.nextToken();
            int intToken = Integer.parseInt(token);

            Iterator itr = IL.keySet().iterator();
            while (itr.hasNext()) {
              nextItr = (String) itr.next();
              ListElement ile = (ListElement) IL.get(nextItr);
              IntMember smid = (IntMember) ile.get("ItemID");
              Integer listItemid = (Integer) smid.getMemberValue();

              if (listItemid.intValue() == intToken) {

                StringMember smName = (StringMember) ile.get("Name"); // name =
                // description
                String name = (String) smName.getMemberValue();

                StringMember smSku = (StringMember) ile.get("SKU");
                String sku = (String) smSku.getMemberValue();
                FloatMember dmprice = (FloatMember) ile.get("Price");

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
              }// end of if ( listItemid.intValue() == intToken )
            }// end of while (itr.hasNext())
          }// end of while (st.hasMoreTokens())
          itemLines.calculate();
          proposallistform.setItemLines(itemLines);
        }// end of if(newItemID != null)
        OpportunityForm opportunityForm = (OpportunityForm) session.getAttribute("opportunityForm");
        request.setAttribute("opportunityForm", opportunityForm);
        session.setAttribute("opportunityForm", opportunityForm);

      } // end of else if statement
      // (viewOperation.equalsIgnoreCase(ProposalConstantKeys.ADDITEM))

      request.setAttribute("proposallistform", proposallistform);
      // request.setAttribute("ItemLines" ,itemLines);
      FORWARD_final = FORWARD_viewproposal;
    } // end of try block
    catch (Exception e) {
      logger.error("[Exception] ViewProposalHandler.Execute Handler ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    } // end of catch block (Exception)
    return mapping.findForward(FORWARD_final);
  } // end of execute method
} // end of ViewProposalHandler class
