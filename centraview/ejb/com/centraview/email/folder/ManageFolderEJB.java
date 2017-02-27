/*
 * $RCSfile: ManageFolderEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:02 $ - $Author: mcallist $
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

package com.centraview.email.folder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.email.AccountDetail;
import com.centraview.email.Folder;
import com.centraview.email.FolderList;
import com.centraview.preference.PreferenceLocal;
import com.centraview.preference.PreferenceLocalHome;

//import com.centraview.common.*;
/**
*  This class is a Stateless Session Bean
*  which returns Data for each call
*/

public class ManageFolderEJB implements SessionBean
{
	private String dataSource = "MySqlDS";
	private SessionContext sc ;
	 /** Default constructor
     */
    public ManageFolderEJB()
    {
    }

    /**

     * Called by the container to create a session bean instance. Its parameters typically
     * contain the information the client uses to customize the bean instance for its use.
     * It requires a matching pair in the bean class and its home interface.
     */
    public void ejbCreate()
    {
    }

    /**

     * A container invokes this method before it ends the life of the session object. This
     * happens as a result of a client's invoking a remove operation, or when a container
     * decides to terminate the session object after a timeout. This method is called with
     * no transaction context.
     */
    public void ejbRemove()
    {
    }

    /**

     * The activate method is called when the instance is activated from its 'passive' state.
     * The instance should acquire any resource that it has released earlier in the ejbPassivate()
     * method. This method is called with no transaction context.
     */
    public void ejbActivate()
    {
    }

    /**

     * The passivate method is called before the instance enters the 'passive' state. The
     * instance should release any resources that it can re-acquire later in the ejbActivate()
     * method. After the passivate method completes, the instance must be in a state that
     * allows the container to use the Java Serialization protocol to externalize and store
     * away the instance's state. This method is called with no transaction context.
     */
    public void ejbPassivate()
    {
    }

    /*]*

     * Set the associated session context. The container calls this method after the instance
     * creation. The enterprise Bean instance should store the reference to the context
     * object in an instance variable. This method is called with no transaction context.
     */
    public void setSessionContext(SessionContext sc)
    {
		this.sc = sc ;
    }

	/**
	This method adds new folder  to email box
	*/
	public int  addFolder( int userId, HashMap preference)
	{
		int folderId = 0;

		try
		{
			Integer accountid = (Integer)preference.get( "AccountID" );
			int AccountID = accountid.intValue();



			Integer subfolderid = (Integer)preference.get( "SubfolderID" );
			int parentID = subfolderid.intValue();

			String newfoldername =  (String)preference.get( "foldername" );
			String foldertype =  (String)preference.get( "foldertype" );

			CVDal cvdl = new CVDal(dataSource);
			Collection v = null;
			cvdl.setSql("email.getcreatefolder");
 			cvdl.setInt( 1, parentID );
			cvdl.setInt( 2, AccountID );
			cvdl.setString( 3, newfoldername );
			cvdl.setString( 4, "User" );
			cvdl.executeUpdate();
			folderId = cvdl.getAutoGeneratedKey();

			cvdl.clearParameters();
			cvdl.destroy();

		}
		catch( Exception e )
		{
			System.out.println( "getMailMessage " );
			e.printStackTrace();
		}
		return folderId;

	}


	/**
	This method checks the presence of folder
	*/

	public int checkFoldersPresence( int userId, HashMap preference )
	{

		try
		{

			Integer accountid = (Integer)preference.get( "AccountID" );
			int AccountID = accountid.intValue();
			//System.out.println( "checkFoldersPresence AccountID "+ AccountID );

			Integer subfolderid = (Integer)preference.get( "SubfolderID" );
			int parentID = subfolderid.intValue();
			//System.out.println( "checkFoldersPresence parentID "+ parentID );

			String newfoldername =  (String )preference.get( "foldername" );
			//System.out.println( "checkFoldersPresence newfoldername "+ newfoldername );

			CVDal cvdl = new CVDal(dataSource);
			Collection v = null;
			cvdl.setSql("email.checkfolderpresence");
			cvdl.setInt( 1, parentID );
			cvdl.setString( 2, newfoldername );
			cvdl.setInt( 3, AccountID );
			v = cvdl.executeQuery();

			cvdl.clearParameters();
			cvdl.destroy();

			Iterator it = v.iterator();
			HashMap hm = ( HashMap  )it.next();
			int count = (( Integer )hm.get("foldercount" ) ).intValue() ;

			//System.out.println( " checkFoldersPresence :: count " + count );

			if ( count != 0  )
			{
				//record present there
				return 0 ;
			}else
			{
				// record not present
				return 1 ;
			}
		}
		catch( Exception e )
		{
		//System.out.println( "getMailMessage " );
		e.printStackTrace();
		}
		return 0 ;

	}

	/***
	*   This method edit the folder details
	*/
	public int editFolder( int userId, HashMap preference )
	{

		try
		{



			Integer accountid = (Integer)preference.get( "AccountID" );
			int AccountID = accountid.intValue();
			Integer parent = (Integer)preference.get( "parentid" );
			int parentid= parent.intValue();
			String newfoldername =  (String)preference.get( "foldername" );
			Integer folder =  (Integer)preference.get( "folderid" );
			int folderid= folder.intValue();

			CVDal cvdl = new CVDal(dataSource);
			Collection v = null;
			cvdl.setSql("email.geteditfolder");
			cvdl.setInt( 1, parentid );
			cvdl.setString( 2, newfoldername );
			cvdl.setInt( 3, folderid );
			cvdl.setInt( 4, AccountID  );



			cvdl.executeUpdate();
			cvdl.clearParameters();
			cvdl.destroy();


		}
		catch( Exception e )
		{
		//System.out.println( "getMailMessage " );
		e.printStackTrace();
		}
		return 0;
	}

	/**
	* Remove Folder
	* @param	int	   sourceId  Source Id of the folder.
	* @param	int    trashfolderId  Id of trash folder.
	*/
	public int removeFolder(int sourceId , int trashfolderId)
	{

		int result=0;
	 	try
		{
			CVDal cvdl = new CVDal(dataSource);
			cvdl.setSql("email.movemailupdate1");
			//System.out.println (" Email Delete Folder - " + sourceId + " trashFolderID - " + trashfolderId );
			cvdl.setInt( 1, trashfolderId);
			cvdl.setInt( 2, sourceId );
	   		cvdl.executeUpdate();
			cvdl.clearParameters();

			cvdl.setSql("email.deleteemailfolder");
			cvdl.setInt( 1, sourceId );
	   		cvdl.executeUpdate();
			cvdl.clearParameters();
		}
		catch(Exception e)
		{
	    	e.printStackTrace();
			result = 1;
		}
	  return result;
	}


  /**
  This method returns folder list
  */

  public FolderList getFolderList(int userID)
	{
		FolderList folderlist = new FolderList();
		HashMap hashmap = new HashMap();
		HashMap hmEmail = new HashMap();
		try
		{
			CVDal cvdal = new CVDal(dataSource);


      InitialContext ic = CVUtility.getInitialContext();
      PreferenceLocalHome home = (PreferenceLocalHome)ic.lookup("local/Preference");

      PreferenceLocal remote = home.create();
      remote.setDataSource(this.dataSource);

			//hmEmail = remote.getUserEmailDelegators(userID);
			hmEmail.put(new Integer(userID),Constants.VIEWSENDEMAIL);


			Set s=hmEmail.keySet();

			Iterator iter=s.iterator();
			AccountDetail accountdetail = null;


			cvdal.setSql("email.getdefaultemailaccount");
			cvdal.setInt(1, userID);

			//System.out.println("userID"+userID);
			Collection defaultaccount = cvdal.executeQuery();
			cvdal.clearParameters();

			Iterator iteratordefaultaccount = defaultaccount.iterator();
			int defaultAccount= 0;
			while(iteratordefaultaccount.hasNext())
			{
				HashMap hmAccount = (HashMap)iteratordefaultaccount.next();
				String prefAccount=(String)hmAccount.get("preference_value");
				if (prefAccount != null && !prefAccount.equals("")){
					defaultAccount=Integer.parseInt(prefAccount);
				}
			}


			while (iter.hasNext())
			{
				Integer intKey	=(Integer)iter.next();
				String str		=(String)hmEmail.get(intKey);

				//System.out.println(" str "+str);
				//System.out.println(" int  "+intKey.intValue());

			//System.out.println("The userID is: " + Integer.toString(userID));
				cvdal.setSql("email.emaillistfolder");
				cvdal.setInt(1, intKey.intValue());
				cvdal.setInt(2, intKey.intValue());
				Collection collection = cvdal.executeQuery();
				cvdal.clearParameters();




				Iterator iterator = collection.iterator();
				String signature = null;
				int j = 0;
				int accountID = 0;
				int k = 0;
				while(iterator.hasNext())
				{

					HashMap hashmap1 = (HashMap)iterator.next();
					signature = (String)hashmap1.get("signature");


					accountID = ((Number)hashmap1.get("accountID")).intValue();
					//System.out.println("accountID  ::"+accountID);
/*
					if(((String)hashmap1.get("Default")).equals("YES"))
					{
						folderlist.setDefaultaccount(accountID);
						if(((String)hashmap1.get("FolderName")).equalsIgnoreCase("Inbox"))
						{
							folderlist.setDefaultFolder(((Long)hashmap1.get("folderid")).intValue());
							//System.out.println("DEFAULT  " + folderlist.getDefaultFolder());
						}
					}
*/
					//System.out.println("defaultAccount  ::"+defaultAccount);
					if(defaultAccount == accountID){
						folderlist.setDefaultaccount(accountID);
						if(((String)hashmap1.get("FolderName")).equalsIgnoreCase("Inbox"))
						{
							folderlist.setDefaultFolder(((Long)hashmap1.get("folderid")).intValue());
							//System.out.println("DEFAULT  " + folderlist.getDefaultFolder());
						}
					}

					if(k == 0)
					{
						//System.out.println("i == 0");
						String address = (String)hashmap1.get("address");
						String accountName = (String)hashmap1.get("accountName");
						accountdetail = new AccountDetail(address);
						// 1408
						String smtpserver = (String)hashmap1.get("smtpserver");
						accountdetail.setSmtpserver( smtpserver );

						accountdetail.setRights(str);

						int folderID = ((Long)hashmap1.get("folderid")).intValue();
						int parentID = ((Long)hashmap1.get("parent")).intValue();
						String folderName = (String)hashmap1.get("FolderName");
						String folderType = (String)hashmap1.get("ftype");

						String parentName = (String)hashmap1.get("parentname");

						int unreadmessages =((Long)hashmap1.get("unreadmessages")).intValue();

						int  allmesaage = ((Long)hashmap1.get("allMesages")).intValue();
						if ( allmesaage == -1 )
						{
							hashmap1 = (HashMap)iterator.next();
							allmesaage = ((Long)hashmap1.get("allMesages")).intValue();
							k++;
						}

						accountdetail.addFolder(new Folder(folderName, folderID, parentID, folderType , allmesaage ,unreadmessages ,parentName));
						accountdetail.setAccountid(accountID);
						accountdetail.setSignature(signature  );
						j = accountID;
					}
					else if(accountID != j)
					{
						//System.out.println("lastid" + j);
						folderlist.put(new Integer(j), accountdetail);
						//System.out.println("lastid");
						String s1 = (String)hashmap1.get("address");
						String s3 = (String)hashmap1.get("accountName");

						accountdetail = new AccountDetail(s1);
						accountdetail.setRights(str);
						int l1 = ((Long)hashmap1.get("folderid")).intValue();
						int j2 = ((Long)hashmap1.get("parent")).intValue();
						String s7 = (String)hashmap1.get("FolderName");
						String parentName = (String)hashmap1.get("parentname");
						String s9 = (String)hashmap1.get("ftype");

						int unreadmessages =((Long)hashmap1.get("unreadmessages")).intValue();

						int  allmesaage = ((Long)hashmap1.get("allMesages")).intValue();
						if ( allmesaage == -1 )
						{
						  hashmap1 = (HashMap)iterator.next();
						  allmesaage = ((Long)hashmap1.get("allMesages")).intValue();
						  k++;
						}

						accountdetail.addFolder(new Folder(s7, l1, j2, s9 , allmesaage ,unreadmessages,parentName ));
						accountdetail.setAccountid(accountID);
						accountdetail.setSignature(signature  );
						j = accountID;
					}
					else
					{
						//System.out.println("same id ");
						int i1 = ((Long)hashmap1.get("folderid")).intValue();
						int j1 = ((Long)hashmap1.get("parent")).intValue();
						String s4 = (String)hashmap1.get("FolderName");
						String parentName = (String)hashmap1.get("parentname");
						String s5 = (String)hashmap1.get("ftype");

						// 1408
						String smtpserver = (String)hashmap1.get("smtpserver");

						int unreadmessages =((Long)hashmap1.get("unreadmessages")).intValue();

						int  allmesaage = ((Long)hashmap1.get("allMesages")).intValue();
						if ( allmesaage == -1 )
						{
						  hashmap1 = (HashMap)iterator.next();
						  allmesaage = ((Long)hashmap1.get("allMesages")).intValue();
						  k++;
						}

						accountdetail.addFolder(new Folder(s4, i1, j1, s5 , allmesaage ,unreadmessages, parentName));
						accountdetail.setRights(str);
						accountdetail.setAccountid(accountID);
						accountdetail.setSignature(signature  );
						j = accountID;
					}

					k++;
					//System.out.println("in while loop ---- value of k -"+k + "----" +accountdetail.getRights() + "----" + accountdetail.getAccountid());

				} //end of while loop

				folderlist.put(new Integer(j), accountdetail);
				//System.out.println(folderlist.size());
			}
			cvdal.destroy();
			cvdal = null;


		}
		catch(NullPointerException nullPointerException)
		{
			System.out.println("This User must not have an email account setup.");
			nullPointerException.printStackTrace();
		} //end of catch block (NullPointerException)
    	catch(Exception exception)
		{
			System.out.println("An error has occurred in ManageFolderEJB.getFolderList:");
			exception.printStackTrace();
		} //end of catch block (Exception)
		return folderlist;
	} //end of getFolderList method

	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	 public void setDataSource(String ds) {
	 	this.dataSource = ds;
	 }

}