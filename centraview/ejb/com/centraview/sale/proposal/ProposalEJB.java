/*
 * $RCSfile: ProposalEJB.java,v $    $Revision: 1.3 $  $Date: 2005/10/17 17:11:43 $ - $Author: mcallist $
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

package com.centraview.sale.proposal;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.account.helper.AccountHelperLocal;
import com.centraview.account.helper.AccountHelperLocalHome;
import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.DDNameValue;
import com.centraview.common.DateUtility;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.file.CvFileFacade;
import com.centraview.sale.opportunity.OpportunityLocal;
import com.centraview.sale.opportunity.OpportunityLocalHome;

/**
 */
public class ProposalEJB implements SessionBean {
  private static Logger logger = Logger.getLogger(ProposalEJB.class);
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private DecimalFormat currencyFormat = new DecimalFormat("#0.00");
  private String dataSource = "MySqlDS";

  public void setSessionContext(SessionContext ctx) throws RemoteException
  {
    this.ctx = ctx;
  }

  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  public void ejbRemove()
  {}

  public void ejbCreate()
  {}

  public int addProposal(int individualID, ProposalListForm proposalForm)
      throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Proposals", individualID, this.dataSource)) {
      throw new AuthorizationFailedException("Proposals - addProposal");
    }

    int gid = 0;
    CVDal dl = new CVDal(dataSource);
    try {
      dl
          .setSqlQuery("insert into  proposal(OpportunityID, Title, Description, Owner, TypeID, Status, Stage, ForecastAmmount, ActualAmount, Probability, Source, AccountManager, AccountTeam, CreatedBy,  ModifiedBy, Created, Modified, EstimatedCloseDate, ActualCloseDate, Billing, Shipping, Billingid, Shippingid, Instructions,orderIsGenerated,termid,individualid) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, concat(CURRENT_DATE,CURRENT_TIME), concat(CURRENT_DATE,CURRENT_TIME), ?, ?, ?, ?, ?, ?, ?,?,?,?)");

      dl.setInt(1, Integer.parseInt(proposalForm.getOpportunityid()));
      dl.setString(2, proposalForm.getProposal());
      dl.setString(3, proposalForm.getProdescription());
      dl.setInt(4, individualID);
      dl.setInt(14, individualID);
      dl.setInt(15, individualID);
      dl.setInt(5, Integer.parseInt(proposalForm.getTypeid()));// typeid
      dl.setInt(6, Integer.parseInt(proposalForm.getStatuslist()));// status
      dl.setInt(7, Integer.parseInt(proposalForm.getStage()));
      dl.setDouble(8, 0.00);// forecastAmount
      dl.setDouble(9, 0.00);// actualAmount
      dl.setInt(10, Integer.parseInt(proposalForm.getProbability()));
      dl.setInt(11, 0);// source
      dl.setInt(12, 0);// acountManger
      dl.setInt(13, 0);// acountteam

      String ecYear = proposalForm.getEcyear();
      String ecMonth = proposalForm.getEcmon();
      String ecDay = proposalForm.getEcday();
      if (ecYear.length() != 0 && ecMonth.length() != 0 && ecDay.length() != 0) {
        Timestamp estimatedClose = DateUtility.createTimestamp(ecYear, ecMonth, ecDay);
        dl.setTimestamp(16, estimatedClose);
      } else {
        dl.setString(16, "");
      }
      
      String acYear = proposalForm.getAcyear();
      String acMonth = proposalForm.getAcmon();
      String acDay = proposalForm.getAcday();
      if (acYear.length() != 0 && acMonth.length() != 0 && acDay.length() != 0) {
        Timestamp actualClose = DateUtility.createTimestamp(acYear, acMonth, acDay);
        dl.setTimestamp(17, actualClose);
      } else {
        dl.setString(17, "");
      }
      dl.setString(18, proposalForm.getBillingaddress());
      dl.setString(19, proposalForm.getShippingaddress());
      String sTemp1 = proposalForm.getBillingaddressid();
      if (sTemp1 != null && (sTemp1 == "0" || sTemp1.length() != 0)) {
        dl.setInt(20, Integer.parseInt(proposalForm.getBillingaddressid()));
      } else {
        dl.setInt(20, 0);
      }
      String sTemp2 = proposalForm.getShippingaddressid();
      if (sTemp2 != null && (sTemp2 == "0" || sTemp2.length() != 0)) {
        dl.setInt(21, Integer.parseInt(proposalForm.getShippingaddressid()));
      } else {
        dl.setInt(21, 0);
      }

      dl.setString(22, proposalForm.getSpecialinstructions());
      if (proposalForm.getOrderIsGenerated())
        dl.setString(23, "YES");
      else
        dl.setString(23, "NO");

      dl.setString(24, proposalForm.getTerms());// Terms
      dl.setInt(25, Integer.parseInt(proposalForm.getIndividualid()));
      dl.executeUpdate();
      gid = dl.getAutoGeneratedKey();

      int taxJurisdictionId = proposalForm.getJurisdictionID();

      ItemLines itemLines = proposalForm.getItemLines();
      java.util.Set listkey = itemLines.keySet();
      java.util.Iterator it = listkey.iterator();
      while (it.hasNext()) {
        Object str = it.next();
        ItemElement ele = (ItemElement)itemLines.get(str);
        String status = ele.getLineStatus();
        if (status.equals("New") || status.equals("")) {
          this.addProposalItem(ele, gid, taxJurisdictionId);
        }
      }// end of while

      dl.setSqlQueryToNull();
      CvFileFacade cvf = new CvFileFacade();
      String[] values = proposalForm.getAttachFileIds();
      if (values != null) {
        for (int i = 0; i < values.length; i++) {
          cvf.commitEmailAttachment(individualID, Integer.parseInt(values[i]), this.dataSource);
          dl.setSqlQuery("insert into proposallink(proposalid,recordtypeid,recordid) values ("
              + gid + ",33," + values[i] + ")");
          dl.executeUpdate();
        }
      }

      // end of added by sandeepj for adding attachFileIds into ProposalLink
      // table
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic
          .lookup("local/Authorization");
      AuthorizationLocal authorizationLocal = authorizationHome.create();
      authorizationLocal.setDataSource(dataSource);
      authorizationLocal.saveCurrentDefaultPermission("Proposals", gid, individualID);
    } catch (Exception e) {
      logger.error("[addProposal]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return gid;
  }

  // This function return proposals probability....
  public int getProbabilityPercent(int userId, int probability)
  {
    // if(!CVUtility.canPerformRecordOperation(userId,"Proposals", probability,
    // ModuleFieldRightMatrix.VIEW_RIGHT))
    // throw new AuthorizationFailedException("Proposals -
    // getProbabilityPercent");

    CVDal dl = new CVDal(dataSource);
    int iProb = 0;
    try {
      dl.setSqlQuery("select Probability from salesprobability where probabilityid = ?");
      dl.setInt(1, probability);
      java.util.Collection colOne = dl.executeQuery();
      // String sTitle = null;
      if (colOne != null) {
        java.util.Iterator it = colOne.iterator();
        if (it.hasNext()) {
          java.util.HashMap hm = (java.util.HashMap)it.next();
          iProb = Integer.parseInt((hm.get("Probability")).toString());
        } // end of if statement (it.hasNext())
      }// end of if
      // iProb = Integer.parseInt(sTitle.substring(0,sTitle.indexOf("%")-1));
    } catch (Exception e) {
      logger.error("[getProbabilityPercent]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return iProb;
  }

  // modified by sandeepw for adding functionality to delete forecast amount
  // from related opportunity....
  public boolean deleteProposal(int proposalID)
  {
    CVDal cvdal = new CVDal(dataSource);
    try {
      deleteForecastFromOpportunity(1, proposalID);

      cvdal.setSql("sale.deleteassociatedproposallinks");
      cvdal.setInt(1, proposalID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      cvdal.setSql("sale.deleteproposalitems");
      cvdal.setInt(1, proposalID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      cvdal.setSql("sale.deleteproposal");
      cvdal.setInt(1, proposalID);
      cvdal.executeUpdate();
    } catch (Exception exception) {
      logger.error("[deleteProposal]: Exception", exception);
      return false;
    } finally {
      cvdal.destroy();
    }
    return true;
  }

  public boolean updateProposal(int userId, ProposalListForm proposalForm)
      throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(userId, "Proposals", Integer.parseInt(proposalForm
        .getProposalid()), ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException("Proposals - updateProposal");
    }

    boolean returnValue = false;
    CVDal dl = new CVDal(dataSource);

    try {
      int proposalID = Integer.parseInt(proposalForm.getProposalid());
      boolean oldIncludeFlag = false;
      boolean newIncludeFlag = false;

      // get the old "Include in Forcast flag"
      dl.setSqlQuery("select IncludeForcastSale from proposal where ProposalID = ?");
      dl.setInt(1, proposalID);

      Collection resultsCollection = dl.executeQuery();

      if (resultsCollection != null) {
        Iterator resultsIterator = resultsCollection.iterator();

        if (resultsIterator.hasNext()) {
          HashMap hashMap = (HashMap)resultsIterator.next();
          String includeString = (String)hashMap.get("IncludeForcastSale");
          oldIncludeFlag = includeString.equalsIgnoreCase("YES") ? true : false;
        } // end of while loop (resultsIterator.hasNext())
      } // end of if statement (resultsCollection != null)

      dl
          .setSqlQuery("update  proposal set OpportunityID = ?, Title = ?, Description=?, "
              + "individualid= ?, TypeID =?, Status = ?, Stage=?  ,  ForecastAmmount= ?,  ActualAmount=?,  "
              + "Probability= ?,  Source=?,  AccountManager= ? , AccountTeam=? ,  CreatedBy= ?, "
              + "ModifiedBy=?, EstimatedCloseDate=? ,  ActualCloseDate= ? , Billing=?  , shipping=?, "
              + "Billingid=? , shippingid=? , Instructions=? , IncludeForcastSale=?, orderIsGenerated=? , Modified = concat(CURRENT_DATE,CURRENT_TIME), termid = ? where ProposalID = ?");

      dl.setInt(1, Integer.parseInt(proposalForm.getOpportunityid()));
      dl.setString(2, proposalForm.getProposal());
      dl.setString(3, proposalForm.getProdescription());
      dl.setInt(4, Integer.parseInt(proposalForm.getIndividualid()));
      dl.setInt(5, Integer.parseInt(proposalForm.getTypeid()));// typeid
      dl.setInt(6, Integer.parseInt(proposalForm.getStatuslist()));// status
      dl.setInt(7, Integer.parseInt(proposalForm.getStage()));
      dl.setDouble(9, 0.00);// actualAmount
      dl.setInt(10, Integer.parseInt(proposalForm.getProbability()));
      dl.setInt(11, 1);// source
      dl.setInt(12, 1);// acountManger
      dl.setInt(13, 1);// acountteam
      dl.setInt(14, Integer.parseInt(proposalForm.getIndividualid()));
      dl.setInt(15, Integer.parseInt(proposalForm.getIndividualid()));

      String ecYear = proposalForm.getEcyear();
      String ecMonth = proposalForm.getEcmon();
      String ecDay = proposalForm.getEcday();
      if (ecYear.length() != 0 && ecMonth.length() != 0 && ecDay.length() != 0) {
        Timestamp estimatedClose = DateUtility.createTimestamp(ecYear, ecMonth, ecDay);
        dl.setTimestamp(16, estimatedClose);
      } else {
        dl.setString(16, "");
      }
      
      String acYear = proposalForm.getAcyear();
      String acMonth = proposalForm.getAcmon();
      String acDay = proposalForm.getAcday();
      if (acYear.length() != 0 && acMonth.length() != 0 && acDay.length() != 0) {
        Timestamp actualClose = DateUtility.createTimestamp(acYear, acMonth, acDay);
        dl.setTimestamp(17, actualClose);
      } else {
        dl.setString(17, "");
      }
      dl.setString(18, proposalForm.getBillingaddress());
      dl.setString(19, proposalForm.getShippingaddress());
      dl.setInt(20, Integer.parseInt(proposalForm.getBillingaddressid()));
      dl.setInt(21, Integer.parseInt(proposalForm.getShippingaddressid()));
      dl.setString(22, proposalForm.getSpecialinstructions());
      String orderGenerated = proposalForm.getOrderIsGenerated() ? "YES" : "NO";
      dl.setString(24, orderGenerated);
      dl.setString(25, proposalForm.getTerms());
      dl.setInt(26, Integer.parseInt(proposalForm.getProposalid()));

      ItemLines itemLines = proposalForm.getItemLines();
      java.util.Set listkey = itemLines.keySet();
      java.util.Iterator it = listkey.iterator();

      int taxJurisdictionId = proposalForm.getJurisdictionID();

      float fTax = 0;
      float fSubTotal = 0;
      float fOrderTotal = 0;
      String status = "";
      while (it.hasNext()) {
        Object str = it.next();
        ItemElement ele = (ItemElement)itemLines.get(str);
        status = ele.getLineStatus();
        if (status.equals("New") || status.equals("")) {
          if (ele.get("TaxAmount") != null) {
            fTax += ((Float)((FloatMember)ele.get("TaxAmount")).getMemberValue()).floatValue();
          }
          float priceEach = ((Float)((FloatMember)ele.get("Price")).getMemberValue()).floatValue();
          int qty = ((Integer)((IntMember)ele.get("Quantity")).getMemberValue()).intValue();
          fSubTotal += (priceEach * qty);
          addProposalItem(ele, proposalID, taxJurisdictionId);
        } else if (status.equals("Deleted")) {
          IntMember lineid = (IntMember)ele.get("LineId");
          markDeleatedProposalItem(status, ((Integer)lineid.getMemberValue()).intValue(),
              proposalID);
        } else if (status.equals("Active")) {
          if (ele.get("TaxAmount") != null) {
            fTax += ((Float)((FloatMember)ele.get("TaxAmount")).getMemberValue()).floatValue();
          }
          float priceEach = ((Float)((FloatMember)ele.get("Price")).getMemberValue()).floatValue();
          int qty = ((Integer)((IntMember)ele.get("Quantity")).getMemberValue()).intValue();
          fSubTotal += (priceEach * qty);
          updateProposalItem(ele, proposalID, taxJurisdictionId);
        }
      }// end of while

      fOrderTotal = Float.parseFloat(currencyFormat.format(new Float(fSubTotal + fTax)));
      int iProbability = Integer.parseInt(proposalForm.getProbability());
      int iProb = getProbabilityPercent(userId, iProbability);

      float fForeCastAmt = Float.parseFloat(currencyFormat.format(new Float(
          (fOrderTotal * iProb) / 100)));
      dl.setFloat(8, fForeCastAmt);

      dl.setString(23, proposalForm.getForecastinc());
      newIncludeFlag = (proposalForm.getForecastinc()).equalsIgnoreCase("YES") ? true : false;

      dl.executeUpdate();
      dl.setSqlQueryToNull();

      // Update the Opportunity to reflect any changes.
      if ((oldIncludeFlag != newIncludeFlag) || newIncludeFlag) {
        // get EJB to Opportunity
        int opportunityID = Integer.parseInt(proposalForm.getOpportunityid());
        InitialContext ic = CVUtility.getInitialContext();
        OpportunityLocalHome home = (OpportunityLocalHome)ic.lookup("local/Opportunity");
        OpportunityLocal local = home.create();
        local.setDataSource(this.dataSource);
        float opportunityTotal = local.updateTotalAmount(opportunityID);
        local.updateForcastAmount(opportunityTotal, opportunityID);
        // do the update
      }
      CvFileFacade cvf = new CvFileFacade();
      // while updating for attached file ids, only commiting them as
      // isTemporary=NO is to be done.
      // deleting records for 'whatever user has removed from previous list'
      // files from CVFILE table and DISK
      // and Adding new records for 'whatever user has adde into list' Will be
      // Done at attachmentHandler and removeHandler
      // So when control comes here, only 'files to be kept' list will be ther
      // in array as well as in CVFILE records as well as on DISK
      dl.setSqlQuery("delete from proposallink where proposalid = "
          + proposalForm.getProposalid());
      dl.executeUpdate();
      String[] values = proposalForm.getAttachFileIds();
      if (values != null) {

        // 33 is RecordTypeId for CVFile table and it is hardcoded.
        // Later it may be taken from 'CVTable' table.
        for (int i = 0; i < values.length; i++) {
          StringTokenizer stk = new StringTokenizer(values[i], "#");
          String fileIDStr = stk.nextToken();
          if (fileIDStr != null) {
            cvf.commitEmailAttachment(userId, Integer.parseInt(fileIDStr), this.dataSource);
            dl.setSqlQuery("insert into proposallink(proposalid,recordtypeid,recordid) values ("
                + proposalForm.getProposalid() + ",33," + fileIDStr + ")");
            dl.executeUpdate();
          }
        }
      }
      returnValue = true;
    } catch (Exception e) {
      logger.error("[updateProposal]: Exception", e);
    } finally {
      // close the database connections
      dl.destroy();
      dl = null;
    } // end of finally block
    return returnValue;
  } // end of updateProposal method

  // Delete forecast Amount(Proposal) from Opportunity for proposal.
  public void deleteForecastFromOpportunity(int userId, int proposalid)
  {
    CVDal dl = new CVDal(dataSource);
    int iOpportunityID = 0;
    int iProb = 0;
    int iOpporProb = 0;
    float iOpporTotal = 0;
    float iPreviousAmount = 0;
    try {
      dl.setSqlQuery("select OpportunityID,PreviousForecastAmount from proposal where proposalid = ?");
      dl.setInt(1, proposalid);
      java.util.Collection col = dl.executeQuery();
      dl.setSqlQueryToNull();
      if (col != null) {
        java.util.Iterator it = col.iterator();
        int count = 1;
        while (it.hasNext()) {
          java.util.HashMap hm = (java.util.HashMap)it.next();

          iOpportunityID = Integer.parseInt((hm.get("OpportunityID")).toString());
          iPreviousAmount = Float.parseFloat((hm.get("PreviousForecastAmount")).toString());

          count++;
        }
      }// end of if

      dl.setSqlQuery("select actualamount,probability from opportunity where opportunityid= ?");
      dl.setInt(1, iOpportunityID);
      java.util.Collection colTwo = dl.executeQuery();
      dl.setSqlQueryToNull();
      if (colTwo != null) {
        java.util.Iterator it = colTwo.iterator();
        int count = 1;
        while (it.hasNext()) {
          java.util.HashMap hm = (java.util.HashMap)it.next();
          iOpporProb = Integer.parseInt((hm.get("probability")).toString());
          iOpporTotal = Float.parseFloat((hm.get("actualamount")).toString());
          count++;
        }
      }// end of if

      if (iPreviousAmount != 0) {

        dl.setSqlQuery("update opportunity set ActualAmount = ? where opportunityid =?");
        dl.setFloat(1, (iOpporTotal - iPreviousAmount));
        dl.setInt(2, iOpportunityID);
        dl.executeUpdate();
        dl.setSqlQueryToNull();

        float iModifiedActualAmount = 0;
        dl.setSqlQuery("select actualamount from opportunity where opportunityid= ?");
        dl.setInt(1, iOpportunityID);
        java.util.Collection colFour = dl.executeQuery();
        dl.setSqlQueryToNull();
        if (colFour != null) {
          java.util.Iterator it = colFour.iterator();
          int count = 1;
          while (it.hasNext()) {
            java.util.HashMap hm = (java.util.HashMap)it.next();
            iModifiedActualAmount = Float.parseFloat((hm.get("actualamount")).toString());
            count++;
          }
        }// end of if
        iProb = getProbabilityPercent(userId, iOpporProb);
        dl.setSqlQuery("update opportunity set ForecastAmmount =? where opportunityid =?");
        dl.setFloat(1, ((iModifiedActualAmount * iProb) / 100));
        dl.setInt(2, iOpportunityID);
        dl.executeUpdate();
      }
    } catch (Exception e) {
      logger.error("[deleteForecastFromOpportunity]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
  }

  public java.util.HashMap viewProposal(int individualID, int listId,
      ProposalListForm proposallistform) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("Proposals", individualID, this.dataSource))
      throw new AuthorizationFailedException("Proposals - viewProposal");

    java.util.HashMap hm_return = new java.util.HashMap(3);
    CVDal dl = new CVDal(dataSource);

    try {
      // dl.setSqlQuery( "SELECT * FROM proposal where ProposalID = ?" );
      dl
          .setSqlQuery("SELECT p.OpportunityID, o.title oTitle, p.Title, p.individualid , p.Description, p.Owner, concat(i.firstname, '  ', i.lastname ) individualname, p.TypeID,"
              + " p.Status, p.Stage, p.Probability, p.Created, p.Modified, p.EstimatedCloseDate, p.Shippingid, concat(add2.street1,' ',add2.street2,',',add2.state,' ', add2.zip)  Shipping, p.Billingid, concat(add1.street1,' ',add1.street2,',',add1.state,' ', add1.zip)  Billing,add1.jurisdictionID, p.Instructions, "
              + " p.IncludeForcastSale, p.ForecastAmmount, p.ActualCloseDate, p.OrderIsGenerated,p.orderID ,p.termid FROM proposal p "
              + " left outer join  address add1  on p.Billingid = add1.addressid  "
              + " left outer join  address add2  on p.Shippingid = add2.addressid  "
              + "left outer join individual i on i.individualid = p.individualid left outer join opportunity o on o.opportunityid =  p.opportunityid where p.proposalid = ?");
      dl.setInt(1, listId);

      java.util.Collection col = dl.executeQuery();

      if (col != null) {
        java.util.HashMap hm = (java.util.HashMap)col.iterator().next();
        proposallistform.setProposalid("" + listId);
        // added by sandeepj
        if (hm.get("OrderIsGenerated") == null || ((String)hm.get("OrderIsGenerated")).equals("NO")) {
          proposallistform.setOrderIsGenerated(false);
        } else {
          proposallistform.setOrderIsGenerated(true);
        }
        // added by sandeepj end
        proposallistform.setOpportunityid((String)hm.get("OpportunityID"));
        proposallistform.setOpportunity((String)hm.get("oTitle"));
        proposallistform.setProposal((String)hm.get("Title"));

        proposallistform.setProdescription((String)hm.get("Description"));
        proposallistform.setIndividualid((String)hm.get("individualid"));

        if (hm.get("individualname") != null) {
          proposallistform.setIndividual(hm.get("individualname").toString());
        } // end of if statement (hm.get("individualname") != null)
        else {
          proposallistform.setIndividual("");
        } // end of else statement (hm.get("individualname") != null)

        proposallistform.setTypeid((String)hm.get("TypeID"));
        proposallistform.setStatuslist((String)hm.get("Status"));
        proposallistform.setStage((String)hm.get("Stage"));
        // forcast amount
        // actual amount
        proposallistform.setProbability((String)hm.get("Probability"));

        // Changed DATE format By Shilpa
        String dateFormat = "M/d/yyyy - h:mm a";

        String timeZone = "EST";
        GregorianCalendar gCal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
        SimpleDateFormat dForm = new SimpleDateFormat(dateFormat);
        dForm.setCalendar(gCal);

        Timestamp ts = (Timestamp)hm.get("Created");

        String createdDt = dForm.format(ts);

        ts = (Timestamp)hm.get("Modified");

        String modifiedDt = dForm.format(ts);
        // By Shilpa ends here

        proposallistform.setCreatedDate(createdDt);
        proposallistform.setModifyDate(modifiedDt);
        if (hm.get("Billing") != null) {
          proposallistform.setBillingaddress(hm.get("Billing").toString());
        } else {
          proposallistform.setBillingaddress("");
        }

        if (hm.get("Shipping") != null) {
          proposallistform.setShippingaddress(hm.get("Shipping").toString());
        } else {
          proposallistform.setShippingaddress("");
        }
        proposallistform.setBillingaddressid(hm.get("Billingid").toString());
        proposallistform.setShippingaddressid(hm.get("Shippingid").toString());
        int jurisdictionID = 0;
        if (hm.get("jurisdictionID") != null) {
          jurisdictionID = ((Number)hm.get("jurisdictionID")).intValue();
        }
        proposallistform.setJurisdictionID(jurisdictionID);

        int iShippingId = Integer.parseInt(hm.get("Shippingid").toString());
        proposallistform.setSpecialinstructions(hm.get("Instructions").toString());
        proposallistform.setForecastinc(hm.get("IncludeForcastSale").toString());
        proposallistform.setForcastAmount(currencyFormat.format(hm.get("ForecastAmmount")));
        proposallistform.setTerms((hm.get("termid")).toString());

        int orderID = ((Number)hm.get("orderID")).intValue();

        proposallistform.setOrderID(orderID);

        Timestamp ec = (Timestamp)hm.get("EstimatedCloseDate");
        Calendar estimatedClose = new GregorianCalendar();
        estimatedClose.setTimeInMillis(ec.getTime());
        Timestamp ac = (Timestamp)hm.get("ActualCloseDate");
        Calendar actualClose = new GregorianCalendar();
        actualClose.setTimeInMillis(ac.getTime());

        if (ec != null) {
          proposallistform.setEcday(String.valueOf(estimatedClose.get(Calendar.DATE)));
          proposallistform.setEcmon(String.valueOf(estimatedClose.get(Calendar.MONTH)+1));
          proposallistform.setEcyear(String.valueOf(estimatedClose.get(Calendar.YEAR)));
        } else {
          proposallistform.setEcday("");
          proposallistform.setEcmon("");
          proposallistform.setEcyear("");
        } // end of if statement (ec != null)

        if (ac != null) {
          proposallistform.setAcday(String.valueOf(actualClose.get(Calendar.DATE)));
          proposallistform.setAcmon(String.valueOf(actualClose.get(Calendar.MONTH)+1));
          proposallistform.setAcyear(String.valueOf(actualClose.get(Calendar.YEAR)));
        } else {
          proposallistform.setAcday("");
          proposallistform.setAcmon("");
          proposallistform.setAcyear("");
        } // end of else statement (ac != null)
        ItemLines itemLines = setInvoiceItemData(listId, iShippingId);
        hm_return.put("itemLines", itemLines);
      }
      dl.setSqlQueryToNull();
      dl.setSqlQuery("select recordid from proposallink where proposalid = "
          + proposallistform.getProposalid());
      col = null;
      col = dl.executeQuery();
      String[] values = new String[col.size()];
      String query = "select title,fileid from cvfile where fileid in (";
      if (col != null) {
        int i = 0;
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          values[i] = ((Long)hm.get("recordid")).toString();
          query += values[i] + " ,";
          i++;
        }
      }
      proposallistform.setAttachFileIds(values);
      Vector pairs = new Vector();
      if (values.length > 0) // if there are files then load filenames
      {
        query = query.substring(0, query.length() - 2); // cut the last comma ,
        query = query + ")"; // complete the bracket for in (.. values in query
        dl.setSqlQuery(query);
        col = dl.executeQuery();
        if (col != null) {
          int i = 0;
          Iterator it = col.iterator();
          while (it.hasNext()) {
            HashMap hm = (HashMap)it.next();
            String tempfileID = hm.get("fileid") + "#" + hm.get("title");
            pairs.add(new DDNameValue(tempfileID, (String)hm.get("title")));
            i++;
          }
        }
      } // else query String is decarded no need to get anydata as there are no
        // fileids
      proposallistform.setAttachFileValues(pairs);
      hm_return.put("dyna", proposallistform);
    } catch (Exception e) {
      logger.error("[viewProposal]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    } // end of finally block
    return hm_return;
  } // end of viewProposal method

  /*
   * Used to deleteProposal @param proposalid int @return int
   */
  public int deleteProposal(int userId, int proposalID) throws AuthorizationFailedException
  {

    if (!CVUtility.canPerformRecordOperation(userId, "Proposals", proposalID,
        ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource))
      throw new AuthorizationFailedException("Proposal - deleteProposal");

    int result = 0;
    deleteForecastFromOpportunity(userId, proposalID);

    CVDal cvdal = new CVDal(dataSource);
    try {
      cvdal.setSql("sale.deleteassociatedproposallinks");
      cvdal.setInt(1, proposalID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      cvdal.setSql("sale.deleteproposalitems");
      cvdal.setInt(1, proposalID);
      cvdal.executeUpdate();
      cvdal.setSqlQueryToNull();

      cvdal.setSql("sale.deleteproposal");
      cvdal.setInt(1, proposalID);
      cvdal.executeUpdate();
    } catch (Exception exe) {
      logger.error("[deleteProposal]: Exception", exe);
      result = 1;
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return result;
  }

  // Helper method

  private void addProposalItem(ItemElement ele, int propsalID, int taxJurisdictionId)
  {
    CVDal dl = new CVDal(dataSource);
    try {

      IntMember itemid = (IntMember)ele.get("ItemId");
      StringMember sku = (StringMember)ele.get("SKU");
      IntMember qty = (IntMember)ele.get("Quantity");
      FloatMember priceEach = (FloatMember)ele.get("Price");
      StringMember desc = (StringMember)ele.get("Description");

      int quantity = ((Number)qty.getMemberValue()).intValue();
      float price = ((Number)priceEach.getMemberValue()).floatValue();
      int itemID = ((Number)itemid.getMemberValue()).intValue();

      InitialContext ic = CVUtility.getInitialContext();
      AccountHelperLocalHome home = (AccountHelperLocalHome)ic.lookup("local/AccountHelper");
      AccountHelperLocal remote = home.create();
      int taxClassID = remote.getTaxClassID(itemID);
      float taxRate = 0;
      if (taxClassID != -1 && taxJurisdictionId != 0) {
        taxRate = remote.getTax(taxClassID, taxJurisdictionId);
      }// if (taxJurisdictionId !=0)

      float taxTotal = ((quantity * price) * (taxRate / 100));

      dl.setSqlQuery("insert into proposalitem (ProposalID , ItemID , Quantity ,Price ,SKU, Description,taxAmount)  values(?, ?, ?, ?, ?, ?, ?)");
      dl.setInt(1, propsalID);
      dl.setInt(2, itemID);
      dl.setInt(3, quantity);
      dl.setFloat(4, ((Float)priceEach.getMemberValue()).floatValue());
      dl.setString(5, (String)sku.getMemberValue());
      dl.setString(6, (String)desc.getMemberValue());
      dl.setFloat(7, taxTotal);
      dl.executeUpdate();
    } catch (Exception e) {
      logger.error("[addProposalItem]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
  }// end of

  private ItemLines setInvoiceItemData(int proposalid, int shippingid)
  {
    CVDal dl = new CVDal(dataSource);
    ItemLines itemLines = new ItemLines();
    try {
      dl.setSqlQuery("select ProposalLineId, ItemId ItemID, Quantity, Price, Description, SKU, status,taxAmount from proposalitem where ProposalId=? and status != 'Deleted'");
      dl.setInt(1, proposalid);
      Collection col = dl.executeQuery();
      if (col != null && col.size() != 0) {
        Iterator it = col.iterator();
        int count = 1;
        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          int lineID = ((Number)hm.get("ProposalLineId")).intValue();
          int itemID = ((Number)hm.get("ItemID")).intValue();
          int quantity = ((Number)hm.get("Quantity")).intValue();
          float price = ((Number)hm.get("Price")).floatValue();
          String sku = (String)hm.get("SKU");
          String description = (String)hm.get("Description");
          float taxTotal = ((Number)hm.get("taxAmount")).floatValue();
          String status = (String)hm.get("status");
          float priceExtended = quantity * price;

          IntMember LineId = new IntMember("LineId", lineID, 'D', "", 'T', false, 20);
          IntMember ItemId = new IntMember("ItemId", itemID, 'D', "", 'T', false, 20);
          IntMember Quantity = new IntMember("Quantity", quantity, 'D', "", 'T', false, 20);
          FloatMember PriceEach = new FloatMember("Price", new Float(price), 'D', "", 'T', false,
              20);
          StringMember SKU = new StringMember("SKU", sku, 'D', "", 'T', false);
          StringMember Description = new StringMember("Description", description, 'D', "", 'T',
              false);
          FloatMember PriceExtended = new FloatMember("PriceExtended", new Float(priceExtended),
              'D', "", 'T', false, 20);
          FloatMember TaxAmount = new FloatMember("TaxAmount", new Float(taxTotal), 'D', "", 'T',
              false, 20);

          ItemElement ie = new ItemElement();
          ie.put("LineId", LineId);
          ie.put("ItemId", ItemId);
          ie.put("Quantity", Quantity);
          ie.put("Price", PriceEach);
          ie.put("SKU", SKU);
          ie.put("Description", Description);
          ie.put("PriceExtended", PriceExtended);
          ie.put("TaxAmount", TaxAmount);
          ie.setLineStatus(status);
          itemLines.put("" + count, ie);
          count++;
        }
      }// end of if
    } catch (Exception e) {
      logger.error("[setInvoiceItemData]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
    return itemLines;
  }// end of setOrderItemData

  private void updateProposalItem(ItemElement ele, int proposalID, int taxJurisdictionId)
  {
    CVDal dl = new CVDal(dataSource);
    try {

      IntMember lineid = (IntMember)ele.get("LineId");
      IntMember itemid = (IntMember)ele.get("ItemId");
      StringMember sku = (StringMember)ele.get("SKU");
      IntMember qty = (IntMember)ele.get("Quantity");
      FloatMember priceEach = (FloatMember)ele.get("Price");
      StringMember desc = (StringMember)ele.get("Description");

      int quantity = ((Number)qty.getMemberValue()).intValue();
      float price = ((Number)priceEach.getMemberValue()).floatValue();
      int itemID = ((Number)itemid.getMemberValue()).intValue();

      InitialContext ic = CVUtility.getInitialContext();
      AccountHelperLocalHome home = (AccountHelperLocalHome)ic.lookup("local/AccountHelper");
      AccountHelperLocal remote = home.create();
      int taxClassID = remote.getTaxClassID(itemID);
      float taxRate = 0;
      if (taxClassID != -1 && taxJurisdictionId != 0) {
        taxRate = remote.getTax(taxClassID, taxJurisdictionId);
      }// if (taxJurisdictionId !=0)

      float taxTotal = ((quantity * price) * (taxRate / 100));

      dl.setSqlQuery("update proposalitem set ItemID =?, Description =?, Quantity=?, Price=?, SKU =?, taxAmount=? where ProposalID = ? and  ProposalLineID=?");
      dl.setInt(1, itemID);
      dl.setString(2, (String)desc.getMemberValue());
      dl.setInt(3, quantity);
      dl.setFloat(4, price);
      dl.setString(5, (String)sku.getMemberValue());
      dl.setFloat(6, taxTotal);
      dl.setInt(7, proposalID);
      dl.setInt(8, ((Integer)lineid.getMemberValue()).intValue());
      dl.executeUpdate();
    } catch (Exception e) {
      logger.error("[updateProposalItem]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    } // end of finally block
  }// end of updateProposalItem method

  // commit the status Active to Deleted
  private void markDeleatedProposalItem(String status, int lineId, int proposalID)
  {
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSqlQuery("update proposalitem set status =? where ProposalLineID =? and ProposalID =?");
      dl.setString(1, status);
      dl.setInt(2, lineId);
      dl.setInt(3, proposalID);
      dl.executeUpdate();
    } catch (Exception e) {
      logger.error("[markDeleatedProposalItem]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
  }// end of markDeleatedInvoiceItem

  public void setOrderIsGenerated(boolean orderIsGenerated, int proposalID, int orderID)
  {
    CVDal dl = new CVDal(dataSource);
    String flag = null;
    if (orderIsGenerated)
      flag = "YES";
    else
      flag = "NO";
    try {
      dl.setSqlQuery("update proposal set orderIsGenerated = '" + flag + "' , orderID = " + orderID
          + " where proposalID = " + proposalID);
      dl.executeUpdate();
    } catch (Exception e) {
      logger.error("[setOrderIsGenerated]: Exception", e);
    } finally {
      dl.destroy();
      dl = null;
    }
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com> This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
   * This method returns Proposal Name Of the Proposal
   * @param ProposalID The ProposalID to collect the Proposal Title
   * @return ProposalName The ProposalName
   */
  public String getProposalName(int ProposalID)
  {
    String proposalName = "";
    CVDal dl = new CVDal(dataSource);
    try {
      String proposalQuery = "select ProposalID, Title  from proposal where ProposalID = ?";
      dl.setSqlQuery(proposalQuery);
      dl.setInt(1, ProposalID);
      Collection col = dl.executeQuery();

      if (col != null) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
          HashMap hm = (HashMap)it.next();
          proposalName = (String)hm.get("Title");
        }// end of while (it.hasNext())
      }// end of if (col != null)
    } catch (Exception e) {
      logger.error("[getProposalName]: Exception", e);
    } // end of catch block (Exception)
    finally {
      dl.destroy();
      dl = null;
    } // end of finally block
    return proposalName;
  }
}
