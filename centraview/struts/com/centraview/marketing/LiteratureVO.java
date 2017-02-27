/*
 * $RCSfile: LiteratureVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:15 $ - $Author: mking_cv $
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
 * LiteratureVO.java
 *
 * @author   Sunita
 * @version  1.0    
 * 
 */



package com.centraview.marketing;

import com.centraview.common.CVAudit;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.individual.IndividualVO;


// This class stores the value object of Files

public class LiteratureVO extends CVAudit
{


	private int literatureId;
	private String title;
	private String detail;
	private String status;
	private IndividualVO authorVO;
	private int relateEntity;
	private EntityVO relateEntityVO;
	private int relateIndividual;
	private IndividualVO relateIndividualVO;

	/**
	 * Constructor
	 *
	 */
	public LiteratureVO()
	{
		super();
	}

	

	/**
	 * gets Detail
	 *
	 * @return    String 
	 */
	public String getDetail()
	{
		return this.detail;
	}


	/**
	 * sets Detail
	 *
	 * @param   detail  
	 */
	public void setDetail(String detail)
	{
		this.detail = detail;
	}

	

	/**
	 * gets Literature Id
	 *
	 * @return  int   
	 */
	public int getLiteratureId()
	{
		return this.literatureId;
	}


	/**
	 * sets Literature Id
	 *
	 * @param   literatureId  
	 */
	public void setLiteratureId(int literatureId)
	{
		this.literatureId = literatureId;
	}

	

	

	/**
	 * gets status
	 *
	 * @return  String   
	 */
	public String getStatus()
	{
		return this.status;
	}


	/**
	 * sets Status
	 *
	 * @param   status  
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}

	

	/**
	 * gets title
	 *
	 * @return   String  
	 */
	public String getTitle()
	{
		return this.title;
	}


	/**
	 * sets title
	 *
	 * @param   title  
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}
}
