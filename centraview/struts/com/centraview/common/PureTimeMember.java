/*
 * $RCSfile: PureTimeMember.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:15 $ - $Author: mking_cv $
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

package com.centraview.common ;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PureTimeMember extends DateMember implements Serializable
{

	public PureTimeMember (  String memberType  , Date  memberValue, int auth , String requestURL , char displayType ,
						boolean  linkEnabled , int  displayWidth  ,String timezoneid  )
	{
		super(memberType, memberValue, auth,requestURL, displayType,
						linkEnabled, displayWidth, timezoneid );
	}	

	public String  getDisplayString()
	{		
		System.out.println("In display Pure Time ** member string-----------------****");
		if ( memberValue != null )
		{
			DateFormat   df= new SimpleDateFormat("h:mm a"); 
			return df.format(memberValue);
		}else
		{
			return "";
		}
	}	
	
	public  String getSortString()
	{
		if(memberValue != null && !memberValue.equals(""))
			return ""+memberValue.getHours()+memberValue.getMinutes() ;
		else
			return "";	
	}

}


