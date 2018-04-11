package com.mip.application.controllers;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.SmsTemplateMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.services.SMSTemplateService;
import com.mip.application.view.SMSPlaceHolderVO;
import com.mip.application.view.SMSTemplateVO;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;


/**
 * <p>
 * <code>SMSTemplateController.java</code> contains all the methods pertaining to  
 * SMS Template use case. This controller extends the <code>BaseController</code>
 * class and is invoked when a user attempts to edit SMS template in the platform
 * </p>
 * 
 * @author T H B S
 *
 */
public class SMSTemplateController extends BasePlatformController {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			SMSTemplateController.class);

	/**
	 * Set inversion of Control for <code>SMSTemplateService</code>
	 */
	private SMSTemplateService smsTemplateService;

	/**
	 * @param smsTemplateService the smsTemplateService to set
	 */
	public void setSmsTemplateService(SMSTemplateService smsTemplateService) {
		this.smsTemplateService = smsTemplateService;
	}
	
	/**
	 * This method controls editing SMS Template 
	 * 
	 * @param request - An instance of HttpServletRequest Object
	 * @param response - An instance of HttpServletResponse Object
	 * @return - Returns ModelAndView Object
	 * @throws MISPException
	 *               In case of exception throws MISPException
	 */
	public ModelAndView editSMSTemplate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws MISPException {
		
		logger.entering("editSMSTemplate()");
		ModelAndView mavObj = null;	
		List<SmsTemplateMaster> templateNameList = null;
		Map<String,SMSPlaceHolderVO> placeHolderMap = null;
		try{
			templateNameList = smsTemplateService.getSMSTemplateTypes();
			placeHolderMap = smsTemplateService.getPlaceHolders();
			mavObj = new ModelAndView(MAVPaths.VIEW_SMSTEMPLATE_EDIT);
			mavObj.addObject(PlatformConstants.TEMPLATE_NAME_LIST, templateNameList);
			mavObj.addObject(PlatformConstants.PLACE_HOLDER_LIST, placeHolderMap);
			
			httpServletRequest.getSession().setAttribute(PlatformConstants.TEMPLATE_NAME_LIST, templateNameList);
			httpServletRequest.getSession().setAttribute(PlatformConstants.PLACE_HOLDER_LIST, placeHolderMap);
		}
		catch (MISPException exception) {			
			logger.error(FaultMessages.SMS_EDIT_TEMPLATE_PAGE_LOADING_FAILURE,
					exception);			
			mavObj = super.error(FaultMessages.SMS_EDIT_TEMPLATE_PAGE_LOADING_FAILURE);			
			return mavObj;
		}
		logger.exiting("editSMSTemplate",mavObj);
		return mavObj;
	}
	/**
	 * This method controls getting SMS Template content
	 * 
	 * @param request - An instance of HttpServletRequest Object
	 * @param response - An instance of HttpServletResponse Object
	 * @param smsTemplateVO - An instance holding SMS Template details
	 * @return - Returns ModelAndView Object
	 * @throws MISPException
	 *               In case of exception throws MISPException
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView getTemplateContent(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,SMSTemplateVO smsTemplateVO) throws MISPException {
		
		logger.entering("getTemplateContent()");
		ModelAndView mavObj = null;
		int selectedTemplateId = Integer.parseInt(smsTemplateVO.getSmsTemplateId().toString());
		try{
			smsTemplateVO = smsTemplateService.getSMSTemplateContent(selectedTemplateId);
			
			String smsTemplateText = smsTemplateVO.getSmsTemplate();
			
			Map<String,SMSPlaceHolderVO> newPlaceHolderMap = 
				new TreeMap<String, SMSPlaceHolderVO>(); 
			Map<String,SMSPlaceHolderVO> placeHolderMap = 
				(Map<String,SMSPlaceHolderVO>)httpServletRequest.getSession().
				getAttribute(PlatformConstants.PLACE_HOLDER_LIST);
			
			for(String displayOrder : placeHolderMap.keySet()){
				SMSPlaceHolderVO placeHolderVO = placeHolderMap.get(displayOrder);
				if(smsTemplateText.indexOf(
						"<"+placeHolderVO.getPlaceHolderCode()+">") != -1){
					newPlaceHolderMap.put(displayOrder, placeHolderVO);
				}
			}
			
			
			mavObj = new ModelAndView(MAVPaths.VIEW_SMSTEMPLATE_EDIT);
			mavObj.addObject(PlatformConstants.TEMPLATE_NAME_LIST, httpServletRequest.getSession().getAttribute(PlatformConstants.TEMPLATE_NAME_LIST));
			mavObj.addObject(PlatformConstants.PLACE_HOLDER_LIST, newPlaceHolderMap);			
			mavObj.addObject(PlatformConstants.SMS_TEMPLATE_VO, smsTemplateVO);	
		}
		catch (MISPException exception) {			
			logger.error(FaultMessages.SMS_TEMPLATE_CONTENT_FETCH_FAILURE,
					exception);			
			mavObj = super.error(FaultMessages.SMS_TEMPLATE_CONTENT_FETCH_FAILURE);			
			return mavObj;
		}
		logger.exiting("getTemplateContent",mavObj);
		return mavObj;
	}
	/**
	 * This method controls saving SMS Template.
	 * 
	 * @param request - An instance of HttpServletRequest Object
	 * @param response - An instance of HttpServletResponse Object
	 * @param smsTemplateVO - An instance holding SMS Template details
	 * @return - Returns ModelAndView Object
	 * @throws MISPException
	 *               In case of exception throws MISPException
	 */
	public ModelAndView saveTemplate(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse,SMSTemplateVO smsTemplateVO) throws MISPException {
		
		logger.entering("saveTemplate()",smsTemplateVO.toString());
		ModelAndView mavObj = null;
		boolean isSaved = false;
		HttpSession session = null;
		UserDetails user = null;
		try{
			session = httpServletRequest.getSession(false);			
			user = (UserDetails) session.getAttribute(SessionKeys.SESSION_USER_DETAILS);
			isSaved = smsTemplateService.saveSMSTemplate(smsTemplateVO,user);
			if(isSaved)
				mavObj = super.success(SuccessMessages.SMS_TEMPLATE_UPDATE_SUCCESS,MAVPaths.URL_SMSTEMPLATE_EDIT);
		}
		catch (MISPException exception) {			
			logger.error(FaultMessages.SMS_TEMPLATE_UPDATE_FAILURE,
					exception);			
			mavObj = super.error(FaultMessages.SMS_TEMPLATE_UPDATE_FAILURE);			
			return mavObj;
		}
		logger.exiting("saveTemplate()",mavObj);
		return mavObj;
	}
}
