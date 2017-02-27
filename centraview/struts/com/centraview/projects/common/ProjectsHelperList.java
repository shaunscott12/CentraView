/*
 * $RCSfile: ProjectsHelperList.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:28 $ - $Author: mking_cv $
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
package com.centraview.projects.common;

import java.util.HashMap;
import java.util.Vector;

import com.centraview.common.CVUtility;
import com.centraview.projects.helper.ProjectsHelper;
import com.centraview.projects.helper.ProjectsHelperHome;

/**
 * This SingleTon Class is written to get differnet list from
 * database while openeing new project etc
 * Name and package to be change after final code freeze
 *
 */
public class ProjectsHelperList extends HashMap
{
  public static ProjectsHelperList helperList = null;
  private static String dataSource = "";

  private ProjectsHelperList()
  {
    try {
      ProjectsHelperHome phh = (ProjectsHelperHome)CVUtility.getHomeObject("com.centraview.projects.helper.ProjectsHelperHome", "ProjectsHelper");
      ProjectsHelper remote = (ProjectsHelper) phh.create();
      remote.setDataSource(ProjectsHelperList.dataSource);

      Vector statusCol = remote.getProjectStatusList();
      this.put("ProjectStatus", statusCol);
    } catch (Exception e) {
      System.out.println("[Exception] ProjectsHelperList.ProjectsHelperList: " + e.toString());
      e.printStackTrace();
    }
  }

  public synchronized static ProjectsHelperList getProjectsHelperList(String newDataSource)
  {
    dataSource = newDataSource;
    if (helperList == null) {
      helperList = new ProjectsHelperList();
    }
    return helperList; 
  }
  
  /**
   * This method sets the GlobalMasterLists to null and 
   * then refreshes them. The refreshing of the GlobalMasterLists
   * is done here so that there isn't a performance hit the
   * next time someone loads the GlobalMasterList.
   */
  public static void refreshProjectsHelperList()
  {
    helperList = null;
    ProjectsHelperList.getProjectsHelperList(dataSource);
  }

}
