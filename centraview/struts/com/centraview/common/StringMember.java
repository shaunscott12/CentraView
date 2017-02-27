/*
 * $RCSfile: StringMember.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:17 $ - $Author: mking_cv $
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

import java.io.Serializable;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;

/**
 * The StringMember class.
 *
 * @author 
 */
public class StringMember extends ListElementMember implements Serializable
{
  private int auth;
  private String requestURL;
  private char displayType;
  private String graphicResourceURL;
  private boolean linkEnabled;
  private String memberValue;
  private String memberType;

  public StringMember(String memberType, String memberValue, int auth, String requestURL, char displayType, boolean linkEnabled)
  {
    this.auth = auth;
    this.requestURL = requestURL;
    this.displayType = displayType; // Graphics  or Text
    this.linkEnabled = linkEnabled;
    this.memberValue = memberValue;
    this.memberType = memberType; //column name
  }

  public String getMemberType()
  {
    return memberType;
  }

  public String getDataType()
  {
    return("string");
  }

  public Object getMemberValue()
  {
    if ((memberValue == null) || memberValue.equals("null"))
    {
      return "";
    }
    return memberValue;
  }

  public String getRequestURL()
  {
    return requestURL;
  }

  public void setRequestURL(String str)
  {
    requestURL = str;
  }

  public char getDisplayType()
  {
    return displayType;
  }

  public String getGraphicResourceURL()
  {
    return graphicResourceURL;
  }

  public void setGraphicResourceURL(String str)
  {
    graphicResourceURL = str;
  }

  public boolean getLinkEnabled()
  {
    return linkEnabled;
  }

  public void setLinkEnabled(boolean b)
  {
    linkEnabled = b;
  }

  public int getAuth()
  {
    return auth;
  }

  public void setAuth(int auth)
  {
    this.auth = auth;
  }

  public String getDisplayString()
  {
    if ((memberValue == null) || memberValue.equals("null") || (auth == ModuleFieldRightMatrix.NONE_RIGHT))
    {
      return "";
    }

    return memberValue;
  }

  public String getSortString()
  {
    StringBuffer sb = new StringBuffer();
    String theVal;
    sb.setLength(100);

    for (int i = 0; i < 100; i++)
    {
      sb.setCharAt(i, ' ');
    }

    if (memberValue == null)
    {
      theVal = " ";
    }else{
      theVal = memberValue.substring(0, Math.min(30, (memberValue.length())));
    }

    //String str = memberValue;
    sb.insert(0, theVal);

    return sb.toString().toUpperCase();
  }


  /**
   * Over-rides the Object.toString() method, which makes for
   * debug statements which are easier to read and give the
   * developer useful information.
   * @return String representation of this StringMember object
   */
  public String toString()
  {
    StringBuffer toReturn = new StringBuffer("");
    toReturn.append("[" + this.memberValue + "]");
    return(toReturn.toString());
  }   // end toString() method


} //end of StringMember class

