/*
 * $RCSfile: SaveInventoryHandler.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

package com.centraview.account.inventory;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.item.ItemVO;
import com.centraview.common.CVUtility;
import com.centraview.common.UserObject;
import com.centraview.common.UserPrefererences;
import com.centraview.contact.entity.EntityVO;
import com.centraview.contact.helper.CustomFieldVO;
import com.centraview.settings.Settings;

public class SaveInventoryHandler extends org.apache.struts.action.Action {

  // Global Forwards
  public static final String GLOBAL_FORWARD_failure = "failure";

  // Local Forwards
  private static final String FORWARD_savenew = ".view.accounting.savenew";
  private static final String FORWARD_saveclose = ".view.accounting.saveclose";
  private static final String FORWARD_save = ".view.accounting.save";

  private static String FORWARD_final = FORWARD_savenew;

  public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
      HttpServletResponse response) throws Exception
  {

    String dataSource = Settings.getInstance().getSiteInfo(
        CVUtility.getHostName(super.getServlet().getServletContext())).getDataSource();
    try {
      HttpSession session = request.getSession(true);
      request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.INVENTORY);

      InventoryForm invenForm = (InventoryForm)request.getAttribute("inventoryform");

      ActionMessages allErrors = new ActionMessages();

      // now, check the account ID on the form...
      if (invenForm.getItemName() == null || invenForm.getItemName().length() <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            "error.general.requiredField", "Item"));
      }

      // now, check the account ID on the form...
      if (invenForm.getLocationName() == null || invenForm.getLocationName().length() <= 0) {
        allErrors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
            "error.general.requiredField", "Location"));
      }
      int invID = 0;

      if (invenForm != null)
        invID = invenForm.getInventoryID();

      if (!allErrors.isEmpty()) {
        saveErrors(request, allErrors);
        request.setAttribute("inventoryform", invenForm);
        if (invID > 0) {
          FORWARD_final = FORWARD_save;
        } else {
          FORWARD_final = FORWARD_savenew;
        }
        return (mapping.findForward(FORWARD_final));
      }

      request.setAttribute("body", "EDIT");

      UserObject userobjectd = (UserObject)session.getAttribute("userobject");
      int individualID = userobjectd.getIndividualID();

      UserPrefererences userPref = userobjectd.getUserPref();
      String dateFormat = userPref.getDateFormat();

      dateFormat = "MMMMMMMMM dd, yyyy";
      String timeZone = userPref.getTimeZone();
      if (timeZone == null)
        timeZone = "EST";

      GregorianCalendar gCal = new GregorianCalendar(TimeZone.getTimeZone(timeZone));
      SimpleDateFormat dForm = new SimpleDateFormat(dateFormat);
      dForm.setCalendar(gCal);

      String typeOfSave = null;

      if (request.getParameter("typeOfSave") != null)
        typeOfSave = request.getParameter("typeOfSave");

      // Convert all strings to int
      invenForm.convertFormbeanToValueObject();

      InventoryHome hm = (InventoryHome)CVUtility.getHomeObject(
          "com.centraview.account.inventory.InventoryHome", "Inventory");
      Inventory remote = hm.create();
      remote.setDataSource(dataSource);

      InventoryVO inventoryVO = new InventoryVO();

      ItemVO itemVO = new ItemVO();
      itemVO.setItemId(invenForm.getItemIDValue());

      inventoryVO.setItemVO(itemVO);

      inventoryVO.setQty(invenForm.getQty());

      inventoryVO.setStrIdentifier(invenForm.getIdentifier());
      inventoryVO.setStrDescription(invenForm.getDescription());

      inventoryVO.setIntLocationID(invenForm.getLocationIDValue());

      EntityVO entityVO = new EntityVO();
      entityVO.setContactID(invenForm.getManufactureIDValue());

      inventoryVO.setManufacturerVO(entityVO);

      entityVO = new EntityVO();
      entityVO.setContactID(invenForm.getVendorIDValue());

      inventoryVO.setVendorVO(entityVO);

      inventoryVO.setIntStatusID(invenForm.getStatusIDValue());
      entityVO = new EntityVO();
      entityVO.setContactID(invenForm.getSoldToIDValue());

      inventoryVO.setSoldToVO(entityVO);

      // for custom fields
      Vector customFieldVec = getCustomFieldVO(request, response);

      invenForm.setCustomFieldsVec(customFieldVec);
      inventoryVO.setCustomFieldsVec(customFieldVec);

      // for custom fields ends here

      if (invID > 0) {
        inventoryVO.setInventoryID(invID);
        remote.updateInventory(individualID, inventoryVO);
      }

      if (invID == 0) {
        inventoryVO = remote.insertInventory(individualID, inventoryVO);
        invenForm.setInventoryID(inventoryVO.getInventoryID());

        Timestamp timeStamp = inventoryVO.getModified();
        Date date = inventoryVO.getCreated();

        if (inventoryVO.getCreated() != null)
          invenForm.setCreated(dForm.format(date));

        if (inventoryVO.getModified() != null)
          invenForm.setModified(dForm.format(timeStamp));

      }

      if (typeOfSave != null) {
        if (typeOfSave.equals("savenew")) {
          // forward to jsp page
          FORWARD_final = FORWARD_savenew;

          request.setAttribute("body", "EDIT");
          request.removeAttribute("inventoryform");
        }
        if (typeOfSave.equals("saveclose")) {
          // forward to jsp page
          FORWARD_final = FORWARD_saveclose;

          request.setAttribute("body", "list");
        }

        if (typeOfSave.equals("save")) {

          // forward to jsp page
          FORWARD_final = FORWARD_save;
          request.setAttribute("rowId", String.valueOf(invenForm.getInventoryID()));
        }

      }

    } catch (Exception e) {
      System.out.println("[Exception][SaveInventoryHandler.execute] Exception Thrown: " + e);
      FORWARD_final = GLOBAL_FORWARD_failure;
    }
    return (mapping.findForward(FORWARD_final));
  }

  public Vector getCustomFieldVO(HttpServletRequest request, HttpServletResponse response)
  {
    Vector vec = new Vector();

    for (int i = 1; i < 4; i++) {
      String fieldid = request.getParameter("fieldid" + i);
      String fieldType = request.getParameter("fieldType" + i);
      String textValue = request.getParameter("text" + i);

      if (fieldid == null)
        fieldid = "0";
      int intfieldId = Integer.parseInt(fieldid);
      CustomFieldVO cfvo = new CustomFieldVO();
      cfvo.setFieldID(intfieldId);
      cfvo.setFieldType(fieldType);
      cfvo.setValue(textValue);
      vec.add(cfvo);
    }
    return vec;
  }// end of getCustomFieldVO

}
