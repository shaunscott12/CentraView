/*
 * $RCSfile: ViewSecurityProfileHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:26:37 $ - $Author: mking_cv $
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import com.centraview.administration.common.AdministrationConstantKeys;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;

/**
 * ViewSecurityProfileHandler.java
 * @author CentraView, LLC.
 */
public class ViewSecurityProfileHandler extends Action
{
  public static final String GLOBAL_FORWARD_failure = "failure";
  private static final String FORWARD_view = ".view.administration.security_profile";
  private static String FORWARD_final = GLOBAL_FORWARD_failure;
  
  private int divCount = 0;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
  {
    String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    try {
      HashMap securityProfileMap;
      DynaActionForm securityProfileBean = (DynaActionForm)form;
      HttpSession session = request.getSession(true);
      UserObject userObject = (UserObject)session.getAttribute("userobject");
      int individualid = userObject.getIndividualID();
      ModuleFieldRightMatrix mfrm;

      int profileId = 0;

      // get profileid as rowid if rowid is null
      if (request.getParameter("rowId") != null) {
        profileId = Integer.parseInt(request.getParameter("rowId").toString());
      } else if (request.getParameter("profileId") != null) {
        profileId = Integer.parseInt(request.getParameter("profileId").toString());
      } else if (request.getAttribute("profileId") != null) {
        profileId = Integer.parseInt((String)request.getAttribute("profileId"));
      }
      
      securityProfileBean.set("profileid", Integer.toString(profileId));
      AuthorizationHome ah = (AuthorizationHome)CVUtility.getHomeObject("com.centraview.administration.authorization.AuthorizationHome", "Authorization");
      Authorization authEJB = (Authorization)ah.create();
      authEJB.setDataSource(dataSource);

      if (profileId != 0) {  
        // we should get it from the EJB layer and populate the formbean with
        // the contents of the matrix
        securityProfileMap = (HashMap)authEJB.getSecurityProfile(profileId);
        securityProfileBean.set("profilename", securityProfileMap.get("profilename"));

        mfrm = (ModuleFieldRightMatrix)securityProfileMap.get("modulefieldrightmatrix");
        this.populateFormBean(mfrm, securityProfileBean);
      } else { // if(profileId != 0) if it does equal Zero we are trying to do a New.
        // So get a blank one with full rights.  Let the user take some away.
        mfrm = authEJB.getBlankFieldRightMatrix(ModuleFieldRightMatrix.DELETE_RIGHT);
        this.populateFormBean(mfrm, securityProfileBean);
      }
      TreeMap moduleRightsTreeMap = mfrm.getModuleRights();
      HashMap fieldRightMap = mfrm.getFieldRightsMap();
      String dynamicHTML = generatePageHTML(moduleRightsTreeMap, fieldRightMap);

      request.setAttribute("dynamicHTML", dynamicHTML);
      request.setAttribute("dyna", securityProfileBean);
      request.setAttribute("Body", "view");  // we probably don't need this.
      request.setAttribute("typeofmodule", AdministrationConstantKeys.USERADMINISTRATION);
      request.setAttribute("typeofsubmodule", AdministrationConstantKeys.USERADMINISTRATION_SECURITY);

      FORWARD_final = FORWARD_view;
    } catch (Exception e) {
      System.out.println("[Exception][ViewSecurityProfileHandler.execute] Exception Thrown: " + e);
      e.printStackTrace();
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return mapping.findForward(FORWARD_final);
  }
  
  private void populateFormBean(ModuleFieldRightMatrix mfrm, DynaActionForm securityProfileBean)
  {
    // Step through the field rights and set them on the formbean.
    HashMap fieldRightMap = mfrm.getFieldRightsMap();
    if (fieldRightMap != null) {
      Set fieldHashKey = fieldRightMap.keySet();
      Iterator moduleIter = fieldHashKey.iterator();
      while (moduleIter.hasNext()) {
        String moduleName = (String)moduleIter.next();
        HashMap fields = (HashMap)fieldRightMap.get(moduleName);
        Set fieldKey = fields.keySet();
        Iterator fieldIter = fieldKey.iterator();
        while (fieldIter.hasNext()) {
          String fieldName = (String)fieldIter.next();
          int fieldRights = mfrm.getFieldRight(moduleName, fieldName);
          String formBeanKey = moduleName + "_" + fieldName;
          securityProfileBean.set(formBeanKey, String.valueOf(fieldRights));
        }
      }
    }
    
    TreeMap moduleRightsTreeMap = mfrm.getModuleRights();
    Set moduleIDs = moduleRightsTreeMap.keySet();
    Iterator moduleIterator = moduleIDs.iterator();
    
    Vector moduleRightsVector = new Vector();
    while (moduleIterator.hasNext()) {
      Number moduleID = (Number)moduleIterator.next();
      HashMap moduleRightsHashMap = (HashMap)moduleRightsTreeMap.get(moduleID);
      String moduleName = (String)moduleRightsHashMap.get("name");
      moduleRightsVector.add(moduleName);
    }
    String[] moduleRightsArray = new String[moduleRightsVector.size()];
    moduleRightsVector.toArray(moduleRightsArray);
    securityProfileBean.set("moduleright", moduleRightsArray);
  }

  /**
   * In the future... make this method recursive. This method 
   * goes through the moduleRights TreeMap and the 
   * module fields right tables and generates the HTML
   * necessary to display all of these modules and fields.
   * @param moduleRightsTreeMap TreeMap of the modules.
   * @param fieldRights HashMap of the module fields.
   * @return
   */
  private String generatePageHTML(TreeMap moduleRightsTreeMap, HashMap fieldRights)
  {
    StringBuffer sb = new StringBuffer();
    StringBuffer sbJS = new StringBuffer();
    
    sbJS.append("<script language=\"JavaScript1.2\">\nmodulesTree = {");

    if (moduleRightsTreeMap != null && fieldRights != null) {
      // To Iterate is human ...
      Set moduleIDs = moduleRightsTreeMap.keySet();
      Iterator moduleIterator = moduleIDs.iterator();
      while (moduleIterator.hasNext()) {
        Number moduleID = (Number) moduleIterator.next();
        HashMap moduleRightsHashMap = (HashMap) moduleRightsTreeMap.get(moduleID);
        Number moduleParentID = (Number) moduleRightsHashMap.get("parentId");
        if (moduleParentID.intValue() != 0) {
        // We're at the top level and this module has parents... go fish.
          continue;  
        }
        // we must really be a top level module, take the key out of the set.  We won't be needing it
        moduleIterator.remove();
        String moduleName = (String) moduleRightsHashMap.get("name");
        Number moduleRightsLevel = (Number) moduleRightsHashMap.get("rights");
        
        sb.append(this.generateStartModuleHTML(moduleName, null, moduleRightsLevel.intValue()));

        // The top module may have fields.
        HashMap moduleFields =  (HashMap) fieldRights.get(moduleName);
        if (moduleFields != null) {
          Set moduleFieldSet = moduleFields.keySet();
          Iterator moduleFieldIterator = moduleFieldSet.iterator();
          sb.append(this.generateStartFieldHTML());
          while (moduleFieldIterator.hasNext()) {
            String fieldName = (String) moduleFieldIterator.next();
            Number fieldLevelRights = (Number) moduleFields.get(fieldName);
            sb.append(this.generateModuleFieldSelectHTML(moduleName, fieldName, fieldLevelRights.intValue()));
          } //end of while loop (moduleFieldIterator.hasNext())
          sb.append(this.generateEndFieldHTML());
        } //end of if statement (moduleFields != null)
        
        Iterator subModuleIterator = moduleIDs.iterator();
        sb.append(this.generateStartSubModuleHTML());

        sbJS.append(" \"" + moduleName + "\" : [   ");
        while (subModuleIterator.hasNext()) {
          Number subModuleID = (Number) subModuleIterator.next();
          HashMap subModuleRightsHashMap = (HashMap) moduleRightsTreeMap.get(subModuleID);   
          Number subModuleParentID = (Number) subModuleRightsHashMap.get("parentId");       
          if (subModuleParentID.equals(moduleID)) {
            String subModuleName = (String) subModuleRightsHashMap.get("name");
            Number subModuleRightsLevel = (Number) subModuleRightsHashMap.get("rights");
            sb.append(this.generateStartModuleHTML(subModuleName, moduleName, subModuleRightsLevel.intValue()));
            
            sbJS.append("\"" + subModuleName + "\", ");

            HashMap subModuleFields =  (HashMap) fieldRights.get(subModuleName);
            if (subModuleFields != null) {
              Set subModuleFieldSet = subModuleFields.keySet();
              Iterator subModuleFieldIterator = subModuleFieldSet.iterator();
              sb.append(this.generateStartFieldHTML());
              while (subModuleFieldIterator.hasNext()) {
                String fieldName = (String) subModuleFieldIterator.next();
                Number fieldLevelRights = (Number) subModuleFields.get(fieldName);
                sb.append(this.generateModuleFieldSelectHTML(subModuleName, fieldName, fieldLevelRights.intValue()));
              } //end of while loop (subModuleFieldIterator.hasNext())
              sb.append(this.generateEndFieldHTML());
            } //end of if statement (subModuleFields != null)
            // Close out the subModule
            sb.append(this.generateEndModuleHTML());
          } //end of if statement (parentModuleID.equals(subParentModuleID))
        } //end of while loop (subModuleIterator.hasNext())
        sb.append(this.generateEndSubModuleHTML());
        // Close out the parent Module
        sb.append(this.generateEndModuleHTML());
        sbJS.delete(sbJS.length()-2,sbJS.length()-1);
        sbJS.append("], ");
      } //end of while loop (moduleIterator.hasNext())
    } //end of if statement (moduleRights != null && fieldRights != null)    
    sb.append(this.generateDivCountComment());
    sbJS.delete(sbJS.length()-2,sbJS.length()-1);
    sbJS.append("};\n</script>");
    
    sb.append(sbJS);

    return sb.toString();
  }
  
  /**
   * Generates and returns the HTML needed to display a 
   * module field with it's options (None/View/Update). 
   * 
   * @param moduleName The Module Name
   * @param fieldName The Field Name
   * @return The HTML to display the module field.
   */
  private String generateModuleFieldSelectHTML(String moduleName, String fieldName, int rightsLevel)
  {
    if ((rightsLevel != ModuleFieldRightMatrix.NONE_RIGHT) 
      || (rightsLevel != ModuleFieldRightMatrix.VIEW_RIGHT)) {
      rightsLevel = ModuleFieldRightMatrix.UPDATE_RIGHT;
    } //end of if statement ((rightsLevel != ModuleFieldRightMatrix.NONE_RIGHT) ...
    StringBuffer sb = new StringBuffer();
    sb.append("<!-- " + fieldName + " Field -->\n");
    sb.append("<tr>\n");
    sb.append("<td class=\"popupTableText\" width=\"100\">" + fieldName + "</td>\n");
    sb.append("<td class=\"popupTableText\">\n");
    sb.append("<select name=\"" + moduleName + "_" + fieldName);
    sb.append("\" class=\"securitySelect\">" + fieldName + "\n");
    sb.append("<option value=\"" + ModuleFieldRightMatrix.NONE_RIGHT + "\"");
    if (rightsLevel == ModuleFieldRightMatrix.NONE_RIGHT) {
      sb.append("selected");
    } //end of if statement (rightsLevel == ModuleFieldRightMatrix.NONE_RIGHT)
    sb.append(">None</option>\n");
    sb.append("<option value=\"" + ModuleFieldRightMatrix.VIEW_RIGHT + "\"");
    if (rightsLevel == ModuleFieldRightMatrix.VIEW_RIGHT) {
      sb.append("selected");
    } //end of if statement (rightsLevel == ModuleFieldRightMatrix.VIEW_RIGHT)
    sb.append(">View</option>\n");
    sb.append("<option value=\"" + ModuleFieldRightMatrix.UPDATE_RIGHT + "\"");
    if (rightsLevel == ModuleFieldRightMatrix.UPDATE_RIGHT) {
      sb.append("selected");
    } //end of if statement (rightsLevel == ModuleFieldRightMatrix.UPDATE_RIGHT)
    sb.append(">Update</option>\n");
    sb.append("</select>\n");
    sb.append("</td>\n");
    sb.append("</tr>\n");
    return sb.toString();
  } //end of generateModuleFieldSelectHTML method
  
  private String generateStartFieldHTML()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("\t\t\t<span class=\"branch\" id=\"s_" + divCount + "\" style=\"display: none;\">\n");
    sb.append("\t\t\t\t<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\">\n");
    
    return sb.toString();
  }
  
  private String generateEndFieldHTML()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("\t\t\t\t</table>\n");
    sb.append("\t\t\t</span>\n");
  
    return sb.toString();
  }
  
  private String generateStartSubModuleHTML()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("\t\t\t<span class=\"branch\" id=\"s_" + divCount + "\" style=\"display: none;\">\n");
  
    return sb.toString();
  }

  private String generateEndSubModuleHTML()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("\t\t\t</span>\n");

    return sb.toString();
  }
  
  /**
   * Generates and returns the HTML needed to display a 
   * module name with expand/collapse links. If  
   * </code>parentModuleName</code> is not
   * <code>null</code> then the module will look like a 
   * submodule.
   * 
   * @param moduleName The name of the current module.
   * @param parentModuleName The current module's parent
   *  module. If there is not, pass in <code>null</code>
   * @return The HTML to display the module.
   */
  private String generateStartModuleHTML(String moduleName, String parentModuleName, int rightsLevel)
  {
    StringBuffer sb = new StringBuffer();
    divCount++;
    sb.append("<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\">\n");
    sb.append("\t<tr>\n");
    sb.append("\t\t<td class=\"popupTableText\" valign=\"top\">\n");
    sb.append("\t\t\t<input type=\"checkbox\" name=\"moduleright\""); 
    
    sb.append("value=\"" + moduleName + "\"");
    
    if (rightsLevel != ModuleFieldRightMatrix.NONE_RIGHT) {
      sb.append(" checked ");
    } //end of if statement (rightsLevel != ModuleFieldRightMatrix.NONE_RIGHT)
    
    sb.append(" onclick=\"checkSubModules(this);\"");

    sb.append(" class=\"securityXbox\">");
    sb.append("</td>\n");
    
    sb.append("\t\t<td class=\"popupTableText\">\n");
    sb.append("\t\t\t<span class=\"currbranchstate\" ");
    sb.append("id=\"e_" + divCount + "\" style=\"display: none;\" >");
    sb.append("<strong>" + moduleName + "</strong>");
    sb.append("&nbsp;&laquo;&nbsp;\n");
    sb.append("\t\t\t\t<a href='javascript:showBranch(\"s_" + divCount + "\",");
    sb.append("\"e_" + divCount + "\",\"c_" + divCount + "\")'");
    sb.append("class=\"contactTableLink\">Collapse</a>&nbsp;&raquo;</span>\n");
    
    sb.append("\t\t\t<span class=\"nextbranchstate\" ");
    sb.append("id=\"c_" + divCount + "\" style=\"display: block;\" >");
    sb.append("<strong>" + moduleName + "</strong>");
    sb.append("&nbsp;&laquo;&nbsp;\n");
    sb.append("\t\t\t\t<a href='javascript:showBranch(\"s_" + divCount + "\",");
    sb.append("\"e_" + divCount + "\",\"c_" + divCount + "\")'");
    sb.append("class=\"contactTableLink\">Expand</a>&nbsp;&raquo;</span>\n");
       
    return sb.toString();  
  } //end of generateStartModuleHTML method

  /**
   * Generates and returns the HTML needed to display the 
   * end of a module span. 
   * 
   * @return The HTML to display the end of a module span.
   */
  private String generateEndModuleHTML()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("\t\t</td>\n");
    sb.append("\t</tr>\n");
    sb.append("</table>\n");
    
    return sb.toString();
  } //end of generateEndModuleHTML method
  
  /**
   * Generates and returns the HTML needed to display the 
   * number of div tags comment. 
   * 
   * @return The HTML to display the number of div tags comment.
   */
  private String generateDivCountComment()
  {
    StringBuffer sb = new StringBuffer();
    sb.append("<!-- There are " + divCount + " div tags on this page. -->");
    
    return sb.toString();
  } //end of generateDivCountComment method
}
