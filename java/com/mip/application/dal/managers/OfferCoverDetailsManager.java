package com.mip.application.dal.managers;

import java.util.List;

import org.hibernate.Query;
import org.springframework.dao.DataAccessException;

import com.mip.application.model.ProductCoverDetails;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 19/04/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>OfferCoverDetailsManager.java</code> contains all the database related 
 *  operations pertaining to offer coverdetails.
 * </p>
 * 
 * @author T H B S
 * 
 */

public class OfferCoverDetailsManager extends DataAccessManager<ProductCoverDetails, Integer>{

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			OfferCoverDetailsManager.class);
	
	/**
	 * Default Constructor
	 */
	public OfferCoverDetailsManager() {
		super(ProductCoverDetails.class);		
	}
	
	/**
	 * This method returns the offers cover details for the given offer.
	 * 
	 * @return <code>List<OfferDetails></code> of offercover details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    @SuppressWarnings("unchecked")
	public List<ProductCoverDetails> retrieveOfferCoverBasedOnOfferId(int OfferId) throws DBException{
    	
    	logger.entering("retrieveOfferCoverBasedOnOfferId");
    	
    	List<ProductCoverDetails> productCoverDetails = null;
    	  	
    	try{
    		
    		StringBuilder selectSql = new StringBuilder("FROM OfferCoverDetails OCD");
			selectSql.append(" WHERE OCD.offerDetails.offerId =:selOfferId order by OCD.offerCover asc");
								
			Query query = super.getSession().createQuery(selectSql.toString());
			
			query.setParameter("selOfferId", OfferId);
			
			productCoverDetails = query.list();
			
    		
    	}catch (DataAccessException e) {
    		logger.error("Exception occured while retrieving OfferCover Details.", e);
			throw new DBException(e);
		}
    	
    	logger.exiting("retrieveOfferCoverBasedOnOfferId", productCoverDetails);
    	    	    	
    	return productCoverDetails;
		
	}
    
    /**
	 * This method deletes the offercover details for the given offer.
	 * 
	 * @param OfferId, holds the offer id.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public void deleteOfferCoverDetails(Integer OfferId) throws DBException{
    	
    	logger.entering("deleteOfferCoverDetails");
    	    	    	
    	try{

    		StringBuilder selectSql = new StringBuilder("DELETE FROM OfferCoverDetails OCD");
			selectSql.append(" WHERE OCD.offerDetails.offerId =:selOfferId");
								
			Query query = super.getSession().createQuery(selectSql.toString());
			
			query.setParameter("selOfferId", OfferId);
			
			query.executeUpdate();
	
    	}catch (DataAccessException e) {
    		
    		logger.error("Exception occured while deleting OfferCover Details.", e);
			throw new DBException(e);
		}
    	
    	logger.exiting("deleteOfferCoverDetails");
    			
	}
   
}
