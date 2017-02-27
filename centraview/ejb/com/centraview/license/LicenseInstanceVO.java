/*
 * $RCSfile: LicenseInstanceVO.java,v $    $Revision: 1.1.1.1 $  $Date: 2005/04/28 20:22:29 $ - $Author: mking_cv $
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

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Properties;

import org.apache.axis.encoding.Base64;
import org.apache.log4j.Logger;

import com.centraview.settings.Settings;

/**
 * This singleton object will hold the license file information
 * in memory. It will be fed information from the getlicensefile
 * service. It will be accessable from the login module.
 *
 * @author Ryan Grier <ryan@centraview.com>
 */
public class LicenseInstanceVO implements Serializable
{
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
  
  /** The License File. */
  private Properties licenseFile = null;
  
  /** The License's current status. */
  private String status = null;
  
  /** Whether or not this copy of CentraView expires. */
  private boolean expires = false;
  
  /** The License's expiration date. */
  private Date expirationDate = null;
  
  /** The License's current Mac Address. */
  private String macAddress = null;
  
  /** The License's current Host Name. */
  private String hostName = null;
  
  /** The License's validation string. */
  private String validationKey = null;
  
  /** The server's offset from the CentraView License Server. */
  private long serverOffset = 0;
  
  /** The number of concurrent users allowed to login to CentraView. */
  private int numberOfUsers = 0;
  
  /** The license key from the license file. */
  private String licenseKey = null;

  /** Determines whether this instance has been setup. */
  private boolean isSetup = true;

  private static Logger logger = Logger.getLogger(LicenseInstanceVO.class);
  
  private static final long MILLIS_IN_A_DAY = 86400000;
  
  public LicenseInstanceVO()
  {
    //use LicenseInstanceVO.getInstance();
  } //end of LicenseInstanceVO constructor
  
  /**
   * Constructs a LicenseInstanceVO object with the license properties.
   * 
   * @param licenseFile
   */
  public LicenseInstanceVO(Properties licenseFile)
  {
    this.updateLicenseInformation(licenseFile);
  } //end of LicenseInstanceVO constructor
  
  /**
   * Updates the existing LicenseInstanceVO object with 
   * information from the license file.
   *
   * @param licenseFile the decrypted license file.
   */
  public synchronized final void updateLicenseInformation(Properties licenseFile)
  {
    this.licenseFile = licenseFile;
    this.setMACAddress(this.licenseFile.getProperty(LICENSE_FILE_MAC_KEY));
    this.setHostName(this.licenseFile.getProperty(LICENSE_FILE_HOST_NAME_KEY));
    this.setStatus(this.licenseFile.getProperty(LICENSE_FILE_STATUS_KEY));
    this.setValidationKey(this.licenseFile.getProperty(LICENSE_FILE_SHA_KEY));
    this.setLicenseKey(this.licenseFile.getProperty(LICENSE_KEY_KEY));
    
    String thisNumberOfUsersString = this.licenseFile.getProperty(LICENSE_FILE_USERS_KEY);
    String thisExpirationDateString = this.licenseFile.getProperty(LICENSE_FILE_EXP_DATE_KEY);
    String thisExpiresString = this.licenseFile.getProperty(LICENSE_EXPIRES_KEY);
    String thisOffsetString = this.licenseFile.getProperty(LICENSE_FILE_OFFSET_KEY);
    
    Date thisExpirationDate = (thisExpirationDateString == null)?new Date():new Date(Long.parseLong(thisExpirationDateString));
    
    long thisOffset = (thisOffsetString == null)?0:Long.parseLong(thisOffsetString);
    int thisNumberOfUsers = (thisNumberOfUsersString == null)?0:Integer.parseInt(thisNumberOfUsersString);
      
    boolean thisExpires = false;
    if (thisExpiresString != null && thisExpiresString.equalsIgnoreCase("true"))
    {
      thisExpires = true;
    } //end of if statement (thisExpiresString != null && thisExpiresString.equalsIgnoreCase("true"))
    this.setOffset(thisOffset);
    this.setNumberOfUsers(thisNumberOfUsers);
    this.setExpires(thisExpires);
    this.setExpirationDate(thisExpirationDate);   
    setIsSetup(true);
  } //end of updateLicenseInformation method
  
  /**
   * Updates the existing LicenseInstanceVO object 
   * and sets it to 'INVALID'.
   */
  public synchronized final void setLicenseToInvalid()
  {
    this.setStatus("INVALID");
    setIsSetup(true);
  } //end of setLicenseToInvalid method
  
  /**
   * Sets the license file.
   *
   * @param licenseFile The license file.
   */
  private final void setLicenseFile(Properties licenseFile)
  {
    this.licenseFile = licenseFile; 
  } //end of setLicenseFile method
  
  /**
   * Returns the license file.
   *
   * @return The license file.
   */
  private final Properties getLicenseFile() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    }
    return this.licenseFile; 
  } //end of getLicenseFile method
  
  /**
   * Sets the MAC Address from the license file.
   *
   * @param macAddress The MAC Address from the license file.
   */
  private final void setMACAddress(String macAddress)
  {
    this.macAddress = macAddress; 
  } //end of setMACAddress method
  
  /**
   * Returns the MAC Address from the license file.
   *
   * @return The MAC Address from the license file.
   */
  public final String getMACAddress() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    }
    return this.macAddress; 
  } //end of getMACAddress method
  
  /**
   * Sets the Host Name from the license file.
   *
   * @param hostName The Host Name from the license file.
   */
  private final void setHostName(String hostName)
  {
    this.hostName = hostName; 
  } //end of setHostName method
  
  /**
   * Returns the Host Name from the license file.
   *
   * @return The Host Name from the license file.
   */
  public final String getHostName() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    }
    return this.hostName; 
  } //end of getHostName method
  
  /**
   * Sets the Validation Key from the license file.
   *
   * @param hostName The Validation Key from the license file.
   */
  private final void setValidationKey(String validationKey)
  {
    this.validationKey = validationKey; 
  } //end of setValidationKey method
  
  /**
   * Returns the Validation Key from the license file.
   *
   * @return The Validation Key from the license file.
   */
  private final String getValidationKey()
  {
    return this.validationKey; 
  } //end of getValidationKey method
  
  /**
   * Sets the Status from the license file.
   *
   * @param status The Status from the license file.
   */
  private final void setStatus(String status)
  {
    this.status = status; 
  } //end of setStatus method
  
  /**
   * Returns the Status from the license file.
   *
   * @return The Status from the license file.
   */
  public final String getStatus() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    }
    return this.status; 
  } //end of getStatus method
  
  /**
   * Sets the License Key from the license file.
   *
   * @param licenseKey The License Key from the license file.
   */
  private final void setLicenseKey(String licenseKey)
  {
    this.licenseKey = licenseKey; 
  } //end of setLicenseKey method
  
  /**
   * Returns the License Key from the license file.
   *
   * @return The License Key from the license file.
   */
  public final String getLicenseKey() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    }
    return this.licenseKey; 
  } //end of getLicenseKey method
  
  /**
   * Sets the Expiration Date from the license file.
   *
   * @param expirationDate The Expiration Date from the license file.
   */
  private final void setExpirationDate(Date expirationDate)
  {
    this.expirationDate = expirationDate; 
  } //end of setExpirationDate method
  
  /**
   * Returns the Expiration Date from the license file.
   *
   * @return The Expiration Date from the license file.
   */
  public final Date getExpirationDate() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    }
    return this.expirationDate; 
  } //end of getExpirationDate method
  
  /**
   * Sets the Offset from the license file.
   *
   * @param offset The Offset from the license file.
   */
  private final void setOffset(long offset)
  {
    this.serverOffset = offset; 
  } //end of setOffset method
  
  /**
   * Returns the Offset from the license file.
   *
   * @return The Offset from the license file.
   */
  private final long getOffset()
  {
    return this.serverOffset; 
  } //end of getOffset method
  
  /**
   * Sets the Expires flag from the license file.
   *
   * @param expires The Expires flag from the license file.
   */
  private final void setExpires(boolean expires)
  {
    this.expires = expires; 
  } //end of setExpires method
  
  /**
   * Returns the Expires flag from the license file.
   *
   * @return The Expires flag from the license file.
   */
  public final boolean expires() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    } //end of if statement (!isSetup())
    return this.expires; 
  } //end of expires method

  /**
   * Returns whether the current License is expired.
   * 
   * @return True if the license is expired, false otherwise.
   */
  public final boolean isExpired() throws LicenseNotSetupException
  {
    return this.getExpirationDate().before(new Date());
  } //end of isExpired method

  /**
   * Returns the number of days remaining with this license.
   * <p>
   * The following are possible results from this
   * method.
   * <ul>
   * <li>Any negative number means the license has expired.
   * <li>0 Means the license expires today.
   * <li>Any positive number means that many days remain
   * until the license expires.
   * </ul>
   *
   * @return The number of days remaining on this license.
   */
  public final int getNumberOfDaysRemaining() throws LicenseNotSetupException
  {
    long nowTimestamp = new Date().getTime();
    long expirationDateTimestamp = this.getExpirationDate().getTime();
    long difference = expirationDateTimestamp - nowTimestamp;
    double daysLeft = (difference/MILLIS_IN_A_DAY);

    return new Double(daysLeft).intValue();
  } //end of getNumberOfDaysRemaining method
  
  /**
   * Sets the Number of Users from the license file.
   *
   * @param numberOfUsers The Number of Users from the license file.
   */
  private final void setNumberOfUsers(int numberOfUsers)
  {
    this.numberOfUsers = numberOfUsers; 
  } //end of setNumberOfUsers method
  
  /**
   * Returns the Number of Users from the license file.
   *
   * @return The Number of Users from the license file.
   */
  public final int getNumberOfUsers() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    }
    return this.numberOfUsers; 
  } //end of getNumberOfUsers method

  /**
   * Sets whether this instance has been setup.
   *
   * @param isSetup Whether this instance has been setup.
   */
  private final void setIsSetup(boolean isSetup)
  {
    this.isSetup = isSetup; 
  } //end of setIsSetup method
  
  /**
   * Returns whether this instance has been setup.
   *
   * @return Whether this instance has been setup.
   */
  private final boolean isSetup()
  {
    return this.isSetup; 
  } //end of isSetup method
  
  /**
   * Gets the LicnseFile (in memory) and validates the file.
   * 
   * @return true if the license file is valid. false if the license file is not
   *         valid.
   */
  public synchronized final boolean isValidLicenseFile() throws LicenseNotSetupException
  {
    if (!isSetup())
    {
      throw new LicenseNotSetupException();
    }
    boolean returnValue = false;
    Properties properties = getLicenseFile();

    logger.debug("isValidLicenseFile: License to validate:");
    logger.debug(properties);

    //This is here so we can validate the correct information in the same order
    // everytime.
    StringBuffer validationStringBuffer = new StringBuffer();
    validationStringBuffer.append(LICENSE_KEY_KEY + ":" + properties.getProperty(LICENSE_KEY_KEY) + ",");
    validationStringBuffer.append(LICENSE_FILE_STATUS_KEY + ":" + properties.getProperty(LICENSE_FILE_STATUS_KEY) + ",");
    validationStringBuffer.append(LICENSE_FILE_USERS_KEY + ":" + properties.getProperty(LICENSE_FILE_USERS_KEY) + ",");
    validationStringBuffer.append(LICENSE_FILE_MAC_KEY + ":" + properties.getProperty(LICENSE_FILE_MAC_KEY) + ",");
    validationStringBuffer.append(LICENSE_FILE_HOST_NAME_KEY + ":" + properties.getProperty(LICENSE_FILE_HOST_NAME_KEY) + ",");
    validationStringBuffer.append(LICENSE_FILE_OFFSET_KEY + ":" + properties.getProperty(LICENSE_FILE_OFFSET_KEY) + ",");
    validationStringBuffer.append(LICENSE_FILE_EXP_DATE_KEY + ":" + properties.getProperty(LICENSE_FILE_EXP_DATE_KEY) + ",");
    validationStringBuffer.append(LICENSE_EXPIRES_KEY + ":" + properties.getProperty(LICENSE_EXPIRES_KEY));

    logger.debug("isValidLicenseFile: Validation String Buffer: "+validationStringBuffer.toString());
    String validationKey = (String)properties.getProperty(LICENSE_FILE_SHA_KEY);
    try
    {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
      messageDigest.update(validationStringBuffer.toString().getBytes());
      String newValidation = Base64.encode(messageDigest.digest());
      if (newValidation.equals(validationKey))
      {
        if (getMACAddress().equals(Settings.getInstance().getMACAddress()))
        {
          returnValue = true;
        } //end of if statement (getMACAddress().equals(getServerMACAddress()))
      } //end of if statement (newValidation.equals(validationKey))
    }//end of try block
    catch (Exception exception)
    {
      System.out.println("Exception in LicenseInstanceVO.isValidLicenseFile");
      //exception.printStackTrace();
    } //end of catch block (Exception)
    return returnValue;
  } //end ov isValidLicenseFile method.
  
  /**
   * Returns a String representation of the current LicenseInstanceVO
   * object. Each value is on a new line. This method overrides the
   * standard Object.toString method.
   *
   * @return A string representation of this object.
   */
  public final String toString()
  {
    StringBuffer sb = new StringBuffer();
    try
    {
      sb.append("{");
      sb.append("MAC Address: " + this.getMACAddress() + ",");
      sb.append("Host Name: " + this.getHostName() + ",");
      sb.append("Status: " + this.getStatus() + ",");
      sb.append("License Key: " + this.getLicenseKey() + ",");
      sb.append("Number Of Users: " + this.getNumberOfUsers() + ",");
      sb.append("Expires: " + this.expires() + ",");
      sb.append("Expiration Date: " + this.getExpirationDate() + ",");
      sb.append("Expired: " + this.isExpired() + ",");
      sb.append("Days Remaining: " + this.getNumberOfDaysRemaining());
      sb.append("}");
    } //end of try block
    catch (LicenseNotSetupException licenseNotSetupException)
    {
      sb = new StringBuffer(licenseNotSetupException.getMessage());
    } //end of catch block (LicenseNotSetupException)
    
    return sb.toString();
  } //end of toString method
} //end of LicenseInstanceVO class
