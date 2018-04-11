package com.mip.application.services.transaction;

import com.mip.application.constants.FaultMessages;
import com.mip.application.dal.managers.CustomerManager;
import com.mip.application.model.CustomerDetails;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * This class injects Transaction into the Service layer pertaining to 
 * Customer use-case model. 
 * 
 * @author T H B S
 */
public class CustomerTXService {

	/**
	 * An instance of logger.
	 */	
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomerTXService.class);
	
	/**
	 * Set inversion of control for <code>CustomerManager</code>
	 */
	private CustomerManager customerManager;		

	/**
	 * Set inversion of control for <code>OfferDetailsManager</code>
	 */
	/*private OfferDetailsManager offerDetailsMgr;*/
	
	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}
	
	/*public void setOfferDetailsMgr(OfferDetailsManager offerDetailsMgr) {
		this.offerDetailsMgr = offerDetailsMgr;
	}*/
	
	/**
	 * This method modify the customer registration details.
	 * 
	 * @param custModel
	 * 		instance of CustomerDetails, containing new customer details.
	 * @param insModel
	 * 		instance of InsuredRelativeDetails, containing new insured relative details.
	 * @param addIrd 
	 * @return
	 * 		isModified true upon success, false otherwise.
	 * @throws MISPException
	 */
	public boolean modifyCustomerDetails(CustomerDetails custModel,
			InsuredRelativeDetails insModel)
			throws MISPException {
	
		Object[] params = {custModel,insModel};
		logger.entering("modifyCustomerDetails", params);

		boolean isModified = false;
		try{			
			isModified = this.customerManager.
					updateCustomerDetails(custModel,insModel);
			
		}catch (DBException dbException) {
			
			logger.error(FaultMessages.CUSTOMER_UPDATE_DB_EXCEPTION,
					dbException);
			throw new MISPException(dbException);
		}
		logger.exiting("modifyCustomerDetails",isModified);
		return isModified;
	}
	
	/**
	 * This method modify the details of unconfirmed customers
	 *  
	 * @param custModel
	 * 		instance of CustomerDetails, containing new customer details.
	 * @param insModel
	 * 		instance of InsuredRelativeDetails, containing new insured relative details.
	 * @param isOfferChanged 
	 * @return
	 * 		isModified true upon success, false otherwise.
	 * @throws MISPException
	 */
	public boolean modifyUnconfirmedCustomerDetails(CustomerDetails custModel,
			InsuredRelativeDetails insModel, boolean isOfferChanged)
			throws MISPException {
		
		Object[] params = {custModel,insModel};
		logger.entering("modifyUnconfirmedCustomerDetails", params);
		
		boolean isModified = false;
		
		isModified = modifyCustomerDetails(custModel, insModel);
		
		if(isModified == true && isOfferChanged){										
				isModified = customerManager.frameSMS(custModel);
			} 

		logger.exiting("modifyUnconfirmedCustomerDetails",isModified);
		
		return isModified;
	}
	/**
	 * This method modify the customer registration and  
	 * offer details. 
	 * 
	 * @param custObj 
	 * 			instance of CustomerVO, containing previous customer details.
	 * @param custModel 
	 * 			instance of CustomerDetails, containing new customer details.
	 * @param insModel 
	 * 			instance of InsuredRelativeDetails, containing new insured relative details.
	 * @param isOfferSubscribed
	 * 			Contains subscription value for the current offer
	 * @return
	 * 			isSuccess true upon success, false otherwise.
	 * 
	 * @throws MISPException
	 */
/*	public boolean modifyCustomerAndOfferDetails(CustomerVO custObj, CustomerDetails custModel,
			InsuredRelativeDetails insModel,
			byte isOfferSubscribed) throws MISPException {
		
		Object[] params = {custModel, insModel, isOfferSubscribed};
		logger.entering("modifyCustomerAndOfferDetails", params);
		
		boolean isSuccess = false;		
		
		try {
			if((byte)0 == isOfferSubscribed) {
				// Customer has not confirmed his offer subscription.
				
				*//**
				 * As spring-aop implements the execution in the reverse order,
				 * the execution of below calls are in the reverse order.
				 *//*
								
				isSuccess = this.offerDetailsMgr.modifyOfferDetails(custModel, 
						custObj.getOfferId());
				
				if(isSuccess) {
					custModel.setOfferId(null);
					isSuccess = modifyCustomerDetails(custModel, insModel);
				}
			}
			
		}catch (DBException dbException) {

			logger.error(FaultMessages.CUSTOMER_UPDATE_DB_EXCEPTION,
					dbException);
			throw new MISPException(dbException);
		}
		logger.exiting("modifyCustomerAndOfferDetails", isSuccess);
		return isSuccess;
	}*/

}
