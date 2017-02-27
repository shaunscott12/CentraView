/*
 * $RCSfile: EmailHelperEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:16 $ - $Author: mking_cv $
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

package com.centraview.email.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;

/*
This is the EJB class for Email
The Logic for methods defined in Remote interface 
is defined in this class
*/

public class EmailHelperEJB implements SessionBean
{
	protected SessionContext ctx;
	private String dataSource = "MySqlDS";
	
	public void setSessionContext(SessionContext ctx)
	{
		this.ctx=ctx;
	}

	/**
	 *
	 *
	 */
	public void ejbCreate()
	{
	}

	/**
	 *
	 *
	 */
	public void ejbRemove()
	{
	}

	/**
	 *
	 *
	 */
	public void ejbActivate()
	{
	}

	/**
	 *
	 *
	 */
	public void ejbPassivate()
	{
	}
	
	/** Get IndividualID from mocrelate table for given email address
	  * @return int
	  */
	public int getIndividualID(String emailAddress) 
	{
		int indvId = 0;
		int contactType = 0;
		int entityId = 0;
		
	    CVDal dl = new CVDal(dataSource);
		try
		{
			String sql = " select mr.contactid,mr.contacttype from mocrelate mr left outer join methodofcontact mc  on  mc.mocid = mr.mocid and mc.content = '"+
						 emailAddress +"' and  mr.isPrimary='YES'";
						 
			dl.setSqlQueryToNull();
		    dl.setSqlQuery(sql);
			
			Collection  col  = (Collection)dl.executeQuery();
			Iterator it = col.iterator();
			
			//If its individualid
			if (col != null) 
			{			
				while (it.hasNext()) 
				{
					HashMap hm	= (HashMap)it.next();
					contactType = ((Long)hm.get("contacttype")).intValue();
					
					if(contactType == 2)
					indvId 		= ((Long)hm.get("contactid")).intValue();
					else
					entityId	= ((Long)hm.get("contactid")).intValue();
					
					break;
				}
			}
			
			//If its entityId 
			if (contactType == 1)
			{
				sql = " select individualid from individual where entity = "+entityId;
							 
				dl.setSqlQueryToNull();
				dl.setSqlQuery(sql);
				
				col  = (Collection)dl.executeQuery();
				it = col.iterator();
				
				if (col != null) 
				{			
					while (it.hasNext()) 
					{
						HashMap hm	= (HashMap)it.next();
						indvId 		= ((Long)hm.get("individualid")).intValue();
					}
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			dl.destroy();
			dl = null;
		}
		
		return indvId;	
	
	}
	
	/** Get FolderID from emailfolder table for given a/c id and folder type
	  * @return int
	  */
	
	public int getFolderIDForAccount(int accountId,String folderName,String folderType)
	{
	
		CVDal dl = new CVDal(dataSource);
		
		int folderID = 0;
		try
		{
			String sql = "SELECT folderID FROM `emailfolder` where accountid = "+accountId+" and name='"+folderName+"'  and ftype='"+folderType+"'";
						 
			dl.setSqlQueryToNull();
		    dl.setSqlQuery(sql);
			
			Collection  col  = (Collection)dl.executeQuery();
			Iterator it = col.iterator();
			
			if (col != null) 
			{			
				while (it.hasNext()) 
				{
					HashMap hm	= (HashMap)it.next();
					folderID = ((Long)hm.get("folderID")).intValue();
					break;
				}
			}
		}		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			dl.destroy();
			dl = null;
		}
		
		return folderID;	
	}

  /**
   * Returns a HashMap representation of the information needed
   * to send a particular system email (ie: Forgot Password email)
   * for the given System Email ID.
   * @param systemEmailID The ID of the system email which we need
   * information for. Use one of the correct constants found in
   * com.centraview.administration.common.AdminConstantKeys
   */
  public HashMap getSystemEmailInfo(int systemEmailID) throws EJBException
  {
    if (systemEmailID <= 0)
    {
      // if the systemEmailID is not a positive, non-zero integer,
      // then something is wrong, so throw an exception
      throw new EJBException("System Email ID is required");
    }

    CVDal dbConn = new CVDal(this.dataSource);
    HashMap emailInfoMap = new HashMap();

    try
    {
      dbConn.setSql("system.email.getSystemEmailInfo");
      dbConn.setInt(1, systemEmailID);
      Collection sqlResults = dbConn.executeQuery();

      if (sqlResults != null)
      {
        Iterator iter = sqlResults.iterator();
        while (iter.hasNext())
        {
          emailInfoMap = (HashMap)iter.next();
          break;
        }
      }
    }catch(Exception e){
      System.out.println("[Exception][EmailHelperEJB] Exception thrown in getSystemEmailInfo(): " + e);
      e.printStackTrace();    // TODO: remove this stacktrace
      throw new EJBException("Error while retrieving system email information from the database.");
    }finally{
      dbConn.clearParameters();
      dbConn.destroy();
      dbConn = null;
    }
    return(emailInfoMap);
  }   // end getSystemEmailInfo() method


	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }
	
}
