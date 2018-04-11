package com.mip.application.model.mappings;

import java.util.Date;

import com.mip.application.model.SmsTemplateMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.view.SMSTemplateVO;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 10/05/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>SMSTemplateMappings.java</code> contains all the methods pertaining to
 * SMS Template management use case model. This is a mapping class for all SMS
 * Template management modules. The methods is used to convert a VO Object to a
 * DAL Object.
 * </p>
 * 
 * @author T H B S
 * 
 */
public class SMSTemplateMappings {

	

	public SMSTemplateMappings() {
		super();
		Object params[] = null;
		logger.entering("SMSTemplateMappings", params);
	}	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			SMSTemplateMappings.class);
	/**
	 * This class is used to map the VO to the Model in the DAL layer.
	 * 
	 * @param smsTemplateVO
	 * @param createdBy
	 * @param createdDate
	 * @param modifiedDate
	 * @param modifedBy
	 * @param smsPlaceHoldersCount
	 * @param smsTemplateName
	 * @return smsTemplateMasterObj
	 */
	public SmsTemplateMaster mapSMSTempVO2Model(SMSTemplateVO smsTemplateVO,
			UserDetails createdBy, Date createdDate, Date modifiedDate, UserDetails modifedBy,
			int smsPlaceHoldersCount, String smsTemplateName) {
		SmsTemplateMaster smsTemplateMasterObj = new SmsTemplateMaster();
		Object[] params = { smsTemplateVO };
		logger.entering("mapSMSTempVO2Model", params);
		if (smsTemplateVO != null) {
			smsTemplateMasterObj.setSmsTemplateId(Integer
					.parseInt(smsTemplateVO.getSmsTemplateId()));
			smsTemplateMasterObj.setSmsTemplateName(smsTemplateName);
			smsTemplateMasterObj.setSmsTemplateDescription(smsTemplateName);
			smsTemplateMasterObj.setSmsTemplate(smsTemplateVO.getSmsTemplate());
			smsTemplateMasterObj.setSmsPlaceHoldersCount(smsPlaceHoldersCount);
			smsTemplateMasterObj.setCreatedDate(createdDate);
			smsTemplateMasterObj.setCreatedBy(createdBy);
			smsTemplateMasterObj.setModifiedDate(modifiedDate);
			smsTemplateMasterObj.setModifiedBy(modifedBy);
			smsTemplateMasterObj.setSmsValidity(new Integer(smsTemplateVO.getSmsValidity()));
		}
		logger.exiting("mapSMSTempVO2Model",smsTemplateMasterObj);
		return smsTemplateMasterObj;
	}
}
