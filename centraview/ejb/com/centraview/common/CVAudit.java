/*
 * $RCSfile: CVAudit.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:53 $ - $Author: mking_cv $
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


package com.centraview.common;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.ejb.FinderException;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import com.centraview.contact.individual.IndividualLocal;
import com.centraview.contact.individual.IndividualLocalHome;
import com.centraview.contact.individual.IndividualPK;
import com.centraview.contact.individual.IndividualVO;

public class CVAudit implements Serializable
{
  private static Logger logger = Logger.getLogger(CVAudit.class);
  private int modifiedBy;
  private Timestamp modifiedOn;
  private int createdBy;
  private Timestamp createdOn;
  private int owner;
  private IndividualVO modifiedByVO;
  private IndividualVO CreatedByVO;
  private IndividualVO ownerVO;

  public void fillAuditDetails(String dataSource)
  {
    // This method fills in the oft standard Owner, Modifier, Creator details
    try
    {
      InitialContext ic = CVUtility.getInitialContext();
      IndividualLocalHome home = (IndividualLocalHome)ic.lookup("local/Individual");
      IndividualLocal remote;
      try
      {
        remote = home.findByPrimaryKey(new IndividualPK(this.owner, dataSource));
        this.setOwnerVO(remote.getIndividualVOBasic());
      } catch (FinderException e) {
        logger.debug("[fillAuditDetails] FinderException finding owner ID: "+this.owner);
      } catch (Exception e) {
        logger.error("[fillAuditDetails] Exception thrown.", e);
      }

      try
      {
        remote = home.findByPrimaryKey(new IndividualPK(this.createdBy, dataSource));
        this.setCreatedByVO(remote.getIndividualVOBasic());
      } catch (FinderException e) {
        logger.debug("[fillAuditDetails] FinderException finding creator ID: "+this.createdBy);
      } catch (Exception e) {
        logger.error("[fillAuditDetails] Exception thrown.", e);
      }

      try
      {
        remote = home.findByPrimaryKey(new IndividualPK(this.modifiedBy, dataSource));
        this.setModifiedByVO(remote.getIndividualVOBasic());
      } catch (FinderException e) {
        logger.debug("[fillAuditDetails] FinderException finding ModifiedBy ID: "+this.modifiedBy);
      } catch (Exception e) {
        logger.error("[fillAuditDetails] Exception thrown.", e);
      }
    } catch (Exception e) {
      logger.error("[fillAuditDetails] Exception thrown.", e);
    }
  }

  public int getCreatedBy()
  {
    return this.createdBy;
  }

  public void setCreatedBy(int createdBy)
  {
    this.createdBy = createdBy;
  }

  public IndividualVO getCreatedByVO()
  {
    return this.CreatedByVO;
  }

  public void setCreatedByVO(IndividualVO CreatedByVO)
  {
    this.CreatedByVO = CreatedByVO;
  }

  public Timestamp getCreatedOn()
  {
    return this.createdOn;
  }

  public void setCreatedOn(Timestamp createdOn)
  {
    this.createdOn = createdOn;
  }

  public int getModifiedBy()
  {
    return this.modifiedBy;
  }

  public void setModifiedBy(int modifiedBy)
  {
    this.modifiedBy = modifiedBy;
  }

  public IndividualVO getModifiedByVO()
  {
    return this.modifiedByVO;
  }

  public void setModifiedByVO(IndividualVO modifiedByVO)
  {
    this.modifiedByVO = modifiedByVO;
  }

  public Timestamp getModifiedOn()
  {
    return this.modifiedOn;
  }

  public void setModifiedOn(Timestamp modifiedOn)
  {
    this.modifiedOn = modifiedOn;
  }

  public int getOwner()
  {
    return this.owner;
  }

  public void setOwner(int owner)
  {
    this.owner = owner;
  }

  public IndividualVO getOwnerVO()
  {
    return this.ownerVO;
  }

  public void setOwnerVO(IndividualVO ownerVO)
  {
    this.ownerVO = ownerVO;
  }
}
