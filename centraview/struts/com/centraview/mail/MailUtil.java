/*
 * $RCSfile: MailUtil.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:07 $ - $Author: mking_cv $
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

package com.centraview.mail;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;

/**
 * Helper for email. 
 * Created: Oct 11, 2004
 * 
 * @author CentraView, LLC. 
 */
public class MailUtil
{
  private static Logger logger = Logger.getLogger(MailUtil.class);
  
  static MailFolderVO getFolderVO(int folderID, int individualID, String dataSource)
  {
    MailFolderVO folderVO = new MailFolderVO();
    
    try {
      MailHome home = (MailHome)CVUtility.getHomeObject("com.centraview.mail.MailHome", "Mail");
      Mail mailRemote = (Mail)home.create();
      mailRemote.setDataSource(dataSource);
      
      if (folderID == 0)
      {
        int defaultAccountID = mailRemote.getDefaultAccountID(individualID);
        folderVO = mailRemote.getPrimaryEmailFolder(defaultAccountID);
      } else {
        folderVO = mailRemote.getEmailFolder(folderID);
      }
    } catch (Exception e) {
      logger.error("Exception in MailUtil.getFolderVO()");
      e.printStackTrace();
    }
    return(folderVO);
  }
}
