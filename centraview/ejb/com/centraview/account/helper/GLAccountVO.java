/*
 * $RCSfile: GLAccountVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:26 $ - $Author: mking_cv $
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

import java.io.Serializable;

public class GLAccountVO implements Serializable
{
  private int glaccountID;
  private String title;
  private String description;
  private float balance;
  private String glAccountType;
  private int parentAccountID;
  private String externalID;



  /** return the Current Balance Amount of this account
   *
   *
   * @return float Balance of this account
   */
  public float getBalance()
  {
    return this.balance;
  }


  /** sets the Balance of this Account
   *
   *
   * @param float Balance amount to set
   */
  public void setBalance(float balance)
  {
    this.balance = balance;
  }



  /** returns Description about this account
   *
   *
   * @return String description of this account
   */
  public String getDescription()
  {
    return this.description;
  }


  /** sets the Description of this account
   *
   *
   * @param String description of this account
   */
  public void setDescription(String description)
  {
    this.description = description;
  }



  /**returns the ID of this account
   *
   *
   * @return int ID of this account
   */
  public int getGlaccountID()
  {
    return this.glaccountID;
  }


  /** sets the ID of this account
   *
   *
   * @param int glaccountID to set
   */
  public void setGlaccountID(int glaccountID)
  {
    this.glaccountID = glaccountID;
  }



  /** returns the AccountType of this account
   *
   *
   * @return String AccountType of this account
   */
  public String getGLAccountType()
  {
    return this.glAccountType;
  }


  /**sets the AccountType of this account
   *
   *
   * @param String glaccountType to set
   */
  public void setGLAccountType(String glAccountType)
  {
    this.glAccountType = glAccountType;
  }



  /** returns the id of parent account of this account
   *
   *
   * @return int Parent AccountID
   */
  public int getParentAccountID()
  {
    return this.parentAccountID;
  }


  /**sets the id of parentAccount of this account
   *
   *
   * @param int parentAccountID to set
   */
  public void setParentAccountID(int parentAccountID)
  {
    this.parentAccountID = parentAccountID;
  }



  /**returns the Title of this account
   *
   *
   * @return String Title of this account
   */
  public String getTitle()
  {
    return this.title;
  }


  /**sets the Title of this acoount
   *
   *
   * @param String title to set
   */
  public void setTitle(String title)
  {
    this.title = title;
  }



  /**
   *
   *
   * @return
   */
  public String getExternalID()
  {
    return this.externalID;
  }


  /**
   *
   *
   * @param   externalID
   */
  public void setExternalID(String externalID)
  {
    this.externalID = externalID;
  }
}
