package com.mip.application.dal.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;

import com.mip.application.model.CustomerSubscription;
import com.mip.application.view.CustomerVO;
import com.mip.application.view.OfferMgmtVO;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>CustomerSubscriptionManager.java</code> contains all the database related 
 *  operations pertaining to Customer subscription module.
 * </p>
 * 
 * @author T H B S
 * 
 */

public class CustomerSubscriptionManager extends 
            DataAccessManager<CustomerSubscription, Integer>{
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomerSubscriptionManager.class);
	
	/**
	 * Default Constructor
	 */
	public CustomerSubscriptionManager() {
		super(CustomerSubscription.class);		
	}
	
	/**
	 * This method assigns the selected offer to all the active customers.
	 * 
	 * @param offerMgmtVO,<code>OfferMgmtVO</code> holding offer details.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public void assignOfferToAllCustomers(OfferMgmtVO offerMgmtVO) throws DBException{
		
		logger.entering("assignOfferToAllCustomers", offerMgmtVO);
		
		try{
			//Query to update the customer subscription table
			StringBuilder updateSql = new StringBuilder("UPDATE CustomerSubscription cs ");
			updateSql.append(" set cs.offerDetails.offerId = :selOfferId WHERE cs.dormant = 0 ");
			
			Query query = super.getSession().createQuery(updateSql.toString());
			query.setInteger("selOfferId", offerMgmtVO.getProducts());
			
			int updateCount = query.executeUpdate();
			
			logger.info("Number of customers assigned to the Offer", updateCount);
					
		}catch (DataAccessException exception) {
			
			logger.error("Exception occured while assigning Offer ", exception);
			throw new DBException(exception);
		}
			
		logger.exiting("assignOfferToAllCustomers");
			
	}
	
	/**
	 * This method assigns the offer to selected active customers.
	 * 
	 * @param fromDate, <code>Date</code> holds the from date
	 * @param toDate, <code>Date</code> holds the to date
	 * @param offerId, the offerId to be assigned to the customers
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public void assignOfferToSelectedCustomers(Date fromDate, Date toDate,
			int offerId) throws DBException{
			
		logger.entering("assignOfferToSelectedCustomers", fromDate, toDate, offerId);
		
		try{
			
			//Query to update the customer subscription
			StringBuilder updateSql = new StringBuilder("UPDATE CustomerSubscription cs ");
			updateSql.append(" SET cs.offerDetails.offerId =:selOfferId ");
			updateSql.append(" WHERE  cs.offerDetails.offerId = null AND cs.dormant=0 AND ");
			updateSql.append(" cs.customerDetails.custId = (SELECT cd.custId from CustomerDetails cd ");
			updateSql.append(" where cd.createdDate between  :startDate and :toDate and ");
			updateSql.append(" cd.confirmed=1 and ");
			updateSql.append(" cd.custId = cs.customerDetails.custId )");
					
			Query query = super.getSession().createQuery(updateSql.toString());
			query.setParameter("selOfferId", offerId);
			query.setParameter("startDate", fromDate);
			query.setParameter("toDate", toDate);
			
			int updateCount = query.executeUpdate();
			
			logger.info("Number of customers assigned to the Offer", updateCount);
			
		}catch (DataAccessException exception) {
			
			logger.error("Exception occured while assigning Offer ", exception);
			throw new DBException(exception);
		}
			
		logger.exiting("assignOfferToSelectedCustomers");
			
	}
	
	/**
	 * This method retrieves the count of customers assigned to an offer.
	 * between the given date range and are not assigned with any offer.
	 * 
	 * @param fromDate, <code>Date</code> holds the from date
	 * @param toDate, <code>Date</code> holds the to date
	 * @return count, the count of the customers.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public long fetchCustomersBasedOnOffer(OfferMgmtVO offerMgmtVO) throws DBException{
			
		logger.entering("fetchCustomersBasedOnOffer", offerMgmtVO);
		
		long customerCount = 0;
		
		try{
			//Query to retrieve the count of registered customers between the given dates.
			StringBuilder selectSql = new StringBuilder("SELECT COUNT(*) FROM CustomerSubscription cs ");
			selectSql.append(" WHERE cs.dormant=0 ");
			
			//IF selected offer is not ALL
			if(offerMgmtVO.getProducts() != -1){
				selectSql.append(" AND cs.offerDetails.offerId =:selOfferId ");
			}else {
				selectSql.append(" AND cs.offerDetails.offerId != null ");
			}
											
			Query query = super.getSession().createQuery(selectSql.toString());
			
			if(offerMgmtVO.getProducts() != -1){
				query.setParameter("selOfferId", offerMgmtVO.getProducts());
			}
			
			List customerList = query.list();
			
			customerCount = ((Long) customerList.get(0)).longValue();
						
		}catch (DataAccessException exception) {
			
			logger.error("Exception occured while fetching customer details", exception);
			throw new DBException(exception);
		}
				
		logger.exiting("fetchCustomersBasedOnOffer", customerCount);
		
		return customerCount;
	}
		
	/**
	 * This method revokes the selected offer from the customers.
	 * 
	 * @param offerMgmtVO,<code>OfferMgmtVO</code> holding offer details.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public void revokeOffer(OfferMgmtVO offerMgmtVO) throws DBException{
		
		logger.entering("revokeOffer", offerMgmtVO);
		
		int updateCount = 0;
		
		try{
			
			//Query to update the customer subscription
			StringBuilder updateSql = new StringBuilder("UPDATE CustomerSubscription cs ");
			updateSql.append(" SET cs.offerDetails.offerId =null, cs.offerSubscribed =0 WHERE cs.dormant=0 ");
			
			//IF selected offer is not ALL
			if(offerMgmtVO.getProducts() != -1){
				
				updateSql.append(" AND cs.offerDetails.offerId =:selOfferId ");
				
			}
				
			Query query = super.getSession().createQuery(updateSql.toString());
			
			if(offerMgmtVO.getProducts() != -1){
				
			  query.setParameter("selOfferId", offerMgmtVO.getProducts());
			  
			}
			
			updateCount = query.executeUpdate();
						
		}catch (DataAccessException exception) {
			logger.error("Exception occured while revoking Offer ", exception);
			throw new DBException(exception);
		}
			
		logger.exiting("revokeOffer",updateCount);
			
	}
	
	/**
	 * This method updates Business Rule for all records in 
	 * Customer subscription table.
	 * 
	 * @param bRuleId, Current active business rule id.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public void updateCustomerBusinessRule(int bRuleId) throws DBException{
		
		logger.entering("updateCustomerBusinessRule", bRuleId);
		
		int updateCount = 0;		
		try{			
			//Query to update the customer subscription
			StringBuilder updateSql = 
				new StringBuilder("UPDATE CustomerSubscription cs ");
			updateSql.append(" SET cs.businessRuleMaster.brId = :bRuleId ");
						
			Query query = super.getSession().createQuery(updateSql.toString());
			query.setParameter("bRuleId", bRuleId);
			
			updateCount = query.executeUpdate();
						
		}catch (DataAccessException exception) {
			logger.error("Exception occured while updating Business Rule for " +
					"all records in Customer subscription. ", exception);
			throw new DBException(exception);
		}
			
		logger.exiting("updateCustomerBusinessRule",updateCount);
			
	}
	
	/**
	 * This methods queries the database to check if any offer is assigned to 
	 * the input MSISDN.
	 * 
	 * @param msisdn , input MSISDN.
	 * @return true if exists and false otherwise.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public boolean checkIfMSISDNAssignedToOffers(String msisdn) throws DBException {
		logger.entering("checkIfMSISDNAssignedToOffers", msisdn);
		
		int rowCount=0;
		try {
			StringBuilder selectSql = new StringBuilder("SELECT COUNT(*) FROM CustomerSubscription cs ");
			selectSql.append("WHERE cs.offerSubscribed=1 AND cs.customerDetails.custId = " );
			selectSql.append("(SELECT cd.custId FROM CustomerDetails cd WHERE cd.msisdn = :msisdn )");
			
			Query query = super.getSession().createQuery(selectSql.toString());
			query.setParameter("msisdn", msisdn);
			
			List customerList = query.list();
			
			rowCount = Integer.valueOf(customerList.get(0).toString());
		}
		catch (DataAccessException exception) {
			logger.error("Exception occured while validating MSISDN ", exception);
			throw new DBException(exception);
		}
		catch (Exception exception) {
			logger.error("Exception occured while validating MSISDN ", exception);
		}
		logger.exiting("checkIfMSISDNAssignedToOffers", rowCount);
		
		return rowCount > 0 ? true : false;
	}

	/**
	 * This method retrieves the count of customers assigned to an offer
	 * based on their MSISDN 
	 * 
	 * @param offerMgmtVO - holding the MSISDN details
	 * @return customerCount, the count of the customers.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public long fetchCustomersBasedOnMSISDN(OfferMgmtVO offerMgmtVO) throws DBException{
			
		logger.entering("fetchCustomersBasedOnMSISDN", offerMgmtVO);
		
		long customerCount = 0;
		String[] msisdnList = offerMgmtVO.getMsisdnCSV().split(",");
		try{
			//Query to retrieve the count of registered customers based on MSISDN.
			StringBuilder selectSql = new StringBuilder("SELECT COUNT(*) FROM CustomerSubscription cs ");
			selectSql.append("WHERE cs.offerSubscribed =1 AND cs.customerDetails.custId IN ");
			selectSql.append("(SELECT cd.custId FROM CustomerDetails cd WHERE cd.msisdn IN ");

			StringBuilder msisdnCSV = new StringBuilder("'" + msisdnList[0] + "'");
			for (int i=1; i<msisdnList.length; i++) {
				msisdnCSV.append(",");
				msisdnCSV.append("'" + msisdnList[i] + "'");
			}
			
			selectSql.append("(" + msisdnCSV.toString() + "))");
													
			Query query = super.getSession().createQuery(selectSql.toString());
			
			List customerList = query.list();
			
			customerCount = ((Long) customerList.get(0)).longValue();
						
		}catch (DataAccessException exception) {
			
			logger.error("Exception occured while fetching customer details", exception);
			throw new DBException(exception);
		}
				
		logger.exiting("fetchCustomersBasedOnMSISDN", customerCount);
		
		return customerCount;
	}
	
	/**
	 * This method revokes the offers from the customers based on the MSISDN.
	 * 
	 * @param offerMgmtVO - holding the MSISDN details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public void revokeOfferForMSISDN(OfferMgmtVO offerMgmtVO) throws DBException{
		
		logger.entering("revokeOfferForMSISDN", offerMgmtVO);
		int updateCount = 0;
		
		try{
			
			StringBuilder updateSql = new StringBuilder("UPDATE CustomerSubscription cs ");
			updateSql.append("SET cs.offerDetails.offerId =null, cs.offerSubscribed =2 ");
			updateSql.append("WHERE cs.customerDetails.custId IN ");
			updateSql.append("(SELECT cd.custId FROM CustomerDetails cd WHERE cd.msisdn IN ");
			
			List<String> msisdnCSVList = Arrays.asList(offerMgmtVO.getMsisdnCSV().split(","));
			
			StringBuilder msisdnCSVBldr = new StringBuilder("'" + msisdnCSVList.get(0).toString() + "'");
			for (int i=1; i<msisdnCSVList.size(); i++) {
				msisdnCSVBldr.append(",");
				msisdnCSVBldr.append("'" + msisdnCSVList.get(i).toString() + "'");
			}
			updateSql.append("(" + msisdnCSVBldr + "))");

			Query query = super.getSession().createQuery(updateSql.toString());
			
			updateCount = query.executeUpdate();
						
		}catch (DataAccessException exception) {
			logger.error("Exception occured while revoking Offer ", exception);
			throw new DBException(exception);
		}
			
		logger.exiting("revokeOfferForMSISDN",updateCount);
			
	}

	/**
	 * The method fetches the customer subscription details of the customer.
	 * 
	 * @param 
	 * 		customerId cust_id of the customer.
	 * @return
	 * 		<code>CustomerSubscription</code> Object of type CustomerSubscription.
	 * @throws 
	 * 		<code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public CustomerSubscription getCustomerSubscriptionDetails(int customerId) throws DBException {
		Object[] params = {customerId};
		logger.entering("getCustomerSubscriptionDetails", params);
		
		CustomerSubscription customerSubscription = null;
		ArrayList<CustomerSubscription> custSubsList = null;
		try {
			String selectSql = "FROM CustomerSubscription cs WHERE " +
					"cs.customerDetails.custId=" + customerId;
			// above is commented as it is not checking is_deactivated = 0
			
//			String selectSql = "FROM CustomerSubscription cs WHERE " +
//					"cs.customerDetails.custId=" + customerId + " and cs.isDeactivated =" +0;
			
			custSubsList = (ArrayList<CustomerSubscription>) getHibernateTemplate()
					.find(selectSql);
			
			Iterator<CustomerSubscription> itr = custSubsList.iterator();
			
			while(itr.hasNext()) {
				customerSubscription = itr.next();
			}
			logger.info("Query: ", selectSql.toString());			
		} catch (DataAccessException exception) {
			
			logger.error("Exception occured while fetching customer subscription details", exception);
			throw new DBException(exception);
		}
		
		logger.exiting("getCustomerSubscriptionDetails", customerSubscription);
		return customerSubscription;
	}
	

	
	/**
	 * The method fetches the de registered customer subscription details of the customer.
	 * 
	 * @param 
	 * 		customerId cust_id of the customer.
	 * @return
	 * 		<code>CustomerSubscription</code> Object of type CustomerSubscription.
	 * @throws 
	 * 		<code>DBException</code>, if any database error occurs.
	 */
	
	@SuppressWarnings("unchecked")
	public List<CustomerVO> getDeregCustdetails(String custId, String offerId,String custName) throws DBException {
		logger.entering("getDeregisteredCustomerDetails");

		List<CustomerVO> deRegCustDetails = null;

		try {
			
			StringBuilder searchQuery = new StringBuilder("SELECT ")
			.append("bc.fname AS fname, ")
			.append("bc.sname AS sname, ")
			.append("bc.msisdn AS msisdn, ")
			.append("bc.dob AS dob, ")
			.append("bc.age AS age, ")
			.append("bc.gender AS gender, ")
			.append("bc.implied_age AS impliedAge, ")
			.append("bc.cust_relationship AS insRelation, ")
			.append("bc.irfname AS insRelFname, ")
			.append("bc.irsname AS insRelSurname, ")
			.append("bc.irage AS insRelAge, ")
			.append("bc.irdob AS insRelIrDoB, ")
			.append("bc.irMsisdn AS insMsisdn, ")
			.append("bc.kinfname AS ipNomFirstName, ")
			.append("bc.kinsname AS ipNomSurName, ")
			.append("bc.kinage AS ipNomAge, ")
			.append("bc.kiMsisdn AS ipInsMsisdn, ")
			.append("pd.product_name AS OfferName, ")
			.append("pc.product_level_id AS productCoverIdIP ")
			//.append("round(pcd.product_cover) AS productCover ")
			.append("from ")
			.append("bima_cancellations bc join product_cancellations pc on bc.bc_id = pc.bc_id ")
			//.append("join product_cover_details pcd on pc.product_level_id = pcd.product_level_id ")
			.append("join product_details pd on pc.product_id = pd.product_id ")
			.append("where ")
			.append("concat(bc.fname,' ',bc.sname) like '%").append(custName).append("%' and ")
			.append("bc.cust_id = '").append(custId).append("' and pc.product_id ='").append(offerId).append("'").append(" order by bc.bc_id desc limit 1");
	
			deRegCustDetails = (List<CustomerVO>) getSession()
				.createSQLQuery(searchQuery.toString())
				.addScalar("fname", Hibernate.STRING)
				.addScalar("sname", Hibernate.STRING)
				.addScalar("msisdn", Hibernate.STRING)
				.addScalar("dob", Hibernate.STRING)
				.addScalar("age", Hibernate.STRING)
				.addScalar("impliedAge", Hibernate.STRING)
				.addScalar("gender", Hibernate.STRING)
				.addScalar("insRelation", Hibernate.STRING)
				.addScalar("insRelFname", Hibernate.STRING)
				.addScalar("insRelSurname", Hibernate.STRING)
				.addScalar("insRelAge", Hibernate.STRING)
				.addScalar("insRelIrDoB", Hibernate.STRING)
				.addScalar("insMsisdn", Hibernate.STRING)
				.addScalar("ipNomFirstName", Hibernate.STRING)
				.addScalar("ipNomSurName", Hibernate.STRING)
				.addScalar("ipNomAge", Hibernate.STRING)
				.addScalar("ipInsMsisdn", Hibernate.STRING)
				.addScalar("OfferName", Hibernate.STRING)
				.addScalar("productCoverIdIP", Hibernate.STRING)
				//.addScalar("productCover", Hibernate.STRING)
				.setResultTransformer(
						Transformers
								.aliasToBean(CustomerVO.class))
				.list();
			
		} catch (HibernateException ex) {

			logger.error("Error retrieving Nominee Details: " + ex);

			throw new DBException(ex);
		}
		return deRegCustDetails;
	}

	
}
