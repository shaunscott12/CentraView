/*
 * $RCSfile: SupportHelperEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:23:10 $ - $Author: mking_cv $
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

package com.centraview.support.helper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import com.centraview.common.CVDal;
import com.centraview.common.DDNameValue;
/*
 * @author  Prasanta Sinha
 * @version 1.0
 */

public class SupportHelperEJB implements SessionBean
{

    protected javax.ejb.SessionContext ctx;
    protected Context environment;
	private String dataSource = "MySqlDS";
	/*
     * Set the associated session context. The container calls this method after the instance
     * creation. The enterprise Bean instance should store the reference to the context
     * object in an instance variable. This method is called with no transaction context.
     */
    public void setSessionContext(SessionContext ctx)
    {
      this.ctx = ctx;
    }


    public SupportHelperEJB    ()
    {

    }

    /**
     * Called by the container to create a session bean instance. Its parameters typically
     * contain the information the client uses to customize the bean instance for its use.
     * It requires a matching pair in the bean class and its home interface.
     */
    public void ejbCreate    ()
    {

    }
    /**
     * A container invokes this method before it ends the life of the session object. This
     * happens as a result of a client's invoking a remove operation, or when a container
     * decides to terminate the session object after a timeout. This method is called with
     * no transaction context.
     */
   public void ejbRemove    ()
    {

    }

    /**
     * The activate method is called when the instance is activated from its 'passive' state.
     * The instance should acquire any resource that it has released earlier in the ejbPassivate()
     * method. This method is called with no transaction context.
     */
    public void ejbActivate    ()
    {

    }

    /**
     * The passivate method is called before the instance enters the 'passive' state. The
     * instance should release any resources that it can re-acquire later in the ejbActivate()
     * method. After the passivate method completes, the instance must be in a state that
     * allows the container to use the Java Serialization protocol to externalize and store
     * away the instance's state. This method is called with no transaction context.
     */
    public void ejbPassivate    ()
    {

    }


	/**
     *  In this Method returns the collect of master data of support status.
	 *  @retune Collection
	 *
	 * 
	 */
	public Vector getAllStatus()
	{
		Collection col = null;
		Vector	vec  = new Vector();
		try
		{
			CVDal dl = new CVDal(dataSource);
			//ALLSQL.put("support.ticket.getallstatus","select statusid , name  from supportstatus");
			dl.setSql("support.ticket.getallstatus");
			col = dl.executeQuery();
			
			dl.clearParameters();
			dl.destroy();

			if (col != null)
			{
				Iterator it = col.iterator();
				while (it.hasNext())
				{
			    	HashMap hm = (HashMap)it.next();
					int id 		= ((Long)hm.get("statusid")).intValue();
					String name = (String)hm.get("name");
					DDNameValue dd = new DDNameValue(id,name);
					vec.add(dd);
				}
			}
			//else
		}
		catch(Exception e)
		{
			System.out.println("[Exception] SupportHelperEJB.getAllStatus: " + e.toString());
			//e.printStackTrace();
		}
	    return vec;
	
	
	}
	public Vector getAllPriorities()
	{
		Collection col = null;
		Vector	vec  = new Vector();
		try
		{
			CVDal dl = new CVDal(dataSource);
			//ALLSQL.put("support.ticket.getallpriorities","select priorityid id, name name from supportpriority");
			dl.setSql("support.ticket.getallpriorities");
			col = dl.executeQuery();
			dl.clearParameters();
			dl.destroy();

			if (col != null)
			{
				Iterator it = col.iterator();
				while (it.hasNext())
				{
			    	HashMap hm = (HashMap)it.next();
					int id 		= ((Long)hm.get("priorityid")).intValue();
					String name = (String)hm.get("name");
					DDNameValue dd = new DDNameValue(id,name);
					vec.add(dd);
				}
			}// end of if
		}
		catch(Exception e)
		{
      System.out.println("[Exception] SupportHelperEJB.getAllPriorities: " + e.toString());
			//e.printStackTrace();
		}
	    return vec;
	
	}
	
	public Vector getAllTicketIDs() 
	{
		Collection col = null;
		Vector	vec  = new Vector();
		CVDal dl = null;
        try
		{
			dl = new CVDal(dataSource);
			//ALLSQL.put("support.ticket.getallticketids","select ticketid from ticket");
			dl.setSql("support.ticket.getallticketids");
			col = dl.executeQuery();

			if (col != null)
			{
				Iterator it = col.iterator();
				while (it.hasNext())
				{
			    	HashMap hm = (HashMap)it.next();
					vec.add((Long)hm.get("ticketid"));
				}
			}// end of if
		}
		catch(Exception e)
		{
      System.out.println("[Exception] SupportHelperEJB.getAllTicketIDs: " + e.toString());
			//e.printStackTrace();
		}
        finally
        {
          dl.clearParameters();
          dl.destroy();
          dl = null;
        }
	    return vec;
	}

  /**
   * Returns the "supportEmailCheckInterval" value from the
   * "systemsettings" table in the database, which defines
   * how often the system should check for new messages on
   * ALL of the defined support email accounts.
   * @return int The number in minutes between checks of the
   *         support email accounts. A value of zero indicates
   *         that support accounts should never be checked.
   */
  public int getSupportEmailCheckInterval()
  {
    int interval = 0;   // zero means never check accounts
    CVDal cvdal = new CVDal(dataSource);
    try
    {
      cvdal.setSqlQuery("SELECT settingValue FROM systemsettings WHERE settingName=?");
      cvdal.setString(1, "supportEmailCheckInterval");
      Collection results = cvdal.executeQuery();
      cvdal.clearParameters();
      if (results != null && results.size() > 0)
      {
        Iterator iter = results.iterator();
        if (iter.hasNext())
        {
          HashMap row = (HashMap)iter.next();
          String intervalValue = (String)row.get("settingValue");
          try {
            interval = Integer.parseInt(intervalValue);
          }catch(NumberFormatException nfe){
            // "You're in the jungle, baby... you're gonna die!!!" 
          }
        }
      }
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return interval;
  }   // end getSupportEmailCheckInterval() method

  /**
   * Updates the "supportEmailCheckInterval" value in the
   * "systemsettings" table in the database, which defines
   * how often the system should check for new messages on
   * ALL of the defined support email accounts.
   * @param newInterval The number in minutes between checks of the
   *         support email accounts. A value of zero indicates
   *         that support accounts should never be checked.
   * @return int The number of records updated - should always be zero or 1.
   */
  public int setSupportEmailCheckInterval(int interval)
  {
    int rowsAffected = 0;
    CVDal cvdal = new CVDal(this.dataSource);
    try
    {
      cvdal.setSqlQuery("UPDATE systemsettings SET settingValue=? WHERE settingName=?");
      cvdal.setString(1, String.valueOf(interval));
      cvdal.setString(2, "supportEmailCheckInterval");
      rowsAffected = cvdal.executeUpdate();
    }finally{
      cvdal.destroy();
      cvdal = null;
    }
    return rowsAffected;
  }   // end getSupportEmailCheckInterval() method
  
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }
}
