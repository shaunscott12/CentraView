/*
 * $RCSfile: PaymentForm.java,v $    $Revision: 1.2 $  $Date: 2005/09/07 19:38:11 $ - $Author: mcallist $
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

package com.centraview.account.payment;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;

import com.centraview.account.common.AccountConstantKeys;
import com.centraview.account.common.ItemElement;
import com.centraview.account.common.PaymentLines;
import com.centraview.common.DateMember;
import com.centraview.common.DoubleMember;
import com.centraview.common.IntMember;
import com.centraview.common.StringMember;
import com.centraview.common.Validation;

public class PaymentForm extends org.apache.struts.action.ActionForm
{

	protected static MessageResources messages = MessageResources.getMessageResources("ApplicationResources");

	private String[] linestatus;

   /*
	* Save entity id and name
	*/
	private String entityId;
	private int entityIdValue;
	private String entityName;

   /*
   	* Save other fields in payment form
    */
	private String paymentAmount;
	private float pmntAmount;
	private String paymentMethodID;
	private int paymentMethodIDValue;

	private String pmntID;
	private int pmntIDValue;
	private String reference;
	private String description;
	private String cardType;
	private String cardNumber;
	private java.sql.Date cardExpiry;
	private String cardDateStr;
	private String checkNumber;

	/*
	*	Stores COLUMNS related to INVOICES
	*/
	private int[] invoiceID;
	private String paymentID;
	private String[] invoiceNum;
	private int[] total;
	private int[] amtDue;
	private double[] payment;
	private java.sql.Date[] invDate;
	private String[] invDateStr;
	private int modifiedBy;

	private PaymentLines paymentLines;
	private PaymentLines paymentAppliedLines;

	private double totalPayment = 0.0;

	public void	convertPaymentLines()
	{
		int count;
		paymentLines = new PaymentLines();
		paymentAppliedLines = new PaymentLines();

		if (paymentLines != null)
		{
			if (invoiceID != null)
			{
				for (int i=0;i<invoiceID.length;i++)
				{
					ItemElement ie = new ItemElement();
					IntMember 		InvoiceId = new IntMember("InvoiceId",invoiceID[i],'D',"",'T',false,20);
					String invNumStr = "";
					StringMember 	InvoiceNum = new StringMember("InvoiceNum",invoiceNum[i],'D',"",'T',false);

					DateFormat   df = new SimpleDateFormat("yyyy-mm-dd") ;
					java.util.Date InvoiceDate = new java.util.Date();
					try{
						InvoiceDate = df.parse(invDateStr[i]) ;
					}
					catch (Exception e){
						e.printStackTrace();
					}
					DateMember invoiceDate = new DateMember ("Date",InvoiceDate,10,"",'T',false,1,"EST");

					DoubleMember 	Total = new DoubleMember("Total",new Double(total[i]),'D',"0",'T',false,20);


					DoubleMember 	AmountDue = new DoubleMember("AmountDue",new Double(amtDue[i]),'D',"",'T',false,20);
					DoubleMember 	AmountApplied = new DoubleMember("AmountApplied",new Double(payment[i]),'D',"",'T',false,20);


					ie.put ("InvoiceId", InvoiceId);
					ie.put ("InvoiceNum", InvoiceNum);
					ie.put ("Date", invoiceDate);
					ie.put ("Total", Total);
					ie.put ("AmountDue", AmountDue);
					ie.put ("AmountApplied", AmountApplied);

					String status = linestatus[i];
					if(status == null)
						status = "New";

					ie.setLineStatus(status);

					totalPayment += payment[i];

				//	double paymentamount = 0.0d;

					//paymentamount += AmountApplied;

					paymentLines.put(new Integer(i),ie);



				}
			}

			//paymentamount += totalPayment;
			//this.setPaymentAmount(paymentamount);
		}
	}


	public int[] getAmtDue()
	{
		return this.amtDue;
	}

	public void setAmtDue(int[] amtDue)
	{
		this.amtDue = amtDue;
	}

	public String getCardNumber()
	{
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber)
	{
		this.cardNumber = cardNumber;
	}

	public String getCardType()
	{
		return this.cardType;
	}

	public void setCardType(String cardType)
	{
		this.cardType = cardType;
	}


	public String getEntityId()
	{
		return this.entityId;
	}

	public void setEntityId(String entityId)
	{
		this.entityId = entityId;
	}


	public String getEntityName()
	{
		return this.entityName;
	}

	public void setEntityName(String entityName)
	{
		this.entityName = entityName;
	}


	public Date[] getInvDate()
	{
		return this.invDate;
	}

	public void setInvDate(Date[] invDate)
	{
		this.invDate = invDate;
	}


	public String[] getInvoiceNum()
	{
		return this.invoiceNum;
	}

	public void setInvoiceNum(String[] invoiceNum)
	{
		this.invoiceNum = invoiceNum;
	}


	public String[] getLinestatus()
	{
		return this.linestatus;
	}

	public void setLinestatus(String[] linestatus)
	{
		this.linestatus = linestatus;
	}


	public double[] getPayment()
	{
		return this.payment;
	}

	public void setPayment(double[] payment)
	{
		this.payment = payment;
	}


	public String getPaymentAmount()
	{
		return this.paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount)
	{
		this.paymentAmount = paymentAmount;
	}


	public String getPaymentID()
	{
		return this.paymentID;
	}

	public void setPaymentID(String paymentID)
	{
		this.paymentID = paymentID;
	}


	public PaymentLines getPaymentAppliedLines()
	{
		return this.paymentAppliedLines;
	}

	public void setPaymentAppliedLines(PaymentLines paymentAppliedLines)
	{
		this.paymentAppliedLines = paymentAppliedLines;
	}


	public PaymentLines getPaymentLines()
	{
		return this.paymentLines;
	}

	public void setPaymentLines(PaymentLines paymentLines)
	{
		this.paymentLines = paymentLines;
	}


	public String getPmntID()
	{
		return this.pmntID;
	}

	public void setPmntID(String pmntID)
	{
		this.pmntID = pmntID;
	}


	public String getReference()
	{
		return this.reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}


	public int[] getTotal()
	{
		return this.total;
	}

	public void setTotal(int[] total)
	{
		this.total = total;
	}


	public String getDescription()
	{
		return this.description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}


	public String getCheckNumber()
	{
		return this.checkNumber;
	}

	public void setCheckNumber(String checkNumber)
	{
		this.checkNumber = checkNumber;
	}

	public void convertFormbeanToValueObject()
	{
		try
		{
			if(paymentMethodID != null && paymentMethodID.length() != 0)
				this.paymentMethodIDValue = Integer.parseInt(paymentMethodID);

			if(entityId != null && entityId.length() != 0)
				this.entityIdValue = Integer.parseInt(entityId);

			if(pmntID != null && pmntID.length() != 0)
				this.pmntIDValue = Integer.parseInt(pmntID);

			if(paymentAmount != null && paymentAmount.length() != 0)
				this.pmntAmount = Float.parseFloat(paymentAmount);

			if (cardExpiry != null)
			{
				// format date later
				this.cardDateStr = cardExpiry+"";

			}
			//temporary code
			else
			{
				this.setCardDateStr("Aug 28, 2003 12:00 AM");
			}
		}
		catch(Exception e)
		{
			System.out.println("Error while converting strings to int"+e);
		}

	}

	public int getEntityIdValue()
	{
		return this.entityIdValue;
	}

	public void setEntityIdValue(int entityIdValue)
	{
		this.entityIdValue = entityIdValue;
	}


	public String getPaymentMethodID()
	{
		return this.paymentMethodID;
	}

	public void setPaymentMethodID(String paymentMethodID)
	{
		this.paymentMethodID = paymentMethodID;
	}


	public int getPaymentMethodIDValue()
	{
		return this.paymentMethodIDValue;
	}

	public void setPaymentMethodIDValue(int paymentMethodIDValue)
	{
		this.paymentMethodIDValue = paymentMethodIDValue;
	}


	public float getPmntAmount()
	{
		return this.pmntAmount;
	}

	public void setPmntAmount(float pmntAmount)
	{
		this.pmntAmount = pmntAmount;
	}


	public int getPmntIDValue()
	{
		return this.pmntIDValue;
	}

	public void setPmntIDValue(int pmntIDValue)
	{
		this.pmntIDValue = pmntIDValue;
	}


	public java.sql.Date getCardExpiry()
	{
		return this.cardExpiry;
	}

	public void setCardExpiry(java.sql.Date cardExpiry)
	{
		this.cardExpiry = cardExpiry;
	}


	public String getCardDateStr()
	{
		return this.cardDateStr;
	}
	public void setCardDateStr(String cardDateStr)
	{
		this.cardDateStr = cardDateStr;
	}


	public int[] getInvoiceID()
	{
		return this.invoiceID;
	}

	public void setInvoiceID(int[] invoiceID)
	{
		this.invoiceID = invoiceID;
	}


	public String[] getInvDateStr()
	{
		return this.invDateStr;
	}

	public void setInvDateStr(String[] invDateStr)
	{
		this.invDateStr = invDateStr;
	}

	public double getTotalPayment()
	{
		return this.totalPayment;
	}

	public void setTotalPayment(double totalPayment)
	{
		this.totalPayment = totalPayment;
	}


	/*
	 *	Validates user input data
	 *	@param mapping ActionMapping
	 *	@param request HttpServletRequest
	 *	@return errors ActionErrors
	 */
	public ActionErrors validate (ActionMapping mapping, HttpServletRequest request)
	{
		// initialize new actionerror object
		ActionErrors errors = new ActionErrors();

		try
		{
			// initialize validation
			Validation validation = new Validation();

			this.convertPaymentLines();

      if (this.getEntityId() == null || this.getEntityId().trim().length() <= 0) {
        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("error.general.requiredField", "Entity"));
      }

			if (errors != null)
			{
				request.setAttribute("body", "ADD");
				request.setAttribute("clearform", "false");

				request.setAttribute("paymentForm", this);
				request.setAttribute(AccountConstantKeys.TYPEOFSUBMODULE, AccountConstantKeys.PAYMENT);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return errors;
	}

	/**
	 * Sets the modifiedBy for this payment.
	 * @param modifiedBy The new modifiedBy for this payment.
	 */
	public void setModifiedBy(int modifiedBy)
	{
		this.modifiedBy = modifiedBy;
	} //end of setModifiedBy method

	/**
	 * @return The modifiedBy for this payment.
	 */
	public int getModifiedBy()
	{
		return this.modifiedBy;
	} //end of getModifiedBy method

	public static PaymentForm clearForm(PaymentForm paymentForm)
	{
		paymentForm.setAmtDue(null);
		paymentForm.setCardNumber("");
		paymentForm.setCardType("");
		paymentForm.setEntityId("");
		paymentForm.setEntityName("");
		paymentForm.setInvDate(null);
		paymentForm.setInvoiceNum(null);
		paymentForm.setPayment(null);
		paymentForm.setPaymentAmount("");
		paymentForm.setPaymentID("");
		paymentForm.setPmntID("");
		paymentForm.setReference("");
		paymentForm.setTotal(null);
		paymentForm.setDescription("");
		paymentForm.setCheckNumber("");
		paymentForm.setEntityIdValue(0);
		paymentForm.setPaymentMethodID("");
		paymentForm.setPaymentMethodIDValue(0);
		paymentForm.setPmntAmount(0.0f);
		paymentForm.setPmntIDValue(0);
		paymentForm.setCardExpiry(null);
		paymentForm.setCardDateStr("");
		paymentForm.setInvoiceID(null);
		paymentForm.setInvDateStr(null);
		paymentForm.setTotalPayment(0.0);

		paymentForm.paymentLines = null;
		return paymentForm;
	}

}
