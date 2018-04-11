package com.mip.application.controllers;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.SuccessMessages;
import com.mip.application.services.LoginService;
import com.mip.application.services.transaction.AdminConfigTXService;
import com.mip.application.view.AdminConfigVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>LoginController.java</code> contains the methods pertaining to  
 * following Admin Configuration use cases: 
 * <table border="1">
 * 	<tr><th width="5%">Sl. No.</th><th>Use Case</th><th>Dispatcher action method signature</th></tr>
 * 	<tr><td>1.</td><td>Retrieve Config details</td><td>{@link #retrieveConfigDetails(HttpServletRequest,HttpServletResponse)}</td></tr>
 * 	<tr><td>2.</td><td>Save Config details</td><td>{@link #saveConfigDetails(HttpServletRequest,HttpServletResponse,AdminConfigVO)}</td></tr>
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
public class AdminConfigController extends BasePlatformController {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
							AdminConfigController.class);

	/**
	 * Set inversion of Control for <code>LoginService</code> 
	 * and <code>AdminConfigTXService</code>
	 */
	private LoginService loginService;
	private AdminConfigTXService adminConfigTXService;
	/*private ProductsManagementService productsMgmtService;*/
	
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	public void setAdminConfigTXService(AdminConfigTXService 
				adminConfigTXService) {
		this.adminConfigTXService = adminConfigTXService;
	}

	/*public void setProductsMgmtService(ProductsManagementService productsMgmtService) {
		this.productsMgmtService = productsMgmtService;
	}*/
	
	/**
	 * This method fetches all Configuration details that are used in the 
	 * platform.
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */	
	public ModelAndView retrieveConfigDetails(
			HttpServletRequest httpServletRequest, 
			HttpServletResponse httpServletResponse) {
		
		logger.entering("retrieveConfigDetails");
		
		AdminConfigVO adminConfigVO = null;
		/*List<ProductDetails> productsList = null;*/
		ModelAndView mav = null;
		try{
			adminConfigVO = loginService.getConfigDetails();
			/*productsList = productsMgmtService.retrieveProductNamesAndIds();*/

			mav = new ModelAndView(MAVPaths.VIEW_ADMIN_CONFIG);	
			mav.addObject(MAVObjects.VO_ADMIN_CONFIG, adminConfigVO);
			/*mav.addObject(MAVObjects.LIST_PRODUCTS, productsList);*/
		}
		catch(MISPException exception){			
			logger.error("An exception occured while retrieving Admin-specific"
					 + " Configuration Details.", exception);			
			mav = super.error("");
		}
		catch (Exception exception){			
			logger.error("An exception occured while retrieving Admin-specific"
					 + " Configuration Details.", exception);			
			mav = super.error("");
		}	
		
		logger.exiting("retrieveConfigDetails",mav);
		return mav;
	}	
	

	/**
	 * This method updates the Configuration details that are used in the 
	 * platform, with the one entered by Business administrator.
	 * 
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @return
	 */	
	public ModelAndView saveConfigDetails(
			HttpServletRequest httpServletRequest, 
			HttpServletResponse httpServletResponse, 
			AdminConfigVO adminConfigVO) {
		
		logger.entering("saveConfigDetails");
		
		ModelAndView mav = null;
		try{					
			adminConfigTXService.saveConfigDetails(adminConfigVO);

			mav = super.success(SuccessMessages.ADMIN_CONFIG_SAVED, 
							MAVPaths.URL_LOAD_ADMIN_CONFIG);
		}
		catch(MISPException exception){			
			logger.error("An exception occured while saving Admin-specific " +
					"Configuration Details.", exception);			
			mav = super.error("");
		}
		catch (Exception exception){			
			logger.error("An exception occured while saving Admin-specific " +
					"Configuration Details.", exception);			
			mav = super.error("");
		}	
		
		logger.exiting("saveConfigDetails",mav);
		return mav;
	}	
}
