package com.mip.application.dal.managers;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.mip.application.model.BusinessRuleDefinition;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>BusinessRuleDefinitionManager.java</code> contains all the database related 
 *  operations pertaining to Business Rule module.
 * </p>
 * 
 * @author T H B S
 * 
 */

public class BusinessRuleDefinitionManager extends DataAccessManager<BusinessRuleDefinition, Integer>{

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BusinessRuleDefinitionManager.class);
	
	/**
	 * Default Constructor
	 */
	public BusinessRuleDefinitionManager() {
		super(BusinessRuleDefinition.class);		
	}
	
	/**
	 * This method returns the active BusinessRule definition.
	 * 
	 * @return <code>List<OfferDetails></code> of offer details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    @SuppressWarnings("unchecked")
	public List<BusinessRuleDefinition> retrieveActiveBusinessRuleDef() throws DBException{
    	
    	logger.entering("retrieveActiveBusinessRuleDef");
    	
    	List<BusinessRuleDefinition> busRuleDefList = null;
    	
    	try{
    		
    		//Query to get the active business rule
    		StringBuilder query = new StringBuilder("FROM BusinessRuleDefinition  BRD,");
    		query.append("BusinessRuleMaster BRM WHERE BRD.businessRuleMaster.brId = BRM.brId ");
    		query.append(" AND  BRM.active = 1");
    		
    		busRuleDefList = getHibernateTemplate().find(query.toString());
    	    		
    	}catch (DataAccessException e) {
    		
    		logger.error("Exception occured while retrieving active business rule definition", e);
			throw new DBException(e);
		}
    	
    	logger.exiting("retrieveActiveBusinessRuleDef", busRuleDefList);
    	    	    	
    	return busRuleDefList;
		
	}
  

}
