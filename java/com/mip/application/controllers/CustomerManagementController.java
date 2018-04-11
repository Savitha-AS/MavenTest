package com.mip.application.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.ReportKeys;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.CustomerSubscription;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.ProductDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.services.CustomerService;
import com.mip.application.view.CustomerVO;
import com.mip.application.view.LoyaltyCustomerVO;
import com.mip.application.view.ModifyCustomerVO;
import com.mip.application.view.ProductCoverDetailsVO;
import com.mip.application.view.mappings.CustomerM2VMappings;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.StringUtil;
import com.mip.framework.utils.TypeUtil;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 28/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>CustomerManagementController.java</code> contains all the methods
 * pertaining to Customer Management use case model. This controller extends the
 * <code>BasePlatformController</code> which intern extends
 * <code>MultiActionController</code> class of spring framework.
 * </p>
 * 
 * @see com.mip.framework.controllers.BasePlatformController
 * @see org.springframework.web.servlet.mvc.multiaction.MultiActionController
 * o
 * @author T H B S
 * 
 */
public class CustomerManagementController extends BasePlatformController {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomerManagementController.class);

	/**
	 * An instance of session.
	 */
	HttpSession session = null;

	private UserDetails userDetails = null;

	/**
	 * Default constructor for Controller class
	 */
	public CustomerManagementController() {
	}

	/**
	 * Set inversion of control for <code>CustomerService</code>
	 */
	private CustomerService customerService;

	/**
	 * This is a setter method for creating dependency injection for
	 * <code>CustomerService</code>
	 * 
	 * @param customerService
	 *            - An instance of CustomerService class
	 */
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * This method controls the launching of Customer Registration page
	 * 
	 * @param request
	 *            - An instance of HttpServletRequest Object
	 * @param response
	 *            - An instance of HttpServletResponse Object
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView loadRegisterCustomer(HttpServletRequest request,
			HttpServletResponse response) {

		logger.entering("loadRegisterCustomer");

		List<String> relationshipList = null;

		List<ProductDetails> productList = null;

		ModelAndView mavObj = null;

		try {
			relationshipList = customerService.getRelationshipTypes();

			productList = customerService.retrieveOfferNamesAndIds();

			mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_REGISTER);

			mavObj.addObject(MAVObjects.LIST_RELATIONSHIP_TYPES,
					relationshipList);
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, productList);
		} catch (MISPException mispException) {
			logger.error(FaultMessages.REG_CUS_PAGE_LOADING_FAILED,
					mispException);

			mavObj = super.error(FaultMessages.REG_CUS_PAGE_LOADING_FAILED,
					MAVPaths.URL_REGISTER_CUSTOMER);
		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("loadRegisterCustomer", mavObj);

		return mavObj;
	}

	/**
	 * This method controls the flow for Registering the Customer
	 * 
	 * @param request
	 *            - An instance of HttpServletRequest Object
	 * @param response
	 *            - An instance of HttpServletResponse Object
	 * @param custObj
	 *            - CustomerVO object containing customer details
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView registerCustomer(HttpServletRequest request,
			HttpServletResponse response, CustomerVO custVO) {
		logger.entering("registerCustomer", custVO);

		ModelAndView mavObj = null;
		String resultCode = "";

		CustomerVO existCustomerDetails;
		CustomerSubscription customerSubscription=null;
		String custId=null;
		Set<InsuredRelativeDetails> insuredRelativesSet = null;
		InsuredRelativeDetails insuredRelativeDetails = null;
		List<InsuredRelativeDetails> insRelativeDetailsList=new ArrayList<InsuredRelativeDetails>();
		ModifyCustomerVO modifyCustomerVO =null;
		try {
			userDetails = (UserDetails) request.getSession().getAttribute(
					SessionKeys.SESSION_USER_DETAILS);
			
			if(custVO.getProductCoverIdIP()=="" || custVO.getProductCoverIdIP()==null)
			{
				custVO.setProductCoverIdIP(null);
			}
			
			//check the customer details are updated 
			custId=custVO.getCustId();

			
			if(!custId.equals("0"))
			{
				
				//check the reactivation customer 
				if(custVO.getIsReactive().equals("1"))
				{
					
					resultCode=customerService.customerReactivation(custVO,userDetails);
				}
				else
				{			
					
					customerSubscription = this.customerService
							.getCustomerSubscriptionDetails(Integer.parseInt(custId));
					
					insuredRelativesSet = customerSubscription.getCustomerDetails()
							.getInsuredRelatives();
					
					if (insuredRelativesSet != null) {
						for (InsuredRelativeDetails insDetails : insuredRelativesSet) {
							insuredRelativeDetails = insDetails;
							insRelativeDetailsList.add(insuredRelativeDetails);
						}
					}
					
					existCustomerDetails =  CustomerM2VMappings
							.mapCustModelToCustVOForModify(
									customerSubscription.getCustomerDetails(),
									insRelativeDetailsList, userDetails);	
					
					//compare the old value and new value
					modifyCustomerVO = customerService.getNewOldCustomerDetails(custVO,existCustomerDetails);
					
					
					//check for the the reactivation
					String deaativatedProductId=customerService.getProductDeactivedDetails(custVO.getCustId());
					
					String tempProudctList=custVO.getProductId().replace("2","1,2");
					
					
					String reqReactivateProduct=null;
					if(deaativatedProductId !=null )
					{					
						Set tempProudctListSet = new HashSet(Arrays.asList(tempProudctList.split(","))); 
						Set deaativatedProductIdSet = new HashSet(Arrays.asList(deaativatedProductId.split(","))); 
										
						Set<Integer> intersection = Sets.intersection(tempProudctListSet, deaativatedProductIdSet);
						reqReactivateProduct=Joiner.on(",").join(intersection);
						
					}				
							
					logger.info("Requested reactivation products-->", reqReactivateProduct);
					if(deaativatedProductId!=null && (reqReactivateProduct!=null && reqReactivateProduct.trim().length()>0))
					{
						//product requested for reactivation 
						logger.info("Customer comes for reactivation ");
						CustomerVO tempCustVO=new CustomerVO();
						tempCustVO=(CustomerVO)custVO.clone();  
						tempCustVO.setProductId(reqReactivateProduct);
						tempCustVO.setProductCoverIdIP(custVO.getProductCoverIdIP());
						
						resultCode=customerService.customerReactivation(tempCustVO,userDetails);
												
						
					}else if(custVO.getProductId().equals(custVO.getProductActiveRegister()))
					{
						custVO.setProductId(custVO.getProductRegistered());
					}
				}	
				
				
			}	
			
			if(custVO.getIsReactive().equals("0") )
			{
				resultCode = customerService.registerCustomer(custVO, userDetails);
			}
			if(modifyCustomerVO!=null && modifyCustomerVO.getModifiedRecord()==1)
			{
				modifyCustomerVO.setModifiedBy(String.valueOf(userDetails.getUserId()));
				modifyCustomerVO.setCustId(custId);
				modifyCustomerVO.setMsisdn(custVO.getMsisdn());
				customerService.insertIntoCustomerAuditTable(modifyCustomerVO);
				
				//Send SMS to nominee
				if(modifyCustomerVO.getIsModifiedInsMsisdn()==1)
					customerService.sendSMStoNominee(custVO, "1,2");
				
				if(modifyCustomerVO.getIsModifiedInsIPMsisdn()==1)
					customerService.sendSMStoNominee(custVO, "4");
			}			
			


			if (resultCode.equalsIgnoreCase("true,true")
					|| resultCode.equalsIgnoreCase("true,false")) {
				mavObj = loadRegisterCustomer(request, response);
				mavObj.addObject("message", SuccessMessages.CUSTOMER_REGISTERED);
			} else if (resultCode.equalsIgnoreCase("false,true")) {
				mavObj = loadRegisterCustomer(request, response);
				mavObj.addObject("message", SuccessMessages.CUSTOMER_MODIFIED);
			} else if (resultCode.equalsIgnoreCase("false,false")) {
				mavObj = loadRegisterCustomer(request, response);
				mavObj.addObject("message",
						FaultMessages.FAILED_CUSTOMER_MODIFY);
			}
		} catch (MISPException mispException) {
			logger.error(FaultMessages.CUSTOMER_REG_FAILED, mispException);

			mavObj = super.error(FaultMessages.CUSTOMER_REG_FAILED,
					MAVPaths.URL_REGISTER_CUSTOMER);

		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("registerCustomer", mavObj);

		return mavObj;
	}

	/**
	 * This method controls the flow for Modifying the customer
	 * 
	 * @param request
	 *            An instance of HttpServetRequest Object
	 * @param response
	 *            An instance of HttpServletResponse Object
	 * @param custObj
	 *            CustomerVO Object containing customer details
	 * @return <code>ModelAndView</code> Returns the ModelAndView object
	 * @throws <code>MISPException</code> In case of exception throws
	 *         ServerException
	 */
	public ModelAndView modifyCustomerDetails(HttpServletRequest request,
			HttpServletResponse response, CustomerVO custVO) {

		logger.entering("modifyCustomerDetails", custVO);

		boolean isModified = false;
		ModelAndView mavObj = null;

		try {

			session = request.getSession(false);
			userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);

			isModified = customerService.modifyCustomerDetails(custVO,
					userDetails);

			if (isModified) {
				mavObj = super.success(SuccessMessages.CUSTOMER_MODIFIED,
						MAVPaths.JSP_CUSTOMER_SEARCH);
			}

		} catch (MISPException mispException) {
			logger.error(FaultMessages.CUSTOMER_MODIFY_EXCEPTION
					+ mispException);

			mavObj = super.error(FaultMessages.CUSTOMER_MODIFY_EXCEPTION,
					MAVPaths.JSP_CUSTOMER_SEARCH);
		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);

		}

		logger.exiting("modifyCustomerDetails");

		return mavObj;

	}

	/**
	 * This method controls the flow for Getting Customer Details
	 * 
	 * @param request
	 *            An instance of HttpServletRequest Object
	 * @param response
	 *            An instance of HttpServletResponse Object
	 * @param custObj
	 *            CustomerVO Object containing customer details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	
	public ModelAndView getCustomerDetails(HttpServletRequest request,
			HttpServletResponse response, CustomerVO custObj) {
		logger.entering("getCustomerDetails", custObj);

		ModelAndView mavObj = null;
		CustomerVO customerDetails = null;
		List<CustomerVO> deRegCustDetails = null;
		List<String> relationshipList = null;
		List<ProductDetails> productsList = null;
		List<ProductCoverDetailsVO> productCoverList = null;
		List<InsuredRelativeDetails> insRelativeDetailsList=new ArrayList<InsuredRelativeDetails>();

		CustomerSubscription customerSubscription = null;
		Set<InsuredRelativeDetails> insuredRelativesSet = null;
		InsuredRelativeDetails insuredRelativeDetails = null;
		CustomerVO custVo = null;
		List<ModifyCustomerVO> summaryDetailsChangesList = null;
		
		// To check whether the offer details are modifiable.
		boolean is_offer_modifiable = true;
		boolean is_deregisteredcust = true;
		String custId = "";
		String offerId = "";
		String custName = "";
		try {
			session = request.getSession();
			userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);

			relationshipList = customerService.getRelationshipTypes();
			productsList = customerService.retrieveOfferNamesAndIds();
			productCoverList = customerService.retrieveOfferCoverDetailsIPProduct();
			
			if (!StringUtil.isEmpty(String.valueOf(custObj.getCustId())))
				custId = String.valueOf(custObj.getCustId());
				offerId = String.valueOf(custObj.getOfferId());
				custName = String.valueOf(custObj.getCustName());
				
			// Gets customer subscription details for the customer
			customerSubscription = this.customerService
					.getCustomerSubscriptionDetails(Integer.parseInt(custId));

			if (custObj.getStatus().equalsIgnoreCase("De-registered")) {
			
				/*customerDetails = customerService.getCustomerDetails(custId,
						userDetails);*/
				
				deRegCustDetails = customerService.getDeregCustdetails(custId,offerId,custName);
				
				CustomerVO customerDetails1 = new CustomerVO();;
				
				customerDetails1.setFname(deRegCustDetails.get(0).getFname());
				customerDetails1.setSname(deRegCustDetails.get(0).getSname());
				customerDetails1.setMsisdn(deRegCustDetails.get(0).getMsisdn());
				customerDetails1.setDob(deRegCustDetails.get(0).getDob());
				customerDetails1.setAge(deRegCustDetails.get(0).getAge());
				customerDetails1.setImpliedAge(deRegCustDetails.get(0).getImpliedAge());
				customerDetails1.setGender(deRegCustDetails.get(0).getGender());
				customerDetails1.setInsRelation(deRegCustDetails.get(0).getInsRelation());
				customerDetails1.setInsRelFname(deRegCustDetails.get(0).getInsRelFname());
				customerDetails1.setInsRelSurname(deRegCustDetails.get(0).getInsRelSurname());
				customerDetails1.setInsRelIrDoB(deRegCustDetails.get(0).getInsRelation());
				customerDetails1.setInsRelAge(deRegCustDetails.get(0).getInsRelAge());
				customerDetails1.setInsMsisdn(deRegCustDetails.get(0).getInsMsisdn());
				customerDetails1.setDeductionMode("1");
				customerDetails1.setOfferName(deRegCustDetails.get(0).getOfferName());
				customerDetails1.setProductId(offerId);
				customerDetails1.setProductCoverIdIP(deRegCustDetails.get(0).getProductCoverIdIP());
				customerDetails1.setProductCover(deRegCustDetails.get(0).getProductCover());
				customerDetails1.setIpNomFirstName(deRegCustDetails.get(0).getIpNomFirstName());
				customerDetails1.setIpNomSurName(deRegCustDetails.get(0).getIpNomSurName());
				customerDetails1.setIpInsMsisdn(deRegCustDetails.get(0).getIpInsMsisdn());
				customerDetails1.setIpNomAge(deRegCustDetails.get(0).getIpNomAge());
				
				summaryDetailsChangesList = customerService.getSummaryDetailsChange(custId);
				
				if (null != deRegCustDetails) {

					boolean is_editable = false;
					is_deregisteredcust = true;

					mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_MODIFY);
					mavObj.addObject(MAVObjects.LIST_RELATIONSHIP_TYPES,
							relationshipList);
					mavObj.addObject(MAVObjects.VO_CUSTOMER, customerDetails1);
					mavObj.addObject(MAVObjects.LIST_PRODUCTS, productsList);
					mavObj.addObject(MAVObjects.LIST_COVER, productCoverList);
					mavObj.addObject(MAVObjects.IS_EDITABLE, is_editable);
					mavObj.addObject(MAVObjects.IS_DEREGISTEREDCUST,is_deregisteredcust);
					mavObj.addObject(MAVObjects.IS_OFFER_MODIFIABLE,
							is_offer_modifiable);
					mavObj.addObject(MAVObjects.LIST_SUMMARY_DETAILS_CHANGES,
							summaryDetailsChangesList);
				}
				
				
			} else {
				
				if(!(custObj.getStatus().equalsIgnoreCase("De-registered"))){
					is_deregisteredcust = false;
				}
				
				insuredRelativesSet = customerSubscription.getCustomerDetails()
						.getInsuredRelatives();

				if (insuredRelativesSet != null) {
					for (InsuredRelativeDetails insDetails : insuredRelativesSet) {
						insuredRelativeDetails = insDetails;
						insRelativeDetailsList.add(insuredRelativeDetails);
					}
				}

				summaryDetailsChangesList = customerService.getSummaryDetailsChange(custId);

				/**
				 * Map the retrieved customer details from customer model to
				 * customer view object to display in GUI.
				 */
				customerDetails = CustomerM2VMappings
						.mapCustModelToCustVOForModify(
								customerSubscription.getCustomerDetails(),
								insRelativeDetailsList, userDetails);				
				if (null != customerDetails) {
					
					boolean is_editable = false;
					custVo = customerService.getOfferSubscriptionFromCustId(Integer.parseInt(customerDetails.getCustId()));
					
					customerDetails.setProductId(custVo.getPurchasedProducts());
					customerDetails.setProductCoverIdIP(custVo.getProductCoverIdIP());
					customerDetails.setOfferName(custObj.getOfferName());
					customerDetails.setProductId(offerId);
					
					mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_MODIFY);
					mavObj.addObject(MAVObjects.LIST_RELATIONSHIP_TYPES,
							relationshipList);
					mavObj.addObject(MAVObjects.VO_CUSTOMER, customerDetails);
					mavObj.addObject(MAVObjects.LIST_PRODUCTS, productsList);
					mavObj.addObject(MAVObjects.LIST_COVER, productCoverList);
					mavObj.addObject(MAVObjects.IS_EDITABLE, is_editable);
					mavObj.addObject(MAVObjects.IS_DEREGISTEREDCUST,is_deregisteredcust);
					mavObj.addObject(MAVObjects.IS_OFFER_MODIFIABLE,
							is_offer_modifiable);
					mavObj.addObject(MAVObjects.LIST_SUMMARY_DETAILS_CHANGES, summaryDetailsChangesList);
				}
			}
		} catch (MISPException mispException) {
			logger.error(FaultMessages.CUSTOMER_FETCH_FAILED + mispException);
			mavObj = super.error(FaultMessages.CUSTOMER_FETCH_FAILED,
					MAVPaths.URL_SEARCH_CUSTOMER);
		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);

		}
		logger.exiting("getCustomerDetails", mavObj);
		return mavObj;
	}
	
	/**
	 * This method controls the flow for Getting De-Registered Customer Details
	 * 
	 * @param request
	 *            An instance of HttpServletRequest Object
	 * @param response
	 *            An instance of HttpServletResponse Object
	 * @param custObj
	 *            CustomerVO Object containing customer details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	
	
	public ModelAndView getDeregCustomerDetails(HttpServletRequest request,
			HttpServletResponse response, CustomerVO custObj) {
		logger.entering("getDeregCustomerDetails", custObj);

		ModelAndView mavObj = null;
		CustomerVO customerDetails = null;
		List<String> relationshipList = null;
		List<ProductDetails> productsList = null;
		List<ProductCoverDetailsVO> productCoverList = null;
		
		
		CustomerVO custVo=null;
		boolean is_offer_modifiable = false;
		String custId = "";
		try {
			session = request.getSession();
			userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);

			relationshipList = customerService.getRelationshipTypes();
			productsList = customerService.retrieveOfferNamesAndIds();
			productCoverList = customerService.retrieveOfferCoverDetailsIPProduct();
			
			if (!StringUtil.isEmpty(String.valueOf(custObj.getCustId())))
				custId = String.valueOf(custObj.getCustId());
			
				customerDetails = customerService.getDeregisteredCustomerDetails(custId,
						userDetails);
				if (null != customerDetails) {
					
					boolean is_editable = false;
					custVo = customerService.getDeregOfferSubscriptionFromCustId(custId);
					
					customerDetails.setProductId(custVo.getPurchasedProducts());
					customerDetails.setProductCoverIdIP(custVo.getProductCoverIdIP());
					
					mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_DEACTIVE);
					mavObj.addObject(MAVObjects.LIST_RELATIONSHIP_TYPES,
							relationshipList);
					mavObj.addObject(MAVObjects.VO_CUSTOMER, customerDetails);
					mavObj.addObject(MAVObjects.LIST_PRODUCTS, productsList);
					mavObj.addObject(MAVObjects.LIST_COVER, productCoverList);
					mavObj.addObject(MAVObjects.IS_EDITABLE, is_editable);
					mavObj.addObject(MAVObjects.IS_OFFER_MODIFIABLE,
							is_offer_modifiable);
				}
			
		} catch (MISPException mispException) {
			logger.error(FaultMessages.CUSTOMER_FETCH_FAILED + mispException);
			mavObj = super.error(FaultMessages.CUSTOMER_FETCH_FAILED,
					MAVPaths.URL_SEARCH_CUSTOMER);
		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);

		}
		logger.exiting("getDeregCustomerDetails", mavObj);
		return mavObj;
	}
	

	/**
	 * This method controls the flow for searching the customer
	 * 
	 * @param request
	 *            An instance of HttpServletRequest Object
	 * @param response
	 *            An instance of HttpServletResponse Object
	 * @param custObj
	 *            CustomerVO Object containing customer details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	
	public ModelAndView searchCustomerDetails(HttpServletRequest request,
			HttpServletResponse response, CustomerVO custObj) {
		logger.entering("searchCustomerDetails", custObj);
		ModelAndView mavObj = null;
		boolean isSearchCriteriaReady = false;
		String products = "";

		try {
			session = request.getSession();
			userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);

			if (null != userDetails && null != userDetails.getUserUid())
				logger.info("Search Customer attempted by: ",
						userDetails.getUserUid());

			HashMap<String, String> searchMap = new HashMap<String, String>();

			if (!StringUtil.isEmpty(custObj.getFname())){
				searchMap.put("fname", StringUtil.trim(custObj.getFname()));
				isSearchCriteriaReady = true;
			}
				

			if (!StringUtil.isEmpty(custObj.getSname())){
				searchMap.put("sname", StringUtil.trim(custObj.getSname()));
				isSearchCriteriaReady = true;
			}
				

			if (!StringUtil.isEmpty(custObj.getMsisdn())) {
				searchMap.put("msisdn", StringUtil.trim(custObj.getMsisdn()));
				isSearchCriteriaReady = true;
			}
			if (!StringUtil.isEmpty(custObj.getCustId())){
				searchMap.put("custId", StringUtil.trim(custObj.getCustId()));
				isSearchCriteriaReady = true;
			}
				

			ArrayList<CustomerVO> customerDetailsList = new ArrayList<CustomerVO>();

			if (isSearchCriteriaReady)
				customerDetailsList = customerService.searchCustomerDetails(
						searchMap, userDetails);

			ModelMap modelMap = new ModelMap();

			modelMap.addAttribute(MAVObjects.LIST_CUST_DET, customerDetailsList);

			if (customerDetailsList != null) {
				mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_LIST);

				mavObj.addObject(MAVObjects.LIST_CUSTOMER_DETAILS,
						customerDetailsList);
			}
		} catch (MISPException mispException) {
			logger.error(FaultMessages.CUSTOMER_SEARCH_FAILED + mispException);
			mavObj = super.error(FaultMessages.CUSTOMER_SEARCH_FAILED,
					MAVPaths.JSP_CUSTOMER_SEARCH);
		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("searchCustomerDetails");
		return mavObj;
	}

	/**
	 * This method handles the request and response for downloading E-Signature.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView downloadSignature(HttpServletRequest request,
			HttpServletResponse response) {
		logger.entering("downloadSignature");

		File reportDirectory = null;
		File eSignatureFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		String msisdn = request.getParameter("msisdn");

		try {

			// Creates Report File Name
			String reportFileName = msisdn + ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(request.getSession().getServletContext()
					.getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);

			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			eSignatureFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!eSignatureFile.exists()) {
				eSignatureFile.createNewFile();
			}

			// Writes report data to excel file
			workbook = new HSSFWorkbook();
			customerService.writeSignature(workbook, msisdn);

			fileOutStream = new FileOutputStream(eSignatureFile);
			workbook.write(fileOutStream);

			logger.debug("Data written to excel workbook");

			// Sets the report file for download
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ reportFileName);
			servletOutStream = response.getOutputStream();
			fileInputStream = new FileInputStream(eSignatureFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}

			servletOutStream.flush();

		} catch (IOException e) {
			logger.error("Exception occured while downloading E-Signature");
			return null;
		} catch (MISPException e) {

			logger.error("Exception occured while downloading E-Signature");
			return null;
		} catch (Exception e) {

			logger.error("Exception occured while downloading E-Signature", e);
			return null;
		} finally {
			// Closes all resources
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}

				if (fileInputStream != null) {
					fileInputStream.close();

				}
				// deletes the report file
				if (eSignatureFile != null && eSignatureFile.exists()) {
					eSignatureFile.delete();
				}
			} catch (IOException e) {
				logger.error("Exception occured while closing "
						+ "resources after E-Signature download");
				return null;
			}
		}

		logger.exiting("downloadSignature");
		return null;
	}

	public ModelAndView checkCustomerExists(HttpServletRequest request,
			HttpServletResponse response, CustomerVO custVO) {

		logger.entering("checkCustomerExists", custVO);

		ModelAndView mavObj = null;
		CustomerVO customerVO = new CustomerVO();
		CustomerVO customerVODereg = new CustomerVO();
		List<String> relationshipList = null;
		List<ProductDetails> productsList = null;
		List<ProductCoverDetailsVO> productsCoverListIP = null;
		boolean is_editable = false, is_hpCust = false, custexists = false, is_xlCust = false,is_ipCust=false;

		boolean is_DeductionMode = false;
		boolean isIPReg = false;
        String reactivationMsg=null;
        String reactivationProductList=null;
        String msisdnCodes=null;
        boolean ipDeactivated=false;
        String custEXistInBimaCancellationMsg=null;
        boolean isIPReactive=false;
        boolean isXLreactive=false;
        boolean isHPreactive=false;
        
		
		try {
			relationshipList = customerService.getRelationshipTypes();

			productsList = customerService.retrieveOfferNamesAndIds();
			productsCoverListIP = customerService.retrieveOfferCoverDetailsIP();
			session = request.getSession();
			userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);
			customerVO = customerService
					.checkCustomerExists(custVO.getMsisdn());
			
			msisdnCodes = customerService.getMsisdnCodes();
			isIPReg = userDetails.getRoleMaster().getIsIPReg() == 1 ? true
					: false;
		
			
			
			if (customerVO.getCustId().equalsIgnoreCase("0")) {
				custexists = false;
				is_editable = true;
				
				customerVO=customerService.checkCustomerExistInBimaProductCancellation(custVO.getMsisdn());
				customerVO.setIsReactive("0");
				
				if(!customerVO.getCustId().equalsIgnoreCase("0"))
				{
					customerVO.setIsReactive("1");
					customerVO.setIsCustExist("0");
									
					String prodcutList=customerService.checkInProductCancellation(customerVO.getCustId(),"0");
					custEXistInBimaCancellationMsg=PlatformConstants.CUST_EXIST_BIMA_CANCELLATION_MSG;
					
					prodcutList=prodcutList.replace("1,2", PlatformConstants.XL_PRODUCT);
					prodcutList=prodcutList.replace("3", PlatformConstants.HP_PRODUCT);
					prodcutList=prodcutList.replace("4", PlatformConstants.IP_PRODUCT);
					
					custEXistInBimaCancellationMsg=custEXistInBimaCancellationMsg.replace("<1>", prodcutList);
										
				}
				customerVO.setMsisdn(custVO.getMsisdn());
				
						
				// check access for deduction mode
				if (PlatformConstants.AGENT_ACCESS_DEDUCTION_MODE_LIST
						.contains(userDetails.getRoleMaster()
								.getRoleDescription())) {
					is_DeductionMode = true;
				}
				

			} else {
				custexists = true;
				String[] productsLit = customerVO.getPurchasedProducts().split(",");
				String[] deactivatedProductsLit = customerVO.getDeactivatedProducts().split(",");
				String[] productListForReactivation=null;
				
				Map<String, String > productList=new HashMap<String , String>();
				
				customerVO.setIsReactive("0");
				customerVO.setIsCustExist("1");
				int i=0;
				for (String productIdDeactivationStatus : deactivatedProductsLit) {
					productList.put(productsLit[i], productIdDeactivationStatus);
										
					i++;				
					
				}			
				
				//check in the bima cancellation and product cancellation 
				String prodcutList=customerService.checkInProductCancellation(customerVO.getCustId(),customerVO.getPurchasedProducts());
				customerVO.setCustProductCancellation(prodcutList);
				
				customerVO.setProductRegistered(customerVO.getPurchasedProducts());
				

				if (customerVO.getPurchasedProducts().equalsIgnoreCase("3"))
				{				
					is_hpCust = true;
					if(productList.get("3").equals("1"))
					{
						
						reactivationProductList=reactivationProductList == null ?PlatformConstants.HP_PRODUCT:","+PlatformConstants.HP_PRODUCT;
						custexists = false;					
						customerVO.setPurchasedProducts("");
					}
								
				}

				//Registered  for IP
				if (customerVO.getPurchasedProducts().equalsIgnoreCase("4")){
					is_ipCust = true;
					
					if(productList.get("4").equals("1"))
					{
						
						reactivationProductList=reactivationProductList == null ?PlatformConstants.IP_PRODUCT:","+PlatformConstants.IP_PRODUCT;
						custexists = false;		
						ipDeactivated=true;
					}
					if(custexists == false )
					{
						customerVO.setPurchasedProducts("");
											
					}	
					
				}
				if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,3"))
					is_hpCust = true;

				if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,2"))
				{
					
					is_xlCust = true;
					
					if(productList.get("2").equals("1"))
					{
						
						reactivationProductList=reactivationProductList == null ?PlatformConstants.XL_PRODUCT:","+PlatformConstants.XL_PRODUCT;
						custexists = false;					
						customerVO.setPurchasedProducts("");
					}
				}
					

				if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,2,3")) {
					is_xlCust = true;
					is_hpCust = true;
					
					
					
					if(productList.get("3").equals("1"))
					{
							
						reactivationProductList=reactivationProductList == null ?PlatformConstants.HP_PRODUCT:","+PlatformConstants.HP_PRODUCT;
						isHPreactive=true;						
					}					
					if(productList.get("2").equals("1"))
					{
						
						reactivationProductList=reactivationProductList == null ?PlatformConstants.XL_PRODUCT:reactivationProductList+","+PlatformConstants.XL_PRODUCT;
						isXLreactive=true;	
					}			
					
					
					if(productList.get("3").equals("1") && productList.get("2").equals("1") )
						customerVO.setPurchasedProducts("");
					else if(productList.get("2").equals("1"))
						customerVO.setPurchasedProducts("3");
					else if(productList.get("3").equals("1"))
						customerVO.setPurchasedProducts("1,2");
										
					
				}
				//Registered  for xtra life and IP
				if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,2,4")) {
					is_xlCust = true;
					is_ipCust = true;					
					
					
					if(productList.get("4").equals("1"))
					{
							
						reactivationProductList=reactivationProductList == null ?PlatformConstants.IP_PRODUCT:","+PlatformConstants.IP_PRODUCT;
						is_ipCust = false;		
						ipDeactivated=true;
						isIPReactive=true;
					}					
					if(productList.get("2").equals("1"))
					{
						
						reactivationProductList=reactivationProductList == null ?PlatformConstants.XL_PRODUCT:reactivationProductList+","+PlatformConstants.XL_PRODUCT;
						isXLreactive=true;						
					}			
					
					
					if(productList.get("4").equals("1") && productList.get("2").equals("1") )
						customerVO.setPurchasedProducts("");
					else if(productList.get("2").equals("1"))
						customerVO.setPurchasedProducts("4");
					else if(productList.get("4").equals("1"))
						customerVO.setPurchasedProducts("1,2");
				
				}
				
				//Registered  for HP and IP
				if (customerVO.getPurchasedProducts().equalsIgnoreCase("3,4")) {
					is_ipCust = true;
					//is_hpCust = true;
					customerVO.setPurchasedProducts("4");
					if(productList.get("4").equals("1"))
					{
							
						reactivationProductList=reactivationProductList == null ?PlatformConstants.IP_PRODUCT:","+PlatformConstants.IP_PRODUCT;
						custexists = false	;
						customerVO.setPurchasedProducts("");
					}
					
					
					
				}
				
				//Registered  for Xtra life ,HP and IP
				if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,2,3,4")) {
					is_xlCust = true;
					is_hpCust = false;
					is_ipCust = true;
					customerVO.setPurchasedProducts("1,2,4");
					
					
					if(productList.get("4").equals("1"))
					{
							
						reactivationProductList=reactivationProductList == null ?PlatformConstants.IP_PRODUCT:","+PlatformConstants.IP_PRODUCT;
						is_ipCust = false;			
						ipDeactivated=true;
					}					
					if(productList.get("2").equals("1"))
					{
						
						reactivationProductList=reactivationProductList == null ?PlatformConstants.XL_PRODUCT:reactivationProductList+","+PlatformConstants.XL_PRODUCT;
						isXLreactive=true;	
					}			
										
					if(productList.get("4").equals("1") && productList.get("2").equals("1") )
						customerVO.setPurchasedProducts("");
					else if(productList.get("2").equals("1"))
						customerVO.setPurchasedProducts("4");
					else if(productList.get("4").equals("1"))
						customerVO.setPurchasedProducts("1,2");
									
					
				}
				
				
				// check access for deduction mode
				if (PlatformConstants.AGENT_ACCESS_DEDUCTION_MODE_LIST
						.contains(userDetails.getRoleMaster()
								.getRoleDescription())) {
					is_DeductionMode = true;
				}
				
				if (userDetails.getRoleMaster().getRoleName()
						.equalsIgnoreCase(PlatformConstants.ADMIN_ROLE)) {

					is_editable = true;
				} else {

					Calendar today = Calendar.getInstance();

					Calendar customerRegDate = Calendar.getInstance();

					customerRegDate.setTime(DateUtil.toDateTimeSQL(customerVO
							.getCreatedDate()));
					
					
					if (DateUtil.getDateDiff(today, customerRegDate) <= PlatformConstants.MIN_DIFF)
						is_editable = true;
					
					
				}
				
				customerVO.setProductActiveRegister(customerVO.getPurchasedProducts());
				
				

			}
			
			if(reactivationProductList!=null)
			{
				reactivationMsg=PlatformConstants.REACTIVATION_MSG;
				is_editable = true;
					
				String prodcutList="";
				if(!StringUtil.isEmpty(customerVO.getCustProductCancellation()))
				{
					prodcutList =customerVO.getCustProductCancellation();
					prodcutList=prodcutList.replace("1,2", PlatformConstants.XL_PRODUCT);
					prodcutList=prodcutList.replace("3", PlatformConstants.HP_PRODUCT);
					prodcutList=prodcutList.replace("4", PlatformConstants.IP_PRODUCT);
					
				}
				
				if(!StringUtil.isEmpty(prodcutList))
					prodcutList=","+prodcutList;
				
				reactivationMsg=reactivationMsg.replace("<1>", (reactivationProductList +" " + prodcutList));
								
			}else if(reactivationProductList==null && !customerVO.getCustId().equalsIgnoreCase("0")) 
			{
				
				if(!StringUtil.isEmpty(customerVO.getCustProductCancellation()))
				{
					reactivationMsg=PlatformConstants.REACTIVATION_MSG;
					String prodcutList =customerVO.getCustProductCancellation();
					prodcutList=prodcutList.replace("1,2", PlatformConstants.XL_PRODUCT);
					prodcutList=prodcutList.replace("3", PlatformConstants.HP_PRODUCT);
					prodcutList=prodcutList.replace("4", PlatformConstants.IP_PRODUCT);
					
					reactivationMsg=reactivationMsg.replace("<1>", prodcutList);
										
				}
			}
			logger.info("is_editable: ", is_editable, " ,is_xlCust:",is_xlCust, " ,is_hpCust: ",
					is_hpCust," ,is_ipCust: ",is_ipCust," ,isPAReg:",is_ipCust, " ,custexists: ", custexists);
			mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_REGISTER);
			mavObj.addObject(MAVObjects.LIST_RELATIONSHIP_TYPES,
					relationshipList);
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, productsList);
			mavObj.addObject(MAVObjects.VO_CUSTOMER, customerVO);
			mavObj.addObject(MAVObjects.IS_EDITABLE, is_editable);
			mavObj.addObject(MAVObjects.IS_HP_REG, is_hpCust);
			mavObj.addObject(MAVObjects.IS_XL_REG, is_xlCust);
			mavObj.addObject(MAVObjects.IS_IP_REG, is_ipCust);
			mavObj.addObject(MAVObjects.IS_CUSTEXIST, custexists);
			mavObj.addObject(MAVObjects.IS_DEDUCTION_MODE, is_DeductionMode);
			mavObj.addObject(MAVObjects.IS_PA_REG, isIPReg);
			mavObj.addObject(MAVObjects.LIST_OFFERCOVER_DETAILS,
					productsCoverListIP);
			mavObj.addObject(MAVObjects.REACTIVATION_MSG, reactivationMsg);
			mavObj.addObject(MAVObjects.MSISDN_CODES, msisdnCodes);
			mavObj.addObject(MAVObjects.IP_DEACTIVATED, ipDeactivated);
			mavObj.addObject(MAVObjects.CUST_EXIST_BIMA_CANCEL_MSG, custEXistInBimaCancellationMsg);
			mavObj.addObject(MAVObjects.IS_IP_REACTIVE, isIPReactive);
			mavObj.addObject(MAVObjects.IS_XL_REACTIVE, isXLreactive);
			mavObj.addObject(MAVObjects.IS_HP_REACTIVE, isHPreactive);
					
			
			
		} catch (MISPException mispException) {
			logger.error(FaultMessages.REG_CUS_PAGE_LOADING_FAILED,
					mispException);

			mavObj = super.error(FaultMessages.REG_CUS_PAGE_LOADING_FAILED,
					MAVPaths.URL_REGISTER_CUSTOMER);
		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);

		}

		logger.exiting("checkCustomerExists", mavObj);

		return mavObj;

	}

	/**
	 * This method launches the Customer De-Registration page.
	 * 
	 * @param request
	 *            - An instance of HttpServletRequest Object
	 * 
	 * @param response
	 *            - An instance of HttpServletResponse Object
	 * 
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView loadDeRegisterCustomer(HttpServletRequest request,
			HttpServletResponse response) {
		logger.entering("loadDeRegisterCustomer");

		ModelAndView mavObj = null;

		mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_DEREGISTER);

		logger.exiting("loadDeRegisterCustomer", mavObj);

		return mavObj;
	}

	public ModelAndView getCustProductsForDeReg(HttpServletRequest request,
			HttpServletResponse response, CustomerVO custVO) {

		logger.entering("getCustProductsForDeReg", custVO);

		ModelAndView mavObj = null;
		CustomerVO customerVO = new CustomerVO();
		String subscribedProductList = null;
		List<ProductDetails> productsList = null;
		boolean custexists = false;
		mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_DEREGISTER);
		List<CustomerVO> customerDetailsList = new ArrayList<CustomerVO>();
		List<CustomerVO> customerDeregDetailsList = null;
		String msisdn="";
		try {
			msisdn = custVO.getMsisdn();
			productsList = customerService.retrieveOfferNamesAndIds();
			session = request.getSession();
			userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);
			
			
			//Product list 
			customerVO = customerService.getCustProductsForDeReg(custVO
					.getMsisdn());
			
			subscribedProductList = customerVO.getPurchasedProducts();
			
			//information fetch from bima cancellation and product cancellation 
			customerDeregDetailsList = customerService
					.getCustomerDeregisteredDetails(msisdn);
			
	
			//get customer subscription details
			customerDetailsList = customerService
					.getcustomerDetailsForDereg((msisdn));
			
			customerDetailsList=customerDetailsList.isEmpty()?null:customerDetailsList;
			
			custexists = true;
			if(customerDeregDetailsList==null && customerDetailsList == null)
			{
				
				custexists = false;
				
				customerVO.setMsisdn(custVO.getMsisdn());
				mavObj.addObject("message",
						FaultMessages.DE_REGISTER_MSISDN_NOT_REGISTERED);
				
			}

			logger.info("custexists: ", custexists);
			customerVO.setMsisdn(msisdn);
			
			mavObj.addObject(MAVObjects.DEREGED_CUSTOMER_LIST,
					customerDeregDetailsList);
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, productsList);
			mavObj.addObject(MAVObjects.VO_CUSTOMER, customerVO);
			mavObj.addObject(MAVObjects.IS_CUSTEXIST, custexists);
			mavObj.addObject(MAVObjects.LIST_SUBSCRIBED_PRODUCTS,
					subscribedProductList);
			mavObj.addObject(MAVObjects.DEREG_CUSTOMER_LIST,
					customerDetailsList);
		} catch (MISPException mispException) {
			logger.error(FaultMessages.DEREG_CUS_PAGE_LOADING_FAILED,
					mispException);

			mavObj = super.error(FaultMessages.DEREG_CUS_PAGE_LOADING_FAILED,
					MAVPaths.URL_DEREGISTER_CUSTOMER);
		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);

		}

		logger.exiting("getCustProductsForDeReg");

		return mavObj;

	}

	/**
	 * This method controls the flow for Customer De-Registration.
	 * 
	 * @param request
	 *            - An instance of HttpServletRequest Object
	 * 
	 * @param response
	 *            - An instance of HttpServletResponse Object
	 * 
	 * @param custObj
	 *            - CustomerVO object containing customer details
	 * 
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView deRegisterCustomer(HttpServletRequest request,
			HttpServletResponse response, CustomerVO custVO) {
		logger.entering("deRegisterCustomer", custVO);

		ModelAndView mavObj = null;
		String deRegistrationResponse = "";
		try {
			userDetails = (UserDetails) request.getSession().getAttribute(
					SessionKeys.SESSION_USER_DETAILS);

			deRegistrationResponse = customerService.deRegisterCustomer(custVO,
					userDetails);

			if (deRegistrationResponse.length() > 0) {
				Object[] msgParams = { custVO.getMsisdn() };
				mavObj = super.success(deRegistrationResponse,
						TypeUtil.arrayToCsv(msgParams),
						MAVPaths.URL_DEREGISTER_CUSTOMER);

			}
		} catch (MISPException mispException) {
			logger.error(FaultMessages.CUSTOMER_DEREG_FAILED, mispException);

			mavObj = super.error(FaultMessages.CUSTOMER_DEREG_FAILED,
					MAVPaths.URL_DEREGISTER_CUSTOMER);

		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("deRegisterCustomer", mavObj);

		return mavObj;
	}

	// getDeductionMode

	public ModelAndView getDeductionMode(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, CustomerVO customerVO) {

		logger.entering("getDeductionMode", customerVO);

		String msisdn = "";
		ModelAndView mav = null;

		try {
			msisdn = customerVO.getMsisdn();
			List<CustomerVO> customerDetails = null;

			customerDetails = customerService.findCustomer(msisdn);

			mav = new ModelAndView(MAVPaths.VIEW_CHANGE_DEDUCTION_MODE);
			mav.addObject(MAVObjects.VO_CUSTOMER, customerDetails);

			/*
			 * mav.addObject(MAVObjects.CUST_OFFER_DETAILS, custOfferDetails);
			 * mav.addObject(MAVObjects.OFFER_COVER_LIST, offerCoverList);
			 * mav.addObject(MAVObjects.LIST_OFFERS, offersList);
			 */
		} catch (Exception mispException) {
			logger.error("Exception occurred while fetching the cover levels.",
					mispException);
			/*
			 * mav = super.error(
			 * "Exception occurred while fetching the cover levels.",
			 * MAVPaths.JSP_UPDATE_COVER_LEVEL);
			 */
			mav = super.error(
					"Exception occurred while fetching the Deduction Mode",
					MAVPaths.VIEW_CHANGE_DEDUCTION_MODE);
		} /*
		 * catch (Exception exception) {
		 * logger.error("Exception occurred while fetching the cover levels.",
		 * exception); }
		 */

		logger.exiting("getDeductionMode", mav);
		return mav;
	}

	public ModelAndView changeDeductionMode(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, CustomerVO customerVO) {

		logger.entering("changeDeductionMode", customerVO);

		ModelAndView mav = null;
		String successMsg = "";

		boolean isChangeDeductionMode = false;

		try {

			session = httpServletRequest.getSession();
			userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);

			isChangeDeductionMode = customerService.ChangeDeductionMode(
					customerVO, userDetails);

			if (isChangeDeductionMode) {
				mav = new ModelAndView(MAVPaths.VIEW_CHANGE_DEDUCTION_MODE);
				successMsg = SuccessMessages.CHANGE_DEDUCTION_MODE;
				mav.addObject("message", successMsg);

			} else {
				mav = new ModelAndView(MAVPaths.VIEW_CHANGE_DEDUCTION_MODE);
				successMsg = FaultMessages.CHANGE_DEDUCTION_MODE_FAILED;
				;
				mav.addObject("message", successMsg);
			}

		} catch (Exception mispException) {
			logger.error("Exception occurred while fetching deduction mode.",
					mispException);
			logger.error(FaultMessages.GENERIC_ERROR, mispException);

			mav = super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("changeDeductionMode", mav);
		return mav;
	}

	public ModelAndView getCoverHistory(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, CustomerVO customerVO) {
		logger.entering("getCoverHistory", customerVO);
		String msisdn = "";
		ModelAndView mav = null;
		try {
			msisdn = customerVO.getMsisdn();
			logger.info(" msisdn ", msisdn);
			List<CustomerVO> customerDetails = null;
			List<CustomerVO> customerDetailsXL = null;
			List<CustomerVO> customerDetailsHP = null;
			List<CustomerVO> customerDetailsIP = null;
			int gracePeriodXLFlag=-1;
			int gracePeriodHPFlag = -1;
			int gracePeriodIPFlag = -1;			
			
			customerDetails = customerService.coverHistory(msisdn);

			// geting xp customer
			customerDetailsXL = customerService
					.getXLCustomerDetails(customerDetails);

			// geting hp customer
			customerDetailsHP = customerService
					.getHPCustomerDetails(customerDetails);
			
			// getting ip customer
			customerDetailsIP = customerService
					.getIPCustomerDetails(customerDetails);
			
			//check gracePeriodForXL
			
			gracePeriodXLFlag=customerService.checkGracePeriod(msisdn,2);
			logger.info("gracePeriodXLFlag ", gracePeriodXLFlag);

			//check gracePeriodForHP
			gracePeriodHPFlag=customerService.checkGracePeriod(msisdn,3);
			logger.info("gracePeriodHPFlag ", gracePeriodHPFlag);
			
			//check gracePeriodForIP
			gracePeriodIPFlag=customerService.checkGracePeriod(msisdn,4);
			logger.info("gracePeriodIPFlag ", gracePeriodIPFlag);

			String noClaimBonusDate="";
			if(customerService.getNoClaimBonusDate(msisdn)==null){
				noClaimBonusDate="n/a";
			} else {
				DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
				noClaimBonusDate=df.format(((Date) customerService.getNoClaimBonusDate(msisdn)));
				logger.info("noClaimBonusDate ", noClaimBonusDate);
			}
			
			mav = new ModelAndView(MAVPaths.VIEW_COVER_HISTORY);
			mav.addObject(MAVObjects.VO_CUSTOMER, customerDetailsXL);
			mav.addObject(MAVObjects.VO_CUSTOMER_HP, customerDetailsHP);
			mav.addObject(MAVObjects.VO_CUSTOMER_IP, customerDetailsIP);
			mav.addObject(MAVObjects.MOBILE_NUMBER, msisdn);
			mav.addObject(MAVObjects.GRACE_PERIOD_XL_EXIST, gracePeriodXLFlag);
			mav.addObject(MAVObjects.GRACE_PERIOD_HP_EXIST, gracePeriodHPFlag);
			mav.addObject(MAVObjects.GRACE_PERIOD_IP_EXIST, gracePeriodIPFlag);
			mav.addObject(MAVObjects.NO_CLAIM_BONUS_DATE, noClaimBonusDate);

			if (customerDetailsHP == null)
				mav.addObject(MAVObjects.MESSAGE_HP, "not registered");

			if (customerDetailsXL == null)
				mav.addObject(MAVObjects.MESSAGE_XL, "not registered");
			
			if (customerDetailsIP == null)
				mav.addObject(MAVObjects.MESSAGE_IP, "not registered");

			// showing msisdn number

			if (customerDetailsXL != null || customerDetailsHP != null || customerDetailsIP !=null)
				mav.addObject(MAVObjects.SHOW_MSISDN, "show");

		} catch (Exception mispException) {
			logger.error("Exception occurred while fetching the cover levels.",
					mispException);

			mav = super.error(
					"Exception occurred while fetching the Cover History",
					MAVPaths.VIEW_COVER_HISTORY);
		} 

		logger.exiting("getCoverHistory", mav);
		return mav;
	}

	public ModelAndView getMoreCoverHistory(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, CustomerVO customerVO) {
		logger.entering("getMoreCoverHistory", customerVO);
		String msisdn = "";
		ModelAndView mav = null;
		try {
			msisdn = customerVO.getMsisdn();
			logger.info(" msisdn ", msisdn);

			List<CustomerVO> customerDetails = null;
			List<CustomerVO> customerDetailsMore = null;
			List<CustomerVO> customerDetailsXL = null;
			List<CustomerVO> customerDetailsHP = null;
			List<CustomerVO> customerDetailsIP = null;
			int gracePeriodXLFlag=-1;
			int gracePeriodHPFlag = -1;
			int gracePeriodIPFlag = -1;			

			// customer details from main schema
			customerDetails = customerService.coverHistory(msisdn);

			// customer details from another schema
			customerDetailsMore = customerService.moreCoverHistory(msisdn);

			// customerDetailsMore=customerService.coverHistory(msisdn);

			// make one customer details

			customerDetails.addAll(customerDetailsMore);

			if (customerDetails.size() > 12) {
				customerDetails = customerService
						.getFirst12CustomerDetails(customerDetails);
			}

			// geting xp customer
			customerDetailsXL = customerService
					.getXLCustomerDetails(customerDetails);

			// geting hp customer
			customerDetailsHP = customerService
					.getHPCustomerDetails(customerDetails);
			
			// getting ip customer
			customerDetailsIP = customerService.getIPCustomerDetails(customerDetails);
			
			//check gracePeriodForXL
			
			gracePeriodXLFlag=customerService.checkGracePeriod(msisdn,2);
			logger.info("gracePeriodXLFlag ", gracePeriodXLFlag);

			//check gracePeriodForHP
			gracePeriodHPFlag=customerService.checkGracePeriod(msisdn,3);
			logger.info("gracePeriodHPFlag ", gracePeriodHPFlag);
			
			//check gracePeriodForIP
			gracePeriodIPFlag=customerService.checkGracePeriod(msisdn,4);
			logger.info("gracePeriodIPFlag ", gracePeriodIPFlag);

			String noClaimBonusDate="";
			if(customerService.getNoClaimBonusDate(msisdn)==null){
				noClaimBonusDate="n/a";
			} else {
				DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
				noClaimBonusDate=df.format(((Date) customerService.getNoClaimBonusDate(msisdn)));
				logger.info("noClaimBonusDate ", noClaimBonusDate);
			}

			mav = new ModelAndView(MAVPaths.VIEW_COVER_HISTORY);
			mav.addObject(MAVObjects.VO_CUSTOMER, customerDetailsXL);
			mav.addObject(MAVObjects.VO_CUSTOMER_HP, customerDetailsHP);
			mav.addObject(MAVObjects.VO_CUSTOMER_IP, customerDetailsIP);
			mav.addObject(MAVObjects.DISABLE_MORE, "true");
			mav.addObject(MAVObjects.MOBILE_NUMBER, msisdn);
			mav.addObject(MAVObjects.GRACE_PERIOD_XL_EXIST, gracePeriodXLFlag);
			mav.addObject(MAVObjects.GRACE_PERIOD_HP_EXIST, gracePeriodHPFlag);
			mav.addObject(MAVObjects.GRACE_PERIOD_IP_EXIST, gracePeriodIPFlag);
			mav.addObject(MAVObjects.NO_CLAIM_BONUS_DATE, noClaimBonusDate);


			if (customerDetailsHP == null)
				mav.addObject(MAVObjects.MESSAGE_HP, "not registered");

			if (customerDetailsXL == null)
				mav.addObject(MAVObjects.MESSAGE_XL, "not registered");
			
			
			if(customerDetailsIP == null)
				mav.addObject(MAVObjects.MESSAGE_IP, "not registered");
			// showing msisdn number

			if (customerDetailsXL != null || customerDetailsHP != null || customerDetailsIP != null)
				mav.addObject(MAVObjects.SHOW_MSISDN, "show");

		} catch (Exception mispException) {
			logger.error("Exception occurred while fetching the cover levels.",
					mispException);
			mav = super.error(
					"Exception occurred while fetching the cover history.",
					MAVPaths.VIEW_COVER_HISTORY);

		} /*
		 * catch (Exception exception) {
		 * logger.error("Exception occurred while fetching the cover levels.",
		 * exception); }
		 */

		logger.exiting("getMoreCoverHistory", mav);
		return mav;
	}	

	
	public ModelAndView loyaltyCustomers(HttpServletRequest request,
			HttpServletResponse response) {
		
		logger.entering("loyaltyCustomers ");

		ModelAndView mavObj = null;

		mavObj = new ModelAndView(MAVPaths.VIEW_LOYALTY_CUSTOMERS);

		logger.exiting("loyaltyCustomers", mavObj);

		return mavObj;
	}
	
	public ModelAndView getLoyaltyInformation(HttpServletRequest request,
			HttpServletResponse response , CustomerVO custVO) throws MISPException {
		
		logger.entering("getLoyaltyInformation ",custVO);
				
		ModelAndView mavObj = null;
		List<LoyaltyCustomerVO> loyalCustomersResponse = null;
		boolean custexists = true;
		boolean loyaltyEligible = false; 
		
		
		try {
			userDetails = (UserDetails) request.getSession().getAttribute(
					SessionKeys.SESSION_USER_DETAILS);
			
			loyalCustomersResponse = customerService.getLoyalCustomers(custVO,
					userDetails);			
			
			mavObj = new ModelAndView(MAVPaths.VIEW_LOYALTY_CUSTOMERS);
			
			loyalCustomersResponse=loyalCustomersResponse.isEmpty()?null:loyalCustomersResponse;
			
			if(loyalCustomersResponse != null){
			
			for(int i=0;i<loyalCustomersResponse.size();i++)
			{
				if(loyalCustomersResponse.get(i).getIsLoyaltyEligible().equalsIgnoreCase("YES"))
				{
					loyaltyEligible = true;
				}
				if(loyalCustomersResponse.get(i).getProductId().equals(2))
				{
					mavObj.addObject(MAVObjects.XL_PACKAGE,loyalCustomersResponse.get(i).getIsDataPackage());
				} else if (loyalCustomersResponse.get(i).getProductId().equals(3))
				{
					mavObj.addObject(MAVObjects.HP_PACKAGE,loyalCustomersResponse.get(i).getIsDataPackage());
				}
			}
		  }
		
			mavObj.addObject(MAVObjects.IS_CUSTEXIST, custexists);
			mavObj.addObject(MAVObjects.LOYALTY_ELIGIBLE, loyaltyEligible);
			mavObj.addObject(MAVObjects.MSISDN, custVO.getMsisdn());
			mavObj.addObject(MAVObjects.VO_LOYALCUSTOMER, loyalCustomersResponse);
			if (loyalCustomersResponse==null || loyalCustomersResponse.isEmpty()){
				mavObj.addObject("message",
						FaultMessages.LOYALTY_MSISDN_INVALID);
			}
			
			
		} catch (MISPException mispException) {
			logger.error(FaultMessages.GET_LOYALTY_FAILED, mispException);

			mavObj = super.error(FaultMessages.GET_LOYALTY_FAILED,
					MAVPaths.URL_GET_LOYALTY_CUSTOMERS);

		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("getLoyaltyInformation  ", mavObj);

		return mavObj;
	}

	
	@SuppressWarnings("null")
	public ModelAndView applyLoyalty(HttpServletRequest request,
			HttpServletResponse response , CustomerVO custVO) throws MISPException {
		
		logger.entering("applyLoyalty",custVO);

		ModelAndView mavObj = null;
		boolean custexists = true;
		int  loyaltyResponse=-1;
		String[] result = new String[2];
		int count = 0;
		int oldLoyaltyPack = -1;
		int newLoyaltyPack = -1;
		int userId = 0;
		
		try {
			userDetails = (UserDetails) request.getSession().getAttribute(
					SessionKeys.SESSION_USER_DETAILS);
			
			userId = userDetails.getUserId();
			logger.debug("Msisdn :", custVO.getMsisdn()," User Id : ",userId);
			
			logger.info("applyLoyalty: ", custexists);
			
			String[] productIds=custVO.getProductId().split(",");
			
			LoyaltyCustomerVO loyaltyCustomerVO = null;
			
			mavObj = new ModelAndView(MAVPaths.VIEW_LOYALTY_CUSTOMERS);	
			
			for(String pId: productIds) {
				custVO.setProductId(pId);
			
				 loyaltyCustomerVO = customerService.getLoyaltyInformation(custVO);
				
				
				
				if(null!=loyaltyCustomerVO) {
							
						custVO.setLcId(loyaltyCustomerVO.getLcId());
						if(pId.equals("2"))
						{
							if(loyaltyCustomerVO.getIsDataPackage() == Integer.parseInt(custVO.getPakageXL()))
							{
								custVO.setIsData(loyaltyCustomerVO.getIsDataPackage());
							} else {
								custVO.setIsData(Integer.parseInt(custVO.getPakageXL()));
								
							}
							
							 oldLoyaltyPack = loyaltyCustomerVO.getIsDataPackage();
							 newLoyaltyPack =Integer.parseInt( custVO.getPakageXL());
						
						} else if(pId.equals("3")){
							if(loyaltyCustomerVO.getIsDataPackage() == Integer.parseInt(custVO.getPakageHP()))
							{
								custVO.setIsData(loyaltyCustomerVO.getIsDataPackage());
							} else {
								custVO.setIsData(Integer.parseInt(custVO.getPakageHP()));
							}
							
							 oldLoyaltyPack = loyaltyCustomerVO.getIsDataPackage();
							 //newLoyaltyPack =Integer.parseInt( custVO.getPakageXL());
							 newLoyaltyPack =Integer.parseInt( custVO.getPakageHP());
							
						}
					
						custVO.setCustId(String.valueOf(loyaltyCustomerVO.getCustId()));
						//custVO.setProductId(pId);
						custVO.setSnId(loyaltyCustomerVO.getSnId());
						
						loyaltyResponse= customerService.applyLoyaltyPackage(custVO,oldLoyaltyPack,newLoyaltyPack,userId);
						
						logger.info("applyLoyalty :: ", loyaltyResponse);
						
						
					
						if(405000000==loyaltyResponse) {
							if(loyaltyCustomerVO.getIsDataPackage() != custVO.getIsData())
							{
							   customerService.updatePackage(custVO);
							}
							customerService.updateLoyaltyInfo(custVO,userId,"SUCCESS",loyaltyResponse);	
							result[count]="SUCCESS";
							
						} else {
						
							customerService.updateLoyaltyInfo(custVO,userId,"FAILURE",loyaltyResponse);
							result[count]="FAILURE";
							
						}
						count++;
				} 	else{
					customerService.updateLoyaltyInfo(custVO,userId,"FAILURE",loyaltyResponse);
					
					mavObj.addObject("message", FaultMessages.LOYLATY_FAILED);
				}
				
				
			}
			
			for(String pId: productIds) {
				if(productIds.length == 1)
				{
					if(pId.equals("2"))
					{
						if(result[0] == "SUCCESS")
						{
							mavObj.addObject("message", SuccessMessages.LOYLATY_APPLIED_XL);
							
						} else if(result[0] == "FAILURE") {
							
							mavObj.addObject("faultmessage", FaultMessages.LOYLATY_FAILED_XL);
						}
					}else if(pId.equals("3")){
						
						if(result[0] == "SUCCESS")
						{
							mavObj.addObject("message", SuccessMessages.LOYLATY_APPLIED_HP);
							
						} else if(result[0] == "FAILURE") {
							
							mavObj.addObject("faultmessage", FaultMessages.LOYLATY_FAILED_HP);
							
						}
						
					}
				} else {
					
					if(result[0] == "SUCCESS" && result[1] == "SUCCESS" )
					{
						mavObj.addObject("message", SuccessMessages.LOYALTY_SUCCESS_HP_XL);
						
					} else if(result[0] == "FAILURE" && result[1] == "FAILURE" ) {
						
						mavObj.addObject("faultmessage", FaultMessages.LOYALTY_FAILURE_HP_XL);
					}  else if(result[0] == "SUCCESS" && result[1] == "FAILURE" ) {
						
						mavObj.addObject("message", SuccessMessages.LOYLATY_APPLIED_XL);
						mavObj.addObject("faultmessage", FaultMessages.LOYLATY_FAILED_HP);
					
					}  else if(result[0] == "FAILURE" && result[1] == "SUCCESS" ) {
						
						mavObj.addObject("message", SuccessMessages.LOYLATY_APPLIED_HP);
						mavObj.addObject("faultmessage", FaultMessages.LOYLATY_FAILED_XL);
					} 
				}
			}
		} catch (MISPException mispException) {
			logger.error(FaultMessages.LOYALTY_ALLOCATION_FAILED, mispException);

			mavObj = super.error(FaultMessages.LOYALTY_ALLOCATION_FAILED,
					MAVPaths.URL_GET_LOYALTY_CUSTOMERS);

		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("applyLoyalty", mavObj);

		return mavObj;
	}


}