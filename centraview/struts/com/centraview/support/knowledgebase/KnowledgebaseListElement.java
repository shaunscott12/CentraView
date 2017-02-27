/*
 * $RCSfile: KnowledgebaseListElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:50 $ - $Author: mking_cv $
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
/**
 * KnowledgeBaseListElement.java
 *
 * 
 * Creation date: 27 August 2003
 * @author: Amit Gandhe
 */



package com.centraview.support.knowledgebase;

import com.centraview.common.ListElement;
import com.centraview.common.ListElementMember;

/**
This class stores all columns for each row of Knowledge Base

*/

public class KnowledgebaseListElement extends ListElement
{


	/**
	 * Constructor
	 *
	 * @param   i  
	 */
	public KnowledgebaseListElement(int i) {
		itsElementID = i;
	}


	/**
	 *
	 *
	 * @param   i  
	 * @param   listelementmember  
	 */
	public void addMember(int i, ListElementMember listelementmember) {
		put(new Integer(i), listelementmember);
	}


	/**
	 *
	 *
	 * @param   s  
	 * @return    ListElementMember 
	 */
	public ListElementMember getMember(String s) {
		return null;
	}


	/**
	 *
	 *
	 * @param   c  
	 */
	public void setListAuth(char c) {
		super.auth = c;
	}


	/**
	 *
	 *
	 * @return    char 
	 */
	public char getListAuth() {
		return auth;
	}


	/**
	 *
	 *
	 * @return int    
	 */
	public int getElementID() {
		return itsElementID;
	}

	private int itsElementID;
}

