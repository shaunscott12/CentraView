/*
 * $RCSfile: AccountListEJB.java,v $    $Revision: 1.3 $  $Date: 2005/07/18 21:04:54 $ - $Author: mcallist $
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


package com.centraview.account.accountlist;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Logger;

import com.centraview.account.expense.ExpenseList;
import com.centraview.account.expense.ExpenseListElement;
import com.centraview.account.glaccount.GLAccountList;
import com.centraview.account.glaccount.GLAccountListElement;
import com.centraview.account.inventory.InventoryList;
import com.centraview.account.inventory.InventoryListElement;
import com.centraview.account.invoice.InvoiceList;
import com.centraview.account.invoice.InvoiceListElement;
import com.centraview.account.item.ItemList;
import com.centraview.account.item.ItemListElement;
import com.centraview.account.order.OrderList;
import com.centraview.account.order.OrderListElement;
import com.centraview.account.payment.PaymentList;
import com.centraview.account.payment.PaymentListElement;
import com.centraview.account.purchaseorder.PurchaseOrderList;
import com.centraview.account.purchaseorder.PurchaseOrderListElement;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.DateMember;
import com.centraview.common.DoubleMember;
import com.centraview.common.EJBUtil;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.LocationListElement;
import com.centraview.common.StringMember;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This is the EJB class for Accounting
 * The Logic for methods defined in Remote interface
 * is defined in this class.
 */
public class AccountListEJB implements SessionBean
{
  private static Logger logger = Logger.getLogger(AccountListEJB.class);
  protected SessionContext ctx;
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;
  }

  public void ejbCreate()
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  /**
   * Gets the Whole list of GLAccounts
   *
   * @param   individualID
   * @param   hashmap
   * @return
   */
  public GLAccountList getGLAccountList(int individualID, HashMap hashmap) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("GLAccount", individualID, this.dataSource))
    {
      throw new AuthorizationFailedException("GLAccount- getGLAccountList");
    }

    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");
    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int beginIndex = 0;

    GLAccountList glList = new GLAccountList();

    glList.setSortMember(strSortMem);

    CVDal cvdl = new CVDal(dataSource);

    Collection colList = null;

    String sortType = "ASC";

    if (charSort == 'D')
    {
      sortType = "DESC";
    }

    //SELECT gla.glaccountsid GLAccountsID,gla.title Name ,gla.accounttype Type ,gla.Balance Balance ,gla1.title ParentAccount FROM glaccount gla left outer join glaccount gla1 on gla.parent = gla1.glaccountsid
    String selectQuery =
      " SELECT gla.glaccountsid GLAccountsID,gla.title Name ,gla.glaccounttype Type ,gla.Balance Balance ,gla1.title ParentAccount ";
    String fromQuery =
      " FROM glaccount gla left outer join glaccount gla1 on gla.parent = gla1.glaccountsid";
    String whereQuery = " where 1=1 ";
    String groupByQuery =
      " group by GLAccountsID,Name,Type,Balance,ParentAccount";
    String orderByQuery = " order by '" + strSortMem + "' " + sortType;

    String listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    if ((strSearch != null) && strSearch.startsWith("SIMPLE :"))
    {
      strSearch = strSearch.substring(8);

      whereQuery =
        whereQuery + "AND  gla.glaccountsid like '%" + strSearch
        + "%'   OR gla.title like '%" + strSearch
        + "%' OR gla.accounttype like  '%" + strSearch
        + "%' OR gla.Balance like  '%" + strSearch + "%' OR gla1.title like '%"
        + strSearch + "%' ";
    }

    String advSearchstr = strSearch;

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      advSearchstr = advSearchstr.substring(8);

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `glaccountlistsearch`;");
      cvdl.executeUpdate();
      cvdl.clearParameters();

      strSearch = "create TEMPORARY TABLE glaccountlistsearch " + advSearchstr;

      cvdl.setSqlQuery(strSearch);
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.clearParameters();

      fromQuery = fromQuery + " ,glaccountlistsearch";
      whereQuery =
        whereQuery + " AND gla.glaccountsid = glaccountlistsearch.glaccountsid";
    }

    listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    cvdl.setSqlQuery(listQuery);
    colList = cvdl.executeQuery();
    cvdl.clearParameters();

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE glaccountlistsearch");
      cvdl.executeUpdate();
      cvdl.clearParameters();
    }

    cvdl.destroy();

    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();
        int glAccID = ((Long) hm.get("GLAccountsID")).intValue();

        String strGLAccountID = new String();
        strGLAccountID = String.valueOf(glAccID);

        try
        {
          StringMember strName = null;

          if ((hm.get("Name") != null))
          {
            strName =
              new StringMember("Name", (String) hm.get("Name"), 10, "", 'T',
                false);
          }
          else
          {
            strName = new StringMember("Name", null, 10, "", 'T', false);
          }

          StringMember strType =
            new StringMember("Type", (String) hm.get("Type"), 10, "", 'T', false);

          DoubleMember dblBalance =
            new DoubleMember("Balance",
              new Double(Double.parseDouble(hm.get("Balance").toString())), 10,
              "", 'T', false, 10);

          StringMember strParentAccount =
            new StringMember("ParentAccount", (String) hm.get("ParentAccount"),
              10, "", 'T', false);

          GLAccountListElement glListElement =
            new GLAccountListElement(glAccID);

          glListElement.put("GLAccountsID", strGLAccountID);
          glListElement.put("Name", strName);
          glListElement.put("Type", strType);
          glListElement.put("Balance", dblBalance);
          glListElement.put("ParentAccount", strParentAccount);

          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);

          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(),
            stringbuffer.length(), s3);

          String s4 = stringbuffer.toString();

          glList.put(s4, glListElement);
        }
        catch (Exception e)
        {
          System.out.println(" Exception in packing data " + e);

          //e.printStackTrace();
        }
      }
    }

    glList.setTotalNoOfRecords(glList.size());
    glList.setListType("GLAccount");
    glList.setBeginIndex(beginIndex);

    //filelist.setEndIndex(endIndex);
    glList.setEndIndex(glList.size());

    return glList;
  }
   // getGLAccountList ends here

  /**
   * Gets the Whole list of Inventory
   *
   * @param   individualID
   * @param   hashmap
   * @return
   */
  public InventoryList getInventoryList(int individualID, HashMap hashmap) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Inventory", individualID, this.dataSource))
    {
      throw new AuthorizationFailedException("Inventory- getInventoryList");
    }

    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int beginIndex = 0;

    InventoryList invList = new InventoryList();

    invList.setSortMember(strSortMem);

    CVDal cvdl = new CVDal(dataSource);

    Collection colList = null;

    String sortType = "ASC";

    if (charSort == 'D')
    {
      sortType = "DESC";
    }

    String selectQuery =
      " select ven.entityid as EntityID,inventory.inventoryid as InventoryID,item.title as ItemName,inventory.title as Identifier  ,man.name as Manufacturer,ven.name as Vendor";
    String fromQuery =
      " from inventory  left outer join item on inventory.item = item.itemid left outer join entity man on item.manufacturerid = man.entityid left outer join entity ven on item.vendorid  = ven.entityid";
    String whereQuery = " where inventory.linestatus != 'deleted' ";
    String groupByQuery =
      " group by EntityID,InventoryID,ItemName,Identifier,Manufacturer,Vendor";
    String orderByQuery = " order by '" + strSortMem + "' " + sortType;

    String listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    if ((strSearch != null) && strSearch.startsWith("SIMPLE :"))
    {
      strSearch = strSearch.substring(8);

      whereQuery =
        whereQuery + "AND  inventory.inventoryid like '%" + strSearch
        + "%'   OR item.title like '%" + strSearch
        + "%' OR inventory.title like  '%" + strSearch
        + "%' OR man.name like  '%" + strSearch + "%' OR ven.name like '%"
        + strSearch + "%' ";
    }

    String advSearchstr = strSearch;

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      advSearchstr = advSearchstr.substring(8);

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `inventorylistsearch`;");
      cvdl.executeUpdate();
      cvdl.clearParameters();

      strSearch = "create TEMPORARY TABLE inventorylistsearch " + advSearchstr;

      cvdl.setSqlQuery(strSearch);
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.clearParameters();

      fromQuery = fromQuery + " ,inventorylistsearch";
      whereQuery =
        whereQuery
        + " AND inventory.inventoryid = inventorylistsearch.inventoryid";
    }

    listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    cvdl.setSqlQuery(listQuery);
    colList = cvdl.executeQuery();
    cvdl.clearParameters();

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE inventorylistsearch");
      cvdl.executeUpdate();
      cvdl.clearParameters();
    }

    cvdl.destroy();

    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();
        int invID = ((Long) hm.get("InventoryID")).intValue();

        int entityID = 0;

        if ((hm.get("EntityID") != null))
        {
          entityID = ((Long) hm.get("EntityID")).intValue();
        }

        try
        {
          IntMember intInvID =
            new IntMember("InventoryID", invID, 10, null, 'T', true, 10);
          IntMember intEntityID =
            new IntMember("EntityID", entityID, 10, null, 'T', false, 10);

          StringMember strItemName = null;

          if ((hm.get("ItemName") != null))
          {
            strItemName =
              new StringMember("ItemName", (String) hm.get("ItemName"), 10,
                null, 'T', false);
          }
          else
          {
            strItemName =
              new StringMember("ItemName", null, 10, null, 'T', false);
          }

          StringMember strIdentifier =
            new StringMember("Identifier", (String) hm.get("Identifier"), 10,
              null, 'T', false);

          StringMember strManufacturer =
            new StringMember("Manufacturer", (String) hm.get("Manufacturer"),
              10, null, 'T', false);

          StringMember strVendor =
            new StringMember("Vendor", (String) hm.get("Vendor"), 10, "", 'T',
              true);

          InventoryListElement invListElement = new InventoryListElement(invID);

          invListElement.put("InventoryID", intInvID);
          invListElement.put("ItemName", strItemName);
          invListElement.put("Identifier", strIdentifier);
          invListElement.put("Manufacturer", strManufacturer);
          invListElement.put("Vendor", strVendor);

          invListElement.put("EntityID", intEntityID);

          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);

          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(),
            stringbuffer.length(), s3);

          String s4 = stringbuffer.toString();

          invList.put(s4, invListElement);
        }
        catch (Exception e)
        {
          System.out.println(" Exception in packing data " + e);

          //e.printStackTrace();
        }
      }
    }

    invList.setTotalNoOfRecords(invList.size());
    invList.setListType("Inventory");
    invList.setBeginIndex(beginIndex);

    //invList.setEndIndex(endIndex);
    invList.setEndIndex(invList.size());

    return invList;
  }
   // getInventoryList ends here

  /**
   * Gets the Whole list of Item
   *
   * @param   individualID
   * @param   hashmap
   * @return
   */
  public ItemList getItemList(int individualID, HashMap hashmap) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("OrderHistory", individualID, this.dataSource))
    {
      throw new AuthorizationFailedException("Item- getItemList");
    }

    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int beginIndex = 0;
    Float f = new Float(5);

    ItemList list = new ItemList();
    list.setSortMember(strSortMem);

    CVDal cvdl = new CVDal(dataSource);

    Collection colList;
		String sortType= "";
		if (charSort == 'D')
		{
					sortType = "DESC";
		}
	  String appendSearchCondition ="";
    if ((strSearch != null) && strSearch.startsWith("ADVANCE:"))
    {
			int searchIndex = (strSearch.toUpperCase()).indexOf("WHERE");
			appendSearchCondition = " and "+strSearch.substring((searchIndex+5),strSearch.length());
    }

	String itemQuery = " select item.itemid,item.sku,item.title as Name ,itemtype.title as type ,item.listprice as Price ,count(inventory.qty) as OnHand ,taxclass.taxclassid as taxclassid ,taxclass.title as taxclass,item.cost ,"+
						 " ven.entityid as vendorid,ven.name as vendor,man.entityid as manid, man.name as manufacturer  from  item left outer join itemtype on item.type = itemtype.itemtypeid "+
						 " left outer join inventory on item.itemid = inventory.item left outer join taxclass on item.taxclass = taxclass.taxclassid "+
						 " left outer join entity man on item.manufacturerid = man.entityid   left outer join entity ven on item.vendorid = ven.entityid "+
						 " where item.deletestatus != 'Deleted' "+appendSearchCondition+" Group by item.itemid,item.sku,item.title,itemtype.title,item.listprice, taxclass.title,item.cost order by '"+ strSortMem +"' "+sortType;
	cvdl.setSqlQuery(itemQuery);
    colList = cvdl.executeQuery();
    cvdl.clearParameters();
    cvdl.destroy();

    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();
        int id = ((Long) hm.get("itemid")).intValue();

        try
        {
          IntMember intListID =
            new IntMember("ItemID", id, 10, "", 'T', false, 10);

          StringMember strSku = null;

          if ((hm.get("sku") != null))
          {
            strSku =
              new StringMember("SKU", (String) hm.get("sku"), 10, "", 'T', true);
          }
          else
          {
            strSku = new StringMember("SKU", null, 10, "", 'T', true);
          }

          StringMember strName =
            new StringMember("Name", (String) hm.get("Name"), 10, "", 'T', false);

          //					StringMember strCatagory = new StringMember("Category", (String)hm.get("categoryname"), 10, "", 'T', false);new Float(((Double) hm.get( "cost" )).floatValue())
          StringMember strType =
            new StringMember("Type", (String) hm.get("type"), 10, "", 'T', false);

          FloatMember price = null;

          if (hm.get("Price") != null)
          {
            price =
              new FloatMember("Price",
                new Float(Float.parseFloat((hm.get("Price")).toString())), 10,
                "", 'T', false, 10);
          }
          else
          {
            price =
              new FloatMember("Price", new Float(0), 10, "", 'T', false, 10);
          }

          DoubleMember intQty = null;

          if (hm.get("OnHand") != null)
          {
            intQty =
              new DoubleMember("OnHand",
                new Double(Double.parseDouble((hm.get("OnHand")).toString())),
                10, "", 'T', false, 10);
          }
          else
          {
            intQty =
              new DoubleMember("OnHand", new Double(0), 10, "", 'T', false, 10);
          }

          StringMember strtaxClass =
            new StringMember("TaxClass", (String) hm.get("taxclass"), 10, "",
              'T', false);


          int taxClassId = ((Number) hm.get("taxclassid")).intValue();

          IntMember intTaxClassID = new IntMember("TaxClassID", taxClassId, 10, "", 'T', false, 10);

          FloatMember cost =
            new FloatMember("Cost",
              new Float(Float.parseFloat((hm.get("cost")).toString())), 10, "",
              'T', false, 10);

          //Added by Shilpa for getting Manufacture and Vendor for item
          int manid = 0;
          int venid = 0;

          if (hm.get("manid") != null)
          {
            manid = ((Long) hm.get("manid")).intValue();
          }

          IntMember manufacturerID =
            new IntMember("ManufacturerID", manid, 10, "", 'T', false, 10);

          if (hm.get("vendorid") != null)
          {
            venid = ((Long) hm.get("vendorid")).intValue();
          }

          IntMember vendorID =
            new IntMember("VendorID", venid, 10, "", 'T', false, 10);

          StringMember strVendor = null;
          StringMember strManufacturer = null;

          if (hm.get("vendor") != null)
          {
            strVendor =
              new StringMember("Vendor", (String) hm.get("vendor"), 10, "",
                'T', false);
          }

          if (hm.get("manufacturer") != null)
          {
            strManufacturer =
              new StringMember("Manufacturer", (String) hm.get("manufacturer"),
                10, "", 'T', false);
          }

          //Added by shilpa ends here
          ItemListElement listElement = new ItemListElement(id);

          listElement.put("ItemID", intListID);
          listElement.put("SKU", strSku);
          listElement.put("Name", strName);

          //					listElement .put("Category", strCatagory);
          listElement.put("Type", strType);
          listElement.put("Price", price);
          listElement.put("OnHand", intQty);
          listElement.put("TaxClass", strtaxClass);
          listElement.put("TaxClassID", intTaxClassID);

          listElement.put("Cost", cost);

          //Added By Shilpa
          listElement.put("VendorID", vendorID);
          listElement.put("Vendor", strVendor);
          listElement.put("Manufacturer", strManufacturer);
          listElement.put("ManufacturerID", manufacturerID);

          //Added By Shilpa ends here
          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);

          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(),
            stringbuffer.length(), s3);

          String s4 = stringbuffer.toString();

          list.put(s4, listElement);
        }
        catch (Exception e)
        {
          System.out.println(" Exception in AccountListEJB.getItemList  " + e);

          //e.printStackTrace();
        }
      }
    }

    list.setTotalNoOfRecords(list.size());
    list.setListType("Item");
    list.setBeginIndex(beginIndex);
    list.setEndIndex(list.size());

    return list;
  }
   // ItemList   ends here

  /**
   * Gets the Whole list of Order
   *
   * @param   individualID
   * @param   hashmap
   * @return
   */
  public OrderList getOrderList(int individualID, HashMap hashmap) throws AuthorizationFailedException
  {
  	//Added the condition If we have the privilege to view the entity module then we can generate the list
  	// Coz we must have to display the related Order for and entity
  	
	boolean flagEntity = CVUtility.isModuleVisible("Entity",individualID, this.dataSource);
	boolean flagOrder = CVUtility.isModuleVisible("OrderHistory",individualID, this.dataSource);

	if(!flagEntity && !flagOrder){
		throw new AuthorizationFailedException("We didn't have privilege for viewing either Entity or Order module ");
	}

    String timezoneid = "EST";
    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");
    char charSort = chrSortType.charValue();
    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();
    int beginIndex = 0;

    OrderList list = new OrderList();
    list.setSortMember(strSortMem);
    CVDal cvdl = new CVDal(dataSource);
    Collection colList = null;

    //     order by '"+ sortMember +"' "+sortType

    if (charSort == 'A')
    {
      cvdl.setDynamicQuery("account.getorderlist", "ASC", strSortMem);
    }
    else
    {
      cvdl.setDynamicQuery("account.getorderlist", "DESC", strSortMem);
    }

    String sortType = "ASC";

    if (charSort != 'A')
    {
      sortType = "DESC";
    }

    String selectQuery = "SELECT cvorder.orderid AS OrderNO, "
      + "cvorder.entityid, entity.name AS Entity, cvorder.created AS date, "
      + "cvorder.total, cvorder.accountmgr, "
      + "CONCAT(indv1.firstname,' ',indv1.lastname) AS salesrep, "
      + "accountingstatus.title AS status, cvorder.discount, cvorder.creator, "
      + "CONCAT(indv2.firstname, ' ', indv2.lastname) AS creator, cvorder.ponumber ";

    String fromQuery = "FROM cvorder LEFT OUTER JOIN entity ON "
      + "cvorder.entityid = entity.entityid LEFT OUTER JOIN individual indv1 ON "
      + "cvorder.accountmgr = indv1.individualid LEFT OUTER JOIN individual indv2 ON "
      + "cvorder.creator = indv2.individualid LEFT OUTER JOIN accountingstatus ON "
      + "cvorder.status = accountingstatus.statusid ";

    String whereQuery = "WHERE orderstatus !=  'Deleted' ";
    String orderByQuery = " ORDER BY '" + strSortMem + "' " + sortType;

    String listQuery = selectQuery + fromQuery + whereQuery + orderByQuery;


    if ((strSearch != null) && strSearch.startsWith("ADVANCE:"))
    {
      String advSearchstr = strSearch.substring(8);

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `orderlistsearch`;");
      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.setSqlQueryToNull();

      cvdl.setSqlQuery("CREATE TEMPORARY TABLE orderlistsearch " + advSearchstr);
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.clearParameters();

      fromQuery = fromQuery + " , orderlistsearch ";
      whereQuery = whereQuery + " AND cvorder.orderid = orderlistsearch.orderid ";
    }

    listQuery = selectQuery + fromQuery + whereQuery + orderByQuery;

    cvdl.setSqlQuery(listQuery);
    colList = cvdl.executeQuery();
    cvdl.clearParameters();

    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE IF EXISTS orderlistsearch;");
    cvdl.executeUpdate();
    cvdl.clearParameters();
    cvdl.destroy();

    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();
        int id = ((Long) hm.get("OrderNO")).intValue();

        try
        {
          IntMember intListID =
            new IntMember("OrderID", id, 10, "", 'T', false, 10);

          IntMember intorderNo =
            new IntMember("OrderNO", id, 10, "", 'T', true, 10);

          OrderListElement listElement = new OrderListElement(id);

          StringMember strentityName =
            new StringMember("Entity", (String) hm.get("Entity"), 10, "", 'T',
              true);

          id = ((Long) hm.get("entityid")).intValue();

          IntMember entityID =
            new IntMember("EntityID", id, 10, "", 'T', false, 10);

          DateMember date =
            new DateMember("Date",
              new Date(((Timestamp) hm.get("date")).getTime()), 10, "", 'T',
              false, 1, timezoneid);

          DoubleMember total =
            new DoubleMember("Total",
              new Double(Double.parseDouble(hm.get("total").toString())), 10,
              "", 'T', false, 10);

          id = ((Long) hm.get("accountmgr")).intValue();

          IntMember saleID =
            new IntMember("SalesRepID", id, 10, "", 'T', false, 10);

          StringMember salesrep =
            new StringMember("SalesRep", (String) hm.get("salesrep"), 10, "",
              'T', true);
          StringMember status =
            new StringMember("Status", (String) hm.get("status"), 10, "", 'T',
              false);
          DoubleMember discount =
            new DoubleMember("Discount", (Double) hm.get("Discount"), 10, "",
              'T', false, 10);
          StringMember creator =
            new StringMember("Creator", (String) hm.get("creator"), 10, "",
              'T', false);
          StringMember poNumber =
            new StringMember("PONumber", (String) hm.get("ponumber"), 10, "",
              'T', false);

          listElement.put("OrderID", intListID);
          listElement.put("OrderNO", intorderNo);
          listElement.put("Entity", strentityName);
          listElement.put("EntityID", entityID);
          listElement.put("Date", date);
          listElement.put("Total", total);
          listElement.put("SalesRepID", saleID);
          listElement.put("SalesRep", salesrep);
          listElement.put("Status", status);
          listElement.put("Discount", discount);
          listElement.put("Creator", creator);
          listElement.put("PONumber", poNumber);

          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);

          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(),
            stringbuffer.length(), s3);

          String s4 = stringbuffer.toString();

          list.put(s4, listElement);
        }
        catch (Exception e)
        {
          System.out.println(" Exception in getOrderList" + e);

          //e.printStackTrace();
        }
      }
    }

    list.setTotalNoOfRecords(list.size());
    list.setListType("Order");
    list.setBeginIndex(beginIndex);
    list.setEndIndex(list.size());

    return list;
  }
   // OrderList   ends here

  /**
   * Gets the Whole list of invoice
   *
   * @param   individualID
   * @param   hashmap
   * @return
   */
  public InvoiceList getInvoiceList(int userId, HashMap hashmap) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("InvoiceHistory", userId, this.dataSource))
    {
      throw new AuthorizationFailedException("Invoice- getInvoiceList");
    }

    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");

    long entityID = 0;
    long orderID = 0;

    if (hashmap.get("EntityID") != null)
    {
      entityID = Long.parseLong(hashmap.get("EntityID").toString());
    }

    if (hashmap.get("OrderID") != null)
    {
      orderID = Long.parseLong(hashmap.get("OrderID").toString());
    }

    String sortType = "ASC";

    if (chrSortType.charValue() == 'D')
    {
      sortType = "DESC";
    }

    String selectQuery =
      "select invoice.InvoiceID,invoice.OrderID,invoice.CustomerID as CustID ,cust.name as CustomerID,invoice.Creator as CreatorID,invoice.InvoiceDate,invoice.Total, concat(creator.firstname ,' ',creator.lastname) as Creator, sum(applypayment.Amount) as AmountPaid";
    String fromQuery =
      " from invoice left outer join applypayment on invoice.InvoiceID = applypayment.InvoiceID left outer join entity cust on invoice.CustomerID = cust.entityid left outer join individual creator on invoice.Creator=creator.individualid ";
    String whereQuery = " where  1=1 and invoice.invoicestatus !=  'Deleted' ";
    String groupByQuery =
      " group by invoice.InvoiceID,CustomerID,invoice.InvoiceDate,invoice.Total,firstname  ";
    String orderByQuery = " order by '" + strSortMem + "' " + sortType;

    if ((entityID != 0) && (orderID == 0))
    {
      whereQuery = whereQuery + "AND invoice.CustomerID=" + entityID;
    }

    if (orderID != 0)
    {
      whereQuery = whereQuery + "AND invoice.OrderID=" + orderID;
    }

    String listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int beginIndex = 0;

    CVDal cvdl = new CVDal(dataSource);

    Collection colList = null;

    if ((strSearch != null) && strSearch.startsWith("SIMPLE :"))
    {
      strSearch = strSearch.substring(8);

      whereQuery =
        whereQuery + "AND  invoice.InvoiceID like '%" + strSearch
        + "%'   OR cust.name like '%" + strSearch
        + "%' OR invoice.InvoiceDate like  '%" + strSearch
        + "%' OR invoice.Total like  '%" + strSearch
        + "%'  OR concat(creator.firstname ,' ',creator.lastname) like  '%"
        + strSearch + "%' ";
    }

    String advSearchstr = strSearch;

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      advSearchstr = advSearchstr.substring(8);

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `invoicelistsearch`;");
      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.setSqlQueryToNull();

      strSearch = "CREATE TEMPORARY TABLE invoicelistsearch " + advSearchstr;

      cvdl.setSqlQuery(strSearch);
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.clearParameters();

      fromQuery = fromQuery + " , invoicelistsearch";
      whereQuery =
        whereQuery + " AND invoice.InvoiceID = invoicelistsearch.InvoiceID";
    }

    listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    cvdl.setSqlQuery(listQuery);
    colList = cvdl.executeQuery();
    cvdl.clearParameters();

    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE IF EXISTS invoicelistsearch;");
    cvdl.executeUpdate();
    cvdl.clearParameters();

    cvdl.destroy();

    InvoiceList invList = new InvoiceList();

    invList.setSortMember(strSortMem);

    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();

        int invID = Integer.parseInt(hm.get("InvoiceID").toString());

        Double amount = null;

        if (hm.get("Total") != null)
        {
          amount = new Double(Double.parseDouble(hm.get("Total").toString()));
        }

        Double amountPaid = null;

        if (hm.get("AmountPaid") != null)
        {
          amountPaid =
            new Double(Double.parseDouble(hm.get("AmountPaid").toString()));
        }

        try
        {
          IntMember intInvID =
            new IntMember("InvoiceID", invID, 10, null, 'T', true, 10);

          IntMember intOrderID = null;

          if (hm.get("OrderID") != null)
          {
            int oID = Integer.parseInt(hm.get("OrderID").toString());
            intOrderID = new IntMember("Order", oID, 10, null, 'T', true, 10);
          }

          StringMember strCustID = null;

          if (hm.get("CustomerID") != null)
          {
            strCustID =
              new StringMember("CustomerID", (String) hm.get("CustomerID"), 10,
                null, 'T', true);
          }
          else
          {
            strCustID =
              new StringMember("CustomerID", null, 10, null, 'T', true);
          }

          DateMember date = null;

          if (hm.get("InvoiceDate") != null)
          {
            date =
              new DateMember("InvoiceDate", (Date) hm.get("InvoiceDate"), 10,
                null, 'T', false, 10, "EST");
          }
          else
          {
            date =
              new DateMember("InvoiceDate", null, 10, null, 'T', false, 10,
                "EST");
          }

          DoubleMember dblAmount =
            new DoubleMember("Total", amount, 10, null, 'T', false, 10);

          StringMember strPaid = null;

          double amt = 0;

          if (amount != null)
          {
            amt = amount.doubleValue();
          }

          double amtPaid = 0;

          if (amountPaid != null)
          {
            amtPaid = amountPaid.doubleValue();
          }

          int result = 0;

          if ((amount != null) && (amountPaid != null))
          {
            result = amount.compareTo(amountPaid);
          }

          if (result == 0)
          {
            strPaid = new StringMember("Paid", "Yes", 10, null, 'T', false);
          }
          else if ((result > 0) && (amtPaid > 0))
          {
            strPaid = new StringMember("Paid", "Partial", 10, null, 'T', false);
          }
          else
          {
            strPaid = new StringMember("Paid", "No", 10, null, 'T', false);
          }

          StringMember strCreator = null;

          if (hm.get("Creator") != null)
          {
            strCreator =
              new StringMember("Creator", (String) hm.get("Creator"), 10, null,
                'T', true);
          }
          else
          {
            strCreator = new StringMember("Creator", null, 10, null, 'T', true);
          }

          int creatorID = 0;

          if (hm.get("CreatorID") != null)
          {
            creatorID = ((Long) hm.get("CreatorID")).intValue();
          }

          int custID = 0;

          if (hm.get("CustID") != null)
          {
            custID = ((Long) hm.get("CustID")).intValue();
          }

          InvoiceListElement invListElement = new InvoiceListElement(invID);

          invListElement.put("InvoiceID", intInvID);
          invListElement.put("Order", intOrderID);
          invListElement.put("CustomerID", strCustID);
          invListElement.put("CustID",
            new IntMember("CustID", custID, 10, null, 'T', true, 10));
          invListElement.put("InvoiceDate", date);
          invListElement.put("Total", dblAmount);
          invListElement.put("Paid", strPaid);
          invListElement.put("Creator", strCreator);
          invListElement.put("CreatorID",
            new IntMember("CreatorID", creatorID, 10, null, 'T', true, 10));

          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);

          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(),
            stringbuffer.length(), s3);

          String s4 = stringbuffer.toString();

          invList.put(s4, invListElement);
        }
        catch (Exception e)
        {
          System.out.println(" Exception in packing data " + e);

          //e.printStackTrace();
        }
      }
    }

    invList.setTotalNoOfRecords(invList.size());
    invList.setListType("InvoiceHistory");
    invList.setBeginIndex(beginIndex);

    //invList.setEndIndex(endIndex);
    invList.setEndIndex(invList.size());

    return invList;
  }

  //getInvoiceList ends here

  /**
   * Gets the Whole list of Expense
   *
   * @param   individualID
   * @param   hashmap
   * @return
   */
  public ExpenseList getExpenseList(int individualID, HashMap hashmap) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Expense", individualID, this.dataSource))
    {
      throw new AuthorizationFailedException("Expense- getExpenseList");
    }

    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int beginIndex = 0;

    ExpenseList expList = new ExpenseList();

    expList.setSortMember(strSortMem);

    CVDal cvdl = new CVDal(dataSource);

    Collection colList = null;

    String sortType = "ASC";

    if (charSort == 'D')
    {
      sortType = "DESC";
    }

    String selectQuery =
      "	select expense.ExpenseID,expense.Amount,expense.Created,entity.name as Reference ,expense.Status, concat(individual.firstname,' ',individual.lastname) as Creator,individual.IndividualID";
    String fromQuery =
      " from expense left outer join individual on expense.owner = individual.individualid left outer join entity on expense.entityid = entity.entityid";
    String whereQuery = " where expense.linestatus != 'deleted'";
    String groupByQuery =
      " group by expense.ExpenseID,expense.Amount,expense.Created,Reference ,expense.Status, Creator";
    String orderByQuery = " order by '" + strSortMem + "' " + sortType;

    String listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    if ((strSearch != null) && strSearch.startsWith("SIMPLE :"))
    {
      strSearch = strSearch.substring(8);

      whereQuery =
        whereQuery + "AND  expense.ExpenseID like '%" + strSearch
        + "%'   OR expense.Amount like '%" + strSearch
        + "%' OR expense.Created like  '%" + strSearch
        + "%' OR entity.name like  '%" + strSearch
        + "%' OR expense.Status like '%" + strSearch
        + "%' OR concat(individual.firstname,' ',individual.lastname) like'%"
        + strSearch + "%' ";
    }

    String advSearchstr = strSearch;

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      String tmpAdvSearchstr = advSearchstr.substring(8);

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `expenselistsearch`;");
      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.setSqlQueryToNull();

      strSearch = "create TEMPORARY TABLE expenselistsearch " + tmpAdvSearchstr;

      cvdl.setSqlQuery(strSearch);
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.clearParameters();

      fromQuery = fromQuery + " ,expenselistsearch";
      whereQuery =
        whereQuery + " AND expense.ExpenseID = expenselistsearch.ExpenseID";
    }

    listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    cvdl.setSqlQuery(listQuery);
    colList = cvdl.executeQuery();
    cvdl.clearParameters();

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE expenselistsearch");
      cvdl.executeUpdate();
      cvdl.clearParameters();
    }

    cvdl.destroy();

    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();

        int expenseID = Integer.parseInt(hm.get("ExpenseID").toString());
        int indID = 0;

        if ((hm.get("IndividualID") != null))
        {
          indID = Integer.parseInt(hm.get("IndividualID").toString());
        }

        Double amount = null;

        if (hm.get("Amount") != null)
        {
          amount = new Double(Double.parseDouble(hm.get("Amount").toString()));
        }

        try
        {
          IntMember intExpID =
            new IntMember("ExpenseID", expenseID, 10, null, 'T', true, 10);
          IntMember intIndID =
            new IntMember("IndividualID", indID, 10, null, 'T', false, 10);
          DoubleMember dblAmount =
            new DoubleMember("Amount", amount, 10, "", 'T', false, 10);

          DateMember submitedDt =
            new DateMember("Created", (Date) hm.get("Created"), 10, "", 'T',
              false, 1, "EST");

          StringMember strReference = null;

          if ((hm.get("Reference") != null))
          {
            strReference =
              new StringMember("Reference", (String) hm.get("Reference"), 10,
                null, 'T', false);
          }
          else
          {
            strReference =
              new StringMember("Reference", null, 10, null, 'T', false);
          }

          StringMember strStatus =
            new StringMember("Status", (String) hm.get("Status"), 10, null,
              'T', false);

          StringMember strCreator = null;

          if ((hm.get("Creator") != null))
          {
            strCreator =
              new StringMember("Creator", (String) hm.get("Creator"), 10, null,
                'T', true);
          }
          else
          {
            strCreator =
              new StringMember("Creator", null, 10, null, 'T', false);
          }

          ExpenseListElement expListElement = new ExpenseListElement(expenseID);

          expListElement.put("ExpenseID", intExpID);
          expListElement.put("Amount", dblAmount);
          expListElement.put("Created", submitedDt);
          expListElement.put("Reference", strReference);
          expListElement.put("Status", strStatus);
          expListElement.put("Creator", strCreator);

          expListElement.put("IndividualID", intIndID);

          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);

          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(),
            stringbuffer.length(), s3);

          String s4 = stringbuffer.toString();

          expList.put(s4, expListElement);
        }
        catch (Exception e)
        {
          System.out.println(" Exception in packing data " + e);

          //e.printStackTrace();
        }
      }
    }

    expList.setTotalNoOfRecords(expList.size());
    expList.setListType("Expense");
    expList.setBeginIndex(beginIndex);

    //expList.setEndIndex(endIndex);
    expList.setEndIndex(expList.size());

    return expList;
  }
   // getExpenseList ends here

  /**
   * Gets the Whole list of PurchaseOrder
   *
   * @param   individualID
   * @param   hashmap
   * @return
   */
  public PurchaseOrderList getPurchaseOrderList(int individualID,
    HashMap hashmap) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("PurchaseOrder", individualID,
          this.dataSource))
    {
      throw new AuthorizationFailedException(
        "PurchaseOrder- getPurchaseOrderList");
    }

    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int beginIndex = 0;

    PurchaseOrderList poList = new PurchaseOrderList();

    poList.setSortMember(strSortMem);

    CVDal cvdl = new CVDal(dataSource);

    Collection colList = null;

    String sortType = "ASC";

    if (charSort == 'D')
    {
      sortType = "DESC";
    }



    String selectQuery =
      "	select purchaseorder.PurchaseOrderID,purchaseorder.purchaseorderdate as Created, individual.IndividualID, concat(individual.firstname,' ',individual.lastname) as Creator,entity.EntityID,entity.name as  Entity ,purchaseorder.SubTotal,purchaseorder.Tax, purchaseorder.Total, accstatus.title as Status";
    String fromQuery =
      " from purchaseorder left outer join individual on purchaseorder.creator = individual.individualid left outer join entity on purchaseorder.entity = entity.entityid left outer join accountingstatus accstatus on purchaseorder.status = accstatus.statusid";
    String whereQuery =
      " where 1=1 and purchaseorder.purchaseorderstatus !=  'Deleted' ";
    String groupByQuery =
      " group by purchaseorder.PurchaseOrderID,purchaseorder.Created,Creator,Entity,purchaseorder.SubTotal,purchaseorder.Tax, purchaseorder.Total ,Status";
    String orderByQuery = " order by " + strSortMem + " " + sortType;

    String listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;


    if ((strSearch != null) && strSearch.startsWith("SIMPLE :"))
    {
      strSearch = strSearch.substring(8);

      whereQuery =
        whereQuery + "AND  purchaseorder.PurchaseOrderID like '%" + strSearch
        + "%'   OR purchaseorder.Created like '%" + strSearch
        + "%' OR concat(individual.firstname,' ',individual.lastname) like  '%"
        + strSearch + "%' OR entity.name like  '%" + strSearch
        + "%' OR purchaseorder.SubTotal like '%" + strSearch
        + "%' OR purchaseorder.Tax like '%" + strSearch
        + "%' OR purchaseorder.Total like '%" + strSearch
        + "%' OR accstatus.title like '%" + strSearch + "%' ";
    }

    String advSearchstr = strSearch;

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      advSearchstr = advSearchstr.substring(8);

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `purchaseorderlistsearch`;");
      cvdl.executeUpdate();
      cvdl.clearParameters();

      strSearch =
        "create TEMPORARY TABLE purchaseorderlistsearch " + advSearchstr;

      cvdl.setSqlQuery(strSearch);
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.clearParameters();

      fromQuery = fromQuery + " ,purchaseorderlistsearch";
      whereQuery =
        whereQuery
        + " AND  purchaseorder.PurchaseOrderID = purchaseorderlistsearch.PurchaseOrderID";
    }

    listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;


    cvdl.setSqlQuery(listQuery);
    colList = cvdl.executeQuery();
    cvdl.clearParameters();

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE purchaseorderlistsearch");
      cvdl.executeUpdate();
      cvdl.clearParameters();
    }

    cvdl.destroy();

    //System.out.println("colList " +colList);
    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();

        int poID = Integer.parseInt((hm.get("PurchaseOrderID")).toString());

        int indID = 0;

        if ((hm.get("IndividualID") != null))
        {
          indID = Integer.parseInt((hm.get("IndividualID")).toString());
        }

        int entityID = 0;

        if ((hm.get("EntityID") != null))
        {
          entityID = Integer.parseInt((hm.get("EntityID")).toString());
        }

        Double subTotal = null;

        if (hm.get("SubTotal") != null)
        {
          subTotal =
            new Double(Double.parseDouble((hm.get("SubTotal")).toString()));
        }

        Double tax = null;

        if (hm.get("Tax") != null)
        {
          tax = new Double(Double.parseDouble((hm.get("Tax")).toString()));
        }

        Double total = null;

        if (hm.get("Total") != null)
        {
          total = new Double(Double.parseDouble((hm.get("Total")).toString()));
        }

        try
        {
          IntMember intPOID =
            new IntMember("PurchaseOrderID", poID, 10, null, 'T', true, 10);
          IntMember intIndID =
            new IntMember("IndividualID", indID, 10, null, 'T', true, 10);
          IntMember intEntityID =
            new IntMember("EntityID", entityID, 10, null, 'T', true, 10);

          DoubleMember dblSubTotal =
            new DoubleMember("SubTotal", subTotal, 10, "", 'T', false, 10);
          DoubleMember dblTax =
            new DoubleMember("Tax", tax, 10, "", 'T', false, 10);
          DoubleMember dblTotal =
            new DoubleMember("Total", total, 10, "", 'T', false, 10);

          DateMember date =
            new DateMember("Created", (Date) hm.get("Created"), 10, "", 'T',
              false, 1, "EST");

          StringMember strEntity = null;

          if ((hm.get("Entity") != null))
          {
            strEntity =
              new StringMember("Entity", (String) hm.get("Entity"), 10, null,
                'T', true);
          }
          else
          {
            strEntity = new StringMember("Entity", null, 10, null, 'T', false);
          }

          StringMember strStatus =
            new StringMember("Status", (String) hm.get("Status"), 10, null,
              'T', false);

          StringMember strCreator = null;

          if ((hm.get("Creator") != null))
          {
            strCreator =
              new StringMember("Creator", (String) hm.get("Creator"), 10, null,
                'T', true);
          }
          else
          {
            strCreator =
              new StringMember("Creator", null, 10, null, 'T', false);
          }

          PurchaseOrderListElement poListElement =
            new PurchaseOrderListElement(poID);

          poListElement.put("PurchaseOrderID", intPOID);
          poListElement.put("SubTotal", dblSubTotal);
          poListElement.put("Tax", dblTax);
          poListElement.put("Total", dblTotal);
          poListElement.put("Created", date);
          poListElement.put("Entity", strEntity);
          poListElement.put("Status", strStatus);
          poListElement.put("Creator", strCreator);

          poListElement.put("IndividualID", intIndID);
          poListElement.put("EntityID", intEntityID);

          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);

          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(),
            stringbuffer.length(), s3);

          String s4 = stringbuffer.toString();

          poList.put(s4, poListElement);
        }
        catch (Exception e)
        {
          System.out.println(" Exception in packing data " + e);

          //e.printStackTrace();
        }
      }
    }

    poList.setTotalNoOfRecords(poList.size());
    poList.setListType("PurchaseOrder");
    poList.setBeginIndex(beginIndex);

    //poList.setEndIndex(endIndex);
    poList.setEndIndex(poList.size());

    return poList;
  }
   // getPurchaseOrderList ends here

  /**
   * Gets the Whole list of Payment
   *
   * @param   individualID
   * @param   hashmap
   * @return
   */
  public PaymentList getPaymentList(int individualID, HashMap hashmap) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Payment", individualID, this.dataSource))
    {
      throw new AuthorizationFailedException("Payment- getPaymentList");
    }

    Integer intStart = (Integer) hashmap.get("startATparam");
    Integer intEnd = (Integer) hashmap.get("EndAtparam");
    String strSearch = (String) hashmap.get("searchString");
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");

    char charSort = chrSortType.charValue();

    int intStartParam = intStart.intValue();
    int intEndParam = intEnd.intValue();

    int invoiceID = 0;
    int beginIndex = 0;

    if (hashmap.get("InvoiceID") != null)
    {
      invoiceID = Integer.parseInt(hashmap.get("InvoiceID").toString());
    }

    PaymentList paymentList = new PaymentList();

    paymentList.setSortMember(strSortMem);

    CVDal cvdl = new CVDal(dataSource);

    //Create Temp table to store AmountApplied and amount unapplied
    cvdl.setSqlQuery(
      "create TEMPORARY table temppayment  select PaymentID ,sum(amount) as AppliedAmount from applypayment  group by PaymentID ");
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.clearParameters();

    Collection colList = null;

    String sortType = "ASC";

    if (charSort == 'D')
    {
      sortType = "DESC";
    }

    String selectQuery =
      "select payment.PaymentID, entity.EntityID,entity.name as Entity,payment.amount  as AmountPaid,temppayment.AppliedAmount , ( payment.amount  - temppayment.AppliedAmount ) as UnAppliedAmount,payment.created as PaymentDate,pm.title as PaymentMethod,payment.Reference, individual.IndividualID ,concat(individual.firstName , '  ',individual.lastname) as EnteredBy ";
    String fromQuery =
      " from payment left outer join paymentmethod  pm on payment.paymentmethod = pm.methodid left outer join individual on individual.individualid = payment.creator left outer join entity on entity.entityid = payment.entityid left outer join temppayment on temppayment.PaymentID = payment.PaymentID";
    String whereQuery = " where payment.linestatus != 'deleted'";
    String groupByQuery =
      " group by PaymentID, EntityID,Entity, AmountPaid ,AppliedAmount,UnAppliedAmount,PaymentDate, PaymentMethod,Reference, EnteredBy";
    String orderByQuery = " order by '" + strSortMem + "' " + sortType;

    if (invoiceID > 0)
    {
      fromQuery =
        fromQuery
        + " inner join applypayment on payment.paymentid = applypayment.paymentid";
      whereQuery = whereQuery + " AND applypayment.invoiceid = " + invoiceID;
    }

    String listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    if ((strSearch != null) && strSearch.startsWith("SIMPLE :"))
    {
      strSearch = strSearch.substring(8);

      whereQuery =
        whereQuery + "AND  payment.PaymentID like '%" + strSearch
        + "%'   OR entity.name like '%" + strSearch
        + "%' OR payment.amount like  '%" + strSearch
        + "%' OR payment.Reference like  '%" + strSearch
        + "%' OR payment.created like '%" + strSearch
        + "%' OR pm.title like '%" + strSearch
        + "%' OR concat(individual.firstName , '  ',individual.lastname) like '%"
        + strSearch + "%' OR temppayment.AppliedAmount like '%" + strSearch
        + "%' OR ( payment.amount  - temppayment.AppliedAmount ) like '%"
        + strSearch + "%'";
    }

    String advSearchstr = strSearch;

    if ((advSearchstr != null) && advSearchstr.startsWith("ADVANCE:"))
    {
      advSearchstr = advSearchstr.substring(8);

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE IF EXISTS `paymentlistsearch`;");
      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.setSqlQueryToNull();

      strSearch = "create TEMPORARY TABLE paymentlistsearch " + advSearchstr;

      cvdl.setSqlQuery(strSearch);
      cvdl.executeUpdate();

      cvdl.setSqlQueryToNull();
      cvdl.clearParameters();

      fromQuery = fromQuery + " ,paymentlistsearch";
      whereQuery =
        whereQuery + " AND payment.PaymentID = paymentlistsearch.PaymentID";
    }

    listQuery =
      selectQuery + fromQuery + whereQuery + groupByQuery + orderByQuery;

    cvdl.setSqlQuery(listQuery);
    colList = cvdl.executeQuery();
    cvdl.clearParameters();

    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE IF EXISTS paymentlistsearch");
    cvdl.executeUpdate();
    cvdl.clearParameters();


    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE temppayment");
    cvdl.executeUpdate();
    cvdl.clearParameters();

    cvdl.destroy();

    //System.out.println("colList " +colList);
    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();

        int paymentID = Integer.parseInt(hm.get("PaymentID").toString());

        int indID = 0;

        if ((hm.get("IndividualID") != null))
        {
          indID = Integer.parseInt(hm.get("IndividualID").toString());
        }

        int entityID = 0;

        if ((hm.get("EntityID") != null))
        {
          entityID = Integer.parseInt(hm.get("EntityID").toString());
        }

        Double amount = null;

        if (hm.get("AmountPaid") != null)
        {
          amount =
            new Double(Double.parseDouble(hm.get("AmountPaid").toString()));
        }

        Double appliedAmt = null;

        if (hm.get("AppliedAmount") != null)
        {
          appliedAmt =
            new Double(Double.parseDouble(hm.get("AppliedAmount").toString()));
        }

        Double unAppliedAmt = null;

        if (hm.get("UnAppliedAmount") != null)
        {
          unAppliedAmt =
            new Double(Double.parseDouble(hm.get("UnAppliedAmount").toString()));
        }

        try
        {
          IntMember intPayID =
            new IntMember("PaymentID", paymentID, 10, null, 'T', true, 10);
          IntMember intIndID =
            new IntMember("IndividualID", indID, 10, null, 'T', false, 10);
          IntMember intEntID =
            new IntMember("EntityID", entityID, 10, null, 'T', false, 10);

          DoubleMember dblAmount =
            new DoubleMember("AmountPaid", amount, 10, "", 'T', false, 10);
          DoubleMember dblAppAmt =
            new DoubleMember("AppliedAmount", appliedAmt, 10, "", 'T', false, 10);
          DoubleMember dblUnAppAmt =
            new DoubleMember("UnAppliedAmount", unAppliedAmt, 10, "", 'T',
              false, 10);

          DateMember paymentDt =
            new DateMember("PaymentDate", (Date) hm.get("PaymentDate"), 10, "",
              'T', false, 1, "EST");

          StringMember strReference = null;

          if ((hm.get("Reference") != null))
          {
            strReference =
              new StringMember("Reference", (String) hm.get("Reference"), 10,
                null, 'T', false);
          }
          else
          {
            strReference =
              new StringMember("Reference", null, 10, null, 'T', false);
          }

          StringMember strMethod =
            new StringMember("PaymentMethod", (String) hm.get("PaymentMethod"),
              10, null, 'T', false);

          StringMember strEnteredBy = null;

          if ((hm.get("EnteredBy") != null))
          {
            strEnteredBy =
              new StringMember("EnteredBy", (String) hm.get("EnteredBy"), 10,
                null, 'T', true);
          }
          else
          {
            strEnteredBy =
              new StringMember("EnteredBy", null, 10, null, 'T', false);
          }

          StringMember strEntity = null;

          if ((hm.get("Entity") != null))
          {
            strEntity =
              new StringMember("Entity", (String) hm.get("Entity"), 10, null,
                'T', true);
          }
          else
          {
            strEntity = new StringMember("Entity", null, 10, null, 'T', false);
          }

          PaymentListElement payListElement = new PaymentListElement(paymentID);

          payListElement.put("PaymentID", intPayID);
          payListElement.put("AmountPaid", dblAmount);
          payListElement.put("AppliedAmount", dblAppAmt);
          payListElement.put("UnAppliedAmount", dblUnAppAmt);
          payListElement.put("PaymentDate", paymentDt);
          payListElement.put("Reference", strReference);
          payListElement.put("PaymentMethod", strMethod);
          payListElement.put("EnteredBy", strEnteredBy);
          payListElement.put("Entity", strEntity);

          payListElement.put("IndividualID", intIndID);
          payListElement.put("EntityID", intEntID);

          StringBuffer stringbuffer = new StringBuffer("00000000000");
          stringbuffer.setLength(11);

          String s3 = (new Integer(i)).toString();
          stringbuffer.replace(stringbuffer.length() - s3.length(),
            stringbuffer.length(), s3);

          String s4 = stringbuffer.toString();

          paymentList.put(s4, payListElement);
        }
        catch (Exception e)
        {
          System.out.println(" Exception in packing data of Payment" + e);

          //e.printStackTrace();
        }
      }
    }

    paymentList.setTotalNoOfRecords(paymentList.size());
    paymentList.setListType("Payment");
    paymentList.setBeginIndex(beginIndex);

    //paymentList.setEndIndex(endIndex);
    paymentList.setEndIndex(paymentList.size());

    return paymentList;
  }
   // getPaymentList ends here

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
  * Gets the Whole list of Item
  *
  * @param   individualID
  * @param   hashmap
  * @return
  */
  public ArrayList getLocationList(int individualID, HashMap hashmap) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("OrderHistory", individualID, this.dataSource))
    {
      throw new AuthorizationFailedException("Location- getLocationList");
    }

    /*         Integer intStart = (Integer)hashmap.get("startATparam");
     Integer intEnd = (Integer)hashmap.get("EndAtparam");
     String strSearch = (String)hashmap.get("searchString");*/
    String strSortMem = (String) hashmap.get("sortmem");
    Character chrSortType = (Character) hashmap.get("sortType");

    char charSort = chrSortType.charValue();

    /*  int intStartParam = intStart.intValue();
      int intEndParam = intEnd.intValue();
    */
    int beginIndex = 0;
    ArrayList list = new ArrayList();

    /*LocationList list= new LocationList();
    list.setSortMember(strSortMem);*/
    CVDal cvdl = new CVDal(dataSource);

    Collection colList;

    if (charSort == 'A')
    {
      cvdl.setDynamicQuery("account.getlocationlist", "ASC", strSortMem);
    }
    else
    {
      cvdl.setDynamicQuery("account.getlocationlist", "DESC", strSortMem);
    }

    //select item.itemid,item.sku,item.title,itemcategory.categoryname,itemtype.title as type ,item.listprice,sum(inventory.qty) as qty ,taxclass.title as taxclass,item.cost from item,itemcategory,itemtype,inventory,taxclass where  item.itemcategory =itemcategory.categoryid and item.type = itemtype.itemtypeid and item.itemid = inventory.item and item.taxclass = taxclass.taxclassid Group by item.itemid,item.sku,item.title,itemcategory.categoryname,itemtype.title,item.listprice, taxclass.title,item.cost
    colList = cvdl.executeQuery();
    cvdl.clearParameters();
    cvdl.destroy();

    if (colList != null)
    {
      Iterator it = colList.iterator();

      int i = 0;

      while (it.hasNext())
      {
        i++;

        HashMap hm = (HashMap) it.next();
        int id = Integer.parseInt((hm.get("locationid")).toString());

        try
        {
          IntMember intListID =
            new IntMember("LocationID", id, 10, "", 'T', false, 10);

          StringMember strName =
            new StringMember("Name", (String) hm.get("title"), 10, "", 'T',
              false);

          IntMember parent =
            new IntMember("LocationParent",
              Integer.parseInt((hm.get("locationid")).toString()), 10, "", 'T',
              false, 10);

          LocationListElement listElement = new LocationListElement(id);

          listElement.put("LocationID", intListID);
          listElement.put("Name", strName);
          listElement.put("LocationParent", parent);

          list.add(listElement);
        }
        catch (Exception e)
        {
          System.out.println(
            "[Exception][AccountListEJB.getLocationList] Exception Thrown: "
            + e);

          //e.printStackTrace();
        }
      }
    }

    return list;
  } // ItemList   ends here

  /**
   * Returns a ValueListVO representing a list of Invoice records, based on
   * the <code>parameters</code> argument which limits results.
   */
  public ValueListVO getInvoiceValueList(int individualId, ValueListParameters parameters)
  {
    // How all the getValueLists should work:
    // 1. Query should be mostly canned, maybe to a temp table.
    // 2. The sort and limit options of the final query should be built using
    //    data from the parameters object.
    // 3. The columns from each row of the query will be stuffed into an arraylist
    //    Which will, each, populate the list being returned.
    ArrayList list = new ArrayList();
    // permissionSwitch turns the permission parts of the query on and off.
    // if individualId is less than zero then the list is requested without limiting
    // rows based on record rights.  If it is true than the rights are used.
    boolean permissionSwitch = individualId < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0)
    {
      String str = "CREATE TABLE invoicelistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter)
    {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "invoicelistfilter", individualId, 56, "invoice", "InvoiceId", "owner", null, permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 56, "invoice", "InvoiceId", "owner", null, permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildInvoiceListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("SELECT * FROM invoicelist");
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE invoicelist");
    cvdl.executeUpdate();
    // throw away the temp filter table, if necessary.
    if (applyFilter)
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE invoicelistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch)
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    return new ValueListVO(list, parameters);
  }   // end getInvoiceValueList() method

  private String buildInvoiceListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
  {

  	String select = " SELECT invoice.InvoiceID, invoice.OrderID, invoice.CustomerID, " +
      				     " cust.name as Customer, invoice.Creator as CreatorID, " +
      				     " concat(creator.firstname ,' ',creator.lastname) as Creator," +
      				     " invoice.InvoiceDate, invoice.Total, sum(applypayment.Amount) as Paid ";
    String joinConditions = " LEFT OUTER JOIN applypayment on ( invoice.InvoiceID = applypayment.InvoiceID ) " +
    		  		   " LEFT OUTER JOIN entity AS cust on ( invoice.CustomerID = cust.entityid ) " +
    		  		   " LEFT OUTER JOIN individual AS creator on ( invoice.Creator=creator.individualid ) ";
    
    String groupBy = " GROUP BY 1 ";
    StringBuffer from = new StringBuffer("FROM invoice ");
    StringBuffer where = new StringBuffer("WHERE invoice.invoicestatus !=  'Deleted' ");
    String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary invoice list filter table.
    if (applyFilter || permissionSwitch)
    {
      from.append(", listfilter AS lf ");
      where.append("AND invoice.InvoiceID = lf.InvoiceID");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE invoicelist ");
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(groupBy);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }   // end buildInvoiceListQuery() method

  /**
  * Returns a ValueListVO representing a list of Order records, based on
  * the <code>parameters</code> argument which limits results.
  */
  public ValueListVO getOrderValueList(int individualId, ValueListParameters parameters)
  {
    // How all the getValueLists should work:
    // 1. Query should be mostly canned, maybe to a temp table.
    // 2. The sort and limit options of the final query should be built using
    //    data from the parameters object.
    // 3. The columns from each row of the query will be stuffed into an arraylist
    //    Which will, each, populate the list being returned.
    ArrayList list = new ArrayList();
    // permissionSwitch turns the permission parts of the query on and off.
    // if individualId is less than zero then the list is requested without limiting
    // rows based on record rights.  If it is true than the rights are used.
    boolean permissionSwitch = individualId < 1 ? false : true;
    boolean applyFilter = false;
    String filter = parameters.getFilter();
    CVDal cvdl = new CVDal(this.dataSource);
    if (filter != null && filter.length() > 0)
    {
      String str = "CREATE TABLE orderlistfilter " + filter;
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      applyFilter = true;
    }
    int numberOfRecords = 0;
    if (applyFilter)
    {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "orderlistfilter", individualId, 42, "cvorder", "orderid", "owner", null, permissionSwitch);
    } else if (permissionSwitch) {
      numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 42, "cvorder", "orderid", "owner", null, permissionSwitch);
    }
    parameters.setTotalRecords(numberOfRecords);
    String query = this.buildOrderListQuery(applyFilter, permissionSwitch, parameters);
    cvdl.setSqlQuery(query);
    cvdl.executeUpdate();
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("SELECT * FROM orderlist");
    list = cvdl.executeQueryList(1);
    cvdl.setSqlQueryToNull();
    cvdl.setSqlQuery("DROP TABLE orderlist");
    cvdl.executeUpdate();
    // throw away the temp filter table, if necessary.
    if (applyFilter)
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE orderlistfilter");
      cvdl.executeUpdate();
    }
    if (applyFilter || permissionSwitch)
    {
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE listfilter");
      cvdl.executeUpdate();
    }
    cvdl.destroy();
    cvdl = null;
    
    return new ValueListVO(list, parameters);
  }   // end getOrderValueList() method

  private String buildOrderListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
  {
    String select = "SELECT cvorder.orderid AS OrderNO, cvorder.entityid, entity.name AS Entity, " +
    		" cvorder.created AS Date, cvorder.total, cvorder.accountmgr, " +
    		" CONCAT(indv1.firstname,' ',indv1.lastname) AS SalesRep, " + 
			" accountingstatus.title AS Status, cvorder.discount, cvorder.creator AS creatorID, " +
			" CONCAT(indv2.firstname, ' ', indv2.lastname) AS creator, cvorder.ponumber ";
    
    String joinConditions = " LEFT OUTER JOIN entity ON ( cvorder.entityid = entity.entityid ) " +
        		" LEFT OUTER JOIN individual indv1 ON ( cvorder.accountmgr = indv1.individualid )" +
        		" LEFT OUTER JOIN individual indv2 ON ( cvorder.creator = indv2.individualid )" +
        		" LEFT OUTER JOIN accountingstatus ON ( cvorder.status = accountingstatus.statusid ) ";

    StringBuffer from = new StringBuffer(" FROM cvorder ");
    StringBuffer where = new StringBuffer("WHERE orderstatus !=  'Deleted' ");
    String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
    String limit = parameters.getLimitParam();

    // If a filter is applied we need to do an additional join against the
    // temporary order list filter table.
    if (applyFilter || permissionSwitch)
    {
      from.append(", listfilter AS lf ");
      where.append("AND cvorder.orderid = lf.orderid");
    }
    // Build up the actual query using all the different permissions.
    // Where owner = passed individualId
    StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE orderlist ");
    query.append(select);
    query.append(from);
    query.append(joinConditions);
    query.append(where);
    query.append(orderBy);
    query.append(limit);
    return query.toString();
  }   // end buildOrderListQuery() method

  /**
   * Returns a ValueListVO representing a list of Payment records, based on
   * the <code>parameters</code> argument which limits results.
   */
   public ValueListVO getPaymentValueList(int individualId, ValueListParameters parameters)
   {
     // How all the getValueLists should work:
     // 1. Query should be mostly canned, maybe to a temp table.
     // 2. The sort and limit options of the final query should be built using
     //    data from the parameters object.
     // 3. The columns from each row of the query will be stuffed into an arraylist
     //    Which will, each, populate the list being returned.
     ArrayList list = new ArrayList();
     // permissionSwitch turns the permission parts of the query on and off.
     // if individualId is less than zero then the list is requested without limiting
     // rows based on record rights.  If it is true than the rights are used.
     boolean permissionSwitch = individualId < 1 ? false : true;
     boolean applyFilter = false;
     String filter = parameters.getFilter();
     CVDal cvdl = new CVDal(this.dataSource);
     if (filter != null && filter.length() > 0)
     {
       String str = "CREATE TABLE paymentlistfilter " + filter;
       cvdl.setSqlQuery(str);
       cvdl.executeUpdate();
       cvdl.setSqlQueryToNull();
       applyFilter = true;
     }

     //Create Temp table to store AmountApplied and amount unapplied
     String temporaryPaymentTable = "CREATE TEMPORARY TABLE temppayment " +
		 		" SELECT PaymentID , sum(amount) as AppliedAmount FROM applypayment GROUP BY PaymentID ";     
     cvdl.setSqlQuery(temporaryPaymentTable);
     cvdl.executeUpdate();
     cvdl.setSqlQueryToNull();
     cvdl.clearParameters();
     
     int numberOfRecords = 0;
     if (applyFilter)
     {
       numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "paymentlistfilter", individualId, 43, "payment", "PaymentID", "owner", null, permissionSwitch);
     } else if (permissionSwitch) {
       numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 43, "payment", "PaymentID", "owner", null, permissionSwitch);
     }
     parameters.setTotalRecords(numberOfRecords);
     String query = this.buildPaymentListQuery(applyFilter, permissionSwitch, parameters);
     cvdl.setSqlQuery(query);
     cvdl.executeUpdate();
     cvdl.setSqlQueryToNull();
     cvdl.setSqlQuery("SELECT * FROM paymentlist");
     list = cvdl.executeQueryList(1);
     cvdl.setSqlQueryToNull();
     cvdl.setSqlQuery("DROP TABLE paymentlist");
     cvdl.executeUpdate();

     cvdl.setSqlQuery("DROP TABLE temppayment");
     cvdl.executeUpdate();
     
     // throw away the temp filter table, if necessary.
     if (applyFilter)
     {
       cvdl.setSqlQueryToNull();
       cvdl.setSqlQuery("DROP TABLE paymentlistfilter");
       cvdl.executeUpdate();
     }
     if (applyFilter || permissionSwitch)
     {
       cvdl.setSqlQueryToNull();
       cvdl.setSqlQuery("DROP TABLE listfilter");
       cvdl.executeUpdate();
     }
     cvdl.destroy();
     cvdl = null;
     
     return new ValueListVO(list, parameters);
   }   // end getPaymentValueList() method

   private String buildPaymentListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
   {
      String select = " SELECT payment.PaymentID, entity.EntityID, entity.name as Entity," +
    		             " payment.amount as AmountPaid, temppayment.AppliedAmount," +
    		             " ( payment.amount  - temppayment.AppliedAmount ) as UnAppliedAmount, " +
    		             " payment.created as PaymentDate, pm.title as PaymentMethod,payment.Reference, " +
    		             " individual.IndividualID ,concat(individual.firstName , '  ',individual.lastname) " +
    		             " as CreatedBy ";
    
      String joinConditions = " LEFT OUTER JOIN paymentmethod  pm on payment.paymentmethod = pm.methodid " +
      					 " LEFT OUTER JOIN individual on individual.individualid = payment.owner " +
      					 " LEFT OUTER JOIN entity on entity.entityid = payment.entityid " +
      					 " LEFT OUTER JOIN temppayment on temppayment.PaymentID = payment.PaymentID ";
    
   
     StringBuffer from = new StringBuffer(" FROM payment ");
     StringBuffer where = new StringBuffer(" WHERE payment.linestatus != 'deleted' ");
     String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
     String limit = parameters.getLimitParam();

     // If a filter is applied we need to do an additional join against the
     // temporary order list filter table.
     if (applyFilter || permissionSwitch)
     {
       from.append(", listfilter AS lf ");
       where.append("AND payment.PaymentID = lf.PaymentID");
     }
     // Build up the actual query using all the different permissions.
     // Where owner = passed individualId
     StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE paymentlist ");
     query.append(select);
     query.append(from);
     query.append(joinConditions);
     query.append(where);
     query.append(orderBy);
     query.append(limit);
     return query.toString();
   }   // end buildExpenseListQuery() method
   


   /**
    * Returns a ValueListVO representing a list of Expense records, based on
    * the <code>parameters</code> argument which limits results.
    */
    public ValueListVO getExpenseValueList(int individualId, ValueListParameters parameters)
    {
      // How all the getValueLists should work:
      // 1. Query should be mostly canned, maybe to a temp table.
      // 2. The sort and limit options of the final query should be built using
      //    data from the parameters object.
      // 3. The columns from each row of the query will be stuffed into an arraylist
      //    Which will, each, populate the list being returned.
      ArrayList list = new ArrayList();
      // permissionSwitch turns the permission parts of the query on and off.
      // if individualId is less than zero then the list is requested without limiting
      // rows based on record rights.  If it is true than the rights are used.
      boolean permissionSwitch = individualId < 1 ? false : true;
      boolean applyFilter = false;
      String filter = parameters.getFilter();
      CVDal cvdl = new CVDal(this.dataSource);
      if (filter != null && filter.length() > 0)
      {
        String str = "CREATE TABLE expenselistfilter " + filter;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        applyFilter = true;
      }
      int numberOfRecords = 0;
      if (applyFilter)
      {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "expenselistfilter", individualId, 44, "expense", "ExpenseID", "owner", null, permissionSwitch);
      } else if (permissionSwitch) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 44, "expense", "ExpenseID", "owner", null, permissionSwitch);
      }
      parameters.setTotalRecords(numberOfRecords);
      String query = this.buildExpenseListQuery(applyFilter, permissionSwitch, parameters);
      cvdl.setSqlQuery(query);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("SELECT * FROM expenselist");
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery("DROP TABLE expenselist");
      cvdl.executeUpdate();
      // throw away the temp filter table, if necessary.
      if (applyFilter)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE expenselistfilter");
        cvdl.executeUpdate();
      }
      if (applyFilter || permissionSwitch)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE listfilter");
        cvdl.executeUpdate();
      }
      cvdl.destroy();
      cvdl = null;
      
      return new ValueListVO(list, parameters);
    }   // end getPaymentValueList() method

    private String buildExpenseListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
    {
       String select =  " SELECT expense.ExpenseID, expense.Amount, expense.owner, " +
       					" concat(individual.firstname,' ',individual.lastname) as Creator, " +
       					" expense.entityid, entity.name as EntityName , expense.Status, expense.Created "; 
     
       String joinConditions = " LEFT OUTER JOIN individual on expense.owner = individual.individualid " +
       					 " LEFT OUTER JOIN entity on entity.entityid = expense.entityid ";
     
    
      StringBuffer from = new StringBuffer(" FROM expense ");
      StringBuffer where = new StringBuffer(" WHERE expense.linestatus != 'deleted' ");
      String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
      String limit = parameters.getLimitParam();

      // If a filter is applied we need to do an additional join against the
      // temporary order list filter table.
      if (applyFilter || permissionSwitch)
      {
        from.append(", listfilter AS lf ");
        where.append("AND expense.ExpenseID = lf.ExpenseID");
      }
      // Build up the actual query using all the different permissions.
      // Where owner = passed individualId
      StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE expenselist ");
      query.append(select);
      query.append(from);
      query.append(joinConditions);
      query.append(where);
      query.append(orderBy);
      query.append(limit);
      return query.toString();
    }   // end buildExpenseListQuery() method

    /**
     * Returns a ValueListVO representing a list of Purchase Order records, based on
     * the <code>parameters</code> argument which limits results.
     */
     public ValueListVO getPurchaseOrderValueList(int individualId, ValueListParameters parameters)
     {
       // How all the getValueLists should work:
       // 1. Query should be mostly canned, maybe to a temp table.
       // 2. The sort and limit options of the final query should be built using
       //    data from the parameters object.
       // 3. The columns from each row of the query will be stuffed into an arraylist
       //    Which will, each, populate the list being returned.
       ArrayList list = new ArrayList();
       // permissionSwitch turns the permission parts of the query on and off.
       // if individualId is less than zero then the list is requested without limiting
       // rows based on record rights.  If it is true than the rights are used.
       boolean permissionSwitch = individualId < 1 ? false : true;
       boolean applyFilter = false;
       String filter = parameters.getFilter();
       CVDal cvdl = new CVDal(this.dataSource);
       if (filter != null && filter.length() > 0)
       {
         String str = "CREATE TABLE purchaseorderlistfilter " + filter;
         cvdl.setSqlQuery(str);
         cvdl.executeUpdate();
         cvdl.setSqlQueryToNull();
         applyFilter = true;
       }
       int numberOfRecords = 0;
       if (applyFilter)
       {
         numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "purchaseorderlistfilter", individualId, 44, "purchaseorder", "PurchaseOrderID", "owner", null, permissionSwitch);
       } else if (permissionSwitch) {
         numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 44, "purchaseorder", "PurchaseOrderID", "owner", null, permissionSwitch);
       }
       parameters.setTotalRecords(numberOfRecords);
       String query = this.buildPurchaseOrderListQuery(applyFilter, permissionSwitch, parameters);
       cvdl.setSqlQuery(query);
       cvdl.executeUpdate();
       cvdl.setSqlQueryToNull();
       cvdl.setSqlQuery("SELECT * FROM purchaseorderlist");
       list = cvdl.executeQueryList(1);
       cvdl.setSqlQueryToNull();
       cvdl.setSqlQuery("DROP TABLE purchaseorderlist");
       cvdl.executeUpdate();
       // throw away the temp filter table, if necessary.
       if (applyFilter)
       {
         cvdl.setSqlQueryToNull();
         cvdl.setSqlQuery("DROP TABLE purchaseorderlistfilter");
         cvdl.executeUpdate();
       }
       if (applyFilter || permissionSwitch)
       {
         cvdl.setSqlQueryToNull();
         cvdl.setSqlQuery("DROP TABLE listfilter");
         cvdl.executeUpdate();
       }
       cvdl.destroy();
       cvdl = null;
       
       return new ValueListVO(list, parameters);
     }   // end getPurchaseOrderValueList() method

     private String buildPurchaseOrderListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
     {
		  
        String select =  " SELECT purchaseorder.PurchaseOrderID, purchaseorder.purchaseorderdate as Created," +
        				 " individual.IndividualID, concat(individual.firstname,' ',individual.lastname) as Creator," +
        				 " entity.EntityID, entity.name as  Entity, purchaseorder.SubTotal, purchaseorder.Tax," +
        				 " purchaseorder.Total, accstatus.title as Status "; 
      
        String joinConditions = " LEFT OUTER JOIN individual ON purchaseorder.creator = individual.individualid " +
        					    " LEFT OUTER JOIN entity ON purchaseorder.entity = entity.entityid " +
        					    " LEFT OUTER JOIN accountingstatus accstatus ON purchaseorder.status = accstatus.statusid";
      
     
       StringBuffer from = new StringBuffer(" FROM purchaseorder ");
       StringBuffer where = new StringBuffer(" WHERE purchaseorder.purchaseorderstatus !=  'Deleted' ");
       String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
       String limit = parameters.getLimitParam();

       // If a filter is applied we need to do an additional join against the
       // temporary order list filter table.
       if (applyFilter || permissionSwitch)
     {
       from.append(", listfilter AS lf ");
       where.append("AND  purchaseorder.PurchaseOrderID = lf.PurchaseOrderID");
     }
     // Build up the actual query using all the different permissions.
     // Where owner = passed individualId
     StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE purchaseorderlist ");
     query.append(select);
     query.append(from);
     query.append(joinConditions);
     query.append(where);
     query.append(orderBy);
     query.append(limit);
     return query.toString();
   } // end buildPurchaseOrderListQuery() method    
  
   /**
    * Returns a ValueListVO representing a list of Item records, based on
    * the <code>parameters</code> argument which limits results.
    */
    public ValueListVO getItemValueList(int individualId, ValueListParameters parameters)
    {
      // How all the getValueLists should work:
      // 1. Query should be mostly canned, maybe to a temp table.
      // 2. The sort and limit options of the final query should be built using
      //    data from the parameters object.
      // 3. The columns from each row of the query will be stuffed into an arraylist
      //    Which will, each, populate the list being returned.
      ArrayList list = new ArrayList();
      // permissionSwitch turns the permission parts of the query on and off.
      // if individualId is less than zero then the list is requested without limiting
      // rows based on record rights.  If it is true than the rights are used.
      boolean permissionSwitch = individualId < 1 ? false : true;
      boolean applyFilter = false;
      String filter = parameters.getFilter();
      CVDal cvdl = new CVDal(this.dataSource);
      if (filter != null && filter.length() > 0)
      {
        String str = "CREATE TABLE itemlistfilter " + filter;
        cvdl.setSqlQuery(str);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        applyFilter = true;
      }

      int numberOfRecords = 0;
      if (applyFilter)
      {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "itemlistfilter", individualId, 46, "item", "ItemID", "createdby", null, permissionSwitch);
      } else if (permissionSwitch) {
        numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 46, "item", "ItemID", "createdby", null, permissionSwitch);
      }
      parameters.setTotalRecords(numberOfRecords);
      String query = this.buildItemListQuery(applyFilter, permissionSwitch, parameters);
      cvdl.setSqlQuery(query);
      cvdl.executeUpdate();
      cvdl.setSqlQueryToNull();

      cvdl.setSqlQuery("SELECT * FROM itemlist");
      list = cvdl.executeQueryList(1);
      cvdl.setSqlQueryToNull();
      
      cvdl.setSqlQuery("DROP TABLE itemlist");
      cvdl.executeUpdate();
      // throw away the temp filter table, if necessary.
      if (applyFilter)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE itemlistfilter");
        cvdl.executeUpdate();
      }
      if (applyFilter || permissionSwitch)
      {
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE listfilter");
        cvdl.executeUpdate();
      }
      cvdl.destroy();
      cvdl = null;
      
      return new ValueListVO(list, parameters);
    }   // end getItemValueList() method

    private String buildItemListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
    {
	String select = " SELECT item.itemid, item.sku, item.title as Name, itemtype.title as type, " +
		           " item.listprice as Price, count(inventory.qty) as OnHand, " +
		           " taxclass.taxclassid as taxclassid ,taxclass.title as taxclass, " +
		           " item.cost, ven.entityid as vendorid, ven.name as vendor, " +
		           " man.entityid as manid, man.name as Manufacturer, count(item2.parent) AS noOfChild ";
       String joinConditions = " LEFT OUTER JOIN itemtype ON item.type = itemtype.itemtypeid "+
							 " LEFT OUTER JOIN inventory ON item.itemid = inventory.item " +
							 " LEFT OUTER JOIN item AS item2 ON item.itemid = item2.parent " +
							 " LEFT OUTER JOIN taxclass ON item.taxclass = taxclass.taxclassid "+
							 " LEFT OUTER JOIN entity man ON item.manufacturerid = man.entityid " +
							 " LEFT OUTER JOIN entity ven ON item.vendorid = ven.entityid ";
      String groupBy = " GROUP BY 1 ";
      StringBuffer from = new StringBuffer(" FROM item ");
      StringBuffer where = new StringBuffer(" WHERE item.deletestatus != 'Deleted' ");
      String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
      String limit = parameters.getLimitParam();

      // If a filter is applied we need to do an additional join against the
      // temporary order list filter table.
      if (applyFilter || permissionSwitch)
      {
        from.append(", listfilter AS lf ");
        where.append("AND  item.itemid = lf.ItemID");
      }
      // Build up the actual query using all the different permissions.
      // Where owner = passed individualId
      StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE itemlist ");
      query.append(select);
      query.append(from);
      query.append(joinConditions);
      query.append(where);
      query.append(groupBy);
      query.append(orderBy);
      query.append(limit);
      return query.toString();
    } // end buildItemListQuery() method    
        
    /**
     * Returns a ValueListVO representing a list of GlAccount records, based on
     * the <code>parameters</code> argument which limits results.
     */
     public ValueListVO getGlAccountValueList(int individualId, ValueListParameters parameters)
     {
       // How all the getValueLists should work:
       // 1. Query should be mostly canned, maybe to a temp table.
       // 2. The sort and limit options of the final query should be built using
       //    data from the parameters object.
       // 3. The columns from each row of the query will be stuffed into an arraylist
       //    Which will, each, populate the list being returned.
       ArrayList list = new ArrayList();
       // permissionSwitch turns the permission parts of the query on and off.
       // if individualId is less than zero then the list is requested without limiting
       // rows based on record rights.  If it is true than the rights are used.
       boolean permissionSwitch = individualId < 1 ? false : true;
       boolean applyFilter = false;
       String filter = parameters.getFilter();
       CVDal cvdl = new CVDal(this.dataSource);
       if (filter != null && filter.length() > 0)
       {
         String str = "CREATE TABLE glaccountlistfilter " + filter;
         cvdl.setSqlQuery(str);
         cvdl.executeUpdate();
         cvdl.setSqlQueryToNull();
         applyFilter = true;
       }
       
       String query = this.buildGlAccountListQuery(applyFilter, parameters);
       cvdl.setSqlQuery(query);
       cvdl.executeUpdate();
       cvdl.setSqlQueryToNull();
       cvdl.setSqlQuery("SELECT * FROM glaccountlist");
       list = cvdl.executeQueryList(1);
       cvdl.setSqlQueryToNull();
       
       int numberOfRecords = 0;
       cvdl.setSqlQuery("SELECT count(*) AS NoOfRecords FROM glaccountlist");
       Collection colList = cvdl.executeQuery();
       cvdl.clearParameters();
       cvdl.setSqlQueryToNull();

	 if (colList != null)
	 {
	   Iterator it = colList.iterator();
	   if (it.hasNext())
	   {
	     HashMap hm = (HashMap)it.next();
	     numberOfRecords = ((Number)hm.get("NoOfRecords")).intValue();
	   }//end of if (it.hasNext())
	 }//end of if (colList != null)
	 
       parameters.setTotalRecords(numberOfRecords);
       
       cvdl.setSqlQuery("DROP TABLE glaccountlist");
       cvdl.executeUpdate();
       // throw away the temp filter table, if necessary.
       if (applyFilter)
       {
         cvdl.setSqlQueryToNull();
         cvdl.setSqlQuery("DROP TABLE glaccountlistfilter");
         cvdl.executeUpdate();
       }

       cvdl.destroy();
       cvdl = null;
       
       return new ValueListVO(list, parameters);
     }   // end getGlAccountValueList() method

     private String buildGlAccountListQuery(boolean applyFilter, ValueListParameters parameters)
     {
	 String select = " SELECT gla.glaccountsid GLAccountsID, gla.title Name, gla.glaccounttype Type," +
	  				  " gla.Balance Balance ,glaccountParent.title ParentAccount ";
       String joinConditions = " LEFT OUTER JOIN glaccount glaccountParent ON gla.parent = glaccountParent.glaccountsid ";
     
       StringBuffer from = new StringBuffer(" FROM glaccount AS gla ");
       StringBuffer where = new StringBuffer(" WHERE 1=1 ");
       String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
       String limit = parameters.getLimitParam();

       // If a filter is applied we need to do an additional join against the
       // temporary order list filter table.
       if (applyFilter)
       {
         from.append(", glaccountlistfilter AS glf ");
         where.append(" AND gla.glaccountsid = glf.glaccountsid ");
       }
       // Build up the actual query using all the different permissions.
       // Where owner = passed individualId
       StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE glaccountlist ");
       query.append(select);
       query.append(from);
       query.append(joinConditions);
       query.append(where);
       query.append(orderBy);
       query.append(limit);
       return query.toString();
     } // end buildGlAccountListQuery() method    
     
     /**
      * Returns a ValueListVO representing a list of Item records, based on
      * the <code>parameters</code> argument which limits results.
      */
      public ValueListVO getInventoryValueList(int individualId, ValueListParameters parameters)
      {
        // How all the getValueLists should work:
        // 1. Query should be mostly canned, maybe to a temp table.
        // 2. The sort and limit options of the final query should be built using
        //    data from the parameters object.
        // 3. The columns from each row of the query will be stuffed into an arraylist
        //    Which will, each, populate the list being returned.
        ArrayList list = new ArrayList();
        // permissionSwitch turns the permission parts of the query on and off.
        // if individualId is less than zero then the list is requested without limiting
        // rows based on record rights.  If it is true than the rights are used.
        boolean permissionSwitch = individualId < 1 ? false : true;
        boolean applyFilter = false;
        String filter = parameters.getFilter();
        CVDal cvdl = new CVDal(this.dataSource);
        if (filter != null && filter.length() > 0)
        {
          String str = "CREATE TABLE inventorylistfilter " + filter;
          cvdl.setSqlQuery(str);
          cvdl.executeUpdate();
          cvdl.setSqlQueryToNull();
          applyFilter = true;
        }
        int numberOfRecords = 0;
        if (applyFilter)
        {
          numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "inventorylistfilter", individualId, 48, "inventory", "InventoryID", "owner", null, permissionSwitch);
        } else if (permissionSwitch) {
          numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualId, 48, "inventory", "InventoryID", "owner", null, permissionSwitch);
        }
        parameters.setTotalRecords(numberOfRecords);
        String query = this.buildInventoryListQuery(applyFilter, permissionSwitch, parameters);
        cvdl.setSqlQuery(query);
        cvdl.executeUpdate();
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("SELECT * FROM inventorylist");
        list = cvdl.executeQueryList(1);
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("DROP TABLE inventorylist");
        cvdl.executeUpdate();
        // throw away the temp filter table, if necessary.
        if (applyFilter)
        {
          cvdl.setSqlQueryToNull();
          cvdl.setSqlQuery("DROP TABLE inventorylistfilter");
          cvdl.executeUpdate();
        }
        if (applyFilter || permissionSwitch)
        {
          cvdl.setSqlQueryToNull();
          cvdl.setSqlQuery("DROP TABLE listfilter");
          cvdl.executeUpdate();
        }
        cvdl.destroy();
        cvdl = null;
        
        return new ValueListVO(list, parameters);
      }   // end getItemValueList() method

      private String buildInventoryListQuery(boolean applyFilter, boolean permissionSwitch, ValueListParameters parameters)
      {
  
		String select = " SELECT inventory.inventoryid as InventoryID, " +
		  		  		  " item.title as ItemName, inventory.title as Identifier, " +
		  		  		  " man.entityid, man.name as Manufacturer, ven.entityid as venEntityID, ven.name as Vendor ";
        String joinConditions = " LEFT OUTER JOIN item ON item.itemid = inventory.item "+
								 " LEFT OUTER JOIN entity man ON item.manufacturerid = man.entityid " +
								 " LEFT OUTER JOIN entity ven ON inventory.vendorid = ven.entityid ";
      
        StringBuffer from = new StringBuffer(" FROM inventory ");
        StringBuffer where = new StringBuffer(" WHERE inventory.linestatus != 'deleted' ");
        String orderBy = " ORDER BY " + String.valueOf(parameters.getSortColumn() + " " + parameters.getSortDirection());
        String limit = parameters.getLimitParam();

        // If a filter is applied we need to do an additional join against the
        // temporary order list filter table.
        if (applyFilter || permissionSwitch)
        {
          from.append(", listfilter AS lf ");
          where.append(" AND inventory.inventoryid = lf.inventoryid ");
        }
        // Build up the actual query using all the different permissions.
        // Where owner = passed individualId
        StringBuffer query = new StringBuffer("CREATE TEMPORARY TABLE inventorylist ");
        query.append(select);
        query.append(from);
        query.append(joinConditions);
        query.append(where);
        query.append(orderBy);
        query.append(limit);
        return query.toString();
      } // end buildInventoryListQuery() method    

}
