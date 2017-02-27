/*
 * $RCSfile: HrListsEJB.java,v $    $Revision: 1.3 $  $Date: 2005/09/01 15:31:06 $ - $Author: mcallist $
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


package com.centraview.hr.hrlist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;

import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.common.EJBUtil;
import com.centraview.common.EmployeeList;
import com.centraview.common.ExpensesListElement;
import com.centraview.common.FloatMember;
import com.centraview.common.IndividualListElement;
import com.centraview.common.IntMember;
import com.centraview.common.PureDateMember;
import com.centraview.common.StringMember;
import com.centraview.common.TimeSheetList;
import com.centraview.common.TimeSheetListElement;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListVO;

/**
 * This EJB gets all lists related to hr module.
 */
public class HrListsEJB implements SessionBean
{
	protected javax.ejb.SessionContext ctx;
	protected Context environment;
	private String dataSource = "MySqlDS";

	public void setSessionContext(SessionContext ctx)
	{
		this.ctx = ctx;
	}

	public void ejbActivate() {}
	public void ejbPassivate() {}
	public void ejbRemove() {}
	public void ejbCreate() {}

	public EmployeeList getEmployeeDetailList(int userID, HashMap preference) throws AuthorizationFailedException
	{
    if (!CVUtility.isModuleVisible("EmployeeList",userID, this.dataSource))
    {
      throw new AuthorizationFailedException("EmployeeList - getEmployeeDetailList");
    }
    
    String advSearchstr = "";
    if (preference != null)
    {
      advSearchstr  = (String)preference.get("ADVANCESEARCHSTRING");
    }

		EmployeeList DL = new EmployeeList();

		DL.setSortMember("Name");
		CVDal cvdl = new CVDal(dataSource);

		cvdl.setSqlQueryToNull();
		cvdl.setSqlQuery("DROP TABLE  IF EXISTS individuallist");
		cvdl.executeUpdate();

		cvdl.setSqlQueryToNull();
		cvdl.setSqlQuery("DROP TABLE  IF EXISTS individuallistSearch");
		cvdl.executeUpdate();

		cvdl.setSqlQuery("CREATE TEMPORARY TABLE individuallist "+
			" SELECT i.IndividualID AS individualID, i.List AS dbid, i.Entity AS EntityID, CONCAT(i.FirstName, ' ', i.LastName) AS Name, i.FirstName, i.MiddleInitial, "+
			"i.LastName, i.Title, e.Name AS Entity, a.Street1, a.Street2, a.City, a.State, "+
			"a.Zip, a.Country, moc.Content AS Phone, moc.Content AS Email, moc.Content AS Fax  "+
			"FROM individual i LEFT OUTER JOIN entity e ON (i.Entity=e.EntityID) LEFT OUTER  "+
			"JOIN addressrelate ar ON (i.IndividualID=ar.Contact) LEFT OUTER JOIN address a ON  "+
			"(ar.Address=a.AddressID) LEFT OUTER JOIN methodofcontact moc ON (moc.MOCID=0) , employee "+
			"WHERE i.IndividualID = employee.IndividualID AND ar.contacttype = 2");
		cvdl.executeUpdate();
		cvdl.clearParameters();

		cvdl.setSql("contact.individuallistupdate1");
		cvdl.executeUpdate();
		cvdl.clearParameters();

		cvdl.setSql("contact.individuallistupdate2");
		cvdl.executeUpdate();
		cvdl.clearParameters();

		cvdl.setSql("contact.individuallistupdate3");
		cvdl.executeUpdate();
		cvdl.clearParameters();
/*
		cvdl.setSql("contact.individuallistupdate4");
		cvdl.executeUpdate();
		cvdl.clearParameters();

		cvdl.setSql("contact.individuallistupdate5");
		cvdl.executeUpdate();
		cvdl.clearParameters();
*/



		Collection v = null;

		/* Added for Advance Search	*/
		if (advSearchstr != null && advSearchstr.startsWith("ADVANCE:"))
		{
			advSearchstr = advSearchstr.substring(8);

			String str = "create TEMPORARY TABLE individuallistSearch "+advSearchstr;
			cvdl.setSqlQueryToNull();
			cvdl.setSqlQuery(str);
			cvdl.executeUpdate();
			cvdl.clearParameters();

			// If some field change then change also ContactListEjb
			str = "Select individuallist.individualID ,individuallist.EntityID,concat(individuallist.FirstName ,'  ', individuallist.LastName) Name, individuallist.FirstName, individuallist.MiddleInitial, individuallist.LastName, individuallist.Title, individuallist.Entity, individuallist.Phone ,individuallist.Email, individuallist.Fax,concat(individuallist.Street1,'  ',individuallist.Street2,' ',individuallist.City ,' ', individuallist.State ,' ' , individuallist.Zip , ' ',individuallist.Country ) Address from individuallist ,individuallistSearch where individuallist.individualID =  individuallistSearch.individualID";

			cvdl.setSqlQueryToNull();
			cvdl.setSqlQuery(str);
			v = cvdl.executeQuery();

			cvdl.setSqlQueryToNull();
			cvdl.setSqlQuery("DROP TABLE individuallistSearch");
			cvdl.executeUpdate();

		}
		else
		{
			cvdl.setSql("contact.individuallistselect");
			v = cvdl.executeQuery();
			cvdl.clearParameters();
		}

		cvdl.setSql("contact.individuallistdroptable");
		cvdl.executeUpdate();

		cvdl.clearParameters();
		cvdl.destroy();

		Iterator it = v.iterator();

		while( it.hasNext() )
		{
			HashMap hm = ( HashMap  )it.next();
			//int EntityID = ((Integer)hm.get("EntityID")).intValue();

			String IndividualName = (String)hm.get( "Name" );
			int IndividualID = ((Long)hm.get("individualID")).intValue();
			int entId = ((Long)hm.get("EntityID")).intValue();

			IntMember intmem = new IntMember( "IndividualID"  , IndividualID , 10 , "", 'T' , false , 10 );
			IntMember entityId  = new IntMember( "EntityID", entId ,10 , "" , 'T' , false,10  );
			StringMember one  = new StringMember( "Name", (String) hm.get( "Name" ) ,10 , "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.INDIVIDUAL  + "&rowId=" + IndividualID  , 'T' , true  );

			//Added by Parshruam
			StringMember fname  = new StringMember( "FirstName", (String)hm.get( "FirstName" ) ,10 , "" , 'T' , false   );
			StringMember lName  = new StringMember( "LastName", (String)hm.get( "LastName" ) ,10 , "" , 'T' , false   );
			StringMember mName  = new StringMember( "MiddleInitial", (String)hm.get( "MiddleInitial" ) ,10 , "" , 'T' , false   );

			StringMember two  = new StringMember( "Title", (String)hm.get( "Title" ) ,10 , "" , 'T' , false   );
			StringMember three= new StringMember( "Company",(String)hm.get( "Entity" ) ,10 , "/centraview/ViewHandler.do?" + Constants.TYPEOFCONTACT + "=" + Constants.INDIVIDUAL  + "&rowId=" + IndividualID , 'T' , true  );
			StringMember four = new StringMember( "Phone" , (String)hm.get( "Phone" ) ,10 , " " , 'T' ,false  );
			StringMember five = new StringMember( "Fax" , (String)hm.get( "Fax" ) ,10 , "#" , 'T' , false   );
			StringMember six  = new StringMember( "Email", (String)hm.get( "Email" ) ,10 , "/centraview/jsp/common/MailCompose.jsp" , 'T' , true   );

			IndividualListElement ele = new IndividualListElement( IndividualID );
			ele.put( "IndividualID", intmem );
			ele.put( "EntityID", entityId );
			ele.put( "Name", one );
			ele.put( "Title" ,  two );
		//	ele.put( "Entity" , three );
			ele.put( "Company" , three );
			ele.put( "Phone", four );
			ele.put( "Fax" ,  five );
			ele.put( "Email" , six  );

			ele.put( "FirstName" , fname  );
			ele.put( "LastName" , lName  );
			ele.put( "MiddleInitial" , mName  );

			DL.put( IndividualName+IndividualID , ele );
		}
		// Added by Parshuram
		DL.setTotalNoOfRecords(DL.size());
		DL.setBeginIndex(1);
		DL.setEndIndex(DL.getTotalNoOfRecords());
		DL.setStartAT( 1 );
		DL.setEndAT ( 10 );

		return DL;
	}

	public com.centraview.common.TimeSheetList getTimeSheetList(int individualId, HashMap info) throws AuthorizationFailedException
	{
	    if(!CVUtility.isModuleVisible("Time Sheets",individualId, this.dataSource))
	    	throw new AuthorizationFailedException("Time Sheets - getTimeSheetList");

		TimeSheetList timeSheetList = new TimeSheetList();
		try
		{
			Integer startATparam = (Integer) info.get( "startATparam" ) ;
			Integer EndAtparam = (Integer) info.get( "EndAtparam" ) ;
			String  searchString = (String) info.get( "searchString" ) ;
			String sortmem = (String) info.get( "sortmem" ) ;
			Character chr = (Character) info.get( "sortType" ) ;

			char sorttype =  chr.charValue();
			int startat = startATparam.intValue();
			int endat = EndAtparam.intValue();
			int beginindex  = Math.max( startat - 100, 1  ) ;
			int endindex  = endat + 100;

			timeSheetList.setSortMember( sortmem );

			boolean allRecords = true;

			CVDal cvdl = new CVDal(dataSource);

			if(sortmem.equals("Employee"))
			 sortmem="Name";
			//else if(sortmem.equals("Entity"))
			  //sortmem="EntityName";
			//else if(sortmem.equals("DueDate"))
			  //sortmem="End";

			String appendStr = "";
			Collection col = null;


			cvdl.setSql("hr.createtimesheetlist");
			//please check the query individualid shoud be of user logged in cvdl.set
			cvdl.executeUpdate();
			cvdl.clearParameters();

			cvdl.setSql("hr.inserttimesheetlist");
			cvdl.setInt(1,individualId);
			cvdl.setInt(2,individualId);
			cvdl.setInt(3,individualId);
			cvdl.executeUpdate();
			cvdl.clearParameters();

			cvdl.setSql("hr.updatetimesheetlist1");
			cvdl.executeUpdate();
			cvdl.clearParameters();

			cvdl.setSql("hr.updatetimesheetlist2");
			cvdl.executeUpdate();
			cvdl.clearParameters();

			String strquery = "create temporary table tempduration select timesheetID ID,sum(Hours) Hours from  timeslip group by timesheetid";
			cvdl.setSqlQuery(strquery);
			cvdl.executeUpdate();

			String strquery2 = "update timesheetlist,tempduration set Duration= Hours where timesheetlist.ID = tempduration.ID";
			cvdl.setSqlQuery(strquery2);
			cvdl.executeUpdate();

			String strquery3 = "DROP TABLE tempduration";
			cvdl.setSqlQuery(strquery3);
			cvdl.executeUpdate();


			if (searchString != null && searchString.startsWith("ADVANCE:"))
			{
				searchString = searchString.substring(8);

				String str = "create TEMPORARY TABLE timesheetsearch "+searchString;
				cvdl.setSqlQueryToNull();
				cvdl.setSqlQuery(str);
				cvdl.executeUpdate();
				cvdl.clearParameters();
				str = "select timesheetlist.ID, EmpIndvidualID, Name, StartDate, EndDate, Duration, CreatedBy, Creator FROM timesheetlist, timesheetsearch WHERE timesheetlist.ID = timesheetsearch.TimeSheetID";

				cvdl.setSqlQueryToNull();
				cvdl.setSqlQuery(str);
				col = cvdl.executeQuery();

				cvdl.setSqlQueryToNull();
				cvdl.setSqlQuery("DROP TABLE timesheetsearch");
				cvdl.executeUpdate();

				allRecords = false;

			}
			else
			{
				if (searchString.startsWith("SIMPLE :"))
				{
					searchString = searchString.substring(8);
					appendStr = " WHERE (ID like '%"+searchString+"%' "
					+ "OR Name like '%"+searchString+"%' "
					+ "OR StartDate like  '%"+searchString+"%' "
					+ "OR EndDate like  '%"+searchString+"%' "
					+ "OR Duration like  '%"+searchString+"%' "
					+ "OR CreatedBy like  '%"+searchString+"%') ";

					allRecords = false;
				}


				String str =  "SELECT * FROM timesheetlist";

				if ( sorttype == 'A' )
				{
					str = str + appendStr + " order by "+ sortmem + " asc limit "+(beginindex-1) +" , "+ (endindex+1) +";" ;
					cvdl.setSqlQuery( str );
				}else
				{
					str = str + appendStr +" order by "+ sortmem + " desc limit "+(beginindex-1) +" , "+ (endindex+1) +";";
					cvdl.setSqlQuery( str );
				}

				col = cvdl.executeQuery();

			}

			Iterator it = col.iterator();
			int i=0 ;
			while( it.hasNext() )
			{
				i++;
				HashMap hm = ( HashMap  )it.next();

				int TimeSheetId 	= ((Long)hm.get("ID")).intValue();
				String EmployeeName = (String)hm.get("Name");
				java.util.Date StartDate		= (java.util.Date)hm.get("StratDate");
				java.util.Date EndDate		= (java.util.Date)hm.get("EndDate");
				float Duration		= Float.parseFloat(	hm.get("Duration").toString() );
				String CreatedBy	= (String)hm.get("CreatedBy");

				IntMember intmem 	= new IntMember( "ID"  , TimeSheetId , 10 , "", 'T' , true , 10 );
				StringMember sname  = new StringMember( "Employee", EmployeeName ,10 , "" , 'T' , true );


				PureDateMember startdate= new PureDateMember( "StartDate" ,(java.util.Date)hm.get("StartDate"),10 , "URL" , 'T' , false ,100 ,"EST");
				PureDateMember enddate	= new PureDateMember( "EndDate" ,(java.util.Date)hm.get("EndDate"),10 , "URL" , 'T' , false ,100 ,"EST");

				StringMember cname  = new StringMember( "CreatedBy", CreatedBy ,10 , "" , 'T' , true );

				TimeSheetListElement ele = new TimeSheetListElement(TimeSheetId );
				ele.put( "ID", intmem );
				ele.put( "EmployeeID", new IntMember( "EmployeeID"  , ((Long)hm.get("EmpIndvidualID")).intValue() , 10 , "", 'T' , false , 10 ));
				ele.put( "Employee", sname );
				ele.put( "StartDate", startdate );
				ele.put( "EndDate", enddate );

				//FloatMember duration= new FloatMember( "Duration", new Float(Duration) ,10 , ""  , 'T' , false , 10 );
				StringMember duration = null;
				if ( Duration > 1.0 )
				{
					duration = new StringMember( "Duration" , Float.toString(Duration)+ " hours"  , 10 , "URL", 'T' , false );
				}
				else if ( Duration == 1.0 )
				{
					duration = new StringMember( "Duration" , Float.toString(Duration)+ " hour"  , 10 , "URL", 'T' , false );
				}
				else if ( Duration > 0.0 && Duration < 1.0 )
				{
				  	duration = new StringMember( "Duration" , Float.toString(Duration*60)+ " minutes"  , 10 , "URL", 'T' , false );
				}
				else
				{
				   	duration = new StringMember( "Duration" , ""  , 10 , "URL", 'T' , false );
				}
				ele.put( "Duration", duration );
				ele.put( "Creator", new IntMember( "Creator"  , ((Long)hm.get("Creator")).intValue() , 10 , "", 'T' , false , 10 ));
				ele.put( "CreatedBy", cname );


				StringBuffer sb = new StringBuffer("00000000000");
				sb.setLength(11);
				String str = (new Integer(i)).toString();
				sb.replace((sb.length()-str.length()),(sb.length()),str);
				String newOrd = sb.toString();

				cvdl.clearParameters();

				timeSheetList.put(newOrd , ele );


			}

			if (!allRecords)
			{
				timeSheetList.setTotalNoOfRecords( timeSheetList.size() );
			}
			else
			{
				int count = 0;
				cvdl.setSql("hr.selecttimesheetcount");
				Collection col2 = cvdl.executeQuery();
				Iterator ite2 = col2.iterator();
				if (ite2.hasNext())
				{
					HashMap hm2 = (HashMap) ite2.next();
					count = ((Integer)hm2.get("count(TimeSheetID)")).intValue();

				}
				timeSheetList.setTotalNoOfRecords( count );
			}

			timeSheetList.setListType( "TimeSheet" );
			timeSheetList.setBeginIndex( beginindex );
			timeSheetList.setEndIndex( endindex ) ;

			cvdl.clearParameters();

			//cvdl.setSql("hr.deletetimesheetlist");
			cvdl.setSqlQuery("DROP TABLE timesheetlist");
			cvdl.executeUpdate();

			cvdl.destroy();


		}
		catch(Exception e)
		{
			System.out.println("[Exception][HrListsEJB.getTimeSheetList] Exception Thrown: "+e);
			e.printStackTrace();
		}
		return timeSheetList;
	}

	public com.centraview.common.ExpenseFormList getExpenseFormList(int individualId, HashMap info) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("ExpenseForms",individualId, this.dataSource))
			throw new AuthorizationFailedException("EmployeeList - getExpenseFormList");

		com.centraview.common.ExpenseFormList expenseformList = new com.centraview.common.ExpenseFormList();
		try
		{
			Integer startATparam = (Integer) info.get( "startATparam" ) ;
			Integer EndAtparam = (Integer) info.get( "EndAtparam" ) ;
			String  searchString = (String) info.get( "searchString" ) ;
			String sortmem = (String) info.get( "sortmem" ) ;
			Character chr = (Character) info.get( "sortType" ) ;

			char sorttype =  chr.charValue();
			int startat = startATparam.intValue();
			int endat = EndAtparam.intValue();
			int beginindex  = Math.max( startat - 100, 1  ) ;
			int endindex  = endat + 100;

			expenseformList.setSortMember( sortmem );

			boolean allRecords = true;

			CVDal cvdl = new CVDal(dataSource);

			//if(sortmem.equals("Name"))
			  //sortmem="projecttitle";
			//else if(sortmem.equals("Entity"))
			  //sortmem="EntityName";
			//else if(sortmem.equals("DueDate"))
			  //sortmem="End";

			String appendStr = "";
			Collection col = null;

			//cvdl.setSql("hr.deleteexpenseformlist");
			//cvdl.executeUpdate();

			cvdl.setSql("hr.createexpenseformlist");
			cvdl.executeUpdate();

			cvdl.setSql("hr.insertexpenseformlist");
			cvdl.setInt(1,individualId);
			cvdl.setInt(2,individualId);
			cvdl.setInt(3,individualId);
			cvdl.executeUpdate();

			cvdl.setSql("hr.updateexpenseformlist1");
			cvdl.executeUpdate();

			cvdl.setSql("hr.updateexpenseformlist2");
			cvdl.executeUpdate();

			cvdl.setSql("hr.createexpenseamount");
			cvdl.executeUpdate();
 
			cvdl.setSql("hr.updateexpenseformlist3");
			cvdl.executeUpdate();

			cvdl.setSql("hr.deleteexpenseamount");
			cvdl.executeUpdate();

			String strUpdate = "select distinct exf.Status as Status, ex.ExpenseFormId as ExpenseFormId from expense ex, expenseform exf where ex.LineStatus not in ('Deleted') and ex.Expenseformid=exf.Expenseformid and ex.Expenseformid <> 0 order by ex.ExpenseFormId" ;
			cvdl.setSqlQuery(strUpdate);

			HashMap statusMap = new HashMap();
			Collection  colstatus = cvdl.executeQuery();
			Iterator itstatus = colstatus.iterator();

			while( itstatus.hasNext() )
			{

				HashMap hm = ( HashMap  )itstatus.next();

				String status		= (String)(hm.get("Status"));
				// Casting ExpenseFormId to Number as it seems architecture dependent if
				// JDBC returns Long or Int
				int expenseidIn 	= ((Number)hm.get("ExpenseFormId")).intValue();
				cvdl.setSql("hr.updateexpenseformlist4");
				cvdl.setString(1, status);
				cvdl.setInt(2, expenseidIn);
				cvdl.executeUpdate();

			}

			if (searchString != null && searchString.startsWith("ADVANCE:"))
			{
				searchString = searchString.substring(8);

				String str = "create TEMPORARY TABLE expenseformsearch "+searchString;
				//cvdl.setlQueryToNull();
				cvdl.setSqlQuery(str);
				cvdl.executeUpdate();
				cvdl.clearParameters();

				str = "SELECT expenseFormList.ID, EmpIndvidualID, Employee, StartDate, EndDate, Amount, CreatedBy,Status,Creator FROM expenseFormList,expenseformsearch WHERE expenseFormList.ID = expenseformsearch.ExpenseFormId";
				cvdl.setSqlQueryToNull();
				cvdl.setSqlQuery(str);
				col = cvdl.executeQuery();

				cvdl.setSqlQueryToNull();
				cvdl.setSqlQuery("DROP TABLE expenseformsearch");
				cvdl.executeUpdate();

				allRecords = false;

			}
			else
			{
				if (searchString.startsWith("SIMPLE :"))
				{
					searchString = searchString.substring(8);
					appendStr = " WHERE (ID like '%"+searchString+"%' "
					+ "OR Employee like '%"+searchString+"%' "
					+ "OR StartDate like  '%"+searchString+"%' "
					+ "OR EndDate like  '%"+searchString+"%' "
					+ "OR Amount like  '%"+searchString+"%' "
					+ "OR Status like  '%"+searchString+"%' "
					+ "OR CreatedBy like  '%"+searchString+"%' ) ";

					//+ "OR Status like  '%"+searchString+"%' "


					allRecords = false;
				}

				String str =  "SELECT * FROM expenseFormList";

				if ( sorttype == 'A' )
				{
					str = str + appendStr + " order by "+ sortmem + " asc limit "+(beginindex-1) +" , "+ (endindex+1) +";" ;
					cvdl.setSqlQuery( str );
				}else
				{
					str = str + appendStr +" order by "+ sortmem + " desc limit "+(beginindex-1) +" , "+ (endindex+1) +";";
					cvdl.setSqlQuery( str );
				}
				col = cvdl.executeQuery();
			}

			Iterator it = col.iterator();
			int i=0 ;
			while( it.hasNext() )
			{
				i++;
				HashMap hm = ( HashMap  )it.next();
				Long lid = (Long)hm.get("ID");
				int ExpenseFormID = lid.intValue();

				IntMember intmem = new IntMember( "ID" , ExpenseFormID , 10 , "URL", 'T' , true , 10 );

				StringMember employee = null, createdBy = null , status = null;
				PureDateMember startDate = null, endDate = null;
				IntMember expenseFormId = null;
				FloatMember amount = null;

				IntMember expenseFormID = new IntMember( "ExpenseFormId" , ExpenseFormID , 10 , "URL", 'T' , false , 10 );

				if( hm.get( "Employee" ) !=null)
					employee  = new StringMember( "Employee", (String) hm.get( "Employee" ) ,10 , "", 'T' , true  );
				else
					employee  = new StringMember( "Employee", "" ,10 , "", 'T' , true  );

				if( hm.get( "CreatedBy" ) !=null)
					createdBy  = new StringMember( "CreatedBy", (String) hm.get( "CreatedBy" ) ,10 , "", 'T' , true  );
				else
					createdBy  = new StringMember( "CreatedBy", "" ,10 , "", 'T' , true  );

				if(hm.get( "Status" ) !=null)
					status  = new StringMember( "Status" , (String)hm.get( "Status" ), 10 , "URL" , 'T' , false);
				else
					status  = new StringMember( "Status" , "", 10 , "URL" , 'T' , false);


				if (hm.get( "StartDate") != null)
				{
					 java.util.Date StartDate = (java.util.Date)hm.get("StartDate");
					 startDate  = new PureDateMember( "StartDate" ,StartDate,10 , "URL" , 'T' , false ,100 ,"EST");

				}

				if (hm.get( "EndDate") != null)
				{
					java.util.Date EndDate = (java.util.Date)hm.get("EndDate");
					endDate  = new PureDateMember( "EndDate" ,EndDate,10 , "URL" , 'T' , false ,100 ,"EST");
				}

				float Amount = Float.parseFloat(	hm.get("Amount").toString() );
				amount = new FloatMember("Amount", new Float(Amount),10 , ""  , 'T' , false , 10 );



				ExpensesListElement ele = new ExpensesListElement(ExpenseFormID);
				ele.put("ID", intmem );
				ele.put("EmployeeID", new IntMember( "EmployeeID"  , ((Long)hm.get("EmpIndvidualID")).intValue() , 10 , "", 'T' , false , 10 ));
				ele.put("Employee", employee );
				ele.put("StartDate" ,  startDate );
				ele.put("EndDate" , endDate );
				ele.put("Amount" , amount );
				ele.put("Status", status);
				ele.put("Creator", new IntMember( "Creator"  , ((Long)hm.get("Creator")).intValue() , 10 , "", 'T' , false , 10 ));
				ele.put("CreatedBy", createdBy);


				StringBuffer sb = new StringBuffer("00000000000");
				sb.setLength(11);
				String str1 = (new Integer(i)).toString();
				sb.replace((sb.length()-str1.length()),(sb.length()),str1);
				String newOrd = sb.toString();

				cvdl.clearParameters();

				expenseformList.put(newOrd , ele );

			}

			if (!allRecords)
			{
				expenseformList.setTotalNoOfRecords( expenseformList.size() );
			}
			else
			{
				int count = 0;
				String str="SELECT count(ExpenseFormID) FROM expenseform";
				cvdl.setSqlQuery(str);
				Collection col2 = cvdl.executeQuery();
				Iterator ite2 = col2.iterator();
				if (ite2.hasNext())
				{
					HashMap hm2 = (HashMap) ite2.next();
					count = ((Integer)hm2.get("count(ExpenseFormID)")).intValue();

				}
				expenseformList.setTotalNoOfRecords( count );
			}
			expenseformList.setListType( "Expenses" );
			expenseformList.setBeginIndex( beginindex );
			expenseformList.setEndIndex( endindex ) ;

			cvdl.setSql("hr.deleteexpenseformlist");
		 	cvdl.executeUpdate();
			cvdl.clearParameters();
			cvdl.destroy();


		}
		catch(Exception e)
		{
			System.out.println("[Exception][HrListsEJB.getExpenseFormList] Exception Thrown: "+e);
			e.printStackTrace();
		}
	return expenseformList;
	}
	/**
	 * @author Kevin McAllister <kevin@centraview.com>
	 * This simply sets the target datasource to be used for DB interaction
	 * @param ds A string that contains the cannonical JNDI name of the datasource
	 */
	public void setDataSource(String ds) {
	  this.dataSource = ds;
	}
	
	/**
	 * Returns a ValueListVO representing a list of Expense Form records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getExpenseFormValueList(int individualID, ValueListParameters parameters) {
	  ArrayList list = new ArrayList();
	  
	  boolean permissionSwitch = individualID < 1 ? false : true;
	  boolean applyFilter = false;
	  String filter = parameters.getFilter();
	  
	  CVDal cvdl = new CVDal(this.dataSource);
	  if (filter != null && filter.length() > 0) {
	    String str = "CREATE TABLE expenseformlistfilter " + filter;
	    cvdl.setSqlQuery(str);
	    cvdl.executeUpdate();
	    cvdl.setSqlQueryToNull();
	    applyFilter = true;
	  }
	  int numberOfRecords = 0;
	  if (applyFilter) {
	    numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "expenseformlistfilter", individualID, 51, "expenseform", "ExpenseFormID", "Owner", null, permissionSwitch);
	  } else if (permissionSwitch) {
	    numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 51, "expenseform", "ExpenseFormID", "Owner", null, permissionSwitch);
	  }
	  parameters.setTotalRecords(numberOfRecords);
	  
	  String query = this.buildExpenseFormListQuery(applyFilter, individualID, cvdl, parameters);
	  cvdl.setSqlQuery(query);
	  list = cvdl.executeQueryList(1);
	  cvdl.setSqlQueryToNull();
	  
	  cvdl.setSqlQuery("DROP TABLE expenseformlist");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  if (applyFilter) {
	    cvdl.setSqlQuery("DROP TABLE expenseformlistfilter");
	    cvdl.executeUpdate();
	    cvdl.setSqlQueryToNull();
	  }
	  if (applyFilter || permissionSwitch) {
	    cvdl.setSqlQuery("DROP TABLE listfilter");
	    cvdl.executeUpdate();
	    cvdl.setSqlQueryToNull();
	  }
	  
	  cvdl.destroy();
	  cvdl = null;
	  return new ValueListVO(list, parameters);
	}
	
	private String buildExpenseFormListQuery(boolean applyFilter, int individualId, CVDal cvdl, ValueListParameters parameters) 
	{
	  // Create table column definitions
	  String create = 
	    "CREATE TEMPORARY TABLE expenseformlist ";
	  String select = 
	    "SELECT ef.ExpenseFormID, emp.IndividualID AS EmployeeID, CONCAT(i.FirstName, ' ', i.LastName) AS" +
	    " EmployeeName, ef.FromDate, ef.ToDate, SUM(Amount) AS Amount, exp.Status, CONCAT(i.FirstName, ' ', " +
	    "i.LastName) AS CreatedBy, ef.Creator ";
	  String from = 
	    "FROM expenseform ef, expense exp,individual i, employee emp ";
	  String where = "WHERE 1 = 0 GROUP BY exp.ExpenseFormID";
	  String query = create + select + from + where;
	  cvdl.setSqlQuery(query);
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  // Populate
	  String insert = 
	    "INSERT INTO expenseformlist (ExpenseFormID, EmployeeID, FromDate, ToDate, Amount, Status, Creator) ";
	  select = "SELECT ExpenseFormID, Owner, FromDate, ToDate, 0, Status, Creator ";
	  from = "FROM expenseform ";
	  where = 
	    "WHERE (Creator = " + individualId + " OR Owner = " + individualId + " OR ReportingTo = " + 
	    individualId + ") AND lineStatus <> 'Deleted'";
	  if (applyFilter)
	  {
	    from += ", listfilter lf ";
	    where += " AND lf.ExpenseFormID = ef.ExpenseFormID ";
	  }
	  query = insert + select + from + where;
	  cvdl.setSqlQuery(query);
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  // Various updates
	  cvdl.setSqlQuery("UPDATE expenseformlist efl, individual i SET efl.EmployeeName = CONCAT(FirstName, ' ', " +
	  "LastName) WHERE EmployeeID = i.IndividualID");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  cvdl.setSqlQuery("UPDATE expenseformlist efl, individual i SET efl.CreatedBy = CONCAT(FirstName, ' ', " +
	  "LastName) WHERE efl.Creator = i.IndividualID");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  // Create expenseamount temp table, update and delete
	  cvdl.setSqlQuery("CREATE TEMPORARY TABLE expenseformamount SELECT ExpenseFormId, SUM(Amount) AS Total FROM " +
	  "expense WHERE lineStatus NOT IN ('Deleted') GROUP BY ExpenseFormId");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  cvdl.setSqlQuery("UPDATE expenseformlist efl, expenseformamount efa SET efl.Amount = efa.Total WHERE " +
	  "efl.ExpenseFormID = efa.ExpenseFormID");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  cvdl.setSqlQuery("DROP TABLE expenseformamount");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  String str = "SELECT DISTINCT ef.Status, exp.ExpenseFormId FROM expense exp, expenseform ef WHERE " +
	  "exp.lineStatus NOT IN ('Deleted') AND exp.ExpenseFormId = ef.ExpenseFormID AND exp.ExpenseFormID <> " +
	  "0 ORDER BY exp.ExpenseFormId";
	  cvdl.setSqlQuery(str);
	  HashMap statusMap = new HashMap();
	  Iterator iter = cvdl.executeQuery().iterator();
	  while(iter.hasNext()) {
	    HashMap hm = (HashMap )iter.next();
	    String status		= (String)(hm.get("Status"));
	    // Casting ExpenseFormId to Number as it seems architecture dependent if
	    // JDBC returns Long or Int
	    int id = ((Number)hm.get("ExpenseFormId")).intValue();
	    cvdl.setSqlQuery("UPDATE expenseformlist SET Status = '" + status + "' WHERE ExpenseFormID = " + id);
	    cvdl.executeUpdate();
	    cvdl.setSqlQueryToNull();
	  }
	  
	  // Order and limit
	  String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + 
	      " " + parameters.getSortDirection());
	  String limit = parameters.getLimitParam();
	  
	  // Finally, the query.
	  query = 
	    "SELECT ExpenseFormID, EmployeeID, EmployeeName, FromDate, ToDate, Amount, Status, CreatedBy, " +
	    "Creator FROM expenseformlist " + orderBy + limit;
	  return(query);
	}
	
	/**
	 * Returns a ValueListVO representing a list of Time Sheet records, based on
	 * the <code>parameters</code> argument which limits results.
	 */
	public ValueListVO getTimeSheetValueList(int individualID, ValueListParameters parameters) {
	  ArrayList list = new ArrayList();
	  
	  boolean permissionSwitch = individualID < 1 ? false : true;
	  boolean applyFilter = false;
	  String filter = parameters.getFilter();
	  
	  CVDal cvdl = new CVDal(this.dataSource);
	  if (filter != null && filter.length() > 0) {
	    String str = "CREATE TABLE timesheetlistfilter " + filter;
	    cvdl.setSqlQuery(str);
	    cvdl.executeUpdate();
	    cvdl.setSqlQueryToNull();
	    applyFilter = true;
	  }
	  int numberOfRecords = 0;
	  if (applyFilter) {
	    numberOfRecords = EJBUtil.buildListFilterTable(cvdl, "timesheetlistfilter", individualID, 52, "timesheet", "TimeSheetID", "Owner", null, permissionSwitch);
	  } else if (permissionSwitch) {
	    numberOfRecords = EJBUtil.buildListFilterTable(cvdl, null, individualID, 52, "timesheet", "TimeSheetID", "Owner", null, permissionSwitch);
	  }
	  parameters.setTotalRecords(numberOfRecords);
	  
	  String query = this.buildTimeSheetListQuery(applyFilter, individualID, cvdl, parameters);
	  cvdl.setSqlQuery(query);
	  list = cvdl.executeQueryList(1);
	  cvdl.setSqlQueryToNull();
	  
	  cvdl.setSqlQuery("DROP TABLE timesheetlist");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  if (applyFilter) {
	    cvdl.setSqlQuery("DROP TABLE timesheetlistfilter");
	    cvdl.executeUpdate();
	    cvdl.setSqlQueryToNull();
	  }
	  if (applyFilter || permissionSwitch) {
	    cvdl.setSqlQuery("DROP TABLE listfilter");
	    cvdl.executeUpdate();
	    cvdl.setSqlQueryToNull();
	  }
	  
	  cvdl.destroy();
	  cvdl = null;
	  return new ValueListVO(list, parameters);
	}
	
	private String buildTimeSheetListQuery(boolean applyFilter, int individualId, CVDal cvdl, ValueListParameters parameters)
	{
	  // Create table column definitions
	  String create = 
	    "CREATE TEMPORARY TABLE timesheetlist ";
	  String select = 
	    "SELECT ts.TimeSheetID, emp.IndividualID EmployeeID, CONCAT(FirstName, ' ', LastName) EmployeeName, " +
	    "ts.Start, ts.End, SUM(Hours) Duration, CONCAT(FirstName, ' ', LastName) CreatedBy, ts.Creator ";
	  String from = "FROM individual i, employee emp, timesheet ts, timeslip tsp ";
	  String where = 
	    "WHERE emp.IndividualID = i.IndividualID AND ts.TimeSheetID = tsp.TimeSheetID AND emp.IndividualID = " + 
	    individualId + " AND 1 = 0 GROUP BY ts.TimeSheetID";
	  String query = create + select + from + where;
	  cvdl.setSqlQuery(query);
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  // Populate
	  String insert = 
	    "INSERT INTO timesheetlist (TimeSheetID, EmployeeID, Start, End, Creator) ";
	  select = "SELECT TimeSheetID, Owner, Start, End, Creator ";
	  from = "FROM timesheet ";
	  where = 
	    "WHERE (Creator = " + individualId + " OR Owner = " + individualId + " OR ReportingTo = " + 
	    individualId + ") ";
	  if (applyFilter)
	  {
	    from += ", listfilter lf ";
	    where += "AND lf.TimeSheetID = ef.TimeSheetID ";
	  }
	  query = insert + select + from + where;
	  cvdl.setSqlQuery(query);
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  // Various updates
	  cvdl.setSqlQuery("UPDATE timesheetlist tsl, individual i SET  tsl.EmployeeName = CONCAT(i.FirstName, ' ', " +
	  		"i.LastName) WHERE tsl.EmployeeID = i.IndividualID");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  cvdl.setSqlQuery("UPDATE timesheetlist tsl,individual i SET tsl.CreatedBy = CONCAT(i.FirstName, ' ', " +
	  		"i.LastName) WHERE tsl.Creator = i.IndividualID");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  // Create timesheetduration temp table, update and delete
	  cvdl.setSqlQuery("CREATE TEMPORARY TABLE timesheetduration SELECT TimeSheetID, SUM(Hours) Duration FROM " +
	  		"timeslip GROUP BY TimeSheetID");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  cvdl.setSqlQuery("UPDATE timesheetlist tsl, timesheetduration tsd SET tsl.Duration = tsd.Duration WHERE " +
	  		"tsl.TimeSheetID = tsd.TimeSheetID");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  cvdl.setSqlQuery("DROP TABLE timesheetduration");
	  cvdl.executeUpdate();
	  cvdl.setSqlQueryToNull();
	  
	  // Order and limit
	  String orderBy = "ORDER BY " + String.valueOf(parameters.getSortColumn() + 
	      " " + parameters.getSortDirection());
	  String limit = parameters.getLimitParam();
	  
	  // Finally, the query.
	  query = 
	    "SELECT TimeSheetID, EmployeeID, EmployeeName, Start, End, Duration, CreatedBy, Creator FROM " +
	    "timesheetlist " + orderBy + limit;
	  return(query);
	}
}
