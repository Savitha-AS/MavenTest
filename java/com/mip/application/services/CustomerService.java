package com.mip.application.services;

import static com.mip.framework.constants.PlatformConstants.BASE_KEY_RELATIONSHIP;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.mip.application.WS.managers.CustomerWSManager;
import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.ReportKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.dal.managers.AdminConfigManager;
import com.mip.application.dal.managers.CoverHistoryArchiveManager;
import com.mip.application.dal.managers.CustomerManager;
import com.mip.application.dal.managers.CustomerSubscriptionManager;
import com.mip.application.dal.managers.OfferDetailsManager;
import com.mip.application.model.CoverHistory;
import com.mip.application.model.CustomerDetails;
import com.mip.application.model.CustomerSubscription;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.LoyaltyPackRequest;
import com.mip.application.model.ProductDetails;
import com.mip.application.model.ReactivationCustomerRequest;
import com.mip.application.model.RegisterCustomerRequest;
import com.mip.application.model.UserDetails;
import com.mip.application.model.mappings.CustomerMappings;
import com.mip.application.services.transaction.CustomerTXService;
import com.mip.application.utils.ReportUtil;
import com.mip.application.view.AdminConfigVO;
import com.mip.application.view.CustomerVO;
import com.mip.application.view.LoyaltyCustomerVO;
import com.mip.application.view.ModifyCustomerVO;
import com.mip.application.view.ProductCoverDetailsVO;
import com.mip.application.view.mappings.CustomerM2VMappings;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.exceptions.WSException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.PropsUtil;
import com.mip.framework.utils.StringUtil;

/**
 * <p>
 * <code>CustomerService.java</code> contains all the methods pertaining to
 * Customer Management use case model. This is a service class for all customer
 * management modules.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class CustomerService {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomerService.class);

	/**
	 * Set inversion of control for <code>customerWSManager</code>
	 */
	private CustomerWSManager customerWSManager;

	/**
	 * Set inversion of control for <code>customerManager</code>
	 */
	private CustomerManager customerManager;

	/**
	 * Set inversion of control for <code>LoginService</code>
	 */
	private LoginService loginService;

	/**
	 * Set inversion of control for <code>OfferDetailsManager</code>
	 */
	private OfferDetailsManager offerDetailsManager;

	/**
	 * Set inversion of control for <code>CustomerTXService</code>
	 */
	private CustomerTXService customerTXService;

	/**
	 * Set inversion of control for <code>adminConfigManager</code>
	 */
	private AdminConfigManager adminConfigManager;

	/**
	 * Set inversion of control for <code>customerSubsManager</code>
	 */
	private CustomerSubscriptionManager customerSubsManager;

	/**
	 * An instance of session.
	 */

	private CoverHistoryArchiveManager coverHistoryArchiveManager;

	HttpSession session = null;

	/**
	 * @param customerWSManager
	 *            the customerWSManager to set
	 */
	public void setCustomerWSManager(CustomerWSManager customerWSManager) {
		this.customerWSManager = customerWSManager;
	}

	/**
	 * @param customerManager
	 *            the customerManager to set
	 */
	public void setCustomerManager(CustomerManager customerManager) {
		this.customerManager = customerManager;
	}

	public void setCoverHistoryArchiveManager(
			CoverHistoryArchiveManager coverHistoryArchiveManager) {
		this.coverHistoryArchiveManager = coverHistoryArchiveManager;
	}

	/**
	 * @param loginService
	 *            the loginService to set
	 */

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * @param customerTXService
	 *            the customerTXService to set
	 */
	public void setCustomerTXService(CustomerTXService customerTXService) {
		this.customerTXService = customerTXService;
	}

	/**
	 * 
	 * @param offerDetailsManager
	 *            the offerDetailsManager to set
	 */
	public void setOfferDetailsManager(OfferDetailsManager offerDetailsManager) {
		this.offerDetailsManager = offerDetailsManager;
	}

	/**
	 * 
	 * @param adminConfigManager
	 *            the adminConfigManager to set
	 */
	public void setAdminConfigManager(AdminConfigManager adminConfigManager) {
		this.adminConfigManager = adminConfigManager;
	}

	/**
	 * 
	 * @param customerSubsManager
	 *            the customerSubsManager to set
	 */
	public void setCustomerSubsManager(
			CustomerSubscriptionManager customerSubsManager) {
		this.customerSubsManager = customerSubsManager;
	}

	/**
	 * This service method delegates the customer registration request to
	 * web-service service layer and invokes the web-service.
	 * 
	 * @param custVO
	 *            object containing the customer details.
	 * 
	 * @param user
	 *            object containing the logged-in user details.
	 * 
	 * @return true if successful, false otherwise.
	 * @throws Exception 
	 */
	public String registerCustomer(CustomerVO custVO, UserDetails user)
			throws Exception {
		logger.entering("registerCustomer: ", custVO, user);
		boolean isRegistered = false, isModified = false;
		String resultCode = "";
		AdminConfigVO adminConfigVO = loginService.getConfigDetails();
		String URL = adminConfigVO.getRegisterCustomerWSURL();
		String assignOfferURL=adminConfigVO.getAssignOfferWSURL();
		CustomerVO getExistingCustomerDetails = null;
		try {
			/*
			 * Only when the its a new registration call Registration
			 * web-service.
			 */
			if (custVO.getCustId().equalsIgnoreCase("0")) {
				
				// 
				RegisterCustomerRequest registerCustomerRequest = new  RegisterCustomerRequest();
				
				registerCustomerRequest.setFirstName(custVO.getFname());
				registerCustomerRequest.setLastName(custVO.getSname());
				registerCustomerRequest.setDateOfBirth(StringUtil.isDefault(custVO.getDob()) ? null :  DateUtil.toSQLDate(custVO.getDob()).toString());
				registerCustomerRequest.setAge(Integer.valueOf(custVO.getAge()));
				
				registerCustomerRequest.setGender(custVO.getGender());
				registerCustomerRequest.setIsConfirmed(0);
				registerCustomerRequest.setMsisdn(custVO.getMsisdn());
				registerCustomerRequest.setDedcutionType(Integer.valueOf(custVO.getDeductionMode()));
				
				
				if(custVO.getProductId().contains("2"))
				{
					
					registerCustomerRequest.setInsRelFirstName(custVO.getInsRelFname());
					registerCustomerRequest.setInsRelLastName(custVO.getInsRelSurname());
					registerCustomerRequest.setInsRelDateOfBirth(StringUtil.isDefault(custVO.getInsRelIrDoB()) ? null :  DateUtil.toSQLDate(custVO.getInsRelIrDoB()).toString());
					registerCustomerRequest.setInsRelAge(Integer.valueOf(custVO.getInsRelAge()));
					registerCustomerRequest.setInsRelRelationship(custVO.getInsRelation());
					registerCustomerRequest.setInsRelMsisdn(custVO.getInsMsisdn());
					
				}	
				
				if(custVO.getProductId().contains("4"))
				{
					
					registerCustomerRequest.setIpNomFirstName(custVO.getIpNomFirstName());
					registerCustomerRequest.setIpNomSurName(custVO.getIpNomSurName());
					registerCustomerRequest.setIpNomAge(Integer.valueOf(custVO.getIpNomAge()));
					registerCustomerRequest.setIpNomMsisdn(custVO.getIpInsMsisdn());
					
				}		
				
				registerCustomerRequest.setRegisteredBy(user.getUserId());
				registerCustomerRequest.setRegistrationChannel(1);
				
				String produtsAssigned;
				if(custVO.getProductId().contains("2"))
					produtsAssigned = PlatformConstants.PRODUCT_FM + ","
							+ custVO.getProductId();
				else
					produtsAssigned =  custVO.getProductId();
				logger.info("products ", produtsAssigned);
				
				registerCustomerRequest.setProductsSelected(produtsAssigned);
				
				if(custVO.getProductCoverIdIP()!=null)
				{
					
					registerCustomerRequest.setProductCoverId(Integer.parseInt(custVO.getProductCoverIdIP().trim()));
				}else
					{
					registerCustomerRequest.setProductCoverId(0);
					
					}
						
				isRegistered = customerWSManager.invokeRegisterCustWS(registerCustomerRequest,
						user, URL);
			} else {
				/*
				 * Assign Free Model if
				 */
				String produtsAssigned = PlatformConstants.PRODUCT_FM;
				
				if(custVO.getProductId().contains("2"))
					produtsAssigned = PlatformConstants.PRODUCT_FM + ","
							+ custVO.getProductId();
				else
					produtsAssigned =  custVO.getProductId();
					
				
				logger.info("products ", produtsAssigned);
				custVO.setProductId(produtsAssigned);

				/* Get existing customer details */
				getExistingCustomerDetails = customerManager
						.checkCustomerExists(custVO.getMsisdn());

				if (null != getExistingCustomerDetails
						&& !getExistingCustomerDetails.getCustId()
								.equalsIgnoreCase("0")) {

					logger.info("No Product Change: ", custVO.getProductId(),
							" ; ", getExistingCustomerDetails
									.getPurchasedProducts(), " :",
							getExistingCustomerDetails.getPurchasedProducts()
									.equalsIgnoreCase(custVO.getProductId()));

					/* When there is no change in products selected. */
					if (getExistingCustomerDetails.getPurchasedProducts()
							.equalsIgnoreCase(custVO.getProductId())) {

						/*
						 * If existing Customer is HP and fills Insured relative
						 * details (i.e. registers for free) Call ASSIGN_OFFER
						 * Procedure.
						 */

						if (getExistingCustomerDetails.getPurchasedProducts()
								.equalsIgnoreCase(PlatformConstants.PRODUCT_HP)
								&& !custVO.getInsRelation().isEmpty()) {

							custVO.setProductId(PlatformConstants.PRODUCT_FM
									+ "," + PlatformConstants.PRODUCT_HP);
							
							int resultCodeFromWs=customerWSManager.invokeAssignOfferWS(custVO,
									user,assignOfferURL);
							isModified = customerManager.registerCust(custVO,
									user,resultCodeFromWs);
							isModified = modifyCustomerDetails(custVO, user);
						} else {
							/*
							 * If Product is same then only modify customer
							 * details No need for ASSIGN_OFFER Procedure call.
							 */
							isModified = modifyCustomerDetails(custVO, user);
						}

					}else {
		
						int resultCodeFromWs=customerWSManager.invokeAssignOfferWS(custVO,
								user,assignOfferURL);
						isModified = customerManager.registerCust(custVO, user,resultCodeFromWs);
						isModified = modifyCustomerDetails(custVO, user);
					}
				} else {
					isRegistered = false;
					isModified = false;
				}

			}
			resultCode = isRegistered + "," + isModified;
		} catch (WSException wsException) {
			logger.error("WSException occurred: Error in Web Service Invocation");
			throw new MISPException(wsException);
		}
		logger.exiting("registerCustomer: ", resultCode);
		return resultCode;
	}

	/**
	 * This service method delegates flow to manager for updating customer
	 * details after performing few transformations.
	 * 
	 * @param custVO
	 *            instance of CustomerVO, containing customer details.
	 * 
	 * @param userDetails
	 *            instance of UserDetails containing logged in user details.
	 * 
	 * @return isModified true upon success, false otherwise.
	 * 
	 * @throws MISPException
	 */
	public boolean modifyCustomerDetails(CustomerVO custVO,
			UserDetails userDetails) throws MISPException {
		logger.entering("modifyCustomerDetails", custVO, userDetails);

		boolean isModified = false;

		CustomerDetails custModel = null;

		List<InsuredRelativeDetails> insModelList = new ArrayList<InsuredRelativeDetails>();

		InsuredRelativeDetails insModel = new InsuredRelativeDetails();
		InsuredRelativeDetails nomineeModel = new InsuredRelativeDetails();

		CustomerSubscription customerSubscription = null;

		try {
			customerSubscription = customerSubsManager
					.getCustomerSubscriptionDetails(new Integer(custVO
							.getCustId()));

			// Gets the existing customer details
			if (null != customerSubscription) {
				// Customer has confirmed the registration.
				custModel = customerSubscription.getCustomerDetails();
			}

			// Gets the existing insured relative details
			Set<InsuredRelativeDetails> insuredRelativesSet = custModel
					.getInsuredRelatives();
			logger.info("InsuredRelativesSet: ", insuredRelativesSet);
			if (null != insuredRelativesSet && !insuredRelativesSet.isEmpty()) {
				for (InsuredRelativeDetails insDetails : insuredRelativesSet) {

					if (insDetails.getOfferId() == Integer
							.parseInt(PlatformConstants.PRODUCT_XL))
						insModel = insDetails;
					else if (insDetails.getOfferId() == Integer
							.parseInt(PlatformConstants.PRODUCT_IP))
						nomineeModel = insDetails;

				}
				// Gets the insured relative details to update
				insModel = CustomerMappings.getInsModelToUpdate(insModel,
						custVO, userDetails);
				nomineeModel = CustomerMappings.getNomineeModelToUpdate(
						nomineeModel, custVO, userDetails);
			}

			insModel = CustomerMappings.getInsModelToUpdate(insModel, custVO,
					userDetails);
			nomineeModel = CustomerMappings.getNomineeModelToUpdate(
					nomineeModel, custVO, userDetails);
			// Gets the customer details to update
			custModel = CustomerMappings.getCustModelToUpdate(custModel,
					custVO, userDetails);

			if (null != insModel)
				insModelList.add(insModel);

			if (null != nomineeModel)
				insModelList.add(nomineeModel);

			// Confirmed XL customer

			for (InsuredRelativeDetails irdDetail : insModelList) {
				isModified = this.customerTXService.modifyCustomerDetails(
						custModel, irdDetail);
			}

		} catch (DBException dbException) {
			logger.error(FaultMessages.CUSTOMER_UPDATE_DB_EXCEPTION,
					dbException);
			throw new MISPException(dbException);
		}

		logger.exiting("modifyCustomerDetails", isModified);

		return isModified;
	}

	/**
	 * This service method calls the manager to search for the customers
	 * matching the given search criteria.
	 * 
	 * @param searchMap
	 *            contains the search parameters which includes FirstName,
	 *            LastName, MSISDN and status
	 * 
	 * @param userDetails
	 *            <code>userDetails</code> An instance of the UserDetails
	 *            containing logged-in user details.
	 * 
	 * @return <code>ArrayList<CustomerVO></code> ArrayList of CustomerVO
	 *         containing the search results.
	 * 
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	
	public ArrayList<CustomerVO> searchCustomerDetails(
			HashMap<String, String> searchMap, UserDetails userDetails)
			throws MISPException {
		logger.entering("searchCustomerDetails", searchMap, userDetails);

		ArrayList<CustomerVO> customerList = null;
		ArrayList<CustomerVO> activeCustomerList = null;
		ArrayList<CustomerVO> deregisterCustomerList = null;
		//long diffDays = 0;
		try {
			customerList = new ArrayList<CustomerVO>();
			activeCustomerList = new ArrayList<CustomerVO>();
			deregisterCustomerList = new ArrayList<CustomerVO>();
			activeCustomerList = customerManager
					.searchCustomerDetails(searchMap);
			deregisterCustomerList = customerManager
					.getDeregisteredCustomerList(searchMap);
			
			if (activeCustomerList.size() > 10) {
				logger.info("Found [", customerList.size(),
						"] number of customers.");
			} else {
				logger.info("Found [", customerList, "] number of customers.");
			}
			if (!activeCustomerList.isEmpty()) {
				for (int i = 0; i < activeCustomerList.size(); i++) {
					if (activeCustomerList.get(i).getConfirmed() == 1) {
						activeCustomerList.get(i).setStatus("Confirmed");

					} else {
						activeCustomerList.get(i).setStatus("Not Confirmed");
					}
				}
			}
			if (!deregisterCustomerList.isEmpty()) {
				for (int j = 0; j < deregisterCustomerList.size(); j++) {
					deregisterCustomerList.get(j).setStatus("De-registered");
				}
			}
			customerList.addAll(activeCustomerList);
			customerList.addAll(deregisterCustomerList);

		} catch (DBException exception) {
			logger.error(
					"An exception occured while searching customer Details.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("searchCustomerDetails");
		return customerList;
	}
	

	/* This method will return the de registered info of the customer */

	/*public List<CustomerVO> getDeregisteredCustomerDetails(
			HashMap<String, String> searchMap, UserDetails userDetails)
			throws MISPException {
		logger.entering("getDeregisteredCustomerDetails", searchMap,
				userDetails);

		List<CustomerVO> custVODeregisteredList = null;
		try {
			custVODeregisteredList = customerManager
					.searchDeregisteredCustomerDetails(searchMap);

			for (CustomerVO customerVO : custVODeregisteredList) {
				customerVO.setIsActive(false);
				if (customerVO.getProductIds().equals("1,2,3")) {

					customerVO.setXLConfirmed(customerVO.getConfirmation()
							.split(",")[1]);
					customerVO.setXLOfferId(customerVO.getProductIds().split(
							",")[1]);
					customerVO.setXLRegBy(customerVO.getRegBy().split(",")[1]);
					customerVO
							.setXLRegDate(customerVO.getRegDate().split(",")[1]);
					customerVO.setXLDeregisteredDate(customerVO.getDeRegDate()
							.split(",")[1]);
					if (customerVO.getXLConfirmed().equals("1")) {
						customerVO.setXLConfDate(customerVO.getConfDate()
								.split(",")[1]);
					}

					customerVO.setHPConfirmed(customerVO.getConfirmation()
							.split(",")[2]);
					customerVO.setHPOfferId(customerVO.getProductIds().split(
							",")[2]);
					customerVO.setHPRegBy(customerVO.getRegBy().split(",")[2]);
					customerVO
							.setHPRegDate(customerVO.getRegDate().split(",")[2]);
					customerVO.setHPDeregisteredDate(customerVO.getDeRegDate()
							.split(",")[2]);
					if (customerVO.getHPConfirmed().equals("1")) {
						customerVO.setHPConfDate(customerVO.getConfDate()
								.split(",")[2]);
					}
				} else if (customerVO.getProductIds().equals("1,2")) {

					customerVO.setXLConfirmed(customerVO.getConfirmation()
							.split(",")[1]);
					customerVO.setXLOfferId(customerVO.getProductIds().split(
							",")[1]);
					customerVO.setXLRegBy(customerVO.getRegBy().split(",")[1]);
					customerVO
							.setXLRegDate(customerVO.getRegDate().split(",")[1]);
					customerVO.setXLDeregisteredDate(customerVO.getDeRegDate()
							.split(",")[1]);
					if (customerVO.getXLConfirmed().equals("1")) {
						customerVO.setXLConfDate(customerVO.getConfDate()
								.split(",")[1]);
					}

				} else if (customerVO.getProductIds().equals("3")) {
					customerVO.setHPConfirmed(customerVO.getConfirmation());
					customerVO.setHPOfferId(customerVO.getProductIds());
					customerVO.setHPRegBy(customerVO.getRegBy());
					customerVO.setHPRegDate(customerVO.getRegDate());
					customerVO.setHPDeregisteredDate(customerVO.getDeRegDate());
					if (customerVO.getHPConfirmed().equals("1")) {
						customerVO.setHPConfDate(customerVO.getConfDate());
					}
				} else if (customerVO.getProductIds().equals("1,2,4")) {

					customerVO.setXLConfirmed(customerVO.getConfirmation()
							.split(",")[1]);
					customerVO.setXLOfferId(customerVO.getProductIds().split(
							",")[1]);
					customerVO.setXLRegBy(customerVO.getRegBy().split(",")[1]);
					customerVO
							.setXLRegDate(customerVO.getRegDate().split(",")[1]);
					customerVO.setXLDeregisteredDate(customerVO.getDeRegDate()
							.split(",")[1]);
					if (customerVO.getXLConfirmed().equals("1")) {
						customerVO.setXLConfDate(customerVO.getConfDate()
								.split(",")[1]);
					}

					customerVO.setIPConfirmed(customerVO.getConfirmation()
							.split(",")[2]);
					customerVO.setIPOfferId(customerVO.getProductIds().split(
							",")[2]);
					customerVO.setIPRegBy(customerVO.getRegBy().split(",")[2]);
					customerVO
							.setIPRegDate(customerVO.getRegDate().split(",")[2]);
					customerVO.setIPDeregisteredDate(customerVO.getDeRegDate()
							.split(",")[2]);
					if (customerVO.getIPConfirmed().equals("1")) {
						customerVO.setIPConfDate(customerVO.getConfDate()
								.split(",")[2]);
					}
				} else if (customerVO.getProductIds().equals("4")) {
					customerVO.setIPConfirmed(customerVO.getConfirmation());
					customerVO.setIPOfferId(customerVO.getProductIds());
					customerVO.setIPRegBy(customerVO.getRegBy());
					customerVO.setIPRegDate(customerVO.getRegDate());
					customerVO.setIPDeregisteredDate(customerVO.getDeRegDate());
					if (customerVO.getIPConfirmed().equals("1")) {
						customerVO.setIPConfDate(customerVO.getConfDate());
					}
				} else if (customerVO.getProductIds().equals("1,2,3,4")) {

					customerVO.setXLConfirmed(customerVO.getConfirmation()
							.split(",")[1]);
					customerVO.setXLOfferId(customerVO.getProductIds().split(
							",")[1]);
					customerVO.setXLRegBy(customerVO.getRegBy().split(",")[1]);
					customerVO
							.setXLRegDate(customerVO.getRegDate().split(",")[1]);
					customerVO.setXLDeregisteredDate(customerVO.getDeRegDate()
							.split(",")[1]);
					if (customerVO.getXLConfirmed().equals("1")) {
						customerVO.setXLConfDate(customerVO.getConfDate()
								.split(",")[1]);
					}
					customerVO.setHPConfirmed(customerVO.getConfirmation()
							.split(",")[2]);
					customerVO.setHPOfferId(customerVO.getProductIds().split(
							",")[2]);
					customerVO.setHPRegBy(customerVO.getRegBy().split(",")[2]);
					customerVO
							.setHPRegDate(customerVO.getRegDate().split(",")[2]);
					customerVO.setHPDeregisteredDate(customerVO.getDeRegDate()
							.split(",")[2]);
					if (customerVO.getHPConfirmed().equals("1")) {
						customerVO.setHPConfDate(customerVO.getConfDate()
								.split(",")[2]);
					}

					customerVO.setIPConfirmed(customerVO.getConfirmation()
							.split(",")[3]);
					customerVO.setIPOfferId(customerVO.getProductIds().split(
							",")[3]);
					customerVO.setIPRegBy(customerVO.getRegBy().split(",")[3]);
					customerVO
							.setIPRegDate(customerVO.getRegDate().split(",")[3]);
					customerVO.setIPDeregisteredDate(customerVO.getDeRegDate()
							.split(",")[3]);
					if (customerVO.getIPConfirmed().equals("1")) {
						customerVO.setIPConfDate(customerVO.getConfDate()
								.split(",")[3]);
					}
				} else if (customerVO.getProductIds().equals("2")) {
					customerVO.setXLConfirmed(customerVO.getConfirmation());
					customerVO.setXLOfferId(customerVO.getProductIds());
					customerVO.setXLRegBy(customerVO.getRegBy());
					customerVO.setXLRegDate(customerVO.getRegDate());
					customerVO.setXLDeregisteredDate(customerVO.getDeRegDate());
					if (customerVO.getXLConfirmed().equals("1")) {
						customerVO.setXLConfDate(customerVO.getConfDate());
					}
				}

			}
		} catch (DBException exception) {
			logger.error(
					"An exception occured while searching Deregistered customer Details.",
					exception);
			throw new MISPException(exception);
		}
		logger.debug("Number of customers matching the search criteria :",
				custVODeregisteredList.size());
		logger.exiting("getDeregisteredCustomerDetails");

		return custVODeregisteredList;
	}*/

	/**
	 * This service method calls the manager method to get the customer details
	 * for the given customer Id.
	 * 
	 * @param custId
	 *            Customer Id of the registered customer.
	 * 
	 * @param userDetails
	 *            An instance of the UserDetails containing logged-in user
	 *            details.
	 * 
	 * @return <code>CustomerVO</code> Object of type CustomerVO.
	 * 
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public CustomerVO getCustomerDetails(String msisdn, UserDetails userDetails)
			throws MISPException {
		logger.entering("getCustomerDetails", msisdn, userDetails);

		CustomerVO custDetailsVO = new CustomerVO();

		try {
			custDetailsVO = customerManager.checkCustomerExists(msisdn);

		} catch (DBException dbException) {
			logger.error(
					"An exception occured while retrieving customer Details.",
					dbException);
			throw new MISPException(dbException);
		}

		logger.exiting("getCustomerDetails", custDetailsVO);

		return custDetailsVO;
	}

	/*
	 * This method will check whether the Deregistered Customer details are
	 * present or not and display it accordingly
	 */
	public CustomerVO getDeregisteredCustomerDetails(String custId,
			UserDetails userDetails) throws MISPException {
		logger.entering("getDeregisteredCustomerDetails", custId, userDetails);

		List<CustomerVO> custDetailsVOList = null;

		CustomerVO custVo = new CustomerVO();

		try {
			custDetailsVOList = customerManager
					.checkDeregisteredCustomerExists(custId);
			for (CustomerVO customerVO : custDetailsVOList) {
				custVo.setFname(customerVO.getFname());
				custVo.setSname(customerVO.getSname());
				custVo.setMsisdn(customerVO.getMsisdn());
				custVo.setDob(customerVO.getDob());
				custVo.setAge(customerVO.getAge());
				custVo.setGender(customerVO.getGender());
				custVo.setInsRelFname(customerVO.getInsRelFname());
				custVo.setInsRelSurname(customerVO.getInsRelSurname());
				custVo.setInsRelIrDoB(customerVO.getInsRelIrDoB());
				custVo.setInsRelAge(customerVO.getInsRelAge());
				custVo.setInsRelation(customerVO.getInsRelation());
				custVo.setIpNomFirstName(customerVO.getIpNomFirstName());
				custVo.setIpNomSurName(customerVO.getIpNomSurName());
				custVo.setIpNomAge(customerVO.getIpNomAge());
				custVo.setInsMsisdn(customerVO.getInsMsisdn());
				custVo.setIpInsMsisdn(customerVO.getIpInsMsisdn());

			}
		} catch (DBException dbException) {
			logger.error(
					"An exception occured while retrieving customer Details.",
					dbException);
			throw new MISPException(dbException);
		}

		logger.exiting("getDeregisteredCustomerDetails", custVo);

		return custVo;
	}

	/**
	 * This service method provides the list of relationship types for the
	 * register customer page
	 * 
	 * @return <code>List<String></code>, relationship types
	 * 
	 * @throws <code>MISPException</code>, if any error occurs
	 */
	public List<String> getRelationshipTypes() throws MISPException {

		logger.entering("getRelationshipTypes");

		List<String> relationshipList = null;
		try {
			relationshipList = PropsUtil.getPropertyList(BASE_KEY_RELATIONSHIP);

		} catch (Exception exception) {

			logger.error(FaultMessages.ERROR_PREPARING_RELATIONSHIP_TYPES);

			throw new MISPException(exception);
		}
		logger.exiting("getRelationshipTypes", relationshipList);

		return relationshipList;
	}

	/**
	 * Invoked as a DWR call, this method checks if the input MSISDN and MSISDN
	 * operator code already exists in the database.
	 * <p>
	 * <b>[January 12th 2012]</b> : A change request from customer to add
	 * operator code validation in the input MSISDN. The validation is included
	 * in this DWR call to avoid another DWR implementation.
	 * </p>
	 * 
	 * @param msisdn
	 *            input MSISDN.
	 * @param customerId
	 *            cust_id of the corresponding MSISDN.
	 * 
	 * @return <p>
	 *         <b> [true,true] </b> if MSISDN Exists and is Valid
	 *         </p>
	 *         <p>
	 *         <b> [false,false] </b> if MSISDN does not Exist and Invalid
	 *         </p>
	 */
	public String checkIfMSISDNExists(String msisdn, String customerId) {
		logger.entering("checkIfMSISDNExists", msisdn, customerId);
		boolean isMSISDNExisting = false;
		boolean isMSISNCodeValid = false;
		String result = "";
		try {
			// Check if MSISDN already exists.
			isMSISDNExisting = customerManager.checkIfMSISDNExists(msisdn,
					Integer.valueOf(customerId));

			// Check if the MSISDN starts with valid operator code.
			// Added : January 12th 2012.
			isMSISNCodeValid = checkIfMSISDNCodesIsInvalid(msisdn, customerId);
			result = String.valueOf(isMSISDNExisting) + ","
					+ String.valueOf(isMSISNCodeValid);

		} catch (DBException dbException) {
			logger.error("An error occured while "
					+ "validating customer's MSISDN", dbException);
		}
		logger.exiting("checkIfMSISDNExists", result);
		return result;
	}

	/**
	 * This method retrieves the available/active offers - Offer Name and Offer
	 * ID only.
	 * 
	 * @return <code>List&ltOfferDetails&gt</code>
	 * 
	 * @throws <code>MISPException</code>
	 */
	public List<ProductDetails> retrieveOfferNamesAndIds() throws MISPException {
		logger.entering("retrieveOfferNamesAndIds");

		List<ProductDetails> products = null;

		try {
			products = offerDetailsManager.retrieveOfferNamesAndIds();
		} catch (DBException exception) {
			logger.error("An exception occured while retrieving offers.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("retrieveOfferNamesAndIds");
		return products;
	}

	/**
	 * This method checks if the input MSISDN starts with a valid operator code.
	 * <p>
	 * Example : <b>027,257,023,026</b> etc.,
	 * </p>
	 * 
	 * @param msisdn
	 *            input MSISDN.
	 * @param customerId
	 *            cust_id of the corresponding MSISDN.
	 * 
	 * @return false if valid and true otherwise
	 */
	public boolean checkIfMSISDNCodesIsInvalid(String msisdn, String customerId) {
		logger.entering("checkIfMSISDNCodesIsInvalid", msisdn, customerId);

		String firstThreeDigits = msisdn.substring(0, 3);
		boolean isMSISDNCodesExisting = false;
		ArrayList<String> msisdnCodes = new ArrayList<String>();

		try {
			// get the adminconfig details and store it in a HashMap
			HashMap<String, String> configMap = (HashMap<String, String>) adminConfigManager
					.getConfigDetails();
			String msisdncsv = configMap.get(PlatformConstants.MSISDN_CODES);
			String delimiter = ",";

			// TODO: Modify the String[] to ArrayList<String>
			String[] tokens = msisdncsv.split(delimiter);

			for (int i = 0; i < tokens.length; i++) {
				msisdnCodes.add(tokens[i].trim());
			}
			isMSISDNCodesExisting = msisdnCodes.contains(firstThreeDigits);

		} catch (DBException dbException) {
			logger.error("An error occured while "
					+ "validating customer's MSISDNcodes", dbException);
		}

		logger.exiting("checkIfMSISDNCodesIsInvalid", !isMSISDNCodesExisting);

		return !isMSISDNCodesExisting;
	}

	/**
	 * This service method delegates the customer de-registration request to
	 * web-service service layer and invokes the web-service.
	 * 
	 * @param custVO
	 *            an instance of {@link com.mip.application.view.CustomerVO}
	 * 
	 * @param user
	 *            an instance of {@link com.mip.application.model.UserDetails}
	 * 
	 * @return TRUE if successful, FALSE otherwise.
	 * 
	 * @throws MISPException
	 */
	public String deRegisterCustomer(CustomerVO custVO, UserDetails user)
			throws MISPException {
		logger.entering("deRegisterCustomer: ", custVO, user);

		String responseCode = "";
		int deregResponse = 0;
		int churnType = 1;

		try {

			String productList = custVO.getProductId().replaceAll(",", "");
			
			int deregChurnType=4;
			
			if(PlatformConstants.DEREG_TIGO_STAFF_CHURN_TYPE_3.contains(user.getRoleMaster().getRoleName()))
				deregChurnType=3;
			
			deregResponse = customerManager.deregisterCustomer(
					custVO.getMsisdn(), user.getUserId(), productList,
					churnType,deregChurnType);

			if (deregResponse == 2) {
				responseCode = SuccessMessages.DE_REGISTER_SUCCESS;
			} else {
				logger.error("Unknown Error has occurred while De-Registering Customer");
				throw new MISPException(
						new Throwable(
								"Unknown Error has occurred while De-Registering Customer"));
			}
		} catch (DBException dbException) {
			logger.error("DBException occurred: Error in Procedure Invocation");
			throw new MISPException(dbException);
		}

		logger.exiting("deRegisterCustomer: ", responseCode);

		return responseCode;
	}

	/**
	 * This method return the Customer Subscription details for the customer.
	 * 
	 * @param customerId
	 *            cust_id of the customer.
	 * @return <code>CustomerSubscription</code> Object of type
	 *         CustomerSubscription.
	 * 
	 * @throws MISPException
	 */
	public CustomerSubscription getCustomerSubscriptionDetails(int customerId)
			throws MISPException {
		Object[] params = { customerId };
		logger.entering("getCustomerSubscriptionDetails", params);

		CustomerSubscription customerSubscription = null;

		try {
			customerSubscription = this.customerSubsManager
					.getCustomerSubscriptionDetails(customerId);
		} catch (DBException dbException) {
			logger.error(
					"An error occurred while getting customer subscription details",
					dbException);
			throw new MISPException(dbException);
		}

		logger.exiting("getCustomerSubscriptionDetails", customerSubscription);
		return customerSubscription;
	}

	/**
	 * This method return the de registered Customer Subscription details for
	 * the customer.
	 * 
	 * @param customerId
	 *            cust_id of the customer.
	 * @return <code>CustomerSubscription</code> Object of type
	 *         CustomerSubscription.
	 * 
	 * @throws MISPException
	 */

	/**
	 * This method writes the E-Signature to the excel workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param msisdn
	 *            Customer's MSISDN
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public void writeSignature(HSSFWorkbook workbook, String msisdn)
			throws MISPException {
		Object[] params = { msisdn };
		logger.entering("writeSignature", params);

		// CustomerDetails custDetails = null;
		CustomerVO custDetails = null;
		try {
			custDetails = this.customerManager.getCustomerFromMSISDN(msisdn);

			HSSFSheet sheet = workbook.createSheet(msisdn);

			// Format for data
			HSSFCellStyle dataCellFormat = ReportUtil
					.getDataRowCellStyle(workbook);

			HSSFRow row = null;
			HSSFCell cell = null;

			row = sheet.createRow(0);
			cell = row.createCell(0);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(ReportKeys.CUSTOMER_FIRST_NAME);

			cell = row.createCell(1);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(ReportKeys.CUSTOMER_SURNAME);

			cell = row.createCell(2);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(ReportKeys.MOBILE_NUMBER);

			cell = row.createCell(3);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(ReportKeys.CONFIRMED);

			cell = row.createCell(4);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(ReportKeys.CONFIRMED_DATE);

			row = sheet.createRow(1);
			cell = row.createCell(0);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(custDetails.getFname());

			cell = row.createCell(1);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(custDetails.getSname());

			cell = row.createCell(2);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(custDetails.getMsisdn());

			cell = row.createCell(3);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(custDetails.getConfirmed() == (byte) 1 ? "YES"
					: "NO");

			cell = row.createCell(4);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(custDetails.getConfirmedDate());

			// Resize Columns
			for (int i = 0; i < 5; i++)
				sheet.autoSizeColumn(i);

		} catch (DBException dbException) {
			logger.error(
					"An error occurred while getting customer E-Signature.",
					dbException);
			throw new MISPException(dbException);
		} catch (Exception exception) {
			logger.error(
					"An error occurred while creating customer E-Signature.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("writeSignature");
	}

	/**
	 * Invoked as a DWR call, this method checks if the input MSISDN was
	 * de-registered previously.
	 * 
	 * @param msisdn
	 *            input MSISDN.
	 * 
	 * @return @return Comma Separated Status <b> [isRegistered, isConfirmed,
	 *         isSubscribed, isDeregistered] </b>
	 *         <p>
	 *         isRegistered : if MSISDN Exists or not
	 *         </p>
	 *         <p>
	 *         isConfirmed : if MSISDN Confirmed or not
	 *         </p>
	 *         <p>
	 *         isSubscribed : is MSISDN subscribed for offer or not
	 *         </p>
	 *         <p>
	 *         isDeregistered: if MSISDN already de-registered or not
	 *         </p>
	 */
	public String checkMSISDNStatus(String msisdn) {
		logger.entering("checkMSISDNStatus", msisdn);
		String returnStatus = "";

		try {

			returnStatus = customerManager.checkMSISDNStatus(msisdn);

		} catch (DBException dbException) {
			logger.error("An error occured while checking MSISDN status",
					dbException);
		}

		logger.exiting("checkMSISDNStatus");
		return returnStatus;
	}

	/**
	 * Invoked as a DWR call, this method checks if the input MSISDN was
	 * registered previously.
	 * 
	 * @param msisdn
	 *            input MSISDN.
	 * 
	 * @return
	 * 
	 */
	public CustomerVO checkCustomerExists(String msisdn) throws MISPException {
		logger.entering("checkCustomerExists", msisdn);
		CustomerVO customerVO = new CustomerVO();
		try {
			customerVO = customerManager.checkCustomerExists(msisdn);
		} catch (DBException dbException) {
			logger.error("An error occured while getting MSISDN details",
					dbException);
		}

		logger.exiting("checkCustomerExists", customerVO);
		return customerVO;
	}

	
	
	public CustomerVO checkCustomerExistsForReg(String msisdn) throws MISPException {
	logger.entering("checkCustomerExistsForReg", msisdn);
	CustomerVO customerVO = new CustomerVO();
	try {
		customerVO = customerManager.checkCustomerExists(msisdn);
	} catch (DBException dbException) {
		logger.error("An error occured while getting MSISDN details",
				dbException);
	}

	logger.exiting("checkCustomerExistsForReg", customerVO);
	return customerVO;
	}

	public CustomerVO getCustProductsForDeReg(String msisdn)
			throws MISPException {
		logger.entering("getCustProductsForDeReg", msisdn);
		CustomerVO customerVO = new CustomerVO();
		try {
			customerVO = customerManager.getCustProductsForDeReg(msisdn);
		} catch (DBException dbException) {
			logger.error("An error occured while getting MSISDN details",
					dbException);
		}

		logger.exiting("getCustProductsForDeReg", customerVO);
		return customerVO;
	}

	public String checkMSISDNExist(String msisdn) {
		logger.entering("checkMSISDNExist", msisdn);
		String result = "0";
		boolean isExist;
		try {
			isExist = customerManager.checkIfMSISDNExistsForChangeMode(msisdn);

			result = (isExist == true ? "1" : "0");
		} catch (DBException e) {
			logger.error("Exception occured while checking msisdn is exists or not");
		}

		logger.exiting("checkMSISDNExist", result);
		return result;

	}

	public List<CustomerVO> findCustomer(String msisdn) throws DBException {
		logger.entering("findCustomer", msisdn);

		List<CustomerVO> customerVO;
		customerVO = customerManager.getCustomerDetailsForChangeMode(msisdn);
		return customerVO;
	}

	public boolean ChangeDeductionMode(CustomerVO customerVO,
			UserDetails userDetails) {
		logger.entering("ChangeDeductionMode", customerVO, userDetails);
		String url = "";
		boolean result = false;

		try {
			AdminConfigVO adminConfigVO = loginService.getConfigDetails();
			url = adminConfigVO.getChangeDeductionModeWSURL();

			result = customerWSManager.invokeChangeDeductionModeWS(customerVO,
					userDetails, url);

		} catch (MISPException e) {
			logger.error(
					"Exception occurred while changing deduction mode of the customer",
					e);
		}

		logger.exiting("ChangeDeductionMode", result);
		return result;

	}

	// check for msisdn exists for cover history
	public String checkMSISDNStatusCoverHistory(String msisdn) {
		logger.entering("checkMSISDNStatusCoverHistory", msisdn);
		String result = "";
		boolean isExists = false;
		try {
			isExists = customerManager
					.checkIfMSISDNExistsForCoverHistory(msisdn);
			result = (isExists == true ? "1" : "0");

		} catch (DBException e) {
			logger.error(
					"Exception occurred while checking msisdn exists or not", e);
		}
		result = result + "," + "0";
		logger.exiting("checkMSISDNStatusCoverHistory", result);
		return result;

	}

	public List<CustomerVO> coverHistory(String msisdn) throws DBException {
		logger.entering("coverHistory", msisdn);
		List<CustomerVO> customerDetails;
		customerDetails = customerManager.coverHistoryDetails(msisdn);

		logger.exiting("coverHistory", customerDetails.size());
		return customerDetails;
	}

	public List<CustomerVO> moreCoverHistory(String msisdn) throws DBException {
		logger.entering("coverHistory", msisdn);
		List<CustomerVO> customerDetails=null;
		List<CoverHistory> coverHistory=null;
		CustomerM2VMappings customerM2VMappings=new CustomerM2VMappings();
		
		//CoverHistoryArchiveManager coverHistoryArchiveManager=new CoverHistoryArchiveManager();
		
		coverHistory = coverHistoryArchiveManager.getCoverHistoryDetails(msisdn);

		//mapping
		customerDetails=customerM2VMappings.mapCoverModelToCustomerVO(coverHistory);
				
		logger.exiting("coverHistory", customerDetails);
		return customerDetails;
	}

	public List<CustomerVO> getXLCustomerDetails(
			List<CustomerVO> customerDetails) {
		logger.entering("getXLCustomerDetails", customerDetails);

		List<CustomerVO> xlCustomerVO = new ArrayList<CustomerVO>();
		
		int xlCount = 0;
		int freeCount=0;
		int cover1=0;
		for (CustomerVO customerVO : customerDetails) {

			if (customerVO.getCoverXL() == null) {
				xlCount++;
			}
			
			if (customerVO.getCoverFree() == null) {
				freeCount++;
			}
			
			
			if(customerVO.getCoverFree() != null && customerVO.getCoverXL() != null)
			{
				
				double cover=Double.parseDouble(customerVO.getCoverFree())+Double.parseDouble(customerVO.getCoverXL());
				cover1=(int)cover;
				customerVO.setFreeAndxlCover("GHC " + String.valueOf(cover1));
			}
			
			if(customerVO.getCoverFree() != null && customerVO.getCoverXL() == null)
			{
				
				double cover=Double.parseDouble(customerVO.getCoverFree());
				cover1=(int)cover;
				customerVO.setFreeAndxlCover("GHC " +String.valueOf(cover1));
			}
			
			if(customerVO.getCoverFree() == null && customerVO.getCoverXL() != null)
			{
				
				double cover=Double.parseDouble(customerVO.getCoverXL());
				cover1=(int)cover;
				customerVO.setFreeAndxlCover("GHC " +String.valueOf(cover1));
			}
			
			if(customerVO.getCoverFree() == null && customerVO.getCoverXL() == null)
			{
				
				/*double cover=Double.parseDouble(customerVO.getCoverXL());
				cover1=(int)cover;*/
				customerVO.setFreeAndxlCover("GHC " +" 0");
			}
			
              //concate  currency
			
			String temp="GHC "+(customerVO.getPrevMonthUsage()==null ?"  0 ":customerVO.getPrevMonthUsage());
			customerVO.setPrevMonthUsage(temp);
			
			temp="GHC "+(customerVO.getChargesXL()==null ? "  0 ":customerVO.getChargesXL());
			customerVO.setChargesXL(temp);
			
			xlCustomerVO.add(customerVO);
		}

		if (xlCount == customerDetails.size() && freeCount==customerDetails.size()) {
			logger.exiting("getXLCustomerDetails", "null");
			return null;
		}
		logger.exiting("getXLCustomerDetails", xlCustomerVO.size());
		return xlCustomerVO;

	}

	public List<CustomerVO> getHPCustomerDetails(
			List<CustomerVO> customerDetails) {
		logger.entering("getHPCustomerDetails", customerDetails);

		List<CustomerVO> hpCustomerVO = new ArrayList<CustomerVO>();
		int hpCount = 0;
		for (CustomerVO customerVO : customerDetails) {

			if (customerVO.getCoverHP() == null) {
				hpCount++;
			}

			String hpcharges = "GHC "
					+ ((customerVO.getChargesHP() == null) ? " 0" : customerVO
							.getChargesHP());
			String hpCover = "GHC "
					+ ((customerVO.getCoverHP() == null) ? " 0" : (int) (Double
							.parseDouble(customerVO.getCoverHP()) / 30));

			customerVO.setChargesHP(hpcharges);
			customerVO.setCoverHP(hpCover);

			hpCustomerVO.add(customerVO);
		}

		if (hpCount == customerDetails.size()) {
			logger.exiting("getHPCustomerDetails", "null");
			return null;
		}
		logger.exiting("getHPCustomerDetails", hpCustomerVO.size());
		return hpCustomerVO;

	}

	public List<CustomerVO> getFirst12CustomerDetails(
			List<CustomerVO> customerDetails) {
		logger.entering("getFirst12CustomerDetails", customerDetails);
		List<CustomerVO> customerDetailsList=new ArrayList<CustomerVO>();
		
	     for(int i=0;i<12;++i)
	     {
	    	 customerDetailsList.add(customerDetails.get(i));
	     }
		
		logger.info("customerDetailsList size", customerDetailsList.size());
		logger.exiting("getFirst12CustomerDetails",customerDetailsList.size());
		return customerDetailsList;
	}

	/**
	 * This method customer information For deregistration
	 * 
	 * @param msisdn
	 * @return
	 */
	public List<CustomerVO> getcustomerDetailsForDereg(String msisdn) {
		logger.entering("getcustomerDetailsForDereg", msisdn);
		List<CustomerVO> customerDetailsList = new ArrayList<CustomerVO>();
		try {
			customerDetailsList = customerManager
					.customerDetailsForDereg(msisdn);
		} catch (DBException dbException) {
			logger.error("An error occured while getting MSISDN details",
					dbException);
		}

		logger.exiting("getcustomerDetailsForDereg", customerDetailsList);
		return customerDetailsList;

	}

	/**
	 * This method get deregistered details of customer
	 * 
	 * @param msisdn
	 * @return
	 * @throws DBException
	 */
	public List<CustomerVO> getCustomerDeregisteredDetails(String msisdn)
			throws DBException {
		logger.entering("getCustomerDeregisteredDetails", msisdn);
		List<CustomerVO> customerDetailsList=new ArrayList<CustomerVO>();
		customerDetailsList=customerManager.customerDeregisteredDetails(msisdn);
		if(customerDetailsList.size() == 0)
			customerDetailsList=null;
		logger.exiting("getCustomerDeregisteredDetails",customerDetailsList);
		return customerDetailsList;
	}

	/**
	 * This method returns the offer cover segments based on the customer Id
	 * 
	 * @param custId
	 *            Customer Id
	 * @return <code>List</code> containing Offer Cover details.
	 * @throws MISPException
	 */
	public CustomerVO getOfferSubscriptionFromCustId(int custId)
			throws MISPException {
		logger.entering("getOfferSubscriptionFromCustId", custId);

		CustomerVO customerVO = null;

		try {
			customerVO = customerManager.getOfferSubscriptionFromCustId(custId);

		} catch (DBException dbException) {
			logger.error(
					"An exception occurred while retrieving offer subscription.",
					dbException);
			throw new MISPException(dbException);
		}

		logger.exiting("getOfferSubscriptionFromCustId");
		return customerVO;
	}

	public CustomerVO getDeregOfferSubscriptionFromCustId(String custId)
			throws MISPException {
		logger.entering("getDeregOfferSubscriptionFromCustId", custId);

		CustomerVO customerVO = null;

		try {
			customerVO = customerManager
					.getDeregisteredOfferSubscriptionFromCustId(custId);

		} catch (DBException dbException) {
			logger.error(
					"An exception occurred while retrieving offer subscription.",
					dbException);
			throw new MISPException(dbException);
		}

		logger.exiting("getDeregOfferSubscriptionFromCustId");
		return customerVO;
	}

	/**
	 * This method retrieves the available/active offers - Offer Name and Offer
	 * ID only.
	 * 
	 * @return <code>List&ltOfferDetails&gt</code>
	 * 
	 * @throws <code>MISPException</code>
	 */
	public List<ProductCoverDetailsVO> retrieveOfferCoverDetailsIPProduct()
			throws MISPException {
		logger.entering("retrieveOfferCoverDetailsIPProduct");

		List<ProductCoverDetailsVO> products = null;

		try {
			products = offerDetailsManager.retrieveOfferCoverDetailsIPProduct();
		} catch (DBException exception) {
			logger.error("An exception occured while retrieving offers.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("retrieveOfferCoverDetailsIPProduct");
		return products;
	}

	public List<CustomerVO> getIPCustomerDetails(
			List<CustomerVO> customerDetails) {
		logger.entering("getIPCustomerDetails", customerDetails);

		List<CustomerVO> ipCustomerVO = new ArrayList<CustomerVO>();
		int ipCount = 0;
		for (CustomerVO customerVO : customerDetails) {

			if (customerVO.getCoverIP() == null) {
				ipCount++;
			}

			String ipcharges = "GHC "
					+ ((customerVO.getChargesIP() == null) ? " 0" : customerVO
							.getChargesIP());
			String ipCover = "GHC "
					+ ((customerVO.getCoverIP() == null) ? " 0" : (int) (Double
							.parseDouble(customerVO.getCoverIP())));

			customerVO.setChargesIP(ipcharges);
			customerVO.setCoverIP(ipCover);

			ipCustomerVO.add(customerVO);
		}

		if (ipCount == customerDetails.size()) {
			logger.exiting("getIPCustomerDetails", "null");
			return null;
		}
		logger.exiting("getIPCustomerDetails", ipCustomerVO.size());
		return ipCustomerVO;

	}

	/**
	 * This method retrieve OfferCover Details IP
	 * 
	 * @return
	 * @throws MISPException
	 */

	public List<ProductCoverDetailsVO> retrieveOfferCoverDetailsIP()
			throws MISPException {
		logger.entering("retrieveOfferNamesAndIdsIgnoreIP");

		List<ProductCoverDetailsVO> productCoverListIP = null;

		try {
			productCoverListIP = offerDetailsManager
					.retrieveOfferCoverDetailsIP();
		} catch (DBException exception) {
			logger.error("An exception occured while retrieving offers.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("retrieveOfferNamesAndIdsIgnoreIP");
		return productCoverListIP;
	}


	/**
	 * This method retrieve the summary of modifications done with the existing
	 * customer Details IP and Xl
	 * 
	 * @return
	 * @throws MISPException
	 */

	public List<ModifyCustomerVO> getSummaryDetailsChange(String custId)
			throws Exception {
		logger.entering("getSummaryDetailsChange", custId);
		AdminConfigVO adminConfigVO = loginService.getConfigDetails();
		String recordsLimit = adminConfigVO
				.getSummaryCustomerDetailsChangesRecordLimit();
		List<ModifyCustomerVO> modifyCustomerVO = customerManager
				.getSummaryDetailsChange(custId, recordsLimit);
		logger.exiting("getSummaryDetailsChange", modifyCustomerVO);
		return modifyCustomerVO;

	}


	

// Checks customer has grace period or not
	public int checkGracePeriod(String msisdn, int productId) throws MISPException {
		
		logger.entering("checkGracePeriod ", msisdn,productId);
		
		int resultCode=-1;

		try {
			resultCode= customerManager.checkGracePeriod(msisdn,productId);
		} catch (Exception exception) {
			logger.error("An exception occured while checking grace period.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("checkGracePeriod ",resultCode);
		return resultCode;
	}

	// gets the no claim bonus date
		public Date getNoClaimBonusDate(String msisdn) throws MISPException {
			
			logger.entering("getNoClaimBonusDate ", msisdn);
			
			Date resultCode;

			try {
				resultCode= customerManager.getNoClaimBonusDate(msisdn);
			} catch (Exception exception) {
				logger.error("An exception occured while getting no claim bonus date .",
						exception);
				throw new MISPException(exception);
			}

			logger.exiting("getNoClaimBonusDate ",resultCode);
			return resultCode;
		}

public ModifyCustomerVO  getNewOldCustomerDetails(CustomerVO newCustVO,
			CustomerVO oldCustVO) throws Exception {

		logger.entering("getNewOldCustomerDetails", newCustVO, oldCustVO);
		ModifyCustomerVO modifyCustomerVO = new ModifyCustomerVO();
		modifyCustomerVO.setModifiedRecord(0);

		java.util.Date date = new Date();
		Object currentDate = new java.sql.Timestamp(date.getTime());
		
		// First name
		if (!newCustVO.getFname().trim().equals(oldCustVO.getFname())) {
			modifyCustomerVO.setNewFname(newCustVO.getFname());
			modifyCustomerVO.setOldFname(oldCustVO.getFname());
			modifyCustomerVO.setModifiedRecord(1);

		}

		// surname name
		if (!newCustVO.getSname().trim().equals(oldCustVO.getSname())) {
			modifyCustomerVO.setNewSname(newCustVO.getSname());
			modifyCustomerVO.setOldSname(oldCustVO.getSname());
			modifyCustomerVO.setModifiedRecord(1);

		}
		
				
		//dob
		if (!newCustVO.getDob().trim().equals(oldCustVO.getDob())) {
			modifyCustomerVO.setNewDob(StringUtil.isDefault(newCustVO.getDob()) ? null :  DateUtil.toSQLDate(newCustVO.getDob()).toString());
			modifyCustomerVO.setOldDob(StringUtil.isDefault(oldCustVO.getDob() )? null :  DateUtil.toSQLDate(oldCustVO.getDob()).toString());
			modifyCustomerVO.setModifiedRecord(1);

		}
		
		
		//Age
		if (!newCustVO.getAge().trim().equals(oldCustVO.getAge())) {
			modifyCustomerVO.setNewAge(newCustVO.getAge());
			modifyCustomerVO.setOldAge(oldCustVO.getAge());
			modifyCustomerVO.setModifiedRecord(1);

		}
			
		
		//gender
		if (newCustVO.getGender()!=null && !newCustVO.getGender().trim().equals(oldCustVO.getGender()) ) {
			modifyCustomerVO.setNewGender(newCustVO.getGender());
			modifyCustomerVO.setOldGender(oldCustVO.getGender());
			modifyCustomerVO.setModifiedRecord(1);

		}
		
		
		if(!StringUtil.isEmpty(newCustVO.getInsRelFname()) && !StringUtil.isEmpty(oldCustVO.getInsRelFname()) )
		{
			// Insured First name
			if (!newCustVO.getInsRelFname().trim().equals(oldCustVO.getInsRelFname())) {
				modifyCustomerVO.setNewInsRelFname(newCustVO.getInsRelFname());
				modifyCustomerVO.setOldInsRelFname(oldCustVO.getInsRelFname());
				modifyCustomerVO.setModifiedRecord(1);
				
			}
			
			// Insured surname name
			if (!newCustVO.getInsRelSurname().trim().equals(oldCustVO.getInsRelSurname())) {
				modifyCustomerVO.setNewInsRelSurname(newCustVO.getInsRelSurname());
				modifyCustomerVO.setOldInsRelSurname(oldCustVO.getInsRelSurname());
				modifyCustomerVO.setModifiedRecord(1);
				
			}
			
			// Insured dob
			if (!newCustVO.getInsRelIrDoB().trim().equals(oldCustVO.getInsRelIrDoB())) {
				modifyCustomerVO.setNewInsRelIrDoB(StringUtil.isDefault(newCustVO.getInsRelIrDoB()) ? null: DateUtil.toSQLDate(newCustVO.getInsRelIrDoB()).toString());
				modifyCustomerVO.setOldInsRelIrDoB(StringUtil.isDefault(oldCustVO.getInsRelIrDoB()) ? null:DateUtil.toSQLDate(oldCustVO.getInsRelIrDoB()).toString());
				modifyCustomerVO.setModifiedRecord(1);
				
			}
			
			
			
			// Insured Age
			if (!newCustVO.getInsRelAge().trim().equals(oldCustVO.getInsRelAge())) {
				newCustVO.setInsRelAge(StringUtil.isDefault(newCustVO.getInsRelAge(), null));
				oldCustVO.setInsRelAge(StringUtil.isDefault(oldCustVO.getInsRelAge(), null));
				modifyCustomerVO.setNewInsRelAge(newCustVO.getInsRelAge());
				modifyCustomerVO.setOldInsRelAge(oldCustVO.getInsRelAge());
				modifyCustomerVO.setModifiedRecord(1);
				
			}
			
			//Insured relation ship 
			if (!newCustVO.getInsRelation().trim().equals(oldCustVO.getInsRelation())) {
				modifyCustomerVO.setNewCustRelationship(newCustVO.getInsRelation());
				modifyCustomerVO.setOldCustRelationship(oldCustVO.getInsRelation());
				modifyCustomerVO.setModifiedRecord(1);
								
			}
			
			
			// Insured MSISDN
			if (!newCustVO.getInsMsisdn().trim().equals(oldCustVO.getInsMsisdn())) {
				modifyCustomerVO.setNewInsMsisdn(newCustVO.getInsMsisdn());
				modifyCustomerVO.setOldInsMsisdn(oldCustVO.getInsMsisdn());
				modifyCustomerVO.setModifiedRecord(1);
				modifyCustomerVO.setIsModifiedInsMsisdn(1);		
				
			}
			
		}
		
		
		
		
		if(!StringUtil.isEmpty(newCustVO.getIpNomFirstName()) &&  !StringUtil.isEmpty(oldCustVO.getIpNomFirstName()) )
		{			
			// IP insured First name
			if (!newCustVO.getIpNomFirstName().trim().equals(oldCustVO.getIpNomFirstName())) {
				modifyCustomerVO.setNewIpNomFirstName(newCustVO.getIpNomFirstName());
				modifyCustomerVO.setOldIpNomFirstName(oldCustVO.getIpNomFirstName());
				modifyCustomerVO.setModifiedRecord(1);
				
			}
			
			// IP Insured Age
			if (!newCustVO.getIpNomAge().trim().equals(oldCustVO.getIpNomAge())) {
				modifyCustomerVO.setNewIpNomAge(StringUtil.isDefault(newCustVO.getIpNomAge())?null: newCustVO.getIpNomAge() );
				modifyCustomerVO.setOldIpNomAge(StringUtil.isDefault(oldCustVO.getIpNomAge())?null:	oldCustVO.getIpNomAge());
				modifyCustomerVO.setModifiedRecord(1);
				
			}
			
			// IP Insured surname name
			if (!newCustVO.getIpNomSurName().trim().equals(oldCustVO.getIpNomSurName())) {
				modifyCustomerVO.setNewIpNomSurName(newCustVO.getIpNomSurName());
				modifyCustomerVO.setOldINomSurName(oldCustVO.getIpNomSurName());
				modifyCustomerVO.setModifiedRecord(1);
				
			}
			
			
			
			// Insured MSISDN
			if (!newCustVO.getIpInsMsisdn().trim().equals(oldCustVO.getIpInsMsisdn())) {
				modifyCustomerVO.setNewIpInsMsisdn(newCustVO.getIpInsMsisdn());
				modifyCustomerVO.setOldIpInsMsisdn(oldCustVO.getIpInsMsisdn());
				modifyCustomerVO.setModifiedRecord(1);
				modifyCustomerVO.setIsModifiedInsIPMsisdn(1);				
				
			}
			
		}	
			
		
				
		
		

		logger.exiting("getNewOldCustomerDetails", modifyCustomerVO);
		return modifyCustomerVO;

	}

	public void insertIntoCustomerAuditTable(ModifyCustomerVO modifyCustomerVO) throws Exception {
		logger.entering("insertIntoCustomerAuditTable", modifyCustomerVO);
		customerManager.insertIntoCustomerAuditTable(modifyCustomerVO);
		logger.exiting("insertIntoCustomerAuditTable");
		}

	public String  getProductDeactivedDetails(String custId) throws Exception {
		logger.entering("getProductDeactivedDetails", custId);
		String deactivatedProductId=customerManager.getProductDeactivedDetails(custId);
		logger.exiting("getProductDeactivedDetails",deactivatedProductId);
		return deactivatedProductId;
		
	}

	public String  customerReactivation(CustomerVO custVO, UserDetails userDetails)
			throws MISPException {
		logger.entering("customerReactivation", custVO);

		AdminConfigVO adminConfigVO = loginService.getConfigDetails();
		String URL = adminConfigVO.getReactivationCustomerWSURL();

		ReactivationCustomerRequest reactivationCustomerRequest = new ReactivationCustomerRequest();
		reactivationCustomerRequest.setCustId(Integer.parseInt(custVO
				.getCustId()));
		reactivationCustomerRequest.setMsisdn(custVO.getMsisdn());

		// customer deatils
		reactivationCustomerRequest.setFirstName(custVO.getFname());
		reactivationCustomerRequest.setLastName(custVO.getSname());
		reactivationCustomerRequest.setDateOfBirth(StringUtil.isEmpty(custVO.getDob())?null:DateUtil.toSQLDate(custVO.getDob()).toString());
		reactivationCustomerRequest.setAge(Integer.parseInt(custVO.getAge()));
		reactivationCustomerRequest.setGender(custVO.getGender());

		
		
		if (custVO.getProductId().contains("2")) {
			reactivationCustomerRequest.setInsRelFirstName(custVO.getInsRelFname());
			reactivationCustomerRequest.setInsRelLastName(custVO.getInsRelSurname());
			reactivationCustomerRequest.setInsRelDateOfBirth(StringUtil.isEmpty(custVO.getInsRelIrDoB())?null: DateUtil.toSQLDate(custVO.getInsRelIrDoB()).toString());
			reactivationCustomerRequest.setInsRelAge(Integer.parseInt(custVO.getInsRelAge()));
			reactivationCustomerRequest.setInsRelMsisdn(custVO.getInsMsisdn());
			reactivationCustomerRequest.setInsRelRelationship(custVO.getInsRelation());

		}

		if (custVO.getProductId().contains("4")) {
			reactivationCustomerRequest.setIpNomFirstName(custVO.getIpNomFirstName());
			reactivationCustomerRequest.setIpNomSurName(custVO.getIpNomSurName());
			reactivationCustomerRequest.setIpNomAge(Integer.parseInt(custVO.getIpNomAge()));
			reactivationCustomerRequest.setIpNomMsisdn(custVO.getIpInsMsisdn());
			reactivationCustomerRequest.setProductCoverId(Integer.parseInt(custVO.getProductCoverIdIP()));
		}
		reactivationCustomerRequest.setDedcutionType(Integer.parseInt(custVO.getDeductionMode()));
		reactivationCustomerRequest.setRegisteredBy(userDetails.getUserId());
		reactivationCustomerRequest.setRegistrationChannelId(1);
		
		if(custVO.getProductId().contains("2"))
			custVO.setProductId(custVO.getProductId().replace("2", "1,2"));
		
		reactivationCustomerRequest.setProductsSelected(custVO.getProductId());
		reactivationCustomerRequest.setIsCustExist(Integer.parseInt(custVO.getIsCustExist()));
				
		 boolean result=customerWSManager.invokeReactivationCustWS(reactivationCustomerRequest
		 , URL);
		 
		 String resultCode="false,false";
		 if(result)
			 resultCode="true,true";
		logger.exiting("customerReactivation",resultCode);
		return resultCode;

	}

	public CustomerVO checkCustomerExistInBimaProductCancellation(String msisdn) throws DBException {
		logger.entering("checkCustomerExistInBimaProductCancellation", msisdn);
		
		CustomerVO customerVO=customerManager.checkCustomerExistInBimaProductCancellation(msisdn);
		logger.exiting("checkCustomerExistInBimaProductCancellation",customerVO);
		return customerVO;
	}

	public String getMsisdnCodes() throws Exception {
		logger.entering("getMsisdnCodes");
		AdminConfigVO adminConfigVO = loginService.getConfigDetails();
		String msisdnCodes = adminConfigVO.getInsMsisdnCode();
		logger.exiting("getMsisdnCodes");
		return msisdnCodes;
	}

	public String  checkInProductCancellation(String custId,String productReg) throws DBException {
		logger.entering("checkInProductCancellation", custId,productReg);
		String prodcutList=customerManager.checkInProductCancellation(custId,productReg);
		logger.exiting("checkInProductCancellation",prodcutList);
		return prodcutList;
		
	}

	public void sendSMStoNominee(CustomerVO custVO,String productId) throws Exception {
		logger.entering("sendSMStoNominee", custVO,productId);
		customerManager.sendSMStoNominee(custVO,productId);
		logger.exiting("sendSMStoNominee");
		
	}

	public List<LoyaltyCustomerVO> getLoyalCustomers(CustomerVO custVO, UserDetails user) throws MISPException  {
		
		logger.entering("getLoyalCustomers: ", custVO, user);

		List<LoyaltyCustomerVO> custVOList = null;

		try {
			custVOList = customerManager.getLoyalCustomers(custVO);

		}catch (DBException exception) {
			logger.error(
					"An exception occured while getting loyal customer Details.",
					exception);
			throw new MISPException(exception);
		}
		logger.debug("Number of customers matching the search criteria :",
				custVOList.size());
		logger.exiting("getLoyalCustomers");

		return custVOList;
	}

	/**
	 * @param custVO
	 * @param oldLoyaltyPack
	 * @param newLoyaltyPack
	 * @param userId
	 * @return
	 * @throws MISPException
	 */
	public int applyLoyaltyPackage(CustomerVO custVO,int oldLoyaltyPack,int newLoyaltyPack, int userId) throws MISPException {
		
		logger.entering("applyLoyaltyPackage" ,custVO ," UserId :" , userId);
		
		int resultCodeFromWs=-1;
		
		AdminConfigVO adminConfigVO = loginService.getConfigDetails();
		
		String URL = adminConfigVO.getLoyaltyPackWSURL();
		
		LoyaltyPackRequest loyaltyPackRequest = null;
		
		loyaltyPackRequest = new LoyaltyPackRequest();
		loyaltyPackRequest.setCustId(Integer.parseInt(custVO.getCustId()));
		loyaltyPackRequest.setMsisdn(custVO.getMsisdn());
		loyaltyPackRequest.setOfferCoverId(0);
		loyaltyPackRequest.setOfferId(Integer.parseInt(custVO.getProductId()));
		loyaltyPackRequest.setSnId(custVO.getSnId());
		loyaltyPackRequest.setNewLoyaltyPack(newLoyaltyPack);
		loyaltyPackRequest.setOldLoyaltyPack(oldLoyaltyPack);
		loyaltyPackRequest.setUserId(userId);
		loyaltyPackRequest.setLcId(custVO.getLcId());
			
		try {
			
			resultCodeFromWs=customerWSManager.invokeLoyaltyVoicepackServiceWS(loyaltyPackRequest, userId, URL);
		} catch (WSException e) {
			
			logger.error("Exception occured while calling apply loyalty Web service", e );
		}
		
		logger.exiting("applyLoyaltyPackage" ,resultCodeFromWs);
		
		return resultCodeFromWs;
		
	}

	public LoyaltyCustomerVO getLoyaltyInformation(CustomerVO custVO) throws MISPException {
		
		logger.entering("getLoyaltyInformation: ", custVO);

		LoyaltyCustomerVO loyaltyInfo = null;

		try {
			loyaltyInfo = customerManager.getLoyaltyInformation(custVO);

		}catch (DBException exception) {
			logger.error(
					"An exception occured while getting loyal customer Details.",
					exception);
			throw new MISPException(exception);
		}
		logger.debug("Number of customers matching the search criteria :",
				loyaltyInfo);
		logger.exiting("getLoyaltyInformation ");

		return loyaltyInfo;
	}
	
	public boolean updatePackage(CustomerVO custVO) throws MISPException {
		
		logger.entering("updatePackage: ", custVO);

		
		boolean result = false;

		try {
			result = customerManager.updatePackage(custVO);

		}catch (DBException exception) {
			logger.error(
					"An exception occured while updating package .",
					exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("updatePackage ",result);

		return result;
	}

	/**
	 * @param custVO
	 * @param userId
	 * @param statusCode
	 * @param responseCode
	 * @return
	 * @throws MISPException
	 */
	public boolean updateLoyaltyInfo(CustomerVO custVO,int userId,String statusCode,int responseCode) throws MISPException {
		
		logger.entering("updateLoyaltyInfo: ", custVO,userId,statusCode);

		boolean resultCode = false;

		try {
			resultCode = customerManager.updateLoyaltyInfo(custVO,userId,statusCode,responseCode);

		}catch (DBException exception) {
			logger.error(
					"An exception occured while updating loyal customer Details.",
					exception);
			throw new MISPException(exception);
		}
		logger.exiting("updateLoyaltyInfo",resultCode);

		return resultCode;
		
	}
	
	public List<CustomerVO> getDeregCustdetails(String custId,String offerId,String custName)
			throws MISPException {
		Object[] params = { custId,offerId };
		logger.entering("getDeregCustdetails", params);

		List<CustomerVO> deRegCustDetails = null;

		try {
			deRegCustDetails = this.customerSubsManager
					.getDeregCustdetails(custId,offerId,custName);
		} catch (DBException dbException) {
			logger.error(
					"An error occurred while getting customer subscription details",
					dbException);
			throw new MISPException(dbException);
		}

		logger.exiting("getDeregCustdetails", deRegCustDetails);
		return deRegCustDetails;
	}

	


	




}
