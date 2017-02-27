/*
 * $RCSfile: AdditionalMenuVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:38 $ - $Author: mking_cv $
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

package com.centraview.additionalmenu;

import java.io.Serializable;
public class AdditionalMenuVO implements Serializable
{
	private int menuItemId;
	private String menuItemName;
	private int moduleId;
	private String moduleName;
	private String forwardResource;
	private boolean isNewWin = false;
	private String winProperty;
	private String params;

	
	public int getMenuItemId()
	{
		return this.menuItemId;
	}

	public void setMenuItemId(int menuItemId)
	{
		this.menuItemId = menuItemId;
	}

	
	public String getMenuItemName()
	{
		return this.menuItemName;
	}

	public void setMenuItemName(String menuItemName)
	{
		this.menuItemName = menuItemName;
	}

	
	public int getModuleId()
	{
		return this.moduleId;
	}

	public void setModuleId(int moduleId)
	{
		this.moduleId = moduleId;
	}

	
	public String getForwardResource()
	{
		return this.forwardResource;
	}

	public void setForwardResource(String forwardResource)
	{
		this.forwardResource = forwardResource;
	}

	
	public boolean getIsNewWin()
	{
		return this.isNewWin;
	}

	public void setIsNewWin(boolean isNewWin)
	{
		this.isNewWin = isNewWin;
	}

	
	public String getWinProperty()
	{
		return this.winProperty;
	}

	public void setWinProperty(String winProperty)
	{
		this.winProperty = winProperty;
	}

	
	public String getParams()
	{
		return this.params;
	}

	public void setParams(String params)
	{
		this.params = params;
	}

	
	public String getModuleName()
	{
		return this.moduleName;
	}

	public void setModuleName(String moduleName)
	{
		this.moduleName = moduleName;
	}
}
