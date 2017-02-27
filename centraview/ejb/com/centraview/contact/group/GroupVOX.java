/*
 * $RCSfile: GroupVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:06 $ - $Author: mking_cv $
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


package com.centraview.contact.group;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;

public class GroupVOX extends GroupVO
{
  /**
   * In this constructor the Populate Group Value Object.
   * @param form ActionForm
   */
  public GroupVOX(ActionForm form, int individualId)
  {
    DynaActionForm dynaForm = (DynaActionForm)form;
    if (dynaForm.get("groupid") != null) {
      setGroupID(Integer.parseInt((String)dynaForm.get("groupid")));
    }
    setDescription((String)dynaForm.get("groupdescription"));
    setGroupName((String)dynaForm.get("groupname"));
    String owner = (String)dynaForm.get("owner");
    if (owner == null || owner.equals("")) {
      owner = String.valueOf(individualId);
    }
    setOwner(Integer.parseInt(owner));
  }

  /**
   * In this constructor the Populate Group Value Object.
   * 
   * @param form ActionForm
   * @param form HttpServletRequest
   */
  public GroupVOX(ActionForm form, HttpServletRequest request)
  {
    try {
      DynaActionForm dynaForm = (DynaActionForm)form;
      HttpSession session = request.getSession(true);
      UserObject userobject = (UserObject)session.getAttribute("userobject");
      ModuleFieldRightMatrix mfrmx = ((UserObject)session.getAttribute("userobject")).getUserPref().getModuleAuthorizationMatrix();
      HashMap groupFieldRights = mfrmx.getFieldRights("Group");
      int individualId = userobject.getIndividualID();
      ContactFacadeHome aa = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade remote = (ContactFacade)aa.create();
      int groupId = 0;
      if (dynaForm.get("groupid") != null) {
        groupId = Integer.parseInt((String)dynaForm.get("groupid"));
        this.setGroupID(groupId);
      }
      GroupVO groupVOReference = remote.getGroup(individualId, groupId);

      int fieldRight = 0;

      if (groupFieldRights != null && groupFieldRights.containsKey("groupdescription")) {
        fieldRight = ((Number)groupFieldRights.get("groupdescription")).intValue();
        if (fieldRight == ModuleFieldRightMatrix.NONE_RIGHT || fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT) {
          this.setDescription(groupVOReference.getDescription());
        } else {
          this.setDescription((String)dynaForm.get("groupdescription"));
        }
      }
      fieldRight = 0;
      if (groupFieldRights != null && groupFieldRights.containsKey("groupname")) {
        fieldRight = ((Number)groupFieldRights.get("groupname")).intValue();
        if (fieldRight == ModuleFieldRightMatrix.NONE_RIGHT || fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT) {
          this.setGroupName(groupVOReference.getGroupName());
        } else {
          this.setGroupName((String)dynaForm.get("groupname"));
        }
      }
      fieldRight = 0;
      if (groupFieldRights != null && groupFieldRights.containsKey("owner")) {
        fieldRight = ((Number)groupFieldRights.get("owner")).intValue();
        if (fieldRight == ModuleFieldRightMatrix.NONE_RIGHT || fieldRight == ModuleFieldRightMatrix.VIEW_RIGHT) {
          this.setOwner(groupVOReference.getOwner());
        } else {
          this.setOwner(Integer.parseInt((String)dynaForm.get("owner")));
        }
      }
    } catch (Exception e) {
      System.out.println("[Exception][GroupVOX.GroupVOX] Exception Thrown: " + e);
      e.printStackTrace();
    }
  }
  /**
   * Returns the GroupVO Object.
   *
   * @return The Group Value Object
   */
  public GroupVO getVO()
  {
    return super.getVO();
  }
} //end of GroupVOX class
