package com.mip.application.dal.managers;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.springframework.dao.DataAccessException;

import com.mip.application.model.BusinessRuleMaster;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>BusinessRuleMasterManager.java</code> contains all the database related 
 *  operations pertaining to Business Rule module.
 * </p>
 * 
 * @author T H B S
 * 
 */

public class BusinessRuleMasterManager extends DataAccessManager<BusinessRuleMaster, Integer>{

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BusinessRuleMasterManager.class);
	
	/**
	 * Default Constructor
	 */
	public BusinessRuleMasterManager() {
		super(BusinessRuleMaster.class);		
	}
		
	/**
	 * This method returns the active Business Rule in the MISP system 
	 * @return <code>BusinessRuleMaster<OfferDetails></code> , the active BusinessRule.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    @SuppressWarnings("unchecked")
	public BusinessRuleMaster retrieveActiveBusinessRule() throws DBException{
    	
    	logger.entering("retrieveActiveBusinessRule");
    	
    	BusinessRuleMaster businessRule = null;
    	
    	try{
    		
    		//Query to get the active business rule
    		String query = "FROM BusinessRuleMaster AS BR WHERE BR.active = 1";

    		List<BusinessRuleMaster> busRuleList = new ArrayList<BusinessRuleMaster>();
    		    		
    		busRuleList = getHibernateTemplate().find(query);
    		    	
    		if(busRuleList == null || busRuleList.size() > 0){
    			
    			businessRule = (BusinessRuleMaster) busRuleList.get(0);
    			    			
    		}
    		
    		businessRule.getBusRuleDef().remove(null);
    		
    		    		
    	}catch (DataAccessException e) {
    		
    		logger.error("Exception occured while retrieving active business rule", e);
			throw new DBException(e);
		}
    	
    	logger.exiting("retrieveActiveBusinessRule", businessRule);
    	    	    	
    	return businessRule;
		
	}
   
    /**
	 * This method saves the BusinessRule definition in to the database.
	 * @param <code>BusinessRuleMaster</code> containing business rule definition
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public void saveBusinessRule(BusinessRuleMaster businessRule) throws DBException{
    	
    	logger.entering("saveBusinessRule", businessRule);
    
    	try{
    		
    		super.save(businessRule);
    	    		
    	}catch (DataAccessException e) {
    		
    		logger.error("Exception occured saving the business rule", e);
			throw new DBException(e);
		}
    	
    	logger.exiting("saveBusinessRule");
    			
	}
    
    /**
	 * This method returns all the Business Rules in the MISP system 
	 * 
	 * @return <code>List<BusinessRuleMaster></code> of business rules
	 * @throws <code>DBException</code>, if any database error occurs
	 * 
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<BusinessRuleMaster> listBusinessRules() throws DBException{
    	
    	logger.entering("listBusinessRules");
    	
    	ArrayList<BusinessRuleMaster> businessRulesList = null;
    	
    	try{
    		
    		Criteria  criteria= super.getSession().createCriteria(BusinessRuleMaster.class);
    		criteria.setFetchMode("insuranceCompany", FetchMode.JOIN);
    		    		
    		businessRulesList = (ArrayList<BusinessRuleMaster>) criteria.list();
    	       		    		
    	}catch (DataAccessException e) {
    		
    		logger.error("Exception occured while retrieving business rule versions", e);
			throw new DBException(e);
		}
    	
    	logger.exiting("listBusinessRules", businessRulesList);
    	    	    	
    	return businessRulesList;
		
	}
    
    /**
	 * This method returns the business rule based on the business rule Id.
	 * 
	 * @param businessRuleId, business rule id
	 * @return <code>BusinessRuleMaster</code> , business rule details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public BusinessRuleMaster retrieveBusinessRuleBasedOnId(Integer businessRuleId) throws DBException{
    	
    	logger.entering("retrieveBusinessRuleBasedOnId", businessRuleId);
    	
    	BusinessRuleMaster businessRule = null;
    	
    	try{
    	
    		businessRule = super.fetch(businessRuleId);
    		
    		businessRule.getBusRuleDef().remove(null);
    		
    		    		
    	}catch (DataAccessException e) {
    		
    		logger.error("Exception occured while retrieving business rule", e);
			throw new DBException(e);
		}
    	
    	logger.exiting("retrieveBusinessRuleBasedOnId", businessRule);
    	    	    	
    	return businessRule;
		
	}
    
    /**
	 * This method returns the max business rule id.
	 *  
	 * @return businessRuleId , amx business rule id.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    @SuppressWarnings("unchecked")
	public int retrieveMaxIdFromBusinessMaster() throws DBException{
    	
    	logger.entering("retrieveMaxIdFromBusinessMaster");
    	
    	long businessRuleId = 0;
    	
    	try{
    		//Query to get the active business rule
    		String query = "SELECT MAX(brId) FROM  BusinessRuleMaster";
    		
    		List countList = getHibernateTemplate().find(query);
    		
    		if(countList != null && countList.get(0) != null) {
    			
    			businessRuleId =  (Integer) countList.get(0);
    			
    		} else {
    			
    			businessRuleId = 0;
    		}
    	        		
    	}catch (DataAccessException e) {
    		
    		logger.error("Exception occured while retrieving max business rule id", e);
			throw new DBException(e);
		}
    	
    	logger.exiting("retrieveMaxIdFromBusinessMaster", businessRuleId);
    	    	    	
    	return (int) businessRuleId;
		
	}
  
}
