/*
 * $RCSfile: PreferenceVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:46 $ - $Author: mking_cv $
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
 * PreferenceVO.java
 *
 * @author   Deepa Sarwate
 * @version  1.0    
 * 
 */


package com.centraview.preference;


import com.centraview.common.CVAudit;

// This class stores the properties of Preference

public class PreferenceVO extends CVAudit
{
	private int individualId;
	private int moduleId;
	private String preferenceName;
	private String preferenceValue;
	private String tag;
	
	/** 
	 * Constructor
	 *
	 */
	public PreferenceVO()
	{
		super();
	}

	/** 
		Individual id can be null. Sought from Individual Classes
	 */
	public int getIndividualId()
	{
		return this.individualId;
	}

	public void setIndividualId(int individualId)
	{
		this.individualId = individualId;
	}

	
	/** 
		Module id defines the id for current module.
	 */
	public int getModuleId()
	{
		return this.moduleId;
	}

	public void setModuleId(int moduleId)
	{
		this.moduleId = moduleId;
	}

	/** 
		Preference name can be refresh rate or view options on the home page
	 */
	public String getPreferenceName()
	{
		return this.preferenceName;
	}

	public void setPreferenceName(String preferenceName)
	{
		this.preferenceName = preferenceName;
	}

	
	/** 
		Preference value is the corresponding value of pref name
	 */
	public String getPreferenceValue()
	{
		return this.preferenceValue;
	}

	public void setPreferenceValue(String preferenceValue)
	{
		this.preferenceValue = preferenceValue;
	}

	
	public String getTag()
	{
		return this.tag;
	}

	public void setTag(String tag)
	{
		this.tag = tag;
	}

	public PreferenceVO getVO()
	{
		return this;
	}
	

}
