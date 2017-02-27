/*
 * $RCSfile: SaveOpportunityHandler.java,v $    $Revision: 1.4 $  $Date: 2005/10/17 17:11:34 $ - $Author: mcallist $
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

package com.centraview.sale;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.NumberFormat;

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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.CVUtility;
import com.centraview.common.DateUtility;
import com.centraview.common.UserObject;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

/**
 * Used to save opportunity information
 */
public class SaveOpportunityHandler extends Action {
  public static final String GLOBAL_FORWARD_failure = ".view.sales.new_opportunity";
  private static final String FORWARD_saveOpportunity = ".view.sales.new_opportunity";
  private static final String FORWARD_permission = "salepermission";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  private static Logger logger = Logger.getLogger(SaveOpportunityHandler.class);

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException, CommunicationException,
      NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");

    ActionMessages allErrors = new ActionMessages();

    int rowID = 0;
    int ownerId = userObject.getIndividualID();

    SaleFacadeHome mfh = (SaleFacadeHome)CVUtility.getHomeObject(
        "com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");

    try {
      SaleFacade remote = mfh.create();
      remote.setDataSource(dataSource);

      OpportunityForm opportunityForm = (OpportunityForm)form;
      OpportunityVO opportunityVO = new OpportunityVO();

      ActivityVO activityVO = new ActivityVO();
      activityVO.setStatus(Integer.parseInt(opportunityForm.getStatusid()));
      activityVO.setTitle(opportunityForm.getTitle());
      activityVO.setActivityDetails(opportunityForm.getDescription());
      activityVO.setCreatedBy(ownerId);
      activityVO.setOwner(ownerId);

      opportunityVO.setActivityVO(activityVO);
      opportunityVO.setTitle(opportunityForm.getTitle());
      opportunityVO.setDescription(opportunityForm.getDescription());

      int entityIdInt = 0;
      String entityId = opportunityForm.getEntityid();
      try {
        entityIdInt = Integer.parseInt(entityId);
      } catch (NumberFormatException nfe) {}

      if (entityIdInt <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            "error.general.requiredField", "Entity"));
      }

      opportunityVO.setEntityID(entityIdInt);

      if (!opportunityForm.getIndividualid().trim().equals("")) {
        opportunityVO.setIndividualID(Integer.parseInt(opportunityForm.getIndividualid()));
      }

      opportunityVO.setOpportunityTypeID(Integer.parseInt(opportunityForm.getOpportunitytypeid()));
      opportunityVO.setStatusID(Integer.parseInt(opportunityForm.getStatusid()));
      opportunityVO.setStageID(Integer.parseInt(opportunityForm.getStageid()));

      if (!opportunityForm.getTotalamount().trim().equals("")) {
        String Totalamount = opportunityForm.getTotalamount();
        if (Totalamount != null) {
          Totalamount = Totalamount.replaceAll(" ", "");// remove empty spaces
          NumberFormat nf = NumberFormat.getInstance();
          Totalamount = nf.parse(Totalamount).toString();
        }
        opportunityVO.setActualAmount(Float.parseFloat(Totalamount));
      }

      String ForecastedAmount = opportunityForm.getForecastedamount();
      if (ForecastedAmount != null) {
        ForecastedAmount = ForecastedAmount.replaceAll(" ", "");// remove empty
                                                                // spaces
        NumberFormat nf = NumberFormat.getInstance();
        ForecastedAmount = nf.parse(ForecastedAmount).toString();
      }
      opportunityVO.setForecastedAmount(Float.parseFloat(ForecastedAmount));
      opportunityVO.setProbability(Integer.parseInt(opportunityForm.getProbabilityid()));

      if ((opportunityForm.getSourceid() != null)
          && ((opportunityForm.getSourceid()).length() > 0)) {
        opportunityVO.setSourceID(Integer.parseInt(opportunityForm.getSourceid()));
      }
      opportunityVO.setSource(opportunityForm.getSourcename());

      if (opportunityForm.getAcctmgrid() != null
          && !opportunityForm.getAcctmgrid().trim().equals("")
          && !opportunityForm.getAcctmgrid().trim().equals("0")) {
        opportunityVO.setAcctMgr(Integer.parseInt(opportunityForm.getAcctmgrid()));
      } else {
        opportunityVO.setAcctMgr(ownerId);
      }

      if (!opportunityForm.getAcctteamid().trim().equals("")) {
        opportunityVO.setAcctTeam(Integer.parseInt(opportunityForm.getAcctteamid()));
      }

      if ((opportunityForm.getEstimatedclosemonth() != null)
          && (opportunityForm.getEstimatedcloseday() != null)
          && (opportunityForm.getEstimatedcloseyear() != null)) {
        String estimatedclosemonth = opportunityForm.getEstimatedclosemonth();
        String estimatedcloseday = opportunityForm.getEstimatedcloseday();
        String estimatedcloseyear = opportunityForm.getEstimatedcloseyear();

        try {
          Timestamp eClose = DateUtility.createTimestamp(estimatedcloseyear, estimatedclosemonth,
              estimatedcloseday);
          opportunityVO.setEstimatedClose(eClose);
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
        }
      }

      if (!allErrors.isEmpty()) {
        this.saveErrors(request, allErrors);
        return mapping.findForward(".view.sales.new_opportunity");
      }

      int recordID = remote.addOpportunity(ownerId, opportunityVO);
      rowID = recordID;

      request.setAttribute(SaleConstantKeys.CURRENTTAB, SaleConstantKeys.DETAIL);
      request.setAttribute(SaleConstantKeys.TYPEOFOPERATION, SaleConstantKeys.ADD);
      request.setAttribute(SaleConstantKeys.WINDOWID, "1");
      form = (OpportunityForm)form;

      String close = request.getParameter("closeornew");

      if (close.equals("close")) {
        return mapping.findForward(".forward.close_window");
      }
      request.setAttribute("closeWindow", "false");
      request.setAttribute("opportunityform", new OpportunityForm());

      FORWARD_final = FORWARD_saveOpportunity;
      request.setAttribute("refreshWindow", "true");

      if (!request.getParameter("Button").equals("yes")) {
        if ((request.getParameter("Flag").equals("Sales"))) {
          session.setAttribute("modulename", request.getParameter("Flag"));
          session.setAttribute("rowID", rowID + "");
          FORWARD_final = FORWARD_permission;
        }
      }
    } catch (Exception e) {
      logger.error("[Exception] [SaveOpportunityHandler.execute Calling Sale Facade] ", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
