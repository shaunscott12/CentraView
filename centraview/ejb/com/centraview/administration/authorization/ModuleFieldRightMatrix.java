/*
 * $RCSfile: ModuleFieldRightMatrix.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:42 $ - $Author: mking_cv $
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

package com.centraview.administration.authorization;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * This is a wrapper class to encapsulate the module and field level rights
 * of a Security Profile or a user (unique rights).
 */

public class ModuleFieldRightMatrix implements Serializable
{
  // the least restrivctive has the smallest number
  public static final int DELETE_RIGHT = 10;
  public static final int UPDATE_RIGHT = 20;
  public static final int VIEW_RIGHT =  30;
  public static final int NONE_RIGHT =  40;

  // Vector of Integer object containg the
  // the module id of all visible modules
  TreeMap moduleMap = null;

  // key will be module id
  // value will be a hashmap
  // the inner hashmaps key will be field id and cvalue will be
  // the right (NONE, UPDATE, VIEW)
  HashMap fieldRights = null;

  // added in the moduleRights TreeMap
  public void addModule(String moduleName, Integer moduleId, Integer parentId)
  {
    if (moduleMap == null) {
      moduleMap = new TreeMap();
    }

    HashMap moduleRight = (HashMap)moduleMap.get(moduleId);
    if (moduleRight == null) {
      moduleRight = new HashMap();
    }
    moduleRight.put("name", moduleName);
    moduleRight.put("parentId", parentId);
    moduleMap.put(moduleId, moduleRight);
  }

  public boolean setModuleRight(Integer moduleId, Integer right)
  {
    HashMap moduleRight = (HashMap)moduleMap.get(moduleId);
    if (moduleMap == null || moduleRight == null) {
      return false;
    }
    moduleRight.put("rights", right);
    return true;
  }

  public boolean setVisibleModule(String moduleName)
  {
    if (moduleMap == null) {
      return false;
    }
    Set ids = moduleMap.keySet();
    Iterator moduleId = ids.iterator();
    while (moduleId.hasNext()) {
      HashMap moduleInfo = (HashMap)moduleMap.get((Integer)moduleId.next());
      String name = (String)moduleInfo.get("name");
      if (moduleName.equals(name)) {
        moduleInfo.put("rights", new Integer(DELETE_RIGHT));
        return true;
      }
    }
    return false;
  }

  // Returns true if an entry for the module exists in the moduleRight vector
  public boolean isModuleVisible(String moduleName)
  {
    if (moduleMap != null) {
      Collection moduleRights = moduleMap.values();
      Iterator iter = moduleRights.iterator();
      while (iter.hasNext()) {
        HashMap module = (HashMap)iter.next();
        String name = (String)module.get("name");
        Integer rightInteger = (Integer)module.get("rights");
        int right = (rightInteger != null) ? rightInteger.intValue() : NONE_RIGHT;
        if (name.equals(moduleName) && (right != NONE_RIGHT)) {
          return true;
        }
      }
      return moduleRights.contains(moduleName);
    }
    return false;
  }

  public TreeMap getModuleRights()
  {
    return moduleMap;
  }

  // added in the fieldRights HashMap
  public void setFieldRight(String moduleName, String fieldName, int right)
  {
    if (fieldRights == null) {
      fieldRights = new HashMap();
    }

    if (fieldRights.containsKey(moduleName)) {
      HashMap fldRightHm = (HashMap)fieldRights.get(moduleName);

      if (fldRightHm == null) {
        fldRightHm = new HashMap();
      }

      fldRightHm.put(fieldName, new Integer(right));
    } else {
      HashMap fldRightHm = new HashMap();
      fldRightHm.put(fieldName, new Integer(right));
      fieldRights.put(moduleName, fldRightHm);
    }

  }


  public HashMap getFieldRights(String moduleName)
  {
    if (fieldRights != null) {
      return (HashMap)fieldRights.get(moduleName);
    } else {
      return null;
    }
  }

  public HashMap getFieldRightsMap()
  {
    return fieldRights;
  }

  // Returns true if an entry for the module exists in the moduleRight vector
  // and an entry for the field exists in the inner hashmap
  // and thefield has update right
  public boolean isFieldUpdate(String moduleName, String fieldName)
  {
    return checkFieldRight(moduleName, fieldName, UPDATE_RIGHT);
  }

  // Returns true if an entry for the module exists in the moduleRight vector
  // and an entry for the field exists in the inner hashmap
  // and thefield has view right
  public boolean isFieldView(String moduleName, String fieldName)
  {
    return checkFieldRight(moduleName, fieldName, VIEW_RIGHT);
  }

  // Returns true if an entry for the module exists in the moduleRight vector
  // and an entry for the field exists in the inner hashmap
  // and thefield has None right
  public boolean isFieldNone(String moduleName, String fieldName)
  {
    return checkFieldRight(moduleName, fieldName, NONE_RIGHT);
  }

  // Returns true if an entry for the module exists in the moduleRight vector
  // and an entry for the field exists in the inner hashmap
  // and thefield has update or view right or entry not fouond
  public boolean isFieldVisible(String moduleName, String fieldName)
  {
    return checkFieldRight(moduleName, fieldName, VIEW_RIGHT);
  }


  public int getFieldRight(String moduleName, String fieldName)
  {
    int retVal = 0;
    if (fieldRights != null) {
      if (fieldRights.containsKey(moduleName)) {
        HashMap flr = (HashMap)fieldRights.get(moduleName);
        if (flr.containsKey(fieldName)) {
          // delete right consider as update
          retVal = ((Integer)flr.get(fieldName)).intValue();
        } else {
          retVal = 0;
        }
      } else {
        retVal = 0;
      }
    } else {
      retVal = 0;
    }
    return retVal;
  }   // end getFieldRight() method


  private boolean checkFieldRight(String moduleName, String fieldName, int checkVal)
  {
    boolean retVal = false;
    if (fieldRights != null) {
      if (fieldRights.containsKey(moduleName)) {
        HashMap flr = (HashMap)fieldRights.get(moduleName);
        if (flr.containsKey(fieldName)) {
          // delete right consider as update
          retVal = (((Integer)flr.get(fieldName)).intValue() <= checkVal);
        } else {
          retVal = true;
        }
      } else {
        retVal = false;
      }
    } else {
      if (this.isModuleVisible(moduleName)) {
        retVal = true;
      } else {
        retVal = false;
      }
    }
    return retVal;
  }   // end checkFieldRight() method

  public String toString()
  {
    StringBuffer toReturn = new StringBuffer();
    toReturn.append("    moduleRights = " + this.moduleMap + "\n");
    toReturn.append("    fieldRights = " + this.fieldRights + "\n");
    return(toReturn.toString());
  }   // end toString() method

}   // end class definition

