/*
 * $RCSfile: SourceVOX.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:21:59 $ - $Author: mking_cv $
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

package com.centraview.common.source;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;

/**
 * @author 
 */
public class SourceVOX extends SourceVO
{
  public SourceVOX()
  {
    //System.out.println("In Source VOX Constructor");
  }

  /**
   *  In this constructor set the Source Object from DynaAction form by getting values.
   *
   * @param   form  Action Form
   */
  public SourceVOX(ActionForm form)
  {
    DynaActionForm dynaForm = (DynaActionForm) form;
    setName((String) dynaForm.get("SourceName"));
    String id = (String) dynaForm.get("SourceID");

    if (id == null)
    {
      id = "-1";
    }

    setSourceId(Integer.parseInt(id));
  } // end of construtor

  /**
   *  In this constructor set the Source Object from DynaAction form by getting values.
   *
   * @param   form  Action Form,HttpServletRequest request
   */
  public SourceVOX(ActionForm form, HttpServletRequest request)
  {
    DynaActionForm dynaForm = (DynaActionForm) form;
    setName((String) dynaForm.get("SourceName"));
    String id = (String) dynaForm.get("SourceID");

    if (id == null)
    {
      id = "-1";
    }

    setSourceId(Integer.parseInt(id));
  } // end of construtor  SourceVOX(ActionForm form,HttpServletRequest request)

  public SourceVO getVO()
  {
    return super.getVO();
  }
}
