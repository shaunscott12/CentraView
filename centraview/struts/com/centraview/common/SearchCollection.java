/*
 * $RCSfile: SearchCollection.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:16 $ - $Author: mking_cv $
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
 * SearchHandler.java
 * Author Shilpa Patil
 * Date 19/07/2003
 */
package com.centraview.common;

import java.util.ArrayList;
import java.util.Vector;

public class SearchCollection extends Vector 
{
	private String strPowerString;

	// This method returns final power string
	// prepared with each search record 
	// present in collection.
	public String getFinalPowerString()
	{
		return strPowerString;
	}
	
	// This method parses the PS and creates
	// Records and fill the collection
	public ArrayList getSearchCollection(String temp)
	{
		
		ArrayList searchRecordArl = new ArrayList();
		ArrayList finalRecords =  new ArrayList();
		ArrayList joinArl =  new ArrayList();
		
		int intANDPos = 0;
		int intORPos = 0;
				
		while(intANDPos != -1 || intORPos!= -1)
		{
			intANDPos = temp.indexOf("AND");
			intORPos  = temp.indexOf("OR");
		
			if (intANDPos != -1 && intORPos!= -1)
			{
				if (intANDPos < intORPos)
				{
					searchRecordArl.add(temp.substring(0,intANDPos));
					joinArl.add("AND");
					temp = temp.substring(intANDPos+3);
				}
				else
				{
					searchRecordArl.add(temp.substring(0,intORPos));
					joinArl.add("OR");		
					temp = temp.substring(intORPos+2);
				}
		
			} 
			
			if (intANDPos == -1 && intORPos != -1)
			{
				searchRecordArl.add(temp.substring(0,intORPos));
				joinArl.add("OR");
				temp = temp.substring(intORPos+2);
			}

			if (intANDPos != -1 && intORPos == -1)
			{
				searchRecordArl.add(temp.substring(0,intANDPos));
				joinArl.add("AND");
				temp = temp.substring(intANDPos+3);
			}
			
		}
		
		//Last element in temp is query only ie no AND or OR
		searchRecordArl.add(temp);
		
	
		int size = searchRecordArl.size();
		
		String record = null;
		String join = null;
		
		//No need to append join for 1st element
		finalRecords.add(searchRecordArl.get(0));
		
		//Append joins from arl to records
		for(int k =1;k<size;k++)
		{
			record = (String)searchRecordArl.get(k);
			join = (String)joinArl.get(k-1);

			finalRecords.add(join+" "+record);
		}
		
		return finalRecords;

	}
	
	
	public static void main(String args[])
	{
		SearchCollection sColl =  new SearchCollection();
			
		String temp = "   table1.column1  <Begins With>  'c'  OR table1.column1  <Begins With>  'c'  OR table1.column1  <Begins With>  'c'  OR table1.column1  <Begins With>  'c' ";
					  
		System.out.println("records are "+sColl.getSearchCollection(temp));
	}
	
}