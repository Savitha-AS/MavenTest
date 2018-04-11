package com.mip.application.services;

import static com.mip.application.controllers.UserController.props;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.mail.MessagingException;

import com.mip.application.dal.managers.LeaveManagementManager;
import com.mip.application.dal.managers.RoleManager;
import com.mip.application.dal.managers.UserManager;
import com.mip.application.model.RoleAccess;
import com.mip.application.model.RoleMaster;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserHash;
import com.mip.application.model.UserLeaveDetails;
import com.mip.application.model.mappings.UserHashMapping;
import com.mip.application.model.mappings.UserMappings;
import com.mip.application.services.transaction.UserTXService;
import com.mip.application.view.AdminConfigVO;
import com.mip.application.view.ReportAgentVO;
import com.mip.application.view.UserVO;
import com.mip.application.view.mappings.UserDetailsM2VMappings;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.EncryptException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.security.HashService;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.EmailUtil;
import com.mip.framework.utils.TypeUtil;

/**
 * <p>
 * <code>UserService.java</code> contains all the service layer methods 
 * pertaining to User Management use case model.
 * </p>
 * 
 * @author T H B S
 */
public class UserService {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			UserService.class);
	
	/**
	 * Set inversion of Control for <code>UserManager</code>, 
	 * <code>UserHashManager</code>, <code>UserTXService</code>,
	 * and <code>LoginService</code>.
	 */
	private UserManager userManager;	
	private RoleManager roleManager;
	private UserTXService userTXService;	
	private LoginService loginService;
	private LeaveManagementManager leaveManagementManager;
			
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void setUserTXService(UserTXService userTXService) {
		this.userTXService = userTXService;
	} 

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	
	public void setLeaveManagementManager(
			LeaveManagementManager leaveManagementManager) {
		this.leaveManagementManager = leaveManagementManager;
	}

	/**
	 * Registers the user details into database.
	 * 
	 * @param userVO 
	 * 				UserVO containing the data to be added into database.
	 * @param loginUserId 
	 * 				contains user id of the logged in user.
	 * @throws MISPException	 
	 */
	
	public String registerUser(UserVO userVO, int loginUserId) 
			throws MISPException{	
			
		Object[] params = {userVO, ""+loginUserId};
		logger.entering("registerUser", params);	
		
		String userHashStr = null;
		String userUid = null;
		
		try{
			/**
			 * TODO : check for synchronizing the actual definition of 
			 * below method. 
			 */
			// Get userId and auto-increment it
			int userId = userManager.getMaxUserIdFromDB();
			userId++;		// Set next sequence for UserUid.
			
			AdminConfigVO adminConfigVO = loginService.getConfigDetails();
			userUid = (new StringBuilder(adminConfigVO.getUserLoginPrefix())
							.append(userId)).toString();
			userVO.setUserUid(userUid);
			
			userHashStr = HashService.encrypt(adminConfigVO.getDefaultPwd());
			
			if(userVO.getUserId() != null){
				userVO.setUserId(null);
				// This assignment indicates that the operation is INSERT,
				// 	in the method UserMappings.mapUserVOToUserDetailsModel().
			}
			UserDetails userDetails = UserMappings.
					mapUserVOToUserDetailsModel(userVO, loginUserId, 
							new UserDetails());
			UserHash userHash = UserHashMapping.
					mapUserHashDetailsModel(userHashStr, userDetails);

			userTXService.registerUser(userDetails, userHash);
		}
		catch(EncryptException exception) {
			logger.error("An exception occured while encrypting the " +
					"default Password.", exception);
			throw new MISPException(exception);
		}
		catch(DBException exception){
			logger.error("An exception occured while registering User Details.",
					exception);
			throw new MISPException(exception);
		}
		catch(MISPException exception){
			logger.error("An exception occured while mapping VO to Model.",
					exception);
			throw new MISPException(exception);
		}
		logger.exiting("registerUser",userUid);
		return userUid;
	}
		
	/**
	 * Gets a list of User Details
	 * 
	 * @return List of User Details
	 * @throws MISPException
	 */
	public List<UserDetails> listUserDetails()
				throws MISPException{
	
		logger.entering("listUserDetails");
		
		List<UserDetails> userList = null;
		try{
			userList = userManager.getUserDetailsList();
			
		}catch(DBException exception){
			logger.error("An exception occured while fetching list of " +
					"User Details.", exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("listUserDetails");
		return userList;
	}
	
	/**
	 * Gets a particular user's User Details.
	 * Maps the table data to an appropriate View Object, and 
	 * sends it back to the controller.
	 * 
	 * @param userId
	 * 				User Id of the user.
	 * @return
	 * 			UserVO, the view object used for representing user data in
	 * 			all the use cases of User Management use case model. 
	 * @throws MISPException
	 */
	public UserVO getUserDetails(String userId)
			throws MISPException{
			
		Object[] params = {userId};
		logger.entering("getUserDetails", params);
		
		UserVO userVO = new UserVO();
						
		try{
			UserDetails userDetails = userManager.getUserDetails(
					new Integer(userId));
			
			userVO = UserDetailsM2VMappings.mapUserModelToUserVO(userDetails);
				
		}catch(DBException exception){
			logger.error("An exception occured while fetching " +
					"User Details based on a user id.", exception);
			throw new MISPException(exception);
		}
		catch(MISPException exception){
			logger.error("An exception occured while mapping Model to VO.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("getUserDetails", userVO);
		return userVO; 
	}
	
	/**
	 * Searches a user record based on the criteria fields in UserVO.
	 * 
	 * @param userVO UserVO containing search criteria fields.
	 * @return List of user records, matching the search criteria.
	 * @throws MISPException
	 */
	public List<UserDetails> searchUserDetails(UserVO userVO) 
			throws MISPException{
			
		Object[] params = {userVO};
		logger.entering("searchUserDetails", params);
		
		List<UserDetails> userList = null;
		try{
			UserDetails userDetails = UserMappings.
								mapUserVOToUserDetailsModel(userVO, -1, 
										new UserDetails());
								// Here, -1 is a dummy value.
			
			userList = userManager.searchUser(userDetails, false);	
		}
		catch(DBException exception){
			logger.error("An exception occured while a list of fetching " +
				"User Details based on different user parameters.", exception);
			throw new MISPException(exception);
		}
		catch(MISPException exception){
			logger.error("An exception occured while mapping VO to Model.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("searchUserDetails");
		return userList;
	}
	
	/**
	 * Modifies the user details in the database.
	 * 
	 * @param userVO 
	 * 			UserVO containing the data to be modified.
	 * @param loginUserId
	 * 			User Id of the logged in user.
	 * @throws MISPException
	 */
	public void modifyUserDetails(UserVO userVO, 
			int loginUserId) throws MISPException{
			
		Object[] params = {userVO,loginUserId};
		logger.entering("modifyUserDetails", params);
						
		try{
			UserDetails userDetails = userManager.getUserDetails(
					TypeUtil.toInt(userVO.getUserId()));
			
			//logger.debug("userDetails : ",userDetails);
			
			userDetails = UserMappings.mapUserVOToUserDetailsModel(userVO,
								loginUserId,userDetails);
			
			userManager.updateUserDetails(userDetails);
		}catch(DBException exception){
			logger.error("An exception occured while modifying user details.", 
						exception);
			throw new MISPException(exception);
		}
		catch(MISPException exception){
			logger.error("An exception occured while mapping VO to Model.",
					exception);
			throw new MISPException(exception);
		}

		logger.exiting("modifyUserDetails", loginUserId);
	}
	
	/**
	 * Gets role details from the database, and puts it into a 
	 * <code>java.util.Map</code>.
	 *
	 * @return <code>Map</code> containing the role details.
	 *  
	 * @throws MISPException
	 */
	public Map<Integer, String> getAllRoles() throws MISPException{
			
		logger.entering("getAllRoles");
			
		Map<Integer, String> roleMap = new HashMap<Integer, String>();
		try{			
			List<RoleMaster> roleMasterList = roleManager.getRolesList();
			
			for(RoleMaster roleMaster : roleMasterList){				
				roleMap.put(roleMaster.getRoleId(), roleMaster.getRoleName());
			}
			
		}catch(DBException exception){
			logger.error("An exception occured while fetching all roles.", 
						exception);
			throw new MISPException(exception);
		}

		logger.exiting("getAllRoles");
		return roleMap;
	}
	
	/**
	 * Invoked as a DWR call, this method checks if the input MSISDN 
	 * already exists in the database.
	 * 
	 * @param msisdn input MSISDN.
	 * 
	 * @return true if exists and false otherwise.
	 */
	public boolean checkMSISDN(String msisdn, String userID)
	{
		boolean isMSISDNExisting = false;
		try {
			isMSISDNExisting = userManager.checkIfMSISDNExists(msisdn, 
													Integer.valueOf(userID));
		} catch (DBException exception) {
			logger.error("An error occured while " +
					"validating user's MSISDN", exception);
		}
		return isMSISDNExisting;
	}

	/**
	 * Gets all Users leave details, and puts it into a 
	 * <code>java.util.Map</code>.
	 * 
	 * @param agentReportVO
	 * 			AgentReportVO containing the date range
	 * 
	 * @return <code>Map</code> containing leave details
	 * 
	 * @throws MISPException
	 */
	public Map<Integer, List<Map<String, String>>> 
	getAllWeeksLeave(ReportAgentVO agentReportVO) throws MISPException {
		Object[] params = {agentReportVO};
		logger.entering("getAllWeeksLeave", params);
		
		// Holds leave date and leave reason
		Map<String, String> dailyLeaveMap = null;
		
		List<Map<String, String>> dailyLeaveMapList = null;
		
		// Holds leave data for all weeks
		Map<Integer, List<Map<String, String>>> allWeeksleave;
		
		List<UserLeaveDetails> userLeaveDetailsList;
		try{
			Date now = null;
			Calendar start = Calendar.getInstance();
			start.setTime(DateUtil.toDate(agentReportVO.getFromDate()));
			Calendar end = Calendar.getInstance();
			end.setTime(DateUtil.toDate(agentReportVO.getToDate()));
			
			allWeeksleave = new HashMap<Integer, List<Map<String, String>>>();
			
			userLeaveDetailsList = userManager.getUserLeaveDetails();
			
			// TODO : Needs re-implementation to improve performance. 
			// Converting list of leave details into map
			String leaveDate;
			for(; !start.after(end); start.add(Calendar.DATE, 1)) {
				for(UserDetails userDetails : agentReportVO.getUserDetailsList()) {
					dailyLeaveMapList = new ArrayList<Map<String,String>>();

					now = start.getTime();					
					for(UserLeaveDetails userLeaveDetails : userLeaveDetailsList) {
						leaveDate = DateUtil.toDateString(userLeaveDetails.getLeaveDate());
						
						if(userLeaveDetails.getUser().getUserId() == userDetails.getUserId()) {
							
							if(leaveDate.equals(DateUtil.toDateString(now))) {
								dailyLeaveMap = new HashMap<String, String>();
								dailyLeaveMap.put(leaveDate, userLeaveDetails.getReason());
								
								dailyLeaveMapList.add(dailyLeaveMap);
							}
						}
					}
					if(dailyLeaveMapList.size() > 0) {
						if(allWeeksleave.containsKey(userDetails.getUserId()))
							allWeeksleave.get(userDetails.getUserId()).add(dailyLeaveMap);
						else
							allWeeksleave.put(userDetails.getUserId(), dailyLeaveMapList);
					}
				}
			}
		}
		catch(DBException e) {
			logger
			.error(
					"Error occured while getting the leave details",
					e);
			throw new MISPException(e);
		}
		
		logger.exiting("getAllWeeksLeave");
		return allWeeksleave;
	}
	
	//fetching userLeave list details here
	public Map<Integer, Integer> userYearLeaveDetails(List<UserDetails> userList) throws MISPException{
		logger.entering("listUserLeaveDetails");
		Map<Integer, Integer> yearLeaveMap = new HashMap<Integer, Integer>();
		int yearLeave = 0;
		try{
		List<UserLeaveDetails> userLeaveList=leaveManagementManager.getLeaveRecordsList();
		
		for(UserDetails userDetails : userList) {
			yearLeave = 0;
			for (UserLeaveDetails userLeaveDetails : userLeaveList) {
				
				if(userDetails.getUserId() == userLeaveDetails.getUser().getUserId()) {
					
					if (DateUtil.getYear(new Date())== DateUtil.getYear(userLeaveDetails.getLeaveDate())) {
						yearLeave++;
					}
				}
			}
		yearLeaveMap.put(userDetails.getUserId(),yearLeave);
		}
		}catch(DBException exception){
			logger.error("An exception occured while fetching list of leave records.", 
					exception);
		throw new MISPException(exception);
	}
		return yearLeaveMap;
	}
	
	//fetches the currentmonth leave details
	public Map<Integer, Integer> userMonthLeaveDetails(List<UserDetails> userList) throws MISPException{
		logger.entering("userMonthLeaveDetails");
		Map<Integer, Integer> monthLeaveMap = new HashMap<Integer, Integer>();
		int monthLeave = 0;
		try{
		List<UserLeaveDetails> userLeaveList=leaveManagementManager.getLeaveRecordsList();
		for(UserDetails userDetails : userList) {
			monthLeave = 0;
			for (UserLeaveDetails userLeaveDetails : userLeaveList) {
				
				if(userDetails.getUserId() == userLeaveDetails.getUser().getUserId()) {
					
					if (DateUtil.getMonth(new Date()) == DateUtil
							.getMonth(userLeaveDetails.getLeaveDate())) {
						monthLeave++;
					}
				}
			}
		monthLeaveMap.put(userDetails.getUserId(), monthLeave);
		}
		}catch(DBException exception){
			logger.error("An exception occured while fetching list of leave records.", 
					exception);
		throw new MISPException(exception);
	}
		return monthLeaveMap;
	}
	
	
	//trying send the list
	public List<UserLeaveDetails> userCurrentLeaveDetails()
			throws MISPException {
		logger.entering("userCurrentLeaveDetails");
		List<UserLeaveDetails> userLeaveList = null;
		try {
			userLeaveList = leaveManagementManager.getLeaveRecordsList();
		} catch (DBException exception) {
			logger.error(
					"An exception occured while fetching list of leave records.",
					exception);
			throw new MISPException(exception);
		}
		logger.exiting("userCurrentLeaveDetails");
		return userLeaveList;
	}
	
	/**
	 * This method checks if the input User ID already exists in the database.
	 * 
	 * @param userUid input User login ID.
	 * 
	 * @return true,true if user and an email-id exists for user
	 * 		true,false if user exists but no email-id
	 * 		false,false if user and email does not exist.
	 */
	public String checkIfUserExists(String userUID)
	{
		boolean isUserExisting = false;
		boolean isEmailIDExisting = false;
		String result="";
		
		logger.entering("checkIfUserExists", userUID);
		
		try {
			isUserExisting = userManager.checkIfUserExists(userUID);
			
			isEmailIDExisting = userManager.checkEmailIDForUser(userUID);
			
			result = String.valueOf(isUserExisting)+","
									+ String.valueOf(isEmailIDExisting);
			
			
		} catch (DBException exception) {
			logger.error("An error occured while " +
					"validating user's ID for existance and email", exception);
		}
		
		logger.exiting("checkIfUserExists", result);
		
		return result;
	}
	
	/**
	 * This method is used to get the user ID based on the user UID
	 * 
	 * @param userUID - user login ID
	 * 
	 * @return userID - user ID of user.
	 * @throws MISPException 
	 */
	public String getUserID(String userUID) throws MISPException{
		
		logger.entering("getUserID", userUID);
		
		String userID="";
		try {
			userID = userManager.getUserID(userUID);
		} catch (DBException exception) {
			logger.error("Exception occurred while fetching user ID", exception);
			throw new MISPException(exception);
		}
		
		logger.exiting("getUserID", userID);
		
		return userID;
	}
	
	/**
	 * This method is used to generate an email to be sent to the user when
	 * he submits his UID for forgot password
	 * 
	 * @param userUID input login ID of user
	 * 
	 * @return 
	 */
	public boolean generateAndSendEmail(String userUID) {
		
		logger.entering("generateAndSendEmail", userUID);
		boolean isEmailSent = false;
		try {
			String userEmail = "";
			userEmail = userManager.getEmailIDForUser(userUID);

			String ID = getUserID(userUID);
			
			String hashedData = "";

			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
			String currentDate = formatter.format(cal.getTime());

			String dataToHash = ID.concat(currentDate);

			hashedData = HashService.encrypt(dataToHash);
			
			String urlData = props.getProperty("mail.url.domain")
					+ props.getProperty("mail.url.page")
					+ props.getProperty("mail.url.paramID") + ID
					+ props.getProperty("mail.url.paramHashValue") + hashedData;

			EmailUtil objMail = new EmailUtil();
			String body = "Dear User,© Please click or copy the below URL to reset your"
					+ " password.©"
					+ urlData
					+ " © <b>Note</b> : The URL is valid only for " 
					+ DateUtil.toDateString(new Date(), "MMM dd yyyy")
					+ ".© Regards, © MIP Support ©**** This is an auto-generated email. " 
					+ "Please do not reply to this email. **** ";
			objMail.send_mail(props.getProperty("mail.account.username"),
					userEmail, "MIP Password Reset", body);
			
			isEmailSent = true;
		}
		catch (DBException exception) {
			logger.error("An error occurred while "
					+ "fetching user's email ID", exception);
		} catch (EncryptException encryptException) {
			logger.error("Exception occurred while encrypting data",
					encryptException);
		} catch (MessagingException messageException) {
			logger.error("Exception occurred while sending email",
					messageException);
		} catch (Exception exception) {
			logger
					.error("Exception occurred while generating email",
							exception);
		}
		logger.exiting("generateAndSendEmail", isEmailSent);
		return isEmailSent;
	}
	
	/**
	 * This method is used to validate the URL input across the generated
	 * hash value and based on result will reset the password for the user.
	 * 
	 * @param userID - user ID number removing the prefix from it
	 * @param hashData - hash data received from url
	 * @return true - if password is reset.
	 * 			false - if password reset failed
	 * @throws MISPException 
	 */
	
	public boolean valUrlInputForPassReset(String hashData,
								UserVO userVO) throws MISPException{
		
		logger.entering("valUrlInputForPassReset", userVO, hashData);
		
		String hashedData = "";
		boolean isResetSuccess = false;
		
		try {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
			String currentDate = formatter.format(cal.getTime());

			String dataToHash = userVO.getUserId().concat(currentDate);
			hashedData = HashService.encrypt(dataToHash);

			logger.debug("hashedData : ", hashedData);
			logger.debug("hashData : ", hashData);
			
			if (hashedData.equals(hashData)) {
				loginService.resetPassword(userVO);
				isResetSuccess = true;

			} else {
				logger.error("Hash data mismatch");
			}
		}catch (EncryptException encryptException) {
			logger.error("Exception occurred while encrypting data"
					,encryptException);
			
			throw new MISPException(encryptException);
		}catch(MISPException exception){			
			logger.error("An exception occured while resetting " +
					"password of the user.", exception);
			
			throw new MISPException(exception);
		}catch (Exception exception){					
			logger.error("An exception occured while resetting " +
					"password of the user.", exception);
			
			throw new MISPException(exception);
		}
		return isResetSuccess;
	}
	
	
	/**
	 * Method to get all roles for given url.
	 * 
	 * @return an instance list of <code>String</code> representing allRoles.
	 */
	public  Map<String, String> getAllRolesAllowed(){
		
		Map<String, String> allRolesAllowedMap = new HashMap<String, String>();
		
		try {
			
			List<RoleAccess> allRoleNamesList = this.userManager.getAllRoleNamesList();
			
			for(RoleAccess roleAccess: allRoleNamesList)
			{
				allRolesAllowedMap.put(roleAccess.getRoleUrl(), roleAccess.getRoleAllowed());
			}
			
		}
		catch (DBException e) {
			
			e.printStackTrace();
		}
		
		return allRolesAllowedMap;
	}
	
	/**
	 * Method to get all roles for given url.
	 * 
	 * @param allRolesAllowedMap an instance of <code>Map</code> 
	 * 		  representing  allRolesAllowedMap.
	 *  
	 * @param url an instance of <code>String</code> representing url.
	 * 
	 * @return an instance list of <code>String</code> representing allRoles.
	 * 
	 * @throws MISPException in case of any failure.
	 */
	public List<String> getRolesByURL(
			Map<String, String> allRolesAllowedMap, String url) 
				throws MISPException
	{
		
		List<String> roleNamesAllowed = new ArrayList<String>();
		
		try {
		
			for(int i=0; i< allRolesAllowedMap.size(); i++) {
				String urlPattern = "";
				urlPattern = (String)allRolesAllowedMap.keySet().toArray()[i];
				if(urlPattern.contains("*")) {
					if(url.startsWith(urlPattern.substring(0, urlPattern.length()-1))) {
						url=urlPattern;
					}
				}
			}
			
			if(url.contains("?")){
				url = url.substring(url.indexOf("/"), url.indexOf("?"));
			}
			
			if(allRolesAllowedMap.containsKey(url)){
				StringTokenizer stringTokenizer = 
						new StringTokenizer(allRolesAllowedMap.get(url), ",");
				while(stringTokenizer.hasMoreElements()){
					roleNamesAllowed.add((String)stringTokenizer.nextElement());
				}
			}
		} 
		catch (Exception exception) {
			
			logger.error("An exception occured while registering User Details.",
					exception);
			throw new MISPException(exception);
		}
		return roleNamesAllowed;
	}
}
