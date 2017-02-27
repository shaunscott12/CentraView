/*
 * $RCSfile: ListMemberForm.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:14 $ - $Author: mking_cv $
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

package com.centraview.marketing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;



public class ListMemberForm extends ActionForm
{
	   //form 1
		protected FormFile theFile;
		protected String filePath;
		protected String masterlistid;
		protected String listdescription;
		protected String listname;
		protected String fieldseprator;
		protected String lineseprator;
		protected String tab;
		protected String line;
		protected String headerrow="Yes";
		protected String ownername;

		protected String owner;

	  //Form2
	  	protected String entity;
		protected String primary;
		protected String address1;
		protected String address2;
		protected String city;
		protected String state;
		protected String zip;
		protected String contry;
		protected String phone;
		protected String email;
		protected String website;
		protected String eusk;

		protected Vector  headervector;

		protected Collection  importIndividualList;
		protected Collection  importEntityList;



		public ListMemberForm(){
			super();
			reset();
		}

		public String getHeaderrow() {
		    return headerrow;
		}

		public void setHeaderrow(String hasRow) {
		    this.headerrow = hasRow;
		}




    public Collection getCustomIndividualList()
    {
    	return this.importIndividualList;
    }

    public void setCustomIndividualList(Collection importIndividualList)
    {
    	this.importIndividualList = importIndividualList;
    }


		public void setCustomEntityList(Collection importEntityList) {
			this.importEntityList=importEntityList;
		}


		public Collection getCustomEntityList(){
			return this.importEntityList;
		}

    public FormFile getTheFile() {
        return theFile;
    }

    public void setTheFile(FormFile theFile) {
        this.theFile = theFile;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getListname()
    {
    	return this.listname;
    }

    public void setListname(String listname)
    {
    	this.listname = listname;
    }

    public String getOwnername()
    {
    	return this.ownername;
    }

    public void setOwner(String owner)
    {
    	this.owner = owner;
    }

    public String getOwner()
    {
    	return this.owner;
    }

    public void setOwnername(String ownername)
    {
    	this.ownername = ownername;
    }

    public String getListdescription()
    {
    	return this.listdescription;
    }

    public void setListdescription(String listdescription)
    {
    	this.listdescription = listdescription;
    }


    public String getMasterlistid()
    {
    	return this.masterlistid;
    }

    public void setMasterlistid(String  masterlistid)
    {
    	this.masterlistid = masterlistid;
    }


	public String getFieldseprator()
	{
		return this.fieldseprator;
	}

	public void setFieldseprator(String fieldseprator)
	{
		this.fieldseprator = fieldseprator;
	}


	public String getLineseprator()
	{
		return this.lineseprator;
	}

	public void setLineseprator(String lineseprator)
	{
		this.lineseprator = lineseprator;
	}

	public String getTab()
	{
		return this.tab;
	}

	public void setTab(String tab)
	{
		this.tab = tab;
	}


	public String getLine()
	{
		return this.line;
	}

	public void setLine(String line)
	{
		this.line = line;
	}

	public void reset() {

		theFile= null;
		filePath= "";
		masterlistid="-1";
		listdescription="";
		listname="";
		fieldseprator="";
		lineseprator="";
		tab="";
		line="";
		headerrow="Yes";
		ownername="";

		owner="";

		//Form2
		entity="";
		primary="";
		address1="";
		address2="";
		city="";
		state="";
		zip="";
		contry="";
		phone="";
		email="";
		website="";
		eusk="";

		headervector= new Vector();
		importIndividualList = new ArrayList();
		importEntityList = new ArrayList();

/*
		setHeaderrow("");
		setTheFile(null);
		setFilePath("");
		setListname("");
		setOwner("");
		setOwnername("");
		setListdescription("");
		setMasterlistid("");
		setFieldseprator("");
		setLineseprator("");
		setTab("off");
		setLine("off");
		setEntity("");
		setPhone("");
		setPrimary("");
		setState("");
		setZip("");
		setEmail("");
		setContry("");
		setCity("");
		setAddress1("");
		setAddress2("");
		setEusk("");
		setWebsite("");
		setHeadervector(new Vector());
*/
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
	    ActionErrors errors = null;
	    //has the maximum length been exceeded?
	    Boolean maxLengthExceeded = (Boolean)
	            request.getAttribute(MultipartRequestHandler.ATTRIBUTE_MAX_LENGTH_EXCEEDED);
	    if ((maxLengthExceeded != null) && (maxLengthExceeded.booleanValue()))
	    {
	        errors = new ActionErrors();

	    }
	    return errors;

	}



	public String getEntity()
	{
		return this.entity;
	}

	public void setEntity(String entity)
	{
		this.entity = entity;
	}

	public String getPhone()
	{
		return this.phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getPrimary()
	{
		return this.primary;
	}

	public void setPrimary(String primary)
	{
		this.primary = primary;
	}


	public String getState()
	{
		return this.state;
	}

	public void setState(String state)
	{
		this.state = state;
	}


	public String getZip()
	{
		return this.zip;
	}

	public void setZip(String zip)
	{
		this.zip = zip;
	}


	public String getEmail()
	{
		return this.email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}


	public String getContry()
	{
		return this.contry;
	}

	public void setContry(String contry)
	{
		this.contry = contry;
	}


	public String getCity()
	{
		return this.city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}


	public String getAddress1()
	{
		return this.address1;
	}

	public void setAddress1(String address1)
	{
		this.address1 = address1;
	}


	public String getAddress2()
	{
		return this.address2;
	}

	public void setAddress2(String address2)
	{
		this.address2 = address2;
	}


	public String getEusk()
	{
		return this.eusk;
	}

	public void setEusk(String eusk)
	{
		this.eusk = eusk;
	}


	public String getWebsite()
	{
		return this.website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}


	public Vector getHeadervector()
	{
		return this.headervector;
	}

	public void setHeadervector(Vector headervector)
	{
		this.headervector = headervector;
	}
}
