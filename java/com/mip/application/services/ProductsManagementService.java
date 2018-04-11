package com.mip.application.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mip.application.dal.managers.BusinessRuleMasterManager;
import com.mip.application.dal.managers.CustomerManager;
import com.mip.application.dal.managers.CustomerSubscriptionManager;
import com.mip.application.dal.managers.OfferCoverDetailsManager;
import com.mip.application.dal.managers.OfferDetailsManager;
import com.mip.application.model.BusinessRuleMaster;
import com.mip.application.model.ProductCoverDetails;
import com.mip.application.model.ProductDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.model.mappings.OfferMappings;
import com.mip.application.view.OfferCreateVO;
import com.mip.application.view.OfferMgmtVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;


/**
 * <p>
 * <code>OffersManagementService.java</code> contains all the service layer
 * methods pertaining to offers management module.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class ProductsManagementService {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ProductsManagementService.class);

	/**
	 * An instance of <code>OfferDetailsManager</code>.
	 */
	private OfferDetailsManager offerDetailsMgr;

	/**
	 * An instance of <code>CustomerSubscriptionManager</code>.
	 */
	private CustomerSubscriptionManager customerSubsManager;

	/**
	 * An instance of <code>CustomerManager</code>.
	 */
	private CustomerManager customerdetailsManager;

	/**
	 * An instance of <code>OfferTypeManager</code>.
	 */
//	private OfferTypeManager offerTypeManager;

	/**
	 * An instance of <code>BusinessRuleMasterManager</code>.
	 */
	private BusinessRuleMasterManager busRuleMasterManager;

	/**
	 * An instance of <code>OfferCoverDetailsManager</code>.
	 */
	private OfferCoverDetailsManager offCoverDetailsManager;

	/**
	 * @param offCoverDetailsManager
	 *            setter method for offCoverDetailsManager
	 */
	public void setOffCoverDetailsManager(
			OfferCoverDetailsManager offCoverDetailsManager) {
		this.offCoverDetailsManager = offCoverDetailsManager;
	}

	/**
	 * @param busRuleMasterManager
	 *            setter method for busRuleMasterManager
	 */
	public void setBusRuleMasterManager(
			BusinessRuleMasterManager busRuleMasterManager) {
		this.busRuleMasterManager = busRuleMasterManager;
	}

	/**
	 * @param offerTypeManager
	 *            setter method for offerTypeManager
	 */
//	public void setOfferTypeManager(OfferTypeManager offerTypeManager) {
//		this.offerTypeManager = offerTypeManager;
//	}

	/**
	 * @param offerDetailsMgr
	 *            setter method for offerDetailsMgr
	 */
	public void setOfferDetailsMgr(OfferDetailsManager offerDetailsMgr) {
		this.offerDetailsMgr = offerDetailsMgr;
	}

	/**
	 * 
	 * @param customerSubsManager
	 *            setter method for customerSubsManager.
	 */
	public void setCustomerSubsManager(
			CustomerSubscriptionManager customerSubsManager) {
		this.customerSubsManager = customerSubsManager;
	}

	/**
	 * 
	 * @param customerdetailsManager
	 *            setter method for customerdetailsManager.
	 */
	public void setCustomerdetailsManager(CustomerManager customerdetailsManager) {
		this.customerdetailsManager = customerdetailsManager;
	}

	/**
	 * This method returns all the offers in the MISP system.
	 * 
	 * @return <code>List<OfferDetails></code> of offer details
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public List<ProductDetails> retrieveOffers() throws MISPException {

		logger.entering("retrieveOffers");

		List<ProductDetails> productsList = null;

		try {

			productsList = this.offerDetailsMgr.retrieveOffers();

		} catch (DBException exception) {

			logger.error(
					"An exception occured while retrieving offer details.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("retrieveOffers", productsList);

		return productsList;

	}
	
	/**
	 * This method retrieves the available/active offers - Offer Name 
	 * and Offer ID only.
	 * 
	 * @return
     * 	<code>List&ltOfferDetails&gt</code>
     * 
     * @throws <code>MISPException</code>
	 */
	public List<ProductDetails> retrieveOfferNamesAndIds() throws MISPException
	{
		logger.entering("retrieveOfferNamesAndIds");
		
		List<ProductDetails> products = null;
		
		try{		
			products = offerDetailsMgr.retrieveOfferNamesAndIds();
		}
		catch (DBException exception){			
			logger.error
					("An exception occured while retrieving offers.",exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("retrieveOfferNamesAndIds");
		return products;		
	}

	/**
	 * This method assigns the selected offer to the customers.
	 * 
	 * @param offerMgmtVO
	 *            ,<code>OfferMgmtVO</code> holds the offer details
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public void assignOffer(OfferMgmtVO offerMgmtVO) throws MISPException {

		logger.entering("assignOffer", offerMgmtVO);

		try {

			// Assign offer to all the customers
			if (PlatformConstants.ALL_CUSTOMERS.equalsIgnoreCase(offerMgmtVO
					.getCustomers())) {

				this.customerSubsManager.assignOfferToAllCustomers(offerMgmtVO);

			} else if (PlatformConstants.SELECTED_CUSTOMERS
					.equalsIgnoreCase(offerMgmtVO.getCustomers())) {

				// Assign offer to selected customers
				Date fromDate = DateUtil.toDate(offerMgmtVO.getFromDate());
				Date toDate = DateUtil.toDate(offerMgmtVO.getToDate());

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(toDate);

				calendar.set(Calendar.HOUR_OF_DAY, 23);
				calendar.set(Calendar.MINUTE, 59);
				calendar.set(Calendar.SECOND, 59);

				toDate = calendar.getTime();

				this.customerSubsManager.assignOfferToSelectedCustomers(
						fromDate, toDate, offerMgmtVO.getProducts());
			}

		} catch (DBException exception) {
			logger.error("An exception occured while assigning Offer.",
					exception);
			throw new MISPException(exception);

		}

		logger.exiting("assignOffer");

	}

	/**
	 * This method retrives the customers registered between the given dates.
	 * 
	 * @param offerMgmtVO
	 *            ,<code>OfferMgmtVO</code> holds the search criteria.
	 * @return customerCount, customer count registered between the given dates.
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public long fetchRegCustomersBwnDates(OfferMgmtVO offerMgmtVO)
			throws MISPException {

		logger.entering("fetchRegCustomersBwnDates", offerMgmtVO);

		long customerCount = 0;

		try {

			Date fromDate = DateUtil.toDate(offerMgmtVO.getFromDate());
			Date toDate = DateUtil.toDate(offerMgmtVO.getToDate());

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(toDate);

			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);

			toDate = calendar.getTime();

			customerCount = this.customerdetailsManager
					.fetchRegCustomersBwnDates(fromDate, toDate);

		} catch (DBException exception) {
			logger.error(
					"An exception occured while retrieving customer details.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("fetchRegCustomersBwnDates", customerCount);

		return customerCount;

	}

	/**
	 * This method retrieves the offer types in the MISP system.
	 * 
	 * @return <code>List<OfferType></code> of offer type details
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	/*public List<OfferType> retrieveOfferTypes() throws MISPException {

		logger.entering("retrieveOfferTypes");

		List<OfferType> offerTypeList = null;

		try {
			offerTypeList = this.offerTypeManager.retrieveOfferTypes();

		} catch (DBException exception) {

			logger.error("An exception occured while retrieving offer types.",
					exception);
			throw new MISPException(exception);

		}

		logger.exiting("retrieveOfferTypes", offerTypeList);

		return offerTypeList;

	}*/

	/**
	 * This method retrives the active business rule in the MISP system.
	 * 
	 * @return <code>BusinessRuleMaster</code>, the active business rule
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public BusinessRuleMaster retrieveActiveBusinessRule() throws MISPException {

		logger.entering("retrieveActiveBusinessRule");

		BusinessRuleMaster businessRule = null;

		try {

			businessRule = this.busRuleMasterManager
					.retrieveActiveBusinessRule();

		} catch (DBException exception) {

			logger.error(
					"An exception occured while retrieving active BusinessRule.",
					exception);
			throw new MISPException(exception);

		}

		logger.exiting("retrieveActiveBusinessRule", businessRule);

		return businessRule;

	}

	/**
	 * This method is to save the new offer details in the MISP system.
	 * 
	 * @param offerCreateVO
	 *            , An instance of <code>OfferCreateVO</code>.
	 * @param user
	 *            , holds the user details.
	 * @return true if successfully saved, else returns false.
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public boolean saveOfferDetails(OfferCreateVO offerCreateVO,
			UserDetails user) throws MISPException {

		logger.entering("saveOfferDetails", offerCreateVO);

		boolean savedSuccesfully = false;

		try {

			// Map VO to Model
			ProductDetails productDetails = OfferMappings
					.mapOfferCreateVOToOfferDetailsModel(offerCreateVO);

			if (new Integer(offerCreateVO.getOfferType()).intValue() == PlatformConstants.ADDITIONAL_COVER) {
				/*productDetails.setCoverMultiplier(null);*/
				productDetails
						.setProductDescription(PlatformConstants.OFFER_TYPE_ADDITIONAL_COVER);
			}

			if (new Integer(offerCreateVO.getOfferType()).intValue() == PlatformConstants.MULTIPLE_COVER) {
				productDetails
						.setProductDescription(PlatformConstants.OFFER_TYPE_MULTIPLE_COVER);
			}

			productDetails.setPerDayDeduction(Float.valueOf(offerCreateVO
					.getPerDayDeduction()));
			productDetails.setProductCreatedBy(user);
			productDetails.setProductCreatedDate(new Date());

			// Check if duplicate Offer Name
			if (!this.offerDetailsMgr.checkForDuplicateOffer(offerCreateVO)) {
				this.offerDetailsMgr.saveOfferDetails(productDetails);
				savedSuccesfully = true;
			}

		} catch (DBException exception) {

			logger.error("An exception occured while creating new offer.",
					exception);
			throw new MISPException(exception);

		}

		logger.exiting("saveOfferDetails", savedSuccesfully);

		return savedSuccesfully;

	}

	/**
	 * This method retrieves the customers assigned to an offer.
	 * 
	 * @param offerMgmtVO
	 *            ,<code>OfferMgmtVO</code> holds the search criteria.
	 * @return count, the number of customers assigned to the given offer.
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public long fetchCustomersBasedOnOffer(OfferMgmtVO offerMgmtVO)
			throws MISPException {

		logger.entering("fetchCustomersBasedOnOffer", offerMgmtVO);

		long customerCount = 0;

		try {

			customerCount = this.customerSubsManager
					.fetchCustomersBasedOnOffer(offerMgmtVO);

		} catch (DBException exception) {

			logger.error(
					"An exception occured while retrieving the customers for an offer",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("fetchCustomersBasedOnOffer", customerCount);

		return customerCount;

	}

	/**
	 * This method revokes the selected Offer from the customers
	 * 
	 * @param offerMgmtVO
	 *            , holding the offer details
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public void revokeOffer(OfferMgmtVO offerMgmtVO) throws MISPException {

		logger.entering("revokeOffer", offerMgmtVO);

		try {

			this.customerSubsManager.revokeOffer(offerMgmtVO);

		} catch (DBException exception) {
			logger.error("An exception occured while revoking Offer.",
					exception);
			throw new MISPException(exception);

		}

		logger.exiting("revokeOffer");

	}

	/**
	 * This method returns all the offers and offer types in the MISP system
	 * 
	 * @return <code>List<OfferDetails></code> of offer details
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public List<ProductDetails> retrieveOffersWithOfferType()
			throws MISPException {

		logger.entering("retrieveOffersWithOfferType");

		List<ProductDetails> productsList = null;

		try {

			productsList = this.offerDetailsMgr.retrieveOffersWithOfferType();

		} catch (DBException exception) {

			logger.error(
					"An exception occured while retrieving offer Details.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("retrieveOffersWithOfferType", productsList);

		return productsList;

	}

	/**
	 * This method returns offercover details for the given offerid.
	 * 
	 * @return <code>OfferCoverDetails</code> of offercover details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public List<ProductCoverDetails> retrieveOfferCoverBasedOnOfferId(int offerId)
			throws MISPException {

		logger.entering("retrieveOfferCoverBasedOnOfferId");

		List<ProductCoverDetails> productCoverDetails = null;

		try {

			productCoverDetails = this.offCoverDetailsManager
					.retrieveOfferCoverBasedOnOfferId(offerId);

		} catch (DBException e) {

			logger.error(
					"Exception occured while retrieving OfferCover Details.", e);
			throw new MISPException(e);
		}

		logger.exiting("retrieveOfferCoverBasedOnOfferId", productCoverDetails);

		return productCoverDetails;

	}
	
	/**
	 * This method retrieves the customers assigned to an offer based on MSISDN.
	 * 
	 * @param offerMgmtVO 
	 * 					, <code>OfferMgmtVO</code> holds the search criteria.
	 * @return count, the number of customers assigned to the given offer.
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public long fetchCustomersBasedOnMSISDN(OfferMgmtVO offerMgmtVO)
			throws MISPException {

		logger.entering("fetchCustomersBasedOnMSISDN", offerMgmtVO);

		long customerCount = 0;

		try {

			customerCount = this.customerSubsManager
					.fetchCustomersBasedOnMSISDN(offerMgmtVO);

		} catch (DBException exception) {

			logger.error(
					"An exception occured while retrieving the customers for an offer",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("fetchCustomersBasedOnMSISDN", customerCount);

		return customerCount;

	}
	
	
	/**
	 * This method revokes the Offers from the customers for input MSISDN.
	 * 
	 * @param offerMgmtVO 
	 * 					, holding the MSISDN details.
	 * 
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public void revokeOfferForMSISDN(OfferMgmtVO offerMgmtVO) throws MISPException {
		logger.entering("revokeOfferForMSISDN", offerMgmtVO);

		try {

			this.customerSubsManager.revokeOfferForMSISDN(offerMgmtVO);

		} catch (DBException exception) {
			logger.error("An exception occured while revoking Offer.",
					exception);
			throw new MISPException(exception);

		}

		logger.exiting("revokeOfferForMSISDN");
	}
	
	/**
	 * Invoked as a DWR call, this method checks if the input MSISDN 
	 * already exists in the database and also check if any offer is assigned 
	 * to the already registered MSISDN.
	 * 
	 * @param msisdnCSV - <code>String</code> holding the comma separated values of input MSISDN
	 * @param customerId - <code>String</code> holding the Customer Id.
	 * @return Comma separated value of MSISDN registered, MSISDN subscribed to any offer.
	 */
	public String checkIfValidMSISDN(String msisdnCSV, String customerId){
		logger.entering("checkIfValidMSISDN", msisdnCSV, customerId);

		String existingMSISDN = checkIfMSISDNsExists(msisdnCSV, customerId);
		String assignedMSISDN = "";
		if(!existingMSISDN.equals(""))
			assignedMSISDN = checkIfMSISDNsAssigned(existingMSISDN, customerId);

		logger.exiting("checkIfValidMSISDN", existingMSISDN+ "|" +assignedMSISDN);
		return existingMSISDN+ "|" +assignedMSISDN;
	}
	
	
	/**
	 * This method checks if the input MSISDN already exists in the database.
	 * 
	 * @param msisdnCSV - <code>String</code> holding the comma separated values of MSISDN
	 * @param customerId - <code>String</code> holding the Customer Id.
	 * @return Comma separated value of existing MSISDN.
	 */
	public String checkIfMSISDNsExists(String msisdnCSV, String customerId){
		logger.entering("checkIfMSISDNExists", msisdnCSV, customerId);

		int flag=0;
		List<String> msisdnList =  new ArrayList<String>();
		if(msisdnCSV.lastIndexOf(",") != -1)
			msisdnList = Arrays.asList(msisdnCSV.split(","));
		else
			msisdnList.add(msisdnCSV);
		
		StringBuilder existingMSISDN = new StringBuilder();
		boolean isMSISDNExist = false;
		
		try {
			for(String msisdn : msisdnList) {
				isMSISDNExist = customerdetailsManager.checkIfMSISDNExists(msisdn, Integer.valueOf(customerId));
				if(isMSISDNExist) {
					existingMSISDN.append(msisdn);
					existingMSISDN.append(",");
					flag=1;
				}
				logger.debug("isMSISDNExist check: ", isMSISDNExist);
			}
			// Removing comma from the end.
			if(existingMSISDN.lastIndexOf(",") != -1)
				existingMSISDN = existingMSISDN.deleteCharAt(existingMSISDN.lastIndexOf(","));
		}
		catch (DBException exception) {
			logger.error("An error occured while " +
					"validating customer's MSISDN", exception);
		}
		catch (Exception exception) {
			logger.error("An error occured while " +
					"validating customer's MSISDN", exception);
		}		
		logger.exiting("checkIfMSISDNExists", existingMSISDN.toString());
		
		return (flag != 0) ? existingMSISDN.toString() : "";
	}
	
	
	/**
	 * This method checks if any offer is assigned to the input MSISDN.
	 * 
	 * @param msisdnCSV - <code>String</code> holding the comma separated values of MSISDN
	 * @param customerId - <code>String</code> holding the Customer Id.
	 * @return  Comma separated value of assigned MSISDN.
	 */
	public String checkIfMSISDNsAssigned(String msisdnCSV, String customerId) {
		logger.entering("checkIfMSISDNsAssigned", msisdnCSV, customerId);
		
		int flag=0;
		List<String> msisdnList =  new ArrayList<String>();
		if(msisdnCSV.lastIndexOf(",") != -1)
			msisdnList = Arrays.asList(msisdnCSV.split(","));
		else
			msisdnList.add(msisdnCSV);
				
		StringBuilder assignedMSISDN = new StringBuilder();
		boolean isMSISDNAssigned = false;
		
		try {
			for(String msisdn : msisdnList) {
				isMSISDNAssigned = customerSubsManager.checkIfMSISDNAssignedToOffers(msisdn);
				if(isMSISDNAssigned) {
					assignedMSISDN.append(msisdn);
					assignedMSISDN.append(",");
					flag=1;
				}
				logger.debug("isMSISDNAssigned check: ", isMSISDNAssigned);
			}
			// Removing comma from the end.
			if(assignedMSISDN.lastIndexOf(",") != -1)
				assignedMSISDN = assignedMSISDN.deleteCharAt(assignedMSISDN.lastIndexOf(","));
		}
		catch (DBException exception) {
			logger.error("An error occured while " +
					"validating customer's MSISDN", exception);
		}
		catch (Exception exception) {
			logger.error("An error occured while " +
					"validating customer's MSISDN", exception);
		}		
		logger.exiting("checkIfMSISDNsAssigned", assignedMSISDN.toString());
		return (flag != 0) ? assignedMSISDN.toString() : "";
	}
}
