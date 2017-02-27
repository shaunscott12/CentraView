/*
 * $RCSfile: SavePromotionHandler.java,v $    $Revision: 1.2 $  $Date: 2005/10/17 17:11:42 $ - $Author: mcallist $
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
import java.util.Vector;

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
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.marketing.promotion.PromotionVO;
import com.centraview.settings.Settings;

public class SavePromotionHandler extends org.apache.struts.action.Action {
  private static Logger logger = Logger.getLogger(SavePromotionHandler.class);

  public static final String GLOBAL_FORWARD_failure = "failure";

  private static final String FORWARD_savepromotion = ".view.marketing.edit.promotion";
  private static final String FORWARD_savepromotionnew = ".view.marketing.new.promotion";
  private static final String FORWARD_PromotionList = ".view.marketing.promotions.list";

  private static String FORWARD_final = GLOBAL_FORWARD_failure;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      PromotionDetailListForm promotionDetailListForm;
      promotionDetailListForm = (PromotionDetailListForm)form;
      promotionDetailListForm.convertItemLines();

      PromotionVO promotionVO = new PromotionVO();
      promotionVO.setName(promotionDetailListForm.getPname());
      promotionVO.setDescription(promotionDetailListForm.getPdescription());
      promotionVO.setStatus(promotionDetailListForm.getPstatus());
      promotionVO.setNotes(promotionDetailListForm.getNotes());
      promotionVO.setItemlines(promotionDetailListForm.getItemLines());
      promotionVO.setNotes(promotionDetailListForm.getNotes());

      if ((promotionDetailListForm.getStartmonth() != null)
          && (promotionDetailListForm.getStartday() != null)
          && (promotionDetailListForm.getStartyear() != null)) {
        String startmonth = promotionDetailListForm.getStartmonth();
        String startday = promotionDetailListForm.getStartday();
        String startyear = promotionDetailListForm.getStartyear();
        try {
          Timestamp start = DateUtility.createTimestamp(startyear, startmonth, startday);
          promotionVO.setStartdate(start);
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
        }
      }

      if ((promotionDetailListForm.getEndmonth() != null)
          && (promotionDetailListForm.getEndday() != null)
          && (promotionDetailListForm.getEndyear() != null)) {
        String endmonth = promotionDetailListForm.getEndmonth();
        String endday = promotionDetailListForm.getEndday();
        String endyear = promotionDetailListForm.getEndyear();
        try {
          Timestamp end = DateUtility.createTimestamp(endyear, endmonth, endday);
          promotionVO.setEnddate(end);
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
        }
      }

      Vector vec = new Vector();
      int total = 0;
      String Total = request.getParameter("TotalCustomFields");
      if (Total != null) {
        try {
          total = Integer.parseInt(Total);
        } catch (Exception e) {
          logger.error("[execute]: Exception", e);
          total = 0;
        }
      }

      for (int i = 1; i <= total; i++) // starting from 1...
      {
        String fieldid = request.getParameter("fieldid" + i);
        String fieldType = request.getParameter("fieldType" + i);
        String textValue = request.getParameter("text" + i);

        if (fieldid == null)
          fieldid = "0";
        int intfieldId = Integer.parseInt(fieldid);
        CustomFieldVO cfvo = new CustomFieldVO();
        cfvo.setFieldID(intfieldId);
        cfvo.setFieldType(fieldType);
        cfvo.setValue(textValue);

        if (intfieldId != 0)
          vec.add(cfvo);
      }

      promotionVO.setCustomfield(vec);

      HttpSession session = request.getSession();
      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      HashMap mapPromotion = new HashMap();
      String promotionid = "";

      if (request.getParameter("promotionid") != null) {
        promotionid = request.getParameter("promotionid");
        mapPromotion.put("PromotionID", promotionid);
      }

      MarketingFacadeHome mfh = (MarketingFacadeHome)CVUtility.getHomeObject(
          "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = mfh.create();
      remote.setDataSource(dataSource);
      if (request.getParameter("operation") != null) {
        if (request.getParameter("operation").equals("Add")) {
          mapPromotion.put("PromotionVo", promotionVO);
          promotionid = remote.addPromotion(individualId, mapPromotion);
          promotionDetailListForm.setPromotionid(promotionid);
          request.setAttribute("promotionid", promotionid);
        } else if (request.getParameter("operation").equals("Edit")) {

          promotionVO.setPromotionid(Integer.parseInt(promotionid));
          mapPromotion.put("PromotionVo", promotionVO);
          remote.updatePromotion(individualId, mapPromotion);
        }
      }

      request.setAttribute(MarketingConstantKeys.CURRENTTAB, MarketingConstantKeys.DETAIL);
      request.setAttribute(MarketingConstantKeys.TYPEOFOPERATION, MarketingConstantKeys.ADD);
      request.setAttribute(MarketingConstantKeys.WINDOWID, "1");
      if (request.getParameter("closeornew").equals("close")) {
        request.setAttribute("closeWindow", "true");
      } else if (request.getParameter("closeornew").equals("new")) {
        request.setAttribute("closeWindow", "false");
        request.setAttribute("reset", "true");
      }

      FORWARD_final = FORWARD_savepromotion;
      if (request.getParameter("closeornew") != null) {
        if (request.getParameter("closeornew").equals("close"))
          FORWARD_final = FORWARD_PromotionList;
        if (request.getParameter("closeornew").equals("new"))
          FORWARD_final = FORWARD_savepromotionnew;

        promotionDetailListForm.setPromotionid("");
        promotionDetailListForm.setPname("");
        promotionDetailListForm.setPdescription("");
        promotionDetailListForm.setPstatus("");
        promotionDetailListForm.setNotes("");
        promotionDetailListForm.setStartmonth("");
        promotionDetailListForm.setStartday("");
        promotionDetailListForm.setStartyear("");

        promotionDetailListForm.setEndmonth("");
        promotionDetailListForm.setEndday("");
        promotionDetailListForm.setEndyear("");
        promotionDetailListForm.setItemid(null);
        request.setAttribute("promotionlistform", promotionDetailListForm);

      }

      request.setAttribute("refreshWindow", "true");
      form.reset(mapping, request);
      form = promotionDetailListForm;
    } catch (Exception e) {
      logger.error("[execute]: Exception", e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
}
