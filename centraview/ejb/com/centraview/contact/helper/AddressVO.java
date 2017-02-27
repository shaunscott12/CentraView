/*
 * $RCSfile: AddressVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:06 $ - $Author: mking_cv $
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

public class AddressVO implements Serializable
{
  private int addressID;
  private int addressType = 1;
  private String street1;
  private String street2;
  private String city;
  private String stateName;
  private String zip;
  private String countryName;
  private String isPrimary = "NO";
  private String website;
  private int jurisdictionID;

  /**
   * @return The Address ID.
   */
  public int getAddressID()
  {
    return this.addressID;
  }

  /**
   * Set the AddressID
   * 
   * @param addressID
   */
  public void setAddressID(int addressID)
  {
    this.addressID = addressID;
  }

  /**
   * Set teh AddressType ID
   * 
   * @return The AddressType ID.
   */
  public int getAddressType()
  {
    return this.addressType;
  }

  public void setAddressType(int addressType)
  {
    this.addressType = addressType;
  }

  /**
   * @return The City Name
   */
  public String getCity()
  {
    return this.city;
  }

  /**
   * Set the City Name
   * 
   * @param city
   */
  public void setCity(String city)
  {
    this.city = city;
  }

  /**
   * @return The Street1 Name
   */
  public String getStreet1()
  {
    return this.street1;
  }

  /**
   * Set the street1 name
   * 
   * @param street1
   */
  public void setStreet1(String street1)
  {
    this.street1 = street1;
  }

  /**
   * @return The Street2 name
   */
  public String getStreet2()
  {
    return this.street2;
  }

  /**
   * Set the Street2 Name
   * 
   * @param street2
   */
  public void setStreet2(String street2)
  {
    this.street2 = street2;
  }

  /**
   * @return The Zip
   */
  public String getZip()
  {
    return this.zip;
  }

  /**
   * Set the Zip Name
   * 
   * @param zip
   */
  public void setZip(String zip)
  {
    this.zip = zip;
  }

  /**
   * @return true if Address is Primary else false
   */
  public String getIsPrimary()
  {
    return this.isPrimary;
  }

  /**
   * Set YES if Address is Primary else NO.
   * 
   * @param isPrimary
   */
  public void setIsPrimary(String isPrim)
  {
    this.isPrimary = isPrim;
  }

  /**
   * @return The Website Name.
   */
  public String getWebsite()
  {
    return this.website;
  }

  /**
   * Set the website Name.
   * 
   * @param website
   */
  public void setWebsite(String website)
  {
    this.website = website;
  }

  /**
   * @return The AddressVO object
   */
  public AddressVO getVO()
  {
    return this;
  }

  public AddressVO()
  {
    super();
  }

  public String getStateName()
  {
    return this.stateName;
  }

  public void setStateName(String stateName)
  {
    this.stateName = stateName;
  }

  public String getCountryName()
  {
    return this.countryName;
  }

  public void setCountryName(String countryName)
  {
    this.countryName = countryName;
  }

  /**
   * @return The jurisdiction ID.
   */
  public int getJurisdictionID()
  {
    return this.jurisdictionID;
  }

  /**
   * Set the Jurisdiction ID
   * 
   * @param jurisdictionID
   */
  public void setJurisdictionID(int jurisdictionID)
  {
    this.jurisdictionID = jurisdictionID;
  }

  public boolean isEmpty()
  {
    StringBuffer buffer = new StringBuffer();
    buffer.append(street1);
    buffer.append(street2);
    buffer.append(city);
    buffer.append(stateName);
    buffer.append(zip);
    buffer.append(countryName);
    buffer.append(website);
    if (buffer.length() < 1)
    {
      return true;
    }
    return false;
  }
  
  public String toString()
  {
    StringBuffer string = new StringBuffer();
    string.append("AddressVO: {addressID: " + addressID);
    string.append(", addressType: " + addressType);
    string.append(", street1: " + street1);
    string.append(", street2: " + street2);
    string.append(", city: " + city);
    string.append(", state: " + stateName);
    string.append(", zip: " + zip);
    string.append(", country: " + countryName);
    string.append(", website: " + website);
    string.append(", isPrimary: " + isPrimary);
    string.append(", jurisdictionID: " + jurisdictionID);
    return string.toString();
  }
}