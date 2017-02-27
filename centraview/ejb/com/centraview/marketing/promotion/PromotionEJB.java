/*
 * $RCSfile: PromotionEJB.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:42 $ - $Author: mking_cv $
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

/*
----------------------------------------------------------------------------
Date  : 19082003
Author:  J Nikam
LastUpdated Date :19082003
-----------------------------------------------------------------------------
*/
package com.centraview.marketing.promotion;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.ListElementMember;
import com.centraview.common.StringMember;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.customfield.CustomFieldLocal;
import com.centraview.customfield.CustomFieldLocalHome;
import com.centraview.marketing.ItemElement;
import com.centraview.marketing.ItemLines;


/**
This class is a Statefull session Bean
which acts as a Interface for Marketing Module
*/

public class PromotionEJB implements SessionBean
{


	protected javax.ejb.SessionContext ctx;
	protected Context environment;
	private String dataSource = "MySqlDS";

	/**
	This method sets the context for this Bean
	*/
	public void setSessionContext(SessionContext ctx) throws RemoteException
	{
		this.ctx = ctx;
	}

	/**
	These are life cycle methods from EJB
	*/
	public void ejbActivate()   { }
	public void ejbPassivate()   { }
	public void ejbRemove()   { }
	public void ejbCreate()  { }


	/**
	This method will returns PromotionVO object filled with data as
	per requested Promotion ID
	*/

	public PromotionVO getPromotion(int userId , HashMap data ) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("Promotion",userId, this.dataSource))
			throw new AuthorizationFailedException("Promotion - getPromotion");

		PromotionVO promotionvo = new PromotionVO();
		String id = ( String )data.get( "PromotionID" );
	//	System.out.println("id"+id);
		int PromotionID = Integer.parseInt( id );

		if(!CVUtility.canPerformRecordOperation(userId,"Promotion",PromotionID,ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
			throw new AuthorizationFailedException("Promotion - deletePromotion");

		CVDal cvdl = new CVDal(dataSource);
		cvdl.clearParameters();
		cvdl.setSql( "promotion.getpromotion1" );
		//cvdl.setSqlQuery( "select * from Promotion where PromotionID = ?" );
		cvdl.setInt( 1 ,PromotionID );
		Collection v = cvdl.executeQuery();
		//cvdl.clearParameters();

		Iterator it = v.iterator();

		HashMap hm = ( HashMap  )it.next();
		PromotionID =  ((Long)hm.get( "PromotionID" )).intValue() ;

		if ( PromotionID != 0 )
		{
			promotionvo.setPromotionid( PromotionID );
		}

		if ( (String)hm.get( "Title" ) != null )
		{
			promotionvo.setName( (String)hm.get( "Title" ) ) ;
		}

		if ( (String)hm.get( "Description" ) != null )
		{
			promotionvo.setDescription( (String)hm.get( "Description" ) ) ;
		}

		if ( (String)hm.get( "Status" ) != null )
		{
			promotionvo.setStatus( (String)hm.get( "Status" ) ) ;
		}

		if ( (Timestamp)hm.get( "Startdate" ) != null )
		{
			promotionvo.setStartdate( (Timestamp)hm.get( "Startdate" ) ) ;
		}

		if ( (Timestamp)hm.get( "Enddate" ) != null )
		{
			promotionvo.setEnddate( (Timestamp)hm.get( "Enddate" ) ) ;
		}

		if ( (String)hm.get( "Notes" ) != null )
		{
			promotionvo.setNotes( (String)hm.get( "Notes" ) ) ;
		}
		//Vector customfield = getCustomfields();
		//promotionvo.setCustomfield( customfield )

		HashMap info = new HashMap();

		info.put( "startATparam" , new Integer( 1 ) ) ;
		info.put( "EndAtparam" , new Integer( 10 ) ) ;
		info.put( "searchString" , "" ) ;
		info.put( "sortmem" , "PromotionID" ) ;
		info.put( "sortType" , new Character('A') ) ;

		ItemLines itemlines = getItemLines( PromotionID , info  );

		promotionvo.setItemlines( itemlines );


		cvdl.destroy();
		return promotionvo;

	}



	/**
	This method adds promotion to database
	*/
	public ItemLines getItemLines( int promotionID , HashMap info )
	{
/*
		if(!CVUtility.isModuleVisible("Promotion",promotionID, this.dataSource))
			throw new AuthorizationFailedException("Promotion - getItemLines");
*/
		CVDal cvdl = new CVDal(dataSource);
		cvdl.clearParameters();
		ItemLines  itemlineslist = new  ItemLines();


		try
	  	{
			Integer startATparam = (Integer) info.get( "startATparam" ) ;
			Integer EndAtparam =  (Integer) info.get( "EndAtparam" ) ;
			String  searchString =  (String) info.get( "searchString" ) ;
			String sortmem =  (String) info.get( "sortmem" ) ;
			Character chr = ( Character ) info.get( "sortType" ) ;

			char sorttype =  chr.charValue();
			int startat = startATparam.intValue();
			int endat = EndAtparam.intValue();
	        int beginindex  = Math.max( startat- 100 , 1  ) ;
	        int endindex  = endat + 100   ;

			itemlineslist.setSortMember( sortmem );



			String strSQL = "";
			String appendStr = "";
			if ( sorttype == 'A' )
	     	{
	     		strSQL = "select * from `promoitem` pi , `item` i where PromotionID ="+promotionID + " and pi.itemid = i.itemid order by "+ sortmem + " limit "+(beginindex-1) +" , "+ (endindex+1) +";" ;
	     	}else
	     	{
	     		strSQL = "select * from `promoitem` pi , `item` i where PromotionID ="+promotionID +" and pi.itemid = i.itemid order by "+ sortmem + " desc limit "+(beginindex-1) +" , "+ (endindex+1) +";" ;
	     	}

			cvdl.setSqlQuery( strSQL );
			Collection col = cvdl.executeQuery();

	  		Iterator it = col.iterator();
			int i=0 ;
			int count=0 ;

			while( it.hasNext() )
	  		{
						i++;
						count++;

						HashMap hm = ( HashMap  )it.next();
						promotionID = ((Long)hm.get("PromotionID")).intValue();
						int ItemID = ((Long)hm.get("ItemID")).intValue();
						int Quantity = ((Long)hm.get("Quantity")).intValue();
						float price = ((Number)hm.get("Price")).floatValue();
						float lprice = 0.0f;
						if (hm.get("listprice") != null ){
							lprice = ((Number )hm.get("listprice")).floatValue();
						}
						float costprice = 0.0f;
						if (hm.get("cost") != null ){
							costprice = ((Number )hm.get("cost")).floatValue();
						}

						IntMember lineid  = new IntMember( "LineId"  , count , 10 , "URL", 'T' , false , 10 );
						StringMember desc  = new StringMember( "Description", (String) hm.get( "description" ) ,10 , "", 'T' , true  );
						StringMember sku  = new StringMember( "SKU", (String) hm.get( "sku" ) ,10 , "", 'T' , true  );
						IntMember one  = new IntMember( "PromotionID"  , promotionID , 10 , "URL", 'T' , false , 10 );
						IntMember two  = new IntMember( "ItemID"  , ItemID , 10 , "URL", 'T' , false , 10 );
						IntMember three  = new IntMember( "Quantity"  , Quantity , 10 , "URL", 'T' , false , 10 );
						StringMember Rule  = new StringMember( "Rule", (String) hm.get( "Rule" ) ,10 , "", 'T' , true  );
						FloatMember four = new  FloatMember (  "Price" , new Float( price ) , 10 , "", 'T' , true , 10 );

						FloatMember listprice = new  FloatMember (  "ListPrice" , new Float( lprice ) , 10 , "", 'T' , true , 10 );
						FloatMember cost = new  FloatMember (  "Cost" , new Float( costprice ) , 10 , "", 'T' , true , 10 );

						ItemElement ele = new ItemElement( promotionID );
						ele.put("LineId", lineid );
						ele.put("PromotionID", one );
						ele.put("ItemId", two );
						ele.put("Value" ,  three );// Quantity
						ele.put("Type" , Rule );//Rule
						ele.put("DiscountedPrice" , four );//Price
						ele.put("Description" , desc );//Desc
						ele.put("SKU" , sku );//SKU

						ele.put("ListPrice" , listprice );//ListPrice
						ele.put("Cost" , cost );//Cost

						StringBuffer sb = new StringBuffer("00000000000");
						sb.setLength(11);
						String str = (new Integer(i)).toString();
						sb.replace((sb.length()-str.length()),(sb.length()),str);
						String newOrd = sb.toString();
						itemlineslist.put( newOrd , ele );
						itemlineslist.setListType( "PromotionItem" );
						cvdl.clearParameters();
						cvdl.destroy();
		  	}

	  	}
	  	catch( Exception e )
	  	{
		  	System.out.println("[Exception][PromotionEJB.getItemLines] Exception Thrown: "+e);
        e.printStackTrace();
	  	}
		  	return itemlineslist;
	}


	/**
	This method adds promotion to database
	*/

	public String addPromotion ( int userId , HashMap data ) throws AuthorizationFailedException
	{
		if(!CVUtility.isModuleVisible("Promotion",userId, this.dataSource))
			throw new AuthorizationFailedException("Promotion - addPromotion");

		PromotionVO promotionvo =( PromotionVO ) data.get( "PromotionVo" );
	//	Vector v = promotionvo.getCustomfield();
		String description = promotionvo.getDescription();
		Timestamp enddate= promotionvo.getEnddate();
		String name = promotionvo.getName();
		String notes = promotionvo.getNotes();
		Timestamp startdate = promotionvo.getStartdate();
		String status = promotionvo.getStatus();

		CVDal dl = new CVDal(dataSource);
        int promotionid = 0;
        try {
        dl.clearParameters();
		dl.setSql( "promotion.addpromotion1" );

		//Title
		dl.setString( 1 , name );
		//description
		dl.setString( 2 , description );
		//Status
		dl.setString( 3 , status );
		//Startdate
		dl.setTimestamp( 4 , startdate );
		//Enddate
		dl.setTimestamp( 5 , enddate );
		//Notes
		dl.setString( 6 , notes );
		//Owner
		dl.setInt( 7 , userId );
		dl.executeUpdate();
		promotionid = dl.getAutoGeneratedKey();

		//ItemLines itemlines = null;
		ItemLines itemlines = promotionvo.getItemlines();


		if ( itemlines!= null )
		{

			Set s = itemlines.keySet();
			Iterator itr = s.iterator();

			while ( itr.hasNext())
			{
				Object o = itr.next();
				ItemElement ele = (ItemElement) itemlines.get(o);

				dl.clearParameters();
				dl.setSql( "promotion.addpromotion2" );

				//PromotionID
				dl.setInt( 1 , promotionid );

				//ItemID
				ListElementMember sm  = ( ListElementMember )ele.get( "ItemId" );
				int value = ((Integer)sm.getMemberValue()).intValue() ;
				dl.setInt( 2 , value );

				//Quantity
				sm = ( ListElementMember )ele.get( "Value" );
				value = ((Integer)sm.getMemberValue()).intValue() ;
				dl.setInt( 3 , value );

				//Rule
				sm = ( ListElementMember )ele.get( "Type" );
				dl.setString( 4 , ( String ) sm.getMemberValue() );

				//Price
				sm = ( ListElementMember )ele.get( "DiscountedPrice" );
				float price =  ((Float)sm.getMemberValue()).floatValue() ;
				dl.setFloat( 5 , value );

				dl.executeUpdate();

			}
		}
        } finally{
          dl.destroy();
          dl = null;
        }
		try
		{
			// Save CustomFields
			Vector custfieldVec = promotionvo.getCustomfield();
			InitialContext ic = CVUtility.getInitialContext();
			CustomFieldLocalHome custHome = (CustomFieldLocalHome)ic.lookup("local/CustomField");
			CustomFieldLocal custRemote = custHome.create();
			custRemote.setDataSource(this.dataSource);

			for (int i =0;i<custfieldVec.size();i++)
			{
				CustomFieldVO custFieldVO = (CustomFieldVO)custfieldVec.get(i);
				custFieldVO.setRecordID( promotionid );
				custRemote.addCustomField(custFieldVO);
			}

            AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
            AuthorizationLocal authorizationLocal = authorizationHome.create();
            authorizationLocal.setDataSource(dataSource);
            authorizationLocal.saveCurrentDefaultPermission("Promotion", promotionid, userId);

		}
		catch(Exception e )
		{
			System.out.println("[Exception][PromotionEJB.addPromotion] Exception Thrown: "+e);
		}

		return promotionid+"";  // whaaaa... ???
                        // TODO someone needs to think harder.
	}

	/**
	This method delete promotion from database
	*/
	public String deletePromotion( int userId, int elementID ) throws AuthorizationFailedException
	{
		//PromotionVO promotionvo = ( PromotionVO ) data.get( "PromotionID" );

		String id = Integer.toString(elementID);
		if (id != null){
			int PromotionID = Integer.parseInt(id);
			if(!CVUtility.canPerformRecordOperation(userId,"Promotion",PromotionID,ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
				throw new AuthorizationFailedException("Promotion - deletePromotion");

			try
			{
				//String id = ( String )data.get( "PromotionID" );
				CVDal dl = new CVDal(dataSource);
				dl.clearParameters();
				dl.setSql( "promotion.deletepromotion1" );
				dl.setInt( 1, PromotionID );
				dl.executeUpdate();
				dl.clearParameters();
				dl.setSql( "promotion.deletepromotion2" );
				dl.setInt( 1, PromotionID );
				dl.executeUpdate();
				dl.clearParameters();
				dl.destroy();
			}catch( Exception e )
			{
				System.out.println("[Exception][PromotionEJB.deletePromotion] Exception Thrown: "+e);			return "Fail" ;
			}
		}
		return "OK" ;
	}

	/**
	This method update promotion into database
	*/
	public String updatePromotion(int userId , HashMap data ) throws AuthorizationFailedException
	{

		PromotionVO promotionvo = ( PromotionVO ) data.get( "PromotionVo" );

		if(!CVUtility.canPerformRecordOperation(userId,"Promotion",promotionvo.getPromotionid(),ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
			throw new AuthorizationFailedException("Promotion - updatePromotion");

		String description = promotionvo.getDescription();
		Timestamp enddate= promotionvo.getEnddate();
		String name = promotionvo.getName();
		String notes = promotionvo.getNotes();
		Timestamp startdate = promotionvo.getStartdate();
		String status = promotionvo.getStatus();
		int PromotionID = promotionvo.getPromotionid();

		CVDal dl = new CVDal(dataSource);
		dl.clearParameters();
		dl.setSql( "promotion.updatepromotion1" );


		// begin :: adding for fieldlevel rights...
		PromotionVO promotionDBVO = new PromotionVO();
		promotionDBVO = getPromotion(userId, data);
		promotionvo = (PromotionVO)CVUtility.replaceVO(promotionDBVO, promotionvo, "Promotion", userId, this.dataSource);
		// end :: adding for fieldlevel rights...



    //	ALLSQL.put( "promotion.updatepromotion1","update promotion set Title = ? , Description = ? , Status = ? , Startdate = ? , Enddate=? , Notes =? where PromotionID = ? " );
		//Title
		dl.setString( 1 , name );
		//description
		dl.setString( 2 , description );
		//Status
		dl.setString( 3 , status );
		//Startdate
		dl.setTimestamp( 4 , startdate );
		//Enddate
		dl.setTimestamp( 5 , enddate );
		//Notes
		dl.setString( 6 , notes );

		dl.setInt( 7 , PromotionID );

		dl.executeUpdate();

		// call delete for deleting already Items
		dl.clearParameters();
		dl.setSql( "promotion.deletepromotion2" );
		dl.setInt( 1, PromotionID );
		dl.executeUpdate();
		dl.clearParameters();



		//ItemLines itemlines = null;
		ItemLines itemlines = promotionvo.getItemlines();
		if ( itemlines!= null )
		{

			Set s = itemlines.keySet();
			Iterator itr = s.iterator();

			while ( itr.hasNext())
			{
				Object o = itr.next();
				ItemElement ele = (ItemElement) itemlines.get(o);
				dl.clearParameters();
				dl.setSql( "promotion.addpromotion2" );

				//PromotionID
				dl.setInt( 1 , PromotionID );

				//ItemID
				ListElementMember sm = ( ListElementMember )ele.get( "ItemId" );
				int value =  ((Integer)sm.getMemberValue()).intValue() ;
				dl.setInt( 2 , value );

				//Quantity
				sm = ( ListElementMember )ele.get( "Value" );
				value = ((Integer)sm.getMemberValue()).intValue() ;
				dl.setInt( 3 , value );

				//Rule
				sm = ( ListElementMember )ele.get( "Type" );
				dl.setString( 4 , ( String ) sm.getMemberValue() );

				//Price
				sm = ( ListElementMember )ele.get( "DiscountedPrice" );
				float fvalue =  ((Float)sm.getMemberValue()).floatValue() ;
				dl.setInt( 5 , value );
				dl.executeUpdate();

			}


		}


		try
		{
			// update CustomFields
			Vector custfieldVec = promotionvo.getCustomfield();
			InitialContext ic = CVUtility.getInitialContext();
			CustomFieldLocalHome custHome = (CustomFieldLocalHome)ic.lookup("local/CustomField");
			CustomFieldLocal custRemote = custHome.create();
			custRemote.setDataSource(this.dataSource);

			for (int i =0;i<custfieldVec.size();i++)
			{
				CustomFieldVO custFieldVO = (CustomFieldVO)custfieldVec.get(i);
				custFieldVO.setRecordID( PromotionID );
				custRemote.updateCustomField( custFieldVO );
			}
		}
		catch(Exception e )
		{
			System.out.println("[Exception][PromotionEJB.updatePromotion] Exception Thrown: "+e);
		}

		dl.destroy();
		dl = null;
		return "";

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