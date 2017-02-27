/*
 * $RCSfile: ListElementMember.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:04 $ - $Author: mking_cv $
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


/**
 * @author 
 */
abstract public class ListElementMember
{
  int auth;

  abstract public String getMemberType();

  abstract public String getRequestURL();

  abstract public Object getMemberValue();

  abstract public char getDisplayType();

  abstract public String getGraphicResourceURL();

  abstract public boolean getLinkEnabled();

  abstract public void setLinkEnabled(boolean enabled);

  abstract public int getAuth();

  abstract public void setAuth(int auth);

  abstract public String getDisplayString();

  abstract public String getSortString();

  /**
   * This method search the String with displayString
   * Both string convert to UpperCase and and then
   * Compare with contentEquals method.
   *
   * @param searchString The search String we are searching the
   * ListElementMember's display String.
   *
   * @return Whether or not the searchString was found.
   *
   * @see #getDisplayString()
   */
  public boolean search(String searchString)
  {
    if (searchString == null)
    {
      searchString = "";
    } //end of if statement (searchString == null)

    if (searchString.length() > 8)
    {
      searchString = searchString.substring(8);
    } //end of if statement (searchString.length() > 8)


    String searchValue = searchString.toUpperCase().trim();
    String dispValue = this.getDisplayString();

    if (dispValue == null)
    {
      dispValue = "";
    } //end of if statement (dispString == null)

    dispValue = dispValue.toUpperCase().trim();

    int occuranceCount = dispValue.indexOf(searchValue);

    boolean searchflag = true;
    if (occuranceCount == -1){
			searchflag = false;
		}
    return searchflag;

  } //end of search method
} //end of ListElementMember class
