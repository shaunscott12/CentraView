/*
 * $RCSfile: DirectoryAuthentication.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:40 $ - $Author: mking_cv $
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

/*****************************************************************
File		: DirectoryAuthentication.java
Description	: The class handles the user authentication against
			  LDAP and AD user directories
Dependencies: RMI, CORBA, LDAP, JNDI
*****************************************************************/

package com.centraview.administration.authentication;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchResult;

public class DirectoryAuthentication
{
	// Type of authentication
	public static final int 	AUTH_TYPE_LDAP				= 0;
	public static final int 	AUTH_TYPE_AD				= 1;
	
	// Constants required to build the environment for authentication
	public static final String 	AUTH_LDAP_URL				= "ldap://";
	public static final String 	AUTH_AD_URL					= "ldap://";
	public static final String 	AUTH_LDAP_DEFPORT			= "389";
	public static final String 	AUTH_PORT_SEP				= ":";
	public static final String 	AUTH_SERVER					= "Server";
	public static final String 	AUTH_PORT					= "Port";
	public static final String 	AUTH_USERNAME				= "Username";
	public static final String 	AUTH_PASSWORD				= "Password";
	public static final String 	AUTH_USERNAME_FIELD			= "UsernameField";
	public static final String 	AUTH_PASSWORD_FIELD			= "PasswordField";
	public static final String 	AUTH_AUTH_FIELD				= "AuthField";

	// Attributes of the LDAP/AD directory entry or Distinguished Name (DN)
	public static final String 	AUTH_ATTR_DOMAIN			= "dc";
	public static final String 	AUTH_ATTR_ORGANIZATION		= "o";
	public static final String 	AUTH_ATTR_ORGANIZATIONNAME	= "on";
	public static final String 	AUTH_ATTR_ORGANIZATIONUNIT	= "ou";
	public static final String 	AUTH_ATTR_SURNAME			= "sn";
	public static final String 	AUTH_ATTR_FIRSTNAME			= "givenname";
	public static final String 	AUTH_ATTR_COMMONNAME		= "cn";
	public static final String 	AUTH_ATTR_OBJECTCLASS		= "objectclass";
	public static final String 	AUTH_ATTR_JPEGPHOTO			= "jpegphoto";
	public static final String 	AUTH_ATTR_MAIL				= "mail";
	public static final String 	AUTH_ATTR_FAXNUMBER			= "facsimiletelephonenumber";
	public static final String 	AUTH_ATTR_TELEPHONENUMBER	= "telephonenumber";
	public static final String 	AUTH_ATTR_USERPRINCIPALNAME	= "userPrincipalName";
	public static final String 	AUTH_ATTR_MAILNICKNAME		= "mailNickname";
	public static final String 	AUTH_ATTR_DISTINGUISHEDNAME	= "distinguishedname";

	// Attribute values	
	public static final String 	AUTH_ATTR_USERDOMAIN		= "cn=users";
	public static final String 	AUTH_ATTR_VALUE_PERSON 		= "person";

	// Internal members to work around the authentication 
	private Hashtable 	properties							= null;
	private Hashtable 	environment 						= null;
	private DirContext 	dirContext 							= null;
	private Attributes 	attributes 							= null;
	
	/**
	 * Method		: setProperty
	 * Description	: Sets the individual property to be used for authenticating user and retrieving complete information, assumes that all the properties necessary will be provided by the user
	 * Parameter	: propertyName, the name of the property whole value is specified, should be one from the publically available from this class
	 * Parameter	: propertyValue, the value of the property
	 * Return		: None
	 */
	public void setProperty(String propertyName, String propertyValue)
	{
		if (null == properties)
		{
			properties = new Hashtable();
		}
		
		properties.put(propertyName, propertyValue);
	}
	
	/**
	 * Method		: getProperty
	 * Description	: Gets the value of property
	 * Parameter	: propertyName, the name of the proeprty for which to retrieve the value
	 * Return		: value of the property
	 */
	public String getProperty(String propertyName)
	{
		String propertyValue = "";
		
		if (null != properties)
		{
			propertyValue = (String)properties.get(propertyName);
		}
		
		return propertyValue;
	}
	
	/**
	 * Method		: setProperties
	 * Description	: Sets the properties to be used for authenticating user and retrieving complete information, assumes that all the properties necessary will be provided by the user
	 * Parameter	: props, the hashtable of the properties
	 * Return		: None
	 */
	public void setProperties(Hashtable props)
	{
		this.properties = props;
	}
	
	/**
	 * Method		: authenticateUser
	 * Description	: Authenticates the user on the directory
	 * Parameter	: authType, type of authentication, currently not used as AD also supports LDAP
	 * Return		: None, throws exception in case user can not be authenticated on the server
	 */
	public void authenticateUser(int authType) throws Exception
	{
		String		serverURL = null;

		try
		{
			// Create the environment if not already created
			if (null == environment)
			{
				environment = new Hashtable();
			}
			else
			{
				environment.clear();
			}
			
			// Setup the LDAP context environment 
			environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

			// Set the server url				
			serverURL = DirectoryAuthentication.AUTH_LDAP_URL 
				  		+ this.getProperty(DirectoryAuthentication.AUTH_SERVER);

			// Set the specific server port if provided else use the defgault port			
			if ((null != this.getProperty(DirectoryAuthentication.AUTH_PORT)) && 
				(0 < (this.getProperty(DirectoryAuthentication.AUTH_PORT)).length()))
			{
				serverURL = serverURL + DirectoryAuthentication.AUTH_PORT_SEP 
					  		+ this.getProperty(DirectoryAuthentication.AUTH_PORT);
			}
			else
			{
				// Use the default port
				serverURL = serverURL + DirectoryAuthentication.AUTH_PORT_SEP 
					  		+ DirectoryAuthentication.AUTH_LDAP_DEFPORT;
			}
			
			System.out.println("Server URL: " + serverURL);
			// Set the LDAP/AD server's url
			environment.put(Context.PROVIDER_URL, serverURL);

			System.out.println("Principal: " + this.getProperty(DirectoryAuthentication.AUTH_USERNAME));
			// Set the authentication username 
			environment.put(Context.SECURITY_PRINCIPAL, this.getProperty(DirectoryAuthentication.AUTH_USERNAME));

			System.out.println("Credentials: " + this.getProperty(DirectoryAuthentication.AUTH_PASSWORD));
			// Set the authentication password 
			environment.put(Context.SECURITY_CREDENTIALS, this.getProperty(DirectoryAuthentication.AUTH_PASSWORD));

			// Create the LDAP context with the environment setup 
			dirContext = new InitialDirContext(environment);
		}
		catch (Exception e)
		{
			throw e; 
		}
	}

	public void refreshAllAttributes() throws Exception
	{
		this.attributes = null;
		getAllAttributes();
	}
		
	/**
	 * Method		: getAllAttributes
	 * Description	: Gets all the attibutes of the directory object and keeps with self for later use
	 * Parameter	: None
	 * Return		: None
	 */
	public void getAllAttributes() throws Exception
	{
		String 	domain = "";
		String 	domainDN = "";
		String 	userDN = "";
		
		// Get the attributes of the person 
		if (null == attributes)
		{
			// Get the domain value if provided
			domain = this.getProperty(DirectoryAuthentication.AUTH_ATTR_DOMAIN);
			if (null == domain) domain = ""; domain.trim();
			
			// If no domain specified, retrieve it from the user's principal name provided for authentication
			// Assumption: the principal value will always be of the form xyz@abc.com .co.us etc
			if (0 == domain.length())
			{
				// Get the username provided for authentication
				domain = this.getProperty(DirectoryAuthentication.AUTH_USERNAME);
				// Get name without the email attributes
				domain = domain.substring(domain.indexOf("@")+1);
				
				// Get the complete domain's DN from the PRINCIPAL
				// Get all the domain components like 'co.uk' has 'co' and 'uk'
				// and form them into chain like 'dc=co,dc=uk'
				// We do this from the reverse side of the domain string
				while (0 < domain.length())
				{
					// If already components are present, must append as list separated by comma
					if (0 < domainDN.length())
					{
						domainDN = "," + domainDN;
					}
						
					if (0 <= domain.lastIndexOf("."))
					{
						// Get current level component
						domainDN = "dc=" + domain.substring(domain.lastIndexOf(".")+1) + domainDN;
						// Remove this domain component
						domain = domain.substring(0, domain.lastIndexOf("."));
					}
					else
					{
						// Add the last component
						domainDN = "dc=" + domain + domainDN;
						// Set to exit the loop 					
						domain = "";
					}
				}

				// Prepend the users sub-domain 
				domain = DirectoryAuthentication.AUTH_ATTR_USERDOMAIN + "," + domainDN;
			}
			
			// Get the User's Distinguished Name to search for the user entry 
			// in the user directory
			dirContext = (DirContext)dirContext.lookup(domain);

			// Create the search attributes				
			Attributes 	matchingAttrs = new BasicAttributes(true);
			// Add the attribute for principal name to search for
			matchingAttrs.put(new BasicAttribute(DirectoryAuthentication.AUTH_ATTR_USERPRINCIPALNAME, this.getProperty(DirectoryAuthentication.AUTH_USERNAME)));

			// Get the search results for this user				
			NamingEnumeration enum = dirContext.search("", matchingAttrs);

			// We are expecting only one search result, if none, then invalid user
			// BUT which is impossible because the user has already been authenticated on the server				
			SearchResult sr = (SearchResult)enum.next();
			attributes = sr.getAttributes();
		}
	}
	
	/**
	 * Method		: getAttribute
	 * Description	: Gets the value of the desired attibute
	 * Parameter	: attributeName, name of the attribute, should be from the one publically available definition from this class, SEE TOP declarations
	 * Return		: value of the attribute
	 */
	public String getAttribute(String attributeName) throws Exception
	{
		// Make sure that the attributes are retrived before getting the specifc attribute value
		this.getAllAttributes();
		
		return (String)attributes.get(attributeName).get();
	}
	
	/**
	 * Method		: compareAttribute
	 * Description	: Checks the value of the attibute against the one supplied and returns a logical answer
	 * Parameter	: attributeName, name of the attribute, should be from the one publically available definition from this class, SEE TOP declarations
	 * Parameter	: attributeValue, value of the attribute to be compared against
	 * Return		: true is same, false if different, comparison is case-insensitive
	 */
	public boolean compareAttribute(String attributeName, String attributeValue) throws Exception 
	{
		// Get the value and compare for equality
		return (0 == attributeValue.compareToIgnoreCase(this.getAttribute(attributeName)));
	}
	
	/**
	 * Method		: listAllAttributes
	 * Description	: Lists all the attibutes of the directory object already authenticated
	 * Parameter	: None
	 * Return		: None, prints to the system output the list of all attributes
	 */
	public void listAllAttributes() throws Exception
	{
		NamingEnumeration 	valueEnum = null;
		Attribute			attr = null;
		int 				aindex = 0, vindex = 0;

		this.getAllAttributes();

		// Search for objects that have those matching attributes
		NamingEnumeration attrEnum = attributes.getAll();
		
		aindex = 0;
		
		// List the attributes 
		while (attrEnum.hasMore())
		{
			attr = (Attribute)attrEnum.next();
			
			System.out.println("Attribute[ " + aindex + "]: " + attr.getID());
			
			valueEnum = attr.getAll();

			vindex = 0;
			
			while (valueEnum.hasMore())
			{
				System.out.println("Attribute value[" + vindex + "]: " + valueEnum.next());

				vindex++;
			}
			
			aindex++;			
		}
	}
	
	/**
	 * Method		: main
	 * Description	: Purely for testing the class
	 * Parameter	: args, string array for command-line parameters
	 * Return		: none
	 */
	public static void main(String args[]) throws Exception
	{
		byte	byteInput[] = new byte[256];
		String 	input = "";
		
		System.out.println("Creating the authentication object.");
		
		DirectoryAuthentication auth = new DirectoryAuthentication();

		if (null != args && null != args[0] && null != args[1] && null != args[2])
		{			
			auth.setProperty(DirectoryAuthentication.AUTH_SERVER, args[0]);
			auth.setProperty(DirectoryAuthentication.AUTH_USERNAME, args[1]);
			auth.setProperty(DirectoryAuthentication.AUTH_PASSWORD, args[2]);
		}
		else
		{
			System.out.println("Ideal invocation is as follows: ");
			System.out.println("	com.centraview.adminstration.authentication.DirectoryAuthentication server user pwd");
			System.out.println("		server 	- fully qualified servername in the form of mail.abc.com or public IP");
			System.out.println("		user 	- username in the form of xyz@abc.com, domain specification is required");
			System.out.println("		pwd 	- user's password on the server\n");

			System.out.println("Please enter the LDAP/AD Server IP or Fully qualified name (e.g. www.yahoo.com): ");
			System.in.read(byteInput);
			input = new String(byteInput);
			input = input.substring(0, input.indexOf("\r\n"));
			auth.setProperty(DirectoryAuthentication.AUTH_SERVER, input);
			
			System.out.println("Please enter the fully qualified username (e.g. xyz@abc.com): ");
			System.in.read(byteInput);
			input = new String(byteInput);
			input = input.substring(0, input.indexOf("\r\n"));
			auth.setProperty(DirectoryAuthentication.AUTH_USERNAME, input);
			
			System.out.println("Please enter the password: ");
			System.in.read(byteInput);
			input = new String(byteInput);
			input = input.substring(0, input.indexOf("\r\n"));
			auth.setProperty(DirectoryAuthentication.AUTH_PASSWORD, input);
		}
		
		System.out.println("The input received for authentication: ");
		System.out.println("Server: '" + auth.getProperty(DirectoryAuthentication.AUTH_SERVER) + "'");
		System.out.println("Username: '" + auth.getProperty(DirectoryAuthentication.AUTH_USERNAME) + "'");
		System.out.println("Password: '" + auth.getProperty(DirectoryAuthentication.AUTH_PASSWORD) + "'");
		
		// Ask to authenticate the user		
		auth.authenticateUser(DirectoryAuthentication.AUTH_TYPE_LDAP);
		
		// Try listing attributes of the attribute object for the user authenticated above
		auth.listAllAttributes();
		
		// Try listing specific attribute of the user
		System.out.println("Attribute - " + DirectoryAuthentication.AUTH_ATTR_MAIL + ": " + auth.getAttribute(DirectoryAuthentication.AUTH_ATTR_MAIL));
		System.out.println("Attribute - " + DirectoryAuthentication.AUTH_ATTR_MAIL + " - check - " + auth.compareAttribute(DirectoryAuthentication.AUTH_ATTR_MAIL, auth.getProperty(DirectoryAuthentication.AUTH_USERNAME)));
	}
}
