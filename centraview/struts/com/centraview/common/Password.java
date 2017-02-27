/*
 * $RCSfile: Password.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:14 $ - $Author: mking_cv $
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public final class Password
{
  private static Password instance;
  
  /* default Constructor*/
  private Password(){
  }

  public synchronized String encrypt(String plaintext)
  {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA1"); 
    }
    catch(NoSuchAlgorithmException noSuchAlgorithmException)
    {
		noSuchAlgorithmException.printStackTrace();
    }//end of catch block
    
    try
    {
      md.update(plaintext.getBytes("UTF-8")); 
    }
    catch(UnsupportedEncodingException unsupportedEncodingException)
    {
		unsupportedEncodingException.printStackTrace();
    }//end of catch block
    byte raw[] = md.digest(); 
    String hash = (new BASE64Encoder()).encode(raw); 
    return hash; 
  }//end of encrypt(String plaintext)
  public static synchronized Password getInstance()
  {
    if(instance == null)
    {
      return new Password();
    }//end of if(instance == null)
    else
    {
      return instance;
    }//end of else for if(instance == null)
  }//end of method Password getInstance()
  
}//ednd of class Password