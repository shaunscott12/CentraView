/*
 * $RCSfile: EmailSettingsVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:43 $ - $Author: mking_cv $
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


package com.centraview.administration.emailsettings;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

/**
 * This class will set all the methods of EmailSettingsVO by taking input from the Form.
 *
 * @author Naresh Patel <npatel@centraview.com>
 * @version $Revision: 1.1.1.1 $
 */
public class EmailSettingsVOX extends EmailSettingsVO
{
	public EmailSettingsVOX()
	{
		super();
	}// end of Default Constructor

	public EmailSettingsVOX(ActionForm actionForm)
	{
		DynaActionForm form = (DynaActionForm) actionForm;
		Integer smtpPort = (Integer)form.get("port");
		this.setSmtpPort(smtpPort.intValue());
		this.setSmtpServer((String)form.get("smtpserver"));
		this.setUsername((String)form.get("username"));
		this.setPassword((String)form.get("password"));
		Boolean authentication = (Boolean)form.get("authentication");
		boolean authenticationRequired = false;
		if (authentication != null && authentication.booleanValue() == true)
		{
			authenticationRequired = true;
		}// end of if (authentication != null && authentication.booleanValue() == true)
		this.setAuthentication(authenticationRequired);
	}// end of public EmailSettingsVOX(ActionForm actionForm)

	public EmailSettingsVO getVO()
	{
		return super.getVO();
	}//  end of public EmailSettingsVO getVO()

}// end of public class EmailSettingsVOX