/*
 * $RCSfile: ContactFacadeEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:04 $ - $Author: mcallist $
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


package com.centraview.contact.contactfacade;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AddressList;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.EntityList;
import com.centraview.common.GroupList;
import com.centraview.common.IndividualList;
import com.centraview.common.MOCList;
import com.centraview.common.helper.CommonHelperLocal;
import com.centraview.common.helper.CommonHelperLocalHome;
import com.centraview.common.source.SourceLocal;
import com.centraview.common.source.SourceLocalHome;
import com.centraview.common.source.SourcePK;
import com.centraview.common.source.SourceVO;
import com.centraview.contact.contactlist.ContactListLocal;
import com.centraview.contact.contactlist.ContactListLocalHome;
import com.centraview.contact.entity.EntityLocal;
import com.centraview.contact.entity.EntityLocalHome;
import com.centraview.contact.entity.EntityPK;
import com.centraview.contact.entity.EntityVO;
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
import com.centraview.contact.individual.UserLoginAssociationException;
import com.centraview.cvattic.CvAtticLocal;
import com.centraview.cvattic.CvAtticLocalHome;

public class ContactFacadeEJB implements SessionBean
{
  private static Logger logger = Logger.getLogger(ContactFacadeEJB.class);
	protected javax.ejb.SessionContext ctx;
	protected Context environment;
	private String dataSource = "MySqlDS";

	public Vector  getGroupMemberIDs( int userID , int groupId  )
	{
		Vector v = null ;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			GroupLocalHome home = (GroupLocalHome)ic.lookup("local/Group");
			GroupLocal remote = home.create();
			remote.setDataSource(dataSource);
			v = remote.getGroupMemberIDs(userID,groupId );
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getGroupMemberIDs] Exception Thrown: "+e);
      //e.printStackTrace();
		}
		return v;
	}

	/**
	 * Gets an Individual with all Basic References
	 * @param	int	IndividualID to fetch
	 * return	IndividualVO
	*/
	public IndividualVO getIndividual(int indId)
	{
		IndividualVO iv = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");
			IndividualLocal remote =  home.findByPrimaryKey(new IndividualPK(indId,this.dataSource));
			// the ejbLoad will take care of setting the dataSource on a findByPrimaryKey.
			iv = remote.getIndividualVOWithBasicReferences();
			CommonHelperLocalHome chhome= (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
			CommonHelperLocal commonhelper= chhome.create();
			// we need to explicitly set the dataSource on a stateless session bean.
			commonhelper.setDataSource(dataSource);
			iv.setSourceName(commonhelper.getSourceName(iv.getSource()));
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getIndividual] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return iv;
	}


	/**
	 * Gets an Entity with all Basic References
	 * @param	int	EntityID to fetch
	 * return	EntityVO
	*/
	public EntityVO getEntity(int entId)
	{
		EntityVO ev = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			EntityLocalHome home = (EntityLocalHome)ic.lookup("local/Entity");
			EntityLocal remote =  home.findByPrimaryKey(new EntityPK(entId,this.dataSource));
			CommonHelperLocalHome chhome= (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
			CommonHelperLocal commonhelper= chhome.create();
			// Explicitly set the dataSource on the stateless Session Bean.
			commonhelper.setDataSource(this.dataSource);
			ev = remote.getEntityVOWithBasicReferences();
			ev.setSourceName(commonhelper.getSourceName(ev.getSource()));
		} catch(Exception e)
		{
			System.out.println("[Exception][ContactFacadeEJB.getEntity] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return ev;

	}

  /**
   * Method to return the marketingList id of the entity related to the entity that
   * is passed in.  If the EntityId passed in returns no results (doesn't exist in the DB)
   * the the Integer 1 will be returned.  This method is used mainly for ensuring Individuals
   * are created in the "correct" marketing list.
   *
   * @author Kevin McAllister <kevin@centraview.com>
   * @param entityId
   * @return Integer of the marketing list for the entityId, or 1 if something goes wrong.
   * @throws RemoteException
   */
  public Integer getEntityMarketingList(int entityId)
  {
    CVDal cvdl = new CVDal(this.dataSource);
    Integer marketingListId = new Integer(1);
    try {
      // "SELECT entity.entityid, entity.name FROM entity, individual where entity.entityid = individual.entity AND individual.individualid = ?"
      cvdl.setSql("contact.getentitymarketinglist");
      cvdl.setInt(1, entityId);
      Vector results = (Vector)cvdl.executeQuery();
      Iterator iter = results.iterator();
      if (iter.hasNext())  // we should only get one result, otherwise we will just return the empty hashmap.
      {
        HashMap resultMap = (HashMap)iter.next();
        if(resultMap.get("list") != null)  // If it's not null, it probably resembles a number.
        {
          marketingListId = new Integer(((Number)resultMap.get("list")).intValue());
        }
      } // end of if(iter.hasNext())
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    // The hashmap will have two keys "entityid", "name"
    return marketingListId;
  }

	/**
	 * Gets an Source ID with help of passing the Source Name
	 * @param	String	sourceName to fetch
	 * return	sourceID
	*/
	public HashMap getSourceList()
	{
		HashMap sourceList = new HashMap();
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			CommonHelperLocalHome chhome= (CommonHelperLocalHome)ic.lookup("local/CommonHelper");
			CommonHelperLocal commonhelper= chhome.create();
			commonhelper.setDataSource(dataSource);
			sourceList = commonhelper.getSourceList();
		} catch(Exception e)
		{
			System.out.println("[Exception][ContactFacadeEJB.getSourceList] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return sourceList;

	}

	/**
	 * Creates a new Entity
	 * @param	EntityVO	EntityVo for data to be added
	 * @param	int			Id of the user performing the action
	*/
	public int createEntity(EntityVO entityDetails, int ActionUserId)
	{
		int entityID = 0;
    try {
			InitialContext ic = CVUtility.getInitialContext();
			EntityLocalHome home = (EntityLocalHome)ic.lookup("local/Entity");
			ctx.getUserTransaction().begin();
			EntityLocal remote =  home.create(entityDetails, ActionUserId, this.dataSource);
      EntityVO evo = remote.getEntityVOBasic();
			ctx.getUserTransaction().commit();

			entityID = evo.getContactID();

			HashMap historyInfo = new HashMap();
			historyInfo.put("recordID",new Integer(entityID));
			historyInfo.put("recordTypeID", new Integer(Constants.EntityModuleID));
			historyInfo.put("operation", new Integer(Constants.CREATED));
			historyInfo.put("individualID", new Integer(ActionUserId));
			historyInfo.put("referenceActivityID", new Integer(0));
			historyInfo.put("recordName", entityDetails.getName());
			CVUtility.addHistoryRecord(historyInfo, this.dataSource);
		}catch(Exception e){
      logger.error("[createEntity] Exception thrown.", e);
		}
		return entityID;
	}

	/**
	 * Creates a new Individual
	 * @param	IndividualVO	IndividualVo for data to be added
	 * @param	int			Id of the user performing the action
	 * @return 	indID	The IndividualID of the created individual, if it is zero something didn't work review the logs.
	*/
	public int createIndividual(IndividualVO indDetail,int actionUserId) throws AuthorizationFailedException
	{
		if (! CVUtility.isModuleVisible("Individual", actionUserId, this.dataSource))
    {
			throw new AuthorizationFailedException("[ContactFacadeEJB] createIndividual() - isModuleVisible returned false for individualID = " +actionUserId);
    }

		int indID = 0;

		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");

			ctx.getUserTransaction().begin();

			IndividualLocal remote =  home.create(indDetail,actionUserId,this.dataSource);
			IndividualVO vo = remote.getIndividualVOBasic();
			indID = vo.getContactID();

			ctx.getUserTransaction().commit();

		   // The code to add the entry in DB is added to IndividualEJB
		   // The reson is the IndividualCreate method is called directly
		   //Instead of using ContactFacade  ...
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.createIndividual] Exception Thrown: "+e);
			try
			{
				ctx.getUserTransaction().rollback();
			}catch (SystemException se){
				System.out.println("[SystemException][ContactFacadeEJB.createIndividual] Exception Thrown trying rollback: "+se);
				se.printStackTrace();
			}
			e.printStackTrace();
		}

		return indID;
	}   // end createIndividual()

  /**
   * Creates a new Group
   * @param	GroupVO		GroupVo for data to be added
   */
  public int createGroup(int userId, GroupVO groupDetail) throws AuthorizationFailedException
  {
    if (! CVUtility.isModuleVisible("Group",userId, this.dataSource))
    {
      throw new AuthorizationFailedException("Group - createGroup");
    }

		int newGroupID = 0;

		try
		{
      InitialContext ic = CVUtility.getInitialContext();
      GroupLocalHome home = (GroupLocalHome)ic.lookup("local/Group");
      GroupLocal remote =  home.create();
      remote.setDataSource(dataSource);
      // Transaction not required cause even if adding member fails its ok.
      newGroupID = remote.createGroup(userId, groupDetail);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.createGroup] Exception Thrown: "+e);
      //e.printStackTrace();
    }
    return newGroupID;
  }   // end createGroup() method


	/**
	 * Adds all the passed Contacts (Entity/Individual) to a given Group
	 * @param	int[]	int Array of all the contact ID's to be added
	*/
	public void addContactToGroup(int userId,int groupId, int[] contactId)
	{
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			GroupLocalHome home = (GroupLocalHome)ic.lookup("local/Group");
			GroupLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			remote.addContactToGroup(userId, contactId,groupId);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.addContactToGroup] Exception Thrown: "+e);
			//e.printStackTrace();
		}
	}

	/**
	 * Updtaes a given Entity's data
	 * @param	EntityDetail  New data to be updated
	 * @param individualID  individualId of user who updates Entity
	*/
	public void updateEntity(EntityVO entityDetail, int individualID) throws AuthorizationFailedException
	{
	  if (! CVUtility.canPerformRecordOperation(individualID, "Entity", entityDetail.getContactID(), ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource)) {
	    throw new AuthorizationFailedException("Entity - updateEntity");
	  }
	  try {
        InitialContext ic = CVUtility.getInitialContext();
        EntityLocalHome home = (EntityLocalHome)ic.lookup("local/Entity");
        EntityLocal remote = home.findByPrimaryKey(new EntityPK(entityDetail.getContactID(), this.dataSource));
  
        ctx.getUserTransaction().begin();
        remote.setEntityVO(entityDetail);
        ctx.getUserTransaction().commit();
  
        HashMap historyInfo = new HashMap();
        historyInfo.put("recordID", new Integer(entityDetail.getContactID()));
        historyInfo.put("recordTypeID", new Integer(Constants.EntityModuleID));
        historyInfo.put("operation", new Integer(Constants.UPDATED));
        historyInfo.put("individualID", new Integer(individualID));
        historyInfo.put("referenceActivityID", new Integer(0));
        historyInfo.put("recordName", entityDetail.getName());
        CVUtility.addHistoryRecord(historyInfo, this.dataSource);
      } catch (Exception e) {
        logger.error("[updateEntity] Exception thrown.", e);
      }
    }

	public void entityMove(int individualId, int listId, String entityIDs[])
  {
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			remote.entityMove(individualId, listId, entityIDs );
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.deleteEntity] Exception Thrown: "+e);
			//e.printStackTrace();
		}
	}

	/**
	 * Deletes a given Entity's data
	 * @param	int		EntityID to be deleted
	 * @param int individualID the indvID of user who deletes entity
	*/
	public void deleteEntity(int entityId, int individualID) throws AuthorizationFailedException
	{
    if (! CVUtility.canPerformRecordOperation(individualID, "Entity", entityId, ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource))
    {
      throw new AuthorizationFailedException("Entity - deleteEntity");
    }

		try{
			InitialContext ic = CVUtility.getInitialContext();
			EntityLocalHome home = (EntityLocalHome)ic.lookup("local/Entity");
			EntityLocal remote =  home.findByPrimaryKey(new EntityPK(entityId,this.dataSource));

			EntityVO ev = remote.getEntityVOWithBasicReferences();

			HashMap hmDetails=new HashMap();
			hmDetails.put("title",ev.getName());
			hmDetails.put("owner",new Integer(ev.getOwner()));
			hmDetails.put("module",new Integer(1));
			hmDetails.put("recordtype",new Integer(1));

			InitialContext ctxl=CVUtility.getInitialContext();
			CvAtticLocalHome cahome=(CvAtticLocalHome)ctxl.lookup("local/CvAttic");

			CvAtticLocal caremote =cahome.create();
			caremote.setDataSource(this.dataSource);

			int transactionID=caremote.getAtticTransactionID(individualID,Constants.CV_GARBAGE,hmDetails);

			HashMap hmentity=new HashMap();
			hmentity.put("EntityID",(new Integer(ev.getContactID())).toString());
			caremote.dumpData(individualID,transactionID,"entity",hmentity);

			AddressVO adv = ev.getPrimaryAddress();

			if (adv != null)
			{
				HashMap hmaddr=new HashMap();
				hmaddr.put("AddressID",(new Integer(adv.getAddressID())).toString());
				caremote.dumpData(individualID,transactionID,"address",hmaddr);
			}

			Vector vec = ev.getCustomField();

			if (vec != null)
			{
				Iterator it = vec.iterator();
				while (it.hasNext())
				{
					CustomFieldVO cfv = (CustomFieldVO)it.next();
					String cftablename = "customfieldscalar";
					if (cfv.getFieldType() == CustomFieldVO.MULTIPLE)
					{
						cftablename = "customfieldmultiple";
					}

					HashMap hmcfv=new HashMap();
					hmcfv.put("CustomFieldID", (new Integer(cfv.getFieldID())).toString());
					caremote.dumpData(individualID, transactionID, cftablename, hmcfv);
				}
			}

			Vector mocvec = ev.getMOC();
			if (mocvec != null)
			{
				Iterator it = mocvec.iterator();
				while (it.hasNext())
				{
					MethodOfContactVO mocv = (MethodOfContactVO)it.next();

					HashMap hmmocr=new HashMap();
					hmmocr.put("MOCID",(new Integer(mocv.getMocID())).toString());
					hmmocr.put("ContactType",(new Integer(1)).toString());
					hmmocr.put("ContactID",(new Integer(entityId)).toString());
					caremote.dumpData(individualID,transactionID,"mocrelate",hmmocr);

					HashMap hmmoc=new HashMap();
					hmmoc.put("methodofcontact",(new Integer(mocv.getMocID())).toString());
					caremote.dumpData(individualID,transactionID,"methodofcontact",hmmoc);
				}
			}

			ctx.getUserTransaction().begin();
			remote.remove();
			ctx.getUserTransaction().commit();

			HashMap historyInfo = new HashMap();
			historyInfo.put("recordID",new Integer(entityId));
			historyInfo.put("recordTypeID", new Integer(Constants.EntityModuleID));
			historyInfo.put("operation", new Integer(Constants.DELETED));
			historyInfo.put("individualID", new Integer(individualID));
			historyInfo.put("referenceActivityID", new Integer(0));
			historyInfo.put("recordName", ev.getName());
			CVUtility.addHistoryRecord(historyInfo , this.dataSource);
		}catch(CreateException ce){
			throw new EJBException(ce);
		}catch(NamingException re){
			throw new EJBException(re);
		}catch(FinderException fe){
			System.out.println("[Exception][ContactFacadeEJB.deleteEntity] Exception Thrown: "+fe);
		}catch(RemoveException fe){
			throw new EJBException(fe);
		}catch(NotSupportedException nse){
			throw new EJBException(nse);
		}catch(RollbackException rbe){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Entity - deleteEntity");
		}catch(SystemException se){
			throw new EJBException(se);
		}catch(HeuristicMixedException hme){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Entity - deleteEntity");
		}catch(HeuristicRollbackException hre){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Entity - deleteEntity");
		}
  }   // end deleteEntity() method

  public void updateModifiedBy(CustomFieldVO customFieldVO, int userID)
  {
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      remote.updateModifiedBy(customFieldVO, userID);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.updateMOC] Exception Thrown: "+e);
    }
  }

  /**
   * Updates a given Individual's data
   * @param IndividualVO Individual data to be updated
   * @param individualID The individualID of user who updates Individual
   */
  public void updateIndividual(IndividualVO individualDet, int individualID) throws AuthorizationFailedException
  {
    if (!CVUtility.canPerformRecordOperation(individualID, "Individual", individualDet.getContactID(), ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource)) {
      throw new AuthorizationFailedException("[ContactFacadeEJB] updateIndividual() - canPerformRecordOperation() failed for individualID = " + individualID);
    }
    try {
      InitialContext ic = CVUtility.getInitialContext();
      IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");
      IndividualLocal remote = home.findByPrimaryKey(new IndividualPK(individualDet.getContactID(), this.dataSource));
      ctx.getUserTransaction().begin();
      remote.setIndividualVO(individualDet);
      ctx.getUserTransaction().commit();
      HashMap historyInfo = new HashMap();
      historyInfo.put("recordID", new Integer(individualDet.getContactID()));
      historyInfo.put("recordTypeID", new Integer(Constants.IndividualModuleID));
      historyInfo.put("operation", new Integer(Constants.UPDATED));
      historyInfo.put("individualID", new Integer(individualID));
      historyInfo.put("referenceActivityID", new Integer(0));
      historyInfo.put("recordName", individualDet.getFirstName() + " " + individualDet.getMiddleName() + " " + individualDet.getLastName());
      CVUtility.addHistoryRecord(historyInfo, this.dataSource);
    } catch (Exception e) {
      logger.error("[updateIndividual] Exception thrown.", e);
    }
  } // end updateIndividual() method

	/**
	 * Deletes a given Individual's data
	 * @param	int	Individual ID to be deleted
	 * @param indID the Individual ID of user who delete Individual
	 */
	public void deleteIndividual(int individualId, int indID) throws AuthorizationFailedException,NamingException, RemoveException
	{
		if (!CVUtility.canPerformRecordOperation(indID, "Individual", individualId, ModuleFieldRightMatrix.DELETE_RIGHT, this.dataSource))
    {
      throw new AuthorizationFailedException("Individual - deleteIndividual");
    }
		try{
			InitialContext ic = CVUtility.getInitialContext();
			IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");
			IndividualLocal remote =  home.findByPrimaryKey(new IndividualPK(individualId,this.dataSource));

			IndividualVO indVO = remote.getIndividualVOWithBasicReferences();

			HashMap hmDetails=new HashMap();
			hmDetails.put("title",indVO.getFirstName() + " " + indVO.getLastName());
			hmDetails.put("owner",new Integer(indVO.getOwner()));
			hmDetails.put("module",new Integer(1));
			hmDetails.put("recordtype",new Integer(2));

			InitialContext ctxl=CVUtility.getInitialContext();
			CvAtticLocalHome cahome=(CvAtticLocalHome)ctxl.lookup("local/CvAttic");

			CvAtticLocal caremote =cahome.create();
			caremote.setDataSource(this.dataSource);

			int transactionID=caremote.getAtticTransactionID(indID,Constants.CV_GARBAGE,hmDetails);

			HashMap hmind=new HashMap();
			hmind.put("IndividualID",(new Integer(indVO.getContactID())).toString());
			caremote.dumpData(indID,transactionID,"individual",hmind);

			AddressVO adv = indVO.getPrimaryAddress();
			if (adv != null)
			{
				HashMap hmaddr=new HashMap();
				hmaddr.put("AddressID",(new Integer(adv.getAddressID())).toString());
				caremote.dumpData(indID,transactionID,"address",hmaddr);

				HashMap addressRelateHashMap = new HashMap();
				addressRelateHashMap.put("Address",(new Integer(adv.getAddressID())).toString());
				caremote.dumpData(indID,transactionID,"addressrelate", addressRelateHashMap);
			}

			Vector mocvec = indVO.getMOC();
			if (mocvec != null)
			{
				Iterator it = mocvec.iterator();
				while (it.hasNext())
				{
					MethodOfContactVO mocv = (MethodOfContactVO)it.next();

					HashMap hmmocr=new HashMap();
					hmmocr.put("MOCID",(new Integer(mocv.getMocID())).toString());
					hmmocr.put("ContactType",(new Integer(2)).toString());
					hmmocr.put("ContactID",(new Integer(individualId)).toString());
					caremote.dumpData(indID, transactionID, "mocrelate", hmmocr);

					HashMap hmmoc=new HashMap();
					hmmoc.put("MOCID",(new Integer(mocv.getMocID())).toString());
					hmmoc.put("MOCType", Integer.toString(mocv.getMocType()));
					caremote.dumpData(indID, transactionID, "methodofcontact", hmmoc);
				}
			}

			Vector vec = indVO.getCustomField();
			if (vec != null)
			{
				Iterator it = vec.iterator();
				while (it.hasNext())
				{
					CustomFieldVO cfv = (CustomFieldVO)it.next();
					String cftablename = "customfieldscalar";
					if (cfv.getFieldType() == CustomFieldVO.MULTIPLE)
					{
						cftablename = "customfieldmultiple";
					}

					HashMap hmcfv=new HashMap();
					hmcfv.put("CustomFieldID",(new Integer(cfv.getFieldID())).toString());
					caremote.dumpData(indID,transactionID,cftablename,hmcfv);
				}
			}
			remote.setOperationIndividualId(individualId);

			ctx.getUserTransaction().begin();
			remote.remove();
      ctx.getUserTransaction().commit();

            HashMap historyInfo = new HashMap();
		    historyInfo.put("recordID",new Integer(individualId));
		    historyInfo.put("recordTypeID", new Integer(Constants.IndividualModuleID));
		    historyInfo.put("operation", new Integer(Constants.DELETED));
		    historyInfo.put("individualID", new Integer(indID));
		    historyInfo.put("referenceActivityID", new Integer(0));
		    historyInfo.put("recordName", indVO.getFirstName()+ " "+ indVO.getMiddleName()+ " " + indVO.getLastName());
		    CVUtility.addHistoryRecord(historyInfo , this.dataSource);

		}catch(FinderException fe){
			System.out.println("[Exception][ContactFacadeEJB.deleteIndividual] Exception Thrown: "+fe);
		}catch(RemoveException fe){
		  if (fe instanceof UserLoginAssociationException) {
		    try {
		      ctx.getUserTransaction().commit();
		    } catch (RollbackException rbe){
					//TODO we shouldn't do like this Since we aren't parsing the record information.
					// Its a time being hack.
					throw new AuthorizationFailedException("Individual - deleteIndividual");
				} catch (SystemException se){
					throw new EJBException(se);
				} catch (HeuristicMixedException hme){
					throw new EJBException(hme);
				} catch (HeuristicRollbackException hre){
					//TODO we shouldn't do like this Since we aren't parsing the record information.
					// Its a time being hack.
					throw new AuthorizationFailedException("Individual - deleteIndividual");
				}
		    throw new UserLoginAssociationException();
		  }
	    throw new RemoveException();
		}catch(RollbackException rbe){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Individual - deleteIndividual");
		}catch(SystemException se){
			throw new EJBException(se);
		}catch(HeuristicMixedException hme){
			throw new EJBException(hme);
		}catch(HeuristicRollbackException hre){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Individual - deleteIndividual");
		}catch(NotSupportedException nse){
			throw new EJBException(nse);
		}catch(CreateException ce){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Individual - deleteIndividual");
		}
		}   // end deleteIndividual() method

	public void createAddress(int contactId,  int contactType, AddressVO addressDet, int individualId)
	{
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			remote.addAddress(addressDet, contactId, contactType, individualId, true);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.createAddress] Exception Thrown: "+e);
			//e.printStackTrace();
		}
	}   // end createAddress() method

	/**
   * Updates an Address record.
   */
  public void updateAddress(int contactId,  AddressVO addressDet)
  {
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      remote.updateAddress(addressDet, contactId, 1,true);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.updateAddress] Exception Thrown: "+e);
    }
  }   // end  updateAddress() method

	/**
   * Updates an Address and relate record.
   */
	public void updateRelateAddress(AddressVO addressVO, int contactType, int contactId, int userId)
	{
		try
		{
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      remote.updateRelateAddress(addressVO, contactType, contactId);
      // When a contact method or address is updated, it must alter the modified
      // time of the individual or entity record with which it is associated to the current
      // time.  If this does not happen, sync is broken.
      remote.updateModified("address", contactId, userId, addressVO.getAddressID());
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.updateRelatedAddress] Exception Thrown: "+e);
      //e.printStackTrace();
    }
  }   // end  updateAddress() method


  /**
   * Simply changes the addressrelate table to change an  address from relating to
   * one contact and relate it to another.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param mocId the uniqueid of the address you want to re-relate.
   * @param contactId the unique id of the contact you want to relate it to.
   * @param contactType the type of contact you want to relate it to (Entity = 1, Individual = 2)
   * @return an Int for the number or rows effected.  (the results from the DataAccessLayer)
   */
  public int changeAddressRelate(int address, int contactId, int contactType)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    int returnValue = 0;
    try
    {
      cvdal.setSqlQuery("UPDATE addressrelate SET contacttype = ?, contact = ? WHERE address = ?");
      cvdal.setInt(1, contactType);
      cvdal.setInt(2, contactId);
      cvdal.setInt(3, address);
      returnValue = cvdal.executeUpdate();
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.changeAddressRelate] Exception Thrown: "+e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return returnValue;
  }

	/**
   *	In this method Address is deleted from following Table.
   *	1) AddressRelate
   *	2) Address.
   */
	public void deleteAddress(int addressId, int contactId, int userId)
	{
		try{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			remote.deleteAddress(addressId,  contactId,  userId,true);
			remote.updateModified("address", contactId, userId, addressId);
		}catch(NamingException re){
			throw new EJBException(re);
		}catch(CreateException ce){
			throw new EJBException(ce);
		}
	}   // end deleteAddress() method


	public void createMOC(int contactId,  int contactType, MethodOfContactVO mocDet, int individualId)
	{
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			remote.addMOC(mocDet, contactId, contactType, individualId,true);
			// TODO: userId HARDCODED
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.createMOC] Exception Thrown: "+e);
			//e.printStackTrace();
		}
	}   // end createMOC() method

	/**
   * Updates the given method of contact record.
   */
  public void updateMOC(MethodOfContactVO contactMethod, int contactId, int userId)
  {
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      remote.updateMOC(contactMethod, contactId, userId,true);
      // When a contact method or address is updated, it must alter the modified
      // time of the individual or entity record with which it is associated to the current
      // time.  If this does not happen, sync is broken.
      remote.updateModified("moc", contactId, userId,contactMethod.getMocID());
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.updateMOC] Exception Thrown: " + e);
    }
  }   // end updateMOC() method

  /**
   * Simply changes the mocrelate table to change a MOC from relating to
   * one contact and relate it to another.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param mocId the uniqueid of the moc you want to re-relate.
   * @param contactId the unique id of the contact you want to relate it to.
   * @param contactType the type of contact you want to relate it to (Entity = 1, Individual = 2)
   * @return an Int for the number or rows effected.  (the results from the DataAccessLayer)
   */
  public int changeMOCRelate(int mocId, int contactId, int contactType)
  {
    CVDal cvdal = new CVDal(this.dataSource);
    int returnValue = 0;
    try
    {
      cvdal.setSqlQuery("UPDATE mocrelate SET contacttype = ?, contactid = ? WHERE mocid = ?");
      cvdal.setInt(1, contactType);
      cvdal.setInt(2, contactId);
      cvdal.setInt(3, mocId);
      returnValue = cvdal.executeUpdate();
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.changeMOCRelate] Exception Thrown: "+e);
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return returnValue;
  }

  /**
   * Returns All Address for contactId and ContactType.
   */
	public AddressList getAllAddresses(int contactId,int contactType)
	{
		AddressList  addressList = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
			ContactListLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			// TODO userId HARDCODED
			addressList = remote.getAddressList(1, contactId,contactType);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getAllAddresses] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return  addressList;
	}   // end getAllAddresses() method

  /**
   * This method returns what you always wanted, a Collection
   * actually implemented as an ArrayList of
   * AddressVOs for the particular contactID passed in.
   * <p>The collection will be empty if something goes wrong.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param contactId the contact you want to join the address table against.
   * @param contactType the contact type of the contact.
   * @return a Collection, actually an ArrayList either empty or full of addressVOs.
   */
  public Collection getAllAddressVOs(int contactId, int contactType)
  {
    CVDal dl = new CVDal(this.dataSource);
    Collection addressVOs = new ArrayList();
    Collection rawAddresses = null;
    try
    {
      dl.setSql("contacts.getAllAddressVO");
      dl.setInt(1,contactId);
      dl.setInt(2,contactType);
      rawAddresses = dl.executeQuery();
    }finally{
      dl.clearParameters();
      dl.destroy();
    }
    // Now we have it, lets build the VOs.
    // And put them on the ArrayList
    if (rawAddresses != null)
    {
      Iterator addressIteration = rawAddresses.iterator();
      while(addressIteration.hasNext())
      {
        AddressVO addressVO = new AddressVO();
        HashMap hm = (HashMap) addressIteration.next();
        addressVO.setAddressID(((Number)hm.get("addressId")).intValue());
        addressVO.setStreet1((String) hm.get("street1"));
        addressVO.setStreet2((String) hm.get("street2"));
        addressVO.setCity((String) hm.get("city"));
        addressVO.setStateName((String) hm.get("state"));
        addressVO.setZip((String) hm.get("zip"));
        addressVO.setCountryName((String) hm.get("country"));
        addressVO.setWebsite((String) hm.get("website"));
        addressVO.setAddressType(((Number) hm.get("addressType")).intValue());
        addressVO.setIsPrimary((String) hm.get("isPrimary"));
        addressVOs.add(addressVO);
      } // end while()
    } // end if(rawAddress != null)
    return addressVOs;
  } // getAllAddressesBasic(int contactId, int contactType)

	public MOCList getAllMOC(int contactId, int contactType)
	{
		MOCList  ml = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
			ContactListLocal remote =  home.create();
			remote.setDataSource(dataSource);
			ml = remote.getMOCList(1, contactId,contactType);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getAllMOC] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return  ml;
	}   //  end getAllMOC() method

	public void createSource(SourceVO svo)
	{
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			SourceLocalHome home = (SourceLocalHome)ic.lookup("local/Source");
			SourceLocal remote =  home.create(svo,this.dataSource);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.createSource] Exception Thrown: "+e);
			//e.printStackTrace();
		}
	}

	/**
   *   @param sourceid id of source
   *   @param svo  source view object
   */
	public void updateSource(int sourceId, SourceVO svo)
	{
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			SourceLocalHome home = (SourceLocalHome)ic.lookup("local/Source");
			SourceLocal remote =  home.findByPrimaryKey(new SourcePK(sourceId,this.dataSource));
			remote.setSourceVO(svo);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.updateSource] Exception Thrown: "+e);
			//e.printStackTrace();
		}
  }   // end updateSource() method


	/**
   *	@author 
   *  @param id of Source
   */
	public void deleteSource(int sourceId)
	{
		try{
			InitialContext ic = CVUtility.getInitialContext();
			SourceLocalHome home = (SourceLocalHome)ic.lookup("local/Source");
			SourceLocal remote =  home.findByPrimaryKey(new SourcePK(sourceId,this.dataSource));
			remote.remove();
		}catch(NamingException re){
			throw new EJBException(re);
		}catch(FinderException fe){
			System.out.println("[Exception][ContactFacadeEJB.deleteSource] Exception Thrown: "+fe);
		}catch(RemoveException fe){
			throw new EJBException(fe);
		}
	}   // end deleteSource() method


	public void setSessionContext(SessionContext ctx)
	{
		this.ctx = ctx;
	}

	public void ejbActivate()  { }
	public void ejbPassivate()  { }
	public void ejbRemove()  { }
	public void ejbCreate() throws CreateException{ }

  public EntityList getAllEntityList( int userId, HashMap preference)
  {
		EntityList  entityList = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
			ContactListLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			entityList = (EntityList)remote.getAllEntityList( userId,  preference);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getAllEntityList] Exception Thrown: "+e);
			//e.printStackTrace();
			return null;
		}
		return entityList;
  }

  /**
   * Returns and IndividualList object (sub-class of DisplayList).
   */
  public IndividualList getAllIndividualList(int userID, HashMap preference)
  {
    IndividualList  individualList = null;

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
      ContactListLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      individualList = (IndividualList)remote.getAllIndividualList(userID, preference);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB] Exception thrown in getAllIndividualList(): " + e);
    }
    return(individualList);
  }   // end getAllIndividualList() method

  /**
  * Returns and IndividualList object (sub-class of DisplayList).
  */
  public IndividualList getAllIndividualAndEntityEmailList(int userID, HashMap preference)
  {
    IndividualList  individualList = null;

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
      ContactListLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      individualList = (IndividualList)remote.getAllIndividualAndEntityEmailList(userID, preference);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB] Exception thrown in getAllIndividualAndEntityEmailList(): " + e);
    }
    return(individualList);
  }   // end getAllIndividualAndEntityEmailList() method


  public HashMap getEmployeeList()
  {
    HashMap  individualList = new HashMap();

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      individualList = remote.getEmployeeList();
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB] Exception thrown in getAllIndividualList(): " + e);
      return(null);
    }
    return(individualList);
  }

  /**
   * This method builds a Collection (ArrayList) of Individual VOs
   * where the Individuals related Entity is equal to the entityId.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param entityId
   * @return a Collection of IndividualVOs, all related to the passed entityId.
   */
  public Collection getAllIndividualVOs(int entityId)
  {
    ArrayList individualVOs = new ArrayList();
    // Query CVDal for a collection of IndividualIds
    // Get the IndividualVOs for each, and send 'em back
    // in the collection.
    CVDal cvdal = new CVDal(this.dataSource);
    Collection results = null;
    try
    {
      cvdal.setSqlQuery("SELECT individualid FROM individual WHERE entity = ?");
      cvdal.setInt(1, entityId);
      results = cvdal.executeQuery();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }

    if (results != null)
    {
      Iterator resultsIterator = results.iterator();
      while(resultsIterator.hasNext())
      {
        HashMap resultMap = (HashMap)resultsIterator.next();
        int individualId = ((Number)resultMap.get("individualid")).intValue();
        IndividualVO individual = this.getIndividual(individualId);
        individualVOs.add(individual);
      } // end while(resultsIterator.hasNext())
    } // end if (results != null)
    return individualVOs;
  }   // end getAllIndividualVOs() method

  /**
   * Returns a list of Employee (Individual) records for display
   * in the Employee lookup screen.
   * @return Collection of sql results (each row being a HashMap)
   */
  public Collection getEmployeeListCollection(int individualID)
  {
    Collection employeeList = (Collection)new ArrayList();
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      HashMap hm = new HashMap();
			hm.put("sortColumn","Name");
			hm.put("sortDirection","A");
			hm.put("dbID", new Integer(0));
      hm.put("ADVANCESEARCHSTRING", "ADVANCE:SELECT IndividualID FROM individual WHERE entity=1");

      ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
      ContactListLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      employeeList = remote.getAccessIndividualList(individualID, hm);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB] Exception thrown in getAllIndividualList(): " + e);
      //e.printStackTrace();
    }
    return(employeeList);
  }   // end getEmployeeList() method

  /**
   * This method just gets the employee list ready for display, in a labelvaluebean.
   * Used initially in permission screens, probably various other uses for it as well.
   * @return
   */
  public Collection getEmployeeListDisplay()
  {
    ArrayList employeeList = new ArrayList();
    String query = "SELECT individualId AS employeeId, CONCAT(firstName, ' ', lastName) AS name FROM individual WHERE entity = 1 ORDER BY lastName ASC";
    CVDal cvdl = new CVDal(dataSource);
    try {
      cvdl.setSqlQuery(query);
      Collection results = cvdl.executeQuery();
      Iterator i = results.iterator();
      while (i.hasNext()) {
        HashMap row = (HashMap)i.next();
        Number employeeId = (Number)row.get("employeeId");
        String name = (String)row.get("name");
        employeeList.add(new LabelValueBean(name, employeeId.toString()));
      }
    } finally {
      cvdl.destroy();
      cvdl = null;
    }
    return employeeList;
  }
  
  
  /**
   * Returns a list of user (Individual) records for display
   * in the permission screen.
   * @return Collection of sql results (each row being a HashMap)
   */
  public Collection getUserListCollection(int individualID)
  {
    Collection employeeList = (Collection)new ArrayList();
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      HashMap hm = new HashMap();
			hm.put("sortColumn","Name");
			hm.put("sortDirection","A");
			hm.put("dbID", new Integer(0));
      hm.put("ADVANCESEARCHSTRING", "ADVANCE:SELECT IndividualID FROM user WHERE usertype != 'CUSTOMER'");

      ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
      ContactListLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      employeeList = remote.getAccessIndividualList(individualID, hm);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB] Exception thrown in getAllIndividualList(): " + e);
      //e.printStackTrace();
    }
    return(employeeList);
  }   // end getUserListCollection() method

  public HashMap getEntityList(int listId)
  {
    HashMap  entityList = new HashMap();

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);

      entityList = remote.getEntityList(listId);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB] Exception thrown in getAllIndividualList(): " + e);
      return(null);
    }
    return(entityList);
  }   // end getEntityList() method

  /**
   * Returns and GroupList object (sub-class of DisplayList).
   */
  public GroupList getAllGroupList(int userId, HashMap preference)
  {
    GroupList  groupList = null;
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
      ContactListLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      groupList = (GroupList)remote.getAllGroupList( userId,  preference);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB] Exception thrown in getAllGroupList(): " + e);
      //e.printStackTrace();
      return null;
    }
    return groupList;
  }   // end getAllGroupList() method


  public GroupVO getGroup(int userId, int groupId)
  {
    GroupVO gvo = null;

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      GroupLocalHome home = (GroupLocalHome)ic.lookup("local/Group");
      GroupLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      // Transaction not required cause even if adding member fails its ok.
      gvo  = remote.getGroupDetails(userId, groupId);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.getGroup] Exception Thrown: "+e);
      //e.printStackTrace();
    }
    return gvo;
  }   // end getGroup() method

  public void deleteGroup(int userId, int groupId)
  {
		try{
			InitialContext ic = CVUtility.getInitialContext();
			GroupLocalHome home = (GroupLocalHome)ic.lookup("local/Group");
			GroupLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			// Transaction not required cause even if adding member fails its ok.
			remote.deleteGroup(groupId);
		}catch(NamingException re){
			throw new EJBException(re);
		}catch(CreateException ce){
			throw new EJBException(ce);
		}
  }   // end deleteGroup() method

  public void deleteGroupMember(int groupMemberId, int groupId)
  {
    try {
      InitialContext ic = CVUtility.getInitialContext();
      GroupLocalHome home = (GroupLocalHome)ic.lookup("local/Group");
      GroupLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      remote.deleteGroupMember(groupId, groupMemberId);
    } catch (NamingException re) {
      throw new EJBException(re);
    } catch (CreateException ce) {
      throw new EJBException(ce);
    }
  } // end deleteGroupMember() method

  public void updateGroup(int userId, GroupVO groupDetail) throws AuthorizationFailedException
  {
		boolean recordFlag = CVUtility.canPerformRecordOperation(userId,"Group",groupDetail.getGroupID(),ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource);

    if (!recordFlag)
    {
			throw new AuthorizationFailedException("Group - updateGroup");
    }

		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			GroupLocalHome home = (GroupLocalHome)ic.lookup("local/Group");
			GroupLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			remote.updateGroup(userId,groupDetail);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.updateGroup] Exception Thrown: "+e);
			e.printStackTrace();
		}
  }   // end updateGroup() method

  public void deleteMOC(int mocId, int contactId, int userId)
  {
		try{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			remote.deleteMOC(mocId,  contactId,  userId,true);
			remote.updateModified("moc", contactId, userId, mocId);
		}catch(NamingException re){
			throw new EJBException(re);
		}catch(CreateException ce){
			throw new EJBException(ce);
		}
	}   // end deleteMOC() method

	public Vector getStates()
	{
		Vector col = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			col = remote.getStates();
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getStates] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return col;
	}   // end getStates() method

	public Vector getCountry()
	{
		Vector col = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			col = remote.getCountry();
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getCountry] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return col;
	}   // end getCountry() method

	public Vector getUsers()
	{
		Vector col = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			col = remote.getUsers();
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getUsers] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return col;
	}   // end getUsers() method


	public Vector getGroups()
	{
		Vector col = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			col = remote.getGroups();
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getGroups] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return col;
	}   // end getGroups() method


	public Vector getMOCType()
	{
		Vector col = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			col = remote.getMOCType();
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getMOCType] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return col;
	}   // end getMOCType() method

	public Vector getSyncAs()
	{
		Vector col = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			col = remote.getSyncAs();
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getSyncAs] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return col;
	}   // end getSyncAs() method

	public AddressVO getAddress(int addressId)
	{
		AddressVO avo = null;

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      // Transaction not required cause even if adding member fails its ok.
      avo  = remote.getAddress(addressId);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.getAddress] Exception Thrown: "+e);
      //e.printStackTrace();
    }
    return avo;
  }   // end getAddress() method


	public AddressVO getRelatedAddress(int addressId, int contactType, int contactID)
	{
		AddressVO avo = null;

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      // Transaction not required cause even if adding member fails its ok.
      avo  = remote.getRelatedAddress(addressId, contactType, contactID);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.getAddress] Exception Thrown: "+e);
      //e.printStackTrace();
    }
    return avo;
  }   // end getAddress() method

	public MethodOfContactVO getMOC(int mocId)
	{
		MethodOfContactVO mvo = null;

    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
      ContactHelperLocal remote =  home.create();
      remote.setDataSource(this.dataSource);
      // Transaction not required cause even if adding member fails its ok.
      mvo  = remote.getMoc(mocId);
    }catch(Exception e){
      System.out.println("[Exception][ContactFacadeEJB.getMOC] Exception Thrown: "+e);
      //e.printStackTrace();
    }
		return mvo;
	}   // end getMOC() method

	public Vector getDBList(int userId)
	{
		Vector vecDB = new Vector();

		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactListLocalHome home = (ContactListLocalHome)ic.lookup("local/ContactList");
			ContactListLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			vecDB = (Vector)remote.getDBList(userId);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getDBList] Exception Thrown: "+e);
			//e.printStackTrace();
			return null;
		}
		return vecDB;
	}   // end getDBList() method

  /**
   * This method differs from the getDBList method, in that it
   * doesn't provide any record rights, it is used in Administration
   * It also differs in that it returns the raw values, an ArrayList (Collection)
   * of HashMaps.  The keys on the HashMap are listid and title.  This
   * way you won't have to pull it out of a DDNameValue if that isn't
   * your preferred data transfer vehicle.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return A collection of HashMaps with "listid" and "title".
   */
  public Collection getAllMarketingList()
  {
    ArrayList marketingListNames = new ArrayList();
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      // SELECT listid, title FROM marketinglist
      cvdal.setSql("contact.getAllMarketingList");
      marketingListNames.addAll(cvdal.executeQuery());
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return marketingListNames;
  }   // end getAllMarketingList() method

  /**
   * Calls GroupEJB.duplicateGroup() in order to duplicate a
   * group and its members in the database. Given a groupID,
   * this method duplicates the group record in the database,
   * and also adds the origin group's members to the new group.
   * @param individualID    the individualID of the user who is creating the new group.
   * @param groupID         the groupID orginal group which is to be duplicated.
   * @return  int: newGroupID - the groupID of the newly created group.
   */
  public int duplicateGroup(int individualID, int groupID)
  {
    int newGroupID = 0;   // method will return 0 upon failure
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      GroupLocalHome home = (GroupLocalHome)ic.lookup("local/Group");
      GroupLocal remote = home.create();
      remote.setDataSource(this.dataSource);
      newGroupID = remote.duplicateGroup(individualID, groupID);
    }catch(Exception e){
      System.out.println("[Exception] Exception thrown in ContactFacade.duplicateGroup(): " + e);
      // e.printStackTrace();
		}
		return newGroupID;
  }   // end duplicateGroup()



  /**
   * This method uses the CVUtility method getAllAccessible Records
   * to create a temporary table called entityaccess, which provides
   * all the entity Ids taht the passed individualId is allowed to access
   * based on the public record, record authorisation tables and the owner field.
   *
   * I believe the Entity Lists that are returned are already sufficiently
   * pared down based on this info so I am marking this as deprecated.
   *
   * @deprecated the ContactListEJB returns only the Entities which are accessible.
   * @param individualId
   * @return
   */
	public Vector getEntityAccessRecords(int individualId)
	{
	  Vector allAccRec = new Vector();
	  CVDal cvdl = new CVDal(dataSource);
	  try
      {
        CVUtility.getAllAccessibleRecords("Entity", "entityaccess", "entity", "EntityID", "Owner", null, individualId, cvdl);
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("select EntityID from entityaccess");
        Collection col = cvdl.executeQuery();
        Iterator eit = col.iterator();
        cvdl.setSqlQueryToNull();
        cvdl.setSqlQuery("drop table entityaccess");
        cvdl.executeUpdate();
        if (eit.hasNext())
        {
          while (eit.hasNext())
          {
            HashMap hm = (HashMap)eit.next();
            if (hm != null)
            {
              Number entid = (Number)hm.get("EntityID");
              allAccRec.add(entid);
            }
          }
        }
      } catch (Exception e) {
        System.out.println("[Exception] ContactFacadeEJB.getEntityAccessRecords: " + e.toString());
      } finally {
        cvdl.destroy();
        cvdl = null;
      }
      return allAccRec;
	}   // end getEntityAccessRecords() method

	public Vector getIndividualAccessRecords(int individualId)
	{
		Vector allAccRec = new Vector();
		CVDal cvdl = new CVDal(dataSource);
    try
    {
  		CVUtility.getAllAccessibleRecords("Individual", "individualaccess", "individual", "IndividualID", "Owner", null, individualId, cvdl);
  		cvdl.clearParameters();
  		cvdl.setSqlQuery("select IndividualID from individualaccess");

  		Collection col = cvdl.executeQuery();
  		Iterator eit = col.iterator();
  		cvdl.clearParameters();
  		cvdl.setSqlQuery("drop table individualaccess");
  		cvdl.executeUpdate();

  		if (eit.hasNext())
  		{
  			while (eit.hasNext())
  			{
  				HashMap hm = (HashMap)eit.next();
  				if (hm!=null)
  				{
  					Integer entid = new Integer(((Long)(hm.get("IndividualID"))).intValue());
  					allAccRec.add(entid);
  				}
  			}
  		}
    }catch (Exception e){
      System.out.println("[Exception] ContactFacadeEJB.getIndividualAccessRecords: "  + e);
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
		return allAccRec;
	}   // end getIndividualAccessRecords() method

	public Vector getGroupAccessRecords(int individualId)
	{
		Vector allAccRec = new Vector();
		CVDal cvdl = new CVDal(dataSource);
		CVUtility.getAllAccessibleRecords("Group", "groupaccess", "grouptbl", "GroupID", "owner", null, individualId, cvdl);
		cvdl.clearParameters();
		cvdl.setSqlQuery("select GroupID from groupaccess");

		Collection col = cvdl.executeQuery();
		Iterator eit = col.iterator();

		cvdl.clearParameters();
		cvdl.setSqlQuery("drop table groupaccess");
		cvdl.executeUpdate();

		if (eit.hasNext())
		{
			while (eit.hasNext())
			{
				HashMap hm = (HashMap)eit.next();
				if (hm!=null)
				{
					Integer entid = new Integer(((Long)(hm.get("GroupID"))).intValue());
					//System.out.println("IQ the enitytid is  " + entid);
					allAccRec.add(entid);
				}
			}
		}
		return allAccRec;
	}   // end getGroupAccessRecords() method

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

	public HashMap getCustomerIndividual(int indId)
	{
		HashMap hm=new HashMap();
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");
			IndividualLocal remote =  home.findByPrimaryKey(new IndividualPK(indId,this.dataSource));
			hm = remote.getCustomerIndividualVOWithBasicReferences();
		}catch(Exception e){
			System.out.println("Failed in the ContactFacadewhile getting Individual ");
			//e.printStackTrace();
		}
		return hm;
	}   // end getCustomerIndividual() method


  /**
   * Method to return the entityid and name of the Entity related to a particular
   * individualId.  Much ligter weight than building a whole IndividualVO to get the info
   * This was originally written to support some relatedInfo stuff.
   * @author Kevin McAllister <kevin@centraview.com>
   * @param individualId an int of the IndividualId
   * @return HashMap with two keys "entityid" and "name", if there wasn't a match it will be an empty hashmap.
   * @throws RemoteException per the EJB spec.
   */
  public HashMap getIndividualRelatedEntity(int individualId)
  {
    CVDal cvdl = new CVDal(this.dataSource);
    HashMap relatedEntity = new HashMap();
    try
    {
      // "SELECT entity.entityid, entity.name FROM entity, individual where entity.entityid = individual.entity AND individual.individualid = ?"
      cvdl.setSql("contact.getindividualrelatedentity");
      cvdl.setInt(1, individualId);
      Vector results = (Vector)cvdl.executeQuery();
      Iterator iter = results.iterator();
      if (iter.hasNext())  // we should only get one result, otherwise we will just return the empty hashmap.
      {
        relatedEntity = (HashMap)iter.next();
      }
    }finally{
      cvdl.destroy();
      cvdl = null;
    }
    // The hashmap will have two keys "entityid", "name"
    return relatedEntity;
  } // end of getIndividualRelatedEntity(int)


  /**
  * Deletes a given set of Entity's data which are belonging to the ListID
  * @param listID The List ID which we are going to process and delete the Entity in that List
  */
  // TODO: CHANGE THE NAME OF THIS METHOD!!!!! deleteEntity(int listID)
  public void deleteEntity(int listID) throws AuthorizationFailedException
  {
		CVDal cvdl = new CVDal(this.dataSource);

		try{
			InitialContext ic = CVUtility.getInitialContext();
			EntityLocalHome home = (EntityLocalHome)ic.lookup("local/Entity");
			cvdl.setSqlQuery("SELECT EntityID from entity where list="+listID);
			Collection results = cvdl.executeQuery();

			if (results != null)
			{
				Iterator iter = results.iterator();
				while (iter.hasNext())
				{
					HashMap entityList = (HashMap)iter.next();
					if (entityList != null)
					{
						int entityId = ((Number)entityList.get("EntityID")).intValue();
						EntityLocal remote =  home.findByPrimaryKey(new EntityPK(entityId,this.dataSource));
						ctx.getUserTransaction().begin();
						remote.remove();
						ctx.getUserTransaction().commit();
					}
				}
			}
		}catch(NamingException fe){
			throw new EJBException(fe);
		}catch(RemoveException fe){
			throw new EJBException(fe);
		}catch(FinderException fe){
			System.out.println("[Exception][ContactFacadeEJB.deleteEntity] Exception Thrown: "+fe);
		}catch(NotSupportedException nse){
			throw new EJBException(nse);
		}catch(RollbackException rbe){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Entity - deleteEntity");
		}catch(SystemException se){
			throw new EJBException(se);
		}catch(HeuristicMixedException hme){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Entity - deleteEntity");
		}catch(HeuristicRollbackException hre){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Entity - deleteEntity");
		}

  }   // end deleteEntity() method

  /**
   * Deletes a given set of Individual's data which are belonging to the ListID
   * @param listID The List ID which we are going to process and delete the individual in that List
   */
  // TODO: CHANGE THE NAME OF THIS METHOD!!!!! deleteIndividual(int listID)
  public void deleteIndividual(int listID) throws AuthorizationFailedException
  {
		try{
			CVDal cvdl = new CVDal(this.dataSource);
			InitialContext ic = CVUtility.getInitialContext();
			IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");

			cvdl.setSqlQuery("SELECT IndividualID from individual where list="+listID);
			Collection results = cvdl.executeQuery();

			if (results != null)
			{
				Iterator iter = results.iterator();
				while (iter.hasNext())
				{
					HashMap individualList = (HashMap)iter.next();
					if (individualList != null)
					{
						int individualId = ((Number)individualList.get("EntityID")).intValue();
						IndividualLocal remote =  home.findByPrimaryKey(new IndividualPK(individualId,this.dataSource));
						ctx.getUserTransaction().begin();
						remote.remove();
						ctx.getUserTransaction().commit();
					}
				}
			}
		}catch(NamingException re){
			throw new EJBException(re);
		}catch(FinderException fe){
			System.out.println("[Exception][ContactFacadeEJB.deleteIndividual] Exception Thrown: "+fe);
		}catch(RemoveException fe){
			throw new EJBException(fe);
		}catch(NotSupportedException nse){
			throw new EJBException(nse);
		}catch(RollbackException rbe){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Individual - deleteIndividual");
		}catch(SystemException se){
			throw new EJBException(se);
		}catch(HeuristicMixedException hme){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Individual - deleteIndividual");
		}catch(HeuristicRollbackException hre){
			//TODO we shouldn't do like this Since we aren't parsing the record information.
			// Its a time being hack.
			throw new AuthorizationFailedException("Individual - deleteIndividual");
		}
  }   // end deleteIndividual() method


	/**
	  * This method returns the entity ID for the associated Individual.
	  *
	  * @param individualID The individualID is for the Individual.
	  * @return toList The entityID for the Associated Individual.
	  *
	  */
	public int getEntityIDForIndividual(int individualID)
	{
		int entityID = 0;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
	  		ContactHelperLocal remote =  home.create();
	  		remote.setDataSource(this.dataSource);
	  		entityID  = remote.getEntityIDForIndividual(individualID);
		}catch(Exception e){
	  		System.out.println("[Exception][ContactFacadeEJB.getEntityIDForIndividual] Exception Thrown: "+e);
	  		//e.printStackTrace();
		}
		return entityID;
	}

	/**
	 * This method returns whether an individualID is also
	 * a user in the system.
	 *
	 * @param individualID The individualID to check to see if
	 * it's a user.
	 *
	 * @return true if the individual is also a user, false otherwise.
	 */
	public boolean isIndividualAUser(int individualID){
		boolean isUser = false;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			isUser  = remote.isIndividualAUser(individualID);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getEntityIDForIndividual] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return isUser;
	}

  /**
   * This method returns the String with the email address belonging to the individual/Entity.
   *
   * @param contactID You can pass either EntityID or IndividualID and get their primary emailAddress.
	 * @param contactType It will define wheather you are looking for the Entity or Individual EmailAddress.
   * @return emailAddress It returns the emailAddress belonging to the individual/Entity.
   *
   */
  public String getPrimaryEmailAddress(int contactID,int contactType){
		String emailAddress = null;
		try
		{
			InitialContext ic = CVUtility.getInitialContext();
			ContactHelperLocalHome home = (ContactHelperLocalHome)ic.lookup("local/ContactHelper");
			ContactHelperLocal remote =  home.create();
			remote.setDataSource(this.dataSource);
			emailAddress  = remote.getPrimaryEmailAddress(contactID,contactType);
		}catch(Exception e){
			System.out.println("[Exception][ContactFacadeEJB.getPrimaryEmailAddress] Exception Thrown: "+e);
			//e.printStackTrace();
		}
		return emailAddress;
	}// end of getPrimaryEmailAddress(int contactID,int contactType)

}   // end class definition

