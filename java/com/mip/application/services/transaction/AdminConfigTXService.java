package com.mip.application.services.transaction;

import com.mip.application.dal.managers.AdminConfigManager;
import com.mip.application.view.AdminConfigVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.StringUtil;

/**
 * This class injects Transaction into the Service layer pertaining to 
 * AdminConfig use-case model. 
 * 
 * @author T H B S
 */
public class AdminConfigTXService {
	
	/**
	 * An instance of logger.
	 */	
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			AdminConfigTXService.class);
	
	
	/**
	 * Set inversion of Control for <code>AdminConfigManager</code>.
	 */
	private AdminConfigManager adminConfigManager;

	public void setAdminConfigManager(AdminConfigManager adminConfigManager) {
		this.adminConfigManager = adminConfigManager;
	}

	/**
	 * Saves the Config details in DB.
	 * 
	 * @param maxIdleCount
	 * @throws MISPException
	 */
	public void saveConfigDetails(AdminConfigVO adminConfigVO) 
		throws MISPException{
		
		Object[] params = {adminConfigVO};
		logger.entering("saveConfigDetails",params);
		
		try {	
			adminConfigManager.saveConfigDetails(
					PlatformConstants.DEFAULT_PASSWORD, 
					adminConfigVO.getDefaultPwd());	
			adminConfigManager.saveConfigDetails(
					PlatformConstants.USER_LOGIN_PREFIX, 
					adminConfigVO.getUserLoginPrefix());
			adminConfigManager.saveConfigDetails(
					PlatformConstants.PASSWORD_HISTORY_LIMIT, 
					String.valueOf(adminConfigVO.getPwdHistoryLimit()));
			adminConfigManager.saveConfigDetails(
					PlatformConstants.MAX_LOGIN_ATTEMPTS, 
					String.valueOf(adminConfigVO.getMaxLoginAttempts()));
			adminConfigManager.saveConfigDetails(
					PlatformConstants.MAX_IDLE_COUNT, 
					String.valueOf(adminConfigVO.getMaxIdleCount()));
			adminConfigManager.saveConfigDetails(
					PlatformConstants.REGISTER_CUSTOMER_WS_URL, 
					adminConfigVO.getRegisterCustomerWSURL());	
			adminConfigManager.saveConfigDetails(
					PlatformConstants.ANNOUNCEMENT_MESSAGE, 
					adminConfigVO.getAnnouncementMessage());
			adminConfigManager.saveConfigDetails(
					PlatformConstants.OFFER_SUBSCRIPTION_LAST_DAY, 
					String.valueOf(adminConfigVO.getOfferSubscriptionLastDay()));
			adminConfigManager.saveConfigDetails(
					PlatformConstants.MSISDN_CODES, 
					adminConfigVO.getMsisdnCodes());
			/*adminConfigManager.saveConfigDetails(
					PlatformConstants.OFFER_UNSUBSCRIBE_WS_URL, 
					adminConfigVO.getOfferUnsubscribeWSURL());
			adminConfigManager.saveConfigDetails(
					PlatformConstants.REMOVE_CUSTOMER_REGISTRATION_WS_URL, 
					adminConfigVO.getRemoveCustomerRegistrationWSURL());*/
			adminConfigManager.saveConfigDetails(
					PlatformConstants.COMMISSION_PERCENTAGE, 
					StringUtil.trim(adminConfigVO.getCommissionPercent()));
			
			if(adminConfigVO.getDefaultOfferAssigned() != null && 
				adminConfigVO.getDefaultOfferAssigned().trim().length() == 0){
				
				adminConfigVO.setDefaultOfferAssigned(null);
			}
			/*adminConfigManager.saveConfigDetails(
					PlatformConstants.DEFAULT_OFFER_ASSIGNED, 
					adminConfigVO.getDefaultOfferAssigned());*/
			
		} catch (DBException e) {
			logger.error("Exception occured while saving Config details " +
					"in DB.", e);
			throw new MISPException(e);
		}
		
		logger.exiting("saveConfigDetails");
	}
}
