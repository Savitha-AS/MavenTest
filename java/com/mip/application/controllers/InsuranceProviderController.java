package com.mip.application.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.InsuranceCompany;
import com.mip.application.model.UserDetails;
import com.mip.application.services.InsuranceProviderService;
import com.mip.application.view.InsuranceCompanyVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>InsuranceProviderController.java</code> contains the methods pertaining   
 * to Insurance Company Management use case model. Following are the use cases 
 * in this use case model: 
 * <table border="1">
 * 	<tr><th width="5%">Sl. No.</th><th>Use Case</th><th>Dispatcher action method signature</th></tr>
 * 	<tr><td>1.</td><td>Register Insurance Provider</td><td>{@link #registerInsuranceProvider(HttpServletRequest,HttpServletResponse,InsuranceCompanyVO)}</td></tr>
 * 	<tr><td>2.</td><td>List Insurance Providers</td><td>{@link #listInsuranceProviders(HttpServletRequest,HttpServletResponse)}</td></tr>
 * 	<tr><td rowspan="2">3.</td><td rowspan="2">View and Modify Insurance Provider</td><td>{@link #getInsuranceProviderDetail(HttpServletRequest,HttpServletResponse,InsuranceCompanyVO)}</td></tr>
 * 	<tr><td>{@link #modifyInsuranceProviderDetails(HttpServletRequest,HttpServletResponse,InsuranceCompanyVO)}</td></tr>
 * 	
 * </table>			
 * <br/>
 * This controller extends the <code>BasePlatformController</code> class 
 * of our MISP framework.
 * </p>
 * 
 * @see com.mip.framework.controllers.BasePlatformController
 * 
 * @author T H B S
 *
 */
public class InsuranceProviderController extends BasePlatformController {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			InsuranceProviderController.class);

	/**
	 * Set inversion of Control for <code>InsuranceProviderService</code>
	 */
	private InsuranceProviderService insuranceService;
	
	public void setInsuranceService(InsuranceProviderService insuranceService) {
		this.insuranceService = insuranceService;
	}

	/**
	 * This method registers the company details in the DB and receives a
	 * boolean from the service layer: TRUE: If the data got inserted properly
	 * in the DB. FALSE: If the data fails to insert in the DB.
	 * 
	 * @param request Request object
	 * @param response Response object
	 * @param insuranceCompanyVO <code>InsuranceCompanyVO</code>, 
	 * 			contains Insurance company details
	 * @return <code>ModelAndView</code>, a view object
	 */
	public ModelAndView registerInsuranceProvider(HttpServletRequest request,
			HttpServletResponse response,InsuranceCompanyVO insuranceCompanyVO){	
		
		logger.entering("registerInsuranceProvider",insuranceCompanyVO);
		
		ModelAndView mavObj = null;
		boolean isDataAdded = false;	
		try {
			HttpSession session = request.getSession();			
			UserDetails userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);
			
			isDataAdded = insuranceService.registerInsuranceProvider(
					insuranceCompanyVO,	userDetails);	
			
			if (isDataAdded) {				
				mavObj = super.success(SuccessMessages.INSURANCE_COMPANY_SAVED, 
						MAVPaths.JSP_INS_COMP_REGISTER);
			}		
		}
		catch (MISPException exception) {			
			logger.error(FaultMessages.INS_COMPANY_REG_EXCEPTION,
					exception);			
			mavObj = super.error(FaultMessages.GENERIC_ERROR, 
					MAVPaths.VIEW_INS_COMP_REGISTER);			
		} 
		catch (Exception e) {			
			logger.error(FaultMessages.GENERIC_ERROR, e);			
			mavObj = super.error(FaultMessages.GENERIC_ERROR);			
		}
		
		logger.exiting("registerInsuranceProvider",mavObj);			
        return mavObj;
	}

	/**
	 * This method modifies the Company details in the DB and receives a boolean
	 * value from the service layer: TRUE: If the data got modified properly
	 * FALSE: If the data fails to get modified
	 * 
	 * @param request Request object
	 * @param response Response object
	 * @param icObj <code>InsuranceCompanyVO</code>, contains Insurance company
	 *              details
	 * @return <code>ModelAndView</code>, view object
	 * @throws MISPException <code>MISPException</code>, if any error occurs
	 */
	public ModelAndView modifyInsuranceProviderDetails(
			HttpServletRequest request, HttpServletResponse response,
			InsuranceCompanyVO insuranceCompanyVO){
		
		logger.entering("modifyInsuranceProviderDetails", insuranceCompanyVO);
		
		boolean isDataModified = false;		
		ModelAndView mavObj = null;
		try {
			HttpSession session = request.getSession();			
			UserDetails userDetails = (UserDetails) session.getAttribute(
					SessionKeys.SESSION_USER_DETAILS);
			
			isDataModified = insuranceService.modifyInsuranceProviderDetails(
					insuranceCompanyVO, userDetails);	
			
			if (isDataModified){				
				mavObj = super.success(
						SuccessMessages.INSURANCE_COMPANY_MODIFIED, 
						MAVPaths.URL_LIST_INS_COMP);				
			}
		}
		catch (MISPException mispException) {			
			logger.error(FaultMessages.INS_COMPANY_MOD_EXCEPTION,mispException);			
			mavObj = super.error(FaultMessages.INS_COMPANY_MOD_EXCEPTION, 
					MAVPaths.URL_LIST_INS_COMP);
		} 
		catch (Exception exception) {			
			logger.error(FaultMessages.GENERIC_ERROR, exception);			
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}
		
		logger.exiting("modifyInsuranceProviderDetails");		
		return mavObj;
	}

	/**
	 * This method retrieves the company details from the DB.
	 * 
	 * @param request Request object
	 * @param response Response object
	 * @param insuranceCompanyVO <code>InsuranceCompanyVO</code>, 
	 * 		contains Insurance company details
	 * @return <code>ModelAndView</code>, view object
	 * @throws <code>MISPException</code>, if any error occurs.
	 */
	public ModelAndView getInsuranceProviderDetail(HttpServletRequest request,
		HttpServletResponse response, InsuranceCompanyVO insuranceCompanyVO){	
		
		logger.entering("getInsuranceProviderDetail", insuranceCompanyVO);
		
		ModelAndView mavObj = null;		
		try {
			String companyId = insuranceCompanyVO.getCompanyId();			
			insuranceCompanyVO = insuranceService.
						getInsuranceProviderDetail(companyId);	
			
			if (insuranceCompanyVO != null) {
				mavObj = new ModelAndView(MAVPaths.VIEW_INS_COMP_MODIFY);				
				mavObj.addObject(MAVObjects.VO_INS_PROVIDER, 
						insuranceCompanyVO);				
			} 
		}
		catch (MISPException exception) {		
			logger.error(FaultMessages.INS_COMPANY_DETAILS_FETCH_EXCEPTION, 
					exception);			
			mavObj = super.error(
					FaultMessages.INS_COMPANY_DETAILS_FETCH_EXCEPTION, 
					MAVPaths.URL_LIST_INS_COMP);			
		} 
		catch (Exception e){			
			logger.error(FaultMessages.GENERIC_ERROR, e);			
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}	
		
		logger.exiting("getInsuranceProviderDetail", mavObj);		
		return mavObj;
	}

	/**
	 * This method gets the details of all the registered companies.
	 * 
	 * @param request Request object
	 * @param response Response object
	 * @param icObj <code>InsuranceCompanyVO</code>, contains Insurance company
	 *              details
	 * @return <code>ModelAndView</code>, view object
	 */
	public ModelAndView listInsuranceProviders(HttpServletRequest request,
			HttpServletResponse response){	
		
		logger.entering("listInsuranceProviders");
		
		ModelAndView mavObj = null;		
		List<InsuranceCompany> insuranceCompanyList = null;		
		try {			
			insuranceCompanyList = insuranceService.listInsuranceProviders();
		
			if (insuranceCompanyList != null) {			
				mavObj = new ModelAndView(MAVPaths.VIEW_INS_COMP_LIST);				
				mavObj.addObject(MAVObjects.LIST_INS_COMP,insuranceCompanyList);
			}
		}
		catch (MISPException exception) {				
			logger.error(FaultMessages.INS_COMPANY_LIST_EXCEPTION,exception);
			mavObj = super.error(FaultMessages.INS_COMPANY_LIST_EXCEPTION, 
					MAVPaths.JSP_INS_COMP_LIST);				
		}
		catch (Exception e){				
			logger.error(FaultMessages.GENERIC_ERROR, e);
			mavObj = super.error(FaultMessages.GENERIC_ERROR);
		}	
		
		logger.exiting("listInsuranceProviders", mavObj);			
		return mavObj;
	}
}