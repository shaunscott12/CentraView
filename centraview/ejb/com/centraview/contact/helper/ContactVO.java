/*
 * $RCSfile: ContactVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:08 $ - $Author: mking_cv $
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


package com.centraview.contact.helper;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

/*
 *  This is Contact Value Object which represent the Contact Data.
 *	This Class implements Serializable Interface.
 */

public class ContactVO implements Serializable
{
  private int contactID;
  private AddressVO primaryAddress;
  private Vector customFields; // collection of customfieldVO
  private Vector moc; // collection of MethodOfContactVO
  private MethodOfContactVO mocVO;
  private int modifiedBy;
  private int createdBy;
  private int owner;
  private Date modifiedOn;
  private Date createdOn;
  private int contacyType; // contactType 1 = Entity, 2 = Individual
  private String modifiedByName = "";
  private String createdByName = "";


  public ContactVO()
  {
    this.moc = new Vector();
    this.customFields = new Vector();
    this.primaryAddress = new AddressVO();
    this.mocVO =new MethodOfContactVO();
  }
  public String getContent(Vector vec,int mocType)
  {
    int size = vec.size();
    for(int i=0;i < size;i++)
    {
      mocVO = (MethodOfContactVO)vec.get(i);
      if (mocVO.getMocType() == mocType)
      {
        return mocVO.getContent();
      }
    }
    return null;
  }
  public String getPhone()
  {
    return this.getContent(this.getMOC(),4);
  }
  public void setPhone(String phone)
  {
    MethodOfContactVO mofcVO = new MethodOfContactVO();
    mofcVO.setMocType(4);
    mofcVO.setIsPrimary("YES");
    mofcVO.setContent(phone);
    this.setMOC(mofcVO);
  }
  public String getEmail()
  {
    return this.getContent(this.getMOC(),1);
  }
  public void setEmail(String email)
  {
    MethodOfContactVO mofcVO = new MethodOfContactVO();
    mofcVO.setMocType(1);
    mofcVO.setIsPrimary("YES");
    mofcVO.setContent(email);
    this.setMOC(mofcVO);
  }
  public String getFax()
  {
    return this.getContent(this.getMOC(),2);
  }
  public void setFax(String fax)
  {
    MethodOfContactVO mofcVO = new MethodOfContactVO();
    mofcVO.setIsPrimary("YES");
    mofcVO.setMocType(2);
    mofcVO.setContent(fax);
    this.setMOC(mofcVO);
  }
  public String getMobile()
  {
    return this.getContent(this.getMOC(),3);
  }
  public void setMobile(String mobile)
  {
    MethodOfContactVO mofcVO = new MethodOfContactVO();
    mofcVO.setIsPrimary("YES");
    mofcVO.setMocType(3);
    mofcVO.setContent(mobile);
    this.setMOC(mofcVO);
  }

  public int getContactType()
  {
    return this.contacyType;
  }

  public void setContactType(int i)
  {
    this.contacyType = i;
  }

  /**
   *	Return Contact ID
   *
   * @return    Contact ID
   */
  public int getContactID()
  {
    return this.contactID;
  }

  /**
   *	Set the ContactID. This may be EntityID as well as IndividualID.
   *
   * @param   contactID  ContactID.
   */
  public void setContactID(int contactID)
  {
    this.contactID = contactID;
  }

  /**
   *
   *
   * @return  The CreatedBy ID
   */
  public int getCreatedBy()
  {
    return this.createdBy;
  }

  /**
   *	Set the Created By ID
   *
   * @param   createdBy  CreatedBy ID.
   */
  public void setCreatedBy(int createdBy)
  {
    this.createdBy = createdBy;
  }

  /**
   *
   *
   * @return  The CreatedOn Date.
   */
  public Date getCreatedOn()
  {
    return this.createdOn;
  }

  /**
   *	Set the CreatedOn Date.
   *
   * @param   createdOn  CreatedOn Date
   */
  public void setCreatedOn(Date createdOn)
  {
    this.createdOn = createdOn;
  }

  /**
   *
   *
   * @return   The Collection of CustomFields.
   */
  public Vector getCustomField()
  {
    return this.customFields;
  }

  /**
   *	Set the Collection of CustomFields.
   *
   * @param   customFields  Collection of CustomFields
   */
  public void setCustomField(CustomFieldVO customField)
  {
    this.customFields.add(customField);
  }

  /**
   *	Set the Collection of CustomFieldVO.
   *
   * @param   customFields  Collection of CustomFields
   */

  public void setCustomFieldVOs(Vector vec)
  {
    this.customFields = vec;
  }

  /**
   *
   *
   * @return   The modifiedBy ID.
   */
  public int getModifiedBy()
  {
    return this.modifiedBy;
  }

  /**
   *	Set ModifiedByID
   *
   * @param   modifiedBy  ModifiedBy ID.
   */
  public void setModifiedBy(int modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  }

  public void setCustomType(String name)
  {
    CustomFieldVO cfVO = new CustomFieldVO();
    cfVO.setFieldType("MULTIPLE");
    cfVO.setFieldID(1);
    if (name.equals("Customer"))
    {
      cfVO.setValue("1");
    }
    else
    {
        cfVO.setValue("2");
    }
    this.setCustomField(cfVO);
    this.setCustomFieldVOs(this.customFields);
  }
  public Date getModifiedOn()
  {
    return this.modifiedOn;
  }

  public void setModifiedOn(Date modifiedOn)
  {
    this.modifiedOn = modifiedOn;
  }

  /**
   *
   *
   * @return  The Owner
   */
  public int getOwner()
  {
    return this.owner;
  }

  /**
   *	Set owner ID
   *
   * @param   owner  OwnerID
   */
  public void setOwner(int owner)
  {
    this.owner = owner;
  }

  /**
   *
   *
   * @return     The Primary Address for this Contact.
   */
  public AddressVO getPrimaryAddress()
  {
    return this.primaryAddress;
  }

  /**
   *	Set the PrimaryAddress for the Contact.
   *
   * @param   primaryAddress  AddressVO Object.
   */
  public void setPrimaryAddress(AddressVO primaryAddress)
  {
    this.primaryAddress = primaryAddress;
  }

  /**
   *
   *
   * @return   The Collection of Methood of Contact.
   */
  public Vector getMOC()
  {
    return this.moc;
  }

  /**
   *	Set the method of Contacts.
   *
   * @param   primaryMOC  Collection of Method of Contacts
   */
  public void setMOC(MethodOfContactVO mocvo)
  {
    this.moc.add(mocvo);
  }

  public void clearMOC()
  {
    this.moc.clear();
  }

  public String getModifiedByName()
  {
    return this.modifiedByName;
  }

  public void setModifiedByName(String modifiedByName)
  {
    this.modifiedByName = modifiedByName;
  }

  public String getCreatedByName()
  {
    return this.createdByName;
  }

  public void setCreatedByName(String createdByName)
  {
    this.createdByName = createdByName;
  }


  /**
   * Returns a string representing the <b>structure</b> of this
   * object. Over-rides the toString() method of Object so that
   * printing this object into the log file shows useful
   * information. This is especially useful for debugging.
   * @return String representation of this object.
   */
  public String toString()
  {
    StringBuffer toReturn = new StringBuffer();
    toReturn.append(" - contactID = " + this.contactID + "\n");
    toReturn.append(" - modifiedBy = " + this.modifiedBy + "\n");
    toReturn.append(" - createdBy = " + this.createdBy + "\n");
    toReturn.append(" - owner = " + this.owner + "\n");
    toReturn.append(" - modifiedOn = " + this.modifiedOn + "\n");
    toReturn.append(" - createdOn = " + this.createdOn + "\n");
    toReturn.append(" - contacyType = " + this.contacyType + "\n");
    toReturn.append(" - modifiedByName = " + this.modifiedByName + "\n");
    toReturn.append(" - primaryAddress = " + this.primaryAddress + "\n"); //AddressVO
    toReturn.append(" - customFields = " + this.customFields + "\n"); //Vector
    toReturn.append(" - moc =\n");

    Iterator iter = moc.iterator();
    while (iter.hasNext())
    {
      MethodOfContactVO tmpVO = (MethodOfContactVO)iter.next();
      toReturn.append("   - " + tmpVO + "\n"); // Vector
    }
    return (toReturn.toString());
  } // end toString() method
  /**
   * @return Returns the contacyType.
   */
  public int getContacyType()
  {
    return this.contacyType;
  }

  /**
   * @return Returns the customFields.
   */
  public Vector getCustomFields()
  {
    return this.customFields;
  }

  /**
   * @param contacyType The contacyType to set.
   */
  public void setContacyType(int contacyType)
  {
    this.contacyType = contacyType;
  }

  /**
   * @param customFields The customFields to set.
   */
  public void setCustomFields(Vector customFields)
  {
    this.customFields = customFields;
  }

  /**
   * @param moc The moc to set.
   */
  public void setMoc(Vector moc)
  {
    this.moc = moc;
  }

} // end class definition
