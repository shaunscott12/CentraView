/*
 * $RCSfile: FaqLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:09 $ - $Author: mking_cv $
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
 * FaqLocal.java
 *
 * @author   Diwakar Purighalla
  * @version  1.0    
 * 
 */
package com.centraview.support.faq;

import javax.ejb.EJBLocalObject;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.note.NoteException;


/**
* This is  remote interface which is called from client
*/

public interface FaqLocal extends EJBLocalObject
{
	public int addFaq(int userId, FaqVO nvo) throws FaqException;
	public FaqVO getFaq(int userId, int faqId) throws NoteException;
	public void updateFaq(int userId, FaqVO nvo) throws FaqException,AuthorizationFailedException;
	public void deleteFaq(int userId, int faqId) throws FaqException,AuthorizationFailedException;
	public int duplicateFaq(int userId, FaqVO fvo) throws FaqException;
//	public QuestionList getQuestionList(int individualID ,int faqId) ;
	
	public int addQuestion(int userId, QuestionVO qvo) throws  FaqException;
	public void updateQuestion(int userId, QuestionVO qvo) throws  FaqException;
	public QuestionVO getQuestion(int userId, int questionId) throws FaqException;
	public void deleteQuestion(int userId, int questionId) throws FaqException;
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);	
	
}

                                                                                                                                                                                                                                                                                                                                                                                      










