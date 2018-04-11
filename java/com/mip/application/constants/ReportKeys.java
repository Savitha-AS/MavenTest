/**
 * 
 */
package com.mip.application.constants;

/**
 * This interface contains all constants related to reports module
 * 
 * @author THBS
 */
public interface ReportKeys {

	String[] MONTH_NAMES = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November",
			"December" };

	/**
	 * Lookup for Column reference
	 */
	char[] A2Z =
		{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S'
		,'T','U','V','W','X','Y','Z'};
	
	/**
	 * Maximum number of records to be fetched for customer report
	 */
	int REPORTS_MAX_RECORDS = 50000;

	/**
	 * Fetches all active customers
	 */
	int CUST_IS_DORMAT = 0;
	
	/**
	 * Customer Report Headings
	 * 28-Aug-2012: Removed Usage column
	 */
	//Modified 'Total Cover' to 'Total Charges' as a part of CR.
	String[] CUST_REPORT_HEADINGS = { "Customer Name", "Customer Age",
			"Insured Relative Name", "Insured Relative Age",
			"Insured Relative Relationship", "Customer ID", 
			"Free Model Registered By", "XL Registered By", "HP Registered By",
			"Free Model Registration Date", "Free Model Confirmation Date",
			"XL Registration Date", "XL Confirmation Date", 
			"HP Registration Date", "HP Confirmation Date",
			"Last Modified By", "Last Modified Date", "Product Name",
			"Free Model Earned Cover", "XL Earned Cover", "HP Earned Cover", 
			"XL Cover Charges", "HP Cover Charges", 
			"Total Charges" };
	
	String[] CUST_REPORT_HEADINGS_ALL = { "Customer Name", "Customer Age",
			"Insured Relative Name", "Insured Relative Age",
			"Insured Relative Relationship", "Msisdn", "Customer ID", 
			"Free Model Registered By", "XL Registered By", "HP Registered By",
			"Free Model Registration Date", "Free Model Confirmation Date",
			"XL Registration Date", "XL Confirmation Date", 
			"HP Registration Date", "HP Confirmation Date",
			"Last Modified By", "Last Modified Date", "Product Name",
			"Free Model Earned Cover", "XL Earned Cover", "HP Earned Cover", 
			"XL Cover Charges", "HP Cover Charges", 
			"Total Charges" };
	
	String[] CUST_REPORT_HEADINGS_CUST_ID = { "Customer Name", "Customer Age",
			"Insured Relative Name", "Insured Relative Age",
			"Insured Relative Relationship", "Customer ID", 
			"Free Model Registered By", "XL Registered By", "HP Registered By",
			"Free Model Registration Date", "Free Model Confirmation Date",
			"XL Registration Date", "XL Confirmation Date", 
			"HP Registration Date", "HP Confirmation Date",
			"Last Modified By", "Last Modified Date", "Product Name",
			"Free Model Earned Cover", "XL Earned Cover", "HP Earned Cover", 
			"XL Cover Charges", "HP Cover Charges", 
			"Total Charges" };
	
	String CUST_REPORT_TITLE = "Customer Report For ";

	String CUST_REPORT_BR_VERSION = "Business Rule Version : ";

	String CUST_REPORT_TOTAL_CUSTOMERS = "Total Customers : ";

	String CUST_REPORT_SHEET_NAME = "Customer Report";

	String CUST_REPORT_FILE_NAME = "Customer_Report_";
	
	String CUST_REPORT_FILE = "Customer_Report";
	
	String REPORT_DIRECTORY_PATH = "D:\\MIP\\Archives\\Reports";	
	String REPORT_FOLDER_PATH = "Downloads";
	String REPORT_ZIP_FILE_TYPE = ".zip";
	
	String FINANCIAL_REPORT_ERROR = "Error occured while creating financial report.";

	String FINANCIAL_REPORT_TITLE = "Financial Report For ";

	String FINANCIAL_REP_ACTIVE_CUST = "Active customers in the platform :";

	String FINANCIAL_REP_FREE_SUM = "Free Sum Assured for the customers :";

	String FINANCIAL_REP_PAID_SUM = "Paid Sum Assured for the customers :";

	String FINANCIAL_REP_TOTAL_SUM = "Total Sum Assured for the customers :";

	String FINANCIAL_REP_SHEET_NAME = "Financial Report";

	String FINANCIAL_REP_FILE_NAME = "Financial_Report_";
	
	
	// Agent Report	
	
	byte AGENT_REP_COLOR_RED = (byte) 213;

	byte AGENT_REP_COLOR_GREEN = (byte) 222;

	byte AGENT_REP_COLOR_BLUE = (byte) 237;
	
	int AGENT_REP_ZOOM_NUMERATOR = 9;
	
	int AGENT_REP_ZOOM_DENOMINATOR = 10;
	
	short REP_FONT_SIZE = 10;
	
	String[] AGENT_REP_HEADING = {"AGENT ID", "AGENT NAME", "PHONE", "EMAIL", "GENDER",
			"TYPE", "REGION", "CITY", "LOCATION" };
	/**
	 * Changed : 12-06-2012
	 * Q3 CR : Agent Report Changes
	 * 
	 * Columns added in AGENT_REP_DAILY_HEADING and AGENT_REP_TOTAL_REG
	 */
	/*String[] AGENT_REP_DAILY_HEADING = { "REGISTERED", "LAPTOP", "HANDSET",
			"FREEMIUM", "CONFIRMED", "AVERAGE CONFIRMED" };*/
	
	String[] AGENT_REP_DAILY_HEADING = { "REGISTERED FREE TOTAL", "FREE LAPTOP", 
			"FREE HANDSET", "REGISTERED XL TOTAL", "XL LAPTOP", "XL HANDSET", 
			"CONFIRMED FREE", "CONFIRMED XL", "DERGISTERED FREE", "DERGISTERED XL",  
			"AVERAGE CONFIRMED FREE" };
	
	String[] AGENT_REP_NEW_DAILY_HEADING = { "REGISTERED XL TOTAL", "CONFIRMED XL TOTAL",
			"REGISTERED HP TOTAL", "CONFIRMED HP TOTAL","REGISTERED IP TOTAL","CONFIRMED IP TOTAL"};
	
	String[] AGENT_REP_SAL_DAILY_HEADING = { "AGENT ID", "AGENT NAME", "PHONE", "EMAIL", "GENDER",
			"TYPE", "REGION", "CITY", "LOCATION", "REGISTERED XL TOTAL", "CONFIRMED XL TOTAL",
			"REGISTERED HP TOTAL", "CONFIRMED HP TOTAL","REGISTERED IP TOTAL","CONFIRMED IP TOTAL", "REGISTERED XL and HP TOTAL","REGISTERED XL and IP TOTAL", "CONFIRMED XL and HP TOTAL","CONFIRMED XL and IP TOTAL"};
	
	String[] AGENT_REP_DEDUCTION_DAILY_HEADING = { "AGENT ID", "AGENT NAME", "PHONE", "EMAIL", "GENDER",
			"TYPE", "REGION", "CITY", "LOCATION", "DEDUCTED XL TOTAL","DEDUCTED HP TOTAL","DEDUCTED IP TOTAL", "DEDUCTED XL and HP TOTAL","DEDUCTED XL and IP TOTAL"};
	
	String AGENT_REPORT_ERROR = "Error occured while creating agent report.";
	
	String AGENT_REPORT_TITLE = "AGENTS PERFORMANCE REPORT";
	
	String AGENT_SALARY_REPORT_TITLE = "AGENT SALARY REPORT";
	
	String DEDUCTED_REPORT_TITLE = "FIRST DAY DEDUCTIONS REPORT";
	
	String[] AGENT_REP_TOTAL_REG = {"TOTAL REGISTERED FREE", "TOTAL REGISTERED XL", 
			"TOTAL CONFIRMED FREE", "TOTAL CONFIRMED XL" };
	
	//	AVG CONFIRMED XL and HP is changed to CONFIRMATION RATE %
	String[] AGENT_REP_NEW_TOTAL_REG = {"TOTAL CONFIRMED XL","TOTAL CONFIRMED HP", 
			"TOTAL CONFIRMED IP","TOTAL CONFIRMED XL and HP","TOTAL CONFIRMED XL and IP", "CONFIRMATION RATE %", 
			"TOTAL DEREGISTERED XL", "TOTAL DEREGISTERED HP","TOTAL DEREGISTERED IP"};
	
	String AGENT_REP_TOTAL = "TOTALS";

	String AGENT_REP_SHEET_NAME = "Week ";
	
	String AGENT_REP_SALARY_SHEET_NAME = "Agent Salary Report";
	
	String AGENT_REP_DEDUCTED_SHEET_NAME = "First Day deductions Report";
	
	String AGENT_REP_FILE_NAME = "All_Agents_Performance_Report";
	
	String AGENT_SAL_REP_FILE_NAME = "All_Agents_Salary_Report";
	
	String AGENT_DED_REP_FILE_NAME = "All_Agents_First_Day_Deductions_Report";
	
	String[] AGENT_REP_ACCESS_LEVEL = {"1","2","3","4","5","6","7","8"};
	
	// Weekly Report Keys
	
	String WEEKLY_REP_FILE_NAME = "Weekly_Tigo_Report";
	
	String WEEKLY_REP_TITLE = "Weekly Tigo Report";
	
	String WEEKLY_REP_SHEET_NAME = "Tigo Weekly";
	
	String[] WEEKLY_REP_HEADING = {
			"Date of last data update", "Period start date",
			"Month or period end", "Number of past agent working days (man days)",
			"Current average # of registrations per agent per day for current period",
			"Number of CSC agents to date", "Number of Mobile agents to date",
			"Total number of agents to date", "Number of days till period end",
			"Total number of agent working days till period end (man days)", 
			"Registrations for period", "Total number of unique subs (AC)", 
			"Target period end registrations", 
			"Outstanding number of registrations for period", 
			"Total period end registrations at current average rate (FC)", 
			"Percent completion (FC)", 
			"Average # of daily registrations per agent to meet period end target"};
	
	
	
	// Revenue Report
	
	String REVENUE_REP_FILE_NAME = "Revenue_Report";
	
	String REVENUE_REP_TITLE = "Revenue Report";
	
	String REVENUE_REP_SHEET_NAME = "Revenue";
	
	String[] REVENUE_REP_HEADING = {
			"Revenue from Freemium Model", "Total Revenue",
			"Cost for Commissions", "Cost for Premiums (Free Model)",
			"Cost for Premiums (Freemium Model)", "Total Premium Cost",
			"Total Costs", "Profit"};
	
	
	// Coverage Report
	
	String COVERAGE_REP_FILE_NAME = "Coverage_Report";
	
	String COVERAGE_REP_SHEET1_TITLE = "Coverage Report";
	
	String COVERAGE_REP_SHEET2_TITLE = "Coverage Report Summary";
	
	String COVERAGE_REP_SHEET1_NAME = "Coverage";
	
	String COVERAGE_REP_SHEET2_NAME = "Coverage Summary";
	
	String[] COVERAGE_REP_SHEET_HEADING1 = {"Region", "City", "0 (all)", "0 (inactive)"};
	
	String COVERAGE_REP_ACTIVE_PERCENTAGE = "% of active";
	
	String[] COVERAGE_REP_SHEET_HEADING2 = {"No. Active (revenue > 0 in previous month)", 
			"No. Registered (end of previous month)"};
	
	String[] COVERAGE_REP_SHEET_HEADING3 = {"0 (all)", "0 (inactive)"};
	
	String[] COVERAGE_REP_SHEET_HEADING4 = {"Number", "% of active", "% of registered"};
	
	String COVERAGE_REP_TOTAL = "TOTAL";
	
	// E-Signature
	
	String CUSTOMER_FIRST_NAME = "First_Name";
	
	String CUSTOMER_SURNAME = "Surname";
	
	String MOBILE_NUMBER = "Mobile_Number";
	
	String CONFIRMED = "Is_Confirmed";
	
	String CONFIRMED_DATE = "Confirmed_Date";
	
	String CONF_STATUS_CONFIRMED = "Confirmed";
	
	String CONF_STATUS_UNCONFIRMED = "Unconfirmed";
	
	String REG_TYPE_FREE = "Free";
	
	String REG_TYPE_XL = "XL";
	
	String REG_TYPE_HP = "HP";
	
	String REG_TYPE_ALL = "All_Products";

	String CONF_STATUS_CONF_AND_UNCONF = "Conf_and_Unconf";
	
	String CURRENCY = "GHS";

	String CUST_SUBS_DORMAT = "dormant";

	String REPORTS_FOLDER = "reports";

	String REPORT_FILE_TYPE = ".xls";
	
	String REPORT_FILE_TYPE_CSV = ".csv";

	String REPORT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	String SPACE = " ";

	String UNDERSCORE = "_";
	
	String REPORT_DATE_FORMAT = "yyyy-MM-dd";
	
	

	String USER_REP_FILE_NAME = "User_Report_";

	String USER_REPORT_SHEET_NAME = "User Report";

	String[] USER_REPORT_HEADINGS = { "User Id", "User Name", "Mobile Number", 
			"Date of Birth", "Age", "Gender", "Role", "Branch", "Registered Date","Current Month Leave","Current Year Leave"};
	
	/**
	 * CSV related keys
	 */
	String CSV_FIELD_SEPARATOR = ",";
	String CSV_LINE_SEPARATOR = "\n";
	String CSV_QUOTE_CHARACTER = "\"";
	
	String ROLE_INSURER = "ROLE_INSURER";
	
	//CSC Weekly report keys
	
	String CSC_WEEKLY_REP_FILE_NAME = "CSC_WEEKLY_REPORT";
	String[] CSC_REPORT_HEADINGS = { "Store", "Monthly Target", "Daily Target","MTD"};
	String WEEK_ENDING_DAY = "Wed";
	final int MONTHLY_TARGET = 50;
	final int DAILY_TARGET = 2;

	String FIRST_MONTH_DEDUCTION_REP_FILE_NAME = "First_Month_Deduction_Report";

	String FIRST_MONTH_DEDUCTION_SHEET_NAME = "First Month Deduction Report";
	
	String[] FIRST_MONTH_DEDUCTION_HEADING = { "AGENT ID", "AGENT NAME", "PHONE", "EMAIL", "GENDER",
			"TYPE", "REGION", "CITY", "LOCATION", "PARTIAL DEDUCTION XL TOTAL IN CURRENT MONTH","PARTIAL DEDUCTION XL TOTAL IN PREVIOUS MONTH", "FULL DEDUCTION XL TOTAL IN CURRENT MONTH","FULL DEDUCTION XL TOTAL IN PREVIOUS MONTH",
			"PARTIAL DEDUCTION HP TOTAL IN CURRENT MONTH","PARTIAL DEDUCTION HP TOTAL IN PREVIOUS MONTH", "FULL DEDUCTION HP TOTAL IN CURRENT MONTH","FULL DEDUCTION HP TOTAL IN PREVIOUS MONTH", "PARTIAL DEDUCTION IP TOTAL IN CURRENT MONTH","PARTIAL DEDUCTION IP TOTAL IN PREVIOUS MONTH", 
			"FULL DEDUCTION IP TOTAL IN CURRENT MONTH","FULL DEDUCTION IP TOTAL IN PREVIOUS MONTH" };
	
	String FIRST_QUARTER_DEDUCTION_REP_FILE_NAME = "First_Quarter_Deduction_Report";
	

	String FIRST_QUARTER_DEDUCTION_SHEET_NAME = "First Quarter Deduction Report";
	
	String[] FIRST_QUARTER_DEDUCTION_HEADING = { "AGENT ID", "AGENT NAME", "PHONE", "EMAIL", "GENDER",
			"TYPE", "REGION", "CITY", "LOCATION", "FULL DEDUCTION XL TOTAL",
			"FULL DEDUCTION HP TOTAL", "FULL DEDUCTION IP TOTAL" };

	
}
