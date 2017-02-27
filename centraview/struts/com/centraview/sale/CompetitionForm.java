/*
 * $RCSfile: CompetitionForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:39 $ - $Author: mking_cv $
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
 *	This file is used for storing user input data
 * 	during adding or editing of Invoice.
 *	@author : Linesh
 *
 */

package com.centraview.sale;

// java import package
import java.io.Serializable;

public class CompetitionForm extends org.apache.struts.action.ActionForm implements Serializable
{
	private int EntityID;
	private String Strenghts;
	private String Weaknesses;
	private String Notes;	

   	
	
	public int getEntityID(){
		return this.EntityID;
	}

	public void setEntityID(int entityID){
		this.EntityID = entityID;
	}



	public String getStrenghts(){
		return this.Strenghts;
	}

	public void setStrenghts(String Strenghts){
		this.Strenghts = Strenghts;
	}


	public String getWeaknesses(){
		return this.Weaknesses;
	}

	public void setWeaknesses(String Weaknesses){
		this.Weaknesses = Weaknesses;
	}



	public String getNotes(){
		return this.Notes;
	}

	public void setNotes(String Notes){
		this.Notes = Notes;
	}

}