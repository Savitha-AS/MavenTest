package com.mip.application.view.mappings;

import com.mip.application.model.SmsTemplateMaster;
import com.mip.application.view.SMSTemplateVO;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/* HISTORY
* =====================================================================
* Version | Date        | Who              | Comments
* ---------------------------------------------------------------------
* 1.0     | 10/05/2011  | T H B S          |Initial Version.
* =====================================================================
*/

/**
* <p>
* <code>SMSTemplateM2VMappings.java</code> contains all the methods pertaining to  
* map model layer objects to VO layer objects. 
* </p>
* 
* @author T H B S
*
*/

public class SMSTemplateM2VMappings {
	
	public SMSTemplateM2VMappings() {
		super();
		Object params[] = null;
		logger.entering("SMSTemplateM2VMappings", params);
	}	
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			SMSTemplateM2VMappings.class);
	
	public SMSTemplateVO mapSMSModel2SMSTemplateVO(SmsTemplateMaster smsTemplateMasterDetails){
		
		Object params[] = {smsTemplateMasterDetails};
		logger.entering("mapSMSModel2SMSTemplateVO", params);
		SMSTemplateVO smsTemplateVO = new SMSTemplateVO();
		
		if(smsTemplateMasterDetails !=null){
			smsTemplateVO.setSmsTemplate(smsTemplateMasterDetails.getSmsTemplate());
			smsTemplateVO.setSmsTemplateId(smsTemplateMasterDetails.getSmsTemplateId()+"");
			smsTemplateVO.setSmsValidity(smsTemplateMasterDetails.getSmsValidity()+"");
		}
		logger.exiting("mapSMSModel2SMSTemplateVO", smsTemplateVO);
		return smsTemplateVO;
	}
}
