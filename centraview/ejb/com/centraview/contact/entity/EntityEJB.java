/*
 * $RCSfile: EntityEJB.java,v $    $Revision: 1.3 $  $Date: 2005/08/04 14:56:47 $ - $Author: mcallist $
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

package com.centraview.contact.entity;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.helper.CommonHelperLocal;
import com.centraview.common.helper.CommonHelperLocalHome;
import com.centraview.contact.group.GroupLocal;
import com.centraview.contact.group.GroupLocalHome;
import com.centraview.contact.group.GroupVO;
import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactHelperLocal;
import com.centraview.contact.helper.ContactHelperLocalHome;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualLocal;
import com.centraview.contact.individual.IndividualLocalHome;
import com.centraview.contact.individual.IndividualPK;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.customfield.CustomFieldLocal;
import com.centraview.customfield.CustomFieldLocalHome;

/**
 * Handles all operations related to an Entity in centraview
 * Uses ContactHelper for Address,MOC and customfields.
 * USes Individual for Primary contace, accManager ets
 * uses Group for Account Team
 */
public class EntityEJB implements EntityBean
{
  private static Logger logger = Logger.getLogger(EntityEJB.class);
  public EntityContext EJB_Context;
  private EntityVO envo; //set from ejbLoad and getEntityVOxxx()
  private EntityVO oldEntityVO;  //will be set with original vo in case of update (setEntityVO())
  private boolean basicReferencesFilled = false;  //set to true if getEntityVOWithBasicReferences Called
  private boolean isDirty = false;  //update data if isDirty is true (ejbStore)
  private String dataSource = "MySqlDS";

  /**
 	 * Gets all the basic and related data for an Entity.
	 * see list below:
	 * All Data from Entity table (name,id,modifiedby,owner,created by etc.)
	 * Primary Address (AddressVO)
	 * Primary Contact (IndividualVO)
	 * MOC
	 * Custom Fields
   */
  public EntityVO getEntityVOWithBasicReferences()
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote = home.create();
      remote.setDataSource(this.dataSource);

      int cntType = 1;
      int cntId = this.envo.getContactID();

      // TODO: hardocoded userid!
      AddressVO adr = remote.getPrimaryAddressForContact(1, cntId, cntType); //user ID HardCoded
      this.envo.setPrimaryAddress(adr);

      // TODO: hardocoded userid!
      Collection cl = remote.getAllMOCForContact(1, cntId, cntType);
      this.envo.clearMOC();

      Iterator it = cl.iterator();
      while (it.hasNext()) {
        this.envo.setMOC((MethodOfContactVO)it.next());
      }

      if (envo.getModifiedBy() != 0) {
        try {
          String individualName = remote.getIndividualName(this.envo.getModifiedBy());
          if (individualName != null && individualName.length() > 20) {
            individualName = individualName.substring(0,20);
            individualName = individualName +"&#8230;";
          }
          this.envo.setModifiedByName(individualName);
        }catch(Exception e){
          logger.error("[getEntityVOWithBasicReferences] Exception thrown.", e);
        } //end of catch block (Exception)
      } // end if (envo.getModifiedBy() != 0)

      if (envo.getCreatedBy() != 0) {
        try {
          String individualName = remote.getIndividualName(this.envo.getCreatedBy());
          if (individualName != null && individualName.length() > 20) {
            individualName = individualName.substring(0,20);
            individualName = individualName +"&#8230;";
          }
          this.envo.setCreatedByName(individualName);
        }catch(Exception e){
          logger.error("[getEntityVOWithBasicReferences] Exception thrown.", e);
        } //end of catch block (Exception)
      } // end if (envo.getCreatedBy() != 0)

      IndividualLocalHome ih = (IndividualLocalHome)ic.lookup("local/Individual");

      try {
        IndividualLocal il = ih.findByPrimaryContact(cntId, this.dataSource);
        IndividualVO iv = il.getIndividualVOBasic();
        this.envo.setIndividualVO(iv);
      }catch(FinderException e){
        IndividualVO iv = new IndividualVO();
        iv.setEntityID(this.envo.getContactID());
        this.envo.setIndividualVO(iv);
      }

      if (this.envo.getAccManager() != 0) {
        try {
          IndividualLocal ilAccMang = ih.findByPrimaryKey(new IndividualPK(envo.getAccManager(), this.dataSource));
          IndividualVO indAccMang = 	ilAccMang.getIndividualVOBasic();
          this.envo.setAcctMgrName(indAccMang.getFirstName()+" "+indAccMang.getLastName());
        }catch(Exception e){
          logger.error("[getEntityVOWithBasicReferences] Exception thrown.", e);
          this.envo.setAcctMgrName("");
        }
      } else {
        this.envo.setAcctMgrName("");
      }

      //This is for getting group Detail
      if (this.envo.getAccTeam() != 0) {
        try {
          GroupLocalHome groupHome = (GroupLocalHome)ic.lookup("local/Group");
          GroupLocal groupRemote = groupHome.create();
          groupRemote.setDataSource(this.dataSource);
          GroupVO groupVO = groupRemote.getGroupDetails(1,this.envo.getAccTeam());
          this.envo.setAcctTeamName(groupVO.getGroupName());
        }catch(Exception e){
          logger.error("[getEntityVOWithBasicReferences] Exception thrown.", e);
          this.envo.setAcctTeamName("");
        }
      } else {
        this.envo.setAcctTeamName("");
      }
      this.basicReferencesFilled = true;
    } catch(Exception e) {
      logger.error("[getEntityVOWithBasicReferences] Exception thrown.", e);
    }
    return this.envo;
  } // end getEntityVOWithBasicReferences() method

  /**
   *  Gets all data from Entity table only.
   *  see list below:
   *		Name,EntityId,ModifiedBy.CreatedBy,Owner,
   *		Created/Modified date, Source, ExternalId, DBase, List
   */
  public EntityVO getEntityVOBasic()
  {
    return this.envo;
  }

  /**
   * Finds if a given Entity exits in the database
   * @param	int	Entity ID
   * @return EntityPK class (EJB clients get Remote)
   */
  public EntityPK ejbFindByPrimaryKey(EntityPK primaryKey) throws FinderException
  {
    HashMap hm = getBasic(primaryKey);
    if (hm == null) {
      throw new FinderException("Could not find Entity: " + primaryKey);
    }
    return new EntityPK(primaryKey.getId(), primaryKey.getDataSource());
  } // end ejbFindByPrimaryKey() method

  /**
   * EJB Container callback method
   * Sets the this.envo with fresh only basic Entity data
   */
  public void ejbLoad()
  {
    EntityPK epk = (EntityPK)(EJB_Context.getPrimaryKey());
    // If we got created using one of the finder methods we need to make sure
    // to set the DataSource ourselves.  And the key dataSource must match
    // the EJB dataSource, or we're in a world of hurt.
    this.setDataSource(epk.getDataSource());
    HashMap hm = getBasic(epk);
    if (hm == null) {
      return;
    }
    setBasicVO(hm);
  } // end ejbLoad() method

  /**
   * Constructor
   */
  public EntityEJB()
  {}

  /**
   * EJB Container callback method which creates a new Entity record, and all
   * associated addresses, methods of contact, etc.
   * @param entityVO A fully populated EntityVO object representing the new
   *          entity record.
   * @param	individualID The individualID of the user performing the task.
   */
  public EntityPK ejbCreate(EntityVO entityVO, int individualID, String dataSource)
  {
    int newEntityID;

    InitialContext ic = CVUtility.getInitialContext();

    CVDal dl = new CVDal(dataSource);
    dl.setSql("contact.addentity");
    dl.setString(1, entityVO.getExternalID());
    dl.setString(2, entityVO.getName());

    int sourceID = 0;
    if (entityVO.getSource() > 0) {
      sourceID = entityVO.getSource();
    }else{
      String sourceName = entityVO.getSourceName();
      if (sourceName != null && !sourceName.equals("")) {
        try {
          CommonHelperLocalHome helperHome = (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
          CommonHelperLocal helperRemote =  helperHome.create();
          sourceID = helperRemote.getSourceID(sourceName);
        }catch(Exception e){
          logger.error("[ejbCreate] Exception thrown.", e);
        }
      }   // end if (sourceName != null && !sourceName.equals(""))
    }   // end if (entityVO.getSource() > 0)

    dl.setInt(3, sourceID);
    dl.setInt(4, entityVO.getDBase());
    dl.setInt(5, entityVO.getList());
    dl.setInt(6, individualID); //creator
    dl.setInt(7, individualID); //owner

    // account manager
    if (entityVO.getAccManager() != 0) {
      dl.setInt(8, entityVO.getAccManager());
    }else{
      dl.setInt(8, individualID);
    }

    dl.setInt(9, entityVO.getAccTeam());
    dl.executeUpdate();

    newEntityID = dl.getAutoGeneratedKey();

    try {
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(dataSource);

      int contactType = entityVO.getContactType();

      if (entityVO.getPrimaryAddress() != null) {
        // The boolean value false tells the ContactHelperEJB to NOT
        // update the 'entity.Modified' field, since we are already
        // updating that record at this time.
        remote.addAddress(entityVO.getPrimaryAddress(), newEntityID, contactType, individualID, false);
      }

      Iterator mocIter = entityVO.getMOC().iterator();
      while (mocIter.hasNext()) {
        // The boolean value false tells the ContactHelperEJB to NOT
        // update the 'entity.Modified' field, since we are already
        // updating that record at this time.
        remote.addMOC((MethodOfContactVO)(mocIter.next()), newEntityID, contactType, individualID, false);
      }

      IndividualVO individualVO = entityVO.getIndividualVO();
      if (individualVO != null && individualVO.getContactID() <= 0) {
        IndividualLocalHome ih = (IndividualLocalHome)ic.lookup("local/Individual");
        individualVO.setEntityID(newEntityID);
        ih.create(individualVO, individualID, dataSource);
      }
      // Save CustomFields from the Form (which is only the 3 on the form)
      CustomFieldLocalHome custHome = (CustomFieldLocalHome)ic.lookup("local/CustomField");
      CustomFieldLocal custRemote = custHome.create();
      custRemote.setDataSource(dataSource);

      Vector custfieldVec = entityVO.getCustomField();
      for (int i = 0; i < custfieldVec.size(); i++) {
        CustomFieldVO custFieldVO = (CustomFieldVO)custfieldVec.get(i);
        custFieldVO.setRecordID(newEntityID);
        custRemote.addCustomField(custFieldVO);
      }

      // Everything is created now apply the default permissions for this user.
      AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
      AuthorizationLocal authorizationLocal = authorizationHome.create();
      authorizationLocal.setDataSource(dataSource);
      authorizationLocal.saveCurrentDefaultPermission("Entity", newEntityID, individualID);
    }catch(Exception e){
      logger.error("[ejbCreate] Exception thrown.", e);
    }finally{
      dl.destroy();
      dl = null;
    }
    return new EntityPK(newEntityID, dataSource);
  }   // end ejbCreate(EntityVO,int,String) method

  /**
   * EJB Container callback method
   */
  public void ejbPostCreate(EntityVO ent,int actionUserId, String ds)
  {
    ejbLoad();
  }

  /**
   * EJB Container callback method
   */
  public void ejbActivate()
  {
  }

  /**
   * EJB Container callback method
   */
  public void ejbPassivate()
  {
  }

  /**
   * EJB Container callback method which stores all the changes made to the
   * entity. Values are set using the setEntityVO()
   */
  public void ejbStore()
  {
    if (this.isDirty) {
      int entId = this.envo.getContactID();
      int modBy = this.envo.getModifiedBy();

      InitialContext ic = CVUtility.getInitialContext();

      CVDal dl = new CVDal(dataSource);
      try {
        dl.setSql("contact.updateentity");
        dl.setString(1, this.envo.getExternalID());
        dl.setString(2, this.envo.getName());

        int sourceID = 0;

        if (this.envo.getSource() > 0) {
          sourceID = this.envo.getSource();
        } else {
          String sourceName = this.envo.getSourceName();
          if (sourceName != null && !sourceName.equals("")) {
            try {
              CommonHelperLocalHome helperHome = (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
              CommonHelperLocal helperRemote = helperHome.create();
              sourceID = helperRemote.getSourceID(sourceName);
            } catch (Exception e) {
              logger.error("[ejbStore] Exception thrown.", e);
            }
          }
        }

        dl.setInt(3, sourceID);
        dl.setInt(4, this.envo.getDBase());
        dl.setInt(5, modBy);
        dl.setInt(6, this.envo.getAccManager());
        dl.setInt(7, this.envo.getAccTeam());
        dl.setInt(8, this.envo.getContactID());
        dl.executeUpdate();
      } finally {
        dl.destroy();
        dl = null;
      }

      try {
        // update CustomFields
        CustomFieldLocalHome custHome = (CustomFieldLocalHome)ic.lookup("local/CustomField");
        CustomFieldLocal custRemote = custHome.create();
        custRemote.setDataSource(this.dataSource);

        Vector custfieldVec = this.envo.getCustomField();

        for (int i = 0; i < custfieldVec.size(); i++) {
          CustomFieldVO custFieldVO = (CustomFieldVO)custfieldVec.get(i);
          custFieldVO.setRecordID(entId);
          custRemote.updateCustomField(custFieldVO);
        }

        ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
        ContactHelperLocal remote = home.create();
        remote.setDataSource(this.dataSource);

        int cntType = this.envo.getContactType();
        AddressVO adr = this.envo.getPrimaryAddress();
        if (adr != null) {
          int addID = adr.getAddressID();
          if (addID > 0) {
            // The boolean value false tells the ContactHelperEJB to NOT
            // update the 'entity.Modified' field, since we are already
            // updating that record at this time.
            remote.updateAddress(this.envo.getPrimaryAddress(), entId, modBy, false);
          } else {
            // don't update Modified field
            remote.addAddress(this.envo.getPrimaryAddress(), entId, cntType, modBy, false); 
          }
        }

        Collection mocVOs = this.envo.getMOC();
        if (mocVOs != null) {
          Iterator mi = this.envo.getMOC().iterator();
          while (mi.hasNext()) {
            MethodOfContactVO mocObj = (MethodOfContactVO)mi.next();
            int mocID = mocObj.getMocID();
            if (mocObj.isAdded()) {
              // The boolean value false tells the ContactHelperEJB to NOT
              // update the 'entity.Modified' field, since we are already
              // updating that record at this time.
              remote.addMOC(mocObj, entId, cntType, modBy, false);
            } else if (mocObj.isUpdated()) {
              remote.updateMOC(mocObj, entId, modBy, false); // don't update
            } else if (mocObj.isDelete()) {
              remote.deleteMOC(mocID, entId, modBy, false); // don't update
            }
          }
        } // end if (mocVOs != null)

        IndividualVO newInd = this.envo.getIndividualVO();
      
        if (newInd != null) {
          if (newInd.getContactID() == 0) {
            // if the ID is zero there wasn't a primary contact for this entity, so create one.
            IndividualLocalHome ih = (IndividualLocalHome)ic.lookup("local/Individual");
            newInd.setEntityID(entId);
            ih.create(newInd, modBy, this.dataSource);
          } else {
            // otherwise the changePrimaryContact method does a bunch of stuff.
            this.changePrimaryContact();
          }
        }
        ejbLoad();
        this.isDirty = false;
      } catch (Exception e) {
        logger.error("[ejbStore] Exception thrown.", e);
      }
    }
  } // end ejbStore() method

  /**
   * EJB Container callback method which deletes Entity.
   */
  public void ejbRemove() throws RemoveException
  {
    CVDal dl = new CVDal(dataSource);

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);

      if (! basicReferencesFilled)
      {
        getEntityVOWithBasicReferences();
      }

      // delete Address
      AddressVO addVO = this.envo.getPrimaryAddress();
      if (addVO != null)
      {
        // The boolean value false tells the ContactHelperEJB to NOT
        // update the 'entity.Modified' field, since we are already
        // updating that record at this time.
        remote.deleteAddress(addVO.getAddressID(), this.envo.getContactID(), 1, false);
      }

      // All Method of Contact is to deleted.
      Iterator mi = this.envo.getMOC().iterator();
      while (mi.hasNext())
      {
        MethodOfContactVO mocVO = (MethodOfContactVO)mi.next();
        // The boolean value false tells the ContactHelperEJB to NOT
        // update the 'entity.Modified' field, since we are already
        // updating that record at this time.
        remote.deleteMOC(mocVO.getMocID(), this.envo.getContactID(), 1, false);
      }


      dl.setSqlQuery("SELECT IndividualID AS id FROM individual WHERE individual.Entity = ?");
      dl.setInt(1,this.envo.getContactID());

      Vector resultVector = (Vector) dl.executeQuery();

      dl.setSqlQueryToNull();

      Iterator iter = resultVector.iterator();

      IndividualLocalHome homeIndividual = (IndividualLocalHome)ic.lookup("local/Individual");

      while (iter.hasNext())
      {
        HashMap customField = (HashMap) iter.next();
        IndividualLocal il = homeIndividual.findByPrimaryKey( new IndividualPK(Integer.parseInt(customField.get("id").toString()),this.dataSource));
        il.remove();
      }

			//Delete Custom Field
      dl.setSql("contact.deletecustomfieldscalar");
      dl.setString(1,"Entity");
      dl.setInt(2,this.envo.getContactID());
      dl.executeUpdate();
      dl.setSqlQueryToNull();

      dl.setSql("contact.deletecustomfieldmultiple");
      dl.setString(1,"Entity");
      dl.setInt(2,this.envo.getContactID());
      dl.executeUpdate();
      dl.setSqlQueryToNull();

      dl.setSql("contact.deleteentity");
      dl.setInt(1,this.envo.getContactID());
      dl.executeUpdate();
		}catch(Exception e){
      System.out.println("[Exception][EntityEJB.ejbRemove] Exception Thrown: "+e);
      e.printStackTrace();
    }finally{
			dl.setSqlQueryToNull();
      dl.destroy();
      dl = null;
    }
  }   // end ejbRemove() method

  public void setEntityContext(EntityContext ctx)
  {
    EJB_Context = ctx;
  }

  /**
   * EJB Container callback method
   */
  public void unsetEntityContext()
  {
    EJB_Context = null;
  }

  /**
   * Sets the current EntityVo with the new one.
   * The new EntityVO is saved in ejbStore
   * Do not forget to set AddressID and other ID's in their
   * respective VO's.
   * @param entityVO A fully populated EntityVO object with new data.
   *
   */
  public void setEntityVO(EntityVO entityVO)
  {
    if (!this.basicReferencesFilled) {
      this.getEntityVOWithBasicReferences();
    }
    this.oldEntityVO = this.envo;
    this.envo = entityVO;
    this.isDirty = true;
  }

  /**
   * Marks the current Primary Contact as not Primary,
   * creates new Individual if not exixts as Primary contact,
   * updates Individual is already exits and marks as Primary
   */
  private void changePrimaryContact()
  {
    // update original and make the IsPrimary = "NO"
    // See if new one exits
    // if yes, then update with isPrimary = "YES"
    // if no, then inser new with IsPrimary = True
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      IndividualVO newIndividualVO = this.envo.getIndividualVO();
      IndividualVO oldIndividualVO = this.oldEntityVO.getIndividualVO();

      IndividualLocalHome ih = (IndividualLocalHome)ic.lookup("local/Individual");

      if (newIndividualVO.getContactID() != oldIndividualVO.getContactID())
      {
        // the primary contact was changed. so ...
        try
        {
          // set the old guy to not be the primary contact
          IndividualLocal oldIndividualEntityBean = ih.findByPrimaryKey(new IndividualPK(oldIndividualVO.getContactID(), this.dataSource));
          oldIndividualVO.setIsPrimaryContact("NO");
          oldIndividualEntityBean.setIndividualVO(oldIndividualVO);
        } catch (FinderException fe) {
          logger.info("[changePrimaryContact] old primary contact: "+oldIndividualVO.getContactID()+", not found.");
        }
        // set the the new one to be the primary contact
        IndividualLocal newIndividualEntityBean = ih.findByPrimaryKey(new IndividualPK(newIndividualVO.getContactID(), this.dataSource));
        newIndividualVO.setIsPrimaryContact("YES");
        newIndividualEntityBean.setIndividualVO(newIndividualVO);
      } else {
        // The selected primary contact is the same as the previous primary contact, just
        // need to set it as primary.
        IndividualLocal individualEntityBean = ih.findByPrimaryKey(new IndividualPK(newIndividualVO.getContactID(), this.dataSource));
        newIndividualVO.setIsPrimaryContact("YES");
        individualEntityBean.setIndividualVO(newIndividualVO);
      }
    } catch(Exception e) {
      logger.error("[changePrimaryContact] Exception thrown.", e);
    }
  }   // end changePrimaryContact() method


  /**
   * Returns all the fields from the Entity table as a HashMap;
   */
  private HashMap getBasic(EntityPK primaryKey)
  {
    CVDal dl = new CVDal(primaryKey.getDataSource());
    try {
      dl.setSql("contact.getentity");
      dl.setInt(1, primaryKey.getId());
      Collection col = dl.executeQuery();
      Iterator it = col.iterator();
      if (!it.hasNext()) {
        return null; //throw excepiton here
      }
      return (HashMap)it.next();
    } finally {
      dl.destroy();
      dl = null;
    }
  }

  /**
   * Updates the basic Entity data in the VO
   */
  private void setBasicVO(HashMap hm)
  {
    this.envo = new EntityVO();
    this.basicReferencesFilled = false;
    Long tmp;

    tmp = (Long)(hm.get("EntityID"));
    if (tmp != null) {
      this.envo.setContactID(tmp.intValue());
    }

    tmp = (Long)(hm.get("ModifiedBy"));
    if (tmp != null) {
      this.envo.setModifiedBy(tmp.intValue());
    }

    tmp = (Long)(hm.get("DBase"));
    if (tmp != null) {
      this.envo.setDBase(tmp.intValue());
    }

    tmp = (Long)(hm.get("List"));
    if (tmp != null) {
      this.envo.setList(tmp.intValue());
    }

    tmp = (Long)(hm.get("Owner"));
    if (tmp != null) {
      this.envo.setOwner(tmp.intValue());
    }

    tmp = (Long)(hm.get("Creator"));
    if (tmp != null) {
      this.envo.setCreatedBy(tmp.intValue());
    }

    this.envo.setExternalID((String)(hm.get("ExternalID")));
    this.envo.setName((String)(hm.get("Name")));
    tmp = (Long)(hm.get("Source"));
    if (tmp != null) {
      this.envo.setSource(tmp.intValue());
    }
    this.envo.setModifiedOn((Date)(hm.get("Modified")));
    this.envo.setCreatedOn((Date)(hm.get("Created")));

    tmp = (Long)(hm.get("AccountManagerID"));
    if (tmp != null) {
      this.envo.setAccManager(tmp.intValue());
    }

    tmp = (Long)(hm.get("AccountTeamID"));
    if (tmp != null) {
      this.envo.setAccTeam(tmp.intValue());
    }
    this.envo.setContactType(1); //Change it to CONSTANT VALUE
  }

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }
}   // end class definition

