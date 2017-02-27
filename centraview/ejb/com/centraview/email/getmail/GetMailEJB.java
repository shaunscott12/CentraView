/*
 * $RCSfile: GetMailEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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

package com.centraview.email.getmail;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;
import com.centraview.email.Attachment;
import com.centraview.email.MailAccount;
import com.centraview.email.MailMessage;

//import com.centraview.common.*;
/**
*  This class is a Stateless Session Bean
*  which returns Data for each call
*/

public class GetMailEJB implements SessionBean
{
	private String dataSource = "MySqlDS";
	private SessionContext sc ;
	 /** Default constructor
     */
    public GetMailEJB()
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
	This method returns MailMessage object
	*/

	public MailMessage getMailMessage( int userId, HashMap preference)
	{
		MailMessage mailmessage = new MailMessage();
		try
		{

		Integer messageid = (Integer)preference.get( "MessageID" );
		int id = messageid.intValue();

		//System.out.println( "*****Message ID***** " + id );

		CVDal cvdl = new CVDal(dataSource);
		Collection v = null;



		cvdl.setSql("email.getcreatetable");
     	cvdl.executeUpdate();
     	cvdl.clearParameters();

     	cvdl.setSql("email.getmailinsert");
     	cvdl.setInt( 1, id );
		cvdl.executeUpdate();
     	cvdl.clearParameters();

     	cvdl.setSql("email.getmailupdate1");
     	cvdl.executeUpdate();
     	cvdl.clearParameters();

		cvdl.setSql("email.getmailselect");
		v = cvdl.executeQuery();
	 	cvdl.clearParameters();

		cvdl.setSql("email.getmaildrop");
     	cvdl.executeUpdate();
     	cvdl.clearParameters();
		cvdl.destroy();


		//System.out.println( " ***** Main Query Complete ***** " );

     	Iterator it = v.iterator();
		HashMap hm = ( HashMap  )it.next();


		MailAccount mailaccount = new MailAccount();

		int AccountID =  ((Long)hm.get( "AccountID" )).intValue() ;

		//System.out.println( "MailAccount" + AccountID );


		/**
		mailmessage.setAccountID( AccountID  );
		if ( ( ((Long)hm.get( "AttachmentID" )).intValue() ) != 0)
		{
			mailmessage.setAttachmentID(  ((Long)hm.get( "AttachmentID" )).intValue() ) ;
		}
		*/

		if ( (String)hm.get( "Body" ) != null )
		{
			//System.out.println( "Body" + (String)hm.get( "Body" ) );
			mailmessage.setBody( (String)hm.get( "Body" ) ) ;
		}
		//	mailmessage.setFolder( ((Long)hm.get( "folder" )).intValue() ) ;

		if ( hm.get( "FromIndividual" ) != null )
		{
			mailmessage.setFromIndividual( ((Long)hm.get( "FromIndividual" )).intValue() );
		}

		if ( (String)hm.get( "Headers" ) != null )
		{
			// IQ Added for headers
			//System.out.println( "Headers" + (String)hm.get( "Headers" ) );
			String allHeader = (String)hm.get( "Headers" );
			mailmessage.setHeaders( allHeader );
/*
			if (allHeader != null && allHeader.length() > 0)
			{
				StringTokenizer st = new StringTokenizer(allHeader,Constants.EH_HEADER_DELIMETER);

				while (st.hasMoreTokens()) {
				    String fullHeader = st.nextToken();
					int eqIndx = fullHeader.indexOf(Constants.EH_KEYVALUE_DELEMETER);
					mailmessage.setHeadersHM(fullHeader.substring(0,eqIndx).trim(),fullHeader.substring(eqIndx+1).trim());
				}
			}
*/
		}

		if ( (String)hm.get( "Importance" ) != null )
		{
			//System.out.println( "Importance" + (String)hm.get( "Importance" ) );
			mailmessage.setImportance( (String)hm.get( "Importance" ) ) ;
		}

		if ( ( Timestamp )hm.get( "MessageDate" ) != null )
		{
			//System.out.println( "MessageDate" + ( Timestamp )hm.get( "MessageDate" ) );
			mailmessage.setMessageDate( ( Timestamp )hm.get( "MessageDate" ) );
		}

		if ( ( ((Long)hm.get( "MessageID" )).intValue() ) != 0)
		{
			//System.out.println( "MessageID" + ( ((Long)hm.get( "MessageID" )).intValue() ) );
			mailmessage.setMessageID( ((Long)hm.get( "MessageID" )).intValue() ) ;
		}

		if ( ( String )hm.get( "Priority" ) != null )
		{
			//System.out.println( "Priority" + ( String )hm.get( "Priority" ) );
			mailmessage.setPriority( (String)hm.get( "Priority" ) ) ;
		}
		//	mailmessage.setStatus( (String)hm.get( "Status" ) ) ;
		if ( ( String )hm.get( "Subject" ) != null )
		{
			//System.out.println( "Subject" + ( String )hm.get( "Subject" ) );
			mailmessage.setSubject( (String)hm.get( "Subject" )) ;
		}


		mailmessage.setTheMailAccount( mailaccount ) ;

		if ( ( String )hm.get( "MailFrom" ) != null )
		{
			//System.out.println( "MailFrom" + ( String )hm.get( "MailFrom" ) );
			mailmessage.setMailFrom( (String)hm.get("MailFrom")) ;
		}


		ArrayList to = new ArrayList();
		ArrayList cc = new ArrayList();
		ArrayList bcc = new ArrayList();
		// to

		CVDal cvdlto = new CVDal(dataSource);
		Collection vto = null;
		cvdlto.setSql("email.getmailaddressTO");
		cvdlto.setInt( 1 , id );

		vto = cvdlto.executeQuery();
	 	cvdlto.clearParameters();
		cvdlto.destroy();
		Iterator itto = vto.iterator();

     	while( itto.hasNext() )
     	{
			HashMap hmto = ( HashMap  )itto.next();
			String addto = (String) hmto.get( "Address" );
			//System.out.println( addto );
			to.add ( new MailAddress( addto ) );
     	}



		// CC

		CVDal cvdlcc = new CVDal(dataSource);
		Collection vcc = null;
		cvdlcc.setSql("email.getmailaddressCC");
		cvdlcc.setInt( 1 , id );
		vcc = cvdlcc.executeQuery();
	 	cvdlcc.clearParameters();
		cvdlcc.destroy();
		Iterator itcc = vcc.iterator();
		int i=0 ;
     	while( itcc.hasNext() )
     	{
     		i++;
			HashMap hmcc = ( HashMap  )itcc.next();
			String addcc = (String) hmcc.get( "Address" );
			//System.out.println( addcc );

			cc.add ( new MailAddress( addcc ) );
     	}


		//BCC
		// change from shirish ,linesh
		CVDal cvdlbcc = new CVDal(dataSource);
		Collection vbcc = null;
		cvdlbcc.setSql("email.getmailaddressBCC");
		cvdlbcc.setInt( 1 , id );

		vbcc = cvdlbcc.executeQuery();
	 	cvdlbcc.clearParameters();
		cvdlbcc.destroy();
		Iterator itbcc = vbcc.iterator();

     	while( itbcc.hasNext() )
     	{
			HashMap hmbcc = ( HashMap  )itbcc.next();
			String addbcc = (String) hmbcc.get( "Address" );
			//System.out.println("bcc"+ addbcc );
			bcc.add ( new MailAddress( addbcc ) );
     	}

		//End  change from shirish ,linesh
		mailmessage.setTo( to ) ;
		mailmessage.setCc( cc) ;
		mailmessage.setBcc( bcc) ;

		//	mailmessage.setBcc( (String)hm.get( "bcc" ) ) ;



		/** Attachment added on 29-07-2003 */
		CVDal cvdlattch = new CVDal(dataSource);
		Collection attch = null;
		cvdlattch.setSql("email.getallattachments");
		cvdlattch.setInt( 1, id );
     	attch = cvdlattch.executeQuery();
     	cvdlattch.clearParameters();
		cvdlattch.destroy();


		Iterator itattch = attch.iterator();
		ArrayList arrayattch = new ArrayList();

		while( itattch.hasNext() )
     	{
     		HashMap hmattach = ( HashMap  )itattch.next();
			int AttachmentID = ( (Long) hmattach.get( "AttachmentID" )).intValue() ;
			int MessageID = ((Long) hmattach.get( "MessageID" )).intValue();
			int FileID = ((Long) hmattach.get( "FileID" )).intValue();
			String FileName = (String) hmattach.get( "FileName" );
			//System.out.println(AttachmentID + " "+MessageID + " "+FileID+ " "+FileName );
			Attachment a = new Attachment( AttachmentID, MessageID, FileID, FileName  );
			arrayattch.add( a );
     	}

		mailmessage.setAttachmentID( arrayattch );


		/** attchment End  */



		}
		catch( Exception e )
		{
			//System.out.println( "getMailMessage " );
			e.printStackTrace();
		}
		return mailmessage;
	}


	public boolean checkEmailAccount(int userId)
	{
		CVDal cvdl = new CVDal(dataSource);
		try{
			Collection v = null;
			cvdl.setSql("email.checkemailaccount");
			cvdl.setInt(1, userId);
			v = cvdl.executeQuery();
			cvdl.clearParameters();

			Iterator it = v.iterator();
			boolean flag= false;
			if (!it.hasNext())
			{
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public MailMessage getAttachment( int userId, HashMap preference)
	{
		MailMessage mailmessage = new MailMessage();
    CVDal cvdl = new CVDal(dataSource);
		try
		{

			Integer messageid = (Integer)preference.get( "MessageID" );
			int id = messageid.intValue();

//			System.out.println( "*****Message ID***** " + id );
			Collection v = null;

			Collection attch = null;
      cvdl.setSql("email.getallattachments");
      cvdl.setInt( 1, id );
		 	attch = cvdl.executeQuery();

			Iterator itattch = attch.iterator();
			ArrayList arrayattch = new ArrayList();

			while( itattch.hasNext() )
		 	{
		 		HashMap hmattach = ( HashMap  )itattch.next();
				int AttachmentID = ( (Long) hmattach.get( "AttachmentID" )).intValue() ;
				int MessageID = ((Long) hmattach.get( "MessageID" )).intValue();
				int FileID = ((Long) hmattach.get( "FileID" )).intValue();
				String FileName = (String) hmattach.get( "FileName" );
//				System.out.println(AttachmentID + " "+MessageID + " "+FileID+ " "+FileName );
				Attachment a = new Attachment( AttachmentID, MessageID, FileID, FileName  );
				arrayattch.add( a );
		 	}

			mailmessage.setAttachmentID( arrayattch );
		}
		catch( Exception e )
		{
			System.out.println("[Exception][GetMailEJB.getAttachment] Exception Thrown: "+e);
			e.printStackTrace();
		}
    finally
    {
      cvdl.clearParameters();
      cvdl.destroy();
    }
		return mailmessage;
	}


	/**
	This method create new email account
	*/
	public int createNewEmailAccount ( int userId, MailAccount mailaccount )
	{
		int status = 0 ;
		int accountid = 0;
		int rootid = 0;

    CVDal cvdl = new CVDal(dataSource);

		try
		{
      String address = mailaccount.getAddress();
      String leaveOnServer = mailaccount.getLeaveonserver();
      String login = mailaccount.getLogin();
      String name = mailaccount.getName();
      String password = mailaccount.getPassword();
      String replyTo = mailaccount.getReplyto();
	  String mailserver = mailaccount.getMailserver();
      String serverType = mailaccount.getServertype();
      String signature = mailaccount.getSignature();
      String smtpServer = mailaccount.getSmtpserver();
      String port = mailaccount.getPort();

/*
	System.out.println("address"+address);
	System.out.println("leaveOnServer"+leaveOnServer);
	System.out.println("login"+login);
	System.out.println("name"+name);
	System.out.println("password"+password);
	System.out.println("port"+port);
	System.out.println("mailserver"+mailserver);

	System.out.println("replyTo"+replyTo);
	System.out.println("serverType"+serverType);
	System.out.println("signature"+signature);
	System.out.println("smtpServer"+smtpServer);
*/


      boolean continueFlag = true;

      if (address != null)
      {
        if (address.equals(""))
        {
          continueFlag = false;
        }
      }else{
        continueFlag = false;
      }


      if (login != null)
      {
        if (login.equals(""))
        {
          continueFlag = false;
        }
      }else{
        continueFlag = false;
      }

      if (name != null)
      {
        if (name.equals(""))
        {
          continueFlag = false;
        }
      }else{
        continueFlag = false;
      }

      if (password != null)
      {
        if (password.equals(""))
        {
          continueFlag = false;
        }
      }else{
        continueFlag = false;
      }

      if (serverType != null)
      {
        if (serverType.equals(""))
        {
          continueFlag = false;
        }else if (serverType.equals("POP")){
          serverType = "pop3";
        }
      }else{
        continueFlag = false;
      }

      if (smtpServer != null)
      {
        if (smtpServer.equals(""))
        {
          continueFlag = false;
        }
      }else{
        continueFlag = false;
      }

   //   System.out.println("---------- checking continueFlag = [" + continueFlag + "]");

      if (continueFlag == false)
      {
      	//System.out.println("---------- returning false");
        return(0);
      }

		Collection v = null;
		cvdl.setSql("email.checkemailaccount");
		cvdl.setInt(1, userId);
		v = cvdl.executeQuery();
		cvdl.clearParameters();


		Iterator it = v.iterator();
		boolean flag= false;
		while( it.hasNext() )
	 	{
			HashMap hm = ( HashMap  )it.next();
			String emailAddress = (String)hm.get("Address") ;
			if(emailAddress != null && emailAddress.equals(address)){
				return(-1);
			}
		}

		if (v != null && v.size()==0){
			flag=true;
		}

		int portID = 125;
		if(port != null && !port.equals("")){
			portID = Integer.parseInt(port);
		}

			v = null;
			cvdl.setSql("email.createnewemailaccount");
			cvdl.setString(1, address);
			cvdl.setString(2, leaveOnServer);
			cvdl.setString(3, login);
			cvdl.setString(4, name);
			cvdl.setInt(5, userId);
			cvdl.setString(6, password);
			cvdl.setString(7, replyTo);
			cvdl.setString(8, serverType);
			cvdl.setString(9, signature);
			cvdl.setString(10, smtpServer);
			cvdl.setString(11, mailserver);
			cvdl.setInt(12, portID);
		    int updateResult = cvdl.executeUpdate();


			/**
			ADDING DEFAULT FOLDERS
			*/

			accountid = cvdl.getAutoGeneratedKey();
      if (updateResult == 0)
      {
        // something went wrong
        return(0);
      }else{
        status = accountid;
      }


			//System.out.println("Auto generated key" + accountid  );
		 	cvdl.clearParameters();

			cvdl.setSql( "email.newemailaccountfolder" );
			cvdl.setInt( 1 , 0);
			cvdl.setInt( 2 , accountid );
			cvdl.setString( 3, "root");
			cvdl.setString( 4, "SYSTEM");
			cvdl.executeUpdate();
			rootid = cvdl.getAutoGeneratedKey();
			//System.out.println( "Auto generated key" + rootid  );
			cvdl.clearParameters();



			String str[] =
			{"Inbox" , "Sent" , "Drafts" , "Trash" , "My Folders"
			};

			for( int j=0; j < str.length ; j++ )
			{

				cvdl.setSql( "email.newemailaccountfolder" );
				cvdl.setInt( 1 , rootid);
				cvdl.setInt( 2 , accountid );
				cvdl.setString( 3, str[j] );
				cvdl.setString( 4, "SYSTEM");
				cvdl.executeUpdate();
				int folderid = cvdl.getAutoGeneratedKey();
				if (str[j].equals("Inbox")){
					if (flag){

						//Collection updatedefaultemailfolder = null;
						cvdl.setSql("email.updatedefaultemailfolder");
						cvdl.setString(1, accountid+"");
						cvdl.setInt(2, userId);
						//updatedefaultemailfolder = cvdl.executeQuery();
						cvdl.executeUpdate();
						cvdl.clearParameters();

					}
				}

				cvdl.clearParameters();
				//System.out.println( "Auto generated key" + str[j]  );
			}
			cvdl.destroy();
    }catch(Exception e){
      System.out.println("[Exception][GetMailEJB] Exception thrown in createNewEmailAccount(): " + e);
      e.printStackTrace();
    }finally{
			cvdl.destroy();
      cvdl = null;
    }
System.out.println("[Doogie] status = [" + status + "]");
		return status;
	}

	/**
	This method edit email account
	*/
	public int editEmailAccount ( int userId, MailAccount mailaccount )
	{
		int status = 0 ;
		try
		{
			CVDal cvdl = new CVDal(dataSource);
			Collection v = null;
			cvdl.setSql("email.editemailaccount");

			String address = mailaccount.getAddress();
			//System.out.println( "Address::" + address );
			cvdl.setString( 1 , address );

			String Leaveonserver = mailaccount.getLeaveonserver();
			//System.out.println( "Leaveonserver:: "+Leaveonserver );
			cvdl.setString( 2 , Leaveonserver );

			String Login = mailaccount.getLogin();
			//System.out.println( "Login::"+ Login );
			cvdl.setString( 3, Login );


			String Name = mailaccount.getName();
			//System.out.println( "Name::"+ Name );
			cvdl.setString( 4 , Name );

			int Owner = mailaccount.getOwner();
			//System.out.println( "Owner::"+ Owner );
			cvdl.setInt(  5 , Owner );

			String Password = mailaccount.getPassword();
			//System.out.println( "Password::"+ Password );
			cvdl.setString( 6 , Password );

			String Replyto = mailaccount.getReplyto();
			//System.out.println( "Replyto::"+ Replyto );
			cvdl.setString( 7 , Replyto );

			String Servertype = mailaccount.getServertype();
			//System.out.println( "Servertype::"+ Servertype);
			if (Servertype.equals("POP")){
			       Servertype = "pop3";
        	}
			cvdl.setString( 8 , Servertype);

			String Signature = mailaccount.getSignature();
			//System.out.println( "Signature::"+ Signature );
			cvdl.setString( 9 , Signature );


			String Smtpserver = mailaccount.getSmtpserver();
			//System.out.println( "Smtpserver::"+ Smtpserver );
			cvdl.setString( 10 ,Smtpserver );


			String mailserver = mailaccount.getMailserver();
			//System.out.println( "Smtpserver::"+ Smtpserver );
			cvdl.setString( 11 ,mailserver );



			String port = mailaccount.getPort();

			int portID = 125;
			if(port != null && !port.equals("")){
				portID = Integer.parseInt(port);
			}

			//System.out.println( "Smtpserver::"+ Smtpserver );
			cvdl.setInt( 12 ,portID );

			int accoint = mailaccount.getAccountID();
			//System.out.println( "accoint::"+ accoint );
			cvdl.setInt(  13 , accoint );

			cvdl.executeUpdate();
		 	cvdl.clearParameters();
			cvdl.destroy();
		}
		catch( Exception e )
		{
			System.out.println( "createNewAccount " );
			e.printStackTrace();
		}
		return status;
	}


	/**
	This method edit email account
	*/
	public int deleteEmailAccount ( int userId, int AccountID )
	{
		int status = 0 ;
		try
		{
			CVDal cvdl = new CVDal(dataSource);
			Collection v = null;
			cvdl.setSql("email.deleteemailaccount");
			cvdl.setInt(  1 , AccountID );
		 	cvdl.executeUpdate();
		 	cvdl.clearParameters();
			cvdl.destroy();
		}
		catch( Exception e )
		{
			System.out.println( "deleteEmailAccount " );
			e.printStackTrace();
		}
		return status;
	}


	public MailAccount getAccountDetails( int userid  , HashMap preference )
	{

		MailAccount mailaccount = new MailAccount();
		try
		{

			//System.out.println( "getAccountDetails"+ (Integer)preference.get("AccountID") );
			int accountid = ((Integer)preference.get("AccountID")).intValue();
			System.out.println( "getAccountDetails"+ accountid );

			CVDal cvdl = new CVDal(dataSource);
			Collection v = null;
			cvdl.setSql("email.getemailaccountforedit");
			cvdl.setInt(  1 , accountid );
			v = cvdl.executeQuery();
			cvdl.clearParameters();
			cvdl.destroy();

			Iterator it = v.iterator();
			HashMap hm = ( HashMap  )it.next();

			int account = ( (Number)hm.get("AccountID")).intValue();

			mailaccount.setAccountID( account );

			String address = (String)hm.get("Address") ;
			//System.out.println( address);
			mailaccount.setAddress( address );

			String leaveonserver = (String)hm.get("LeaveOnServer") ;
			//System.out.println( leaveonserver);
			mailaccount.setLeaveonserver( leaveonserver );

			String login = (String)hm.get("Login") ;
			//System.out.println( login);
			mailaccount.setLogin( login );

			String mailserver = (String)hm.get("mailserver") ;
			//System.out.println( mailserver );
			mailaccount.setMailserver(mailserver);

			String smtpserver = (String)hm.get("SMTPServer") ;
			//System.out.println( mailserver );
			mailaccount.setSmtpserver(smtpserver);

			String port = ((Number)hm.get("Port")).intValue() +"";
			//System.out.println( mailserver );
			mailaccount.setPort(port);

			String name = (String)hm.get("Name") ;
			//System.out.println( name);
			mailaccount.setName(name );

			String password = (String)hm.get("Password") ;
			//System.out.println( password);
			mailaccount.setPassword( password );

			String replyto = (String)hm.get("ReplyTo") ;
			//System.out.println( replyto);
			mailaccount.setReplyto( replyto );

			String servertype = (String)hm.get("ServerType") ;
			//System.out.println( servertype );
			mailaccount.setServertype(servertype );

			String signature = (String)hm.get("Signature") ;
			//System.out.println( signature);
			mailaccount.setSignature( signature );





		}
		catch( Exception e )
		{
			System.out.println( "getAccountDetails " );
			e.printStackTrace();
		}
		return mailaccount;
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
