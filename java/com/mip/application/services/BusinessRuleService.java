package com.mip.application.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.mip.application.dal.managers.BusinessRuleMasterManager;
import com.mip.application.dal.managers.InsuranceProviderManager;
import com.mip.application.dal.managers.OfferCoverDetailsManager;
import com.mip.application.dal.managers.OfferDetailsManager;
import com.mip.application.model.BusinessRuleDefinition;
import com.mip.application.model.BusinessRuleMaster;
import com.mip.application.model.InsuranceCompany;
import com.mip.application.model.ProductCoverDetails;
import com.mip.application.model.ProductDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.model.mappings.BusinessRuleMappings;
import com.mip.application.view.BusinessRuleOfferVO;
import com.mip.application.view.BusinessRuleVO;
import com.mip.application.view.OfferCreateVO;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
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
 * <code>BusinessRuleService.java</code> contains all the service layer
 * methods pertaining to BusinessRules module.
 * </p>
 * 
 * @author T H B S
 * 
 */

public class BusinessRuleService {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BusinessRuleService.class);
	
	/**
     * An instance of <code>InsuranceProviderManager</code>.
     */
	private InsuranceProviderManager insuranceProviderManger;
	
	/**
     * An instance of <code>BusinessRuleMasterManager</code>.
     */
	private BusinessRuleMasterManager busRuleMasterManager;
	
	/**
     * An instance of <code>OfferDetailsManager</code>.
     */
	private OfferDetailsManager offerDetailsMgr;
	
	/**
     * An instance of <code>OfferCoverDetailsManager</code>.
     */
	private OfferCoverDetailsManager offCoverDetailsManager;
		
	/**
     * @param offCoverDetailsManager
     *              setter method for offCoverDetailsManager
     */
	public void setOffCoverDetailsManager(
			OfferCoverDetailsManager offCoverDetailsManager) {
		this.offCoverDetailsManager = offCoverDetailsManager;
	}
	
	/**
     * @param offerDetailsMgr
     *              setter method for offerDetailsMgr
     */
	public void setOfferDetailsMgr(OfferDetailsManager offerDetailsMgr) {
		this.offerDetailsMgr = offerDetailsMgr;
	}

	/**
    * @param insuranceProviderManger
    *              setter method for insuranceProviderManger
    */
	public void setInsuranceProviderManger(
			InsuranceProviderManager insuranceProviderManger) {
		this.insuranceProviderManger = insuranceProviderManger;
	}
	
	/**
	 * @param busRuleMasterManager
	 *              setter method for busRuleMasterManager
	 */
	public void setBusRuleMasterManager(
			BusinessRuleMasterManager busRuleMasterManager) {
		this.busRuleMasterManager = busRuleMasterManager;
	}
	
	/**
	 * This method retrieves the list of the Insurance companies.
	 * 
	 * @return <code>ArrayList</code> of the Insurance Company details.
	 */
	public List<InsuranceCompany> listInsuranceProviders() throws MISPException{
		
		logger.entering("listInsuranceProviders");
		List<InsuranceCompany> insuranceCompanyList = null;
		
		try{
			
			insuranceCompanyList = this.insuranceProviderManger.listInsuranceProviders();
			
		}catch (DBException exception) {
			
			logger.error("An exception occured while retrieving Insurance providers.",
					exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("listInsuranceProviders");
		return insuranceCompanyList;
	}
	
	/**
	 * This method retrieves the list of the Insurance companies.
	 * 
	 * @param businessRuleVO, the business rule definition
	 * @throws <code>MISPException</code> if any error occurs.
	 */
	public void saveBusinessRule(BusinessRuleVO businessRuleVO, UserDetails user) throws MISPException{
		
		logger.entering("saveBusinessRule", businessRuleVO);
		
		BusinessRuleMaster businessRuleMaster = null;
		
		try{
							
			StringBuilder dateString = new StringBuilder("");
			
			Calendar cal = Calendar.getInstance();

			int year = cal.get(Calendar.YEAR);

			int month = cal.get(Calendar.MONTH)+1;

			int currdate = cal.get(Calendar.DATE);
			
			dateString.append(year);
			
			if(month <= 9)
				dateString.append("0");
			
			dateString.append(month);
			
			if(currdate <= 9)
				dateString.append("0");
			
			dateString.append(currdate);
					
			//Map the BusinessRuleVO to BusinessRuleMaster model
			businessRuleMaster = BusinessRuleMappings.mapBusinessRuleVOToBusinessRuleModel(businessRuleVO);
		
			businessRuleMaster.setCreatedBy(user);
			
			int busRuleId = this.busRuleMasterManager.retrieveMaxIdFromBusinessMaster();
			
			busRuleId = busRuleId+1;
			
			if(busRuleId <= 9)
				dateString.append("_000").append(busRuleId);
			else if(busRuleId > 9 && busRuleId <= 99)
				dateString.append("_00").append(busRuleId);
			else
				dateString.append("_0").append(busRuleId);
			
			businessRuleMaster.setBrVersion("BR_"+dateString);
						
			this.busRuleMasterManager.saveBusinessRule(businessRuleMaster);
			
		}catch (DBException exception) {
			
			logger.error("An exception occured while saving the business rule.",
					exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("saveBusinessRule");
	
	}
		
	/**
	 * This method lists all the Business Rule versions in the MISP system.
	 * 
	 * @return <code>List<BusinessRuleMaster></code>, of business rule versions
	 * @throws <code>MISPException</code> if any error occurs.
	 */
	public ArrayList<BusinessRuleMaster> listBusinessRules() throws MISPException{
		
		logger.entering("listBusinessRules");
		
		ArrayList<BusinessRuleMaster> businessRulesList = null;
		
		try{
		
			businessRulesList = this.busRuleMasterManager.listBusinessRules();
			
		}catch (DBException exception) {
			
			logger.error("An exception occured while retrieving the business rule versions.",
					exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("listBusinessRules");
		
		return businessRulesList;
	
	}
	
	
	/**
	 * This method returns all the Multiplecover offers in the MISP system.
	 * 
	 * @return <code>List<OfferDetails></code> of offer details
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public List<ProductDetails> retrieveMultipleCoverOffers() throws MISPException{
		
		logger.entering("retrieveMultipleCoverOffers");
		
		List<ProductDetails> productsList = null;
		
		try{
			
			productsList = this.offerDetailsMgr.retrieveMultipleCoverOffers();
			
		}catch (DBException exception) {
			
			logger.error("An exception occured while retrieving Offers of type MultipleCover .",
					exception);
			throw new MISPException(exception);
		}
    	
    	logger.exiting("retrieveMultipleCoverOffers", productsList);
    	
    	return productsList;

	}
	
	
	 /**
	 * This method returns Offercover details for the given offerid. 
	 * 
	 * @return <code>OfferCoverDetails</code> of offercover details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public List<ProductCoverDetails> retrieveOfferCoverBasedOnOfferId(int offerId) throws MISPException{
        	
    	logger.entering("retrieveOfferCoverBasedOnOfferId", offerId);
    	
    	List<ProductCoverDetails> productCoverDetails= null;
    	
    	try{
    		productCoverDetails = this.offCoverDetailsManager.retrieveOfferCoverBasedOnOfferId(offerId);
    		
    	}catch (DBException e) {
    		logger.error("Exception occured while retrieving OfferCover Details.", e);
			throw new MISPException(e);
		}
    	
    	logger.exiting("retrieveOfferCoverBasedOnOfferId", productCoverDetails);
    	
    	return productCoverDetails;
     }
    
    
    /**
	 * This method returns the active BusinessRule in the MISP system.
	 *  
	 * @return <code>BusinessRuleMaster</code> , active business rule
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public BusinessRuleMaster retrieveActiveBusinessRule() throws MISPException{
    	 	
    	logger.entering("retrieveActiveBusinessRule");
    	
    	BusinessRuleMaster businessRuleMaster = null;
    	
    	try{
    		
    		businessRuleMaster = this.busRuleMasterManager.retrieveActiveBusinessRule();
    		
    	}catch (DBException e) {
    		logger.error("Exception occured while retrieving active business rule.", e);
			throw new MISPException(e);
		}
       	
    	logger.exiting("retrieveActiveBusinessRule", businessRuleMaster);
    	
    	return businessRuleMaster;
      	
    }
   
    /**
	 * This method returns the business rule based on the business rule id.
	 * 
	 * @param busRuleID, business rule id.
	 * @return <code>BusinessRuleMaster</code> ,with business rule details.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public BusinessRuleMaster retrieveBusinessRuleBasedOnId(Integer busRuleID) throws MISPException{
    	    	
    	logger.entering("retrieveBusinessRuleBasedOnId", busRuleID);
    	
    	BusinessRuleMaster businessRuleMaster = null;
    	
    	try{
    		
    		businessRuleMaster = this.busRuleMasterManager.retrieveBusinessRuleBasedOnId(busRuleID);
    		
    	}catch (DBException e) {
    		logger.error("Exception occured while retrieving business rule.", e);
			throw new MISPException(e);
		}
    	
    	logger.exiting("retrieveBusinessRuleBasedOnId", businessRuleMaster);
    	
    	return businessRuleMaster;
 
    }
      
    /**
	 * This method retrieves the offerdetails and the linked business rule.
	 * 
	 * @param <code>OfferCreateVO</code> offer details.
	 * @param offerId, offer id to retrieve.
	 * @param <code>BusinessRuleMaster</code> holds the business rule details.
	 * @return <code>BusinessRuleOfferVO</code> ,with offer and business rule details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public BusinessRuleOfferVO viewOffer(OfferCreateVO offerCreateVO, String offerId,
    		BusinessRuleMaster businessRule) throws MISPException{
    	  	
    	logger.entering("viewOffer", offerCreateVO);
    	
    	List<ProductCoverDetails> productCoverDetails = null;
		List<BusinessRuleDefinition> futureActiveBusRuleDefList = null;
		BusinessRuleOfferVO businessRuleOfferVO = null;
    	
    	try{
			
			productCoverDetails = this.offCoverDetailsManager
					.retrieveOfferCoverBasedOnOfferId(new Integer(offerId));
					
			futureActiveBusRuleDefList = businessRule.getBusRuleDef();
			
			int multiplier = new Integer(offerCreateVO.getMultiValue()).intValue();
			
			businessRuleOfferVO = new BusinessRuleOfferVO();
			businessRuleOfferVO.setOfferId(new Integer(offerId));
			businessRuleOfferVO.setOfferName(offerCreateVO.getOfferName());
			businessRuleOfferVO.setOfferType(offerCreateVO.getOfferType());
			businessRuleOfferVO.setMultiValue(offerCreateVO.getMultiValue());
			
			List<BusinessRuleDefinition> busRuleDefList = new ArrayList<BusinessRuleDefinition>();
			
			BusinessRuleDefinition oldBusRuleDef = null;
			BusinessRuleDefinition busRuleDefinition = null;
			
			for(int i = 0; futureActiveBusRuleDefList != null && 
			      i < futureActiveBusRuleDefList.size();i++){
				
				busRuleDefinition = new BusinessRuleDefinition();
				oldBusRuleDef = futureActiveBusRuleDefList.get(i);
				
				busRuleDefinition.setBrRangeFrom(oldBusRuleDef.getBrRangeVal());
				busRuleDefinition.setBrRangeTo(oldBusRuleDef.getBrRangeVal()*multiplier);
				
				if(productCoverDetails != null && i < productCoverDetails.size())
				  busRuleDefinition.setBrRangeVal(productCoverDetails.get(i).getCoverCharges());
				else{
					busRuleDefinition.setBrRangeVal(-1);
				}
				
				busRuleDefList.add(busRuleDefinition);
			}
			
			businessRuleOfferVO.setBusRuleDefList(busRuleDefList);
    		
    	}catch (DBException e) {
    		logger.error("Exception occured while retrieving offer details.", e);
			throw new MISPException(e);
		}
    	
    	
    	logger.exiting("viewOffer", businessRuleOfferVO);
    	
    	return businessRuleOfferVO;
    	
    	
    }   
    
    /**
	 * This method retrieves the business rule details.
	 * 
	 * @param busRuleId, business rule id to retrieve.
	 * @param insCompany, insurance company.
	 * @return <code>BusinessRuleMaster</code> ,with business rule details
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public BusinessRuleMaster viewBusinessRule(Integer busRuleId, String insCompany) throws MISPException{
    	  	
    	logger.entering("viewBusinessRule", busRuleId, insCompany);
    	
    	BusinessRuleMaster businessRuleMaster = null;
    	    	
    	try{
			
    		businessRuleMaster = this.busRuleMasterManager
					.retrieveBusinessRuleBasedOnId(busRuleId);
    		
    		if(businessRuleMaster != null){
    			
    			InsuranceCompany insuranceCompany = new InsuranceCompany();
        		insuranceCompany.setInsCompName(insCompany);
        		
        		businessRuleMaster.setInsuranceCompany(insuranceCompany);
    			
    		}
    	}catch (DBException e) {
    		logger.error("Exception occured while retrieving business rule details.", e);
			throw new MISPException(e);
		}
    	
    	logger.exiting("viewBusinessRule", businessRuleMaster);
    	
    	return businessRuleMaster;
   	
    }
    
    /**
     * Used for AJAX calls to check if selected Business Rule for Activation 
     * is same as the active Business Rule.
     * 
     * @param selectedBusRuleId selected Business Rule for Activation.
     * 
     * @return Boolean Flag
     */
    public boolean isSelectedBRActive(String selectedBusRuleId){    	  	
    	logger.entering("isSelectedBRActive", selectedBusRuleId);
    	
		try {	
			
			BusinessRuleMaster selectedBusinessRule = this.
				retrieveBusinessRuleBasedOnId(new Integer(selectedBusRuleId));

	    	BusinessRuleMaster activeBusinessRule = retrieveActiveBusinessRule();
	    				
			if(selectedBusinessRule.equals(activeBusinessRule)){
				
		    	logger.exiting("isSelectedBRActive", true);
				return true;
			}
			
		} catch (NumberFormatException e) {
			logger.error("Exception occured while converting selectedBusRuleId " 
					+ "to Integer.", e);
		} catch (MISPException e) {
			logger.error("Exception occured while retrieving Business Rules " 
					+ "from DB.", e);
		}
		
    	logger.exiting("isSelectedBRActive", false);
		return false;
    }
 
}
