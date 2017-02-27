/*
 * $RCSfile: BackgroundImportMembers.java,v $    $Revision: 1.2 $  $Date: 2005/08/04 15:22:25 $ - $Author: mcallist $
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

package com.centraview.marketing;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.centraview.common.CVUtility;
import com.centraview.marketing.marketingfacade.MarketingFacade;
import com.centraview.marketing.marketingfacade.MarketingFacadeHome;

/**
 * This class used will be called at the time of import the contacts to the
 * mail.
 * @author Naresh Patel <npatel@centraview.com>
 */
public class BackgroundImportMembers extends Thread implements Serializable {

  private static Logger logger = Logger.getLogger(BackgroundImportMembers.class);

  private Vector importList = new Vector();
  private int headerRow = 0;
  private int listID = 0;
  private int individualID = 0;
  private Collection CustomEntList = null;
  private Collection CustomIndList = null;
  private String tabDelimiter;
  private String lineDelimiter;
  private String headLine;
  private String dataSource = null;
  private HashMap importMessage = new HashMap();

  /**
   * This method Parse the information column by column and stores the
   * information in the database and returns a String of Message objects.
   * @param Vector The importList to get the Import row and Column.
   * @param int The headerRow to get the head row.
   * @param int The listID Identify we are are importing records into which
   *          list.
   * @param int The individualID Identify who is logged in an performing the
   *          task.
   * @param Collection The CustomEntList to get the list of Custom Entity.
   * @param Collection The CustomIndList to get the list of Custom Individual.
   * @param String The tabDelimiter to identify the column seperation delimiter.
   * @param String The lineDelimiter to identify the row seperation delimiter.
   * @param String The headLine its a header line.
   * @param dataSource A string that contains the cannonical JNDI name of the
   *          datasource.
   * @param threadName A string that contains the unique identity to the
   *          processing thread.
   * @return String[] of Message objects.
   */
  public BackgroundImportMembers(Vector importList, int headerRow, int listID, int individualID, Collection CustomEntList, Collection CustomIndList,
      String tabDelimiter, String lineDelimiter, String headLine, String dataSource, String threadName) {
    super(threadName);
    this.importList = importList;
    this.headerRow = headerRow;
    this.listID = listID;
    this.individualID = individualID;
    this.CustomEntList = CustomEntList;
    this.CustomIndList = CustomIndList;
    this.tabDelimiter = tabDelimiter;
    this.lineDelimiter = lineDelimiter;
    this.headLine = headLine;
    this.dataSource = dataSource;
  }

  /**
   * Sets a String array of messages at the time of import.
   * @param HashMap It contains the result of the import process in the map
   *          format.
   */
  public void setImportedMessages(HashMap importMessage)
  {
    this.importMessage = importMessage;
  } // end setImportedMessages method

  /**
   * Returns The String array of messages after the importing the information we
   * will display it to the user.
   * @return HashMap of messages at the time of import
   */
  public HashMap getImportedMessages()
  {
    return this.importMessage;
  } // end of getImportedMessages method

  public void run()
  {
    try {
      MarketingFacadeHome marketingFacadeHome = (MarketingFacadeHome) CVUtility.getHomeObject(
          "com.centraview.marketing.marketingfacade.MarketingFacadeHome", "MarketingFacade");
      MarketingFacade remote = marketingFacadeHome.create();
      remote.setDataSource(this.dataSource);
      this.importMessage = remote.getImportList(this.importList, this.headerRow, this.listID, this.individualID, this.CustomEntList,
          this.CustomIndList, this.tabDelimiter, this.lineDelimiter, this.headLine);

    } catch (Exception e) {
      logger.error("[run] Exception thrown.", e);
    }
  }
}
