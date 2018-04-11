package com.mip.application.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.ReportKeys;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.ProductDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.services.ProductsManagementService;
import com.mip.application.services.ReportManagementService;
import com.mip.application.services.UserService;
import com.mip.application.utils.FileUtil;
import com.mip.application.view.CustDeductionReportVO;
import com.mip.application.view.CustomerReportDataVO;
import com.mip.application.view.DeductionReportVO;
import com.mip.application.view.DeregisteredCustomersVO;
import com.mip.application.view.ReportAgentVO;
import com.mip.application.view.ReportCustomerVO;
import com.mip.application.view.ReportDailyNewVO;
import com.mip.application.view.ReportFinancialVO;
import com.mip.application.view.ReportWeeklyVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;

/**
 * <p>
 * <code>ReportManagementController</code> handles all requests and response
 * from the ReportManagementScreens (Customer Reports and Financial Reports). <br/>
 * This controller extends the <code>BasePlatformController</code> class of MISP
 * framework.
 * </p>
 * 
 * @see com.mip.framework.controllers.BasePlatformController
 * 
 * @author T H B S
 * 
 */
public class ReportManagementController extends BasePlatformController {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ReportManagementController.class);
	
	
	/**
	 * An instance of session.
	 */
	HttpSession session = null;
	
	private UserDetails userDetails = null;

	/**
	 * Set inversion of Control for <code>ReportManagementService</code>
	 */
	private ReportManagementService reportManagementService;
	
	/**
	 * Set inversion of Control for <code>UserService</code>
	 */
	private UserService userService;

	/**
	 * Set inversion of Control for <code>OffersManagementService</code>
	 */
	int noOfRoles=0; // To put Mobile And CSC agent role in Agent Report Section
	private ProductsManagementService productsMgmtService;

	public void setReportManagementService(
			ReportManagementService reportManagementService) {
		this.reportManagementService = reportManagementService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setProductsMgmtService(ProductsManagementService productsMgmtService) {
		this.productsMgmtService = productsMgmtService;
	}
	
	/**
	 * This method handles the request and response for retrieving customer
	 * ranges based on the total number of subscribed customers.
	 * 
	 * @param httpServletRequest
	 *            an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *            an instance of HTTPServletRequest
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView retrieveCustomerRanges(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		logger.entering("retrieveCustomerRanges");

		Map<String, String> reportCustomerRangeMap = null;
		ModelAndView mavObj = null;
		try {
			reportCustomerRangeMap = reportManagementService
					.getCustomerRanges();

			// Checks if there subscribed customers for current month
			if (reportCustomerRangeMap == null
					|| reportCustomerRangeMap.size() == 0) {
				return super.info(SuccessMessages.REPORTS_NO_CUSTOMERS_SUBSCRIBED);
			}
			mavObj = new ModelAndView(MAVPaths.VIEW_CUSTOMER_REPORT);
			mavObj.addObject(MAVObjects.MAP_REPORT_RANGES,
					reportCustomerRangeMap);

		} catch (MISPException e) {
			logger
					.error("Exception occured while retrieving subscribed customer ranges");
			return super.error(FaultMessages.GENERIC_ERROR);
		} catch (Exception e) {

			logger.error(
					"Exception occured while retrieving subscribed customer ranges",
					e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("retrieveCustomerRanges", reportCustomerRangeMap);

		return mavObj;
	}
	
	/**
	 * This method handles the request and response for downloading customer
	 * report
	 * 
	 * @param httpServletRequest
	 *            an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *            an instance of HTTPServletRequest
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView downloadCustomerReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportCustomerVO customerReportVO) {

		logger.entering("downloadCustomerReport");
		ModelAndView mav = null;
		Map<String, String> customerRanges=null;

		File reportDirectory = null;
		File custReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;
		String fromDate = null;
		String toDate = null;
		
		try {
		
			
			int filterOption = Integer.valueOf(httpServletRequest.getParameter("filterOption"));
			if(filterOption == 0) {
				// Date Range
				fromDate = customerReportVO.getFromDate();
				toDate = customerReportVO.getToDate();				
				
			} else if (filterOption == 1) {
				int quickSelectOption = Integer.valueOf(httpServletRequest
						.getParameter("quickSelectOption"));
				if (quickSelectOption == 0) {
					// Current Week
					fromDate = DateUtil.toDateString(DateUtil.getISOFirstDayOfWeek(new Date()));
					toDate = DateUtil.toDateString(DateUtil.getISOLastDayOfWeek(new Date()));
				} else if(quickSelectOption == 1) {
					// Previous Week
					fromDate = DateUtil.toDateString(DateUtil.getISOFirstDayOfPrevWeek(new Date()));
					toDate = DateUtil.toDateString(DateUtil.getISOLastDayOfWeek(DateUtil.toDate(fromDate)));
				} else if(quickSelectOption == 2) {
					// Current Month
					fromDate = DateUtil.toDateString(DateUtil.getFirstDayOfTheMonth(new Date()));
					toDate = DateUtil.toDateString(DateUtil.getLastDayOfTheMonth(new Date()));
				} else if(quickSelectOption == 3) {
					// Previous Month
					fromDate = DateUtil.toDateString(DateUtil.getFirstDayOfThePreviousMonth(new Date()));
					toDate = DateUtil.toDateString(DateUtil.getLastDayOfThePreviousMonth(new Date()));
				}
			}
			
			customerReportVO.setFromDate(fromDate);
			customerReportVO.setToDate(toDate);
			
			//Report Style - New (Options)
			if(customerReportVO.getRepStyle().equalsIgnoreCase("2")){
				customerRanges = reportManagementService
						.getCustomerRanges(customerReportVO);
				if (customerRanges == null || customerRanges.isEmpty()) {
					logger.debug("There are no customers for the selected user criteria.");
					httpServletRequest.setAttribute("errorMessage", "true");
					mav = new ModelAndView(MAVPaths.VIEW_CUSTOMER_REPORT);
					return mav;
				} else {
					generateCustomerReportArchive(httpServletRequest,
							httpServletResponse, customerReportVO, customerRanges);
				}
			}
			//Report Style - Old(Customer Ranges)
			else if(customerReportVO.getRepStyle().equalsIgnoreCase("1")){
			
				try{
					
				String reportRange = customerReportVO.getReportRange();

				logger.debug("Report Range: ", reportRange);

				// Creates the file name
				DateFormat format = new SimpleDateFormat("ddMMyyyy");
				String currTimeStamp = format.format(new Date());

				String reportFileName = ReportKeys.CUST_REPORT_FILE_NAME
						+ currTimeStamp + "_" + reportRange.split("_")[2]
						+ ReportKeys.REPORT_FILE_TYPE;

				// Creates the directory for generating report
				reportDirectory = new File(httpServletRequest.getSession()
						.getServletContext().getRealPath("/")
						+ ReportKeys.REPORTS_FOLDER);

				if (!reportDirectory.exists()) {
					logger.debug("Creating directory");
					reportDirectory.mkdir();
				}

				// Creates the report file
				custReportFile = new File(reportDirectory.getAbsolutePath()
						+ File.separator + reportFileName);

				if (!custReportFile.exists()) {
					custReportFile.createNewFile();
				}

				// Writes report data to excel file
				workbook = new HSSFWorkbook();
				reportManagementService.writeCustomerReport(workbook, reportRange);

				fileOutStream = new FileOutputStream(custReportFile);
				workbook.write(fileOutStream);

				logger.debug("Data written to excel workbook");

				// Sets the report file for download
				httpServletResponse.setContentType("application/vnd.ms-excel");
				httpServletResponse.setHeader("Content-Disposition",
						"attachment; filename=" + reportFileName);
				servletOutStream = httpServletResponse.getOutputStream();
				fileInputStream = new FileInputStream(custReportFile);
				int i;
				while ((i = fileInputStream.read()) != -1) {
					servletOutStream.write(i);
				}

				servletOutStream.flush();

			} catch (IOException e) {
				logger.error("Exception occured while downloading customer report");
				return null;
			} catch (MISPException e) {
				logger.error("Exception occured while downloading customer report",
						e);
				return null;
			} catch (Exception e) {

				logger.error("Exception occured while downloading customer report",
						e);
				return super.error(FaultMessages.GENERIC_ERROR);
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
					if (custReportFile != null && custReportFile.exists()) {
						custReportFile.delete();
					}
				} catch (IOException e) {
					logger.error("Exception occured while closing " +
									"resources after customer report download");
					return null;
				}

			}
				
			}				
		} catch (Exception e) {
			logger.error("Exception occured while downloading customer report",e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("downloadCustomerReport");
		// Returns null as response has been set
		return null;

	}

	public ModelAndView downloadCustomerReportCSV(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportCustomerVO customerReportVO) {

		logger.entering("downloadCustomerReportCSV");
		ModelAndView mav = null;
		Map<String, String> customerRanges = null;

		File reportDirectory = null;
		File custReportFile = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;
		String fromDate = null;
		String toDate = null;
		
		try {
			
			userDetails = (UserDetails)httpServletRequest.getSession().
					getAttribute(SessionKeys.SESSION_USER_DETAILS);				
			

			// Report Style - Old(Customer Ranges)
			if (customerReportVO.getRepStyle().equalsIgnoreCase("1")) {

				try {

					String reportRange = customerReportVO.getReportRange();

					logger.debug("Report Range: ", reportRange);

					// Creates the file name
					DateFormat format = new SimpleDateFormat("ddMMyyyy");
					String currTimeStamp = format.format(new Date());

					String reportFileName = ReportKeys.CUST_REPORT_FILE_NAME
							+ currTimeStamp + "_" + reportRange.split("_")[2]
							+ ReportKeys.REPORT_FILE_TYPE_CSV;

					// Creates the directory for generating report
					reportDirectory = new File(httpServletRequest.getSession()
							.getServletContext().getRealPath("/")
							+ ReportKeys.REPORTS_FOLDER);

					if (!reportDirectory.exists()) {
						logger.debug("Creating directory");
						reportDirectory.mkdir();
					}

					// Creates the report file
					custReportFile = new File(reportDirectory.getAbsolutePath()
							+ File.separator + reportFileName);

					if (!custReportFile.exists()) {
						custReportFile.createNewFile();
					}

					List<CustomerReportDataVO> customerReportDataVOList = this.reportManagementService
							.generateCustomerReportOld(
									reportRange);
					
					if (null != customerReportDataVOList
							&& customerReportDataVOList.size() > 0) {

						
						logger.info("No of records for selected criteria: ",
								customerReportDataVOList.size());

						fileOutStream = new FileOutputStream(custReportFile);

						StringBuilder columnRow = new StringBuilder();
						String[] headings = ReportKeys.CUST_REPORT_HEADINGS_ALL;
						if("ROLE_INSURER".equals(userDetails.getRoleMaster().getRoleDescription())){
							
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
							double totalCoverCharges = 0;
							
							stringTokenizer = new StringTokenizer(
									cr.getCoverChargesAll(), ",");
							while (stringTokenizer.hasMoreTokens()) {
								totalCoverCharges += Double
										.parseDouble(stringTokenizer
												.nextToken());
							}
							
							String productSubscribedTo = cr.getProductNameAll();
							
							String[] createdBys = null;
							String[] createdDates = null;
							String[] confDates = null;
							String[] earnedCovers = null;
							String[] coverCharges = null;
							
							if(productSubscribedTo.contains(PlatformConstants.FM_PRODUCT))
							{
								if(productSubscribedTo.contains(PlatformConstants.XL_PRODUCT))
								{
									if(productSubscribedTo.contains(PlatformConstants.HP_PRODUCT))
									{
										//Product Subscribed - FM, XL, HP
										createdBys = cr.getCreatedByAll().split(",");  
										freeRegBy = createdBys[0];
										xlRegBy = createdBys[1];
										hpRegBy = createdBys[2];
										
										createdDates = cr.getCreatedDateAll().split(",");
										freeRegDate = createdDates[0];
										xlRegDate = createdDates[1];
										hpRegDate = createdDates[2];
										
										confDates = cr.getConfDateAll().split(",");
										freeConfDate = confDates[0];
										xlConfDate = confDates[1];
										hpConfDate = confDates[2];
										
										earnedCovers = cr.getEarnedCoverAll().split(",");
										freeEarnedCover = earnedCovers[0];
										xlEarnedCover = earnedCovers[1];
										hpEarnedCover = earnedCovers[2];
										
										coverCharges = cr.getCoverChargesAll().split(",");
										xlCoverCharges = coverCharges[1];
										hpCoverCharges = coverCharges[2];
										
									}
									else
									{
										//Product Subscribed - FM, XL
										createdBys = cr.getCreatedByAll().split(",");  
										freeRegBy = createdBys[0];
										xlRegBy = createdBys[1];
										hpRegBy = "";
										
										createdDates = cr.getCreatedDateAll().split(",");
										freeRegDate = createdDates[0];
										xlRegDate = createdDates[1];
										hpRegDate = "";
										
										confDates = cr.getConfDateAll().split(",");
										freeConfDate = confDates[0];
										xlConfDate = confDates[1];
										hpConfDate = "";
										
										earnedCovers = cr.getEarnedCoverAll().split(",");
										freeEarnedCover = earnedCovers[0];
										xlEarnedCover = earnedCovers[1];
										hpEarnedCover = "";
										
										coverCharges = cr.getCoverChargesAll().split(",");
										xlCoverCharges = coverCharges[1];
										hpCoverCharges = "";
									}
								}
								//If customer not subscribed to FreeModel Product. 
								else{
									if(productSubscribedTo.contains(PlatformConstants.HP_PRODUCT))
									{
										//Product Subscribed - FM, HP
										createdBys = cr.getCreatedByAll().split(",");  
										freeRegBy = createdBys[0];
										xlRegBy = "";
										hpRegBy = createdBys[1];
										
										createdDates = cr.getCreatedDateAll().split(",");
										freeRegDate = createdDates[0];
										xlRegDate = "";
										hpRegDate = createdDates[1];
										
										
										confDates = cr.getConfDateAll().split(",");
										freeConfDate = confDates[0];
										xlConfDate = "";
										hpConfDate = confDates[1];
										
										earnedCovers = cr.getEarnedCoverAll().split(",");
										freeEarnedCover = earnedCovers[0];
										xlEarnedCover = "";
										hpEarnedCover = earnedCovers[1];
										
										coverCharges = cr.getCoverChargesAll().split(",");
										xlCoverCharges = "";
										hpCoverCharges = coverCharges[1];	
									}
									else{
										//Product Subscribed - FM only.
										createdBys = cr.getCreatedByAll().split(",");  
										freeRegBy = createdBys[0];
										xlRegBy = "";
										hpRegBy = "";
										
										createdDates = cr.getCreatedDateAll().split(",");
										freeRegDate = createdDates[0];
										xlRegDate = "";
										hpRegDate = "";
										
										confDates = cr.getConfDateAll().split(",");
										freeConfDate = confDates[0];
										xlConfDate = "";
										hpConfDate = "";
										
										earnedCovers = cr.getEarnedCoverAll().split(",");
										freeEarnedCover = earnedCovers[0];
										xlEarnedCover = "";
										hpEarnedCover = "";
										
										coverCharges = cr.getCoverChargesAll().split(",");
										xlCoverCharges = "";
										hpCoverCharges = "";	
									}
								}
							}
							else
							{
								// Customer not subscribed to Free Model. 
								if(productSubscribedTo.contains(PlatformConstants.XL_PRODUCT)) 
								{
									if(productSubscribedTo.contains(PlatformConstants.HP_PRODUCT))
									{
										//Product Subscribed - XL, HP
										createdBys = cr.getCreatedByAll().split(",");  
										freeRegBy = "";
										xlRegBy = createdBys[0];
										hpRegBy = createdBys[1];
										
										createdDates = cr.getCreatedDateAll().split(",");
										freeRegDate = "";
										xlRegDate = createdDates[0];
										hpRegDate = createdDates[1];
										
										confDates = cr.getConfDateAll().split(",");
										freeConfDate = "";
										xlConfDate = confDates[0];
										hpConfDate = confDates[1];
										
										earnedCovers = cr.getEarnedCoverAll().split(",");
										freeEarnedCover = "";
										xlEarnedCover = earnedCovers[0];
										hpEarnedCover = earnedCovers[1];
										
										coverCharges = cr.getCoverChargesAll().split(",");
										xlCoverCharges =earnedCovers[0];
										hpCoverCharges = earnedCovers[1];	
									}
									else
									{
										//Product Subscribed - XL alone.
										createdBys = cr.getCreatedByAll().split(",");  
										freeRegBy = "";
										xlRegBy = createdBys[0];
										hpRegBy = "";
										
										createdDates = cr.getCreatedDateAll().split(",");
										freeRegDate = "";
										xlRegDate = createdDates[0];
										hpRegDate = "";
										
										confDates = cr.getConfDateAll().split(",");
										freeConfDate = "";
										xlConfDate = confDates[0];
										hpConfDate = "";
										
										earnedCovers = cr.getEarnedCoverAll().split(",");
										freeEarnedCover = "";
										xlEarnedCover = earnedCovers[0];
										hpEarnedCover = "";
										
										coverCharges = cr.getCoverChargesAll().split(",");
										xlCoverCharges = coverCharges[0];
										hpCoverCharges = "";	
									}
								}
								else
								{
									if(productSubscribedTo.contains(PlatformConstants.HP_PRODUCT))
									{
										//Product subscribed - HP alone.
										createdBys = cr.getCreatedByAll().split(",");  
										freeRegBy = "";
										xlRegBy = "";
										hpRegBy = createdBys[0];
										
										createdDates = cr.getCreatedDateAll().split(",");
										freeRegDate = "";
										xlRegDate = "";
										hpRegDate = createdDates[0];
										
										confDates = cr.getConfDateAll().split(",");
										freeConfDate = "";
										xlConfDate ="";
										hpConfDate = confDates[0];
										
										earnedCovers = cr.getEarnedCoverAll().split(",");
										freeEarnedCover = "";
										xlEarnedCover = "";
										hpEarnedCover = earnedCovers[0];
										
										coverCharges = cr.getCoverChargesAll().split(",");
										xlCoverCharges = "";
										hpCoverCharges = coverCharges[0];
									}
									else
									{
										//No Products Subscribed	
									}
								}
							}
							
							
							
							dataRow = new StringBuilder()
									.append(cr.getCustName())
									.append(ReportKeys.CSV_FIELD_SEPARATOR)

									.append(cr.getCustAge())
									.append(ReportKeys.CSV_FIELD_SEPARATOR)

									.append(cr.getIrdName())
									.append(ReportKeys.CSV_FIELD_SEPARATOR)

									.append(cr.getIrdAge())
									.append(ReportKeys.CSV_FIELD_SEPARATOR)

									.append(cr.getCustRelationship())
									.append(ReportKeys.CSV_FIELD_SEPARATOR);
							
									if(!"ROLE_INSURER".equals(userDetails.getRoleMaster().getRoleDescription())){
										
										dataRow.append(cr.getMsisdn())
										.append(ReportKeys.CSV_FIELD_SEPARATOR);
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

									.append(DateUtil.toDateString(cr.getModifiedDate(), ReportKeys.REPORT_DATE_TIME_FORMAT))
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

						// Sets the report file for download
						httpServletResponse
								.setContentType("application/vnd.ms-excel");
						httpServletResponse.setHeader("Content-Disposition",
								"attachment; filename=" + reportFileName);
						servletOutStream = httpServletResponse
								.getOutputStream();
						fileInputStream = new FileInputStream(custReportFile);
						int i;
						while ((i = fileInputStream.read()) != -1) {
							servletOutStream.write(i);
						}

						logger.info("Data written to csv file");
						servletOutStream.flush();
					}
				} catch (IOException e) {
					logger.error("Exception occured while downloading customer report");
					return null;
				} catch (MISPException e) {
					logger.error(
							"Exception occured while downloading customer report",
							e);
					return null;
				} catch (Exception e) {

					logger.error(
							"Exception occured while downloading customer report",
							e);
					return super.error(FaultMessages.GENERIC_ERROR);
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
						if (custReportFile != null && custReportFile.exists()) {
							custReportFile.delete();
						}
					} catch (IOException e) {
						logger.error("Exception occured while closing "
								+ "resources after customer report download");
						return null;
					}

				}

			}

			// Report Style - New (Options)
			else if (customerReportVO.getRepStyle().equalsIgnoreCase("2")) {
				
				int filterOption = Integer.valueOf(httpServletRequest
						.getParameter("filterOption"));
				if (filterOption == 0) {
					// Date Range
					fromDate = customerReportVO.getFromDate();
					toDate = customerReportVO.getToDate();

				} else if (filterOption == 1) {
					int quickSelectOption = Integer.valueOf(httpServletRequest
							.getParameter("quickSelectOption"));
					if (quickSelectOption == 0) {
						// Current Week
						fromDate = DateUtil.toDateString(DateUtil
								.getISOFirstDayOfWeek(new Date()));
						toDate = DateUtil.toDateString(DateUtil
								.getISOLastDayOfWeek(new Date()));
					} else if (quickSelectOption == 1) {
						// Previous Week
						fromDate = DateUtil.toDateString(DateUtil
								.getISOFirstDayOfPrevWeek(new Date()));
						toDate = DateUtil.toDateString(DateUtil
								.getISOLastDayOfWeek(DateUtil.toDate(fromDate)));
					} else if (quickSelectOption == 2) {
						// Current Month
						fromDate = DateUtil.toDateString(DateUtil
								.getFirstDayOfTheMonth(new Date()));
						toDate = DateUtil.toDateString(DateUtil
								.getLastDayOfTheMonth(new Date()));
					} else if (quickSelectOption == 3) {
						// Previous Month
						fromDate = DateUtil.toDateString(DateUtil
								.getFirstDayOfThePreviousMonth(new Date()));
						toDate = DateUtil.toDateString(DateUtil
								.getLastDayOfThePreviousMonth(new Date()));
					}
				}

				customerReportVO.setFromDate(fromDate);
				customerReportVO.setToDate(toDate);
				
				customerRanges = reportManagementService
						.getCustomerRanges(customerReportVO);
				if (customerRanges == null || customerRanges.isEmpty()) {
					logger.debug("There are no customers for the selected user criteria.");
					httpServletRequest.setAttribute("errorMessage", "true");
					mav = new ModelAndView(MAVPaths.VIEW_CUSTOMER_REPORT);
					return mav;
				} else {
					generateCustomerReportArchiveCSV(httpServletRequest,
							httpServletResponse, customerReportVO,
							customerRanges);
				}
			}
			// Report Style - Custom
			else if(customerReportVO.getRepStyle().equalsIgnoreCase("3")){
				
				String customSelectOption = httpServletRequest.
						getParameter("customSelectOption");
				
				//Renewed Policies Customer Report
				if(customSelectOption.equalsIgnoreCase("1")){
					customerRanges = reportManagementService
							.getCustomerRangesForRenewedPolicies(
									customerReportVO);
					if (customerRanges == null || customerRanges.isEmpty()) {
						logger.debug("There are no customers for the selected user criteria.");
						httpServletRequest.setAttribute("errorMessage", "true");
						mav = new ModelAndView(MAVPaths.VIEW_CUSTOMER_REPORT);
						return mav;
					} else {
						generateCustomerReportArchiveForRenewedPoliciesCSV(
								httpServletRequest, httpServletResponse, 
								customerReportVO, customerRanges);
					}	
				}
				//New Policies Customer Report
				else if(customSelectOption.equalsIgnoreCase("2")){
					
					customerRanges = reportManagementService
							.getCustomerRangesForNewPolicies(customerReportVO);
					if (customerRanges == null || customerRanges.isEmpty()) {
						logger.debug("There are no customers for the selected user criteria.");
						httpServletRequest.setAttribute("errorMessage", "true");
						mav = new ModelAndView(MAVPaths.VIEW_CUSTOMER_REPORT);
						return mav;
					} else {
						generateCustomerReportArchiveForNewPoliciesCSV(
								httpServletRequest, httpServletResponse, 
								customerReportVO, customerRanges);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception occured while downloading customer report",
					e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("downloadCustomerReportCSV");
		// Returns null as response has been set
		return null;
	}
	
	
	/**
	 * This method generates the customer report archive file for the current
	 * month
	 * 
	 * @return true if report is generated successfully else false
	 * 
	 * @throws MISPException
	 *             If any {@link Exception} occurs
	 */
	public ModelAndView generateCustomerReportArchive(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportCustomerVO customerReportVO, Map<String, String> rangeMap) {

		logger.entering("generateCustomerReportArchive", rangeMap);

		FileOutputStream fileOutStream = null;
		HSSFWorkbook workbook = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;
		boolean isZipFileCreated = false;
		try {

			Map<String, String> customerRanges = rangeMap;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());

			String currMonYear = String.valueOf(calendar.getTimeInMillis());
			String fileName = ReportKeys.CUST_REPORT_FILE_NAME + currMonYear;

			String reportDirPath = ReportKeys.REPORT_DIRECTORY_PATH;
			logger.debug(" The file Path is : " + reportDirPath);

			String archiveDirPath = reportDirPath 
					+ File.separator + ReportKeys.REPORT_FOLDER_PATH 					
					+ File.separator + currMonYear;

			File archiveDir = new File(archiveDirPath);

			// Creates archival directory
			if (!archiveDir.exists()) {
				archiveDir.mkdir();
			}

			String zipFileName = fileName + ReportKeys.REPORT_ZIP_FILE_TYPE;

			// Iterates over customer ranges and generates the file for each
			// range
			for (String key : customerRanges.keySet()) {

				logger.debug("Generating report for " + customerRanges.get(key)
						+ " records");
				String fullFileName = fileName + ReportKeys.UNDERSCORE
						+ key.split(ReportKeys.UNDERSCORE)[2]
						+ ReportKeys.REPORT_FILE_TYPE;
				logger.debug("Report File Name : " + fileName);

				try {
					File custReportFile = new File(archiveDirPath
							+ File.separator + fullFileName);
					if (custReportFile.exists()) {
						custReportFile.createNewFile();
					}

					workbook = new HSSFWorkbook();
					if(customerReportVO.getRepStyle().equalsIgnoreCase("2"))
						reportManagementService.writeCustomerReport(workbook,
							customerReportVO, key);

					fileOutStream = new FileOutputStream(custReportFile);
					workbook.write(fileOutStream);
					fileOutStream.flush();

					// Creates the report zip file
					isZipFileCreated = FileUtil.createZipFile(archiveDirPath,
							zipFileName);

				} catch (FileNotFoundException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} catch (IOException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} catch (MISPException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} finally {
					try {
						FileUtil.close(fileOutStream);
					} catch (MISPException e) {
						logger.debug("Exception occurred while closing the "
								+ "resources after generating customer report",
								e);
					}
				}
			}
			try {
				if (isZipFileCreated) {
					File zipReportFile = new File(archiveDirPath
							+ File.separator + zipFileName);

					// Sets the file for download
					httpServletResponse
							.setContentType("application/octet-stream");
					httpServletResponse.setHeader("Content-Disposition",
							"attachment; filename=" + zipFileName);

					servletOutStream = httpServletResponse.getOutputStream();
					fileInputStream = new FileInputStream(zipReportFile);

					int i;
					while ((i = fileInputStream.read()) != -1) {
						servletOutStream.write(i);
					}
					servletOutStream.flush();
				}
			} catch (IOException ioException) {
				logger.error("An exception occured while writing the zip file "
						+ "to outstream for user download.", ioException);
			} finally {
				FileUtil.close(servletOutStream);

				File destDirPath = new File(archiveDirPath);
				File[] dirFiles = destDirPath.listFiles();
				for (int i = 0; i < dirFiles.length; i++) {
					if (!dirFiles[i].getName().contains(
							ReportKeys.REPORT_ZIP_FILE_TYPE)) {
						dirFiles[i].getAbsoluteFile().delete();
					}
				}
			}
		} catch (MISPException e) {
			logger.debug("Exception occurred while generating customer report",
					e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("generateCustomerReportArchive");

		return null;

	}

	/**
	 * This method generates the customer report archive file for the current
	 * month
	 * 
	 * @return true if report is generated successfully else false
	 * 
	 * @throws MISPException
	 *             If any {@link Exception} occurs
	 */
	public ModelAndView generateCustomerReportArchiveCSV(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportCustomerVO customerReportVO, Map<String, String> rangeMap) {

		logger.entering("generateCustomerReportArchiveCSV", rangeMap);

		HttpSession session = httpServletRequest.getSession();
		UserDetails userDetails = (UserDetails) session.getAttribute(
				SessionKeys.SESSION_USER_DETAILS);
		
		File reportDirectory = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;
		boolean isZipFileCreated = false;
		
		String timeStampFormat = "ddMMyyyy";
		String fromTimeStamp = DateUtil.toDateString(
				DateUtil.toDate(customerReportVO.getFromDate()),
				timeStampFormat);
		String toTimeStamp = DateUtil
				.toDateString(DateUtil.toDate(customerReportVO.getToDate()),
						timeStampFormat);
		
		try {

			Map<String, String> customerRanges = rangeMap;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			String currMonYear = String.valueOf(calendar.getTimeInMillis());
			
			String fileName = ReportKeys.CUST_REPORT_FILE_NAME;
			
			String confirmationStatus = customerReportVO.getConfStat();
			
			if(confirmationStatus.equals("0")){
				fileName  = fileName + ReportKeys.CONF_STATUS_CONF_AND_UNCONF;
			}
			else if(confirmationStatus.equals("1")){
				fileName  = fileName + ReportKeys.CONF_STATUS_CONFIRMED;
			}
			else if(confirmationStatus.equals("2")){
				fileName  = fileName + ReportKeys.CONF_STATUS_UNCONFIRMED;
			}

			String registrationType = customerReportVO.getRegType();
			
			if(registrationType.equals("0")){
				fileName = fileName + ReportKeys.UNDERSCORE 
							+ ReportKeys.REG_TYPE_ALL;
			}
			else if(registrationType.equals("1")){
				fileName = fileName + ReportKeys.UNDERSCORE 
						+ ReportKeys.REG_TYPE_FREE;
			}
			else if(registrationType.equals("2")){
				fileName = fileName + ReportKeys.UNDERSCORE 
						+ ReportKeys.REG_TYPE_XL;
			}			
			else if(registrationType.equals("3")){
				fileName = fileName + ReportKeys.UNDERSCORE 
						+ ReportKeys.REG_TYPE_HP;
			}
			
			fileName = fileName + ReportKeys.UNDERSCORE 
						+ fromTimeStamp + ReportKeys.UNDERSCORE + 
						toTimeStamp;
			
			String reportDirPath = ReportKeys.REPORT_DIRECTORY_PATH;
			logger.debug(" The file Path is : " + reportDirPath);

			String archiveDirPath = reportDirPath 
					+ File.separator + ReportKeys.REPORT_FOLDER_PATH 					
					+ File.separator + currMonYear + " - " 
					+ userDetails.getUserUid();

			File archiveDir = new File(archiveDirPath);

			// Creates archival directory
			if (!archiveDir.exists()) {
				archiveDir.mkdir();
			}

			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}
			
			String zipFileName = fileName + ReportKeys.REPORT_ZIP_FILE_TYPE;

			// Iterates over customer ranges and generates the file for each
			// range

			for (String key : customerRanges.keySet()) {

				logger.debug("Generating report for : ", key, " - ", customerRanges.get(key)
						+ " records");
				String fullFileName = fileName + ReportKeys.UNDERSCORE
						+ key.split(ReportKeys.UNDERSCORE)[2]
						+ ReportKeys.REPORT_FILE_TYPE_CSV;
				logger.debug("Report File Name : " + fullFileName);

				try {
					File custReportFile = new File(archiveDirPath
							+ File.separator + fullFileName);
					if (custReportFile.exists()) {
						custReportFile.createNewFile();
					}
					
					if(customerReportVO.getRepStyle().equalsIgnoreCase("2"))
						reportManagementService.writeCustomerReportCSV(
								custReportFile,customerReportVO, key,userDetails.getRoleMaster().getRoleDescription());

					// Creates the report zip file
					isZipFileCreated = FileUtil.createZipFile(archiveDirPath,
							zipFileName);

				} catch (FileNotFoundException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} catch (IOException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} catch (MISPException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} finally {
					try {
						FileUtil.close(fileOutStream);
					} catch (MISPException e) {
						logger.debug("Exception occurred while closing the "
								+ "resources after generating customer report",
								e);
					}
				}
			}
			try {
				if (isZipFileCreated) {
					File zipReportFile = new File(archiveDirPath
							+ File.separator + zipFileName);

					// Sets the file for download
					httpServletResponse
							.setContentType("application/octet-stream");
					httpServletResponse.setHeader("Content-Disposition",
							"attachment; filename=" + zipFileName);

					servletOutStream = httpServletResponse.getOutputStream();
					fileInputStream = new FileInputStream(zipReportFile);

					int i;
					while ((i = fileInputStream.read()) != -1) {
						servletOutStream.write(i);
					}
					servletOutStream.flush();
				}
			} catch (IOException ioException) {
				logger.error("An exception occured while writing the zip file "
						+ "to outstream for user download.", ioException);
			} finally {
				FileUtil.close(servletOutStream);

				File destDirPath = new File(archiveDirPath);
				File[] dirFiles = destDirPath.listFiles();
				for (int i = 0; i < dirFiles.length; i++) {
					if (!dirFiles[i].getName().contains(
							ReportKeys.REPORT_ZIP_FILE_TYPE)) {
						dirFiles[i].getAbsoluteFile().delete();
					}
				}
			}
		} catch (MISPException e) {
			logger.debug("Exception occurred while generating customer report",
					e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("generateCustomerReportArchiveCSV");

		return null;
	}
	
	/**
	 * This method generates the customer report archive file for the current
	 * month
	 * 
	 * @return true if report is generated successfully else false
	 * 
	 * @throws MISPException
	 *             If any {@link Exception} occurs
	 */
	public ModelAndView generateCustomerReportArchiveForRenewedPoliciesCSV(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportCustomerVO customerReportVO, Map<String, String> rangeMap) {

		logger.entering("generateCustomerReportArchiveForRenewedPoliciesCSV", rangeMap);

		HttpSession session = httpServletRequest.getSession();
		UserDetails userDetails = (UserDetails) session.getAttribute(
				SessionKeys.SESSION_USER_DETAILS);
		
		File reportDirectory = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;
		boolean isZipFileCreated = false;
		try {

			Map<String, String> customerRanges = rangeMap;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			
			String currMonYear = String.valueOf(calendar.getTimeInMillis());
			
			String timeStampFormat = "ddMMyyyy";
			String fromTimeStamp = DateUtil.toDateString(
					DateUtil.getFirstDayOfThePreviousMonth(new Date()),
					timeStampFormat);
			
			String toTimeStamp = DateUtil.toDateString(
					DateUtil.getLastDayOfThePreviousMonth(new Date()),
							timeStampFormat);
			
			String fileName = ReportKeys.CUST_REPORT_FILE_NAME;
			fileName = fileName + fromTimeStamp + ReportKeys.UNDERSCORE + 
					toTimeStamp;
			
			String reportDirPath = ReportKeys.REPORT_DIRECTORY_PATH;
			logger.debug(" The file Path is : " + reportDirPath);

			String archiveDirPath = reportDirPath 
					+ File.separator + ReportKeys.REPORT_FOLDER_PATH 					
					+ File.separator + currMonYear + " - " 
					+ userDetails.getUserUid();

			File archiveDir = new File(archiveDirPath);

			// Creates archival directory
			if (!archiveDir.exists()) {
				archiveDir.mkdir();
			}

			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}
			
			String zipFileName = fileName + ReportKeys.REPORT_ZIP_FILE_TYPE;

			// Iterates over customer ranges and generates the file for each
			// range

			for (String key : customerRanges.keySet()) {

				logger.debug("Generating report for : ", key, " - ", customerRanges.get(key)
						+ " records");
				String fullFileName = fileName + ReportKeys.UNDERSCORE
						+ key.split(ReportKeys.UNDERSCORE)[2]
						+ ReportKeys.REPORT_FILE_TYPE_CSV;
				logger.debug("Report File Name : " + fullFileName);

				try {
					File custReportFile = new File(archiveDirPath
							+ File.separator + fullFileName);
					if (custReportFile.exists()) {
						custReportFile.createNewFile();
					}
					
					if(customerReportVO.getRepStyle().equalsIgnoreCase("3"))
						reportManagementService.
							writeCustomerReportForRenewedPoliciesCSV(
								custReportFile,customerReportVO, key, userDetails.getRoleMaster().getRoleDescription());

					// Creates the report zip file
					isZipFileCreated = FileUtil.createZipFile(archiveDirPath,
							zipFileName);

				} catch (FileNotFoundException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} catch (IOException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} catch (MISPException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} finally {
					try {
						FileUtil.close(fileOutStream);
					} catch (MISPException e) {
						logger.debug("Exception occurred while closing the "
								+ "resources after generating customer report",
								e);
					}
				}
			}
			try {
				if (isZipFileCreated) {
					File zipReportFile = new File(archiveDirPath
							+ File.separator + zipFileName);

					// Sets the file for download
					httpServletResponse
							.setContentType("application/octet-stream");
					httpServletResponse.setHeader("Content-Disposition",
							"attachment; filename=" + zipFileName);

					servletOutStream = httpServletResponse.getOutputStream();
					fileInputStream = new FileInputStream(zipReportFile);

					int i;
					while ((i = fileInputStream.read()) != -1) {
						servletOutStream.write(i);
					}
					servletOutStream.flush();
				}
			} catch (IOException ioException) {
				logger.error("An exception occured while writing the zip file "
						+ "to outstream for user download.", ioException);
			} finally {
				FileUtil.close(servletOutStream);

				File destDirPath = new File(archiveDirPath);
				File[] dirFiles = destDirPath.listFiles();
				for (int i = 0; i < dirFiles.length; i++) {
					if (!dirFiles[i].getName().contains(
							ReportKeys.REPORT_ZIP_FILE_TYPE)) {
						dirFiles[i].getAbsoluteFile().delete();
					}
				}
			}
		} catch (MISPException e) {
			logger.debug("Exception occurred while generating customer report",
					e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("generateCustomerReportArchiveForRenewedPoliciesCSV");

		return null;
	}
	
	/**
	 * This method generates the customer report archive file for the current
	 * month
	 * 
	 * @return true if report is generated successfully else false
	 * 
	 * @throws MISPException
	 *             If any {@link Exception} occurs
	 */
	public ModelAndView generateCustomerReportArchiveForNewPoliciesCSV(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportCustomerVO customerReportVO, Map<String, String> rangeMap) {

		logger.entering("generateCustomerReportArchiveForNewPoliciesCSV", rangeMap);

		HttpSession session = httpServletRequest.getSession();
		UserDetails userDetails = (UserDetails) session.getAttribute(
				SessionKeys.SESSION_USER_DETAILS);
		
		File reportDirectory = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;
		boolean isZipFileCreated = false;
		try {

			Map<String, String> customerRanges = rangeMap;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());

			String currMonYear = String.valueOf(calendar.getTimeInMillis());
			
			String timeStampFormat = "ddMMyyyy";
			String fromTimeStamp = DateUtil.toDateString(
					DateUtil.getFirstDayOfThePreviousMonth(new Date()),
					timeStampFormat);
			
			String toTimeStamp = DateUtil.toDateString(
					DateUtil.getLastDayOfThePreviousMonth(new Date()),
							timeStampFormat);
			
			String fileName = ReportKeys.CUST_REPORT_FILE_NAME;
			fileName = fileName + fromTimeStamp + ReportKeys.UNDERSCORE + 
					toTimeStamp;
			
			String reportDirPath = ReportKeys.REPORT_DIRECTORY_PATH;
			logger.debug(" The file Path is : " + reportDirPath);

			String archiveDirPath = reportDirPath 
					+ File.separator + ReportKeys.REPORT_FOLDER_PATH 					
					+ File.separator + currMonYear + " - " 
					+ userDetails.getUserUid();

			File archiveDir = new File(archiveDirPath);

			// Creates archival directory
			if (!archiveDir.exists()) {
				archiveDir.mkdir();
			}

			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}
			
			String zipFileName = fileName + ReportKeys.REPORT_ZIP_FILE_TYPE;

			// Iterates over customer ranges and generates the file for each
			// range

			for (String key : customerRanges.keySet()) {

				logger.debug("Generating report for : ", key, " - ", customerRanges.get(key)
						+ " records");
				String fullFileName = fileName + ReportKeys.UNDERSCORE
						+ key.split(ReportKeys.UNDERSCORE)[2]
						+ ReportKeys.REPORT_FILE_TYPE_CSV;
				logger.debug("Report File Name : " + fullFileName);

				try {
					File custReportFile = new File(archiveDirPath
							+ File.separator + fullFileName);
					if (custReportFile.exists()) {
						custReportFile.createNewFile();
					}
					
					if(customerReportVO.getRepStyle().equalsIgnoreCase("3"))
						reportManagementService.
							writeCustomerReportForNewPoliciesCSV(
								custReportFile,customerReportVO, key, userDetails.getRoleMaster().getRoleDescription());

					// Creates the report zip file
					isZipFileCreated = FileUtil.createZipFile(archiveDirPath,
							zipFileName);

				} catch (FileNotFoundException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} catch (IOException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} catch (MISPException e) {
					logger.debug(
							"Exception occurred while generating customer report",
							e);
				} finally {
					try {
						FileUtil.close(fileOutStream);
					} catch (MISPException e) {
						logger.debug("Exception occurred while closing the "
								+ "resources after generating customer report",
								e);
					}
				}
			}
			try {
				if (isZipFileCreated) {
					File zipReportFile = new File(archiveDirPath
							+ File.separator + zipFileName);

					// Sets the file for download
					httpServletResponse
							.setContentType("application/octet-stream");
					httpServletResponse.setHeader("Content-Disposition",
							"attachment; filename=" + zipFileName);

					servletOutStream = httpServletResponse.getOutputStream();
					fileInputStream = new FileInputStream(zipReportFile);

					int i;
					while ((i = fileInputStream.read()) != -1) {
						servletOutStream.write(i);
					}
					servletOutStream.flush();
				}
			} catch (IOException ioException) {
				logger.error("An exception occured while writing the zip file "
						+ "to outstream for user download.", ioException);
			} finally {
				FileUtil.close(servletOutStream);

				File destDirPath = new File(archiveDirPath);
				File[] dirFiles = destDirPath.listFiles();
				for (int i = 0; i < dirFiles.length; i++) {
					if (!dirFiles[i].getName().contains(
							ReportKeys.REPORT_ZIP_FILE_TYPE)) {
						dirFiles[i].getAbsoluteFile().delete();
					}
				}
			}
		} catch (MISPException e) {
			logger.debug("Exception occurred while generating customer report",
					e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("generateCustomerReportArchiveForNewPoliciesCSV");

		return null;
	}
	
	/**
	 * This method handles the request and response for generating financial
	 * report based on user input.
	 * 
	 * @param httpServletRequest
	 *            an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *            an instance of HTTPServletRequest
	 * 
	 * @param financialReportVO
	 *            <code>ReportFinancialVO</code> object containing user input
	 * 
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView generateFinancialReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportFinancialVO financialReportVO) {

		logger.entering("generateFinancialReport");
		ReportFinancialVO financialReport = null;
		String reportDate = null;
		try {

			// Gets the financial report
			financialReport = reportManagementService
					.generateFinancialReport(financialReportVO);

			// Checks and sets the message if there are no subscribed customers
			// for the current month
			if (financialReport == null) {
				return super
						.success(SuccessMessages.REPORTS_NO_CUSTOMERS_SUBSCRIBED);
			} else {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				reportDate = ReportKeys.MONTH_NAMES[calendar
						.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR);
			}
		} catch (MISPException e) {

			logger.error("Exception occured while generating financial report");
			return super.error(FaultMessages.GENERIC_ERROR);
		} catch (Exception e) {

			logger.error(
					"Exception occured while  generating financial report", e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("generateFinancialReport", financialReport);
		
		ModelAndView mavObj=new ModelAndView(MAVPaths.VIEW_FINANCIAL_REPORT_PAGE);
		mavObj.addObject(MAVObjects.VO_FINANCIAL_REPORT, financialReport);
		mavObj.addObject(MAVObjects.REPORT_CURRENT_MONTH,
				reportDate);

		return mavObj;
		
				
	}

	/**
	 * This method handles the request and response for downloading financial
	 * report
	 * 
	 * @param httpServletRequest
	 *            an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *            an instance of HTTPServletRequest
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView downloadFinancialReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		logger.entering("downloadFinancialReport");

		File reportDirectory = null;
		File custReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		try {

			ReportFinancialVO financialRepVO = new ReportFinancialVO();

			financialRepVO.setTotalActiveCustomers(httpServletRequest
					.getParameter("totalCust"));
			financialRepVO.setFreeSumAssured(httpServletRequest
					.getParameter("freeSum"));
			financialRepVO.setPaidSumAssured(httpServletRequest
					.getParameter("paidSum"));
			financialRepVO.setTotalSumAssured(httpServletRequest
					.getParameter("totalSum"));

			// Creates the file name
			DateFormat format = new SimpleDateFormat("ddMMyyyyHHmmss");
			String currTimeStamp = format.format(new Date());

			String reportFileName = ReportKeys.FINANCIAL_REP_FILE_NAME
					+ currTimeStamp + ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// creates the report file
			custReportFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!custReportFile.exists()) {
				custReportFile.createNewFile();

			}

			// Writes report data to excel file
			workbook = new HSSFWorkbook();
			reportManagementService.writeFinancialReport(workbook,
					financialRepVO);

			fileOutStream = new FileOutputStream(custReportFile);
			workbook.write(fileOutStream);
			fileOutStream.flush();

			// Sets the file for download
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=" + reportFileName);
			servletOutStream = httpServletResponse.getOutputStream();
			fileInputStream = new FileInputStream(custReportFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}

			servletOutStream.flush();

		} catch (IOException e) {
			logger.error(
					"Exception occured while downloading financial report", e);
			return null;
		} catch (MISPException e) {
			logger.error(
					"Exception occured while downloading financial report", e);
			return null;
		} catch (Exception e) {

			logger.error(
					"Exception occured while downloading financial report", e);
			return null;
		} finally {
			// closes all resources
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}

				if (servletOutStream != null) {
					servletOutStream.close();
				}

				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (custReportFile != null && custReportFile.exists()) {
					custReportFile.delete();
				}
			} catch (Exception e) {
				logger.error("Exception occured while " +
								"closing resources after financial report download");
				return null;
			}
		}

		logger.exiting("downloadFinancialReport");
		// Returns null as response has been set
		return null;
	}
	
	/**
	 * This method handles the request and response for downloading agent
	 * report
	 * 
	 * @param httpServletRequest
	 *          an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *			an instance of HTTPServletRequest
	 *
	 * @param agentReportVO
	 *			<code>ReportAgentVO</code> object containing user input
	 * 
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView downloadAgentReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, ReportAgentVO agentReportVO) {
		Object[] params = { agentReportVO };
		logger.entering("downloadAgentReport", params);

		ModelAndView mav = null;

		String toDate = null;
		String fromDate = null;
		List<UserDetails> userDetailsList = null;
		Map<Integer, List<Map<String, List<ReportDailyNewVO>>>> allWeeksReport = null;
		Map<String, ReportDailyNewVO> deductionReport = null;
		Map<String, ReportDailyNewVO> durationReport = null;
		Map<Integer, List<Map<String, String>>> allWeeksLeave = null;
		Map<Integer, List<DeregisteredCustomersVO>> deregFMCustomersDetails = null;
		Map<Integer, List<DeregisteredCustomersVO>> deregXLCustomersDetails = null;
		Map<Integer, List<DeregisteredCustomersVO>> deregHPCustomersDetails = null;
		Map<Integer, List<DeregisteredCustomersVO>> deregIPCustomersDetails = null;
		Map<Integer, List<ReportDailyNewVO>> confirmedXLandHPDetails = null;
		Map<Integer, List<ReportDailyNewVO>> confirmedXLandIPDetails = null;
		Map<Integer, ReportDailyNewVO> registeredXLandHPDetailsMap = null;
		Map<Integer, ReportDailyNewVO> registeredXLandIPDetailsMap = null;
		Map<Integer, ReportDailyNewVO> confirmedXLandHPDetailsMap = null;
		Map<Integer, ReportDailyNewVO> confirmedXLandIPDetailsMap = null;
		
		//Added by harsh
		List<UserDetails> branchwiseUserDetails = null;
		List<Integer> distinctBranchIdList = null;
		Map<Integer,Map<String, List<ReportDailyNewVO>>> agentDaywiseReportMap = null;
		List<String> dateRange = new ArrayList<String>();	
		int filterOption=-1;
		
		try {

			int reportType = Integer.valueOf(httpServletRequest.getParameter("reportType"));
			
			// The report type to download
			int filterOptionType = -1;
			// Gets the details of all users
			userDetailsList = reportManagementService
					.getUserDetailList(agentReportVO,noOfRoles);
			
			if ( reportType == 3 ) { 
				
				int quickSelectOptionForDeducted = Integer.valueOf(httpServletRequest
						.getParameter("quickSelectOptionForDeducted"));
				
				dateRange = DateUtil.getDateRangeForDeductedReport(quickSelectOptionForDeducted);
				fromDate = dateRange.get(0);
				toDate = dateRange.get(1);
				filterOptionType = 0;
					
			} else{
				
				filterOption = Integer.valueOf(httpServletRequest.getParameter("filterOption"));
			}
						
			
			logger.info("fromDate: "+fromDate);
			logger.info("toDate: "+toDate);
			
			if (filterOption == 0) {
				// Date Range
				fromDate = agentReportVO.getFromDate();
				toDate = agentReportVO.getToDate();
				filterOptionType = 0;
				
			}  else if (filterOption == 1 && reportType !=2) {
				
				int quickSelectOption = Integer.valueOf(httpServletRequest
						.getParameter("quickSelectOption"));
				
				if (quickSelectOption == 0) {
					// Current Week
					fromDate = DateUtil.toDateString(DateUtil
							.getISOFirstDayOfWeek(new Date()));
					toDate = DateUtil.toDateString(DateUtil
							.getISOLastDayOfWeek(new Date()));
					filterOptionType = 1;
				} else if (quickSelectOption == 1) {
					// Previous Week
					fromDate = DateUtil.toDateString(DateUtil
							.getISOFirstDayOfPrevWeek(new Date()));
					toDate = DateUtil.toDateString(DateUtil
							.getISOLastDayOfWeek(DateUtil.toDate(fromDate)));
					filterOptionType = 2;
				} else if (quickSelectOption == 2) {
					// Current Month
					fromDate = DateUtil.toDateString(DateUtil
							.getFirstDayOfTheMonth(new Date()));
					toDate = DateUtil.toDateString(DateUtil
							.getLastDayOfTheMonth(new Date()));
					filterOptionType = 3;
				} else if (quickSelectOption == 3) {
					// Previous Month
					fromDate = DateUtil.toDateString(DateUtil
							.getFirstDayOfThePreviousMonth(new Date()));
					toDate = DateUtil.toDateString(DateUtil
							.getLastDayOfThePreviousMonth(new Date()));
					filterOptionType = 4;
				}

			} else if (filterOption == 1 && reportType ==2) {
				
				int quickSelectOption = Integer.valueOf(httpServletRequest
						.getParameter("quickSelectOption"));
				
				if (quickSelectOption == 0) {
					// Current Week
					dateRange = DateUtil.getCurrentWeekRangeForCSCWeekly();
					fromDate = dateRange.get(0);
					toDate = dateRange.get(1);
					filterOptionType = 1;
				} else if (quickSelectOption == 1) {
					// Previous Week
					dateRange = DateUtil.getPreviousWeekRangeForCSCWeekly();
					fromDate = dateRange.get(0);
					toDate = dateRange.get(1);
					filterOptionType = 2;
				} else if (quickSelectOption == 2) {
					// Current Month
					dateRange = DateUtil.getCurrentMonthRangeForCSCWeekly();
					fromDate = dateRange.get(0);
					toDate = dateRange.get(1);
					filterOptionType = 3;
				} else if (quickSelectOption == 3) {
					// Previous Month
					dateRange = DateUtil.getPreviousMonthRangeForCSCWeekly();
					fromDate = dateRange.get(0);
					toDate = dateRange.get(1);
					filterOptionType = 4;
				}
	
				
			}

			agentReportVO.setFromDate(fromDate);
			agentReportVO.setToDate(toDate);
			
			//Performance Report
			if (reportType == 0) {
				// Getting report data
				allWeeksReport = reportManagementService
						.getAllWeeksReport(agentReportVO);
				agentReportVO.setUserDetailsList(userDetailsList);

				// deregFMCustomersDetails =
				// reportManagementService.getDeregFMCustomersDetails(agentReportVO);
				deregXLCustomersDetails = reportManagementService
						.getDeregXLCustomersDetails(agentReportVO);
				deregHPCustomersDetails = reportManagementService
						.getDeregHPCustomersDetails(agentReportVO);
				deregIPCustomersDetails = reportManagementService
						.getDeregIPCustomersDetails(agentReportVO);
				confirmedXLandHPDetails = reportManagementService
						.getConfirmedXLandHP(agentReportVO);
				confirmedXLandIPDetails = reportManagementService
						.getConfirmedXLandIP(agentReportVO);
				// Getting leave data
				allWeeksLeave = userService.getAllWeeksLeave(agentReportVO);

				agentReportVO.setAllWeeksReport(allWeeksReport);
				agentReportVO.setAgentLeaves(allWeeksLeave);
				agentReportVO
						.setDeregFMCustomersDetails(deregFMCustomersDetails);
				agentReportVO
						.setDeregXLCustomersDetails(deregXLCustomersDetails);
				agentReportVO
						.setDeregHPCustomersDetails(deregHPCustomersDetails);
				agentReportVO
						.setConfirmedXLandHPDetails(confirmedXLandHPDetails);
				agentReportVO
						.setConfirmedXLandIPDetails(confirmedXLandIPDetails);
				agentReportVO
						.setDeregIPCustomersDetails(deregIPCustomersDetails);

				mav = createAgentReport(httpServletRequest,
						httpServletResponse, agentReportVO, filterOptionType);

			}else if(reportType == 1){	//Salary Report
				// Getting report data
				durationReport = reportManagementService
						.getDurationReport(agentReportVO);
				
				agentReportVO.setUserDetailsList(userDetailsList);

				registeredXLandHPDetailsMap = reportManagementService.
						getRegisteredXLandHPWithUserDetails(agentReportVO);
				registeredXLandIPDetailsMap = reportManagementService.
						getRegisteredXLandIPWithUserDetails(agentReportVO);
				confirmedXLandHPDetailsMap = reportManagementService
						.getConfirmedXLandHPWithUserDetails(agentReportVO);
				confirmedXLandIPDetailsMap = reportManagementService
						.getConfirmedXLandIPWithUserDetails(agentReportVO);
				// Getting leave data
//				allWeeksLeave = userService.getAllWeeksLeave(agentReportVO);

				agentReportVO.
					setDurationReport(durationReport);
				//agentReportVO.setAgentLeaves(allWeeksLeave);
				agentReportVO.
					setRegisteredXLandHPDetailsMap(
						registeredXLandHPDetailsMap);
				agentReportVO.
					setConfirmedXLandHPDetailsMap(
						confirmedXLandHPDetailsMap);
				agentReportVO.
					setConfirmedXLandIPDetailsMap(confirmedXLandIPDetailsMap);
				agentReportVO.
					setRegisteredXLandIPDetailsMap(registeredXLandIPDetailsMap);
			
				mav = createAgentSalaryReport(httpServletRequest,
						httpServletResponse, agentReportVO, filterOptionType);

			}else if(reportType == 2){ //CSC Weekly Report
				
				/*
				 * First get the list of Users based on the role categorized by Branch.
				 * Get all the data of agent based on the role selected by the user based on the date range.
				 * Build the report by mapping the agentID from both the data retrieved.
				 * 
				 */
				dateRange = null;
				dateRange = DateUtil.getBeginEndDateRange(agentReportVO.getFromDate(),agentReportVO.getToDate());
				agentReportVO.setDateRange(dateRange);
				
				distinctBranchIdList = reportManagementService.getDistinctBranchId(agentReportVO.getRole());
				agentReportVO.setDistinctBranchIdList(distinctBranchIdList);
				
				branchwiseUserDetails = reportManagementService.getBrnachwiseUserDetails(agentReportVO);
				agentReportVO.setUserDetailsList(branchwiseUserDetails);
				
				// Getting report data
				agentDaywiseReportMap = reportManagementService.getDaywiseReportData(agentReportVO);
				agentReportVO.setAgentDaywiseReportMap(agentDaywiseReportMap);
				
				mav = createCSCWeeklyReport(httpServletRequest,
						httpServletResponse, agentReportVO, filterOptionType);
				
			}else if(reportType==3){ // deducted clients reports
				
				deductionReport = reportManagementService
						.getDeductionReport(agentReportVO);
				
				agentReportVO.setUserDetailsList(userDetailsList);

				agentReportVO.setDurationReport(deductionReport);
				
				mav = createAgentDeductionReport(httpServletRequest,
						httpServletResponse, agentReportVO, filterOptionType);
				
			}
			
			
			

		} catch (MISPException e) {

			logger.error("Exception occured while downloading agent report",e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}catch (Exception e){
			logger.error("Exception occured while downloading agent report",e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("downloadAgentReport");
		return mav;
	}
	
	/**
	 * This method handles the creation of agent report based on report type.
	 * 
	 * @param httpServletRequest
	 *          an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *			an instance of HTTPServletRequest
	 *
	 * @param agentReportVO
	 *			<code>ReportAgentVO</code> object containing user input
	 *
	 * @param reportType
	 * 			type of report
	 * 			<table border=1>
	 * 				<tr><td>1</td><td>Date Range</td></tr>
	 * 				<tr><td>2</td><td>Current Week</td></tr>
	 * 				<tr><td>3</td><td>Previous Week</td></tr>
	 * 				<tr><td>4</td><td>Current Month</td></tr>
	 * 			</table>
	 * 
	 */
	private ModelAndView createAgentReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportAgentVO agentReportVO, int reportType) {
		Object[] params = { agentReportVO, reportType };
		logger.entering("createAgentReport");

		File reportDirectory = null;
		File agentReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		try {

			// Creates Report File Name
			String timeStampFormat = "ddMMyyyy";
			String fromTimeStamp = DateUtil.toDateString(
					DateUtil.toDate(agentReportVO.getFromDate()),
					timeStampFormat);
			String toTimeStamp = DateUtil
					.toDateString(DateUtil.toDate(agentReportVO.getToDate()),
							timeStampFormat);
			String reportFileName = ReportKeys.AGENT_REP_FILE_NAME + "_"
					+ fromTimeStamp + "_" + toTimeStamp
					+ ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			agentReportFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!agentReportFile.exists()) {
				agentReportFile.createNewFile();
			}

			workbook = new HSSFWorkbook();
			reportManagementService.writeAgentReport(workbook, agentReportVO,
					reportType);

			fileOutStream = new FileOutputStream(agentReportFile);
			workbook.write(fileOutStream);
			fileOutStream.flush();

			logger.debug("Data written to excel workbook");

			// Sets the file for download
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=" + reportFileName);
			servletOutStream = httpServletResponse.getOutputStream();
			fileInputStream = new FileInputStream(agentReportFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}
			servletOutStream.flush();

		} catch (IOException e) {
			logger.error("Exception occured while downloading agent report", e);
			return null;
		} catch (MISPException e) {
			logger.error("Exception occured while downloading agent report", e);
			return null;
		} catch (Exception e) {

			logger.error("Exception occured while downloading agent report", e);
			return null;
		} finally {
			// Closes all resources
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}

				if (servletOutStream != null) {
					servletOutStream.close();
				}

				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (agentReportFile != null && agentReportFile.exists()) {
					agentReportFile.delete();
				}
			} catch (Exception e) {
				logger.error("Exception occured while "
						+ "closing resources after agent report download");
				return null;
			}
		}

		logger.exiting("createAgentReport");
		return null;
	}

	/**
	 * This method handles the creation of agent report based on report type.
	 * 
	 * @param httpServletRequest
	 *          an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *			an instance of HTTPServletRequest
	 *
	 * @param agentReportVO
	 *			<code>ReportAgentVO</code> object containing user input
	 *
	 * @param filterOptionType
	 * 			type of report
	 * 			<table border=1>
	 * 				<tr><td>1</td><td>Date Range</td></tr>
	 * 				<tr><td>2</td><td>Current Week</td></tr>
	 * 				<tr><td>3</td><td>Previous Week</td></tr>
	 * 				<tr><td>4</td><td>Current Month</td></tr>
	 * 			</table>
	 * 
	 */
	private ModelAndView createAgentSalaryReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportAgentVO agentReportVO, int filterOptionType) {
		
		logger.entering("createAgentSalaryReport");

		File reportDirectory = null;
		File agentReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		try {

			// Creates Report File Name
			String timeStampFormat = "ddMMyyyy";
			String fromTimeStamp = DateUtil.toDateString(
					DateUtil.toDate(agentReportVO.getFromDate()),
					timeStampFormat);
			String toTimeStamp = DateUtil
					.toDateString(DateUtil.toDate(agentReportVO.getToDate()),
							timeStampFormat);
			String reportFileName = ReportKeys.AGENT_SAL_REP_FILE_NAME + "_"
					+ fromTimeStamp + "_" + toTimeStamp
					+ ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			agentReportFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!agentReportFile.exists()) {
				agentReportFile.createNewFile();
			}

			workbook = new HSSFWorkbook();
			reportManagementService.writeAgentSalaryReport(workbook, agentReportVO,
					filterOptionType);

			fileOutStream = new FileOutputStream(agentReportFile);
			workbook.write(fileOutStream);
			fileOutStream.flush();

			logger.debug("Data written to excel workbook");

			// Sets the file for download
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=" + reportFileName);
			servletOutStream = httpServletResponse.getOutputStream();
			fileInputStream = new FileInputStream(agentReportFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}
			servletOutStream.flush();

		} catch (IOException e) {
			logger.error("Exception occured while downloading agent report", e);
			return null;
		} catch (MISPException e) {
			logger.error("Exception occured while downloading agent report", e);
			return null;
		} catch (Exception e) {

			logger.error("Exception occured while downloading agent report", e);
			return null;
		} finally {
			// Closes all resources
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}

				if (servletOutStream != null) {
					servletOutStream.close();
				}

				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (agentReportFile != null && agentReportFile.exists()) {
					agentReportFile.delete();
				}
			} catch (Exception e) {
				logger.error("Exception occured while "
						+ "closing resources after agent report download");
				return null;
			}
		}

		logger.exiting("createAgentSalaryReport");
		return null;
	}
	
	/**
	 * This method handles the creation of agent report based on report type.
	 * 
	 * @param httpServletRequest
	 *          an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *			an instance of HTTPServletRequest
	 *
	 * @param agentReportVO
	 *			<code>ReportAgentVO</code> object containing user input
	 *
	 * @param filterOptionType
	 * 			type of report
	 * 			<table border=1>
	 * 				<tr><td>1</td><td>Date Range</td></tr>
	 * 				<tr><td>2</td><td>Current Week</td></tr>
	 * 				<tr><td>3</td><td>Previous Week</td></tr>
	 * 				<tr><td>4</td><td>Current Month</td></tr>
	 * 			</table>
	 * 
	 */
	private ModelAndView createAgentDeductionReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportAgentVO agentReportVO, int filterOptionType) {
		
		logger.entering("createAgentDeductionReport");

		File reportDirectory = null;
		File agentReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		try {

			// Creates Report File Name
			String timeStampFormat = "ddMMyyyy";
			String fromTimeStamp = DateUtil.toDateString(
					DateUtil.toDate(agentReportVO.getFromDate()),
					timeStampFormat);
			String toTimeStamp = DateUtil
					.toDateString(DateUtil.toDate(agentReportVO.getToDate()),
							timeStampFormat);
			String reportFileName = ReportKeys.AGENT_DED_REP_FILE_NAME + "_"
					+ fromTimeStamp + "_" + toTimeStamp
					+ ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			agentReportFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!agentReportFile.exists()) {
				agentReportFile.createNewFile();
			}

			workbook = new HSSFWorkbook();
			reportManagementService.writeAgentDeductedReport(workbook, agentReportVO,
					filterOptionType);

			fileOutStream = new FileOutputStream(agentReportFile);
			workbook.write(fileOutStream);
			fileOutStream.flush();

			logger.debug("Data written to excel workbook");

			// Sets the file for download
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=" + reportFileName);
			servletOutStream = httpServletResponse.getOutputStream();
			fileInputStream = new FileInputStream(agentReportFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}
			servletOutStream.flush();

		} catch (IOException e) {
			logger.error("Exception occured while downloading agent report", e);
			return null;
		} catch (MISPException e) {
			logger.error("Exception occured while downloading agent report", e);
			return null;
		} catch (Exception e) {

			logger.error("Exception occured while downloading agent report", e);
			return null;
		} finally {
			// Closes all resources
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}

				if (servletOutStream != null) {
					servletOutStream.close();
				}

				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (agentReportFile != null && agentReportFile.exists()) {
					agentReportFile.delete();
				}
			} catch (Exception e) {
				logger.error("Exception occured while "
						+ "closing resources after agent report download");
				return null;
			}
		}

		logger.exiting("createAgentDeductionReport");
		return null;
	}
	
	/**
	 * This method launches the Weekly Report Download page.
	 * 
	 * @param request
	 *            - An instance of HttpServletRequest Object
	 * 
	 * @param response
	 *            - An instance of HttpServletResponse Object
	 * 
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView loadWeeklyReport(HttpServletRequest request,
			HttpServletResponse response) {
		logger.entering("loadWeeklyReport");

		List<ProductDetails> productsList = null;
		ModelAndView mavObj = null;
		StringBuilder products = new StringBuilder();
		try {
			productsList = productsMgmtService.retrieveOfferNamesAndIds();

			for (ProductDetails productDetails : productsList) {
				products.append(productDetails.getProductName());
				products.append(",");
			}

			if (products.lastIndexOf(",") != -1)
				products.deleteCharAt(products.lastIndexOf(","));

			mavObj = new ModelAndView(MAVPaths.VIEW_WEEKLY_REPORT_PAGE);

			mavObj.addObject(MAVObjects.LIST_PRODUCTS, products.toString());
		} catch (MISPException mispException) {
			logger.error(FaultMessages.WEEKLY_REPORT_PAGE_LOADING_FAILED,
					mispException);

			mavObj = super.error(
					FaultMessages.WEEKLY_REPORT_PAGE_LOADING_FAILED,
					MAVPaths.URL_WEEKLY_REPORT);
		} catch (Exception exception) {
			logger.error(FaultMessages.GENERIC_ERROR, exception);

			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}

		logger.exiting("loadWeeklyReport", mavObj);

		return mavObj;
	}

	/**
	 * This method handles the request and response for downloading weekly
	 * report
	 * 
	 * @param httpServletRequest
	 *            an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *            an instance of HTTPServletRequest
	 * 
	 * @param reportWeeklyVO
	 *            <code>ReportWeeklyVO</code> object containing user input
	 * 
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView downloadWeeklyReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportWeeklyVO reportWeeklyVO) {
		Object[] params = { reportWeeklyVO };
		logger.entering("downloadWeeklyReport", params);

		File reportDirectory = null;
		File weeklyReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		try {

			// Creates Report File Name
			String timeStampFormat = "ddMMyyyy";
			String fromTimeStamp = DateUtil.toDateString(
					DateUtil.toDate(reportWeeklyVO.getFromDate()),
					timeStampFormat);
			String toTimeStamp = DateUtil.toDateString(
					DateUtil.toDate(reportWeeklyVO.getToDate()),
					timeStampFormat);
			String reportFileName = ReportKeys.WEEKLY_REP_FILE_NAME + "_"
					+ fromTimeStamp + "_" + toTimeStamp
					+ ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);

			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			weeklyReportFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!weeklyReportFile.exists()) {
				weeklyReportFile.createNewFile();
			}

			// Writes report data to excel file
			workbook = new HSSFWorkbook();
			reportManagementService.writeWeeklyReport(workbook, reportWeeklyVO);

			fileOutStream = new FileOutputStream(weeklyReportFile);
			workbook.write(fileOutStream);

			logger.debug("Data written to excel workbook");

			// Sets the report file for download
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=" + reportFileName);
			servletOutStream = httpServletResponse.getOutputStream();
			fileInputStream = new FileInputStream(weeklyReportFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}

			servletOutStream.flush();

		} catch (IOException e) {
			logger.error("Exception occured while downloading weekly report");
			return null;
		} catch (MISPException e) {

			logger.error("Exception occured while downloading weekly report");
			return null;
		} catch (Exception e) {

			logger.error("Exception occured while downloading weekly report", e);
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
				if (weeklyReportFile != null && weeklyReportFile.exists()) {
					weeklyReportFile.delete();
				}
			} catch (IOException e) {
				logger.error("Exception occured while closing "
						+ "resources after weekly report download");
				return null;
			}
		}

		logger.exiting("downloadWeeklyReport");
		return null;
	}

	/**
	 * This method handles the request and response for downloading revenue
	 * report
	 * 
	 * @param httpServletRequest
	 *            an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *            an instance of HTTPServletRequest
	 * 
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView downloadRevenueReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		logger.entering("downloadRevenueReport");

		ModelAndView mav = null;

		String year = httpServletRequest.getParameter("reportYear");
		String month = httpServletRequest.getParameter("reportMonth");

		logger.debug("Revenue Report Year/Month: ", year + " " + month);

		File reportDirectory = null;
		File revenueReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;
		boolean isDataExist = false;

		try {

			// Creates Report File Name
			String reportTimeStamp = year
					+ (Integer.valueOf(month) < 9 ? "0" + month : month);
			String reportFileName = ReportKeys.REVENUE_REP_FILE_NAME + "_"
					+ reportTimeStamp + ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);

			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			revenueReportFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!revenueReportFile.exists()) {
				revenueReportFile.createNewFile();
			}

			// Writes report data to excel file
			workbook = new HSSFWorkbook();
			isDataExist = reportManagementService.writeRevenueReport(workbook,
					year, month);

			if (isDataExist) {
				fileOutStream = new FileOutputStream(revenueReportFile);
				workbook.write(fileOutStream);

				logger.debug("Data written to excel workbook");

				// Sets the report file for download
				httpServletResponse.setContentType("application/vnd.ms-excel");
				httpServletResponse.setHeader("Content-Disposition",
						"attachment; filename=" + reportFileName);
				servletOutStream = httpServletResponse.getOutputStream();
				fileInputStream = new FileInputStream(revenueReportFile);
				int i;
				while ((i = fileInputStream.read()) != -1) {
					servletOutStream.write(i);
				}

				servletOutStream.flush();
			} else {
				mav = new ModelAndView(MAVPaths.VIEW_REVENUE_REPORT_PAGE);
				mav.addObject("message", FaultMessages.NO_RECORD_EXIST);
				mav.addObject("month", month);
				mav.addObject("year", year);
			}

		} catch (IOException e) {
			logger.error("Exception occured while downloading revenue report");
			return mav;
		} catch (MISPException e) {

			logger.error("Exception occured while downloading revenue report");
			return mav;
		} catch (Exception e) {

			logger.error("Exception occured while downloading revenue report",
					e);
			return mav;
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
				if (revenueReportFile != null && revenueReportFile.exists()) {
					revenueReportFile.delete();
				}
			} catch (IOException e) {
				logger.error("Exception occured while closing "
						+ "resources after revenue report download");
				return mav;
			}
		}

		logger.exiting("downloadRevenueReport");
		return mav;
	}

	/**
	 * This method handles the request and response for downloading coverage
	 * report
	 * 
	 * @param httpServletRequest
	 *            an instance of HTTPServletRequest downloadCoverageReport
	 * @param httpServletResponse
	 *            an instance of HTTPServletRequest
	 * 
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView downloadCoverageReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		logger.entering("downloadCoverageReport");

		ModelAndView mav = null;

		File reportDirectory = null;
		File coverageReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;
		boolean isDataExist = false;

		String year = httpServletRequest.getParameter("reportYear");
		String month = httpServletRequest.getParameter("reportMonth");

		try {

			// Creates Report File Name
			String reportTimeStamp = year
					+ (Integer.valueOf(month) < 9 ? "0" + month : month);
			String reportFileName = ReportKeys.COVERAGE_REP_FILE_NAME + "_"
					+ reportTimeStamp + ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);

			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			coverageReportFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!coverageReportFile.exists()) {
				coverageReportFile.createNewFile();
			}

			// Writes report data to excel file
			workbook = new HSSFWorkbook();
			isDataExist = reportManagementService.writeCoverageReport(workbook,
					year, month);

			if (isDataExist) {
				fileOutStream = new FileOutputStream(coverageReportFile);
				workbook.write(fileOutStream);

				logger.debug("Data written to excel workbook");

				// Sets the report file for download
				httpServletResponse.setContentType("application/vnd.ms-excel");
				httpServletResponse.setHeader("Content-Disposition",
						"attachment; filename=" + reportFileName);
				servletOutStream = httpServletResponse.getOutputStream();
				fileInputStream = new FileInputStream(coverageReportFile);
				int i;
				while ((i = fileInputStream.read()) != -1) {
					servletOutStream.write(i);
				}

				servletOutStream.flush();
			} else {
				mav = new ModelAndView(MAVPaths.VIEW_COVERAGE_REPORT_PAGE);
				mav.addObject("message", FaultMessages.NO_RECORD_EXIST);
				mav.addObject("month", month);
				mav.addObject("year", year);
			}

		} catch (IOException e) {
			logger.error("Exception occured while downloading coverage report");
			return mav;
		} catch (MISPException e) {

			logger.error("Exception occured while downloading coverage report");
			return mav;
		} catch (Exception e) {

			logger.error("Exception occured while downloading coverage report",
					e);
			return mav;
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
				if (coverageReportFile != null && coverageReportFile.exists()) {
					coverageReportFile.delete();
				}
			} catch (IOException e) {
				logger.error("Exception occured while closing "
						+ "resources after coverage report download");
				return mav;
			}
		}

		logger.exiting("downloadCoverageReport");
		return mav;
	}

	/**
	 * This method launches the Agent Report Download page.
	 * 
	 * @param request
	 *            - An instance of HttpServletRequest Object
	 * 
	 * @param response
	 *            - An instance of HttpServletResponse Object
	 * 
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView loadAgentReport(HttpServletRequest request,
			HttpServletResponse response) {
		logger.entering("loadAgentReport");

		ModelAndView mavObj = null;
		Map<Integer, String> roleMap = null;
		Map<Integer, String> roleMapNew = null;

		try {
			// Creates a list of authorized roles for agent report download
			//old List<String> roleAccessList = Arrays.asList(ReportKeys.AGENT_REP_ACCESS_LEVEL);

			roleMap = userService.getAllRoles();
			noOfRoles=0;
			noOfRoles=roleMap.size();
			noOfRoles++;
			// Creates a new role map with required roles only
			/*old roleMapNew = new HashMap<Integer, String>();
			for (int roleId : roleMap.keySet()) {
				if (roleAccessList.contains(String.valueOf(roleId)))
				{ old*/
					/*if(roleId==3)
					{   
						String mobileCSC="Mobile & CSC Agents";
						roleMapNew.put(roleId, mobileCSC);	
					}
					else */
						/*old roleMapNew.put(roleId, roleMap.get(roleId));
					
					
			  }
			}
			//added role id for "Mobile & CSC Agents"
			roleMapNew.put(9, "Mobile & CSC Agents");
			old*/
			roleMap.put(noOfRoles, "Mobile & CSC Agents");
			mavObj = new ModelAndView(MAVPaths.VIEW_AGENT_REPORT_PAGE);
			mavObj.addObject(MAVObjects.MAP_ROLES, roleMap);
		}
		catch(MISPException mispException)
		{
			logger.error
					(FaultMessages.AGENT_REPORT_PAGE_LOADING_FAILED, mispException);
			
			mavObj = super.error(FaultMessages.AGENT_REPORT_PAGE_LOADING_FAILED);
		}
		catch (Exception exception)
		{			
			logger.error(FaultMessages.GENERIC_ERROR, exception);
			
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}
		
		logger.exiting("loadAgentReport",mavObj);
		
		return mavObj;
	}
	
	/**
	 * This method handles the creation of agent report based on report type.
	 * 
	 * @param httpServletRequest
	 *          an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *			an instance of HTTPServletRequest
	 *
	 * @param agentReportVO
	 *			<code>ReportAgentVO</code> object containing user input
	 *
	 * @param reportType
	 * 			type of report
	 * 			<table border=1>
	 * 				<tr><td>1</td><td>Date Range</td></tr>
	 * 				<tr><td>2</td><td>Current Week</td></tr>
	 * 				<tr><td>3</td><td>Previous Week</td></tr>
	 * 				<tr><td>4</td><td>Current Month</td></tr>
	 * 			</table>
	 * 
	 */
	private ModelAndView createCSCWeeklyReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			ReportAgentVO agentReportVO, int reportType) {
		Object[] params = { agentReportVO, reportType };
		logger.entering("createCSCWeeklyReport");

		File reportDirectory = null;
		File cscWeeklyReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		try {

			// Creates Report File Name
			String timeStampFormat = "ddMMyyyy";
			String fromTimeStamp = DateUtil.toDateString(
					DateUtil.toDate(agentReportVO.getFromDate()),
					timeStampFormat);
			String toTimeStamp = DateUtil
					.toDateString(DateUtil.toDate(agentReportVO.getToDate()),
							timeStampFormat);
			String reportFileName = ReportKeys.CSC_WEEKLY_REP_FILE_NAME + "_"
					+ fromTimeStamp + "_" + toTimeStamp
					+ ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			cscWeeklyReportFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!cscWeeklyReportFile.exists()) {
				cscWeeklyReportFile.createNewFile();
			}

			workbook = new HSSFWorkbook();
			reportManagementService.writeCSCWeeklyReport(workbook, agentReportVO,reportType);

			fileOutStream = new FileOutputStream(cscWeeklyReportFile);
			workbook.write(fileOutStream);
			fileOutStream.flush();

			logger.debug("Data written to excel workbook");

			// Sets the file for download
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=" + reportFileName);
			servletOutStream = httpServletResponse.getOutputStream();
			fileInputStream = new FileInputStream(cscWeeklyReportFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}
			servletOutStream.flush();

		} catch (IOException e) {
			logger.error("Exception occured while downloading agent report", e);
			return null;
		} catch (Exception e) {
			logger.error("Exception occured while downloading agent report", e);
			return null;
		} finally {
			// Closes all resources
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}

				if (servletOutStream != null) {
					servletOutStream.close();
				}

				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (cscWeeklyReportFile != null && cscWeeklyReportFile.exists()) {
					cscWeeklyReportFile.delete();
				}
			} catch (Exception e) {
				logger.error("Exception occured while "
						+ "closing resources after agent report download");
				return null;
			}
		}

		logger.exiting("createCSCWeeklyReport");
		return null;
	}
	
	
	/**
	 * This method launches the Agent Report Download page.
	 * 
	 * @param request
	 *            - An instance of HttpServletRequest Object
	 * 
	 * @param response
	 *            - An instance of HttpServletResponse Object
	 * 
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView loadDeductionReport(HttpServletRequest request,
			HttpServletResponse response) {
		logger.entering("loadDeductionReport");

		ModelAndView mavObj = null;
		Map<Integer, String> roleMap = null;
		Map<Integer, String> roleMapNew = null;
		Map<Integer, String> monthMap = null;

		try {
			// Creates a list of authorized roles for agent report download
			//old List<String> roleAccessList = Arrays.asList(ReportKeys.AGENT_REP_ACCESS_LEVEL);
			roleMap = userService.getAllRoles();
			noOfRoles=0;
			noOfRoles=roleMap.size();
			noOfRoles++;
			// Creates a new role map with required roles only
			/*old roleMapNew = new HashMap<Integer, String>();
			for (int roleId : roleMap.keySet()) {
				if (roleAccessList.contains(String.valueOf(roleId)))
				{ old*/
					/*if(roleId==3)
					{   
						String mobileCSC="Mobile & CSC Agents";
						roleMapNew.put(roleId, mobileCSC);	
					}
					else */
						/*old roleMapNew.put(roleId, roleMap.get(roleId));
					
					
			  }
			}
			//added role id for "Mobile & CSC Agents"
			roleMapNew.put(9, "Mobile & CSC Agents");
			old*/
			roleMap.put(noOfRoles, "Mobile & CSC Agents");
			
			mavObj = new ModelAndView(MAVPaths.VIEW_DEDUCTIONS_REPORT_PAGE);
			mavObj.addObject(MAVObjects.MAP_ROLES, roleMap);
		}
		catch(MISPException mispException)
		{
			logger.error
					(FaultMessages.CHANGE_FIRST_MONTH_MODE_FAILED, mispException);
			
			mavObj = super.error(FaultMessages.CHANGE_FIRST_MONTH_MODE_FAILED);
		}
		catch (Exception exception)
		{			
			logger.error(FaultMessages.GENERIC_ERROR, exception);
			
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}
		
		logger.exiting("loadDeductionReport",mavObj);
		
		return mavObj;
	}
	
	
	public ModelAndView downloadDeductionReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			DeductionReportVO dedReportVO) {
		
		Object[] params = { dedReportVO };
		logger.entering("downloadDeductionReport", params);
		
		ModelAndView mav = null;

		String toDate = null;
		String fromDate = null;
		List<UserDetails> userDetailsList = null;

		int filterOption = -1;
		int quickSelectOptionForQuarter = -1;
		int quickSelectOption = -1;
		int monthSel = 0;
		try {

			List<String> dateRange = new ArrayList<String>();
			Map<Integer, List<Integer>> dedReportMap = new HashMap<Integer, List<Integer>>();
			
			Map<Integer, CustDeductionReportVO> deductionReport = null;
			
			int reportType = Integer.valueOf(httpServletRequest
					.getParameter("reportType"));
			// Gets the details of all users
			userDetailsList = reportManagementService.getActiveUserDetailList(
					dedReportVO, noOfRoles);
			if (reportType == 0) {
				filterOption = Integer.valueOf(httpServletRequest
						.getParameter("filterOption"));

				if (filterOption == 0) {
					quickSelectOption = Integer.valueOf(httpServletRequest
							.getParameter("quickSelectOption"));

					// if quickSelectOption = 0 then date range for current month
					// if quickSelectOption = 1 then date range for previous month
					dateRange = DateUtil
							.getDateRangeForFirstMonthDeductedReport(quickSelectOption);
				} else {
					monthSel = Integer.valueOf(httpServletRequest
							.getParameter("monthSel"));

					// based on drop-down month, set date range for First Month Report
					dateRange = DateUtil.getDateRange(monthSel);
				}
			} else {
				// date range for "First Quarter Deduction Report"
				quickSelectOptionForQuarter = Integer
						.valueOf(httpServletRequest
								.getParameter("quickSelectOptionForQuarter"));
				dateRange = DateUtil.getDateRangeForCalenderMonth(quickSelectOptionForQuarter);
			
			}
			
			fromDate = dateRange.get(0);
			toDate = dateRange.get(1);
		
			logger.info("fromDate : "+fromDate);
			logger.info("toDate : "+toDate);

			dedReportVO.setFromDate(fromDate);
			dedReportVO.setToDate(toDate);
			dedReportVO.setUserDetailsList(userDetailsList);

			// First Month Deduction Report
			if(reportType == 0){
				
				// fetch confirmed customer list, for selected month by user
				dedReportMap = reportManagementService.getConfirmedCustList(dedReportVO);
				// fetch customer partially/fully deduction list based on confirmed customer of prior 3 months
				deductionReport = reportManagementService.getCustDeductionReportList(dedReportMap,fromDate,toDate);
				dedReportVO.setDeductionReport(deductionReport);
				mav = createAgentDeductionReport(httpServletRequest,httpServletResponse, dedReportVO, fromDate, toDate,reportType);
			}else{
				// fetch confirmed customer list, prior 3rd month from current month
				dedReportMap = reportManagementService.getConfirmedCustListForQuarterReport(dedReportVO);
				dateRange = DateUtil.getDateRangeForQuarterReport(quickSelectOptionForQuarter);
				String fromDate1 = dateRange.get(0);
				String toDate1 = dateRange.get(1);
				// fetch fully deducted customer count for next 2 month, based on confirmed customer
				deductionReport = reportManagementService.getCustDeductionQuarterReportList(dedReportMap,fromDate1,toDate1);
				dedReportVO.setDeductionReport(deductionReport);
				
				mav = createAgentDeductionReport(httpServletRequest,httpServletResponse, dedReportVO,fromDate,toDate,reportType);
				
			}
			
			
		} catch (MISPException e) {
			logger.error("Exception occured while downloading agent report", e);
			return super.error(FaultMessages.GENERIC_ERROR);
		} catch (Exception e) {
			logger.error("Exception occured while downloading agent report", e);
			return super.error(FaultMessages.GENERIC_ERROR);
		}

		return mav;
	}

	
	/**
	 * This method handles the creation of first month deduction 
	 * report based on report type.
	 * 
	 * @param httpServletRequest
	 *          an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *			an instance of HTTPServletRequest
	 * @param toDate 
	 * @param fromDate 
	 * @param reportType 
	 *
	 * @param List of CustDeductionReportVO
	 *			<code>CustDeductionReportVO</code> object containing user input
	 *
	 * 
	 */
	private ModelAndView createAgentDeductionReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,
			DeductionReportVO dedReportVO, String fromDate, String toDate, int reportType) throws MISPException{
		logger.entering("createAgentDeductionReport", dedReportVO,fromDate,toDate,reportType);
		
		File reportDirectory = null;
		File deductionFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		try {

			// Creates Report File Name
			String timeStampFormat = "ddMMyyyy";
			String reportFileName = null;
			String fromTimeStamp = DateUtil.toDateString(
					DateUtil.toDate(fromDate),
					timeStampFormat);
			String toTimeStamp = DateUtil
					.toDateString(DateUtil.toDate(toDate),
							timeStampFormat);
			
			if(reportType == 0){
				reportFileName = ReportKeys.FIRST_MONTH_DEDUCTION_REP_FILE_NAME + "_"
						+ fromTimeStamp + "_" + toTimeStamp
						+ ReportKeys.REPORT_FILE_TYPE;
			}else{
				reportFileName = ReportKeys.FIRST_QUARTER_DEDUCTION_REP_FILE_NAME + "_"
						+ fromTimeStamp + "_" + toTimeStamp
						+ ReportKeys.REPORT_FILE_TYPE;
			}
			

			// Creates the directory for generating report
			reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}

			// Creates the report file
			deductionFile = new File(reportDirectory.getAbsolutePath()
					+ File.separator + reportFileName);

			if (!deductionFile.exists()) {
				deductionFile.createNewFile();
			}

			workbook = new HSSFWorkbook();
			reportManagementService.writeDeductionReport(workbook, dedReportVO,
					fromDate,toDate,reportType);

			fileOutStream = new FileOutputStream(deductionFile);
			workbook.write(fileOutStream);
			fileOutStream.flush();

			logger.debug("Data written to excel workbook");

			// Sets the file for download
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=" + reportFileName);
			servletOutStream = httpServletResponse.getOutputStream();
			fileInputStream = new FileInputStream(deductionFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}
			servletOutStream.flush();

		} catch (IOException e) {
			logger.error("Exception occured while downloading first month deductions report", e);
			return null;
		} catch (MISPException e) {
			logger.error("Exception occured while downloading first month deductions report", e);
			return null;
		} catch (Exception e) {

			logger.error("Exception occured while downloading first month deductions report", e);
			return null;
		} finally {
			// Closes all resources
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}

				if (servletOutStream != null) {
					servletOutStream.close();
				}

				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (deductionFile != null && deductionFile.exists()) {
					deductionFile.delete();
				}
			} catch (Exception e) {
				logger.error("Exception occured while "
						+ "closing resources after agent report download");
				return null;
			}
		}
		
		
		logger.exiting("createAgentDeductionReport");
		return null;
	}	
	
	
}
