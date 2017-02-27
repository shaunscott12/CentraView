/*
 * $RCSfile: PopulateMarketingForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:17 $ - $Author: mking_cv $
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

package com.centraview.marketing;


import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;

public class PopulateMarketingForm
{
	// reset form
	public void resetForm (HttpServletRequest request, HttpServletResponse response, ActionForm form)
	{
		try
		{

			System.out.println("PopulateMarketingForm::resetForm(HttpServletRequest, HttpServletResponse, ActionForm)::entry");
			HttpSession session = request.getSession();

			ListMemberForm resetForm = (ListMemberForm) form;

			resetForm.setHeaderrow("");
			resetForm.setTheFile(null);
			resetForm.setFilePath("");
			resetForm.setListname("");
			resetForm.setOwner("");
			resetForm.setOwnername("");
			resetForm.setListdescription("");
			resetForm.setMasterlistid("");
			resetForm.setFieldseprator("");
			resetForm.setLineseprator("");
			resetForm.setTab("");
			resetForm.setLine("");
			resetForm.setEntity("");
			resetForm.setPhone("");
			resetForm.setPrimary("");
			resetForm.setState("");
			resetForm.setZip("");
			resetForm.setEmail("");
			resetForm.setContry("");
			resetForm.setCity("");
			resetForm.setAddress1("");
			resetForm.setAddress2("");
			resetForm.setEusk("");
			resetForm.setWebsite("");
			resetForm.setHeadervector(new Vector());
			session.setAttribute("importListForm", resetForm);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
