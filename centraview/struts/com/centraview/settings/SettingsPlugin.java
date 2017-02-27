/*
 * $RCSfile: SettingsPlugin.java,v $    $Revision: 1.3 $  $Date: 2005/09/01 15:31:04 $ - $Author: mcallist $
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

package com.centraview.settings;

import java.io.File;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

import com.centraview.administration.applicationsettings.AppSettings;
import com.centraview.administration.applicationsettings.AppSettingsHome;
import com.centraview.common.CVUtility;
import com.centraview.common.Constants;
import com.centraview.jobs.SupportEmailCheck;
import com.centraview.support.helper.SupportHelper;
import com.centraview.support.helper.SupportHelperHome;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 * Implements the Struts PlugIn Mechanism to configure CentraView information that
 * must be set that can't be stored in the DataBase.
 * e.g. Getting JNDI connection information and the dataSource information.
 * Based on Example from The O'Reily Programming Jakarta Struts 2nd Edition book.
 * 
 */
public class SettingsPlugin implements PlugIn
{
  private static Logger logger = Logger.getLogger(SettingsPlugin.class);

  /**
   * @see org.apache.struts.action.PlugIn#destroy()
   * call close on the InitialContext
   * Also shutdown the quartz scheduler.
   */
  public void destroy()
  {
    logger.info("[destroy] Shuting down CentraView.");
    SettingsInterface settings = Settings.getInstance();
    InitialContext initCtx = settings.getInitCtx();
    try {
      if (initCtx != null) {
        initCtx.close();
        initCtx = null;
      }
    } catch (NamingException e) {
      logger.error("[destroy] closing initialContext Exception thrown.", e);
    }
    logger.info("[CentraView]: Shutdown complete.");
  }

  /**
   * Read in CentraView.properties getInitialContext with the JNDI
   * properties that we hope are in CentraView.properties, stick the
   * initialContext in our Settings Singleton, and stick the dataSource
   * on the in the settings Singleton. We do this instead of using the
   * ServletContext because the Code in the web tier doesn't make it
   * consistently easy to get access to the ServletContext when we need it.
   * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
   */
  public void init(ActionServlet servlet, ModuleConfig config) throws ServletException
  {
    logger.debug("[init] Starting Up.");
    Properties inProps = new Properties();
    SettingsInterface settings = Settings.getInstance();
    InitialContext initCtx = null;
    String host = CVUtility.getHostName(servlet.getServletContext());
    // use log4j Mapped diagnostic context to uniquely identify
    // each hosts log entries.
    MDC.put("HOSTNAME", host);
    try {
      java.io.InputStream propertiesInputStream = servlet.getServletContext().getResourceAsStream("/WEB-INF/classes/CentraView.properties");
      if (propertiesInputStream != null) {
        inProps.load(propertiesInputStream);
      } else {
        throw new UnavailableException("Cannot create CentraView.properties InputStream.");
      }
      initCtx = new InitialContext(inProps);
    } catch (java.io.IOException e1) {
      // catch the IOException if the properties file can't be found or read.
      logger.error("[init] Cannot read/find CentraView.properties.", e1);
      throw new UnavailableException("Cannot load CentraView.properties file.");
    } catch (NamingException e) {
      logger.error("[init] Failed getting InitialContext.", e);
      throw new UnavailableException("Cannot get Initial Context");
    }
    String dataSource = inProps.getProperty("centraview.datasource");

    // put the info in the singleton
    settings.setInitCtx(initCtx);
    SiteInfo siteInfo = new SiteInfo();
    siteInfo.setDataSource(dataSource);

    File fsRoot = CVUtility.getCentraViewFileSystemRoot(dataSource);
    // This is where we instanciate a java.util.Timer
    // for scheduling recurring checks of email accounts.
    // it is a daemon thread, that will not prolong the life of the application.
    // This means we don't need to explicitly clean it up at the end.
    // It will just die it's meaningless death.
    Timer supportEmailTimer = new Timer(true);

    // schedule a job to check support email every x minutes
    int supportInterval = 0;
    try {
      SupportHelperHome supportHome = (SupportHelperHome)CVUtility.getHomeObject("com.centraview.support.helper.SupportHelperHome", "SupportHelper");
      SupportHelper supportRemote = supportHome.create();
      supportRemote.setDataSource(dataSource);
      supportInterval = supportRemote.getSupportEmailCheckInterval();
    } catch (Exception e) { }

    if (supportInterval > 0) {
      // minutes to seconds, then seconds to miliseconds...
      Integer interval = new Integer(supportInterval * 60 * 1000);
      // 600000L = 10 minutes * 60 seconds/minute * 1000 miliseconds/second
      // delay the first check for 10 minutes after the startup.
      // Just to give things time to settle down.
      TimerTask supportEmailTask = new SupportEmailCheck(dataSource);
      supportEmailTimer.schedule(supportEmailTask, 600000L, interval.longValue());
      siteInfo.setSupportEmailTask(supportEmailTask);
    }

    // now stick the Timer into the Settings Singleton so that
    // it can be accessed by anything in the Tomcat4 webapp classloader.
    // This is necessary for rescheduling support email checks.
    siteInfo.setSupportEmailTimer(supportEmailTimer);

    siteInfo.setCustomerLogo(this.getCustomerLogoFile(dataSource, fsRoot));
    // Place the setInfo object on the settings Singleton.
    settings.setSiteInfo(host, siteInfo);
    if (logger.isInfoEnabled()) {
      logger.info("[CentraView]: Setup for Host " + host + " completed.");
      logger.info("[CentraView]: setup InitialContext with: java.naming.factory.initial=" + inProps.getProperty("java.naming.factory.initial") + " and java.naming.provider.url=" + inProps.getProperty("java.naming.provider.url"));
      logger.info("[CentraView]: set dataSource to: " + dataSource);
    }
  } // end init() method

  
  private final File getCustomerLogoFile(String dataSource, File fsRoot)
  {
    String customerLogoName = null;
    try {
      AppSettingsHome appHome = (AppSettingsHome)CVUtility.getHomeObject("com.centraview.administration.applicationsettings.AppSettingsHome", "AppSettings");
      AppSettings appRemote = appHome.create();
      appRemote.setDataSource(dataSource);
      customerLogoName = appRemote.getApplicationSettings("CUSTOMERLOGO");
    } catch (Exception e) {
      logger.error("[getCustomerLogoFile] Exception thrown.", e);
    }
    File customerLogo = new File(fsRoot.getAbsolutePath() + Constants.CUSTOMERLOGO_PATH + customerLogoName);
    return customerLogo;
  }

}   // end class definition

