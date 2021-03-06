/*
 * $RCSfile: DoubleMember.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:00 $ - $Author: mking_cv $
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

import com.centraview.administration.authorization.ModuleFieldRightMatrix;


public class DoubleMember extends ListElementMember implements Serializable
{
	private int auth ;
	private String requestURL ;
	private char displayType ;
	private String graphicResourceURL ;
	private boolean  linkEnabled ;
	private int  displayWidth ;
	private Double memberValue ;
	private String memberType ;

	public DoubleMember (  String memberType  , Double  memberValue, int auth , String requestURL , char displayType ,
						boolean  linkEnabled , int  displayWidth )
	{
		this.auth = auth ;
		this.requestURL =requestURL;
		this.displayType =displayType; // Graphics  or Text
		this.linkEnabled =linkEnabled;
		this.displayWidth=displayWidth ;
		this.memberValue =memberValue;
		this.memberType =memberType ; //column name
	}

	//abstract
	public String getMemberType()
	{
		return memberType ;
	}

	public String  getDataType()
	{
		return "string" ;
	}

	//abstract
	public Object getMemberValue()
	{
		
		return memberValue ;
	}

	//abstract
	public String  getRequestURL()
	{
		return requestURL;
	}

	public void   setRequestURL( String str )
	{
		 requestURL = str ;
	}

	//abstract
	public int  getDisplayWidth()
	{
		return displayWidth;
	}

	//abstract
	public char getDisplayType()
	{
		return displayType ;
	}

	//abstract
	public String  getGraphicResourceURL()
	{
		return graphicResourceURL;
	}

	public void  setGraphicResourceURL( String str )
	{
		 graphicResourceURL = str ;
	}

	//abstract
	public boolean getLinkEnabled()
	{
		return linkEnabled;
	}

	public void  setLinkEnabled( boolean b )
	{
		linkEnabled = b ;
	}

	//abstract
	public int getAuth()
	{
		return auth ;
	}

	//abstract
	public void setAuth(int auth)
	{
		this.auth = auth;
	}


	public String getDisplayString()
	{
		if(auth == ModuleFieldRightMatrix.NONE_RIGHT)
			return "";
	
		if(memberValue == null)
			return "";
		else	
			return  memberValue.doubleValue()+"";
	}

	public  String getSortString()
	{
		return "" ;
	}

}


