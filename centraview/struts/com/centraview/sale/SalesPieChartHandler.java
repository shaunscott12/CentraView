/*
 * $RCSfile: SalesPieChartHandler.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:42 $ - $Author: mking_cv $
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
 
package com.centraview.sale;

import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.CreateException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.centraview.chart.Chart;
import com.centraview.chart.ChartHome;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.settings.Settings;
import com.centraview.valuelist.ValueListParameters;

/**
 * Action/servlet for serving up the Opportunities by Stage
 * chart image. Relies on a ValueListParameter being set
 * on the session, which is used to help get the data from
 * the ChartEJB.
 */
public class SalesPieChartHandler extends org.apache.struts.action.Action
{
  private static Logger logger = Logger.getLogger(SalesPieChartHandler.class);
  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws RuntimeException, Exception
  {
    final String dataSource = Settings.getInstance().getSiteInfo(CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();

    HttpSession session = request.getSession();
    UserObject userObject = (UserObject)session.getAttribute("userobject");
    int individualId = userObject.getIndividualID();    // logged in user
    
    // get the data from the session
    ValueListParameters listParameters = (ValueListParameters)session.getAttribute("salesPieChartParams");

    // get the data from the EJB
    ChartHome chartHome = (ChartHome)CVUtility.getHomeObject("com.centraview.chart.ChartHome", "Chart");
    Chart chartRemote = null;
    try
    {
      chartRemote = chartHome.create();
    }catch(CreateException e){
      logger.error("[execute] Exception thrown.", e);
      throw new ServletException(e);
    }
    chartRemote.setDataSource(dataSource);
    Collection chartRawData = (Collection)chartRemote.getOpportunityPieData(individualId, listParameters);

    // Add the raw data to a JFree dataset
    DefaultPieDataset pieData = new DefaultPieDataset();
    float totalForecast = 0.00f;
    float totalActual = 0.00f;
    Iterator iter = chartRawData.iterator();
    while (iter.hasNext())
    {
      HashMap row = (HashMap)iter.next();
      String stageName = (String)row.get("stageName");
      Number count = (Number)row.get("count");
      Number forecastAmount = (Number)row.get("forecastAmount");
      Number actualAmount = (Number)row.get("actualAmount");

      totalForecast += forecastAmount.floatValue();
      totalActual += actualAmount.floatValue();

      pieData.setValue(stageName + " (" + count.toString() + ")", forecastAmount);
    }

    // format the totals as US Currency
    NumberFormat numFormatter = NumberFormat.getCurrencyInstance();
    String totalForecastString = numFormatter.format(totalForecast);
    String totalActualString = numFormatter.format(totalActual);

    // create the chart
    JFreeChart pieChart = ChartFactory.createPieChart("Opportunities by Stage", pieData, true, true, false);

    // set the sub-title
    TextTitle t1 = new TextTitle("Total Forecasted: " + totalForecastString + "           " + 
                                 "Total Amount: " + totalActualString,
                                 new Font("SansSerif", Font.PLAIN, 11));
    pieChart.addSubtitle(t1);

    // set the chart visual options
    PiePlot plot = (PiePlot)pieChart.getPlot();
    plot.setForegroundAlpha(0.65f);
    plot.setNoDataMessage("No data to display");
    plot.setLabelGenerator(new CustomLabelGenerator(numFormatter));

    // print the chart image directly the the HTTP stream
    OutputStream out = response.getOutputStream();
    response.setContentType("image/jpeg");
      
    try
    {
      ChartUtilities.writeChartAsJPEG(out, 1.0f, pieChart, 400, 300);
    }catch(IOException e){
      logger.error("[getOpportunityPieData] Exception thrown.", e);
      throw new ServletException(e);
    }finally{
      out.close();
    }

    session.removeAttribute("salesPieChartParams");

    // return null (don't forward anywhere, we've done the output already)
    return(null);
  }   // end execute() method
  
  /**
   * Custom label generator that labels the pie sections with
   * the format "[Stage] ([#opp]) - $x,xxx.xx". ie: "Approach (2) = $595.00"
   */
  static class CustomLabelGenerator implements PieSectionLabelGenerator
  {
    private NumberFormat nfInstance;
    public CustomLabelGenerator(NumberFormat nf)
    {
      this.nfInstance = nf;
    }
    /**
     * Generates a label for a pie section.
     * @param dataset  the dataset (<code>null</code> not permitted).
     * @param key  the section key (<code>null</code> not permitted).
     * @return the label (possibly <code>null</code>).
     */
    public String generateSectionLabel(PieDataset dataset, Comparable key)
    {
      String result = null;    
      if (dataset != null)
      {
        result = key.toString() + " - " + nfInstance.format(dataset.getValue(key));
      }
      return result;
    }
  }

}   // end class definition

