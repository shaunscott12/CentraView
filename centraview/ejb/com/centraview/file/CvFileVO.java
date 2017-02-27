/*
 * $RCSfile: CvFileVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:24 $ - $Author: mking_cv $
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


/*
 * CvFileVO.java
 *
 * @author   Iqbal Khan
 * @version  1.0
 *
 */



package com.centraview.file;

import java.sql.Timestamp;
import java.util.Vector;

import com.centraview.common.CVAudit;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.individual.IndividualVO;


// This class stores the value object of Files

public class CvFileVO extends CVAudit
{
	public static final String FS_DRAFT = "DRAFT";
	public static final String FS_PUBLISH = "PUBLISHED";

	public static final String FV_PUBLIC = "PUBLIC";
	public static final String FV_PRIVATE = "PRIVATE";

	public static final String FCV_YES = "YES";
	public static final String FCV_NO = "NO";

	public static final String FIT_YES = "YES";
	public static final String FIT_NO = "NO";


	public static final String FP_PHYSICAL = "PHYSICAL";
	public static final String FP_VIRTUAL = "VIRTUAL";
	public static final int DEFAULT_LOCATION = 1;
	public static final String DEFAULT_VERSION = "1.0";


	private int fileId;
	private String name;

	private String parentname;

	private String title;
	private String description;
	//private int locationId;
	//private String locationName;
	private float fileSize;
	private String version;
	private String status;
	private String visibility = FV_PRIVATE;
	private String physical;

	private Vector virtualFolderVO = null; //cause one file can be refered from many folders

	private int physicalFolder;
	private CvFolderVO physicalFolderVO;

	private int authorId;
	private IndividualVO authorVO;

	private int relateEntity;
	private EntityVO relateEntityVO;

	private int relateIndividual;
	private IndividualVO relateIndividualVO;
	private String companyNews;
	private Timestamp from;
	private Timestamp to;
	// used speciallly for email attachment
	// cause in attachment file is first uploaded then
	// email is sent. so if the email fails then a daemon can delete this file
	private String isTemporary = FIT_NO;

	private String customerView = FCV_NO;


  /** The ID of the type of the item related to this File. */
  protected int relatedTypeID;

  /** The value of the type of the item related to this File. */
  protected String relatedTypeValue;

  /** The ID of the item related to this File. */
  protected int relatedFieldID;

  /** The value of the item related to this File. */
  protected String relatedFieldValue;

	/**
	 * Constructor
	 *
	 */
	public CvFileVO()
	{
		super();
	}



	/**
	 * gets Description
	 *
	 * @return    String
	 */
	public String getDescription()
	{
		return this.description;
	}

        /**
         * gets from
         *
         * @return    Timestamp
         */
        public Timestamp getFrom()
        {
                return this.from;
        }
        /**
         * sets from
         *
         * @param   from
         */
        public void setFrom(Timestamp from)
        {
                this.from = from;
        }

        /**
         * gets to
         *
         * @return    Timestamp
         */
        public Timestamp getTo()
        {
                return this.to;
        }
        /**
         * sets to
         *
         * @param   to
         */
        public void setTo(Timestamp to)
        {
                this.to = to;
        }



        /**
         * gets CompanyNews
         *
         * @return    String
         */
        public String getCompanyNews()
        {
                return this.companyNews;
        }

        /**
         * sets CompanyNews
         *
         * @param   companyNews
         */
        public void setCompanyNews(String companyNews)
        {
                this.companyNews = companyNews;
        }


	/**
	 * sets Description
	 *
	 * @param   description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}



	/**
	 * gets ParentName
	 *
	 * @return    String
	 */
	public String getParentName()
	{
		return this.parentname;
	}


	/**
	 * sets ParentName
	 *
	 * @param   parentname
	 */
	public void setParentName(String parentname)
	{
		this.parentname = parentname;
	}



	/**
	 * gets File Id
	 *
	 * @return  int
	 */
	public int getFileId()
	{
		return this.fileId;
	}


	/**
	 * sets File Id
	 *
	 * @param   fileId
	 */
	public void setFileId(int fileId)
	{
		this.fileId = fileId;
	}



	/**
	 * gets File Size
	 *
	 * @return     float
	 */
	public float getFileSize()
	{
		return this.fileSize;
	}


	/**
	 * sets File Size
	 *
	 * @param   fileSize
	 */
	public void setFileSize(float fileSize)
	{
		this.fileSize = fileSize;
	}

	/* moved to folder cause physical folders are created
	public int getLocationId()
	{
		return this.locationId;
	}

	public void setLocationId(int locationId)
	{
		this.locationId = locationId;
	}


	public String getLocationName()
	{
		return this.locationName;
	}

	public void setLocationName(String locationName)
	{
		this.locationName = locationName;
	}
	*/


	/**
	 * gets name
	 *
	 * @return     String
	 */
	public String getName()
	{
		return this.name;
	}


	/**
	 * sets name
	 *
	 * @param   name
	 */
	public void setName(String name)
	{
		this.name = name;
	}



	/**
	 * gets Physical
	 *
	 * @return   String
	 */
	public String getPhysical()
	{
		return this.physical;
	}


	/**
	 * sets Physical
	 *
	 * @param   physical
	 */
	public void setPhysical(String physical)
	{
		//if(!(physical.equals(FP_PHYSICAL) || physical.equals(FP_VIRTUAL)))
		//	this.physical = FP_VIRTUAL;
		//else
			this.physical = physical;
	}




	/**
	 * gets status
	 *
	 * @return  String
	 */
	public String getStatus()
	{
		return this.status;
	}


	/**
	 * sets Status
	 *
	 * @param   status
	 */
	public void setStatus(String status)
	{
		this.status = status;
	}



	/**
	 * gets title
	 *
	 * @return   String
	 */
	public String getTitle()
	{
		return this.title;
	}


	/**
	 * sets title
	 *
	 * @param   title
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}



	/**
	 * gets version
	 *
	 * @return String
	 */
	public String getVersion()
	{
		return this.version;
	}


	/**
	 * sets version
	 *
	 * @param   version
	 */
	public void setVersion(String version)
	{
		this.version = version;
	}



	/**
	 * gets  Visibility
	 *
	 * @return     String
	 */
	public String getVisibility()
	{
		return this.visibility;
	}


	/**
	 * sets  Visibility
	 *
	 * @param   visibility
	 */
	public void setVisibility(String visibility)
	{
		if(visibility == null || !(visibility.equals(FV_PRIVATE) || visibility.equals(FV_PUBLIC)))
			this.visibility = FV_PRIVATE;
		else
			this.visibility = visibility;
	}


	/**
	 * gets the VirtualFolderVO
	 *
	 * @return   Vector
	 */
	public Vector getVirtualFolderVO()
	{
		return this.virtualFolderVO;
	}


	/**
	 * sets the VirtualFolderVO
	 *
	 * @param   folderVo
	 */
	public void setVirtualFolderVO(CvFolderVO folderVo)
	{
		if(this.virtualFolderVO == null)
			this.virtualFolderVO = new Vector();

		this.virtualFolderVO.add(folderVo);
	}



	/**
	 * get the Author Id
	 *
	 * @return   int
	 */
	public int getAuthorId()
	{
		return this.authorId;
	}


	/**
	 *
	 * sets the Author ID
	 * @param   authorId
	 */
	public void setAuthorId(int authorId)
	{
		this.authorId = authorId;
	}



	/**
	 * get the VO of author
	 *
	 * @return  IndividualVO
	 */
	public IndividualVO getAuthorVO()
	{
		return this.authorVO;
	}


	/**
	 * sets the Author VO
	 *
	 * @param   authorVO
	 */
	public void setAuthorVO(IndividualVO authorVO)
	{
		this.authorVO = authorVO;
	}



	/**
	 * gets the Physical Folder
	 *
	 * @return   int
	 */
	public int getPhysicalFolder()
	{
		return this.physicalFolder;
	}


	/**
	 * sets the Physical Folder
	 *
	 * @param   physicalFolder
	 */
	public void setPhysicalFolder(int physicalFolder)
	{
		this.physicalFolder = physicalFolder;
	}



	/**
	 * gets the PhysicalFolderVO
	 *
	 * @return  CvFolderVO
	 */
	public CvFolderVO getPhysicalFolderVO()
	{
		return this.physicalFolderVO;
	}


	/**
	 * sets the physicalFolderVO
	 *
	 * @param   physicalFolderVO
	 */
	public void setPhysicalFolderVO(CvFolderVO physicalFolderVO)
	{
		this.physicalFolderVO = physicalFolderVO;
	}



	/**
	 * gets the RelateEntity
	 *
	 * @return  int
	 */
	public int getRelateEntity()
	{
		return this.relateEntity;
	}


	/**
	 * sets the RelateEntity
	 *
	 * @param   relateEntity
	 */
	public void setRelateEntity(int relateEntity)
	{
		this.relateEntity = relateEntity;
	}



	/**
	 * gets the EntityVO
	 *
	 * @return   EntityVO
	 */
	public EntityVO getRelateEntityVO()
	{
		return this.relateEntityVO;
	}


	/**
	 * sets the RelateEntityVO
	 *
	 * @param   relateEntityVO
	 */
	public void setRelateEntityVO(EntityVO relateEntityVO)
	{
		this.relateEntityVO = relateEntityVO;
	}



	/**
	 * gets the RelateIndividual
	 *
	 * @return int
	 */
	public int getRelateIndividual()
	{
		return this.relateIndividual;
	}


	/**
	 * sets the RelateIndividual
	 *
	 * @param   relateIndividual
	 */
	public void setRelateIndividual(int relateIndividual)
	{
		this.relateIndividual = relateIndividual;
	}



	/**
	 * get the IndividualVO
	 *
	 * @return    IndividualVO
	 */
	public IndividualVO getRelateIndividualVO()
	{
		return this.relateIndividualVO;
	}


	/**
	 * sets RelateIndividualVO
	 *
	 * @param   relateIndividualVO
	 */
	public void setRelateIndividualVO(IndividualVO relateIndividualVO)
	{
		this.relateIndividualVO = relateIndividualVO;
	}



	/**
	 * gets IsTemporary
	 *
	 * @return     String
	 */
	public String getIsTemporary()
	{
		return this.isTemporary;
	}


	/**
	 * sets the IsTemporary field
	 *
	 * @param   isTemporary
	 */
	public void setIsTemporary(String isTemporary)
	{
		if(isTemporary == null || !(isTemporary.equals(FIT_YES) || isTemporary.equals(FIT_NO)))
			this.isTemporary = FIT_NO;
		else
			this.isTemporary = isTemporary;
	}



	public String getCustomerView()
	{
		return this.customerView;
	}

	public void setCustomerView(String customerView)
	{
		if(customerView == null || !(customerView.equals(FCV_YES) || customerView.equals(FCV_NO)))
			this.customerView = FCV_NO;
		else
			this.customerView = customerView;
	}


  /**
   * Sets the RelatedTypeValue for this File.
   *
   * @param relatedTypeValue The new RelatedTypeValue for the File.
   */
  public void setRelatedTypeValue(String relatedTypeValue)
  {
    this.relatedTypeValue = relatedTypeValue;
  } //end of setRelatedTypeValue method

  /**
   * Returns the RelatedTypeValue for this File.
   *
   * @return The RelatedTypeValue for the File.
   */
  public String getRelatedTypeValue()
  {
    return this.relatedTypeValue;
  } //end of getRelatedTypeValue method

  /**
   * Sets the RelatedTypeID for this File.
   *
   * @param relatedTypeID The new RelatedTypeID for the File.
   */
  public void setRelatedTypeID(int relatedTypeID)
  {
    this.relatedTypeID = relatedTypeID;
  } //end of setProjectID method

  /**
   * Returns the RelatedTypeID for this File.
   *
   * @return The RelatedTypeID for the File.
   */
  public int getRelatedTypeID()
  {
    return this.relatedTypeID;
  } //end of getRelatedTypeID method

  /**
   * Sets the RelatedFieldValue for this File.
   *
   * @param relatedFieldValue The new RelatedFieldValue for the File.
   */
  public void setRelatedFieldValue(String relatedFieldValue)
  {
    this.relatedFieldValue = relatedFieldValue;
  } //end of setRelatedFieldValue method

  /**
   * Returns the RelatedFieldValue for this File.
   *
   * @return The RelatedFieldValue for the File.
   */
  public String getRelatedFieldValue()
  {
    return this.relatedFieldValue;
  } //end of getRelatedFieldValue method

  /**
   * Sets the RelatedFieldID for this File.
   *
   * @param relatedFieldID The new RelatedFieldID for the File.
   */
  public void setRelatedFieldID(int relatedFieldID)
  {
    this.relatedFieldID = relatedFieldID;
  } //end of setRelatedFieldID method

  /**
   * Returns the RelatedFieldID for this File.
   *
   * @return The RelatedFieldID for the File.
   */
  public int getRelatedFieldID()
  {
    return this.relatedFieldID;
  } //end of getRelatedFieldID method

  public String toString()
  {
    StringBuffer string = new StringBuffer();
    string.append("CVFileVO: {fileTitle: "+this.title);
    string.append(", fileID: "+this.fileId);
    string.append(", fileName: "+this.name+"}");
    return string.toString();
  }
}
