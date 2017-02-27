/*
 * $RCSfile: LicenseEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:28 $ - $Author: mking_cv $
 * 
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the License"); you may not use this file except in
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

package com.centraview.license;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;

/**
 * This provides the Session Bean for the 
 * License EJB.
 *
 * @author Ryan Grier <ryan@centraview.com>
 * @version 1.0
 */
public class LicenseEJB implements SessionBean
{
	/** The SessionContext interface for the instance. */
	protected SessionContext sessionContext;
	
	private String dataSource = "MySqlDS";
	
	/** LicenseEJB Constructor. */
	public LicenseEJB()
	{
		//Empty
	} //end of LicenseEJB constructor
	
	//javadoc api in SessionBean interface
	public void ejbCreate ()
	{
		//Empty
	} //end of ejbCreate method
	
	/**
	 * A container invokes this method before it ends the life of the 
	 * session object. This happens as a result of a client's 
	 * invoking a remove operation, or when a container decides 
	 * to terminate the session object after a timeout.
	 * <p>
	 * This method is called with no transaction context.
	 *
	 * @throws EJBException     Thrown by the method to indicate a failure 
	 * caused by a system-level error.
   * @throws RemoteException - This exception is defined in the method 
	 * signature to provide backward compatibility for applications 
	 * written for the EJB 1.0 specification. Enterprise beans written 
	 * for the EJB 1.1 specification should throw the javax.ejb.EJBException
	 * instead of this exception. Enterprise beans written for the EJB2.0 
	 * and higher specifications must throw the javax.ejb.EJBException 
	 * instead of this exception.
	 */
	public void ejbRemove ()
	{
		//Empty
	} //end of ejbRemove method
	
	/**
	 * The activate method is called when the instance is activated 
	 * from its "passive" state. The instance should acquire any 
	 * resource that it has released earlier in the ejbPassivate() method.
	 * <p>
	 * This method is called with no transaction context.
	 *
	 * @throws EJBException     Thrown by the method to indicate a failure 
	 * caused by a system-level error.
   * @throws RemoteException - This exception is defined in the method 
	 * signature to provide backward compatibility for applications 
	 * written for the EJB 1.0 specification. Enterprise beans written 
	 * for the EJB 1.1 specification should throw the javax.ejb.EJBException
	 * instead of this exception. Enterprise beans written for the EJB2.0 
	 * and higher specifications must throw the javax.ejb.EJBException 
	 * instead of this exception.
	 */
	public void ejbActivate ()
	{
		//Empty
	} //end of ejbActivate method
	
	/**
	 * The passivate method is called before the instance enters the 
	 * "passive" state. The instance should release any resources 
	 * that it can re-acquire later in the ejbActivate() method.
	 * <p>
	 * After the passivate method completes, the instance must be in a 
	 * state that allows the container to use the Java Serialization 
	 * protocol to externalize and store away the instance's state.
	 * <p>
	 * This method is called with no transaction context.
	 *
	 * @throws EJBException     Thrown by the method to indicate a failure 
	 * caused by a system-level error.
   * @throws RemoteException - This exception is defined in the method 
	 * signature to provide backward compatibility for applications 
	 * written for the EJB 1.0 specification. Enterprise beans written 
	 * for the EJB 1.1 specification should throw the javax.ejb.EJBException
	 * instead of this exception. Enterprise beans written for the EJB2.0 
	 * and higher specifications must throw the javax.ejb.EJBException 
	 * instead of this exception.
	 */
	public void ejbPassivate ()
	{
		//Empty
	} //end of ejbPassivate method
	
	/**
	 * Set the associated session context. 
	 * The container calls this method after the instance creation.
	 * <p>
	 * The enterprise Bean instance should store the 
	 * reference to the context object in an instance variable.
	 * <P>
	 * This method is called with no transaction context.
	 * 
	 * @param sessionContext A SessionContext interface for the instance.
	 *
	 * @throws EJBException     Thrown by the method to indicate a failure 
	 * caused by a system-level error.
   * @throws RemoteException - This exception is defined in the method 
	 * signature to provide backward compatibility for applications 
	 * written for the EJB 1.0 specification. Enterprise beans written 
	 * for the EJB 1.1 specification should throw the javax.ejb.EJBException
	 * instead of this exception. Enterprise beans written for the EJB2.0 
	 * and higher specifications must throw the javax.ejb.EJBException 
	 * instead of this exception.
	 */
	public void setSessionContext (SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
	} //end of setSessionContext method
	
	public int createLicense (LicenseVO licenseDetail) 
	{
		int newLicenseID = 0;
		try
		{
			CVDal dataConnection = new CVDal(dataSource);
			
			dataConnection.setSqlQuery(
				"insert into license(LicenseKey, LastVerified, LicenseVerification)"
				+ "values(?, NULL, NULL)");
				
			dataConnection.setString (1,licenseDetail.getLicenseKey());
		  dataConnection.executeUpdate();
		  newLicenseID = dataConnection.getAutoGeneratedKey();
		  dataConnection.clearParameters();
		  dataConnection.destroy();
		  dataConnection = null;
			
		} //end of try block
		catch (Exception ex)
		{
			System.out.println("[Exception][LicenseEJB.createLicense] Exception Thrown: " + ex);
			ex.printStackTrace ();
		} //end of catch block
		return newLicenseID;
	} //end of createLicense method
	
	public LicenseVO getLicenseDetails (int licenseID)
	{
		LicenseVO licenseDetails = null;
		try
		{
			CVDal dataConnection = new CVDal(dataSource);
			
			dataConnection.setSqlQuery( 
				"select LicenseID, LicenseKey, LastVerified , LicenseVerification "
				+ "from license where LicenseID = ?" );
			
			dataConnection.setInt(1,licenseID);
			Collection resultsCollection = dataConnection.executeQuery();
			dataConnection.clearParameters();
			dataConnection.destroy();
			dataConnection = null;
			
			if (null != resultsCollection)
			{
				//No need to iterate through the collection
				//because there should only be one result.
				HashMap result = (HashMap) resultsCollection.iterator().next();
				licenseDetails = new LicenseVO ();
				
				licenseDetails.setLicenseID (((Long) result.get("LicenseID")).intValue());
				licenseDetails.setLicenseKey ((String) result.get("LicenseKey"));
				licenseDetails.setLicenseVerification ((String) result.get("LicenseVerification"));
				licenseDetails.setLastVerified ((java.util.Date) result.get("LastVerified"));
				
				resultsCollection = null;
				result = null;
			} //end of if statement
		} //end of try block
		catch (Exception ex)
		{
			System.out.println ("Exception in LicenseEJB.getLicenseDetails:");
			ex.printStackTrace ();
		} //end of catch block
		return licenseDetails;
	} //end of getLicenseDetails method
	
	public void updateLicense(LicenseVO licenseDetail)
	{
		try
		{
			CVDal dataConnection = new CVDal(dataSource);
			
			dataConnection.setSqlQuery ( 
				"update license set LicenseKey = ?, licenseVerification = ?, "
				+ "LastVerified = ? where LicenseID = ? ");
				
			dataConnection.setString (1, licenseDetail.getLicenseKey());
			dataConnection.setString (2, licenseDetail.getLicenseVerification());
			dataConnection.setRealTimestamp (3, 
				new java.sql.Timestamp (licenseDetail.getLastVerified().getTime()));
			dataConnection.setInt (4, licenseDetail.getLicenseID());

			dataConnection.executeUpdate();
			dataConnection.clearParameters();
			dataConnection.destroy();
			dataConnection = null;
		} //end of try block
		catch (Exception ex)
		{
			System.out.println ("Exception in LicenseEJB.updateLicense:");
			ex.printStackTrace ();
		} //end of catch block
		
	} //end of updateLicense method
	
	public void deleteLicense (int licenseID)
	{
		try
		{
			CVDal dataConnection = new CVDal(dataSource);
			
			dataConnection.setSqlQuery ("delete from license where LicenseID = ? ");
			dataConnection.setInt (1, licenseID);
			
			dataConnection.executeUpdate();
			dataConnection.clearParameters();
			dataConnection.destroy();
			dataConnection = null;
		} //end of try block
		catch (Exception ex)
		{
			System.out.println ("Exception in LicenseEJB.deleteLicense:");
			ex.printStackTrace ();
		} //end of catch block
	} //end of deleteLicense method
	
	public LicenseVO getPrimaryLicense()
	{
		LicenseVO licenseDetails = null;
		CVDal dataConnection = new CVDal (dataSource);
		try
		{
			dataConnection.setSqlQuery("SELECT LicenseID, LicenseKey, LastVerified , LicenseVerification "
				+ "FROM license ORDER BY LicenseID ASC" );
			
			Collection resultsCollection = dataConnection.executeQuery();
			if (null != resultsCollection)
			{
				//No need to iterate through the collection
				//because are taking the first result.
				HashMap result = (HashMap) resultsCollection.iterator().next();
				licenseDetails = new LicenseVO ();
				licenseDetails.setLicenseID (((Long) result.get("LicenseID")).intValue());
				licenseDetails.setLicenseKey ((String) result.get("LicenseKey"));
				licenseDetails.setLicenseVerification ((String) result.get("LicenseVerification"));
				licenseDetails.setLastVerified ((java.util.Date) result.get("LastVerified"));
				resultsCollection = null;
				result = null;
			} //end of if statement
		} //end of try block
		catch (NoSuchElementException noSuchElementException)
		{
			System.out.println ("There is no license installed for this server.");
			licenseDetails = null;
		}
		catch (Exception ex)
		{
			System.out.println ("Exception in LicenseEJB.getPrimaryLicense:");
			ex.printStackTrace ();
		} finally {
		  dataConnection.destroy();
		  dataConnection = null;
		}
		return licenseDetails;
	} //end of getPrimaryLicense method
	
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

} //end of LicenseEJB class	