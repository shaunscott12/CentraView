/*
 * $RCSfile: UserProfileForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:10 $ - $Author: mcallist $
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

package com.centraview.preference;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.centraview.common.Validation;

public class UserProfileForm extends org.apache.struts.action.ActionForm 
{
	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
	
	private String firstName;
	private String lastName;
	private String middleName;
	private String title;
	private String addressID;
	private int	   addressIDValue;
	private String street1;
	private String street2;
	private String city;
	private String state;
	private String stateId;	
	private String zip;
	private String country;
	private String countryId;
	
	private String phone;
	private String pExt;
	private String email;
	private String mobile;
	private String mExt;
	private String fax;
	private String fExt;
	
	private String userName;
	private String oldPassword;
	private String newPassword;

	public String getAddressID()
	{
		return this.addressID;
	}

	public void setAddressID(String addressID)
	{
		this.addressID = addressID;
	}

	
	public String getCity()
	{
		return this.city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	
	public String getCountry()
	{
		return this.country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	
	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	
	public String getFax()
	{
		return this.fax;
	}

	public void setFax(String fax)
	{
		this.fax = fax;
	}

	
	public String getFExt()
	{
		return this.fExt;
	}

	public void setFExt(String fExt)
	{
		this.fExt = fExt;
	}

	
	public String getFirstName()
	{
		return this.firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	
	public String getLastName()
	{
		return this.lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	
	public String getMExt()
	{
		return this.mExt;
	}

	public void setMExt(String mExt)
	{
		this.mExt = mExt;
	}

	
	public String getMiddleName()
	{
		return this.middleName;
	}

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	
	public String getMobile()
	{
		return this.mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	
	public String getPExt()
	{
		return this.pExt;
	}

	public void setPExt(String pExt)
	{
		this.pExt = pExt;
	}

	
	public String getPhone()
	{
		return this.phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	
	public String getState()
	{
		return this.state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	
	public String getStreet1()
	{
		return this.street1;
	}

	public void setStreet1(String street1)
	{
		this.street1 = street1;
	}

	
	public String getStreet2()
	{
		return this.street2;
	}

	public void setStreet2(String street2)
	{
		this.street2 = street2;
	}

	
	public String getTitle()
	{
		return this.title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	
	public String getUserName()
	{
		return this.userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	
	public String getZip()
	{
		return this.zip;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}

	
	public String getCountryId()
	{
		return this.countryId;
	}

	public void setCountryId(String countryId)
	{
		this.countryId = countryId;
	}

	
	public String getStateId()
	{
		return this.stateId;
	}

	public void setStateId(String stateId)
	{
		this.stateId = stateId;
	}


	public String getNewPassword()
	{
		return this.newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

	
	public String getOldPassword()
	{
		return this.oldPassword;
	}

	public void setOldPassword(String oldPassword)
	{
		this.oldPassword = oldPassword;
	}

	public int getAddressIDValue()
	{
		return this.addressIDValue;
	}

	public void setAddressIDValue(int addressIDValue)
	{
		this.addressIDValue = addressIDValue;
	}
	
	
	public void convertFormbeanToValueObject()
	{
		try
		{
			if(addressID != null && addressID.length() != 0)
				this.addressIDValue = Integer.parseInt(addressID);
			System.out.println("addressIDValue=========================="+addressIDValue);	
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}

	
	/*
	 *	Validates user input data
	 *	@param mapping ActionMapping
	 *	@param request HttpServletRequest
	 *	@return errors ActionErrors
	 */	
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request) 
	{
		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();

		try 
		{
			// initialize validation
			Validation validation = new Validation();	

      if (this.getFirstName() == null || this.getFirstName().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Name"));
      }
			
      if (this.getOldPassword() == null || this.getOldPassword().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Old Password"));
      }
			
      if (this.getNewPassword() == null || this.getNewPassword().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "New Password"));
      }
			
      if (this.getNewPassword() == null || this.getNewPassword().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Confirm Password"));
      }
			

			if (errors != null)
			{
				request.setAttribute("body", "ADD");
				request.setAttribute("clearform", "false");
				
				request.setAttribute("userForm", this);
				request.setAttribute("TYPEOFSUBMODULE", "PREFERENCE");
			} 
		}
		catch (Exception e)	
		{
			e.printStackTrace();
		}
		return errors;	
	}

}
