/*
 * $RCSfile: CompanyNewsVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:19 $ - $Author: mking_cv $
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
 * CompanyNewsVO.java
 *
 * @author   Maksim Mostovnikov 24/01/2004
 * @version  1.0
 *
 */

package com.centraview.file;

import java.io.Serializable;

// This class stores the value object of Company News

public class CompanyNewsVO implements Serializable {


	private int fileID;

	private String description;

	private String title;

        /**
        * Constructor
        *
        */

        public CompanyNewsVO()
        {
          super();
        }

        /**
         * Constructor
         * @param fileID int
         * @param description String
         * @param title String
         */
        public CompanyNewsVO(int fileID,String description,String title)
        {
          this.fileID = fileID;
          this.title = title;
          this.description = description;
        }


	/**
	*
	* @return int
	*/
	public int getFileID()
	{
          return this.fileID;
	}
	/**
	*
	* @param fileID int
	*/

	public void setFileID(int fileID)
	{
          this.fileID = fileID;
	}
        /**
         *
         * @return String
         */
        public String getDescription()
	{
          return this.description;
	}
        /**
         *
         * @param description String
         */
        public void setDescription(String description)
	{
          this.description = description;
	}
        /**
         *
         * @return String
         */
        public String getTitle()
	{
          return this.title;
	}
        /**
         *
         * @param title String
         */
        public void setTitle(String title)
	{
          this.title = title;
	}
}
