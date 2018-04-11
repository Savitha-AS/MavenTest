package com.mip.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.BusinessRuleMaster;
import com.mip.application.model.ProductCoverDetails;
import com.mip.application.model.ProductDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.services.ProductsManagementService;
import com.mip.application.view.OfferCreateVO;
import com.mip.application.view.OfferMgmtVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.StringUtil;

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
 * <code>OffersManagementController.java</code> contains all the methods pertaining to  
 * offers management module. This controller extends the <code>BasePlatformController</code>
 * </p>
 * 
 * @author T H B S
 *
 */
public class OffersManagementController extends BasePlatformController {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			OffersManagementController.class);
	/**
     * An instance of <code>OffersManagementService</code>.
     */
	private ProductsManagementService productsMgmtService;

    /**
     * 
     * @param productsMgmtService
     *              setter method for offersMgmtService
     */
	public void setProductsMgmtService(ProductsManagementService productsMgmtService) {
		this.productsMgmtService = productsMgmtService;
	}
	
	/**
	 * This method retrieves all the offers from the MISP system.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView retrieveOffers(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		
		logger.entering("retrieveOffers");
		
		ModelAndView mavObj = null;
		List<ProductDetails> productDetails = null;
		
		try{
			
			productDetails = this.productsMgmtService.retrieveOffers();
			
			mavObj = new ModelAndView(MAVPaths.VIEW_OFFER_ASSIGN);
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, productDetails);
			
			httpServletRequest.getSession().setAttribute(MAVObjects.LIST_PRODUCTS, productDetails);
				
		}catch (MISPException exception) {
			
			logger.error("An exception occured while retrieving Offer Details.",
					exception);
			
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_ASSIGN_OFFER);
			
			return mavObj;
			
		}catch (Exception e) {
			
			logger.error("An exception occured while retrieving Offer Details.",e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
		
		logger.exiting("retrieveOffers",mavObj);
		
		return mavObj;
		
	}
	
	/**
	 * This method assigns the selected offer to all the active customers.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @param offerMgmtVO,<code>OfferMgmtVO</code> holding the offer details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView assignOffer(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, OfferMgmtVO offerMgmtVO) {
			
		logger.entering("assignOffer", offerMgmtVO);
		
		ModelAndView mavObj = null;
		
		try{
			
			this.productsMgmtService.assignOffer(offerMgmtVO);
			
			mavObj = super.success(SuccessMessages.OFFER_ASSIGNED,
					MAVPaths.URL_ASSIGN_OFFER);
			
			httpServletRequest.getSession().removeAttribute(MAVObjects.LIST_PRODUCTS);
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while assigning Offer.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_ASSIGN_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while assigning Offer.",e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
		
		logger.exiting("assignOffer",mavObj);
		
		return mavObj;
		
	}
	
	/**
	 * This method retrives the number of customers registered between the given dates.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @param offerMgmtVO,<code>OfferMgmtVO</code> holds the search criteria.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView fetchRegCustomersBwnDates(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, OfferMgmtVO offerMgmtVO) throws MISPException {
				
		logger.entering("fetchRegCustomersBwnDates", offerMgmtVO);
		
		ModelAndView mavObj=null;;
		
		try{
			long count = this.productsMgmtService.fetchRegCustomersBwnDates(offerMgmtVO);
			
			List<ProductDetails> productDetails = (List<ProductDetails>) httpServletRequest.getSession().
			            getAttribute(MAVObjects.LIST_PRODUCTS);
			
			mavObj = new ModelAndView(MAVPaths.VIEW_OFFER_ASSIGN);
			mavObj.addObject(MAVObjects.CUSTOMER_COUNT, count);
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, productDetails);
			mavObj.addObject(MAVObjects.VO_ASSIGN_OFFER, offerMgmtVO);
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while fetching customers registered between given dates .",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_ASSIGN_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while fetching customers registered between given dates .",e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("fetchRegCustomersBwnDates",mavObj);
		
		return mavObj;
		
	}
	
	
	/**
	 * This method retrives offer types and direct the user to create offer page.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView loadCreateOffer(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws MISPException {
				
		logger.entering("loadCreateOffer");
		
		ModelAndView mavObj=null;;
//		List<OfferType> offerTypes = null;
		BusinessRuleMaster busRuleMaster = null;
		
		try{
//			offerTypes = this.productsMgmtService.retrieveOfferTypes();
			busRuleMaster = this.productsMgmtService.retrieveActiveBusinessRule();
			
			mavObj = new ModelAndView(MAVPaths.VIEW_OFFER_CREATE);
//			mavObj.addObject(MAVObjects.LIST_OFFERS_TYPES, offerTypes);
			mavObj.addObject(MAVObjects.ACTIVE_BUSINESS_RULE, busRuleMaster);
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while loading create offer page.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_CREATE_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while loading create offer page.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("loadCreateOffer",mavObj);
		
		return mavObj;
		
	}
	
	/**
	 * This method is to save the new offer details in MISP system.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @param offerCreateVO,<code>OfferCreateVO</code> holds offer details.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView saveOfferDetails(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,  OfferCreateVO offerCreateVO) throws MISPException
	{
		
		logger.entering("saveOfferDetails", offerCreateVO);
				
		ModelAndView mavObj = null;
		
		try{
			HttpSession session = httpServletRequest.getSession(false);
			
			UserDetails user = (UserDetails) session.getAttribute(SessionKeys.SESSION_USER_DETAILS);
			
			boolean isSaved = this.productsMgmtService.saveOfferDetails(offerCreateVO, user);
					
			if(isSaved){
				mavObj = super.success(SuccessMessages.OFFER_SAVED,
						MAVPaths.URL_CREATE_OFFER);
			}else {
				mavObj = super.error(FaultMessages.OFFER_INVALID_NAME,
						MAVPaths.URL_CREATE_OFFER);
			}
					
		}catch (MISPException exception) {
			
			logger.error("An exception occured while creating new offer.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_CREATE_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while creating new offer.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("saveOfferDetails",mavObj);
		
		return mavObj;
	}
		
	/**
	 * This method is for revoking the offer from the customers.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView loadRevokeOffer(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws MISPException {
				
		logger.entering("loadRevokeOffer");
		
		ModelAndView mavObj=null;
		List<ProductDetails> productDetails = null;
		
		try{
			
            productDetails = this.productsMgmtService.retrieveOffers();
			
			mavObj = new ModelAndView(MAVPaths.VIEW_OFFER_REVOKE);
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, productDetails);

			httpServletRequest.getSession().setAttribute(MAVObjects.LIST_PRODUCTS, productDetails);
		
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while loading revokeoffer page.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_REVOKE_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while loading revokeoffer page.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("loadRevokeOffer",mavObj);
		
		return mavObj;
		
	}
	
	/**
	 * This method retrieves the customers assigned to an offer.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @param offerMgmtVO,<code>OfferMgmtVO</code> holds offer details.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView fetchCustomersBasedOnOffer(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, OfferMgmtVO offerMgmtVO) throws MISPException {
				
		logger.entering("fetchCustomersBasedOnOffer", offerMgmtVO);
		
		ModelAndView mavObj=null;
		List<ProductDetails> productDetails = null;
						
		try{
			
			long count = this.productsMgmtService.fetchCustomersBasedOnOffer(offerMgmtVO);
			
			mavObj = new ModelAndView(MAVPaths.VIEW_OFFER_REVOKE);
			
			productDetails = (List<ProductDetails>) httpServletRequest.getSession().
			              getAttribute(MAVObjects.LIST_PRODUCTS);
			
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, productDetails);
			mavObj.addObject(MAVObjects.CUSTOMER_COUNT, count);
			mavObj.addObject(MAVObjects.VO_ASSIGN_OFFER, offerMgmtVO);

			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while retrieving the customers assigned to an offer.",
					exception);
		    mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_REVOKE_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while retrieving the customers assigned to an offer.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("fetchCustomersBasedOnOffer",mavObj);
		
		return mavObj;
		
	}
	
	
	/**
	 * This method retrieves the customers assigned to an offer.
	 * 
	 * @param httpServletRequest , An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse , An instance of <code>HttpServletResponse</code>.
	 * @param offerMgmtVO , <code>OfferMgmtVO</code> holds MSISDN details.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code> , if any error occurs.
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView fetchCustomersBasedOnMSISDN(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, OfferMgmtVO offerMgmtVO) throws MISPException {
				
		logger.entering("fetchCustomersBasedOnMSISDN",offerMgmtVO);
		
		ModelAndView mavObj=null;
		List<ProductDetails> productDetails = null;
		
		offerMgmtVO.setMsisdnCSV(StringUtil.trim(offerMgmtVO.getMsisdnCSV()));
		try{
			
			long count = this.productsMgmtService.fetchCustomersBasedOnMSISDN(offerMgmtVO);
			mavObj = new ModelAndView(MAVPaths.VIEW_OFFER_REVOKE);
			
			productDetails = (List<ProductDetails>) httpServletRequest.getSession().
			              getAttribute(MAVObjects.LIST_PRODUCTS);
			
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, productDetails);
			mavObj.addObject(MAVObjects.CUSTOMER_COUNT, count);
			mavObj.addObject(MAVObjects.VO_ASSIGN_OFFER, offerMgmtVO);

		
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while retrieving the customers assigned to an offer.",
					exception);
		    mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_REVOKE_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while retrieving the customers assigned to an offer.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
				
		logger.exiting("fetchCustomersBasedOnMSISDN",mavObj);
		
		return mavObj;
		
	}
	
	
	
	/**
	 * This method revokes the selected offer from the customers.
	 * 
	 * @param httpServletRequest , An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse , An instance of <code>HttpServletResponse</code>.
	 * @param offerMgmtVO , <code>OfferMgmtVO</code> holding the offer details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code> , if any error occurs.
	 */
	public ModelAndView revokeOffer(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, OfferMgmtVO offerMgmtVO) {
				
		logger.entering("revokeOffer", offerMgmtVO);
		
		ModelAndView mavObj = null;
		
		try{
			
			this.productsMgmtService.revokeOffer(offerMgmtVO);
			
			mavObj = super.success(SuccessMessages.OFFER_REVOKED, 
					MAVPaths.URL_REVOKE_OFFER);
			httpServletRequest.getSession().removeAttribute(MAVObjects.LIST_PRODUCTS);
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while revoking Offer.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_REVOKE_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while revoking Offer.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
		
		logger.exiting("revokeOffer",mavObj);
		
		return mavObj;
		
	}

	
	/**
	 * This method revokes offers from the customers based on the input MSISDN
	 * 
	 * @param httpServletRequest , An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse , An instance of <code>HttpServletResponse</code>.
	 * @param offerMgmtVO , <code>OfferMgmtVO</code> holding the MSISDN details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code> , if any error occurs.
	 */
	public ModelAndView revokeOfferForMSISDN(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, OfferMgmtVO offerMgmtVO) {
				
		logger.entering("revokeOfferForMSISDN", offerMgmtVO);
		
		ModelAndView mavObj = null;
		
		try{
			offerMgmtVO.setMsisdnCSV(StringUtil.trim(offerMgmtVO.getMsisdnCSV()));
			this.productsMgmtService.revokeOfferForMSISDN(offerMgmtVO);
			
			mavObj = super.success(SuccessMessages.OFFER_REVOKED, 
					MAVPaths.URL_REVOKE_OFFER);
			httpServletRequest.getSession().removeAttribute(MAVObjects.LIST_PRODUCTS);
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while revoking Offer.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_REVOKE_OFFER);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while revoking Offer.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
		
		logger.exiting("revokeOfferForMSISDN",mavObj);
		
		return mavObj;
		
	}
	
	
	/**
	 * This method retrieve the offers and directs the user to listoffers page.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView listOffers(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
				
		logger.entering("listOffers");
		
		ModelAndView mavObj = null;
		List<ProductDetails> offList = null;
		
		try{
			
			offList = this.productsMgmtService.retrieveOffersWithOfferType();
			
			mavObj = new ModelAndView(MAVPaths.VIEW_OFFER_LIST);
			
			mavObj.addObject(MAVObjects.LIST_PRODUCTS, offList);
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while loading listoffers page.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_LIST_OFFERS);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while loading listoffers page.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
		
		logger.exiting("listOffers",mavObj);
		
		return mavObj;
		
	}
	
	
	/**
	 * This method retrieve the offer details from the MISP system.
	 * 
	 * @param httpServletRequest, An instance of <code>HttpServletRequest</code>.
	 * @param httpServletResponse, An instance of <code>HttpServletResponse</code>.
	 * @param offerCreateVO,<code>OfferCreateVO</code> holds the offer details
	 * @return <code>ModelAndView</code> , a view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView viewOffer(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, OfferCreateVO offerCreateVO) {
				
		logger.entering("viewOffer", offerCreateVO);
		
		ModelAndView mavObj = null;
		List<ProductCoverDetails> productCoverDetails = null;
		
		try{
			
			String offerId = httpServletRequest.getParameter("offerId");
			
			productCoverDetails = this.productsMgmtService
					.retrieveOfferCoverBasedOnOfferId(new Integer(offerId));
		    			
			mavObj = new ModelAndView(MAVPaths.VIEW_OFFER_DETAILS);
			mavObj.addObject(MAVObjects.VO_CREATE_OFFER, offerCreateVO);
			mavObj.addObject(MAVObjects.LIST_OFFERCOVER_DETAILS, productCoverDetails);
			
		}catch (MISPException exception) {
			
			logger.error("An exception occured while getting offer details.",
					exception);
			mavObj = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.URL_LIST_OFFERS);
			return mavObj;
			
		} catch (Exception e) {
			
			logger.error("An exception occured while getting offer details.", e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
			return mavObj;
			
		}
		
		logger.exiting("viewOffer",mavObj);
		
		return mavObj;
		
	}

}
