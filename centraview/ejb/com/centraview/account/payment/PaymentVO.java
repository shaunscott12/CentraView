/*
 * $RCSfile: PaymentVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:31 $ - $Author: mking_cv $
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


package com.centraview.account.payment;

import com.centraview.account.common.PaymentLines;
import com.centraview.common.CVAudit;

public class PaymentVO extends CVAudit
{

  /**  Elements for payment **/
  /**  Form elements for payment ends here**/

  private String externalID;

  private int paymentID;

  private String entity;
  private int entityID;

  private float paymentAmount;

  private String reference;
  private String description;
  private int paymentMethodID;
  private String checkNumber;
  private String cardNumber;
  private String cardType;
  private java.sql.Date expirationDate;

  /**  Elements for Invoices **/
  private PaymentLines paymentLines;
  private PaymentLines paymentAppliedLines;

  private int modifiedBy;

  public PaymentVO()
  {
    paymentLines = new PaymentLines();
    paymentAppliedLines = new PaymentLines();
  }

  /**  Elements for Invoices ends here**/



  public String getCardNumber()
  {
    return this.cardNumber;
  }

  public void setCardNumber(String cardNumber)
  {
    this.cardNumber = cardNumber;
  }


  public String getCardType()
  {
    return this.cardType;
  }

  public void setCardType(String cardType)
  {
    this.cardType = cardType;
  }


  public String getCheckNumber()
  {
    return this.checkNumber;
  }

  public void setCheckNumber(String checkNumber)
  {
    this.checkNumber = checkNumber;
  }


  public String getDescription()
  {
    return this.description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }


  public String getEntity()
  {
    return this.entity;
  }

  public void setEntity(String entity)
  {
    this.entity = entity;
  }


  public int getEntityID()
  {
    return this.entityID;
  }

  public void setEntityID(int entityID)
  {
    this.entityID = entityID;
  }


  public java.sql.Date getExpirationDate()
  {
    return this.expirationDate;
  }

  public void setExpirationDate(java.sql.Date expirationDate)
  {
    this.expirationDate = expirationDate;
  }


  public float getPaymentAmount()
  {
    return this.paymentAmount;
  }

  public void setPaymentAmount(float paymentAmount)
  {
    this.paymentAmount = paymentAmount;
  }


  public PaymentLines getPaymentAppliedLines()
  {
    return this.paymentAppliedLines;
  }

  public void setPaymentAppliedLines(PaymentLines paymentAppliedLines)
  {
    this.paymentAppliedLines = paymentAppliedLines;
  }

  public PaymentLines getPaymentLines()
  {
    return this.paymentLines;
  }

  public void setPaymentLines(PaymentLines paymentLines)
  {
    this.paymentLines = paymentLines;
  }


  public int getPaymentMethodID()
  {
    return this.paymentMethodID;
  }

  public void setPaymentMethodID(int paymentMethodID)
  {
    this.paymentMethodID = paymentMethodID;
  }


  public String getReference()
  {
    return this.reference;
  }

  public void setReference(String reference)
  {
    this.reference = reference;
  }


  public int getPaymentID()
  {
    return this.paymentID;
  }

  public void setPaymentID(int paymentID)
  {
    this.paymentID = paymentID;
  }

  public PaymentVO getVO()
  {
    return this;
  }



  public String getExternalID()
  {
    return this.externalID;
  }

  public void setExternalID(String externalID)
  {
    this.externalID = externalID;
  }

  /**
   * Sets the modifiedBy for this payment.
   * @param modifiedBy The new modifiedBy for this payment.
   */
  public void setModifiedBy(int modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  } //end of setModifiedBy method

  /**
   * @return The modifiedBy for this payment.
   */
  public int getModifiedBy()
  {
    return this.modifiedBy;
  } //end of getModifiedBy method

}














