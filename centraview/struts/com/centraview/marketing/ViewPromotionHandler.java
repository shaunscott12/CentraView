/*
 * $RCSfile: ViewPromotionHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:20 $ - $Author: mking_cv $
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

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.item.ItemList;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;
import com.centraview.marketing.promotion.PromotionVO;
import com.centraview.settings.Settings;

public class ViewPromotionHandler extends org.apache.struts.action.Action
{

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try
    {
      HttpSession session = request.getSession(true);
      int individualId = ((UserObject)session.getAttribute("userobject")).getIndividualID();
      MarketingFacadeHome home = (MarketingFacadeHome)CVUtility.getHomeObject("com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = home.create();
      remote.setDataSource(dataSource);

      // TODO so which one do we want the parameter or the Attribute?
      int promotionid = 0;
      if (request.getAttribute("promotionid") != null)
        promotionid = Integer.parseInt(request.getAttribute("promotionid").toString());
      if (promotionid == 0)
        promotionid = Integer.parseInt(request.getParameter("promotionid").toString());

      HashMap mapPromotion = new HashMap();
      mapPromotion.put("PromotionID", "" + promotionid);
      PromotionVO promotionVO = remote.getPromotion(individualId, mapPromotion);

      PromotionDetailListForm promotionDetailListForm = (PromotionDetailListForm)form;
      promotionDetailListForm.convertItemLines();
      promotionDetailListForm.setPromotionid("" + promotionid);
      promotionDetailListForm.setPname(promotionVO.getName());
      promotionDetailListForm.setPdescription(promotionVO.getDescription());
      promotionDetailListForm.setPstatus(promotionVO.getStatus());
      promotionDetailListForm.setNotes(promotionVO.getNotes());

      if (promotionVO.getStartdate() != null)
      {
        Timestamp date = (Timestamp)promotionVO.getStartdate();
        String strMonth = "" + (date.getMonth() + 1);
        String strDay = "" + date.getDate();
        String strYear = "" + (date.getYear() + 1900);
        promotionDetailListForm.setStartmonth(strMonth);
        promotionDetailListForm.setStartday(strDay);
        promotionDetailListForm.setStartyear(strYear);
      }

      if (promotionVO.getEnddate() != null)
      {
        Timestamp date = (Timestamp)promotionVO.getEnddate();
        String strMonth = "" + (date.getMonth() + 1);
        String strDay = "" + date.getDate();
        String strYear = "" + (date.getYear() + 1900);
        promotionDetailListForm.setEndmonth(strMonth);
        promotionDetailListForm.setEndday(strDay);
        promotionDetailListForm.setEndyear(strYear);
      }

      ItemLines itemLines = promotionVO.getItemlines();
      promotionDetailListForm.setItemLines(itemLines);

	  
      if (request.getParameter(MarketingConstantKeys.TYPEOFOPERATION) != null)
        if (request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).equals("REMOVEITEM"))
        {
          String removeIDs = request.getParameter("removeID");
          StringTokenizer st;
          Iterator itr;
          Vector removeKeys = new Vector();
          if (itemLines != null)
          {
            st = new StringTokenizer(removeIDs, ",");
            while (st.hasMoreTokens())
            {
              String str = st.nextToken();
              int removeToken = Integer.parseInt(str);

              itr = itemLines.keySet().iterator();
              while (itr.hasNext())
              {
                Object obj = itr.next();
                ItemElement ILE = (ItemElement)itemLines.get(obj);
                IntMember ItemId = (IntMember)ILE.get("ItemId");

                Integer currItemId = (Integer)ItemId.getMemberValue();
                if (currItemId.intValue() == removeToken)
                {
                  String status = ILE.getLineStatus();
                  if (status.equals("Active") || status.equals("") )
                  {
                    ILE.setLineStatus("Deleted");
                    removeKeys.add(obj);

                  }
                  else if (status.equals("New"))
                  {

                    removeKeys.add(obj);
                  }
                }
              }
            }

            for (int i = 0; i < removeKeys.size(); i++)
            {
              itemLines.remove(removeKeys.get(i));
            }
          }

          promotionDetailListForm.setItemLines(itemLines);
        }

      /*
      	ADD item
      */
      else if (request.getParameter(MarketingConstantKeys.TYPEOFOPERATION) != null)
        if (request.getParameter(MarketingConstantKeys.TYPEOFOPERATION).equals("ADDITEM"))
        {
          String newItemID = request.getParameter("theitemid");

          ItemList IL = null;

          ListGenerator lg = ListGenerator.getListGenerator(dataSource); //get the List Generator object for Listing
          IL = (ItemList)lg.getItemList(individualId, 1, 10, "", "ItemID"); //called when the request for the list is for first time

          StringTokenizer st;
          String token, nextItr;
          Iterator itr = IL.keySet().iterator();

          if (newItemID != null)
          {
            st = new StringTokenizer(newItemID, ",");

            if (itemLines == null)
              itemLines = new ItemLines();
            int counter = itemLines.size();
            while (st.hasMoreTokens())
            {
              token = (String)st.nextToken();
              int intToken = Integer.parseInt(token);

              while (itr.hasNext())
              {
                nextItr = (String)itr.next();
                ListElement ile = (ListElement)IL.get(nextItr);
                IntMember smid = (IntMember)ile.get("ItemID");
                Integer listItemid = (Integer)smid.getMemberValue();

                if (listItemid.intValue() == intToken)
                {
                  StringMember smName = (StringMember)ile.get("Name"); // name = description
                  String name = (String)smName.getMemberValue();

                  StringMember smSku = (StringMember)ile.get("SKU");
                  String sku = (String)smSku.getMemberValue();

                  FloatMember smPrice = (FloatMember)ile.get("Price");
                  Float dp = (Float)smPrice.getMemberValue();
                  float dprice = dp.floatValue();
                  FloatMember smCost = (FloatMember)ile.get("Cost");
                  Float dc = (Float)smCost.getMemberValue();
                  float dcost = dc.floatValue();

                  int id = ile.getElementID();

                  IntMember LineId = new IntMember("LineId", 11, 10, "", 'T', false, 20);
                  IntMember ItemId = new IntMember("ItemId", id, 10, "", 'T', false, 20);
                  StringMember SKU = new StringMember("SKU", sku, 10, "", 'T', false);
                  StringMember Description = new StringMember("Description", name, 10, "", 'T', false);
                  FloatMember Quantity = new FloatMember("Quantity", new Float(0.0f), 10, "", 'T', false, 20);
                  FloatMember PriceEach = new FloatMember("PriceEach", new Float(0.0f), 10, "", 'T', false, 20);
                  FloatMember PriceExtended = new FloatMember("PriceExtended", new Float(0.0f), 10, "", 'T', false, 20);
                  FloatMember UnitTax = new FloatMember("UnitTax", new Float(0.0f), 10, "", 'T', false, 20);
                  FloatMember TaxRate = new FloatMember("UnitTaxrate", new Float(0.0f), 10, "", 'T', false, 20);
                  FloatMember OrderQuantity = new FloatMember("OrderQuantity", new Float(0.0f), 10, "", 'T', false, 20);
                  FloatMember PendingQuantity = new FloatMember("PendingQuantity", new Float(0.0f), 10, "", 'T', false, 20);
                  IntMember Value = new IntMember("Value", 0, 10, "", 'T', false, 20);
                  StringMember Type = new StringMember("Type", "", 10, "", 'T', false);
                  FloatMember DiscountedPrice = new FloatMember("DiscountedPrice", new Float(0.0f), 10, "", 'T', false, 20);
                  FloatMember price = new FloatMember("Price", dp, 10, "", 'T', false, 10);
                  FloatMember cost = new FloatMember("Cost", dc, 10, "", 'T', false, 10);

                  ItemElement ie = new ItemElement(11);
                  ie.put("LineId", LineId);
                  ie.put("ItemId", ItemId);
                  ie.put("SKU", SKU);
                  ie.put("Description", Description);
                  ie.put("Quantity", Quantity);
                  ie.put("PriceEach", PriceEach);
                  ie.put("PriceExtended", PriceExtended);
                  ie.put("UnitTax", UnitTax);
                  ie.put("UnitTaxrate", TaxRate);
                  ie.put("OrderQuantity", OrderQuantity);
                  ie.put("PendingQuantity", PendingQuantity);
                  ie.put("Type", Type);
                  ie.put("Value", Value);
                  ie.put("DiscountedPrice", DiscountedPrice);
                  ie.put("ListPrice", price);
                  ie.put("Cost", cost);

                  ie.setLineStatus("New");
                  counter += 1;
                  itemLines.put(counter+"", ie);

                  break;
                }
              }
            }
            promotionDetailListForm.setItemLines(itemLines);
          }
        }
      	request.setAttribute("ItemLines",itemLines);
		ArrayList colStatus = new ArrayList();
        colStatus.add(new DDNameValue("YES","Active"));
        colStatus.add(new DDNameValue("NO","Inactive"));
        request.setAttribute("colStatus", colStatus);      	
      	request.setAttribute("TypeOfOperation", "Promotions");
      	request.setAttribute("Operation", "Edit");
    }
    catch (Exception e)
    {
      System.out.println("[Exception][ViewPromotionHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
    }
    return (mapping.findForward(".view.marketing.edit.promotion"));
  }
}
