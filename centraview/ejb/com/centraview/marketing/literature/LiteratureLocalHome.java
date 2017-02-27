/*
 * $RCSfile: LiteratureLocalHome.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:40 $ - $Author: mking_cv $
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

package com.centraview.marketing.literature;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/* 
 *	Used for Add , Edit ,Delete ,Get Literature details.
 *	Date 19 Aug 2003
 * 	@author  Sunita
 * 	@version 1.0
 */
 
public interface LiteratureLocalHome extends EJBLocalHome
{
	public LiteratureLocal create() throws CreateException ;
}
