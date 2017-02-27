/*
 * $RCSfile: ValueListClassMapping.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:07 $ - $Author: mking_cv $
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
 * The ValueListClassMapping stores information JNDI name and method regarding a
 * particular ListType so they can be called generically through reflection.
 * This is done primarily to keep the code clean and easier to maintain, since
 * it will turn many lines of code into just a few fairly advanced lines of
 * code. This stuff is used in the ValueListEJB, to call the right EJB and
 * method.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class ValueListClassMapping
{
  private String jndiName;
  private String methodName;
  
  /**
   * Create a VLClassMapping.
   * @param jndiName
   * @param methodName
   */
  public ValueListClassMapping(String jndiName, String methodName)
  {
    this.jndiName = jndiName;
    this.methodName = methodName;
  }

  
  /**
   * @return Returns the jndiName.
   */
  public final String getJndiName()
  {
    return jndiName;
  }
  /**
   * @param jndiName The jndiName to set.
   */
  public final void setJndiName(String jndiName)
  {
    this.jndiName = jndiName;
  }
  /**
   * @return Returns the methodName.
   */
  public final String getMethodName()
  {
    return methodName;
  }
  /**
   * @param methodName The methodName to set.
   */
  public final void setMethodName(String methodName)
  {
    this.methodName = methodName;
  }
}
