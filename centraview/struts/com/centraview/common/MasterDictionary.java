/*
 * $RCSfile: MasterDictionary.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:13 $ - $Author: mking_cv $
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
 
package com.centraview.common;

import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;
public class MasterDictionary extends HashMap implements java.io.Serializable
{
	/**
	 *	In this method get the track table Name and their search Clause
	 *
	 * @param   target  Target Table Name
	 * @param   source  Source Table Name
	 * @return   HashMap Containing Table Set and Search Clause  
	 */
	public HashMap getQuryData(String target,String source)
	{
		HashMap hmap = new HashMap();

		Stack stack = getTableTrack(target, source);

		Set tableSet = new TreeSet();
		
		String pop1 = "";
		String pop2 = "";		
		pop1 = (String)stack.pop();
		tableSet.add(pop1);
		String searchClause = " ";
		
		while(!stack.empty())
		{
			pop2 = (String)stack.pop();			
			tableSet.add(pop1);
			tableSet.add(pop2);			
			Vector vec = (Vector) ((HashMap)this.get(pop2)).get(pop1);

			String clause = "";
			String othTableNames = "";

			if (vec.size() == 1)
			{
					TableRelate tr = (TableRelate)vec.get(0);
					
					clause 		 = tr.getCaluse();
					String othName = tr.getOthertablename();
					if(othName == null)
						othName = "";
					othTableNames =  othName;			
			}
			else
			{
				clause = clause + " AND (  ";
				for (int i = 0;i<vec.size();i++)
				{
					TableRelate tr = (TableRelate)vec.get(i);
					
					if(i > 0)
						clause 		 = clause + " OR ( 1 = 1 "+ tr.getCaluse() + " ) ";
					else
						clause 		 = clause + " ( 1 = 1 "+ tr.getCaluse() + " ) ";										
						
					String othName = tr.getOthertablename();
					if(othName == null)
						othName = "";

					if( !((othTableNames.trim()).equals("")))
					othTableNames = othTableNames +" , "+ othName;
						
				}
				clause = clause + " ) ";
			}
			StringTokenizer parse = new StringTokenizer(othTableNames, ",");

			while (parse.hasMoreTokens ()) 
			{ 
				 tableSet.add(parse.nextToken().toString());
			} 
	
			
			searchClause = searchClause +" "+clause;
			pop1 = pop2;			
			
		}
		hmap.put("Table",tableSet);
		hmap.put("Search",searchClause);		
		return hmap;
		
	}// end of getQuryData
	

	/**
	 *	This method find the Table Track for given source and Taget
	 *
	 * @param   target  Target Table Name  
	 * @param   source  Source  Table Name  
	 * @return   track Table set   
	 */
	private Stack getTableTrack(String target,String source )
	{
		String currEle = source;

		Stack current = new Stack();
		Stack track = new Stack();
		Stack found = new Stack();

		found.push(source);

		if (source.equals(target))
		{
			track.push(source);
			return track ;			
		}
		

		while(!found.empty() )
		{
			String popEle = (String)found.pop();
			String lastTrack ="";

			//1 st Step
			if (!track.empty() )
			{
				lastTrack = (String)track.pop();

				if (popEle.equals(lastTrack))	continue;
				else
				{
					currEle = popEle;
					found.push(currEle);
					track.push(lastTrack);
				}
			}
			// 2 nd Step
			HashMap currChild = (HashMap)get(currEle);
			Set set = currChild.keySet();
			current.addAll(set);
			
			//3 rd Step
			if (current.empty())
			{
				continue;
			}
			else
			{
				if (current.search(target) != -1)
				{
					track.push(currEle);
					track.push(target);					
					break;
				}
				else
				{
					while ( !current.empty())
					{
						found.push(current.pop());
						track.push(currEle);
					}
				}
			}
		
		}// end of while
		return track ;
	}// end of searchPath
	
	public MasterDictionary()
	{}
}// end of MasterDictionary












