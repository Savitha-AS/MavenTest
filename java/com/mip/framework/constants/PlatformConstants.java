package com.mip.framework.constants;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface PlatformConstants {

	/**
	 * This represents the Date Format which will be used for the incident
	 * number
	 */
	String TICKET_NUMBER_FORMAT = "yyyyMMddhhmmss";

	/**
	 * Represents the ticket constant.
	 */
	String TICKET_NUMBER = "ticketNumber";

	/**
	 * Represents the error message to be displayed on the error page in case of
	 * any exceptions.
	 */
	String ERROR_MESSAGE = "errorMessage";

	/**
	 * Represents the goBackLink.
	 */
	String GO_BACK_LINK = "goBackLink";

	/**
	 * Represents the errorModelMap passed to the error page.
	 */
	String ERROR_MODEL_MAP = "errorModelMap";

	/**
	 * Represents the success message to be displayed on the screen.
	 * 
	 */
	String SUCCESS_MESSAGE_KEY = "successMessageKey";
	String SUCCESS_MESSAGE_ARGS = "successMessageArgs";

	/**
	 * Represents the model map passed to the success page.
	 */
	String SUCCESS_MODEL_MAP = "successModelMap";

	/**
	 * Represents the info message to be displayed on the screen.
	 * 
	 */
	String INFO_MESSAGE_KEY = "infoMessageKey";
	String INFO_MESSAGE_ARGS = "infoMessageArgs";

	/**
	 * Represents the model map passed to the info page.
	 */
	String INFO_MODEL_MAP = "infoModelMap";

	/**
	 * Represents the data to hold the Home page data.
	 */
	String MARQUEE_MESSAGE = "marqueeMessage";
	String TOTAL_CUSTOMER_COUNT = "totalCustomerCount";
	String TOTAL_XL_CUSTOMER_COUNT = "totalXLCustomerCount";
	String CUSTOMER_REG_CUR_MONTH = "customerRegCurMonth";
	/* String CUSTOMER_REG_BY_USER = "customerRegByUser"; */
	String XL_REG_BY_USER_CONFIRMED = "xlRegByUserConfirmed";
	String XL_REG_BY_USER_PENDING = "xlRegByUserPending";
	String FREE_MODEL_REG_BY_USER_CONFIRMED = "freeModelRegByUserConfirmed";
	String FREE_MODEL_REG_BY_USER_PENDING = "freeModelRegByUserPending";
	String HOSP_REG_BY_USER_CONFIRMED = "hospRegByUserConfirmed";
	String HOSP_REG_BY_USER_PENDING = "hospRegByUserPending";
	/*
	 * String CUSTOMER_REG_CONFIRMED = "customerRegConfirmed"; String
	 * CUSTOMER_REG_ACTIVE = "customerRegActive";
	 */
	String USER_LAST_LOGIN_DATE = "lastLoginDate";

	/**
	 * Represents Admin Config Keys.
	 */
	String DEFAULT_PASSWORD = "default_password";
	String USER_LOGIN_PREFIX = "user_login_prefix";
	String PASSWORD_HISTORY_LIMIT = "password_history_limit";
	String MAX_LOGIN_ATTEMPTS = "max_login_attempts";
	String MAX_IDLE_COUNT = "max_idle_count";
	String REGISTER_CUSTOMER_WS_URL = "register_customer_ws_url";
	String ANNOUNCEMENT_MESSAGE = "announcement_message";
	String OFFER_SUBSCRIPTION_LAST_DAY = "offer_subscription_last_day";
	String MSISDN_CODES = "msisdn_codes";
	String OFFER_UNSUBSCRIBE_WS_URL = "offer_unsubscribe_ws_url";
	String REMOVE_CUSTOMER_REGISTRATION_WS_URL = "remove_customer_registration_ws_url";
	String DEFAULT_OFFER_ASSIGNED = "default_offer_assigned";
	String COMMISSION_PERCENTAGE = "commission_percentage";
	String DEREGISTER_CUSTOMER_WS_URL = "deregister_customer_ws_url";
	String SUMMARY_CUSTOMER_DETAILS_CHANGES_RECORD_LIMIT="summary_customer_details_changes_record_limit";
	/**
	 * Represents Selected customers.
	 */
	String SELECTED_CUSTOMERS = "selectedCustomers";

	/**
	 * Represents ALL customers.
	 */
	String ALL_CUSTOMERS = "allCustomers";

	/**
	 * Represents Multiple Cover offer type.
	 */
	String OFFER_TYPE_MULTIPLE_COVER = "Multiple Cover";

	/**
	 * Represents Multiple Cover offer type
	 */
	String OFFER_TYPE_ADDITIONAL_COVER = "Additional Cover";

	/**
	 * Represents active state of business rule.
	 */
	int BUSINESS_RULE_ACTIVE = 1;

	/**
	 * Represents inactive state of business rule.
	 */
	int BUSINESS_RULE_INACTIVE = 0;

	/**
	 * Represents Multiple Cover offer type.
	 */
	int MULTIPLE_COVER = 2;

	/**
	 * Represents Additional Cover offer type.
	 */
	int ADDITIONAL_COVER = 1;

	/**
	 * For SMS Management module
	 */
	String TEMPLATE_NAME_LIST = "TEMPLATE_NAME_LIST";
	String TEMPLATE_CONTENT = "TEMPLATE_CONTENT";
	String PLACE_HOLDER_LIST = "PLACE_HOLDER_LIST";
	String SELECTED_TEMPLATE_ID = "SELECTED_TEMPLATE_ID";
	String SMS_TEMPLATE_VO = "SMS_TEMPLATE_VO";

	/**
	 * For Customer Management module
	 */
	String RELATIONSHIP_TYPE_LIST = "RELATIONSHIP_TYPE_LIST";

	/**
	 * Property file name
	 */
	String PROPERTY_FILE_NAME = "/nls/messages.properties";
	String BASE_KEY_RELATIONSHIP = "relationship";

	/**
	 * Audit Logger name
	 */
	String AUDIT_LOGGER_APPENDER = "auditLogger";

	/**
	 * Users who will be excluded during a Search operation.
	 */
	String USERS_DASHBOARD = "Dashboard";
	String USERS_USSD = "USSD";

	/**
	 * Branch Code prefix.
	 */
	String BRANCH_CODE_PREFIX = "BC";

	/**
	 * Represents the DB date format.
	 */
	String DB_DATE_FORMAT = "dd/MM/yyyy";
	String DB_DATETIME_FORMAT = "dd/MM/yyyy hh:mm:ss";

	/**
	 * Time in minutes (For Modify Customer Dteails)
	 */
	long MIN_DIFF = 30;

	/**
	 * Represents Administrator Role.
	 */
	String ADMIN_ROLE = "Business Administrator";

	/**
	 * Represents Active/ Inactive status.
	 */
	Byte STATUS_ACTIVE = 1;
	Byte STATUS_INACTIVE = 0;

	/**
	 * Represents Weekly Tigo Report Keys
	 */
	String MAN_DAYS = "man_days";
	String AVERAGE_REGISTRATION_COUNT = "average_registration_count";
	String CSC_AGENTS_COUNT = "csc_agents_count";
	String MOBILE_AGENTS_COUNT = "mobile_agents_count";
	String PERIOD_REGISTRATION_COUNT = "period_registration_count";
	String TOTAL_REGISTRATION_COUNT = "total_registration_count";

	/**
	 * Represents Revenue Report Keys
	 */
	String FREEMIUM_REVENUE = "freemium_revenue";
	String TOTAL_REVENUE = "total_revenue";
	String COMMISSION_COST = "commission_cost";
	String FREE_PREMIUM_COST = "free_premium_cost";
	String FREEMIUM_PREMIUM_COST = "freemium_premium_cost";
	String TOTAL_PREMIUM_COST = "total_premium_cost";
	String TOTAL_COST = "total_cost";
	String PROFIT = "profit";

	/**
	 * For fetching SMS template
	 */
	String SMS_DASHBOARD_XL_REG_SUCCESS = "dashboard_xl_reg_success";
	
	String SMS_BENEFICIARY_SMS = "beneficiary_sms";

	
	
	/**
	 * Communication Channels
	 */
	int CHANNEL_DASHBOARD = 1;

	/**
	 * Value of 1 Ghana Cedi, in terms of units used by OCS for Deduction.
	 */
	long ONE_GHC_UNIT = 10000;

	/**
	 * Properties used to access web service.
	 */
	String ACCOUNT_ADJUST_WEBSERVICE_ENDPOINT = "http://Vesb01GH:8280/services/AdjustAccountPS";
	BigInteger ACCOUNT_ADJUST_WEBSERVICE_ACCOUNT_TYPE = new BigInteger("2000");
	BigInteger ACCOUNT_ADJUST_WEBSERVICE_OPERATE_TYPE = new BigInteger("2");

	String PRODUCT_FM = "1";
	String PRODUCT_XL = "2";
	String PRODUCT_HP = "3";
	String PRODUCT_IP = "4";

	String FM_PRODUCT = "Free Model";
	String XL_PRODUCT = "Xtra-Life";
	String HP_PRODUCT = "Hospitalization";
	String IP_PRODUCT = "Income Protection";

	int RECORDS_PER_AGENT = 30;

	/**
	 * Constants for Dynamic Role Access
	 */
	String DEFAULT_MENU_URLS = "'/index.jsp','/pages/login.jsp','/pages/logout.jsp',"
			+ "'/theme*','/appScripts*','/login*','/dwr*',"
			+ "'/platform.page.changePassword.task'";

	String ROLE_DESCRIPTION_INITIAL = "ROLE_";

	String CHANGE_DEDUCTIONMODE_WSURL = "change_deduction_mode_ws_url";
	
	String REACTIVATION_CUSTOMER_WSURL = "reactivation_ws_url";

	String ASSIGN_OFFER_WS_URL_WSURL ="assign_offer_ws_url";
	
	String CALL_CENTER_AGENT = "Call Center Agent";
	
	String CLAIMS_OFFICER = "ROLE_CLER_";
	
	String[] AGENT_ACCESS_DEDUCTION_MODE = { "ROLE_CALL_CENTER_AGENT",
			"ROLE_ADMIN", "ROLE_USER_ADMIN", "ROLE_REPORTING_EXECUTIVE",
			"ROLE_INSURER" };
	
	ArrayList<String> AGENT_ACCESS_DEDUCTION_MODE_LIST = new ArrayList<String>(
			Arrays.asList(AGENT_ACCESS_DEDUCTION_MODE));
	

	String[] ROLE_CLAIMS_MANAGERS = new String[]{ "ROLE_CLER",
			"ROLE_ADMIN", "ROLE_INSURER_" };
	
	ArrayList<String> ROLE_CLAIMS_MANAGERS_LIST = new ArrayList<String>(
			Arrays.asList(ROLE_CLAIMS_MANAGERS));
	
	static final Set<String> ROLE_CLAIMS_MANAGERS1 = new HashSet<String>(Arrays.asList(
		     new String[] {"ROLE_CLER",
		 			"ROLE_ADMIN", "ROLE_INSURER"}
		));
	
	String REACTIVATION_MSG="Customer deactivated <1> and may do a reactivation";
	String CUST_EXIST_BIMA_CANCELLATION_MSG="Customer deactivated <1> and may do a reactivation";
	

 	List<String> DEREG_TIGO_STAFF_CHURN_TYPE_3 = 		
      		new ArrayList<String>() {		
                  {		
                     		
                      add("Millicom Agent");		
                      add("Tigo Store Managers");
                      add("Tigo Staff Executives");
                      add("Tigo call center advisors");
                     
                     		
                  }		
              };

	String LOYALTY_PACK_WS_URL_WSURL = "loyaltypack_ws_url";

	String INS_MSISDN_CODES ="ins_msisdn_codes";
}
