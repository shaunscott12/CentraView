/*
 * $RCSfile: AppSettingsVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:40 $ - $Author: mking_cv $
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
 * AppSettingsVO.java
 *
 * @author   Sandip Wadkar
 * @version  1.0    
 * 
 */


package com.centraview.administration.applicationsettings;


import java.util.Vector;

import com.centraview.common.CVAudit;

// This class stores the properties of Admministration

public class AppSettingsVO extends CVAudit
{
	private Vector valuelist = null;
	
			
	public Vector getValueList()
	{
		return this.valuelist;
	}

	public void setValueList(Vector valuelist)
	{
		this.valuelist = valuelist;
	}

	public AppSettingsVO getVO()
	{
		return this;
	}
}
