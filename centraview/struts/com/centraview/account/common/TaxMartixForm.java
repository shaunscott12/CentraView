/*
 * $RCSfile: TaxMartixForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:12 $ - $Author: mking_cv $
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

package com.centraview.account.common;

import java.io.Serializable;

/**
 * This EJB handles all Database related
 * issues with the EmailSettings client in CentraView.
 *
 * @author Naresh Patel <npatel@centraview.com>
 * @version $Revision: 1.1.1.1 $
 */
public class TaxMartixForm implements Serializable
{
	/** The taxClassID of this TaxMartix. */
	int taxClassID;


	/** The taxJurisdictionID of this TaxMartix. */
	int taxJurisdictionID;


	/** The taxRate of this TaxMartix. */
	float taxRate;

	/** Default Constructor. */
  public TaxMartixForm(int taxClassID,int taxJurisdictionID, float taxRate)
  {
		this.taxClassID = taxClassID;
		this.taxJurisdictionID = taxJurisdictionID;
		this.taxRate = taxRate;
  }//end of TaxMartixForm constructor

  /**
   * Returns the taxClassID.
   *
   * @return Returns the taxClassID.
   */
	public int getTaxClassID()
	{
		return this.taxClassID;
	}// end of getTaxClassID()

  /**
   * Sets the taxClassID.
   *
   * @param taxClassID The taxClassID to set.
   */
	public void setTaxClassID(int taxClassID)
	{
		this.taxClassID = taxClassID;
	}// end of setTaxClassID(int taxClassID)

  /**
   * Returns the taxJurisdictionID.
   *
   * @return Returns the taxJurisdictionID.
   */
	public int getTaxJurisdictionID()
	{
		return this.taxJurisdictionID;
	}// end of getTaxJurisdictionID()

  /**
   * Sets the taxJurisdictionID.
   *
   * @param taxJurisdictionID The taxJurisdictionID to set.
   */
	public void setTaxJurisdictionID(int taxJurisdictionID)
	{
		this.taxJurisdictionID = taxJurisdictionID;
	}// end of setTaxJurisdictionID(int taxJurisdictionID)

  /**
   * Returns the taxRate.
   *
   * @return Returns the taxRate.
   */
	public float getTaxRate()
	{
		return this.taxRate;
	}// end of getTaxRate()

  /**
   * Sets the taxRate.
   *
   * @param taxRate The taxRate to set.
   */
	public void setTaxRate(float taxRate)
	{
		this.taxRate = taxRate;
	}// end of setTaxRate(float taxRate)

}// end of class TaxMatrixForm