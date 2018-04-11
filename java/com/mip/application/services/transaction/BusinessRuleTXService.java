package com.mip.application.services.transaction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.mip.application.dal.managers.BusinessRuleMasterManager;
import com.mip.application.dal.managers.CustomerSubscriptionManager;
import com.mip.application.dal.managers.OfferCoverDetailsManager;
import com.mip.application.dal.managers.OfferDetailsManager;
import com.mip.application.model.BusinessRuleMaster;
import com.mip.application.model.ProductDetails;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * This class injects Transaction into the Service layer pertaining to 
 * Business Rule use-case model. 
 * 
 * @author T H B S
 */
public class BusinessRuleTXService {

	/**
	 * An instance of logger.
	 */	
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BusinessRuleTXService.class);

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
     * An instance of <code>CustomerSubscriptionManager</code>.
     */
	private CustomerSubscriptionManager customerSubsManager;
	
	/**
	 * @param busRuleMasterManager
	 *              setter method for busRuleMasterManager
	 */
    public void setBusRuleMasterManager(
			BusinessRuleMasterManager busRuleMasterManager) {
		this.busRuleMasterManager = busRuleMasterManager;
	}

    /**
     * @param offerDetailsMgr
     *              setter method for offerDetailsMgr
     */
	public void setOfferDetailsMgr(OfferDetailsManager offerDetailsMgr) {
		this.offerDetailsMgr = offerDetailsMgr;
	}

	/**
     * @param offCoverDetailsManager
     *              setter method for offCoverDetailsManager
     */
	public void setOffCoverDetailsManager(
			OfferCoverDetailsManager offCoverDetailsManager) {
		this.offCoverDetailsManager = offCoverDetailsManager;
	}
		
	/**
     * @param customerSubscriptionManager
     *              setter method for customerSubscriptionManager
     */
	public void setCustomerSubsManager(
			CustomerSubscriptionManager customerSubsManager) {
		this.customerSubsManager = customerSubsManager;
	}

	/**
	 * This method is to save the offerdetails and activate the new business rule.
	 * 
	 * @param <code>HashMap<String, OfferDetails></code>, hold the offers to update.
	 * @param <code>BusinessRuleMaster</code>, the current active business rule.
	 *  @param <code>BusinessRuleMaster</code>, the new business rule to be activated.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
    public void activateBusinessRule(HashMap<String, ProductDetails> offerDetailsMap,
			BusinessRuleMaster activeBusinessRule, BusinessRuleMaster newBusinessRule) throws MISPException {

		logger.entering("activateBusinessRule", offerDetailsMap,
				activeBusinessRule, newBusinessRule);
    	    	
    	try{
    		
    		Collection<ProductDetails> offerDetailsList =  offerDetailsMap.values();
    		
    		Iterator<ProductDetails> iterator = offerDetailsList.iterator();
    		
    		while(iterator.hasNext()){
    			
    			ProductDetails productDetails = (ProductDetails) iterator.next();
    			    			
    			ProductDetails existingOffer = this.offerDetailsMgr.fetch(productDetails.getProductId());
    			
    			this.offCoverDetailsManager.deleteOfferCoverDetails(existingOffer.getProductId());
    			
    			//Set the new Business Rule to the Offer
    			/*existingOffer.setBusinessRuleMaster(newBusinessRule);*/
    			
    			existingOffer.setProductCoverDetails(productDetails.getProductCoverDetails());
    		    			    			
    			this.offerDetailsMgr.saveOfferDetails(existingOffer);
    			
    			//Deactivate the active business rule
    			activeBusinessRule.setActive((byte) PlatformConstants.BUSINESS_RULE_INACTIVE);
    			
    			this.busRuleMasterManager.saveBusinessRule(activeBusinessRule);
    			
    			//activate the new business rule
    			newBusinessRule.setActive((byte) PlatformConstants.BUSINESS_RULE_ACTIVE);
    			
    			this.busRuleMasterManager.saveBusinessRule(newBusinessRule);
    			
    			this.customerSubsManager.updateCustomerBusinessRule(
    								newBusinessRule.getBrId());
    	
    		}
    		
    	}catch (DBException e) {
    		
    		logger.error("Exception occured while saving Offers and activating the business rule.", e);
			throw new MISPException(e);
			
		}
    	
    	logger.exiting("activateBusRule");
    
    }

}
