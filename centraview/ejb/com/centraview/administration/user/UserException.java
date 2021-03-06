/*
 * $RCSfile: UserException.java,v $    $Revision: 1.2 $  $Date: 2005/09/23 11:06:02 $ - $Author: mcallist $
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
 * NoteException.java
 *
 * @author   Iqbal Khan
 * @version  1.0    
 * 
 */

package com.centraview.administration.user;

public class UserException extends java.lang.Exception {
  private int exceptionId;
  private String exceptionDescription;

  public static final int OTHER_EXCEPTION = 0;
  public static final int INSERT_FAILED = 1;
  public static final int UPDATE_FAILED = 2;
  public static final int GET_FAILED = 3;
  public static final int DELETE_FAILED = 4;
  public static final int INVALID_DATA = 5;
  public static final int COULDNOT_CHANGE_PASSWORD = 6;
  public static final int IO_FAILURE = 7;

  /**
   * Constructor
   * @param exceptionId
   * @param exceptionDescrription
   */
  public UserException(int exceptionId, String exceptionDescription) {
    this.exceptionDescription = exceptionDescription;
    this.exceptionId = exceptionId;
  }

  /**
   * gets the Exception Description
   * @return String
   */

  public String getExceptionDescription()
  {
    return this.exceptionDescription;
  }

  /**
   * sets the Exception description
   * @param exceptionDescription
   */
  public void setExceptionDescription(String exceptionDescription)
  {
    this.exceptionDescription = exceptionDescription;
  }

  /**
   * gets the Exception Id
   * @return int
   */
  public int getExceptionId()
  {
    return this.exceptionId;
  }

  /**
   * sets the exception Id
   * @param exceptionId
   */
  public void setExceptionId(int exceptionId)
  {
    this.exceptionId = exceptionId;
  }
}
