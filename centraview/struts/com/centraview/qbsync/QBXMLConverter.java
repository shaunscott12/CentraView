/*
 * $RCSfile: QBXMLConverter.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:34 $ - $Author: mking_cv $
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
 * This Class converts QB XML to ObjectCV List there and back
 * @author   Maksim Mostovnikov
 * @version  1.0
 *
 */

package com.centraview.qbsync;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

import com.centraview.account.common.ItemElement;
import com.centraview.account.common.ItemLines;
import com.centraview.common.CVUtility;
import com.centraview.common.FloatMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;

public class QBXMLConverter
{
  private static Logger logger = Logger.getLogger(QBXMLConverter.class);
  public static final String FILE_CONFIG = "QBXMLConfig.xml";
  public static final String FILE_CONFIG_ADD = "QBXMLAddConfig.xml";
  public static final String FILE_CONFIG_MOD = "QBXMLModConfig.xml";
  public static final String TAG_VONAME = "voname";
  public static final String TAG_MODULE_NAME = "modulename";
  public static final String TAG_LISTID = "ListID";
  public static final String TAG_SEPARATOR = "Separator";
  public static final String ATTR_REQUEST_ID = "requestID";
  public static final String ATTR_STATUS_CODE = "statusCode";
  public static final String ATTR_STATUS_MESSAGE = "statusMessage";
  public static final String ATTR_LISTVO = "listvo";
  public static final String XML_VERSION = "<?xml version=\"1.0\" ?> \n <?qbxml version=\"2.0\" ?>";
  public static final String XML_HEADER = XML_VERSION + " \n<QBXML> \n<QBXMLMsgsRq onError=\"continueOnError\"> \n";
  public static final String XML_FOOTER = "</QBXMLMsgsRq> \n</QBXML> \n";

  public static String filePath = null;
  private String dataSource = null;

  /**
   * This Method is for getting ExternalID(CV) i.e. ListID(QB)
   * by Primary Key of CV table
   * @param module String reference for table name
   * @param cvid int
   * @return String ExternalID(CV) i.e. ListID(QB)
   */
  public String getEXIDFromID(String module, int cvid)
  {
    String exid = "";
    if (isEntityModule(module))
    {
      module = "Entity";
    }
    if (isItemModule(module))
    {
      module = "Item";
    }
    try
    {
      QBSyncHome qbsh = (QBSyncHome)CVUtility.getHomeObject("com.centraview.qbsync.QBSyncHome", "QBSync");
      QBSync remote = (QBSync)qbsh.create();
      remote.setDataSource(this.dataSource);
      exid = remote.getExtidforCVid(module, cvid);
    } catch (Exception e)
    {
      logger.error("[getEXIDFromID] Exception thrown.", e);
      return "";
    }
    return exid;
  }
  /**
   *This Method is for getting Primary Key of CV table
   *from ExternalID(CV) i.e. ListID(QB)
   *@param String reference for table name
   *@param String value of ExternalID(CV) i.e. ListID(QB)
   *@return String PK
   */
  public String getIDFromEXID(String key, String value)
  {
    int id = 0;
    if (isEntityModule(key))
    {
      key = "Entity";
    }
    if (isItemModule(key))
    {
      key = "Item";
    }
    try
    {
      QBSyncHome qbsh = (QBSyncHome)CVUtility.getHomeObject("com.centraview.qbsync.QBSyncHome", "QBSync");
      QBSync remote = (QBSync)qbsh.create();
      remote.setDataSource(this.dataSource);
      id = remote.getCVidForExtid(key, value);
    } catch (Exception e)
    {
      logger.error("[getIDFromEXID] Exception thrown getting ID.", e);
      return "";
    }
    return "" + id;
  }

  /**
   * This Method is for getting ExternalID List
   * from QB XML.
   * @param xml String QB XML
   * @return ArrayList ExternalID List
   */
  public ArrayList getVOListfromResponse(String xml, String path)
  {
    ArrayList arl = new ArrayList();

    try
    {
      filePath = path;
      Document qbXML = getDocumentFromString(xml);

      String idXML = getQBXMLName(qbXML);
      String nameQBXML = idXML.substring(0, idXML.length() - 5);

      NodeList list = qbXML.getElementsByTagName(nameQBXML + "AddRs");
      Node tmp = null;

      int requestID = 0;
      int length = list.getLength();

      for (int i = 0; i < length; i++)
      {

        tmp = list.item(i);
        requestID = Integer.parseInt(tmp.getAttributes().getNamedItem(ATTR_REQUEST_ID).getNodeValue());

        if (!tmp.getAttributes().getNamedItem(ATTR_STATUS_CODE).getNodeValue().equals("0"))
        {
          System.out.print("******RequestID = " + requestID);
          System.out.println(" * Message from QB :  " + tmp.getAttributes().getNamedItem(ATTR_STATUS_MESSAGE).getNodeValue());
          arl.add(requestID, null);
        } else
        {
          arl.add(requestID, getValueOfNodeByTagName(tmp, TAG_LISTID));
        }
      }
    } catch (Exception e)
    {
      logger.error("[getVOListfromResponse] Exception thrown.", e);
    }

    return arl;
  }
  /**
   *
   * @param exids ArrayList
   * @param qbxmlName String
   * @return String
   */
  public String createQBXMLForEditSequence(ArrayList exids, String qbxmlName)
  {
    qbxmlName = qbxmlName.substring(0, qbxmlName.length() - 2);
    String xml = XML_HEADER + "<" + qbxmlName + "Rq requestID = \"0\"> \n";
    int size = exids.size();

    for (int i = 0; i < size; i++)
    {
      xml += "<" + TAG_LISTID + ">" + exids.get(i) + "</" + TAG_LISTID + "> \n";
    }

    xml += "</" + qbxmlName + "Rq> \n" + XML_FOOTER;

    return xml;
  }
  /**
   *
   * @param qbxml String
   * @param qbxmlName String
   * @return HashMap (ExternalID,EditSequence)
   */
  public HashMap getEditSequenceList(String qbxml, String qbxmlName)
  {
    HashMap hm = new HashMap();

    try
    {
      NodeList list = getDocumentFromString(qbxml).getElementsByTagName(qbxmlName.substring(0, qbxmlName.length() - 7) + "Ret");
      Node node = null;

      String externalID;
      String editSequence;

      int length = list.getLength();

      for (int i = 0; i < length; i++)
      {
        node = list.item(i);
        externalID = getValueOfNodeByTagName(node, TAG_LISTID);
        editSequence = getValueOfNodeByTagName(node, "EditSequence");
        hm.put(externalID, editSequence);
      }
    } catch (Exception e)
    {
      logger.error("[getEditSequenceList] Exception thrown.", e);
      e.printStackTrace();
    }

    return hm;
  }

  /**
   * This Method is for getting objectCV list from QB XML.
   * @param xml String QB XML.
   * @return HashMap {modulename,qbxmlname,listvo}
   */
  public HashMap getVOListFromQBXML(String xml, String path)
  {
    HashMap listVOAndModuleName = new HashMap();

    try
    {
      filePath = path;
      ArrayList objectsCV = new ArrayList();
      Document qbXML = getDocumentFromString(xml);
      String nameQBXML = getQBXMLName(qbXML);
      Node node = qbXML.getElementsByTagName(nameQBXML).item(0);

      Document configForQBXML = getDocumentFromString(getDocumentFromString(FILE_CONFIG).getElementsByTagName(nameQBXML).item(0).toString());

      listVOAndModuleName = new HashMap();
      NodeList listQB = qbXML.getElementsByTagName(getValueOfNodeByTagName(configForQBXML, TAG_SEPARATOR) + "Ret");
      Node config = configForQBXML.getFirstChild();

      String moduleName = getValueOfNodeByTagName(config, TAG_MODULE_NAME);
      String className = getValueOfNodeByTagName(config, TAG_VONAME);

      Object cvObj = null;

      int length = listQB.getLength();
      if (node.getAttributes().getNamedItem(ATTR_STATUS_CODE).getNodeValue().equals("0"))
      {
        for (int i = 0; i < length; i++)
        {
          cvObj = getVOfromQBXML(listQB.item(i), config);
          setCVID(cvObj, moduleName, className);
          objectsCV.add(cvObj);
        }
      } else
      {
        System.out.println("*** Message from QB:  " + node.getAttributes().getNamedItem(ATTR_STATUS_MESSAGE).getNodeValue());
      }
      listVOAndModuleName.put("qbxmlname", nameQBXML);
      listVOAndModuleName.put(ATTR_LISTVO, objectsCV);
      listVOAndModuleName.put(TAG_MODULE_NAME, moduleName);
    } catch (Exception e)
    {
      logger.error("[getVOListFromQBXML] Exception thrown.", e);
    }

    return listVOAndModuleName;
  }
  /**
   * 
   * @param moduleName String
   * @return boolean
   */
  public boolean isItemModule(String moduleName)
  {
    return (moduleName.equals("ItemInventory") || moduleName.equals("ItemNonInventory") || moduleName.equals("ItemService"));
  }
  /**
   * 
   * @param moduleName String
   * @return boolean
   */
  public boolean isEntityModule(String moduleName)
  {
    return (moduleName.equals("Customer") || moduleName.equals("Vendor"));
  }

  /**
   * 
   * @param obj Object
   * @param moduleName String
   * @param className String
   */
  public void setCVID(Object obj, String moduleName, String className)
  {
    Class classCV[] = new Class[1];
    Class arg[] = new Class[1];
    Object idCV[] = new Object[1];
    Method method = null;
    Object ret = null;
    String met = "";
    String id = "";

    try
    {
      if (moduleName.equals("Invoice"))
        return;

      classCV[0] = Class.forName(className);
      method = classCV[0].getMethod("getExternalID", null);
      ret = method.invoke(obj, null);

      id = getIDFromEXID(moduleName, ret.toString());

      if (isEntityModule(moduleName))
      {
        moduleName = "Entity";
        met = "setContactID";
      } else if (moduleName.equals("Individual"))
      {
        met = "setIndividualID";
      } else if (moduleName.equals("PurchaseOrder"))
      {
        met = "setPurchaseOrderId";
      } else if (moduleName.equals("Expense"))
      {
        met = "setExpenseID";
      } else if (isItemModule(moduleName))
      {
        moduleName = "Item";
        met = "setItemId";
      } else if (moduleName.equals("Payment"))
      {
        met = "setPaymentID";
      } else if (moduleName.equals("PaymentMethod"))
      {
        met = "setMethodID";
      } else if (moduleName.equals("GLAccount"))
      {
        met = "setGlaccountID";
      }
      arg[0] = Integer.TYPE;
      idCV[0] = getObjectByName("int", id);
      method = classCV[0].getMethod(met, arg);
      method.invoke(obj, idCV);
    } catch (Exception e)
    {
      System.out.print(e);
      e.printStackTrace();
    }

  }
  /**
   *
   * @param objectCV Object
   * @param configXML Node
   * @param className String
   */
  public void setType(Object objectCV, Node configXML, String className)
  {
    String sep = "";

    try
    {

      Class argumentType[] = new Class[1];
      Method method = null;
      Class classCV[] = new Class[1];
      classCV[0] = Class.forName(className);
      Object arguments[] = new Object[1];

      if (className.equals("com.centraview.contact.entity.EntityVO") || className.equals("com.centraview.contact.individual.IndividualVO"))
      {

        if (className.equals("com.centraview.contact.entity.EntityVO"))
        {
          argumentType[0] = Class.forName("java.lang.String");
          sep = getValueOfNodeByTagName(configXML, TAG_SEPARATOR);
          method = classCV[0].getMethod("setCustomType", argumentType);
          arguments[0] = sep;
          method.invoke(objectCV, arguments);

          arguments[0] = getObjectByName("int", "1");
        } else
        {
          arguments[0] = getObjectByName("int", "2");
        }
        argumentType[0] = Integer.TYPE;
        method = classCV[0].getMethod("setContactType", argumentType);
        method.invoke(objectCV, arguments);
      } else if (className.equals("com.centraview.account.item.ItemVO"))
      {
        String type = "";
        sep = getValueOfNodeByTagName(configXML, TAG_SEPARATOR);
        if (sep.equals("ItemInventory"))
        {
          type = "1";
        } else if (sep.equals("ItemNonInventory"))
        {
          type = "2";
        } else if (sep.equals("ItemService"))
        {
          type = "3";
        }
        argumentType[0] = Integer.TYPE;
        arguments[0] = getObjectByName("int", type);
        method = classCV[0].getMethod("setItemTypeId", argumentType);
        method.invoke(objectCV, arguments);
      }

    } catch (Exception e)
    {
      logger.error("[setType] Exception thrown.", e);
    }
  }
  /**
   * This Method is for getting ObjectCV
   * from fragment QB XML.
   * @param xml Node Fragment QB XML
   * @param configXML Node config for QB XML.
   * @return Object ObjectCV
   */
  public Object getVOfromQBXML(Node xml, Node configXML)
  {
    Object objectCV = null;
    String tagName = "";

    try
    {
      Document configForQBXML;
      NamedNodeMap attr;

      Class classCV[] = new Class[1];
      Class argumentType[] = new Class[1];

      Object arguments[] = new Object[1];

      String className = getValueOfNodeByTagName(configXML, TAG_VONAME);

      String typeArg = "";
      String value = "";

      NodeList listItem = configXML.getChildNodes();
      NodeList tmpNodeList = null;

      int length = listItem.getLength();

      classCV[0] = Class.forName(className);
      objectCV = classCV[0].newInstance();

      Method method = null;
      setType(objectCV, configXML, className);

      for (int i = 0; i < length; i++)
      {
        tagName = listItem.item(i).getNodeName();

        if (tagName.equals("#text") || !listItem.item(i).hasAttributes())
        {
          continue;
        }

        attr = listItem.item(i).getAttributes();

        if (attr.getNamedItem("aatocv") != null)
        {
          tmpNodeList = getDocumentFromString(xml.toString()).getElementsByTagName(tagName);

          if (tmpNodeList.getLength() != 0)
          {
            setAAtoCV(objectCV, attr, tmpNodeList, classCV, tagName);
          }
          continue;
        }

        if (attr.getNamedItem("xml") != null)
        {
          configForQBXML = getDocumentFromString(getDocumentFromString(attr.getNamedItem("xml").getNodeValue()).getElementsByTagName(attr.getNamedItem("tag").getNodeValue()).item(0).toString());
          tmpNodeList = getDocumentFromString(xml.toString()).getElementsByTagName(tagName);

          if (tmpNodeList.item(0) != null)
          {
            forward(objectCV, attr, tmpNodeList.item(0), classCV, className, configForQBXML);
          }
          continue;
        }

        value = getValueOfNodeByTagName(xml, tagName);

        if (value == null)
          continue;

        typeArg = attr.getNamedItem("type").getNodeValue();

        argumentType[0] = getArgType(typeArg);
        arguments[0] = getObjectByName(typeArg, value);

        method = classCV[0].getMethod(attr.getNamedItem("set").getNodeValue(), argumentType);
        method.invoke(objectCV, arguments);
      }
    } catch (Exception e)
    {
      logger.error("QBXMLTag = <" + tagName + "> ********* Error while loading up/down classes : ", e);
    }

    return objectCV;
  }
  /**
   * This Method is for setting CVid
   * by ExternalID
   * @param objectCV ObjectCV
   * @param attr NamedNodeMap Attributes for current field
   * @param xml Node fragment QB XML
   * @param classCV Class[]
   * @param tagName String current tag
   */
  public void setAAtoCV(Object objectCV, NamedNodeMap attr, NodeList list, Class classCV[], String tagName)
  {
    try
    {
      Class thisClass = Class.forName("com.centraview.qbsync.QBXMLConverter");
      Class twoInputs[] = new Class[2];
      Class oneInput[] = new Class[1];

      Object thisObj = thisClass.newInstance();
      Object argList[] = new Object[2];
      Object oneArg[] = new Object[1];
      Object ret = null;

      Node xml = null;
      oneInput[0] = Class.forName("java.lang.String");
      twoInputs[0] = Class.forName("java.lang.String");
      twoInputs[1] = Class.forName("java.lang.String");

      Method method = null;

      if (attr.getNamedItem("aatocv").getNodeValue().equals("getIDFromEXID"))
      {
        xml = list.item(0);
        argList[0] = attr.getNamedItem("table").getNodeValue();
        argList[1] = getRecord(xml.toString(), TAG_LISTID);
        method = thisClass.getMethod("getIDFromEXID", twoInputs);
        ret = method.invoke(thisObj, argList);

        if (ret != null)
        {
          Object arg[] = new Object[1];

          arg[0] = getObjectByName(attr.getNamedItem("type").getNodeValue(), ret.toString());
          oneInput[0] = getArgType(attr.getNamedItem("type").getNodeValue());

          method = classCV[0].getMethod(attr.getNamedItem("set").getNodeValue(), oneInput);
          method.invoke(objectCV, arg);
        }
      } else if (attr.getNamedItem("aatocv").getNodeValue().equals("getValueFromTags"))
      {
        Object arg[] = new Object[1];
        xml = list.item(0);
        arg[0] = getRecord(xml.toString(), tagName);
        oneInput[0] = getArgType("java.lang.String");
        method = thisClass.getMethod(attr.getNamedItem("aatocv").getNodeValue(), oneInput);

        ret = method.invoke(thisObj, arg);
        arg[0] = (String)ret;

        method = classCV[0].getMethod(attr.getNamedItem("set").getNodeValue(), oneInput);
        method.invoke(objectCV, arg);
      } else if (attr.getNamedItem("aatocv").getNodeValue().equals("getItemLinesFromTags"))
      {
        oneArg[0] = getItemLinesFromTags(list);
        oneInput[0] = Class.forName(attr.getNamedItem("type").getNodeValue());
        method = classCV[0].getMethod(attr.getNamedItem("set").getNodeValue(), oneInput);
        method.invoke(objectCV, oneArg);

      } else
      {
        xml = list.item(0);
        oneInput[0] = getArgType(attr.getNamedItem("type").getNodeValue());
        oneArg[0] = getRecord(xml.toString(), TAG_LISTID);
        method = classCV[0].getMethod(attr.getNamedItem("aatocv").getNodeValue(), oneInput);
        method.invoke(objectCV, oneArg);
      }

    } catch (Exception e)
    {
      logger.error("[setAAtoCV] QBXML TAG: <" + tagName + ">", e);
    }
  }
  /**
   * This Method is for getting ExternalID by CVID
   * and returned fragment QB XML
   * <ListID>ExternalID</ListID>
   * @param objectCV Object
   * @param attr NamedNodeMap Attributes for current field
   * @param classCV Class[]
   * @param tagName String current tag
   * @return String
   */
  public String getAAfromCV(Object objectCV, NamedNodeMap attr, Class classCV[], String tagName)
  {
    String retXML = "";

    try
    {
      Class thisClass = Class.forName("com.centraview.qbsync.QBXMLConverter");
      Class twoInputs[] = new Class[2];
      Class oneInput[] = new Class[1];

      Object thisObj = thisClass.newInstance();
      Object argList[] = new Object[2];
      Object ret = null;

      twoInputs[0] = Class.forName("java.lang.String");

      Method method = null;
      String tmpXML = "";

      if (attr.getNamedItem("cvtoaa").getNodeValue().equals("getEXIDFromID"))
      {
        method = classCV[0].getMethod(attr.getNamedItem("get").getNodeValue(), null);
        ret = method.invoke(objectCV, null);

        if (ret != null)
        {
          argList[0] = attr.getNamedItem("table").getNodeValue();
          argList[1] = getObjectByName("int", ret.toString());

          twoInputs[1] = Integer.TYPE;
          method = thisClass.getMethod("getEXIDFromID", twoInputs);
          ret = method.invoke(thisObj, argList);

          if (ret != null)
          {
            if (!ret.toString().equals(""))
              tmpXML = "<" + tagName + "> \n" + "<" + TAG_LISTID + ">" + ret + "</" + TAG_LISTID + ">" + "</" + tagName + "> \n";
          }
        }
      } else if (attr.getNamedItem("cvtoaa").getNodeValue().equals("getTagsFromItemLines"))
      {
        ItemLines itemLines = null;
        method = classCV[0].getMethod("getItemLines", null);
        itemLines = (ItemLines)method.invoke(objectCV, null);
        tmpXML = getTagsFromItemLines(itemLines, tagName);
      } else
      {
        method = classCV[0].getMethod(attr.getNamedItem("get").getNodeValue(), null);
        ret = method.invoke(objectCV, null);

        Object arg[] = new Object[1];

        if (ret != null)
        {
          oneInput[0] = getArgType("java.lang.String");
          arg[0] = getObjectByName("java.lang.String", ret.toString());

          method = thisClass.getMethod(attr.getNamedItem("cvtoaa").getNodeValue(), oneInput);
          ret = method.invoke(thisObj, arg);
          if (!ret.toString().equals(""))
            tmpXML = "<" + tagName + "> \n" + ret.toString() + "</" + tagName + "> \n";
        }
      }
      retXML = tmpXML;
    } catch (Exception e)
    {
      logger.error("[getAAfromCV] Exception thrown.", e);
    }
    return retXML;
  }
  /**
   * This Method getting Class
   * for Type
   * @param type String
   * @return Class
   */
  public Class getArgType(String type)
  {
    Class argType = null;

    try
    {
      if (type.equals("boolean"))
      {
        argType = Boolean.TYPE;
      } else if (type.equals("int"))
      {
        argType = Integer.TYPE;
      } else if (type.equals("long"))
      {
        argType = Long.TYPE;
      } else if (type.equals("float"))
      {
        argType = Float.TYPE;
      } else if (type.equals("double"))
      {
        argType = Double.TYPE;
      } else
      {
        argType = Class.forName(type);
      }
    } catch (Exception e)
    {
      logger.error("[getArgType] Exception thrown.", e);
    }

    return argType;
  }
  /**
   * This Method make forward in new Config
   * with new ObjectCV field
   * @param objectCV Object ObjectCV
   * @param attr NamedNodeMap Attributes for current field
   * @param xml Node fragment QB XML
   * @param classCV Class[]
   * @param className String
   * @param config Document config for QB XML
   */
  public void forward(Object objectCV, NamedNodeMap attr, Node xml, Class classCV[], String className, Document config)
  {
    try
    {
      Method method = null;
      Object obj = null;
      String classNameField = "";
      Class argumentType[] = null;

      Node get = attr.getNamedItem("get");

      if (get == null)
      {
        next(objectCV, config.getFirstChild(), xml, className, attr);
      } else
      {
        classNameField = attr.getNamedItem("type").getNodeValue();
        method = classCV[0].getMethod(get.getNodeValue(), argumentType);
        obj = method.invoke(objectCV, argumentType);

        next(obj, config.getFirstChild(), xml, classNameField, attr);
      }
    } catch (Exception e)
    {
      logger.error("[forward] Error while loading up/down classes", e);
    }
  }
  /**
   * This Method fills ObjectCV field
   * @param objectCV ObjectCV field
   * @param config Node config for ObjectCV field
   * @param qbXML Node fragment QB XML
   * @param className String Class Name ObjectCV field
   */
  public void next(Object objectCV, Node config, Node qbXML, String className, NamedNodeMap att)
  {
    NodeList listItem = config.getChildNodes();

    Class classCV[] = new Class[1];
    Class argumentType[] = new Class[1];
    Object arguments[] = new Object[1];

    Method method = null;
    int length = listItem.getLength();

    String typeArg = "";
    String value = "";
    String tagName = "";

    Node node = att.getNamedItem("isprimary");
    try
    {
      classCV[0] = Class.forName(className);
      if (node != null)
      {
        argumentType[0] = Class.forName("java.lang.String");
        Method setPrim = classCV[0].getMethod("setIsPrimary", argumentType);
        arguments[0] = node.getNodeValue();
        setPrim.invoke(objectCV, arguments);
      }

      for (int i = 0; i < length; i++)
      {
        tagName = listItem.item(i).getNodeName();

        if (tagName.equals("#text") || !listItem.item(i).hasAttributes())
          continue;

        NamedNodeMap attr = listItem.item(i).getAttributes();
        typeArg = attr.getNamedItem("type").getNodeValue();
        argumentType[0] = getArgType(typeArg);

        value = getValueOfNodeByTagName(qbXML, tagName);

        arguments[0] = getObjectByName(typeArg, value);

        method = classCV[0].getMethod(attr.getNamedItem("set").getNodeValue(), argumentType);
        method.invoke(objectCV, arguments);
      }
    } catch (Exception e)
    {
      logger.error("[next] Error while loading up/down classes", e);
    }
  }
  /**
   * This Method make forward in new Config
   * with ObjectCV field
   * @param classCV Class[]
   * @param attr NamedNodeMap Attributes for current field
   * @param objectCV Object current ObjectCV
   * @param className String current ObjectCV Class Name
   * @param config Document config for QB XML
   * @return String fragment QB XML
   */
  public String forwardXML(Class classCV[], NamedNodeMap attr, Object objectCV, String className, Document config)
  {
    String retXML = "";

    try
    {
      Method method = null;
      Object obj = null;
      String classNameField = "";
      Class argumentType[] = null;

      Node get = attr.getNamedItem("get");

      if (get == null)
      {
        retXML = nextXML(objectCV, config.getFirstChild(), className);
      } else
      {
        classNameField = attr.getNamedItem("type").getNodeValue();
        method = classCV[0].getMethod(get.getNodeValue(), argumentType);
        obj = method.invoke(objectCV, argumentType);
        retXML = nextXML(obj, config.getFirstChild(), classNameField);
      }
    } catch (Exception e)
    {
      logger.error("[forwardXML] Exception thrown.", e);
    }

    return retXML;
  }
  /**
   * This Method returned fragment QB XML
   * for objectCV
   * @param objectCV Object current ObjectCV
   * @param configXML Node config for QB XML
   * @return String fragment QB XML
   */
  public String getQBXMLFromVO(Object objectCV, Node configXML, HashMap hm)
  {
    String xml = "";

    String tagName = "";

    try
    {

      Document configForQBXML;

      Object ret = null;
      Method method = null;
      String value = "";
      Class classCV[] = new Class[1];

      StringTokenizer st;
      String tmp = "";
      String tmpXML = "";
      String className = getValueOfNodeByTagName(configXML, TAG_VONAME);
      classCV[0] = Class.forName(className);

      Node met = null;
      NodeList listItem = configXML.getChildNodes();

      int length = listItem.getLength();

      for (int i = 0; i < length; i++)
      {

        tagName = listItem.item(i).getNodeName();

        if (tagName.equals("#text") || !listItem.item(i).hasAttributes())
          continue;

        NamedNodeMap attr = listItem.item(i).getAttributes();

        if (attr.getNamedItem("cvtoaa") != null)
        {
          tmpXML = getAAfromCV(objectCV, attr, classCV, tagName);

          if (!tmpXML.trim().equals(""))
            xml += tmpXML;

          continue;
        }

        if (attr.getNamedItem("xml") != null)
        {
          configForQBXML = getDocumentFromString(getDocumentFromString(attr.getNamedItem("xml").getNodeValue()).getElementsByTagName(attr.getNamedItem("tag").getNodeValue()).item(0).toString());

          tmpXML = forwardXML(classCV, attr, objectCV, className, configForQBXML);

          if (!tmpXML.trim().equals(""))
            xml += "<" + tagName + ">" + tmpXML + "</" + tagName + "> \n";

          continue;
        }
        met = attr.getNamedItem("get");
        method = classCV[0].getMethod(met.getNodeValue(), null);
        ret = method.invoke(objectCV, null);

        if (ret != null)
        {
          value = ret.toString();
          if (met.getNodeValue().equalsIgnoreCase("getExternalID"))
          {
            xml += "<" + tagName + ">" + value + "</" + tagName + "> \n";
            xml += "<EditSequence>" + hm.get(value) + "</EditSequence> \n";

            continue;
          }
        } else
          continue;

        if (value.equals(""))
          continue;

        if (tagName.equals("OpenBalance"))
        {
          st = new StringTokenizer(value, ".");

          value = st.nextToken();
          if (!st.hasMoreElements())
            value += ".00";
          else
          {
            tmp = st.nextToken();
            if (tmp.length() < 2)
              tmp += "0";
            value += "." + tmp;
          }
        }

        xml += "<" + tagName + ">" + value + "</" + tagName + "> \n";

      }
    } catch (Exception e)
    {
      logger.error("[getQBXMLFromVO] QBXML TAG:' <" + tagName + "> '  Error while loading up/down classes", e);
    }

    return xml;
  }
  /**
   * This Method returned fragment QB XML
   * from ObjectCV field
   * @param objectCV Object ObjectCV field
   * @param configXML Node config for ObjectCV field
   * @param className String Class Name ObjectCV field
   * @return String fragment QB XML
   */
  public String nextXML(Object objectCV, Node configXML, String className)
  {
    String retxml = "";

    try
    {
      Method method = null;

      String tagName = "";
      String value = "";
      String tmpXML = "";

      Class classCV[] = new Class[1];
      classCV[0] = Class.forName(className);

      NodeList listItem = configXML.getChildNodes();

      int length = listItem.getLength();

      for (int i = 0; i < length; i++)
      {
        tagName = listItem.item(i).getNodeName();

        if (tagName.equals("#text"))
          continue;

        NamedNodeMap attr = listItem.item(i).getAttributes();

        if (attr.getNamedItem("cvtoaa") != null)
        {
          tmpXML = getAAfromCV(objectCV, attr, classCV, tagName);

          if (!tmpXML.trim().equals(""))
            retxml += tmpXML;

          continue;
        }

        method = classCV[0].getMethod(attr.getNamedItem("get").getNodeValue(), null);

        if (method.invoke(objectCV, null) != null)
        {
          value = method.invoke(objectCV, null).toString();
        } else
        {
          continue;
        }

        if (!value.trim().equals(""))
          retxml += "<" + tagName + ">" + value + "</" + tagName + "> \n";
      }
    } catch (Exception e)
    {
      logger.error("[nextXML] Exception thrown.", e);
    }

    return retxml;
  }

  /**
   * This Method is for getting QBXML
   * from ArrayList.
   * @param objectsCV ArrayList ObjectCV List
   * @param nameQBXML String name QB XML
   * @return String QB XML
   */

  public String getQBXMLFromVOList(ArrayList objectsCV, String nameQBXML, String path, HashMap hm, String operation)
  {
    String qbXML = "";

    try
    {
      filePath = path;
      String configName = "";

      if (operation.equalsIgnoreCase("ADD"))
      {
        configName = FILE_CONFIG_ADD;
      } else
      {
        configName = FILE_CONFIG_MOD;
      }

      Document configForQBXML = getDocumentFromString(getDocumentFromString(configName).getElementsByTagName(nameQBXML).item(0).toString());

      String tag = getValueOfNodeByTagName(configForQBXML.getFirstChild(), TAG_SEPARATOR);

      int length = objectsCV.size();
      int requestID = 0;

      String xmlMod = "";
      String xmlAdd = "";
      String tempXML = "";

      Node config = configForQBXML.getFirstChild();

      for (int i = 0; i < length; i++)
      {
        tempXML = getQBXMLFromVO(objectsCV.get(i), config, hm);

        if (operation.equalsIgnoreCase("ADD"))
        {
          xmlAdd += "<" + tag + "AddRq requestID = \"" + requestID + "\"> \n <" + tag + "Add> \n" + tempXML + "</" + tag + "Add> \n" + "</" + tag + "AddRq> \n";
          requestID++;
        } else
        {
          xmlMod += "<" + tag + "ModRq requestID = \"" + (length - i + 1) + "\"> \n <" + tag + "Mod> \n" + tempXML + "</" + tag + "Mod> \n" + "</" + tag + "ModRq> \n";
        }
      }
      if (!xmlMod.equals("") || !xmlAdd.equals(""))
      {
        qbXML = XML_HEADER + xmlAdd + xmlMod + XML_FOOTER;
      }
    } catch (Exception e)
    {
      logger.error("[getQBXMLFromVOList] Exception thrown.", e);
    }

    return qbXML;
  }
  /**
   * This Method is for getting ObjectCV
   * by Class Name&Value
   * @param className String Class Name
   * @param value String Value
   * @return Object
   */
  public Object getObjectByName(String className, String value)
  {

    if (className.equals("java.sql.Timestamp"))
    {
      value = value.replace('T', ' ');
      value = value.replace('+', ' ');
      value = value.substring(0, 19);

      return Timestamp.valueOf(value);
    } else if (className.equals("int") || className.equals("java.lang.Integer"))
    {
      StringTokenizer st = new StringTokenizer(value, ".");
      value = st.nextToken();
      return new Integer(value);
    } else if (className.equals("boolean") || className.equals("java.lang.Boolean"))
    {
      return new Boolean(value);
    } else if (className.equals("double") || className.equals("java.lang.Double"))
    {
      StringTokenizer st = new StringTokenizer(value, ",");
      value = st.nextToken();

      if (st.hasMoreElements())
        value += st.nextToken();

      return new Double(value);
    } else if (className.equals("float") || className.equals("java.lang.Float"))
    {
      StringTokenizer st = new StringTokenizer(value, ",");
      value = st.nextToken();

      if (st.hasMoreElements())
        value += st.nextToken();

      return new Float(value);
    }

    else if (className.equals("java.sql.Date"))
    {
      java.sql.Date date = java.sql.Date.valueOf(value);
      return new java.sql.Date(date.getYear() + 1900, date.getMonth(), date.getDate());
    } else if (className.equals("java.util.Date"))
    {
      value = value.replace('T', ' ');
      value = value.replace('+', ' ');
      value = value.substring(0, 19);

      return new java.util.Date(Timestamp.valueOf(value).getTime());
    } else if (className.equals("long") || className.equals("java.lang.Long"))
    {
      StringTokenizer st = new StringTokenizer(value, ",");
      value = st.nextToken();

      if (st.hasMoreElements())
        value += st.nextToken();

      return new Long(value);
    }

    return value;
  }
  /**
   * This Method is for getting Value
   * from <tag>value</tag>
   * @param node Node fragment XML
   * @param tag String Tag
   * @return String Value
   */
  public String getValueOfNodeByTagName(Node node, String tag)
  {
    Node nextNode = null;
    String value = "";

    try
    {

      if (node.getNodeName().equals(tag))
      {
        return node.getFirstChild().getNodeValue();
      }

      if (node.hasChildNodes())
      {
        nextNode = node.getFirstChild();

        while (nextNode != null)
        {
          value = getValueOfNodeByTagName(nextNode, tag);
          nextNode = nextNode.getNextSibling();

          if (value != null)
          {
            return value;
          }
        }
      }
    } catch (Exception e)
    {
      System.out.print("");
    }
    return null;
  }
  /**
   * This Method is for getting Document
   * from String value
   * @param xml String fragment XML
   * @return Document
   */
  public Document getDocumentFromString(String xml)
  {

    Document returnDocument = null;
    try
    {

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      factory.setValidating(true);
      DocumentBuilder builder = factory.newDocumentBuilder();

      ErrorHandler errorHandler = new ErrorHandler() {

        public void warning(org.xml.sax.SAXParseException e)
        {
        //  System.err.println("Warning : " + e.getMessage());
        }

        public void error(org.xml.sax.SAXParseException e)
        {
        // System.err.println("Error : " + e.getMessage());
        }

        public void fatalError(org.xml.sax.SAXParseException e)
        {
        //  System.err.println("FatalError : " + e.getMessage());
        }
      };
      builder.setErrorHandler(errorHandler);

      if (xml.substring(xml.length() - 3, xml.length()).equals("xml"))
      {
        returnDocument = builder.parse(new File(filePath + xml));
      } else
      {
        InputStream in = new BufferedInputStream(new StringBufferInputStream(xml));
        returnDocument = builder.parse(in);
      }
    } catch (IOException e)
    {
      logger.error("[getDocumentFromString] Exception thrown.", e);
    } catch (ParserConfigurationException e)
    {
      logger.error("[getDocumentFromString] Exception thrown.", e);
    } catch (SAXException e)
    {
      logger.error("[getDocumentFromString] Exception thrown.", e);
    }

    return returnDocument;
  }

  /**
   * This Method returned qbXML Name.
   * @param node Node QB XML
   * @return String Name for QB XML
   */
  public String getQBXMLName(Node node)
  {
    Node nextFils = null;
    String name;
    try
    {
      if (node.hasAttributes())
      {
        NamedNodeMap atts;
        atts = node.getAttributes();

        if (atts.getNamedItem(ATTR_REQUEST_ID) != null)
        {

          return node.getNodeName();
        }
      }
      if (node.hasChildNodes())
      {
        nextFils = node.getFirstChild();
        while (nextFils != null)
        {
          name = getQBXMLName(nextFils);
          nextFils = nextFils.getNextSibling();
          if (name != null)
          {
            return name;

          }
        }
      }
    } catch (Exception e)
    {
      logger.error("[getQBXMLName] Exception thrown.", e);
    }
    return null;
  }
  /**
   * This Method is for getting fragment XML
   * from <tag> ... </tag>
   * @param resultXML String XML
   * @param delimiter String Tag
   * @return String fragment XML
   */
  public String getRecord(String resultXML, String delimiter)
  {
    String records = "";

    String opengTag = "<" + delimiter + ">";
    String closingTag = "</" + delimiter + ">";

    int indexOfOpngTag = -1;
    int indexOfClsgTag = -1;
    int lenOpngTag = opengTag.length();
    int lenClosTag = closingTag.length();
    indexOfOpngTag = resultXML.indexOf(opengTag);

    indexOfClsgTag = resultXML.indexOf(closingTag, indexOfOpngTag);

    int i = 0;
    while (indexOfOpngTag != -1)
    {
      records += opengTag + resultXML.substring(indexOfOpngTag + lenOpngTag, indexOfClsgTag) + closingTag;

      indexOfOpngTag = resultXML.indexOf(opengTag, indexOfClsgTag);

      indexOfClsgTag = resultXML.indexOf(closingTag, indexOfOpngTag);

      i++;
    }

    return records.substring(lenOpngTag, (records.length() - lenClosTag));
  }
  /**
   *This Method is for getting QB Address Tag values
   *from Address Value of VO. For ex, BillToAddress of
   *PO VO is String so by spliting it we can get QB
   *Address tag values
   *@param String value for VO
   *@return String QB Address Tag
   */
  public String getTagsFromValue(String value)
  {
    String qbTags = "";

    StringTokenizer st = new StringTokenizer(value, ",");

    int i = 0;
    String temp = "";
    while (st.hasMoreTokens())
    {
      temp = (String)st.nextToken();

      if (i == 0 && temp.length() != 0)
        qbTags = qbTags + "<Addr1>" + temp + "</Addr1>";
      else if (i == 1 && temp.length() != 0)
        qbTags = qbTags + "<Addr2>" + temp + "</Addr2>";
      else if (i == 2 && temp.length() != 0)
        qbTags = qbTags + "<City>" + temp + "</City>";
      else if (i == 3 && temp.length() != 0)
        qbTags = qbTags + "<State>" + temp + "</State>";

      i++;
    }

    return qbTags;
  }

  /**
   *This Method is for getting Address Value of VO
   *from QB Address Tag values. For ex, BillToAddress of
   *PO VO is String so by concataning QB Address tag values
   *we can get this BillToAddress value
   *@param String QB Address Tag
   *@return String value for VO
   */
  public String getValueFromTags(String rsXml)
  {
    String voValue = "";

    int i = rsXml.indexOf("<Addr1>");
    int j = 0;

    if (i != -1)
    {
      j = rsXml.indexOf("</Addr1>", i);
      voValue = rsXml.substring(i + 7, j);
    }

    i = rsXml.indexOf("<Addr2>");
    if (i != -1)
    {
      j = rsXml.indexOf("</Addr2>", i);
      voValue = voValue + "," + rsXml.substring(i + 7, j);
    }

    i = rsXml.indexOf("<City>");
    if (i != -1)
    {
      j = rsXml.indexOf("</City>", i);
      voValue = voValue + "," + rsXml.substring(i + 6, j);
    }

    i = rsXml.indexOf("<State>");
    if (i != -1)
    {
      j = rsXml.indexOf("</State>", i);
      voValue = voValue + "," + rsXml.substring(i + 7, j);
    }

    return voValue;
  }
  /**
   *This Method is for getting ItemLines object
   *from QB X-LineRet tag values
   *@param String QB X-LineRet Tag values
   *@return ItemLines
   */
  public ItemLines getItemLinesFromTags(NodeList list)
  {
    ItemLines itemLines = new ItemLines();

    int length = list.getLength();

    Document document = null;

    for (int j = 0; j < length; j++)
    {
      document = getDocumentFromString(list.item(j).toString());

      ItemElement ie = new ItemElement();

      int itemId = 0;

      String sku = "";
      String externalID = "";
      String cvID = "";
      if (document.getElementsByTagName("ItemRef").getLength() != 0)
      {
        sku = getValueOfNodeByTagName(document.getElementsByTagName("ItemRef").item(0), "FullName");
        externalID = getValueOfNodeByTagName(document.getElementsByTagName("ItemRef").item(0), "ListID");
        cvID = getIDFromEXID("Item", externalID);

        if (!cvID.equals(""))
          itemId = Integer.parseInt(cvID);
      }
      if (cvID.equals("0") || cvID.equals(""))
        continue;
      IntMember ItemId = new IntMember("ItemId", itemId, 'D', "", 'T', false, 20);

      StringMember SKU = new StringMember("SKU", sku, 'D', "", 'T', false);

      String desc = "";
      if (document.getElementsByTagName("Desc").getLength() != 0)
      {
        desc = getValueOfNodeByTagName(document.getFirstChild(), "Desc");
      }

      StringMember Description = new StringMember("Description", desc, 'D', "", 'T', false);

      String qty = "0";

      if (document.getElementsByTagName("Quantity").getLength() != 0)
      {
        qty = getValueOfNodeByTagName(document.getFirstChild(), "Quantity");

      }

      FloatMember Quantity = new FloatMember("Quantity", new Float(qty), 'D', "0", 'T', false, 20);

      String priceEach = "0";
      if (document.getElementsByTagName("Rate").getLength() != 0)
      {
        priceEach = getValueOfNodeByTagName(document.getFirstChild(), "Rate");
      }

      FloatMember PriceEach = new FloatMember("PriceEach", new Float(priceEach), 'D', "", 'T', false, 20);

      String priceExt = "0";

      if (document.getElementsByTagName("PriceExtended").getLength() != 0)
      {
        priceExt = getValueOfNodeByTagName(document.getFirstChild(), "PriceExtended");
      }

      FloatMember PriceExtended = new FloatMember("PriceExtended", new Float(priceExt), 'D', "", 'T', false, 20);
      FloatMember UnitTax = new FloatMember("UnitTax", new Float("1"), 'D', "", 'T', false, 20);
      FloatMember unitTaxrate = new FloatMember("UnitTax", new Float("1"), 'D', "", 'T', false, 20);
      ie.put("ItemId", ItemId);
      ie.put("UnitTaxrate", unitTaxrate);
      ie.put("UnitTax", UnitTax);
      ie.put("SKU", SKU);
      ie.put("Description", Description);
      ie.put("Quantity", Quantity);
      ie.put("PriceEach", PriceEach);
      ie.put("PriceExtended", PriceExtended);
      ie.setLineStatus("New");
      itemLines.put(new Integer(j), ie);
    }
    //   itemLines.calculate();
    return itemLines;
  }

  /**
   *This Method is for getting QB X-LineRet tag values
   *from ItemLines object
   *@param ItemLines
   *@param String X in X-LineRet ie PurchaseOrderLineRet
   *@return String QB X-LineRet Tag values
   */
  public String getTagsFromItemLines(ItemLines itemLines, String tagName)
  {
    String qbTags = "";

    Set listkey = itemLines.keySet();
    Iterator it = listkey.iterator();
    String itemExternalID = "";
    int id = 0;
    String fmt = "0.00#";
    DecimalFormat df = new DecimalFormat(fmt, new DecimalFormatSymbols(Locale.US));

    Integer itemID = new Integer(1);

    while (it.hasNext())
    {

      Object obj = it.next();

      itemID = Integer.valueOf(obj.toString());

      ItemElement ele = (ItemElement)itemLines.get(itemID.toString());

      IntMember ItemId = (IntMember)ele.get("ItemId");

      id = Integer.valueOf(ItemId.getDisplayString()).intValue();
      //itemID = (Integer)ItemId.getMemberValue();
      itemExternalID = getEXIDFromID("Item", id);

      StringMember sku = (StringMember)ele.get("SKU");

      FloatMember qty = (FloatMember)ele.get("Quantity");

      FloatMember priceEach = (FloatMember)ele.get("PriceEach");

      FloatMember priceExe = (FloatMember)ele.get("PriceExtended");

      qbTags += "<" + tagName + "> \n" + "<ItemRef> \n" + "<ListID>" + itemExternalID + "</ListID> \n" + "</ItemRef> \n" + "<Quantity>" + df.format(Float.valueOf(qty.getMemberValue().toString())) + "</Quantity> \n" + "<Rate>" + df.format(Float.valueOf(priceEach.getMemberValue().toString())) + "</Rate> \n" + "<Amount>"
          + df.format(Float.valueOf(priceExe.getMemberValue().toString())) + "</Amount> \n" + "</" + tagName + "> \n";
    }
    return qbTags;
  }

  /**
   * @author Kevin McAllister <kevin@centraview.com>
   * This simply sets the target datasource to be used for DB interaction
   * @param ds A string that contains the cannonical JNDI name of the datasource
   */
  public void setDataSource(String ds)
  {
    this.dataSource = ds;
  }
  
}