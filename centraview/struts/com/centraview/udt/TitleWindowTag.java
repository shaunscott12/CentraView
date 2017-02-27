/*
 * $RCSfile: TitleWindowTag.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:29:06 $ - $Author: mking_cv $
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
package com.centraview.udt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

/**
 * Provides customize display of title text on browser window.
 *
 *  @author   Ashwin Nagar
 */
public final class TitleWindowTag extends TagSupport
{
  protected static MessageResources messages =
    MessageResources.getMessageResources("ApplicationResources");

  /**
   *  Name of company displayed as first string.
   */
  private String companyname = "CentraView";

  /**
   *  User name displayed as second string.
   */
  private String username = "";

  /**
   *  Module name displayed as third string.
   */
  private String moduleName = "";

  /**
   *  Action name displayed as fourth string
   */
  private String actionname = "";

  /**
   *  Action details dsiplayed as fifth string. Action detail can be blank.
   */
  private String actiondetail = "";

  /**
   * The title containing the concatenation of companyname, 
   * username, moduleName, actionname and actiondetail 
   * separated by delimeter ':'.
   */
  private String title = "";

  /**
   *  Constructor, with no parameter.
   */
  public TitleWindowTag()
  {
    super();
  }

  /**
   *  Getter method for companyname
   *  returns String
   */
  public String getCompanyname()
  {
    return companyname;
  }

  /**
   *  Setter method for companyname
   *  @param String
   *  Accepts string to display company name
   */
  public void setCompanyname(String companyName)
  {
    if (messages.getMessage(companyName) != null)
    {
      this.companyname = messages.getMessage(companyName);
    }
    else
    {
      this.companyname = companyName;
    }
  }

  /**
   *  Getter method for username
   *  returns String
   */
  public String getUsername()
  {
    return username;
  }

  /**
   *  Setter method for username
   *  @param String
   *  Accepts string to display user name
   */
  public void setUsername(String userName)
  {
    this.username = messages.getMessage(userName);
  }

  /**
   *  Getter method for moduleName
   *  returns String
   */
  public String getModuleName()
  {
    return moduleName;
  }

  /**
   *  Setter method for moduleName
   *  @param String
   *  Accepts string to display module name
   */
  public void setModuleName(String moduleName)
  {
    if (messages.getMessage(moduleName) != null)
    {
      this.moduleName = messages.getMessage(moduleName);
    }
    else
    {
      this.moduleName = moduleName;
    }
  }

  /**
   *  Getter method for actionname
   *  returns String
   */
  public String getActionname()
  {
    return actionname;
  }

  /**
   *  Setter method for actionname
   *  @param String
   *  Accepts string to display action information
   */
  public void setActionname(String actionName)
  {
    if (messages.getMessage(actionName) != null)
    {
      this.actionname = messages.getMessage(actionName);
    }
    else
    {
      this.actionname = actionName;
    }
  }

  /**
   *  Getter method for actiondetail
   *  returns String
   */
  public String getActiondetail()
  {
    return actiondetail;
  }

  /**
   *  Setter method for actiondetail
   *  @param String
   *  Accepts string to display action detail information
   */
  public void setActiondetail(String actionDetail)
  {
    if (messages.getMessage(actionDetail) != null)
    {
      this.actiondetail = messages.getMessage(actionDetail);
    }
    else
    {
      this.actiondetail = actionDetail;
    }
  }

  /**
   *  Getter method for title
   *  returns String
   */
  public String getTitle()
  {
    return title;
  }

  /**
   *  Setter method for title
   *  Accepts string to display action detail information
   */
  public void setTitle()
  {
    setCompanyname("CentraView");
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    HttpSession session = request.getSession(true);
    title = "<title> ";

    if (session.getAttribute("userName") != null)
    {
      setUsername(session.getAttribute("userName").toString());
      title += (companyname + ":" + username);
    }
    else
    {
      title += companyname;
    }

    title += (": " + moduleName);

    if (request.getAttribute("detailName") != null)
    {
      title += (": " + request.getAttribute("detailName"));
    }

    if (request.getAttribute("actionName") != null)
    {
      setActionname(request.getAttribute("actionName").toString());
      title += (":" + actionname);
    }

    if (request.getAttribute("actionDetail") != null)
    {
      setActiondetail(request.getAttribute("actionDetail").toString());
      title += (":" + actiondetail);
    }

    title += " </title>";
  }

  /**
   *  Executes at start of tag
   *  returns int
   */
  public int doStartTag()
    throws JspException
  {
    JspWriter writer = pageContext.getOut();

    try
    {
      ;
    }
    catch (Exception e)
    {
      System.out.println("Exception in start tag is : " + e.toString());
    }

    return (EVAL_BODY_INCLUDE);
  }

  /**
   * Executes after body of tag but before end of tag delimeter
   *  returns int
   */
  public int doAfterBody()
    throws JspException
  {
    // setTitle();
    return 0;
  }

  /**
   *  Executes at end of tag delimeter
   *  returns int
   */
  public int doEndTag()
    throws JspException
  {
    JspWriter writer = pageContext.getOut();

    try
    {
      setTitle();
      writer.print(getTitle());
    }
    catch (Exception e)
    {
      System.out.println("Exception in end tag is : " + e.toString());
    }

    return (EVAL_PAGE);
  }

  /**
   *  Initialize all variables to null or boolean
   */
  public void release()
  {
    super.release();
    username = "";
    moduleName = "";
    actionname = "";
    actiondetail = "";
    title = "";
  }
}
