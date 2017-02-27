/*
 * $RCSfile: MergeSearchResultVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:46 $ - $Author: mking_cv $
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


package com.centraview.administration.merge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This VO will contain the search results.  that were found as part of the Merge search proccess.
 * It will consist of an ArrayList of ArrayLists of HashMaps.
 * The outerarraylist will indicate the groupings, so ArrayList(0) will be an
 * ArrayList of HashMaps.  The HashMap will have the following Keys
 * "id", "title" and "owner"  This can easily be turned into a Generic displaylist in Struts world.
 * 
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class MergeSearchResultVO implements Serializable
{
  /**
   * group will simply contain an arraylist for each node on the list.
   */
  private ArrayList group = null;
  
  /**
   * The averageGroupSize will be a float displayed as 0.00
   * Which will basically be calculated by iterating the group, getting sizes of
   * the groupmember arraylists and calculating the average.  The field will support
   * the need based lazy calculation of this value. 
   */
  private float averageGroupSize = 0;

  /**
   * This field will allow a series of servlets to track where they are, while manually iterating this arraylist.
   */
  private int cursor = 0; 
  
  /**
   * field to hold the type of the merge results these are
   * 1 = entity, 2 = individual
   */
  private int mergeType = 1;
  
  /**
   * addGrouping takes an ArrayList and appends it to the end of the private group
   * ArrayList.  If group is null a new one is created and grouping is inserted at index 0.
   * For the specific purposes of this code, it is assumed in the handler that the nodes in grouping are
   * Hashmaps with keys "id", "title", "owner"
   * 
   * @author Kevin McAllister <kevin@centraview.com>
   * @param grouping
   */
  public void addGrouping(ArrayList grouping) 
  {
    if (this.group == null)
    {
      group = new ArrayList();
      group.add(0, grouping);  // Hopefully this doesn't throw index out of bounds.
    } else {
      group.add(grouping);
    }
  }
  
  public ArrayList getGroup()
  {
    return this.group;
  }
  
  /**
   * returns the current ArrayList.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return a valid grouping ArrayList or null
   */
  public ArrayList currentGrouping()
  {
    if (group == null) {
      return null; // returning null rather than empty arraylist 
    }
    if (cursor < group.size()) {
      return (ArrayList)group.get(cursor);
    }
    return null;
  }
  
  /**
   * increments internal cursor and returns the next grouping.  If for any reason
   * a valid grouping cannot be returned, null will be returned.
   * 
   * @author Kevin McAllister <kevin@centraview.com>
   * @return a valid grouping ArrayList or null
   */
  public ArrayList nextGrouping()
  {
    if (group == null) {
      return null; // returning null rather than empty arraylist 
    }
    if (++cursor < group.size()) {
      return (ArrayList)group.get(cursor);
    }
    return null;
  }
  
  /**
   * To see if there are more groupings left.  to be returned from nextGrouping()
   * @author Kevin McAllister <kevin@centraview.com>
   * @return true if there are more groupings, false otherwise.
   */
  public boolean hasNextGrouping()
  {
    if((group != null) && ((cursor+1) < group.size()))
    {
      return true;
    }
    return false;
  }
  
  /**
   * Provides a means to find the current place in our manual iteration of
   * the group ArrayList.
   * @author Kevin McAllister <kevin@centraview.com>
   * @return int the cursor, should be > 0
   */
  public int getCursor()
  {
    return this.cursor;
  }
  
  /**
   * Provides the size of the underlying group ArrayList.
   * Simply deferred to group.size()
   * @author Kevin McAllister <kevin@centraview.com>
   * @return group.size() or 0 if group == null;
   */
  public int size()
  {
    if (group != null)
    {
      return group.size();
    }
    return 0;
  }
  
  /**
   * Simply sets cursor = 0;  Manual iteration will start all over again.
   * 
   * @author Kevin McAllister <kevin@centraview.com>
   * 
   */
  public void resetCursor()
  {
    this.cursor = 0;
  }
  
  public String toString()
  {
    StringBuffer string = new StringBuffer();
    for (int i = 0; i < this.size(); i++)
    {
      string.append("group["+i+"]:\n");
      ArrayList subgroup = (ArrayList)this.group.get(i);
      for (int j=0; j < subgroup.size(); j++)
      {
        string.append("\tsubgroup["+i+"]: ");
        string.append(((HashMap)subgroup.get(j)).toString());
        string.append("\n");
      }
    }
    return string.toString();
  }
  
  public int getTotalRecords()
  {
    if (group == null)
    {
      return 0;
    }
    int size = 0;
    for(int i = 0; i < group.size(); i++)
    {
      size += ((ArrayList)group.get(i)).size();
    }
    return size;
  }
  
  /**
   * A getter for the mergeType
   * @author Kevin McAllister <kevin@centraview.com>
   * @return mergeType 1 = Entity, 2 = Individual
   */
  public int getMergeType()
  {
    return mergeType;
  }

  /**
   * A setter for the mergeType
   * @author Kevin McAllister <kevin@centraview.com>
   * @param int i type, 1=Entity, 2=Individual
   */
  public void setMergeType(int i)
  {
    mergeType = i;
  }

}