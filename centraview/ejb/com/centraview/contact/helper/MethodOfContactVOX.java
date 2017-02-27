/*
 * $RCSfile: MethodOfContactVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:08 $ - $Author: mking_cv $
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
/*
 * @author  Parshuram Walunjkar
 * @version 1.0
 */
 

public class MethodOfContactVOX  extends MethodOfContactVO
{

	/**
	 *	In the constructor populate the  MethodOfContact Value Object.
	 *
	 * @param   form  ActionForm
	 */
	public MethodOfContactVOX(ActionForm form)
	{
		DynaActionForm dynaForm = (DynaActionForm)form;
		//setMocID()

		if((String)dynaForm.get("mocid") != null  && ((String)dynaForm.get("mocid")).length() > 0)
			setMocID( Integer.parseInt((String)dynaForm.get("mocid")));
			
		setMocType( Integer.parseInt((String)dynaForm.get("select")));
		setContent( (String)dynaForm.get("text3"));
		setNote((String)dynaForm.get("text4"));
		setSyncAs((String)dynaForm.get("syncas"));
	}

	/**
	 *
	 *
	 * @return  The MethodOfContact  value Object.   
	 */
	public MethodOfContactVO getVO()
	{
		return super.getVO();
	}
	
}