/*
 * $RCSfile: ValueListTag.java,v $    $Revision: 1.9 $  $Date: 2005/09/20 20:22:21 $ - $Author: mcallist $
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

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import com.centraview.common.DDNameValue;
import com.centraview.mail.MailFolderVO;
import com.centraview.valuelist.Button;
import com.centraview.valuelist.FieldDecoration;
import com.centraview.valuelist.FieldDescriptor;
import com.centraview.valuelist.ValueListConstants;
import com.centraview.valuelist.ValueListParameters;
import com.centraview.valuelist.ValueListRow;
import com.centraview.valuelist.ValueListVO;

/**
 * This tag is used to pull the list out of a ValueListVO iterate it and display
 * the contents.
 */
public class ValueListTag extends TagSupport {
  private static Logger logger = Logger.getLogger(ValueListTag.class);
  private MessageResources messages = MessageResources.getMessageResources("ApplicationResources");
  private Locale locale;
  private HttpServletRequest request;
  private long debugTime;
  private String listObjectName;
  private ValueListVO listObject;
  private ArrayList selectedColumns;
  private int numberOfRows;
  /**
   * We will use this value to identify wheather we are processing Read or
   * unRead message
   */
  private String readStatus = "Yes";

  public void setListObjectName(String listObjectName)
  {
    this.listObjectName = listObjectName;
  }

  /**
   * @return Returns the listObject.
   */
  public String getListObjectName()
  {
    return listObjectName;
  }

  public int doStartTag() throws JspTagException
  {
    this.request = (HttpServletRequest) pageContext.getRequest();
    if (logger.isDebugEnabled()) {
      this.debugTime = System.currentTimeMillis();
    }
    this.locale = this.pageContext.getRequest().getLocale();
    this.listObject = (ValueListVO) this.pageContext.findAttribute(this.listObjectName);

    if (this.listObject == null) {
      throw new JspTagException("No listObject with name " + this.listObjectName + " found.");
    }

    this.selectedColumns = new ArrayList();
    StringBuffer buffer = new StringBuffer();
    buffer.append(ValueListConstants.FORM_OPEN);
    buffer.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n");

    if (this.listObject.getDisplay().isPagingBar()) {
      buffer.append("<tr>\n");
      buffer.append("<td class=\"pagingBarContainer\">\n");
      this.pagingBarDetails(buffer);
      buffer.append("</td>\n");
      buffer.append("</tr>\n");
    }

    buffer.append("<tr>\n");
    buffer.append("<td>\n");
    buffer.append("<table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");

    if (!listObject.getDisplay().isSkipHeader()) {
      this.buildTableHead(buffer);
    }

    this.buildTableBody(buffer);
    buffer.append(ValueListConstants.TABLE_CLOSE);
    buffer.append("</td>\n");
    buffer.append("</tr>\n");

    if (this.listObject.getDisplay().isPagingBar()) {
      buffer.append("<tr>\n");
      buffer.append("<td class=\"pagingBarContainer\">\n");
      this.pagingBarDetails(buffer);
      buffer.append("</td>\n");
      buffer.append("</tr>\n");
    }

    buffer.append("</table>\n");
    buffer.append("<input type=\"hidden\" name=\"listId\" value=\"");
    buffer.append(this.pageContext.findAttribute("dbid"));
    buffer.append("\"/>");
    buffer.append("<input type=\"hidden\" name=\"currentPage\" value=\"");
    buffer.append(this.currentPageURL());
    buffer.append("\"/>");
    buffer.append("<input type=\"hidden\" name=\"noofrecord\" value=\"");
    buffer.append(this.listObject.getList().size());
    buffer.append("\"/>");
    buffer.append("<input type=\"hidden\" name=\"listTypeId\" value=\"");
    buffer.append(this.listObject.getParameters().getValueListType());
    buffer.append("\"/>");

    buffer.append(ValueListConstants.FORM_CLOSE);
    this.write(buffer.toString());
    return SKIP_BODY;
  }

  public int doEndTag()
  {
    if (logger.isDebugEnabled()) {
      long tagTime = System.currentTimeMillis() - this.debugTime;
      logger.debug("[doEndTag] Finished processing: Tag Time = " + tagTime + " ms.");
    }
    return EVAL_PAGE;
  }

  public void release()
  {
    listObjectName = null;
    listObject = null;
    selectedColumns = null;
    super.release();
  }

  /**
   * Write a string to the default JspWriter out.
   * @param String to be written
   * @throws JspTagException if an IOException occurs
   */
  private void write(String string) throws JspTagException
  {
    try {
      JspWriter out = this.pageContext.getOut();
      out.write(string);
    } catch (IOException e) {
      throw new JspTagException("Writer Exception: " + e.getMessage());
    }
  }

  /**
   * Iterate the list filling the buffer with appropriate HTML, to create the
   * body of the table.
   * @param buffer StringBuffer that already has a properly opened Table
   */
  private void buildTableBody(StringBuffer buffer)
  {
    List list = listObject.getList();

    ValueListParameters params = listObject.getParameters();
    int listType = params.getValueListType();

    numberOfRows = list.size();
    for (int i = 0; i < numberOfRows; i++) {
      boolean odd = false;
      String rowClass = "tableRowEven";
      if (i % 2 == 0) {
        rowClass = "tableRowOdd";
      } else {
        odd = true;
      }

      buffer.append("<tr>\n");
      ValueListRow row = (ValueListRow) list.get(i);
      List rowData = row.getRowData();

      // if this is an Entity list, and we're looking at the Default entity
      // record (id == 1), highlight the row
      if (listType == 1 && row.getRowId() == 1) { // sorry 'bout the magic
                                                  // numbers
        rowClass = "defaultEntityRow";
      }

      if (this.listObject.getDisplay().isCheckboxes() && !this.listObject.getLookupType().equals("SecurityProfile")) {
        if (this.listObject.getParameters().getValueListType() == ValueListConstants.KNOWLEDGEBASE_LIST_TYPE
            || this.listObject.getParameters().getValueListType() == ValueListConstants.FILE_LIST_TYPE) {
          this.addCheckBox(buffer, row.getRowId(), odd, rowClass, rowData, ValueListConstants.FILE_INDEX);
        } else {
          this.addCheckBox(buffer, row.getRowId(), odd, rowClass, null, 0);
        }
      }

      // If the selection widgets are radio boxes instead of the checkboxes
      // (as when the list is a lookup), then we must do the following things:
      if (this.listObject.getDisplay().isRadio()) {
        // Added the selected index value to the javascript function so that it
        // will populate the form
        Integer listTypeInteger = new Integer(this.listObject.getParameters().getValueListType());
        ArrayList radioSelectionIndexList = (ArrayList) ValueListConstants.RADIO_SELECTION_MAP.get(listTypeInteger);
        StringBuffer onSelectParameters = new StringBuffer();
        StringBuffer onRowValues = new StringBuffer();
        onSelectParameters.append("'");
        Iterator thisIndexIterator = radioSelectionIndexList.iterator();
        String typeOfLookup = (String) thisIndexIterator.next();
        String radioType = "radio";

        // This is a special Condition. We have to set the correct lookup type,
        // so that we will make the correct selection of values
        if (this.listObject.getLookupType() != null && this.listObject.getLookupType().equals("lookup_attendee")) {
          typeOfLookup = "lookup_attendee";
          ArrayList radioSelectionIndexAttendee = ValueListConstants.radioSelectedIndexAttendee;

          for (int k = 0; k < radioSelectionIndexAttendee.size(); k++) {
            int currentIndex = ((Integer) radioSelectionIndexAttendee.get(k)).intValue();
            String fieldValue = String.valueOf(rowData.get(currentIndex - 1));

            if (currentIndex == ValueListConstants.ATTENDEEROWID) {
              onRowValues.append(fieldValue + "&");
            } else if (currentIndex == ValueListConstants.ATTENDEEFIRSTNAME) {
              onRowValues.append(fieldValue + " ");
            } else {
              onRowValues.append(fieldValue);
            }
          }
        }

        if (this.listObject.getLookupType() != null && this.listObject.getLookupType().equals("lookupSubItem")) {
          typeOfLookup = "SubItem";
        }

        // First snag the lookupType string from the front of the arrayList.
        onSelectParameters.append(typeOfLookup);
        onSelectParameters.append("','");

        while (thisIndexIterator.hasNext()) {
          int currentIndex = ((Integer) thisIndexIterator.next()).intValue();
          String fieldValue = String.valueOf(rowData.get(currentIndex - 1));

          if (fieldValue != null) {
            Pattern pattern = Pattern.compile("[^a-zA-z_0-9 ]");
            Matcher fieldMatch = pattern.matcher(fieldValue);
            fieldValue = fieldMatch.replaceAll("");
            // fieldValue = fieldValue.replaceAll("'","");
          }

          if (fieldValue == null || fieldValue.equals("null")) {
            fieldValue = "";
          }

          onSelectParameters.append(fieldValue);

          if (thisIndexIterator.hasNext()) {
            onSelectParameters.append("','");
          }
        }
        onSelectParameters.append("'");

        if (this.listObject.getDisplay().isRadioToCheckBox()) {
          radioType = "checkbox";
        }

        boolean flagRadio = true;
        String folderType = null;

        if (listTypeInteger.intValue() == ValueListConstants.FILE_LIST_TYPE) {
          folderType = rowData.get(ValueListConstants.KB_FILE_EMAIL_HACK_INDEX - 1).toString();

          if (folderType != null && folderType.equals("FOLDER")) {
            flagRadio = false;
          }
        }

        if (flagRadio) {
          this.addRadio(buffer, row.getRowId(), onSelectParameters.toString(), onRowValues.toString(), radioType, rowClass);
        }
      }

      if (this.listObject.getLookupType().equals("SecurityProfile")) {
        this.addCheckBox(buffer, row.getRowId(), odd, rowClass, rowData, ValueListConstants.SECURITY_PROFILE);
      }

      if (this.listObject.getDisplay().isIcon()) {
        if (this.listObject.getParameters().getValueListType() == ValueListConstants.EMAIL_LIST_TYPE) {
          this.addIcon(buffer, rowData, ValueListConstants.EMAIL_ICON, rowClass);
        } else if (this.listObject.getParameters().getValueListType() == ValueListConstants.KNOWLEDGEBASE_LIST_TYPE
            || this.listObject.getParameters().getValueListType() == ValueListConstants.FILE_LIST_TYPE) {
          this.addIcon(buffer, rowData, ValueListConstants.KB_FILE_EMAIL_HACK_INDEX, rowClass);
        } else {
          this.addIcon(buffer, rowData, ValueListConstants.ICON_INDEX, rowClass);
        }
      }

      if (this.listObject.getDisplay().isAttachmentIcon()) {
        this.addAttachmentIcon(buffer, rowData, ValueListConstants.ATTACHMENT_ICON, rowClass);
      }

      if (this.listObject.getDisplay().isPriority()) {
        this.addPriorityIcon(buffer, rowData, ValueListConstants.PRIORITY_ICON, rowClass);
      }

      if (this.listObject.getDisplay().isDownloadIcon()) {
        this.addDownloadIcon(buffer, rowData, ValueListConstants.DOWNLOAD_ICON, rowClass);
      }

      // readStatus is NO, that means we haven't read this message
      // we will apply the different Style to it
      if (readStatus != null && readStatus.equals("NO")) {
        rowClass = rowClass + "Bold";
      }

      Iterator selectedIterator = this.selectedColumns.iterator();

      while (selectedIterator.hasNext()) {
        int index = ((Integer) selectedIterator.next()).intValue();
        Object columnData = null;

        // If this a an email lookup list we need extra checkboxes on the front.
        if (this.listObject.isLookup() && this.listObject.getLookupType().equals("Email")) {
          switch (index) {
            case ValueListConstants.TO_INDEX:
            case ValueListConstants.CC_INDEX:
            case ValueListConstants.BCC_INDEX:
              this.addCheckBox(buffer, row.getRowId(), odd, rowClass, rowData, index);
              continue;
          }
        }

        if (index - 1 > rowData.size() || index - 1 < 0) { // Out of bounds,
                                                            // give null string
                                                            // in cell.
          columnData = "&nbsp;";
        } else {
          columnData = rowData.get(index - 1);
        }

        String out = (columnData == null) ? "" : columnData.toString();
        // Decorate the Data with an Link if appropriate.
        // Don't waste any time if there is no data to link against.
        if (!out.equals("") && this.listObject.getDisplay().isLink()) {
          out = this.decorate(index, rowData, out);
        }
        this.openTagCSS(buffer, ValueListConstants.COLUMN, rowClass);

        if (out.length() < 1) {
          StringBuffer tmpOut = new StringBuffer("");
          this.addImg(tmpOut, "spacer.gif", "");
          out = tmpOut.toString();
        }

        buffer.append(out);
        buffer.append(ValueListConstants.COLUMN_CLOSE);
      }
      buffer.append(ValueListConstants.ROW_CLOSE);
    }

    // IF we don't have any records then we should a row stating no records
    // found
    // we have to consider the no of columns , checkbox, radio, icon rows count
    if (numberOfRows == 0) {
      int colSpan = this.selectedColumns.size();
      if (this.listObject.getDisplay().isCheckboxes()) {
        colSpan++;
      }
      if (this.listObject.getDisplay().isRadio()) {
        colSpan++;
      }
      if (this.listObject.getDisplay().isIcon()) {
        colSpan++;
      }
      if (this.listObject.getDisplay().isAttachmentIcon()) {
        colSpan++;
      }
      if (this.listObject.getDisplay().isPriority()) {
        colSpan++;
      }
      if (this.listObject.getDisplay().isDownloadIcon()) {
        colSpan++;
      }
      buffer.append(ValueListConstants.EVEN_ROW_OPEN);
      buffer.append(ValueListConstants.COLUMN_PART_OPEN);
      buffer.append(" colspan=\"" + colSpan + "\" align = \"center\"");
      buffer.append(ValueListConstants.CLOSE);
      buffer.append("No Records Found.");
      buffer.append(ValueListConstants.COLUMN_CLOSE);
      buffer.append(ValueListConstants.ROW_CLOSE);
    }
  }

  private void addPriorityIcon(StringBuffer buffer, List rowData, int priorityIndex, String rowClass)
  {
    buffer.append(ValueListConstants.OPEN + ValueListConstants.COLUMN);
    buffer.append(" class=\"" + rowClass + "\" width=\"20\"");
    buffer.append(ValueListConstants.CLOSE);
    StringBuffer image = new StringBuffer();
    Object columnData = rowData.get(priorityIndex - 1);
    String columnValue = (columnData == null) ? "MEDIUM" : columnData.toString();
    String imageName = (String) ValueListConstants.emailIconMap.get(columnValue);
    if (imageName != null && !imageName.equals("")) {
      this.addImg(image, imageName, "Priority Details");
      buffer.append(image.toString());
    } else {
      buffer.append("&nbsp;");
    }
    buffer.append(ValueListConstants.COLUMN_CLOSE);
  }

  private void addAttachmentIcon(StringBuffer buffer, List rowData, int attachmentIndex, String rowClass)
  {
    buffer.append(ValueListConstants.OPEN + ValueListConstants.COLUMN);
    buffer.append(" class=\"" + rowClass + "\" width=\"20\"");
    buffer.append(ValueListConstants.CLOSE);
    StringBuffer image = new StringBuffer();
    Object columnData = rowData.get(attachmentIndex - 1);
    String columnValue = columnData == null ? "0" : columnData.toString();
    String imageName = "icon_attachment2.gif";
    if (columnValue != null && !columnValue.equals("0")) {
      this.addImg(image, imageName, "Attachment Details");
      buffer.append(image.toString());
    } else {
      buffer.append("&nbsp;");
    }
    buffer.append(ValueListConstants.COLUMN_CLOSE);
  }

  private void addDownloadIcon(StringBuffer buffer, List rowData, int downloadIndex, String rowClass)
  {
    buffer.append(ValueListConstants.OPEN + ValueListConstants.COLUMN);
    buffer.append(" class=\"" + rowClass + "\" width=\"20\"");
    buffer.append(ValueListConstants.CLOSE);

    buffer.append(ValueListConstants.ANCHOR_PART_OPEN);
    buffer.append("href=\"" + this.request.getContextPath() + ValueListConstants.DOWNLOAD_LINK);
    buffer.append(rowData.get(ValueListConstants.DOWNLOAD_COLUMN - 1) + "\"");
    buffer.append(ValueListConstants.CLOSE);

    StringBuffer image = new StringBuffer();
    String columnValue = (String) rowData.get(downloadIndex - 1);
    String imageName = "icon_download.gif";
    if (columnValue != null && columnValue.equals("FILE")) {
      this.addImg(image, imageName, "Download File");
      buffer.append(image.toString());
    } else {
      this.addImg(image, "spacer.gif", "");
      buffer.append(image.toString());
    }
    buffer.append(ValueListConstants.ANCHOR_CLOSE);
    buffer.append(ValueListConstants.COLUMN_CLOSE);
  }

  /**
   * Iterate the list filling the buffer with appropriate HTML, to create the
   * body of the table.
   * @param buffer StringBuffer that already has a properly opened Table
   */
  private void buildTableHead(StringBuffer buffer)
  {
    List columns = this.listObject.getParameters().getColumns();
    int columnsLength = columns.size();
    buffer.append(ValueListConstants.ROW_OPEN);
    if (this.listObject.getDisplay().isCheckboxes()) {
      this.addCheckAll(buffer);
    }
    if (this.listObject.getDisplay().isRadio()) {
      buffer.append("<td class=\"listHeader\">");
      this.addImg(buffer, "spacer.gif", "");
      buffer.append(ValueListConstants.COLUMN_HEAD_CLOSE);
    }
    // TODO: finish the isIcon() column
    if (this.listObject.getDisplay().isIcon()) {
      buffer.append("<td class=\"listHeader\">");
      if (this.listObject.getParameters().getValueListType() == ValueListConstants.EMAIL_LIST_TYPE) {
        this.addImg(buffer, "icon_email.gif", "");
      } else {
        this.addImg(buffer, "spacer.gif", "");
      }
      buffer.append(ValueListConstants.COLUMN_HEAD_CLOSE);
    }
    // TODO: finish the isAttachmentIcon() column
    if (this.listObject.getDisplay().isAttachmentIcon()) {
      buffer.append("<td class=\"listHeader\">");
      this.addImg(buffer, "icon_attachment1.gif", "Attachment");
      buffer.append(ValueListConstants.COLUMN_HEAD_CLOSE);
    }
    // TODO: finish the isPriority() column
    if (this.listObject.getDisplay().isPriority()) {
      buffer.append("<td class=\"listHeader\">");
      this.addImg(buffer, "icon_alerts.gif", "Priority");
      buffer.append(ValueListConstants.COLUMN_HEAD_CLOSE);
    }

    if (this.listObject.getDisplay().isDownloadIcon()) {
      buffer.append("<td class=\"listHeader\">");
      buffer.append(ValueListConstants.COLUMN_HEAD_CLOSE);
    }
    for (int i = 0; i < columnsLength; i++) {
      FieldDescriptor field = (FieldDescriptor) columns.get(i);
      String key = field.getExternalResourceKey();
      String columnName = null;
      if (key.length() > 0) {
        columnName = messages.getMessage(locale, field.getExternalResourceKey());
      } else {
        columnName = field.getFieldName();
      }
      this.openTagCSS(buffer, "TD", "listHeader");
      // build the sort link
      if (this.listObject.getDisplay().isSortable()) {
        this.addSortLink(buffer, field, columnName);
      } else {
        buffer.append(columnName);
      }
      buffer.append(ValueListConstants.COLUMN_HEAD_CLOSE);
      this.selectedColumns.add(new Integer(field.getQueryIndex()));
      buffer.append("\n");
    }
    buffer.append(ValueListConstants.ROW_CLOSE);
    buffer.append(ValueListConstants.HEAD_CLOSE);
  }

  private void addCheckBox(StringBuffer buffer, int rowId, boolean odd, String rowClass, List rowData, int moreCrap)
  {
    this.openTagCSS(buffer, ValueListConstants.COLUMN + " width=\"7\"", rowClass);

    // What an ungodly mess I've made... this use to be one line...
    String tmpCheckbox = null;
    if (rowData != null) {
      switch (moreCrap) {
        case ValueListConstants.TO_INDEX:
          tmpCheckbox = ValueListConstants.CHECKBOX_FIRST_HALF
              + "chkto"
              + ValueListConstants.CHECKBOX_SECOND_HALF.replaceAll("%", String.valueOf(rowId) + "*"
                  + rowData.get(ValueListConstants.KB_FILE_EMAIL_HACK_INDEX - 1));
          break;
        case ValueListConstants.CC_INDEX:
          tmpCheckbox = ValueListConstants.CHECKBOX_FIRST_HALF
              + "chkcc"
              + ValueListConstants.CHECKBOX_SECOND_HALF.replaceAll("%", String.valueOf(rowId) + "*"
                  + rowData.get(ValueListConstants.KB_FILE_EMAIL_HACK_INDEX - 1));
          break;
        case ValueListConstants.BCC_INDEX:
          tmpCheckbox = ValueListConstants.CHECKBOX_FIRST_HALF
              + "chkbcc"
              + ValueListConstants.CHECKBOX_SECOND_HALF.replaceAll("%", String.valueOf(rowId) + "*"
                  + rowData.get(ValueListConstants.KB_FILE_EMAIL_HACK_INDEX - 1));
          break;
        case ValueListConstants.SECURITY_PROFILE:
          tmpCheckbox = ValueListConstants.CHECKBOX_FIRST_HALF + "rowId"
              + ValueListConstants.CHECKBOX_SECOND_HALF.replaceAll("%", String.valueOf(rowId) + "|" + rowData.get(1));
          break;
        case ValueListConstants.FILE_INDEX:
          tmpCheckbox = ValueListConstants.CHECKBOX_FIRST_HALF
              + "rowId"
              + ValueListConstants.CHECKBOX_SECOND_HALF.replaceAll("%", String.valueOf(rowId) + "\\*"
                  + String.valueOf(rowData.get(ValueListConstants.KB_FILE_EMAIL_HACK_INDEX - 1)));
          break;
        default:
          tmpCheckbox = ValueListConstants.CHECKBOX_FIRST_HALF + "rowId"
              + ValueListConstants.CHECKBOX_SECOND_HALF.replaceAll("%", String.valueOf(rowId));
      }

    } else {
      tmpCheckbox = ValueListConstants.CHECKBOX_FIRST_HALF + "rowId" + ValueListConstants.CHECKBOX_SECOND_HALF.replaceAll("%", String.valueOf(rowId));
    }
    buffer.append(tmpCheckbox.replaceAll("#", String.valueOf(odd)));
    buffer.append(ValueListConstants.COLUMN_CLOSE);
  }

  private void addRadio(StringBuffer buffer, int rowId, String onClickString, String onRowString, String radioType, String rowClass)
  {
    buffer.append(ValueListConstants.OPEN);
    buffer.append(ValueListConstants.COLUMN);
    buffer.append(" class=\"" + rowClass + "\"");
    buffer.append(" width=\"20\"");
    buffer.append(ValueListConstants.CLOSE);
    String rowValue = String.valueOf(rowId);
    if (onRowString.length() != 0) {
      rowValue = onRowString;
    }
    String tmpRadio = ValueListConstants.RADIO.replaceAll("%", rowValue);

    tmpRadio = tmpRadio.replaceAll("#", onClickString);
    tmpRadio = tmpRadio.replaceAll("@", radioType);
    buffer.append(tmpRadio);
    buffer.append(ValueListConstants.COLUMN_CLOSE);
  }

  private void addCheckAll(StringBuffer buffer)
  {
    buffer.append("<td class=\"listHeader\">\n");
    buffer.append("<a href=\"javascript:void(0);\" class=\"listHeader\" onclick=\"vl_selectAllRows()\">");
    this.addImg(buffer, "icon_check.gif", "Check All");
    buffer.append("&nbsp;All</a></td>");
  }

  private void addImg(StringBuffer buffer, String imageName, String altText)
  {
    buffer.append("<img src=\"" + this.request.getContextPath() + "/images/");
    buffer.append(imageName);
    buffer.append("\" title=\"");
    buffer.append(altText);
    buffer.append("\" alt=\"");
    buffer.append(altText);
    buffer.append("\" border=\"0\"");
    buffer.append(ValueListConstants.CLOSING_CLOSE);
  }

  private void addIcon(StringBuffer buffer, List rowData, int iconIndex, String rowClass)
  {
    buffer.append("<td class=\"" + rowClass + "\" width=\"20\">");
    StringBuffer image = new StringBuffer();
    readStatus = "Yes";
    if (iconIndex == ValueListConstants.ICON_INDEX) {
      this.addImg(image, "icon_file.gif", "View Details");
    } else if (iconIndex == ValueListConstants.KB_FILE_EMAIL_HACK_INDEX) {
      String imageName = null;
      Object columnData = rowData.get(iconIndex - 1);
      // This "technique" is used by both Knowledgebase and Files.
      if (columnData != null && (columnData.equals("CATEGORY") || columnData.equals("FOLDER"))) {
        imageName = "icon_folder.gif";
        this.addImg(image, imageName, "");
      } else {
        imageName = "icon_file.gif";
        this.addImg(image, imageName, "");
      }
    } else {
      Object columnData = rowData.get(iconIndex - 1);
      String columnValue = columnData == null ? "NO" : columnData.toString();
      readStatus = columnValue;
      String imageName = (String) ValueListConstants.emailIconMap.get(columnValue);
      this.addImg(image, imageName, "View Details");
    }
    if (this.listObject.getDisplay().isLink()) {
      buffer.append(this.decorate(ValueListConstants.ICON_INDEX, rowData, image.toString()));
    } else {
      buffer.append(image);
    }
    buffer.append(ValueListConstants.COLUMN_CLOSE);
  }

  private void openTagCSS(StringBuffer buffer, String tag, String styleClass)
  {
    buffer.append(ValueListConstants.OPEN);
    buffer.append(tag);
    buffer.append(" class=\"" + styleClass + "\"");
    buffer.append(ValueListConstants.CLOSE);
  }

  /**
   * Method to add the header column name wrapped in a Sort link onto the
   * buffer. Builds the HREF adds the image, etc.
   * @param buffer
   * @param field
   * @param columnName
   */
  private void addSortLink(StringBuffer buffer, FieldDescriptor field, String columnName)
  {
    String imageName = null;
    String imageText = null;
    ValueListParameters parameters = this.listObject.getParameters();
    String currentPageParameters = this.listObject.getCurrentPageParameters();
    buffer.append(ValueListConstants.ANCHOR_PART_OPEN);
    buffer.append("class=\"listHeader\" href=\"");
    buffer.append(this.request.getContextPath());
    buffer.append("/Sort.do?listType=");
    buffer.append(parameters.getValueListType());
    buffer.append("&amp;page=");
    buffer.append(parameters.getCurrentPage());
    buffer.append("&amp;rpp=");
    buffer.append(parameters.getRecordsPerPage());
    if (this.listObject.isLookup()) {
      buffer.append("&amp;actionType=" + this.listObject.getLookupType());
    }
    if (this.pageContext.findAttribute("listFilter") != null) {
      buffer.append("&amp;filter=");
      buffer.append(this.request.getAttribute("appliedSearch"));
    }

    // Special case for Activities
    if (this.pageContext.findAttribute("superScope") != null) {
      buffer.append("&amp;superScope=");
      buffer.append(this.request.getAttribute("superScope"));
    }

    // Special case for Opportunities and Notes
    if (this.pageContext.findAttribute("listScope") != null) {
      buffer.append("&amp;listScope=");
      buffer.append(this.request.getAttribute("listScope"));
    }

    // It will be used to maintain the accountID which we are processing for
    // rules
    if (this.pageContext.findAttribute("accountID") != null) {
      buffer.append("&amp;accountID=");
      buffer.append(this.request.getAttribute("accountID"));
    }

    if (this.pageContext.findAttribute("folderID") != null) {
      buffer.append("&amp;folderID=");
      buffer.append(this.request.getAttribute("folderID"));
    }
    // Special case for related info so sorting forwards back to the right
    // place.
    if (this.listObject.getDisplay().isRelatedInfo()) {
      buffer.append("&amp;relatedInfo=true");
    }
    if (currentPageParameters != null) {
      buffer.append(currentPageParameters);
    }
    buffer.append("&amp;sortColumn=");
    buffer.append(field.getQueryIndex());
    buffer.append("&amp;sortDirection=");
    boolean isSortedColumn = (parameters.getSortColumn() == field.getQueryIndex());
    if (isSortedColumn && parameters.getSortDirection().equals("ASC")) {
      buffer.append("DESC");
      imageName = "icon_sort_descending.gif";
      imageText = "Sorted Descending";
    } else {
      buffer.append("ASC");
      imageName = "icon_sort_ascending.gif";
      imageText = "Sorted Ascending";
    }
    buffer.append("\"");
    buffer.append(ValueListConstants.CLOSE);
    buffer.append(columnName);
    buffer.append(ValueListConstants.ANCHOR_CLOSE);
    if (isSortedColumn) {
      buffer.append("&nbsp;");
      this.addImg(buffer, imageName, imageText);
    }
  }

  private void pagingBarDetails(StringBuffer buffer)// , boolean top)
  {
    String tableWidth = this.listObject.isLookup() ? "100%" : "600";

    buffer.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"" + tableWidth + "\">\n");
    buffer.append("<tr>\n");
    buffer.append("<td align=\"left\" valign=\"top\">\n");
    buffer.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
    buffer.append("<tr>\n");
    ArrayList buttonList = this.listObject.getDisplay().getButtonObjects();
    for (int i = 0; i < buttonList.size(); i++) {
      Button button = (Button) buttonList.get(i);
      this.addButton(buffer, button.getName(), button.getLabel(), button.getOnClick(), button.isDisabled());
    }
    if (this.listObject.isMoveTo()) {
      this.addMoveTo(buffer, this.listObject.getParameters().getValueListType());
    }
    buffer.append("</tr>\n");
    buffer.append("</table>\n");
    buffer.append("</td>\n");
    buffer.append("<td align=\"right\" valign=\"top\">\n");
    buffer.append("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">\n");
    buffer.append("<tr>\n");
    this.addPagingButtons(buffer);
    if (!this.listObject.getDisplay().isSkipRPP()) {
      if (!this.listObject.isLookup()) {
        this.addRPPDropDown(buffer);
      }
    }
    buffer.append("<td class=\"pagingButtonTD\" nowrap><span class=\"pagingText\">");
    buffer.append(this.listObject.getParameters().getTotalRecords());
    buffer.append("&nbsp;Records</span></td>\n");
    buffer.append("</tr>\n");
    buffer.append("</table>\n");
    buffer.append("</td>\n");
    buffer.append("</tr>\n");
    buffer.append("</table>\n");
  }

  private void addRPPDropDown(StringBuffer buffer)
  {
    this.openTagCSS(buffer, ValueListConstants.COLUMN, "pagingButtonTD");
    this.openTagCSS(buffer, ValueListConstants.SPAN, "pagingText");
    buffer.append("Records/pg:");
    buffer.append(ValueListConstants.SPAN_CLOSE);
    buffer.append(ValueListConstants.COLUMN_CLOSE);
    this.openTagCSS(buffer, ValueListConstants.COLUMN, "pagingButtonTD");
    ValueListParameters parameters = this.listObject.getParameters();
    String currentPageParameters = this.listObject.getCurrentPageParameters();
    // calculate the URLs for the paging buttons
    StringBuffer pagingURLBuffer = new StringBuffer("");
    pagingURLBuffer.append("/Sort.do?listType=");
    pagingURLBuffer.append(parameters.getValueListType());
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("page=");
    pagingURLBuffer.append(parameters.getCurrentPage());
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("sortColumn=");
    pagingURLBuffer.append(parameters.getSortColumn());
    // Special case for Activities
    if (this.pageContext.findAttribute("superScope") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("superScope=");
      pagingURLBuffer.append(this.request.getAttribute("superScope"));
    }
    // Special case for Opportunities
    if (this.pageContext.findAttribute("listScope") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("listScope=");
      pagingURLBuffer.append(this.request.getAttribute("listScope"));
    }
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("sortDirection=");
    pagingURLBuffer.append(parameters.getSortDirection());
    if (this.pageContext.findAttribute("listFilter") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("filter=");
      pagingURLBuffer.append(request.getAttribute("appliedSearch"));
    }
    // It will be used to maintain the accountID which we are processing for
    // rules
    if (this.pageContext.findAttribute("accountID") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("accountID=");
      pagingURLBuffer.append(this.request.getAttribute("accountID"));
    }
    if (this.pageContext.findAttribute("folderID") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("folderID=");
      pagingURLBuffer.append(this.request.getAttribute("folderID"));
    }
    if (this.listObject.isLookup()) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("actionType=" + this.listObject.getLookupType());
    }
    if (currentPageParameters != null) {
      pagingURLBuffer.append(currentPageParameters);
    }
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("rpp=");
    String pagingURL = pagingURLBuffer.toString();
    buffer.append("<select class=\"pagingDropdown\" onchange=\"vl_rppGoTo(this.value);\">\n");
    int[] possibleRPP = { 10, 20, 50, 100 };
    for (int i = 0; i < possibleRPP.length; i++) {
      int tmpRPP = possibleRPP[i];
      boolean selected = false;
      int selectedRPP = listObject.getParameters().getRecordsPerPage();
      if (tmpRPP == selectedRPP) {
        selected = true;
      }
      buffer.append("<option value=\"");
      buffer.append(pagingURL);
      buffer.append(tmpRPP);
      buffer.append("\"");
      if (selected) {
        buffer.append(" selected=\"selcted\"");
      }
      buffer.append(">");
      buffer.append(tmpRPP);
      buffer.append("</option>\n");
    }
    buffer.append("</select>\n");
    buffer.append("</td>\n");
  }

  private void addPagingButtons(StringBuffer buffer)
  {
    ValueListParameters parameters = this.listObject.getParameters();
    String currentPageParameters = this.listObject.getCurrentPageParameters();

    int current = parameters.getCurrentPage();
    int rpp = parameters.getRecordsPerPage();
    int totalRecords = parameters.getTotalRecords();
    long totalPages = 0;
    if (rpp > 0) {
      totalPages = (long) Math.ceil((double) totalRecords / rpp);
    } else {
      totalPages = 1;
    }
    int tmp;
    // calculate the URLs for the paging buttons
    StringBuffer pagingURLBuffer = new StringBuffer("c_goTo('");
    pagingURLBuffer.append("/Sort.do?listType=");
    pagingURLBuffer.append(parameters.getValueListType());
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("sortColumn=");
    pagingURLBuffer.append(parameters.getSortColumn());
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("sortDirection=");
    pagingURLBuffer.append(parameters.getSortDirection());
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("rpp=");
    pagingURLBuffer.append(rpp);
    // Special case for Activities
    if (this.pageContext.findAttribute("superScope") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("superScope=");
      pagingURLBuffer.append(this.request.getAttribute("superScope"));
    }
    // Special case for Opportunities
    if (this.pageContext.findAttribute("listScope") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("listScope=");
      pagingURLBuffer.append(this.request.getAttribute("listScope"));
    }
    if (this.pageContext.findAttribute("listFilter") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("filter=");
      pagingURLBuffer.append(this.request.getAttribute("appliedSearch"));
    }
    // It will be used to maintain the accountID which we are processing for
    // rules
    if (this.pageContext.findAttribute("accountID") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("accountID=");
      pagingURLBuffer.append(this.request.getAttribute("accountID"));
    }
    if (this.pageContext.findAttribute("folderID") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("folderID=");
      pagingURLBuffer.append(this.request.getAttribute("folderID"));
    }
    if (this.listObject.isLookup()) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("actionType=" + this.listObject.getLookupType());
    }
    // Special case for related info so sorting forwards back to the right
    // place.
    if (this.listObject.getDisplay().isRelatedInfo()) {
      pagingURLBuffer.append("&amp;relatedInfo=true");
    }
    if (currentPageParameters != null) {
      pagingURLBuffer.append(currentPageParameters);
    }
    pagingURLBuffer.append(ValueListConstants.AMP);
    String pagingURL = pagingURLBuffer.toString();
    boolean disabled = false;
    // First information
    String tmpURL = pagingURL + "page=1');";
    if (current == 1) {
      disabled = true;
    }
    this.addButton(buffer, "First Page", "|&laquo;", tmpURL, disabled);
    // Previous information
    disabled = false;
    tmp = current - 1;
    if (tmp < 1) {
      tmp = 1;
      disabled = true;
    }
    tmpURL = pagingURL + "page=" + String.valueOf(tmp) + "');";
    this.addButton(buffer, "Previous Page", "&laquo;", tmpURL, disabled);
    disabled = false;
    // Paging information
    if (totalPages == 0) {
      current = 0;
    }
    // this.openTagCSS(buffer, ValueListConstants.COLUMN,
    // stylePagingAmberTextClass);
    buffer.append("<td class=\"pagingButtonTD\" nowrap>");
    buffer.append("<span class=\"pagingText\">Page:&nbsp;");
    buffer.append(current);
    buffer.append("/");
    buffer.append(totalPages);
    buffer.append("</span>");
    buffer.append(ValueListConstants.COLUMN_CLOSE);
    // next information
    tmp = current + 1;
    if (tmp > totalPages) {
      tmp = (int) totalPages; // here's hoping we don't have billions of pages
                              // ;-)
      disabled = true;
    }
    tmpURL = pagingURL + "page=" + String.valueOf(tmp) + "');";
    this.addButton(buffer, "Next Page", "&raquo;", tmpURL, disabled);
    disabled = false;
    // last
    tmp = (int) totalPages;
    if (current == totalPages) {
      disabled = true;
    }
    tmpURL = pagingURL + "page=" + String.valueOf(tmp) + "');";
    this.addButton(buffer, "Last Page", "&raquo;|", tmpURL, disabled);
  }

  private void addButton(StringBuffer buffer, String buttonName, String buttonValue, String onClick, boolean disabled)
  {

    buffer.append("<td class=\"pagingButtonTD\">");
    buffer.append("<input ");

    List list = listObject.getList();
    numberOfRows = list.size();
    boolean isLookup = this.listObject.isLookup();

    String styleButtonClass = "normalButton";
    if (isLookup) {
      styleButtonClass = "normalButton";
    }
    if (disabled) {
      styleButtonClass = "disabledButton";
      buffer.append("disabled ");
    }

    buffer.append("type=\"button\" class=\"" + styleButtonClass + "\" name=\"");
    buffer.append(buttonName);
    buffer.append("\" value=\"");
    buffer.append(buttonValue);
    buffer.append("\"");
    if (onClick != null) {
      buffer.append(" onClick=\"");
      buffer.append(onClick);
      buffer.append("\"");
    }
    buffer.append(ValueListConstants.CLOSING_CLOSE);
    buffer.append("</td>\n");
  }

  private String currentPageURL()
  {
    ValueListParameters parameters = this.listObject.getParameters();
    String currentPageParameters = this.listObject.getCurrentPageParameters();
    StringBuffer pagingURLBuffer = new StringBuffer();
    pagingURLBuffer.append("/Sort.do?listType=");
    pagingURLBuffer.append(parameters.getValueListType());
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("sortColumn=");
    pagingURLBuffer.append(parameters.getSortColumn());
    // Special case for Activities
    if (this.pageContext.findAttribute("superScope") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("superScope=");
      pagingURLBuffer.append(this.request.getAttribute("superScope"));
    }
    // Special case for Opportunities
    if (this.pageContext.findAttribute("listScope") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("listScope=");
      pagingURLBuffer.append(this.request.getAttribute("listScope"));
    }
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("sortDirection=");
    pagingURLBuffer.append(parameters.getSortDirection());
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("rpp=");
    pagingURLBuffer.append(parameters.getRecordsPerPage());
    pagingURLBuffer.append(ValueListConstants.AMP);
    pagingURLBuffer.append("page=");
    pagingURLBuffer.append(parameters.getCurrentPage());
    if (this.listObject.isLookup()) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("actionType=" + this.listObject.getLookupType());
    }
    if (this.pageContext.findAttribute("listFilter") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("filter=");
      pagingURLBuffer.append(this.request.getAttribute("appliedSearch"));
    }
    // It will be used to maintain the accountID which we are processing for
    // rules
    if (this.pageContext.findAttribute("accountID") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("accountID=");
      pagingURLBuffer.append(this.request.getAttribute("accountID"));
    }
    if (this.pageContext.findAttribute("folderID") != null) {
      pagingURLBuffer.append(ValueListConstants.AMP);
      pagingURLBuffer.append("folderID=");
      pagingURLBuffer.append(this.request.getAttribute("folderID"));
    }
    if (currentPageParameters != null) {
      pagingURLBuffer.append(currentPageParameters);
    }
    return pagingURLBuffer.toString();
  }

  private String decorate(int index, List rowData, String out)
  {
    Integer listTypeInteger = new Integer(this.listObject.getParameters().getValueListType());
    HashMap decoratorMap = (HashMap) ValueListConstants.decoratorData.get(listTypeInteger);
    Integer indexInteger = new Integer(index);
    String folderName = null;

    if (this.pageContext.findAttribute("folderName") != null) {
      folderName = (String) this.request.getAttribute("folderName");
    }

    String lookupType = this.listObject.getLookupType();
    boolean lookupDecorationFlag = true;

    if (listTypeInteger.intValue() == ValueListConstants.ORDER_LIST_TYPE && lookupType != null && lookupType.equals("lookup")
        && index == ValueListConstants.ORDER_INDEX) {
      lookupDecorationFlag = false;
    }

    if (listTypeInteger.intValue() == ValueListConstants.ITEM_LIST_TYPE && lookupType != null && lookupType.startsWith("lookup")) {
      String content = rowData.get(ValueListConstants.ITEM_CHILD_COUNT_INDEX - 1).toString();

      if (content != null && content.equals("0")) {
        lookupDecorationFlag = false;
      }

      if (lookupType != null && lookupType.equals("lookupSubItem")) {
        lookupDecorationFlag = false;
      }
    }

    if (decoratorMap != null && decoratorMap.containsKey(indexInteger) && lookupDecorationFlag) {
      FieldDecoration decoration = null;
      if (folderName != null && folderName.equals(MailFolderVO.DRAFTS_FOLDER_NAME)) {
        decoration = (FieldDecoration) decoratorMap.get(new Integer(ValueListConstants.ICON_INDEX_FOLDER));
      } else if (folderName != null && folderName.equals(MailFolderVO.TEMPLATES_FOLDER_NAME)) {
        decoration = (FieldDecoration) decoratorMap.get(new Integer(ValueListConstants.ICON_INDEX_EMAIL_TEMPLATES));
      } else if (listTypeInteger.intValue() == ValueListConstants.ITEM_LIST_TYPE && lookupType != null && lookupType.equals("lookup")) {
        decoration = (FieldDecoration) ValueListConstants.itemLookupMap.get(new Integer(ValueListConstants.ICON_INDEX));
      } else {
        decoration = (FieldDecoration) decoratorMap.get(indexInteger);
      }

      StringBuffer decoratedField = new StringBuffer();
      String formatedContent = decorateFormat(index, rowData, decoration, out);
      decoratedField.append(formatedContent);
      return decoratedField.toString();
    }
    return out;
  } // end decorate() method

  private void addMoveTo(StringBuffer buffer, int listType)
  {
    this.openTagCSS(buffer, ValueListConstants.COLUMN, "pagingButtonTD");
    buffer.append("<select class=\"pagingDropDown\" onchange=\"vl_moveTo(this.value);\">\n");
    buffer.append("<option value=\"-1\">Move To</option>\n");
    if (listType == ValueListConstants.ENTITY_LIST_TYPE) {
      Vector buttonValueList = (Vector) pageContext.findAttribute("AllDBList");
      for (int i = 0; i < buttonValueList.size(); i++) {
        DDNameValue ddname = (DDNameValue) buttonValueList.elementAt(i);
        if (!(ddname.getName()).equals("All Lists")) {
          buffer.append("<option value=\"");
          buffer.append(ddname.getStrid());
          buffer.append("\">");
          buffer.append(ddname.getName());
          buffer.append("</option>\n");
        }
      }
    }

    if (listType == ValueListConstants.EMAIL_LIST_TYPE) {
      HashMap buttonValueList = (HashMap) pageContext.findAttribute("folderList");

      String folderID = null;
      if (pageContext.findAttribute("folderID") != null) {
        folderID = (this.request.getAttribute("folderID")).toString();
      }
      Set buttonValueKey = buttonValueList.keySet();
      Iterator iter = buttonValueKey.iterator();
      while (iter.hasNext()) {
        Number key = (Number) iter.next();
        String value = (String) buttonValueList.get(key);

        if ((value != null && !value.equals("root")) && (folderID != null && !folderID.equals(key.toString()))) {
          buffer.append("<option value=\"");
          buffer.append(key);
          buffer.append("\">");
          buffer.append(value);
          buffer.append("</option>\n");
        }
      }
    }
    buffer.append("</select>\n");
  }

  /**
   * This method will apply the field decoration format on output of Field.
   * Returning the decorated column by applying format on it.
   * @param index position of the Field in rowData.
   * @param decorationType To specify which decoration should apply on Field.
   * @param rowData Collection of data for a particular row
   * @param decoration Class which will have the other related information of
   *          style.
   * @param out String to displayed in the screen.
   * @return decoratedField decorated column by applying format on it.
   */
  private String decorateFormat(int index, List rowData, FieldDecoration decoration, String out)
  {
    Integer listTypeInteger = new Integer(this.listObject.getParameters().getValueListType());
    StringBuffer decoratedField = new StringBuffer();
    Date valueDate = null;
    Timestamp timeStamp = null;
    boolean flagUrl = true;
    String valueString = "";
    int decorationType = decoration.getFieldDecorationType();

    Object content = rowData.get(decoration.getParameter() - 1);

    switch (decorationType) {
      case FieldDecoration.urlType:
        String folderType = "";
        if (listTypeInteger.intValue() == ValueListConstants.FILE_LIST_TYPE && this.listObject.getLookupType() != null
            && this.listObject.getLookupType().equals("lookup")) {
          folderType = rowData.get(ValueListConstants.KB_FILE_EMAIL_HACK_INDEX - 1).toString();
          if (folderType != null && folderType.equals("FILE")) {
            flagUrl = false;
          }
        }

        if (flagUrl) {
          // display the string in Hyper link format
          decoratedField.append(ValueListConstants.ANCHOR_PART_OPEN);
          decoratedField.append("class=\"plainLink\" ");
          decoratedField.append("href=\"javascript:");
          decoratedField.append(decoration.getUrlOpen());
          valueString = content.toString();
          valueString = valueString.replaceAll("\"", "&#34;");

          // For file and knowledgebase lists we will append type parameter to
          // the url.
          if (listTypeInteger.intValue() == ValueListConstants.KNOWLEDGEBASE_LIST_TYPE
              || listTypeInteger.intValue() == ValueListConstants.FILE_LIST_TYPE) {
            valueString = valueString.concat("&type=" + rowData.get(ValueListConstants.KB_FILE_EMAIL_HACK_INDEX - 1));
            if (this.listObject.isLookup()) {
              valueString += ValueListConstants.AMP + "actionType=" + this.listObject.getLookupType();
            }
          }
          decoratedField.append(valueString);

          if (this.pageContext.findAttribute("listScope") != null) {
            decoratedField.append("&amp;listScope=");
            decoratedField.append(this.request.getAttribute("listScope"));
          }

          if (this.listObject.getCurrentLinkParameters() != null && !this.listObject.getCurrentLinkParameters().equals("")) {
            decoratedField.append(this.listObject.getCurrentLinkParameters());
          }
          decoratedField.append(decoration.getUrlClose());
          decoratedField.append("\"");
          decoratedField.append(ValueListConstants.CLOSE);
          // For completed activities
          if (((listTypeInteger.intValue() == ValueListConstants.ACTIVITY_LIST_TYPE)
              || (listTypeInteger.intValue() == ValueListConstants.APPOINTMENT_LIST_TYPE)
              || (listTypeInteger.intValue() == ValueListConstants.CALL_LIST_TYPE)
              || (listTypeInteger.intValue() == ValueListConstants.FORECASTSALES_LIST_TYPE)
              || (listTypeInteger.intValue() == ValueListConstants.MEETING_LIST_TYPE)
              || (listTypeInteger.intValue() == ValueListConstants.NEXTACTION_LIST_TYPE)
              || (listTypeInteger.intValue() == ValueListConstants.TODO_LIST_TYPE) || (listTypeInteger.intValue() == ValueListConstants.TASK_LIST_TYPE))
              && (index == ValueListConstants.ACTIVITY_TITLE_INDEX)) {
            Object columnData = rowData.get(ValueListConstants.ACTIVITY_STATUS_INDEX - 1);
            if (((String) columnData).indexOf("Completed") != -1) {
              decoratedField.append(ValueListConstants.SPAN_PART_OPEN);
              decoratedField.append("class=\"completedActivity\" ");
              decoratedField.append(ValueListConstants.CLOSE);
              decoratedField.append(out);
              decoratedField.append(ValueListConstants.SPAN_CLOSE);
            } else {
              decoratedField.append(out);
              decoratedField.append(ValueListConstants.ANCHOR_CLOSE);
            }
          } else {
            decoratedField.append(out);
            decoratedField.append(ValueListConstants.ANCHOR_CLOSE);
          }
        } else {
          decoratedField.append(out);
        }
        break;
      case FieldDecoration.dateType: {
        // use the standard i18n way of formating these.
        // that means using the default formats based on locale.
        if (content != null) {
          DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, this.locale);
          if (content instanceof Timestamp) {
            timeStamp = (Timestamp) content;
            valueDate = new Date(timeStamp.getTime());
            valueString = df.format(valueDate);
          }
          if (content instanceof java.sql.Date) {
            java.sql.Date dateStamp = (java.sql.Date) content;
            valueString = df.format(dateStamp);
          }
          if (content instanceof Long) {
            Long dateObject = (Long) content;
            valueDate = new Date();
            valueDate.setTime(dateObject.longValue());
            valueString = df.format(valueDate);
          }
          decoratedField.append(valueString);
        }
        break;
      }
      case FieldDecoration.dateTimeType: {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, this.locale);
        timeStamp = (Timestamp) content;
        if (timeStamp != null) {
          valueDate = new Date(timeStamp.getTime());
          valueString = df.format(valueDate);
        }
        decoratedField.append(valueString);
        break;
      }
      case FieldDecoration.timeType: {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM, this.locale);
        if (content instanceof Timestamp) {
          timeStamp = (Timestamp) content;
          valueDate = new Date(timeStamp.getTime());
          if (timeStamp != null) {
            valueDate = new Date(timeStamp.getTime());
            valueString = df.format(valueDate);
          }
        }
        if (content instanceof Long) {
          Long dateObject = (Long) content;
          if (dateObject != null) {
            valueDate = new Date();
            valueDate.setTime(dateObject.longValue());
            valueString = df.format(dateObject);
          }
        }
        decoratedField.append(valueString);
        break;
      }
      case FieldDecoration.fileSizeType:
        // display the string to file Size
        // which in byte to GB,MB,KB
        float size = (new Float(content.toString())).floatValue();
        DecimalFormat df = new DecimalFormat("###.#");
        Integer s = new Integer((int) size);
        valueString = s.toString();
        if (size > 1000000000) {
          size /= 1073741824; // 1GB
          valueString = df.format(size) + "GB";
        } else if (size > 1000000) {
          size /= 1048576; // 1MB
          valueString = df.format(size) + "MB";
        } else if (size > 1000) {
          size /= 1024; // 1KB
          valueString = df.format(size) + "KB";
        }
        decoratedField.append(valueString);
        break;

      case FieldDecoration.moneyType:
        // display the string to currency format
        NumberFormat numFormatter = NumberFormat.getCurrencyInstance(this.locale);
        if (content == null) {
          content = "0";
        }
        valueString = content.toString();
        Double doubleValue = new Double(valueString);
        if (doubleValue != null) {
          String contentFormated = numFormatter.format(doubleValue.doubleValue());
          decoratedField.append(contentFormated);
        }
        break;
      case FieldDecoration.hourType:
        // We are using the old version of mysql we must have to do this..
        // In futher version of mysql 4.1.1 as the datediff function we can use
        // that, but now no choice
        // we have to do this calculation.

        int startTimeIndex = ((Integer) ValueListConstants.timeslipDurationCalc.get("Start")).intValue();
        int endTimeIndex = ((Integer) ValueListConstants.timeslipDurationCalc.get("End")).intValue();

        Object startTimeObject = rowData.get(startTimeIndex - 1);
        Object endTimeObject = rowData.get(endTimeIndex - 1);
        long startTime = 0;
        long endTime = 0;
        if (startTimeObject != null && startTimeObject instanceof Timestamp) {
          Timestamp startTimeStamp = (Timestamp) startTimeObject;
          if (startTimeStamp != null) {
            startTime = startTimeStamp.getTime();
          }
        }
        if (endTimeObject != null && endTimeObject instanceof Timestamp) {
          Timestamp endTimeStamp = (Timestamp) endTimeObject;
          if (endTimeStamp != null) {
            endTime = endTimeStamp.getTime();
          }
        }

        if (startTime != 0 && endTime != 0) {
          long diffTime = endTime - startTime;
          Date diffDateTime = new Date(diffTime);
          int hour = diffDateTime.getHours();
          int minute = diffDateTime.getMinutes();

          int breakHours = 0;
          int breakMinutes = 0;
          String breakTimeString = content.toString();
          int indexOfdot = breakTimeString.indexOf(".");
          if (breakTimeString != null && breakTimeString.length() != 0) {
            int len = breakTimeString.length();
            if (indexOfdot != -1) {
              breakHours = Integer.parseInt(breakTimeString.substring(0, indexOfdot));
              breakMinutes = Integer.parseInt(breakTimeString.substring((indexOfdot + 1), len));
            } else {
              breakHours = Integer.parseInt(breakTimeString);
            }
          }
          int diffMin = 0;
          if (minute > breakMinutes) {
            diffMin = minute - breakMinutes;
          } else {
            diffMin = 60 - breakMinutes;
          }
          if (diffMin < 0) {
            hour = hour - 1;
          }
          int diffHour = hour - breakHours;
          valueString = diffHour + "." + diffMin;
          if (diffHour > 0 && diffHour == 1) {
            valueString += " hour";
          } else if (diffHour > 1) {
            valueString += " hours";
          } else {
            valueString += " minutes";
          }
          decoratedField.append(valueString);
        }
        break;
      case FieldDecoration.paidType:
        Object totalAmount = rowData.get(ValueListConstants.INVOICE_AMOUNT - 1);
        double amount = 0;
        double amountPaid = 0;

        if (totalAmount != null && totalAmount instanceof Number) {
          amount = ((Number) totalAmount).doubleValue();
        }
        if (content != null && content instanceof Number) {
          amountPaid = ((Number) content).doubleValue();
        }

        int result = 0;
        if ((totalAmount != null) && (content != null)) {
          result = Double.compare(amount, amountPaid);
        }
        if (result == 0) {
          valueString = "Yes";
        } else if ((result > 0) && (amountPaid > 0)) {
          valueString = "Partial";
        } else {
          valueString = "No";
        }
        decoratedField.append(valueString);
        break;
      case FieldDecoration.limitCharsType:
        // limits the length of a String field to the given numberOfChars
        // defined by the "decoration" object. Adds three elipses at the end.
        int numberOfChars = decoration.getNumberOfChars();
        String contentString = content.toString();
        if (numberOfChars >= 1) { // make sure numberOfChars isn't 0 or smaller
          if (contentString.length() > numberOfChars) { // make sure string
                                                        // isn't shorter than
                                                        // given numberOfChars
            decoratedField.append(contentString.substring(0, numberOfChars) + "...");
          } else {
            // field is too small, print it all out
            decoratedField.append(contentString);
          }
        } else {
          // numberOfChars is not valid, default to entire field length
          decoratedField.append(contentString);
        }
        break;
    }
    return decoratedField.toString();
  }
}
