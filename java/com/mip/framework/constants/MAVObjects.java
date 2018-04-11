package com.mip.framework.constants;

/**
 * <p><code>MAVObjects.java</code> serves as a container to hold all the 
 * constants in the name of which, the objects will be added to a 
 * ModelAndView instance declared in the controllers, using the API 
 * <code>mavObj.addObject(key, value);</code></p>
 * 
 * @author T H B S
 *
 */
public interface MAVObjects {

	/**
	 * All VO declarations of package <code>com.mip.application.view</code> 
	 * go here.
	 */
	String VO_BRANCH = "branchVO";
	String VO_USER = "userVO";
	String VO_CUSTOMER = "VO_CUSTOMER";
	String VO_ASSIGN_OFFER = "assignOfferVO";
	String VO_CREATE_OFFER = "createOfferVO";
	String VO_BUSINESS_RULE_OFFER = "businessRuleOfferVO";
	String VO_CUST_REPORT="customerReportVo";	
	String VO_FINANCIAL_REPORT="financialReportVo";
	String VO_CUST_TOTAL_USAGE="custSubsTotalUsage";
	String VO_INS_PROVIDER="VO_INS_PROVIDER";
	String VO_SEARCH_CRITERIA="VO_SEARCH_CRITERIA";
	String VO_ADMIN_CONFIG = "adminConfigVO";
	String VO_USER_LEAVE = "VO_USERLEAVE";
	String VO_CLAIM = "VO_CLAIM";
	String VO_CUSTOMER_STATS = "customerStatsVO";
	String VO_ROLE = "roleVO";
	
	/**
	 * All List declarations of type <code>java.util.List</code> go here. 
	 */
	String LIST_BRANCH = "branchList";
	String LIST_USER = "userList";
	String LIST_PRODUCTS = "productsList";
	String LIST_COVER = "productCoverList";
	String LIST_OFFERS_TYPES = "offerTypeList";
	String LIST_OFFERCOVER_DETAILS = "offerCoverDetailsList";
	String LIST_INSURANCE_COMPANY = "insuranceCompList";
	String LIST_BUSINESS_RULES = "businessRulesList";
	String LIST_SUBSCRIBED_CUSTOMERS="subscribedCustList";
	String LIST_CUSTOMER_DETAILS="customerDetailsList";
	String LIST_DEREGISTERED_DETAILS="deRegisteredDetails";
	String LIST_CUST_DET="customerList";
	String LIST_INS_COMP_DET="companyDetailsList";
	String LIST_INS_COMP="insuranceCompanyList";
	String LIST_RELATIONSHIP_TYPES="RELATIONSHIP_TYPE_LIST";
	String LIST_USER_LEAVES = "userLeaveList";
	String LIST_CLAIMS = "claimList";
	String LIST_SUBSCRIBED_PRODUCTS = "subscribedProductList";
	String LIST_SUMMARY_DETAILS_CHANGES="summaryDetailsChangesList";
	
	/**
	 * All Map declarations of type <code>java.util.Map</code> go here. 
	 */
	String MAP_REPORT_RANGES="reportRangesMap";
	String MAP_ROLES = "roleMap";
	String MAP_OFFERS = "offerMap";
	String MAP_LEAVE_YEAR = "currentYearLeaveMap";
	String MAP_LEAVE_MONTH = "currentMonthLeaveMap";
	String MAP_LEAVE_MONTH_YEAR="monthYearLeave";
	
	
	/**
	 * Represents the General objects
	 */
	String CUSTOMER_COUNT = "customerCount";
	String ACTIVE_BUSINESS_RULE = "activeBusinessRule";
	String FUTURE_ACTIVE_BUSINESS_RULE = "futureActiveBusinessRule";
	String REPORT_CURRENT_MONTH="currentMonth";
	String BUSINESS_RULE_NAME="businessRuleName";
	String BUSINESS_RULE="businessRule";
	String IS_FIRST_LOGIN = "isFirstLogin";
	String IS_EDITABLE="is_editable";
	String IS_ACTIVE="is_active";
	String IS_DEACTIVE="is_deactive";
	String IS_OFFER_MODIFIABLE="is_offer_modifiable";
	String IS_HP_REG="is_hpCust";
	String IS_IP_REG="is_ipCust";
	String IS_XL_REG="is_xlCust";
	String IS_CUSTEXIST="custexists";
	String LOYALTY_ELIGIBLE="loyaltyEligible";
	String IS_CLAIMS_MANAGERS_LOGIN="isClaimsManagerLogin";
	String REACTIVATION_MSG="reactivationMsg";
	String MSISDN_CODES="msisdnCodes";
	String IP_DEACTIVATED="ipDeactivated";
	String CUST_EXIST_BIMA_CANCEL_MSG="custEXistInBimaCancellationMsg";
	String IS_IP_REACTIVE="isIPReactive";
	String IS_XL_REACTIVE="isXLreactive";
	String IS_HP_REACTIVE="isHPreactive";
	
	
	
	String IS_DEREGISTEREDCUST="is_deregisteredcust"; 
	
	String VO_CUSTOMER_HP="VO_CUSTOMER_HP";
	String VO_CUSTOMER_IP="VO_CUSTOMER_IP";
	String DISABLE_MORE="DISABLE_MORE" ;
	
	String MESSAGE_HP="NOT_REGISTERED_HP";
	String MESSAGE_IP="NOT_REGISTERED_IP";
	String MESSAGE_XL="NOT_REGISTERED_XL";
	String SHOW_MSISDN="SHOW_MSISDN";
	String MOBILE_NUMBER="mobileNumber";
	String IS_DEDUCTION_MODE="is_DeductionMode";
	
	String DEREG_CUSTOMER_LIST="customerDetailsList";
	String DEREGED_CUSTOMER_LIST="customerDeregDetailsList";
	
	String IS_PA_REG="isPAReg";
	
	//added for grace period
	String GRACE_PERIOD_XL_EXIST = "gracePeriodXLExist";
	String GRACE_PERIOD_HP_EXIST = "gracePeriodHPExist";
	String GRACE_PERIOD_IP_EXIST = "gracePeriodIPExist";
	String NO_CLAIM_BONUS_DATE = "noClaimBonusDate";
	String VO_LOYALCUSTOMER = "loyalCustomers";	
	String LOYALTY_ELIGIBILITY_PRODUCTS="loyaltyProductList";
	String MSISDN = "loyaltymsisdn";
	String XL_PACKAGE = "xlPackage";
	String HP_PACKAGE = "hpPackage";
}
