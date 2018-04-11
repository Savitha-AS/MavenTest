package com.mip.application.constants;

/**
 * <code>SuccessMessages.java</code>contains all success message keys. 
 * 
 * @author T H B S
 */
public interface SuccessMessages {

	/**
	 * Success messages w.r.t Branch Management use-case model.
	 */
	String BRANCH_ADDED = "success.branch.add";	
	String BRANCH_MODIFIED = "success.branch.modify";
	String BRANCH_DEACTIVATED = "success.branch.deactivate";	
	
	/**
	 * Success messages w.r.t User Management use-case model.
	 */
	String USER_ADDED = "success.user.add";	
	String USER_MODIFIED = "success.user.modify";	
	String PASSWORD_RESET = "success.password.reset";
	String LEAVE_APPLIED= "success.leave.applied";

	/**
	 * Success messages w.r.t Change Password use case.
	 */
	String PASSWORD_CHANGED = "success.password.change";
	
	/**
	 * Success messages w.r.t Products use-case module.
	 */
	String OFFER_ASSIGNED = "success.offer.assign";	
	String OFFER_SAVED = "success.offer.save";	
	String OFFER_REVOKED = "success.offer.revoke";
	
	/**
	 * Success messages w.r.t Business rules use-case module.
	 */
	String BUSINESS_RULE_SAVED = "success.business.rule.save";	
	String BUSINESS_RULE_ACTIVATED = "success.business.rule.activate";
	
	/**
	 * Success messages w.r.t SMS Management use-case module.
	 */
	String SMS_TEMPLATE_UPDATE_SUCCESS = "success.sms.template.modify";	
	
	
	/**
	 * Messages with respect to Reports use-case module.
	 */
	String REPORTS_NO_CUSTOMERS_SUBSCRIBED="reports.no.customer.subscribe";
	
	/**
	 * Success messages w.r.t Customer Management use-case module.
	 */
	String CUSTOMER_REGISTERED= "success.customer.register";
	String CUSTOMER_MODIFIED = "success.customer.modify";	
	
	/**
	 * Success messages w.r.t Insurance Provider use-case module.
	 */
	String INSURANCE_COMPANY_SAVED="success.insurance.company.save";
	String INSURANCE_COMPANY_MODIFIED="success.insurance.company.modify";

	/**
	 * Success messages w.r.t Admin Config use-case.
	 */
	String ADMIN_CONFIG_SAVED = "success.admin.config.save";
	
	String DE_REGISTER_SUCCESS = "success.customer.deregister";
	String DE_REGISTER_SUCCESS_PREV = "success.customer.deregister.prev";
	String DE_REGISTER_SUCCESS_REFUND_SUCCESS = "success.customer.deregister.refund";
	String DE_REGISTER_SUCCESS_REFUND_FAILURE = "success.customer.deregister.norefund";
	
	String INSURANCE_CLAIM_SUCCESS = "success.insurance.claimed";
	String SEARCH_MODIFY_CUST_SUCCESS = "success.search.modify.customer";
	
	/**
	 * Success message w.r.t Forgot password module
	 */
	String FORGOT_PASSWORD_MAIL_SUCCESS = "success.mail.forgot.password";
	
	
	/**
	 * Success messages w.r.t Role Management use-case model.
	 */
	String ROLE_ADDED = "success.role.add";
	String ROLE_MODIFIED = "success.role.modify";
	

	String CHANGE_DEDUCTION_MODE="success.change.deductionMode";
	String LOYLATY_APPLIED_XL = "success.customer.loyalty.xl";
	String LOYLATY_APPLIED_HP = "success.customer.loyalty.hp";
	String LOYALTY_SUCCESS_HP_XL = "success.customer.loyalty.xl.hp";
	// String LOYALTY_SUCCESS_HP_FAILURE_XL = "success.customer.loyalty.hp.failure.xl";
	// String LOYALTY_SUCCESS_XL_FAILURE_HP = "success.customer.loyalty.xl.failure.hp";
}
