/*
 * $RCSfile: FaqEJB.java,v $    $Revision: 1.2 $  $Date: 2005/09/01 15:31:05 $ - $Author: mcallist $
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


package com.centraview.support.faq;



import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;

import com.centraview.administration.authorization.AuthorizationLocal;
import com.centraview.administration.authorization.AuthorizationLocalHome;
import com.centraview.administration.authorization.ModuleFieldRightMatrix;
import com.centraview.common.AuthorizationFailedException;
import com.centraview.common.CVDal;
import com.centraview.common.CVUtility;
import com.centraview.note.NoteException;

public class FaqEJB implements SessionBean
{
	protected SessionContext ctx;
	private String dataSource = "MySqlDS";
	public void setSessionContext(SessionContext ctx)
	{
		this.ctx=ctx;
	}

	public void ejbCreate()
	{
	}

	public void ejbRemove()
	{
	}

	public void ejbActivate()
	{
	}

	public void ejbPassivate()
	{
	}




	/**
	* Adds a new faq
	*
	* @param   userId
	* @param   fvo
	* @exception   FaqException
	*/
	public int addFaq(int userId, FaqVO fvo) throws FaqException
	{
		int faqId = -1;
		CVDal cvdl = new CVDal(dataSource);
		try
		{

			if (fvo == null)
				throw new FaqException(FaqException.INVALID_DATA,"Cannot add faq. FaqVo is empty.");

			if( fvo.getTitle() == null || fvo.getTitle().length() == 0)
				throw new FaqException(FaqException.INVALID_DATA,"Title is Empty");

			if(fvo.getCreatedBy() == 0)
				fvo.setCreatedBy(userId);

			if(fvo.getOwner() == 0)
				fvo.setOwner(userId);

			cvdl.setSql("support.faq.insertfaq");
			cvdl.setString(1,fvo.getTitle());
			cvdl.setString(2,fvo.getDetail());
			cvdl.setInt(3,fvo.getCreatedBy());
			cvdl.setString(4,fvo.getStatus());
			cvdl.setInt(5,fvo.getOwner());
			String publishToCustomerView = "NO";
			if(fvo.getPublishToCustomerView() != null && fvo.getPublishToCustomerView().equals("on")){
				publishToCustomerView = "YES";
			}
			cvdl.setString(6,publishToCustomerView);

			cvdl.executeUpdate();
			faqId  = cvdl.getAutoGeneratedKey();

			// Apply default permissions to the record.
			InitialContext ic = CVUtility.getInitialContext();
			AuthorizationLocalHome authorizationHome = (AuthorizationLocalHome)ic.lookup("local/Authorization");
			AuthorizationLocal authorizationLocal = authorizationHome.create();
			authorizationLocal.setDataSource(dataSource);
			authorizationLocal.saveCurrentDefaultPermission("FAQ", faqId, userId);

		} catch(Exception e)
		{
			e.printStackTrace();
			throw new FaqException(NoteException.INSERT_FAILED,"Failed in faq ejb while adding note");
		}
		finally
		{
			cvdl.destroy();
			cvdl = null;
		}
		return faqId;
	}



	/**
	* This method gets the detailsof the  note
	*
	* @param   userId
	* @param   faqId
	* @return
	* @exception   FaqException
	*/
	public FaqVO getFaq(int userId, int faqId) throws NoteException
	{
		FaqVO fvo=null;
		CVDal cvdl = new CVDal(dataSource);
		try
		{
			cvdl.setSql("support.faq.getfaq");

			cvdl.setInt(1,faqId);
			Collection col = cvdl.executeQuery();

			if (col == null)
			throw new FaqException(FaqException.GET_FAILED,"Could not find Note : " + faqId);

			Iterator it = col.iterator();

			if (!it.hasNext())
			throw new FaqException(FaqException.GET_FAILED,"Could not find Note : " + faqId);

			HashMap hm = (HashMap)it.next();


			fvo = new FaqVO();
			fvo.setFaqId(((Long)hm.get("faqid")).intValue());
			fvo.setTitle((String)hm.get("title"));
			fvo.setDetail((String)hm.get("detail"));
			fvo.setStatus((String)hm.get("status"));

			if(hm.get("owner")!= null)
			fvo.setOwner(((Long)hm.get("owner")).intValue());

			if(hm.get("createdby")!= null)
			fvo.setCreatedBy(((Long)hm.get("createdby")).intValue());

			if(hm.get("updatedby")!= null)
			fvo.setModifiedBy(((Long)hm.get("updatedby")).intValue());
			fvo.setCreatedOn((Timestamp)hm.get("created"));

			String publishToCustomerView = (String)hm.get("publishToCustomerView");
			if(publishToCustomerView != null && publishToCustomerView.equals("YES")){
				publishToCustomerView = "on";
			}
			else{
				publishToCustomerView = "off";
			}
			fvo.setPublishToCustomerView(publishToCustomerView);

		} catch(Exception e)
		{
			System.out.println("[Exception][FaqEJB.getFaq] Exception Thrown: "+e);
			e.printStackTrace();
		} finally {
			cvdl.destroy();
			cvdl = null;
		}
		return fvo;
	}

	/**
	* updated a note using FaqVO
	*
	* @param   userId
	* @param   fvo
	* @exception   FaqException
	*/
	public void updateFaq(int userId, FaqVO fvo) throws FaqException, AuthorizationFailedException
	{
		if(!CVUtility.canPerformRecordOperation(userId, "FAQ", fvo.getFaqId(), ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
		{
			throw new AuthorizationFailedException("User cannot access Update the FAQ.");
		}

		CVDal cvdl = new CVDal(dataSource);
		try
		{

			if (fvo == null)
				throw new FaqException(FaqException.INVALID_DATA,"Cannot update folder. FaqVO is empty.");

			if(fvo.getTitle() == null || fvo.getTitle().length() == 0)
				throw new FaqException(FaqException.INVALID_DATA,"Title is Empty");

			if(fvo.getUpdatedBy() == 0)
				fvo.setUpdatedBy(userId);

			if(fvo.getOwner() == 0)
				fvo.setOwner(userId);

			cvdl.setSql("support.faq.updatefaq");

			cvdl.setString(1,fvo.getTitle());
			cvdl.setString(2,fvo.getDetail());
			cvdl.setInt(3,fvo.getUpdatedBy());
			cvdl.setString(4,fvo.getStatus());
			String publishToCustomerView = "NO";
			if(fvo.getPublishToCustomerView() != null && fvo.getPublishToCustomerView().equals("on")){
				publishToCustomerView = "YES";
			}
			cvdl.setString(5,publishToCustomerView);
			cvdl.setInt(6,fvo.getFaqId());

			cvdl.executeUpdate();

		} catch(Exception e)
		{
			e.printStackTrace();
			throw new FaqException(FaqException.INSERT_FAILED,"Failed in note ejb while updating faq");
		} finally {
			cvdl.destroy();
			cvdl = null;
		}

	}


	/**
	* This method deletes the faq
	*
	* @param   userId
	* @param   faqId
	*/
	public void deleteFaq(int userId, int faqId)throws AuthorizationFailedException
	{
		if(!CVUtility.canPerformRecordOperation(userId, "FAQ", faqId, ModuleFieldRightMatrix.UPDATE_RIGHT, this.dataSource))
		{
			throw new AuthorizationFailedException("User cannot access DELETE the FAQ.");
		}

		CVDal cvdl = new CVDal(dataSource);

		HashMap hmFaq=new HashMap();
		hmFaq.put("faqid",(new Integer(faqId)).toString());
		//sendToAttic(userId,"faq",hmFaq,Constants.CV_ATTIC);

		cvdl.setSql("support.faq.deletefaq");
		cvdl.setInt(userId,faqId);
		cvdl.executeUpdate();

		cvdl.destroy();
	}

	public int duplicateFaq(int userId, FaqVO fvo) throws FaqException
	{

		CVDal dl = null;
		int fId = -1;
		try
		{

			if (fvo == null)
				throw new FaqException(FaqException.INVALID_DATA,"Cannot update folder. FaqVO is empty.");

			if(fvo.getTitle() == null || fvo.getTitle().length() == 0)
				throw new FaqException(FaqException.INVALID_DATA,"Title is Empty");


			if(fvo.getUpdatedBy() == 0)
				fvo.setUpdatedBy(userId);

			if(fvo.getOwner() == 0)
				fvo.setOwner(userId);


			ctx.getUserTransaction().begin();
			int faqId = addFaq(userId,fvo);
			dl = new CVDal(dataSource);

			dl.setSql("support.faq.getquestionforfaq");
			dl.setInt(1,fvo.getFaqId());
			Collection col = dl.executeQuery();
			dl.destroy();
			dl = null;
			Iterator it = col.iterator();

			HashMap hm = null;
			QuestionVO qvo = null;
			while (it.hasNext())
			{
				hm = (HashMap)it.next();
				qvo = new QuestionVO();
				qvo.setFaqId(faqId);
				qvo.setQuestion((String)hm.get("question"));
				qvo.setAnswer((String)hm.get("answer"));
				addQuestion(userId,qvo);
			}
			ctx.getUserTransaction().commit();
			fId = faqId;

		} catch(Exception e)
		{
			System.out.println("Failed in SupportFacade duplicating faq");
			e.printStackTrace();
		}
		finally
		{
			if (dl != null)
			{
				dl.destroy();
				dl = null;
			}
		}
		return fId;
	}




	public int addQuestion(int userId, QuestionVO qvo) throws  FaqException
	{
		CVDal dl = null;
		int qid = -1;
		try
		{

			dl = new CVDal(dataSource);

			dl.setSql("support.faq.insertquestion");
			dl.setInt(1,qvo.getFaqId());
			dl.setString(2,qvo.getQuestion());
			dl.setString(3,qvo.getAnswer());

			dl.executeUpdate();
			qid = dl.getAutoGeneratedKey();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dl != null)
			{
				dl.destroy();
				dl = null;
			}
		}
		return qid;
	}
	public void updateQuestion(int userId, QuestionVO qvo) throws  FaqException
	{
		CVDal dl = null;
		try
		{
			dl = new CVDal(dataSource);

			dl.setSql("support.faq.updatequestion");

			dl.setString(1,qvo.getQuestion());
			dl.setString(2,qvo.getAnswer());
			dl.setInt(3,qvo.getQuestionId());

			dl.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("[Exception][FaqEJB.updateQuestion] Exception Thrown: "+e);
			e.printStackTrace();
		}
		finally
		{
			if (dl != null)
			{
				dl.destroy();
				dl = null;
			}
		}
	}

	public QuestionVO getQuestion(int userId, int questionId) throws FaqException
	{
		CVDal dl = null;
		QuestionVO Qvo = null;
		try
		{
			dl = new CVDal(dataSource);

			dl.setSql("support.faq.getquestion");

			dl.setInt(1,questionId);

			Collection col = dl.executeQuery();
			dl.clearParameters();

			if (col != null)
			{
				HashMap hm = (HashMap)col.iterator().next();

				Qvo = new QuestionVO();
				Qvo.setQuestionId(((Long)hm.get("questionid")).intValue());
				Qvo.setFaqId(((Long)hm.get("faqid")).intValue());
				Qvo.setQuestion((String)hm.get("question"));
				Qvo.setAnswer((String)hm.get("answer"));
			}
		}
		catch(Exception e)
		{
			System.out.println("[Exception][FaqEJB.getQuestion] Exception Thrown: "+e);
			e.printStackTrace();
		}
		finally
		{
			if (dl != null)
			{
				dl.destroy();
				dl = null;
			}
		}
		return Qvo;
	}
	public void deleteQuestion(int userId, int questionId) throws  FaqException
	{
		CVDal dl = null;
		try
		{
			dl = new CVDal(dataSource);

			dl.setSql("support.faq.deletequestion");

			dl.setInt(1,questionId);

			dl.executeUpdate();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dl != null)
			{
				dl.destroy();
				dl = null;
			}
		}
	}

	/**
	* @author Kevin McAllister <kevin@centraview.com>
	* This simply sets the target datasource to be used for DB interaction
	* @param ds A string that contains the cannonical JNDI name of the datasource
	*/
	public void setDataSource(String ds) {
		this.dataSource = ds;
	}

}