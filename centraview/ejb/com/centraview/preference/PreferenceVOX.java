/*
 * $RCSfile: PreferenceVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:46 $ - $Author: mking_cv $
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

/*
 *	This file is used for storing user input data
 * 	during adding or updating user preferences.
 *	@author : Deepa Sarwate
 *
 */

package com.centraview.preference;

import org.apache.struts.action.DynaActionForm;

public class PreferenceVOX extends PreferenceVO
{
	
	public PreferenceVOX(DynaActionForm form)
	{
		this.setPreferenceName((String)form.get("minutes"));
		this.setPreferenceName((String)form.get("seconds"));
		this.setPreferenceName((String)form.get("portletsLeft"));
		this.setPreferenceValue((String)form.get("minutesValue"));
		this.setPreferenceValue((String)form.get("secondsValue"));
		this.setPreferenceValue((String)form.get("portletsLeftValue"));
	}
		
	public PreferenceVO getVO()
	{
		return super.getVO();
	}
}














