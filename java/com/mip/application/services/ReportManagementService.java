/**
 * 
 */
package com.mip.application.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellRangeAddress;

import sun.management.resources.agent;

import com.mip.application.constants.ReportKeys;
import com.mip.application.dal.managers.BranchManager;
import com.mip.application.dal.managers.BusinessRuleMasterManager;
import com.mip.application.dal.managers.OfferDetailsManager;
import com.mip.application.dal.managers.ReportManagementManager;
import com.mip.application.dal.managers.UserManager;
import com.mip.application.model.BranchDetails;
import com.mip.application.model.CustomerDetails;
import com.mip.application.model.CustomerSubscription;
import com.mip.application.model.InsuredRelativeDetails;
import com.mip.application.model.RoleMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.utils.ReportUtil;
import com.mip.application.view.CustDeductionReportVO;
import com.mip.application.view.CustomerReportDataVO;
import com.mip.application.view.DeductionReportVO;
import com.mip.application.view.DeregisteredCustomersVO;
import com.mip.application.view.ReportAgentVO;
import com.mip.application.view.ReportCoverageVO;
import com.mip.application.view.ReportCustomerVO;
import com.mip.application.view.ReportDailyNewVO;
import com.mip.application.view.ReportFinancialVO;
import com.mip.application.view.ReportWeeklyVO;
import com.mip.application.view.UserOfferRegistrationVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.MIPFont;
import com.mip.framework.utils.TypeUtil;

/**
 * <p>
 * <code>ReportManagementService</code> contains all the service layer methods
 * pertaining to Reports use case model (Customer report and Financial report).
 * </p>
 * 
 * @author T H B S
 */

public class ReportManagementService {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ReportManagementService.class);

	/**
	 * Set inversion of Control for <code>UserManager</code>,
	 * <code>ReportManagementManager</code>, <code>OfferDetailsManager</code>
	 * and <code>BranchManager</code>.
	 */

	private UserManager userManager;

	private ReportManagementManager reportManager;

	private OfferDetailsManager offerDetailsMgr;

	private BranchManager branchManager;

	private BusinessRuleMasterManager busRuleMasterManager;

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setReportManager(ReportManagementManager reportManager) {
		this.reportManager = reportManager;
	}

	public void setOfferDetailsMgr(OfferDetailsManager offerDetailsMgr) {
		this.offerDetailsMgr = offerDetailsMgr;
	}

	public void setBranchManager(BranchManager branchManager) {
		this.branchManager = branchManager;
	}

	public void setBusRuleMasterManager(
			BusinessRuleMasterManager busRuleMasterManager) {
		this.busRuleMasterManager = busRuleMasterManager;
	}

	/**
	 * This method returns the customer ranges based on the total number of
	 * subscribed customers. If the total number of customer is 30,000, the the
	 * ranges will be
	 * 
	 * <pre>
	 * 1-10000
	 * 10001-20000
	 * 20001-30000
	 * </pre>
	 * 
	 * @return <code>Map</code> of customer ranges with key as
	 *         "startRow_endRow_reportNo" and value as "startRow - endRow"
	 * 
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public Map<String, String> getCustomerRanges() throws MISPException {

		logger.entering("getCustomerRanges");

		// Holds customer ranges
		Map<String, String> custRangeMap = new LinkedHashMap<String, String>();

		try {

			// Gets the total number of subscribed customers
			List<Integer> totalCount = reportManager.getTotalCustomerCount();
			logger.debug("total customer count", totalCount);

			int totalCustomers = (Integer) totalCount.get(0);

			// Returns null if there are no subscribed customers
			if (totalCustomers == 0) {
				return null;
			}

			// Calculates the ranges
			int custRange = ReportKeys.REPORTS_MAX_RECORDS;
			int intialRange = 1;
			int finalRange = custRange;

			int count = 1;

			do {
				if (count > 1) {
					finalRange = (count * custRange);
				}
				if (finalRange > totalCustomers) {
					finalRange = totalCustomers;
				}
				custRangeMap.put((intialRange + ReportKeys.UNDERSCORE
						+ finalRange + ReportKeys.UNDERSCORE + count),
						intialRange + " - " + (finalRange));

				intialRange += custRange;
				count++;

			} while (intialRange <= totalCustomers);

		} catch (DBException e) {
			logger.error("Exception occured while calculating the ranges for total customers subscribed : "
					+ e);
			throw new MISPException(e);
		}

		logger.exiting("getCustomerRanges");
		return custRangeMap;

	}

	/**
	 * This method generates the customer report based on given customer range
	 * 
	 * @param reportCustomerVO
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @return List of all subscribed customers
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public List<CustomerReportDataVO> generateCustomerReport(
			ReportCustomerVO reportCustomerVO, String reportRange)
			throws MISPException {

		logger.entering("generateCustomerReport", reportCustomerVO);

		List<CustomerReportDataVO> subscribedCustList = null;

		try {

			int startRow = Integer.parseInt(reportRange
					.split(ReportKeys.UNDERSCORE)[0]);

			subscribedCustList = reportManager.retrieveSubscribedCustomers(
					reportCustomerVO, startRow, ReportKeys.REPORTS_MAX_RECORDS);

		} catch (DBException e) {
			logger.error(
					"Error occured while fetching customer report details", e);
			throw new MISPException(e);
		}

		logger.exiting("generateCustomerReport"); // , subscribedCustList);
		return subscribedCustList;
	}

	/**
	 * This method generates the customer report based on given customer range
	 * 
	 * @param reportCustomerVO
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @return List of all subscribed customers
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public List<CustomerReportDataVO> generateCustomerReportForRenewedPolicies(
			ReportCustomerVO reportCustomerVO, String reportRange)
			throws MISPException {

		logger.entering("generateCustomerReportForRenewedPolicies",
				reportCustomerVO);

		List<CustomerReportDataVO> subscribedCustList = null;

		try {

			int startRow = Integer.parseInt(reportRange
					.split(ReportKeys.UNDERSCORE)[0]);

			subscribedCustList = reportManager
					.retrieveSubscribedCustomersForRenewedPolicies(
							reportCustomerVO, startRow,
							ReportKeys.REPORTS_MAX_RECORDS);

		} catch (DBException e) {
			logger.error(
					"Error occured while fetching customer report details", e);
			throw new MISPException(e);
		}

		logger.exiting("generateCustomerReportForRenewedPolicies"); // ,
																	// subscribedCustList);
		return subscribedCustList;
	}

	/**
	 * This method generates the customer report based on given customer range
	 * 
	 * @param reportCustomerVO
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @return List of all subscribed customers
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public List<CustomerReportDataVO> generateCustomerReportForNewPolicies(
			ReportCustomerVO reportCustomerVO, String reportRange)
			throws MISPException {

		logger.entering("generateCustomerReportForNewPolicies",
				reportCustomerVO);

		List<CustomerReportDataVO> subscribedCustList = null;

		try {

			int startRow = Integer.parseInt(reportRange
					.split(ReportKeys.UNDERSCORE)[0]);

			subscribedCustList = reportManager
					.retrieveSubscribedCustomersForNewPolicies(
							reportCustomerVO, startRow,
							ReportKeys.REPORTS_MAX_RECORDS);

		} catch (DBException e) {
			logger.error(
					"Error occured while fetching customer report details", e);
			throw new MISPException(e);
		}

		logger.exiting("generateCustomerReportForNewPolicies"); // ,
																// subscribedCustList);
		return subscribedCustList;
	}

	/**
	 * This method generates the customer report based on given customer range
	 * 
	 * @param reportRange
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @return List of all subscribed customers
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public List<CustomerSubscription> generateCustomerReport(String reportRange)
			throws MISPException {

		logger.entering("generateCustomerReport", reportRange);

		List<CustomerSubscription> subscribedCustList = null;

		try {

			// Gets the start row
			int startRow = Integer.parseInt(reportRange
					.split(ReportKeys.UNDERSCORE)[0]);

			// Gets the subscribed customers
			subscribedCustList = reportManager.retrieveSubscribedCustomers(
					startRow, ReportKeys.REPORTS_MAX_RECORDS);

		} catch (DBException e) {
			logger.error(
					"Error occured while fetching customer report details", e);
			throw new MISPException(e);
		}

		logger.exiting("generateCustomerReport"); // , subscribedCustList);
		return subscribedCustList;
	}

	/**
	 * This method generates the customer report based on given customer range
	 * 
	 * @param reportRange
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @return List of all subscribed customers
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public List<CustomerReportDataVO> generateCustomerReportOld(
			String reportRange) throws MISPException {

		logger.entering("generateCustomerReport", reportRange);

		List<CustomerReportDataVO> subscribedCustList = null;

		try {

			// Gets the start row
			int startRow = Integer.parseInt(reportRange
					.split(ReportKeys.UNDERSCORE)[0]);

			// Gets the subscribed customers
			subscribedCustList = reportManager.retrieveSubscribedCustomersOld(
					startRow, ReportKeys.REPORTS_MAX_RECORDS);

		} catch (DBException e) {
			logger.error(
					"Error occured while fetching customer report details", e);
			throw new MISPException(e);
		}

		logger.exiting("generateCustomerReport"); // , subscribedCustList);
		return subscribedCustList;
	}

	/**
	 * This method generates the financial report for all subscribed customers
	 * based on user input
	 * 
	 * @param financialReportVO
	 *            {@link ReportFinancialVO} containing user input
	 * 
	 * @return {@link ReportFinancialVO} financial report object
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public ReportFinancialVO generateFinancialReport(
			ReportFinancialVO financialReportVO) throws MISPException {

		logger.entering("generateFinancialReport", financialReportVO);
		// CustomerSubscription custSubsTotalUsage = null;

		/*
		 * try {
		 * 
		 * // Gets usage totals of all subscribed customers custSubsTotalUsage =
		 * reportManager .getSubscribeCustomersUsageTotal();
		 * 
		 * // returns null if there are no subscribed customers if
		 * (custSubsTotalUsage == null) { return null; }
		 * 
		 * // Calculates the financial report Float totalActiveCustomers =
		 * Float.parseFloat(financialReportVO .getTotalActiveCustomers())
		 * custSubsTotalUsage.getTotalCustomerCount(); Float freeSumAssured
		 * =(Float.parseFloat(financialReportVO .getFreeSumAssured()) * 2 *
		 * TypeUtil.handleNull( custSubsTotalUsage.getEarnedCover(), new
		 * Float(0))) / ((float) 100.0); Float paidSumAssured =
		 * (Float.parseFloat(financialReportVO .getPaidSumAssured()) * 2 *
		 * TypeUtil.handleNull( custSubsTotalUsage.getEarnedCover(), new
		 * Float(0))) / (float) 100.0;
		 * 
		 * Float totalCover = null;TypeUtil.handleNull(
		 * custSubsTotalUsage.getEarnedCover(), new Float(0)) +
		 * TypeUtil.handleNull(custSubsTotalUsage.getEarnedCover(), new
		 * Float(0));
		 * 
		 * Float totalPaidCover = (Float.parseFloat(financialReportVO
		 * .getTotalSumAssured()) * totalCover) / (float) 100.0;
		 * 
		 * financialReportVO.setTotalActiveCustomers(totalActiveCustomers
		 * .toString());
		 * financialReportVO.setFreeSumAssured(freeSumAssured.toString());
		 * financialReportVO.setPaidSumAssured(paidSumAssured.toString());
		 * financialReportVO.setTotalSumAssured(totalPaidCover.toString());
		 * 
		 * } catch (DBException e) {
		 * logger.error("Error occured while generating financial report", e);
		 * throw new MISPException(e); }
		 */

		logger.exiting("generateFinancialReport", financialReportVO);
		return financialReportVO;
	}

	/**
	 * This method get the usage total (free cover, paid cover, total cover) for
	 * all subscribed customers.
	 * 
	 * @return {@link CustomerSubscription} object containing customer usage
	 *         totals
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public CustomerSubscription getSubscribedCustomersUsageTotal()
			throws MISPException {

		logger.entering("generateCustomerReport");
		CustomerSubscription custSubsTotalUsage = null;

		try {
			custSubsTotalUsage = reportManager
					.getSubscribeCustomersUsageTotal();
		} catch (DBException e) {
			logger.error(
					"Error occured while calculating all subscribed customers total usage ",
					e);
			throw new MISPException(e);
		}
		return custSubsTotalUsage;
	}

	/**
	 * This method writes the customer report to an excel file
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param reportRange
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public void writeCustomerReport(HSSFWorkbook workbook, String reportRange)
			throws MISPException {

		Object[] params = { workbook, reportRange };

		logger.entering("writeCustomerReport", params);

		List<CustomerSubscription> subscribedCustList = null;
		CustomerSubscription custSubsTotalUsage = null;

		// Gets subscribed customers for selected report range
		subscribedCustList = generateCustomerReport(reportRange);
		// Gets usage total of all subscribed customers
		custSubsTotalUsage = getSubscribedCustomersUsageTotal();

		// Creates a excel work sheet
		HSSFSheet sheet = workbook
				.createSheet(ReportKeys.CUST_REPORT_SHEET_NAME);

		// Format for data
		HSSFCellStyle dataCellFormat = ReportUtil.getDataRowCellStyle(workbook);
		HSSFCellStyle dataCellFormatBold = ReportUtil
				.getDataRowCellStyleBold(workbook);

		int rowCount = 1; // First row
		HSSFRow row = null;
		HSSFCell cell = null;
		/**
		 * 28-Aug-2012 : Removed prevMonUsageCellNo
		 */
		int /* freeCoverCellNo = 0, */earnedCoverCellNo = 0, paidCoverChargesCellNo = 0, totalCoverCellNo = 0;

		// Writes Report Title
		int titleRow = rowCount;
		row = sheet.createRow(rowCount++);
		row.setHeight((short) 500);
		cell = row.createCell(0);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
		cell.setCellValue(ReportKeys.CUST_REPORT_TITLE
				+ ReportKeys.MONTH_NAMES[calendar.get(Calendar.MONTH)]
				+ ReportKeys.SPACE + calendar.get(Calendar.YEAR));
		rowCount++;

		/*
		 * // Writes Business Rule version int brVersionRowCount = rowCount; row
		 * = sheet.createRow(rowCount++); cell = row.createCell(0);
		 * cell.setCellStyle(dataCellFormatBold); BusinessRuleMaster
		 * businessRuleMaster = ((CustomerSubscription) subscribedCustList
		 * .get(0)).getBusinessRuleMaster();
		 * cell.setCellValue(ReportKeys.CUST_REPORT_BR_VERSION +
		 * businessRuleMaster.getBrVersion());
		 */
		cell = row.createCell(1);
		cell.setCellStyle(dataCellFormatBold);
		cell = row.createCell(2);
		cell.setCellStyle(dataCellFormatBold);
		cell = row.createCell(3);
		cell.setCellStyle(dataCellFormatBold);
		cell = row.createCell(4);
		cell.setCellStyle(dataCellFormatBold);

		// Writes Headings
		rowCount++;
		ReportUtil.writeHeadings(workbook, sheet,
				ReportKeys.CUST_REPORT_HEADINGS, rowCount);
		rowCount++;

		// Writes customer report data for all subscribed customers
		for (Iterator<CustomerSubscription> iterator = subscribedCustList
				.iterator(); iterator.hasNext();) {

			CustomerSubscription customerSubscription = iterator.next();
			CustomerDetails customerDetail = customerSubscription
					.getCustomerDetails();

			row = sheet.createRow(rowCount++);

			// Holds column no
			int columnCount = 0;

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(customerDetail.getFname() + ReportKeys.SPACE
					+ customerDetail.getSname());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(customerDetail.getAge());

			// Writes All Insured Relatives details
			Set<InsuredRelativeDetails> insuredRelatives = customerDetail
					.getInsuredRelatives();

			StringBuilder insuredRelativeName = new StringBuilder();
			StringBuilder insuredRelativeAge = new StringBuilder();
			StringBuilder insuredRelativeRelation = new StringBuilder();

			for (Iterator<InsuredRelativeDetails> iterator2 = insuredRelatives
					.iterator(); iterator2.hasNext();) {

				InsuredRelativeDetails insuredRelativeDetails = iterator2
						.next();

				insuredRelativeName.append(insuredRelativeDetails.getFname());
				insuredRelativeName.append(ReportKeys.SPACE);
				insuredRelativeName.append(insuredRelativeDetails.getSname());

				insuredRelativeAge.append(insuredRelativeDetails.getAge());

				insuredRelativeRelation.append(insuredRelativeDetails
						.getCustRelationship());

			}

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(insuredRelativeName.toString());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(insuredRelativeAge.toString());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(insuredRelativeRelation.toString());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(customerDetail.getMsisdn());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(customerSubscription.getRegBy().getUserName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(DateUtil.toDateString(
					customerSubscription.getRegDate(),
					ReportKeys.REPORT_DATE_TIME_FORMAT));

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(customerDetail.getModifiedBy().getUserName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(DateUtil.toDateString(
					customerDetail.getModifiedDate(),
					ReportKeys.REPORT_DATE_TIME_FORMAT));

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			/**
			 * Offer name is displayed in Customer Report only for confirmed
			 * offer subscriptions
			 */
			if (customerSubscription.getProductDetails() != null
					&& customerSubscription.getConfirmed() == (byte) 1) {
				cell.setCellValue(customerSubscription.getProductDetails()
						.getProductName());
			} else {
				cell.setCellValue("");
			}

			earnedCoverCellNo = columnCount;
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(TypeUtil.handleNull(
					customerSubscription.getEarnedCover(), ""));

			BigDecimal earnedCover = null;
			if (customerSubscription.getEarnedCover() != null) {
				earnedCover = customerSubscription.getEarnedCover();
			}

			paidCoverChargesCellNo = columnCount;
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(TypeUtil.handleNull(
					customerSubscription.getCoverCharges(), ""));

			totalCoverCellNo = columnCount;
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);

			cell.setCellValue(TypeUtil.handleNull(earnedCover, ""));

		}

		// Writes the usage totals for subscribed customers
		rowCount++;
		row = sheet.createRow(rowCount++);
		HSSFCellStyle totalCellStyle = ReportUtil
				.getTotalRowCellStyle(workbook);

		cell = row.createCell(0);
		cell.setCellStyle(ReportUtil.getTotalRowCellStyleAlignCenter(workbook));
		cell.setCellValue(ReportKeys.CUST_REPORT_TOTAL_CUSTOMERS
				+ custSubsTotalUsage.getTotalCustomerCount());

		cell = row.createCell(earnedCoverCellNo);
		cell.setCellStyle(totalCellStyle);
		cell.setCellValue(TypeUtil.handleNull(
				custSubsTotalUsage.getEarnedCover(), ""));

		BigDecimal totalFreeCover = null;

		if (null != custSubsTotalUsage.getEarnedCover()) {
			totalFreeCover = custSubsTotalUsage.getEarnedCover();
		}

		cell = row.createCell(paidCoverChargesCellNo);
		cell.setCellStyle(totalCellStyle);
		cell.setCellValue(TypeUtil.handleNull(
				custSubsTotalUsage.getCoverCharges(), ""));

		cell = row.createCell(totalCoverCellNo);
		cell.setCellStyle(totalCellStyle);
		cell.setCellValue(TypeUtil.handleNull(totalFreeCover, ""));

		for (int i = 0; i < totalCoverCellNo; i++) {
			sheet.setColumnWidth(i, 4400);
		}

		// Merges the rows (title row, headings row and total display row)
		sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow, 0,
				totalCoverCellNo));
		/*
		 * sheet.addMergedRegion(new CellRangeAddress(brVersionRowCount,
		 * brVersionRowCount, 0, 4));
		 */
		sheet.addMergedRegion(new CellRangeAddress(--rowCount, rowCount, 0,
				earnedCoverCellNo - 1));

		// Title row range address
		CellRangeAddress titleCellAddress = new CellRangeAddress(titleRow,
				titleRow, 0, totalCoverCellNo);
		// Total row range address
		CellRangeAddress totalCellAddress = new CellRangeAddress(rowCount,
				rowCount, 0, earnedCoverCellNo - 1);

		/**
		 * To implement the style(border) of title row, total row. Some of the
		 * style will not be preserved after merging
		 */
		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());

		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				totalCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				totalCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				totalCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				totalCellAddress, sheet, sheet.getWorkbook());

		logger.exiting("writeCustomerReport");

	}

	/**
	 * This method writes the customer report to an excel file
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param reportRange
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public void writeCustomerReport(HSSFWorkbook workbook,
			ReportCustomerVO reportCustomerVO, String reportRange)
			throws MISPException {

		Object[] params = { workbook, reportCustomerVO, reportRange };

		logger.entering("writeCustomerReport", params);

		List<CustomerReportDataVO> subscribedCustList = null;

		// Gets subscribed customers for selected report range
		subscribedCustList = generateCustomerReport(reportCustomerVO,
				reportRange);

		// Creates a excel work sheet
		HSSFSheet sheet = workbook
				.createSheet(ReportKeys.CUST_REPORT_SHEET_NAME);

		// Format for data
		HSSFCellStyle dataCellFormat = ReportUtil.getDataRowCellStyle(workbook);
		HSSFCellStyle dataCellFormatBold = ReportUtil
				.getDataRowCellStyleBold(workbook);

		int rowCount = 1; // First row
		HSSFRow row = null;
		HSSFCell cell = null;
		/**
		 * 28-Aug-2012 : Removed prevMonUsageCellNo
		 */
		int freeCoverCellNo = 0, paidCoverCellNo = 0, paidCoverChargesCellNo = 0, totalCoverCellNo = 0;

		// Writes Report Title
		int titleRow = rowCount;
		row = sheet.createRow(rowCount++);
		row.setHeight((short) 500);
		cell = row.createCell(0);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
		cell.setCellValue(ReportKeys.CUST_REPORT_TITLE
				+ ReportKeys.MONTH_NAMES[calendar.get(Calendar.MONTH)]
				+ ReportKeys.SPACE + calendar.get(Calendar.YEAR));
		cell.setCellValue(ReportKeys.CUST_REPORT_TITLE + "("
				+ reportCustomerVO.getFromDate().replaceAll("/", "-") + " To "
				+ reportCustomerVO.getToDate().replaceAll("/", "-") + ")");
		rowCount++;

		// Writes Business Rule version
		// int brVersionRowCount = rowCount;
		// row = sheet.createRow(rowCount++);
		// cell = row.createCell(0);
		// cell.setCellStyle(dataCellFormatBold);
		// BusinessRuleMaster businessRuleMaster = busRuleMasterManager
		// .retrieveActiveBusinessRule();
		// cell.setCellValue(ReportKeys.CUST_REPORT_BR_VERSION
		// + businessRuleMaster.getBrVersion());
		// cell = row.createCell(1);
		// cell.setCellStyle(dataCellFormatBold);
		// cell = row.createCell(2);
		// cell.setCellStyle(dataCellFormatBold);
		// cell = row.createCell(3);
		// cell.setCellStyle(dataCellFormatBold);
		// cell = row.createCell(4);
		// cell.setCellStyle(dataCellFormatBold);

		// Writes Headings
		rowCount++;
		ReportUtil.writeHeadings(workbook, sheet,
				ReportKeys.CUST_REPORT_HEADINGS, rowCount);
		rowCount++;
		int startRow = rowCount;
		startRow++;
		CustomerReportDataVO reportDataVO = null;
		// Writes customer report data for all subscribed customers
		for (Iterator<CustomerReportDataVO> iterator = subscribedCustList
				.iterator(); iterator.hasNext();) {

			reportDataVO = iterator.next();

			row = sheet.createRow(rowCount++);

			// Holds column no
			int columnCount = 0;

			// Customer Name
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(reportDataVO.getCustName());

			// Customer Age
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(reportDataVO.getCustAge());

			// Insured Relative Name
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(reportDataVO.getIrdName());

			// Insured Relative Age
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(reportDataVO.getIrdAge());

			// Relationship
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(reportDataVO.getCustRelationship());

			// Customer Mobile Number
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(reportDataVO.getMsisdn());

			// Registered By
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			// cell.setCellValue(reportDataVO.getCreatedBy().getUserName());
			cell.setCellValue(reportDataVO.getCreatedByAll());

			// Registration Date
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			// cell.setCellValue(DateUtil.toDateString(
			// reportDataVO.getCreatedDate(),
			// ReportKeys.REPORT_DATE_TIME_FORMAT));
			cell.setCellValue(reportDataVO.getCreatedDateAll());

			// Modified By
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(reportDataVO.getModifiedBy().getUserName());

			// Modification Date
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(DateUtil.toDateString(
					reportDataVO.getModifiedDate(),
					ReportKeys.REPORT_DATE_TIME_FORMAT));

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			/**
			 * Offer name is displayed in Customer Report only for confirmed
			 * offer subscriptions
			 */
			if (reportDataVO.getProductName() != null) {
				// cell.setCellValue(reportDataVO.getProductName());
				cell.setCellValue(reportDataVO.getProductNameAll());
			} else {
				cell.setCellValue("");
			}

			/*
			 * cell = row.createCell(columnCount++);
			 * cell.setCellStyle(dataCellFormat);
			 * cell.setCellValue(reportDataVO.getStatus().equalsIgnoreCase("0")
			 * ? "ACTIVE" : "INACTIVE");
			 */

			// freeCoverCellNo = columnCount;
			// cell = row.createCell(columnCount++);
			// cell.setCellStyle(dataCellFormat);
			// BigDecimal freeCover = new BigDecimal(0);

			// if (reportDataVO.getEarnedCover() != null) {
			// freeCover = reportDataVO.getEarnedCover();
			// }
			// cell.setCellValue(TypeUtil.handleNull(reportDataVO.getEarnedCover(),
			// new BigDecimal(0.00)).doubleValue());

			paidCoverCellNo = columnCount;
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			// cell.setCellValue(TypeUtil.handleNull(reportDataVO.getEarnedCover(),
			// new BigDecimal(0.00)).doubleValue());
			cell.setCellValue(reportDataVO.getEarnedCoverAll());

			BigDecimal paidCover = new BigDecimal(0);
			if (reportDataVO.getEarnedCover() != null) {
				paidCover = reportDataVO.getEarnedCover();
			}

			paidCoverChargesCellNo = columnCount;
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			// cell.setCellValue(TypeUtil.handleNull(
			// reportDataVO.getCoverCharges(), new BigDecimal(0.00))
			// .doubleValue());
			cell.setCellValue(reportDataVO.getCoverChargesAll());

			totalCoverCellNo = columnCount;
			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);

			// cell.setCellValue(Double.parseDouble(String.valueOf(freeCover
			// .add(paidCover))));
			double token = 0;
			if (reportDataVO.getEarnedCoverAll() != null) {
				StringTokenizer stringTokenizer = new StringTokenizer(
						reportDataVO.getEarnedCoverAll(), ",");
				while (stringTokenizer.hasMoreTokens()) {
					token += Double.parseDouble(stringTokenizer.nextToken());
				}
				cell.setCellValue(Double.parseDouble(String.valueOf(token)));
			} else {
				cell.setCellValue(Double.parseDouble(String.valueOf(token)));
			}

		}

		int lastRow = rowCount;
		String formula = "";
		// Writes the usage totals for subscribed customers
		rowCount++;
		row = sheet.createRow(rowCount++);
		HSSFCellStyle totalCellStyle = ReportUtil
				.getTotalRowCellStyle(workbook);

		cell = row.createCell(0);
		cell.setCellStyle(ReportUtil.getTotalRowCellStyleAlignCenter(workbook));
		cell.setCellValue(ReportKeys.CUST_REPORT_TOTAL_CUSTOMERS
				+ reportCustomerVO.getTotalCustCount());

		formula = new StringBuilder("SUM(")
				.append(ReportUtil.getColumnName(freeCoverCellNo))
				.append(startRow).append(":")
				.append(ReportUtil.getColumnName(freeCoverCellNo))
				.append(lastRow).append(")").toString();
		cell = row.createCell(freeCoverCellNo);
		cell.setCellStyle(totalCellStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);

		formula = new StringBuilder("SUM(")
				.append(ReportUtil.getColumnName(paidCoverCellNo))
				.append(startRow).append(":")
				.append(ReportUtil.getColumnName(paidCoverCellNo))
				.append(lastRow).append(")").toString();
		cell = row.createCell(paidCoverCellNo);
		cell.setCellStyle(totalCellStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);

		formula = new StringBuilder("SUM(")
				.append(ReportUtil.getColumnName(paidCoverChargesCellNo))
				.append(startRow).append(":")
				.append(ReportUtil.getColumnName(paidCoverChargesCellNo))
				.append(lastRow).append(")").toString();
		cell = row.createCell(paidCoverChargesCellNo);
		cell.setCellStyle(totalCellStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);
		/*
		 * cell = row.createCell(paidCoverChargesCellNo);
		 * cell.setCellStyle(totalCellStyle);
		 * cell.setCellValue(TypeUtil.handleNull
		 * (reportDataVO.getPaidCoverCharges(), new BigDecimal(0.00))
		 * .doubleValue());
		 */

		formula = new StringBuilder("SUM(")
				.append(ReportUtil.getColumnName(totalCoverCellNo))
				.append(startRow).append(":")
				.append(ReportUtil.getColumnName(totalCoverCellNo))
				.append(lastRow).append(")").toString();
		cell = row.createCell(totalCoverCellNo);
		cell.setCellStyle(totalCellStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(formula);

		for (int i = 0; i < totalCoverCellNo; i++) {
			sheet.setColumnWidth(i, 4400);
		}

		// Merges the rows (title row, headings row and total display row)
		sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow, 0,
				totalCoverCellNo));
		/*
		 * sheet.addMergedRegion(new CellRangeAddress(brVersionRowCount,
		 * brVersionRowCount, 0, 4));
		 */
		sheet.addMergedRegion(new CellRangeAddress(--rowCount, rowCount, 0,
				totalCoverCellNo));

		// Title row range address
		CellRangeAddress titleCellAddress = new CellRangeAddress(titleRow,
				titleRow, 0, totalCoverCellNo);
		// Total row range address
		CellRangeAddress totalCellAddress = new CellRangeAddress(rowCount,
				rowCount, 0, totalCoverCellNo);

		/**
		 * To implement the style(border) of title row, total row. Some of the
		 * style will not be preserved after merging
		 */
		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());

		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				totalCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				totalCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				totalCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				totalCellAddress, sheet, sheet.getWorkbook());

		logger.exiting("writeCustomerReport");

	}

	/**
	 * This method writes the customer report to an excel file
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param reportRange
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public void writeCustomerReportCSV(File custReportFile,
			ReportCustomerVO reportCustomerVO, String reportRange,
			String userRole) throws MISPException {

		FileOutputStream fileOutStream = null;

		Object[] params = { reportCustomerVO, reportRange, userRole };

		logger.entering("writeCustomerReportCSV", params);

		try {
			List<CustomerReportDataVO> customerReportDataVOList = null;

			// Gets subscribed customers for selected report range
			customerReportDataVOList = generateCustomerReport(reportCustomerVO,
					reportRange);

			if (null != customerReportDataVOList
					&& customerReportDataVOList.size() > 0) {

				logger.info("No of records for selected criteria: ",
						customerReportDataVOList.size());

				fileOutStream = new FileOutputStream(custReportFile);

				String[] headings = ReportKeys.CUST_REPORT_HEADINGS_ALL;
				if ("ROLE_INSURER".equals(userRole)) {

					headings = ReportKeys.CUST_REPORT_HEADINGS_CUST_ID;
				}
				StringBuilder columnRow = new StringBuilder();
				for (String columnName : headings) {

					columnRow.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(columnName)
							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);
				}
				columnRow.deleteCharAt(columnRow.length() - 1).append(
						ReportKeys.CSV_LINE_SEPARATOR);

				fileOutStream.write(columnRow.toString().getBytes());

				StringBuilder dataRow = null;
				for (CustomerReportDataVO cr : customerReportDataVOList) {

					String freeRegBy = null;
					String xlRegBy = null;
					String hpRegBy = null;
					String freeRegDate = null;
					String xlRegDate = null;
					String hpRegDate = null;
					String freeConfDate = null;
					String xlConfDate = null;
					String hpConfDate = null;
					String freeEarnedCover = null;
					String xlEarnedCover = null;
					String hpEarnedCover = null;
					String xlCoverCharges = null;
					String hpCoverCharges = null;
					String insRelativeName = "";
					String insRelativeAge = "";
					String custRelationship = "";

					StringTokenizer stringTokenizer = null;
					double totalCoverCharges = 0;

					stringTokenizer = new StringTokenizer(
							cr.getCoverChargesAll(), ",");
					while (stringTokenizer.hasMoreTokens()) {
						totalCoverCharges += Double.parseDouble(stringTokenizer
								.nextToken());
					}

					String productSubscribedTo = cr.getProductNameAll();

					String[] createdBys = null;
					String[] createdDates = null;
					String[] confDates = null;
					String[] earnedCovers = null;
					String[] coverCharges = null;

					if (productSubscribedTo
							.contains(PlatformConstants.FM_PRODUCT)) {
						if (productSubscribedTo
								.contains(PlatformConstants.XL_PRODUCT)) {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - FM, XL, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = createdBys[1];
								hpRegBy = createdBys[2];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = createdDates[1];
								hpRegDate = createdDates[2];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = confDates[1];
								hpConfDate = confDates[2];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = earnedCovers[1];
								hpEarnedCover = earnedCovers[2];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[1];
								hpCoverCharges = coverCharges[2];

							} else {
								// Product Subscribed - FM, XL
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = createdBys[1];
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = createdDates[1];
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = confDates[1];
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = earnedCovers[1];
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[1];
								hpCoverCharges = "";
							}
						}
						// If customer not subscribed to FreeModel Product.
						else {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - FM, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = "";
								hpRegBy = createdBys[1];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = "";
								hpRegDate = createdDates[1];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = "";
								hpConfDate = confDates[1];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = "";
								hpEarnedCover = earnedCovers[1];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = coverCharges[1];
							} else {
								// Product Subscribed - FM only.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = "";
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = "";
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = "";
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = "";
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = "";
							}
						}
					} else {
						// Customer not subscribed to Free Model.
						if (productSubscribedTo
								.contains(PlatformConstants.XL_PRODUCT)) {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - XL, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = createdBys[0];
								hpRegBy = createdBys[1];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = createdDates[0];
								hpRegDate = createdDates[1];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = confDates[0];
								hpConfDate = confDates[1];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = earnedCovers[0];
								hpEarnedCover = earnedCovers[1];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[0];
								hpCoverCharges = coverCharges[1];
							} else {
								// Product Subscribed - XL alone.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = createdBys[0];
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = createdDates[0];
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = confDates[0];
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = earnedCovers[0];
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[0];
								hpCoverCharges = "";
							}
						} else {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product subscribed - HP alone.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = "";
								hpRegBy = createdBys[0];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = "";
								hpRegDate = createdDates[0];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = "";
								hpConfDate = confDates[0];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = "";
								hpEarnedCover = earnedCovers[0];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = coverCharges[0];
							} else {
								// No Products Subscribed
							}
						}
					}

					if (cr.getIrdName() != null) {
						insRelativeName = cr.getIrdName();
					}
					if (cr.getIrdAge() != null) {
						insRelativeAge = cr.getIrdAge();
					}
					if (cr.getCustRelationship() != null) {
						custRelationship = cr.getCustRelationship();
					}

					dataRow = new StringBuilder().append(cr.getCustName())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(cr.getCustAge())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(insRelativeName)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(insRelativeAge)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(custRelationship)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);

					if (!ReportKeys.ROLE_INSURER.equals(userRole)) {

						dataRow.append(cr.getMsisdn()).append(
								ReportKeys.CSV_FIELD_SEPARATOR);
					}

					dataRow.append(cr.getCustId())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(cr.getModifiedBy().getUserName())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(DateUtil.toDateString(cr.getModifiedDate(),
									ReportKeys.REPORT_DATE_TIME_FORMAT))
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(cr.getProductNameAll())
							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(totalCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);

					dataRow.deleteCharAt(dataRow.length() - 1).append(
							ReportKeys.CSV_LINE_SEPARATOR);

					fileOutStream.write(dataRow.toString().getBytes());
				}

			}
		} catch (IOException ioe) {
			logger.error("Error occurred while downloding customer report.");
			ioe.printStackTrace();
		}
		logger.exiting("writeCustomerReportCSV");

	}

	/**
	 * This method writes the customer report to an csv file
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param reportRange
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public void writeCustomerReportForRenewedPoliciesCSV(File custReportFile,
			ReportCustomerVO reportCustomerVO, String reportRange,
			String userRole) throws MISPException {

		FileOutputStream fileOutStream = null;

		Object[] params = { reportCustomerVO, reportRange, userRole };

		logger.entering("writeCustomerReportForRenewedPoliciesCSV", params);

		try {
			List<CustomerReportDataVO> customerReportDataVOList = null;

			// Gets subscribed customers for selected report range
			customerReportDataVOList = generateCustomerReportForRenewedPolicies(
					reportCustomerVO, reportRange);

			if (null != customerReportDataVOList
					&& customerReportDataVOList.size() > 0) {

				logger.info("No of records for selected criteria: ",
						customerReportDataVOList.size());

				fileOutStream = new FileOutputStream(custReportFile);

				StringBuilder columnRow = new StringBuilder();
				// If the logged in role is insurance company download report
				// only with custId else
				// the report will contain both custId and MSISN
				String[] headings = ReportKeys.CUST_REPORT_HEADINGS_ALL;
				if ("ROLE_INSURER".equals(userRole)) {

					headings = ReportKeys.CUST_REPORT_HEADINGS_CUST_ID;
				}
				for (String columnName : headings) {

					columnRow.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(columnName)
							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);
				}
				columnRow.deleteCharAt(columnRow.length() - 1).append(
						ReportKeys.CSV_LINE_SEPARATOR);

				fileOutStream.write(columnRow.toString().getBytes());

				StringBuilder dataRow = null;
				for (CustomerReportDataVO cr : customerReportDataVOList) {

					String freeRegBy = null;
					String xlRegBy = null;
					String hpRegBy = null;
					String freeRegDate = null;
					String xlRegDate = null;
					String hpRegDate = null;
					String freeConfDate = null;
					String xlConfDate = null;
					String hpConfDate = null;
					String freeEarnedCover = null;
					String xlEarnedCover = null;
					String hpEarnedCover = null;
					String xlCoverCharges = null;
					String hpCoverCharges = null;
					StringTokenizer stringTokenizer = null;
					String insRelativeName = "";
					String insRelativeAge = "";
					String custRelationship = "";
					double totalCoverCharges = 0;

					stringTokenizer = new StringTokenizer(
							cr.getCoverChargesAll(), ",");
					while (stringTokenizer.hasMoreTokens()) {
						totalCoverCharges += Double.parseDouble(stringTokenizer
								.nextToken());
					}

					String productSubscribedTo = cr.getProductNameAll();

					String[] createdBys = null;
					String[] createdDates = null;
					String[] confDates = null;
					String[] earnedCovers = null;
					String[] coverCharges = null;

					if (productSubscribedTo
							.contains(PlatformConstants.FM_PRODUCT)) {
						if (productSubscribedTo
								.contains(PlatformConstants.XL_PRODUCT)) {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - FM, XL, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = createdBys[1];
								hpRegBy = createdBys[2];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = createdDates[1];
								hpRegDate = createdDates[2];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = confDates[1];
								hpConfDate = confDates[2];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = earnedCovers[1];
								hpEarnedCover = earnedCovers[2];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[1];
								hpCoverCharges = coverCharges[2];

							} else {
								// Product Subscribed - FM, XL
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = createdBys[1];
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = createdDates[1];
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = confDates[1];
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = earnedCovers[1];
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[1];
								hpCoverCharges = "";
							}
						}
						// If customer not subscribed to FreeModel Product.
						else {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - FM, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = "";
								hpRegBy = createdBys[1];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = "";
								hpRegDate = createdDates[1];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = "";
								hpConfDate = confDates[1];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = "";
								hpEarnedCover = earnedCovers[1];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = coverCharges[1];
							} else {
								// Product Subscribed - FM only.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = "";
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = "";
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = "";
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = "";
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = "";
							}
						}
					} else {
						// Customer not subscribed to Free Model.
						if (productSubscribedTo
								.contains(PlatformConstants.XL_PRODUCT)) {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - XL, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = createdBys[0];
								hpRegBy = createdBys[1];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = createdDates[0];
								hpRegDate = createdDates[1];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = confDates[0];
								hpConfDate = confDates[1];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = earnedCovers[0];
								hpEarnedCover = earnedCovers[1];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = earnedCovers[0];
								hpCoverCharges = earnedCovers[1];
							} else {
								// Product Subscribed - XL alone.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = createdBys[0];
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = createdDates[0];
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = confDates[0];
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = earnedCovers[0];
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[0];
								hpCoverCharges = "";
							}
						} else {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product subscribed - HP alone.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = "";
								hpRegBy = createdBys[0];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = "";
								hpRegDate = createdDates[0];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = "";
								hpConfDate = confDates[0];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = "";
								hpEarnedCover = earnedCovers[0];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = coverCharges[0];
							} else {
								// No Products Subscribed
							}
						}
					}

					if (cr.getIrdName() != null) {
						insRelativeName = cr.getIrdName();
					}
					if (cr.getIrdAge() != null) {
						insRelativeAge = cr.getIrdAge();
					}
					if (cr.getCustRelationship() != null) {
						custRelationship = cr.getCustRelationship();
					}

					dataRow = new StringBuilder().append(cr.getCustName())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(cr.getCustAge())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(insRelativeName)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(insRelativeAge)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(custRelationship)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);

					if (!"ROLE_INSURER".equals(userRole)) {

						dataRow.append(cr.getMsisdn()).append(
								ReportKeys.CSV_FIELD_SEPARATOR);
					}

					dataRow.append(cr.getCustId())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(cr.getModifiedBy().getUserName())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(DateUtil.toDateString(cr.getModifiedDate(),
									ReportKeys.REPORT_DATE_TIME_FORMAT))
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(cr.getProductNameAll())
							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(totalCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);

					dataRow.deleteCharAt(dataRow.length() - 1).append(
							ReportKeys.CSV_LINE_SEPARATOR);

					fileOutStream.write(dataRow.toString().getBytes());
				}

			}
		} catch (IOException ioe) {
			logger.error("Error occurred while downloding customer report for"
					+ "renewed policies.");
			ioe.printStackTrace();
		}
		logger.exiting("writeCustomerReportForRenewedPoliciesCSV");
	}

	/**
	 * This method writes the customer report to an csv file
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param reportRange
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public void writeCustomerReportForNewPoliciesCSV(File custReportFile,
			ReportCustomerVO reportCustomerVO, String reportRange,
			String userRole) throws MISPException {

		FileOutputStream fileOutStream = null;

		Object[] params = { reportCustomerVO, reportRange, userRole };

		logger.entering("writeCustomerReportForNewPoliciesCSV", params);

		try {
			List<CustomerReportDataVO> customerReportDataVOList = null;

			// Gets subscribed customers for selected report range
			customerReportDataVOList = generateCustomerReportForNewPolicies(
					reportCustomerVO, reportRange);

			if (null != customerReportDataVOList
					&& customerReportDataVOList.size() > 0) {

				logger.info("No of records for selected criteria: ",
						customerReportDataVOList.size());

				fileOutStream = new FileOutputStream(custReportFile);

				StringBuilder columnRow = new StringBuilder();
				// If the logged in role is insurance company download report
				// only with custId else
				// the report will contain both custId and MSISN
				String[] headings = ReportKeys.CUST_REPORT_HEADINGS_ALL;
				if ("ROLE_INSURER".equals(userRole)) {

					headings = ReportKeys.CUST_REPORT_HEADINGS_CUST_ID;
				}
				for (String columnName : headings) {

					columnRow.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(columnName)
							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);
				}
				columnRow.deleteCharAt(columnRow.length() - 1).append(
						ReportKeys.CSV_LINE_SEPARATOR);

				fileOutStream.write(columnRow.toString().getBytes());

				StringBuilder dataRow = null;
				for (CustomerReportDataVO cr : customerReportDataVOList) {

					String freeRegBy = null;
					String xlRegBy = null;
					String hpRegBy = null;
					String freeRegDate = null;
					String xlRegDate = null;
					String hpRegDate = null;
					String freeConfDate = null;
					String xlConfDate = null;
					String hpConfDate = null;
					String freeEarnedCover = null;
					String xlEarnedCover = null;
					String hpEarnedCover = null;
					String xlCoverCharges = null;
					String hpCoverCharges = null;
					StringTokenizer stringTokenizer = null;
					String insRelativeName = "";
					String insRelativeAge = "";
					String custRelationship = "";
					double totalCoverCharges = 0;

					stringTokenizer = new StringTokenizer(
							cr.getCoverChargesAll(), ",");
					while (stringTokenizer.hasMoreTokens()) {
						totalCoverCharges += Double.parseDouble(stringTokenizer
								.nextToken());
					}

					String productSubscribedTo = cr.getProductNameAll();

					String[] createdBys = null;
					String[] createdDates = null;
					String[] confDates = null;
					String[] earnedCovers = null;
					String[] coverCharges = null;

					if (productSubscribedTo
							.contains(PlatformConstants.FM_PRODUCT)) {
						if (productSubscribedTo
								.contains(PlatformConstants.XL_PRODUCT)) {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - FM, XL, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = createdBys[1];
								hpRegBy = createdBys[2];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = createdDates[1];
								hpRegDate = createdDates[2];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = confDates[1];
								hpConfDate = confDates[2];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = earnedCovers[1];
								hpEarnedCover = earnedCovers[2];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[1];
								hpCoverCharges = coverCharges[2];

							} else {
								// Product Subscribed - FM, XL
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = createdBys[1];
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = createdDates[1];
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = confDates[1];
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = earnedCovers[1];
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[1];
								hpCoverCharges = "";
							}
						}
						// If customer not subscribed to FreeModel Product.
						else {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - FM, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = "";
								hpRegBy = createdBys[1];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = "";
								hpRegDate = createdDates[1];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = "";
								hpConfDate = confDates[1];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = "";
								hpEarnedCover = earnedCovers[1];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = coverCharges[1];
							} else {
								// Product Subscribed - FM only.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = createdBys[0];
								xlRegBy = "";
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = createdDates[0];
								xlRegDate = "";
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = confDates[0];
								xlConfDate = "";
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = earnedCovers[0];
								xlEarnedCover = "";
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = "";
							}
						}
					} else {
						// Customer not subscribed to Free Model.
						if (productSubscribedTo
								.contains(PlatformConstants.XL_PRODUCT)) {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product Subscribed - XL, HP
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = createdBys[0];
								hpRegBy = createdBys[1];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = createdDates[0];
								hpRegDate = createdDates[1];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = confDates[0];
								hpConfDate = confDates[1];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = earnedCovers[0];
								hpEarnedCover = earnedCovers[1];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[0];
								hpCoverCharges = coverCharges[1];
							} else {
								// Product Subscribed - XL alone.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = createdBys[0];
								hpRegBy = "";

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = createdDates[0];
								hpRegDate = "";

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = confDates[0];
								hpConfDate = "";

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = earnedCovers[0];
								hpEarnedCover = "";

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = coverCharges[0];
								hpCoverCharges = "";
							}
						} else {
							if (productSubscribedTo
									.contains(PlatformConstants.HP_PRODUCT)) {
								// Product subscribed - HP alone.
								createdBys = cr.getCreatedByAll().split(",");
								freeRegBy = "";
								xlRegBy = "";
								hpRegBy = createdBys[0];

								createdDates = cr.getCreatedDateAll()
										.split(",");
								freeRegDate = "";
								xlRegDate = "";
								hpRegDate = createdDates[0];

								confDates = cr.getConfDateAll().split(",");
								freeConfDate = "";
								xlConfDate = "";
								hpConfDate = confDates[0];

								earnedCovers = cr.getEarnedCoverAll()
										.split(",");
								freeEarnedCover = "";
								xlEarnedCover = "";
								hpEarnedCover = earnedCovers[0];

								coverCharges = cr.getCoverChargesAll().split(
										",");
								xlCoverCharges = "";
								hpCoverCharges = coverCharges[0];
							} else {
								// No Products Subscribed
							}
						}
					}

					if (cr.getIrdName() != null) {
						insRelativeName = cr.getIrdName();
					}
					if (cr.getIrdAge() != null) {
						insRelativeAge = cr.getIrdAge();
					}
					if (cr.getCustRelationship() != null) {
						custRelationship = cr.getCustRelationship();
					}

					dataRow = new StringBuilder().append(cr.getCustName())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(cr.getCustAge())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(insRelativeName)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(insRelativeAge)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(custRelationship)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);

					if (!"ROLE_INSURER".equals(userRole)) {

						dataRow.append(cr.getMsisdn()).append(
								ReportKeys.CSV_FIELD_SEPARATOR);
					}

					dataRow.append(cr.getCustId())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpRegBy)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpRegDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpConfDate)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(cr.getModifiedBy().getUserName())
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(DateUtil.toDateString(cr.getModifiedDate(),
									ReportKeys.REPORT_DATE_TIME_FORMAT))
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(cr.getProductNameAll())
							.append(ReportKeys.CSV_QUOTE_CHARACTER)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(freeEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpEarnedCover)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(xlCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(hpCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR)

							.append(totalCoverCharges)
							.append(ReportKeys.CSV_FIELD_SEPARATOR);

					dataRow.deleteCharAt(dataRow.length() - 1).append(
							ReportKeys.CSV_LINE_SEPARATOR);

					fileOutStream.write(dataRow.toString().getBytes());
				}

			}
		} catch (IOException ioe) {
			logger.error("Error occurred while downloding customer report for"
					+ "new policies.");
			ioe.printStackTrace();
		}
		logger.exiting("writeCustomerReportForNewPoliciesCSV");
	}

	/**
	 * This method writes the customer report to an excel file
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param reportRange
	 *            Holds the range in "startRow_endRow_reportNo" format for which
	 *            report needs to be generated.
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public void writeUserReport(HSSFWorkbook workbook,
			Map<Integer, Integer> currentYearLeaveMap,
			Map<Integer, Integer> currentMonthLeaveMap) throws MISPException {

		Object[] params = { workbook, currentYearLeaveMap, currentMonthLeaveMap };
		logger.entering("writeUserReport", params);

		List<UserDetails> userList = null;
		try {
			userList = userManager.getUserDetailsList();

		} catch (DBException exception) {
			logger.error("An exception occured while fetching list of "
					+ "User Details.", exception);
			throw new MISPException(exception);
		}

		// Creates a excel work sheet
		HSSFSheet sheet = workbook
				.createSheet(ReportKeys.USER_REPORT_SHEET_NAME);

		// Format for data
		HSSFCellStyle dataCellFormat = ReportUtil.getDataRowCellStyle(workbook);

		int rowCount = 1; // First row
		HSSFRow row = null;
		HSSFCell cell = null;

		// Writes Headings
		ReportUtil.writeHeadings(workbook, sheet,
				ReportKeys.USER_REPORT_HEADINGS, rowCount);
		rowCount++;

		// Writes user report data
		for (Iterator<UserDetails> iterator = userList.iterator(); iterator
				.hasNext();) {

			UserDetails userDetails = iterator.next();

			row = sheet.createRow(rowCount++);

			// Holds column no
			int columnCount = 0;

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(userDetails.getUserUid());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(userDetails.getUserName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(userDetails.getMsisdn());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(DateUtil.toDateString(userDetails.getDob(),
					ReportKeys.REPORT_DATE_FORMAT));

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(userDetails.getAge());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(userDetails.getGender());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			RoleMaster roleMaster = userDetails.getRoleMaster();
			if (roleMaster != null) {
				cell.setCellValue(roleMaster.getRoleName());
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			BranchDetails branchDetails = userDetails.getBranchDetails();
			if (branchDetails != null) {
				cell.setCellValue(branchDetails.getBranchName());
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(DateUtil.toDateString(
					userDetails.getCreatedDate(),
					ReportKeys.REPORT_DATE_TIME_FORMAT));

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(currentMonthLeaveMap.get(userDetails.getUserId()));

			cell = row.createCell(columnCount++);
			cell.setCellStyle(dataCellFormat);
			cell.setCellValue(currentYearLeaveMap.get(userDetails.getUserId()));
		}

		/**
		 * Customize the width to all columns.
		 */
		sheet.setColumnWidth(0, 2000); /* User ID */
		sheet.setColumnWidth(1, 7500); /* User Name */
		sheet.setColumnWidth(2, 3500); /* Mobile Number */
		sheet.setColumnWidth(3, 3500); /* Date of Birth */
		sheet.setColumnWidth(4, 2000); /* Age */
		sheet.setColumnWidth(5, 2100); /* Gender */
		sheet.setColumnWidth(6, 5500); /* Role */
		sheet.setColumnWidth(7, 4000); /* Branch */
		sheet.setColumnWidth(8, 4400); /* Registered Date */
		sheet.setColumnWidth(9, 2500); /* Current Month Leave */
		sheet.setColumnWidth(10, 2500); /* Current Year Leave */

		logger.exiting("writeUserReport");
	}

	/**
	 * This method writes the financial report data to the excel workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param reportFinancialVo
	 *            {@link ReportFinancialVO} object containing report data
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public void writeFinancialReport(HSSFWorkbook workbook,
			ReportFinancialVO reportFinancialVo) throws MISPException {

		logger.entering("writeFinancialReport", reportFinancialVo);

		HSSFSheet sheet = workbook
				.createSheet(ReportKeys.FINANCIAL_REP_SHEET_NAME);
		sheet.setColumnWidth(0, 10000);
		sheet.setColumnWidth(1, 6000);

		// Format for data
		HSSFCellStyle dataCellFormat = ReportUtil.getDataRowCellStyle(workbook);
		HSSFCellStyle totalCellStyle = ReportUtil
				.getDataRowCellStyleBold(workbook);

		int rowCount = 1;
		HSSFRow row = null;
		HSSFCell cell = null;

		// Writes Report Title
		int titleRow = rowCount;

		row = sheet.createRow(rowCount++);
		row.setHeight((short) 500);
		cell = row.createCell(0);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
		cell.setCellValue(ReportKeys.FINANCIAL_REPORT_TITLE
				+ ReportKeys.MONTH_NAMES[calendar.get(Calendar.MONTH)] + " "
				+ calendar.get(Calendar.YEAR));

		cell = row.createCell(1);
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
		// Merges the title row
		sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow, 0, 1));
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));

		rowCount++;

		// Writes financial report data
		row = sheet.createRow(rowCount++);
		cell = row.createCell(0);
		cell.setCellStyle(dataCellFormat);
		cell.setCellValue(ReportKeys.FINANCIAL_REP_ACTIVE_CUST);

		cell = row.createCell(1);
		cell.setCellStyle(totalCellStyle);
		cell.setCellValue(reportFinancialVo.getTotalActiveCustomers() + " "
				+ ReportKeys.CURRENCY);

		row = sheet.createRow(rowCount++);
		cell = row.createCell(0);
		cell.setCellStyle(dataCellFormat);
		cell.setCellValue(ReportKeys.FINANCIAL_REP_FREE_SUM);

		cell = row.createCell(1);
		cell.setCellStyle(totalCellStyle);
		cell.setCellValue(reportFinancialVo.getFreeSumAssured() + " "
				+ ReportKeys.CURRENCY);

		row = sheet.createRow(rowCount++);
		cell = row.createCell(0);
		cell.setCellStyle(dataCellFormat);
		cell.setCellValue(ReportKeys.FINANCIAL_REP_PAID_SUM);

		cell = row.createCell(1);
		cell.setCellStyle(totalCellStyle);
		cell.setCellValue(reportFinancialVo.getPaidSumAssured() + " "
				+ ReportKeys.CURRENCY);

		row = sheet.createRow(rowCount++);
		cell = row.createCell(0);
		cell.setCellStyle(dataCellFormat);
		cell.setCellValue(ReportKeys.FINANCIAL_REP_TOTAL_SUM);

		cell = row.createCell(1);
		cell.setCellStyle(totalCellStyle);
		cell.setCellValue(reportFinancialVo.getTotalSumAssured() + " "
				+ ReportKeys.CURRENCY);

		logger.exiting("writeFinancialReport");

	}

	/**
	 * This method returns the customer ranges based on the total number of
	 * customers passed as parameter. If the total number of customer is 30,000,
	 * the ranges will be
	 * 
	 * <pre>
	 * 1-10000
	 * 10001-20000
	 * 20001-30000
	 * </pre>
	 * 
	 * @param totalCustomers
	 *            The total number of customer for which range needs to be
	 *            calculated
	 * 
	 * @return <code>Map</code> of customer ranges with key as
	 *         "startRow_endRow_reportNo" and value as "startRow - endRow"
	 * 
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public Map<String, String> getCustomerRanges(
			ReportCustomerVO customerReportVO) throws MISPException {

		logger.entering("getCustomerRanges", customerReportVO);

		// Holds customer ranges
		Map<String, String> custRangeMap = new LinkedHashMap<String, String>();

		try {

			// Returns null if there are no subscribed customers
			// Gets the total number of subscribed customers
			int totalCount = reportManager
					.getTotalCustomerCount(customerReportVO);

			logger.debug("total customer count : ", totalCount);

			int totalCustomers = totalCount;

			// Returns null if there are no subscribed customers
			if (totalCustomers == 0) {
				return null;
			}

			// Calculates the ranges
			int custRange = ReportKeys.REPORTS_MAX_RECORDS;
			int intialRange = 1;
			int finalRange = custRange;

			int count = 1;

			do {
				if (count > 1) {
					finalRange = (count * custRange);
				}
				if (finalRange > totalCustomers) {
					finalRange = totalCustomers;
				}
				custRangeMap.put((intialRange + ReportKeys.UNDERSCORE
						+ finalRange + ReportKeys.UNDERSCORE + count),
						intialRange + " - " + (finalRange));

				intialRange += custRange;
				count++;

			} while (intialRange <= totalCustomers);

		} catch (Exception e) {
			logger.error("Exception occured while calculating the ranges for total customers subscribed : "
					+ e);
			throw new MISPException(e);
		}

		logger.exiting("getCustomerRanges", custRangeMap);
		return custRangeMap;

	}

	/**
	 * This method returns the customer ranges based on the total number of
	 * customers passed as parameter. If the total number of customer is 30,000,
	 * the ranges will be
	 * 
	 * <pre>
	 * 1-10000
	 * 10001-20000
	 * 20001-30000
	 * </pre>
	 * 
	 * @param totalCustomers
	 *            The total number of customer for which range needs to be
	 *            calculated
	 * 
	 * @return <code>Map</code> of customer ranges with key as
	 *         "startRow_endRow_reportNo" and value as "startRow - endRow"
	 * 
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public Map<String, String> getCustomerRangesForRenewedPolicies(
			ReportCustomerVO customerReportVO) throws MISPException {

		logger.entering("getCustomerRangesForRenewedPolicies", customerReportVO);

		// Holds customer ranges
		Map<String, String> custRangeMap = new LinkedHashMap<String, String>();

		try {

			// Returns null if there are no subscribed customers
			// Gets the total number of subscribed customers
			int totalCount = reportManager
					.getTotalCustomerCountForRenewedPolicies(customerReportVO);

			logger.debug("total customer count : ", totalCount);

			int totalCustomers = totalCount;

			// Returns null if there are no subscribed customers
			if (totalCustomers == 0) {
				return null;
			}

			// Calculates the ranges
			int custRange = ReportKeys.REPORTS_MAX_RECORDS;
			int intialRange = 1;
			int finalRange = custRange;

			int count = 1;

			do {
				if (count > 1) {
					finalRange = (count * custRange);
				}
				if (finalRange > totalCustomers) {
					finalRange = totalCustomers;
				}
				custRangeMap.put((intialRange + ReportKeys.UNDERSCORE
						+ finalRange + ReportKeys.UNDERSCORE + count),
						intialRange + " - " + (finalRange));

				intialRange += custRange;
				count++;

			} while (intialRange <= totalCustomers);

		} catch (Exception e) {
			logger.error("Exception occured while calculating the ranges for total customers subscribed : "
					+ e);
			throw new MISPException(e);
		}

		logger.exiting("getCustomerRangesForRenewedPolicies", custRangeMap);
		return custRangeMap;
	}

	/**
	 * This method returns the customer ranges based on the total number of
	 * customers passed as parameter. If the total number of customer is 30,000,
	 * the ranges will be
	 * 
	 * <pre>
	 * 1-10000
	 * 10001-20000
	 * 20001-30000
	 * </pre>
	 * 
	 * @param totalCustomers
	 *            The total number of customer for which range needs to be
	 *            calculated
	 * 
	 * @return <code>Map</code> of customer ranges with key as
	 *         "startRow_endRow_reportNo" and value as "startRow - endRow"
	 * 
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public Map<String, String> getCustomerRangesForNewPolicies(
			ReportCustomerVO customerReportVO) throws MISPException {

		logger.entering("getCustomerRangesForNewPolicies", customerReportVO);

		// Holds customer ranges
		Map<String, String> custRangeMap = new LinkedHashMap<String, String>();

		try {

			// Returns null if there are no subscribed customers
			// Gets the total number of subscribed customers
			int totalCount = reportManager
					.getTotalCustomerCountForNewPolicies(customerReportVO);

			logger.debug("total customer count : ", totalCount);

			int totalCustomers = totalCount;

			// Returns null if there are no subscribed customers
			if (totalCustomers == 0) {
				return null;
			}

			// Calculates the ranges
			int custRange = ReportKeys.REPORTS_MAX_RECORDS;
			int intialRange = 1;
			int finalRange = custRange;

			int count = 1;

			do {
				if (count > 1) {
					finalRange = (count * custRange);
				}
				if (finalRange > totalCustomers) {
					finalRange = totalCustomers;
				}
				custRangeMap.put((intialRange + ReportKeys.UNDERSCORE
						+ finalRange + ReportKeys.UNDERSCORE + count),
						intialRange + " - " + (finalRange));

				intialRange += custRange;
				count++;

			} while (intialRange <= totalCustomers);

		} catch (Exception e) {
			logger.error("Exception occured while calculating the ranges for total customers subscribed : "
					+ e);
			throw new MISPException(e);
		}

		logger.exiting("getCustomerRangesForNewPolicies", custRangeMap);
		return custRangeMap;
	}

	/**
	 * This method return the list of active Users.
	 * 
	 * @return <code>List</code> of UserDetails
	 * 
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public List<UserDetails> getUserDetailList(ReportAgentVO agentReportVO,
			int noOfRoles) throws MISPException {
		logger.entering("getUserDetailList");
		List<UserDetails> userDetailList = null;

		try {
			// Changed to include USSD details also
			userDetailList = this.userManager.getUserDetailsListWithUSSD(
					agentReportVO, noOfRoles);
		} catch (DBException exception) {
			logger.error("Error occured while getting list of all users ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getUserDetailList");
		return userDetailList;
	}

	/**
	 * This method return the freemium details.
	 * 
	 * @param agentReportVO
	 *            {@link ReportAgentVO} object containing user details
	 * @return <code>Map</code> of freemium data with key as "userId" and value
	 *         as <code>List</code> of user offer registration details
	 */
	public Map<Integer, List<UserOfferRegistrationVO>> getOfferRegDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getOfferRegDetails");

		List<UserOfferRegistrationVO> subscribedOfferDetails = null;

		// Holds the list of freemium details for each user
		HashMap<Integer, List<UserOfferRegistrationVO>> offerDetailsMap = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		// Holds the freemium details
		List<UserOfferRegistrationVO> offerDetailsList = null;
		try {
			subscribedOfferDetails = offerDetailsMgr
					.retrieveSubscribedOfferDetails(fromDate, toDate);
			offerDetailsMap = new HashMap<Integer, List<UserOfferRegistrationVO>>();
			for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
				offerDetailsList = new ArrayList<UserOfferRegistrationVO>();
				for (UserOfferRegistrationVO userOfferReg : subscribedOfferDetails) {
					if (userDetails.getUserId() == userOfferReg.getOfferRegBy()) {
						offerDetailsList.add(userOfferReg);
					}
				}
				offerDetailsMap.put(userDetails.getUserId(), offerDetailsList);
			}
		} catch (DBException exception) {
			logger.error(
					"Error occured while getting offer registration details ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getOfferRegDetails");
		return offerDetailsMap;
	}

	/**
	 * This method returns all weeks report within the given date range
	 * 
	 * @param agentReportVO
	 *            {@link ReportAgentVO} object containing date range
	 * @return <code>Map</code> of all weeks data with key as "weekNo" and value
	 *         as <code>List</code> of daily data
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public Map<Integer, List<Map<String, List<ReportDailyNewVO>>>> getAllWeeksReport(
			ReportAgentVO agentReportVO) throws MISPException {
		Object[] params = { agentReportVO };
		logger.entering("getAllWeeksReport", params);

		/**
		 * Holds data for each day within fromDate and toDate with date as key
		 * and list of data for that date as value
		 */
		Map<String, List<ReportDailyNewVO>> dailyReportMap = null;

		/**
		 * Holds data for all weeks within fromDate and toDate with week of year
		 * as key and list of each day data as value
		 */
		Map<Integer, List<Map<String, List<ReportDailyNewVO>>>> allWeeksReport;
		try {
			Date now = null;
			Calendar start = Calendar.getInstance();
			start.setTime(DateUtil.toDate(agentReportVO.getFromDate()));
			Calendar end = Calendar.getInstance();
			end.setTime(DateUtil.toDate(agentReportVO.getToDate()));

			allWeeksReport = new HashMap<Integer, List<Map<String, List<ReportDailyNewVO>>>>();

			for (; !start.after(end); start.add(Calendar.DATE, 1)) {
				now = start.getTime();

				List<ReportDailyNewVO> dailyReportList = reportManager
						.getDetailedReportForDate(now);

				dailyReportMap = new HashMap<String, List<ReportDailyNewVO>>();
				dailyReportMap.put(DateUtil.toDateString(now), dailyReportList);

				// Segregates daily data into weeks

				// Commented to include all 7 days of week
				// if(dailyReportMap.get(DateUtil.toDateString(now)).size() > 0)
				// {
				if (allWeeksReport.containsKey(DateUtil.getISOWeekNumber(now)))
					allWeeksReport.get(DateUtil.getISOWeekNumber(now)).add(
							dailyReportMap);
				else {
					List<Map<String, List<ReportDailyNewVO>>> dailyReportMapList = new ArrayList<Map<String, List<ReportDailyNewVO>>>();
					dailyReportMapList.add(dailyReportMap);

					allWeeksReport.put(DateUtil.getISOWeekNumber(now),
							dailyReportMapList);
				}
				// }
			}
		} catch (DBException e) {
			logger.error(
					"Error occured while getting agent report details for the given range",
					e);
			throw new MISPException(e);
		}

		logger.exiting("getAllWeeksReport");
		return allWeeksReport;
	}

	/**
	 * This method returns all weeks report within the given date range
	 * 
	 * @param agentReportVO
	 *            {@link ReportAgentVO} object containing date range
	 * @return <code>Map</code> of all weeks data with key as "weekNo" and value
	 *         as <code>List</code> of daily data
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public Map<String, ReportDailyNewVO> getDurationReport(
			ReportAgentVO agentReportVO) throws MISPException {
		Object[] params = { agentReportVO };
		logger.entering("getDurationReport", params);

		/**
		 * Holds data for all weeks within fromDate and toDate with week of year
		 * as key and list of each day data as value
		 */
		Map<String, ReportDailyNewVO> dailyReportMap = new HashMap<String, ReportDailyNewVO>();
		try {

			Date fromDate = DateUtil.toDate(agentReportVO.getFromDate());
			Date toDate = DateUtil.toDate(agentReportVO.getToDate());

			dailyReportMap = reportManager.getDetailedReportForDuration(
					fromDate, toDate);

		} catch (DBException e) {
			logger.error(
					"Error occured while getting agent report details for the given range",
					e);
			throw new MISPException(e);
		}

		logger.exiting("getDurationReport");
		return dailyReportMap;
	}

	/**
	 * This method returns all deducted clients count for each user within the
	 * given date range
	 * 
	 * @param agentReportVO
	 *            {@link ReportAgentVO} object containing date range
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public Map<String, ReportDailyNewVO> getDeductionReport(
			ReportAgentVO agentReportVO) throws MISPException {
		Object[] params = { agentReportVO };
		logger.entering("getDeductionReport", params);

		/**
		 * Holds data for all weeks within fromDate and toDate with week of year
		 * as key and list of each day data as value
		 */
		Map<String, ReportDailyNewVO> deductionReportMap = new HashMap<String, ReportDailyNewVO>();
		try {

			Date fromDate = DateUtil.toDate(agentReportVO.getFromDate());
			Date toDate = DateUtil.toDate(agentReportVO.getToDate());

			// getting 3 months back date for confirmation date range
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fromDate);
			calendar.add(Calendar.MONTH, -3);
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date confDate = calendar.getTime();
			String strDate = formatter.format(confDate);

			Date confRangeDate = DateUtil.toDate(strDate);

			deductionReportMap = reportManager.getDetailedReportForDeduction(
					fromDate, toDate, confRangeDate);

		} catch (DBException e) {
			logger.error(
					"Error occured while getting agent report details for the given range",
					e);
			throw new MISPException(e);
		}

		logger.exiting("getDeductionReport");
		return deductionReportMap;
	}

	/**
	 * This method writes the agent report data to the excel workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param agentReportVO
	 *            {@link ReportAgentVO} object containing report data
	 * 
	 * @param filterOptionType
	 *            type of report
	 * 
	 * @throws MISPException
	 *             if any Exception occurs
	 * 
	 */
	public void writeAgentReport(HSSFWorkbook workbook,
			ReportAgentVO agentReportVO, int filterOptionType)
			throws MISPException {
		Object[] params = { agentReportVO, filterOptionType };
		logger.entering("writeAgentReport", params);

		try {
			if (filterOptionType != -1) {
				int weekNo;
				List<Integer> weeks = null;
				int sheetIndex = 0;
				switch (filterOptionType) {
				case 0:
					// Date Range
					weeks = new ArrayList<Integer>(agentReportVO
							.getAllWeeksReport().keySet());
					for (int i = 0; i < agentReportVO.getAllWeeksReport()
							.size(); i++) {
						weekNo = weeks.get(i);
						if (agentReportVO.getAllWeeksReport().size() > 0)
							;
						writeAgentSheet(workbook, weekNo, agentReportVO);
					}
					// Orders sheets of excel workbook
					Collections.sort(weeks);
					sheetIndex = 0;
					for (int week : weeks) {
						String sheetName = ReportKeys.AGENT_REP_SHEET_NAME
								+ week;
						workbook.setSheetOrder(sheetName, sheetIndex++);
					}
					break;
				case 1:
					// Current Week
					weekNo = DateUtil.getISOWeekNumber(new Date());
					if (agentReportVO.getAllWeeksReport().size() > 0)
						writeAgentSheet(workbook, weekNo, agentReportVO);
					break;
				case 2:
					// Previous Week
					weekNo = DateUtil.getISOWeekNumber(new Date());
					if (agentReportVO.getAllWeeksReport().size() > 0)
						writeAgentSheet(workbook, weekNo - 1, agentReportVO);
					break;
				case 3:
					// Current Month
					weeks = new ArrayList<Integer>(agentReportVO
							.getAllWeeksReport().keySet());
					for (int i = 0; i < agentReportVO.getAllWeeksReport()
							.size(); i++) {
						weekNo = weeks.get(i);
						if (agentReportVO.getAllWeeksReport().size() > 0)
							writeAgentSheet(workbook, weekNo, agentReportVO);
					}
					// Orders sheets of excel workbook
					Collections.sort(weeks);
					sheetIndex = 0;
					for (int week : weeks) {
						String sheetName = ReportKeys.AGENT_REP_SHEET_NAME
								+ week;
						workbook.setSheetOrder(sheetName, sheetIndex++);
					}
					break;

				case 4:
					// Previous Month
					weeks = new ArrayList<Integer>(agentReportVO
							.getAllWeeksReport().keySet());
					for (int i = 0; i < agentReportVO.getAllWeeksReport()
							.size(); i++) {
						weekNo = weeks.get(i);
						if (agentReportVO.getAllWeeksReport().size() > 0)
							writeAgentSheet(workbook, weekNo, agentReportVO);
					}
					// Orders sheets of excel workbook
					Collections.sort(weeks);
					sheetIndex = 0;
					for (int week : weeks) {
						String sheetName = ReportKeys.AGENT_REP_SHEET_NAME
								+ week;
						workbook.setSheetOrder(sheetName, sheetIndex++);
					}
					break;
				}
			}
		} catch (Exception e) {
			logger.error("Error occured while writing agent report", e);
			throw new MISPException(e);
		}
		logger.exiting("writeAgentReport");
	}

	/**
	 * This method writes the agent report data to the excel workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param agentReportVO
	 *            {@link ReportAgentVO} object containing report data
	 * 
	 * @param filterOptionType
	 *            type of report
	 * 
	 * @throws MISPException
	 *             if any Exception occurs
	 * 
	 */
	public void writeAgentSalaryReport(HSSFWorkbook workbook,
			ReportAgentVO agentReportVO, int filterOptionType)
			throws MISPException {
		Object[] params = { agentReportVO, filterOptionType };
		logger.entering("writeAgentSalaryReport", params);

		try {
			writeAgentSheet(workbook, agentReportVO);
		} catch (Exception e) {
			logger.error("Error occured while writing agent report", e);
			throw new MISPException(e);
		}
		logger.exiting("writeAgentSalaryReport");
	}

	/**
	 * This method writes the agent report data to the excel workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param agentReportVO
	 *            {@link ReportAgentVO} object containing report data
	 * 
	 * @param filterOptionType
	 *            type of report
	 * 
	 * @throws MISPException
	 *             if any Exception occurs
	 * 
	 */
	public void writeAgentDeductedReport(HSSFWorkbook workbook,
			ReportAgentVO agentReportVO, int filterOptionType)
			throws MISPException {
		Object[] params = { agentReportVO, filterOptionType };
		logger.entering("writeAgentDeductedReport", params);

		try {
			writeAgentSheetForDeductedClients(workbook, agentReportVO);
		} catch (Exception e) {
			logger.error("Error occured while writing agent report", e);
			throw new MISPException(e);
		}
		logger.exiting("writeAgentDeductedReport");
	}

	/**
	 * This method writes the data to the sheet of the excel workbook for the
	 * provided week of year.
	 * 
	 * <table border=1>
	 * <th colspan=2>Change Request</th>
	 * <tr>
	 * <td>Q3 CR</td>
	 * <td>Request to add extra columns in Agent Report.</td>
	 * </tr>
	 * </table>
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param weekNo
	 *            Week of Year
	 * 
	 * @param agentReportVO
	 *            {@link ReportAgentVO} object containing report data
	 * 
	 * @throws If
	 *             any Exception occurs
	 */
	private void writeAgentSheet(HSSFWorkbook workbook, int weekNo,
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("writeAgentSheet");

		// Creates Sheet Name
		String sheetName = ReportKeys.AGENT_REP_SHEET_NAME + weekNo;
		HSSFSheet sheet = workbook.createSheet(sheetName);
		/**
		 * Sets the sheet zoom Takes the fraction of NUMERATOR and DENOMINATOR
		 * Eg : To express a zoom of 75%, use 3 as numerator and 4 as
		 * denominator.
		 */
		sheet.setZoom(ReportKeys.AGENT_REP_ZOOM_NUMERATOR,
				ReportKeys.AGENT_REP_ZOOM_DENOMINATOR);

		// Font used for the report
		HSSFFont globalFont = workbook.createFont();
		globalFont.setFontName(MIPFont.FONT_CALIBRI);
		globalFont.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

		HSSFFont globalFontBold = workbook.createFont();
		globalFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		globalFontBold.setFontName(MIPFont.FONT_CALIBRI);

		// Cell Style used for the report
		HSSFCellStyle globalCellFormat = workbook.createCellStyle();
		globalCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		globalCellFormat.setFont(globalFont);

		// Top Cell Style
		HSSFCellStyle topCellFormat = workbook.createCellStyle();
		topCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		topCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		topCellFormat.setFont(globalFont);

		// Separator Cell Style
		HSSFCellStyle separatorCellFormat = workbook.createCellStyle();
		separatorCellFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		separatorCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		separatorCellFormat.setFont(globalFont);

		// Date Cell Style
		HSSFCellStyle dateCellFormat = workbook.createCellStyle();
		dateCellFormat.cloneStyleFrom(ReportUtil
				.getHeaderRowCellStyle(workbook));
		dateCellFormat.setBorderTop(HSSFCellStyle.NO_FILL);
		dateCellFormat.setBorderBottom(HSSFCellStyle.NO_FILL);

		// Total Cell Style
		HSSFCellStyle totalCellFormat = workbook.createCellStyle();
		totalCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalCellFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		totalCellFormat.setFont(globalFontBold);

		// First row
		int rowCount = 1;
		HSSFRow row = null;
		HSSFCell cell = null;

		// Holds title row index
		int titleRow = rowCount;

		// Creates Sheet Title
		String sheetTitle = ReportKeys.AGENT_REPORT_TITLE + " ( "
				+ agentReportVO.getFromDate().replaceAll("/", "-") + " To "
				+ agentReportVO.getToDate().replaceAll("/", "-") + " )";
		row = sheet.createRow(rowCount++);
		row.setHeight((short) 500);
		cell = row.createCell(0);
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
		cell.setCellValue(sheetTitle);

		// Empty row above date
		HSSFRow emptyRow = sheet.createRow(rowCount++);

		// Gets no of days for which the records exist.
		int dateSize = agentReportVO.getAllWeeksReport().get(weekNo).size();

		// Creates the Date row.
		int dateIndex = 9;
		int currentWeekNo = 0;
		int noOfColumnForOneDay = ReportKeys.AGENT_REP_NEW_DAILY_HEADING.length;
		row = sheet.createRow(rowCount);
		CellRangeAddress dateCellAddress, emptyCellAddress;
		for (Map<String, List<ReportDailyNewVO>> dailyDataList : agentReportVO
				.getAllWeeksReport().get(weekNo)) {

			for (String key : dailyDataList.keySet()) {

				// Creates empty cells to match the border with date cell
				emptyCellAddress = new CellRangeAddress(rowCount - 1,
						rowCount - 1, dateIndex, dateIndex + 8);
				cell = emptyRow.createCell(dateIndex);
				cell.setCellValue("");

				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
						emptyCellAddress, sheet, sheet.getWorkbook());
				HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
						emptyCellAddress, sheet, sheet.getWorkbook());

				// Creates date cells
				dateCellAddress = new CellRangeAddress(rowCount, rowCount,
						dateIndex, dateIndex + (noOfColumnForOneDay - 1));
				sheet.addMergedRegion(dateCellAddress);
				cell = row.createCell(dateIndex);
				cell.setCellStyle(dateCellFormat);
				cell.setCellValue(key);

				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
						dateCellAddress, sheet, sheet.getWorkbook());
				HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
						dateCellAddress, sheet, sheet.getWorkbook());

				dateIndex += noOfColumnForOneDay;
			}
		}
		// Last cell of row above date row
		cell = emptyRow.createCell(dateIndex);
		cell.setCellStyle(separatorCellFormat);
		cell.setCellValue("");

		// Last cell of date row
		cell = row.createCell(dateIndex);
		cell.setCellStyle(separatorCellFormat);
		cell.setCellValue("");

		rowCount++;

		// Creates Header array
		String[] rowHeader = ReportKeys.AGENT_REP_HEADING;
		for (int i = 0; i < dateSize; i++) {
			rowHeader = (String[]) ArrayUtils.addAll(rowHeader,
					ReportKeys.AGENT_REP_NEW_DAILY_HEADING);
		}
		rowHeader = (String[]) ArrayUtils.addAll(rowHeader,
				ReportKeys.AGENT_REP_NEW_TOTAL_REG);

		// Holds last column index.
		int lastColumnIndex = rowHeader.length - 1;

		// Creates the Header Cell Style
		HSSFCellStyle headerFormat = ReportUtil.getHeaderRowCellStyle(workbook);

		// Writes the column headings.
		ReportUtil.writeHeadings(workbook, sheet, rowHeader, rowCount,
				headerFormat);
		rowCount++;

		// Holds the index of the row where the data starts, used to create
		// formulas.
		int dataRowIndex = rowCount + 1;

		/**
		 * Writes agent report data.
		 */
		boolean isCellEmpty = true;
		boolean isOnLeave = false;
		String leaveReason = "";

		int deregXL = 0;
		int deregHP = 0;
		int deregIP = 0;
		int confirmedXLandHP = 0;
		int confirmedXLandIP = 0;

		DeregisteredCustomersVO deregVO = new DeregisteredCustomersVO();

		for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
			row = sheet.createRow(rowCount++);

			int totalRegistered = 0;
			int cnfXLFinalTotal = 0;
			int cnfHPFinalTotal = 0;
			int cnfIPFinalTotal = 0;
			float avgCnfXLHPIPFinalTotal = 0;
			int deregXLFinalTotal = 0;
			int deregHPFinalTotal = 0;
			int deregIPFinalTotal = 0;
			int regIPFinalTotal = 0;
			// Holds column no
			int columnCount = 0;

			cell = row.createCell(columnCount++);
			cell.setCellStyle(globalCellFormat);
			cell.setCellValue(userDetails.getUserUid());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getUserName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getMsisdn());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue("");

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getGender());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getRoleMaster().getRoleName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchRegion());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchCity());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchStreet());

			List<Map<String, String>> dailyLeaveMapList = agentReportVO
					.getAgentLeaves().get(userDetails.getUserId());

			for (Map<String, List<ReportDailyNewVO>> dailyMap : agentReportVO
					.getAllWeeksReport().get(weekNo)) {

				for (String date : dailyMap.keySet()) {
					isCellEmpty = true;
					isOnLeave = false;

					/**
					 * Gets De-registered XL customers details for the current
					 * user and date
					 */
					deregVO.setXLDeregDate(date);
					deregVO.setXLregBy(userDetails.getUserId());
					deregVO.setHPRegBy(null);
					deregVO.setHPDeregDate(null);
					deregVO.setIPRegBy(null);
					deregVO.setIPDeregDate(null);
					for (DeregisteredCustomersVO deRegDetail : agentReportVO
							.getDeregXLCustomersDetails().get(
									userDetails.getUserId())) {
						if (deRegDetail.equals(deregVO)) {

							deregXL = deRegDetail.getDeregXL();

						}
					}

					/**
					 * Gets De-registered HP Model customers details for the
					 * current user and date
					 */
					deregVO.setHPDeregDate(date);
					deregVO.setHPRegBy(userDetails.getUserId());
					deregVO.setXLregBy(null);
					deregVO.setXLDeregDate(null);
					deregVO.setIPRegBy(null);
					deregVO.setIPDeregDate(null);
					for (DeregisteredCustomersVO deRegDetail : agentReportVO
							.getDeregHPCustomersDetails().get(
									userDetails.getUserId())) {
						if (deRegDetail.equals(deregVO)) {

							deregHP = deRegDetail.getDeregHP();

						}
					}
					
					/**
					 * Gets De-registered IP Model customers details for the
					 * current user and date
					 */
					deregVO.setIPDeregDate(date);
					deregVO.setIPRegBy(userDetails.getUserId());
					deregVO.setXLregBy(null);
					deregVO.setXLDeregDate(null);
					deregVO.setHPRegBy(null);
					deregVO.setHPDeregDate(null);
					for (DeregisteredCustomersVO deRegDetail : agentReportVO
							.getDeregIPCustomersDetails().get(
									userDetails.getUserId())) {
						if (deRegDetail.equals(deregVO)) {

							deregIP = deRegDetail.getDeregIP();

						}
					}

					confirmedXLandHP = 0;
					for (ReportDailyNewVO reportVo : agentReportVO
							.getConfirmedXLandHPDetails().get(
									userDetails.getUserId())) {

						if (reportVo.getUserId() == userDetails.getUserId()) {

							currentWeekNo = DateUtil.getISOWeekNumber(DateUtil
									.toDate(reportVo.getRegDate()));

							if (weekNo == currentWeekNo) {
								confirmedXLandHP += reportVo
										.getConfirmedXLandHP();
							}

						}
					}

					confirmedXLandIP = 0;
					for (ReportDailyNewVO reportVo : agentReportVO
							.getConfirmedXLandIPDetails().get(
									userDetails.getUserId())) {

						if (reportVo.getUserId() == userDetails.getUserId()) {

							currentWeekNo = DateUtil.getISOWeekNumber(DateUtil
									.toDate(reportVo.getRegDate()));

							if (weekNo == currentWeekNo) {
								confirmedXLandIP += reportVo
										.getConfirmedXLandIP();
							}

						}
					}

					for (ReportDailyNewVO dailyReport : dailyMap.get(date)) {

						// Gets the leave details for the current date
						if (dailyLeaveMapList != null) {
							for (Map<String, String> dailyLeaveMap : dailyLeaveMapList) {
								for (int key = 0; key < dailyLeaveMap.keySet()
										.size(); key++) {
									String leaveDate = (String) dailyLeaveMap
											.keySet().toArray()[key];
									if (date.equals(leaveDate)) {
										// Leave

										leaveReason = dailyLeaveMap
												.get(leaveDate);
										isOnLeave = true;
										isCellEmpty = false;
									}
								}
							}
						}

						if (!isOnLeave
								&& dailyReport.getUserUid().equals(
										userDetails.getUserUid())) {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(globalCellFormat);
							cell.setCellValue(dailyReport.getRegisteredXL());

							cell = row.createCell(columnCount++);
							cell.setCellStyle(globalCellFormat);
							cell.setCellValue(dailyReport.getConfirmedXL());

							cell = row.createCell(columnCount++);
							cell.setCellStyle(globalCellFormat);
							cell.setCellValue(dailyReport.getRegisteredHP());

							cell = row.createCell(columnCount++);
							cell.setCellStyle(globalCellFormat);
							cell.setCellValue(dailyReport.getConfirmedHP());

							cell = row.createCell(columnCount++);
							cell.setCellStyle(globalCellFormat);
							cell.setCellValue(dailyReport.getRegisteredIP());

							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue(dailyReport.getConfirmedIP());

							totalRegistered += dailyReport.getRegisteredXL()
									+ dailyReport.getRegisteredHP()
									+ dailyReport.getRegisteredIP();
							cnfXLFinalTotal += dailyReport.getConfirmedXL();
							cnfHPFinalTotal += dailyReport.getConfirmedHP();
							cnfIPFinalTotal += dailyReport.getConfirmedIP();
							regIPFinalTotal += dailyReport.getRegisteredIP();
							isCellEmpty = false;
						}
					}
					// Creates cell for leaves
					if (isOnLeave) {
						// Merges the leave cell
						CellRangeAddress leaveCellAddress = new CellRangeAddress(
								row.getRowNum(), row.getRowNum(), columnCount,
								columnCount + noOfColumnForOneDay - 1);
						sheet.addMergedRegion(leaveCellAddress);

						cell = row.createCell(columnCount);
						cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(
								workbook, HSSFColor.LIGHT_ORANGE.index));
						cell.setCellValue(leaveReason);
						HSSFRegionUtil.setBorderRight(
								HSSFCellStyle.BORDER_THIN, leaveCellAddress,
								sheet, sheet.getWorkbook());
						columnCount += noOfColumnForOneDay;

						isCellEmpty = false;
					}
					// Creates empty cells for no data
					if (isCellEmpty) {

						cell = row.createCell(columnCount++);
						cell.setCellValue("");

						cell = row.createCell(columnCount++);
						cell.setCellValue("");

						cell = row.createCell(columnCount++);
						cell.setCellValue("");

						cell = row.createCell(columnCount++);
						cell.setCellValue("");

						cell = row.createCell(columnCount++);
						cell.setCellValue("");

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						cell.setCellValue("");
					}

				}

				deregXLFinalTotal += deregXL;
				deregHPFinalTotal += deregHP;
				deregIPFinalTotal += deregIP;

				deregXL = 0;
				deregHP = 0;
				deregIP = 0;
			}

			// Creates last six columns to hold weekly total registrations

			if (totalRegistered > 0)
				avgCnfXLHPIPFinalTotal = ((cnfXLFinalTotal + cnfHPFinalTotal + cnfIPFinalTotal) * 100)
						/ totalRegistered;
			else
				avgCnfXLHPIPFinalTotal = 0;

			cell = row.createCell(lastColumnIndex - 8);
			cell.setCellStyle(separatorCellFormat);
			if (cnfXLFinalTotal > 0) {
				cell.setCellValue(cnfXLFinalTotal);
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(lastColumnIndex - 7);
			cell.setCellStyle(separatorCellFormat);
			if (cnfHPFinalTotal > 0) {
				cell.setCellValue(cnfHPFinalTotal);
			} else {
				cell.setCellValue("");
			}

			/*cell = row.createCell(lastColumnIndex - 7);
			cell.setCellStyle(separatorCellFormat);
			if (regIPFinalTotal > 0) {
				cell.setCellValue(regIPFinalTotal);
			} else {
				cell.setCellValue("");
			}*/

			cell = row.createCell(lastColumnIndex - 6);
			cell.setCellStyle(separatorCellFormat);
			if (cnfIPFinalTotal > 0) {
				cell.setCellValue(cnfIPFinalTotal);
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(lastColumnIndex - 5);
			cell.setCellStyle(separatorCellFormat);
			if (confirmedXLandHP > 0) {
				cell.setCellValue(confirmedXLandHP);
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(lastColumnIndex - 4);
			cell.setCellStyle(separatorCellFormat);
			if (confirmedXLandIP > 0) {
				cell.setCellValue(confirmedXLandIP);
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(lastColumnIndex - 3);
			cell.setCellStyle(separatorCellFormat);
			if (avgCnfXLHPIPFinalTotal > 0) {

				cell.setCellValue(avgCnfXLHPIPFinalTotal + "%");
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(lastColumnIndex - 2);
			cell.setCellStyle(separatorCellFormat);
			if (deregXLFinalTotal > 0) {
				cell.setCellValue(deregXLFinalTotal);
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(lastColumnIndex - 1);
			cell.setCellStyle(separatorCellFormat);
			if (deregHPFinalTotal > 0) {
				cell.setCellValue(deregHPFinalTotal);
			} else {
				cell.setCellValue("");
			}
			
			cell = row.createCell(lastColumnIndex);
			cell.setCellStyle(separatorCellFormat);
			if (deregIPFinalTotal > 0) {
				cell.setCellValue(deregIPFinalTotal);
			} else {
				cell.setCellValue("");
			}

			// Reset the Final Total values.
			totalRegistered = 0;
			deregXL = 0;
			deregHP = 0;
			deregIP = 0;
			cnfXLFinalTotal = 0;
			cnfHPFinalTotal = 0;
			cnfIPFinalTotal = 0;
			confirmedXLandHP = 0;
			avgCnfXLHPIPFinalTotal = 0;
			deregXLFinalTotal = 0;
			deregHPFinalTotal = 0;
			deregIPFinalTotal = 0;
			regIPFinalTotal = 0;
			confirmedXLandIP = 0;
		}

		// Creates Total Row
		row = sheet.createRow(rowCount);
		// Holds column no
		int columnCount = 0;

		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellValue(ReportKeys.AGENT_REP_TOTAL);

		cell = row.createCell(columnCount++);
		cell.setCellStyle(totalCellFormat);
		cell.setCellValue("");

		cell = row.createCell(columnCount++);
		cell.setCellStyle(totalCellFormat);
		cell.setCellValue("");

		cell = row.createCell(columnCount++);
		cell.setCellStyle(totalCellFormat);
		cell.setCellValue("");

		cell = row.createCell(columnCount++);
		cell.setCellStyle(totalCellFormat);
		cell.setCellValue("");

		cell = row.createCell(columnCount++);
		cell.setCellStyle(totalCellFormat);
		cell.setCellValue("");

		cell = row.createCell(columnCount++);
		cell.setCellStyle(totalCellFormat);
		cell.setCellValue("");

		cell = row.createCell(columnCount++);
		cell.setCellStyle(totalCellFormat);
		cell.setCellValue("");

		cell = row.createCell(columnCount++);
		cell.setCellStyle(totalCellFormat);
		cell.setCellValue("");

		// Holds the sum of register column
		float finalReg = 0;
		// Holds the sum of confirm column
		float finalCnf = 0;
		// Holds the column index
		int columnIndex = 0;
		// Holds the formula for columns
		String formula = "";
		// Holds the formula for last cell of total row
		String totalFormula = "";

		// To evaluate the value from formula cells
		FormulaEvaluator evaluator = workbook.getCreationHelper()
				.createFormulaEvaluator();
		CellValue cv;
		DecimalFormat df = new DecimalFormat("###.##%");

		// Creates the cells for the total row
		for (int i = 0; i < dateSize; i++) {
			for (int j = 0; j < noOfColumnForOneDay; j++) {
				columnIndex = columnCount;
				formula = "SUM(" + ReportUtil.getColumnName(columnIndex)
						+ dataRowIndex + ":"
						+ ReportUtil.getColumnName(columnIndex) + rowCount
						+ ")";
				cell = row.createCell(columnCount++);
				cell.setCellStyle(topCellFormat);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				cell.setCellFormula(formula);
				if (j == 0) {
					cv = evaluator.evaluate(cell);
					finalReg = (float) cv.getNumberValue();
				}
				if (j == 6) {
					cv = evaluator.evaluate(cell);
					finalCnf = (float) cv.getNumberValue();
				}
			}
			// cell = row.createCell(columnCount++);
			// cell.setCellStyle(separatorCellFormat);
			// cell.setCellStyle(topCellFormat);
			// if (finalCnf > 0.0F) {
			// cell.setCellValue(df.format(Math.abs((finalCnf / finalReg))));
			// } else {
			// cell.setCellValue(df.format(Math.abs(0.0F)));
			// }
		}
		
		// Creates formula for second last cell of total row
				/*totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 9)
						+ dataRowIndex + ":"
						+ ReportUtil.getColumnName(lastColumnIndex - 9) + rowCount
						+ ")";

				// Creates the last two cells of total row
				cell = row.createCell(columnCount++);
				cell.setCellStyle(topCellFormat);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				cell.setCellFormula(totalFormula);*/

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 8)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 8) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 7)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 7) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 6)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 6) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 5)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 5) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 4)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 4) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 3)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 3) + rowCount
				+ ")";
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 2)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 2) + rowCount
				+ ")";
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 1)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 1) + rowCount
				+ ")";
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex) + rowCount + ")";
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Resize Columns
		sheet.setColumnWidth(0, 4000);
		for (int i = 1; i <= lastColumnIndex; i++)
			sheet.autoSizeColumn(i);

		// Merges the title row
		sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow, 0,
				lastColumnIndex));

		/**
		 * To implement the style(border) of title row. Some of the style will
		 * not be preserved after merging
		 */

		// Title row range address
		CellRangeAddress titleCellAddress = new CellRangeAddress(titleRow,
				titleRow, 0, lastColumnIndex);

		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());

		logger.exiting("writeAgentSheet");
	}

	private void writeAgentSheet(HSSFWorkbook workbook,
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("writeAgentSheet");

		// Creates Sheet Name
		String sheetName = ReportKeys.AGENT_REP_SALARY_SHEET_NAME;
		HSSFSheet sheet = workbook.createSheet(sheetName);
		/**
		 * Sets the sheet zoom Takes the fraction of NUMERATOR and DENOMINATOR
		 * Eg : To express a zoom of 75%, use 3 as numerator and 4 as
		 * denominator.
		 */
		sheet.setZoom(ReportKeys.AGENT_REP_ZOOM_NUMERATOR,
				ReportKeys.AGENT_REP_ZOOM_DENOMINATOR);

		// Font used for the report
		HSSFFont globalFont = workbook.createFont();
		globalFont.setFontName(MIPFont.FONT_CALIBRI);
		globalFont.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

		HSSFFont globalFontBold = workbook.createFont();
		globalFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		globalFontBold.setFontName(MIPFont.FONT_CALIBRI);

		// Cell Style used for the report
		HSSFCellStyle globalCellFormat = workbook.createCellStyle();
		globalCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		globalCellFormat.setFont(globalFont);

		// Top Cell Style
		HSSFCellStyle topCellFormat = workbook.createCellStyle();
		topCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		topCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		topCellFormat.setFont(globalFont);

		// Separator Cell Style
		HSSFCellStyle separatorCellFormat = workbook.createCellStyle();
		separatorCellFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		separatorCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		separatorCellFormat.setFont(globalFont);

		// Date Cell Style
		HSSFCellStyle dateCellFormat = workbook.createCellStyle();
		dateCellFormat.cloneStyleFrom(ReportUtil
				.getHeaderRowCellStyle(workbook));
		dateCellFormat.setBorderTop(HSSFCellStyle.NO_FILL);
		dateCellFormat.setBorderBottom(HSSFCellStyle.NO_FILL);

		// Total Cell Style
		HSSFCellStyle totalCellFormat = workbook.createCellStyle();
		totalCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalCellFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		totalCellFormat.setFont(globalFontBold);

		// First row
		int rowCount = 1;
		HSSFRow row = null;
		HSSFCell cell = null;

		// Holds title row index
		int titleRow = rowCount;

		// Creates Sheet Title
		String sheetTitle = ReportKeys.AGENT_SALARY_REPORT_TITLE + " ( "
				+ agentReportVO.getFromDate().replaceAll("/", "-") + " To "
				+ agentReportVO.getToDate().replaceAll("/", "-") + " )";
		row = sheet.createRow(rowCount++);
		row.setHeight((short) 500);
		cell = row.createCell(0);
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
		cell.setCellValue(sheetTitle);

		row = sheet.createRow(rowCount);

		rowCount++;

		// Holds the index of the row where the data starts, used to create
		// formulas.
		int dataRowIndex = rowCount + 1;

		/**
		 * Writes agent report data.
		 */
		int registeredXLandHP = 0;
		int registeredXLandIP = 0;
		int confirmedXLandHP = 0;
		int confirmedXLandIP = 0;
		String[] rowHeader = ReportKeys.AGENT_REP_SAL_DAILY_HEADING;

		// Holds last column index.
		int lastColumnIndex = rowHeader.length - 1;

		// Creates the Header Cell Style
		HSSFCellStyle headerFormat = ReportUtil.getHeaderRowCellStyle(workbook);

		// Writes the column headings.
		ReportUtil.writeHeadings(workbook, sheet, rowHeader, rowCount,
				headerFormat);

		rowCount++;

		for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
			row = sheet.createRow(rowCount++);

			// Holds column no
			int columnCount = 0;

			cell = row.createCell(columnCount++);
			cell.setCellStyle(globalCellFormat);
			cell.setCellValue(userDetails.getUserUid());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getUserName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getMsisdn());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue("");

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getGender());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getRoleMaster().getRoleName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchRegion());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchCity());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchStreet());

			// Creates last six columns to hold weekly total registrations
			if (agentReportVO.getDurationReport().get(userDetails.getUserUid()) != null) {

				/*
				 * int registeredFree = agentReportVO.getDurationReport().
				 * get(userDetails.getUserUid()).getRegisteredFree(); cell =
				 * row.createCell(lastColumnIndex - 7);
				 * cell.setCellStyle(separatorCellFormat); if (registeredFree >
				 * 0) { cell.setCellValue(registeredFree); } else {
				 * cell.setCellValue(""); }
				 * 
				 * int confirmedFree = agentReportVO.getDurationReport().
				 * get(userDetails.getUserUid()).getConfirmedFree(); cell =
				 * row.createCell(lastColumnIndex - 6);
				 * cell.setCellStyle(separatorCellFormat); if (confirmedFree >
				 * 0) { cell.setCellValue(confirmedFree); } else {
				 * cell.setCellValue(""); }
				 */

				int registeredXL = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getRegisteredXL();
				cell = row.createCell(lastColumnIndex - 9);
				cell.setCellStyle(separatorCellFormat);
				if (registeredXL > 0) {

					cell.setCellValue(registeredXL);
				} else {
					cell.setCellValue("");
				}

				int confirmedXL = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getConfirmedXL();
				cell = row.createCell(lastColumnIndex - 8);
				cell.setCellStyle(separatorCellFormat);
				if (confirmedXL > 0) {

					cell.setCellValue(confirmedXL);
				} else {
					cell.setCellValue("");
				}

				int registeredHP = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getRegisteredHP();
				cell = row.createCell(lastColumnIndex - 7);
				cell.setCellStyle(separatorCellFormat);
				if (registeredHP > 0) {
					cell.setCellValue(registeredHP);
				} else {
					cell.setCellValue("");
				}

				int confirmedHP = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getConfirmedHP();
				cell = row.createCell(lastColumnIndex - 6);
				cell.setCellStyle(separatorCellFormat);
				if (confirmedHP > 0) {
					cell.setCellValue(confirmedHP);
				} else {
					cell.setCellValue("");
				}

				int registeredIP = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getRegisteredIP();
				cell = row.createCell(lastColumnIndex - 5);
				cell.setCellStyle(separatorCellFormat);
				if (registeredIP > 0) {
					cell.setCellValue(registeredIP);
				} else {
					cell.setCellValue("");
				}

				int confirmedIP = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getConfirmedIP();
				cell = row.createCell(lastColumnIndex - 4);
				cell.setCellStyle(separatorCellFormat);
				if (confirmedIP > 0) {
					cell.setCellValue(confirmedIP);
				} else {
					cell.setCellValue("");
				}

				if (agentReportVO.getRegisteredXLandHPDetailsMap().get(
						userDetails.getUserId()) != null) {
					registeredXLandHP = agentReportVO
							.getRegisteredXLandHPDetailsMap()
							.get(userDetails.getUserId())
							.getRegisteredXLandHP();
					cell = row.createCell(lastColumnIndex - 3);
					cell.setCellStyle(separatorCellFormat);
					if (registeredXLandHP > 0) {
						cell.setCellValue(registeredXLandHP);
					} else {
						cell.setCellValue("");
					}
				} else {
					cell = row.createCell(lastColumnIndex - 3);
					cell.setCellStyle(separatorCellFormat);
					cell.setCellValue("");
				}
				
				if (agentReportVO.getRegisteredXLandIPDetailsMap().get(
						userDetails.getUserId()) != null) {
					registeredXLandIP = agentReportVO
							.getRegisteredXLandIPDetailsMap()
							.get(userDetails.getUserId())
							.getRegisteredXLandIP();
					cell = row.createCell(lastColumnIndex - 2);
					cell.setCellStyle(separatorCellFormat);
					if (registeredXLandIP > 0) {
						cell.setCellValue(registeredXLandIP);
					} else {
						cell.setCellValue("");
					}
				} else {
					cell = row.createCell(lastColumnIndex - 2);
					cell.setCellStyle(separatorCellFormat);
					cell.setCellValue("");
				}

				if (agentReportVO.getConfirmedXLandHPDetailsMap().get(
						userDetails.getUserId()) != null) {
					confirmedXLandHP = agentReportVO
							.getConfirmedXLandHPDetailsMap()
							.get(userDetails.getUserId()).getConfirmedXLandHP();
					cell = row.createCell(lastColumnIndex - 1);
					cell.setCellStyle(separatorCellFormat);
					if (confirmedXLandHP > 0) {
						cell.setCellValue(confirmedXLandHP);
					} else {
						cell.setCellValue("");
					}
				} else {
					cell = row.createCell(lastColumnIndex - 1);
					cell.setCellStyle(separatorCellFormat);
					cell.setCellValue("");
				}

				if (agentReportVO.getConfirmedXLandIPDetailsMap().get(
						userDetails.getUserId()) != null) {
					confirmedXLandIP = agentReportVO
							.getConfirmedXLandIPDetailsMap()
							.get(userDetails.getUserId()).getConfirmedXLandIP();
					cell = row.createCell(lastColumnIndex);
					cell.setCellStyle(separatorCellFormat);
					if (confirmedXLandIP > 0) {
						cell.setCellValue(confirmedXLandIP);
					} else {
						cell.setCellValue("");
					}
				} else {
					cell = row.createCell(lastColumnIndex);
					cell.setCellStyle(separatorCellFormat);
					cell.setCellValue("");
				}

			} else {
				cell = row.createCell(lastColumnIndex - 8);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 7);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 6);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 5);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 4);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 3);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 2);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 1);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");
			}
		}

		// Creates Total Row
		row = sheet.createRow(rowCount);
		// Holds column no
		int columnCount = ReportKeys.AGENT_REP_SAL_DAILY_HEADING.length - 10;

		cell = row.createCell(0);
		cell.setCellStyle(topCellFormat);
		cell.setCellValue(ReportKeys.AGENT_REP_TOTAL);

		for (int i = 1; i < columnCount; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(topCellFormat);
			cell.setCellValue("");
		}

		// Holds the formula for last cell of total row
		String totalFormula = "";
		
		// Creates formula for fourth last cell of total row
				totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 9)
						+ dataRowIndex + ":"
						+ ReportUtil.getColumnName(lastColumnIndex - 9) + rowCount
						+ ")";

				// Creates the last two cells of total row
				cell = row.createCell(columnCount++);
				cell.setCellStyle(topCellFormat);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				cell.setCellFormula(totalFormula);

		// Creates formula for fourth last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 8)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 8) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for fourth last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 7)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 7) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for third last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 6)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 6) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 5)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 5) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 4)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 4) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 3)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 3) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 2)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 2) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 1)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 1) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex) + rowCount + ")";
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Resize Columns
		sheet.setColumnWidth(0, 4000);
		for (int i = 1; i <= lastColumnIndex; i++)
			sheet.autoSizeColumn(i);

		// Merges the title row
		sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow, 0,
				lastColumnIndex));

		/**
		 * To implement the style(border) of title row. Some of the style will
		 * not be preserved after merging
		 */

		// Title row range address
		CellRangeAddress titleCellAddress = new CellRangeAddress(titleRow,
				titleRow, 0, lastColumnIndex);

		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());

		logger.exiting("writeAgentSheet");
	}

	/**
	 * Deducted clients report
	 * 
	 * @param workbook
	 * @param agentReportVO
	 * @throws MISPException
	 */
	private void writeAgentSheetForDeductedClients(HSSFWorkbook workbook,
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("writeAgentSheetForDeductedClients");

		// Creates Sheet Name
		String sheetName = ReportKeys.AGENT_REP_DEDUCTED_SHEET_NAME;
		HSSFSheet sheet = workbook.createSheet(sheetName);
		/**
		 * Sets the sheet zoom Takes the fraction of NUMERATOR and DENOMINATOR
		 * Eg : To express a zoom of 75%, use 3 as numerator and 4 as
		 * denominator.
		 */
		sheet.setZoom(ReportKeys.AGENT_REP_ZOOM_NUMERATOR,
				ReportKeys.AGENT_REP_ZOOM_DENOMINATOR);

		// Font used for the report
		HSSFFont globalFont = workbook.createFont();
		globalFont.setFontName(MIPFont.FONT_CALIBRI);
		globalFont.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

		HSSFFont globalFontBold = workbook.createFont();
		globalFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		globalFontBold.setFontName(MIPFont.FONT_CALIBRI);

		// Cell Style used for the report
		HSSFCellStyle globalCellFormat = workbook.createCellStyle();
		globalCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		globalCellFormat.setFont(globalFont);

		// Top Cell Style
		HSSFCellStyle topCellFormat = workbook.createCellStyle();
		topCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		topCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		topCellFormat.setFont(globalFont);

		// Separator Cell Style
		HSSFCellStyle separatorCellFormat = workbook.createCellStyle();
		separatorCellFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		separatorCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		separatorCellFormat.setFont(globalFont);

		// Date Cell Style
		HSSFCellStyle dateCellFormat = workbook.createCellStyle();
		dateCellFormat.cloneStyleFrom(ReportUtil
				.getHeaderRowCellStyle(workbook));
		dateCellFormat.setBorderTop(HSSFCellStyle.NO_FILL);
		dateCellFormat.setBorderBottom(HSSFCellStyle.NO_FILL);

		// Total Cell Style
		HSSFCellStyle totalCellFormat = workbook.createCellStyle();
		totalCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalCellFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		totalCellFormat.setFont(globalFontBold);

		// First row
		int rowCount = 1;
		HSSFRow row = null;
		HSSFCell cell = null;

		// Holds title row index
		int titleRow = rowCount;

		// Creates Sheet Title
		String sheetTitle = ReportKeys.DEDUCTED_REPORT_TITLE + " ( "
				+ agentReportVO.getFromDate().replaceAll("/", "-") + " To "
				+ agentReportVO.getToDate().replaceAll("/", "-") + " )";
		row = sheet.createRow(rowCount++);
		row.setHeight((short) 500);
		cell = row.createCell(0);
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
		cell.setCellValue(sheetTitle);

		row = sheet.createRow(rowCount);

		rowCount++;

		// Holds the index of the row where the data starts, used to create
		// formulas.
		int dataRowIndex = rowCount + 1;

		/**
		 * Writes agent report data.
		 */
		String[] rowHeader = ReportKeys.AGENT_REP_DEDUCTION_DAILY_HEADING;

		// Holds last column index.
		int lastColumnIndex = rowHeader.length - 1;

		// Creates the Header Cell Style
		HSSFCellStyle headerFormat = ReportUtil.getHeaderRowCellStyle(workbook);

		// Writes the column headings.
		ReportUtil.writeHeadings(workbook, sheet, rowHeader, rowCount,
				headerFormat);

		rowCount++;

		for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
			row = sheet.createRow(rowCount++);

			// Holds column no
			int columnCount = 0;

			cell = row.createCell(columnCount++);
			cell.setCellStyle(globalCellFormat);
			cell.setCellValue(userDetails.getUserUid());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getUserName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getMsisdn());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue("");

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getGender());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getRoleMaster().getRoleName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchRegion());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchCity());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchStreet());

			// Creates last six columns to hold weekly total registrations
			if (agentReportVO.getDurationReport().get(userDetails.getUserUid()) != null) {

				int deductedXL = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getDeductedXL();
				cell = row.createCell(lastColumnIndex - 4);
				cell.setCellStyle(separatorCellFormat);
				if (deductedXL > 0) {

					cell.setCellValue(deductedXL);
				} else {
					cell.setCellValue("");
				}

				int deductedHP = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getDeductedHP();
				cell = row.createCell(lastColumnIndex - 3);
				cell.setCellStyle(separatorCellFormat);
				if (deductedHP > 0) {

					cell.setCellValue(deductedHP);
				} else {
					cell.setCellValue("");
				}

				int deductedIP = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getDeductedIP();
				cell = row.createCell(lastColumnIndex - 2);
				cell.setCellStyle(separatorCellFormat);
				if (deductedIP > 0) {

					cell.setCellValue(deductedIP);
				} else {
					cell.setCellValue("");
				}

				int deductedXLandHP = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getDeductedXLandHP();
				cell = row.createCell(lastColumnIndex - 1);
				cell.setCellStyle(separatorCellFormat);
				if (deductedXLandHP > 0) {

					cell.setCellValue(deductedXLandHP);
				} else {
					cell.setCellValue("");
				}

				int deductedXLandIP = agentReportVO.getDurationReport()
						.get(userDetails.getUserUid()).getDeductedXLandIP();
				cell = row.createCell(lastColumnIndex);
				cell.setCellStyle(separatorCellFormat);
				if (deductedXLandIP > 0) {

					cell.setCellValue(deductedXLandIP);
				} else {
					cell.setCellValue("");
				}

			} else {

				cell = row.createCell(lastColumnIndex - 4);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 3);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 2);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex - 1);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");

				cell = row.createCell(lastColumnIndex);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue("");
			}
		}

		// Creates Total Row
		row = sheet.createRow(rowCount);
		// Holds column no
		int columnCount = ReportKeys.AGENT_REP_DEDUCTION_DAILY_HEADING.length - 5;

		cell = row.createCell(0);
		cell.setCellStyle(topCellFormat);
		cell.setCellValue(ReportKeys.AGENT_REP_TOTAL);

		for (int i = 1; i < columnCount; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(topCellFormat);
			cell.setCellValue("");
		}

		// Holds the formula for last cell of total row
		String totalFormula = "";

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 4)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 4) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 3)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 3) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 2)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 2) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 1)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 1) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex) + rowCount + ")";
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Resize Columns
		sheet.setColumnWidth(0, 4000);
		for (int i = 1; i <= lastColumnIndex; i++)
			sheet.autoSizeColumn(i);

		// Merges the title row
		sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow, 0,
				lastColumnIndex));

		/**
		 * To implement the style(border) of title row. Some of the style will
		 * not be preserved after merging
		 */

		// Title row range address
		CellRangeAddress titleCellAddress = new CellRangeAddress(titleRow,
				titleRow, 0, lastColumnIndex);

		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());

		logger.exiting("writeAgentSheetForDeductedClients");
	}

	/**
	 * This method writes the weekly report data to the excel workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param reportWeeklyVO
	 *            {@link ReportWeeklyVO} object containing the report details
	 * 
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public void writeWeeklyReport(HSSFWorkbook workbook,
			ReportWeeklyVO reportWeeklyVO) throws MISPException {

		Object[] params = { workbook, reportWeeklyVO };
		logger.entering("writeWeeklyReport", params);

		Map<String, String> weeklyReportMap = null;

		try {
			weeklyReportMap = reportManager.getWeeklyReport(reportWeeklyVO);
			HSSFDataFormat df = workbook.createDataFormat();

			// Font used for the report
			HSSFFont globalFont = workbook.createFont();
			globalFont.setFontName(MIPFont.FONT_CALIBRI);
			globalFont.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

			HSSFFont globalFontBold = workbook.createFont();
			globalFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			globalFontBold.setFontName(MIPFont.FONT_CALIBRI);

			// Cell Style used for the report
			HSSFCellStyle globalCellFormat = workbook.createCellStyle();
			globalCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			globalCellFormat.setFont(globalFont);

			// Cell Style used for the report
			HSSFCellStyle globalCellFormatBold = workbook.createCellStyle();
			globalCellFormatBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			globalCellFormatBold.setFont(globalFontBold);

			// Cell Style used for the report
			HSSFCellStyle numberCellFormat = workbook.createCellStyle();
			numberCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			numberCellFormat.setFont(globalFont);
			numberCellFormat.setDataFormat(df.getFormat("#,##0"));

			// Cell Style used for the report
			HSSFCellStyle fractionCellFormat = workbook.createCellStyle();
			fractionCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			fractionCellFormat.setFont(globalFont);
			fractionCellFormat.setDataFormat(df.getFormat("#,##0.00"));

			// Cell Style used for the report
			HSSFCellStyle percentageCellFormat = workbook.createCellStyle();
			percentageCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			percentageCellFormat.setFont(globalFont);
			percentageCellFormat.setDataFormat(df.getFormat("0.00%"));

			// Creates a excel work sheet
			HSSFSheet sheet = workbook
					.createSheet(ReportKeys.WEEKLY_REP_SHEET_NAME);

			// To evaluate the value from formula cells
			FormulaEvaluator evaluator = workbook.getCreationHelper()
					.createFormulaEvaluator();
			CellValue cv;

			// First row
			int rowCount = 1;
			HSSFRow row = null;
			HSSFCell cell = null;

			// Writes Report Title
			int titleRow = rowCount;

			row = sheet.createRow(rowCount++);
			row.setHeight((short) 500);
			cell = row.createCell(0);
			cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
			cell.setCellValue(ReportKeys.WEEKLY_REP_TITLE);

			// Writes Data rows
			// B3: Date of last data update
			int header = 0;
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(globalCellFormat);
			cell.setCellValue(reportWeeklyVO.getRefDate());

			// B4: Period start date
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(globalCellFormat);
			cell.setCellValue(reportWeeklyVO.getFromDate());

			// B5: Month or period end
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(globalCellFormat);
			cell.setCellValue(reportWeeklyVO.getToDate());

			// B6: Number of past agent working days (man days)
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(numberCellFormat);
			cell.setCellValue(Integer.valueOf(weeklyReportMap
					.get(PlatformConstants.MAN_DAYS)));

			// B7: Current average # of registrations per agent per day for
			// current period
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(fractionCellFormat);
			cell.setCellValue(Float.valueOf(weeklyReportMap
					.get(PlatformConstants.AVERAGE_REGISTRATION_COUNT)));
			float b7 = (float) cell.getNumericCellValue();

			// B8: Number of CSC agents to date
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(numberCellFormat);
			cell.setCellValue(Integer.valueOf(weeklyReportMap
					.get(PlatformConstants.CSC_AGENTS_COUNT)));

			// B9: Number of Mobile agents to date
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(numberCellFormat);
			cell.setCellValue(Integer.valueOf(weeklyReportMap
					.get(PlatformConstants.MOBILE_AGENTS_COUNT)));

			// B10: Total number of agents to date
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(numberCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula("B" + (rowCount - 2) + "+B" + (rowCount - 1));

			// B11: Number of days till period end
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(numberCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellValue(Integer.valueOf(DateUtil.getNoOfDays(
					reportWeeklyVO.getRefDate(), reportWeeklyVO.getToDate())
					+ ""));

			// B12: Total number of agent working days till period end (man
			// days)
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(numberCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula("B" + (rowCount - 2) + "*B" + (rowCount - 1));
			cv = evaluator.evaluate(cell);
			float b12 = (float) cv.getNumberValue();

			// B13: Registrations for period
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(numberCellFormat);
			cell.setCellValue(Integer.valueOf(weeklyReportMap
					.get(PlatformConstants.PERIOD_REGISTRATION_COUNT)));

			// B14: Total number of unique subs (AC)
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(numberCellFormat);
			cell.setCellValue(Integer.valueOf(weeklyReportMap
					.get(PlatformConstants.TOTAL_REGISTRATION_COUNT)));
			float b14 = (float) cell.getNumericCellValue();

			// B15: Target period end registrations
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cell.setCellStyle(numberCellFormat);
			cell.setCellValue(Integer.valueOf(reportWeeklyVO
					.getTargetEndDateReg()));
			float b15 = (float) cell.getNumericCellValue();

			// B16: Outstanding number of registrations for period
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(numberCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula("B" + (rowCount - 1) + "-B" + (rowCount - 2));
			cv = evaluator.evaluate(cell);
			float b16 = (float) cv.getNumberValue();

			// B17: Total period end registrations at current average rate (FC)
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(fractionCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			if (b7 > 0)
				cell.setCellValue(b14 + (b12 / b7));
			else
				cell.setCellValue(b14);
			float b17 = (float) cell.getNumericCellValue();

			// B18: Percent completion (FC)
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(percentageCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			if (b15 > 0)
				cell.setCellValue(b17 / b15);
			else
				cell.setCellValue(0);

			// B19: Average # of daily registrations per agent to meet period
			// end target
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(globalCellFormatBold);
			cell.setCellValue(ReportKeys.WEEKLY_REP_HEADING[header++]);
			cell = row.createCell(1);
			cell.setCellStyle(fractionCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			if (b12 > 0)
				cell.setCellValue(b16 / b12);
			else
				cell.setCellValue(0);

			// Resize Columns
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);

			// Merges the title row
			sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow, 0, 1));

			/**
			 * To implement the style(border) of title row. Some of the style
			 * will not be preserved after merging
			 */

			// Creates Border
			CellRangeAddress titleCellAddress = new CellRangeAddress(titleRow,
					titleRow, 0, 1);
			CellRangeAddress outerBorderAddress = new CellRangeAddress(
					titleRow, sheet.getLastRowNum(), 0, 1);
			CellRangeAddress innerBorderAddress = new CellRangeAddress(
					titleRow, sheet.getLastRowNum(), 0, 0);

			// Title Row Border
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
					titleCellAddress, sheet, sheet.getWorkbook());
			HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
					titleCellAddress, sheet, sheet.getWorkbook());
			HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
					titleCellAddress, sheet, sheet.getWorkbook());
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
					titleCellAddress, sheet, sheet.getWorkbook());

			// Outer Border
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
					outerBorderAddress, sheet, sheet.getWorkbook());
			HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
					outerBorderAddress, sheet, sheet.getWorkbook());

			// Inner Border
			HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
					innerBorderAddress, sheet, sheet.getWorkbook());
		} catch (DBException dbException) {
			logger.error(
					"Error occured while getting the weekly report for the given date range",
					dbException);
			throw new MISPException(dbException);
		} catch (Exception exception) {
			logger.error(
					"Error occured while writing the weekly report data to the excel workbook",
					exception);
			throw new MISPException(exception);
		}
		logger.exiting("writeWeeklyReport");
	}

	/**
	 * This method writes the revenue report data to the excel workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * @param month
	 * @param year
	 * 
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public boolean writeRevenueReport(HSSFWorkbook workbook, String year,
			String month) throws MISPException {

		Object[] params = { workbook, year, month };
		logger.entering("writeRevenueReport", params);

		boolean result = false;
		Map<String, String> revenueReportMap = null;

		try {
			revenueReportMap = reportManager.getRevenueReport(year, month);

			if (null != revenueReportMap) {
				HSSFDataFormat df = workbook.createDataFormat();

				// Font used for the report
				HSSFFont globalFont = workbook.createFont();
				globalFont.setFontName(MIPFont.FONT_CALIBRI);
				globalFont.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

				HSSFFont globalFontBold = workbook.createFont();
				globalFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				globalFontBold.setFontName(MIPFont.FONT_CALIBRI);

				// Cell Style used for the report
				HSSFCellStyle globalCellFormatBold = workbook.createCellStyle();
				globalCellFormatBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				globalCellFormatBold.setFont(globalFontBold);

				// Cell Style used for the report
				HSSFCellStyle fractionCellFormat = workbook.createCellStyle();
				fractionCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				fractionCellFormat.setFont(globalFont);
				fractionCellFormat.setDataFormat(df.getFormat("#,##0.00"));

				// Creates a excel work sheet
				HSSFSheet sheet = workbook
						.createSheet(ReportKeys.REVENUE_REP_SHEET_NAME);

				// First row
				int rowCount = 1;
				HSSFRow row = null;
				HSSFCell cell = null;

				// Writes Report Title
				int titleRow = rowCount;

				row = sheet.createRow(rowCount++);
				row.setHeight((short) 500);
				cell = row.createCell(0);
				cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
				cell.setCellValue(ReportKeys.REVENUE_REP_TITLE);

				// Writes Data rows
				// Revenue from Freemium Model
				int header = 0;
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.REVENUE_REP_HEADING[header++]);
				cell = row.createCell(1);
				cell.setCellStyle(fractionCellFormat);
				cell.setCellValue(Double.valueOf(revenueReportMap
						.get(PlatformConstants.FREEMIUM_REVENUE)));

				// Total Revenue
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.REVENUE_REP_HEADING[header++]);
				cell = row.createCell(1);
				cell.setCellStyle(fractionCellFormat);
				cell.setCellValue(Double.valueOf(revenueReportMap
						.get(PlatformConstants.TOTAL_REVENUE)));

				// Cost for commissions
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.REVENUE_REP_HEADING[header++]);
				cell = row.createCell(1);
				cell.setCellStyle(fractionCellFormat);
				cell.setCellValue(Double.valueOf(revenueReportMap
						.get(PlatformConstants.COMMISSION_COST)));

				// Cost for premiums (Free model)
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.REVENUE_REP_HEADING[header++]);
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(fractionCellFormat);
				cell.setCellValue(Double.valueOf(revenueReportMap
						.get(PlatformConstants.FREE_PREMIUM_COST)));

				// Cost for premiums (Freemium)
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.REVENUE_REP_HEADING[header++]);
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(fractionCellFormat);
				cell.setCellValue(Double.valueOf(revenueReportMap
						.get(PlatformConstants.FREEMIUM_PREMIUM_COST)));

				// Total Premium Cost
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.REVENUE_REP_HEADING[header++]);
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(fractionCellFormat);
				cell.setCellValue(Double.valueOf(revenueReportMap
						.get(PlatformConstants.TOTAL_PREMIUM_COST)));

				// Total Costs
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.REVENUE_REP_HEADING[header++]);
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(fractionCellFormat);
				cell.setCellValue(Double.valueOf(revenueReportMap
						.get(PlatformConstants.TOTAL_COST)));

				// Profit
				row = sheet.createRow(rowCount++);
				cell = row.createCell(0);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.REVENUE_REP_HEADING[header++]);
				cell = row.createCell(1);
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellStyle(fractionCellFormat);
				cell.setCellValue(Double.valueOf(revenueReportMap
						.get(PlatformConstants.PROFIT)));

				// Resize Columns
				sheet.autoSizeColumn(0);
				sheet.autoSizeColumn(1);

				// Merges the title row
				sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow,
						0, 1));

				/**
				 * To implement the style(border) of title row. Some of the
				 * style will not be preserved after merging
				 */

				// Creates Border
				CellRangeAddress titleCellAddress = new CellRangeAddress(
						titleRow, titleRow, 0, 1);
				CellRangeAddress outerBorderAddress = new CellRangeAddress(
						titleRow, sheet.getLastRowNum(), 0, 1);
				CellRangeAddress innerBorderAddress = new CellRangeAddress(
						titleRow, sheet.getLastRowNum(), 0, 0);

				// Title Row Border
				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
						titleCellAddress, sheet, sheet.getWorkbook());
				HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
						titleCellAddress, sheet, sheet.getWorkbook());
				HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
						titleCellAddress, sheet, sheet.getWorkbook());
				HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
						titleCellAddress, sheet, sheet.getWorkbook());

				// Outer Border
				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
						outerBorderAddress, sheet, sheet.getWorkbook());
				HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
						outerBorderAddress, sheet, sheet.getWorkbook());

				// Inner Border
				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
						innerBorderAddress, sheet, sheet.getWorkbook());

				result = true;
			}
		} catch (DBException dbException) {
			logger.error(
					"Error occured while getting the revenue report for the given date range",
					dbException);
			throw new MISPException(dbException);
		} catch (Exception exception) {
			logger.error(
					"Error occured while writing the revenue report data to the excel workbook",
					exception);
			throw new MISPException(exception);
		}
		logger.exiting("writeRevenueReport");
		return result;
	}

	/**
	 * This method writes the coverage report data to the excel workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * @param month
	 *            <String> holds the current month
	 * @param year
	 *            <String> holds the current year
	 * 
	 * @throws MISPException
	 *             If any {@link DBException} occurs
	 */
	public boolean writeCoverageReport(HSSFWorkbook workbook, String year,
			String month) throws MISPException {
		Object[] params = { workbook, year, month };
		logger.entering("writeCoverageReport", params);

		boolean result = false;
		List<ReportCoverageVO> coverageReportData = null;
		List<BranchDetails> branchList = null;
		List<ReportCoverageVO> coverageReportDataTemp = null;

		Map<String, List<ReportCoverageVO>> coverageReportMap = new HashMap<String, List<ReportCoverageVO>>();
		Map<Integer, String> regionCityMap = new HashMap<Integer, String>();

		try {
			branchList = branchManager.getBranchDetailsList();
			coverageReportData = reportManager.getCoverageReportData(year,
					month);

			logger.debug("Coverage Report Data : ", coverageReportData);
			if (null != coverageReportData) {

				// Gets the subMap based on the Regions
				for (BranchDetails br : branchList) {
					for (ReportCoverageVO cr : coverageReportData) {
						if (br.getBranchId() == cr.getBranchId()) {

							if (coverageReportMap.containsKey(br
									.getBranchRegion())) {
								coverageReportMap.get(br.getBranchRegion())
										.add(cr);
							} else {
								coverageReportDataTemp = new ArrayList<ReportCoverageVO>();
								coverageReportDataTemp.add(cr);
								coverageReportMap.put(br.getBranchRegion(),
										coverageReportDataTemp);
							}
							regionCityMap.put(br.getBranchId(),
									br.getBranchCity());
						}
					}
				}

				HSSFDataFormat df = workbook.createDataFormat();

				// Font used for the report
				HSSFFont globalFont = workbook.createFont();
				globalFont.setFontName(MIPFont.FONT_CALIBRI);
				globalFont.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

				HSSFFont globalFontBold = workbook.createFont();
				globalFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				globalFontBold.setFontName(MIPFont.FONT_CALIBRI);
				globalFontBold.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

				// Cell Style used for the report
				HSSFCellStyle numberCellFormat = workbook.createCellStyle();
				numberCellFormat.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				numberCellFormat.setFont(globalFont);
				numberCellFormat.setDataFormat(df.getFormat("#,##0"));

				// Cell Style used for the report
				HSSFCellStyle percentageCellFormat = workbook.createCellStyle();
				percentageCellFormat.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				percentageCellFormat.setFont(globalFont);
				percentageCellFormat.setDataFormat(df.getFormat("0.00%"));

				// Cell Style used for the report
				HSSFCellStyle globalCellFormat = workbook.createCellStyle();
				globalCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				globalCellFormat.setFont(globalFont);

				// Cell Style used for the report
				HSSFCellStyle globalCellFormatBold = workbook.createCellStyle();
				globalCellFormatBold.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				globalCellFormatBold.setFont(globalFontBold);

				// Cell Style used for the report
				HSSFCellStyle numberCellFormatBold = workbook.createCellStyle();
				numberCellFormatBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				numberCellFormatBold.setFont(globalFontBold);
				numberCellFormatBold.setDataFormat(df.getFormat("#,##0"));

				// Cell Style used for the report
				HSSFCellStyle percentageCellFormatBold = workbook
						.createCellStyle();
				percentageCellFormatBold
						.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
				percentageCellFormatBold.setFont(globalFontBold);
				percentageCellFormatBold.setDataFormat(df.getFormat("0.00%"));

				/****** Start of Sheet I *****/
				// Creates a excel work sheet I
				HSSFSheet coverageSheet = workbook
						.createSheet(ReportKeys.COVERAGE_REP_SHEET1_NAME);

				// First row
				int rowCount = 1;
				HSSFRow row = null;
				HSSFRow regionRow = null;
				HSSFRow totalRow = null;
				HSSFCell cell = null;

				// Writes Report Title
				int titleRow = rowCount;

				// Creates sheet I
				row = coverageSheet.createRow(rowCount++);
				row.setHeight((short) 500);
				cell = row.createCell(0);
				cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
				cell.setCellValue(ReportKeys.COVERAGE_REP_SHEET1_TITLE);

				rowCount++;

				// Creates the Header Cell Style
				HSSFCellStyle headerFormat = ReportUtil
						.getHeaderRowCellStyle(workbook);

				// Creates the column headings for Coverage and Coverage Summary
				// sheets.
				String[] headers = ReportKeys.COVERAGE_REP_SHEET_HEADING1;
				String[] summaryHeaders = ReportKeys.COVERAGE_REP_SHEET_HEADING3;

				ReportCoverageVO coverRep = coverageReportData.get(0);
				if (coverRep.getCover1() != -1) {
					headers = (String[]) ArrayUtils
							.addAll(headers,
									new String[] {
											coverRep.getCover1() + " (active)",
											ReportKeys.COVERAGE_REP_ACTIVE_PERCENTAGE });
					summaryHeaders = (String[]) ArrayUtils.add(summaryHeaders,
							coverRep.getCover1() + " (active)");
				}
				if (coverRep.getCover2() != -1) {
					headers = (String[]) ArrayUtils
							.addAll(headers,
									new String[] {
											coverRep.getCover2() + "",
											ReportKeys.COVERAGE_REP_ACTIVE_PERCENTAGE });
					summaryHeaders = (String[]) ArrayUtils.add(summaryHeaders,
							coverRep.getCover2() + "");
				}
				if (coverRep.getCover3() != -1) {
					headers = (String[]) ArrayUtils
							.addAll(headers,
									new String[] {
											coverRep.getCover3() + "",
											ReportKeys.COVERAGE_REP_ACTIVE_PERCENTAGE });
					summaryHeaders = (String[]) ArrayUtils.add(summaryHeaders,
							coverRep.getCover3() + "");
				}
				if (coverRep.getCover4() != -1) {
					headers = (String[]) ArrayUtils
							.addAll(headers,
									new String[] {
											coverRep.getCover4() + "",
											ReportKeys.COVERAGE_REP_ACTIVE_PERCENTAGE });
					summaryHeaders = (String[]) ArrayUtils.add(summaryHeaders,
							coverRep.getCover4() + "");
				}
				if (coverRep.getCover5() != -1) {
					headers = (String[]) ArrayUtils
							.addAll(headers,
									new String[] {
											coverRep.getCover5() + "",
											ReportKeys.COVERAGE_REP_ACTIVE_PERCENTAGE });
					summaryHeaders = (String[]) ArrayUtils.add(summaryHeaders,
							coverRep.getCover5() + "");
				}
				if (coverRep.getCover6() != -1) {
					headers = (String[]) ArrayUtils
							.addAll(headers,
									new String[] {
											coverRep.getCover6() + "",
											ReportKeys.COVERAGE_REP_ACTIVE_PERCENTAGE });
					summaryHeaders = (String[]) ArrayUtils.add(summaryHeaders,
							coverRep.getCover6() + "");
				}
				if (coverRep.getCover7() != -1) {
					headers = (String[]) ArrayUtils
							.addAll(headers,
									new String[] {
											coverRep.getCover7() + "",
											ReportKeys.COVERAGE_REP_ACTIVE_PERCENTAGE });
					summaryHeaders = (String[]) ArrayUtils.add(summaryHeaders,
							coverRep.getCover7() + "");
				}
				if (coverRep.getCover8() != -1) {
					headers = (String[]) ArrayUtils
							.addAll(headers,
									new String[] {
											coverRep.getCover8() + "",
											ReportKeys.COVERAGE_REP_ACTIVE_PERCENTAGE });
					summaryHeaders = (String[]) ArrayUtils.add(summaryHeaders,
							coverRep.getCover8() + "");
				}
				headers = (String[]) ArrayUtils.addAll(headers,
						ReportKeys.COVERAGE_REP_SHEET_HEADING2);
				summaryHeaders = (String[]) ArrayUtils.addAll(summaryHeaders,
						ReportKeys.COVERAGE_REP_SHEET_HEADING2);

				// Writes the column headings.
				ReportUtil.writeHeadings(workbook, coverageSheet, headers,
						rowCount, headerFormat);
				rowCount++;

				int headerRow = rowCount;
				int columnCount = 1;
				int colIndex = 0;
				int start = 0;
				int end = 0;
				int lastColumnIndex = headers.length - 1;
				Object[] data = null;
				ArrayList<Integer> totalRowIndexes = new ArrayList<Integer>();
				ArrayList<Long> totalRowValues = new ArrayList<Long>();
				StringBuilder formulaString;
				// To evaluate the value from formula cells
				FormulaEvaluator evaluator = workbook.getCreationHelper()
						.createFormulaEvaluator();
				CellValue cv;

				// Writes the data cells
				for (String key : coverageReportMap.keySet()) {
					totalRowIndexes.add(rowCount + 1);
					regionRow = coverageSheet.createRow(rowCount++);

					start = rowCount + 1;
					for (ReportCoverageVO cr : coverageReportMap.get(key)) {
						data = null;

						if (cr.getCover1() != -1) {
							data = ArrayUtils.addAll(data, new Object[] {
									cr.getCount1().longValue(),
									cr.getPercent1().doubleValue() });
						}
						if (cr.getCover2() != -1) {
							data = ArrayUtils.addAll(data, new Object[] {
									cr.getCount2().longValue(),
									cr.getPercent2().doubleValue() });
						}
						if (cr.getCover3() != -1) {
							data = ArrayUtils.addAll(data, new Object[] {
									cr.getCount3().longValue(),
									cr.getPercent3().doubleValue() });
						}
						if (cr.getCover4() != -1) {
							data = ArrayUtils.addAll(data, new Object[] {
									cr.getCount4().longValue(),
									cr.getPercent4().doubleValue() });
						}
						if (cr.getCover5() != -1) {
							data = ArrayUtils.addAll(data, new Object[] {
									cr.getCount5().longValue(),
									cr.getPercent5().doubleValue() });
						}
						if (cr.getCover6() != -1) {
							data = ArrayUtils.addAll(data, new Object[] {
									cr.getCount6().longValue(),
									cr.getPercent6().doubleValue() });
						}
						if (cr.getCover7() != -1) {
							data = ArrayUtils.addAll(data, new Object[] {
									cr.getCount7().longValue(),
									cr.getPercent7().doubleValue() });
						}
						if (cr.getCover8() != -1) {
							data = ArrayUtils.addAll(data, new Object[] {
									cr.getCount8().longValue(),
									cr.getPercent8().doubleValue() });
						}
						// Writes city data
						columnCount = 1;
						row = coverageSheet.createRow(rowCount++);

						cell = row.createCell(columnCount++);
						cell.setCellStyle(globalCellFormat);
						cell.setCellValue(regionCityMap.get(cr.getBranchId()));

						cell = row.createCell(columnCount++);
						cell.setCellStyle(numberCellFormat);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(cr.getZeroInactive()
								.add(BigInteger.valueOf((Long) data[0]))
								.longValue());

						cell = row.createCell(columnCount++);
						cell.setCellStyle(numberCellFormat);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(cr.getZeroInactive().longValue());

						colIndex = columnCount;
						for (int i = 0; i < data.length; i++) {
							cell = row.createCell(i + colIndex);
							cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
							if (i % 2 == 0) {
								cell.setCellStyle(numberCellFormat);
								cell.setCellValue((Long) data[i]);
							} else {
								cell.setCellStyle(percentageCellFormat);
								cell.setCellValue(((Double) data[i] / 100));
							}
							columnCount++;
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(numberCellFormat);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(cr.sumOfCount().longValue());

						cell = row.createCell(columnCount++);
						cell.setCellStyle(numberCellFormat);
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(cr.sumOfCount()
								.add(cr.getZeroInactive()).doubleValue());

					}
					end = rowCount;

					// Writes region data
					columnCount = 0;
					cell = regionRow.createCell(columnCount++);
					cell.setCellStyle(globalCellFormatBold);
					cell.setCellValue(key);

					cell = regionRow.createCell(columnCount++);

					cell = regionRow.createCell(columnCount++);
					cell.setCellStyle(numberCellFormatBold);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellFormula("SUM("
							+ ReportUtil.getColumnName(columnCount - 1) + start
							+ ":" + ReportUtil.getColumnName(columnCount - 1)
							+ end + ")");

					cell = regionRow.createCell(columnCount++);
					cell.setCellStyle(numberCellFormatBold);
					cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					cell.setCellFormula("SUM("
							+ ReportUtil.getColumnName(columnCount - 1) + start
							+ ":" + ReportUtil.getColumnName(columnCount - 1)
							+ end + ")");

					colIndex = columnCount;
					for (int i = 0; i < data.length; i++) {
						cell = regionRow.createCell(i + colIndex);
						if (i % 2 == 0)
							cell.setCellStyle(numberCellFormatBold);
						else
							cell.setCellStyle(percentageCellFormatBold);
						cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						cell.setCellFormula("SUM("
								+ ReportUtil.getColumnName(columnCount) + start
								+ ":" + ReportUtil.getColumnName(columnCount)
								+ end + ")");
						columnCount++;
					}

					cell = regionRow.createCell(columnCount++);
					cell.setCellStyle(numberCellFormatBold);
					cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					cell.setCellFormula("SUM("
							+ ReportUtil.getColumnName(columnCount - 1) + start
							+ ":" + ReportUtil.getColumnName(columnCount - 1)
							+ end + ")");

					cell = regionRow.createCell(columnCount++);
					cell.setCellStyle(numberCellFormatBold);
					cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					cell.setCellFormula("SUM("
							+ ReportUtil.getColumnName(columnCount - 1) + start
							+ ":" + ReportUtil.getColumnName(columnCount - 1)
							+ end + ")");

				}

				// Creates the Total Row
				columnCount = 0;
				totalRow = coverageSheet.createRow(rowCount);
				cell = totalRow.createCell(columnCount++);
				cell.setCellStyle(globalCellFormatBold);
				cell.setCellValue(ReportKeys.COVERAGE_REP_TOTAL);

				cell = totalRow.createCell(columnCount++);

				formulaString = new StringBuilder("");
				cell = totalRow.createCell(columnCount++);
				cell.setCellStyle(numberCellFormatBold);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				for (Integer tri : totalRowIndexes)
					formulaString
							.append(ReportUtil.getColumnName(columnCount - 1))
							.append(tri).append("+");
				if (formulaString.lastIndexOf("+") != -1)
					formulaString.deleteCharAt(formulaString.lastIndexOf("+"));
				cell.setCellFormula(formulaString.toString());
				cv = evaluator.evaluate(cell);
				long val = (long) cv.getNumberValue();
				totalRowValues.add(val);

				formulaString = new StringBuilder("");
				cell = totalRow.createCell(columnCount++);
				cell.setCellStyle(numberCellFormatBold);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				for (Integer tri : totalRowIndexes)
					formulaString
							.append(ReportUtil.getColumnName(columnCount - 1))
							.append(tri).append("+");
				if (formulaString.lastIndexOf("+") != -1)
					formulaString.deleteCharAt(formulaString.lastIndexOf("+"));
				cell.setCellFormula(formulaString.toString());
				cv = evaluator.evaluate(cell);
				val = (long) cv.getNumberValue();
				totalRowValues.add(val);

				colIndex = columnCount;
				for (int i = 0; i < data.length; i++) {
					formulaString = new StringBuilder("");
					cell = totalRow.createCell(i + colIndex);
					cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					for (Integer tri : totalRowIndexes)
						formulaString
								.append(ReportUtil.getColumnName(columnCount))
								.append(tri).append("+");
					if (formulaString.lastIndexOf("+") != -1)
						formulaString.deleteCharAt(formulaString
								.lastIndexOf("+"));
					cell.setCellFormula(formulaString.toString());
					if (i % 2 == 0) {
						cell.setCellStyle(numberCellFormatBold);
						cv = evaluator.evaluate(cell);
						val = (long) cv.getNumberValue();
						totalRowValues.add(val);
					} else
						cell.setCellStyle(percentageCellFormatBold);

					columnCount++;
				}

				formulaString = new StringBuilder("");
				cell = totalRow.createCell(columnCount++);
				cell.setCellStyle(numberCellFormatBold);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				for (Integer tri : totalRowIndexes)
					formulaString
							.append(ReportUtil.getColumnName(columnCount - 1))
							.append(tri).append("+");
				if (formulaString.lastIndexOf("+") != -1)
					formulaString.deleteCharAt(formulaString.lastIndexOf("+"));
				cell.setCellFormula(formulaString.toString());
				cv = evaluator.evaluate(cell);
				val = (long) cv.getNumberValue();
				totalRowValues.add(val);

				formulaString = new StringBuilder("");
				cell = totalRow.createCell(columnCount++);
				cell.setCellStyle(numberCellFormatBold);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				for (Integer tri : totalRowIndexes)
					formulaString
							.append(ReportUtil.getColumnName(columnCount - 1))
							.append(tri).append("+");
				if (formulaString.lastIndexOf("+") != -1)
					formulaString.deleteCharAt(formulaString.lastIndexOf("+"));
				cell.setCellFormula(formulaString.toString());
				cv = evaluator.evaluate(cell);
				val = (long) cv.getNumberValue();
				totalRowValues.add(val);

				// Merges the title row and sets the border
				coverageSheet.addMergedRegion(new CellRangeAddress(titleRow,
						titleRow, 0, lastColumnIndex));

				CellRangeAddress titleCellAddress = new CellRangeAddress(
						titleRow, titleRow, 0, lastColumnIndex);
				CellRangeAddress cityCellAddress = new CellRangeAddress(
						headerRow, coverageSheet.getLastRowNum() - 1, 1, 1);
				CellRangeAddress totalCellAddress = new CellRangeAddress(
						coverageSheet.getLastRowNum(),
						coverageSheet.getLastRowNum(), 0, lastColumnIndex);

				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
						titleCellAddress, coverageSheet,
						coverageSheet.getWorkbook());
				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
						cityCellAddress, coverageSheet,
						coverageSheet.getWorkbook());
				HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
						totalCellAddress, coverageSheet,
						coverageSheet.getWorkbook());

				// Resize Columns
				for (int i = 0; i <= lastColumnIndex; i++)
					coverageSheet.autoSizeColumn(i);

				/****** End of Sheet I *****/

				/****** Start of Sheet II *****/

				HSSFCellStyle numberCellFormatBorder = workbook
						.createCellStyle();
				numberCellFormatBorder.cloneStyleFrom(numberCellFormat);
				numberCellFormatBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);
				numberCellFormatBorder
						.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				numberCellFormatBorder.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				numberCellFormatBorder
						.setBorderRight(HSSFCellStyle.BORDER_THIN);

				HSSFCellStyle percentCellFormatBorder = workbook
						.createCellStyle();
				percentCellFormatBorder.cloneStyleFrom(percentageCellFormat);
				percentCellFormatBorder.setBorderTop(HSSFCellStyle.BORDER_THIN);
				percentCellFormatBorder
						.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				percentCellFormatBorder
						.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				percentCellFormatBorder
						.setBorderRight(HSSFCellStyle.BORDER_THIN);

				// Creates a excel work sheet II
				HSSFSheet summarySheet = workbook
						.createSheet(ReportKeys.COVERAGE_REP_SHEET2_NAME);

				// First row
				rowCount = 1;
				row = null;
				cell = null;
				int startRow = 0;

				titleRow = rowCount;

				// Writes Report Title
				row = summarySheet.createRow(rowCount++);
				row.setHeight((short) 500);
				cell = row.createCell(0);
				cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
				cell.setCellValue(ReportKeys.COVERAGE_REP_SHEET2_TITLE);

				rowCount++;

				// Creates horizontal headings
				columnCount = 0;
				row = summarySheet.createRow(rowCount++);
				for (int i = 0; i < ReportKeys.COVERAGE_REP_SHEET_HEADING4.length; i++) {
					columnCount = i + 1;
					cell = row.createCell(columnCount);
					cell.setCellStyle(globalCellFormatBold);
					cell.setCellValue(ReportKeys.COVERAGE_REP_SHEET_HEADING4[i]);
				}

				// Creates vertical headings and data
				startRow = rowCount;
				columnCount = 0;
				for (int i = 0; i < summaryHeaders.length; i++) {
					row = summarySheet.createRow(rowCount++);
					cell = row.createCell(columnCount);
					cell.setCellStyle(globalCellFormatBold);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(summaryHeaders[i]);

					// NUMBER Column
					cell = row.createCell(columnCount + 1);
					cell.setCellStyle(numberCellFormatBorder);
					cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
					cell.setCellValue(totalRowValues.get(i));

					// % of active Column
					if (i > 1 && i < 8) {
						cell = row.createCell(columnCount + 2);
						cell.setCellStyle(percentCellFormatBorder);
						cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						cell.setCellFormula(ReportUtil
								.getColumnName(columnCount + 1)
								+ (row.getRowNum() + 1)
								+ "/"
								+ ReportUtil.getColumnName(columnCount + 1)
								+ ((startRow + totalRowValues.size()) - 1));
					} else if (i < 8) {
						cell = row.createCell(columnCount + 2);
						cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(
								workbook, HSSFColor.GREY_50_PERCENT.index));
					}

					// % of Reg Column
					if (i != 2 && i < 8) {
						cell = row.createCell(columnCount + 3);
						cell.setCellStyle(percentCellFormatBorder);
						cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						cell.setCellFormula(ReportUtil
								.getColumnName(columnCount + 1)
								+ (row.getRowNum() + 1)
								+ "/"
								+ ReportUtil.getColumnName(columnCount + 1)
								+ ((startRow + totalRowValues.size())));
					} else if (i < 8) {
						cell = row.createCell(columnCount + 3);
						cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(
								workbook, HSSFColor.GREY_50_PERCENT.index));
					}
				}

				// Resize Columns
				for (int i = 0; i < 4; i++)
					summarySheet.autoSizeColumn(i);

				// Merges the title row and sets the border
				summarySheet.addMergedRegion(new CellRangeAddress(titleRow,
						titleRow, 0, 3));
				titleCellAddress = new CellRangeAddress(titleRow, titleRow, 0,
						3);
				HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
						titleCellAddress, summarySheet,
						summarySheet.getWorkbook());

				/****** End of Sheet II *****/

				result = true;
			}
		} catch (DBException dbException) {
			logger.error("Error occured while getting the coverage report "
					+ "for the current selection", dbException);
			throw new MISPException(dbException);
		} catch (Exception exception) {
			logger.error(
					"Error occured while writing the coverage report data "
							+ "to the excel workbook", exception);
			throw new MISPException(exception);
		}
		logger.exiting("writeCoverageReport", result);
		return result;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */
	public Map<Integer, List<ReportDailyNewVO>> getConfirmedXLandHP(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getConfirmedXLandHP", agentReportVO);

		List<ReportDailyNewVO> confirmedXLandHPList = null;
		Map<Integer, List<ReportDailyNewVO>> confirmedXLandHPDetails = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		List<ReportDailyNewVO> customersList = null;
		try {

			confirmedXLandHPList = reportManager.getConfirmedXLandHP(fromDate,
					toDate);

			confirmedXLandHPDetails = new HashMap<Integer, List<ReportDailyNewVO>>();
			for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
				customersList = new ArrayList<ReportDailyNewVO>();
				for (ReportDailyNewVO user : confirmedXLandHPList) {
					if (userDetails.getUserId() == user.getUserId()) {
						customersList.add(user);
					}
				}
				confirmedXLandHPDetails.put(userDetails.getUserId(),
						customersList);
			}

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the confirmed XL and HP details",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getConfirmedXLandHP");
		return confirmedXLandHPDetails;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */
	public Map<Integer, List<ReportDailyNewVO>> getConfirmedXLandIP(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getConfirmedXLandIP", agentReportVO);

		List<ReportDailyNewVO> confirmedXLandIPList = null;
		Map<Integer, List<ReportDailyNewVO>> confirmedXLandIPDetails = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		List<ReportDailyNewVO> customersList = null;
		try {

			confirmedXLandIPList = reportManager.getConfirmedXLandIP(fromDate,
					toDate);

			confirmedXLandIPDetails = new HashMap<Integer, List<ReportDailyNewVO>>();
			for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
				customersList = new ArrayList<ReportDailyNewVO>();
				for (ReportDailyNewVO user : confirmedXLandIPList) {
					if (userDetails.getUserId() == user.getUserId()) {
						customersList.add(user);
					}
				}
				confirmedXLandIPDetails.put(userDetails.getUserId(),
						customersList);
			}

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the confirmed XL and IP details",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getConfirmedXLandIP");
		return confirmedXLandIPDetails;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */
	public Map<Integer, ReportDailyNewVO> getRegisteredXLandHPWithUserDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getRegisteredXLandHPWithUserDetails", agentReportVO);

		Map<Integer, ReportDailyNewVO> tempRegisteredXLandHPMap = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		try {

			tempRegisteredXLandHPMap = this.reportManager
					.getRegisteredXLandHPWithUserDetails(fromDate, toDate);

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the registered XL and HP details",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getRegisteredXLandHPWithUserDetails");
		return tempRegisteredXLandHPMap;
	}
	
	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */
	public Map<Integer, ReportDailyNewVO> getRegisteredXLandIPWithUserDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getRegisteredXLandIPWithUserDetails", agentReportVO);

		Map<Integer, ReportDailyNewVO> tempRegisteredXLandIPMap = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		try {

			tempRegisteredXLandIPMap = this.reportManager
					.getRegisteredXLandIPWithUserDetails(fromDate, toDate);

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the registered XL and IP details",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getRegisteredXLandIPWithUserDetails");
		return tempRegisteredXLandIPMap;
	}
	
	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */
	public Map<Integer, ReportDailyNewVO> getConfirmedXLandHPWithUserDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getConfirmedXLandHPWithUserDetails", agentReportVO);

		Map<Integer, ReportDailyNewVO> tempConfirmedXLandHPMap = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		try {

			tempConfirmedXLandHPMap = this.reportManager
					.getConfirmedXLandHPWithUserDetails(fromDate, toDate);

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the confirmed XL and HP details",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getConfirmedXLandHPWithUserDetails");
		return tempConfirmedXLandHPMap;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */

	public Map<Integer, ReportDailyNewVO> getConfirmedXLandIPWithUserDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getConfirmedXLandIPWithUserDetails", agentReportVO);

		Map<Integer, ReportDailyNewVO> tempConfirmedXLandIPMap = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		try {

			tempConfirmedXLandIPMap = this.reportManager
					.getConfirmedXLandIPWithUserDetails(fromDate, toDate);

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the confirmed XL and HP details",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getConfirmedXLandIPWithUserDetails");
		return tempConfirmedXLandIPMap;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @return
	 * @throws MISPException
	 */
	public Map<Integer, List<DeregisteredCustomersVO>> getDeregFMCustomersDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getDeregFMCustomersDetails", agentReportVO);

		List<DeregisteredCustomersVO> deregCustomersList = null;
		Map<Integer, List<DeregisteredCustomersVO>> deregCustomerDetails = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		List<DeregisteredCustomersVO> customersList = null;
		try {

			deregCustomersList = reportManager.getFreeModelDeregDetails(
					fromDate, toDate);

			deregCustomerDetails = new HashMap<Integer, List<DeregisteredCustomersVO>>();
			for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
				customersList = new ArrayList<DeregisteredCustomersVO>();
				for (DeregisteredCustomersVO userDereg : deregCustomersList) {
					if (userDetails.getUserId() == userDereg.getRegBy()) {
						customersList.add(userDereg);
					}
				}
				deregCustomerDetails
						.put(userDetails.getUserId(), customersList);
			}

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the deregister customers data",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getDeregFMCustomersDetails");
		return deregCustomerDetails;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */
	public Map<Integer, List<DeregisteredCustomersVO>> getDeregXLCustomersDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getDeregXLCustomersDetails", agentReportVO);

		List<DeregisteredCustomersVO> deregCustomersList = null;
		Map<Integer, List<DeregisteredCustomersVO>> deregCustomerDetails = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		List<DeregisteredCustomersVO> customersList = null;
		try {

			deregCustomersList = reportManager.getXLDeregDetails(fromDate,
					toDate);

			deregCustomerDetails = new HashMap<Integer, List<DeregisteredCustomersVO>>();
			for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
				customersList = new ArrayList<DeregisteredCustomersVO>();
				for (DeregisteredCustomersVO userDereg : deregCustomersList) {
					if (userDetails.getUserId() == userDereg.getXLregBy()
							.intValue()) {
						customersList.add(userDereg);
					}
				}
				deregCustomerDetails
						.put(userDetails.getUserId(), customersList);
			}

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the deregister customers data",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getDeregXLCustomersDetails");
		return deregCustomerDetails;
	}
	
	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */
	
	
	public Map<Integer, List<DeregisteredCustomersVO>> getDeregIPCustomersDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getDeregIPCustomersDetails", agentReportVO);

		List<DeregisteredCustomersVO> deregCustomersList = null;
		Map<Integer, List<DeregisteredCustomersVO>> deregCustomerDetails = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		List<DeregisteredCustomersVO> customersList = null;
		try {

			deregCustomersList = reportManager.getIPDeregDetails(fromDate,
					toDate);

			deregCustomerDetails = new HashMap<Integer, List<DeregisteredCustomersVO>>();
			for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
				customersList = new ArrayList<DeregisteredCustomersVO>();
				for (DeregisteredCustomersVO userDereg : deregCustomersList) {
					if (userDetails.getUserId() == userDereg.getIPRegBy()
							.intValue()) {
						customersList.add(userDereg);
					}
				}
				deregCustomerDetails
						.put(userDetails.getUserId(), customersList);
			}

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the deregister customers data",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getDeregIPCustomersDetails");
		return deregCustomerDetails;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @throws MISPException
	 */
	public Map<Integer, List<DeregisteredCustomersVO>> getDeregHPCustomersDetails(
			ReportAgentVO agentReportVO) throws MISPException {
		logger.entering("getDeregHPCustomersDetails", agentReportVO);

		List<DeregisteredCustomersVO> deregCustomersList = null;
		Map<Integer, List<DeregisteredCustomersVO>> deregCustomerDetails = null;

		String fromDate = agentReportVO.getFromDate();
		fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

		String toDate = agentReportVO.getToDate();
		toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
				+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
				+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

		List<DeregisteredCustomersVO> customersList = null;
		try {

			deregCustomersList = reportManager.getHPDeregDetails(fromDate,
					toDate);

			deregCustomerDetails = new HashMap<Integer, List<DeregisteredCustomersVO>>();
			for (UserDetails userDetails : agentReportVO.getUserDetailsList()) {
				customersList = new ArrayList<DeregisteredCustomersVO>();
				for (DeregisteredCustomersVO userDereg : deregCustomersList) {
					if (userDetails.getUserId() == userDereg.getHPRegBy()
							.intValue()) {
						customersList.add(userDereg);
					}
				}
				deregCustomerDetails
						.put(userDetails.getUserId(), customersList);
			}

		} catch (DBException dbe) {
			logger.error(
					"Error occured while getting the deregister customers data",
					dbe);
			throw new MISPException(dbe);
		}

		logger.exiting("getDeregHPCustomersDetails");
		return deregCustomerDetails;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @return
	 * @throws MISPException
	 *             This method will fetch the data of the agents based on there
	 *             role and branch which they report to.
	 */
	public List<UserDetails> getBrnachwiseUserDetails(
			ReportAgentVO agentReportVO) throws MISPException {

		logger.entering("getBrnachwiseUserDetails");
		List<UserDetails> branchwiseUserData = null;

		try {
			branchwiseUserData = this.userManager
					.getBranchwiseUserDetails(agentReportVO);
		} catch (DBException exception) {
			logger.error("Error occured while getting list of all users ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getBrnachwiseUserDetails");
		return branchwiseUserData;
	}

	/**
	 * 
	 * @param role
	 * @return
	 * @throws MISPException
	 *             This method will fetch the distinct branch Id list of the
	 *             users based on there role.
	 */
	public List<Integer> getDistinctBranchId(String role) throws MISPException {
		logger.entering("getDistinctBranchId");
		List<Integer> getDistinctBranchId = null;

		try {
			getDistinctBranchId = this.userManager.getDistinctBranchId(role);
		} catch (DBException exception) {
			logger.error("Error occured while getting list of all users ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getDistinctBranchId");
		return getDistinctBranchId;

	}

	/**
	 * 
	 * @param agentReportVO
	 * @return
	 * @throws MISPException
	 *             This method will fetch the daywise performance data of the
	 *             user during particular date range.
	 */
	public Map<Integer, Map<String, List<ReportDailyNewVO>>> getDaywiseReportData(
			ReportAgentVO agentReportVO) throws MISPException {

		Object[] params = { agentReportVO };
		logger.entering("getDaywiseReportData", params);

		/**
		 * Holds data for each day within fromDate and toDate with date as key
		 * and list of data for that date as value
		 */
		Map<Integer, Map<String, List<ReportDailyNewVO>>> agentDaywiseReportMap = new LinkedHashMap<Integer, Map<String, List<ReportDailyNewVO>>>();
		Map<String, List<ReportDailyNewVO>> dailyReportMap = null;

		int userId = 0;

		try {
			List<UserDetails> users = agentReportVO.getUserDetailsList();
			for (UserDetails user : users) {
				userId = user.getUserId();
				Date now = null;
				Calendar start = Calendar.getInstance();
				start.setTime(DateUtil.toDate(agentReportVO.getFromDate()));
				Calendar end = Calendar.getInstance();
				end.setTime(DateUtil.toDate(agentReportVO.getToDate()));

				dailyReportMap = new LinkedHashMap<String, List<ReportDailyNewVO>>();

				for (; !start.after(end); start.add(Calendar.DATE, 1)) {
					now = start.getTime();

					List<ReportDailyNewVO> dailyReportList = reportManager
							.getTotalConfirmationCountForDate(now, userId);

					dailyReportMap.put(DateUtil.toDateDayString(now),
							dailyReportList);

				}

				agentDaywiseReportMap.put(userId, dailyReportMap);

				dailyReportMap = null;
			}
		} catch (DBException e) {
			logger.error(
					"Error occured while getting agent report details for the given range",
					e);
			throw new MISPException(e);
		}

		logger.exiting("getDaywiseReportData", agentDaywiseReportMap);
		return agentDaywiseReportMap;

	}

	/**
	 * 
	 * @param workbook
	 * @param agentReportVO
	 * @param reportType
	 *            This method will generate the CSC weekly report to display the
	 *            agent performance based on the branch level.
	 */
	public void writeCSCWeeklyReport(HSSFWorkbook workbook,
			ReportAgentVO agentReportVO, int reportType) {

		Object[] params = { workbook, agentReportVO, reportType };

		logger.entering("writeCSCWeeklyReport", params);

		List<String> dateRange = agentReportVO.getDateRange();
		List<UserDetails> userDetails = agentReportVO.getUserDetailsList();
		Map<Integer, Map<String, List<ReportDailyNewVO>>> agentDaywiseReportMap = agentReportVO
				.getAgentDaywiseReportMap();
		Map<String, List<ReportDailyNewVO>> dayWiseData = null;

		String lastDateDay = "";
		if (dateRange.size() > 0) {
			lastDateDay = dateRange.get(dateRange.size() - 1);
		}
		// Creates a excel work sheet
		HSSFSheet sheet = workbook
				.createSheet(ReportKeys.CSC_WEEKLY_REP_FILE_NAME);

		// Font used for the report
		HSSFFont globalFont = workbook.createFont();
		globalFont.setFontName(MIPFont.FONT_CALIBRI);
		globalFont.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

		HSSFFont globalFontBold = workbook.createFont();
		globalFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		globalFontBold.setFontName(MIPFont.FONT_CALIBRI);

		// Cell Style used for the report
		HSSFCellStyle globalCellFormat = workbook.createCellStyle();
		globalCellFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		globalCellFormat.setFont(globalFont);

		// Separator Cell Style
		HSSFCellStyle separatorCellFormat = workbook.createCellStyle();
		separatorCellFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		separatorCellFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		separatorCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		separatorCellFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		separatorCellFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		separatorCellFormat.setFont(globalFont);

		// Left Corner Cell Style
		HSSFCellStyle cornerCellFormat = workbook.createCellStyle();
		cornerCellFormat
				.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		cornerCellFormat.setFillBackgroundColor(HSSFColor.BLACK.index);
		cornerCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cornerCellFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		cornerCellFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cornerCellFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cornerCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		cornerCellFormat.setFont(globalFontBold);

		// Total Sum Column Cell Style
		HSSFCellStyle totalColCellFormat = workbook.createCellStyle();
		totalColCellFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		totalColCellFormat.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		totalColCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalColCellFormat.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		totalColCellFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		totalColCellFormat.setFont(globalFontBold);

		// First row
		int rowCount = 1;
		ArrayList<Integer> endRowList = new ArrayList<Integer>();
		ArrayList<Integer> endColList = null;

		HSSFRow row = null;
		HSSFCell cell = null;

		// Writes Headings
		ReportUtil.writeHeadings(workbook, sheet,
				ReportKeys.CSC_REPORT_HEADINGS, rowCount, dateRange);
		rowCount++;
		int startRow = 0, endRow = 0;

		// Holds column number
		int columnCount = 0, formulaCount = 0, startColumnCount = 0, endColumnCount = 0;

		// Formula cell
		String rowLevelTotalFormula = "", columnLevelTotalFormula = "", weekLevelTotalFormula = "", finalTotalFormula = "";

		int i = 0;
		logger.info("Size of User details" + userDetails.size());

		for (Integer branchId : agentReportVO.getDistinctBranchIdList()) {

			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(cornerCellFormat);
			cell.setCellValue(userDetails.get(i).getBranchDetails()
					.getBranchName());

			startRow = rowCount;
			formulaCount = 0;

			while (i < userDetails.size()
					&& userDetails.get(i) != null
					&& userDetails.get(i).getBranchDetails().getBranchId() == branchId) {

				endColList = new ArrayList<Integer>();
				dayWiseData = agentDaywiseReportMap.get(userDetails.get(i)
						.getUserId());
				columnCount = 0;
				row = sheet.createRow(rowCount++);

				cell = row.createCell(columnCount++);
				cell.setCellStyle(globalCellFormat);
				cell.setCellValue(userDetails.get(i).getUserName());

				cell = row.createCell(columnCount++);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue(ReportKeys.MONTHLY_TARGET);

				cell = row.createCell(columnCount++);
				cell.setCellStyle(separatorCellFormat);
				cell.setCellValue(ReportKeys.DAILY_TARGET);

				startColumnCount = columnCount;

				// fetch the data for the day of the user id
				if (dayWiseData != null && dayWiseData.size() > 0) {

					for (Map.Entry<String, List<ReportDailyNewVO>> entry : dayWiseData
							.entrySet()) {
						String key = entry.getKey();
						List<ReportDailyNewVO> reportDailyNewVO = entry
								.getValue();
						ReportDailyNewVO dailyReportDataObj = reportDailyNewVO
								.get(0);
						if (key.contains(ReportKeys.WEEK_ENDING_DAY)) {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue(dailyReportDataObj
									.getConfirmedXLandHP());
							cell = row.createCell(columnCount++);
							cell.setCellStyle(totalColCellFormat);
							cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
							rowLevelTotalFormula = "SUM("
									+ ReportUtil
											.getColumnName(startColumnCount)
									+ rowCount + ":"
									+ ReportUtil.getColumnName(columnCount - 2)
									+ rowCount + ")";

							cell.setCellFormula(rowLevelTotalFormula);
							startColumnCount = columnCount;
							endColList.add(startColumnCount);
						} else {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue(dailyReportDataObj
									.getConfirmedXLandHP());
						}

					} // end of day wise iteration

					// last week sum
					if (!lastDateDay.contains(ReportKeys.WEEK_ENDING_DAY)) {
						cell = row.createCell(columnCount++);
						cell.setCellStyle(totalColCellFormat);
						cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						rowLevelTotalFormula = "SUM("
								+ ReportUtil.getColumnName(startColumnCount)
								+ rowCount + ":"
								+ ReportUtil.getColumnName(columnCount - 2)
								+ rowCount + ")";

						cell.setCellFormula(rowLevelTotalFormula);
						endColList.add(columnCount);
					}

					// month end sum

					cell = row.createCell(columnCount++);
					cell.setCellStyle(totalColCellFormat);
					cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
					weekLevelTotalFormula = getFormulaStringForColumn(
							endColList, rowCount);

					cell.setCellFormula(weekLevelTotalFormula);

				} else {
					logger.debug("Collection is null");
				}

				endRow = rowCount;
				endColumnCount = columnCount;
				i++; // incrementing the counter to retrieve
				endColList = null;
			} // End of while

			// Formula row at the end of each of branch iteration
			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(cornerCellFormat);
			cell.setCellValue("Total "
					+ userDetails.get(i - 1).getBranchDetails().getBranchName());

			while (formulaCount < columnCount - 1) {
				cell = row.createCell(++formulaCount);
				cell.setCellStyle(totalColCellFormat);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				columnLevelTotalFormula = "SUM("
						+ ReportUtil.getColumnName(formulaCount)
						+ (startRow + 1) + ":"
						+ ReportUtil.getColumnName(formulaCount) + endRow + ")";

				cell.setCellFormula(columnLevelTotalFormula);
			}

			row = sheet.createRow(rowCount++);
			cell = row.createCell(0);
			cell.setCellStyle(cornerCellFormat);
			cell.setCellValue("");

			endRowList.add(endRow + 1);

		} // end of all the branch iteration.

		// build a final count.
		int colValue = 1;
		formulaCount = 1;
		row = sheet.createRow(rowCount++);
		cell = row.createCell(0);
		cell.setCellStyle(cornerCellFormat);
		cell.setCellValue("Total Pilot Stores");

		if (endRowList.size() == 1) {
			int startIndex = 4;
			while (colValue < endColumnCount) {
				cell = row.createCell(formulaCount++);
				cell.setCellStyle(totalColCellFormat);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				finalTotalFormula = "SUM(" + ReportUtil.getColumnName(colValue)
						+ startIndex + ":" + ReportUtil.getColumnName(colValue)
						+ endRowList.get(0) + ")";
				cell.setCellFormula(finalTotalFormula);
				colValue++;
			}
		} else {

			while (colValue < endColumnCount) {
				cell = row.createCell(formulaCount++);
				cell.setCellStyle(totalColCellFormat);
				cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
				finalTotalFormula = getFormulaString(endRowList, colValue);
				cell.setCellFormula(finalTotalFormula);
				colValue++;
			}
		}
		// End row of the report.
		row = sheet.createRow(rowCount++);
		cell = row.createCell(0);
		cell.setCellStyle(cornerCellFormat);
		cell.setCellValue("Total Advisors");

		cell = row.createCell(1);
		cell.setCellStyle(separatorCellFormat);
		cell.setCellValue(userDetails.size());

	}

	/**
	 * 
	 * @param endColList
	 * @param row
	 * @return It will return the formula string to add the series of columns in
	 *         the sheet.
	 */
	private String getFormulaStringForColumn(ArrayList<Integer> endColList,
			int row) {
		String formulaString = "";

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("SUM(");

		for (Integer col : endColList) {
			stringBuilder.append(ReportUtil.getColumnName(col - 1)).append(row)
					.append(",");
		}
		if (stringBuilder.length() > 0) {
			formulaString = stringBuilder.toString();
			formulaString = formulaString.substring(0,
					formulaString.length() - 1) + ")";
		}

		return formulaString;
	}

	/**
	 * 
	 * @param endRowList
	 * @param colValue
	 * @return It will return the formula string to add the random columns in
	 *         the sheet.
	 */
	private String getFormulaString(ArrayList<Integer> endRowList, int colValue) {

		String formulaString = "";

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("SUM(");

		for (Integer row : endRowList) {
			stringBuilder.append(ReportUtil.getColumnName(colValue))
					.append(row).append(",");
		}
		if (stringBuilder.length() > 0) {
			formulaString = stringBuilder.toString();
			formulaString = formulaString.substring(0,
					formulaString.length() - 1) + ")";
		}

		return formulaString;
	}

	@SuppressWarnings("deprecation")
	public static void main(String args[]) {

		Calendar curDateC = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date curDTemp = curDateC.getTime();
		String curDTempStr = formatter.format(curDTemp);

		// Date confRangeDate = DateUtil.toDate(curDTempStr);

		Date curD = DateUtil.toDate(curDTempStr);// curDateC.getTime();

		Date nextD = null;
		Date prevD = null;

		int dayC = curD.getDate();
		int mC = curD.getMonth();
		int yC = curD.getYear();

		String nextDate = null;
		String prevDate = null;

		String currentDate = null;

		if (dayC >= 16) {

			Calendar nextMonthDate = Calendar.getInstance();
			nextMonthDate.add(Calendar.MONTH, 1);

			Date nextDTemp = curDateC.getTime();
			String nextDTempStr = formatter.format(nextDTemp);

			nextD = DateUtil.toDate(nextDTempStr);// nextMonthDate.getTime();

			int dayN = nextD.getDate();
			int mN = nextD.getMonth();
			int yN = nextD.getYear();

			currentDate = 16 + "/" + mC + "/" + yC;
			nextDate = 15 + "/" + mN + "/" + yN;

		} else {

			Calendar nextMonthDate = Calendar.getInstance();
			nextMonthDate.add(Calendar.MONTH, -1);

			Date prevDTemp = curDateC.getTime();
			String prevDTempStr = formatter.format(prevDTemp);

			prevD = DateUtil.toDate(prevDTempStr); // nextMonthDate.getTime();

			int dayN = prevD.getDate();
			int mN = prevD.getMonth();
			int yN = prevD.getYear();

			prevDate = 16 + "/" + mN + "/" + yN;
			currentDate = 15 + "/" + mC + "/" + yC;

		}

		/*
		 * Date fromDate = DateUtil.toDate("16/03/2016");
		 * 
		 * DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); Date
		 * confDate=calendar.getTime(); String strDate =
		 * formatter.format(confDate);
		 * 
		 * Date confRangeDate = DateUtil.toDate(strDate);
		 * 
		 * Calendar calendar = Calendar.getInstance();
		 * calendar.setTime(fromDate);
		 * 
		 * calendar.add(Calendar.MONTH, -2); DateFormat formatter = new
		 * SimpleDateFormat("dd/MM/yyyy");
		 * 
		 * Date confDate=calendar.getTime();
		 * 
		 * String
		 * confdateR=confDate.getDate()+"/"+confDate.getMonth()+"/"+confDate
		 * .getYear(); String strDate = formatter.format(confDate);
		 * 
		 * 
		 * Date confDateRange = DateUtil.toDate(confdateR);
		 */

		/*
		 * Calendar calendar = Calendar.getInstance();
		 * calendar.setTime(fromDate); calendar.add(Calendar.MONTH, -3);
		 * DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); Date
		 * confDate=calendar.getTime(); String strDate =
		 * formatter.format(confDate);
		 * 
		 * Date confRangeDate = DateUtil.toDate(strDate);
		 */

	}

	/**
	 * This method return the list of active Users.
	 * 
	 * @return <code>List</code> of UserDetails
	 * 
	 * @throws MISPException
	 *             If any Exception occurs
	 */
	public List<UserDetails> getActiveUserDetailList(
			DeductionReportVO dedReportVO, int noOfRoles) throws MISPException {
		logger.entering("getActiveUserDetailList");
		List<UserDetails> userDetailList = null;

		try {
			// Changed to include USSD details also
			userDetailList = this.userManager
					.getActiveUserDetailsListForDeductions(dedReportVO,
							noOfRoles);
		} catch (DBException exception) {
			logger.error("Error occured while getting list of all users ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getActiveUserDetailList");
		return userDetailList;
	}

	public Map<Integer, List<Integer>> getConfirmedCustList(
			DeductionReportVO dedReportVO) throws Exception {
		logger.entering("getConfirmedCustList ", dedReportVO);

		List<Integer> custList = new ArrayList<Integer>();
		Map<Integer, List<Integer>> dedMap = new HashMap<Integer, List<Integer>>();
		try {
			String fromDate = dedReportVO.getFromDate();
			String toDate = dedReportVO.getToDate();
			int userId = 0;

			fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1)
					+ "-" + DateUtil.getDay(DateUtil.toSQLDate(fromDate))
					+ " 00:00:00";

			toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
					+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

			logger.info("from Date, for prior 3 month ", fromDate);
			logger.info("to Date, for prior 3 month ", toDate);

			for (int i = 0; i < dedReportVO.getUserDetailsList().size(); i++) {
				userId = dedReportVO.getUserDetailsList().get(i).getUserId();
				custList = reportManager.fetchConfirmedCustomerList(userId,
						fromDate, toDate);
				dedMap.put(userId, custList);
			}

		} catch (Exception exception) {
			logger.error(
					"Error occured while getting confirmed customer list ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getConfirmedCustList ");
		return dedMap;

	}

	public Map<Integer, CustDeductionReportVO> getCustDeductionReportList(
			Map<Integer, List<Integer>> dedReportMap, String fromDate,
			String toDate) throws Exception {

		Object params[] = { dedReportMap.size(), fromDate, toDate };
		logger.entering("getCustDeductionReportList", params);
		Map<Integer, CustDeductionReportVO> deductionMap = new HashMap<Integer, CustDeductionReportVO>();
		List<CustDeductionReportVO> custDeductionList = new ArrayList<CustDeductionReportVO>();
		try {

			fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1)
					+ "-" + DateUtil.getDay(DateUtil.toSQLDate(fromDate))
					+ " 00:00:00";

			toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
					+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";

			logger.info("current_month from Date ", fromDate);
			logger.info("current_month to Date ", toDate);

			Iterator<Entry<Integer, List<Integer>>> iterator = dedReportMap
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, List<Integer>> entry = iterator.next();
				int agentId = entry.getKey();
				List<Integer> custList = entry.getValue();
				CustDeductionReportVO custDedReportVO = new CustDeductionReportVO();
				if (custList.size() > 0) {
					custDedReportVO = reportManager.fetchCustomerDeductionList(
							custList, fromDate, toDate);
					if (!deductionMap.containsKey(agentId)) {
						deductionMap.put(agentId, custDedReportVO);
					}

				}
			}

		} catch (Exception exception) {
			logger.error("Error occured while getting deduction report ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getCustDeductionReportList", custDeductionList.size());
		return deductionMap;
	}

	/**
	 * This method writes the first month deductions report data to the excel
	 * workbook.
	 * 
	 * @param workbook
	 *            Excel file workbook object to which report will be written
	 * 
	 * @param dedReportVO
	 *            List<CustDeductionReportVO> object containing report data
	 * 
	 * @param fromDate
	 *            type of String
	 * 
	 * @param toDate
	 *            type of String
	 * @param reportType
	 * 
	 * @throws MISPException
	 *             if any Exception occurs
	 * 
	 */
	public void writeDeductionReport(HSSFWorkbook workbook,
			DeductionReportVO dedReportVO, String fromDate, String toDate,
			int reportType) throws MISPException {
		logger.entering("writeDeductionReport", dedReportVO, fromDate, toDate,
				reportType);

		try {
			writeDeductionSheet(workbook, dedReportVO, fromDate, toDate,
					reportType);

		} catch (Exception e) {
			logger.error("Error occured while writing deduction report", e);
			throw new MISPException(e);
		}

		logger.exiting("writeDeductionReport");
	}

	private void writeDeductionSheet(HSSFWorkbook workbook,
			DeductionReportVO dedReportVO, String fromDate, String toDate,
			int reportType) {

		Object params[] = { dedReportVO, fromDate, toDate, reportType };
		logger.entering("writeDeductionSheet", params);
		String sheetName = null;
		String sheetTitle = null;
		String[] rowHeader = null;

		// Creates Sheet Name
		if (reportType == 0) {
			sheetName = ReportKeys.FIRST_MONTH_DEDUCTION_SHEET_NAME;
		} else {
			sheetName = ReportKeys.FIRST_QUARTER_DEDUCTION_SHEET_NAME;
		}

		HSSFSheet sheet = workbook.createSheet(sheetName);
		/**
		 * Sets the sheet zoom Takes the fraction of NUMERATOR and DENOMINATOR
		 * Eg : To express a zoom of 75%, use 3 as numerator and 4 as
		 * denominator.
		 */
		sheet.setZoom(ReportKeys.AGENT_REP_ZOOM_NUMERATOR,
				ReportKeys.AGENT_REP_ZOOM_DENOMINATOR);

		// Font used for the report
		HSSFFont globalFont = workbook.createFont();
		globalFont.setFontName(MIPFont.FONT_CALIBRI);
		globalFont.setFontHeightInPoints(ReportKeys.REP_FONT_SIZE);

		HSSFFont globalFontBold = workbook.createFont();
		globalFontBold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		globalFontBold.setFontName(MIPFont.FONT_CALIBRI);

		// Cell Style used for the report
		HSSFCellStyle globalCellFormat = workbook.createCellStyle();
		globalCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		globalCellFormat.setFont(globalFont);

		// Top Cell Style
		HSSFCellStyle topCellFormat = workbook.createCellStyle();
		topCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		topCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		topCellFormat.setFont(globalFont);

		// Separator Cell Style
		HSSFCellStyle separatorCellFormat = workbook.createCellStyle();
		separatorCellFormat.setBorderRight(HSSFCellStyle.BORDER_THIN);
		separatorCellFormat.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		separatorCellFormat.setFont(globalFont);

		// Date Cell Style
		HSSFCellStyle dateCellFormat = workbook.createCellStyle();
		dateCellFormat.cloneStyleFrom(ReportUtil
				.getHeaderRowCellStyle(workbook));
		dateCellFormat.setBorderTop(HSSFCellStyle.NO_FILL);
		dateCellFormat.setBorderBottom(HSSFCellStyle.NO_FILL);

		// Total Cell Style
		HSSFCellStyle totalCellFormat = workbook.createCellStyle();
		totalCellFormat.setBorderTop(HSSFCellStyle.BORDER_THIN);
		totalCellFormat.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		totalCellFormat.setFont(globalFontBold);

		// First row
		int rowCount = 1;
		HSSFRow row = null;
		HSSFCell cell = null;

		// Holds title row index
		int titleRow = rowCount;

		// Creates Sheet Title
		if (reportType == 0) {
			sheetTitle = ReportKeys.FIRST_MONTH_DEDUCTION_SHEET_NAME + " ( "
					+ fromDate.replaceAll("/", "-") + " To "
					+ toDate.replaceAll("/", "-") + " )";
		} else {
			sheetTitle = ReportKeys.FIRST_QUARTER_DEDUCTION_SHEET_NAME + " ( "
					+ fromDate.replaceAll("/", "-") + " To "
					+ toDate.replaceAll("/", "-") + " )";
		}

		row = sheet.createRow(rowCount++);
		row.setHeight((short) 500);
		cell = row.createCell(0);
		cell.setCellStyle(ReportUtil.getHeaderRowCellStyle(workbook));
		cell.setCellValue(sheetTitle);

		row = sheet.createRow(rowCount);

		rowCount++;

		// Holds the index of the row where the data starts, used to create
		// formulas.
		int dataRowIndex = rowCount + 1;

		/**
		 * Writes agent report data.
		 */

		if (reportType == 0) {
			rowHeader = ReportKeys.FIRST_MONTH_DEDUCTION_HEADING;
		} else {
			rowHeader = ReportKeys.FIRST_QUARTER_DEDUCTION_HEADING;
		}

		// Holds last column index.
		int lastColumnIndex = rowHeader.length - 1;

		// Creates the Header Cell Style
		HSSFCellStyle headerFormat = ReportUtil.getHeaderRowCellStyle(workbook);

		// Writes the column headings.
		ReportUtil.writeHeadings(workbook, sheet, rowHeader, rowCount,
				headerFormat);

		rowCount++;
		for (UserDetails userDetails : dedReportVO.getUserDetailsList()) {
			row = sheet.createRow(rowCount++);

			// Holds column no
			int columnCount = 0;

			cell = row.createCell(columnCount++);
			cell.setCellStyle(globalCellFormat);
			cell.setCellValue(userDetails.getUserUid());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getFname() + " "
					+ userDetails.getSname());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getMsisdn());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			if (userDetails.getEmailId() != null) {
				cell.setCellValue(userDetails.getEmailId());
			} else {
				cell.setCellValue("");
			}

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getGender());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getRoleMaster().getRoleName());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchRegion());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchCity());

			cell = row.createCell(columnCount++);
			cell.setCellStyle(separatorCellFormat);
			cell.setCellValue(userDetails.getBranchDetails().getBranchStreet());

			Map<Integer, CustDeductionReportVO> deductionReport = dedReportVO
					.getDeductionReport();
			for (Integer userId : deductionReport.keySet()) {
				if (userId.intValue() == userDetails.getUserId()) {
					if (dedReportVO.getDeductionReport().get(
							userDetails.getUserId()) != null) {

						if (reportType == 0) {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							/*
							 * BigDecimal partialXL =
							 * dedReportVO.getDeductionReport()
							 * .get(userDetails.getUserId())
							 * .getPartialDedCountXL();
							 */

							if (dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getPartialDedCountXL()
									.compareTo(BigDecimal.ZERO) != 0
									|| dedReportVO.getDeductionReport()
											.get(userDetails.getUserId())
											.getPartialDedCountXL() != null) {

								cell.setCellValue(dedReportVO
										.getDeductionReport()
										.get(userDetails.getUserId())
										.getPartialDedCountXL().doubleValue());
							} else {
								cell.setCellValue("0");
							}

							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							/*
							 * BigDecimal partialXL =
							 * dedReportVO.getDeductionReport()
							 * .get(userDetails.getUserId())
							 * .getPartialDedCountXL();
							 */

							if (dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getPartialDedCountXLPrev()
									.compareTo(BigDecimal.ZERO) != 0
									|| dedReportVO.getDeductionReport()
											.get(userDetails.getUserId())
											.getPartialDedCountXLPrev() != null) {

								cell.setCellValue(dedReportVO
										.getDeductionReport()
										.get(userDetails.getUserId())
										.getPartialDedCountXLPrev()
										.doubleValue());
							} else {
								cell.setCellValue("0");
							}

						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);

						if (dedReportVO.getDeductionReport()
								.get(userDetails.getUserId())
								.getFullyDedCountXL()
								.compareTo(BigDecimal.ZERO) != 0
								|| dedReportVO.getDeductionReport()
										.get(userDetails.getUserId())
										.getFullyDedCountXL() != null) {
							cell.setCellValue(dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getFullyDedCountXL().doubleValue());
						} else {
							cell.setCellValue("0");
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);

						if (dedReportVO.getDeductionReport()
								.get(userDetails.getUserId())
								.getFullyDedCountXLPrev()
								.compareTo(BigDecimal.ZERO) != 0
								|| dedReportVO.getDeductionReport()
										.get(userDetails.getUserId())
										.getFullyDedCountXLPrev() != null) {
							cell.setCellValue(dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getFullyDedCountXLPrev().doubleValue());
						} else {
							cell.setCellValue("0");
						}

						/*
						 * BigDecimal fullyXL = dedReportVO.getDeductionReport()
						 * .get(userDetails.getUserId()).getFullyDedCountXL();
						 * if (fullyXL != null && fullyXL.doubleValue() != 0.0)
						 * { cell.setCellValue(fullyXL.doubleValue()); } else {
						 * cell.setCellValue("0"); }
						 */

						if (reportType == 0) {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							/*
							 * BigDecimal partialHP =
							 * dedReportVO.getDeductionReport()
							 * .get(userDetails.getUserId())
							 * .getPartialDedCountHP();
							 */
							if (dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getPartialDedCountHP()
									.compareTo(BigDecimal.ZERO) != 0
									|| dedReportVO.getDeductionReport()
											.get(userDetails.getUserId())
											.getPartialDedCountHP() != null) {
								cell.setCellValue(dedReportVO
										.getDeductionReport()
										.get(userDetails.getUserId())
										.getPartialDedCountHP().doubleValue());
							} else {
								cell.setCellValue("0");
							}

							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							/*
							 * BigDecimal partialHP =
							 * dedReportVO.getDeductionReport()
							 * .get(userDetails.getUserId())
							 * .getPartialDedCountHP();
							 */
							if (dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getPartialDedCountHPPrev()
									.compareTo(BigDecimal.ZERO) != 0
									|| dedReportVO.getDeductionReport()
											.get(userDetails.getUserId())
											.getPartialDedCountHPPrev() != null) {
								cell.setCellValue(dedReportVO
										.getDeductionReport()
										.get(userDetails.getUserId())
										.getPartialDedCountHPPrev()
										.doubleValue());
							} else {
								cell.setCellValue("0");
							}
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						/*
						 * BigDecimal fullyHP = dedReportVO.getDeductionReport()
						 * .get(userDetails.getUserId()).getFullyDedCountHP();
						 */
						if (dedReportVO.getDeductionReport()
								.get(userDetails.getUserId())
								.getFullyDedCountHP()
								.compareTo(BigDecimal.ZERO) != 0
								|| dedReportVO.getDeductionReport()
										.get(userDetails.getUserId())
										.getFullyDedCountHP() != null) {
							cell.setCellValue(dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getFullyDedCountHP().doubleValue());
						} else {
							cell.setCellValue("0");
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						/*
						 * BigDecimal fullyHP = dedReportVO.getDeductionReport()
						 * .get(userDetails.getUserId()).getFullyDedCountHP();
						 */
						if (dedReportVO.getDeductionReport()
								.get(userDetails.getUserId())
								.getFullyDedCountHPPrev()
								.compareTo(BigDecimal.ZERO) != 0
								|| dedReportVO.getDeductionReport()
										.get(userDetails.getUserId())
										.getFullyDedCountHPPrev() != null) {
							cell.setCellValue(dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getFullyDedCountHPPrev().doubleValue());
						} else {
							cell.setCellValue("0");
						}

						if (reportType == 0) {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							/*
							 * BigDecimal partialIP =
							 * dedReportVO.getDeductionReport()
							 * .get(userDetails.getUserId())
							 * .getPartialDedCountIP();
							 */
							if (dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getPartialDedCountIP()
									.compareTo(BigDecimal.ZERO) != 0
									|| dedReportVO.getDeductionReport()
											.get(userDetails.getUserId())
											.getPartialDedCountIP() != null) {
								cell.setCellValue(dedReportVO
										.getDeductionReport()
										.get(userDetails.getUserId())
										.getPartialDedCountIP().doubleValue());
							} else {
								cell.setCellValue("0");
							}

							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							/*
							 * BigDecimal partialIP =
							 * dedReportVO.getDeductionReport()
							 * .get(userDetails.getUserId())
							 * .getPartialDedCountIP();
							 */
							if (dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getPartialDedCountIPPrev()
									.compareTo(BigDecimal.ZERO) != 0
									|| dedReportVO.getDeductionReport()
											.get(userDetails.getUserId())
											.getPartialDedCountIPPrev() != null) {
								cell.setCellValue(dedReportVO
										.getDeductionReport()
										.get(userDetails.getUserId())
										.getPartialDedCountIPPrev()
										.doubleValue());
							} else {
								cell.setCellValue("0");
							}
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						/*
						 * BigDecimal fullyIP = dedReportVO.getDeductionReport()
						 * .get(userDetails.getUserId()).getFullyDedCountIP();
						 */
						if (dedReportVO.getDeductionReport()
								.get(userDetails.getUserId())
								.getFullyDedCountIP()
								.compareTo(BigDecimal.ZERO) != 0
								|| dedReportVO.getDeductionReport()
										.get(userDetails.getUserId())
										.getFullyDedCountIP() != null) {
							cell.setCellValue(dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getFullyDedCountIP().doubleValue());
						} else {
							cell.setCellValue("0");
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						/*
						 * BigDecimal fullyIP = dedReportVO.getDeductionReport()
						 * .get(userDetails.getUserId()).getFullyDedCountIP();
						 */
						if (dedReportVO.getDeductionReport()
								.get(userDetails.getUserId())
								.getFullyDedCountIPPrev()
								.compareTo(BigDecimal.ZERO) != 0
								|| dedReportVO.getDeductionReport()
										.get(userDetails.getUserId())
										.getFullyDedCountIPPrev() != null) {
							cell.setCellValue(dedReportVO.getDeductionReport()
									.get(userDetails.getUserId())
									.getFullyDedCountIPPrev().doubleValue());
						} else {
							cell.setCellValue("0");
						}

					} else {

						if (reportType == 0) {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue("0");

							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue("0");
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						cell.setCellValue("0");

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						cell.setCellValue("0");

						if (reportType == 0) {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue("0");

							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue("0");
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						cell.setCellValue("0");

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						cell.setCellValue("0");

						if (reportType == 0) {
							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue("0");

							cell = row.createCell(columnCount++);
							cell.setCellStyle(separatorCellFormat);
							cell.setCellValue("0");
						}

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						cell.setCellValue("0");

						cell = row.createCell(columnCount++);
						cell.setCellStyle(separatorCellFormat);
						cell.setCellValue("0");

					}

				}/*
				 * else{ if (reportType == 0) { cell =
				 * row.createCell(columnCount++);
				 * cell.setCellStyle(separatorCellFormat);
				 * cell.setCellValue("0"); }
				 * 
				 * cell = row.createCell(columnCount++);
				 * cell.setCellStyle(separatorCellFormat);
				 * cell.setCellValue("0");
				 * 
				 * if (reportType == 0) { cell = row.createCell(columnCount++);
				 * cell.setCellStyle(separatorCellFormat);
				 * cell.setCellValue("0"); }
				 * 
				 * cell = row.createCell(columnCount++);
				 * cell.setCellStyle(separatorCellFormat);
				 * cell.setCellValue("0");
				 * 
				 * if (reportType == 0) { cell = row.createCell(columnCount++);
				 * cell.setCellStyle(separatorCellFormat);
				 * cell.setCellValue("0"); }
				 * 
				 * cell = row.createCell(columnCount++);
				 * cell.setCellStyle(separatorCellFormat);
				 * cell.setCellValue("0"); }
				 */

			}
		}

		// Creates Total Row
		row = sheet.createRow(rowCount);
		// Holds column no
		int columnCount = 0;
		if (reportType == 0) {
			columnCount = ReportKeys.FIRST_MONTH_DEDUCTION_HEADING.length - 12;
		} else {
			columnCount = ReportKeys.FIRST_QUARTER_DEDUCTION_HEADING.length - 3;
		}

		cell = row.createCell(0);
		cell.setCellStyle(topCellFormat);
		cell.setCellValue(ReportKeys.AGENT_REP_TOTAL);

		for (int i = 1; i < columnCount; i++) {
			cell = row.createCell(i);
			cell.setCellStyle(topCellFormat);
			cell.setCellValue("");
		}

		// Holds the formula for last cell of total row
		String totalFormula = "";

		if (reportType == 0) {
			// Creates formula for second last cell of total row
			
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 11)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 11) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);
			
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 10)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 10) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);
			
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 9)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 9) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);
			
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 8)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 8) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);
			
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 7)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 7) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);
			
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 6)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 6) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);
			
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 5)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 5) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);

			// Creates formula for second last cell of total row
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 4)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 4) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);

			// Creates formula for second last cell of total row
			totalFormula = "SUM("
					+ ReportUtil.getColumnName(lastColumnIndex - 3)
					+ dataRowIndex + ":"
					+ ReportUtil.getColumnName(lastColumnIndex - 3) + rowCount
					+ ")";

			// Creates the last two cells of total row
			cell = row.createCell(columnCount++);
			cell.setCellStyle(topCellFormat);
			cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
			cell.setCellFormula(totalFormula);
		}

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 2)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 2) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for second last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex - 1)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex - 1) + rowCount
				+ ")";

		// Creates the last two cells of total row
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Creates formula for last cell of total row
		totalFormula = "SUM(" + ReportUtil.getColumnName(lastColumnIndex)
				+ dataRowIndex + ":"
				+ ReportUtil.getColumnName(lastColumnIndex) + rowCount + ")";
		cell = row.createCell(columnCount++);
		cell.setCellStyle(topCellFormat);
		cell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
		cell.setCellFormula(totalFormula);

		// Resize Columns
		sheet.setColumnWidth(0, 4000);
		for (int i = 1; i <= lastColumnIndex; i++)
			sheet.autoSizeColumn(i);

		// Merges the title row
		sheet.addMergedRegion(new CellRangeAddress(titleRow, titleRow, 0,
				lastColumnIndex));

		/**
		 * To implement the style(border) of title row. Some of the style will
		 * not be preserved after merging
		 */

		// Title row range address
		CellRangeAddress titleCellAddress = new CellRangeAddress(titleRow,
				titleRow, 0, lastColumnIndex);

		HSSFRegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());
		HSSFRegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN,
				titleCellAddress, sheet, sheet.getWorkbook());

		logger.exiting("writeDeductionSheet");

	}

	public Map<Integer, List<Integer>> getConfirmedCustListForQuarterReport(
			DeductionReportVO dedReportVO) throws Exception {
		logger.entering("getConfirmedCustListForQuarterReport ", dedReportVO);

		List<CustDeductionReportVO> custDedReportList = new ArrayList<CustDeductionReportVO>();
		Map<Integer, List<Integer>> dedMap = new HashMap<Integer, List<Integer>>();
		List<Integer> custList = new ArrayList<Integer>();
		try {
			String fromDate = dedReportVO.getFromDate();
			String toDate = dedReportVO.getToDate();
			int userId = 0;

			fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1)
					+ "-" + DateUtil.getDay(DateUtil.toSQLDate(fromDate))
					+ " 00:00:00";

			toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
					+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";
			for (int i = 0; i < dedReportVO.getUserDetailsList().size(); i++) {
				userId = dedReportVO.getUserDetailsList().get(i).getUserId();
				custList = reportManager
						.fetchConfirmedCustListForFirstQuarterMonth(userId,
								fromDate, toDate);
				dedMap.put(userId, custList);
			}

		} catch (Exception exception) {
			logger.error(
					"Error occured while getting confirmed customer list for First Quarter month ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getConfirmedCustListForQuarterReport ",
				custDedReportList.size());
		return dedMap;

	}

	public Map<Integer, CustDeductionReportVO> getCustDeductionQuarterReportList(
			Map<Integer, List<Integer>> dedReportMap, String fromDate,
			String toDate) throws Exception {
		Object params[] = { dedReportMap.size(), fromDate, toDate };
		logger.entering("getCustDeductionQuarterReportList", params);

		Map<Integer, CustDeductionReportVO> deductionMap = new HashMap<Integer, CustDeductionReportVO>();
		try {

			fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1)
					+ "-" + DateUtil.getDay(DateUtil.toSQLDate(fromDate))
					+ " 00:00:00";

			toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
					+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";
			logger.info("current_month from Date ", fromDate);
			logger.info("current_month to Date ", toDate);

			Iterator<Entry<Integer, List<Integer>>> iterator = dedReportMap
					.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, List<Integer>> entry = iterator.next();
				int agentId = entry.getKey();
				List<Integer> custList = entry.getValue();
				CustDeductionReportVO custDedReportVO = new CustDeductionReportVO();
				if (custList.size() > 0) {
					custDedReportVO = reportManager.fetchQuarterDeductionList(
							custList, agentId, fromDate, toDate);
					if (!deductionMap.containsKey(agentId)) {
						deductionMap.put(agentId, custDedReportVO);
					}

				}
			}

		} catch (Exception exception) {
			logger.error("Error occured while getting deduction report ",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getCustDeductionQuarterReportList", deductionMap.size());
		return deductionMap;
	}

}
