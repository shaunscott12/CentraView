/*
 * $RCSfile: AddressVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:06 $ - $Author: mking_cv $
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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

public class AddressVOX extends AddressVO
{
  /**
   * In this Constructor set the AddressO Object.
   * @param form ActionForm
   */
  public AddressVOX(ActionForm form)
  {
    DynaActionForm dynaForm = (DynaActionForm)form;
    if ((String)dynaForm.get("addressid") != null && ((String)dynaForm.get("addressid")).length() > 0) {
      setAddressID(new Integer((String)dynaForm.get("addressid")).intValue());
    }
    setCity((String)dynaForm.get("city"));
    setStateName((String)dynaForm.get("state"));
    setZip((String)dynaForm.get("zipcode"));
    setCountryName((String)dynaForm.get("country"));
    setStreet1((String)dynaForm.get("street1"));
    setStreet2((String)dynaForm.get("street2"));
    setIsPrimary((String)dynaForm.get("isPrimary"));
    String strJurisdictionID = (String)dynaForm.get("jurisdictionID");
    if (strJurisdictionID != null && !strJurisdictionID.equals("") && !strJurisdictionID.equals("null")) {
      int jurisdictionID = Integer.parseInt(strJurisdictionID);
      setJurisdictionID(jurisdictionID);
    }
  }
}