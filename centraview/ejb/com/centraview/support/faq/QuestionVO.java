/*
 * $RCSfile: QuestionVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:10 $ - $Author: mking_cv $
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
 * QuestionVO.java
 *
 * @author   Prasanta Sinha
 * @version  1.0    
 * 
 */

package com.centraview.support.faq;

import com.centraview.common.CVAudit;

// This class stores the properties of Note

public class QuestionVO extends CVAudit
{

	private int questionId;
	private int faqId;
	private String question;
	private String answer;


	/** 
	 * Constructor
	 *
	 */
	public QuestionVO()
	{
		super();
	}
	
	public int getQuestionId()
	{
		return this.questionId;
	}

	public void setQuestionId(int questionId)
	{
		this.questionId = questionId;
	}

	
	public int getFaqId()
	{
		return this.faqId;
	}

	public void setFaqId(int faqId)
	{
		this.faqId = faqId;
	}

	
	public String getQuestion()
	{
		return this.question;
	}

	public void setQuestion(String question)
	{
		this.question = question;
	}

	
	public String getAnswer()
	{
		return this.answer;
	}

	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
}