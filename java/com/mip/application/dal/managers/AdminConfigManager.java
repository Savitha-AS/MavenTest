package com.mip.application.dal.managers;

import static com.mip.application.constants.DBObjects.TABLE_ADMIN_CONFIG;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.HibernateException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mip.application.view.CustomerStatsVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
/**
 * <p>
 * <code>AdminConfigManager.java</code> contains all the data access layer methods 
 * pertaining to AdminConfig model.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class AdminConfigManager {
		
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			AdminConfigManager.class);

	/**
	 * Set inversion of Control for <code>JdbcTemplate</code>.
	 */
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * Gets max idle count from Admin Config table.
	 * 
	 * @return maxIdleCount
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getConfigDetails() throws DBException{			
		logger.entering("getConfigDetails");

		Map<String,String> configMap = new HashMap<String, String>();
		try{			
			StringBuilder queryString = new StringBuilder("");
			queryString.append("SELECT data_val ");
			queryString.append("FROM ").append(TABLE_ADMIN_CONFIG);

			List<ListOrderedMap> valueList = jdbcTemplate.queryForList(
						queryString.toString());
			String defaultPwd = String.valueOf(
								valueList.get(0).get("data_val"));
			String userLoginPrefix = String.valueOf(
								valueList.get(1).get("data_val"));
			String pwdHistoryLimit = String.valueOf(
								valueList.get(2).get("data_val"));
			String maxLoginAttempts = String.valueOf(
								valueList.get(3).get("data_val"));
			String maxIdleCount = String.valueOf(
								valueList.get(4).get("data_val"));
			String registerCustomerWSURL = String.valueOf(
								valueList.get(5).get("data_val"));
			String announcement = String.valueOf(
								valueList.get(6).get("data_val"));
			String offerSubscriptionLastDay = String.valueOf(
								valueList.get(7).get("data_val"));
			String msisdnCodes = String.valueOf(
								valueList.get(8).get("data_val"));
			String offerUnsubscribeWSURL = String.valueOf(
								valueList.get(9).get("data_val"));
			String removeCustomerRegistrationWSURL = String.valueOf(
					valueList.get(10).get("data_val"));
			String commissionPercent = String.valueOf(
					valueList.get(12).get("data_val"));
			String deregisterCustomerWSURL = String.valueOf(
					valueList.get(13).get("data_val"));
			String changeDeductionModeWSURL=String.valueOf(
					valueList.get(16).get("data_val"));
			String summaryRecordCustomerDetailsChanges=String.valueOf(
					valueList.get(17).get("data_val"));
			
			String reactivationCustomerWSURL=String.valueOf(
					valueList.get(19).get("data_val"));
			
			String assignOfferWSURL=String.valueOf(
					valueList.get(18).get("data_val"));
			
			String loyaltyPackWSURL=String.valueOf(
					valueList.get(20).get("data_val"));
			
			String insMsisdnCodes=String.valueOf(
					valueList.get(21).get("data_val"));
			
			configMap.put(PlatformConstants.DEFAULT_PASSWORD, defaultPwd);
			configMap.put(PlatformConstants.USER_LOGIN_PREFIX, userLoginPrefix);
			configMap.put(PlatformConstants.PASSWORD_HISTORY_LIMIT, 
					pwdHistoryLimit);
			configMap.put(PlatformConstants.MAX_LOGIN_ATTEMPTS, 
					maxLoginAttempts);
			configMap.put(PlatformConstants.MAX_IDLE_COUNT, maxIdleCount);
			configMap.put(PlatformConstants.REGISTER_CUSTOMER_WS_URL, 
					registerCustomerWSURL);
			configMap.put(PlatformConstants.ANNOUNCEMENT_MESSAGE, announcement);
			configMap.put(PlatformConstants.OFFER_SUBSCRIPTION_LAST_DAY,
					offerSubscriptionLastDay);
			configMap.put(PlatformConstants.MSISDN_CODES, msisdnCodes);
			configMap.put(PlatformConstants.OFFER_UNSUBSCRIBE_WS_URL
													, offerUnsubscribeWSURL);
			configMap.put(PlatformConstants.REMOVE_CUSTOMER_REGISTRATION_WS_URL
											, removeCustomerRegistrationWSURL);
			configMap.put(PlatformConstants.COMMISSION_PERCENTAGE,
					commissionPercent);
			configMap.put(PlatformConstants.DEREGISTER_CUSTOMER_WS_URL, 
					deregisterCustomerWSURL);
			configMap.put(PlatformConstants.CHANGE_DEDUCTIONMODE_WSURL,
					changeDeductionModeWSURL);
			configMap.put(PlatformConstants.SUMMARY_CUSTOMER_DETAILS_CHANGES_RECORD_LIMIT, summaryRecordCustomerDetailsChanges);
			
			configMap.put(PlatformConstants.REACTIVATION_CUSTOMER_WSURL,
					reactivationCustomerWSURL);
			
			configMap.put(PlatformConstants.ASSIGN_OFFER_WS_URL_WSURL,
					assignOfferWSURL);
			
			configMap.put(PlatformConstants.LOYALTY_PACK_WS_URL_WSURL,
					loyaltyPackWSURL);
			configMap.put(PlatformConstants.INS_MSISDN_CODES,
					insMsisdnCodes);
			
			
			
			
		}catch(DataAccessException e){
			logger.error("Exception occured while retrieving Config details " +
					"from DB.", e);
			throw new DBException(e);
		}
		
		logger.exiting("getConfigDetails", configMap);
		return configMap;
	}

	/**
	 * Save max idle count into Admin Config table.
	 * 
	 * @param value
	 * @return noOfRowsAffected
	 * @throws DBException
	 */
	public int saveConfigDetails(String name, String value) throws DBException {
		Object[] params = { name, value };
		logger.entering("saveConfigDetails", params);

		int noOfRowsAffected = 0;
		try {
			StringBuilder queryString = new StringBuilder("");
			queryString.append("UPDATE ").append(TABLE_ADMIN_CONFIG);

			Object[] queryParams = null;
			if (value == null) {
				queryString.append(" SET data_val = NULL ");
				queryParams = new Object[] { name };
			} else {
				queryString.append(" SET data_val = ? ");
				queryParams = new Object[] { "" + value, name };
			}
			queryString.append(" WHERE param = ? ") ;

			logger.debug("",  queryString) ;

			noOfRowsAffected = jdbcTemplate.update(queryString.toString(),
					queryParams);

		} catch (DataAccessException e) {
			logger.error("Exception occured while updating Config details "
					+ "in DB.", e);
			throw new DBException(e);
		}
		logger.exiting("saveConfigDetails");
		return noOfRowsAffected;
	}

	/**
	 * This method retrieves the customer statistics from the database.
	 * 
	 * @param loginId
	 *            User Id of the logged in user.
	 * 
	 * @return <code>Map</code> containing statistics details.
	 * 
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public CustomerStatsVO getCustomerStats(int loginId) throws DBException {
		Object[] params = { loginId };
		logger.entering("getCustomerStats", params);
		CustomerStatsVO customerStats = null;
	//	CustomerStatsVO customerStatsDeducted = null;
		CustomerStatsVO customerStatsdeductedConfirmed=null;
		CustomerStatsVO customerStatsFullyPartiallyDeducted = null;

		try {

			Calendar curDate = Calendar.getInstance();
			Calendar date15th = Calendar.getInstance();
			date15th.set(Calendar.DATE, 15);

			StringBuilder queryString = new StringBuilder("SELECT ");
	        StringBuilder deductedConfirmed = new StringBuilder("SELECT ");
			StringBuilder partialFullyDeducted = new StringBuilder("SELECT ");
			float HospConfirmedByFullyDeductedf=0.0f;
			float IpConfirmedByFullyDeductedf=0.0f;
			float XlConfirmedByFullyDeductedf=0.0f;
			
			if (curDate.after(date15th)) {

				String confDate="(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00')) AND (DATE_ADD(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 1 MONTH))";
				queryString
						.append("COUNT(IF(product_id=1 AND is_confirmed=1 AND is_reactivated=0 ,1,null)) freeModelRegByUserConfirmed,")
						.append("COUNT(IF(product_id=1 AND is_confirmed=0 AND is_reactivated=0 ,1,null)) freeModelRegByUserPending,")
						.append("COUNT(IF(product_id=2 AND is_confirmed=1 AND is_reactivated=0 ,1,null)) xlRegByUserConfirmed,")
						.append("COUNT(IF(product_id=2 AND is_confirmed=0 AND is_reactivated=0 ,1,null)) xlRegByUserPending,")
						.append("COUNT(IF(product_id=3 AND is_confirmed=1 AND is_reactivated=0 ,1,null)) hospRegByUserConfirmed,")
						.append("COUNT(IF(product_id=3 AND is_confirmed=0 AND is_reactivated=0 ,1,null)) hospRegByUserPending,")
						.append("COUNT(IF(product_id=4 AND is_confirmed=1 AND is_reactivated=0 ,1,null)) ipRegByUserConfirmed,")
						.append("COUNT(IF(product_id=4 AND is_confirmed=0 AND is_reactivated=0 ,1,null)) ipRegByUserPending,")
						.append("(SELECT data_val FROM ADMIN_CONFIG  WHERE param = ?) marqueeMessage ")
						.append("FROM CUSTOMER_SUBSCRIPTION WHERE reg_by=? AND ")
						.append("reg_date BETWEEN DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00') AND DATE_ADD(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 1 MONTH) ");
				
				/**
				 * below query fetches the count of confirmed and deducted customers
				 */
	
				
				deductedConfirmed
						.append("sum(IF(product_id = 2   AND  ifnull(is_fully_deducted,0) <> 1 AND   is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN"+confDate+" ) , 0.5, 0)) xlConfirmedByPartiallyDeducted,")
						.append("sum(IF(product_id = 3  AND  ifnull(is_fully_deducted,0) <> 1 AND   is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN"+confDate+") , 0.5, 0)) hospConfirmedByPartiallyDeducted,")
						.append("sum(IF(product_id = 4  AND  ifnull(is_fully_deducted,0) <> 1 AND   is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN "+confDate+"), 0.5, 0)) ipConfirmedByPartiallyDeducted, ")
						.append("sum(IF(product_id = 2  AND  is_fully_deducted=1 AND  (" )
						.append("fully_deducted_date BETWEEN"+confDate+") , 1, 0)) xlConfirmedByFullyDeducted,")
						.append("sum(IF(product_id = 3  AND  is_fully_deducted=1 AND (" )
						.append("fully_deducted_date BETWEEN"+confDate+") , 1, 0)) hospConfirmedByFullyDeducted, ")
						.append("sum(IF(product_id = 4  AND  is_fully_deducted=1 AND (" )
						.append("fully_deducted_date BETWEEN "+confDate+") , 1, 0)) ipConfirmedByFullyDeducted ")
						.append("FROM CUSTOMER_SUBSCRIPTION WHERE reg_by=? AND  is_confirmed=1 AND ")
						.append("conf_date BETWEEN DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00') AND " )
						.append(" DATE_ADD(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 1 MONTH) ");
				/**
				 * below query fetches the count of confirmed 90 days before 16th and partially fully deducted customers
				 */
				partialFullyDeducted
						.append(" sum(IF(product_id = 2  AND  ifnull(is_fully_deducted,0) <> 1 AND   is_partially_deducted=1 AND (" )
						.append(" partially_deducted_date BETWEEN"+confDate+") , 0.5, 0)) xlConfirmedByPartiallyDeducted,")
						.append(" sum(IF(product_id = 3  AND  ifnull(is_fully_deducted,0) <> 1 AND   is_partially_deducted=1 AND (" )
						.append(" partially_deducted_date BETWEEN"+confDate+") , 0.5, 0)) hospConfirmedByPartiallyDeducted,")
						.append(" sum(IF(product_id = 4   AND  ifnull(is_fully_deducted,0) <> 1 AND   is_partially_deducted=1 AND (" )
						.append(" partially_deducted_date BETWEEN"+confDate+"), 0.5, 0)) ipConfirmedByPartiallyDeducted, ")
						
						.append(" SUM(CASE WHEN product_id=2 AND  IFNULL(is_partially_deducted,0) = 1 ")
						.append(" AND partially_deducted_date BETWEEN DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 1 MONTH) " )
						.append(" AND DATE_ADD(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 0 MONTH)")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDate+" ")
						.append(" THEN 1*0.5 ")
						.append(" WHEN product_id=2 ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDate+" ")
						.append(" THEN 1 ELSE 0 END ) xlConfirmedByFullyDeducted, ")
						
						.append(" SUM(CASE WHEN product_id=3 AND  IFNULL(is_partially_deducted,0) = 1 ")
						.append(" AND partially_deducted_date BETWEEN DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 1 MONTH) " )
						.append(" AND DATE_ADD(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 0 MONTH)")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDate+" ")
						.append(" THEN 1*0.5 ")
						.append(" WHEN product_id=3 ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDate+" ")
						.append(" THEN 1 ELSE 0 END ) hospConfirmedByFullyDeducted, ")
						
						.append(" SUM(CASE WHEN product_id=4 AND  IFNULL(is_partially_deducted,0) = 1 ")
						.append(" AND partially_deducted_date BETWEEN DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 1 MONTH) " )
						.append(" AND DATE_ADD(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 0 MONTH)")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDate+" ")
						.append(" THEN 1*0.5 ")
						.append(" WHEN product_id=4 ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDate+" ")
						.append(" THEN 1 ELSE 0 END ) ipConfirmedByFullyDeducted  ")
						.append("FROM CUSTOMER_SUBSCRIPTION WHERE reg_by=? AND  is_confirmed=1 AND ")
						//.append("conf_date BETWEEN DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 2 MONTH) " )
						//.append(" AND DATE_ADD(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 1 MONTH)");
						//Bug fix by changing the interval
						.append("conf_date BETWEEN DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 1 MONTH) " )
						.append(" AND DATE_ADD(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 0 MONTH)");
				
						
			} 
			
			else {
				
				String confDateBfore="(DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 1 MONTH)) AND (DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'))";
				queryString
						.append("COUNT(IF(product_id=1 AND is_confirmed=1 AND is_reactivated=0 ,1,null)) freeModelRegByUserConfirmed,")
						.append("COUNT(IF(product_id=1 AND is_confirmed=0 AND is_reactivated=0 ,1,null)) freeModelRegByUserPending,")
						.append("COUNT(IF(product_id=2 AND is_confirmed=1 AND is_reactivated=0 ,1,null)) xlRegByUserConfirmed,")
						.append("COUNT(IF(product_id=2 AND is_confirmed=0 AND is_reactivated=0 ,1,null)) xlRegByUserPending,")
						.append("COUNT(IF(product_id=3 AND is_confirmed=1 AND is_reactivated=0 ,1,null)) hospRegByUserConfirmed,")
						.append("COUNT(IF(product_id=3 AND is_confirmed=0 AND is_reactivated=0 ,1,null)) hospRegByUserPending,")
						.append("COUNT(IF(product_id=4 AND is_confirmed=1 AND is_reactivated=0 ,1,null)) ipRegByUserConfirmed,")
						.append("COUNT(IF(product_id=4 AND is_confirmed=0 AND is_reactivated=0 ,1,null)) ipRegByUserPending,")
						.append("(SELECT data_val FROM ADMIN_CONFIG  WHERE param = ?) marqueeMessage ")
						.append("FROM CUSTOMER_SUBSCRIPTION WHERE reg_by=? AND ")
						.append(" reg_date BETWEEN DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 1 MONTH) " )
						.append("AND DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59') ");
				
						/**
						 * below query fetches the count of confirmed and deducted customers
						 */
				
				deductedConfirmed
						.append("sum(IF(product_id = 2   AND  ifnull(is_fully_deducted,0) <> 1 AND  is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN"+confDateBfore+" ), 0.5, 0)) xlConfirmedByPartiallyDeducted,")
						.append("sum(IF(product_id = 3  AND  ifnull(is_fully_deducted,0) <> 1  AND  is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN "+confDateBfore+"), 0.5, 0)) hospConfirmedByPartiallyDeducted, ")
						.append("sum(IF(product_id = 4   AND  ifnull(is_fully_deducted,0) <> 1 AND  is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN "+confDateBfore+"), 0.5, 0)) ipConfirmedByPartiallyDeducted, ")
						.append("sum(IF(product_id = 2   AND  is_fully_deducted=1 AND (" )
						.append("fully_deducted_date BETWEEN "+confDateBfore+"), 1, 0)) xlConfirmedByFullyDeducted,")
						.append("sum(IF(product_id = 3   AND  is_fully_deducted=1 AND (" )
						.append("fully_deducted_date BETWEEN "+confDateBfore+"),  1, 0)) hospConfirmedByFullyDeducted, ")
						.append("sum(IF(product_id = 4   AND  is_fully_deducted=1 AND (" )
						.append("fully_deducted_date BETWEEN "+confDateBfore+"), 1, 0)) ipConfirmedByFullyDeducted ")
						.append("FROM CUSTOMER_SUBSCRIPTION WHERE reg_by=? AND  is_confirmed=1 AND ")
						.append("conf_date BETWEEN DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 1 MONTH) " )
						.append("AND DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59')");
				
				/**
				 * below query fetches the count of confirmed90 days before 16th and partially fully deducted customers
				 */
				partialFullyDeducted
						.append("sum(IF(product_id = 2   AND  ifnull(is_fully_deducted,0) <> 1 AND  is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN "+confDateBfore+"), 0.5, 0)) xlConfirmedByPartiallyDeducted,")
						.append("sum(IF(product_id = 3  AND  ifnull(is_fully_deducted,0) <> 1  AND  is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN "+confDateBfore+"), 0.5, 0)) hospConfirmedByPartiallyDeducted, ")
						.append("sum(IF(product_id = 4   AND  ifnull(is_fully_deducted,0) <> 1 AND  is_partially_deducted=1 AND (" )
						.append("partially_deducted_date BETWEEN "+confDateBfore+"), 0.5, 0)) ipConfirmedByPartiallyDeducted, ")
						
						.append(" SUM(CASE WHEN product_id=2 AND  IFNULL(is_partially_deducted,0) = 1 ")
						.append(" AND partially_deducted_date BETWEEN  DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 2 MONTH)" )
						.append(" AND DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 1 MONTH) ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDateBfore+" ")
						.append(" THEN 1*0.5 ")
						.append(" WHEN product_id=2 ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDateBfore+" ")
						.append(" THEN 1 ELSE 0 END ) xlConfirmedByFullyDeducted, ")
						
						.append(" SUM(CASE WHEN product_id=3 AND  IFNULL(is_partially_deducted,0) = 1 ")
						.append(" AND partially_deducted_date BETWEEN  DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 2 MONTH)" )
						.append(" AND DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 1 MONTH) ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDateBfore+" ")
						.append(" THEN 1*0.5 ")
						.append(" WHEN product_id=3 ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDateBfore+" ")
						.append(" THEN 1 ELSE 0 END ) hospConfirmedByFullyDeducted, ")
						
						.append(" SUM(CASE WHEN product_id=4 AND  IFNULL(is_partially_deducted,0) = 1 ")
						.append(" AND partially_deducted_date BETWEEN  DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 2 MONTH)" )
						.append(" AND DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 1 MONTH) ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDateBfore+" ")
						.append(" THEN 1*0.5 ")
						.append(" WHEN product_id=4 ")
						.append(" AND IFNULL(is_fully_deducted,0) = 1 ")
						.append(" AND fully_deducted_date BETWEEN "+confDateBfore+" ")
						.append(" THEN 1 ELSE 0 END ) ipConfirmedByFullyDeducted  ")
						.append("FROM CUSTOMER_SUBSCRIPTION WHERE reg_by=?  AND is_confirmed=1 AND ")
						//.append("conf_date BETWEEN  DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 3 MONTH)" )
						//.append("AND DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 0 MONTH) ");
						.append("conf_date BETWEEN  DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-16 00:00:00'), INTERVAL 2 MONTH)" )
						.append("AND DATE_SUB(DATE_FORMAT(CURDATE(),'%Y-%m-15 23:59:59'), INTERVAL 1 MONTH) ");
		
			}

			Object[] queryParams = {PlatformConstants.ANNOUNCEMENT_MESSAGE,loginId };
			
			Object[] queryDeductedParams = {loginId };
			
			String DefaultValue="0";
			logger.debug("Query: ", queryString);
			logger.debug("Query deductedConfirmed: ", deductedConfirmed);
			logger.debug("Query partialFullyDeducted: ", partialFullyDeducted);
			customerStats = (CustomerStatsVO) jdbcTemplate.queryForObject(
					queryString.toString(), queryParams,
					new BeanPropertyRowMapper(CustomerStatsVO.class));
			
			customerStatsdeductedConfirmed = (CustomerStatsVO) jdbcTemplate.queryForObject(
					deductedConfirmed.toString(), queryDeductedParams,
					new BeanPropertyRowMapper(CustomerStatsVO.class));

			customerStatsFullyPartiallyDeducted = (CustomerStatsVO) jdbcTemplate.queryForObject(
					partialFullyDeducted.toString(), queryDeductedParams,
					new BeanPropertyRowMapper(CustomerStatsVO.class));
			
			if(customerStatsdeductedConfirmed!=null){
	            
				customerStats.setXlConfirmedByFullyDeducted(customerStatsdeductedConfirmed.getXlConfirmedByFullyDeducted());
				customerStats.setXlConfirmedByPartiallyDeducted(customerStatsdeductedConfirmed.getXlConfirmedByPartiallyDeducted());
				customerStats.setHospConfirmedByFullyDeducted(customerStatsdeductedConfirmed.getHospConfirmedByFullyDeducted());
				customerStats.setHospConfirmedByPartiallyDeducted(customerStatsdeductedConfirmed.getHospConfirmedByPartiallyDeducted());
				customerStats.setIpConfirmedByFullyDeducted(customerStatsdeductedConfirmed.getIpConfirmedByFullyDeducted());
				customerStats.setIpConfirmedByPartiallyDeducted(customerStatsdeductedConfirmed.getIpConfirmedByPartiallyDeducted());
				if(customerStatsdeductedConfirmed.getXlConfirmedByFullyDeducted()==null)
				customerStats.setXlConfirmedByFullyDeducted(DefaultValue);
				if(customerStatsdeductedConfirmed.getXlConfirmedByPartiallyDeducted()==null)
				customerStats.setXlConfirmedByPartiallyDeducted(DefaultValue);
				if(customerStatsdeductedConfirmed.getHospConfirmedByFullyDeducted()==null)
				customerStats.setHospConfirmedByFullyDeducted(DefaultValue);
				if(customerStatsdeductedConfirmed.getHospConfirmedByPartiallyDeducted()==null)
				customerStats.setHospConfirmedByPartiallyDeducted(DefaultValue);
				if(customerStatsdeductedConfirmed.getIpConfirmedByFullyDeducted()==null)
				customerStats.setIpConfirmedByFullyDeducted(DefaultValue);
				if(customerStatsdeductedConfirmed.getIpConfirmedByPartiallyDeducted()==null)
				customerStats.setIpConfirmedByPartiallyDeducted(DefaultValue);
							
			}
			else{
				customerStats.setXlConfirmedByFullyDeducted(DefaultValue);
				customerStats.setXlConfirmedByPartiallyDeducted(DefaultValue);
				customerStats.setHospConfirmedByFullyDeducted(DefaultValue);
				customerStats.setHospConfirmedByPartiallyDeducted(DefaultValue);
				customerStats.setIpConfirmedByFullyDeducted(DefaultValue);
				customerStats.setIpConfirmedByPartiallyDeducted(DefaultValue);
			}
			
			if(customerStatsFullyPartiallyDeducted!=null){
			
			float tempPartiallXl = 0,tempFullyXl = 0,tempFullyHp = 0,tempPartiallHp = 0,tempFullyIp = 0,tempPartiallIp = 0;
			if(customerStatsFullyPartiallyDeducted.getXlConfirmedByFullyDeducted()!=null)
				tempFullyXl=Float.parseFloat(customerStatsFullyPartiallyDeducted.getXlConfirmedByFullyDeducted());
			if(customerStatsFullyPartiallyDeducted.getXlConfirmedByPartiallyDeducted()!=null)
				tempPartiallXl=Float.parseFloat(customerStatsFullyPartiallyDeducted.getXlConfirmedByPartiallyDeducted());

			   XlConfirmedByFullyDeductedf =tempPartiallXl +tempFullyXl;
		         
		     if(customerStatsFullyPartiallyDeducted.getHospConfirmedByFullyDeducted()!=null)
		    	 tempFullyHp=Float.parseFloat(customerStatsFullyPartiallyDeducted.getHospConfirmedByFullyDeducted());
		     if(customerStatsFullyPartiallyDeducted.getHospConfirmedByPartiallyDeducted()!=null)
		    	 tempPartiallHp=Float.parseFloat(customerStatsFullyPartiallyDeducted.getHospConfirmedByPartiallyDeducted());
		     
		     HospConfirmedByFullyDeductedf = tempFullyHp +tempPartiallHp;
		     
		     if(customerStatsFullyPartiallyDeducted.getIpConfirmedByFullyDeducted()!=null)
		    	 tempFullyIp=Float.parseFloat(customerStatsFullyPartiallyDeducted.getIpConfirmedByFullyDeducted());
		     if(customerStatsFullyPartiallyDeducted.getIpConfirmedByPartiallyDeducted()!=null)
		    	 tempPartiallIp=Float.parseFloat(customerStatsFullyPartiallyDeducted.getIpConfirmedByPartiallyDeducted());
   		    IpConfirmedByFullyDeductedf = tempFullyIp +tempPartiallIp;
			customerStats.setXlConfirmedInFully_PartiallyDeducted(Float.toString(XlConfirmedByFullyDeductedf));
			customerStats.setHospConfirmedInFully_PartiallyDeducted(Float.toString(HospConfirmedByFullyDeductedf));
			customerStats.setIpConfirmedInFully_PartiallyDeducted(Float.toString(IpConfirmedByFullyDeductedf));

		}
			

		} catch (HibernateException hibernateException) {

			logger.error("Error retrieving customer details: ",
					hibernateException);
			throw new DBException(hibernateException);
		}

		logger.exiting("getCustomerStats");
		return customerStats;
	}
}
