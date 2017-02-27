/*
 * $RCSfile: EntityVO.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import org.apache.struts.action.DynaActionForm;

import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactVO;
import com.centraview.contact.helper.MethodOfContactVO;
import com.centraview.contact.individual.IndividualVO;

public class EntityVO extends ContactVO
{
  private String externalID = "";
  private String name = "";
  private int source;
  private String sourceName;
  private int dBase;
  private int list = 1;
  private int accManager; // Individual ID
  private int accTeam; // groupID
  private int individualID;
  private IndividualVO individualVO;
  private int ownerId;
  private String acctMgrName = "";
  private String acctTeamName = "";

  public EntityVO()
  {
    super();
  }

  /**
   *
   *
   * @return   The AccManager ID
   */
  public int getAccManager()
  {
    return this.accManager;
  }

  /**
   *	Set the Acc.Managet ID ie Individual ID.
   *
   * @param   accManager  The new AccManager ID
   */
  public void setAccManager(int accManager)
  {
    this.accManager = accManager;
  }

  /**
   *
   *
   * @return   The  AccTeam ID.
   */
  public int getAccTeam()
  {
    return this.accTeam;
  }

  /**
   *	Set the Acc.Team ID ie Group ID.
   *
   * @param   accTeam  The new GroupID.
   */
  public void setAccTeam(int accTeam)
  {
    this.accTeam = accTeam;
  }

  /**
   *
   *
   * @return    The DataBase ID
   */
  public int getDBase()
  {
    return this.dBase;
  }

  /**
   *	Set the Database ID.
   *
   * @param   dBase  DataBase ID.
   */
  public void setDBase(int dBase)
  {
    this.dBase = dBase;
  }

  /**
   *
   *
   * @return  The External ID for this Entity.
   */
  public String getExternalID()
  {
    return this.externalID;
  }

  /**
   *	Set the External System ID into our System.
   *
   * @param   externalID  ExternalID
   */
  public void setExternalID(String externalID)
  {
    this.externalID = externalID;
  }

  /**
   *
   *
   * @return
   */
  public int getList()
  {
    return this.list;
  }

  /**
   *
   *
   * @param   list
   */
  public void setList(int list)
  {
    this.list = list;
  }

  /**
   *
   *
   * @return The Name of Entity.
   */
  public String getName()
  {
    return this.name;
  }

  /**
   *  Set the Name of Entity
   *
   * @param   name  Name of Entity
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   *
   *
   * @return The Source Name.
   */
  public int getSource()
  {
    return this.source;
  }

  /**
   *	Set the Source Name
   *
   * @param   source  Source name
   */
  public void setSource(int source)
  {
    this.source = source;
  }

  public void setSourceName(String sourceName)
  {
    this.sourceName = sourceName;
  }

  public String getSourceName()
  {
    return sourceName;
  }

  /**
   *
   *
   * @return    The Primary IndividualValue Object
   */
  public IndividualVO getIndividualVO()
  {
    return this.individualVO;
  }

  /**
   *	Set the Primary Individual Object
   *
   * @param   individualVO  IndividualVO object
   */
  public void setIndividualVO(IndividualVO individualVO)
  {
    this.individualVO = individualVO;
  }

  /**
   *
   *
   * @return  The Primary Individual ID for this Entity
   */
  public int getIndividualID()
  {
    return this.individualID;
  }

  /**
   *	Set the Primary IndividualID for this Entity
   *
   * @param   individualID  Individual ID
   */
  public void setIndividualID(int individualID)
  {
    this.individualID = individualID;
  }

  public String getAcctMgrName()
  {
    return this.acctMgrName;
  }
  public void setAcctMgrName(String acctMgrName)
  {
    this.acctMgrName = acctMgrName;
  }

  public String getAcctTeamName()
  {
    return this.acctTeamName;
  }
  public void setAcctTeamName(String acctTeamName)
  {
    this.acctTeamName = acctTeamName;
  }

  /**
   *  This method returns EntityVO Object
   *
   * @return     EntityVO
   */
  public EntityVO getVO()
  {
    return this;
  }

  public int getOwnerId() {
    return this.ownerId;
  }
  /**
   * @param entityVO
   */
  public void setVO(EntityVO entityVO)
  {
    // EntityVO fields
    this.externalID = entityVO.getExternalID();
    this.name = entityVO.getName();
    this.source = entityVO.getSource();
    this.sourceName = entityVO.getSourceName();
    this.dBase = entityVO.getDBase();
    this.list = entityVO.getList();
    this.accManager = entityVO.getAccManager();
    this.accTeam = entityVO.getAccTeam();
    this.individualID = entityVO.getIndividualID();
    this.individualVO = entityVO.getIndividualVO();
    this.ownerId = entityVO.getOwner();
    this.acctMgrName = entityVO.getAcctMgrName();
    this.acctTeamName = entityVO.getAcctTeamName();
    // ContactVO fields
    this.setContactID(entityVO.getContactID());
    this.setPrimaryAddress(entityVO.getPrimaryAddress());
    this.setCustomFieldVOs(entityVO.getCustomField());
    this.setMoc(entityVO.getMOC());
    this.setModifiedBy(entityVO.getModifiedBy());
    this.setCreatedBy(entityVO.getCreatedBy());
    this.setOwner(entityVO.getOwner());
    this.setModifiedOn(entityVO.getModifiedOn());
    this.setCreatedOn(entityVO.getCreatedOn());
    this.setContacyType(entityVO.getContacyType());
    this.setModifiedByName(entityVO.getModifiedByName());
  }

  public void populateFormBean(DynaActionForm entityForm)
  {
    entityForm.set("entityId", new Integer(this.getContactID()));
    entityForm.set("entityName", this.getName());
    entityForm.set("marketingListId", new Integer(this.getList()));
    String modified = "";
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy ");
    if (this.getModifiedOn() != null) {
      modified = sdf.format(this.getModifiedOn());
    }
    if (this.getModifiedByName() != null) {
      modified += this.getModifiedByName();
    }
    entityForm.set("modified", modified);
    String created = "";
    if (this.getCreatedOn() != null) {
      created = sdf.format(this.getCreatedOn());
    }
    if (this.getCreatedByName() != null) {
      created += this.getCreatedByName();
    }
    entityForm.set("created", created);

    Collection mocList = this.getMOC();
    Iterator iterator = mocList.iterator();
    int count = 1;
    while (iterator.hasNext()) {
      MethodOfContactVO moc = (MethodOfContactVO)iterator.next();
      // this is for email
      if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) {
        entityForm.set("email", moc.getContent());
        entityForm.set("emailId", String.valueOf(moc.getMocID()));
      } else if (count < 4 && moc.getMocType() != 1) {
        entityForm.set("mocType" + count, String.valueOf(moc.getMocType()));
        String mocContent = moc.getContent();
        String mocContentValue = "";
        String mocContentExt = "";
        if (mocContent.indexOf("EXT") != -1) {
          String tempContent = mocContent;
          mocContentValue = tempContent.substring(0, tempContent.indexOf("EXT"));
          mocContentExt = tempContent.substring(tempContent.indexOf("EXT") + 3, tempContent.length());
        } else {
          mocContentValue = mocContent;
        }
        entityForm.set("mocContent" + count, mocContentValue);
        entityForm.set("mocExt" + count, mocContentExt);
        entityForm.set("mocId" + count, String.valueOf(moc.getMocID()));
        count++;
      }
    }
    IndividualVO individualVO = this.getIndividualVO();
    if (individualVO != null) {
      entityForm.set("pcIndividualId", new Integer(individualVO.getContactID()));
      if (individualVO.getFirstName() != null) {
        entityForm.set("pcFirstName", individualVO.getFirstName());
      }
      if (individualVO.getMiddleName() != null) {
        entityForm.set("pcMiddleInitial", individualVO.getMiddleName());
      }
      if (individualVO.getLastName() != null) {
        entityForm.set("pcLastName", individualVO.getLastName());
      }
      if (individualVO.getTitle() != null) {
        entityForm.set("pcTitle", individualVO.getTitle());
      }
    }
    AddressVO address = this.getPrimaryAddress();
    if (address != null) {
      if (address.getAddressID() != 0) {
        entityForm.set("addressId", new Integer(address.getAddressID()));
      }
      if (address.getStreet1() != null) {
        entityForm.set("street1", address.getStreet1());
      }
      if (address.getStreet2() != null) {
        entityForm.set("street2", address.getStreet2());
      }
      if (address.getCity() != null) {
        entityForm.set("city", address.getCity());
      }
      if (address.getStateName() != null) {
        entityForm.set("state", address.getStateName());
      }
      if (address.getZip() != null) {
        entityForm.set("zip", address.getZip());
      }
      if (address.getCountryName() != null) {
        entityForm.set("country", address.getCountryName());
      }
      if (address.getWebsite() != null) {
        entityForm.set("website", address.getWebsite());
      }
    }
    if (this.getSourceName() != null) {
      entityForm.set("sourceName", this.getSourceName());
    }
    if (this.getSource() > 0) {
      entityForm.set("sourceId", new Integer(this.getSource()));
    }
    entityForm.set("accountManagerId", new Integer(this.getAccManager()));
    entityForm.set("accountManagerName", this.getAcctMgrName());
    entityForm.set("accountTeamId", new Integer(this.getAccTeam()));
    entityForm.set("accountTeamName", this.getAcctTeamName());
    if (this.getExternalID() != null) {
      entityForm.set("id2", this.getExternalID());
    }
  }
}