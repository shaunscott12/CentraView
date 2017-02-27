/*
 * $RCSfile: ViewOpportunityHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:43 $ - $Author: mking_cv $
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
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
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

/**
 * ViewOpportunityHandler is used to view opportunity information.
 */
public class ViewOpportunityHandler extends Action
{
  public static final int DATE_YYYY = 1;
  public static final int DATE_MM = 2;
  public static final int DATE_DD = 3;

  public static final String GLOBAL_FORWARD_failure = ".view.error";
  public static final String FORWARD_viewopportunity = ".view.sales.opportunity_details";
  private static final String FORWARD_newOpportunity = ".view.sales.new_opportunity";
  String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    request.setAttribute("recordType", "Opportunity");
    
    try {
      String newWindowId = "1";

      HttpSession session = request.getSession();
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualID = userObject.getIndividualID();

      OpportunityForm oForm = (OpportunityForm)form;

      SaleFacadeHome saleFacade = (SaleFacadeHome)CVUtility.getHomeObject("com.centraview.sale.salefacade.SaleFacadeHome","SaleFacade");
      SaleFacade remote = (SaleFacade)saleFacade.create();
      remote.setDataSource(dataSource);

      // Check the session for an existing error message (possibly from the delete handler)
      ActionErrors allErrors = (ActionErrors)session.getAttribute("listErrorMessage");
      if (allErrors != null) {
        saveErrors(request, allErrors);
        session.removeAttribute("listErrorMessage");
      }
      
      // set the VO from form bean data
      // set the opportunityVO
      String opportunityID = request.getParameter("OPPORTUNITYID");
      if (opportunityID == null) {
        if (request.getAttribute("OPPORTUNITYID") != null) {
          opportunityID = ((Integer) request.getAttribute("OPPORTUNITYID")).toString();
        }else if (request.getParameter("rowId") != null){
          opportunityID = request.getParameter("rowId");
        }else if (request.getParameter("activityId") != null){
          String activityID = request.getParameter("activityId");
          if (activityID != null && !(activityID.equals(""))) {
            opportunityID = remote.getOpportunityID(Integer.parseInt(activityID));
          }
        }
      }
      
      if (opportunityID == null || opportunityID.trim().length() == 0) {
        opportunityID = "0";
      }

      
      session.setAttribute("rowID", opportunityID);
      
      OpportunityVO oVO = remote.getOpportunity(individualID, Integer.parseInt(opportunityID));

      oForm.setOpportunityid(new Integer(oVO.getOpportunityID()).toString());
      request.setAttribute("recordID", new Integer(oVO.getOpportunityID()).toString());
      request.setAttribute("recordId", String.valueOf(opportunityID));
      request.setAttribute("recordName", java.net.URLEncoder.encode(oVO.getTitle(), "ISO-8859-1"));
      
      
      oForm.setTitle(oVO.getTitle());
      String description = oVO.getDescription();
      oForm.setDescription(description);
      oForm.setEntityid(new Integer(oVO.getEntityID()).toString());
      oForm.setEntityname(oVO.getEntityname());
      request.setAttribute("parentId", String.valueOf(oVO.getEntityID()));
      request.setAttribute("parentName", java.net.URLEncoder.encode(oVO.getEntityname(), "ISO-8859-1"));
      oForm.setIndividualid(new Integer(oVO.getIndividualID()).toString());
      oForm.setIndividualname(oVO.getIndividualname());
      oForm.setAcctmgrid(new Integer(oVO.getAcctMgr()).toString());
      oForm.setAcctmgrname(oVO.getManagerName());
      oForm.setAcctteamid(new Integer(oVO.getAcctTeam()).toString());
      oForm.setAcctteamname(oVO.getTeamName());
      oForm.setSourceid(new Integer(oVO.getSourceID()).toString());
      oForm.setStageid(new Integer(oVO.getStageID()).toString());
      oForm.setStatusid(new Integer(oVO.getStatusID()).toString());
      oForm.setSourcename(oVO.getSource());
      
      if (oVO.getEstimatedClose() != null) {
        Timestamp date = (Timestamp)oVO.getEstimatedClose();
        String strMonth = String.valueOf(date.getMonth() + 1);
        String strDay = String.valueOf(date.getDate());
        String strYear = String.valueOf(date.getYear() + 1900);
        oForm.setEstimatedcloseday(strDay);
        oForm.setEstimatedclosemonth(strMonth);
        oForm.setEstimatedcloseyear(strYear);
      }
      
      if (oVO.getActualclose() != null) {
        Timestamp date = (Timestamp)oVO.getActualclose();
        String strMonth = String.valueOf(date.getMonth() + 1);
        String strDay = String.valueOf(date.getDate());
        String strYear = String.valueOf(date.getYear() + 1900);
        oForm.setActualcloseday(strDay);
        oForm.setActualclosemonth(strMonth);
        oForm.setActualcloseyear(strYear);
      }
      
      oForm.setOpportunitytypeid((new Integer(oVO.getOpportunityTypeID())).toString());
      oForm.setProbabilityid((new Integer(oVO.getProbability())).toString());
      DecimalFormat currencyFormat = new DecimalFormat("###,###,##0.00");
      String actualAmount = currencyFormat.format(oVO.getActualAmount());
      oForm.setTotalamount(actualAmount);
      
      String forecast = currencyFormat.format(oVO.getForecastedAmount());
      oForm.setForecastedamount(forecast);
      
      if (oVO.getCreateddate() != null) {
        Timestamp date = (Timestamp)oVO.getCreateddate();
        
        String strMonth = String.valueOf(date.getMonth() + 1);
        String strDay = String.valueOf(date.getDate());
        String strYear = String.valueOf(date.getYear() + 1900);
        String strHours = String.valueOf(date.getHours());
        String strMins = String.valueOf(date.getMinutes());
        int hhmm[] = new int[2];
        hhmm[0] = Integer.parseInt(strHours.trim());
        hhmm[1] = Integer.parseInt(strMins.trim());
        String strTime = CVUtility.convertTime24HrsFormatToStr(hhmm);
        oForm.setCreateddate(strMonth + "/" + strDay + "/" + strYear + " - " + strTime + " ");
      }
      
      if (oVO.getModifieddate() != null) {
        Timestamp date = (Timestamp)oVO.getModifieddate();
        
        String strMonth = String.valueOf(date.getMonth() + 1);
        String strDay = String.valueOf(date.getDate());
        String strYear = String.valueOf(date.getYear() + 1900);
        String strHours = String.valueOf(date.getHours());
        String strMins = String.valueOf(date.getMinutes());
        
        int hhmm[] = new int[2];
        hhmm[0] = Integer.parseInt(strHours.trim());
        hhmm[1] = Integer.parseInt(strMins.trim());
        String strTime = CVUtility.convertTime24HrsFormatToStr(hhmm);
        oForm.setModifieddate(strMonth + "/" + strDay + "/" + strYear + " - " + strTime + " ");
      }
      
      oForm.setCreatedbyname(oVO.getCreatedbyname());
      oForm.setModifiedbyname(oVO.getModifiedbyname());
      oForm.setActivityid(String.valueOf(oVO.getActivityID()));
      
      ActivityVO activityVO = oVO.getActivityVO();

      form = (ActionForm) oForm;

      request.setAttribute(SaleConstantKeys.CURRENTTAB, SaleConstantKeys.DETAIL);
      request.setAttribute(SaleConstantKeys.TYPEOFOPERATION, SaleConstantKeys.EDIT);
      request.setAttribute(SaleConstantKeys.WINDOWID, newWindowId);
      request.setAttribute("opportunityform", form);
      request.setAttribute("hasproposal",new Boolean(oVO.getHasProposal()));
      request.setAttribute("hasProposalInForcast",new Boolean(oVO.getHasProposalInForcast()));

      if (request.getParameter("Duplicate") != null) {
        if (request.getParameter("Duplicate").equals("true")) {
          FORWARD_final = FORWARD_newOpportunity;
        }else{
          FORWARD_final = FORWARD_viewopportunity;
        }
      }else{
        FORWARD_final = FORWARD_viewopportunity;
      }
    }catch (Exception e){
      System.out.println("[Exception] ViewOpportunityHandler.execute: " + e.toString());
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }   // end execute() method

  static class DateTimeUtil
  {
    public static final int DATE_YYYY = 1;
    public static final int DATE_MM = 2;
    public static final int DATE_DD = 3;
    public static final int TIME_HH = 1;
    public static final int TIME_MM = 2;
    public static final int TIME_SS = 3;

    public static String parseDate(java.sql.Date date, int token)
    {
      String dateStr = date.toString().trim();
      dateStr = dateStr.substring(0, dateStr.indexOf(" "));
      StringTokenizer dateTokenizer = new StringTokenizer(dateStr,".",false);
      String dateToken = "";
      for (int i= 0; dateTokenizer.hasMoreTokens(); i++)
      {
        dateToken = dateTokenizer.nextToken();
        if ((i + 1) == token)
        {
          return dateToken;
        } //end of if statement ((i + 1) == token)
      } //end of foor loop (int i= 0; dateTokenizer.hasMoreTokens(); i++)
      return dateToken;
    } //end of parseDate method

    public static String parseDate(java.sql.Timestamp timestamp, int token)
    {
      String dateStr = timestamp.toString().trim();
      dateStr = dateStr.substring(0, dateStr.indexOf(" "));
      StringTokenizer dateTokenizer = new StringTokenizer(dateStr,"-",false);
      String dateToken = "";
      for (int i= 0; dateTokenizer.hasMoreTokens(); i++)
      {
        dateToken = dateTokenizer.nextToken();
        if ((i + 1) == token)
        {
          return dateToken;
        } //end of if statement ((i + 1) == token)
      } //end of for loop (int i= 0; dateTokenizer.hasMoreTokens(); i++)
      return dateToken;
    } //end of parseDate method

    public static String parseTime(java.sql.Timestamp timestamp, int token)
    {
      String timeStr = timestamp.toString().trim();
      timeStr = timeStr.substring(timeStr.indexOf(" "));
      timeStr = timeStr.substring(0,timeStr.indexOf("."));
      StringTokenizer timeTokenizer = new StringTokenizer(timeStr,":",false);
      String timeToken = "";
      for (int i= 0; timeTokenizer.hasMoreTokens(); i++)
      {
        timeToken = timeTokenizer.nextToken();
        if ((i + 1) == token)
        {
          return timeToken;
        }  //end of if statement ((i + 1) == token)
      } //end of for loop (int i= 0; timeTokenizer.hasMoreTokens(); i++)
      return timeToken;
    } //end of parseTime method

    public static String formatDateTime(java.sql.Timestamp timestamp)
    {
      String dateFormat = DateTimeUtil.parseDate(timestamp,DateTimeUtil.DATE_DD);
      dateFormat += "/" + DateTimeUtil.parseDate(timestamp,DateTimeUtil.DATE_MM);
      dateFormat += "/" + DateTimeUtil.parseDate(timestamp,DateTimeUtil.DATE_YYYY);
      dateFormat += " - " + DateTimeUtil.parseTime(timestamp,DateTimeUtil.TIME_HH);
      dateFormat += ":" + DateTimeUtil.parseTime(timestamp,DateTimeUtil.TIME_MM);
      return dateFormat;
    } //end of formatDateTime method
  }

}
