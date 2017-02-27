/*
 * $RCSfile: CustomFieldVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:46 $ - $Author: mking_cv $
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

package com.centraview.projects.helper;

//import com.centraview.contact.*;

import java.io.Serializable;
import java.util.Vector;
/*
 *  This is CustomFieldValue Object which represent the CustomField Data. 
 *
 * @author  Parshuram Walunjkar
 * @version 1.0
 */

public class CustomFieldVO implements Serializable
{
	private String label;  // label of the field
	private String value; // Value of the field
	private String fieldType = "SCALAR"; // this is either SCALAR	or MULTIPLE
	private Vector optionValues; // Values in MULTIPLE Case
	private int fieldID; //filedID
	
	private String recordType; // this is RecordType in String ie table Name
	private int recordTypeID; // this is RecordType ie table id
	private int recordID; //recordID
		
	public int getFieldID()
	{
		return this.fieldID;
	}

	public void setFieldID(int fieldID)
	{
		this.fieldID = fieldID;
	}

	
	public String getLabel()
	{
		return this.label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	
	public Vector getOptionValues()
	{
		return this.optionValues;
	}

	public void setOptionValues(Vector optionValues)
	{
		this.optionValues = optionValues;
	}

	
	public String getFieldType()
	{
		return this.fieldType;
	}

	public void setFieldType(String fieldType)
	{
		this.fieldType = fieldType;
	}

	
	public String getValue()
	{
		return this.value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	
	public String getRecordType()
	{
		return this.recordType;
	}

	public void setRecordType(String recordType)
	{
		this.recordType = recordType;
	}

	
	public int getRecordID()
	{
		return this.recordID;
	}

	public void setRecordID(int recordID)
	{
		this.recordID = recordID;
	}

	
	public int getRecordTypeID()
	{
		return this.recordTypeID;
	}

	public void setRecordTypeID(int recordTypeID)
	{
		this.recordTypeID = recordTypeID;
	}

	

	/**
	 *
	 *
	 * @return  The CustomField value Object.   
	 */
	public CustomFieldVO getVO()
	{
		return this; 
	}
	
}
