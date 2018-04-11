package com.mip.application.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.mip.application.dal.managers.ClaimsManagementManager;
import com.mip.application.dal.managers.CustomerManager;
import com.mip.application.dal.managers.CustomerSubscriptionManager;
import com.mip.application.model.ClaimDetails;
import com.mip.application.model.CustomerDetails;
import com.mip.application.model.CustomerSubscription;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.model.mappings.ClaimMappings;
import com.mip.application.services.transaction.ClaimTXService;
import com.mip.application.view.ClaimVO;
import com.mip.application.view.CustomerVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>LeaveManagementService.java</code> contains all the service layer methods 
 * pertaining to Leave Management use case model.
 * </p>
 * 
 * @author T H B S
 */
public class ClaimsManagementService {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ClaimsManagementService.class);
	
	/**
	 * Set inversion of control for <code>claimsManagementManager</code>
	 */
	private CustomerManager customerManager;
	
	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}
	
	private CustomerSubscriptionManager customerSubsManager;
	
	public void setCustomerSubsManager(
			CustomerSubscriptionManager customerSubsManager) {
		this.customerSubsManager = customerSubsManager;
	}
	
	private ClaimTXService claimTXService;

	public void setClaimTXService(ClaimTXService claimTXService) {
		this.claimTXService = claimTXService;
	}
	
	private ClaimsManagementManager claimsManagementManager;
	
	public void setClaimsManagementManager(
			ClaimsManagementManager claimsManagementManager) {
		this.claimsManagementManager = claimsManagementManager;
	}
	
	/**
	 * This method returns the customer details based on the MSISDN
	 * 
	 * @param msisdn
	 * 		Customer's MSISDN
	 * 
	 * @return
	 * 		instance of CustomerDetails
	 * @throws MISPException
	 */
	public CustomerVO findCustomer(String msisdn) throws MISPException {
		Object[] params = {msisdn};
		logger.entering("findCustomer", params);
		
//		CustomerDetails custDetails = null;
		CustomerVO custDetails = null;
		
		try {
			custDetails = customerManager.getCustomerFromMSISDN(msisdn);
		}
		catch(DBException dbException) {
			logger.error("Exception occured while getting customer details.");
			throw new MISPException(dbException);
		}
		logger.exiting("findCustomer", custDetails);
		return custDetails;
	}
	
	/**
	 * This method returns the customer details based on the MSISDN
	 * 
	 * @param msisdn
	 * 		Customer's MSISDN
	 * 
	 * @return
	 * 		instance of CustomerDetails
	 * @throws MISPException
	 */
	public List<CustomerVO> findCustomerForClaims(String msisdn) throws MISPException {
		Object[] params = {msisdn};
		logger.entering("findCustomerForClaims", params);
		
//		CustomerDetails custDetails = null;
		List<CustomerVO> custDetails = null;
		
		try {
			custDetails = customerManager.getCustomerFromMSISDNForClaims(msisdn);
		}
		catch(DBException dbException) {
			logger.error("Exception occured while getting customer details.");
			throw new MISPException(dbException);
		}
		logger.exiting("findCustomerForClaims", custDetails);
		return custDetails;
	}
	
	
	/**
	 * This method save the claim details in the DB
	 * 
	 * @param claimVO
	 * 		- instance of ClaimVO
	 * @param userDetails
	 * 		- instance of UserDetails
	 * @return
	 * 		True is successful, false otherwise
	 * @throws MISPException
	 */
	public boolean claimAndModifyInsurance(ClaimVO claimVO, UserDetails userDetails)
			throws MISPException {
		
		Object[] params = { claimVO, userDetails };
		logger.entering("claimAndModifyInsurance", params);
		
		boolean isClaimSuccessful = false;
		ClaimDetails claimModel = null;
		CustomerSubscription customerSubscription = null;
		CustomerDetails customerDetails = null;
		InsuredRelativeDetails insuredRelativeModel = null;
		InsuredRelativeDetails nomineeModel = null;
		List<InsuredRelativeDetails> insRelativeList=new ArrayList<InsuredRelativeDetails>();
		
		
		try {
			
			customerSubscription = customerSubsManager
					.getCustomerSubscriptionDetails(claimVO.getCustId());
			
			if(null != customerSubscription) {
				customerDetails = customerSubscription.getCustomerDetails();
			}
			
			Set<InsuredRelativeDetails> insuredRelativesSet = customerDetails
					.getInsuredRelatives();
			if (null != insuredRelativesSet) {
				for (InsuredRelativeDetails insDetails : insuredRelativesSet) {
						if(insDetails.getOfferId()== Integer.parseInt(PlatformConstants.PRODUCT_XL))
							insuredRelativeModel = insDetails;
						else if(insDetails.getOfferId()==Integer.parseInt(PlatformConstants.PRODUCT_IP))
							nomineeModel= insDetails;
						
					}
				}
			
			/**
			 * Gets the current claims details for insert
			 */
			if(null != insuredRelativeModel)
				insRelativeList.add(insuredRelativeModel);
			
			if(null != nomineeModel)
				insRelativeList.add(nomineeModel);
			if(null!= insuredRelativeModel)
			{
			for(InsuredRelativeDetails irdDetail:insRelativeList){
					claimModel = ClaimMappings.getClaimDetailsModelForClaim(
					customerDetails, irdDetail, userDetails);
					claimModel.setClaimedPerson(claimVO.getClaimedPerson());
					customerDetails = ClaimMappings.getCustomerModelForClaim(claimVO, customerDetails, userDetails);

					if (null != insuredRelativeModel) {
						insuredRelativeModel = ClaimMappings.getInsModelToUpdate(insuredRelativeModel,claimVO,userDetails);
									}
					 if (null != nomineeModel) {
						insuredRelativeModel = ClaimMappings.getNomineeModelToUpdate(nomineeModel,claimVO,userDetails);
						}
			
			isClaimSuccessful = claimTXService.modifyClaimDetails(customerDetails, insuredRelativeModel);
			
			}
			}
			else
			{
				claimModel = ClaimMappings.getClaimDetailsModelForClaim(
						customerDetails, insuredRelativeModel, userDetails);
						claimModel.setClaimedPerson(claimVO.getClaimedPerson());
						customerDetails = ClaimMappings.getCustomerModelForClaim(claimVO, customerDetails, userDetails);

						
				isClaimSuccessful = claimTXService.modifyClaimDetails(customerDetails, insuredRelativeModel);
			}
		}
		catch (DBException dbException) {
			logger.error("Exception occured while claiming insurance.");
			throw new MISPException(dbException);
		}
		catch (MISPException mispException) {
			logger.error("Exception occured while claiming insurance.");
			throw new MISPException(mispException);
		}
		
		logger.exiting("claimAndModifyInsurance");
		return isClaimSuccessful;
	}

	/**
	 * This method search and modifies the customer details during claim process.
	 * 
	 * @param claimVO
	 * 		- instance of ClaimVO
	 * @param userDetails
	 * 		- instance of UserDetails
	 * 
	 * @return
	 * 		True if successful, false otherwise
	 * @throws MISPException
	 */
	public boolean searchAndModifyInsurance(ClaimVO claimVO,
			UserDetails userDetails) throws MISPException {
		
		Object[] params = { claimVO, userDetails };
		logger.entering("searchAndModifyInsurance", params);
		
		boolean isClaimSuccessful = false;
		
		ClaimDetails claimModel = null;
		CustomerSubscription customerSubscription = null;
		CustomerDetails customerDetails = null;
		InsuredRelativeDetails insuredRelativeModel = null;
		InsuredRelativeDetails nomineeModel = null;
		List<InsuredRelativeDetails> insRelativeList=new ArrayList<InsuredRelativeDetails>();
		
		boolean isInsRelExist=false;
		boolean isIPExist=false;
		
		try {
			
			customerSubscription = customerSubsManager
					.getCustomerSubscriptionDetails(claimVO.getCustId());
			
			CustomerVO custVO=new CustomerVO();
		    custVO.setMsisdn(claimVO.getMsisdn());
			
			if(null != customerSubscription) {
				customerDetails = customerSubscription.getCustomerDetails();
			}
			
			/**
			 * Gets the insured relative details
			 */
			Set<InsuredRelativeDetails> insuredRelativesSet = customerDetails
					.getInsuredRelatives();
			if (null != insuredRelativesSet) {
				for (InsuredRelativeDetails insDetails : insuredRelativesSet) {
						if(insDetails.getOfferId()== Integer.parseInt(PlatformConstants.PRODUCT_XL))
							insuredRelativeModel = insDetails;
						else if(insDetails.getOfferId()==Integer.parseInt(PlatformConstants.PRODUCT_IP))
							nomineeModel= insDetails;
						
					}
				}
			
			/**
			 * Gets the current claims details for insert
			 */
			if(null != insuredRelativeModel)
				insRelativeList.add(insuredRelativeModel);
			
			if(null != nomineeModel)
				insRelativeList.add(nomineeModel);
			if(null!= insuredRelativeModel || null != nomineeModel)
			{
			for(InsuredRelativeDetails irdDetail:insRelativeList){
					claimModel = ClaimMappings.getClaimDetailsModelForClaim(
					customerDetails, irdDetail, userDetails);
					claimModel.setClaimedPerson(claimVO.getClaimedPerson());
					customerDetails = ClaimMappings.getCustomerModelForClaim(claimVO, customerDetails, userDetails);
					if (null != insuredRelativeModel) {
						insuredRelativeModel = ClaimMappings.getInsModelToUpdate(insuredRelativeModel,claimVO,userDetails);
					    
					     if(null!=insuredRelativeModel && insuredRelativeModel.getInsMsisdn() != null && !insuredRelativeModel.getInsMsisdn().isEmpty()){
					        isInsRelExist=true;
					    	 
					      }					
														
					}
					if (null != nomineeModel) {
						insuredRelativeModel = ClaimMappings.getNomineeModelToUpdate(nomineeModel,claimVO,userDetails);
						if(null!=insuredRelativeModel && insuredRelativeModel.getInsMsisdn() != null && !insuredRelativeModel.getInsMsisdn().isEmpty()){
					        isIPExist=true;
					    	 
					      } 
					}
			isClaimSuccessful = claimTXService.modifyClaimDetails(customerDetails, insuredRelativeModel);
			
			}
			}
			else
			{
				claimModel = ClaimMappings.getClaimDetailsModelForClaim(
						customerDetails, insuredRelativeModel, userDetails);
						claimModel.setClaimedPerson(claimVO.getClaimedPerson());
						customerDetails = ClaimMappings.getCustomerModelForClaim(claimVO, customerDetails, userDetails);

						
				isClaimSuccessful = claimTXService.modifyClaimDetails(customerDetails, insuredRelativeModel);
			}
			
			if(isInsRelExist)
				 customerManager.sendSMStoNominee(custVO,"1,2");
			
			if(isIPExist)
				 customerManager.sendSMStoNominee(custVO,"4");
						
		}
		catch (DBException dbException) {
			logger.error("Exception occured while claiming insurance.");
			throw new MISPException(dbException);
		}
		catch (MISPException mispException) {
			logger.error("Exception occured while claiming insurance.");
			throw new MISPException(mispException);
		}
		catch (Exception e) {
			logger.error("Exception occured while claiming insurance.");
			throw new MISPException(e);
		}
		
		logger.exiting("claimAndModifyInsurance");
		return isClaimSuccessful;
	}

	/**
	 * This method returns the list of all claim details.
	 * 
	 * @return
	 * 		<code>List</code> of ClaimDetails
	 * @throws MISPException
	 */
	public List<ClaimDetails> getAllClaimDetails() throws MISPException {
		logger.entering("getAllClaimDetails");
		
		List<ClaimDetails> claimList = null;
		
		try {
			claimList = claimsManagementManager.getAllClaimDetails();
		} 
		catch (DBException dbException) {
			logger.error("Exception occured while claiming insurance.");
			throw new MISPException(dbException);
		}
		logger.exiting("getAllClaimDetails");
		return claimList;
	}
	
}
