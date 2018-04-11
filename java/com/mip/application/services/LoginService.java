package com.mip.application.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

import com.mip.application.constants.SessionKeys;
import com.mip.application.dal.managers.AdminConfigManager;
import com.mip.application.dal.managers.HashHistoryManager;
import com.mip.application.dal.managers.MenuManager;
import com.mip.application.dal.managers.UserHashManager;
import com.mip.application.dal.managers.UserManager;
import com.mip.application.model.HashHistory;
import com.mip.application.model.Menu;
import com.mip.application.model.RoleMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserHash;
import com.mip.application.model.mappings.LoginMappings;
import com.mip.application.model.mappings.UserMappings;
import com.mip.application.services.transaction.UserTXService;
import com.mip.application.view.AdminConfigVO;
import com.mip.application.view.ChangePasswordVO;
import com.mip.application.view.CustomerStatsVO;
import com.mip.application.view.LoginVO;
import com.mip.application.view.UserVO;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.EncryptException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.security.HashService;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.TypeUtil;

/**
 * <p>
 * <code>LoginService.java</code> contains all the service layer methods 
 * pertaining to Login use case and Change Password use case.
 * </p>
 * 
 * @author T H B S
 */
public class LoginService {

	/**
	 * An instance of logger.
	 */	
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			LoginService.class);

	/**
	 * Set inversion of Control for <code>UserManager</code>, 
	 * <code>MenuManager</code>, <code>UserHashManager</code>, 
	 * <code>HashHistoryManager</code>, <code>AdminConfigManager</code> and 
	 * <code>UserTXService</code>.
	 */
	private UserManager userManager;	
	private MenuManager menuManager;	
	private UserHashManager userHashManager;
	private HashHistoryManager hashHistoryManager; 
	private AdminConfigManager adminConfigManager;
	private UserTXService userTXService;
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setMenuManager(MenuManager menuManager) {
		this.menuManager = menuManager;
	}

	public void setUserHashManager(UserHashManager userHashManager) {
		this.userHashManager = userHashManager;
	}

	public void setHashHistoryManager(HashHistoryManager hashHistoryManager) {
		this.hashHistoryManager = hashHistoryManager;
	}		

	public void setAdminConfigManager(AdminConfigManager adminConfigManager) {
		this.adminConfigManager = adminConfigManager;
	}

	public void setUserTXService(UserTXService userTXService) {
		this.userTXService = userTXService;
	}
/*
	private LoginService loginServiceProxy;

	public void setLoginServiceProxy(LoginService loginServiceProxy) {
		this.loginServiceProxy = loginServiceProxy;
	}*/

	/**
	 * Gets the user details of the logged in user. Also gets the role details 
	 * of the user.
	 * 
	 * @param loginVO 
	 * 				LoginVO containing login details.
	 * @return 
	 * 		UserDetails, details of the logged in user. 
	 * @throws MISPException
	 */
	public UserHash retrieveUserDetails(LoginVO loginVO) 
		throws MISPException {	
		
		Object[] params = {loginVO};
		logger.entering("retrieveUserDetails", params);
		
		UserHash userHash = null;		
		try{
			UserDetails userDetails = LoginMappings.
				mapLoginVOToUserDetailsModel(loginVO);
			
			List<UserDetails> userList = userManager.searchUser(userDetails, 
					true);

			if(userList == null || userList.size() == 0){
				logger.info("User has provided invalid credentials: " +
									"The User Id is wrong.");
			}
			else{
				userDetails = userList.get(0);
				
				userHash = new UserHash();
				userHash.setUserDetails(userDetails);
				userHash = userHashManager.getUserHash(userHash);
			}
		}
		catch(DBException exception){
			logger.error("An exception occured while retrieving User Details.",
					exception);
			throw new MISPException(exception);
		}		
		
		logger.exiting("retrieveUserDetails", userHash);
		return userHash;
	}
	
	/**
	 * Retrieves menu details of the logged in user, based on the user's role.
	 * 
	 * @param roleMaster 
	 * 				RoleMaster, based on which menu details are fetched.
	 * @return Menu details based on the role.
	 * @throws MISPException
	 */
	public Map<Long, List<Menu>> retrieveMenuDetails(RoleMaster roleMaster) 
		throws MISPException {
		
		Object[] params = {roleMaster};
		logger.entering("retrieveMenuDetails", params);
		
		Map<Long, List<Menu>> menuMap = new TreeMap<Long, List<Menu>>();
		List<Menu> menuList = null;
		try {
			menuList = menuManager.fetchMenuDetails(roleMaster);
		} catch (DBException e) {
			logger.error("An exception occured while retrieving Menu Details.",
					e);
			throw new MISPException(e);
		}
		
		try{
			if(menuList != null){
				long previousParent = -1;
				List<Menu> newMenuList = null;
				for(Iterator<Menu> itr = menuList.iterator(); itr.hasNext();){
					Menu menu = itr.next();

					long currentParent = menu.getMenuParentId();
					
					if(currentParent != previousParent){
						if(previousParent != -1){
							Long parent = previousParent;					
							menuMap.put(parent,newMenuList);
							
						}
						previousParent = currentParent;
						newMenuList = new ArrayList<Menu>();
					}
					newMenuList.add(menu);					
				}
				Long parent = previousParent;					
				menuMap.put(parent,newMenuList);			
			}
		}catch(Throwable t){
			logger.error("An exception occured while re-arranging " +
					"Menu Details.",t);
			throw new MISPException(t);
		}
		
		logger.exiting("retrieveMenuDetails");
		return menuMap;
		
	}
	
	/**
	 * Retrieves menu details of the logged in user, based on the user's role.
	 * 
	 * @param roleMaster 
	 * 				RoleMaster, based on which menu details are fetched.
	 * @return Menu details based on the role.
	 * @throws MISPException
	 */
	public Map<Long, List<Menu>> retrieveAllMenuDetails() 
		throws MISPException {
		
		logger.entering("retrieveAllMenuDetails");
		
		Map<Long, List<Menu>> menuMap = new TreeMap<Long, List<Menu>>();
		List<Menu> menuList = null;
		try {
			menuList = menuManager.fetchAllMenuDetails();
		} catch (DBException e) {
			logger.error("An exception occured while retrieving Menu Details.",
					e);
			throw new MISPException(e);
		}
		
		try{
			if(menuList != null){
				long previousParent = -1;
				List<Menu> newMenuList = null;
				for(Iterator<Menu> itr = menuList.iterator(); itr.hasNext();){
					Menu menu = itr.next();
					long currentParent = menu.getMenuParentId();
					
					if(currentParent != previousParent){
						if(previousParent != -1){
							Long parent = previousParent;					
							menuMap.put(parent,newMenuList);
						}
						previousParent = currentParent;
						newMenuList = new ArrayList<Menu>();
					}
					newMenuList.add(menu);					
				}
				Long parent = previousParent;					
				menuMap.put(parent,newMenuList);			
			}
		}catch(Throwable t){
			logger.error("An exception occured while re-arranging " +
					"Menu Details.",t);
			throw new MISPException(t);
		}
		
		logger.exiting("retrieveAllMenuDetails", menuMap);
		return menuMap;
		
	}
	
	/**
	 * Changes the user's password.
	 * 
	 * @param changePasswordVO
	 * 				ChangePasswordVO the new password details.
	 * @param userDetails
	 * 				UserDetails details of the user.
	 * @return 
	 * 		Result Code indicating whether the operation was successful 
	 * 		or failure.
	 * @throws MISPException
	 */
	public void changePassword(ChangePasswordVO changePasswordVO,
			UserDetails userDetails) throws MISPException {	
	
		Object[] params = {changePasswordVO, userDetails};
		logger.entering("changePassword", params);
		
		String currentHash, newHash;
		try{

			/**
			 * Fetch user-entered passwords from VO.
			 */
			currentHash = changePasswordVO.getCurrentHash();
			newHash = changePasswordVO.getNewHash();
			//logger.debug("Pwd: currentHash : ", currentHash);
			//logger.debug("Pwd: newHash : ", newHash);
			
			/**
			 * Generates hashes by encrypting the passwords.
			 */
			currentHash = HashService.encrypt(currentHash);
			newHash = HashService.encrypt(newHash);
			//logger.debug("Hash: currentHash : ", currentHash);
			//logger.debug("Hash: newHash : ", newHash);
		}
		catch(EncryptException exception){
			logger.error("An exception occured while encrypting the password.",
					exception);
			throw new MISPException(exception);
		}	
		
		try{			
			UserHash userHash = new UserHash();
			userHash.setUserDetails(userDetails);
			userHash = userHashManager.getUserHash(userHash);
			
			String firstLogin = changePasswordVO.getFirstLogin();			
			if(firstLogin != null && firstLogin.equals("1")){
				userHash.setFirstLogin(new Byte("0"));
			}
			
			HashHistory hashHistory = new HashHistory();
			hashHistory.setUserDetails(userDetails);
			List<HashHistory> historyList = 
				hashHistoryManager.getHashHistory(hashHistory);

			userHash.setUserHash(newHash);
			userTXService.setPassword(currentHash,historyList,
							userDetails,userHash);
			//loginServiceProxy			
		}
		catch(DBException exception){
			logger.error("An exception occured while changing the password.",
					exception);
			throw new MISPException(exception);
		}		
		
		logger.exiting("changePassword");
	}
		
	/**
	 * Resets the password of the user. Also unlocks the account of the user.
	 * 
	 * @param userVO
	 * 			UserVO containing the data to be modified.
	 * @throws MISPException
	 */
	public void resetPassword(UserVO userVO) throws MISPException{		
		Object[] params = {userVO};
		logger.entering("resetPassword",params);
		
		try{
			UserHash userHash = UserMappings.mapUserVOToUserHashModel(userVO);
			
			UserDetails userDetails = userManager.getUserDetails(
									new Integer(userVO.getUserId()));
			
			userHash = userHashManager.getUserHash(userHash);
			String currentHash = userHash.getUserHash();
			
			/**
			 * Allow user to change password after 
			 * password reset.
			 */
			userHash.setFirstLogin((byte)1);
			
			//userHashManager.updateUserHash(userHash);

			HashHistory hashHistory = new HashHistory();
			hashHistory.setUserDetails(userDetails);
			List<HashHistory> historyList = 
				hashHistoryManager.getHashHistory(hashHistory);
			
			AdminConfigVO adminConfigVO = this.getConfigDetails();
			
			// Unlock User Account.
			byte accountLocked = 0;
			userHash.setAccountLocked(accountLocked);	
			
			// Reset password to the default one.
			userHash.setUserHash(HashService.encrypt(
					adminConfigVO.getDefaultPwd()));   
			userHash.setAttemptCount(0);
			
			userTXService.setPassword(currentHash, historyList, 
					userDetails, userHash);
			//loginServiceProxy
		}
		catch(DBException exception){
			logger.error("An exception occured while retrieving User Details.",
					exception);
			throw new MISPException(exception);
		}
		catch(MISPException exception){
			logger.error("An exception occured while mapping VO to Model.",
					exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("resetPassword");
	}
	
	/**
	 * Gets all the Admin Configuration details.
	 * 
	 * @return adminConfigVO Admin Configuration details
	 * @throws MISPException
	 */
	public AdminConfigVO getConfigDetails() throws MISPException{
		
		logger.entering("getConfigDetails");
		
		AdminConfigVO adminConfigVO = new AdminConfigVO();		
		try {
			Map<String,String> configMap = adminConfigManager.getConfigDetails();

			adminConfigVO.setDefaultPwd(
					configMap.get(PlatformConstants.DEFAULT_PASSWORD));
			adminConfigVO.setUserLoginPrefix(
					configMap.get(PlatformConstants.USER_LOGIN_PREFIX));
			adminConfigVO.setPwdHistoryLimit(TypeUtil.toInt(
					configMap.get(PlatformConstants.PASSWORD_HISTORY_LIMIT)));
			adminConfigVO.setMaxLoginAttempts(TypeUtil.toInt(
					configMap.get(PlatformConstants.MAX_LOGIN_ATTEMPTS)));
			adminConfigVO.setMaxIdleCount(TypeUtil.toInt(
					configMap.get(PlatformConstants.MAX_IDLE_COUNT)));
			adminConfigVO.setRegisterCustomerWSURL(
					configMap.get(PlatformConstants.REGISTER_CUSTOMER_WS_URL));
			adminConfigVO.setAnnouncementMessage(
					configMap.get(PlatformConstants.ANNOUNCEMENT_MESSAGE));
			adminConfigVO.setOfferSubscriptionLastDay(TypeUtil.toInt(
					configMap.get(PlatformConstants.OFFER_SUBSCRIPTION_LAST_DAY)));
			adminConfigVO.setMsisdnCodes(
					configMap.get(PlatformConstants.MSISDN_CODES));
			adminConfigVO.setOfferUnsubscribeWSURL(
					configMap.get(PlatformConstants.OFFER_UNSUBSCRIBE_WS_URL));
			adminConfigVO.setRemoveCustomerRegistrationWSURL(
					configMap.get(PlatformConstants.
										REMOVE_CUSTOMER_REGISTRATION_WS_URL));
			/*adminConfigVO.setDefaultOfferAssigned(
					configMap.get(PlatformConstants.DEFAULT_OFFER_ASSIGNED));*/
			adminConfigVO.setCommissionPercent(
					configMap.get(PlatformConstants.COMMISSION_PERCENTAGE));
			adminConfigVO.setDeregisterCustomerWSURL(
					configMap.get(PlatformConstants.DEREGISTER_CUSTOMER_WS_URL));
			adminConfigVO.setChangeDeductionModeWSURL(configMap.get(PlatformConstants.CHANGE_DEDUCTIONMODE_WSURL));

			adminConfigVO.setSummaryCustomerDetailsChangesRecordLimit(configMap.get(PlatformConstants.SUMMARY_CUSTOMER_DETAILS_CHANGES_RECORD_LIMIT));

			adminConfigVO.setReactivationCustomerWSURL(configMap.get(PlatformConstants.REACTIVATION_CUSTOMER_WSURL));
			adminConfigVO.setAssignOfferWSURL(configMap.get(PlatformConstants.ASSIGN_OFFER_WS_URL_WSURL));
			adminConfigVO.setLoyaltyPackWSURL(configMap.get(PlatformConstants.LOYALTY_PACK_WS_URL_WSURL));
			adminConfigVO.setInsMsisdnCode(configMap.get(PlatformConstants.INS_MSISDN_CODES));

			
		} catch (DBException e) {
			logger.error("Exception occured while retrieving idle count " +
					"from DB.", e);
			throw new MISPException(e);
		}
		
		logger.exiting("getConfigDetails", adminConfigVO);
		return adminConfigVO;
	}
	
	
	/**
	 * Checks if 
	 * 		1) the old password entered is same as current password.
	 *      2) the new password entered exists in the password history.
	 *      
	 * This method is invoked using AJAX.
	 *      
	 * @param currentHash Current Password
	 * @param newHash New Password
	 * @return Status Code
	 */
	public int checkPassword(String currentHash, String newHash){
				
		logger.entering("checkPassword");
		
		int resultCode = -1;
		try{
			/**
			 * Generates hashes by encrypting the passwords.
			 */
			currentHash = HashService.encrypt(currentHash);
			newHash = HashService.encrypt(newHash);
			//logger.debug("Hash: currentHash : ", currentHash);
			//logger.debug("Hash: newHash : ", newHash);
		}
		catch(EncryptException exception){
			logger.error("An exception occured while encrypting the password.",
					exception);
		}	
		
		try{			
			/**
			 * Get Session object from DWR's WebContext API.
			 */
			WebContext webContext = WebContextFactory.get();
			HttpSession session = webContext.getSession();
			UserDetails userDetails = (UserDetails)session.getAttribute(
					SessionKeys.SESSION_USER_DETAILS);
			
			/**
			 * Verify <code>currentHash</code> with the hash 
			 * available in UserHash table.
			 */
			UserHash userHash = new UserHash();
			userHash.setUserDetails(userDetails);
			userHash = userHashManager.getUserHash(userHash);
			if(!userHash.getUserHash().equals(currentHash)){
				logger.debug("Current Password entered is wrong.");
				resultCode = 1;
				logger.exiting("checkPassword", ""+resultCode);
				return resultCode;
			}
			
			/**
			 * Compare <code>newHash</code> with the hashes 
			 * available in HashHistory table.
			 * 
			 * In the HashHistory, 
			 * 		1) If there are 0 records, proceed.
			 * 		2) Else check if the password already exists.  
			 */
			HashHistory hashHistory = new HashHistory();
			hashHistory.setUserDetails(userDetails);
			List<HashHistory> historyList = 
				hashHistoryManager.getHashHistory(hashHistory);
			
			if(historyList != null){
				for(HashHistory hashHist : historyList){
					if(hashHist != null && 
							newHash.equals(hashHist.getUserHash())){
						
						logger.debug("New Password entered already exists " +
								"in the history.");
						resultCode = 2;
						logger.exiting("checkPassword", ""+resultCode);
						return resultCode;
					}
				}
			}
		}
		catch(DBException exception){
			logger.error("An exception occured while changing the password.",
					exception);
		}
		// Successful return
		resultCode = 0;
		logger.exiting("checkPassword", ""+resultCode);
		return resultCode;
	}
	

	/**
	 * Updates last login date of the user with the current date.
	 * 
	 * @param userDetails 
	 * 				UserDetails containing user details.
	 * @throws MISPException
	 */
	public void updateLastLoginDate(UserDetails userDetails) 
		throws MISPException {	
		
		Object[] params = {userDetails};
		logger.entering("updateLastLoginDate", params);
		
		try{
			userDetails.setLastLoggedInDate(new Date());
			userManager.updateUserDetails(userDetails);
		}
		catch(DBException exception){
			logger.error("An exception occured while retrieving User Details.",
					exception);
			throw new MISPException(exception);
		}		
		
		logger.exiting("updateLastLoginDate");
	}
	

	/**
	 * This method retrieves the customer and user statistics from the database.
	 * 
	 * @param loginId
	 *            User Id of the logged in user.
	 *            
	 * @return <code>Map</code> containing statistics details.
	 * 
	 * @throws <code>MISPException</code>, if any business exception occurs.
	 */
	public CustomerStatsVO getCustomerStats(int loginId) 
	throws MISPException {
		Object[] params = {loginId};
		logger.entering("getCustomerStats", params);
		
		CustomerStatsVO custStats = null;
		try {
			
			custStats = adminConfigManager.getCustomerStats(loginId);	
			custStats.setLastUpdateTimeStamp(DateUtil
					.toDateTimeMeridianString(new Date()));
		} catch (DBException exception) {
			logger.error("An exception occured while retrieving Customer " +
					"Statistics.", exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("getCustomerStats");
		return custStats;
	}

	/**
	 * This method retrieves the customer and user statistics from the database
	 * based on the login Id.
	 * 
	 * @param loginId
	 *            User Id of the logged in user.
	 * 
	 * @return instance of <code>CustomerStatsVO</code> containing statistics
	 *         details.
	 * 
	 * @throws <code>MISPException</code>, if any business exception occurs.
	 */
	public CustomerStatsVO getCustomerStatsForUser(int loginId)
			throws MISPException {
		Object[] params = { loginId };
		logger.entering("getCustomerStatsForUser", params);

		CustomerStatsVO custStats = null;
		try {
			custStats = adminConfigManager.getCustomerStats(loginId);
			custStats.setLastUpdateTimeStamp(DateUtil
					.toDateTimeMeridianString(new Date()));

		} catch (DBException exception) {
			logger.error("An exception occured while retrieving Customer "
					+ "Statistics.", exception);
			throw new MISPException(exception);
		}

		logger.exiting("getCustomerStatsForUser", custStats);
		return custStats;
	}
	
	/**
	* Method to process the session.
	*
	* @param isValid
	*         1) True, if user wants to retain the session.
	*         2) False, if user wants to end the session.
	*
	* @return Redirect URL
	*/
	public String processSession(boolean isValid) 
	{
		logger.entering("processSession");
		String redirectURL = "";
		if (isValid) {
		} else {
			try {
				redirectURL = MAVPaths.LOG_OUT_URL;
			} catch (Exception exception) {
				logger.error("An exception occured while processing Session",
						exception);
			}
		}
		logger.exiting("processSession");
		return redirectURL;
	}

	public void changeLoggedInStatus(UserHash userHash) throws MISPException {
		Object[] params = {userHash};
		logger.entering("changeLoggedInStatus", params);
		
		
		try {
			userManager.changeLoggedInStatus(userHash);

		} catch (DBException exception) {
			logger.error("An exception occured while changing the logged in status " +
					"of the User.", exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("changeLoggedInStatus");

	}
}
