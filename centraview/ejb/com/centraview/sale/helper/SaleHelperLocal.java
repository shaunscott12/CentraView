/*
 * $RCSfile: SaleHelperLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:04 $ - $Author: mking_cv $
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

 
package com.centraview.sale.helper;
import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.centraview.sale.proposal.ProposalVO;

public interface SaleHelperLocal extends EJBLocalObject
{
   	//public Vector getAllSource();
	public Vector getAllStatus();
	public Vector getAllStage();
	public Vector getAllType(); 
	public Vector getAllProbability(); 
	public Vector getAllTerm(); 	
	
	/** Returns Tax in float for a given Tax Class & Jurisdiction
	  * @return float tax
	  */
	public float getTax(int taxClassId, int taxJurisdictionId);  	
	
	/** Sets Tax in float for a given Tax Class & Jurisdiction
	  * @return void
	  */
	public void setTax(int taxClassId, int taxJurisdictionId, float tax) ;

	/** Adds / calculates ItemLines after calculating Tax etc and returns the updated ProposalVO
	  * @return ProposalVO
	  * @param int
	  * @param ProposalVO
	  * @param String
	  */
	
	public ProposalVO calculateProposalItems(int userId,ProposalVO proposalVO, String newItemID); 

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);
	
}