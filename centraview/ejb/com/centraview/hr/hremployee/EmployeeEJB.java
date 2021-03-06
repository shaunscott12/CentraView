/*
 * $RCSfile: EmployeeEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:24 $ - $Author: mking_cv $
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


/**
 * Project bean includes all business logic realted 
 * project.
 * 
 * @date   : 22-08-03
 * @version: 1.0
 */
package com.centraview.hr.hremployee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;

public class EmployeeEJB implements SessionBean
{
	//Standard EJB methods
	protected javax.ejb.SessionContext ctx;
	protected Context environment;
	private String dataSource = "MySqlDS";

	public void setSessionContext(SessionContext ctx)
	{
		this.ctx = ctx;
	}

	public void ejbActivate()   { }
	public void ejbPassivate()   { }
	public void ejbRemove()   { }
	public void ejbCreate()  { }

	public ArrayList getEmployeeIds()
	{
		ArrayList employeeList = new ArrayList();
		try
		{
			CVDal dl = new CVDal(dataSource); 
			dl.setSql("hr.getemployeeids");
			Collection col = dl.executeQuery();
			Iterator ite = col.iterator();
			while (ite.hasNext())
			{
				HashMap hm = (HashMap)ite.next();
				employeeList.add((Long)hm.get("IndividualID"));			
			}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return employeeList;
	}	
	
	
  /**
   * Adds an employee in the database.
   * 
   * @param userId Creator of the project
   */
  public int addEmployee(int userId, int individualID) throws AuthorizationFailedException
  {
    if (!CVUtility.isModuleVisible("EmployeeList", userId, this.dataSource))
    {
      throw new AuthorizationFailedException("EmployeeList - addEmployee");
    }

    int empId = 0;
    CVDal cvdal = new CVDal(dataSource);
    try
    {
      // Check to see if the employee already exists
      // if not add'em.
      boolean employeeExists = false;
      String strQuery = "SELECT individualid FROM employee WHERE individualid = ?";
      cvdal.setSqlQuery(strQuery);
      cvdal.setInt(1,individualID);
      Collection col = cvdal.executeQuery();
      if (col != null)
      {
        Iterator it = col.iterator();
        if (it.hasNext())
        {
            employeeExists = true;
        } // end while (it.hasNext())
      } // end if (col != null)
      cvdal.setSqlQueryToNull();
      if (!employeeExists)
      {
        cvdal.setSql("hr.addemployee");
        cvdal.setInt(1, individualID);
        cvdal.executeUpdate();
        empId = cvdal.getAutoGeneratedKey();
      }
    } catch (Exception e) {
      System.out.println("[Exception][EmployeeEJB.addEmployee] Exception Thrown: " + e);
      throw new EJBException(e);
    } finally {
      cvdal.destroy();
      cvdal = null;
    }
    return empId;
  }

	/**
	 * Delete the employee from the database
	 *
	 * @param userId	Id of the user 
	 * @param individualID Id of the employee to be deleted. 
	 */
	public int deleteEmployee( int individualID)
	{
		try
		{
			CVDal cvdl = new CVDal(dataSource);
			cvdl.setSql( "hr.deleteemployee");
			cvdl.setInt( 1 , individualID );
		 	cvdl.executeUpdate();
		 	cvdl.clearParameters();
			
			cvdl.destroy();

		}catch( Exception e )
		{
			System.out.println( "deleteproject"+e );
		}
		return 0;
	}
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

}


