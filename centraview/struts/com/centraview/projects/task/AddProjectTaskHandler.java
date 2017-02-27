/*
 * $RCSfile: AddProjectTaskHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:30 $ - $Author: mking_cv $
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

package com.centraview.projects.task;

import java.io.IOException;
import java.util.Vector;

import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.centraview.common.CVUtility;
import com.centraview.common.ListGenerator;
import com.centraview.common.UserObject;
import com.centraview.projects.helper.CustomFieldVO;
import com.centraview.projects.helper.TaskVO;
import com.centraview.projects.helper.TaskVOX;
import com.centraview.projects.projectfacade.ProjectFacade;
import com.centraview.projects.projectfacade.ProjectFacadeHome;
import com.centraview.settings.Settings;

/**
 * This Handler handles form request for new project.
 *
*/
public final class AddProjectTaskHandler extends Action
{

  String currentTZ = null;

  /**
  * Execute method
  * @param mapping ActionMapping
  * @param form ActionForm
  */
  public ActionForward execute(ActionMapping mapping, ActionForm form,
    HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException, CommunicationException,NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    String saveandclose = (String) request.getParameter("saveandclose");
    String saveandnew = (String) request.getParameter("saveandnew");
    String returnStatus = "failure";

    HttpSession session = request.getSession(true);
    UserObject userobject = (UserObject) session.getAttribute("userobject");

    int indvID = 0;

    if (userobject != null)
    {
      indvID = userobject.getIndividualID();
      currentTZ = userobject.getUserPref().getTimeZone();
    }

    if ((saveandclose != null))
    {
      if (saveForm(indvID, form, request, response))
      {
        returnStatus = ".forward.close_window";
      }

      request.setAttribute("closeWindow", "true");
      request.setAttribute("refreshWindow", "true");
    }
    else if ((saveandnew != null))
    {
      if (saveForm(indvID, form, request, response))
      {
        returnStatus = ".view.projects.new.task";
      }

      clearForm((TaskForm) form, mapping);
      request.setAttribute("refreshWindow", "true");
    }
    return (mapping.findForward(returnStatus));
  }

  public boolean saveForm(int indvID, ActionForm form,
    HttpServletRequest request, HttpServletResponse response) throws CommunicationException,NamingException
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    TaskVOX taskVOX = new TaskVOX(currentTZ, form);
    TaskVO taskVO = taskVOX.getValueObject();

    Vector customFieldVec = getCustomFieldVO(request, response);
    taskVO.setCustomFieldVOs(customFieldVec);

    ProjectFacadeHome pfh = (ProjectFacadeHome)
      CVUtility.getHomeObject("com.centraview.projects.projectfacade.ProjectFacadeHome", "ProjectFacade");

    try
    {
      ProjectFacade remote = (ProjectFacade) pfh.create();
      remote.setDataSource(dataSource);
      int rowID = remote.addProjectTask(indvID, taskVO);
      request.setAttribute("rowID", rowID + "");
      ListGenerator lg = ListGenerator.getListGenerator(dataSource);
      lg.makeListDirty("Task");

      return true;
    }
    catch (Exception e)
    {
      System.out.println("[Exception] AddProjectTaskHandler.saveForm: " + e.toString());
      //e.printStackTrace();
    }

    return false;
  }

  public void clearForm(TaskForm form, ActionMapping mapping)
  {
    form.initialize();
  }

  public Vector getCustomFieldVO(HttpServletRequest request,
    HttpServletResponse response)
  {
    Vector vec = new Vector();

    for (int i = 1; i < 4; i++)
    {
      String fieldid = request.getParameter("fieldid" + i);
      String fieldType = request.getParameter("fieldType" + i);
      String textValue = request.getParameter("text" + i);

      if (fieldid == null)
      {
        fieldid = "0";
      }

      int intfieldId = Integer.parseInt(fieldid);
      CustomFieldVO cfvo = new CustomFieldVO();
      cfvo.setFieldID(intfieldId);
      cfvo.setFieldType(fieldType);
      cfvo.setValue(textValue);
      vec.add(cfvo);
    }

    return vec;
  } // end of getCustomFieldVO
}
