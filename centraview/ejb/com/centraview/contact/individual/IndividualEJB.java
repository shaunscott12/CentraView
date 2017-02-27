/*
 * $RCSfile: IndividualEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:03 $ - $Author: mcallist $
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


package com.centraview.contact.individual;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.helper.CommonHelperLocal;
import com.centraview.common.helper.CommonHelperLocalHome;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.customfield.CustomFieldLocal;
import com.centraview.customfield.CustomFieldLocalHome;

public class IndividualEJB implements EntityBean
{
  public EntityContext EJB_Context;
  public Connection EJB_Connection = null;
  public DataSource EJB_Datasource = null;
  private IndividualVO indVo;
  private IndividualVO tempIndVo;
  private String dataSource = "";
  private static Logger logger = Logger.getLogger(IndividualEJB.class);

  /**
   * The operationIndividualId is the Individual id of the user attempting to
   * perform some operation on this EJB. The setOperationIndividualId(int) must
   * be called before a call to remove(), so the appropriate ID can be
   * propogated to any other referenced methods.
   * 
   * If you do not set it, it is defaulted to 0 so the operation will probably
   * fail.
   */
  private int operationIndividualId = 0;

  /**
   * Default value is false .Value changes from setVO method to true Value
   * changes from ejbStore method to false false = data is fresh (do not store)
   * true = data is changed (store)
   */
  private boolean isDirty;

  /*
   * This is for determining that the Basic Referances is filled in the
   * IndividualVO Object. false by default ie getIndividualVOWithBasicReferences
   * is not called true ie getIndividualVOWithBasicReferences is Called
   */
  private boolean basicReferanceFilled;

  public IndividualEJB() { }

  public IndividualPK ejbCreate(IndividualVO individualDetail, int userID, String ds) throws CreateException
  {
    // This method really should be refactored, so that all create Individual
    // calls
    // go to the ContactFacade and they do most of the other stuff
    // i.e. creating addresses, sources, and this method simply inserts the
    // indivdual
    // record.
    int individualID = 0;
    InitialContext ic = CVUtility.getInitialContext();
    CVDal dl = new CVDal(ds);
    try {
      int entityID = individualDetail.getEntityID();
      int listID = 1;
      if (entityID > 0) {
        String strSQL = "SELECT List FROM entity WHERE EntityID=" + entityID;
        dl.setSqlQuery(strSQL);
        Collection col = dl.executeQuery();
        if (col != null) {
          Iterator it = col.iterator();
          while (it.hasNext()) {
            HashMap hm = (HashMap)it.next();
            listID = ((Number)hm.get("List")).intValue();
          }// end of while( it.hasNext() )
        }// end of if(col != null){
      }//end of if(entityID > 0)

      dl.setSqlQueryToNull();

      dl.setSql("contact.createindividual");
      dl.setInt(1, individualDetail.getEntityID());
      dl.setString(2, individualDetail.getFirstName());
      dl.setString(3, individualDetail.getLastName());
      dl.setString(4, individualDetail.getTitle());
      dl.setInt(5, userID);
      dl.setString(6, individualDetail.getIsPrimaryContact());
      dl.setString(7, individualDetail.getMiddleName());
      int sourceID = individualDetail.getSource();
      // If the sourceId < 1 then it was hand entered instead of
      // looked up. So we either have to find the right ID, or
      // create a new one and provide that Id.
      if (sourceID < 1) {
        String sourceName = individualDetail.getSourceName();
        // Make sure we actually have a sourceName.
        if (sourceName != null && !sourceName.equals("")) {
          sourceID = 0; // just in case we fail
          try {
            CommonHelperLocalHome helperHome = (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
            CommonHelperLocal helperRemote = helperHome.create();
            // Give us an Id.
            sourceID = helperRemote.getSourceID(sourceName);
          } catch (Exception e) {
            logger.error("[ejbCreate] Exception thrown Adding new Source while Creating Individual", e);
          }
        } // end if (sourceName != null && !sourceName.equals(""))
      } // end if (individualDetail.getSource() > 0)
      dl.setInt(8, sourceID);
      dl.setString(9, individualDetail.getExternalID());
      dl.setInt(10, listID);
      dl.setInt(11, userID);
      dl.executeUpdate();
      individualID = dl.getAutoGeneratedKey();

      //The call to this method is from various places instead of ContactFacade
      //so its wise to implement the insert method for history record here.

      HashMap historyInfo = new HashMap();
      historyInfo.put("recordID", new Integer(individualID));
      historyInfo.put("recordTypeID", new Integer(Constants.IndividualModuleID));
      historyInfo.put("operation", new Integer(Constants.CREATED));
      historyInfo.put("individualID", new Integer(userID));
      historyInfo.put("referenceActivityID", new Integer(0));
      historyInfo.put("recordName", individualDetail.getFirstName() + " " + individualDetail.getMiddleName() + " " + individualDetail.getLastName());
      CVUtility.addHistoryRecord(historyInfo, ds);

      // Create the address.
      AddressVO addVO = individualDetail.getPrimaryAddress();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote = home.create();
      remote.setDataSource(ds);
      // if there is no address filled in, automatically fill in the address
      // from the associated entity.
      if ((addVO.getCity() == null || addVO.getCity().equals("")) &&
          (addVO.getCountryName() == null || addVO.getCountryName().equals("")) && 
          (addVO.getStateName() == null || addVO.getStateName().equals("")) && 
          (addVO.getStreet1() == null || addVO.getStreet1().equals("")) && 
          (addVO.getStreet2() == null || addVO.getStreet2().equals("")) && 
          (addVO.getZip() == null || addVO.getZip().equals("")))
      {
        // This of course creates a copy of the address from the entity, therefore
        // it is possible to be changed independently of Entities, however it will
        // not be in sync with changes to the entities.
        addVO = remote.getPrimaryAddressForContact(userID, individualDetail.getEntityID(), 1);
        addVO.setIsPrimary("YES");
      }


      // We are passing the Extra boolean value to the method. if the boolean
      // value is false then we wil not update the individual table
      remote.addAddress(addVO, individualID, individualDetail.getContactType(), userID, false);


      // Getting MethodOfContactVO List from Individual Object
      Collection mocList = individualDetail.getMOC();
      Iterator it = mocList.iterator();
      int counter = 0;
      MethodOfContactVO mocVO = null;
      while (it.hasNext()) {
        mocVO = (MethodOfContactVO)it.next();
        // Count all the Mobile and Phone type MOCs that were manually added.
        // If there were none, try to inheirit copies of them from the parent
        // entity.
        if (mocVO.getIsPrimary().equals("YES")) {
          counter++;
        }

        // We are passing the Extra boolean value to the method. if the boolean
        // value is false then we wil not update the individual table
        remote.addMOC(mocVO, individualID, 2, userID, false); // Hardcoded for
                                                              // Individual
                                                              // ContactType
      }

      // if counter < 1 then there were no phone or mobile numbers entered for
      // this individual. So we should try and suck down copies of the Entities.
      if (counter < 1) {
        Collection col = remote.getAllMOCForContact(userID, individualDetail.getEntityID(), 1);
        if (col != null) {
          Iterator mocIter = col.iterator();
          while (mocIter.hasNext()) {
            mocVO = (MethodOfContactVO)mocIter.next();
            // we ussed to only copy "Main" and "Fax" types, but per Alan's
            // request this has been changed. Please see bug #88. I have
            // changed this to copy ALL MOC's.
            if (mocVO.getIsPrimary().equals("YES")) {
              remote.addMOC(mocVO, individualID, 2, userID, false);   // false means don't update the modified timestamp of individual record
            }
          }
        }
      }

      // Save CustomFields from the Form
      Vector custfieldVec = individualDetail.getCustomField();
      CustomFieldLocalHome custHome = (CustomFieldLocalHome)ic.lookup("local/CustomField");
      CustomFieldLocal custRemote = custHome.create();
      custRemote.setDataSource(ds);
      for (int i = 0; i < custfieldVec.size(); i++) {
        CustomFieldVO custFieldVO = (CustomFieldVO)custfieldVec.get(i);
        custFieldVO.setRecordID(individualID);
        custRemote.addCustomField(custFieldVO);
      }
      // Everything is created now apply the default permissions for this user.
      AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
      AuthorizationLocal authorizationLocal = authorizationHome.create();
      authorizationLocal.setDataSource(ds);
      authorizationLocal.saveCurrentDefaultPermission("Individual", individualID, userID);
    } catch (Exception e) {
      logger.error("[ejbCreate] Exception thrown.", e);
    } finally {
      dl.destroy();
    }
    return new IndividualPK(individualID, ds);
  } // end ejbCreate() method

  public void ejbPostCreate(IndividualVO individualDEtail, int userID, String ds) throws CreateException
  {
    ejbLoad();
  }

  public void ejbActivate()
  {}

  public void ejbPassivate()
  {}

  public void ejbLoad()
  {
    IndividualPK indPK = (IndividualPK)(EJB_Context.getPrimaryKey());
    // If we got created using one of the finder methods we need to make sure
    // to set the DataSource ourselves. And the key dataSource must match
    // the EJB dataSource, or we're in a world of hurt.
    this.setDataSource(indPK.getDataSource());

    HashMap hm = getBasic(indPK);

    if (hm == null) {
      return;
    } else {
      setBasicVO(hm);
    }
  } // end ejbLoad() method

  /**
   * In this method Data is Updated into the DataBase in the "individual" table
   * Before Calling this method the setIndividualVO() is to called
   */
  public void ejbStore()
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();

      if (isDirty) {
        CVDal dl = new CVDal(dataSource);
        dl.setSql("contact.updateindividual");
        dl.setString(1, indVo.getFirstName());
        dl.setString(2, indVo.getLastName());
        dl.setString(3, indVo.getExternalID());
        dl.setString(4, indVo.getTitle());
        dl.setInt(5, indVo.getModifiedBy());
        dl.setString(6, indVo.getIsPrimaryContact());
        dl.setString(7, indVo.getMiddleName());

        int sourceID = 0;
        if (indVo.getSource() > 0) {
          sourceID = indVo.getSource();
        } else {
          String sourceName = indVo.getSourceName();
          if (sourceName != null && !sourceName.equals("")) {
            try {
              CommonHelperLocalHome helperHome = (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
              CommonHelperLocal helperRemote = helperHome.create();
              sourceID = helperRemote.getSourceID(sourceName);
            } catch (Exception e) {
              System.out.println("[Exception][IndividualEJB.ejbCreate] Exception Thrown: " + e);
              e.printStackTrace();
            }
          } // end if (sourceName != null && !sourceName.equals(""))
        } // end if (indVo.getSource() > 0)

        dl.setInt(8, sourceID);
        dl.setInt(9, indVo.getContactID());
        dl.executeUpdate();
        dl.clearParameters();

        // if an entityID was passed in the IndividualVO, then
        // we have to link this individual with the new entity
        int newEntityID = indVo.getEntityID();
        if (newEntityID != 0) {

          dl.setSqlQuery("SELECT i.IndividualID, i.PrimaryContact FROM entity e " + " LEFT OUTER JOIN individual i ON (i.Entity = e.EntityID) " + " WHERE e.EntityID=? AND (i.PrimaryContact='YES' OR i.PrimaryContact='') ORDER BY 2 DESC;");
          dl.setInt(1, newEntityID);
          Collection primaryContactcol = dl.executeQuery();
          dl.clearParameters();
          boolean isPrimaryFlag = false;
          int count = 0;
          ArrayList individualIDList = new ArrayList();
          if (primaryContactcol != null && primaryContactcol.size() != 0) {
            Iterator it = primaryContactcol.iterator();
            while (it.hasNext()) {
              HashMap hm = (HashMap)it.next();
              int individualID = ((Number)hm.get("IndividualID")).intValue();
              individualIDList.add(String.valueOf(individualID));
              String primaryContact = (String)hm.get("PrimaryContact");
              count++;
              // If we get count of yes more than one times
              // then we have to update the other individual to NO
              // because we can't have more than one primary Contact to Set as
              // "YES"
              // One more Case if we have Primary contact "" then set it to "NO"

              if (primaryContact != null && primaryContact.equals("YES")) {
                isPrimaryFlag = true;
              }//end of if(primaryContact != null &&
               // primaryContact.equals("YES"))
              if ((primaryContact != null && primaryContact.equals("")) || count > 1) {
                dl.setSqlQueryToNull();
                dl.setSqlQuery("UPDATE individual SET PrimaryContact= 'NO' where IndividualID=?");
                dl.setInt(1, individualID);
                dl.executeUpdate();
                dl.clearParameters();
              }//end of if ((primaryContact != null &&
               // primaryContact.equals("")) || count > 1)

            }//end of while( it.hasNext() )
          }//end of if(primaryContactcol != null && primaryContactcol.size() !=
           // 0)
          int individualId = indVo.getContactID();

          if (individualIDList == null || !individualIDList.contains(String.valueOf(individualId))) {
            String primaryContact = "YES";
            if (isPrimaryFlag) {
              primaryContact = "NO";
            }//end of if(isPrimaryFlag)
            dl.setSql("contact.individual.linktoentity");
            dl.setInt(1, newEntityID);
            dl.setString(2, primaryContact);
            dl.setInt(3, individualId);
            dl.executeUpdate();
            dl.clearParameters();
          }
        }

        // update CustomFields
        Vector custfieldVec = this.indVo.getCustomField();
        CustomFieldLocalHome custHome = (CustomFieldLocalHome)ic.lookup("local/CustomField");
        CustomFieldLocal custRemote = custHome.create();
        custRemote.setDataSource(this.dataSource);

        boolean customFieldAdd = false;

        dl.setSql("contact.checkindividualcustomfield");
        //ALLSQL.put("contact.checkindividualcustomfield"," select recordid
        // from customfieldmultiple where recordid = ? union select recordid
        // from customfieldscalar where recordid = ? ");
        dl.setInt(1, indVo.getContactID());
        dl.setInt(2, indVo.getContactID());

        Collection col = dl.executeQuery();
        Iterator it = col.iterator();
        if (!it.hasNext()) {
          customFieldAdd = true;
        }

        dl.clearParameters();
        dl.destroy();

        for (int i = 0; i < custfieldVec.size(); i++) {
          CustomFieldVO custFieldVO = (CustomFieldVO)custfieldVec.get(i);
          custFieldVO.setRecordID(indVo.getContactID());

          if (customFieldAdd) {
            custRemote.addCustomField(custFieldVO);
          } else {
            custRemote.updateCustomField(custFieldVO);
          }
        }

        ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
        ContactHelperLocal remote = home.create();
        remote.setDataSource(this.dataSource);

        int cntType = this.indVo.getContactType();
        AddressVO av = this.indVo.getPrimaryAddress();
        if (av != null) {
          if (av.getAddressID() == 0) {
            av.setIsPrimary("YES");
            //We are passing the Extra boolean value to the method. if the
            // boolean value is false then we wil not update the individual
            // table
            remote.addAddress(av, indVo.getContactID(), 2, 1, false);
          } else {
            //We are passing the Extra boolean value to the method. if the
            // boolean value is false then we wil not update the individual
            // table
            remote.updateAddress(this.indVo.getPrimaryAddress(), indVo.getContactID(), indVo.getModifiedBy(), false);
          }
        }
        Iterator mi = this.indVo.getMOC().iterator();
        while (mi.hasNext()) {
          MethodOfContactVO mocObj = (MethodOfContactVO)mi.next();
          int mocID = mocObj.getMocID();
          if (mocObj.isAdded()) {
            //We are passing the Extra boolean value to the method. if the
            // boolean value is false then we wil not update the individual
            // table
            remote.addMOC(mocObj, indVo.getContactID(), cntType, indVo.getModifiedBy(), false);
          } else if (mocObj.isUpdated()) {
            // We are passing the Extra boolean value to the method. if the
            // boolean value is false then we wil not update the individual
            // table
            remote.updateMOC(mocObj, indVo.getContactID(), indVo.getModifiedBy(), false);
          } else if (mocObj.isDelete()) {
            // We are passing the Extra boolean value to the method. if the
            // boolean value is false then we wil not update the individual
            // table
            remote.deleteMOC(mocID, indVo.getContactID(), indVo.getModifiedBy(), false);
          }
        }

        //this is to fill all the system fields like created on and modified on
        // date
        //cause on update the bean stays in memory and hence this fields are
        // shown null
        // cause the new VO does not contain this felds
        ejbLoad();
        isDirty = false;
      } // end if (isDirty)
    } catch (Exception e) {
      logger.error("[Exception] IndividualEJB.ejbStore: ", e);
    }
  } // end ejbStore() method

  /*
   * In this methos Individual is Deleted from following Table 1)Methodofcontact
   * 2)MOCRelate 3)Address 4)AddressRelate 5)Individual
   */
  public void ejbRemove() throws RemoveException
  {
    CVDal dl = new CVDal(dataSource);

    try {
      // if individual is user then do not delete
      boolean isUser = false;
      dl.setSql("contact.checkindividualasUser");
      //ALLSQL.put("contact.checkindividualasUser"," SELECT individualid FROM
      // user where individualid = ? ");
      dl.setInt(1, this.indVo.getContactID());

      Collection col = dl.executeQuery();
      Iterator it = col.iterator();
      if (it.hasNext()) {
        isUser = true;
      }
      dl.clearParameters();

      if (!isUser) {
        InitialContext ic = CVUtility.getInitialContext();
        ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
        ContactHelperLocal remote = home.create();
        remote.setDataSource(this.dataSource);

        if (!basicReferanceFilled) {
          getIndividualVOWithBasicReferences();
        }

        // delete Address
        AddressVO addVO = this.indVo.getPrimaryAddress();
        if (addVO != null) {
          //We are passing the Extra boolean value to the method. if the
          // boolean value is false then we wil not update the individual table
          remote.deleteAddress(addVO.getAddressID(), this.indVo.getContactID(), this.operationIndividualId, false);
        }

        // All Method od Contact is to deleted.
        Iterator mi = this.indVo.getMOC().iterator();
        while (mi.hasNext()) {
          MethodOfContactVO mocVO = (MethodOfContactVO)mi.next();
          // We are passing the Extra boolean value to the method. if the
          // boolean value is false then we wil not update the individual table
          remote.deleteMOC(mocVO.getMocID(), this.indVo.getContactID(), this.operationIndividualId, false);
        }
        //ALLSQL.put("contact.deleteindividual", "delete from individual where
        // individualID = ? ");

        //Delete Custom Field
        //ALLSQL.put("contact.deletecustomfieldscalar", "delete
        // customfieldscalar from customfieldscalar inner join customfield inner
        // join cvtable on(customfield.recordtype = cvtable.tableid and
        // cvtable.name = ? and customfield.fieldtype = 'SCALAR' ) on
        // (customfieldscalar .customfieldid = customfield.customfieldid) where
        // customfieldscalar.recordid = ? ");
        dl.setSql("contact.deletecustomfieldscalar");
        dl.setString(1, "Individual");
        dl.setInt(2, this.indVo.getContactID());
        dl.executeUpdate();
        dl.clearParameters();

        //ALLSQL.put("contact.deletecustomfieldmultiple", "delete
        // customfieldmultiple from customfieldmultiple inner join customfield
        // inner join cvtable on(customfield.recordtype = cvtable.tableid and
        // cvtable.name = ? and customfield.fieldtype = 'MULTIPLE') on
        // (customfieldmultiple.customfieldid = customfield.customfieldid) where
        // customfieldmultiple.recordid = ? ");
        dl.setSql("contact.deletecustomfieldmultiple");
        dl.setString(1, "Individual");
        dl.setInt(2, this.indVo.getContactID());
        dl.executeUpdate();
        dl.clearParameters();

        //ALLSQL.put("contact.deletegroupmemberindividual", "delete from member
        // where ChildID = ? ");
        dl.setSql("contact.deletegroupmemberindividual");
        dl.setInt(1, this.indVo.getContactID());
        dl.executeUpdate();
        dl.clearParameters();

        dl.setSql("contact.deleteindividual");
        dl.setInt(1, this.indVo.getContactID());
        dl.executeUpdate();
        dl.clearParameters();
      } else { // end if (!isUser)
        throw new UserLoginAssociationException();
      }
    } catch (Exception e) {
      if (e instanceof UserLoginAssociationException) {
        throw new UserLoginAssociationException();
      }

      logger.error("[Exception] IndividualEJB.ejbRemove: ", e);
      //e.printStackTrace();
    } finally {
      dl.destroy();
      dl = null;
    }
  } // end ejbRemove() method

  public void setEntityContext(EntityContext EJB_Context)
  {
    this.EJB_Context = EJB_Context;
  }

  public void unsetEntityContext()
  {
    this.EJB_Context = null;
  }

  public IndividualPK ejbFindByPrimaryKey(IndividualPK primaryKey) throws FinderException
  {
    HashMap hm = getBasic(primaryKey);
    if (hm == null) {
      throw new FinderException("Could not find Individual: " + primaryKey);
    } else {
      return new IndividualPK(primaryKey.getId(), primaryKey.getDataSource());
    }
  } // end ejbFindByPrimaryKey()

  /*
   * This method is for Finding the PrimaryContact for Entity by passing the
   * entityID Search condition are ....... 1) entity = entityID Parameter 2)
   * PrimaryContact = 'YES'
   */
  public IndividualPK ejbFindByPrimaryContact(int entityID, String ds) throws FinderException
  {
    CVDal dl = new CVDal(ds);
    //ALLSQL.put("contact.getindividualforprimarycontact", "select
    // individualID, Entity from individual where Entity = ? and PrimaryContact
    // = 'YES' ");
    dl.setSql("contact.getindividualforprimarycontact");
    dl.setInt(1, entityID);
    Collection col = dl.executeQuery();
    dl.destroy();

    Iterator it = col.iterator();
    if (!it.hasNext()) {
      throw new FinderException("Primary Contact not found for this entityID: " + entityID + " dataSource: " + ds);
    } else {
      HashMap hm = (HashMap)it.next();
      Long tmp = ((Long)(hm.get("individualID")));
      return new IndividualPK(tmp.intValue(), ds);
    }
  } // end findByPrimaryContact() method

  public IndividualVO getIndividualVO()
  {
    // TODO: figure out why IndividualEJB.getIndividualVO() returns null
    // always!!!
    return null;
  }

  public IndividualVO getIndividualVOBasic()
  {
    return this.indVo;
  }

  public IndividualVO getIndividualVOWithBasicReferences()
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);

      // Getting AddressVO from Contact Helper Ejb //
      // (userID,contactID,contactType
      // TODO: Hardcoded UserId to be removed Later (use
      // this.operationIndividualID)
      AddressVO addVO = remote.getPrimaryAddressForContact(1, this.indVo.getContactID(), this.indVo.getContactType());
      this.indVo.setPrimaryAddress(addVO);

      if (indVo.getModifiedBy() != 0) {
        String individualName = remote.getIndividualName(this.indVo.getModifiedBy());
        if (individualName != null && individualName.length() > 20) {
          individualName = individualName.substring(0, 20);
          individualName = individualName + "&#8230;";
        }
        this.indVo.setModifiedByName(individualName);
      } // end if (indVo.getModifiedBy() != 0)

      if (indVo.getCreatedBy() != 0) {
        String individualName = remote.getIndividualName(this.indVo.getCreatedBy());
        if (individualName != null && individualName.length() > 20) {
          individualName = individualName.substring(0, 20);
          individualName = individualName + "&#8230;";
        }
        this.indVo.setCreatedByName(individualName);
      } // end if (indVo.getCreatedBy() != 0)

      //Getting AddressVO from Contact Helper Ejb //
      // (userID,contactID,contactType
      //Collection mocColl =
      // remote.getPrimaryMOCForContact(1,this.indVo.getContactID(),this.indVo.getContactType());
      Collection mocColl = remote.getAllMOCForContact(1, this.indVo.getContactID(), this.indVo.getContactType());

      if (mocColl != null) {
        Iterator it = mocColl.iterator();
        this.indVo.clearMOC();
        while (it.hasNext()) {
          this.indVo.setMOC((MethodOfContactVO)it.next());
        } //end of while loop (it.hasNext())
      } //end of if statement (mocColl != null)
      this.basicReferanceFilled = true;
    } catch (Exception e) {
      logger.error("[Exception] IndividualEJB.getIndividualVOWithBasicReferences: ", e);
    }
    return this.indVo;
  } // end getIndividualVOWithBasicReferences() method

  public void unsetAsPrimaryContact()
  {}

  public void setAsPrimaryContact()
  {}

  public void setIndividualVO(IndividualVO indvidualDetail)
  {
    isDirty = true;
    this.tempIndVo = indVo;
    this.indVo = indvidualDetail;
  }

  public void authorizeUser(int userId, String password)
  {}

  /**
   * In this method get basic Individual data from Individual Table.
   */
  private HashMap getBasic(IndividualPK primaryKey)
  {
    CVDal dl = new CVDal(primaryKey.getDataSource());
    //ALLSQL.put("contact.getindividual", "select individualID,
    // Entity,FirstName, LastName, Title,Created, Modified, CreatedBy,
    // ModifiedBy ,PrimaryContact from individual where IndividualID = ?");
    dl.setSql("contact.getindividual");
    dl.setInt(1, primaryKey.getId());
    Collection col = dl.executeQuery();
    dl.destroy();

    Iterator it = col.iterator();
    if (!it.hasNext()) {
      return null; //throw excepiton here
    } else {
      return (HashMap)it.next();
    }
  } // end getBasic() method

  /**
   * In this method getting Value from HashMap and Populate the Individual value
   * Object.
   */
  private void setBasicVO(HashMap hm)
  {
    this.indVo = new IndividualVO();

    // See how this "tmp" variable is used?
    // This is VERY BAD FORM!!! Do not ever do this!!!
    Long tmp;

    tmp = ((Long)(hm.get("individualID")));
    if (tmp != null) {
      this.indVo.setContactID(tmp.intValue());
    }

    tmp = ((Long)(hm.get("Entity")));
    if (tmp != null) {
      this.indVo.setEntityID(tmp.intValue());
    }

    tmp = (Long)(hm.get("Owner"));
    if (tmp != null) {
      this.indVo.setOwnerID(tmp.intValue());
    }

    this.indVo.setFirstName((String)(hm.get("FirstName")));
    this.indVo.setLastName((String)(hm.get("LastName")));
    this.indVo.setMiddleName((String)(hm.get("MiddleInitial")));
    this.indVo.setTitle((String)(hm.get("Title")));
    this.indVo.setExternalID((String)(hm.get("ExternalID")));
    this.indVo.setList(((Number)(hm.get("list"))).intValue());
    this.indVo.setEntityName((String)(hm.get("EntityName")));

    Object sourceTmp = hm.get("source");

    if (sourceTmp != null) {
      tmp = (Long)sourceTmp;
      this.indVo.setSource(tmp.intValue());
    } else {
      this.indVo.setSource(0);
    }

    this.indVo.setCreatedOn(((Date)(hm.get("Created"))));
    this.indVo.setModifiedOn((Date)(hm.get("Modified")));

    tmp = ((Long)(hm.get("ModifiedBy")));
    if (tmp != null) {
      this.indVo.setModifiedBy(tmp.intValue());
    }

    tmp = ((Long)(hm.get("CreatedBy")));
    if (tmp != null) {
      this.indVo.setCreatedBy(tmp.intValue());
    }

    this.indVo.setIsPrimaryContact((String)(hm.get("PrimaryContact")));
    this.indVo.setContactType(2); //Change it to CONSTANT VALUE

    // Set the username for this individual
    CVDal dl = new CVDal(dataSource);
    try {
      //ALLSQL.put("contact.getindividualusername", "SELECT name FROM user
      // where individualid = ?");
      dl.setSql("contact.getindividualusername");
      dl.setInt(1, this.indVo.getContactID());

      Collection col = dl.executeQuery();
      dl.destroy();
      Iterator it = col.iterator();

      if (it.hasNext()) {
        HashMap hmUser = (HashMap)it.next();
        this.indVo.setUserName((String)(hmUser.get("name")));
      }
    } catch (Exception e) {
      logger.error("[Exception] IndividualEJB.setBasicVO: ", e);
    } finally {
      dl.destroy();
      dl = null;
    }
  } // end setBasicVO() method

  /**
   * used by Customer View
   */
  public HashMap getCustomerIndividualVOWithBasicReferences()
  {
    HashMap hm = new HashMap();
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);

      // TODO: HARDCODED USER ID!!! (use this.operationIndividualID)
      Vector addVOs = remote.getCustomerPrimaryAddressForContact(1, this.indVo.getContactID(), this.indVo.getContactType());
      //this.indVo.setPrimaryAddress(addVO);

      if (indVo.getModifiedBy() != 0) {
        String individualName = remote.getIndividualName(this.indVo.getModifiedBy());
        if (individualName != null && individualName.length() > 20) {
          individualName = individualName.substring(0, 20);
          individualName = individualName + "&#8230;";
        }
        this.indVo.setModifiedByName(individualName);
      } // end if (indVo.getModifiedBy() != 0)

      if (indVo.getCreatedBy() != 0) {
        String individualName = remote.getIndividualName(this.indVo.getCreatedBy());
        if (individualName != null && individualName.length() > 20) {
          individualName = individualName.substring(0, 20);
          individualName = individualName + "&#8230;";
        }
        this.indVo.setCreatedByName(individualName);
      } // end if (indVo.getCreatedBy() != 0)

      IndividualLocalHome ih = (IndividualLocalHome)ic.lookup("local/Individual");

      try {
        IndividualLocal il = ih.findByPrimaryKey(new IndividualPK(this.indVo.getContactID(), this.dataSource));
        IndividualVO iv = il.getIndividualVOBasic();
        hm.put("addVOs", addVOs);
        hm.put("IndividualVO", iv);
      } catch (Exception e) {
        logger.error("[Exception]: Find By Primary Key Exception in Individual", e);
      }

      // Getting AddressVO from Contact Helper Ejb //
      // (userID,contactID,contactType
      //Collection mocColl =
      // remote.getPrimaryMOCForContact(1,this.indVo.getContactID(),this.indVo.getContactType());
      Collection mocColl = remote.getAllMOCForContact(1, this.indVo.getContactID(), this.indVo.getContactType());

      if (mocColl != null) {
        Iterator it = mocColl.iterator();
        this.indVo.clearMOC();
        while (it.hasNext()) {
          this.indVo.setMOC((MethodOfContactVO)it.next());
        }
      }
      this.basicReferanceFilled = true;
    } catch (Exception e) {
      logger.error(" Exception in getCustomer IndividualVOBasicReferences  ", e);
      //e.printStackTrace();
    }
    return hm;
  } // end getCustomerIndividualVOWithBasicReferences() method

  /**
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   * @author Kevin McAllister <kevin@centraview.com>
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }

  /**
   * This method is used to set the Indvidual Id of the user who will be performing
   * a task such as deleting an individual so the ejbRemove() method can pass
   * the appropriate value on to an called methods.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param i
   */
  public void setOperationIndividualId(int i)
  {
    operationIndividualId = i;
  }
} // end class defintion
