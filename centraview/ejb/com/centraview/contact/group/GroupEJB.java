/*
 * $RCSfile: GroupEJB.java,v $    $Revision: 1.2 $  $Date: 2005/07/25 13:23:52 $ - $Author: mcallist $
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;

public class GroupEJB implements SessionBean
{
  private static Logger logger = Logger.getLogger(GroupEJB.class);
  protected javax.ejb.SessionContext ctx;
  protected Context environment;
  private String dataSource = "";

  public GroupEJB()
  {

  }
  /**
   * Called by the container to create a session bean instance. Its parameters
   * typically contain the information the client uses to customize the bean
   * instance for its use. It requires a matching pair in the bean class and its
   * home interface.
   */
  public void ejbCreate()
  {

  }

  /**
   * A container invokes this method before it ends the life of the session
   * object. This happens as a result of a client's invoking a remove operation,
   * or when a container decides to terminate the session object after a
   * timeout. This method is called with no transaction context.
   */

  public void ejbRemove()
  {

  }

  /**
   * The activate method is called when the instance is activated from its
   * 'passive' state. The instance should acquire any resource that it has
   * released earlier in the ejbPassivate() method. This method is called with
   * no transaction context.
   */
  public void ejbActivate()
  {

  }

  /**
   * The passivate method is called before the instance enters the 'passive'
   * state. The instance should release any resources that it can re-acquire
   * later in the ejbActivate() method. After the passivate method completes,
   * the instance must be in a state that allows the container to use the Java
   * Serialization protocol to externalize and store away the instance's state.
   * This method is called with no transaction context.
   */
  public void ejbPassivate()
  {

  }

  /*
   * Set the associated session context. The container calls this method after
   * the instance creation. The enterprise Bean instance should store the
   * reference to the context object in an instance variable. This method is
   * called with no transaction context.
   */

  public void setSessionContext(SessionContext ctx)
  {
    this.ctx = ctx;

  }

  public Vector getGroupMemberIDs(int userID, int groupId)
  {
    Vector v = new Vector();
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("contact.listgroupmembers");
      dl.setInt(1, groupId);
      Collection col = dl.executeQuery();
      Iterator it = col.iterator();
      while (it.hasNext()) {
        HashMap hm = (HashMap)it.next();
        v.addElement(hm.get("childid"));
      }
    } catch (Exception e) {
      logger.error("[getGroupMemberIDs]: Exception", e);
    } finally {
      dl.destroy();
    }
    return v;

  }

  /**
   * In this method Group Member is added to Perticular Group
   * 
   * @param userId Login UserID
   * @param contactId int array containing ContactID which is to be added in the
   *          Group.
   * @param groupId GroupID.
   */
  public void addContactToGroup(int userId, int[] contactId, int groupId)
  {
    CVDal dl = new CVDal(dataSource);
    try {
      for (int i = 0; i < contactId.length; i++) {
        dl.setSql("contact.createmember");
        dl.setInt(1, groupId);
        dl.setInt(2, contactId[i]);
        dl.executeUpdate();
        dl.clearParameters();
      }
    } catch (Exception e) {
      logger.error("[addContactToGroup]: Exception", e);
    } finally {
      dl.destroy();
    }
  }// end of addContactToGroup

  /**
   * Not Implemented.
   * 
   * @param userId Login User ID.
   * @param contactId contactID to be deleted.
   */
  public void deleteContactFromGroup(int[] contactId, int groupId)
  {
  //not implemented
  }

  /**
   * In this method the group and it's member are inserted into the DataBase.
   * 
   * @param groupDetail GroupValueObject
   */
  public int createGroup(int userId, GroupVO groupDetail)
  {
    CVDal dl = new CVDal(dataSource);
    int gid = 0;
    try {
      dl.setSqlQuery("INSERT INTO grouptbl(Name,description, owner, createDate, modifyDate) values(?,?,?,NOW(),NOW())");
      dl.setString(1, groupDetail.getGroupName());
      dl.setString(2, groupDetail.getDescription());
      dl.setInt(3, groupDetail.getOwner());
      dl.executeUpdate();
      gid = dl.getAutoGeneratedKey();
      // Everything is created now apply the default permissions for this user.
      InitialContext ic = CVUtility.getInitialContext();
      AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
      AuthorizationLocal authorizationLocal = authorizationHome.create();
      authorizationLocal.setDataSource(dataSource);
      authorizationLocal.saveCurrentDefaultPermission("Group", gid, userId);
    } catch (Exception e) {
      logger.error("[createGroup]: Exception", e);
    } finally {
      dl.destroy();
    }
    return gid;
  }// end of createGroup

  /**
   * In this method returns the Group Value Object for Passing groupId as
   * parameter.
   * 
   * @param groupId GroupID
   * @return GroupValue Object
   */
  public GroupVO getGroupDetails(int userId, int groupId)
  {
    GroupVO group = null;
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSqlQuery("select GroupID,Description,Name , owner , createDate ,modifyDate  from grouptbl where groupid = ?");
      dl.setInt(1, groupId);
      Collection col = dl.executeQuery();
      if (col != null) {
        HashMap hm = (HashMap)col.iterator().next();
        group = new GroupVO();
        group.setGroupID(((Long)hm.get("GroupID")).intValue());
        group.setGroupName((String)hm.get("Name"));
        group.setDescription((String)hm.get("Description"));
        Object ownerObject = hm.get("owner");
        if (ownerObject != null) {
          String newOwnerString = ownerObject.toString();
          group.setOwner(Integer.parseInt(newOwnerString));
        } //end of if statement (ownerObject != null)
        //group.setOwner(((Number) hm.get("owner")).intValue());
        group.setCreatedate((java.util.Date)hm.get("createDate"));
        group.setModifydate((java.util.Date)hm.get("modifyDate"));
        // Loop not Requireded as only one record
        // will be returned (IQ)
      }
    } catch (Exception e) {
      logger.error("[getGroupDetails]: Exception", e);
    } finally {
      dl.destroy();
    }
    return group;
  }// end of getGroupDetails

  public void updateGroup(int userId, GroupVO groupDetail)
  {
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSqlQuery("update grouptbl set name = ?  ,description = ? , owner = ? , modifyDate=concat(CURRENT_DATE,CURRENT_TIME)  where groupid = ? ");
      dl.setString(1, groupDetail.getGroupName());
      dl.setString(2, groupDetail.getDescription());
      dl.setInt(3, groupDetail.getOwner());
      dl.setInt(4, groupDetail.getGroupID());

      dl.executeUpdate();
    } catch (Exception e) {
      logger.error("[updateGroup]: Exception", e);
    } finally {
      dl.destroy();
    }
  }// end of updateGroup

  /**
   * This method delete the Group Member and Group from Following table 1)
   * Member 2) Grouptbl
   * 
   * groupId is the Group Id which is to be deleted.
   */
  public void deleteGroup(int groupId)
  {
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("contact.deletegroupmember");
      dl.setInt(1, groupId);
      dl.executeUpdate();
      dl.setSqlQueryToNull();
      dl.setSql("contact.deletegroup");
      dl.setInt(1, groupId);
      dl.executeUpdate();
    } catch (Exception e) {
      logger.error("[deleteGroup]: Exception", e);
    } finally {
      dl.destroy();
    }
  }

  public void deleteGroupMember(int groupid, int memberId)
  {
    CVDal dl = new CVDal(dataSource);
    try {
      dl.setSql("contact.deleteselectedgroupmember");
      dl.setInt(1, groupid);
      dl.setInt(2, memberId);
      dl.executeUpdate();
    } catch (Exception e) {
      logger.error("[deleteGroupMember]: Exception", e);
    } finally {
      dl.destroy();
    }
  }

  /**
   * Given a <b>groupID </b>, this method duplicates the group record in the
   * database, and also adds the origin group's members to the new group. Please
   * be sure that individualID is the ID of the <u>Individual </u> record of the
   * user who is creating this new record, <u>NOT </u> the userID.
   * 
   * @param individualID the individualID of the user who is creating the new
   *          group.
   * @param groupID the groupID orginal group which is to be duplicated.
   * @return int: newGroupID - the groupID of the newly created group.
   */
  public int duplicateGroup(int individualID, int groupID)
  {
    int newGroupID = 0; // method returns 0 upon failure
    try {
      // first, get the details of the group to be duplicated
      GroupVO originGroupVO = this.getGroupDetails(individualID, groupID);
      // next, create a new GroupVO with the same infomation
      // as the original group (except OwnerID)
      GroupVO newGroupVO = new GroupVO();
      newGroupVO.setGroupName(originGroupVO.getGroupName());
      newGroupVO.setDescription(originGroupVO.getDescription());
      newGroupVO.setOwner(individualID); // current user will own this group
      // now, create the new group
      newGroupID = this.createGroup(individualID, newGroupVO);
      if (newGroupID != 0) {
        // if we successfully created the group, then get the members
        // list from the original group
        Vector groupMembers = this.getGroupMemberIDs(individualID, groupID);

        if (groupMembers != null) {
          Iterator it = groupMembers.iterator();
          int memberArray[] = new int[groupMembers.size()];
          // then, loop through the members list and add each member to
          // the new group, one at a time.
          int i = 0;
          while (it.hasNext()) {
            memberArray[i] = ((Long)it.next()).intValue();
            i++;
          }
          this.addContactToGroup(individualID, memberArray, newGroupID);
        } // end if (groupMembers != null)
      } // end if (newGroupID != 0)
    } catch (Exception e) {
      logger.error("[duplicateGroup]: Exception", e);
    }
    return (newGroupID);
  } // end duplicateGroup() method

  /**
   * @author Kevin McAllister <kevin@centraview.com>This simply sets the target
   *         datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }
}