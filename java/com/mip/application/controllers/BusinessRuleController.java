package com.mip.application.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.BusinessRuleMaster;
import com.mip.application.model.InsuranceCompany;
import com.mip.application.model.ProductDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.model.mappings.OfferMappings;
import com.mip.application.services.BusinessRuleService;
import com.mip.application.services.transaction.BusinessRuleTXService;
import com.mip.application.view.BusinessRuleOfferVO;
import com.mip.application.view.BusinessRuleVO;
import com.mip.application.view.OfferCreateVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.controllers.BasePlatformController;
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
 * <code>BusinessRuleController.java</code> contains all the methods pertaining to  
 * Business Rules module. This controller extends the <code>BasePlatformController</code>
 * </p>
 * 
 * @author T H B S
 *
 */

public class BusinessRuleController extends BasePlatformController {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			BusinessRuleController.class);
	
	/**
     * An instance of <code>BusinessRuleService</code>.
     */
	private BusinessRuleService businessRulService;

    /**
     * @param businessRulService
     *              setter method for businessRulService
     */
	public void setBusinessRulService(BusinessRuleService businessRulService) {
		this.businessRulService = businessRulService;
	}
	
	/**
     * An instance of <code>BusinessRuleTXService</code>.
     */
	private BusinessRuleTXService businessRuleTXService;
	
	/**
     * @param businessRuleTXService
     *              setter method for businessRuleTXService
     */	
	public void setBusinessRuleTXService(BusinessRuleTXService businessRuleTXService) {
		this.businessRuleTXService = businessRuleTXService;
	}

	/**
	 * This method loads the Business Rule creation page.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView loadBusinessRule(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws MISPException {
				
		logger.entering("loadBusinessRule");
		
		ModelAndView mavObj=null;;
		List<InsuranceCompany> insuranceCompanyList = null;
		
		try{
			//Retrieve the list of insurance providers.
			insuranceCompanyList = this.businessRulService.listInsuranceProviders();
			
			mavObj = new ModelAndView(MAVPaths.VIEW_BUSINESS_RULE_ADD);
			mavObj.addObject(MAVObjects.LIST_INSURANCE_COMPANY, insuranceCompanyList);
			
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while fetching insurance providers.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_BR_CREATE_BR);
			return mavObj;
			
		}catch (Exception e) {
			
			logger.error("An exception occured while fetching insurance providers.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("loadBusinessRule",mavObj);
		
		return mavObj;
		
	}
	
	/**
	 * This method creates new Business Rule in the system.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @param businessRuleVO, An instance of <code>BusinessRuleVO</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView saveBusinessRule(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, BusinessRuleVO businessRuleVO) throws MISPException {
				
		logger.entering("saveBusinessRule",businessRuleVO);
		
		ModelAndView mavObj=null;;
				
		try{
			HttpSession session = httpServletRequest.getSession(false);
			
			if(session != null){
				
				UserDetails user = (UserDetails)session.getAttribute(SessionKeys.SESSION_USER_DETAILS);
				
				this.businessRulService.saveBusinessRule(businessRuleVO, user);
			}
			
			mavObj = super.success(SuccessMessages.BUSINESS_RULE_SAVED,
					MAVPaths.URL_BR_CREATE_BR);
						
		}catch (MISPException exception) {
			
			logger.error("An exception occured while saving business rule details.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_BR_CREATE_BR);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while saving business rule details.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("saveBusinessRule",mavObj);
		
		return mavObj;
		
	}
	
	
	/**
	 * This method lists all the Business Rule versions in the MISP system.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView listBusinessRules(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws MISPException {
				
		logger.entering("listBusinessRules");
		
		ModelAndView mavObj=null;;
		ArrayList<BusinessRuleMaster> businessRulesList = null;
				
		try{
			
			HttpSession session = httpServletRequest.getSession(false);
			
			//Clear the session
			if(session != null){
				session.removeAttribute(SessionKeys.SESSION_BR_OFFER_DETAILS);
				session.removeAttribute(SessionKeys.SESSION_ACTIVE_BUSINESS_RULE);
				session.removeAttribute(SessionKeys.SESSION_FUTURE_ACTIVE_BUSINESS_RULE);
			}
					
			businessRulesList = this.businessRulService.listBusinessRules();			
			
			mavObj = new ModelAndView(MAVPaths.VIEW_BUSINESS_RULE_LIST);
			
			mavObj.addObject(MAVObjects.LIST_BUSINESS_RULES,businessRulesList);
						
		}catch (MISPException exception) {
			
			logger.error("An exception occured while retrieving business rule versions.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_BR_LIST_BR);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while retrieving business rule versions.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("listBusinessRules",mavObj);
		
		return mavObj;
		
	}
	
	/**
	 * This method lists all the MultipleCover offers in the MISP system.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView listOffers(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws MISPException {
				
		logger.entering("listOffers");
		
		ModelAndView mavObj=null;;
		ArrayList<ProductDetails> offerDetailsList = null;
		BusinessRuleMaster activeBusinessRule = null;
		BusinessRuleMaster selectedBusinessRule = null;
				
		try{
			String selectedBusRuleId = httpServletRequest.getParameter("selectBR");
			
			logger.info("Business Rule Id selected for Activation : ", selectedBusRuleId);
				
			offerDetailsList = (ArrayList<ProductDetails>) 
			           this.businessRulService.retrieveMultipleCoverOffers();
			
	
			activeBusinessRule = this.businessRulService.retrieveActiveBusinessRule();
			
			httpServletRequest.getSession().setAttribute(SessionKeys.SESSION_ACTIVE_BUSINESS_RULE,
					activeBusinessRule);
	 
			selectedBusinessRule = this.businessRulService.
			              retrieveBusinessRuleBasedOnId(new Integer(selectedBusRuleId));
			

			httpServletRequest.getSession().setAttribute(SessionKeys.SESSION_FUTURE_ACTIVE_BUSINESS_RULE,
					selectedBusinessRule);
					
			mavObj = new ModelAndView(MAVPaths.VIEW_BUSINESS_RULE_LIST_OFFERS);
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, offerDetailsList);
			mavObj.addObject(MAVObjects.ACTIVE_BUSINESS_RULE, activeBusinessRule);
			mavObj.addObject(MAVObjects.FUTURE_ACTIVE_BUSINESS_RULE, selectedBusinessRule);
						
		}catch (MISPException exception) {
			logger.error("An exception occured while retrieving Offers.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_BR_LIST_BR);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while retrieving Offers.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("listOffers",mavObj);
		
		return mavObj;
		
	}
	
	/**
	 * This method retrieves the offer details from the MISP system.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @param offerCreateVO,<code>OfferCreateVO</code> holding the offer details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView viewOffer(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, OfferCreateVO offerCreateVO) {
				
		logger.entering("viewOffer",offerCreateVO);
		
		ModelAndView mavObj = null;
		BusinessRuleMaster selectedBusinessRule = null;
		BusinessRuleOfferVO businessRuleOfferVO = null;
		
		try{
			
			String offerId = httpServletRequest.getParameter("offerId");
									
			selectedBusinessRule =(BusinessRuleMaster) httpServletRequest.getSession().
			       getAttribute(SessionKeys.SESSION_FUTURE_ACTIVE_BUSINESS_RULE);
		
			businessRuleOfferVO = this.businessRulService.viewOffer(
					offerCreateVO, offerId, selectedBusinessRule);
								    			
			mavObj = new ModelAndView(MAVPaths.VIEW_BUSINESS_RULE_VIEW_OFFER);
			mavObj.addObject(MAVObjects.VO_BUSINESS_RULE_OFFER, businessRuleOfferVO);
					
		}catch (MISPException exception) {
			
			logger.error("An exception occured while retrieving offer details.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_BR_LIST_BR);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while retrieving offer details.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
		
		logger.exiting("viewOffer",mavObj);
		
		return mavObj;
		
	}
	
	
	/**
	 * This method is to save the Offer details.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @param offerCreateVO,<code>OfferCreateVO</code> holding the offer details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView saveOfferDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,  OfferCreateVO offerCreateVO) throws MISPException
	{
		
		logger.entering("saveOfferDetails", offerCreateVO);
				
		ModelAndView mavObj = null;
		HashMap<Integer, ProductDetails> offerDetailsMap = null;
		BusinessRuleMaster activeBusinessRule = null;
		BusinessRuleMaster selectedBusinessRule = null;
		ArrayList<ProductDetails> offerDetailsList = null;
		
		try{
			
			String offerId = httpServletRequest.getParameter("offerId");
			
			offerDetailsList = (ArrayList<ProductDetails>) 
	           this.businessRulService.retrieveMultipleCoverOffers();
			
			activeBusinessRule = (BusinessRuleMaster)httpServletRequest.getSession().
	            getAttribute(SessionKeys.SESSION_ACTIVE_BUSINESS_RULE);
	
	        selectedBusinessRule =(BusinessRuleMaster) httpServletRequest.getSession().
	            getAttribute(SessionKeys.SESSION_FUTURE_ACTIVE_BUSINESS_RULE);
			
			HttpSession session = httpServletRequest.getSession();
			
			offerDetailsMap = (HashMap) session.getAttribute(SessionKeys.SESSION_BR_OFFER_DETAILS);
			
			if(offerDetailsMap == null){
				
				offerDetailsMap = new HashMap<Integer, ProductDetails>();
			}
						
			ProductDetails productDetails = OfferMappings.mapOfferCreateVOToOfferDetailsModel(offerCreateVO);
			/*productDetails.setOfferId(new Integer(offerId).intValue());*/
			
			offerDetailsMap.put(new Integer(offerId).intValue(), productDetails);
			
			session.setAttribute(SessionKeys.SESSION_BR_OFFER_DETAILS, offerDetailsMap);
				
			mavObj = new ModelAndView(MAVPaths.VIEW_BUSINESS_RULE_LIST_OFFERS);
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, offerDetailsList);
			mavObj.addObject(MAVObjects.ACTIVE_BUSINESS_RULE, activeBusinessRule);
			mavObj.addObject(MAVObjects.FUTURE_ACTIVE_BUSINESS_RULE, selectedBusinessRule);
		
					
		}catch (MISPException exception) {
			
			logger.error("An exception occured while saving offer details.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_BR_LIST_BR);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while saving offer details.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("saveOfferDetails",mavObj);
		
		return mavObj;
	}
	
	/**
	 * This method is to save the offerdetails and activate the new business rule.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView activateBusinessRule(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws MISPException {
				
		logger.entering("activateBusinessRule");
		
		ModelAndView mavObj=null;;
		
		HashMap<String, ProductDetails> offerDetailsMap = null;
		
		BusinessRuleMaster activeBusinessRule = null;
		BusinessRuleMaster selectedBusinessRule = null;
		
		HttpSession session  = httpServletRequest.getSession(false);
				
		try{
			
			offerDetailsMap = (HashMap) session.getAttribute(SessionKeys.SESSION_BR_OFFER_DETAILS);
			
			activeBusinessRule = (BusinessRuleMaster)httpServletRequest.getSession().
                getAttribute(SessionKeys.SESSION_ACTIVE_BUSINESS_RULE);

            selectedBusinessRule =(BusinessRuleMaster) httpServletRequest.getSession().
                getAttribute(SessionKeys.SESSION_FUTURE_ACTIVE_BUSINESS_RULE);
			
			this.businessRuleTXService.activateBusinessRule(offerDetailsMap, 
					 activeBusinessRule, selectedBusinessRule);
			
			mavObj = super.success(SuccessMessages.BUSINESS_RULE_ACTIVATED,
					MAVPaths.URL_BR_LIST_BR);
								
		}catch (MISPException exception) {
			
			logger.error("An exception occured while saving Offers and activating the business rule.",
					exception);
			exception.printStackTrace();
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_BR_LIST_BR);
			return mavObj;
			
		}catch (Exception e) {
			
			logger.error("An exception occured while saving Offers and activating the business rule.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		} finally{
			
			if(session != null){
				session.removeAttribute(SessionKeys.SESSION_BR_OFFER_DETAILS);
				session.removeAttribute(SessionKeys.SESSION_ACTIVE_BUSINESS_RULE);
				session.removeAttribute(SessionKeys.SESSION_FUTURE_ACTIVE_BUSINESS_RULE);
			}
		}
		logger.exiting("activateBusinessRule",mavObj);
		
		return mavObj;
		
	}
	
	/**
	 * This method retrieves the Business Rule details from the MISP system.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView viewBusinessRule(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
				
		logger.entering("viewBusinessRule");
		
		ModelAndView mavObj = null;
		BusinessRuleMaster businessRule = null;
		
		try{
			
			String busRuleId = httpServletRequest.getParameter("busRuleId");
			String insComp = httpServletRequest.getParameter("insComp");
						
			businessRule = this.businessRulService.viewBusinessRule(new Integer(busRuleId), insComp);
			
			mavObj = new ModelAndView(MAVPaths.VIEW_BUSINESS_RULE);
			mavObj.addObject(MAVObjects.BUSINESS_RULE, businessRule);
					
		}catch (MISPException exception) {
			
			logger.error("An exception occured while retrieving business rule details.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_BR_LIST_BR);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while retrieving business rule details.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
		
		logger.exiting("viewBusinessRule",mavObj);
		
		return mavObj;
		
	}
}
