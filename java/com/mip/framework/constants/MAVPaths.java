package com.mip.framework.constants;

/**
 * <p><code>MAVPaths.java</code> serves as a container to hold all the 
 * constants for resolving resultant view names declared in the controllers.</p>
 * 
 * @author T H B S
 *
 */
public interface MAVPaths {
	
	/**
	 * Following are the paths that will be used to resolve a View object, 
	 * using the Spring framework's <code>viewResolver</code> bean as defined in
	 * <code>platform-servlet.xml</code>, using the class : 
	 * <code>org.springframework.web.servlet.view.InternalResourceViewResolver</code>.
	 */
	String VIEW_HOME = "home";
	String VIEW_GLOBAL_SUCCESS = "global_success";
	String VIEW_GLOBAL_ERROR = "global_error";
	String VIEW_GLOBAL_INFO = "global_info";
	String VIEW_ACCESS_DENIED = "access_denied";
	String VIEW_CHANGE_PASSWORD = "changePassword";
	String VIEW_ADMIN_CONFIG = "adminConfig";
	String VIEW_FIRST_LOGIN_SUCCESS = "first_login_success";
	
	String VIEW_BRANCH_LIST = "branch/listBranch";
	String VIEW_BRANCH_MODIFY = "branch/modifyBranch";
	
	String VIEW_USER_ADD = "user/addUser";
	String VIEW_USER_LIST = "user/listUser";
	String VIEW_USER_DETAILS = "user/userDetails";
	String VIEW_USER_SEARCH = "user/searchUser";
	String VIEW_USER_MODIFY = "user/modifyUserDetails";
	String VIEW_USER_RESET_PASSWORD = "user/resetPassword";
	String VIEW_USER_FORGOT_PASSWORD = "/forgotPassword";

	String VIEW_CUSTOMER_REGISTER = "customer/registerCustomer";
	String VIEW_CUSTOMER_SEARCH = "customer/searchCustomer";
	String VIEW_CUSTOMER_MODIFY = "customer/modifyCustomerDetails";
	String VIEW_CUSTOMER_LIST = "customer/listCustomers";
	String VIEW_CUSTOMER_DEACTIVE= "customer/deregCustomerDetails";
	
	String VIEW_INS_COMP_REGISTER = "insuranceCompany/registerCompany";
	String VIEW_INS_COMP_LIST = "insuranceCompany/listCompany";
	String VIEW_INS_COMP_MODIFY = "insuranceCompany/modifyCompany";

	
	String VIEW_OFFER_CREATE = "offer/createOffer";
	String VIEW_OFFER_ASSIGN = "offer/assignOffer";
	String VIEW_OFFER_REVOKE = "offer/revokeOffer";
	String VIEW_OFFER_LIST = "offer/listOffers";
	String VIEW_OFFER_DETAILS = "offer/offerDetails";
	
	String VIEW_BUSINESS_RULE_ADD = "businessRule/addBusinessRule";
	String VIEW_BUSINESS_RULE_LIST = "businessRule/listBusinessRules";
	String VIEW_BUSINESS_RULE_LIST_OFFERS = "businessRule/listOffers";
	String VIEW_BUSINESS_RULE_VIEW_OFFER = "businessRule/offerDetails";
	String VIEW_BUSINESS_RULE = "businessRule/viewBusinessRule";
	
	String VIEW_SMSTEMPLATE_EDIT = "smsTemplate/addSmsTemplate";
	
	String VIEW_CUSTOMER_REPORT="reports/customerReport";	
	String VIEW_FINANCIAL_REPORT_PAGE="reports/financialReport";
	String VIEW_WEEKLY_REPORT_PAGE="reports/weeklyReport";
	String VIEW_REVENUE_REPORT_PAGE="reports/revenueReport";
	String VIEW_COVERAGE_REPORT_PAGE="reports/coverageReport";

	String VIEW_CUSTOMER_DEREGISTER = "customer/deRegisterCustomer";
	
	String VIEW_USER_AGENT_APPLYLEAVE= "leave/applyLeave";
	String VIEW_USER_AGENT_VIEWLEAVES= "leave/viewLeaves";
	
	String VIEW_CLAIM_INSURANCE = "claims/claimInsurance";
	String VIEW_MODIFY_CLAIM_DETAILS = "claims/claimDetails";
	String VIEW_CLAIM_DETAILS = "claims/viewClaimDetails";
	String VIEW_SEARCH_CUST_DETAILS = "claims/searchCustomer";
	String VIEW_CLAIM_LIST = "claims/listClaims";
	String VIEW_AGENT_REPORT_PAGE="reports/agentReport";
	
	String VIEW_AND_MODIFY_ROLE= "role/viewAndModifyRole";
	
	String VIEW_QA_REPORT_PAGE = "reports/qaReport";
	
	String VIEW_DEDUCTIONS_REPORT_PAGE="reports/deductionReport";
	
	/**
	 * Following are the absolute path of the JSPs from the webApp's 
	 * Context-Root. These are needed to provide the path for Go-Back-Link 
	 * from Global Success page.
	 */
	String JSP_HOME = "/platform.page.home.task";
	String JSP_GLOBAL_SUCCESS = "/platform.page.global_success.task";
	String JSP_GLOBAL_ERROR = "/platform.page.global_error.task";
	String JSP_ACCESS_DENIED = "/platform.page.access_denied.task";
	String JSP_CHANGE_PASSWORD = "/platform.page.changePassword.task";
	
	String JSP_BRANCH_ADD = "/branch.page.addBranch.task";
	String JSP_BRANCH_LIST = "/branch.controller.listBranches.task";
	
	String JSP_USER_ADD = "/user.controller.getRolesAndBranches.task";
	String JSP_USER_LIST = "/user.page.listUser.task";
	String JSP_USER_DETAILS = "/user.page.userDetails.task";
	String JSP_USER_SEARCH = "/user.page.searchUser.task";
	String JSP_USER_MODIFY = "/user.page.modifyUserDetails.task";
	String JSP_USER_RESET_PASSWORD = "/user.page.resetPassword.task";

	String JSP_CUSTOMER_REGISTER = "/customer.page.registerCustomer.task";
	String JSP_CUSTOMER_SEARCH = "/customer.page.searchCustomer.task";
	String JSP_CUSTOMER_MODIFY = "/customer.page.modifyCustomerDetails.task";
	
	String JSP_INS_COMP_REGISTER = "/insuranceCompany.page.registerCompany.task";
	String JSP_INS_COMP_LIST = "/insuranceCompany.page.listCompany.task";
	String JSP_INS_COMP_MODIFY = "/insuranceCompany.page.modifyCompany.task";

	String JSP_OFFER_CREATE = "/offer.page.createOffer.task";
	String JSP_OFFER_ASSIGN = "/offer.page.assignOffer.task";
	
	String JSP_CLAIM_INSURANCE = "/claims.page.claimInsurance.task";
	String JSP_SEARCH_CUST_DETAILS = "/claims.page.searchCustomer.task";
	String JSP_CLAIM_INSURANCE_LIST = "/claims.controller.listClaims.task";
	String JSP_CLAIMS_LIST = "/claims.page.listClaims.task";
	
	String JSP_ROLE_ADD = "/role.page.addRole.task";
	String JSP_ROLE_VIEW = "/role.controller.viewRole.task";
	String JSP_ROLE_MODIFY = "/role.controller.modifyRole.task";
	String JSP_ROLE_VIEW_AND_MODIFY= "role.controller.viewAndModifyRole.task";
		
	/**
	 * Represents the URLs
	 */
	String URL_CREATE_OFFER="/offers.controller.loadCreateOffer.task";
	String URL_ASSIGN_OFFER="/offers.controller.retrieveOffers.task";
	String URL_REVOKE_OFFER="/offers.controller.loadRevokeOffer.task";
	String URL_LIST_OFFERS="/offers.controller.listOffers.task";
	String URL_BR_CREATE_BR="/businessRule.controller.loadBusinessRule.task";
	String URL_BR_LIST_BR="/businessRule.controller.listBusinessRules.task";
	String URL_SMSTEMPLATE_EDIT="/sms.controller.editSMSTemplate.task";
	String URL_MODIFY_CUSTOMER="/customer.controller.modifyCustomerDetails.task";
	String URL_SEARCH_CUSTOMER="/customer.controller.searchCustomerDetails.task";
	String URL_MODIFY_INS_COMP="/insuranceCompany.controller.modifyInsuranceProviderDetails.task";
	String URL_LIST_INS_COMP="/insuranceCompany.controller.listInsuranceProviders.task";
	String URL_REGISTER_CUSTOMER = "/customer.controller.loadRegisterCustomer.task";
	String URL_LOAD_ADMIN_CONFIG = "/adminConfig.controller.retrieveConfigDetails.task";
	String URL_DEREGISTER_CUSTOMER = "/customer.controller.loadDeRegisterCustomer.task";
	String URL_WEEKLY_REPORT = "/customer.controller.loadWeeklyReport.task";
	String URL_VIEW_LEAVE = "/leave.controller.viewLeaves.task";
	
	String LOG_IN_URL = "login.jsp";
	String LOG_OUT_URL = "/pages/logout.jsp";

	String VIEW_CHANGE_DEDUCTION_MODE="customer/changeDeductionMode";
	String VIEW_COVER_HISTORY="customer/coverHistory";
	
	
	String URL_GET_LOYALTY_CUSTOMERS="/customer.controller.loyaltyCustomers.task";
	String VIEW_LOYALTY_CUSTOMERS = "customer/loyaltyCustomers";
}
