package com.mip.application.controllers;

import static com.mip.framework.constants.PlatformConstants.AUDIT_LOGGER_APPENDER;
import static com.mip.framework.constants.PlatformConstants.SUCCESS_MESSAGE_KEY;
import static com.mip.framework.constants.PlatformConstants.SUCCESS_MODEL_MAP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.dal.managers.DatabaseDrivenFilterInvocationDefinitionSource;
import com.mip.application.model.Menu;
import com.mip.application.model.RoleMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserHash;
import com.mip.application.services.LoginService;
import com.mip.application.services.UserService;
import com.mip.application.view.ChangePasswordVO;
import com.mip.application.view.CustomerStatsVO;
import com.mip.application.view.LoginVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.security.CustomMISPUser;
import com.mip.framework.utils.DateUtil;

/**
 * <p>
 * <code>LoginController.java</code> contains the methods pertaining to  
 * following use cases: 
 * <table border="1">
 * 	<tr><th width="5%">Sl. No.</th><th>Use Case</th><th>Dispatcher action method signature</th></tr>
 * 	<tr><td>1.</td><td>Login</td><td>{@link #authenticateUser(HttpServletRequest,HttpServletResponse)}</td></tr>
 * 	<tr><td>2.</td><td>Change Password</td><td>{@link #changePassword(HttpServletRequest,HttpServletResponse,ChangePasswordVO)}</td></tr>
 * 	<tr><td>3.</td><td>Customer and User Statistics</td><td>{@link #getStatistics(HttpServletRequest,HttpServletResponse)}</td></tr>
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
public class LoginController extends BasePlatformController{

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
										LoginController.class);
	private static MISPLogger auditLogger = LoggerFactory.getInstance().getLogger(
			AUDIT_LOGGER_APPENDER);

	/**
	 * Set inversion of Control for <code>LoginService</code>
	 */
	private LoginService loginService;

	
	/**
	 * Set inversion of Control for <code>UserService</code>
	 */
	private UserService userService;
	
	/**
	 * Set inversion of control for 
	 * <code>DatabaseDrivenFilterInvocationDefinitionSource</code>.
	 */
	private DatabaseDrivenFilterInvocationDefinitionSource dbFilter;
	
	/**
	 * Sets the loginService.
	 *  
	 * @param loginService an instance of <code>LoginService</code> to set.
	 */
	
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}

	/**
	 * Sets the User Service.
	 * 
	 * @param userService an instance of <code>UserService</code> to set.
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Sets the dbFilter.
	 *  
	 * @param dbFilter an instance of 
	 * <code>DatabaseDrivenFilterInvocationDefinitionSource</code> to set.
	 */
	public void setDbFilter(
			DatabaseDrivenFilterInvocationDefinitionSource dbFilter) {
		this.dbFilter = dbFilter;
	}
	
	/**
	 * This method retrieves the user details and menu details for the 
	 * logged in user based on the role assigned.
	 * 
	 * @param httpServletRequest an instance of HttpServletRequest.
	 * 
	 * @param httpServletResponse an instance of HttpServletResponse.
	 * 
	 * @return
	 */
	public ModelAndView authenticateUser(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		
		logger.entering("authenticateUser");
		
		ModelAndView mav = null;
		LoginVO loginVO = new LoginVO();
		
		try{
			
			/**
			 *   Sets the default Application TimeZone. 
			 */
			// To be implemented
			// TimeZone.setDefault(TimeZone.getTimeZone(SessionKeys.APP_TIME_ZONE));
			
			CustomMISPUser mispUser = (CustomMISPUser) SecurityContextHolder.
								getContext().getAuthentication().getPrincipal();
						
			loginVO.setLoginId(mispUser.getUsername());
			loginVO.setUserHash(mispUser.getPassword());
			
			/**
			 * Fetch User and Hash details from database.
			 */
			UserHash userHash = loginService.retrieveUserDetails(loginVO);
			UserDetails userDetails = userHash.getUserDetails();
			
			//userHash.setSessionId(httpServletRequest.getRemoteAddr());			
			if(userHash.getLoggedIn() == 0) {
				userHash.setLoggedIn((byte)1);
				loginService.changeLoggedInStatus(userHash);
				auditLogger.info("Successful User Login [User ID] : ",
						mispUser.getUsername());
			}
			//logger.info("userDetails in userHash : ", userDetails);
			
			/**
			 * Store User details in session object. This is to
			 * avoid database access for every request.
			 */
			HttpSession session =  httpServletRequest.getSession();
			session.setAttribute(SessionKeys.SESSION_USER_DETAILS, userDetails);			
			
			if(userHash.getFirstLogin() == 1){
				mav = new ModelAndView(MAVPaths.VIEW_CHANGE_PASSWORD);
				
				Boolean isFirstLogin = true;
				mav.addObject(MAVObjects.IS_FIRST_LOGIN, isFirstLogin);
			}
			else{
				mav = redirectToHomePage(httpServletRequest, httpServletResponse);
			}
			loginService.updateLastLoginDate(userDetails);
		}
		catch(MISPException exception){			
			logger.error("An exception occured while retrieving Menu Details.",
					exception);			
			mav = super.error("", MAVPaths.JSP_HOME);
		}
		catch (Exception exception){					
			logger.error("An exception occured while retrieving Menu Details.",
					exception);			
			mav = super.error("", MAVPaths.JSP_HOME);
		}	
			
		logger.exiting("authenticateUser",mav);
		return mav;
	}
	

	/**
	 * This method redirects the user to Home page, and before doing so, it 
	 * fetches menu details for the logged in user based on the role assigned,
	 * and retrieves the statistics.
	 * 
	 * @param httpServletRequest an instance of HttpServletRequest. 
	 * @param httpServletResponse an instance of HttpServletResponse.
	 * 
	 * @return MAV
	 */
	public ModelAndView redirectToHomePage(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		
		logger.entering("redirectToHomePage");
		
		ModelAndView mav = null;		
		try{

			HttpSession session =  httpServletRequest.getSession();
			UserDetails userDetails = (UserDetails)session.getAttribute(
					SessionKeys.SESSION_USER_DETAILS);
			
			RoleMaster roleMaster = userDetails.getRoleMaster();
			
			/**
			 *  Fetch Menu details based on user role. 
			 */
			Map<Long, List<Menu>> menuMap = 
								loginService.retrieveMenuDetails(roleMaster);
			
			Map<Long, List<Menu>> menuMapAdmin = 
					loginService.retrieveAllMenuDetails();
			
			/**
			 * Store Menu details in session object. This is to
			 * avoid database access for every request.
			 */
			session.setAttribute(SessionKeys.SESSION_MENU_TREE, menuMap);
			
			session.setAttribute(SessionKeys.SESSION_MENU_TREE_ADMIN, 
					menuMapAdmin
					);
			
			mav = getStatistics(httpServletRequest, httpServletResponse);
		}
		catch(MISPException exception){			
			logger.error("An exception occured while retrieving Menu Details/ "+
					"Statistics.", 	exception);			
			mav = super.error("", MAVPaths.JSP_HOME);
		}
		catch (Exception exception){					
			logger.error("An exception occured while retrieving Menu Details/ "+
					"Statistics.", 	exception);			
			mav = super.error("", MAVPaths.JSP_HOME);
		}	
			
		logger.exiting("redirectToHomePage",mav);
		return mav;
	}


	/**
	 * This method changes the password for the logged in user.
	 * 
	 * @param httpServletRequest 
	 * 				an instance of HttpServletRequest.	 
	 * @param httpServletResponse 
	 * 				an instance of HttpServletResponse.
	 * @param changePasswordVO
	 * 				ChangePasswordVO command object
	 * @return
	 */
	public ModelAndView changePassword(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, 
			ChangePasswordVO changePasswordVO) {
		
		Object params[] = {changePasswordVO};
		logger.entering("changePassword",params);
		
		ModelAndView mav = null;
		try{	
			/**
			 * Fetch User details from session.
			 */	
			HttpSession session = httpServletRequest.getSession();
			UserDetails userDetails = (UserDetails)session.getAttribute(
						SessionKeys.SESSION_USER_DETAILS);			
			
			/**
			 * Change the password. 
			 */
			loginService.changePassword(changePasswordVO, userDetails);
			
			String firstLogin = changePasswordVO.getFirstLogin();
			
			if(firstLogin != null && firstLogin.equals("1")){
				ModelMap successModelMap = new ModelMap();
				Map<String,String> successInfoMap 
							= new HashMap<String,String>();
				successInfoMap.put(SUCCESS_MESSAGE_KEY, 
						SuccessMessages.PASSWORD_CHANGED);		

				successModelMap.addAllAttributes(successInfoMap);	
				
				mav = new ModelAndView(MAVPaths.VIEW_FIRST_LOGIN_SUCCESS,  
						SUCCESS_MODEL_MAP, successModelMap);
			}
			else{
				mav = super.success(SuccessMessages.PASSWORD_CHANGED);				
			}
			
		}
		catch(MISPException exception){			
			logger.error("An exception occured while changing " +
					"password of the user.", exception);
			mav = super.error("", MAVPaths.JSP_CHANGE_PASSWORD);
		}
		catch (Exception exception){		
			logger.error("An exception occured while changing " +
					"password of the user.", exception);
			mav = super.error("", MAVPaths.JSP_CHANGE_PASSWORD);
		}
		
		logger.exiting("changePassword",mav);
		return mav;
	}
	
	
	


	/**
	 * This method gets Customer and User Statistics.
	 * 
	 * @param httpServletRequest
	 *            an instance of HttpServletRequest.
	 * @param httpServletResponse
	 *            an instance of HttpServletResponse.
	 * @return
	 */
	public ModelAndView getStatistics(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		logger.entering("getStatistics");

		ModelAndView mav = null;
		CustomerStatsVO customerStatsVO = null;
		try {
			/**
			 * Fetch User details from session.
			 */
			HttpSession session = httpServletRequest.getSession();

			UserDetails userDetails = (UserDetails) session
					.getAttribute(SessionKeys.SESSION_USER_DETAILS);

			customerStatsVO = loginService.getCustomerStatsForUser(userDetails
					.getUserId());

			customerStatsVO
					.setLastLoginDate(DateUtil
							.toDateTimeMeridianString(userDetails
									.getLastLoggedInDate()));

			dbFilter.setAllRolesAllowedMap(userService.getAllRolesAllowed());

			mav = new ModelAndView(MAVPaths.VIEW_HOME);
			mav.addObject(MAVObjects.VO_CUSTOMER_STATS, customerStatsVO);
		} catch (MISPException exception) {
			logger.error("An exception occured while retrieving "
					+ "the customer and user statistics.", exception);
			mav = super.error("", MAVPaths.JSP_HOME);
		} catch (Exception exception) {
			logger.error("An exception occured while retrieving "
					+ "the customer and user statistics.", exception);
			mav = super.error("", MAVPaths.JSP_HOME);
		}

		logger.exiting("getStatistics", mav);
		return mav;
	}
}
