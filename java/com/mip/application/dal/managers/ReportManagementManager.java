package com.mip.application.dal.managers;

import static com.mip.application.constants.DBObjects.TABLE_CUSTOMER_DETAILS;
import static com.mip.application.constants.DBObjects.TABLE_CUSTOMER_SUBSCRIPTION;
import static com.mip.application.constants.DBObjects.TABLE_DEREGISTERED_CUSTOMERS;
import static com.mip.application.constants.DBObjects.TABLE_PRODUCT_CANCELLATIONS;
import static com.mip.application.constants.DBObjects.TABLE_REPORT_COVERAGE;
import static com.mip.application.constants.DBObjects.TABLE_REPORT_REVENUE;
import static com.mip.application.constants.DBObjects.TABLE_USER_DETAILS;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.ListOrderedMap;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mip.application.model.CustomerSubscription;
import com.mip.application.model.UserDetails;
import com.mip.application.view.CustDeductionReportVO;
import com.mip.application.view.CustomerReportDataVO;
import com.mip.application.view.DeregisteredCustomersVO;
import com.mip.application.view.ReportCoverageVO;
import com.mip.application.view.ReportCustomerVO;
import com.mip.application.view.ReportDailyNewVO;
import com.mip.application.view.ReportDailyVO;
import com.mip.application.view.ReportWeeklyVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;

/**
 * <p>
 * <code>ReportManagementManager</code> contains all the data access layer
 * methods pertaining to Reports model.
 * </p>
 * 
 * <br/>
 * This class extends the <code>DataAccessManager</code> class of MISP
 * framework. </p>
 * 
 * @see com.mip.framework.dao.impls.DataAccessManager
 * 
 * @author T H B S
 * 
 */
public class ReportManagementManager extends
		DataAccessManager<CustomerSubscription, Integer> {

	public ReportManagementManager() {
		super(CustomerSubscription.class);
	}

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			ReportManagementManager.class);
	
	/**
	 * Set inversion of Control for <code>JdbcTemplate</code>.
	 */
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * This method returns the total number of subscribed customers
	 * 
	 * @return <code>List</code> containing total number of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getTotalCustomerCount() throws DBException {

		logger.entering("getTotalCustomerCount");

		List<Integer> userCount = null;

		try {

			Criteria searchCriteria = getSession().createCriteria(
					CustomerSubscription.class);

			ProjectionList projectionsList = Projections.projectionList();
			//projectionsList.add(Projections.rowCount());
			projectionsList.add(Projections.countDistinct("customerDetails.custId"));
			searchCriteria.setProjection(projectionsList);
			userCount = searchCriteria.list();

		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving total customers subscribed",
					e);
			throw new DBException(e);
		}

		logger.exiting("getTotalCustomerCount", userCount);

		return userCount;
	}
	
	/**
	 * This method returns the total number of subscribed customers
	 * 
	 * @return <code>List</code> containing total number of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	public int getTotalCustomerCount(ReportCustomerVO customerReportVO) throws DBException {

		logger.entering("getTotalCustomerCount",customerReportVO);

		Integer userCount = 0;
		int countReq = 1;
		try {
			
			String fromDate = customerReportVO.getFromDate(); 
			fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
					+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

			String toDate = customerReportVO.getToDate();
			toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
					+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
					+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";
			
			StringBuilder queryString = new StringBuilder()
		     .append("CALL CUSTOMER_REPORT(?,?,?,?,?,?)");
			
			Object[] queryParams = { 
					   customerReportVO.getRegMode(),
					   customerReportVO.getRegType(), 
					   customerReportVO.getConfStat(),
					   countReq,
					   fromDate,
					   toDate};
			
			 userCount = jdbcTemplate.queryForInt(queryString.toString(),queryParams);
			 customerReportVO.setTotalCustCount(userCount);
			 
		} catch (DataAccessException e) {
			logger
					.error(
							"Exception occured while retrieving total customers subscribed",
							e);
			throw new DBException(e);
		}

		logger.exiting("getTotalCustomerCount", userCount);

		return userCount;
	}
	
	/**
	 * This method returns the total number of subscribed customers
	 * 
	 * @return <code>List</code> containing total number of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	public int getTotalCustomerCountForRenewedPolicies(
			ReportCustomerVO customerReportVO) throws DBException 
	{
		logger.entering("getTotalCustomerCountForRenewedPolicies",customerReportVO);

		Integer userCount = 0;
		
		try {			
			StringBuilder queryString = new StringBuilder()
		     .append("SELECT COUNT(*) FROM customer_report_renewed_policies");
						
			 userCount = jdbcTemplate.queryForInt(queryString.toString());
			 customerReportVO.setTotalCustCount(userCount);
			 
		} catch (DataAccessException e) {
			logger
					.error(
							"Exception occured while retrieving total customers count",
							e);
			throw new DBException(e);
		}

		logger.exiting("getTotalCustomerCountForRenewedPolicies", userCount);

		return userCount;
	}
	
	/**
	 * This method returns the total number of subscribed customers
	 * 
	 * @return <code>List</code> containing total number of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	public int getTotalCustomerCountForNewPolicies(
			ReportCustomerVO customerReportVO) throws DBException 
	{
		logger.entering("getTotalCustomerCountForNewPolicies",customerReportVO);

		Integer userCount = 0;
		
		try {			
			StringBuilder queryString = new StringBuilder()
		     .append("SELECT COUNT(*) FROM customer_report_new_policies");
						
			 userCount = jdbcTemplate.queryForInt(queryString.toString());
			 customerReportVO.setTotalCustCount(userCount);
			 
		} catch (DataAccessException e) {
			logger
					.error(
							"Exception occured while retrieving total customers count",
							e);
			throw new DBException(e);
		}

		logger.exiting("getTotalCustomerCountForNewPolicies", userCount);

		return userCount;
	}
	
	/**
	 * This methods retrieves a list of subscribed customers starting from
	 * startRow. The number of rows returned is maxRows.
	 * 
	 * @param startRow
	 *            offset of the row from which records will be fetched
	 * 
	 * @param maxRows
	 *            number of rows to fetch
	 * 
	 * @return <code>List</code> of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerSubscription> retrieveSubscribedCustomers(int startRow,
			int maxRows) throws DBException {

		Object[] params = { startRow, maxRows };

		logger.entering("retrieveSubscribedCustomers", params);

		List<CustomerSubscription> subscribedCustList = null;

		try {

			Criteria searchCriteria = getSession().createCriteria(
					CustomerSubscription.class);

			searchCriteria.createCriteria("productDetails", Criteria.LEFT_JOIN);
			searchCriteria.createCriteria("customerDetails",
					Criteria.INNER_JOIN);
			searchCriteria.createCriteria("customerDetails.insuredRelatives",
					Criteria.LEFT_JOIN);

			searchCriteria.setFirstResult(startRow - 1);
			searchCriteria.setMaxResults(maxRows);
			//Changes made on JANUARY 13 As per CR # : NEW_CR_009
			
			searchCriteria.add(Restrictions.eq("confirmed",(byte)1));  
			searchCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			subscribedCustList = searchCriteria.list();			

		} catch (DataAccessException e) {
			logger
					.error(
							"Exception occured while retrieving subscribed customer details.",
							e);
			throw new DBException(e);
		}

		logger.exiting("retrieveSubscribedCustomers");
		return subscribedCustList;
	}
	
	/**
	 * This methods retrieves a list of subscribed customers starting from
	 * startRow. The number of rows returned is maxRows.
	 * 
	 * @param startRow
	 *            offset of the row from which records will be fetched
	 * 
	 * @param maxRows
	 *            number of rows to fetch
	 * 
	 * @return <code>List</code> of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerReportDataVO> retrieveSubscribedCustomersOld(
			int startRow, int maxRows) throws DBException 
 {

		logger.entering("retrieveSubscribedCustomersOld");

		List<CustomerReportDataVO> subscribedCustList = null;

		try {

			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
					.append(" CONCAT(CD.fname,' ', CD.sname) as custName, ")
					.append(" CD.age as 'custAge', ")
					.append(" CONCAT(IRD.fname,' ', IRD.sname) as 'irdName', ")
					.append(" IRD.age as 'irdAge', ")
					.append(" IRD.cust_relationship as 'custRelationship', ")
					.append(" CD.msisdn as 'msisdn', ")
					.append(" CD.cust_id as 'custId', ")
					.append(" CS.reg_by as 'createdBy', ")
					.append(" group_concat(CONCAT(UD.fname,' ', UD.sname) order by cs.product_id SEPARATOR ',') as 'createdByAll', ")
					.append(" CS.reg_date as 'createdDate', ")
					.append(" group_concat(CS.reg_date order by cs.product_id SEPARATOR ',') as 'createdDateAll', ")
					.append(" group_concat(CS.conf_date order by cs.product_id SEPARATOR ',') as 'confDateAll', ")
					.append(" CD.modified_by as 'modifiedBy', ")
					.append(" CD.modified_date as 'modifiedDate', ")
					.append(" PD.product_name as 'productName', ")
					.append(" group_concat(PD.product_name order by cs.product_id SEPARATOR ',') as 'productNameAll', ")
					.append("  CS.earned_cover as 'earnedCover', ")
					.append(" group_concat(ifnull(CS.earned_cover,0) order by cs.product_id SEPARATOR ',') as 'earnedCoverAll', ")
					.append(" CS.cover_charges as 'coverCharges', ")
					.append("  group_concat(ifnull(CS.cover_charges,0) order by cs.product_id SEPARATOR ',') as 'coverChargesAll', ")
					.append(" earned_cover as 'totalCover'  ")
					.append(" FROM CUSTOMER_DETAILS CD ") 
					.append(" LEFT OUTER JOIN INSURED_RELATIVE_DETAILS IRD ON CD.cust_id = IRD.cust_id ") 
					.append(" INNER JOIN CUSTOMER_SUBSCRIPTION CS ON CD.cust_id = CS.cust_id ")
					.append(" INNER JOIN PRODUCT_DETAILS PD ON CS.product_id = PD.product_id ") 
					.append(" INNER JOIN USER_DETAILS UD ON UD.user_id = CS.reg_by ")
					.append(" INNER JOIN (select distinct (cust_id) from CUSTOMER_SUBSCRIPTION) as res ON CS.cust_id = res.cust_id")
					.append(" WHERE ")
					.append(" CS.is_confirmed=1 ") 
					.append(" AND CS.reg_by<>2  ")
					.append(" AND CS.reg_chn_id<>2  ")
					.append(" GROUP BY CS.cust_id ")
					.append(" ORDER BY CS.product_id ");

			StringBuilder queryString = new StringBuilder().append(sqlQuery)
					.append(" limit ").append(startRow - 1).append(" , ")
					.append(maxRows);

			subscribedCustList = getSession()
					.createSQLQuery(queryString.toString())
					.addScalar("custName", Hibernate.STRING)
					.addScalar("custAge", Hibernate.STRING)
					.addScalar("irdName", Hibernate.STRING)
					.addScalar("irdAge", Hibernate.STRING)
					.addScalar("custRelationship", Hibernate.STRING)
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("custId", Hibernate.INTEGER)
					.addScalar("createdBy", Hibernate.entity(UserDetails.class))
					.addScalar("createdByAll", Hibernate.STRING)
					.addScalar("createdDate", Hibernate.TIMESTAMP)
					.addScalar("createdDateAll", Hibernate.STRING)
					.addScalar("confDateAll", Hibernate.STRING)
					.addScalar("modifiedBy",
							Hibernate.entity(UserDetails.class))
					.addScalar("modifiedDate", Hibernate.TIMESTAMP)
					.addScalar("productName", Hibernate.STRING)
					.addScalar("productNameAll", Hibernate.STRING)
					.addScalar("earnedCover", Hibernate.BIG_DECIMAL)
					.addScalar("earnedCoverAll", Hibernate.STRING)
					.addScalar("coverCharges", Hibernate.BIG_DECIMAL)
					.addScalar("coverChargesAll", Hibernate.STRING)
					.addScalar("totalCover", Hibernate.BIG_DECIMAL)
					.setResultTransformer(
							Transformers
									.aliasToBean(CustomerReportDataVO.class))
					.list();
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving subscribed customer details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("retrieveSubscribedCustomersOld");
		return subscribedCustList;

		// }
	}
	
	/**
	 * This methods retrieves a list of subscribed customers starting from
	 * startRow. The number of rows returned is maxRows.
	 * @param reportCustomerVO 
	 * 
	 * @param startRow
	 *            offset of the row from which records will be fetched
	 * 
	 * @param maxRows
	 *            number of rows to fetch
	 * 
	 * @return <code>List</code> of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerReportDataVO> retrieveSubscribedCustomers(
			ReportCustomerVO reportCustomerVO,int startRow, int maxRows) 
					throws DBException 
	{

		logger.entering("retrieveSubscribedCustomers", reportCustomerVO);

		List<String> queryForExecution = null;
		List<CustomerReportDataVO> subscribedCustList = null;
		int countReq=0;
		try {
			if(reportCustomerVO.getRepStyle().equalsIgnoreCase("2")){
				
				String fromDate = reportCustomerVO.getFromDate(); 
				fromDate = DateUtil.getYear(DateUtil.toSQLDate(fromDate)) + "-"
						+ (DateUtil.getMonth(DateUtil.toSQLDate(fromDate)) + 1) + "-"
						+ DateUtil.getDay(DateUtil.toSQLDate(fromDate)) + " 00:00:00";

				String toDate = reportCustomerVO.getToDate();
				toDate = DateUtil.getYear(DateUtil.toSQLDate(toDate)) + "-"
						+ (DateUtil.getMonth(DateUtil.toSQLDate(toDate)) + 1) + "-"
						+ DateUtil.getDay(DateUtil.toSQLDate(toDate)) + " 23:59:59";
				
								
				Query query = super
						.getSession()
						.createSQLQuery(
							"CALL CUSTOMER_REPORT(:regMode, :regType, :confirmationStatus, :countReq, :fromDate, :toDate)")				
							.setParameter("regMode",reportCustomerVO.getRegMode())
							.setParameter("regType",reportCustomerVO.getRegType())
							.setParameter("confirmationStatus",reportCustomerVO.getConfStat())
							.setParameter("countReq",countReq)
							.setParameter("fromDate", fromDate)
							.setParameter("toDate", toDate);
					
					queryForExecution = query.list();
					
					String queryToExecute = queryForExecution.get(0);
					
					StringBuilder queryString = new StringBuilder()
				     .append(queryToExecute)
				     .append(" limit ")
				     .append(startRow-1)
				     .append(" , ")
				     .append(maxRows);
					
					subscribedCustList = getSession().createSQLQuery(queryString.toString())
							.addScalar("custName", Hibernate.STRING)
							.addScalar("custAge", Hibernate.STRING)
							.addScalar("irdName", Hibernate.STRING)
							.addScalar("irdAge", Hibernate.STRING)
							.addScalar("msisdn", Hibernate.STRING)
							.addScalar("custRelationship", Hibernate.STRING)
							.addScalar("custId", Hibernate.INTEGER)
							.addScalar("status", Hibernate.STRING)
							.addScalar("createdBy", Hibernate.entity(UserDetails.class))
							.addScalar("createdByAll", Hibernate.STRING)
							.addScalar("createdDate", Hibernate.TIMESTAMP)
							.addScalar("createdDateAll", Hibernate.STRING)
							.addScalar("confDateAll", Hibernate.STRING)
							.addScalar("modifiedBy", Hibernate.entity(UserDetails.class))
							.addScalar("modifiedDate", Hibernate.TIMESTAMP)
							.addScalar("productName", Hibernate.STRING)
							.addScalar("productNameAll", Hibernate.STRING)
							.addScalar("earnedCover", Hibernate.BIG_DECIMAL)
							.addScalar("earnedCoverAll", Hibernate.STRING)
							.addScalar("coverCharges", Hibernate.BIG_DECIMAL)
							.addScalar("coverChargesAll", Hibernate.STRING)
							.addScalar("totalCover", Hibernate.BIG_DECIMAL)
							.setResultTransformer(
								Transformers.aliasToBean(CustomerReportDataVO.class)).list();
			}
		} catch (DataAccessException e) {
			logger
					.error(
							"Exception occured while retrieving subscribed customer details.",
							e);
			throw new DBException(e);
		}

		logger.exiting("retrieveSubscribedCustomers");
		return subscribedCustList;
	}

	/**
	 * This methods retrieves a list of subscribed customers starting from
	 * startRow. The number of rows returned is maxRows.
	 * @param reportCustomerVO 
	 * 
	 * @param startRow
	 *            offset of the row from which records will be fetched
	 * 
	 * @param maxRows
	 *            number of rows to fetch
	 * 
	 * @return <code>List</code> of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerReportDataVO> retrieveSubscribedCustomersForRenewedPolicies(
			ReportCustomerVO reportCustomerVO,int startRow, int maxRows) 
					throws DBException 
 {

		logger.entering("retrieveSubscribedCustomersForRenewedPolicies",
				reportCustomerVO);

		
		List<CustomerReportDataVO> subscribedCustList = null;
		try {
			
			String query = "SELECT * FROM customer_report_renewed_policies" + " limit " 
			+ (startRow - 1) + "," + maxRows;
			
			
			subscribedCustList = getSession().createSQLQuery(query)
					.addScalar("custName", Hibernate.STRING)
					.addScalar("custAge", Hibernate.STRING)
					.addScalar("irdName", Hibernate.STRING)
					.addScalar("irdAge", Hibernate.STRING)
					.addScalar("custRelationship", Hibernate.STRING)
					.addScalar("custId", Hibernate.INTEGER)
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("status", Hibernate.STRING)
					.addScalar("createdBy", Hibernate.entity(UserDetails.class))
					.addScalar("createdByAll", Hibernate.STRING)
					.addScalar("createdDate", Hibernate.TIMESTAMP)
					.addScalar("createdDateAll", Hibernate.STRING)
					.addScalar("confDateAll", Hibernate.STRING)
					.addScalar("modifiedBy", Hibernate.entity(UserDetails.class))
					.addScalar("modifiedDate", Hibernate.TIMESTAMP)
					.addScalar("productName", Hibernate.STRING)
					.addScalar("productNameAll", Hibernate.STRING)
					.addScalar("earnedCover", Hibernate.BIG_DECIMAL)
					.addScalar("earnedCoverAll", Hibernate.STRING)
					.addScalar("coverCharges", Hibernate.BIG_DECIMAL)
					.addScalar("coverChargesAll", Hibernate.STRING)
					.addScalar("totalCover", Hibernate.BIG_DECIMAL)
					.setResultTransformer(
						Transformers.aliasToBean(CustomerReportDataVO.class)).list();
			
			
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving subscribed customer details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("retrieveSubscribedCustomersForRenewedPolicies");
		return subscribedCustList;
	}
	
	/**
	 * This methods retrieves a list of subscribed customers starting from
	 * startRow. The number of rows returned is maxRows.
	 * @param reportCustomerVO 
	 * 
	 * @param startRow
	 *            offset of the row from which records will be fetched
	 * 
	 * @param maxRows
	 *            number of rows to fetch
	 * 
	 * @return <code>List</code> of subscribed customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<CustomerReportDataVO> retrieveSubscribedCustomersForNewPolicies(
			ReportCustomerVO reportCustomerVO,int startRow, int maxRows) 
					throws DBException 
 {

		logger.entering("retrieveSubscribedCustomersForNewPolicies",
				reportCustomerVO);

		List<CustomerReportDataVO> subscribedCustList = null;
		try {

			String query = "SELECT * FROM customer_report_new_policies" + " limit " 
			+ (startRow - 1) + "," + maxRows;
			
			subscribedCustList = getSession().createSQLQuery(query)
					.addScalar("custName", Hibernate.STRING)
					.addScalar("custAge", Hibernate.STRING)
					.addScalar("irdName", Hibernate.STRING)
					.addScalar("irdAge", Hibernate.STRING)
					.addScalar("custRelationship", Hibernate.STRING)
					.addScalar("custId", Hibernate.INTEGER)
					.addScalar("msisdn", Hibernate.STRING)
					.addScalar("status", Hibernate.STRING)
					.addScalar("createdBy", Hibernate.entity(UserDetails.class))
					.addScalar("createdByAll", Hibernate.STRING)
					.addScalar("createdDate", Hibernate.TIMESTAMP)
					.addScalar("createdDateAll", Hibernate.STRING)
					.addScalar("confDateAll", Hibernate.STRING)
					.addScalar("modifiedBy", Hibernate.entity(UserDetails.class))
					.addScalar("modifiedDate", Hibernate.TIMESTAMP)
					.addScalar("productName", Hibernate.STRING)
					.addScalar("productNameAll", Hibernate.STRING)
					.addScalar("earnedCover", Hibernate.BIG_DECIMAL)
					.addScalar("earnedCoverAll", Hibernate.STRING)
					.addScalar("coverCharges", Hibernate.BIG_DECIMAL)
					.addScalar("coverChargesAll", Hibernate.STRING)
					.addScalar("totalCover", Hibernate.BIG_DECIMAL)
					.setResultTransformer(
						Transformers.aliasToBean(CustomerReportDataVO.class)).list();
			
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving subscribed customer details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("retrieveSubscribedCustomersForNewPolicies");
		return subscribedCustList;
	}
	
	/**
	 * This method returns the usage total(free cover,paid cover, total cover)
	 * usage count of all subscribed customers
	 * 
	 * @return <code>CustomerSubscription</code> containing total(free
	 *         cover,paid cover, total cover) usage count of all subscribed
	 *         customers
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public CustomerSubscription getSubscribeCustomersUsageTotal()
			throws DBException {

		logger.entering("getSubscribeCustomersUsageTotal");

		List<Object[]> subCustUsageTotalList = null;

		CustomerSubscription customerSubs = null;

		try {

			Criteria searchCriteria = getSession().createCriteria(
					CustomerSubscription.class);

			ProjectionList projectionsList = Projections.projectionList();
			projectionsList.add(Projections.sum("earnedCover"));
			projectionsList.add(Projections.sum("coverCharges"));
			projectionsList.add(Projections.sum("prevMonthUsage"));
			projectionsList.add(Projections.countDistinct("customerDetails.custId"));
			//Changes made on JANUARY 13 As per CR # : NEW_CR_009
			searchCriteria.add(Restrictions.eq("confirmed",(byte)1));  
			searchCriteria.setProjection(projectionsList);
			subCustUsageTotalList = searchCriteria.list();

			if (subCustUsageTotalList != null
					&& !subCustUsageTotalList.isEmpty()) {

				Object[] object = (Object[]) subCustUsageTotalList.get(0);

				customerSubs = new CustomerSubscription();
				customerSubs.setEarnedCover((BigDecimal) object[0]);
				customerSubs.setCoverCharges((BigDecimal) object[1]);
				customerSubs.setPrevMonthUsage((BigDecimal) object[2]);
				customerSubs.setTotalCustomerCount((Integer) object[3]);

				if (customerSubs.getTotalCustomerCount() == 0) {
					return null;
				}

			}

		} catch (DataAccessException e) {
			logger
					.error(
							"Exception occured while retrieving subscribed customer details.",
							e);
			throw new DBException(e);
		}

		logger.exiting("getSubscribeCustomersUsageTotal", customerSubs);

		return customerSubs;
	}

	/**
	 * This method retrieves the list of daily report for given date
	 * 
	 * <table border=1>
	 * <th colspan=2>Change Request</th>
	 * <tr><td>Q3 CR </td><td>Request to add extra columns in Agent Report.</td></tr>
	 * </table> 
	 * @param date <code>Date</code> for which the records will be fetched
	 * 
	 * @return <code>List</code> of daily report
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDailyVO> getReportForDate(Date date) throws DBException {
		logger.entering("getReportForDate", date);
		
		List<ReportDailyVO> dailyDataList = null;
		try {
			/**
			 * Query modified for agent report changes.
			 */
			StringBuilder sqlQuery = new StringBuilder(" SELECT ud.user_uid userUid, ")
			.append(" COUNT(IF(cs.product_id=1,1,NULL)) regFreeTotal, ")
			.append(" COUNT(IF(cs.product_id=1 AND cs.reg_chn_id=1,1,NULL)) regFreeLaptop, ")
			.append(" COUNT(IF(cs.product_id=1 AND (cs.reg_chn_id=2 OR cs.reg_chn_id=3),1,NULL)) regFreeHandset, ")
			.append(" COUNT(IF(cs.product_id=1 AND cs.is_confirmed=1,1,NULL)) confirmFree, ")
			.append(" ROUND((COUNT(IF(cs.product_id=1 AND cs.is_confirmed = 1,1,NULL))/COUNT(cd.cust_id)*100),2) avgFreeConfirm  ")
			.append(" FROM ").append(TABLE_CUSTOMER_DETAILS)
			.append(" cd ").append(" JOIN ").append(TABLE_CUSTOMER_SUBSCRIPTION)
			.append(" cs ON cs.cust_id=cd.cust_id ")
			.append(" LEFT OUTER JOIN ")
			.append(TABLE_USER_DETAILS).append(" ud ON ")
			.append(" cs.reg_by=ud.user_id ")
			.append(" WHERE YEAR(cs.reg_date)=").append(DateUtil.getYear(date))
			.append(" AND MONTH(cs.reg_date)=").append(DateUtil.getMonth(date)+1)
			.append(" AND DAY(cs.reg_date)=").append(DateUtil.getDay(date))
			.append(" GROUP BY ud.user_uid ")
			.append(" ORDER BY ud.user_id");
			
			dailyDataList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("userUid", Hibernate.STRING)
					.addScalar("regFreeTotal", Hibernate.INTEGER)
					.addScalar("regFreeLaptop", Hibernate.INTEGER)
					.addScalar("regFreeHandset", Hibernate.INTEGER)
					.addScalar("confirmFree", Hibernate.INTEGER)
					.addScalar("avgFreeConfirm", Hibernate.FLOAT)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyVO.class)).list();
			
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving agent report details.",
					e);
			throw new DBException(e);
		}
		logger.exiting("getReportForDate");
		return dailyDataList;
	}

	/**
	 * This method retrieves the list of daily report for given date
	 * 
	 * <table border=1>
	 * <th colspan=2>Change Request</th>
	 * <tr><td>Q3 CR </td><td>Request to add extra columns in Agent Report.</td></tr>
	 * </table> 
	 * @param date <code>Date</code> for which the records will be fetched
	 * 
	 * @return <code>List</code> of daily report
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDailyNewVO> getDetailedReportForDate(Date date) throws DBException {
		logger.entering("getDetailedReportForDate", date);
		
		List<ReportDailyNewVO> dailyDataList = null;
		try {
			/**
			 * Query modified for agent report changes.
			 */
			StringBuilder sqlQuery = new StringBuilder(" SELECT ud.user_uid userUid, ")
				.append(" COUNT(IF(cs.product_id=2, 1, NULL)) registeredXL, ")
				.append(" COUNT(IF(cs.product_id=2 AND cs.is_confirmed=1,1,NULL)) confirmedXL, ")
				.append(" COUNT(IF(cs.product_id=3, 1, NULL)) registeredHP, ")
				.append(" COUNT(IF(cs.product_id=3 AND cs.is_confirmed=1,1,NULL)) confirmedHP, ")
				.append(" COUNT(IF(cs.product_id=4, 1, NULL)) registeredIP, ")
				.append(" COUNT(IF(cs.product_id=4 AND cs.is_confirmed=1,1,NULL)) confirmedIP ")
				.append(" FROM ").append(TABLE_CUSTOMER_SUBSCRIPTION)
				.append(" cs ").append("LEFT OUTER JOIN ")
				.append(TABLE_USER_DETAILS).append(" ud ON ")
				.append(" cs.reg_by=ud.user_id ")
				.append(" WHERE YEAR(cs.reg_date)=").append(DateUtil.getYear(date))
				.append(" AND MONTH(cs.reg_date)=").append(DateUtil.getMonth(date)+1)
				.append(" AND DAY(cs.reg_date)=").append(DateUtil.getDay(date))
				.append(" AND cs.is_reactivated=0 ")
				.append(" GROUP BY ud.user_uid ")
				.append(" ORDER BY ud.user_id");
			
			dailyDataList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("userUid", Hibernate.STRING)
					.addScalar("registeredXL", Hibernate.INTEGER)
					.addScalar("confirmedXL", Hibernate.INTEGER)
					.addScalar("registeredHP", Hibernate.INTEGER)
					.addScalar("confirmedHP", Hibernate.INTEGER)
					.addScalar("registeredIP", Hibernate.INTEGER)
					.addScalar("confirmedIP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class)).list();
			
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving agent report details.",
					e);
			throw new DBException(e);
		}
		logger.exiting("getDetailedReportForDate");
		return dailyDataList;
	}
	
	/**
	 * This method retrieves the list of daily report for given duration
	 * 
	 * <table border=1>
	 * <th colspan=2>Change Request</th>
	 * <tr><td>Q3 CR </td><td>Request to add extra columns in Agent Report.</td></tr>
	 * </table> 
	 * @param date <code>Date</code> for which the records will be fetched
	 * 
	 * @return <code>List</code> of daily report
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ReportDailyNewVO> getDetailedReportForDuration(
			Date fromDate, Date toDate) throws DBException 
	{
		logger.entering("getDetailedReportForDuration", fromDate, toDate);
		
		Date fromDateSql = DateUtil.toSQLDate(DateUtil.toDateString(fromDate));
		Date toDateSql = DateUtil.toSQLDate(DateUtil.toDateString(toDate));
		List<ReportDailyNewVO> durationDataList = null;
		
		Map<String, ReportDailyNewVO> durationDataMap = 
				new HashMap<String, ReportDailyNewVO>();
		
		try {
			/**
			 * Query modified for agent report changes.
			 */
			StringBuilder sqlQuery = new StringBuilder(" SELECT ud.user_uid userUid, ")
				.append(" COUNT(IF(cs.product_id=1  AND cs.is_reactivated=0 AND cs.reg_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ").append(",1,NULL)) registeredFree, ")
				.append(" COUNT(IF(cs.product_id=1 AND cs.is_confirmed=1  AND cs.is_reactivated=0 AND cs.conf_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ").append(",1,NULL)) confirmedFree, ")
				.append(" COUNT(IF(cs.product_id=2   AND cs.is_reactivated=0 AND cs.reg_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ").append(",1,NULL)) registeredXL, ")
				.append(" COUNT(IF(cs.product_id=2 AND cs.is_confirmed=1  AND cs.is_reactivated=0 AND cs.conf_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ").append(",1,NULL)) confirmedXL, ")
				.append(" COUNT(IF(cs.product_id=3   AND cs.is_reactivated=0 AND cs.reg_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ").append(",1,NULL)) registeredHP, ")
				.append(" COUNT(IF(cs.product_id=3 AND cs.is_confirmed=1  AND cs.is_reactivated=0 AND cs.conf_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ").append(",1,NULL)) confirmedHP , ")
				.append(" COUNT(IF(cs.product_id=4   AND cs.is_reactivated=0 AND cs.reg_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ").append(",1,NULL)) registeredIP, ")
				.append(" COUNT(IF(cs.product_id=4 AND cs.is_confirmed=1 AND cs.is_reactivated=0  AND cs.conf_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ").append(",1,NULL)) confirmedIP ")
				.append(" FROM ").append(TABLE_CUSTOMER_SUBSCRIPTION)
				.append(" cs ").append("LEFT OUTER JOIN ")
				.append(TABLE_USER_DETAILS).append(" ud ON ")
				.append(" cs.reg_by=ud.user_id ")
//				.append(" WHERE date(cs.conf_date) BETWEEN '").append(fromDateSql).append(" 00:00:00")
//				.append("' AND '").append(toDateSql).append(" 23:59:59' ")
				.append(" GROUP BY ud.user_uid ")
				.append(" ORDER BY ud.user_id");
			
			durationDataList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("userUid", Hibernate.STRING)
					.addScalar("registeredFree", Hibernate.INTEGER)
					.addScalar("confirmedFree", Hibernate.INTEGER)
					.addScalar("registeredXL", Hibernate.INTEGER)
					.addScalar("confirmedXL", Hibernate.INTEGER)
					.addScalar("registeredHP", Hibernate.INTEGER)
					.addScalar("confirmedHP", Hibernate.INTEGER)
					.addScalar("registeredIP", Hibernate.INTEGER)
					.addScalar("confirmedIP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class)).list();
			
			Iterator<ReportDailyNewVO> itr = durationDataList.iterator();
			
			while (itr.hasNext()) {
				ReportDailyNewVO reportDailyNewVO = itr.next();
				durationDataMap.put(reportDailyNewVO.getUserUid(), reportDailyNewVO);
			}
			
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving agent report details.",
					e);
			throw new DBException(e);
		}
		logger.exiting("getDetailedReportForDuration");
		return durationDataMap;
	}
	
	/**
	 * This method retrieves the list of daily report for given duration for deduction
	 * @param date <code>Date</code> for which the records will be fetched
	 * 
	 * @return <code>List</code> of deduction report
	 * 
	 * @throws DBException
	 *             If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public Map<String, ReportDailyNewVO> getDetailedReportForDeduction(
			Date fromDate, Date toDate,Date confDateRange) throws DBException 
	{
		logger.entering("getDetailedReportForDeduction", fromDate, toDate);
		
		
		Date fromDateSql = DateUtil.toSQLDate(DateUtil.toDateString(fromDate));
		Date toDateSql = DateUtil.toSQLDate(DateUtil.toDateString(toDate));
		Date confDateRangeSql = DateUtil.toSQLDate(DateUtil.toDateString(confDateRange));
		
		List<ReportDailyNewVO> deductionDataList = null;
		
		Map<String, ReportDailyNewVO> deductionDataMap = 
				new HashMap<String, ReportDailyNewVO>();
		
		try {
			
			/*DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
			Date date = formatter.parse("01/29/02");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);*/
			/**
			 * Query modified for agent report changes.
			 */
			StringBuilder sqlQuery = new StringBuilder(" SELECT ud.user_uid userUid, ")
				.append(" COUNT(IF(cs.product_id=2,1,NULL)) deductedXL, ")
				.append(" COUNT(IF(cs.product_id=3,1,NULL)) deductedHP, ")
				.append(" COUNT(IF(cs.product_id=4,1,NULL)) deductedIP, ")
				.append(" COUNT(IF(cs.product_id in (2,3),1,NULL)) deductedXLandHP ,")
				.append(" COUNT(IF(cs.product_id in (2,4),1,NULL)) deductedXLandIP")
				.append(" FROM ").append(TABLE_CUSTOMER_SUBSCRIPTION)
				.append(" cs ").append("LEFT OUTER JOIN ")
				.append(TABLE_USER_DETAILS).append(" ud ON ")
				.append(" cs.reg_by=ud.user_id ")
				.append(" WHERE is_confirmed=1 AND first_ded_date BETWEEN '").append(fromDateSql).append(" 00:00:00")
				.append("' AND '").append(toDateSql).append(" 23:59:59' ")
				.append(" AND cs.conf_date >= '").append(confDateRangeSql).append(" 00:00:00' ")
				.append(" GROUP BY ud.user_uid ")
				.append(" ORDER BY ud.user_id");
			
			deductionDataList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("userUid", Hibernate.STRING)
					.addScalar("deductedXL", Hibernate.INTEGER)
					.addScalar("deductedHP", Hibernate.INTEGER)
					.addScalar("deductedIP", Hibernate.INTEGER)
					.addScalar("deductedXLandHP", Hibernate.INTEGER)
					.addScalar("deductedXLandIP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class)).list();
			
			Iterator<ReportDailyNewVO> itr = deductionDataList.iterator();
			
			while (itr.hasNext()) {
				ReportDailyNewVO reportDailyNewVO = itr.next();
				deductionDataMap.put(reportDailyNewVO.getUserUid(), reportDailyNewVO);
			}
			
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving agent report details.",
					e);
			throw new DBException(e);
		}
		logger.exiting("getDetailedReportForDeduction");
		return deductionDataMap;
	}
	
	/**
	 * This method retrieves the weekly tigo report for given date range.
	 * 
	 * @param reportWeeklyVO
	 * 			{@link ReportWeeklyVO} object containing the date range
	 * 
	 * @return <code>Map</code> of weekly tigo report.
	 * 
	 * @throws DBException
	 * 			If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings({ "unchecked" })
	public Map<String, String> getWeeklyReport(
			ReportWeeklyVO reportWeeklyVO) throws DBException {
		logger.entering("getWeeklyReport");
		
		Map<String,String> weeklyReportMap = new HashMap<String, String>();
		
		try{
			java.sql.Date fromDate = DateUtil.toSQLDate(reportWeeklyVO.getFromDate());
			java.sql.Date toDate = DateUtil.toSQLDate(reportWeeklyVO.getRefDate());
			
			StringBuilder queryString = new StringBuilder();
			if(reportWeeklyVO.getRegistrationType().equals("1"))
				queryString.append("SELECT (SELECT count(is_offer_subscribed) ")
					.append("FROM customer_subscription ")
					.append("WHERE is_offer_subscribed=1) AS totalRegs, ")
					.append("(SELECT COUNT(*) FROM customer_details cd, customer_subscription cs ")
					.append("WHERE cd.cust_id=cs.cust_id AND cs.is_offer_subscribed=1 ")
					.append("AND cd.created_date BETWEEN '")
					.append(fromDate).append(" 00:00:00").append("' AND '")
					.append(toDate).append(" 23:59:59").append("'").append(" AS periodRegs, ");
			else
				queryString.append("SELECT count(cust_id) AS totalRegs, ")
					.append(" (SELECT COUNT(*) FROM customer_details ")
					.append("WHERE created_date BETWEEN '")
					.append(fromDate).append(" 00:00:00").append("' AND '")
					.append(toDate).append(" 23:59:59").append("')").append(" AS periodRegs, ");
			
				queryString.append("(SELECT COUNT(distinct created_by, ")
					.append("DATE_FORMAT(created_date,'%Y-%m-%d')) FROM customer_details WHERE ")
					.append("created_date BETWEEN '")
					.append(fromDate).append(" 00:00:00").append("' AND '")
					.append(toDate).append(" 23:59:59").append("')").append(" AS manDays, ")
					.append("(SELECT COUNT(user_id) from user_details where role_id=2) AS cscAgents, ")
					.append("(SELECT COUNT(user_id) from user_details where role_id=3) AS mobileAgents ");

			if(reportWeeklyVO.getRegistrationType().equals("1"))
				queryString.append("FROM dual");
			else
				queryString.append("FROM ").append(TABLE_CUSTOMER_DETAILS);
						
			List<ListOrderedMap> valueList = jdbcTemplate.queryForList(
					queryString.toString());
			String totalRegs = String.valueOf(
					valueList.get(0).get("totalRegs"));
			String manDays = String.valueOf(
					valueList.get(0).get("manDays"));
			String cscAgents = String.valueOf(
					valueList.get(0).get("cscAgents"));
			String mobileAgents = String.valueOf(
					valueList.get(0).get("mobileAgents"));
			String periodRegs = String.valueOf(
					valueList.get(0).get("periodRegs"));
			
			String avgRegs = Float.valueOf(periodRegs)/Float.valueOf(manDays)+"";

			weeklyReportMap.put(PlatformConstants.TOTAL_REGISTRATION_COUNT, totalRegs);
			weeklyReportMap.put(PlatformConstants.MAN_DAYS, manDays);
			weeklyReportMap.put(PlatformConstants.CSC_AGENTS_COUNT, cscAgents);
			weeklyReportMap.put(PlatformConstants.MOBILE_AGENTS_COUNT, mobileAgents);
			weeklyReportMap.put(PlatformConstants.PERIOD_REGISTRATION_COUNT, periodRegs);
			weeklyReportMap.put(PlatformConstants.AVERAGE_REGISTRATION_COUNT, avgRegs);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while retrieving Weekly Tigo Report details " +
					"from DB.", e);
			throw new DBException(e);
		}
		
		logger.exiting("getWeeklyReport", weeklyReportMap);
		return weeklyReportMap;
	}

	/**
	 * This method retrieves the revenue report for given date range.
	 * @param month 
	 * @param year 
	 * 
	 * @return <code>Map</code> of revenue report.
	 * 
	 * @throws DBException
	 * 			If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public Map<String,String> getRevenueReport(String year, String month) throws DBException {
		logger.entering("getRevenueReport");
		
		Map<String,String> revenueReportMap = null;
		
		try{			
			StringBuilder queryString = new StringBuilder("SELECT ")
				.append("freemium_revenue as freemiumRevenue, ")
				.append("total_revenue as totalRevenue, ")
				.append("commission_cost as commissionCost, ")
				.append("free_premium_cost as freePremiumCost, ")
				.append("freemium_premium_cost as freemiumPremiumCost, ")
				.append("total_premium_cost as totalPremiumCost, ")
				.append("total_cost as totalCost, ")
				.append("profit as profit ")
				.append("FROM ").append(TABLE_REPORT_REVENUE)
				.append(" WHERE rep_year=").append(year)
				.append(" AND rep_month=").append(month);
						
			List<ListOrderedMap> valueList = jdbcTemplate.queryForList(
					queryString.toString());
			
			if(valueList.size() > 0) {
				String freemiumRevenue = String.valueOf(valueList.get(0).get(
						"freemiumRevenue"));
				String totalRevenue = String.valueOf(valueList.get(0).get(
						"totalRevenue"));
				String commissionCost = String.valueOf(valueList.get(0).get(
						"commissionCost"));
				String freePremiumCost = String.valueOf(valueList.get(0).get(
						"freePremiumCost"));
				String freemiumPremiumCost = String.valueOf(valueList.get(0).get(
						"freemiumPremiumCost"));
				String totalPremiumCost = String.valueOf(valueList.get(0).get(
						"totalPremiumCost"));
				String totalCost = String
						.valueOf(valueList.get(0).get("totalCost"));
				String profit = String.valueOf(valueList.get(0).get("profit"));
						
				revenueReportMap = new HashMap<String, String>();
				revenueReportMap.put(PlatformConstants.FREEMIUM_REVENUE, freemiumRevenue);
				revenueReportMap.put(PlatformConstants.TOTAL_REVENUE, totalRevenue);
				revenueReportMap.put(PlatformConstants.COMMISSION_COST, commissionCost);
				revenueReportMap.put(PlatformConstants.FREE_PREMIUM_COST, freePremiumCost);
				revenueReportMap.put(PlatformConstants.FREEMIUM_PREMIUM_COST, freemiumPremiumCost);
				revenueReportMap.put(PlatformConstants.TOTAL_PREMIUM_COST, totalPremiumCost);
				revenueReportMap.put(PlatformConstants.TOTAL_COST, totalCost);
				revenueReportMap.put(PlatformConstants.PROFIT, profit);
			}
			
		}catch(DataAccessException e){
			logger.error("Exception occured while retrieving Revenue Report details " +
					"from DB.", e);
			throw new DBException(e);
		}
		
		logger.exiting("getRevenueReport", revenueReportMap);
		return revenueReportMap;
	}

	/**
	 * This method retrieves the coverage report for the given date.
	 * 
	 * @param month <String> holds the current month
	 * @param year <String> holds the current year
	 * 
	 * @return <code>List<ReportCoverageVO></code> of coverage report.
	 * 
	 * @throws DBException
	 * 			If any {@link DataAccessException} occurs
	 */
	@SuppressWarnings("unchecked")
	public List<ReportCoverageVO> getCoverageReportData(String year, String month)
			throws DBException {
		Object[] params = {year, month};
		logger.entering("getCoverageReportData", params);
		ArrayList<ReportCoverageVO> coverageReportData = null;
		
		try {
			
			StringBuilder selectSql = new StringBuilder("SELECT ")
				.append("branch_id branchId, ")
				.append("zero_inactive zeroInactive, ")
				.append("cover_1 cover1, ")
				.append("count_1 count1, ")
				.append("percent_1 percent1, ")
				.append("cover_2 cover2, ")
				.append("count_2 count2, ")
				.append("percent_2 percent2, ")
				.append("cover_3 cover3, ")
				.append("count_3 count3, ")
				.append("percent_3 percent3, ")
				.append("cover_4 cover4, ")
				.append("count_4 count4, ")
				.append("percent_4 percent4, ")
				.append("cover_5 cover5, ")
				.append("count_5 count5, ")
				.append("percent_5 percent5, ")
				.append("cover_6 cover6, ")
				.append("count_6 count6, ")
				.append("percent_6 percent6, ")
				.append("cover_7 cover7, ")
				.append("count_7 count7, ")
				.append("percent_7 percent7, ")
				.append("cover_8 cover8, ")
				.append("count_8 count8, ")
				.append("percent_8 percent8	")
				.append("FROM ").append(TABLE_REPORT_COVERAGE)
				.append(" WHERE rep_year=").append(year)
				.append(" AND rep_month=").append(month)
				.append(" ORDER BY 1");
			
			coverageReportData = (ArrayList<ReportCoverageVO>) getSession()
				.createSQLQuery(selectSql.toString())
				.addScalar("branchId", Hibernate.INTEGER)
				.addScalar("zeroInactive", Hibernate.BIG_INTEGER)
				.addScalar("cover1", Hibernate.INTEGER)
				.addScalar("count1", Hibernate.BIG_INTEGER)
				.addScalar("percent1", Hibernate.BIG_DECIMAL)
				.addScalar("cover2", Hibernate.INTEGER)
				.addScalar("count2", Hibernate.BIG_INTEGER)
				.addScalar("percent2", Hibernate.BIG_DECIMAL)
				.addScalar("cover3", Hibernate.INTEGER)
				.addScalar("count3", Hibernate.BIG_INTEGER)
				.addScalar("percent3", Hibernate.BIG_DECIMAL)
				.addScalar("cover4", Hibernate.INTEGER)
				.addScalar("count4", Hibernate.BIG_INTEGER)
				.addScalar("percent4", Hibernate.BIG_DECIMAL)
				.addScalar("cover5", Hibernate.INTEGER)
				.addScalar("count5", Hibernate.BIG_INTEGER)
				.addScalar("percent5", Hibernate.BIG_DECIMAL)
				.addScalar("cover6", Hibernate.INTEGER)
				.addScalar("count6", Hibernate.BIG_INTEGER)
				.addScalar("percent6", Hibernate.BIG_DECIMAL)
				.addScalar("cover7", Hibernate.INTEGER)
				.addScalar("count7", Hibernate.BIG_INTEGER)
				.addScalar("percent7", Hibernate.BIG_DECIMAL)
				.addScalar("cover8", Hibernate.INTEGER)
				.addScalar("count8", Hibernate.BIG_INTEGER)
				.addScalar("percent8", Hibernate.BIG_DECIMAL)
				.setResultTransformer(
						Transformers.aliasToBean(ReportCoverageVO.class))
				.list();

		}
		catch(DataAccessException e){
			logger.error("Exception occured while retrieving Coverage Report details " +
					"from DB.", e);
			throw new DBException(e);
		}
		
		logger.exiting("getCoverageReportData");
		return coverageReportData.size() > 0 ? coverageReportData : null;
	}

	/**
	 * This method will retrieve the XL deregistered customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDailyNewVO> getConfirmedXLandHP(String fromDate,
			String toDate) throws DBException {
		Object[] params = { fromDate, toDate };
		logger.entering("getConfirmedXLandHP", params);

		List<ReportDailyNewVO> confirmedXLandHPList = null;

		try {

			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
					.append(" DATE_FORMAT(CS.reg_date,'%d/%m/%Y') AS regDate, ")
					.append(" CS.reg_by AS userId, ")
					.append(" COUNT(*) AS confirmedXLandHP ")
					.append(" FROM ")
					.append(TABLE_CUSTOMER_SUBSCRIPTION)
					.append(" CS ")
					.append(" INNER JOIN ")
					.append(" (SELECT cust_id, reg_by, reg_date FROM customer_subscription WHERE product_id=3 and is_reactivated=0 and date(reg_date) BETWEEN '")
					.append(fromDate)
					.append("' AND '")
					.append(toDate)
					.append("' and is_confirmed=1) CSS")
					.append(" ON CS.cust_id=CSS.cust_id and CSS.reg_by=CS.reg_by and date(CS.reg_date)=date(CSS.reg_date) ")
					.append(" WHERE CS.product_id=2 and date(CS.reg_date) ")
					.append(" BETWEEN '").append(fromDate).append("' AND '")
					.append(toDate).append("' and CS.is_confirmed=1 ")
					.append(" AND CS.is_reactivated=0")
					.append(" GROUP BY 1,2");

			confirmedXLandHPList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("regDate", Hibernate.STRING)
					.addScalar("userId", Hibernate.INTEGER)
					.addScalar("confirmedXLandHP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class))
					.list();
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("getConfirmedXLandHP");
		return confirmedXLandHPList;
	}
	/**
	 * This method will retrieve the XL and IP confirmed customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	
	@SuppressWarnings("unchecked")
	public List<ReportDailyNewVO> getConfirmedXLandIP(String fromDate,
			String toDate) throws DBException {
		Object[] params = { fromDate, toDate };
		logger.entering("getConfirmedXLandIP", params);

		List<ReportDailyNewVO> confirmedXLandIPList = null;

		try {

			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
					.append(" DATE_FORMAT(CS.reg_date,'%d/%m/%Y') AS regDate, ")
					.append(" CS.reg_by AS userId, ")
					.append(" COUNT(*) AS confirmedXLandIP ")
					.append(" FROM ")
					.append(TABLE_CUSTOMER_SUBSCRIPTION)
					.append(" CS ")
					.append(" INNER JOIN ")
					.append(" (SELECT cust_id, reg_by, reg_date FROM customer_subscription WHERE product_id=4 and is_reactivated=0 and date(reg_date) BETWEEN '")
					.append(fromDate)
					.append("' AND '")
					.append(toDate)
					.append("' and is_confirmed=1) CSS")
					.append(" ON CS.cust_id=CSS.cust_id and CSS.reg_by=CS.reg_by and date(CS.reg_date)=date(CSS.reg_date) ")
					.append(" WHERE CS.product_id=2 and date(CS.reg_date) ")
					.append(" BETWEEN '").append(fromDate).append("' AND '")
					.append(toDate).append("' and CS.is_confirmed=1 ")
					.append(" AND CS.is_reactivated=0")
					.append(" GROUP BY 1,2");

			confirmedXLandIPList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("regDate", Hibernate.STRING)
					.addScalar("userId", Hibernate.INTEGER)
					.addScalar("confirmedXLandIP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class))
					.list();
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("getConfirmedXLandIP");
		return confirmedXLandIPList;
	}
	
	
	
	/**
	 * This method will retrieve the XL deregistered customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, ReportDailyNewVO> getRegisteredXLandHPWithUserDetails(
			String fromDate, String toDate) throws DBException 
	{
		Object[] params = { fromDate, toDate };
		logger.entering("getRegisteredXLandHPWithUserDetails", params);

		List<ReportDailyNewVO> registeredXLandHPList = null;
		Map<Integer, ReportDailyNewVO> registeredXLandHPMap = 
				new HashMap<Integer, ReportDailyNewVO>();
		
		try {

			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
					.append(" CS.reg_by AS userId, ")
					.append(" COUNT(*) AS registeredXLandHP ")
					.append(" FROM ")
					.append(TABLE_CUSTOMER_SUBSCRIPTION)
					.append(" CS ")
					.append(" INNER JOIN ")
					.append(" (SELECT cust_id, reg_by, reg_date FROM customer_subscription WHERE product_id=3 and is_reactivated=0 and date(reg_date) BETWEEN '")
					.append(fromDate)
					.append("' AND '")
					.append(toDate)
					.append("' ) CSS")
					.append(" ON CS.cust_id=CSS.cust_id and CSS.reg_by=CS.reg_by and date(CS.reg_date)=date(CSS.reg_date) ")
					.append(" WHERE CS.product_id=2 and CS.is_reactivated=0 ")
					.append(" and date(CS.reg_date) ")
					.append(" BETWEEN '").append(fromDate).append("' AND '")
					.append(toDate).append("'")
					.append(" GROUP BY 1");
			
			registeredXLandHPList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("userId", Hibernate.INTEGER)
					.addScalar("registeredXLandHP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class))
					.list();
			
			Iterator<ReportDailyNewVO> itr = registeredXLandHPList.iterator();
			
			while (itr.hasNext()) {
				ReportDailyNewVO reportDailyNewVO = itr.next();
				registeredXLandHPMap.put(reportDailyNewVO.getUserId(), 
						reportDailyNewVO);
			}
			
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("getRegisteredXLandHPWithUserDetails");
		return registeredXLandHPMap;
	}
	
	/**
	 * This method will retrieve the XL and IP registered customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, ReportDailyNewVO> getRegisteredXLandIPWithUserDetails(
			String fromDate, String toDate) throws DBException 
	{
		Object[] params = { fromDate, toDate };
		logger.entering("getRegisteredXLandIPWithUserDetails", params);

		List<ReportDailyNewVO> registeredXLandIPList = null;
		Map<Integer, ReportDailyNewVO> registeredXLandIPMap = 
				new HashMap<Integer, ReportDailyNewVO>();
		
		try {

			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
					.append(" CS.reg_by AS userId, ")
					.append(" COUNT(*) AS registeredXLandIP ")
					.append(" FROM ")
					.append(TABLE_CUSTOMER_SUBSCRIPTION)
					.append(" CS ")
					.append(" INNER JOIN ")
					.append(" (SELECT cust_id, reg_by, reg_date FROM customer_subscription WHERE product_id=4 and is_reactivated=0 and date(reg_date) BETWEEN '")
					.append(fromDate)
					.append("' AND '")
					.append(toDate)
					.append("' ) CSS")
					.append(" ON CS.cust_id=CSS.cust_id and CSS.reg_by=CS.reg_by and date(CS.reg_date)=date(CSS.reg_date) ")
					.append(" WHERE CS.product_id=2 and CS.is_reactivated=0") 
					.append(" and date(CS.reg_date) ")
					.append(" BETWEEN '").append(fromDate).append("' AND '")
					.append(toDate).append("'")
					.append(" GROUP BY 1");
			
			registeredXLandIPList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("userId", Hibernate.INTEGER)
					.addScalar("registeredXLandIP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class))
					.list();
			
			Iterator<ReportDailyNewVO> itr = registeredXLandIPList.iterator();
			
			while (itr.hasNext()) {
				ReportDailyNewVO reportDailyNewVO = itr.next();
				registeredXLandIPMap.put(reportDailyNewVO.getUserId(), 
						reportDailyNewVO);
			}
			
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving registered XL and IP customers details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("getRegisteredXLandIPWithUserDetails");
		return registeredXLandIPMap;
	}
	
	
	/**
	 * This method will retrieve the XL deregistered customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, ReportDailyNewVO> getConfirmedXLandHPWithUserDetails(
			String fromDate, String toDate) throws DBException 
	{
		Object[] params = { fromDate, toDate };
		logger.entering("getConfirmedXLandHPWithUserDetails", params);

		List<ReportDailyNewVO> confirmedXLandHPList = null;
		Map<Integer, ReportDailyNewVO> confirmedXLandHPMap = 
				new HashMap<Integer, ReportDailyNewVO>();
		
		try {

			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
					.append(" CS.reg_by AS userId, ")
					.append(" COUNT(*) AS confirmedXLandHP ")
					.append(" FROM ")
					.append(TABLE_CUSTOMER_SUBSCRIPTION)
					.append(" CS ")
					.append(" INNER JOIN ")
					.append(" (SELECT cust_id, reg_by, conf_date FROM customer_subscription WHERE product_id=3 and is_reactivated=0 and date(conf_date) BETWEEN '")
					.append(fromDate)
					.append("' AND '")
					.append(toDate)
					.append("' and is_confirmed=1) CSS")
					.append(" ON CS.cust_id=CSS.cust_id and CSS.reg_by=CS.reg_by and date(CS.conf_date)=date(CSS.conf_date) ")
					.append(" WHERE CS.product_id=2 and CS.is_reactivated=0")
					.append(" and date(CS.conf_date) ")
					.append(" BETWEEN '").append(fromDate).append("' AND '")
					.append(toDate).append("' and CS.is_confirmed=1 ")
					.append(" GROUP BY 1");
			
			confirmedXLandHPList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("userId", Hibernate.INTEGER)
					.addScalar("confirmedXLandHP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class))
					.list();
			
			Iterator<ReportDailyNewVO> itr = confirmedXLandHPList.iterator();
			
			while (itr.hasNext()) {
				ReportDailyNewVO reportDailyNewVO = itr.next();
				confirmedXLandHPMap.put(reportDailyNewVO.getUserId(), 
						reportDailyNewVO);
			}
			
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("getConfirmedXLandHPWithUserDetails");
		return confirmedXLandHPMap;
	}
	
	/**
	 * This method will retrieve the confirmed Xl and IP customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public Map<Integer, ReportDailyNewVO> getConfirmedXLandIPWithUserDetails(
			String fromDate, String toDate) throws DBException 
	{
		Object[] params = { fromDate, toDate };
		logger.entering("getConfirmedXLandIPWithUserDetails", params);

		List<ReportDailyNewVO> confirmedXLandIPList = null;
		Map<Integer, ReportDailyNewVO> confirmedXLandIPMap = 
				new HashMap<Integer, ReportDailyNewVO>();
		
		try {

			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
					.append(" CS.reg_by AS userId, ")
					.append(" COUNT(*) AS confirmedXLandIP ")
					.append(" FROM ")
					.append(TABLE_CUSTOMER_SUBSCRIPTION)
					.append(" CS ")
					.append(" INNER JOIN ")
					.append(" (SELECT cust_id, reg_by, conf_date FROM customer_subscription WHERE product_id=4 and is_reactivated=0 and date(conf_date) BETWEEN '")
					.append(fromDate)
					.append("' AND '")
					.append(toDate)
					.append("' and is_confirmed=1) CSS")
					.append(" ON CS.cust_id=CSS.cust_id and CSS.reg_by=CS.reg_by and date(CS.conf_date)=date(CSS.conf_date) ")
					.append(" WHERE CS.product_id=2 and CS.is_reactivated=0")
					.append(" and date(CS.conf_date) ")
					.append(" BETWEEN '").append(fromDate).append("' AND '")
					.append(toDate).append("' and CS.is_confirmed=1 ")
					.append(" GROUP BY 1");
			
			confirmedXLandIPList = getSession()
					.createSQLQuery(sqlQuery.toString())
					.addScalar("userId", Hibernate.INTEGER)
					.addScalar("confirmedXLandIP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class))
					.list();
			
			Iterator<ReportDailyNewVO> itr = confirmedXLandIPList.iterator();
			
			while (itr.hasNext()) {
				ReportDailyNewVO reportDailyNewVO = itr.next();
				confirmedXLandIPMap.put(reportDailyNewVO.getUserId(), 
						reportDailyNewVO);
			}
			
		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details.",
					e);
			throw new DBException(e);
		}

		logger.exiting("getConfirmedXLandIPWithUserDetails");
		return confirmedXLandIPMap;
	}
	
	/**
	 * This method will retrieve the free model deregistered customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<DeregisteredCustomersVO> getFreeModelDeregDetails(
			String fromDate, String toDate) throws DBException {
		Object[] params = {fromDate, toDate};
		logger.entering("getFreeModelDeregDetails", params);
		
		List<DeregisteredCustomersVO> deregCustomersList = null;
		
		try {
			
			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
				.append(" DATE_FORMAT(dr_date, '%d/%m/%Y') AS deregDate, ")
				.append(" IF(created_by is null, 1, created_by) AS regBy, ")
				.append(" COUNT(IF(product_id = 1, 1, NULL )) AS deregFree ")
				.append(" FROM ").append(TABLE_DEREGISTERED_CUSTOMERS)
				.append(" WHERE dr_date BETWEEN '").append(fromDate)
				.append(" ' AND ' ").append(toDate)
				.append(" ' GROUP BY deregDate, regBy ")
				.append(" ORDER BY regBy");
			
			deregCustomersList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("deregDate", Hibernate.STRING)
					.addScalar("regBy", Hibernate.INTEGER)
					.addScalar("deregFree", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(DeregisteredCustomersVO.class)).list();
			
		}
		catch(DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details.",
					e);
			throw new DBException(e);
		}
		
		logger.exiting("getFreeModelDeregDetails");
		return deregCustomersList;
	}
	
	/**
	 * This method will retrieve the XL deregistered customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<DeregisteredCustomersVO> getXLDeregDetails(
			String fromDate, String toDate) throws DBException {
		Object[] params = {fromDate, toDate};
		logger.entering("getXLDeregDetails", params);
		
		List<DeregisteredCustomersVO> deregCustomersList = null;
		
		try {
			
			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
				.append(" DATE_FORMAT(pc_date, '%d/%m/%Y') AS XLDeregDate, ")
				.append(" IF(reg_by is null , 1, reg_by) AS XLRegBy, ")
				.append(" COUNT(*) AS deregXL ")
				.append(" FROM ").append(TABLE_PRODUCT_CANCELLATIONS)
				.append(" WHERE product_id = 2 ")
				.append(" AND pc_date BETWEEN '").append(fromDate)
				.append(" ' AND ' ").append(toDate)
				.append(" ' GROUP BY XLDeregDate, XLRegBy ")
				.append(" ORDER BY XLRegBy");
			
			deregCustomersList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("XLDeregDate", Hibernate.STRING)
					.addScalar("XLRegBy",Hibernate.INTEGER)
					.addScalar("deregXL", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(DeregisteredCustomersVO.class)).list();
		}
		catch(DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details.",
					e);
			throw new DBException(e);
		}
		
		logger.exiting("getXLDeregDetails");
		return deregCustomersList;
	}
	
	/**
	 * This method will retrieve the IP deregistered customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<DeregisteredCustomersVO> getIPDeregDetails(
			String fromDate, String toDate) throws DBException {
		Object[] params = {fromDate, toDate};
		logger.entering("getIPDeregDetails", params);
		
		List<DeregisteredCustomersVO> deregCustomersList = null;
		
		try {
			
			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
				.append(" DATE_FORMAT(pc_date, '%d/%m/%Y') AS IPDeregDate, ")
				.append(" IF(reg_by is null , 1, reg_by) AS IPRegBy, ")
				.append(" COUNT(*) AS deregIP ")
				.append(" FROM ").append(TABLE_PRODUCT_CANCELLATIONS)
				.append(" WHERE product_id = 4 ")
				.append(" AND pc_date BETWEEN '").append(fromDate)
				.append(" ' AND ' ").append(toDate)
				.append(" ' GROUP BY IPDeregDate, IPRegBy ")
				.append(" ORDER BY IPRegBy");
			
			deregCustomersList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("IPDeregDate", Hibernate.STRING)
					.addScalar("IPRegBy",Hibernate.INTEGER)
					.addScalar("deregIP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(DeregisteredCustomersVO.class)).list();
		}
		catch(DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details for IP.",
					e);
			throw new DBException(e);
		}
		
		logger.exiting("getIPDeregDetails");
		return deregCustomersList;
	}
	
	/**
	 * This method will retrieve the XL deregistered customers details.
	 * 
	 * @param fromDate
	 * @param toDate
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<DeregisteredCustomersVO> getHPDeregDetails(
			String fromDate, String toDate) throws DBException {
		Object[] params = {fromDate, toDate};
		logger.entering("getHPDeregDetails", params);
		
		List<DeregisteredCustomersVO> deregCustomersList = null;
		
		try {

			StringBuilder sqlQuery = new StringBuilder(" SELECT ")
			.append(" DATE_FORMAT(pc_date, '%d/%m/%Y') AS HPDeregDate, ")
			.append(" IF(reg_by is null , 1, reg_by) AS HPRegBy, ")
			.append(" COUNT(*) AS deregHP ")
			.append(" FROM ").append(TABLE_PRODUCT_CANCELLATIONS)
			.append(" WHERE product_id = 3 ")
			.append(" AND pc_date BETWEEN '").append(fromDate)
			.append(" ' AND ' ").append(toDate)
			.append(" ' GROUP BY HPDeregDate, HPRegBy ")
			.append(" ORDER BY HPRegBy");
		

			
		deregCustomersList = getSession().createSQLQuery(sqlQuery.toString())
				.addScalar("HPDeregDate", Hibernate.STRING)
				.addScalar("HPRegBy", Hibernate.INTEGER)
				.addScalar("deregHP", Hibernate.INTEGER)
				.setResultTransformer(
						Transformers.aliasToBean(DeregisteredCustomersVO.class)).list();
		}
		catch(DataAccessException e) {
			logger.error(
					"Exception occured while retrieving deregistered customers details.",
					e);
			throw new DBException(e);
		}
		
		logger.exiting("getHPDeregDetails");
		return deregCustomersList;
	}

	/**
	 * 
	 * @param date
	 * @param userId
	 * @return
	 * @throws DBException
	 * This method will fetch the daywise performance data of the agent.
	 */
	@SuppressWarnings("unchecked")
	public List<ReportDailyNewVO> getTotalConfirmationCountForDate(Date date, int userId) throws DBException {
		
		logger.entering("getTotalConfirmationCountForDate", date);
		
		List<ReportDailyNewVO> dailyDataList = null;
		try {
			/**
			 * Query modified for agent report changes.
			 */
			StringBuilder sqlQuery = new StringBuilder(" SELECT ud.user_uid userUid, ")
				.append("  COUNT(IF(cs.product_id = 2 AND cs.is_confirmed = 1,1, NULL)) + ")
				.append(" COUNT(IF(cs.product_id = 3 AND cs.is_confirmed = 1,1, NULL)) AS confirmedXLandHP")
				.append(" FROM ").append(TABLE_CUSTOMER_SUBSCRIPTION)
				.append(" cs ").append("LEFT OUTER JOIN ")
				.append(TABLE_USER_DETAILS).append(" ud ON ")
				.append(" cs.reg_by=ud.user_id ")
				.append(" WHERE YEAR(cs.conf_date)=").append(DateUtil.getYear(date))
				.append(" AND MONTH(cs.conf_date)=").append(DateUtil.getMonth(date)+1)
				.append(" AND DAY(cs.conf_date)=").append(DateUtil.getDay(date))
				.append(" AND ud.user_id=").append(userId);
			
			dailyDataList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("userUid", Hibernate.STRING)
					.addScalar("confirmedXLandHP", Hibernate.INTEGER)
					.setResultTransformer(
							Transformers.aliasToBean(ReportDailyNewVO.class)).list();
			
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving agent report details.",
					e);
			throw new DBException(e);
		}
		logger.exiting("getDetailedReportForDate");
		
		return dailyDataList;
		
	}

	/**
	 * This method will fetch the confirmed customer list
	 * for 3 previous month based on selected customer month
	 * 
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws DBException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> fetchConfirmedCustomerList(int userId,
			String fromDate, String toDate) throws DBException {
		Object params[] = {userId,fromDate,toDate};
		logger.entering("fetchConfirmedCustomerList ", params);
		
		List<Integer> custList = new ArrayList<Integer>();
		
		try{
			StringBuilder sqlQuery = new StringBuilder();
			sqlQuery.append(" SELECT ")
					.append(" CS.sn_id as custSnID ")
					.append(" FROM ")
					.append(" CUSTOMER_SUBSCRIPTION CS ")
					.append(" LEFT OUTER JOIN USER_DETAILS UD ON UD.user_id = CS.reg_by ")
					.append(" WHERE CS.reg_by = "+userId)
					.append(" AND CS.is_confirmed = 1 ")
					.append(" AND CS.conf_date BETWEEN '"+fromDate)
					.append(" ' - INTERVAL 1 MONTH ")
					.append(" AND '"+toDate)
					.append(" ' order by CS.cust_id ");
			
			logger.info("customer confirmation query for prior 3 month : ");

			custList = getSession().createSQLQuery(sqlQuery.toString())
					.list();
			
			
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving confirmed customer details list for deduction count.",
					e);
			throw new DBException(e);
		}
		
		logger.exiting("fetchConfirmedCustomerList size : ",custList.size());
		return custList;
	}
	@SuppressWarnings("unchecked")
	public CustDeductionReportVO fetchCustomerDeductionList(
			List<Integer> custList, String fromDate,
			String toDate) throws DBException {
		Object params[] = {custList.size(),fromDate,toDate};
		logger.entering("fetchCustomerDeductionList ", params);
		
		List<CustDeductionReportVO> deductionCustList = new ArrayList<CustDeductionReportVO>();
		CustDeductionReportVO custReportVO = null;
		
		
		try{
			StringBuilder builder = new StringBuilder();
			builder.append("(");
			if(custList!=null && !custList.isEmpty()){
				for (int i = 0; i < custList.size(); i++) {
				    if(custList.size()>1){
				    	builder
					    .append(custList.get(i))
					    .append(",");
					    
					    if(i == custList.size()-1){
					    	builder.deleteCharAt(builder.lastIndexOf(","));
					    }
				    }else{
				    	builder
					    .append(custList.get(i));
					    
					   /* if(i == custList.size()-1){
					    	builder.deleteCharAt(builder.lastIndexOf(","));
					    }*/
				    }
				}
			}
			builder.append(")");

			StringBuilder dedQuery = new StringBuilder();
			dedQuery.append(" SELECT ")
					
					.append(" SUM(CASE WHEN cs.is_partially_deducted = 1 AND cs.product_id=2 ")
					.append(" AND IFNULL(cs.is_fully_deducted,0) <> 1  ")
					.append(" AND  cs.partially_deducted_date BETWEEN '"+fromDate) 
					.append("' AND '"+toDate)
					.append("' AND cs.conf_date BETWEEN '"+fromDate) 
					.append("' AND '"+toDate)
					.append("' THEN 1*0.5 ELSE 0 END) partialDedCountXL, ")
					
					.append(" SUM(CASE WHEN cs.is_partially_deducted = 1 AND cs.product_id=3 ")
					.append(" AND IFNULL(cs.is_fully_deducted,0) <> 1  ")
					.append(" AND  cs.partially_deducted_date BETWEEN '"+fromDate) 
					.append(" ' AND '"+toDate)
					.append(" ' AND cs.conf_date BETWEEN '"+fromDate) 
					.append(" ' AND '"+toDate)
					.append(" ' THEN 1*0.5 ELSE 0 END) partialDedCountHP,")
					
					.append(" SUM(CASE WHEN cs.is_partially_deducted = 1 AND cs.product_id=4 ")
					.append(" AND IFNULL(cs.is_fully_deducted,0) <> 1  ")
					.append(" AND  cs.partially_deducted_date BETWEEN '"+fromDate) 
					.append(" ' AND '"+toDate)
					.append(" ' AND cs.conf_date BETWEEN '"+fromDate) 
					.append(" ' AND '"+toDate)
					.append(" ' THEN 1*0.5 ELSE 0 END) partialDedCountIP,")
					
					.append(" SUM(CASE WHEN cs.is_partially_deducted = 1 AND cs.product_id=2 ")
					.append(" AND IFNULL(cs.is_fully_deducted,0) <> 1  ")
					.append(" AND  cs.partially_deducted_date BETWEEN '"+fromDate)
					.append(" ' AND '"+toDate)
					.append(" ' AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
					.append("',INTERVAL 1 month) AND DATE_SUB('"+toDate)
					.append("',INTERVAL 1 month)")
					.append(" THEN 1*0.5 ELSE 0 END) partialDedCountXLPrev,")
					
					.append(" SUM(CASE WHEN cs.is_partially_deducted = 1 AND cs.product_id=3 ")
					.append(" AND IFNULL(cs.is_fully_deducted,0) <> 1  ")
					.append(" AND  cs.partially_deducted_date BETWEEN '"+fromDate)
					.append(" ' AND '"+toDate)
					.append(" ' AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
					.append("',INTERVAL 1 month) AND DATE_SUB('"+toDate)
					.append("',INTERVAL 1 month)")
					.append(" THEN 1*0.5 ELSE 0 END) partialDedCountHPPrev,")
					
					.append(" SUM(CASE WHEN cs.is_partially_deducted = 1 AND cs.product_id=4 ")
					.append(" AND IFNULL(cs.is_fully_deducted,0) <> 1  ")
					.append(" AND  cs.partially_deducted_date BETWEEN '"+fromDate)
					.append(" ' AND '"+toDate)
					.append(" ' AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
					.append("',INTERVAL 1 month) AND DATE_SUB('"+toDate)
					.append("',INTERVAL 1 month)")
					.append(" THEN 1*0.5 ELSE 0 END) partialDedCountIPPrev,")
					
						
							.append(" SUM(CASE WHEN cs.product_id=2  AND IFNULL(cs.is_fully_deducted,0) = 1 ")
							.append(" AND  cs.fully_deducted_date BETWEEN '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' AND cs.conf_date BETWEEN  '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' THEN 1 ELSE 0 END ) fullyDedCountXL, ")
							
							.append(" SUM(CASE WHEN cs.product_id=3  AND IFNULL(cs.is_fully_deducted,0) = 1 ")
							.append(" AND  cs.fully_deducted_date BETWEEN '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' AND cs.conf_date BETWEEN  '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' THEN 1 ELSE 0 END ) fullyDedCountHP, ")
							
							.append(" SUM(CASE WHEN cs.product_id=4  AND IFNULL(cs.is_fully_deducted,0) = 1 ")
							.append(" AND  cs.fully_deducted_date BETWEEN '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' AND cs.conf_date BETWEEN  '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' THEN 1 ELSE 0 END ) fullyDedCountIP, ")
							
							
							
							.append(" SUM(CASE WHEN cs.product_id=2 AND  IFNULL(cs.is_partially_deducted,0) = 1 ")
							.append(" AND cs.partially_deducted_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB(' "+toDate)
							.append(" ',INTERVAL 1 month) ")
							.append("  AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB(' "+toDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND IFNULL(cs.is_fully_deducted,0) = 1 AND  cs.fully_deducted_date BETWEEN ' "+fromDate)
							.append(" 'AND '"+toDate)
							.append(" ' THEN 1*0.5 ")
							.append(" WHEN cs.product_id=2 ")
							.append(" AND IFNULL(cs.is_fully_deducted,0) = 1 ")
							.append(" AND  cs.fully_deducted_date BETWEEN '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB('"+toDate)
							.append(" ',INTERVAL 1 month)")
							.append(" THEN 1 ELSE 0 END ) fullyDedCountXLPrev, ")
							
							.append(" SUM(CASE WHEN cs.product_id=3 AND  IFNULL(cs.is_partially_deducted,0) = 1 ")
							.append(" AND cs.partially_deducted_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB(' "+toDate)
							.append(" ',INTERVAL 1 month) ")
							.append("  AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB(' "+toDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND IFNULL(cs.is_fully_deducted,0) = 1 AND  cs.fully_deducted_date BETWEEN ' "+fromDate)
							.append(" 'AND '"+toDate)
							.append(" ' THEN 1*0.5 ")
							.append(" WHEN cs.product_id=3 ")
							.append(" AND IFNULL(cs.is_fully_deducted,0) = 1 ")
							.append(" AND  cs.fully_deducted_date BETWEEN '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB('"+toDate)
							.append(" ',INTERVAL 1 month)")
							.append(" THEN 1 ELSE 0 END ) fullyDedCountHPPrev, ")
							
							.append(" SUM(CASE WHEN cs.product_id=4 AND  IFNULL(cs.is_partially_deducted,0) = 1 ")
							.append(" AND cs.partially_deducted_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB(' "+toDate)
							.append(" ',INTERVAL 1 month) ")
							.append("  AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB(' "+toDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND IFNULL(cs.is_fully_deducted,0) = 1 AND  cs.fully_deducted_date BETWEEN ' "+fromDate)
							.append(" 'AND '"+toDate)
							.append(" ' THEN 1*0.5 ")
							.append(" WHEN cs.product_id=4 ")
							.append(" AND IFNULL(cs.is_fully_deducted,0) = 1 ")
							.append(" AND  cs.fully_deducted_date BETWEEN '"+fromDate)
							.append(" ' AND '"+toDate)
							.append(" ' AND cs.conf_date BETWEEN DATE_SUB( '"+fromDate)
							.append(" ',INTERVAL 1 month) ")
							.append(" AND DATE_SUB('"+toDate)
							.append(" ',INTERVAL 1 month)")
							.append(" THEN 1 ELSE 0 END ) fullyDedCountIPPrev ")
					
					.append(" FROM customer_subscription cs where cs.sn_id in "+builder);
			
			logger.info("customer deduction query : ");
			
			deductionCustList = getSession().createSQLQuery(dedQuery.toString())
					.addScalar("partialDedCountXL", Hibernate.BIG_DECIMAL)
					.addScalar("fullyDedCountXL", Hibernate.BIG_DECIMAL)
					.addScalar("partialDedCountHP", Hibernate.BIG_DECIMAL)
					.addScalar("fullyDedCountHP", Hibernate.BIG_DECIMAL)
					.addScalar("partialDedCountIP", Hibernate.BIG_DECIMAL)
					.addScalar("fullyDedCountIP", Hibernate.BIG_DECIMAL)
					.addScalar("partialDedCountXLPrev", Hibernate.BIG_DECIMAL)
					.addScalar("fullyDedCountXLPrev", Hibernate.BIG_DECIMAL)
					.addScalar("partialDedCountHPPrev", Hibernate.BIG_DECIMAL)
					.addScalar("fullyDedCountHPPrev", Hibernate.BIG_DECIMAL)
					.addScalar("partialDedCountIPPrev", Hibernate.BIG_DECIMAL)
					.addScalar("fullyDedCountIPPrev", Hibernate.BIG_DECIMAL)
					.setResultTransformer(
							Transformers
									.aliasToBean(CustDeductionReportVO.class))
					.list();
			if(deductionCustList.size() != 0 && deductionCustList != null){
				custReportVO = deductionCustList.get(0);
			}
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving confirmed customer details list for deduction count.",
					e);
			throw new DBException(e);
		}
		
		logger.exiting("fetchCustomerDeductionList size : ",deductionCustList.size());
		return custReportVO;
	}

	/**
	 * This method will fetch the confirmed customer list
	 * for prior 3rd month from current month based on selected month by user
	 * 
	 * @param userId
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @throws DBException
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> fetchConfirmedCustListForFirstQuarterMonth(
			int userId, String fromDate, String toDate) throws DBException  {
		Object params[] = {userId,fromDate,toDate};
		logger.entering("fetchConfirmedCustListForFirstQuarterMonth ", params);
		
		List<Integer> custList = new ArrayList<Integer>();
		
		try{
			StringBuilder sqlQuery = new StringBuilder();
			sqlQuery.append(" SELECT ")
					.append(" CS.cust_id as custID ")
					.append(" FROM ")
					.append(" CUSTOMER_SUBSCRIPTION CS ")
					.append(" LEFT OUTER JOIN USER_DETAILS UD ON UD.user_id = CS.reg_by ")
					.append(" WHERE CS.reg_by = "+userId)
					.append(" AND CS.is_confirmed = 1 ")
					.append(" AND month(CS.conf_date) = month(DATE_SUB('"+fromDate)
					.append(" ', INTERVAL 3 MONTH)) AND year(CS.conf_date) = year(DATE_SUB('"+toDate)
					.append(" ', INTERVAL 3 month)) ")
					.append(" group by CS.cust_id ");
			logger.info("customer confirmation query for prior 3rd month from current calendar month : ");

			custList = getSession().createSQLQuery(sqlQuery.toString())
					.list();
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving confirmed customer details list for deduction count.",
					e);
			throw new DBException(e);
		}
		
		logger.exiting("fetchConfirmedCustListForFirstQuarterMonth size : ",custList.size());
		return custList;
	}

	@SuppressWarnings("unchecked")
	public CustDeductionReportVO fetchQuarterDeductionList(List<Integer> custList,int agentId,
			String fromDate, String toDate) throws DBException {
		Object params[] = {custList,agentId,fromDate,toDate};
		logger.entering("fetchCustomerDeductionList ", params);
		
		List<CustDeductionReportVO> deductionCustList = new ArrayList<CustDeductionReportVO>();
		CustDeductionReportVO custReportVO = null;
		
		try{
			
			StringBuilder builder = new StringBuilder();
			builder.append("(");
			for (int i = 0; i < custList.size(); i++) {
			    builder
			    .append(custList.get(i))
			    .append(",");
			    
			    if(i == custList.size()-1){
			    	builder.deleteCharAt(builder.lastIndexOf(","));
			    }
			}
			
			builder.append(")");

			StringBuilder dedQuery = new StringBuilder();
			dedQuery.append(" SELECT ")
					.append(" SUM(IF(fullyDedCountXL >= 2, 1, 0)) AS fullyDedCountXL, ")
					.append(" SUM(IF(fullyDedCountHP >= 2, 1, 0)) AS fullyDedCountHP, ")
					.append(" SUM(IF(fullyDedCountIP >= 2, 1, 0)) AS fullyDedCountIP ")
					.append(" FROM ( ")
					.append(" SELECT cs.reg_by, COUNT(CASE WHEN IFNULL(ch.charges_xl, 0) >= 1.50 ")
					.append(" AND cs.product_id = 2  THEN 1 ELSE null END) fullyDedCountXL, ")
					.append(" COUNT(CASE WHEN IFNULL(ch.charges_hp, 0) >= 1.50 AND ")
					.append(" cs.product_id = 3 THEN 1 ELSE null END) fullyDedCountHP, ")
					.append(" COUNT(CASE WHEN IFNULL(cs.product_level_id,  0) = 14 ")
					.append(" AND  IFNULL(ch.charges_ip, 0) >= 5.00 THEN 1 ")
					.append(" WHEN IFNULL(cs.product_level_id, 0)=15 ")
					.append(" AND  IFNULL(ch.charges_ip, 0) >= 7.00 THEN 1 ")
					.append(" WHEN IFNULL(cs.product_level_id, 0)=16 ")
					.append(" AND  IFNULL(ch.charges_ip, 0) >= 9.00 THEN 1 ELSE null END) fullyDedCountIP ")
					.append(" FROM ")
					.append(" customer_subscription cs LEFT OUTER JOIN coverhistory ch ")
					.append(" ON cs.cust_id = ch.cust_id AND DATE(date_sub(CH.CREATED_DATE, interval 1 month)) ")
					.append(" between DATE('"+fromDate)
					.append(" ') AND DATE('"+toDate)
					.append(" ') ")
					.append(" WHERE ch.cust_id IN "+builder)
					.append(" and cs.reg_by = "+agentId)
					.append(" GROUP BY cs.cust_id) ch GROUP BY ch.reg_by ");
				
					/*.append(" SUM(CASE WHEN IFNULL(ch.charges_xl,0) >= 1.50 AND IFNULL(cs.product_id,0)=2 ")
					.append(" AND cs.reg_by="+agentId)
					.append(" AND ch.month IN(monthname('"+fromDate)
					.append(" '),monthname('").append(toDate+"')) ")
					.append(" AND ch.year IN (year('"+fromDate)
					.append(" '),year('"+toDate)
					.append(" ')) THEN 1 ELSE 0 END) fullyDedCountXL, ")
					
					.append(" SUM(CASE WHEN IFNULL(ch.charges_hp,0) >= 1.50 AND IFNULL(cs.product_id,0)=3 ")
					.append(" AND cs.reg_by="+agentId)
					.append(" AND ch.month IN(monthname('"+fromDate)
					.append(" '),monthname('").append(toDate+"')) ")
					.append(" AND ch.year IN (year('"+fromDate)
					.append(" '),year('"+toDate)
					.append(" ')) THEN 1 ELSE 0 END) fullyDedCountHP, ")
					
					.append(" SUM(CASE WHEN IFNULL(cs.product_id,0)=4 AND IFNULL(cs.product_level_id,0)=14 ")
					.append(" AND cs.reg_by="+agentId)
					.append(" AND IFNULL(ch.charges_ip,0) >= 5.00 AND ")
					.append(" ch.month IN(monthname('"+fromDate)
					.append(" '),monthname('").append(toDate+"')) ")
					.append(" AND ch.year IN (year('"+fromDate)
					.append(" '),year('"+toDate)
					.append(" ')) THEN 1 ")
					
					.append(" WHEN IFNULL(cs.product_id,0)=4 AND IFNULL(cs.product_level_id,0)=15 ")
					.append(" AND cs.reg_by="+agentId)
					.append(" AND IFNULL(ch.charges_ip,0) >= 7.00 AND ")
					.append(" ch.month IN(monthname('"+fromDate)
					.append(" '),monthname('").append(toDate+"')) ")
					.append(" AND ch.year IN (year('"+fromDate)
					.append(" '),year('"+toDate)
					.append(" ')) THEN 1 ")

					.append(" WHEN IFNULL(cs.product_id,0)=4 AND IFNULL(cs.product_level_id,0)=16 ")
					.append(" AND cs.reg_by="+agentId)
					.append(" AND IFNULL(ch.charges_ip,0) >= 9.00 AND ")
					.append(" ch.month IN(monthname('"+fromDate)
					.append(" '),monthname('").append(toDate+"')) ")
					.append(" AND ch.year IN (year('"+fromDate)
					.append(" '),year('"+toDate)
					.append(" ')) THEN 1 ELSE 0 END) fullyDedCountIP ")

					.append(" FROM customer_subscription cs LEFT OUTER join coverhistory ch ON ")
					.append(" cs.cust_id = ch.cust_id LEFT OUTER join product_details pd ON ")
					.append(" cs.product_id = pd.product_id LEFT OUTER JOIN product_cover_details pcd ON ")
					.append(" cs.product_level_id = pcd.product_level_id ")
					.append(" where ch.cust_id IN "+builder);
*/			
			logger.info("customer deduction query : ", dedQuery.toString());
			
			deductionCustList = getSession().createSQLQuery(dedQuery.toString())
					.addScalar("fullyDedCountXL", Hibernate.BIG_DECIMAL)
					.addScalar("fullyDedCountHP", Hibernate.BIG_DECIMAL)
					.addScalar("fullyDedCountIP", Hibernate.BIG_DECIMAL)
					.setResultTransformer(
							Transformers
									.aliasToBean(CustDeductionReportVO.class))
					.list();
			if(deductionCustList.size() != 0 && deductionCustList != null){
				custReportVO = deductionCustList.get(0);
			}
		}catch(DataAccessException e) {
			logger
			.error(
					"Exception occured while retrieving confirmed customer details list for deduction count.",
					e);
			throw new DBException(e);
		}
		
		logger.exiting("fetchCustomerDeductionList size : ",deductionCustList.size());
		return custReportVO;
	}

}

