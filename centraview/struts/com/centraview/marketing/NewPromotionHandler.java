/*
 * $RCSfile: NewPromotionHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.item.ItemList;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElement;
import com.centraview.common.ListGenerator;
import com.centraview.common.StringMember;
import com.centraview.common.UserObject;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.settings.Settings;

public class NewPromotionHandler extends org.apache.struts.action.Action
 {

    // Global Forwards
    public static final String GLOBAL_FORWARD_failure = "failure";

    // Local Forwards
    private static final String FORWARD_Promotion = ".view.marketing.new.promotion";
	private static final String FORWARD_PromotionList = ".view.marketing.promotions.list";
    private static String FORWARD_final = GLOBAL_FORWARD_failure;
	static int counter=0;
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
    	String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
		try {

			HttpSession session = request.getSession(true);
	        UserObject  userobjectd = (UserObject)session.getAttribute( "userobject" );

		    int individualId = userobjectd.getIndividualID();

		    PromotionDetailListForm promotionDetailListForm=(PromotionDetailListForm)form;
		    promotionDetailListForm.convertItemLines();

		    request.setAttribute("promotionlistform", promotionDetailListForm);

			ItemLines itemLines=null;

			request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVOICE);
			request.setAttribute("body",AccountConstantKeys.ADD );

			Vector customFieldVec = getCustomFieldVO(request,response);

			/*	Remove item */
			if ( request.getParameter(AccountConstantKeys.TYPEOFOPERATION) != null && request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals("REMOVEITEM") )
			{
				String removeIDs = request.getParameter("removeID");
				StringTokenizer st;
				Iterator itr;
				Vector removeKeys = new Vector();

				// added by Sandie for handling custom field values
				request.setAttribute("CustomFieldVector",customFieldVec);
				// added by Sandie - end

				itemLines = ((PromotionDetailListForm)form).getItemLines();
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
							if ( currItemId.intValue() == removeToken)
							{
								String status = ILE.getLineStatus();
								if (status.equals("Active") || status.equals(""))
								{
									ILE.setLineStatus("Deleted");
									removeKeys.add(obj);
								}
								else  if (status.equals("New"))
								{

								 	removeKeys.add(obj);
								}
							}
						}
					}

					for(int i=0; i<removeKeys.size(); i++)
					{
						itemLines.remove(removeKeys.get(i));
					}
				}

  				((PromotionDetailListForm)form).setItemLines(itemLines);
			}


			//	ADD item

			else if ( (request.getParameter(AccountConstantKeys.TYPEOFOPERATION) != null) && request.getParameter(AccountConstantKeys.TYPEOFOPERATION).equals("ADDITEM") )
			{
			// added by Sandie for handling custom field values
			request.setAttribute("CustomFieldVector",customFieldVec);
			// added by Sandie - end
			String newItemID = request.getParameter("theitemid");

			ItemList IL = null ;

			ListGenerator lg = ListGenerator.getListGenerator(dataSource);//get the List Generator object for Listing
			IL = (ItemList )lg.getItemList(individualId , 1, 10 , "" , "ItemID");//called when the request for the list is for first time


			StringTokenizer st;
			String token, nextItr;
			Iterator itr = IL.keySet().iterator();

			if (newItemID != null)
			{
				st = new StringTokenizer(newItemID, ",");
				itemLines = ((PromotionDetailListForm)form).getItemLines();
				if(itemLines == null)
					itemLines = new ItemLines();
				counter = itemLines.size();
				while (st.hasMoreTokens())
				{
					token   = (String)st.nextToken();
					int intToken = Integer.parseInt(token);

					while (itr.hasNext())
					{
						nextItr = (String)itr.next();
						ListElement ile = (ListElement)IL.get(nextItr);
						IntMember smid = (IntMember)ile.get("ItemID");
						Integer listItemid = (Integer)smid.getMemberValue();

						if ( listItemid.intValue() == intToken )
						{
							StringMember smName = (StringMember)ile.get("Name"); // name = description
							String name = (String) smName.getMemberValue();

							StringMember smType = (StringMember)ile.get("Type"); // type
							String type = (String) smType.getMemberValue();

							StringMember smSku  = (StringMember) ile.get("SKU");
							String sku = (String)smSku.getMemberValue();

							FloatMember smPrice  = (FloatMember) ile.get("Price");

							float dprice = Float.parseFloat((smPrice.getMemberValue()).toString());

							FloatMember smCost  = (FloatMember) ile.get("Price");

							float dcost = Float.parseFloat((smCost.getMemberValue()).toString());

							int id = ile.getElementID();

							IntMember LineId = new IntMember("LineId",11,10,"",'T',false,20);
							IntMember ItemId = new IntMember("ItemId",id,10,"",'T',false,20);
							StringMember SKU = new StringMember("SKU",sku,10,"",'T',false);
							StringMember Description = new StringMember("Description",name,10,"",'T',false);
							FloatMember  Quantity = new FloatMember("Quantity",new Float(0.0f),10,"",'T',false,20);
							FloatMember  PriceEach = new FloatMember("PriceEach",new Float(0.0f),10,"",'T',false,20);
							FloatMember  PriceExtended = new FloatMember("PriceExtended",new Float(0.0f),10,"",'T',false,20);
							FloatMember  UnitTax = new FloatMember("UnitTax",new Float(0.0f),10,"",'T',false,20);
							FloatMember  TaxRate = new FloatMember("UnitTaxrate",new Float(0.0f),10,"",'T',false,20);
							FloatMember  OrderQuantity = new FloatMember("OrderQuantity",new Float(0.0f),10,"",'T',false,20);
							FloatMember  PendingQuantity = new FloatMember("PendingQuantity",new Float(0.0f),10,"",'T',false,20);
							IntMember 	Value = new IntMember("Value",0,10,"",'T',false,20);
							StringMember Type = new StringMember("Type",type,10,"",'T',false);
							FloatMember  DiscountedPrice = new FloatMember("DiscountedPrice",new Float(0.0f),10,"",'T',false,20);
							FloatMember  price = new FloatMember( "Price"  ,new Float(dprice) , 10 , "", 'T' , false , 10 );
							FloatMember cost	= new FloatMember( "Cost"  ,new Float(dcost) , 10 , "", 'T' , false , 10 );


							ItemElement ie = new ItemElement(11);
							ie.put ("LineId",LineId);
							ie.put ("ItemId",ItemId);
							ie.put ("SKU",SKU);
							ie.put ("Description",Description);
							ie.put ("Quantity",Quantity);
							ie.put ("PriceEach",PriceEach);
							ie.put ("PriceExtended",PriceExtended);
							ie.put ("UnitTax",UnitTax);
							ie.put ("UnitTaxrate",TaxRate);
							ie.put ("OrderQuantity",OrderQuantity);
							ie.put ("PendingQuantity",PendingQuantity);
							ie.put ("Value",Value);
							ie.put ("Type",Type);
							ie.put ("DiscountedPrice",DiscountedPrice);
							ie.put ("ListPrice",price);
							ie.put ("Cost",cost);

							ie.setLineStatus("New");
							counter += 1;
							itemLines.put(counter+"", ie);
							break;
						}
					}
				}

				((PromotionDetailListForm)form).setItemLines(itemLines);

			  }
			} else
			{
				form = null;
				form =  new PromotionDetailListForm();
				request.setAttribute("promotionlistform", new PromotionDetailListForm());
			}
			request.setAttribute("ItemLines",itemLines);
			ArrayList colStatus = new ArrayList();
            colStatus.add(new DDNameValue("YES","Active"));
            colStatus.add(new DDNameValue("NO","Inactive"));
            request.setAttribute("colStatus", colStatus);

            // forward to jsp page
			FORWARD_final = FORWARD_Promotion;
			if (request.getParameter("Forward") != null && request.getParameter("Forward").equals("Listing"))
			{
				FORWARD_final = FORWARD_PromotionList;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			FORWARD_final = GLOBAL_FORWARD_failure;
		}
		finally
		{
			request.setAttribute("TypeOfOperation","Promotions" );
			request.setAttribute("Operation","New" );
			if (request.getParameter("operation") != null)
			{
				if (request.getParameter("operation").equals("Edit"))
					request.setAttribute("Operation","Edit" );
			}
		}
		return (mapping.findForward(FORWARD_final));
    }


    public Vector getCustomFieldVO(HttpServletRequest request,HttpServletResponse response)
    {
    	Vector vec = new Vector();
    	int total = 0;
		String Total = request.getParameter("TotalCustomFields");
		if ( Total != null)
		{
			try
			{
				total = Integer.parseInt(Total);
			}catch(Exception e)
			{
				total = 0;
			}
		}
    	for (int i =1;i<=total;i++)  // started from 1 ...
    	{
    		String fieldid 		= request.getParameter("fieldid"+i);
    		String fieldType	= request.getParameter("fieldType"+i);
    		String textValue 	= request.getParameter("text"+i);

    		if(fieldid == null)
    			fieldid = "0";
    		int intfieldId	 = Integer.parseInt(fieldid);
    		CustomFieldVO cfvo = new CustomFieldVO();
    		cfvo.setFieldID(intfieldId);
    		cfvo.setFieldType(fieldType);
    		cfvo.setValue(textValue);

    		if(intfieldId != 0 )
    			vec.add(cfvo);
    	}
    	return vec;
    }// end of getCustomFieldVO

}

