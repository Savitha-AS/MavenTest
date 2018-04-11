package com.mip.application.constants;

/**
 * <code>FaultMessages.java</code> contains all business error messages. 
 * 
 * @author T H B S
 */
public interface FaultMessages {
	
	/**
	 * Fault messages w.r.t Change Password use case.
	 */	
	String PASSWORD_EXCEPTION_OCCURED = 
			"An Exception has occured while changing the password.";
	
	/**
	 * Fault messages w.r.t Forgot Password use case
	 */
	String FORGOT_PASSWORD_INVALID_USER = 
			"The login id entered is invalid.";
	String FORGOT_PASSWORD_INVALID_EMAIL_ID = 
			"Sorry, we do not have your E-mail ID. Kindly contact the help-desk to reset your password.";
	String RESET_PASSWORD_HASH_MISMATCH = 
			"The reset password URL has been expired. Please try again.";
	
	/**
	 * Fault messages w.r.t Products use case.
	 */
	
	String OFFER_INVALID_NAME = "Product exists with the same name.";
	
	/**
	 * Generic error message
	 */
	String GENERIC_ERROR = "Error while processing the request.";
	
	/**
	 * Fault messages w.r.t SMS Management module.
	 * 
	 */
	String SMS_EDIT_TEMPLATE_PAGE_LOADING_FAILURE = "Loading the Edit SMS Template page Failed";
	String SMS_TEMPLATE_CONTENT_FETCH_FAILURE = "Fetching Template Content Failed";
	String SMS_TEMPLATE_UPDATE_FAILURE = "SMS Template updated Failed";
	String SMS_TEMPLATE_TYPE_FETCH_FAILURE = "Fetching Template Type Failed";
	
	/**
	 * Fault messages w.r.t Customer Management module.
	 */
	String REG_CUS_PAGE_LOADING_FAILED = "Loading Register customer page failed";
	String CUSTOMER_REG_FAILED = "Register customer failed";
	String CUSTOMER_REG_EXCEPTION = "Exception occured while Registering Customer";
	String ERROR_PREPARING_RELATIONSHIP_TYPES = "Error in preparing Relationship type detail";
	String CUSTOMER_MSISDN_INVALID = "Mobile Number already exists.";
	String CUSTOMER_UPDATE_DB_EXCEPTION="DB Exception occurred while updating customer details";
	String CUSTOMER_MODIFY_FAILED = "Modify Customer details failed";
	String CUSTOMER_MODIFY_EXCEPTION = "Exception occured while Modifying Customer Details";
	String CUSTOMER_FETCH_FAILED = "Exception occured while Fetching Customer Details";
	String CUSTOMER_SEARCH_FAILED = "Exception occured while searching customer Details";
	String FAILED_CUSTOMER_MODIFY="fault.customer.modify";
	/**
	 * Fault messages w.r.t Insurance provider module.
	 */
	String INS_COMPANY_NAME_INVALID = "Insurance Company already exists with the same name.";
	String INS_COMPANY_REG_EXCEPTION = "Exception occured while creating new Insurance Company.";
	String INS_COMPANY_MOD_EXCEPTION = "Exception occured while modifying Insurance Company details.";
	String INS_COMPANY_DETAILS_FETCH_EXCEPTION = "Exception occured while fetching Insurance Company details.";
	String INS_COMPANY_LIST_EXCEPTION = "Exception occured while listing the Insurance Companies.";
	
	/**
	 * Fault messages related to DeRegister 
	 */
	String CUSTOMER_DEREG_FAILED = "Your request to de-register the customer has failed.";
	String DEREG_CUS_PAGE_LOADING_FAILED = "Loading De-Register customer page failed.";
	String WEEKLY_REPORT_PAGE_LOADING_FAILED = "Loading Download Weekly Report page failed.";
	String DE_REGISTER_MSISDN_NOT_REGISTERED = "fault.deregister.msisdn.notregistered";
	
	
	/**
	 * Web-service Fault Codes and messages.
	 */
	String OFFER_UNSUBSCRIBE_FAULT_VALIDATION_CODE = "800-OfferUnsubscribe-4000-V";
	String OFFER_UNSUBSCRIBE_FAULT_BUSINESS_B1_CODE = "800-OfferUnsubscribe-3000-B";
	String OFFER_UNSUBSCRIBE_FAULT_BUSINESS_B2_CODE = "800-OfferUnsubscribe-3001-B";
	String OFEFR_UNSUBSCRIBE_SUCCESS_CODE = "800-OfferUnsubscribe-S";
	String REMOVE_CUSTOMER_REGISTRATION_SUCCESS_CODE = "900-RemoveCustomerRegistration-S";
	String REMOVE_CUSTOMER_FAULT_VALIDATION_CODE = "900-RemoveCustomerRegistration-4000-V";
	
	String FAULT_VALIDATION_MESSAGE = "Validation fault. Please check the format of the input.";
	
	String NO_RECORD_EXIST = "reports.no.record.exist";
	
	/**
	 * Fault messages related to Leave Management.
	 */
	String APPLY_LEAVE_EXCEPTION = "Exception occured while applying User Leave Details.";
	String LEAVE_LIST_FETCH_EXCEPTION = "Exception occured while fetching list of all leave records.";
	String LEAVE_DATES_VALIDATION_EXCEPTION = "Exception occured while validating leave dates.";
	String VIEW_LEAVE_PAGE_LOADING_FAILED = "Loading View Leaves page failed.";
	String VIEW_LEAVE_EXCEPTION = "Exception occured while getting user leave details.";
	
	/**
	 * Fault message related to Claims Management.
	 */
	String CLAIM_INS_PAGE_LODING_FAILED = "Loding Claim Insurance page failed.";
	String CLAIM_INS_MODIFY_LODING_FAILED = "Loding Modify Beneficiary page failed.";
	String CLAIM_SEARCH_CUSTOMER_PAGE_LODING_FAILED = "Loding Modify Beneficiary page failed.";
	String CLAIM_INSURANCE_EXCEPTION = "Exception occured while claiming insurance.";
	String CLAIM_SEARCH_MODIFY_EXCEPTION = "Exception occured while modifying cutomer/beneficiary details.";
	String CLAIM_LIST_EXCEPTION = "Exception occured while getting claim details.";
	String CLAIM_INSURANCE_FAILED = "Claim Insurance Failed";
	
	/**
	 * Fault messages w.r.t Report module
	 */	
	String AGENT_REPORT_PAGE_LOADING_FAILED = "fault.agentreport.pageload.failure";
	String QA_REPORT_PAGE_LOADING_FAILED = "fault.qareport.pageload.failure";
	
	String CHANGE_DEDUCTION_MODE_FAILED="fault.change.deductionMode";
	String CHANGE_FIRST_MONTH_MODE_FAILED="fault.first.month.pageload.failure";
	
	String GET_LOYALTY_FAILED="Getting loyalty customers Failed";
	
	String LOYALTY_MSISDN_INVALID="platform.customer.loyalty.not.found";

	String LOYALTY_ALLOCATION_FAILED = "Loyalty Allocation Failed";
	String LOYLATY_FAILED = "failure.customer.loyalty";
	String LOYLATY_FAILED_XL = "failure.customer.loyalty.xl";
	String LOYLATY_FAILED_HP = "failure.customer.loyalty.hp";
	 String LOYALTY_FAILURE_HP_XL = "failure.customer.loyalty.xl.hp";
}
