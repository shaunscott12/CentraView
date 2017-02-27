/*
 * $RCSfile: SupportListLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:13 $ - $Author: mking_cv $
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


package com.centraview.support.supportlist;

import java.util.HashMap;
import java.util.Vector;

import javax.ejb.EJBLocalObject;

import com.centraview.support.faq.FAQList;
import com.centraview.support.faq.QuestionList;
import com.centraview.support.knowledgebase.KnowledgebaseList;
import com.centraview.support.thread.ThreadList;
import com.centraview.support.ticket.TicketList;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

public interface SupportListLocal extends EJBLocalObject
{
	public TicketList getTicketList(int userID,HashMap hashmap) ;
	public FAQList getFAQList(int userID,HashMap hashmap) ;

	public KnowledgebaseList getKnowledgebaseList(int userID,int curCategoryID,HashMap hashmap) ;
	public ThreadList getThreadList(int userID,int curTicketID) ;
	public QuestionList getQuestionList(int userID,int curFaqID);
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);
	public ValueListVO getTicketValueList(int individualID, ValueListParameters parameters);
	public ValueListVO getFAQValueList(int individualID, ValueListParameters parameters);
	public ValueListVO getKnowledgeBaseValueList(int individualID, ValueListParameters parameters);
	public Vector getCategoryRootPath(int userID, int categoryID);
}
