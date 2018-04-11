package com.mip.application.dal.managers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mip.application.model.CoverHistory;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>CoverHistoryArchiveManager.java</code> contains all the data access
 * layer methods pertaining to cover history model.
 * </p>
 * 
 * <br/>
 * This class extends the <code>DataAccessManager</code> class of our MISP
 * framework. </p>
 * 
 * @see com.mip.framework.dao.impls.DataAccessManager
 * 
 * @author T H B S
 * 
 */
public class CoverHistoryArchiveManager {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CoverHistoryArchiveManager.class);

	/**
	 * Set inversion of Control for <code>jdbcTemplateCH</code>.
	 */
	private JdbcTemplate jdbcTemplateCH;

	public JdbcTemplate getJdbcTemplateCH() {
		return jdbcTemplateCH;
	}

	public void setJdbcTemplateCH(JdbcTemplate jdbcTemplateCH) {
		this.jdbcTemplateCH = jdbcTemplateCH;
	}

	/**
	 * This method gets the complete cover history details for a msisdn from
	 * back up cover history tables stored in .128 server
	 * 
	 * @param msisdn
	 *            customer msidn
	 * @return List<CoverHistory> details containing cover history
	 * 
	 * @throws DBException
	 *             If any exception occurs
	 */
	@SuppressWarnings("unchecked")
	public List<CoverHistory> getCoverHistoryDetails(String msisdn)
			throws DBException {

		logger.entering("getCoverHistoryDetails", msisdn);

		List<CoverHistory> coverHistoryList = null;

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		cal.add(Calendar.MONTH, -3);
		
		int currMonth = cal.get(Calendar.MONTH);
		int currYear = cal.get(Calendar.YEAR);
		
		

		logger.debug("currMonth : ", currMonth);
		logger.debug("currYear : ", currYear);

		// Cover history data available from July 2012
		int startYear = 2013;
		
		// Build the sql query
		StringBuilder queryString = new StringBuilder(" select  coverHistoryId,")
		.append("  custId, msisdn,  prevMonthUsage,")
		.append("  coverFree, if(chargesXL = 0.00, 0, chargesXL) as chargesXL, coverXL,")
		.append(" if(chargesHP = 0.00, 0, chargesHP) as chargesHP,   coverHP, ")
		.append(" if(chargesIP = 0.00, 0, chargesIP) as chargesIP,   coverIP, ")
		.append("    month,   year,  createdDate from  (");

		while (startYear <= currYear) {

			if (startYear == currYear && currMonth == 0) {

				break;

			}

			if (startYear != 2013) {

				queryString.append(" union ");
				queryString
						.append(" select coverHistory_id as coverHistoryId,cust_id as custId,msisdn as msisdn,");
				queryString
						.append(" prev_month_usage as prevMonthUsage,cover_free as coverFree,charges_xl as chargesXL,");

				queryString
						.append(" cover_xl as coverXL , charges_hp as chargesHP , cover_hp as coverHP , charges_ip as chargesIP, cover_ip as coverIP,");
				

				queryString
						.append(" month as month, year as year, created_date as createdDate from cover_history_q1_")
						.append(startYear).append(" where msisdn='");
				queryString.append(msisdn);
				queryString.append("'");

				if (startYear == currYear && currMonth <= 2) {

					break;
				}

				queryString.append(" union ");

				queryString
						.append(" select coverHistory_id as coverHistoryId,cust_id as custId,msisdn as msisdn,");
				queryString
						.append(" prev_month_usage as prevMonthUsage,cover_free as coverFree,charges_xl as chargesXL,");

				queryString
						.append(" cover_xl as coverXL , charges_hp as chargesHP , cover_hp as coverHP, charges_ip chargesIP, cover_ip as coverIP, ");

				queryString
						.append(" month as month, year as year, created_date as createdDate from cover_history_q2_")
						.append(startYear).append(" where msisdn='");
				queryString.append(msisdn);
				queryString.append("'");

				if (startYear == currYear && currMonth <= 5) {

					break;
				}
			}

			if (startYear == 2013) {
				
				queryString
						.append(" select coverHistory_id as coverHistoryId,cust_id as custId,msisdn as msisdn,");
				queryString
						.append(" prev_month_usage as prevMonthUsage,cover_free as coverFree,charges_xl as chargesXL,");

				queryString
						.append(" cover_xl as coverXL , charges_hp as chargesHP , cover_hp as coverHP, charges_ip as chargesIP, cover_ip as coverIP, ");

				queryString
						.append(" month as month, year as year, created_date as createdDate from cover_history_q4_")
						.append(startYear).append(" where msisdn='");
				queryString.append(msisdn);
				queryString.append("'");
			} else {

				queryString.append(" union ");
				
				queryString
						.append(" select coverHistory_id as coverHistoryId,cust_id as custId,msisdn as msisdn,");
				queryString
						.append(" prev_month_usage as prevMonthUsage,cover_free as coverFree,charges_xl as chargesXL,");

				queryString
						.append(" cover_xl as coverXL , charges_hp as chargesHP , cover_hp as coverHP, charges_ip as chargesIP, cover_ip as coverIP, ");

				queryString
						.append(" month as month, year as year, created_date as createdDate from cover_history_q3_")
						.append(startYear).append(" where msisdn='");
				queryString.append(msisdn);
				queryString.append("'");

			}
			if (startYear == currYear && currMonth <= 8) {

				break;
			}

			queryString.append(" union ");
			queryString
					.append(" select coverHistory_id as coverHistoryId,cust_id as custId,msisdn as msisdn,");
			queryString
					.append(" prev_month_usage as prevMonthUsage,cover_free as coverFree,charges_xl as chargesXL,");

			queryString
					.append(" cover_xl as coverXL , charges_hp as chargesHP , cover_hp as coverHP, charges_ip as chargesIP, cover_ip as coverIP,");

			queryString
					.append(" month as month, year as year, created_date as createdDate from cover_history_q4_")
					.append(startYear).append(" where msisdn='");
			queryString.append(msisdn);
			queryString.append("'");

			startYear = startYear + 1;
		}

		queryString.append(" order by createdDate desc ");

		queryString.append(" ) coverHistory");
		logger.debug("sql query : ", queryString.toString());

		try {
			
						
			coverHistoryList = jdbcTemplateCH.query(queryString.toString(),
					new BeanPropertyRowMapper(CoverHistory.class));
		} catch (DataAccessException e) {
			logger.error("Error retrieving cover history details ", e);
			throw new DBException(e);
		}

		logger.info(" coverHistoryList ", coverHistoryList);
		logger.exiting("getCoverHistoryDetails",
				(coverHistoryList == null) ? null : coverHistoryList.size());
		return coverHistoryList;

	}

}
