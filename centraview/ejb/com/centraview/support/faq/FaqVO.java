/*
 * $RCSfile: FaqVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:10 $ - $Author: mking_cv $
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
 * NoteVO.java
 *
 * @author   Diwakar Purighalla
 * @version  1.0
 *
 */





package com.centraview.support.faq;


import com.centraview.common.CVAudit;

// This class stores the properties of Note

public class FaqVO extends CVAudit
{


	private int faqId;
	private String status;
	private String title;
	private String detail;
	private String question;
	private String answer;
	private int updatedBy;

	/*
	*	Stores publishToCustomerView
	*/
	private String publishToCustomerView;


	/**
	 * Constructor
	 *
	 */
	public FaqVO()
	{
		super();
	}



	/**
	 * gets the FaqID
	 *
	 * @return  int
	 */
	public int getFaqId()
	{
		return this.faqId;
	}


	/**
	 * set Faq ID
	 *
	 * @param   faqId
	 */
	public void setFaqId(int faqId)
	{
		this.faqId = faqId;
	}



	/**
	 * gets detail
	 *
	 * @return     String
	 */
	public String getDetail()
	{
		return this.detail;
	}


	/**
	 * sets detail
	 *
	 * @param   detail
	 */
	public void setDetail(String detail)
	{
		this.detail = detail;
	}





	/**
	 * gets the title
	 *
	 * @return  String
	 */
	public String getTitle()
	{
		return this.title;
	}


	/**
	 * sets the title
	 *
	 * @param   title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}





	/**
	 *	gets the question
	 *
	 * @return   question
	 */
	public String getQuestion()
	{
		return this.question;
	}


	/**
	 *	sets the question
	 *
	 * @param   question
	 */
	public void setQuestion(String question)
	{
		this.question = question;
	}



	/**
	 *	gets the answer
	 *
	 * @return  answer
	 */
	public String getAnswer()
	{
		return this.answer;
	}




	/**
	 *	sets the answer
	 *
	 * @param   answer
	 */
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}



	/**
	 * gets the status
	 *
	 * @return status
	 */
	public String getStatus()
	{
		return this.status;
	}


	/**
	 * sets the status
	 *
	 * @param   status
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	/**
	 * gets the updatedid
	 *
	 * @return   updatedBy
	 */

	public int getUpdatedBy()
	{
		return this.updatedBy;
	}


	/**
	 * sets the updatedByid
	 *
	 * @param   updatedBy
	 */

	public void setUpdatedBy(int updatedBy)
	{
		this.updatedBy = updatedBy;
	}

	/**
	 * get the publishToCustomerView
	 *
	 * @return The FAQ publishToCustomerView
	 */
	public String getPublishToCustomerView() {
		return this.publishToCustomerView;
	}

	/**
	 *	Set The publishToCustomerView of FAQ
	 *
	 * @param   publishToCustomerView
	 */
	public void setPublishToCustomerView(String publishToCustomerView) {
		this.publishToCustomerView = publishToCustomerView;
	}

}
