/*
 * $RCSfile: SaleHelperEJB.java,v $    $Revision: 1.2 $  $Date: 2005/07/08 20:59:37 $ - $Author: mcallist $
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

package com.centraview.sale.helper;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.account.helper.AccountHelper;
import com.centraview.account.helper.AccountHelperHome;
import com.centraview.account.item.ItemLocal;
import com.centraview.account.item.ItemLocalHome;
import com.centraview.account.item.ItemVO;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;
import com.centraview.sale.proposal.ItemElement;
import com.centraview.sale.proposal.ItemLines;
import com.centraview.sale.proposal.ProposalVO;

public class SaleHelperEJB implements SessionBean {
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private static Logger logger = Logger.getLogger(SaleHelperEJB.class);
  private String dataSource = "";

  /**
   * Set the associated session context. The container calls this method after
   * the instance creation. The enterprise Bean instance should store the
   * reference to the context object in an instance variable. This method is
   * called with no transaction context.
   */
  public void setSessionContext(SessionContext ctx) {
    this.ctx = ctx;
  }

  public SaleHelperEJB() {}

  /**
   * Called by the container to create a session bean instance. Its parameters
   * typically contain the information the client uses to customize the bean
   * instance for its use. It requires a matching pair in the bean class and its
   * home interface.
   */
  public void ejbCreate() {}

  /**
   * A container invokes this method before it ends the life of the session
   * object. This happens as a result of a client's invoking a remove operation,
   * or when a container decides to terminate the session object after a
   * timeout. This method is called with no transaction context.
   */
  public void ejbRemove() {}

  /**
   * The activate method is called when the instance is activated from its
   * 'passive' state. The instance should acquire any resource that it has
   * released earlier in the ejbPassivate() method. This method is called with
   * no transaction context.
   */
  public void ejbActivate() {}

  /**
   * The passivate method is called before the instance enters the 'passive'
   * state. The instance should release any resources that it can re-acquire
   * later in the ejbActivate() method. After the passivate method completes,
   * the instance must be in a state that allows the container to use the Java
   * Serialization protocol to externalize and store away the instance's state.
   * This method is called with no transaction context.
   */
  public void ejbPassivate() {}

  public Vector getAllStatus() {
    return getMasterListVector("sale.getallstatus");
  }

  public Vector getAllStage() {
    return getMasterListVector("sale.getallstage");
  }

  public Vector getAllType() {
    return getMasterListVector("sale.getalltype");
  }

  public Vector getAllProbability() {
    return getMasterListVector("sale.getallprobability");
  }

  public Vector getAllTerm() {
    return getMasterListVector("sale.getallterm");
  }

  /**
   * Description - This method get the result in the vector as key/value pair.
   * It will never return null.
   * 
   * @param queryKey
   *          The name of the query to ba called from QueryCollection.
   * @return A Vector of DDNameValues with the list key/value pairs. It will
   *         never return null.
   * @see com.centraview.common.DDNameValue
   * @see com.centraview.common.QueryCollection
   */
  private Vector getMasterListVector(String queryKey) {
    Vector vec = new Vector();
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql(queryKey);
      Collection col = dl.executeQuery();
      if (col != null) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap) it.next();
          int id = Integer.parseInt(hm.get("id").toString());
          String name = (String) hm.get("name");
          if (!(queryKey.equals("sale.getallstatus") && id == 0)) {
            DDNameValue dd = new DDNameValue(id, name);
            vec.add(dd);
          }
        }
      }
    } catch (Exception e) {
      logger.error("[getMasterListVector]: Exception getting key: "+queryKey, e);
    } finally {
      dl.destroy();
    }
    return vec;
  }

  /**
   * NOTE THIS METHOD IS NOT USED, IT IS KEPT FOR FUTURE ISSUES IF ANY CHANGES
   * COME Returns Tax in float for a given Tax Class & Jurisdiction
   * 
   * @return float tax
   */
  public float getTax(int taxClassId, int taxJurisdictionId) {
    float taxRate = 0.0f;
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSqlQueryToNull();
      dl.setSql("account.tax.gettax");
      dl.setInt(1, taxClassId);
      dl.setInt(2, taxJurisdictionId);
      Collection col = dl.executeQuery();
      if (col != null && !col.isEmpty()) {
          Iterator it = col.iterator();
          HashMap hm = (HashMap) it.next();
          taxRate = ((Double) hm.get("TaxRate")).floatValue();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new EJBException(e);
    } finally {
      dl.destroy();
    }
    return taxRate;
  } // end of getTax

  /**
   * NOTE THIS METHOD IS NOT USED, IT IS KEPT FOR FUTURE ISSUES IF ANY CHANGES
   * COME Sets Tax in float for a given Tax Class & Jurisdiction
   * 
   * @return void
   */
  public void setTax(int taxClassId, int taxJurisdictionId, float tax) {
    try {
      CVDal dl = new CVDal(dataSource);
      dl.setSqlQueryToNull();
      dl.setSql("account.tax.gettax");
      // "select TaxRate from taxmatrix where taxclassid = ? and
      // taxjurisdictionid = ?"
      dl.setInt(1, taxClassId);
      dl.setInt(2, taxJurisdictionId);

      Collection col = dl.executeQuery();
      if (col != null) {
        if (col.isEmpty()) {
          dl.setSqlQueryToNull();
          dl.setSql("account.tax.inserttax");
          // "insert into taxmatrix(taxclassid,taxjurisdictionid, taxrate)
          // values (?,?,?)
          dl.setInt(1, taxClassId);
          dl.setInt(2, taxJurisdictionId);
          dl.setFloat(3, tax);
          dl.executeUpdate();
        }// IF
        else {
          dl.setSqlQueryToNull();
          dl.setSql("account.tax.settax");
          // update taxmatrix set TaxRate = ? where taxclassid = ? and
          // taxjurisdictionid = ?"
          dl.setFloat(1, tax);
          dl.setInt(2, taxClassId);
          dl.setFloat(3, taxJurisdictionId);
          dl.executeUpdate();
        }// else
      }// if
      else {
        dl.setSqlQueryToNull();
        dl.setSql("account.tax.inserttax");
        // "insert into taxmatrix(taxclassid,taxjurisdictionid, taxrate) values
        // (?,?,?)
        dl.setInt(1, taxClassId);
        dl.setInt(2, taxJurisdictionId);
        dl.setFloat(3, tax);
        dl.executeUpdate();
      }// else

    } catch (Exception e) {
      e.printStackTrace();
    }

  }// end of setTax

  /*
   * Add Items to the proposal from Comma Separated String into the ItemLines
   */
  public ProposalVO calculateProposalItems(int userId, ProposalVO proposalVO, String newItemID) {
    StringTokenizer st;
    String token;

    ItemLines itemLines = proposalVO.getItemLines();

    if (itemLines == null) {
      itemLines = new ItemLines();
    } // end of if statement (itemLines == null)

    int counter = itemLines.size();
    if (counter != 0) {
      Set s = itemLines.keySet();
      Iterator itr = s.iterator();
      while (itr.hasNext()) {
        float taxRate = 0.0f;
        // ItemElement ie = (ItemElement)itr.next();
        ItemElement ie = (ItemElement) itemLines.get(itr.next());
        int id = ((Integer) ((IntMember) ie.get("ItemId")).getMemberValue()).intValue();
        try {
          InitialContext ic = CVUtility.getInitialContext();
          ItemLocalHome home = (ItemLocalHome) ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);

          ItemVO item = itemLocal.getItem(userId, id);

          int taxClassId = item.getTaxClassId();
          int taxJurisdictionId = 0;
          int shipToId = 0;

          if (proposalVO.getShippingaddressid() != null) {
            shipToId = Integer.parseInt(proposalVO.getShippingaddressid());
          }

          if (shipToId != 0) {
            AccountHelperHome hm = (AccountHelperHome) ic.lookup("local/AccountHelper");
            AccountHelper accHelper = hm.create();
            accHelper.setDataSource(this.dataSource);

            ContactHelperLocalHome home1 = (ContactHelperLocalHome) ic.lookup("local/ContactHelper");
            ContactHelperLocal contactHelperLocal = home1.create();
            contactHelperLocal.setDataSource(this.dataSource);

            if (taxJurisdictionId != 0) {
              taxRate = accHelper.getTax(taxClassId, taxJurisdictionId);
            } // end if (taxJurisdictionId != 0)
          } // end if (shipToId != 0)

          ((FloatMember) ie.get("UnitTaxrate")).setMemberValue(taxRate);

        } // end try block
        catch (Exception e) {
          ((FloatMember) ie.get("UnitTaxrate")).setMemberValue(0.0f);
        } // end of catch block (Exception)
      } // end of while loop (itr.hasNext())
    } // end of if statement (counter != 0)

    if (newItemID != null && !newItemID.equals("")) {
      st = new StringTokenizer(newItemID, ",");

      while (st.hasMoreTokens()) {
        try {
          float taxRate = 0.0f;
          token = st.nextToken();
          int intToken = Integer.parseInt(token);
          InitialContext ic = CVUtility.getInitialContext();

          float promotionPrice = 0.0f;

          CVDal dl = new CVDal(this.dataSource);
          dl.setSqlQueryToNull();
          dl.setSql("promotion.getpromotionitem");
          dl.setInt(1, intToken);
          Collection col = dl.executeQuery();

          if (col != null && col.size() != 0) {
            Iterator it = col.iterator();
            HashMap hm = (HashMap) it.next();
            if (hm != null) {
              promotionPrice = ((Number) hm.get("Price")).floatValue();
              // FIXME promotions broken here!
              Date promotionStartdate = ((Date) hm.get("Startdate"));
              Date promotionEnddate = ((Date) hm.get("Enddate"));
            }
          }

          dl.clearParameters();
          dl.destroy();

          ItemLocalHome home = (ItemLocalHome) ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);
          ItemVO item = itemLocal.getItem(userId, intToken);

          // Get the Required Fields from the Item VO
          String name = item.getItemName();
          String sku = item.getSku();
          float price = 0.0f;
          if (promotionPrice != 0.0f) {
            price = promotionPrice;
            name = name + " / " + item.getItemDesc() + " (Reflects Promotional Pricing) ";
          } else {
            price = (float) item.getPrice();
            name = name + " / " + item.getItemDesc();
          }

          int id = item.getItemId();
          int taxClassId = item.getTaxClassId();
          int taxJurisdictionId = 0;
          int shipToId = 0;

          if (proposalVO.getShippingaddressid() != null) {
            shipToId = Integer.parseInt(proposalVO.getShippingaddressid());
          } // end of if statement (proposalVO.getShippingaddressid() != null)

          if (shipToId != 0) {
            try {
              AccountHelperHome hm = (AccountHelperHome) ic.lookup("local/AccountHelper");
              AccountHelper accHelper = hm.create();
              accHelper.setDataSource(this.dataSource);

              ContactHelperLocalHome home2 = (ContactHelperLocalHome) ic.lookup("local/ContactHelper");
              ContactHelperLocal contactHelperLocal = home2.create();
              contactHelperLocal.setDataSource(this.dataSource);
              AddressVO addVO = contactHelperLocal.getAddress(shipToId);
              // FIXME I assume the addVO is gotten here so the taxJurisdictionId can be
              // found, unfortunately that doesn't happen.
              if (taxJurisdictionId != 0) {
                taxRate = accHelper.getTax(taxClassId, taxJurisdictionId);
              }// if (taxJurisdictionId !=0)
            } catch (Exception e) {
              System.out.println("[Exception]: SaleHelperEJB.calculateProposalItems: " + e.toString());
              e.printStackTrace();
            }
          } // end of if statement (shipToId != 0)

          // Form the ItemLines
          counter += 1;
          IntMember LineId = new IntMember("LineId", counter, 'D', "", 'T', false, 20);
          IntMember ItemId = new IntMember("ItemId", id, 'D', "", 'T', false, 20);
          StringMember SKU = new StringMember("SKU", sku, 'D', "", 'T', false);
          StringMember Description = new StringMember("Description", name, 'D', "", 'T', false);
          FloatMember Quantity = new FloatMember("Quantity", new Float(1.0f), 'D', "", 'T', false, 20);
          FloatMember PriceEach = new FloatMember("PriceEach", new Float(price), 'D', "", 'T', false, 20);
          FloatMember PriceExtended = new FloatMember("PriceExtended", new Float(0.0f), 'D', "", 'T', false, 20);
          FloatMember UnitTax = new FloatMember("UnitTax", new Float(0.0f), 'D', "", 'T', false, 20);
          FloatMember TaxRate = new FloatMember("UnitTaxrate", new Float(taxRate), 'D', "", 'T', false, 20);
          FloatMember OrderQuantity = new FloatMember("OrderQuantity", new Float(1.0f), 'D', "", 'T', false, 20);
          FloatMember PendingQuantity = new FloatMember("PendingQuantity", new Float(0.0f), 'D', "", 'T', false, 20);

          ItemElement ie = new ItemElement(counter);
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

          ie.setLineStatus("New");

          itemLines.put(new Integer(counter), ie);
        } // end of try block
        catch (Exception e) {
          System.out.println("[Exception]: SaleHelperEJB.calculateProposalItems: " + e.toString());
          e.printStackTrace();
        } // end of catch block (Exception)
      } // end of while loop (st.hasMoreTokens())
    } // end of if statement (newItemID != null && !newItemID.equals(""))

    itemLines.calculate();
    proposalVO.setItemLines(itemLines);
    return proposalVO;
  } // end of calculateProposalItems method

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds
   *          A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds) {
    this.dataSource = ds;
  }
} // end of SaleHelperEJB class
