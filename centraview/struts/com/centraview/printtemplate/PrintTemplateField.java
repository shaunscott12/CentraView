/*
 * $RCSfile: PrintTemplateField.java,v $    $Revision: 1.2 $  $Date: 2005/08/01 21:05:13 $ - $Author: mcallist $
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

package com.centraview.printtemplate;

import java.io.Serializable;

public class PrintTemplateField implements Serializable {
  private String IndividualID = "";
  private String EntityID = "";
  private String Company = "";
  private String FirstName = "";
  private String MiddleInitial = "";
  private String LastName = "";
  private String Title = "";
  private String Street1 = "";
  private String Street2 = "";
  private String City = "";
  private String State = "";
  private String Zip = "";
  private String Country = "";
  private String Website = "";
  private String ExternalID = "";
  private String Source = "";
  private String Fax = "";
  private String Home = "";
  private String Main = "";
  private String Mobile = "";
  private String Other = "";
  private String Pager = "";
  private String Work = "";
  private String Email = "";

  public String getIndividualID()
  {
    return this.IndividualID;
  }

  public void setIndividualID(String IndividualID)
  {
    this.IndividualID = IndividualID;
  }

  public String getEntityID()
  {
    return this.EntityID;
  }

  public void setEntityID(String EntityID)
  {
    this.EntityID = EntityID;
  }

  public String getCompany()
  {
    return this.Company;
  }

  public void setCompany(String Company)
  {
    this.Company = Company;
  }

  public String getFirstName()
  {
    return this.FirstName;
  }

  public void setFirstName(String FirstName)
  {
    this.FirstName = FirstName;
  }

  public String getMiddleInitial()
  {
    return this.MiddleInitial;
  }

  public void setMiddleInitial(String MiddleInitial)
  {
    this.MiddleInitial = MiddleInitial;
  }

  public String getLastName()
  {
    return this.LastName;
  }

  public void setLastName(String LastName)
  {
    this.LastName = LastName;
  }

  public String getTitle()
  {
    return this.Title;
  }

  public void setTitle(String Title)
  {
    this.Title = Title;
  }

  public String getPrimaryAddress()
  {
    if (this.getStreet1() != null && this.getStreet1().equals("null")) {
      this.setStreet1("");
    }
    if (this.getStreet1() == null) {
      this.setStreet1("");
    }
    if (this.getCity() != null && this.getCity().equals("null")) {
      this.setCity("");
    }
    if (this.getCity() == null) {
      this.setCity("");
    }

    if (this.getStreet2() != null && this.getStreet2().equals("null")) {
      this.setStreet2("");
    }

    if (this.getStreet2() == null) {
      this.setStreet2("");
    }

    if (this.getState() != null && this.getState().equals("null")) {
      this.setState("");
    }
    if (this.getState() == null) {
      this.setState("");
    }

    if (this.getZip() != null && this.getZip().equals("null")) {
      this.setZip("");
    }

    if (this.getZip() == null) {
      this.setZip("");
    }

    if (this.getCountry() != null && this.getCountry().equals("null")) {
      this.setCountry("");
    }

    if (this.getCountry() == null) {
      this.setCountry("");
    }

    String tempAddress = "";
    if (this.getStreet1() != null && !this.getStreet1().equals("")) {
      tempAddress = tempAddress + this.getStreet1() + "<br>";
    }
    if (this.getStreet2() != null && !this.getStreet2().equals("")) {
      tempAddress = tempAddress + this.getStreet2() + "<br>";
    }
    if (this.getCity() != null && !this.getCity().equals("")) {
      tempAddress = tempAddress + this.getCity() + ",";
    }
    if (this.getState() != null && !this.getState().equals("")) {
      tempAddress = tempAddress + " " + this.getState();
    }
    if (this.getZip() != null && !this.getZip().equals("")) {
      tempAddress = tempAddress + " " + this.getZip() + "<br>";
    }
    if (this.getCountry() != null && !this.getCountry().equals("")) {
      tempAddress = tempAddress + " " + this.getCountry() + "<br>";
    }

    return tempAddress;
  }

  public String getStreet1()
  {
    return this.Street1;
  }

  public void setStreet1(String Street1)
  {
    this.Street1 = Street1;
  }

  public String getStreet2()
  {
    return this.Street2;
  }

  public void setStreet2(String Street2)
  {
    this.Street2 = Street2;
  }

  public String getCity()
  {
    return this.City;
  }

  public void setCity(String City)
  {
    this.City = City;
  }

  public String getState()
  {
    return this.State;
  }

  public void setState(String State)
  {
    this.State = State;
  }

  public String getZip()
  {
    return this.Zip;
  }

  public void setZip(String Zip)
  {
    this.Zip = Zip;
  }

  public String getCountry()
  {
    return this.Country;
  }

  public void setCountry(String Country)
  {
    this.Country = Country;
  }

  public String getWebsite()
  {
    String tempWebsite = "";
    if (this.Website != null && this.Website.equals("null")) {
      this.Website = "";
    } else if (this.Website == null) {
      this.Website = "";
    } else if (!this.Website.equals("")) {
      tempWebsite = "Website : " + this.Website;
    }
    return tempWebsite;
  }

  public void setWebsite(String Website)
  {
    this.Website = Website;
  }

  public String getExternalID()
  {
    String tempExternalID = "";
    if (this.ExternalID != null && this.ExternalID.equals("null")) {
      this.ExternalID = "";
    } else if (this.ExternalID == null) {
      this.ExternalID = "";
    } else if (!this.ExternalID.equals("")) {
      tempExternalID = "External ID : " + this.ExternalID;
    }
    return tempExternalID;
  }

  public void setExternalID(String ExternalID)
  {
    this.ExternalID = ExternalID;
  }

  public String getSource()
  {
    String tempSource = "";
    if (this.Source != null && this.Source.equals("null")) {
      this.Source = "";
    } else if (this.Source == null) {
      this.Source = "";
    } else if (!this.Source.equals("")) {
      tempSource = "Source : " + this.Source;
    }
    return tempSource;
  }

  public void setSource(String Source)
  {
    this.Source = Source;
  }

  public String getFax()
  {
    String tempFax = "";
    if (this.Fax != null && this.Fax.equals("null")) {
      this.Fax = "";
    } else if (this.Fax == null) {
      this.Fax = "";
    } else if (!this.Fax.equals("")) {
      tempFax = "Fax Phone : " + this.Fax;
    }
    return tempFax;
  }

  public void setFax(String Fax)
  {
    this.Fax = Fax;
  }

  public String getHome()
  {
    String tempHome = "";
    if (this.Home != null && this.Home.equals("null")) {
      this.Home = "";
    } else if (this.Home == null) {
      this.Home = "";
    } else if (!this.Home.equals("")) {
      tempHome = "Home Phone : " + this.Home;
    }
    return tempHome;
  }

  public void setHome(String Home)
  {
    this.Home = Home;
  }

  public String getMain()
  {
    String tempMain = "";
    if (this.Main != null && this.Main.equals("null")) {
      this.Main = "";
    } else if (this.Main == null) {
      this.Main = "";
    } else if (!this.Main.equals("")) {
      tempMain = "Main Phone : " + this.Main;
    }
    return tempMain;
  }

  public void setMain(String Main)
  {
    this.Main = Main;
  }

  public String getMobile()
  {
    String tempMobile = "";
    if (this.Mobile != null && this.Mobile.equals("null")) {
      this.Mobile = "";
    } else if (this.Mobile == null) {
      this.Mobile = "";
    } else if (!this.Mobile.equals("")) {
      tempMobile = "Mobile Phone : " + this.Mobile;
    }
    return tempMobile;
  }

  public void setMobile(String Mobile)
  {
    this.Mobile = Mobile;
  }

  public String getOther()
  {
    String tempOther = "";
    if (this.Other != null && this.Other.equals("null")) {
      this.Other = "";
    } else if (this.Other == null) {
      this.Other = "";
    } else if (!this.Other.equals("")) {
      tempOther = "Other Phone : " + this.Other;
    }
    return tempOther;
  }

  public void setOther(String Other)
  {
    this.Other = Other;
  }

  public String getPager()
  {
    String tempPager = "";
    if (this.Pager != null && this.Pager.equals("null")) {
      this.Pager = "";
    } else if (this.Pager == null) {
      this.Pager = "";
    } else if (!this.Pager.equals("")) {
      tempPager = "Pager Phone : " + this.Pager;
    }
    return tempPager;
  }

  public void setPager(String Pager)
  {
    this.Pager = Pager;
  }

  public String getWork()
  {
    String tempWork = "";
    if (this.Work != null && this.Work.equals("null")) {
      this.Work = "";
    } else if (this.Work == null) {
      this.Work = "";
    } else if (!this.Work.equals("")) {
      tempWork = "Work Phone : " + this.Work;
    }
    return tempWork;
  }

  public void setWork(String Work)
  {
    this.Work = Work;
  }

  public String getEmail()
  {

    return this.Email;
  }

  public void setEmail(String Email)
  {
    this.Email = Email;
  }

} // end class definition

