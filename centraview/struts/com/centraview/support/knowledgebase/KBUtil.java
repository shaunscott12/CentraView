/*
 * $RCSfile: KBUtil.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:50 $ - $Author: mking_cv $
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

package com.centraview.support.knowledgebase;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.struts.action.Action;

import com.centraview.common.DDNameValue;

/** 
 * Utility class for knowledgebase.
 * 
 * Created: Nov 9, 2004
 * 
 * @author CentraView, LLC. 
 */
public class KBUtil extends Action {
  public static void processCategory(ArrayList catList, CategoryVO catVO, ArrayList finalList, int level) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i != level; i++)
      sb.append("-");
    sb.append(catVO.getTitle());
    level++;
    
    finalList.add(new DDNameValue(catVO.getCatid(), sb.toString()));
    
    Iterator iter = catList.iterator();
    while (iter.hasNext()) {
      CategoryVO tmp = (CategoryVO)iter.next();
      if (tmp.getParent() == catVO.getCatid())
        KBUtil.processCategory(catList, tmp, finalList, level);
    }
  }
}
