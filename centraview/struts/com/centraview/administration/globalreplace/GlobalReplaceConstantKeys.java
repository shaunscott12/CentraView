/*
 * $RCSfile: GlobalReplaceConstantKeys.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:40 $ - $Author: mking_cv $
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

package com.centraview.administration.globalreplace;

/**
 * @author Naresh Patel <npatel@centraview.com>
 */
public class GlobalReplaceConstantKeys
{
	// Defined type for Fields on basis of there use in application 
	public static final int FIELD_TYPE_INDIVIDUAL = 1;
	public static final int FIELD_TYPE_GROUP = 2;
	public static final int FIELD_TYPE_SOURCE = 3;
	public static final int FIELD_TYPE_EMPLOYEE = 4;
	public static final int FIELD_TYPE_PHONE = 6;
	public static final int FIELD_TYPE_ENTITY = 7;
	public static final int FIELD_TYPE_MULTIPLE = 8;

	//Defined the Table ID for the related tables to the Global Replace
	public static final int CUSTOM_FIELD_TABLEID = 5;
	public static final int METHOD_OF_CONTACT_TABLEID = 3;
	public static final int ADDRESS_TABLEID = 4;

	public static final String SEARCH_AND_OR = "AND";
	
	// important If any one is changing the field name in the entity/individual.
	// then please update the new fieldname 
	public static final String SEARCH_MARKETING_FIELD_NAME = "list";
}
