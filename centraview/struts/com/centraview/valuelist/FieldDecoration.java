/*
 * $RCSfile: FieldDecoration.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:07 $ - $Author: mking_cv $
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

package com.centraview.valuelist;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class FieldDecoration
{

  public static final int urlType = 1;
  public static final int dateType = 2;
  public static final int dateTimeType = 3;
  public static final int timeType = 4;
  public static final int fileSizeType = 5;
  public static final int moneyType = 6;
  public static final int hourType = 7;
  public static final int paidType = 8;
  public static final int limitCharsType = 9;
	
  int fieldDecorationType;
  private String urlOpen;
  private String urlClose;
  private int parameterIndex;
  private int numberOfChars;
  
  /**
   * @param urlOpen
   * @param urlClose
   * @param parameterIndex
   */
  public FieldDecoration(String urlOpen, String urlClose, int parameterIndex, int fieldDecorationType)
  {
    this.urlOpen = urlOpen;
    this.urlClose = urlClose;
    this.parameterIndex = parameterIndex;
    this.fieldDecorationType = fieldDecorationType;
  }

  public FieldDecoration(int parameterIndex, int numberOfChars, int fieldDecorationType)
  {
    this.parameterIndex = parameterIndex;
    this.numberOfChars = numberOfChars;
    this.fieldDecorationType = fieldDecorationType;
  }
  
  /**
   * @return Returns the parameter.
   */
  public final int getParameter()
  {
    return parameterIndex;
  }
  /**
   * @param parameter The parameter to set.
   */
  public final void setParameter(int parameterIndex)
  {
    this.parameterIndex = parameterIndex;
  }
  /**
   * @return Returns the urlClose.
   */
  public final String getUrlClose()
  {
    return urlClose;
  }
  /**
   * @param urlClose The urlClose to set.
   */
  public final void setUrlClose(String urlClose)
  {
    this.urlClose = urlClose;
  }
  /**
   * @return Returns the urlOpen.
   */
  public final String getUrlOpen()
  {
    return urlOpen;
  }
  /**
   * @param urlOpen The urlOpen to set.
   */
  public final void setUrlOpen(String urlOpen)
  {
    this.urlOpen = urlOpen;
  }

  /**
   * @return returns the fieldDecorationType
   */
  public final int getFieldDecorationType() {
	return fieldDecorationType;
  }//end of getFieldDecorationType()

  /**
   * @param fieldDecorationType The fieldDecorationType to set.
   */
  public final void setFieldDecorationType(int fieldDecorationType)
  {
    this.fieldDecorationType = fieldDecorationType;
  }//end of setFieldDecorationType(int fieldDecorationType)

  public final void setNumberOfChars(int numberOfChars)
  {
    this.numberOfChars = numberOfChars;
  }

  public final int getNumberOfChars()
  {
    return this.numberOfChars;
  }
}
