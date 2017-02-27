/*
 * $RCSfile: SaveDraftEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:19 $ - $Author: mking_cv $
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

package com.centraview.email.savedraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.centraview.common.CVDal;
import com.centraview.email.MailAddress;
import com.centraview.email.MailMessage;

//import com.centraview.common.*;
/**
*  This class is a Stateless Session Bean
*  which returns Data for each call
*/

public class SaveDraftEJB implements SessionBean
{

	SessionContext sc ;
	private String dataSource = "MySqlDS";
	 /** Default constructor
     */
    public SaveDraftEJB()
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
	this method save email data to draft folder
	which contains data of message
	*/
	public int saveDraft( int userId, MailMessage mailmessage )
	{
		int messageid = 0;

		//System.out.println( " ********************attchments ***************************** "  );
		try
		{

			int accountid = mailmessage.getAccountID() ;
			HashMap attchmentids  = mailmessage.getAttachFileIDs() ;

			//get bcc
			ArrayList bcc =  mailmessage.getBcc();
			String arraybcc[] = new String[ bcc.size() ];
			for( int i=0 ; i < arraybcc.length ; i ++ )
			{
				MailAddress ma = ( MailAddress )bcc.get( i );
				arraybcc[i] = ma.getAddress();
			}

			//System.out.println( "arraybcc"+arraybcc.length );

			//get cc
			ArrayList cc = mailmessage.getCc();
			String arraycc[] = new String[ cc.size() ];
			for( int i=0 ; i < arraycc.length ; i ++ )
			{
				MailAddress ma = ( MailAddress )cc.get( i );
				arraycc[i] = ma.getAddress();
			}

			//System.out.println( "arraycc"+arraycc.length );

			ArrayList to = mailmessage.getTo();
			String arrayto[] = new String[ to.size() ];
			for( int i=0 ; i < arrayto.length ; i ++ )
			{
				MailAddress ma = ( MailAddress )to.get( i );
				arrayto[i] = ma.getAddress();
			}

			//System.out.println( "arrayto"+arrayto.length );

			String subject = mailmessage.getSubject();
			String body = mailmessage.getBody();

			//System.out.println("subject"+  subject );
			//System.out.println("body"+ body );


			//ALLSQL.put("email.savedraft","insert into emailmessage( MessageDate, MailFrom, Subject, Body, AccountID ) values( ?,?,?,?,? )");


			CVDal cvdl = new CVDal(dataSource);

			cvdl.setSql( "email.savedraft1" );


			cvdl.setString( 1, mailmessage.getMailFrom() );
			cvdl.setString( 2, subject );
			cvdl.setString( 3, "" );
			cvdl.setString( 4, body );
			cvdl.setString( 5, null );
			cvdl.setInt( 6, accountid );
			cvdl.setInt( 7, userId );
	     	cvdl.executeUpdate();
			//cvdl.clearParameters();

			messageid = cvdl.getAutoGeneratedKey();
			//System.out.println("Auto generated key" + messageid  );
			cvdl.clearParameters();
			cvdl.setSql( "email.savedraft2" );
			cvdl.setInt( 1, mailmessage.getFolder() );
			cvdl.setInt( 2, messageid );
			cvdl.setString( 3, "NO" );
	     	cvdl.executeUpdate();
			cvdl.clearParameters();

			if ( arrayto.length != 0 )
			{
				for( int i=0 ; i < arrayto.length ; i ++ )
				{
	// MessageID , Address , RecipientType ,RecipientIsGroup
				cvdl.setSql( "email.savedraft3" );

				cvdl.setInt( 1, messageid );
				cvdl.setString( 2, arrayto[i] );
				cvdl.setString( 3, "TO" );
				cvdl.setString( 4, "NO" );
		     	cvdl.executeUpdate();
				cvdl.clearParameters();

				}
			}

			if ( arraybcc.length != 0 )
			{
				for( int i=0 ; i < arraybcc.length ; i ++ )
				{
					cvdl.setSql( "email.savedraft3" );
					cvdl.setInt( 1, messageid );
					cvdl.setString( 2, arraybcc[i] );
					cvdl.setString( 3, "BCC" );
					cvdl.setString( 4, "NO" );
					cvdl.executeUpdate();
					cvdl.clearParameters();
				}
			}

			if ( arraycc.length != 0 )
			{
				for( int i=0 ; i < arraycc.length ; i ++ )
				{
					cvdl.setSql( "email.savedraft3" );
					cvdl.setInt( 1, messageid );
					cvdl.setString( 2, arraycc[i] );
					cvdl.setString( 3, "CC" );
					cvdl.setString( 4, "NO" );
					cvdl.executeUpdate();
					cvdl.clearParameters();
				}
			}

			if ( attchmentids != null  )
			{
				if ( attchmentids.size() != 0 )
				{

						Set col = attchmentids.keySet();
						Iterator itt = col.iterator();
						int i=0;
						while( itt.hasNext() )
						{
							String fileid = ( String )itt.next() ;
	 						String name  = (String)attchmentids.get( fileid );
							//System.out.println("fileid" +  fileid );
							//System.out.println("name" +  name );
							cvdl.setSql( "email.savedraftattchment" );
							cvdl.setInt( 1, messageid );
							cvdl.setString( 2, name );
							cvdl.setInt( 3, Integer.parseInt( fileid ) );
							cvdl.executeUpdate();
							cvdl.clearParameters();
							i++;
						}


				}
			}



			cvdl.destroy();

		}
		catch(Exception e )
		{
			e.printStackTrace();
		}
		return messageid ;


	}


	/**
	this method save email data to draft folder
	which contains data of message
	*/
	public int editDraft( int userId, MailMessage mailmessage )
	{
		try
		{

		int messageid = mailmessage.getMessageID();
		int accountid = mailmessage.getAccountID() ;

		java.sql.Timestamp messagedate = (java.sql.Timestamp )mailmessage.getMessageDate();
		String subject = mailmessage.getSubject();
		String body = mailmessage.getBody();

		//System.out.println( "messageid" + messageid );
		//System.out.println( "accountid" + accountid );
		//System.out.println( "subject" + subject );
		//System.out.println( "body" + body );

		CVDal cvdl = new CVDal(dataSource);
		cvdl.setSql( "email.updatedraft1" );

		cvdl.setString( 1,  mailmessage.getMailFrom()  );
		cvdl.setString( 2, subject );
		cvdl.setString( 3, body );
		cvdl.setInt( 4, accountid );
  		cvdl.setInt( 5, messageid );
     	cvdl.executeUpdate();
		cvdl.clearParameters();

		//System.out.println( "-----------email.updatedraft1-------------------");

		cvdl.setSql( "email.deletedraft1" );
		cvdl.setInt( 1, messageid );
     	cvdl.executeUpdate();
		cvdl.clearParameters();

		//System.out.println( "-----------email.deletedraft1-------------------");

		cvdl.setSql( "email.deletedraft2" );
		cvdl.setInt( 1, messageid );
     	cvdl.executeUpdate();
		cvdl.clearParameters();
		//System.out.println( "-----------email.deletedraft2-------------------");

		cvdl.setSql( "email.deletedraftattchment" );
		cvdl.setInt( 1, messageid );
     	cvdl.executeUpdate();
		cvdl.clearParameters();

			//System.out.println( "-----------email.deletedraftattchment-------------------");


		HashMap attchmentids  = mailmessage.getAttachFileIDs() ;

		//get bcc
		ArrayList bcc =  mailmessage.getBcc();
		String arraybcc[] = new String[ bcc.size() ];
		for( int i=0 ; i < arraybcc.length ; i ++ )
		{
			MailAddress ma = ( MailAddress )bcc.get( i );
			arraybcc[i] = ma.getAddress();
		}

		//System.out.println( "arraybcc"+arraybcc.length );

		//get cc
		ArrayList cc = mailmessage.getCc();
		String arraycc[] = new String[ cc.size() ];
		for( int i=0 ; i < arraycc.length ; i ++ )
		{
			MailAddress ma = ( MailAddress )cc.get( i );
			arraycc[i] = ma.getAddress();
		}

		//TO
		ArrayList to = mailmessage.getTo();
		String arrayto[] = new String[ to.size() ];
		for( int i=0 ; i < arrayto.length ; i ++ )
		{
			MailAddress ma = ( MailAddress )to.get( i );
			arrayto[i] = ma.getAddress();
		}


		cvdl.setSql( "email.savedraft2" );
		cvdl.setInt( 1, mailmessage.getFolder() );
		cvdl.setInt( 2, messageid );
		cvdl.setString( 3, "NO" );
     	cvdl.executeUpdate();
		cvdl.clearParameters();

		if ( arrayto.length != 0 )
		{
			for( int i=0 ; i < arrayto.length ; i ++ )
			{
				cvdl.setSql( "email.savedraft3" );
				cvdl.setInt( 1, messageid );
				cvdl.setString( 2, arrayto[i] );
				cvdl.setString( 3, "TO" );
				cvdl.setString( 4, "NO" );
		     	cvdl.executeUpdate();
				cvdl.clearParameters();
			}
		}

		if ( arraybcc.length != 0 )
		{
			for( int i=0 ; i < arraybcc.length ; i ++ )
			{
				cvdl.setSql( "email.savedraft3" );
				cvdl.setInt( 1, messageid );
				cvdl.setString( 2, arraybcc[i] );
				cvdl.setString( 3, "BCC" );
				cvdl.setString( 4, "NO" );
				cvdl.executeUpdate();
				cvdl.clearParameters();
			}
		}

		if ( arraycc.length != 0 )
		{
			for( int i=0 ; i < arraycc.length ; i ++ )
			{
				cvdl.setSql( "email.savedraft3" );
				cvdl.setInt( 1, messageid );
				cvdl.setString( 2, arraycc[i] );
				cvdl.setString( 3, "CC" );
				cvdl.setString( 4, "NO" );
				cvdl.executeUpdate();
				cvdl.clearParameters();
			}
		}

		if ( attchmentids != null  )
		{
			if ( attchmentids.size() != 0 )
			{

					Set col = attchmentids.keySet();
					Iterator itt = col.iterator();
					int i=0;
					while( itt.hasNext() )
					{
						String fileid = ( String )itt.next() ;
 						String name  = (String)attchmentids.get( fileid );
						//System.out.println("fileid" +  fileid );
						//System.out.println("name" +  name );
						cvdl.setSql( "email.savedraftattchment" );
						cvdl.setInt( 1, messageid );
						cvdl.setString( 2, name );
						cvdl.setInt( 3, Integer.parseInt( fileid ) );
						cvdl.executeUpdate();
						cvdl.clearParameters();
						i++;
					}


			}
		}

			cvdl.destroy();
		}
		catch(Exception e )
		{
			e.printStackTrace();
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