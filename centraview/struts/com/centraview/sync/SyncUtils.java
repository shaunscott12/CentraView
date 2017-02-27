/*
 * $RCSfile: SyncUtils.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:02 $ - $Author: mking_cv $
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

package com.centraview.sync;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.struts.action.DynaActionForm;

import com.centraview.common.UserObject;
import com.centraview.contact.helper.MethodOfContactVO;

/**
 * A set of utilities used by the CompanionLink
 * Sync API for operations such as decoding the
 * special characters, etc. This class should be
 * used only by the CompanionLink Sync API code,
 * as all the utilities involved are written
 * specifically to those specifications and should
 * not be modified by use in other modules.
 */
public class SyncUtils
{
  public boolean checkSession(UserObject userObject, DynaActionForm form)
  {
    String formSessionID = (String)form.get("sessionID");
    String userSessionID = (String)userObject.getSessionID();
    if (formSessionID.equals(userSessionID))
    {
      return(true);
    }else{
      return(false);
    }
  }   // end checkSession() method
  
  /**
   * Takes a DynaActionForm bean containing the data
   * that the CompanionLink Agent sent to CentraView,
   * and decodes the reserved special characters back
   * into their appropriate String representations.
   * Characters convertered are: (input - output)
   * <ul>
   * <li>0x06 - Tab (\t)</li>
   * <li>0x07 - Ampersand (&amp;)</li>
   * <li>0x08 - Newline (\n)</li>
   * </ul>
   * @param form DynaActionForm to be processed.
   * @return DynaActionForm whose properties have been processed.
   */
  public DynaActionForm parseSpecialChars(DynaActionForm form)
  {
    HashMap properties = (HashMap)form.getMap();
    Set keys = (Set)properties.keySet();
    Iterator iter = keys.iterator();
    while (iter.hasNext())
    {
      String key = (String)iter.next();
      String value = (String)form.get(key);
      String decodedValue = SyncUtils.decodeString(value);
      form.set(key, decodedValue);
    }
    return(form);
  }   // end parseSpecialChars() method

  /**
   * Loops through the given HashMap, taking each entry
   * and encoding String value according to the CompanionLink
   * specifications for special characters.
   * Characters convertered are: (input - output)
   * <ul>
   * <li>Tab (\t) - 0x06</li>
   * <li>Ampersand (&amp;) - 0x07</li>
   * <li>Newline (\n) - 0x08</li>
   * </ul>
   * @param record HashMap that represents a single record
   * which is to be encoded, and sent to CompanionLink Agent.
   * @return HashMap whose properties/entries have all been
   * encoded to the CompanionLink specifications.
   */
  public HashMap encodeRecord(HashMap record)
  {
    Set keys = (Set)record.keySet();
    Iterator iter = keys.iterator();
    while (iter.hasNext())
    {
      String key = (String)iter.next();
      String value = (String)record.get(key);
      String encodedValue = this.encodeString(value);
      record.put(key, encodedValue);
    }
    return(record);
  }

  /**
   * Takes a CompanionLink-encoded String and decodes
   * the reserved special characters back into their
   * appropriate String representations.
   * Characters convertered are: (input - output)
   * <ul>
   * <li>0x06 - Tab (\t)</li>
   * <li>0x07 - Ampersand (&amp;)</li>
   * <li>0x08 - Newline (\n)</li>
   * </ul>
   * @param value The String which is CompanionLink encoded
   * @return String which has been decoded
   */
  static public String decodeString(String encodedValue)
  {
    String decodedValue = encodedValue;
    decodedValue = decodedValue.replace('\u0006', '\t');  // change 0x06 into Tab
    decodedValue = decodedValue.replace('\u0007', '&');   // change 0x07 into "&"
    decodedValue = decodedValue.replace('\u0008', '\n');  // change 0x08  into newline
    return(decodedValue);
  }   // end decodeValue() method

  /**
   * Takes a String and encodes the reserved special
   * characters into their CompanionLink-encoded String
   * represenations.
   * Characters convertered are: (input - output)
   * <ul>
   * <li>Tab (\t) - 0x06</li>
   * <li>Ampersand (&amp;) - 0x07</li>
   * <li>Newline (\n) - 0x08</li>
   * </ul>
   * @param value The String which is to be CompanionLink encoded
   * @return String which has been encoded
   */
  private String encodeString(String value)
  {
    String encodedValue = value;
    if (value != null)
    {
      encodedValue = encodedValue.replace('\t', '\u0006');  // change Tab in 0x06
      encodedValue = encodedValue.replace('&',  '\u0007');  // change "&" into 0x07
      encodedValue = encodedValue.replace('\n', '\u0008');  // change newline into 0x08
    }
    return(encodedValue);
  }   // end encodeValue() method

  public String formatDate(java.sql.Timestamp timestampObject)
  {
    if (timestampObject != null)
    {
      SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
      String dateString = dateFormatter.format(timestampObject);
      return (dateString);
    }else{
      return("");
    }
  }   // end formatDate(java.sql.Timestamp timestampObject)

  /**
   * Creates and returns a fully populated MethodOfContactVO
   * object, populated with the given parameters. This is useful
   * for the Sync API because the API does this task 14 times.
   * @param mocContent String representation of the "content" field.
   * @param mocExt String representation of the Extension on a phone type.
   * @param syncAs String representation of the "Sync As" value.
   * @param mocType int representation for the type of Method Of Contact like main, mobile, Home, Work etc.,
   * @return A fully populated MethodOfContactVO object.
   */
  public MethodOfContactVO createNewPhoneMoc(String mocContent, String mocExt, String syncAs, int mocType)
  {
    // create new MocVO object
    MethodOfContactVO newMocVO = new MethodOfContactVO();
    
    // set properties
    if (mocExt != null && ! mocExt.equals(""))
    {
      mocContent = mocContent + "EXT" + mocExt;
    }
    newMocVO.setContent(mocContent);
    newMocVO.setSyncAs(syncAs);
    newMocVO.setMocType(mocType);
    return newMocVO;
  }   // end createNewMoc(String) method



}   // end class definition
