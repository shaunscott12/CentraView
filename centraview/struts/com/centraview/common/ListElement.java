/*
 * $RCSfile: ListElement.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:04 $ - $Author: mking_cv $
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
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author CentraView, LLC.
 */
abstract public class ListElement extends HashMap
{
  public char auth;

  abstract public void addMember(int ID, ListElementMember lem);

  abstract public ListElementMember getMember(String memberName);

  abstract public int getElementID();

  /**
   * In this method  iterate the ListElement and
   * Get ListElementMember and search whether
   * the searchstring present in the element.
   *
   * @param searchString The Search String to look for.
   *
   * @return Whether or not the string was found.
   */
  public boolean search(String searchString)
  {
    boolean searchFlag = false;
    //System.out.println(" searchStr in ListElement "+searchStr);
    Set keyset = keySet();
    Iterator it = keyset.iterator();
    String str = null;

    while (it.hasNext())
    {
      str = (String) it.next();
      ListElementMember ele = (ListElementMember) get(str);

      if (ele != null)
      {
        searchFlag = ele.search(searchString);
      } //end of if statement (ele != null)

      if (searchFlag)
      {
        break;
      } //end of if statement (searchFlag)
    } //end of while loop (it.hasNext())
    return searchFlag;
  } //end of search method

  /**
   * In this method  iterate the ListElement and
   * Get ListElementMember and search whether
   * the searchstring present in the element.
   *
   * @param searchString The Search String to look for.
   * @param searchColumn The Search String on particular Column.
   *
   * @return Whether or not the string was found.
   */
  public boolean search(String searchString,String searchColumn)
  {
    boolean searchFlag = false;
	ListElementMember ele = (ListElementMember) get(searchColumn);

	if (ele != null)
	{
		searchFlag = ele.search(searchString);
	} //end of if statement (ele != null)
    return searchFlag;
  } //end of search method
}
