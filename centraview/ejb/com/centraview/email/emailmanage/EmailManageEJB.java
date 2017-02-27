/*
 * $RCSfile: EmailManageEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:14 $ - $Author: mking_cv $
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


package com.centraview.email.emailmanage;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import com.centraview.common.CVDal;

/**
  * emailMoveTo : Moves email to particulat folder
  * emailDelete : Deletes email from particula folder
  * emailDeleteTrash : Physical deletion of email,
  * emailMarkasRead : Mark as Read Email.
*/

public class EmailManageEJB implements SessionBean
{
	protected javax.ejb.SessionContext ctx;
	protected Context environment;
	private String dataSource = "MySqlDS";

	public void setSessionContext(SessionContext ctx) throws RemoteException
	{
		this.ctx = ctx;
	}

	public void ejbActivate()   { }
	public void ejbPassivate()   { }
	public void ejbRemove()   { }
	public void ejbCreate()  { }

	/**
	* Moves email into particular folder.
	* delegates to PreparedStatement's setBoolean
	* @param	int	   sourceId  Source Id of the folder.
	* @param	int    destId    Destination Id of the folder.
	* @param	string mailIdList Mail ID List.
	*/
  public int emailMoveTo(int sourceId, int destId, String mailIdList[])
  {
    String allMailId="";
		int result = 0;

    int mailId = Integer.parseInt(mailIdList[0]);

    try
    {
      allMailId = allMailId + "(";

      for (int i=0; i<mailIdList.length; i++)
      {
        allMailId = allMailId + mailIdList[i];
        if (i != mailIdList.length-1)
        {
          allMailId = allMailId + ",";
        }
      }
      allMailId = allMailId + ")";

      CVDal cvdl = new CVDal(dataSource);

      String str = "update emailstore set emailfolder = " + destId  + " where emailfolder = " + sourceId + " and messageid in " + allMailId;

      cvdl.setSqlQueryToNull();
      cvdl.setSqlQuery(str);
      cvdl.executeUpdate();
      cvdl.clearParameters();
      cvdl.destroy();
    }catch(Exception e){
      System.out.println("[EmailManageEJB] Exception thrown: " + e);
      //e.printStackTrace();
      result = 1;
    }

    return result;
  }   // end emailMoveTo() method

    /**
    * Delete email's from particular folder.
    * @param	int	   sourceId  Source Id of the folder.
    * @param	int    trashfolderId  Id of trash folder.
    * @param	string mailIdList Mail ID List.
    */
    public int emailDelete(int sourceId ,int trashfolderId,String mailIdList[])
    {
		int result=0;

     	try
    	{
		if (sourceId == trashfolderId)
	 		   result = emailDeleteTrash(trashfolderId,mailIdList);
		else
           result = emailMoveTo(sourceId ,trashfolderId,mailIdList);
    	}
    	catch(Exception e)
    	{
        	e.printStackTrace();
    		result = 1;
    	}
   	  return result;
    }


    /**
    * Get email's sender information from Message.
    * @param	string mailIdList Mail ID List.
    */
	public ArrayList getEmailsFrom(String mailIdList[]){
		ArrayList result=new ArrayList();
		CVDal cvdl = null;
		try
		{
			String allMailId="";
			int mailId = Integer.parseInt(mailIdList[0]);

			allMailId = allMailId + "(";
			for (int i=0;i < mailIdList.length;i++)
			{
				allMailId = allMailId + mailIdList[i];
				if (i != mailIdList.length-1)
				   allMailId = allMailId + ",";
			}
			allMailId = allMailId + ")";

			cvdl = new CVDal(dataSource);

			String str = "select mailFrom from emailmessage where MessageID in "+ allMailId + ";";
			cvdl.setSqlQueryToNull();
			cvdl.setSqlQuery(str);
			Collection col = cvdl.executeQuery();
			cvdl.clearParameters();

			Iterator iterator = col.iterator();
			while(iterator.hasNext())
			{
				HashMap emailsFrom = (HashMap)iterator.next();
				String signature = (String)emailsFrom.get("mailFrom");
				result.add(signature);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			cvdl.clearParameters();
			cvdl.destroy();
			cvdl = null;
		}
		return result;
	}

    /**
    * Physical deletes of email from Trash folder.
    * @param	int    trashfolderId  Id of trash folder.
    * @param	string mailIdList Mail ID List.
    */

	public int emailDeleteTrash(int trashfolderid,String mailIdList[])
   {
       int result=0;
       CVDal cvdl = null;
	   try
	   {
          String allMailId="";
	 	  int mailId = Integer.parseInt(mailIdList[0]);

		  allMailId = allMailId + "(";
		  for (int i=0;i < mailIdList.length;i++)
		  {
		    allMailId = allMailId + mailIdList[i];
		    if (i != mailIdList.length-1)
		       allMailId = allMailId + ",";
		  }
		   allMailId = allMailId + ")";

		  cvdl = new CVDal(dataSource);

		  String str = "delete from emailstore " + "where emailfolder = " + trashfolderid  + " and messageid in " + allMailId + ";";
		  cvdl.setSqlQueryToNull();
		  cvdl.setSqlQuery(str);
		  cvdl.executeUpdate();
		  cvdl.clearParameters();

	    for (int i=0;i < mailIdList.length;i++)
	    {

	 	  mailId = Integer.parseInt(mailIdList[i]);

		 //"select from emailstore where EmailFolder = ? and Messageid = ? ;");
	      cvdl.setSql("email.selectemailstore");
  	  	  cvdl.setInt( 1, trashfolderid);
	  	  cvdl.setInt( 2, mailId);

		  Collection col = cvdl.executeQuery();
	  	  cvdl.clearParameters();

		  Iterator it = col.iterator();

		  if (!(it.hasNext()))
		  {
		   cvdl.setSql("email.deleteattachment");
		   cvdl.setInt( 1, mailId);
		   cvdl.executeUpdate();
		   cvdl.clearParameters();

 		   cvdl.setSql("email.deleteemailrecipient");
		   cvdl.setInt( 1, mailId);
		   cvdl.executeUpdate();
		   cvdl.clearParameters();

 		   cvdl.setSql("email.deleteemailmessage");
		   cvdl.setInt( 1, mailId);
		   cvdl.executeUpdate();
		   cvdl.clearParameters();
		  }
	     }
	   }
	   	catch(Exception e)
    	{
            System.out.println("[Exception][EmailManageEJB.emailDeleteTrash] Exception Thrown: "+e);
        	e.printStackTrace();
    		result = 1;
       	}
        finally
        {
          cvdl.clearParameters();
          cvdl.destroy();
          cvdl = null;
        }
		return result;
	}

	/**
	* Marks email as Read
	* @param	int    sourceId  Id of source folder.
	* @param	int    readflag  Flag to indicate
	*				   read/unread
	* @param	string mailIdList Mail ID List.
	*/
	public int emailMarkasRead(int sourceId , int readflag, String mailIdList[])
    {
        String allMailId="";
    	int result=0;
		String str;

    	int mailId = Integer.parseInt(mailIdList[0]);

     	try
    	{
			 allMailId = allMailId + "(";
		    for (int i=0;i < mailIdList.length;i++)
			{
			  allMailId = allMailId + mailIdList[i];
			  if (i != mailIdList.length-1)
  			     allMailId = allMailId + ",";
			}
			 allMailId = allMailId + ")";

			 CVDal cvdl = new CVDal(dataSource);

			 if (readflag == 1)
			     str	 = "update emailstore set ReadStatus = " + "\'YES\'"  + "  where emailfolder = " + sourceId + " and messageid in " + allMailId + ";";
			 else
			     str	 = "update emailstore set ReadStatus = " + "\'NO\'"  + "  where emailfolder = " + sourceId + " and messageid in " + allMailId + ";";

			 //System.out.println ("*** This is Final String for Mark as Read Method ***");
	 		 //System.out.println (str);
			 cvdl.setSqlQueryToNull();
			 cvdl.setSqlQuery(str);
	 		 cvdl.executeUpdate();
			 cvdl.clearParameters();
 			 cvdl.destroy();
    	}
    	catch(Exception e)
    	{
        	e.printStackTrace();
    		result = 1;
    	}
	    return result;
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
