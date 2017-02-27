/*
 * $RCSfile: AccountHelperEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:26 $ - $Author: mking_cv $
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


package com.centraview.account.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import com.centraview.account.common.ItemElement;
import com.centraview.account.common.ItemLines;
import com.centraview.account.common.PaymentLines;
import com.centraview.account.common.TaxMartixForm;
import com.centraview.account.expense.ExpenseVO;
import com.centraview.account.invoice.InvoiceVO;
import com.centraview.account.item.ItemLocal;
import com.centraview.account.item.ItemLocalHome;
import com.centraview.account.item.ItemVO;
import com.centraview.account.order.OrderVO;
import com.centraview.account.purchaseorder.PurchaseOrderVO;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.DateMember;
import com.centraview.common.DoubleMember;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;


/*
This is the EJB class for Accounting
The Logic for methods defined in Remote interface
is defined in this class
*/

public class AccountHelperEJB implements SessionBean
{
  protected SessionContext ctx;
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx)
  {
    this.ctx=ctx;
  }

  /**
   *
   *
   */
  public void ejbCreate()
  {
  }

  /**
   *
   *
   */
  public void ejbRemove()
  {
  }

  /**
   *
   *
   */
  public void ejbActivate()
  {
  }

  /**
   *
   *
   */
  public void ejbPassivate()
  {
  }


  /** returns Item Types in vector
    * @return Vector
    */
  public Vector getItemTypes()
  {
    Vector vec = new Vector();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
        dl.setSql("account.helper.getitemtypes");

      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();

      if (col != null)
      {
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          String name  = (String)hm.get("ItemType");
          int id     = ((Long)hm.get("ID")).intValue();
          DDNameValue dd = new DDNameValue(id,name);

          vec.add(dd);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.getItemTypes()] Exception thrown: "+e);
      e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }
    return vec;
  }


  /**
   * returns GLAccounts in vector
   * @return Vector
   */
  public Vector getGLAccounts()
  {
    Vector arl = new Vector();
    CVDal dl = new CVDal(dataSource);

    try
    {
      dl.setSqlQueryToNull();
      dl.setSql("account.helper.getglaccounts");

      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();

      if (col != null)
      {
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          String name  = (String)hm.get("GLAccount");
          int id     = ((Long)hm.get("ID")).intValue();
          DDNameValue dd = new DDNameValue(id,name);

          arl.add(dd);
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][AccountHelperEJB.getGLAccounts()] Exception thrown: "+e);
      e.printStackTrace();
    }finally{
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }
    return arl;
  }   // end getGLAccounts() method




        /** returns new GLAccounts in vector
          * @return Vector
          */

        public Vector getNewGLAccounts()
        {
                Vector arl = new Vector();
                CVDal dl = new CVDal(dataSource);
                try
                {
                        dl.setSqlQueryToNull();
                        dl.setSql("account.helper.getnewglaccounts");

                        Collection  col  = (Collection)dl.executeQuery();
                        Iterator it = col.iterator();

                        HashMap hm = new HashMap();
                        String name = "";
                        int id   = 0;
                        String description = "";
                        float balance = 0;
                        String type = "";
                        int parent = 0;
                        String externalId = "";
                        GLAccountVO glaVO = new GLAccountVO();


                        if (col != null)
                        {
                                while (it.hasNext())
                                {
                                  glaVO = new GLAccountVO();
                                  name = "";
                                  description = "";
                                  externalId = "";

                                  hm = (HashMap)it.next();
                                  if (hm.get("GLAccount") != null)
                                  {
                                    name = hm.get("GLAccount").toString();
                                  }
                                  id = Integer.parseInt(hm.get("ID").toString());
                                  if (hm.get("Description") != null)
                                  {
                                    description = hm.get("Description").toString();
                                  }
                                  balance = Float.parseFloat(hm.get("Balance").toString());
                                  type = hm.get("Type").toString();
                                  parent = Integer.parseInt(hm.get("Parent").toString());

                                  glaVO.setBalance(balance);
                                  glaVO.setDescription(description);
                                  glaVO.setGlaccountID(id);
                                  glaVO.setExternalID(externalId);
                                  glaVO.setGLAccountType(type);
                                  glaVO.setParentAccountID(parent);
                                  glaVO.setTitle(name);

                                  arl.add(glaVO);
                                }
                        }
                }
                catch(Exception e)
                {
                        System.out.println("[Exception][AccountHelperEJB.getGLAccounts()] Exception thrown: "+e);
                        e.printStackTrace();
                }
                finally
                {
                        dl.clearParameters();
                        dl.destroy();
                        dl = null;
                }
                return arl;

        }


  /**
   * Returns the Account Locations in vector
   *
   * @return Vector of DDName Values
   */
  public Vector getLocations()
  {
    Vector vec = new Vector();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
      dl.setSqlQuery("SELECT locationid, title FROM location");

      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();

      if (col != null)
      {
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          String name = (String) hm.get("title");
          int id    = ((Number) hm.get("locationid")).intValue();
          DDNameValue dd = new DDNameValue(id,name);

          vec.add(dd);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("[Exception] AccountHelperEJB.getLocations: " + e.toString());
      //e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }
    return vec;

  }
  /** Add the GLAccount in DataBase
  * Note: This method is kept for to be Majorly used from
  *    Syncronization module only.
  *    Users are not supposed to Add or Modify any
  *    Account directly as it can affect the whole
  *    System in dangerous manner.
  */
  public void addGLAccount(GLAccountVO glaVO)
  {
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
      dl.setSql("account.helper.addglaccount");
      //ALLSQL.put("account.helper.addglaccount",
      //"insert into glaccount(title,description,balance,
      // glaccounttype,parent,externalid) values(?,?,?,?,?,?)");
      dl.setString(1,glaVO.getTitle());
      dl.setString(2,glaVO.getDescription());
      dl.setFloat(3,glaVO.getBalance());
      dl.setString(4,glaVO.getGLAccountType());
      dl.setInt(5,glaVO.getParentAccountID());
      dl.setString(6,glaVO.getExternalID());

      dl.executeUpdate();
      dl.destroy();
    }catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.addGLAccount()] Exception thrown: "+e);
      e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }
    return;
  }

        public String deleteObjects(String modulename, ArrayList arl){

          CVDal dl = new CVDal(dataSource);

          String query = "";
          String ids = "";
          char zap = ' ';
          Collection col = null;

          Iterator it = arl.iterator();
          while(it.hasNext())
          {
            query += zap + Integer.parseInt(it.next().toString());
            zap = ',';
          }
          zap = ' ';

          try
          {
            if (modulename.equals("Individual"))
            {
              dl.setSql("account.helper.getEmployeesByExternalIds");
              dl.setString(1,query);
              col = dl.executeQuery();

              it = col.iterator();
              while(it.hasNext())
              {
                ids += zap + Integer.parseInt(it.next().toString());
                zap = ',';
              }

              dl.setSql("account.helper.deleteEmployee");
              dl.setString(1,ids);
              dl.executeUpdate();
            }else{

              dl.setSql("account.helper.delete" + modulename);
              dl.setString(1, query);
              dl.executeUpdate();
            }

          }catch(Exception e)
          {
            System.out.println("[Exception][AccountHelperEJB.updateGLAccount()] Exception thrown: "+e);
            e.printStackTrace();
          }
          finally
          {
              dl.clearParameters();
              dl.destroy();
              dl = null;
          }

          return query;

        }

  public void updateGLAccount(GLAccountVO glaVO)
  {
    //ALLSQL.put("account.helper.updateglaccount",
    //"update glaccount set title = ?,description = ?,
    // balance = ?,accounttype = ?,parent = ?,externalid = ?
    // where glaccountid = ?");
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSql("account.helper.updateglaccount");
      dl.setString(1,glaVO.getTitle());
      dl.setString(2,glaVO.getDescription());
      dl.setFloat(3,glaVO.getBalance());
      dl.setString(4,glaVO.getGLAccountType());
      dl.setInt(5,glaVO.getParentAccountID());
      dl.setString(6,glaVO.getExternalID());
      dl.setInt(7,glaVO.getGlaccountID());
      dl.executeUpdate();
      dl.destroy();
    }catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.updateGLAccount()] Exception thrown: "+e);
      e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }
  }

  public void addPaymentMethod(PaymentMethodVO pmVO)
  {
    //ALLSQL.put("account.helper.addPaymentMethod",
    // "insert into paymentmethod(title,externalid)
    // values(?,?)");
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSql("account.helper.addPaymentMethod");
      dl.setString(1,pmVO.getTitle());
      dl.setString(2,pmVO.getExternalID());
      dl.executeUpdate();
      dl.destroy();
    }catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.addPaymentMethod()] Exception thrown: "+e);
      e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }
  }


  public void savePaymentMethod(PaymentMethodVO pmVO)
  {
    try
    {
      CVDal dl = new CVDal(dataSource);


      dl.setSqlQueryToNull();
                        // try to find payment method by title
                dl.setSqlQuery("SELECT MethodID FROM paymentmethod WHERE UPPER(Title)=UPPER(?) AND ExternalId<>?");
                        dl.setString(1,pmVO.getTitle());
                        dl.setString(2,pmVO.getExternalID());

      Iterator it = ((Collection)dl.executeQuery()).iterator();

                        dl.setSqlQueryToNull();
      if (it.hasNext())
      {
                           // such payment method exists, update it

                           HashMap hm  = (HashMap)it.next();
                           int id     = ((Long)hm.get("MethodID")).intValue();
                           dl.setSql("account.helper.updatePaymentMethod");
                           dl.setInt(3,id);
      }
                        else {
                            // add new payment method
                            dl.setSql("account.helper.addPaymentMethod");
                        }
                        dl.setString(1,pmVO.getTitle());
                        dl.setString(2,pmVO.getExternalID());

                        dl.executeUpdate();
      dl.clearParameters();
      dl.destroy();
      dl = null;

    }catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.updatePaymentMethod()] Exception thrown: "+e);
      e.printStackTrace();
    }
  }

  public void updatePaymentMethod(PaymentMethodVO pmVO)
  {
    //ALLSQL.put("account.helper.updatePaymentMethod",
    // "update paymentmethod set title = ?,
    // externalid = ? where methodid = ?");
    try
    {
      CVDal dl = new CVDal(dataSource);
      dl.setSql("account.helper.updatePaymentMethod");
      dl.setString(1,pmVO.getTitle());
      dl.setString(2,pmVO.getExternalID());
      dl.setInt(3,pmVO.getMethodID());
      dl.executeUpdate();
      dl.destroy();
    }catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.updatePaymentMethod()] Exception thrown: "+e);
      e.printStackTrace();
    }
  }

  /** returns Tax Classes in vector
    * @return Vector
    */
  public Vector getTaxClasses()
  {
    Vector vec = new Vector();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
        dl.setSql("account.helper.gettaxclasses");

      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();

      if (col != null)
      {
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          String name  = (String)hm.get("TaxClass");
          int id     = Integer.parseInt(hm.get("ID").toString());
          DDNameValue dd = new DDNameValue(id,name);
          vec.add(dd);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.getTaxClasses()] Exception thrown: "+e);
      e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }

    return vec;

  }

  /** returns Account Status in vector
    * @return Vector
    */
  public Vector getAccountingStatus()
  {
    Vector vec = new Vector();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
        dl.setSql("account.helper.getaccountingstatus");

      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();

      if (col != null)
      {
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          String name  = (String)hm.get("AccountingStatus");
          int id     = ((Long)hm.get("ID")).intValue();
          DDNameValue dd = new DDNameValue(id,name);

          vec.add(dd);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.getAccountingStatus()] Exception thrown: "+e);
      e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }

    return vec;

  }

  /** returns Accounting Terms in vector
    * @return Vector
    */
  public Vector getAccountingTerms()
  {
    Vector vec = new Vector();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
        dl.setSql("account.helper.getaccountingterms");

      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();

      if (col != null)
      {
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          String name  = (String)hm.get("AccountingTerms");
          int id     = ((Long)hm.get("ID")).intValue();
          DDNameValue dd = new DDNameValue(id,name);

          vec.add(dd);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.getAccountingTerms] Exception Thrown: " + e);
      e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }

    return vec;

  }


  /** returns Payment Methods in vector
    * @return Vector
    */
  public Vector getPaymentMethods()
  {
    Vector arl = new Vector();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
        dl.setSql("account.helper.getpaymentmethods");

      Collection  col  = (Collection)dl.executeQuery();
      Iterator it = col.iterator();

      if (col != null)
      {
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          String name  = (String)hm.get("PaymentMethod");
          int id     = ((Long)hm.get("ID")).intValue();
          DDNameValue dd = new DDNameValue(id,name);

          arl.add(dd);
        }
      }
    }
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.getPaymentMethods] Exception Thrown: "+e);
      e.printStackTrace();
    }
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }

    return arl;

  }

        /** returns new Payment Methods in vector
          * @return Vector
          */
        public Vector getNewPaymentMethods()
        {
                Vector arl = new Vector();
                CVDal dl = new CVDal(dataSource);
                try
                {
                        dl.setSqlQueryToNull();
                    dl.setSql("account.helper.getnewpaymentmethods");

                        Collection  col  = (Collection)dl.executeQuery();
                        Iterator it = col.iterator();

            HashMap hm  = new HashMap();
            PaymentMethodVO pvo = new PaymentMethodVO();
            String name  = "";
            int id = 0;
            String externalId = "";


                        if (col != null)
                        {
                                while (it.hasNext())
                                {
                hm = (HashMap)it.next();
                if (hm.get("PaymentMethod") != null)
                {
                  name = hm.get("PaymentMethod").toString();
                }
                if (hm.get("ID") != null)
                {
                  id = Integer.parseInt(hm.get("ID").toString());
                }

                arl.add(new PaymentMethodVO(id,name,externalId));
                                }
                        }
                }
                catch(Exception e)
                {
                        System.out.println("[Exception][AccountHelperEJB.getPaymentMethods] Exception Thrown: "+e);
                        e.printStackTrace();
                }
                finally
                {
                        dl.clearParameters();
                        dl.destroy();
                        dl = null;
                }

                return arl;

        }


    /* This method returns all invocies for the given entity
   * @param int entityID
   * @return PaymentLines , invocies for the entity
   */
  public PaymentLines getPaymentInvoices(int entityID)
  {
    CVDal dl = new CVDal(dataSource);
    PaymentLines paymentLines= new PaymentLines();

    try
    {
      String q1 = " create temporary table  temppayinv select invoice.invoiceid invoiceid,invoice.title invoicetitle ,invoice.invoicedate invoicedate,invoice.total  invoicetotal  , applypayment.amount amountdue, applypayment.amount amountapplied from invoice, applypayment where 1 = 0";
      String q2 = " insert into temppayinv ( invoiceid, invoicetitle, invoicedate, invoicetotal,amountdue ) select invoice.InvoiceID,invoice.title ,invoice.invoicedate, invoice.total ,invoice.total   from invoice where  invoice.customerid = ? group by invoice.InvoiceID,invoice.title ,invoice.invoicedate, invoice.total";
      String q3 = " create temporary table t1 select  applypayment.invoiceid, sum(applypayment.amount) as AmountApplied from temppayinv ,applypayment where temppayinv.invoiceid = applypayment.invoiceid  and applypayment.linestatus != 'Deleted' group by  applypayment.invoiceid ";
      //String q4 = " create temporary table t2 select  temppayinv.invoiceid, (temppayinv.invoicetotal -  sum( applypayment.amount ) ) as AmountDue from applypayment ,temppayinv where  applypayment.invoiceid = temppayinv.invoiceid and applypayment.linestatus != 'Deleted' group by  temppayinv.invoiceid ";
      String q5 = " update  temppayinv ,t1 set temppayinv.AmountApplied   = t1.AmountApplied   where temppayinv.invoiceid = t1.invoiceid ";
      //String q6 = " update  temppayinv ,t2 set temppayinv.amountdue = t2.AmountDue  where temppayinv.invoiceid = t2.invoiceid ";

      String q7 = "select invoiceid,invoicetitle,invoicedate,invoicetotal,amountdue,amountapplied from temppayinv where amountapplied = 0";

      dl.setSqlQuery(q1);
      dl.executeUpdate();
      dl.setSqlQueryToNull();

      dl.setSqlQuery(q2);
      dl.setInt(1,entityID);
      dl.executeUpdate();
      dl.setSqlQueryToNull();

      dl.setSqlQuery(q3);
      dl.executeUpdate();
      dl.setSqlQueryToNull();

      /*dl.setSqlQuery(q4);
      dl.executeUpdate();
      dl.setSqlQueryToNull();
      */

      dl.setSqlQuery(q5);
      dl.executeUpdate();
      dl.setSqlQueryToNull();

/*      dl.setSqlQuery(q6);
      dl.executeUpdate();
      dl.setSqlQueryToNull();*/


      dl.setSqlQuery(q7);
      Collection col = dl.executeQuery();
      dl.setSqlQueryToNull();

      dl.setSqlQuery("drop table temppayinv");
      dl.executeUpdate();
      dl.setSqlQueryToNull();

      dl.setSqlQuery("drop table t1");
      dl.executeUpdate();
      dl.setSqlQueryToNull();


      Iterator it = col.iterator();

      int count = 1;
      while (it.hasNext())
      {
        HashMap hm =(HashMap)it.next();

//        Long lineID = (Long)hm.get("lineid");
        IntMember LineId = new IntMember("LineId",1,10,"",'T',false,20);

        IntMember invoiceID = new IntMember("InvoiceId",Integer.parseInt(hm.get("invoiceid").toString()),10,"",'T',false,20);



        StringMember invoiceNum = null;
        //if(hm.get("invoicetitle") != null)

        invoiceNum = new StringMember("InvoiceNum",((Number) hm.get("invoiceid")).toString(),10,"",'T',false);

        DateMember invDate = new DateMember( "Date" , (java.sql.Date)hm.get("invoicedate")  ,10 , "" , 'T' , false ,1 ,"EST" );

                                DoubleMember dblTotal = null;
        if(hm.get("invoicetotal") != null)
          dblTotal = new DoubleMember( "Total"  ,new Double(Double.parseDouble(hm.get("invoicetotal").toString())) , 10 , "", 'T' , false , 10 );

        DoubleMember dblAppAmt = null;
        if(hm.get("amountapplied") != null)
          dblAppAmt = new DoubleMember( "AmountApplied"  ,new Double(Double.parseDouble(hm.get("amountapplied").toString())) , 10 , "", 'T' , false , 10 );
        DoubleMember dblAmtDue = null;

        if(hm.get("amountdue") != null)
          dblAmtDue = new DoubleMember( "AmountDue"  ,new Double(Double.parseDouble(hm.get("amountdue").toString())) , 10 , "", 'T' , false , 10 );

        ItemElement ie = new ItemElement(11);

        ie.put ("LineId",LineId);
        ie.put ("InvoiceId",invoiceID);
        ie.put ("InvoiceNum",invoiceNum);
        ie.put ("Date",invDate);
        ie.put ("Total",dblTotal);
        ie.put ("AmountDue",dblAmtDue);
        ie.put ("AmountApplied",dblAppAmt);

        ie.setLineStatus("Active");

        paymentLines.put(""+count,ie);
        count ++;
      }
    }
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.getPaymentInvoices] Exception Thrown: "+e);
      e.printStackTrace();
    }
    finally
    {
      dl.destroy();
      dl = null;
    }

    return paymentLines;
  }


  public void addVendor(int entityID,int userID)
   {
     CVDal dl = new CVDal(dataSource);
     try
     {
       dl.setSql("vendor.addvendor");
       //insert into vendor (entityid) values (?)
       dl.setInt(1,entityID); //entityid
       dl.executeUpdate();
     }
     catch(Exception e)
     {
       System.out.println("[Exception][AccountHelperEJB.addVendor] Exception Thrown: "+e);
       e.printStackTrace();
     }
     finally
     {
       dl.destroy();
       dl = null;
     }
   }// end of addVendor

   public void deleteVendor(int entityID,int userID)
   {
     CVDal dl = new CVDal(dataSource);
     try
     {
       dl.setSql("vendor.deletevendor");
       //delete from  vendor where entityid  = ?
       dl.setInt(1,entityID); //entityid
       dl.executeUpdate();
     }
     catch(Exception e)
     {
       System.out.println("[Exception][AccountHelperEJB.deleteVendor] Exception Thrown: "+e);
       e.printStackTrace();
     }
     finally
     {
       dl.destroy();
       dl = null;
     }
   }// end of deleteVendor


  /**
   * Returns Tax in float for a given Tax Class & Jurisdiction.
   *
   * @param taxClassId The tax class id.
   * @param taxJurisdictionId The tax jusrisdiction id.
   *
   * @return The tax value.
   */
  public float getTax(int taxClassId, int taxJurisdictionId)
  {
    CVDal dl = new CVDal(dataSource);
    float returnFloat = 0.0f;
    try
    {
      dl.setSqlQueryToNull();
      dl.setSql("account.tax.gettax");
      dl.setInt(1,taxClassId);
      dl.setInt(2,taxJurisdictionId);

      Collection  col  = (Collection) dl.executeQuery();
      if (col != null)
      {
        if (!col.isEmpty())
        {
          Iterator it = col.iterator();
          HashMap hm  = (HashMap)it.next();
          returnFloat  = Float.parseFloat(hm.get( "TaxRate" ).toString());
        } //end of if statement (!col.isEmpty())
      } //end of if statement (col != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception]: AccountHelperEJB.getTax: " + e.toString());
      e.printStackTrace();
    }  //end of catch block (Exception)
    finally
    {
      dl.destroy();
      dl = null;
    } //end of finally block
    return returnFloat;
  }  //end of getTax method



  // TODO rewrite the calculateInvoiceItems, calculateOrderItems and calculatePurchaseOrderItems
  // These methods all do basically the same thing.  They should be rewritten to be three
  // small methods which call a private method which does the work in a generalized way, any
  // type specific code can be in the smaller methods.
  /*
  *  Add Items to the invoice from Comma Separated String into the ItemLines
  */
  public InvoiceVO calculateInvoiceItems(int userId, InvoiceVO invoiceVO, String newItemID)
  {
    //CVDal dl = new CVDal(dataSource);
    StringTokenizer st;
    String token, nextItr;

    ItemLines itemLines = invoiceVO.getItemLines();
    if(itemLines == null)
      itemLines = new ItemLines();
    int counter = itemLines.size();
    if (counter!=0)
    {
      Set s = itemLines.keySet();
      Iterator itr = s.iterator();
      while(itr.hasNext() )
      {
        float taxRate = 0.0f;
        //ItemElement ie = (ItemElement)itr.next();
        ItemElement ie = (ItemElement)itemLines.get(itr.next());
        int id = ((Integer)((IntMember)ie.get("ItemId")).getMemberValue()).intValue();
        try
        {
          InitialContext ic = CVUtility.getInitialContext();
          ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);
          ItemVO item = itemLocal.getItem(userId,id);
          int taxClassId = item.getTaxClassId();
          int taxJurisdictionId = 0;

          if (invoiceVO.getShipToId()!=0)
          {
            ContactHelperLocalHome home1 = ( ContactHelperLocalHome )ic.lookup("local/ContactHelper");
            ContactHelperLocal contactHelperLocal = home1.create();
            contactHelperLocal.setDataSource(this.dataSource);

            AddressVO addVO = contactHelperLocal.getAddress(invoiceVO.getShipToId());
            taxJurisdictionId = 0;
            if (taxJurisdictionId !=0)
            {
              taxRate = getTax(taxClassId, taxJurisdictionId);
            }//if (taxJurisdictionId !=0)
          }//if InvoiceVO.shipToId!=null
          ((FloatMember)ie.get("UnitTaxrate")).setMemberValue(taxRate);
        }catch (Exception e)
        {
          ((FloatMember)ie.get("UnitTaxrate")).setMemberValue(0.0f);

        }
      }//while


    }//if counter!=0


    if (newItemID != null && !newItemID.equals("") )
      {
        st = new StringTokenizer(newItemID, ",");

        while (st.hasMoreTokens())
        {
          try
          {
          float taxRate = 0.0f;
          token   = (String)st.nextToken();
          int intToken = Integer.parseInt(token);
          InitialContext ic = CVUtility.getInitialContext();
          ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);
          ItemVO item = itemLocal.getItem(userId,intToken);


          //Get the Required Fields from the Item VO
          String name = item.getItemName();
          String sku = item.getSku();
          float price = (float)item.getPrice();
          int id = item.getItemId();
          int taxClassId = item.getTaxClassId();
          int taxJurisdictionId = 0;
          if (invoiceVO.getShipToId()!=0)
          {
            try
            {
            ContactHelperLocalHome home2 =( ContactHelperLocalHome)ic.lookup("local/ContactHelper");
            ContactHelperLocal contactHelperLocal = home2.create();
            contactHelperLocal.setDataSource(this.dataSource);
            AddressVO addVO = contactHelperLocal.getAddress(invoiceVO.getShipToId());
            taxJurisdictionId = 0;
            if (taxJurisdictionId !=0)
            {
              taxRate = getTax(taxClassId, taxJurisdictionId);
            }//if (taxJurisdictionId !=0)
            }catch(Exception e)
              {
                System.out.println("[Exception][AccountHelperEJB.calculateInvoiceItems] Exception Thrown: "+e);
                e.printStackTrace();
              }
          }//if shipToId !=null
          //Form the ItemLines
              counter += 1;
              IntMember LineId = new IntMember("LineId",counter,10,"",'T',false,20);
              IntMember ItemId = new IntMember("ItemId",id,10,"",'T',false,20);
              StringMember SKU = new StringMember("SKU",sku,10,"",'T',false);
              StringMember Description = new StringMember("Description",name,10,"",'T',false);
              FloatMember  Quantity = new FloatMember("Quantity",new Float(1.0f),10,"",'T',false,20);
              FloatMember  PriceEach = new FloatMember("PriceEach",new Float(price),10,"",'T',false,20);
              FloatMember  PriceExtended = new FloatMember("PriceExtended",new Float(0.0f),10,"",'T',false,20);
              FloatMember  UnitTax = new FloatMember("UnitTax",new Float(0.0f),10,"",'T',false,20);
              FloatMember  TaxRate = new FloatMember("UnitTaxrate",new Float(taxRate),10,"",'T',false,20);
              FloatMember  OrderQuantity = new FloatMember("OrderQuantity",new Float(1.0f),10,"",'T',false,20);
              FloatMember  PendingQuantity = new FloatMember("PendingQuantity",new Float(0.0f),10,"",'T',false,20);


              ItemElement ie = new ItemElement(counter);
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

              ie.setLineStatus("New");

              itemLines.put(new Integer(counter), ie);
          }catch(Exception e)
            {
              System.out.println("[Exception][AccountHelperEJB.calculateInvoiceItems] Exception Thrown: "+e);
              e.printStackTrace();
            }

        }//while

      }//if
  itemLines.calculate();
  invoiceVO.setItemLines(itemLines);
  return invoiceVO;

  }//End of AddItems

  /*
   *  Add Items to the Order from Comma Separated String into the ItemLines
   */
  public OrderVO calculateOrderItems(int userId,OrderVO orderVO, String newItemID)
  {

    StringTokenizer st;
    String token, nextItr;

    ItemLines itemLines = orderVO.getItemLines();

    if(itemLines == null)
    itemLines = new ItemLines();

    int counter = itemLines.size();
    if (counter!=0)
    {
      Set s = itemLines.keySet();
      Iterator itr = s.iterator();
      while(itr.hasNext() )
      {
        float taxRate = 0.0f;

        ItemElement ie = (ItemElement)itemLines.get(itr.next());
        int id = ((Integer)((IntMember)ie.get("ItemId")).getMemberValue()).intValue();
        try
        {
          InitialContext ic = CVUtility.getInitialContext();
          ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);
          ItemVO item = itemLocal.getItem(userId,id);
          int taxClassId = item.getTaxClassId();
          int taxJurisdictionId = 0;

          if (orderVO.getShipToAddIdValue()!=0)
          {
            ContactHelperLocalHome home1 = ( ContactHelperLocalHome )ic.lookup("local/ContactHelper");
            ContactHelperLocal contactHelperLocal = home1.create();
            contactHelperLocal.setDataSource(this.dataSource);
            AddressVO addVO = contactHelperLocal.getAddress(orderVO.getShipToAddIdValue());
            taxJurisdictionId = 0;
            if (taxJurisdictionId !=0)
            {
              taxRate = getTax(taxClassId, taxJurisdictionId);
            }//if (taxJurisdictionId !=0)
          }//if InvoiceVO.shipToId!=null
          ((FloatMember)ie.get("UnitTaxrate")).setMemberValue(taxRate);
        }catch (Exception e)
        {
          ((FloatMember)ie.get("UnitTaxrate")).setMemberValue(0.0f);
          System.out.println("[Exception][AccountHelperEJB.calculateOrderItems] Exception Thrown: "+e);
        }
      }//while


    }//if counter!=0


    if (newItemID != null && !newItemID.equals("") )
      {
        st = new StringTokenizer(newItemID, ",");

        while (st.hasMoreTokens())
        {
          try
          {
          float taxRate = 0.0f;
          token   = (String)st.nextToken();
          int intToken = Integer.parseInt(token);
          InitialContext ic = CVUtility.getInitialContext();
          ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);
          ItemVO item = itemLocal.getItem(userId,intToken);


          //Get the Required Fields from the Item VO
          String name = item.getItemName();
          String sku = item.getSku();
          float price = (float)item.getPrice();
          int id = item.getItemId();
          int taxClassId = item.getTaxClassId();
          int taxJurisdictionId = 0;
          if (orderVO.getShipToAddIdValue()!=0)
          {
            try
            {
            ContactHelperLocalHome home2 =( ContactHelperLocalHome)ic.lookup("local/ContactHelper");
            ContactHelperLocal contactHelperLocal = home2.create();
            contactHelperLocal.setDataSource(this.dataSource);
            AddressVO addVO = contactHelperLocal.getAddress(orderVO.getShipToAddIdValue());
            taxJurisdictionId = 0;
            if (taxJurisdictionId !=0)
            {
              taxRate = getTax(taxClassId, taxJurisdictionId);
            }//if (taxJurisdictionId !=0)
            }catch(Exception e)
              {
                System.out.println("[Exception][AccountHelperEJB.calculateOrderItems] Exception Thrown: "+e);
                e.printStackTrace();
              }
          }//if shipToId !=null
          //Form the ItemLines
              counter += 1;
              IntMember LineId = new IntMember("LineId",counter,10,"",'T',false,20);
              IntMember ItemId = new IntMember("ItemId",id,10,"",'T',false,20);
              StringMember SKU = new StringMember("SKU",sku,10,"",'T',false);
              StringMember Description = new StringMember("Description",name,10,"",'T',false);
              FloatMember  Quantity = new FloatMember("Quantity",new Float(1.0f),10,"",'T',false,20);
              FloatMember  PriceEach = new FloatMember("PriceEach",new Float(price),10,"",'T',false,20);
              FloatMember  PriceExtended = new FloatMember("PriceExtended",new Float(0.0f),10,"",'T',false,20);
              FloatMember  UnitTax = new FloatMember("UnitTax",new Float(0.0f),10,"",'T',false,20);
              FloatMember  TaxRate = new FloatMember("UnitTaxrate",new Float(taxRate),10,"",'T',false,20);
              FloatMember  OrderQuantity = new FloatMember("OrderQuantity",new Float(1.0f),10,"",'T',false,20);
              FloatMember  PendingQuantity = new FloatMember("PendingQuantity",new Float(0.0f),10,"",'T',false,20);


              ItemElement ie = new ItemElement(counter);
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

              ie.setLineStatus("New");

              itemLines.put(new Integer(counter), ie);
          }catch(Exception e)
            {
              System.out.println("[Exception][AccountHelperEJB.calculateOrderItems] Exception Thrown: "+e);
              e.printStackTrace();
            }

        }//while

      }//if
    itemLines.calculate();
    orderVO.setItemLines(itemLines);
    return orderVO;

  }//End of AddItems for Order


  /*
   *  Add Items to the PurchaseOrder from Comma Separated String into the ItemLines
   */
  public PurchaseOrderVO calculatePurchaseOrderItems(int userId,PurchaseOrderVO purchaseOrderVO, String newItemID)
  {

    StringTokenizer st;
    String token, nextItr;

    ItemLines itemLines = purchaseOrderVO.getItemLines();

    if(itemLines == null)
    itemLines = new ItemLines();

    int counter = itemLines.size();
    if (counter!=0)
    {
      Set s = itemLines.keySet();
      Iterator itr = s.iterator();
      while(itr.hasNext() )
      {
        float taxRate = 0.0f;

        ItemElement ie = (ItemElement)itemLines.get(itr.next());
        int id = ((Integer)((IntMember)ie.get("ItemId")).getMemberValue()).intValue();
        try
        {
          InitialContext ic = CVUtility.getInitialContext();
          ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);
          ItemVO item = itemLocal.getItem(userId,id);
          int taxClassId = item.getTaxClassId();
          int taxJurisdictionId = 0;
          if (purchaseOrderVO.getShipToId()!=0)
          {
            ContactHelperLocalHome home1 = ( ContactHelperLocalHome )ic.lookup("local/ContactHelper");
            ContactHelperLocal contactHelperLocal = home1.create();
            contactHelperLocal.setDataSource(this.dataSource);
            AddressVO addVO = contactHelperLocal.getAddress(purchaseOrderVO.getShipToId());
            taxJurisdictionId = 0;
            if (taxJurisdictionId !=0)
            {
              taxRate = getTax(taxClassId, taxJurisdictionId);
            }//if (taxJurisdictionId !=0)
          }//if InvoiceVO.shipToId!=null
          ((FloatMember)ie.get("UnitTaxrate")).setMemberValue(taxRate);
        }catch (Exception e)
        {
          ((FloatMember)ie.get("UnitTaxrate")).setMemberValue(0.0f);
          System.out.println("[Exception][AccountHelperEJB.calculatePurchaseOrderItems] Exception Thrown: "+e);
        }
      }//while


    }//if counter!=0


    if (newItemID != null && !newItemID.equals("") )
      {
        st = new StringTokenizer(newItemID, ",");

        while (st.hasMoreTokens())
        {
          try
          {
          float taxRate = 0.0f;
          token   = (String)st.nextToken();
          int intToken = Integer.parseInt(token);
          InitialContext ic = CVUtility.getInitialContext();
          ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);
          ItemVO item = itemLocal.getItem(userId,intToken);


          //Get the Required Fields from the Item VO
          String name = item.getItemName();
          String sku = item.getSku();
          float price = (float)item.getPrice();
          int id = item.getItemId();
          int taxClassId = item.getTaxClassId();
          int taxJurisdictionId = 0;
          if (purchaseOrderVO.getShipToId()!=0)
          {
            try
            {
            ContactHelperLocalHome home2 =( ContactHelperLocalHome)ic.lookup("local/ContactHelper");
            ContactHelperLocal contactHelperLocal = home2.create();
            contactHelperLocal.setDataSource(this.dataSource);
            AddressVO addVO = contactHelperLocal.getAddress(purchaseOrderVO.getShipToId());
            taxJurisdictionId = 0;
            if (taxJurisdictionId !=0)
            {
              taxRate = getTax(taxClassId, taxJurisdictionId);
            }//if (taxJurisdictionId !=0)
            }catch(Exception e)
              {
                System.out.println("[Exception][AccountHelperEJB.calculatePurchaseOrderItems] Exception Thrown: "+e);
                e.printStackTrace();
              }
          }//if shipToId !=null
          //Form the ItemLines
              counter += 1;
              IntMember LineId = new IntMember("LineId",counter,10,"",'T',false,20);
              IntMember ItemId = new IntMember("ItemId",id,10,"",'T',false,20);
              StringMember SKU = new StringMember("SKU",sku,10,"",'T',false);
              StringMember Description = new StringMember("Description",name,10,"",'T',false);
              FloatMember  Quantity = new FloatMember("Quantity",new Float(1.0f),10,"",'T',false,20);
              FloatMember  PriceEach = new FloatMember("PriceEach",new Float(price),10,"",'T',false,20);
              FloatMember  PriceExtended = new FloatMember("PriceExtended",new Float(0.0f),10,"",'T',false,20);
              FloatMember  UnitTax = new FloatMember("UnitTax",new Float(0.0f),10,"",'T',false,20);
              FloatMember  TaxRate = new FloatMember("UnitTaxrate",new Float(taxRate),10,"",'T',false,20);
              FloatMember  OrderQuantity = new FloatMember("OrderQuantity",new Float(1.0f),10,"",'T',false,20);
              FloatMember  PendingQuantity = new FloatMember("PendingQuantity",new Float(0.0f),10,"",'T',false,20);


              ItemElement ie = new ItemElement(counter);
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

              ie.setLineStatus("New");

              itemLines.put(new Integer(counter), ie);
          }catch(Exception e)
            {
              System.out.println("[Exception][AccountHelperEJB.calculatePurchaseOrderItems] Exception Thrown: "+e);
              e.printStackTrace();
            }

        }//while

      }//if
  itemLines.calculate();
  purchaseOrderVO.setItemLines(itemLines);
  return purchaseOrderVO;

}//End of AddItems for PurchaseOrder


  /*
   *  Add Items to the Expense from Comma Separated String into the ItemLines
   */
  public ExpenseVO calculateExpenseItems(int userId,ExpenseVO expenseVO, String newItemID)
  {

    StringTokenizer st;
    String token, nextItr;

    ItemLines itemLines = expenseVO.getItemLines();

    if(itemLines == null)
    itemLines = new ItemLines();

    int counter = itemLines.size();

    if (newItemID != null && !newItemID.equals("") )
    {
      st = new StringTokenizer(newItemID, ",");

      while (st.hasMoreTokens())
      {
        try
        {
          float taxRate = 0.0f;
          token   = (String)st.nextToken();
          int intToken = Integer.parseInt(token);
          InitialContext ic = CVUtility.getInitialContext();
          ItemLocalHome home = (ItemLocalHome)ic.lookup("local/Item");
          ItemLocal itemLocal = home.create();
          itemLocal.setDataSource(this.dataSource);
          ItemVO item = itemLocal.getItem(userId,intToken);

          //Get the Required Fields from the Item VO
          String name = item.getItemName();
          String sku = item.getSku();
          float price = (float)item.getPrice();
          int id = item.getItemId();

          //Form the ItemLines
          counter += 1;
          IntMember LineId = new IntMember("LineId",counter,10,"",'T',false,20);
          IntMember ItemId = new IntMember("ItemId",id,10,"",'T',false,20);
          StringMember SKU = new StringMember("SKU",sku,10,"",'T',false);
          StringMember Description = new StringMember("Description",name,10,"",'T',false);
          FloatMember  Quantity = new FloatMember("Quantity",new Float(1.0f),10,"",'T',false,20);
          FloatMember  PriceEach = new FloatMember("PriceEach",new Float(price),10,"",'T',false,20);
          FloatMember  PriceExtended = new FloatMember("PriceExtended",new Float(0.0f),10,"",'T',false,20);
          FloatMember  UnitTax = new FloatMember("UnitTax",new Float(0.0f),10,"",'T',false,20);
          FloatMember  TaxRate = new FloatMember("UnitTaxrate",new Float(taxRate),10,"",'T',false,20);
          FloatMember  OrderQuantity = new FloatMember("OrderQuantity",new Float(1.0f),10,"",'T',false,20);
          FloatMember  PendingQuantity = new FloatMember("PendingQuantity",new Float(0.0f),10,"",'T',false,20);


          ItemElement ie = new ItemElement(counter);
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

          ie.setLineStatus("New");

          itemLines.put(new Integer(counter), ie);
        }catch(Exception e)
        {
          System.out.println("[Exception][AccountHelperEJB.calculateExpenseItems] Exception Thrown: "+e);
          e.printStackTrace();
        }

      }//while

    }//if
    itemLines.calculate();
    expenseVO.setItemLines(itemLines);
    return expenseVO;

  }//End of AddItems for Expense

  /**
    * This method will create a bean class with information of JurisdictionID and JurisdictionName. Then store the Object in a Collection.
    *
    * @return Vector  Its a collection of JurisdictionID and JurisdictionName.
    */
  public Vector getTaxJurisdiction()
  {
    Vector vec = new Vector();
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
      dl.setSqlQuery("select taxjurisdictionid , taxjurisdictionname from taxjurisdiction");

      Collection  col  = (Collection)dl.executeQuery();
      if (col != null)
      {
        Iterator it = col.iterator();
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          String name  = (String)hm.get("taxjurisdictionname");
          int id     = Integer.parseInt(hm.get("taxjurisdictionid").toString());
          DDNameValue dd = new DDNameValue(id,name);
          vec.add(dd);
        }// end of while (it.hasNext())
      }// end of if (col != null)
    }// end of try block
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.getTaxJurisdiction()] Exception thrown: "+e);
      e.printStackTrace();
    }// end of catch block
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }// end of finally block
    return vec;
  }// end of getTaxJurisdiction()


  /**
    * This method will delete a entry from either taxClass or taxJurisdiotion according the Parmater typeClassOrJurisdiction.
    * We will delete a entry on bais Identity for either taxClass or TaxJurisdiction on baisis of the type of operation.
    * We can delete a jurisdiction directly, but before deleting the taxClass. We must have to make sure that its not used by any other item in the Application.
    *
    * @param taxClassOrJurisdictionID A integer will represent the Identification for either taxClass or TaxJurisdiction on baisis of the type of operation which we are carrying out.
    * @param typeClassOrJurisdiction A String will represent are we going to insert the name in either taxClass or taxJurisdiction
    * @return void
    */
  public void removeTaxClassOrJurisdiction(int taxClassOrJurisdictionID, String typeClassOrJurisdiction){
    CVDal dl = new CVDal(dataSource);
    try{
      if (typeClassOrJurisdiction != null && typeClassOrJurisdiction.equals("taxJurisdiction")){
        dl.setSqlQueryToNull();
        dl.setSqlQuery("delete from taxjurisdiction where taxjurisdictionid = ?");
        dl.setInt(1,taxClassOrJurisdictionID);
        dl.executeUpdate();
        dl.setSqlQueryToNull();
        dl.setSqlQuery("delete from taxmatrix where taxjurisdictionid = ?");
        dl.setInt(1,taxClassOrJurisdictionID);
        dl.executeUpdate();


      }// end of if (typeClassOrJurisdiction != null && typeClassOrJurisdiction.equals("Jurisdiction"))
      if (typeClassOrJurisdiction != null && typeClassOrJurisdiction.equals("taxClass")){

        boolean deleteFlag = true;
        dl.setSqlQueryToNull();
        dl.setSqlQuery("select taxclass from item where taxclass=?");
        dl.setInt(1,taxClassOrJurisdictionID);
        Collection  col  = (Collection) dl.executeQuery();
        if (col != null)
        {
          Iterator it = col.iterator();
          if (it.hasNext())
          {
            deleteFlag = false;
          }// end of if (it.hasNext())
        }// end of if (col != null)
        if(deleteFlag){
          dl.setSqlQueryToNull();
          dl.setSqlQuery("delete from taxclass where taxclassid = ?");
          dl.setInt(1,taxClassOrJurisdictionID);
          dl.executeUpdate();

          dl.setSqlQueryToNull();
          dl.setSqlQuery("delete from taxmatrix where taxclassid = ?");
          dl.setInt(1,taxClassOrJurisdictionID);
          dl.executeUpdate();
        }// end of if(deleteFlag)

      }// end of if (typeClassOrJurisdiction != null && typeClassOrJurisdiction.equals("Class"))
    }// end of try block
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.removeTaxClassOrJurisdiction()] Exception thrown: "+e);
      e.printStackTrace();
    }// end of catch block
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }// end of finally block
  }// end of method insertTaxClassOrJurisdiction()

  /**
    * This method will insert the taxClass or taxJurisdiotion according the Parmater typeClassOrJurisdiction.
    * we will insert the name in either taxClass or taxJurisdiction depending the type of operation which we are performing.
    *
    * @param taxClassOrJurisdiction A String will represent the Name for the either taxClass or taxJurisdiction
    * @param typeClassOrJurisdiction A String will represent are we going to insert the name in either taxClass or taxJurisdiction
    * @return void
    */
  public void insertTaxClassOrJurisdiction(String taxClassOrJurisdiction, String typeClassOrJurisdiction){
    CVDal dl = new CVDal(dataSource);
    try{
      if (typeClassOrJurisdiction != null && typeClassOrJurisdiction.equals("taxJurisdiction")){

        dl.setSqlQueryToNull();
        dl.setSqlQuery("insert into taxjurisdiction (taxjurisdictionname) values(?)");
        dl.setString(1,taxClassOrJurisdiction);
        dl.executeUpdate();
        int taxJurisdictionID  = dl.getAutoGeneratedKey();
        if(taxJurisdictionID > 0){
          Vector taxClasses = this.getTaxClasses();
          if(taxClasses != null && taxClasses.size() != 0){
            for(int i=0; i<taxClasses.size();i++){
              DDNameValue taxClassNameValue = (DDNameValue)taxClasses.get(i);
              if(taxClassNameValue != null){
                int taxClassID = taxClassNameValue.getId();
                dl.setSqlQuery("insert into taxmatrix values(?,?,?)");
                dl.setInt(1,taxClassID);
                dl.setInt(2,taxJurisdictionID);
                dl.setInt(3,0);
                dl.executeUpdate();
              }// end of if(taxClassNameValue != null)
            }// end of for(int i=0; i<taxClasses.size();i++)
          }// end of if(taxClasses != null && taxClasses.size() != 0)
        }// end of if(taxJurisdictionID > 0)

      }// end of if (typeClassOrJurisdiction != null && typeClassOrJurisdiction.equals("Jurisdiction"))
      if (typeClassOrJurisdiction != null && typeClassOrJurisdiction.equals("taxClass")){
        dl.setSqlQueryToNull();
        dl.setSqlQueryToNull();
        dl.setSqlQuery("insert into taxclass (title) values(?)");
        dl.setString(1,taxClassOrJurisdiction);
        dl.executeUpdate();
        int taxClassID  = dl.getAutoGeneratedKey();
        if(taxClassID > 0){
          Vector taxJurisdiction = this.getTaxJurisdiction();
          if(taxJurisdiction != null && taxJurisdiction.size() != 0){
            for(int i=0; i<taxJurisdiction.size();i++){
              DDNameValue taxJurisdictionNameValue = (DDNameValue)taxJurisdiction.get(i);
              if(taxJurisdictionNameValue != null){
                int taxJurisdictionID = taxJurisdictionNameValue.getId();
                dl.setSqlQuery("insert into taxmatrix values(?,?,?)");
                dl.setInt(1,taxClassID);
                dl.setInt(2,taxJurisdictionID);
                dl.setInt(3,0);
                dl.executeUpdate();
              }// end of if(taxJurisdictionNameValue != null)
            }// end of for(int i=0; i<taxJurisdiction.size();i++)
          }// end of if(taxJurisdiction != null && taxJurisdiction.size() != 0)
        }// end of if(taxClassID > 0)

      }// end of if (typeClassOrJurisdiction != null && typeClassOrJurisdiction.equals("Class"))
    }// end of try block
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.insertTaxClassOrJurisdiction()] Exception thrown: "+e);
      e.printStackTrace();
    }// end of catch block
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }// end of finally block
  }// end of method insertTaxClassOrJurisdiction()


  /**
    * We will get the Map for the TaxMatrix with key value
    * (For Example: J1C1 its nothing but following number after character "J" is Jurisdiction ID
    * following number after character "C" is Class ID)
    * Value Object will hold the value for the TaxRate
    *
    * @return taxMatrix Its a Map for the TaxMatrix with key value
    * (For Example: J1C1 its nothing but following number after character "J" is Jurisdiction ID
    * following number after character "C" is Class ID)
    * Value Object will hold the value for the TaxRate
    */
  public HashMap getTaxMartix()
  {
    CVDal dl = new CVDal(dataSource);
    HashMap taxMatrix = new HashMap();
    try
    {
      dl.setSqlQueryToNull();
      dl.setSqlQuery("select * from taxmatrix");
      Collection  col  = (Collection) dl.executeQuery();
      if (col != null)
      {
        Iterator it = col.iterator();
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          int taxClassID = ((Number)hm.get("taxclassid")).intValue();
          int taxJurisdictionID = ((Number)hm.get("taxjurisdictionid")).intValue();
          float taxRate = ((Number)hm.get("taxrate")).floatValue();
          String keyValue= "J"+taxJurisdictionID+"C"+taxClassID;
          taxMatrix.put(keyValue,taxRate+"");
        }// end of while (it.hasNext())
      }// end of if (col != null)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception]: AccountHelperEJB.getTaxMartix: " + e.toString());
      e.printStackTrace();
    }  //end of catch block (Exception)
    finally
    {
      dl.destroy();
      dl = null;
    } //end of finally block
    return taxMatrix;
  }  //end of getTax method

  /**
    * We will set the Tax Matrix values
    *
    * @param taxMatrix Its a collection for the TaxMartixForm bean, Which will hold the information of the Tax like ClassID, JurisdictionID & taxRate
    * @return void
    */
  public void setTaxMatrix(ArrayList taxMatrix){
    CVDal dl = new CVDal(dataSource);
    try
    {
      for(int i=0; i<taxMatrix.size();i++){
        TaxMartixForm taxMartixForm = (TaxMartixForm) taxMatrix.get(i);
        int taxClassId = taxMartixForm.getTaxClassID();
        int taxJurisdictionId = taxMartixForm.getTaxJurisdictionID();
        float taxRate = taxMartixForm.getTaxRate();
        if(taxClassId != 0 && taxJurisdictionId != 0){
          this.updateTaxRate(taxClassId, taxJurisdictionId, taxRate,dl);
        }// end of if(taxClassId != 0 && taxJurisdictionId != 0)
      }// end of for(int i=0; i<taxMatrix.size();i++)
    } //end of try block
    catch (Exception e)
    {
      System.out.println("[Exception]: AccountHelperEJB.setTaxMatrix: " + e.toString());
      e.printStackTrace();
    }  //end of catch block (Exception)
    finally
    {
      dl.destroy();
      dl = null;
    } //end of finally block
  }// end of setTaxMatrix(ArrayList taxMatrix)

  /**
    *  We will update the Tax Matrix Table updating the Tax Rate for the Associated taxClass ID & taxJurisdiction ID.
    *
    * @param taxClassId The Tax Class ID of the Tax Class to update the Tax Rate.
    * @param taxJurisdictionId The Tax Jurisdiction ID of the tax jurisdiction to update the Tax Rate.
    * @param taxRate The tax rate of the taxMatrix table which defines the rate for the associated taxclass and taxJurisdiction.
    * @param dl An <strong>open</strong> database connection.
    * @return void
    */
  private void updateTaxRate(int taxClassId, int taxJurisdictionId, float taxRate,CVDal dl)
  {
    try{
      dl.setSqlQueryToNull();
      dl.clearParameters();
      dl.setSqlQuery("select taxrate from taxmatrix where taxclassid = ? and taxjurisdictionid = ?");
      dl.setInt(1,taxClassId);
      dl.setInt(2,taxJurisdictionId);

      Collection  col  = (Collection)dl.executeQuery();
      if (col != null && col.size() != 0)
      {
        dl.clearParameters();
         dl.setSqlQueryToNull();
        dl.setSqlQuery("update taxmatrix set taxrate =? where taxclassid = ? and taxjurisdictionid = ? ");
        dl.setFloat(1,taxRate);
        dl.setInt(2,taxClassId);
        dl.setInt(3,taxJurisdictionId);
        dl.executeUpdate();
      }//if if (col != null && col.size() != 0)
      else
      {
        dl.clearParameters();
        dl.setSqlQueryToNull();
        dl.setSqlQuery("insert into taxmatrix(taxclassid,taxjurisdictionid, taxrate) values (?,?,?)");
        dl.setInt(1,taxClassId);
        dl.setInt(2,taxJurisdictionId);
        dl.setFloat(3,taxRate);
        int taxRateInserted = dl.executeUpdate();
      }//else for if (col != null && col.size() != 0)

    }catch(Exception e)
    {
      System.out.println("[Exception]: AccountHelperEJB.updateTaxRate: " + e.toString());
      e.printStackTrace();
    }
  }//end of updateTaxRate(int taxClassId, int taxJurisdictionId, float taxRate,CVDal dl)

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical name of the datasource.
   */
  public void setDataSource(String ds) {
    this.dataSource = ds;
  }

  /**
  * This method will get the TaxClassID for the Item.
  *
  * @return integer  The taxClassID Associated to the Item.
  */
  public int getTaxClassID(int itemID)
  {
    int taxClassID = -1;
    CVDal dl = new CVDal(dataSource);
    try
    {
      dl.setSqlQueryToNull();
      dl.setSqlQuery("select taxclass from item where itemid = "+itemID);

      Collection  col  = (Collection)dl.executeQuery();
      if (col != null)
      {
        Iterator it = col.iterator();
        while (it.hasNext())
        {
          HashMap hm  = (HashMap)it.next();
          taxClassID  = ((Number)hm.get("taxclass")).intValue();
        }// end of while (it.hasNext())
      }// end of if (col != null)
    }// end of try block
    catch(Exception e)
    {
      System.out.println("[Exception][AccountHelperEJB.getTaxClassID()] Exception thrown: "+e);
      e.printStackTrace();
    }// end of catch block
    finally
    {
      dl.clearParameters();
      dl.destroy();
      dl = null;
    }// end of finally block
    return taxClassID;
  }// end of getTaxJurisdiction()

  /**
   * This method will get the parentItemID for the Item.
   *
   * @return integer  The parentItemID Associated to the Item.
   */
   public int getParentItemID(int itemID)
   {
     int parentItemID = 0;
     CVDal dl = new CVDal(dataSource);
     try
     {
       dl.setSqlQueryToNull();
       dl.setSqlQuery("select parent from item where itemid = "+itemID);

       Collection  col  = (Collection)dl.executeQuery();
       if (col != null)
       {
         Iterator it = col.iterator();
         while (it.hasNext())
         {
           HashMap hm  = (HashMap)it.next();
           parentItemID  = ((Number)hm.get("parent")).intValue();
         }// end of while (it.hasNext())
       }// end of if (col != null)
     }// end of try block
     catch(Exception e)
     {
       System.out.println("[Exception][AccountHelperEJB.getParentItemID()] Exception thrown: "+e);
       e.printStackTrace();
     }// end of catch block
     finally
     {
       dl.clearParameters();
       dl.destroy();
       dl = null;
     }// end of finally block
     return parentItemID;
   }// end of getParentItemID()
}
