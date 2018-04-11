package com.mip.application.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.ClaimDetails;
import com.mip.application.model.CustomerSubscription;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.ProductDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.services.ClaimsManagementService;
import com.mip.application.services.CustomerService;
import com.mip.application.view.ClaimVO;
import com.mip.application.view.CustomerVO;
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

/**
 * <p>
 * <code>ClaimsManagementController.java</code> contains methods pertaining to
 * Claim Insurance Management use case model. Claim Insurance Management use
 * case model includes following use cases :
 * <table border="1">
 * <tr>
 * <th width="5%">Sl. No.</th>
 * <th>Use Case</th>
 * <th>Dispatcher action method signature</th>
 * </tr>
 * <tr>
 * <td>1.</td>
 * <td>Claim Insurance</td>
 * <td>
 * {@link #findCustomerByMSISDN(HttpServletRequest,HttpServletResponse,CustomerVO)}
 * </td>
 * </tr>
 * <tr>
 * <td>1.</td>
 * <td>Claim Insurance</td>
 * <td>{@link #loadModifyClaimDetails(HttpServletRequest,HttpServletResponse)}</td>
 * </tr>
 * <tr>
 * <td>1.</td>
 * <td>Claim Insurance</td>
 * <td>{@link #claimInsurance(HttpServletRequest,HttpServletResponse,ClaimVO)}</td>
 * </tr>
 * </table>
 * <br/>
 * 
 * This controller extends the <code>BasePlatformController</code> class of our
 * MISP framework.
 * </p>
 * 
 * @see com.mip.framework.controllers.BasePlatformController
 * 
 * @author T H B S
 */

public class ClaimsManagementController extends BasePlatformController {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ClaimsManagementController.class);

	private UserDetails userDetails = null;

	/**
	 * Set inversion of Control for <code>ClaimsManagementService</code>
	 */
	private ClaimsManagementService claimsManagementService;

	public void setClaimsManagementService(
			ClaimsManagementService claimsManagementService) {
		this.claimsManagementService = claimsManagementService;
	}

	/**
	 * Set inversion of Control for <code>CustomerService</code>
	 */
	private CustomerService customerService;

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	/**
	 * This method controls the flow for search customer for claim.
	 * 
	 * @param httpServletRequest
	 *            - An instance of HttpServletRequest Object
	 * @param httpServletResponse
	 *            - An instance of HttpServletResponse Object
	 * @param customerVO
	 *            - An instance of CustomerVO Object
	 * 
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView findCustomerByMSISDN(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, CustomerVO customerVO) {
		logger.entering("findCustomerByMSISDN");

		String msisdn = "";
		ModelAndView mav = null;
		// CustomerDetails custModel = null;
		CustomerVO custModel = null;

		List<CustomerVO> custList = null;
		boolean isSearchAndModify = false;

		boolean isClaimsManagerLogin = false;
		//boolean isShowList = true;


		try {

			isSearchAndModify = httpServletRequest.getParameter("snm").equals(
					"1");
			msisdn = customerVO.getMsisdn();

			userDetails = (UserDetails) httpServletRequest.getSession()
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);

			String userRoleDesc = userDetails.getRoleMaster()
					.getRoleDescription().replaceAll("\\d", "");

			if (PlatformConstants.CLAIMS_OFFICER.equalsIgnoreCase(userRoleDesc)
					) {
				custList = claimsManagementService
						.findCustomerForClaims(msisdn);
				isClaimsManagerLogin = true;
			} else {
				custModel = claimsManagementService.findCustomer(msisdn);
				/*
				 * customerVO =
				 * CustomerM2VMappings.mapCustModelToCustVOForSearch( custModel,
				 * userDetails);
				 */

				custList = Arrays.asList(custModel);
			}
			
			/*if(custList.isEmpty()){
				isShowList = false;
			}*/

			/**
			 * To check whether the request is coming from searchCustomer or
			 * claimInsurance.
			 */
			if (isSearchAndModify) {
				logger.debug("Search and Modify Customer");
				mav = new ModelAndView(MAVPaths.VIEW_SEARCH_CUST_DETAILS);
			} else {
				logger.debug("Claim Insurance");
				mav = new ModelAndView(MAVPaths.VIEW_CLAIM_INSURANCE);
			}
			mav.addObject("custList", custList);
			mav.addObject("isClaimsManagerLogin", isClaimsManagerLogin);
			//mav.addObject("isShowList", isShowList);

		} catch (MISPException mispException) {
			if (isSearchAndModify) {
				logger.error(
						FaultMessages.CLAIM_SEARCH_CUSTOMER_PAGE_LODING_FAILED,
						mispException);
				mav = super.error(
						FaultMessages.CLAIM_SEARCH_CUSTOMER_PAGE_LODING_FAILED,
						MAVPaths.JSP_SEARCH_CUST_DETAILS);
			} else {
				logger.error(FaultMessages.CLAIM_INS_PAGE_LODING_FAILED,
						mispException);
				mav = super.error(FaultMessages.CLAIM_INS_PAGE_LODING_FAILED,
						MAVPaths.JSP_CLAIM_INSURANCE);
			}

		} catch (Exception exception) {
			if (isSearchAndModify) {
				logger.error(
						FaultMessages.CLAIM_SEARCH_CUSTOMER_PAGE_LODING_FAILED,
						exception);
				mav = super.error(
						FaultMessages.CLAIM_SEARCH_CUSTOMER_PAGE_LODING_FAILED,
						MAVPaths.JSP_SEARCH_CUST_DETAILS);
			} else {
				logger.error(FaultMessages.CLAIM_INS_PAGE_LODING_FAILED,
						exception);
				mav = super.error(FaultMessages.CLAIM_INS_PAGE_LODING_FAILED,
						MAVPaths.JSP_CLAIM_INSURANCE);
			}
		}

		logger.exiting("findCustomerByMSISDN", mav);
		return mav;
	}

	/**
	 * This method controls the flow for loading claims details.
	 * 
	 * @param httpServletRequest
	 *            - An instance of HttpServletRequest Object
	 * @param httpServletResponse
	 *            - An instance of HttpServletResponse Object
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView loadModifyClaimDetails(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		logger.entering("loadModifyClaimDetails");

		String claimedPerson = "";
		String msisdn = "";

		ModelAndView mav = null;
		CustomerVO customerVO = null;
		List<String> relationshipList = null;
		boolean isSearchAndModify = false;
		List<ProductDetails> productList = null;
		List<ProductCoverDetailsVO> productCoverList = null;
		/* List<ProductCoverDetailsVO> productCoverList = null; */
		boolean is_hpCust = false;
		boolean is_ipCust = false;
		boolean is_xlCust = false;
		boolean isClaimsManagerLogin = false;
		String msisdnCodes="";
		try {
			isSearchAndModify = httpServletRequest.getParameter("snm").equals(
					"1");

			msisdn = httpServletRequest.getParameter("msisdn");
			logger.debug("msisdn: " + msisdn);

			
			claimedPerson = httpServletRequest.getParameter("claimedPerson");

			userDetails = (UserDetails) httpServletRequest.getSession()
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);

			relationshipList = customerService.getRelationshipTypes();
			productList = customerService.retrieveOfferNamesAndIds();
			msisdnCodes = customerService.getMsisdnCodes();
			productCoverList = customerService
					.retrieveOfferCoverDetailsIPProduct();
			
			customerVO = customerService
					.getCustomerDetails(msisdn, userDetails);

			String userRoleDesc = userDetails.getRoleMaster()
					.getRoleDescription().replaceAll("\\d", "");

			if (PlatformConstants.CLAIMS_OFFICER.equalsIgnoreCase(userRoleDesc)
					) {

				isClaimsManagerLogin = true;
			}

			if (customerVO.getPurchasedProducts().equalsIgnoreCase("3")) {
				is_hpCust = true;
			}
			if (customerVO.getPurchasedProducts().equalsIgnoreCase("4")) {
				is_ipCust = true;
			}
			if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,2")) {
				is_xlCust = true;
			}
			if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,2,3")) {
				is_xlCust = true;
				is_hpCust = true;
			}
			if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,2,4")) {
				is_xlCust = true;
				is_ipCust = true;
			}
			if (customerVO.getPurchasedProducts().equalsIgnoreCase("1,2,3,4")) {
				is_xlCust = true;
				is_hpCust = false;
				is_ipCust = true;
			}
			if (customerVO.getPurchasedProducts().equalsIgnoreCase("3,4")) {
				is_hpCust = false;
				is_ipCust = true;
			}

			if (isSearchAndModify) {
				logger.debug("Search and Modify Customer, is_hpCust: ",
						is_hpCust);
			} else {
				logger.debug("Claim Insurance");
			}
			
			if(isClaimsManagerLogin){
				mav = new ModelAndView(MAVPaths.VIEW_CLAIM_DETAILS);
			}else{
			mav = new ModelAndView(MAVPaths.VIEW_MODIFY_CLAIM_DETAILS);
			}
			
			mav.addObject(MAVObjects.LIST_RELATIONSHIP_TYPES, relationshipList);
			mav.addObject(MAVObjects.LIST_PRODUCTS, productList);
			mav.addObject(MAVObjects.LIST_COVER, productCoverList);
			mav.addObject(MAVObjects.VO_CUSTOMER, customerVO);
			mav.addObject(MAVObjects.IS_HP_REG, is_hpCust);
			mav.addObject(MAVObjects.IS_IP_REG, is_ipCust);
			mav.addObject(MAVObjects.IS_XL_REG, is_xlCust);
			mav.addObject(MAVObjects.IS_CLAIMS_MANAGERS_LOGIN,
					isClaimsManagerLogin);
			mav.addObject(MAVObjects.MSISDN_CODES, msisdnCodes);
			
				if (null == claimedPerson)
					mav.addObject("claimedPerson", "A");
				else
					mav.addObject("claimedPerson", claimedPerson);
			

		} catch (MISPException mispException) {
			if (isSearchAndModify) {
				logger.error(FaultMessages.CLAIM_INS_MODIFY_LODING_FAILED,
						mispException);
				mav = super.error(FaultMessages.CLAIM_INS_MODIFY_LODING_FAILED,
						MAVPaths.JSP_SEARCH_CUST_DETAILS);
			} else {
				logger.error(FaultMessages.CLAIM_INS_PAGE_LODING_FAILED,
						mispException);
				mav = super.error(FaultMessages.CLAIM_INS_PAGE_LODING_FAILED,
						MAVPaths.JSP_CLAIM_INSURANCE);
			}
		} catch (Exception exception) {
			if (isSearchAndModify) {
				logger.error(FaultMessages.CLAIM_INS_MODIFY_LODING_FAILED,
						exception);
				mav = super.error(FaultMessages.CLAIM_INS_MODIFY_LODING_FAILED,
						MAVPaths.JSP_SEARCH_CUST_DETAILS);
			} else {
				logger.error(FaultMessages.CLAIM_INS_PAGE_LODING_FAILED,
						exception);
				mav = super.error(FaultMessages.CLAIM_INS_PAGE_LODING_FAILED,
						MAVPaths.JSP_CLAIM_INSURANCE);
			}
		}

		logger.exiting("loadModifyClaimDetails", customerVO);
		return mav;
	}

	/**
	 * This method controls the flow for Claiming Insurance.
	 * 
	 * @param httpServletRequest
	 *            - An instance of HttpServletRequest Object
	 * @param httpServletResponse
	 *            - An instance of HttpServletResponse Object
	 * @param claimVO
	 *            - An instance of ClaimVO Object
	 * 
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView claimInsurance(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, ClaimVO claimVO) {
		logger.entering("claimInsurance");

		ModelAndView mav = null;
		boolean isClaimSuccessful = false;
		boolean isSearchAndModify = false;
		CustomerVO existCustomerDetails;
		CustomerSubscription customerSubscription=null;
		Set<InsuredRelativeDetails> insuredRelativesSet = null;
		InsuredRelativeDetails insuredRelativeDetails = null;
		List<InsuredRelativeDetails> insRelativeDetailsList=new ArrayList<InsuredRelativeDetails>();
		ModifyCustomerVO modifyCustomerVO =null;
		try {
			logger.debug("claimVO : ", claimVO);

			isSearchAndModify = httpServletRequest.getParameter("snm").equals(
					"1");

			userDetails = (UserDetails) httpServletRequest.getSession()
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);
			
			
			String custId=String.valueOf(claimVO.getCustId());
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
			
			
			CustomerVO custVO=new CustomerVO();
			
			
			custVO.setMsisdn(claimVO.getMsisdn());
			custVO.setCustId(custId);
			
			custVO.setFname(claimVO.getFname());
			custVO.setSname(claimVO.getSname());
			custVO.setDob(claimVO.getDob());
			custVO.setAge(claimVO.getAge());
			custVO.setGender(claimVO.getGender());
			
			
			
			custVO.setInsRelFname(claimVO.getInsRelFname());
			custVO.setInsRelSurname(claimVO.getInsRelSurname());
			custVO.setInsRelIrDoB(claimVO.getInsRelIrDoB());
			custVO.setInsRelAge(claimVO.getInsRelAge());
			custVO.setInsRelation(claimVO.getRelation());
			custVO.setInsMsisdn(claimVO.getInsMsisdn());
			
			
			custVO.setIpNomFirstName(claimVO.getIpNomFirstName());
			custVO.setIpNomAge(claimVO.getIpNomAge());
			custVO.setIpNomSurName(claimVO.getIpNomSurName());
			custVO.setIpInsMsisdn(claimVO.getIpInsMsisdn());
						
			
			//compare the old value and new value
			modifyCustomerVO = customerService.getNewOldCustomerDetails(custVO,existCustomerDetails);
			
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
			
			
			if (isSearchAndModify) {
				isClaimSuccessful = claimsManagementService
						.searchAndModifyInsurance(claimVO, userDetails);
			} else {
				isClaimSuccessful = claimsManagementService
						.claimAndModifyInsurance(claimVO, userDetails);
			}

			if (isClaimSuccessful) {
				if (isSearchAndModify) {
					logger.info(SuccessMessages.SEARCH_MODIFY_CUST_SUCCESS);
					mav = super.success(
							SuccessMessages.SEARCH_MODIFY_CUST_SUCCESS,
							MAVPaths.JSP_SEARCH_CUST_DETAILS);
				} else {
					logger.info(SuccessMessages.INSURANCE_CLAIM_SUCCESS);
					mav = super.success(
							SuccessMessages.INSURANCE_CLAIM_SUCCESS,
							MAVPaths.JSP_CLAIM_INSURANCE);
				}
			} else {
				if (isSearchAndModify) {
					logger.error(FaultMessages.CLAIM_SEARCH_MODIFY_EXCEPTION);
					mav = super.error(
							FaultMessages.CLAIM_SEARCH_MODIFY_EXCEPTION,
							MAVPaths.JSP_SEARCH_CUST_DETAILS);
				} else {
					logger.error(FaultMessages.CLAIM_INSURANCE_FAILED);
					mav = super.error(FaultMessages.CLAIM_INSURANCE_FAILED,
							MAVPaths.JSP_CLAIM_INSURANCE);
				}
			}

		} catch (MISPException mispException) {
			if (isSearchAndModify) {

				logger.error(FaultMessages.CLAIM_SEARCH_MODIFY_EXCEPTION,
						mispException);
				mav = super.error(FaultMessages.CLAIM_SEARCH_MODIFY_EXCEPTION,
						MAVPaths.JSP_SEARCH_CUST_DETAILS);
			} else {

				logger.error(FaultMessages.CLAIM_INSURANCE_EXCEPTION,
						mispException);
				mav = super.error(FaultMessages.CLAIM_INSURANCE_EXCEPTION,
						MAVPaths.JSP_CLAIM_INSURANCE);
			}

		} catch (Exception exception) {
			if (isSearchAndModify) {

				logger.error(FaultMessages.CLAIM_SEARCH_MODIFY_EXCEPTION,
						exception);
				mav = super.error(FaultMessages.CLAIM_SEARCH_MODIFY_EXCEPTION,
						MAVPaths.JSP_SEARCH_CUST_DETAILS);
			} else {

				logger.error(FaultMessages.CLAIM_INSURANCE_EXCEPTION, exception);
				mav = super.error(FaultMessages.CLAIM_INSURANCE_EXCEPTION,
						MAVPaths.JSP_CLAIM_INSURANCE);
			}
		}

		logger.exiting("claimInsurance");
		return mav;
	}

	/**
	 * This method controls the flow for list claims.
	 * 
	 * @param httpServletRequest
	 *            - An instance of HttpServletRequest Object
	 * @param httpServletResponse
	 *            - An instance of HttpServletResponse Object
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView listClaims(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		logger.entering("listClaims");

		ModelAndView mav = null;
		List<ClaimDetails> claimList = null;

		try {

			claimList = claimsManagementService.getAllClaimDetails();

			mav = new ModelAndView(MAVPaths.VIEW_CLAIM_LIST);
			mav.addObject(MAVObjects.LIST_CLAIMS, claimList);

		} catch (MISPException exception) {
			logger.error("An exception occured while fetching list of claim.",
					exception);
			mav = super.error("", MAVPaths.JSP_CLAIMS_LIST);
		} catch (Exception exception) {
			logger.error("An exception occured while fetching list of claim.",
					exception);
			mav = super.error("", MAVPaths.JSP_CLAIMS_LIST);
		}
		logger.exiting("listClaims", mav);
		return mav;
	}
}
