package com.mip.application.dal.managers;

/*
 * HISTORY
 * =====================================================================
 * Version | Date        | Who              | Comments
 * ---------------------------------------------------------------------
 * 1.0     | 12/05/2011  | T H B S          |Initial Version.
 * =====================================================================
 */

/**
 * <p>
 * <code>SMSTemplateManager.java</code> contains all the database related 
 *  operations pertaining to SMS Template management module.
 * </p>
 * 
 * @author T H B S
 * 
 */
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.mip.application.constants.FaultMessages;
import com.mip.application.model.SmsTemplateMaster;
import com.mip.application.view.SMSTemplateVO;
import com.mip.application.view.mappings.SMSTemplateM2VMappings;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

public class SMSTemplateManager extends
		DataAccessManager<SmsTemplateMaster, Integer> {
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			SMSTemplateManager.class);

	/**
	 * Default Constructor
	 */
	public SMSTemplateManager() {
		super(SmsTemplateMaster.class);
	}

	/**
	 * This method is used to fetch the list of templates from the database
	 * 
	 * @return List<SmsTemplateMaster>
	 * @throws DBException
	 */
	public List<SmsTemplateMaster> retrieveSMSTemplateTypes()
			throws DBException {

		logger.entering("retrieveSMSTemplateTypes");

		List<SmsTemplateMaster> smsTemplateTypes = null;

		try {
			// retrieve all Template Types
			smsTemplateTypes = super.fetchAll();

		} catch (DataAccessException e) {
			logger.error(FaultMessages.SMS_TEMPLATE_TYPE_FETCH_FAILURE,
					e);
			throw new DBException(e);
		}

		logger.exiting("retrieveSMSTemplateTypes", smsTemplateTypes);

		return smsTemplateTypes;

	}

	/**
	 * This method is used to fetch the template details for the template Id
	 * specified from the database
	 * 
	 * @return List<SmsTemplateMaster>
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public SMSTemplateVO retrieveSMSTemplateTypeDetails(int templateId)
			throws DBException {

		logger.entering("retrieveSMSTemplateTypeDetails");

		List<SmsTemplateMaster> smsTemplateDetails = null;

		SMSTemplateVO smsTemplateVO = new SMSTemplateVO();
		
		SMSTemplateM2VMappings mapToVO = new SMSTemplateM2VMappings();

		try {
			String query = "FROM SmsTemplateMaster WHERE smsTemplateId="
					+ templateId;

			smsTemplateDetails = getHibernateTemplate().find(query);

			if (!smsTemplateDetails.isEmpty())
				smsTemplateVO = mapToVO.mapSMSModel2SMSTemplateVO(smsTemplateDetails.get(0));

		} catch (DataAccessException e) {
			logger.error(FaultMessages.SMS_TEMPLATE_CONTENT_FETCH_FAILURE,
					e);
			throw new DBException(e);
		}

		logger.exiting("retrieveSMSTemplateTypeDetails", smsTemplateVO);

		return smsTemplateVO;

	}
	/**
	 * This method manages the SMS template update with the database
	 * @param smsTemplateVO
	 * @return returnValue
	 * @throws DBException
	 */
	public SmsTemplateMaster updateSMSTemplate(SmsTemplateMaster smsTemplateMaster)
			throws DBException {

		logger.entering("updateSMSTemplate");

		try {

			super.save(smsTemplateMaster);

		} catch (DataAccessException e) {
			logger.error(FaultMessages.SMS_TEMPLATE_UPDATE_FAILURE,
					e);
			throw new DBException(e);
		}

		logger.exiting("updateSMSTemplate", smsTemplateMaster);

		return smsTemplateMaster;

	}

}
