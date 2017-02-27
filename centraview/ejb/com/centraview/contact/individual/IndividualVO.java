/*
 * $RCSfile: IndividualVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:10 $ - $Author: mking_cv $
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

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.DynaActionForm;

import com.centraview.contact.helper.AddressVO;
import com.centraview.contact.helper.ContactVO;
import com.centraview.contact.helper.MethodOfContactVO;

/*
 * This is Individual Object which represent the Individual Data..
 */
public class IndividualVO extends ContactVO
{
  private static Logger logger = Logger.getLogger(IndividualVO.class);
  private int entityID;
  private int individualID;

  private String firstName = "";
  private String middleName = "";
  private String lastName = "";
  private String title = "";
  private String externalID = "";
  private String isPrimaryContact = "";
  private int source;
  private String sourceName = "";
  private String userName = "";
  private int list = 1;
  private String entityName = "";

  private int ownerId = 0;

  public void setUserName(String userName)
  {
    this.userName = userName;
  }

  public String getUserName()
  {
    return this.userName;
  }

  public void setIsPrimaryContact(String pc)
  {
    this.isPrimaryContact = pc;
  }

  public String getIsPrimaryContact()
  {
    return this.isPrimaryContact;
  }

  public int getEntityID()
  {
    return this.entityID;
  }

  /**
   * 
   * @return The owner ID
   */
  public int getOwnerID()
  {
    return this.ownerId;
  }

  /**
   * Set the Owner ID
   * 
   * @param ownerId The new owner ID
   */
  public void setOwnerID(int ownerId)
  {
    this.ownerId = ownerId;
  }

  /**
   * Set the EntityID of which this Individual belongs to.
   * @param entityID Entity ID of this Individual.
   */
  public void setEntityID(int entityID)
  {
    this.entityID = entityID;
  }

  /**
   * @return First Name of Individual.
   */
  public String getFirstName()
  {
    return this.firstName;
  }

  /**
   * Set the First Name of Individual.
   * @param firstName First Name of Individual.
   */
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }

  /**
   * 
   * 
   * @return Individual ID.s
   */
  public int getIndividualID()
  {
    return this.individualID;
  }

  /**
   * Set the IndividualID
   * 
   * @param individualID IndividualID
   */
  public void setIndividualID(int individualID)
  {
    this.individualID = individualID;
  }

  /**
   * 
   * 
   * @return The Last Name of Individual.s
   */
  public String getLastName()
  {
    return this.lastName;
  }

  /**
   * Set the Last Name of Individual
   * 
   * @param lastName Last Name of Individual
   */
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }

  /**
   * 
   * 
   * @return The Middle Name of Individual.
   */
  public String getMiddleName()
  {
    return this.middleName;
  }

  /**
   * Set the Middle Name of Individual
   * 
   * @param middleName Middle Name of Individual
   */
  public void setMiddleName(String middleName)
  {
    this.middleName = middleName;
  }

  /**
   * 
   * 
   * @return The Title of Individual.
   */
  public String getTitle()
  {
    return this.title;
  }

  /**
   * Set the Title of Individual.
   * 
   * @param title Title of Individual.
   */
  public void setTitle(String title)
  {
    this.title = title;
  }

  /**
   * This method returns IndividualVO Object
   * 
   * @return IndividualVO
   */
  public IndividualVO getVO()
  {
    return this;
  }

  /**
   * 
   * 
   * @return The External ID
   */
  public String getExternalID()
  {
    return this.externalID;
  }

  /**
   * Set the ExternalID
   * 
   * @param externalID External ID
   */
  public void setExternalID(String externalID)
  {
    this.externalID = externalID;
  }

  /**
   * 
   * 
   * @return The source of Individual.
   */
  public String getSourceName()
  {
    return this.sourceName;
  }

  /**
   * Set the Source of Individual.
   * 
   * @param source Source of Individual.
   */
  public void setSourceName(String sourceName)
  {
    this.sourceName = sourceName;
  }

  /**
   * 
   * 
   * @return The source of Individual.
   */
  public int getSource()
  {
    return this.source;
  }

  /**
   * Set the Source of Individual.
   * 
   * @param source Source of Individual.
   */
  public void setSource(int source)
  {
    this.source = source;
  }

  public int getList()
  {
    return this.list;
  }

  public void setList(int list)
  {
    this.list = list;
  }

  /**
   * Set the entityName of Individual.
   * 
   * @param entityName entityName of Individual.
   */
  public void setEntityName(String entityName)
  {
    this.entityName = entityName;
  }
  /**
   * Get the entityName of Individual.
   * 
   * @return The entityName of Individual.
   */
  public String getEntityName()
  {
    return this.entityName;
  }

  /**
   * set all the fields on the individualVO given another individualVO This is
   * used in the IndividualVOX to set itself from the one in the database, to
   * preserve data on the fields and not have to rely on absolute perfect field
   * data from the http form.
   * @param individualVO
   */
  public void setVO(IndividualVO individualVO)
  {
    // IndividualVO fields
    this.individualID = individualVO.getIndividualID();
    this.externalID = individualVO.getExternalID();
    this.firstName = individualVO.getFirstName();
    this.middleName = individualVO.getMiddleName();
    this.lastName = individualVO.getLastName();
    this.source = individualVO.getSource();
    this.sourceName = individualVO.getSourceName();
    this.list = individualVO.getList();
    this.entityID = individualVO.getEntityID();
    this.ownerId = individualVO.getOwner();
    this.isPrimaryContact = individualVO.getIsPrimaryContact();
    this.title = individualVO.getTitle();
    this.userName = individualVO.getUserName();
    // ContactVO fields
    this.setContactID(individualVO.getContactID());
    this.setPrimaryAddress(individualVO.getPrimaryAddress());
    this.setCustomFieldVOs(individualVO.getCustomField());
    this.setMoc(individualVO.getMOC());
    this.setModifiedBy(individualVO.getModifiedBy());
    this.setCreatedBy(individualVO.getCreatedBy());
    this.setOwner(individualVO.getOwner());
    this.setModifiedOn(individualVO.getModifiedOn());
    this.setCreatedOn(individualVO.getCreatedOn());
    this.setContacyType(individualVO.getContacyType());
    this.setModifiedByName(individualVO.getModifiedByName());
    this.setCreatedByName(individualVO.getCreatedByName());
  } // end setVO

  public String toString()
  {
    StringBuffer toReturn = new StringBuffer("");

    toReturn.append("Contents of IndividualVO:\n");
    toReturn.append(" - entityID = " + this.entityID + "\n");
    toReturn.append(" - entityName = " + this.entityName + "\n");
    toReturn.append(" - individualID = " + this.individualID + "\n");
    toReturn.append(" - firstName = " + this.firstName + "\n");
    toReturn.append(" - middleName = " + this.middleName + "\n");
    toReturn.append(" - lastName = " + this.lastName + "\n");
    toReturn.append(" - title = " + this.title + "\n");
    toReturn.append(" - externalID = " + this.externalID + "\n");
    toReturn.append(" - isPrimaryContact = " + this.isPrimaryContact + "\n");
    toReturn.append(" - source = " + this.source + "\n");
    toReturn.append(" - sourceName = " + this.sourceName + "\n");
    toReturn.append(" - userName = " + this.userName + "\n");
    toReturn.append(" - list = " + this.list + "\n");
    toReturn.append(super.toString());

    return (toReturn.toString());
  } // end toString() method

  /**
   * Set individual specific contact fields.
   */
  public IndividualVO() {
    super();
    this.setContactType(2); // Default the contact type to 2
  }

  public void populateFormBean(DynaActionForm individualForm)
  {
    individualForm.set("individualId", new Integer(this.getContactID()));
    individualForm.set("marketingListId", new Integer(this.getList()));
    if (this.getFirstName() != null) {
      individualForm.set("firstName", this.getFirstName());
    }
    if (this.getLastName() != null) {
      individualForm.set("lastName", this.getLastName());
    }
    if (this.getMiddleName() != null) {
      individualForm.set("middleInitial", this.getMiddleName());
    }
    if (this.getEntityID() != 0) {
      individualForm.set("entityId", new Integer(this.getEntityID()));
      individualForm.set("entityName", this.getEntityName());
    }
    if (this.getTitle() != null) {
      individualForm.set("title", this.getTitle());
    }
    if (this.getExternalID() != null) {
      individualForm.set("id2", this.getExternalID());
    }
    if (this.getSourceName() != null) {
      individualForm.set("sourceName", this.getSourceName());
    }
    if (this.getSource() > 0) {
      individualForm.set("sourceId", new Integer(this.getSource()));
    }
    String modified = "";
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy ");
    if (this.getModifiedOn() != null) {
      modified = sdf.format(this.getModifiedOn());
    }
    if (this.getModifiedByName() != null) {
      modified += this.getModifiedByName();
    }
    individualForm.set("modified", modified);

    String created = "";
    if (this.getCreatedOn() != null) {
      created = sdf.format(this.getCreatedOn());
    }
    if (this.getCreatedByName() != null) {
      created += this.getCreatedByName();
    }
    individualForm.set("created", created);

    Collection mocList = this.getMOC();
    Iterator iterator = mocList.iterator();
    int count = 1;
    while (iterator.hasNext()) {
      MethodOfContactVO moc = (MethodOfContactVO)iterator.next();
      // this is for email
      if (moc.getMocType() == 1 && moc.getIsPrimary().equalsIgnoreCase("YES")) {
        individualForm.set("email", moc.getContent());
        individualForm.set("emailId", String.valueOf(moc.getMocID()));
      } else if (count < 4 && moc.getMocType() != 1) {
        individualForm.set("mocType" + count, String.valueOf(moc.getMocType()));
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
        individualForm.set("mocContent" + count, mocContentValue);
        individualForm.set("mocExt" + count, mocContentExt);
        individualForm.set("mocId" + count, String.valueOf(moc.getMocID()));
        count++;
      }
    } // end of while

    AddressVO address = this.getPrimaryAddress();
    if (address != null) {
      if (address.getAddressID() != 0) {
        individualForm.set("addressId", new Integer(address.getAddressID()));
      }
      if (address.getStreet1() != null) {
        individualForm.set("street1", address.getStreet1());
      }
      if (address.getStreet2() != null) {
        individualForm.set("street2", address.getStreet2());
      }
      if (address.getCity() != null) {
        individualForm.set("city", address.getCity());
      }
      if (address.getStateName() != null) {
        individualForm.set("state", address.getStateName());
      }
      if (address.getZip() != null) {
        individualForm.set("zip", address.getZip());
      }
      if (address.getCountryName() != null) {
        individualForm.set("country", address.getCountryName());
      }
      if (address.getWebsite() != null) {
        individualForm.set("website", address.getWebsite());
      }
    }
  } // end of populateFormBean()
  /**
   * returns a printable full name for display purposes
   * @return String the name.
   */
  public String getFullName()
  {
    StringBuffer name = new StringBuffer();
    name.append(this.getFirstName());
    name.append(" ");
    if (this.getMiddleName() != null && this.getMiddleName().length() > 0) {
      name.append(this.getMiddleName());
      name.append(". ");
    }
    name.append(this.getLastName());
    return name.toString();
  }
}