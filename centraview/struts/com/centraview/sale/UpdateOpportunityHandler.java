/*
 * $RCSfile: UpdateOpportunityHandler.java,v $    $Revision: 1.4 $  $Date: 2005/10/17 17:11:34 $ - $Author: mcallist $
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

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.activity.helper.ActivityVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.sale.opportunity.OpportunityVO;
import com.centraview.sale.salefacade.SaleFacade;
import com.centraview.sale.salefacade.SaleFacadeHome;
import com.centraview.settings.Settings;

public class UpdateOpportunityHandler extends Action {
  private static Logger logger = Logger.getLogger(UpdateOpportunityHandler.class);
  public static final String GLOBAL_FORWARD_failure = ".view.error";
  private static final String FORWARD_saveOpportunity = ".forward.sales.view_opportunity";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HttpSession session = request.getSession(true);
      SaleFacadeHome salesFacadeHome = (SaleFacadeHome) CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome", "SaleFacade");
      SaleFacade remote = salesFacadeHome.create();
      remote.setDataSource(dataSource);

      String typeofoperation = SaleConstantKeys.ADD;
      if (request.getParameter(SaleConstantKeys.TYPEOFOPERATION) != null) {
        typeofoperation = request.getParameter(SaleConstantKeys.TYPEOFOPERATION);
      } else if (request.getAttribute(SaleConstantKeys.TYPEOFOPERATION) != null) {
        typeofoperation = (String) request.getAttribute(SaleConstantKeys.TYPEOFOPERATION);
      }

      OpportunityForm opportunityForm = (OpportunityForm) form;

      OpportunityVO opportunityVO = new OpportunityVO();
      ActivityVO activityVO = new ActivityVO();
      activityVO.setStatus(Integer.parseInt(opportunityForm.getStatusid()));
      activityVO.setTitle(opportunityForm.getTitle());
      activityVO.setActivityDetails(opportunityForm.getDescription());

      int ownerId = ((UserObject) session.getAttribute("userobject")).getIndividualID();

      activityVO.setCreatedBy(ownerId);
      activityVO.setOwner(ownerId);
      activityVO.setActivityID(Integer.parseInt(opportunityForm.getActivityid()));
      opportunityVO.setActivityVO(activityVO);
      opportunityVO.setTitle(opportunityForm.getTitle());
      opportunityVO.setDescription(opportunityForm.getDescription());
      opportunityVO.setEntityID(Integer.parseInt(opportunityForm.getEntityid()));
      opportunityVO.setIndividualID(Integer.parseInt(opportunityForm.getIndividualid()));
      opportunityVO.setOpportunityTypeID(Integer.parseInt(opportunityForm.getOpportunitytypeid()));
      opportunityVO.setStatusID(Integer.parseInt(opportunityForm.getStatusid()));
      opportunityVO.setStageID(Integer.parseInt(opportunityForm.getStageid()));
      String Totalamount = opportunityForm.getTotalamount();
      if (Totalamount != null) {
        Totalamount = Totalamount.replaceAll(" ", "");//remove empty spaces
        NumberFormat nf = NumberFormat.getInstance();      	
        Totalamount = nf.parse(Totalamount).toString();
      }
      opportunityVO.setActualAmount(Float.parseFloat(Totalamount));
      opportunityVO.setProbability(Integer.parseInt(opportunityForm.getProbabilityid()));
      opportunityVO.setForecastedAmount(Float.parseFloat(Totalamount));
      opportunityVO.setSourceID(Integer.parseInt(opportunityForm.getSourceid()));
      opportunityVO.setSource(opportunityForm.getSourcename());
      opportunityVO.setAcctMgr(Integer.parseInt(opportunityForm.getAcctmgrid()));
      opportunityVO.setAcctTeam(Integer.parseInt(opportunityForm.getAcctteamid()));
      if ((opportunityForm.getEstimatedclosemonth() != null) && (opportunityForm.getEstimatedcloseday() != null)
          && (opportunityForm.getEstimatedcloseyear() != null)) {
        String estimatedclosemonth = opportunityForm.getEstimatedclosemonth();
        String estimatedcloseday = opportunityForm.getEstimatedcloseday();
        String estimatedcloseyear = opportunityForm.getEstimatedcloseyear();
        try {
          int estimatedclosemonth1 = Integer.parseInt(estimatedclosemonth);
          int estimatedcloseday1 = Integer.parseInt(estimatedcloseday);
          int estimatedcloseyear1 = Integer.parseInt(estimatedcloseyear);
          Calendar estimatedClose = new GregorianCalendar();
          estimatedClose.set(estimatedcloseyear1, estimatedclosemonth1 - 1, estimatedcloseday1);
          opportunityVO.setEstimatedClose(new Timestamp(estimatedClose.getTimeInMillis()));
        } catch (NumberFormatException nfe) {
          logger.info("[execute]: blank date fields");
        } catch (Exception e) {
          logger.error("[execute]: Exception");
        }
      }

      if (typeofoperation.equals(SaleConstantKeys.ADD)) {
        remote.addOpportunity(ownerId, opportunityVO);
      } else if (typeofoperation.equals(SaleConstantKeys.EDIT)) {
        String opportunityid = request.getParameter("opportunityid");

        if ((opportunityForm.getActualclosemonth() != null) && (opportunityForm.getActualcloseday() != null)
            && (opportunityForm.getActualcloseyear() != null)) {
          String actualclosemonth = opportunityForm.getActualclosemonth();
          String actualcloseday = opportunityForm.getActualcloseday();
          String actualcloseyear = opportunityForm.getActualcloseyear();

          try {
            int actualclosemonth1 = Integer.parseInt(actualclosemonth);
            int actualcloseday1 = Integer.parseInt(actualcloseday);
            int actualcloseyear1 = Integer.parseInt(actualcloseyear);
            Calendar actualClose = new GregorianCalendar(actualcloseyear1, actualclosemonth1 - 1, actualcloseday1);
            opportunityVO.setActualclose(new Timestamp(actualClose.getTimeInMillis()));
          } catch (NumberFormatException nfe) {
            logger.info("[execute]: blank date fields");
          } catch (Exception e) {
            logger.error("[execute]: Exception", e);
          }
        }
        opportunityVO.setOpportunityID(Integer.parseInt(opportunityid));
        request.setAttribute("recordID", new Integer(opportunityVO.getOpportunityID()).toString());
        request.setAttribute("recordName", opportunityVO.getTitle());
        request.setAttribute("OPPORTUNITYID", new Integer(opportunityVO.getOpportunityID()));
        remote.updateOpportunity(ownerId, opportunityVO);
      }
      request.setAttribute(SaleConstantKeys.CURRENTTAB, SaleConstantKeys.DETAIL);
      request.setAttribute(SaleConstantKeys.TYPEOFOPERATION, SaleConstantKeys.ADD);
      request.setAttribute(SaleConstantKeys.WINDOWID, "1");
      if (request.getParameter("closeornew").equals("close")) {
        return mapping.findForward(".forward.close_window");
      } else if (request.getParameter("closeornew").equals("new")) {
        request.setAttribute("closeWindow", "false");
        request.setAttribute("reset", "true");
      } else if (request.getParameter("closeornew").equals("savechanges")) {
        request.setAttribute("closeWindow", "false");
        request.setAttribute("reset", "false");
      }
      FORWARD_final = FORWARD_saveOpportunity;
      request.setAttribute("refreshWindow", "true");
      if (request.getParameter("hasproposal").equals("false")) {
        request.setAttribute("hasproposal", new Boolean(false));
      } else {
        request.setAttribute("hasproposal", new Boolean(true));
      }
      form = ((OpportunityForm) form);
      form.reset(mapping, request);
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
