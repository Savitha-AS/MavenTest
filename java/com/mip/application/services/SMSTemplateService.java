package com.mip.application.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.mip.application.constants.FaultMessages;
import com.mip.application.dal.managers.SMSTemplateManager;
import com.mip.application.model.SmsTemplateMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.view.SMSPlaceHolderVO;
import com.mip.application.view.SMSTemplateVO;
import com.mip.framework.castor.CastorService;
import com.mip.framework.castor.SMSPlaceHolders;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.TypeUtil;


/**
 * <p>
 * <code>SMSTemplateService.java</code> contains all the methods pertaining to
 * SMS Template Management use case model. This is a service class for SMS
 * Template related modules.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class SMSTemplateService {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			SMSTemplateService.class);

	/**
	 * Set inversion of control for <code>SMSTemplateManager</code> and 
	 * <code>CastorService</code>
	 */
	private SMSTemplateManager smsTemplateManager;	
	private CastorService castorService;

	/**
	 * @param smsTemplateManager
	 *            the smsTemplateManager to set
	 */
	public void setSmsTemplateManager(SMSTemplateManager smsTemplateManager) {
		this.smsTemplateManager = smsTemplateManager;
	}

	/**
	 * @param castorService the castorService to set
	 */
	public void setCastorService(CastorService castorService) {
		this.castorService = castorService;
	}

	/**
	 * This method returns all the SMS Template types in the MISP system.
	 * 
	 * @return <code>List<SmsTemplateMaster></code> of template details
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public List<SmsTemplateMaster> getSMSTemplateTypes() throws MISPException {

		logger.entering("getSMSTemplateTypes");

		List<SmsTemplateMaster> templateNameList = null;
		try {
			templateNameList = smsTemplateManager.retrieveSMSTemplateTypes();

		} catch (DBException exception) {

			logger.error(
					FaultMessages.SMS_TEMPLATE_TYPE_FETCH_FAILURE,
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getSMSTemplateTypes", templateNameList);
		return templateNameList;
	}

	/**
	 * This method is used to fetch the list of SMS place holder types for the
	 * SMS Templates.
	 * 
	 * @return Map of Place holder Names.
	 */
	@SuppressWarnings("unchecked")
	public Map<String,SMSPlaceHolderVO> getPlaceHolders() throws MISPException {
		logger.entering("getPlaceHolders");
		
		SMSPlaceHolders smsPlaceHolders = castorService.unmarshal();
		
		Map<String, SMSPlaceHolderVO> placeHolderMap = 
			TypeUtil.convertToTreeMap(smsPlaceHolders.getPlaceHolderMap());		
		
		logger.exiting("getPlaceHolders", placeHolderMap);
		return placeHolderMap;
	}

	/**
	 * This method returns the SMS Template content for the specified template
	 * type Id.
	 * 
	 * @return <code>List<SmsTemplateMaster></code>
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public SMSTemplateVO getSMSTemplateContent(int templateId) 
	throws MISPException {

		logger.entering("getSMSTemplateContent()");

		SMSTemplateVO smsTemplateVO = null;

		try {
			smsTemplateVO = smsTemplateManager
					.retrieveSMSTemplateTypeDetails(templateId);

		} catch (DBException exception) {

			logger.error(FaultMessages.SMS_TEMPLATE_CONTENT_FETCH_FAILURE,
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getSMSTemplateContent", smsTemplateVO);

		return smsTemplateVO;

	}
	/**
	 * This method manages the submit SMS Template
	 * @param smsTemplateVO
	 * @return returnValue
	 * @throws MISPException
	 */
	public boolean saveSMSTemplate(SMSTemplateVO smsTemplateVO,UserDetails user) 
	throws MISPException{
		
		logger.entering("saveSMSTemplate()");
		String smsTemplateContent = null;
		StringTokenizer smsTempContent = null;		
		SmsTemplateMaster master =  null;
		int smsPlaceHoldersCount = 0;
		boolean isUpdated = false;
		try{
			smsTemplateContent = smsTemplateVO.getSmsTemplate();
			logger.debug("Content ", smsTemplateContent);
			if(smsTemplateContent.indexOf('<') != -1){
				smsTempContent = new StringTokenizer(smsTemplateContent, "<");
				smsPlaceHoldersCount = (smsTempContent.countTokens()-1);				
			}	
			master = this.smsTemplateManager.fetch(new Integer(
					smsTemplateVO.getSmsTemplateId()));			
			master.setSmsTemplate(smsTemplateContent);
			master.setSmsValidity(new Integer(smsTemplateVO.getSmsValidity()));
			master.setModifiedBy(user);
			master.setModifiedDate(new Date());
			master.setSmsPlaceHoldersCount(smsPlaceHoldersCount);			
		
			master = this.smsTemplateManager.updateSMSTemplate(master);	
			if(master != null)
				isUpdated = true;
		}catch (DBException exception) {
			
			logger.error(FaultMessages.SMS_TEMPLATE_UPDATE_FAILURE,
					exception);
			throw new MISPException(exception);
		}
    	
    	logger.exiting("saveSMSTemplate", isUpdated);
    	
    	return isUpdated;

	}
}
