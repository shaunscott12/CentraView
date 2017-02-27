/*
 * $RCSfile: AccountDetail.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:48 $ - $Author: mking_cv $
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
package com.centraview.email;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

public class AccountDetail implements Serializable
{
  
  
  
  private String  accountaddress;
  int accountid ;
  private ArrayList folderlist;
  private Vector treelist;
  private String signature;
  private String smtpserver;
  private String rights;

  public AccountDetail( String address )
  {

  	accountaddress = address ;
	folderlist = new ArrayList();

  }

	public void addFolder( Folder f )
	{
		folderlist.add( f );
	}


	public ArrayList getFolderList()
	{
		return folderlist;
	}

	public int getAccountid()
	{
	return this.accountid;
	}

	public void setAccountid(int accountid)
	{
	this.accountid = accountid;
	}


	public String getAccountaddress()
	{
	return this.accountaddress;
	}

	public void setAccountaddress(String accountaddress)
	{
	this.accountaddress = accountaddress;
	}

  private void addChildrensToTreelist(int parentID)
    {
        Folder f;
        for ( int i = 0 ; i < folderlist.size(); i ++ )
        {
            f =(Folder) folderlist.get( i );
            if ( f.getParentfolderid() == parentID ){
                treelist.add(f);
                addChildrensToTreelist(f.getFolderid());
            }
        }
    }

    public Vector getTreeViewList()
	{
		int rootID;
		rootID = getFolderIDFromName("root","SYSTEM");
		addChildrensToTreelist(rootID);
	  	return treelist;
	}

	public int getFolderIDFromName(String FolderName, String fType)
    {
		int folderID = 0 ;
    	Folder f;
		for ( int i=0 ; i < folderlist.size(); i ++ )
		{
			f =(Folder) folderlist.get( i );
			if ( f.getFoldername().equalsIgnoreCase(FolderName) && f.getFtype().equalsIgnoreCase(fType) )
			{
				 folderID = f.getFolderid();
				 break;
			}

		}
	    return folderID;
    }






	public String getSignature()
	{
		return this.signature;
	}

	public void setSignature(String signature)
	{
		this.signature = signature;
	}

	
	public String getSmtpserver()
	{
		return this.smtpserver;
	}

	public void setSmtpserver(String smtpserver)
	{
		this.smtpserver = smtpserver;
	}

	
	public String getRights()
	{
		return this.rights;
	}

	public void setRights(String rights)
	{
		this.rights = rights;
	}
}