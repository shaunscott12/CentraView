/*
 * $RCSfile: ListLocal.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:38 $ - $Author: mking_cv $
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


package com.centraview.marketing.List;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalObject;
import javax.naming.NamingException;

import com.centraview.administration.authorization.AuthorizationException;

public interface ListLocal extends EJBLocalObject
{
	public int addList(int userId ,ListVO listVO ) throws AuthorizationException, CreateException, NamingException;
	public boolean deleteList(int userId  );
	public boolean updateList(int userId ,ListVO listVO );
	public ListVO viewList(int listid);
	public Collection getAllList();
	
	/**
	 * This method Parse the information column by column and stores the information
	 * in the database and returns a String of Message objects.
	 *
	 * @param Vector The importList to get the Import row and Column.
	 * @param int The headerRow to get the head row.
	 * @param int The listID Identify we are are importing records into which list.
	 * @param int The individualID Identify who is logged in an performing the task.
	 * @param Collection The CustomEntList to get the list of Custom Entity.
	 * @param Collection The CustomIndList to get the list of Custom Individual.
	 * @param String The tabDelimiter to identify the column seperation delimiter.
	 * @param String The lineDelimiter to identify the row seperation delimiter.
	 * @param String The headLine its a header line.
	 * 
	 * @return HashMap of Message objects.
	 */
	public HashMap getImportList(Vector importList,int headerRow,int listid,int indvID,Collection CustomEntList,Collection CustomIndList,String tabDelimiter,String lineDelimiter,String headLine);
	public boolean addListMember(int userId , int listid , HashMap info );
	public boolean deleteListMember(int userId ,int listid , HashMap info );
	public boolean updateListMember(int userId ,int listid , HashMap info );

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * Allows the client to set the private dataSource
	 * @param ds The cannonical JNDI name of the datasource.
	 */
	public void setDataSource(String ds);
}
