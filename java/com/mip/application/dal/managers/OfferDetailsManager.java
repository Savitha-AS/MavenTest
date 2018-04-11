package com.mip.application.dal.managers;

import static com.mip.application.constants.DBObjects.TABLE_CUSTOMER_DETAILS;
import static com.mip.application.constants.DBObjects.TABLE_CUSTOMER_SUBSCRIPTION;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.mip.application.model.CustomerDetails;
import com.mip.application.model.ProductCoverDetails;
import com.mip.application.model.ProductDetails;
import com.mip.application.view.CustomerVO;
import com.mip.application.view.OfferCreateVO;
import com.mip.application.view.ProductCoverDetailsVO;
import com.mip.application.view.UserOfferRegistrationVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>OfferDetailsManager.java</code> contains all the database related
 * operations pertaining to offers management module.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class OfferDetailsManager extends
		DataAccessManager<ProductDetails, Integer> {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			OfferDetailsManager.class);

	/**
	 * Default Constructor
	 */
	public OfferDetailsManager() {
		super(ProductDetails.class);
	}

	/**
	 * This method returns all the offers in the MISP system
	 * 
	 * @return <code>List<OfferDetails></code> of offer details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public List<ProductDetails> retrieveOffers() throws DBException {

		logger.entering("retrieveOffers");

		List<ProductDetails> productDetails = null;

		try {
			// retrieve all offers
			productDetails = super.fetchAll();

		} catch (DataAccessException e) {
			logger.error("Exception occured while retrieving offer Details.", e);
			throw new DBException(e);
		}

		logger.exiting("retrieveOffers", productDetails);

		return productDetails;

	}

	/**
	 * This method is to save new offer details in the MISP system.
	 * 
	 * @param offerDetails
	 *            , An instance of <code>OfferDetails</code>.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public void saveOfferDetails(ProductDetails productDetails)
			throws DBException {

		logger.entering("saveOfferDetails", productDetails);

		try {

			super.save(productDetails);

		} catch (DataAccessException exception) {

			logger.error(
					"An exception occured while saving new offer details.",
					exception);
			throw new DBException(exception);

		}

		logger.exiting("saveOfferDetails");
	}

	/**
	 * This method returns all the active offers and the offerTypes.
	 * 
	 * @return <code>List<OfferDetails></code> of offer details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public List<ProductDetails> retrieveOffersWithOfferType()
			throws DBException {

		logger.entering("retrieveOffersWithOfferType");

		List<ProductDetails> productDetails = null;

		try {

			Criteria criteria = super.getSession().createCriteria(
					ProductDetails.class);
			criteria.setFetchMode("offerType", FetchMode.JOIN);
			criteria.add(Restrictions.ne("active", (byte) 0));
			productDetails = criteria.list();

		} catch (DataAccessException e) {

			logger.error("Exception occured while retrieving offer Details", e);
			throw new DBException(e);
		}

		logger.exiting("retrieveOffersWithOfferType", productDetails);

		return productDetails;

	}

	/**
	 * This method checks whether an offer exists with the same name.
	 * 
	 * @return true if an offer exists with the same name, else returns false
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public boolean checkForDuplicateOffer(OfferCreateVO offerCreateVO)
			throws DBException {

		logger.entering("checkForDuplicateOffer", offerCreateVO);

		boolean isOfferExist = true;

		try {

			StringBuilder selectSql = new StringBuilder(
					"SELECT COUNT(*) FROM OfferDetails OD ");
			selectSql.append(" WHERE OD.offerName =:inputOfferName");

			Query query = super.getSession().createQuery(selectSql.toString());

			query.setParameter("inputOfferName", offerCreateVO.getOfferName()
					.trim());

			List customerList = query.list();

			long count = ((Long) customerList.get(0)).longValue();

			if (count == 0) {
				isOfferExist = false;
			}

		} catch (DataAccessException e) {

			logger.error(
					"Exception occured while checking for duplicate offer", e);
			throw new DBException(e);
		}

		logger.exiting("checkForDuplicateOffer", isOfferExist);

		return isOfferExist;
	}

	/**
	 * This method returns all the active offers of type MultipleCover.
	 * 
	 * @return <code>List<OfferDetails></code> of offer details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public List<ProductDetails> retrieveMultipleCoverOffers()
			throws DBException {

		logger.entering("retrieveMultipleCoverOffers");

		List<ProductDetails> productDetails = null;

		try {

			StringBuilder selectSql = new StringBuilder("FROM OfferDetails OD ");
			selectSql.append(" WHERE OD.offerType.offerTypeName =:offerType");
			selectSql.append(" AND OD.active =:active");

			Query query = super.getSession().createQuery(selectSql.toString());

			query.setParameter("offerType",
					PlatformConstants.OFFER_TYPE_MULTIPLE_COVER);
			query.setParameter("active", PlatformConstants.STATUS_ACTIVE);

			productDetails = query.list();

		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving offerDetails of type MultipleCover.",
					e);
			throw new DBException(e);
		}

		logger.exiting("retrieveMultipleCoverOffers", productDetails);

		return productDetails;

	}
    
    /**
     * This method retrieves active/available offers from the database.
     * Only "offer name" and "offer id" is retrieved.  
     * 
     * @return
     * 	<code>List<OfferDetails></code> of offer details
     * 
     * @throws <code>DBException</code>, if any database error occurs.
     */
    @SuppressWarnings("unchecked")
	public List<ProductDetails> retrieveOfferNamesAndIds() throws DBException
    {
    	logger.entering("retrieveOfferNameAndId");
    	List<ProductDetails> productDetails = null;
    	try{
    		productDetails =super.getSession()
			.createCriteria(ProductDetails.class)
				.setProjection(Projections.projectionList()
					.add(Projections.property("productId"),"productId")
					.add(Projections.property("productName"),"productName"))				
				.add(Restrictions.ne("active", (byte)0))
				.addOrder(Order.asc("productId"))
				.setResultTransformer
						(Transformers.aliasToBean(ProductDetails.class))
			.list();
    	}catch (DataAccessException e) {
    		logger.error("Exception occured while retrieving offerDetails", e);
			throw new DBException(e);
		}
		logger.exiting("retrieveOfferNameAndId", productDetails);
		return productDetails;
	}

	/**
	 * This methods return the list of freemium details for all the users.
	 * 
	 * <table border=1>
	 * <th colspan=2>Change Request</th>
	 * <tr>
	 * <td>Q3 CR</td>
	 * <td>Request to add extra columns in Agent Report.</td>
	 * </tr>
	 * </table>
	 * 
	 * @param fromDate
	 *            Start date
	 * @param toDate
	 *            End date
	 * 
	 * @return <code>List</code> of freemium details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public List<UserOfferRegistrationVO> retrieveSubscribedOfferDetails(
		String fromDate, String toDate) throws DBException {
    	logger.entering("retrieveSubscribedOfferDetails");
    	
    	List<UserOfferRegistrationVO> offerDetails = null;
    	try{
    		/**
    		 * Query modified for agent report changes.
    		 */
    		StringBuilder selectQuery = new StringBuilder(
					"SELECT ");
    		selectQuery.append("cs.reg_by as offerRegBy, ");
    		selectQuery.append("COUNT(IF(cs.product_id=2 AND reg_chn_id=1,1,NULL)) regXLLaptop, ");
    		selectQuery.append("COUNT(IF(cs.product_id=2 AND reg_chn_id=2 OR reg_chn_id=3,1,NULL)) regXLHandset, ");
    		selectQuery.append("COUNT(IF(cs.product_id=2 AND cs.reg_by>0,1,NULL)) regXLTotal, ");
    		selectQuery.append("COUNT(IF(cs.product_id=2 AND cs.is_confirmed=1,1,NULL)) as confirmXL, ");
    		selectQuery.append("DATE_FORMAT(cs.reg_date,'%d/%m/%Y') offerRegDate ");
    		selectQuery.append("FROM ").append(TABLE_CUSTOMER_DETAILS).append(" cd ");
    		selectQuery.append("JOIN ")
    					.append(TABLE_CUSTOMER_SUBSCRIPTION).append(" cs on ");
    		selectQuery.append("cd.cust_id=cs.cust_id ");
    		selectQuery.append("WHERE ");
    		selectQuery.append("((cs.product_id is not null and cs.product_id>0) OR ");
    		selectQuery.append("(cs.product_id is not null and cs.product_id>0)) and cs.reg_date between '");
    		selectQuery.append(fromDate+"' and '"+toDate+"' ");
    		selectQuery.append("GROUP BY offerRegBy, offerRegDate ");
    		selectQuery.append("ORDER BY offerRegBy ");
    		offerDetails = getSession().createSQLQuery(selectQuery.toString())
    				.addScalar("offerRegBy", Hibernate.INTEGER)
    				.addScalar("regXLLaptop", Hibernate.INTEGER)
    				.addScalar("regXLHandset", Hibernate.INTEGER)
    				.addScalar("regXLTotal", Hibernate.INTEGER)
    				.addScalar("confirmXL", Hibernate.INTEGER)
    				.addScalar("offerRegDate", Hibernate.STRING)
    				.setResultTransformer(Transformers.aliasToBean(UserOfferRegistrationVO.class))
    				.list();
    		
    	}catch (DataAccessException e) {
    		logger.error("Exception occured while retrieving offer Details.", e);
			throw new DBException(e);
		}

		logger.exiting("retrieveSubscribedOfferDetails");
		return offerDetails;
	}

	/**
	 * This method calls the MODIFY_OFFER stored procedure to modify the
	 * customer's offer details.
	 * 
	 * @param custModel
	 *            instance of CustomerDetails, containing the customer details.
	 * @return true if statusCode is 3, false otherwise.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	@SuppressWarnings("unchecked")
	public boolean modifyOfferDetails(CustomerDetails custModel, String offerId)
			throws DBException {
		logger.entering("modifyOfferDetails", custModel, offerId);

		Integer statusCode = 0;
		try {
			Query query = super
					.getSession()
					.createSQLQuery(
							"CALL MODIFY_OFFER(:userId, :msisdn, :offerId)")
					// StatusCode in Stored Procedure
					.addScalar("StatusCode", Hibernate.INTEGER)
					.setParameter("userId", custModel.getModifiedBy())
					.setParameter("msisdn", custModel.getMsisdn())
					.setParameter("offerId", offerId);

			List<Integer> list = query.list();
			statusCode = list.get(0);

		} catch (DataAccessException e) {
			logger.error("Exception occured while retrieving "
					+ "offerDetails of type MultipleCover.", e);
			throw new DBException(e);
		}

		logger.exiting("modifyOfferDetails", (statusCode == 3));
		return (statusCode == 3);
	}
	/**
     * This method retrieves active/available offers from the database.
     * Only "ProductCover"  
     * 
     * @return
     * 	<code>List<OfferDetails></code> of offer details
     * 
     * @throws <code>DBException</code>, if any database error occurs.
     */
   @SuppressWarnings("unchecked")
	    public List<ProductCoverDetailsVO> retrieveOfferCoverDetailsIPProduct()
			   throws DBException {
			  logger.entering("retrieveOfferCoverDetailsIPProduct");
			  List<ProductCoverDetailsVO> productCoverDetails = null;
			  try {
			   StringBuilder sqlQurery = new StringBuilder()
			     .append("select product_level_id as productCoverId ,")
			     .append(" product_cover  as productCover ")
			     .append(" from product_cover_details where product_id=4");

			   productCoverDetails = super
			     .getSession()
			     .createSQLQuery(sqlQurery.toString())
			     .addScalar("productCoverId", Hibernate.STRING)
			     .addScalar("productCover", Hibernate.STRING)
			     .setResultTransformer(
			       Transformers
			         .aliasToBean(ProductCoverDetailsVO.class))
			     .list();

			  } catch (DataAccessException e) {
			   logger.error(
			     "Exception occured while retrieving offer Cover details ",
			     e);
			   throw new DBException(e);
			  }
			  logger.exiting("retrieveOfferCoverDetailsIPProduct", productCoverDetails);
			  return productCoverDetails;
			 }


	/**
	 * This method get product cover details of IP
	 * @return
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<ProductCoverDetailsVO> retrieveOfferCoverDetailsIP()
			throws DBException {
		logger.entering("retrieveOfferNamesAndIdsIgnoreIP");
		List<ProductCoverDetailsVO> productCoverDetails = null;
		try {
			StringBuilder sqlQurery = new StringBuilder()
					.append("select product_level_id as productCoverId ,")
					.append(" concat('GHC ' , truncate(cover_charges,0),' - ') as productCoverCharges , ")
					.append(" product_cover  as productCover ,")
					.append(" concat(' ', truncate(hp_cover  / 30,0) , '/night') as   productCoverIP") 
					.append(" from product_cover_details where product_id=4");

			productCoverDetails = super
					.getSession()
					.createSQLQuery(sqlQurery.toString())
					.addScalar("productCoverId", Hibernate.STRING)
					.addScalar("productCover", Hibernate.STRING)
					.addScalar("productCoverIP", Hibernate.STRING)
					.addScalar("productCoverCharges", Hibernate.STRING)
					.setResultTransformer(
							Transformers
									.aliasToBean(ProductCoverDetailsVO.class))
					.list();

		} catch (DataAccessException e) {
			logger.error(
					"Exception occured while retrieving IP offer Cover details ",
					e);
			throw new DBException(e);
		}
		logger.exiting("retrieveOfferNamesAndIdsIgnoreIP", productCoverDetails);
		return productCoverDetails;
	}
}
