/*
 * $RCSfile: Rule.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:27:51 $ - $Author: mking_cv $
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
package com.centraview.email;
import java.util.StringTokenizer;
import java.util.Vector;
public class Rule extends Vector
{
 private int action_type;
 private int action_folder;
 private Vector action_condition = new Vector();
 public Rule(String rule ,String  action)
 {

   StringTokenizer st = new  StringTokenizer(rule,"<");

  //Rule
   while (st.hasMoreTokens())
   {
   	 RuleObj ruleobj = new RuleObj();
     ruleobj.setJoin(Integer.parseInt(st.nextToken()));
	 ruleobj.setField(Integer.parseInt(st.nextToken()));
	 ruleobj.setCondition(Integer.parseInt(st.nextToken()));
	 ruleobj.setCriteria(st.nextToken());
	 action_condition.add(ruleobj);
   }


  // Action
   st = new  StringTokenizer(action,"<");
   while (st.hasMoreTokens())
   {
    action_type = Integer.parseInt(st.nextToken());
	action_folder = Integer.parseInt(st.nextToken());
   }

 }

 public Vector getActionConditions()
 {
   return action_condition;
 }

 public int getActiontype()
 {
   return action_type;
 }

 public int getActionFolderID()
 {
   return action_folder;
 }


}