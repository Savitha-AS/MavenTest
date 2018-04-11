package com.mip.application.services.transaction;

import com.mip.application.dal.managers.ClaimsManagementManager;
import com.mip.application.dal.managers.CustomerManager;
import com.mip.application.model.ClaimDetails;
import com.mip.application.model.CustomerDetails;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * This class injects Transaction into the Service layer pertaining to 
 * Claims use-case model. 
 * 
 * @author T H B S
 */
public class ClaimTXService {

	/**
	 * An instance of logger.
	 */	
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ClaimTXService.class);
	
	private ClaimsManagementManager claimsManagementManager;
	
	public void setClaimsManagementManager(
			ClaimsManagementManager claimsManagementManager) {
		this.claimsManagementManager = claimsManagementManager;
	}
	
	private CustomerManager customerManager;
	
	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}
	
	/**
	 * This method saves the claims details.
	 *  
	 * @param claimDetails
	 * 			- instance of ClaimDetails
	 * @param custDetails
	 * 			- instance of CustomerDetails
	 * @param insDetails
	 * 			- instance of InsuredRelativeDetails
	 * @return
	 * 			True if successful, false otherwise
	 * @throws MISPException
	 */
	public boolean claimAndModifyInsurance(ClaimDetails claimDetails,
			CustomerDetails custDetails, InsuredRelativeDetails insDetails)
			throws MISPException {
		Object[] params = {claimDetails, custDetails, insDetails};
		logger.entering("claimAndModifyInsurance", params);
		
		boolean isClaimSuccessful = false;
		
		try {
			
			isClaimSuccessful = claimsManagementManager
					.claimInsurance(claimDetails);
			
			if(isClaimSuccessful) {
				isClaimSuccessful = modifyClaimDetails(custDetails, insDetails);
			}
			
		} catch (DBException dbException) {
			logger.error("Exception occured while claiming insurance.");
			throw new MISPException(dbException);
		}
		
		logger.exiting("claimAndModifyInsurance");
		return isClaimSuccessful;
	}
	
	/**
	 * This method modifies the claims details.
	 * 
	 * @param custDetails
	 * 			- instance of CustomerDetails
	 * @param insDetails
	 * 			- instance of InsuredRelativeDetails
	 * @return
	 * 			True if successful, false otherwise
	 * @throws MISPException
	 */
	public boolean modifyClaimDetails(CustomerDetails custDetails, InsuredRelativeDetails insDetails) throws MISPException {
		Object[] params = {custDetails, insDetails};
		logger.entering("modifyClaimDetails", params);
		
		boolean isModified = false;
		
		try {
			isModified = customerManager.updateCustomerDetails(custDetails,
					insDetails);
		} catch (DBException dbException) {
			logger.error("Exception occured while modify claim details.");
			throw new MISPException(dbException);
		}
		logger.exiting("modifyClaimDetails");
		return isModified;
	}
}
