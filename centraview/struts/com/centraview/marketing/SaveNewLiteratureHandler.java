/*
 * $RCSfile: SaveNewLiteratureHandler.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 17:11:42 $ - $Author: mcallist $
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

package com.centraview.marketing;

import java.sql.Timestamp;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.DateUtility;
import com.centraview.common.UserObject;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.settings.Settings;

public class SaveNewLiteratureHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(SaveNewLiteratureHandler.class);
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_saveliterature = ".view.marketing.literaturefulfillment.detail";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public SaveNewLiteratureHandler() {}

  /**
   * Executes initialization of required parameters and open window for entry of
   * note returns ActionForward
   */
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      String typeofoperation = MarketingConstantKeys.ADD;

      if (request.getParameter(MarketingConstantKeys.TYPEOFOPERATION) != null)
        typeofoperation = request.getParameter(MarketingConstantKeys.TYPEOFOPERATION);
      else if (request.getAttribute(MarketingConstantKeys.TYPEOFOPERATION) != null)
        typeofoperation = (String)request.getAttribute(MarketingConstantKeys.TYPEOFOPERATION);

      // call ejb to insert record
      // initialize hashmap
      HashMap mapLiterature = new HashMap();

      mapLiterature.put(" Title ", ((LiteratureForm)form).getTitle());
      mapLiterature.put(" Detail ", ((LiteratureForm)form).getDetail());

      if (((LiteratureForm)form).getAssignedtoid() != null
          && ((LiteratureForm)form).getAssignedtoid().length() > 0)
        mapLiterature.put(" AssignedTo ", ((LiteratureForm)form).getAssignedtoid());

      if (((LiteratureForm)form).getEntityid() != null
          && ((LiteratureForm)form).getEntityid().length() > 0)
        mapLiterature.put(" EntityId ", ((LiteratureForm)form).getEntityid());

      if (((LiteratureForm)form).getIndividualid() != null
          && ((LiteratureForm)form).getIndividualid().length() > 0)
        mapLiterature.put(" IndividualId ", ((LiteratureForm)form).getIndividualid());

      mapLiterature.put(" LiteratureIds ", ((LiteratureForm)form).getLiteratureid());

      mapLiterature.put(" StatusId ", ((LiteratureForm)form).getStatusid());
      mapLiterature.put(" DeliveryId ", ((LiteratureForm)form).getDeliverymethodid());

      if ((((LiteratureForm)form).getDuebymonth() != null)
          && (((LiteratureForm)form).getDuebyday() != null)
          && (((LiteratureForm)form).getDuebyyear() != null)) {
        String duebymonth = ((LiteratureForm)form).getDuebymonth();
        String duebyday = ((LiteratureForm)form).getDuebyday();
        String duebyyear = ((LiteratureForm)form).getDuebyyear();
        String duebyTime = ((LiteratureForm)form).getDuebytime();
        try {
          Timestamp due = DateUtility.createTimestamp(duebyyear, duebymonth, duebyday, duebyTime);
          mapLiterature.put(" DueBy ", due);
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
        }
      }

      // add record to db
      HttpSession session = request.getSession();

      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();

      mapLiterature.put(" Creator ", "" + individualId);
      mapLiterature.put(" Owner ", "" + individualId);

      // call to marketing facade
      MarketingFacadeHome mfh = (MarketingFacadeHome)CVUtility.getHomeObject(
          "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = mfh.create();
      remote.setDataSource(dataSource);
      if (typeofoperation.equals(MarketingConstantKeys.ADD))
        remote.addLiterature(mapLiterature);

      else if (typeofoperation.equals(MarketingConstantKeys.EDIT)) {
        String activityid = request.getParameter("activityid");
        mapLiterature.put(" Activityid ", activityid);
        // mapLiterature.put(" Activityid ",""+873);
        remote.editLiterature(mapLiterature);
      }

      ((LiteratureForm)form).setTitle("");
      ((LiteratureForm)form).setDetail("");
      ((LiteratureForm)form).setEntityname("");
      ((LiteratureForm)form).setEntityid("");
      ((LiteratureForm)form).setIndividualid("");
      ((LiteratureForm)form).setLiteratureid("");
      ((LiteratureForm)form).setIndividualname("");
      ((LiteratureForm)form).setAssignedtoname("");
      ((LiteratureForm)form).setAssignedtoid("");
      ((LiteratureForm)form).setDeliverymethodid("");
      ((LiteratureForm)form).setDeliverymethodname("");
      ((LiteratureForm)form).setStatusid("");
      ((LiteratureForm)form).setStatusname("");
      ((LiteratureForm)form).setDuebymonth("");
      ((LiteratureForm)form).setDuebyday("");
      ((LiteratureForm)form).setDuebytime("");
      ((LiteratureForm)form).setDuebyyear("");
      ((LiteratureForm)form).setLiteraturenamevec(null);
      ((LiteratureForm)form).setLiteraturename(null);

      // set request back to jsp
      request.setAttribute(MarketingConstantKeys.CURRENTTAB, MarketingConstantKeys.DETAIL);
      request.setAttribute(MarketingConstantKeys.TYPEOFOPERATION, MarketingConstantKeys.ADD);
      request.setAttribute(MarketingConstantKeys.WINDOWID, "1");
      FORWARD_final = FORWARD_saveliterature;
      if (request.getParameter("closeornew").equals("close")) {
        FORWARD_final = ".view.marketing.literaturefulfillment.close";
        request.setAttribute("closeWindow", "true");
      } else if (request.getParameter("closeornew").equals("new")) {
        request.setAttribute("closeWindow", "false");
        request.setAttribute("reset", "true");
      }
      request.setAttribute("refreshWindow", "true");

      form = ((LiteratureForm)form);
      form.reset(mapping, request);
      form = null;
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
