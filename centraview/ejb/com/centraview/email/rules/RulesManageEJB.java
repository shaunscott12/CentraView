/*
 * $RCSfile: RulesManageEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:17 $ - $Author: mking_cv $
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


package com.centraview.email.rules;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import com.centraview.common.CVDal;
import com.centraview.email.RuleDetails;

/**
  * addRule   : Add Rule
  * editRule  : Edit Rule
  * deleteRule : Delete Rule
  * getRuleDetails : get Rule Details
*/

public class RulesManageEJB implements SessionBean
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
	* gets rule details for particular rule.
 	* @param	int	   ruleid  Rule Id for getting details.
	*/
	public RuleDetails getRuleDetails(int ruleid)
	{

	  RuleDetails rd = new RuleDetails();
	  HashMap hashmap = new HashMap();
	  try
	  {
		CVDal cvdl = new CVDal(dataSource);
		cvdl.setSql("email.selectemailrule");
		cvdl.setInt(1,ruleid);
		//System.out.println (" ** EJB rule id " + ruleid);

		Collection col = cvdl.executeQuery();
	if (col != null)
	{
		Iterator it = col.iterator();
	    HashMap hm = ( HashMap  )it.next();
	    cvdl.clearParameters();


		  String RuleId = hm.get("ruleid" ).toString();

		  String name  = ( String )hm.get("rulename" );
		  String description = ( String )hm.get("description" );
		  String enabled = ( String )hm.get("enabledstatus" );
		  String AccountID = hm.get("accountid" ).toString();
  		  String rulestatement = (String )hm.get("rulestatement");
		  //System.out.println (" **** RuleManageEJB -- GetRuleDetails Method ");
		  //System.out.println ( " rule id " + ruleid);
  		  //System.out.println ( " rule name " + name);
   		  //System.out.println ( " description " + description);
  		  //System.out.println ( " enabled  " + enabled);
  		  //System.out.println ( " account id  " + AccountID);
  		  //System.out.println ( " rules statement  " + rulestatement);


		  rd.setRuleID (RuleId);
		  rd.setAccountID(AccountID);
		  rd.setName(name);
		  rd.setDescription(description);
		  rd.setEnabled(enabled);

		  StringTokenizer st = new  StringTokenizer(rulestatement,"<");

		  StringTokenizer st1 = new  StringTokenizer(rulestatement,"<");
		  int x=0;
		while(st1.hasMoreTokens())
		{
			String strcolA = ( String )st1.nextToken();
			String strcolB = ( String )st1.nextToken();
			String strcolC = ( String )st1.nextToken();
			String strcolD = ( String )st1.nextToken();
			x++;
		}
		String colA[] = new String[x];
		String colB[] = new String[x];
		String colC[] = new String[x];
		String colD[] = new String[x];

	      for (int i=0; (st.hasMoreTokens()); i++)
		  {

		  	String strcolA = ( String )st.nextToken();
			String strcolB = ( String )st.nextToken();
			String strcolC = ( String )st.nextToken();
			String strcolD = ( String )st.nextToken();
			//System.out.println("strcolA :: "+strcolA);
			//System.out.println("strcolB :: "+strcolB);
			//System.out.println("strcolC :: "+strcolC);
			//System.out.println("strcolD :: "+strcolD);
		  	colA[i] = strcolA;
		  	colB[i] = strcolB;
		  	colC[i] = strcolC;
		  	colD[i] = strcolD;
			//st.nextToken();
		  }
			cvdl.setSql("email.selectemailaction");
			cvdl.setInt(1,ruleid);
			Collection col1 = cvdl.executeQuery();
			//System.out.println (" col1 " + col1);
			Iterator it1 = col1.iterator();
		    HashMap hm1 = new  HashMap();

	    while ( it1.hasNext())
	    {
				hm1 = ( HashMap  )it1.next();
			if (hm1 != null)
			{
				//System.out.println (" hm1 " + hm1.size());

	 			  //System.out.println (" In while loop ");
		 	      String actionname  = ( String )hm1.get("ActionName" );
			      String ID = hm1.get("TargetID" ).toString();
				  //System.out.println (" Action Name " + actionname);
	  			  //System.out.println (" Target ID " + ID);

				  actionname = actionname.trim();
				  if (actionname.equals("MOVE"))
				  {
				  	//System.out.println (" In Move ");
				     rd.setMovemessageto("1");
				  }
					 rd.setMovemessagetofolder(ID);

				  if (actionname.equals("DELETE"))
				  {
				  	//System.out.println (" In Delete ");
				     rd.setDeleteMessage("1");
				  }

				  if (actionname.equals("MARK_AS_READ"))
				  {
	  			  	//System.out.println (" In Mark Read ");
				  	 rd.setMarkasread("1");
				  }
		    }
		  }

		  //System.out.println (" Movemessageto: "+rd.getMovemessageto());
		  //System.out.println (" DeleteMessage: "+rd.getDeleteMessage());
		  //System.out.println (" Markasread: "+rd.getMarkasread());
			  rd.setcolA(colA);
			  rd.setcolB(colB);
			  rd.setcolC(colC);
			  rd.setcolD(colD);
		   }

	        cvdl.clearParameters();
	        cvdl.destroy();
        }
        catch(Exception e )
        {
         	e.printStackTrace();
        }


  	  return rd ;
  }

  /**
  * Adds Rule in rules related tables
  * @param	HashMap containing all the fields of add rule
  */
	public int addRule(HashMap preference)
	{
	  int result=0;
	  int moveid=0;
	  int moveAction=0;
	  int deleteAction=0;
	  int markread=0;
	  int accountid=0;
		  		  ;
	  String ruleStatement="";
	  try
	  {

 	    //System.out.println (" addRule  Method ");
 	    String accid = (String)preference.get("AccountID");
 	    if (accid != null && !accid.equals(""))
 	    {
 	    	try
 	    	{
 	    	accountid = Integer.parseInt(accid);
 	    	}
 	    	catch (NumberFormatException e )
 	    	{
 	    	}
 	    }


 	    //System.out.println ("New  One ");

		String fid = (String)preference.get("MoveFolderId");
        if (fid != null && !fid.equals(""))
		{
			try
			{
			moveid = Integer.parseInt(fid);
			}
			catch (NumberFormatException e )
			{
			}
		}
		String amove =	(String)preference.get("ActionMoveMessage");
        if (amove != null && amove.equals("1"))
		    moveAction = Integer.parseInt(amove);

         //System.out.println (" ** Before Delete Combo ");

		 String deletecbox = (String)preference.get("ActionDeleteMessage");

        if (deletecbox != null && deletecbox.equals("1"))
		    deleteAction = Integer.parseInt(deletecbox);

		String readcbox =(String)preference.get("ActionMarkasRead");
		if 	(readcbox != null && readcbox.equals("1"))
 		    markread= Integer.parseInt(readcbox);

	    //System.out.println (" Three ");

		String ruleName =  (String)preference.get( "RuleName" );
		String description =  (String)preference.get( "Description" );
		String enabledStatus =  (String)preference.get( "EnabledStatus" );

 	    //System.out.println (" Four ");
		String[] join      = (String []) preference.get("Join");
		String[] field     = (String []) preference.get("Field");
		String[] condition = (String []) preference.get("Condition");
		String[] criteria  = (String []) preference.get("Criteria");


		String applyAction =  (String)preference.get( "ApplyAction" );
 	    //System.out.println (" Five ");

        String crt = "";
		ruleStatement="";
		for(int i=0;i < join.length;i++)
		{
			//System.out.println (" I = " + i + "Lenght = " + join.length);
			ruleStatement = ruleStatement + "<" + join[i] + "<" +
			field[i] + "<" + condition[i] + "<" +
			criteria[i];

			//System.out.println("join[i] :::::::::::" + join [i]);
			//System.out.println("field[i] :::::::::::" + field [i]);
			//System.out.println("condition[i] :::::::::::" + condition [i]);
			//System.out.println("criteria[i] :::::::::::" + criteria [i]);

		}

 	    //System.out.println (" Six ");
		//System.out.println (" ruleStatement : " + ruleStatement);
		//System.out.println (" moveid  : " + moveid);
		//System.out.println (" moveaction  : " + moveAction);
		//System.out.println (" deleteaction  : " + deleteAction);
		//System.out.println (" markread  : " + markread);

		String flagValue = "FALSE";

		String junkMail =(String)preference.get("JunkMail");
		if(junkMail != null){
			flagValue = "TRUE";
		}

		CVDal cvdl = new CVDal(dataSource);
		cvdl.setSql("email.insertrule");
 		cvdl.setString( 1, ruleName);
		cvdl.setString( 2, description);
		cvdl.setString( 3, enabledStatus );
		cvdl.setInt( 4, accountid);
		cvdl.setString( 5,ruleStatement);
		cvdl.setString( 6,flagValue);
		cvdl.executeUpdate();

     	int ruleId = cvdl.getAutoGeneratedKey();

		result = ruleId;
     	//System.out.println ("\n\n\n\n\n IQ:: Rule Id : " + ruleId);

     	cvdl.clearParameters();



		if (moveAction == 1)
		{
			//System.out.println (" Entry in move Action ");
			cvdl.setSql("email.insertemailaction");
			cvdl.setInt(1, ruleId);
			cvdl.setString( 2, "MOVE");
			cvdl.setInt (3, moveid);
			cvdl.executeUpdate();
	     	cvdl.clearParameters();
		}
		if (markread  == 1)
		{
			//System.out.println (" Entry in Mark as Read ");
			cvdl.setSql("email.insertemailaction");
			cvdl.setInt(1, ruleId);
			cvdl.setString( 2, "MARK_AS_READ");
			cvdl.setInt (3, moveid);
			cvdl.executeUpdate();
			cvdl.clearParameters();
		}
		if (deleteAction == 1)
		{
			//System.out.println (" Entry in Mark as Read ");
			cvdl.setSql("email.insertemailaction");
			cvdl.setInt(1, ruleId);
	    	cvdl.setString( 2, "DELETE");
			cvdl.setInt (3, 0);
			cvdl.executeUpdate();
	     	cvdl.clearParameters();
		}
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
    * Get ruleId for the JunkMail rule.
    * @param	int accountid.
    */
	public int getRuleId(int accountid){
		int result = 0;
		CVDal cvdl = null;
		try
		{
			cvdl = new CVDal(dataSource);
			String str = "select ruleid from emailrules where flag='TRUE' and accountid="+ accountid + ";";
			cvdl.setSqlQueryToNull();
			cvdl.setSqlQuery(str);
			Collection col = cvdl.executeQuery();
			cvdl.clearParameters();

			Iterator iterator = col.iterator();
			while(iterator.hasNext())
			{
				HashMap ruleId = (HashMap)iterator.next();
			    result = ((Number) ruleId.get("ruleid")).intValue();
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
	* edit Rule in rules related tables
	* @param	HashMap containing all the fields of edit rule
	*/
	public int editRule(HashMap preference)
	{
	  int result=0;
	  String ruleStatement="";
	  int accountid=0;
  	  int moveid=0;
	  int ruleid=0;
	  int moveAction=0;
  	  int deleteAction=0;
	  int markread=0;
	  try
	  {

	    //System.out.println (" editRule  Method ");

			 String rid = (String)preference.get("RuleID");
	    	 if (rid != null && !rid.equals(""))
	    	 {
	    	 	try
	    	 	{
	    	 	 ruleid = Integer.parseInt(rid);
	    	 	}
	    	 	catch (NumberFormatException e )
	    	 	{
	    	 	}
	    	 }

	    	 //System.out.println ("New One ");

	    	 String accid = (String)preference.get("AccountID");
	    	 if (accid != null && !accid.equals(""))
	    	 {
	    	 	try
	    	 	{
	    	 	accountid = Integer.parseInt(accid);
	    	 	}
	    	 	catch (NumberFormatException e )
	    	 	{
	    	 	}
	    	 }


	    	 String fid = (String)preference.get("MoveFolderId");
	    	 if (fid != null && !fid.equals(""))
	    	 {
	    	 	try
	    	 	{
	    	 	moveid = Integer.parseInt(fid);
	    	 	}
	    	 	catch (NumberFormatException e )
	    	 	{
	    	 	}
	    	 }
	    	 String amove =	(String)preference.get("ActionMoveMessage");
	    	 if (amove != null && amove.equals("1"))
	    	     moveAction = Integer.parseInt(amove);

	    	  //System.out.println (" ** Before Delete Combo ");

	    	  String deletecbox = (String)preference.get("ActionDeleteMessage");

	    	 if (deletecbox != null && deletecbox.equals("1"))
	    	     deleteAction = Integer.parseInt(deletecbox);

	    	 String readcbox =(String)preference.get("ActionMarkasRead");
	    	 if 	(readcbox != null && readcbox.equals("1"))
	    	     markread= Integer.parseInt(readcbox);




	    //System.out.println (" Three ");

		String ruleName =  (String)preference.get( "RuleName" );
		String description =  (String)preference.get( "Description" );
		String enabledStatus =  (String)preference.get( "EnabledStatus" );

	    //System.out.println (" Four ");
		String[] join      = (String []) preference.get("Join");
		String[] field     = (String []) preference.get("Field");
		String[] condition = (String []) preference.get("Condition");
		String[] criteria  = (String []) preference.get("Criteria");


		String applyAction =  (String)preference.get( "ApplyAction" );
	    //System.out.println (" Five ");

		ruleStatement="";
		for(int i=0;i < join.length;i++)
		{
			//System.out.println (" I = " + i + "Lenght = " + join.length);
			ruleStatement = ruleStatement + "<" + join[i] + "<" +
			field[i] + "<" + condition[i] + "<" +
			criteria[i];

			//System.out.println("join[i] :::::::::::" + join [i]);
			//System.out.println("field[i] :::::::::::" + field [i]);
			//System.out.println("condition[i] :::::::::::" + condition [i]);
			//System.out.println("criteria[i] :::::::::::" + criteria [i]);

		}

	    //System.out.println (" Six ");
		//System.out.println (" ruleStatement : " + ruleStatement);
		//System.out.println (" moveid  : " + moveid);
		//System.out.println (" moveaction  : " + moveAction);
		//System.out.println (" deleteaction  : " + deleteAction);
		//System.out.println (" markread  : " + markread);

		CVDal cvdl = new CVDal(dataSource);
		cvdl.setSql("email.updaterule");
		cvdl.setString( 1, ruleName);
		cvdl.setString( 2, description);
		cvdl.setString( 3, enabledStatus );
		cvdl.setInt( 4, accountid);
		cvdl.setString( 5,ruleStatement);
		cvdl.setInt( 6,ruleid);
		cvdl.executeUpdate();
	 	cvdl.clearParameters();
		//System.out.println (" Rule Id : " + ruleid);

		cvdl.setSql("email.deleteemailaction");
		cvdl.setInt(1, ruleid);
		cvdl.executeUpdate();
		cvdl.clearParameters();

		if (moveAction == 1)
		{
			//System.out.println (" Entry in move Action ");
			cvdl.setSql("email.insertemailaction");
			cvdl.setInt(1, ruleid);
			cvdl.setString( 2, "MOVE");
			cvdl.setInt (3, moveid);
			cvdl.executeUpdate();
	     	cvdl.clearParameters();
		}
		if (markread  == 1)
		{
			//System.out.println (" Entry in Mark as Read ");
			cvdl.setSql("email.insertemailaction");
			cvdl.setInt(1, ruleid);
			cvdl.setString( 2, "MARK_AS_READ");
			cvdl.setInt (3, moveid);
			cvdl.executeUpdate();
			cvdl.clearParameters();
		}
		if (deleteAction == 1)
		{
			//System.out.println (" Entry in Mark as Read ");
			cvdl.setSql("email.insertemailaction");
			cvdl.setInt(1, ruleid);
	    	cvdl.setString( 2, "DELETE");
			cvdl.setInt (3, 0);
			cvdl.executeUpdate();
	     	cvdl.clearParameters();
		}

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
	* delete Rule in rules related tables
	* @param	HashMap containing rule id of delete rule.
	*/
	public int deleteRule(HashMap preference)
	{
	  int result=0;
	  int ruleId=0;

	  try
	  {

	    //System.out.println (" deleteRule  Method ");

	    String rid = (String)preference.get("RuleID");
	    if (rid != null && !rid.equals(""))
	    {
	    	try
	    	{
	    	 ruleId = Integer.parseInt(rid);
	    	}
	    	catch (NumberFormatException e )
	    	{
	    	}
	    }

		//System.out.println (" Rule Id : " + ruleId);
		CVDal cvdl = new CVDal(dataSource);
		cvdl.setSql("email.deleterule");
		cvdl.setInt ( 1, ruleId);
		cvdl.executeUpdate();
	 	cvdl.clearParameters();

	 	cvdl.setSql("email.deleteemailaction");
	 	cvdl.setInt ( 1, ruleId);
	 	cvdl.executeUpdate();
	 	cvdl.clearParameters();

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
