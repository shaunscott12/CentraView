/*
 * $RCSfile: LicenseUtil.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:28:04 $ - $Author: mking_cv $
 *
 * The contents of this file are subject to the Open Software License
 * Version 2.1 ("the License"); you may not use this file except in
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

package com.centraview.license;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;

import com.centraview.administration.serversettings.ServerSettings;
import com.centraview.administration.serversettings.ServerSettingsHome;
import com.centraview.administration.serversettings.ServerSettingsVOX;
import com.centraview.common.CVUtility;
import com.centraview.common.CentraViewConfiguration;
import com.centraview.contact.contactfacade.ContactFacade;
import com.centraview.contact.contactfacade.ContactFacadeHome;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.individual.IndividualVO;
import com.centraview.settings.Settings;
import com.centraview.settings.SettingsInterface;

/**
 * @author Kevin McAllister <kevin@centraview.com>
 */
public class LicenseUtil
{

  /** CentraView's Public RSA Key.*/
  private final static byte[] PUBLIC_KEY = new byte[] {
    (byte) 0x00000030, (byte) 0x00000082, (byte) 0x00000001, (byte) 0x00000022, (byte) 0x00000030, (byte) 0x0000000d,
    (byte) 0x00000006, (byte) 0x00000009, (byte) 0x0000002a, (byte) 0x00000086, (byte) 0x00000048, (byte) 0x00000086,
    (byte) 0x000000f7, (byte) 0x0000000d, (byte) 0x00000001, (byte) 0x00000001, (byte) 0x00000001, (byte) 0x00000005,
    (byte) 0x00000000, (byte) 0x00000003, (byte) 0x00000082, (byte) 0x00000001, (byte) 0x0000000f, (byte) 0x00000000,
    (byte) 0x00000030, (byte) 0x00000082, (byte) 0x00000001, (byte) 0x0000000a, (byte) 0x00000002, (byte) 0x00000082,
    (byte) 0x00000001, (byte) 0x00000001, (byte) 0x00000000, (byte) 0x000000a7, (byte) 0x00000094, (byte) 0x00000056,
    (byte) 0x00000099, (byte) 0x000000f3, (byte) 0x00000024, (byte) 0x000000ce, (byte) 0x0000007a, (byte) 0x00000035,
    (byte) 0x000000ac, (byte) 0x0000005b, (byte) 0x000000da, (byte) 0x000000d2, (byte) 0x000000f4, (byte) 0x00000045,
    (byte) 0x00000081, (byte) 0x00000021, (byte) 0x00000001, (byte) 0x000000e8, (byte) 0x00000026, (byte) 0x00000013,
    (byte) 0x000000c9, (byte) 0x0000003f, (byte) 0x00000055, (byte) 0x000000e7, (byte) 0x000000f9, (byte) 0x0000000f,
    (byte) 0x000000f1, (byte) 0x000000e3, (byte) 0x0000004b, (byte) 0x000000fe, (byte) 0x00000069, (byte) 0x00000014,
    (byte) 0x00000055, (byte) 0x000000c9, (byte) 0x00000063, (byte) 0x00000090, (byte) 0x00000084, (byte) 0x000000d8,
    (byte) 0x0000006b, (byte) 0x00000079, (byte) 0x000000c8, (byte) 0x00000001, (byte) 0x000000a1, (byte) 0x000000c4,
    (byte) 0x000000a9, (byte) 0x000000ff, (byte) 0x00000042, (byte) 0x000000a7, (byte) 0x0000005e, (byte) 0x0000002a,
    (byte) 0x000000a1, (byte) 0x0000007c, (byte) 0x0000005e, (byte) 0x00000045, (byte) 0x0000004d, (byte) 0x0000007a,
    (byte) 0x00000075, (byte) 0x000000c2, (byte) 0x00000012, (byte) 0x00000011, (byte) 0x000000fa, (byte) 0x000000bc,
    (byte) 0x000000f3, (byte) 0x00000086, (byte) 0x00000017, (byte) 0x000000c2, (byte) 0x000000d1, (byte) 0x000000ce,
    (byte) 0x00000086, (byte) 0x000000e1, (byte) 0x00000029, (byte) 0x000000ce, (byte) 0x0000006a, (byte) 0x0000009a,
    (byte) 0x00000052, (byte) 0x00000083, (byte) 0x00000068, (byte) 0x00000036, (byte) 0x0000006a, (byte) 0x000000bd,
    (byte) 0x000000d7, (byte) 0x00000022, (byte) 0x000000e3, (byte) 0x000000ff, (byte) 0x00000069, (byte) 0x0000007e,
    (byte) 0x000000f6, (byte) 0x000000d5, (byte) 0x00000094, (byte) 0x00000051, (byte) 0x000000f3, (byte) 0x000000b5,
    (byte) 0x0000008e, (byte) 0x000000cc, (byte) 0x000000a1, (byte) 0x0000005c, (byte) 0x00000047, (byte) 0x00000009,
    (byte) 0x000000ed, (byte) 0x0000006a, (byte) 0x00000019, (byte) 0x00000024, (byte) 0x00000005, (byte) 0x0000007f,
    (byte) 0x00000021, (byte) 0x0000001e, (byte) 0x000000a9, (byte) 0x000000a5, (byte) 0x000000b9, (byte) 0x0000004b,
    (byte) 0x0000008a, (byte) 0x0000006f, (byte) 0x0000004c, (byte) 0x0000006a, (byte) 0x00000024, (byte) 0x0000009b,
    (byte) 0x0000002d, (byte) 0x000000c9, (byte) 0x000000de, (byte) 0x00000063, (byte) 0x000000b1, (byte) 0x000000c4,
    (byte) 0x0000000a, (byte) 0x00000094, (byte) 0x00000058, (byte) 0x000000a1, (byte) 0x000000fb, (byte) 0x00000083,
    (byte) 0x00000040, (byte) 0x00000001, (byte) 0x000000db, (byte) 0x000000a6, (byte) 0x00000059, (byte) 0x00000024,
    (byte) 0x0000004a, (byte) 0x000000fe, (byte) 0x00000087, (byte) 0x0000007c, (byte) 0x000000fb, (byte) 0x000000d5,
    (byte) 0x0000009d, (byte) 0x000000ed, (byte) 0x000000b0, (byte) 0x000000b3, (byte) 0x00000009, (byte) 0x000000b7,
    (byte) 0x000000de, (byte) 0x0000003c, (byte) 0x00000060, (byte) 0x0000004c, (byte) 0x00000045, (byte) 0x000000a0,
    (byte) 0x000000fa, (byte) 0x0000006a, (byte) 0x000000f4, (byte) 0x00000092, (byte) 0x00000045, (byte) 0x00000028,
    (byte) 0x00000039, (byte) 0x000000c9, (byte) 0x00000059, (byte) 0x00000046, (byte) 0x00000011, (byte) 0x000000dc,
    (byte) 0x0000003b, (byte) 0x00000077, (byte) 0x00000056, (byte) 0x0000008c, (byte) 0x00000086, (byte) 0x0000006e,
    (byte) 0x0000002d, (byte) 0x00000022, (byte) 0x000000d8, (byte) 0x00000060, (byte) 0x0000002d, (byte) 0x0000005e,
    (byte) 0x000000d6, (byte) 0x00000074, (byte) 0x00000018, (byte) 0x0000007e, (byte) 0x00000016, (byte) 0x00000081,
    (byte) 0x0000001e, (byte) 0x000000e4, (byte) 0x000000ed, (byte) 0x00000026, (byte) 0x000000c5, (byte) 0x000000ff,
    (byte) 0x000000e0, (byte) 0x0000005d, (byte) 0x000000e2, (byte) 0x000000c1, (byte) 0x000000d6, (byte) 0x0000009d,
    (byte) 0x00000022, (byte) 0x00000054, (byte) 0x00000044, (byte) 0x000000d4, (byte) 0x000000e4, (byte) 0x0000007e,
    (byte) 0x00000022, (byte) 0x0000000b, (byte) 0x000000ca, (byte) 0x00000017, (byte) 0x0000009e, (byte) 0x00000041,
    (byte) 0x000000a2, (byte) 0x0000004d, (byte) 0x00000066, (byte) 0x00000098, (byte) 0x000000b7, (byte) 0x00000026,
    (byte) 0x00000064, (byte) 0x000000ab, (byte) 0x000000a6, (byte) 0x000000a0, (byte) 0x0000003c, (byte) 0x00000046,
    (byte) 0x0000001a, (byte) 0x000000fe, (byte) 0x000000f0, (byte) 0x000000c0, (byte) 0x000000fe, (byte) 0x00000025,
    (byte) 0x00000019, (byte) 0x00000080, (byte) 0x00000056, (byte) 0x0000009a, (byte) 0x00000066, (byte) 0x00000044,
    (byte) 0x00000086, (byte) 0x0000007b, (byte) 0x000000ec, (byte) 0x00000019, (byte) 0x000000cd, (byte) 0x00000038,
    (byte) 0x00000001, (byte) 0x000000d5, (byte) 0x00000013, (byte) 0x0000003a, (byte) 0x0000005c, (byte) 0x000000a7,
    (byte) 0x000000b4, (byte) 0x00000049, (byte) 0x00000054, (byte) 0x0000002a, (byte) 0x000000e5, (byte) 0x000000ca,
    (byte) 0x0000000d, (byte) 0x00000040, (byte) 0x000000da, (byte) 0x000000b7, (byte) 0x0000006b, (byte) 0x000000da,
    (byte) 0x0000000b, (byte) 0x00000002, (byte) 0x00000003, (byte) 0x00000001, (byte) 0x00000000, (byte) 0x00000001 };

  private final static int KEY_LENGTH = 256;

  /** The properties file MAC Address value Key. */
  private final static String PROPS_MAC_KEY = "MAC";

  /** The properties file Host Name value Key. */
  private final static String PROPS_HOST_NAME_KEY = "Host_Name";

  /** The properties file Timestamp value Key. */
  private final static String PROPS_TIMESTAMP_KEY = "Timestamp";

  /** The properties file License Key value Key. */
  private final static String PROPS_LICENSE_KEY = "License_Key";

  /** The properties file Entity Name value Key. */
  private final static String PROPS_ENTITY_KEY = "Entity";

  /** The properties file Individual Name value Key. */
  private final static String PROPS_INDIVIDUAL_KEY = "Individual";

  /** The properties file CentraView Version value Key. */
  private final static String PROPS_CV_VERSION_KEY = "CV_Version";

  /** The license file Status value Key. */
  private final static String LICENSE_FILE_STATUS_KEY = "Status";

  /** The license file Number of Users value Key. */
  private final static String LICENSE_FILE_USERS_KEY = "Users";

  /** The license file MAC Address value Key. */
  private final static String LICENSE_FILE_MAC_KEY = "MAC";

  /** The license file Host Name value Key. */
  private final static String LICENSE_FILE_HOST_NAME_KEY = "Host_Name";

  /** The license file Expiration Date value Key. */
  private final static String LICENSE_FILE_EXP_DATE_KEY = "Exp_Date";

  /** The license file SHA1 Validation value Key. */
  private final static String LICENSE_FILE_SHA_KEY = "Validate";

  /** The license file license key value Key. */
  private final static String LICENSE_KEY_KEY = "Key";

  /** The license file expires value Key. */
  private final static String LICENSE_EXPIRES_KEY = "Ex";

  /** The license file Offset value Key. */
  private final static String LICENSE_FILE_OFFSET_KEY = "Offset";

  /** The CentraView server web service URL. */
  private final static String WEB_SERVICE_URL
    = "http://license.centraview.com:8080/centraview/services/LicenseAdminSoapService?wsdl";

  /** The SessionContext interface for the instance. */
  protected SessionContext sessionContext;

  private final static boolean DEBUG = false;

  private final static String SERVER_DOWN_MESSAGE = "The CentraView License Server is down.";

  /** The license key file name */
  private final static String LICENSE_FILE_NAME = "CentraViewLicense.key";

  /** the log4j logger */
  private static Logger logger = Logger.getLogger(LicenseUtil.class);

  /*
   * This static block is here to ensure that the RSA Cipher
   * provider is added to the Java Security table.
   */
  static
  {
    Security.addProvider (new org.bouncycastle.jce.provider.BouncyCastleProvider());
  }

  /**
   * Reads in the license file. If the license file is not available for
   * whatever reason, it will try to obtain the license file from the CentraView
   * License Server.
   */
  public static void getLicenseFile(String dataSource)
  {
    logger.debug("getLicenseFile: Attempting to Read the license file from the filesystem.");
    LicenseInstanceVO licenseInstanceVO = new LicenseInstanceVO();
    int count = 0;
    while (count <= 1) {
      try {
        File centraViewLicenseFile = LicenseUtil.getCentraViewLicenseFile(dataSource);
        byte[] encyptedData = LicenseUtil.readLicenseFileFromDisk(centraViewLicenseFile);
        byte[] encryptedLicenseFile = LicenseUtil.getLicenseFromLicenseFile(encyptedData);
        byte[] encryptedKey = LicenseUtil.getKeyFromLicenseFile(encyptedData);
        Key blowfishKey = LicenseUtil.decryptBlowfishKey(encryptedKey);

        Properties licenseFile = LicenseUtil.decryptLicenseFile(blowfishKey, encyptedData);
        licenseInstanceVO.updateLicenseInformation(licenseFile);

        count++;
      } catch (Exception e) {
        // Something went wrong in reading the file.
        // We will give them another attempt to fetch the file and
        // read it in again.
        logger.debug("getLicenseFile: Unable to read the CentraViewLicense.key file.  count = "+count);
        if (count == 0) {
          logger.info("getLicenseFile: fetch a new License from the server.");
          LicenseUtil.fetchLicenseFile(dataSource);
        } else {
          logger.error("getLicenseFile: Reading License Failure", e);
          logger.info("getLicenseFile: Setting the license to Invalid.");
          licenseInstanceVO.setLicenseToInvalid();
        }
      }
      count++;
    }
    logger.debug("getLicenseFile: Reading License Successful, populating Settings.");
    Settings.getInstance().getSiteInfo(LicenseUtil.getHostName(dataSource)).setLicenseInstance(licenseInstanceVO);
  } //end of getLicenseFile method

  /**
   * Reads the encrypted file from the local file system. It returns the
   * encypted data in a byte array. If there is an error, an IOException will be
   * thrown.
   * @param licenseFile The location of the encrypted CentraView License file.
   * @return The license file in the form of a byte array.
   * @throws IOException Something went wrong when writing out the license file.
   *           It could be anything, full disk, permissions, etc...
   */
  private static final byte[] readLicenseFileFromDisk(File licenseFile) throws IOException
  {
    byte[] returnByteArray = null;

    FileInputStream fileInputStream = new FileInputStream(licenseFile);
    DataInputStream dataInputStream = new DataInputStream(fileInputStream);
    try {
      returnByteArray = new byte[fileInputStream.available()];
      dataInputStream.readFully(returnByteArray);
    } finally {
      dataInputStream.close();
      fileInputStream.close();
    }
    return returnByteArray;
  } //end of readLicenseFileFromDisk method

  /**
   * Decrypts the RSA encypted data received from the customer. Once this
   * information is decrypted, the data is loaded into a properties file.
   * @param serverProperties The RSA encrypted data from CentraView customers.
   * @return A decrypted Properies object with all of the properties from the
   *         CentraView Customer server.
   */
  private static final Properties decryptLicenseFile(Key blowfishKey, byte[] licenseFile)
  {
    Properties returnProperties = null;
    try {
      byte[] zeros = new byte[8];
      Arrays.fill(zeros, (byte)0);
      IvParameterSpec ivSpec = new IvParameterSpec(zeros);
      Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
      cipher.init(Cipher.DECRYPT_MODE, blowfishKey, ivSpec);

      ByteArrayInputStream byteArrayInStream = new ByteArrayInputStream(cipher.doFinal(licenseFile));
      returnProperties = new Properties();
      returnProperties.load(byteArrayInStream);
    } catch (Exception exception) {
      logger.error("decryptLicenseFile: generic Exception", exception);
    }
    return returnProperties;
  } //end of decryptLicenseFile method

  private static byte[] getKeyFromLicenseFile(byte[] encryptedLicenseFile)
  {
    byte[] encryptedKey = new byte[KEY_LENGTH];
    for (int i = 0; i < KEY_LENGTH; i++) {
      encryptedKey[i] = encryptedLicenseFile[i];
    }
    return encryptedKey;
  }

  private static byte[] getLicenseFromLicenseFile(byte[] encryptedLicenseFile)
  {
    int size = encryptedLicenseFile.length - KEY_LENGTH;
    byte[] encryptedLicense = new byte[size];
    int j = KEY_LENGTH;
    for (int i = 0; i < encryptedLicense.length; i++) {
      encryptedLicense[i] = encryptedLicenseFile[j];
      j++;
    }
    return encryptedLicense;
  }

  /**
   * Decrypts the RSA encypted Blowfish Key from the customer. Once this
   * information is decrypted, the data is loaded into a properties file.
   * @param serverProperties The RSA encrypted data from CentraView customers.
   * @return A decrypted Properies object with all of the properties from the
   *         CentraView Customer server.
   */
  private static final Key decryptBlowfishKey(byte[] encryptedKey)
  {
    SecretKeySpec blowfishKey = null;
    try {
      X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(PUBLIC_KEY);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(publicKeySpec);

      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, publicKey);

      byte[] blowfishKeyByteArray = cipher.doFinal(encryptedKey);
      blowfishKey = new SecretKeySpec(blowfishKeyByteArray, "Blowfish");
    } catch (Exception exception) {
      logger.error("[decryptBlowfishKey] Exception thrown.", exception);
    }
    return blowfishKey;
  } //end of decryptServerProperties method


  // FETCH LICENSE FILE METHODS

  /**
   * Fetches the license file.
   */
  public static final boolean fetchLicenseFile(String dataSource)
  {
    boolean returnValue = false;
    try {
      SettingsInterface settings = Settings.getInstance();
      LicenseInstanceVO licenseInstanceVO = new LicenseInstanceVO();
      Properties props = new Properties();

      props.setProperty(PROPS_MAC_KEY, settings.getMACAddress());
      props.setProperty(PROPS_HOST_NAME_KEY, LicenseUtil.getHostName(dataSource));
      props.setProperty(PROPS_TIMESTAMP_KEY, String.valueOf(Calendar.getInstance().getTimeInMillis()));
      String licenseKey = LicenseUtil.getLicenseKey(dataSource);
      if (null == licenseKey) {
        props.setProperty(PROPS_LICENSE_KEY, "");
      } else {
        props.setProperty(PROPS_LICENSE_KEY, licenseKey);
      }

      HashMap names = LicenseUtil.getEntityNameAndIndividualName(dataSource);
      String entityName = "";
      String individualName = "";
      if (!names.isEmpty()) {
        entityName = (String)names.get("entityName");
        individualName =(String)names.get("individualName");
      }

      props.setProperty(PROPS_ENTITY_KEY, entityName);
      props.setProperty(PROPS_INDIVIDUAL_KEY, individualName);
      props.setProperty(PROPS_CV_VERSION_KEY, CentraViewConfiguration.getVersion());
      logger.debug("fetchLicenseFile: ServerProperties being sent: ");
      logger.debug(props);

      Key blowfishKey = LicenseUtil.generateBlowfishKey();
      byte[] encryptedServerData = LicenseUtil.encryptServerData(blowfishKey, props);
      byte[] encryptedBlowfishKey = LicenseUtil.encryptBlowfishKey(blowfishKey);
      Byte[] byteArrayServerData = (Byte[])LicenseUtil.convertToObjectArray(encryptedServerData);
      Byte[] byteArrayKeyData = (Byte[])LicenseUtil.convertToObjectArray(encryptedBlowfishKey);
      byte[] encryptedResponse = LicenseUtil.obtainLicenseFile(byteArrayKeyData, byteArrayServerData);
      logger.debug("fetchLicenseFile: Response Size: "+encryptedResponse.length+" bytes.");
      if (encryptedResponse.length < 1) {
        throw new AxisFault(SERVER_DOWN_MESSAGE);
      }
      logger.debug("fetchLicenseFile: Writing new license to disk.");
      File centraViewLicenseFile = LicenseUtil.getCentraViewLicenseFile(dataSource);
      LicenseUtil.writeLicenseFileToDisk(encryptedResponse, centraViewLicenseFile);
      logger.debug("fetchLicenseFile: Decrpting License.");
      byte[] encryptedLicenseFile = LicenseUtil.getLicenseFromLicenseFile(encryptedResponse);
      byte[] encryptedKey = LicenseUtil.getKeyFromLicenseFile(encryptedResponse);
      Key newBlowfishKey = LicenseUtil.decryptBlowfishKey(encryptedKey);
      Properties licenseFile = LicenseUtil.decryptLicenseFile(newBlowfishKey, encryptedLicenseFile);
      logger.debug("fetchLicenseFile: The recieved license file:");
      logger.debug(licenseFile);
      //do SHA to make sure is valid
      //save data to database.
      licenseInstanceVO.updateLicenseInformation(licenseFile);
      boolean valid = licenseInstanceVO.isValidLicenseFile();
      if (valid) {
        logger.debug("fetchLicenseFile: The license SHA1 hash matches.  Valid!");
        //rock on!
        //update license key
        LicenseUtil.updateLicenseKey(licenseFile, dataSource);
      } else {
        logger.debug("fetchLicenseFile: The SHA1 hash does not match, setting License invalid.");
        licenseInstanceVO.setLicenseToInvalid();
      }
      logger.debug("fetchLicenseFile: fetch complete, Updating licenseVO.");
      Settings.getInstance().getSiteInfo(LicenseUtil.getHostName(dataSource)).setLicenseInstance(licenseInstanceVO);
      returnValue = true;
    } catch (AxisFault axisFault) {
      logger.error("[fetchLicenseFile] Exception thrown.", axisFault);
      returnValue = false;
    } catch (Exception exception) {
      logger.error("[fetchLicenseFile] Exception thrown.", exception);
      returnValue = false;
    }
    return returnValue;
  } //end of fetchLicenseFile method

  /**
   * Writes the encrypted byte array out to the local file system. If
   * there is an error, an IOException will be thrown.
   * @param encryptedLicenseFile The encrypted CentraView License file.
   * @throws IOException Something went wrong when writing out the license
   * file. It could be anything, full disk, permissions, etc...
   */
  private static final void writeLicenseFileToDisk(byte[] encryptedLicenseFile, File centraViewLicenseFile) throws IOException
  {
    FileOutputStream fileOutputStream = new FileOutputStream(centraViewLicenseFile);
    try {
      fileOutputStream.write(encryptedLicenseFile);
      fileOutputStream.flush();
    } finally {
      fileOutputStream.close();
    }
  }

  /**
   * Returns the Primary License Key for this
   * copy of CentraView. Returns null if
   * the license is not set or if there is an
   * error retrieving the license.
   * @return Returns the Primary License Key for this
   * copy of CentraView.
   */
  private static final String getLicenseKey(String dataSource)
  {
    String returnLicense = null;
    try {
      LicenseHome licenseHome = (LicenseHome)CVUtility.getHomeObject("com.centraview.license.LicenseHome","License");
      License license = (License) licenseHome.create();
      license.setDataSource(dataSource);
      LicenseVO licenseVO = license.getPrimaryLicense();
      returnLicense = licenseVO.getLicenseKey();
    } catch(NullPointerException nullPointerException) {
      logger.info("getLicenseKey: The License Key Code doesn't exist, in the Database");
    } catch(Exception exception) {
      logger.error("[getLicenseKey] Exception Thrown.", exception);
    }
    return returnLicense;
  } //end of getLicenseKey method

  /**
   * Updates the Primary CentraView License record in the database.
   * If there isn't a record in the database, one is created.
   * @param licenseFile The decrypted license file from CentraView, LLC.
   */
  private static final void updateLicenseKey (Properties licenseFile, String dataSource)
  {
    try {
      LicenseHome licenseHome = (LicenseHome)CVUtility.getHomeObject("com.centraview.license.LicenseHome","License");
      License license = (License) licenseHome.create();
      license.setDataSource(dataSource);

      LicenseVO licenseVO = license.getPrimaryLicense();
      if (null == licenseVO) {
        licenseVO = new LicenseVO();
        licenseVO.setLicenseKey(licenseFile.getProperty(LICENSE_KEY_KEY));
        int newID = license.createLicense(licenseVO);
        licenseVO.setLicenseID(newID);
      }
      licenseVO.setLicenseKey(licenseFile.getProperty(LICENSE_KEY_KEY));
      long currentTime = new Date().getTime();
      long offset = Long.parseLong(licenseFile.getProperty(LICENSE_FILE_OFFSET_KEY));
      licenseVO.setLastVerified(new Date(currentTime + offset));
      licenseVO.setLicenseVerification(licenseFile.getProperty(LICENSE_FILE_SHA_KEY));

      license.updateLicense(licenseVO);
    } catch(Exception exception) {
      logger.error("[updateLicenseKey] Exception thrown.", exception);
    }
  } //end of updateLicenseKey method

  /**
   * Returns the Primary Entity name licensed with this copy of CentraView.
   * @return The name of the Primary Entity. The name of the entity who owns
   *         this license.
   */
  private static final HashMap getEntityNameAndIndividualName(String dataSource)
  {
    String entityName = "ENTITYNAME";
    HashMap names = new HashMap();
    try {
      ContactFacadeHome contactFacadeHome = (ContactFacadeHome)CVUtility.getHomeObject("com.centraview.contact.contactfacade.ContactFacadeHome", "ContactFacade");
      ContactFacade contactFacade = (ContactFacade)contactFacadeHome.create();
      contactFacade.setDataSource(dataSource);
      EntityVO entityVO = contactFacade.getEntity(1);
      entityName = entityVO.getName();
      IndividualVO individualVO = entityVO.getIndividualVO();
      StringBuffer individualName = new StringBuffer();
      if (individualVO != null) {
        individualName.append(individualVO.getFirstName());
        individualName.append(" ");
        individualName.append(individualVO.getLastName());
      }
      names.put("entityName", entityName);
      names.put("individualName", individualName.toString());
    } catch (Exception e) {
      logger.error("[getEntityNameAndIndividualName] Exception thrown.", e);
    }
    return names;
  } //end of getEntityName method

  /**
   * Returns the HostName of the server running CentraView..
   * @return The HostName of the server running CentraView.
   */
  private static final String getHostName(String dataSource)
  {
    String hostName = "";
    try {
      InitialContext ic = new InitialContext();
      ServerSettingsHome serverSettingsHome = (ServerSettingsHome)CVUtility.getHomeObject("com.centraview.administration.serversettings.ServerSettingsHome", "ServerSettings");
      ServerSettings serverSettingsRemote = (ServerSettings)serverSettingsHome.create();
      serverSettingsRemote.setDataSource(dataSource);
      ServerSettingsVOX serverSettings = serverSettingsRemote.getServerSettings();
      hostName = serverSettings.getHostName();
    } catch (Exception e) {
      logger.error("[getHostName] Exception thrown.", e);
    }
    return hostName;
  } //end of getHostName method

  /**
   * Encrypts a properties file with RSA encryption. The method
   * returns the encrypted byte array. If there is an error
   * in this process, a null byte array will be returned.
   * @param serverProperties The properties file that needs encrypted.
   * @return An RSA encrypted byte array. Suitable for being
   * sent over the internet.
   */
  private static final byte[] encryptServerData(Key blowfishKey, Properties serverProperties)
  {
    byte[] returnByteArray = null;
    try {
      byte[] zeros = new byte[8];
      Arrays.fill(zeros, (byte)0);
      IvParameterSpec ivSpec = new IvParameterSpec(zeros);
      ByteArrayOutputStream byteArrayOutStream = new ByteArrayOutputStream();
      serverProperties.store(byteArrayOutStream, "CentraView Server Properties");

      Cipher cipher = Cipher.getInstance("Blowfish/CBC/PKCS5Padding");
      cipher.init(Cipher.ENCRYPT_MODE, blowfishKey, ivSpec);

      returnByteArray = cipher.doFinal(byteArrayOutStream.toByteArray());
    } catch (Exception exception) {
      logger.error("[encryptServerData] Exception thrown.", exception);
    }
    return returnByteArray;
  } //end of encryptServerData method

  /**
   * Encrypts a properties file with RSA encryption. The method
   * returns the encrypted byte array. If there is an error
   * in this process, a null byte array will be returned.
   * @param serverProperties The properties file that needs encrypted.
   * @return An RSA encrypted byte array. Suitable for being
   * sent over the internet.
   */
  private static final byte[] encryptBlowfishKey(Key blowfishKey)
  {
    byte[] returnByteArray = null;
    try {
      X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(PUBLIC_KEY);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic(publicKeySpec);

      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);

      returnByteArray = cipher.doFinal(blowfishKey.getEncoded());
    } catch (Exception exception) {
      logger.error("[encryptBlowfishKey] Exception thrown.", exception);
    }
    return returnByteArray;
  } //end of encryptServerData method

  private static final Key generateBlowfishKey()
  {
    Key key = null;
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
      keyGenerator.init(128);
      key = keyGenerator.generateKey();
    } catch (Exception e) {
      logger.error("[generateBlowfishKey] Exception thrown.", e);
    }
    return key;
  } //end of generateBlowfishKey method

  /**
   * This method connects to the CentraView License Manager
   * Web Service located at CentraView, LLC. It will return
   * an encrypted byte array with the license file
   * contents inside. If the server is down, or
   * the connection cannot be made, or any
   * other error occurs, a <code>null</code>
   * byte array will be returned.
   * @param serverData An RSA Encrypted byte array with the
   * Server's information in it.
   * @return An encrypted byte array with the License File
   * information.
   */
  private static final byte[] obtainLicenseFile(Byte[] blowfishKey, Byte[] serverData) throws AxisFault
  {
    byte[] returnByteArray = null;
    try {
      Service service = new Service();

      Call call = (Call)service.createCall();
      call.setTargetEndpointAddress(new java.net.URL(WEB_SERVICE_URL));
      call.setOperationName(new QName("LicenseAdminSoapService", "obtainLicenseFile"));
      call.addParameter("arg1", XMLType.XSD_BASE64, ParameterMode.IN);
      call.addParameter("arg2", XMLType.XSD_BASE64, ParameterMode.IN);
      call.setReturnType(XMLType.XSD_BASE64);
      returnByteArray = (byte[])call.invoke(new Object[] { blowfishKey, serverData });
    } catch (RemoteException remoteException) {
      logger.error("[obtainLicenseFile] Exception thrown.", remoteException);
      throw new AxisFault(SERVER_DOWN_MESSAGE);
    } catch (Exception exception) {
      logger.error("[obtainLicenseFile] Exception thrown.", exception);
    }
    return returnByteArray;
  } //end of obtainLicenseFile method

  /**
   * A little utilitiy which converts byte[] to Byte[].
   * This conversion is needed for sending a byte array
   * to a web service.
   * @param inByteArray The byte[] to convert to a Byte[] array.
   * @return A Byte[] with the byte[] contents.
   */
  private static Byte[] convertToObjectArray(byte[] inByteArray)
  {
    Byte[] byteArray = new Byte[inByteArray.length];
    for (int i = 0; i < inByteArray.length; i++) {
      byteArray[i] = new Byte(inByteArray[i]);
    }
    return byteArray;
  } //end of convertToObjectArray method


  /**
   * Returns the CentraView License File.
   * If this file cannot be found (for whatever reason)
   * a null object is returned.
   * @return The CentraView License File (or null if it cannot be found).
   */
  public final static File getCentraViewLicenseFile(String dataSource)
  {
    File returnFile = null;
    try {
      File centraViewFileDirectory = CVUtility.getCentraViewFileSystemRoot(dataSource);
      if (centraViewFileDirectory != null) {
        returnFile = new File(centraViewFileDirectory.getAbsolutePath() + System.getProperty("file.separator", "/") + LICENSE_FILE_NAME);
      }
    } catch (Exception exception) {
      logger.error("[getCentraViewLicenseFile] Exception thrown.", exception);
    }
    return returnFile;
  } //end of getCentraViewLicenseFile method

}


