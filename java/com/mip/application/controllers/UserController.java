package com.mip.application.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.ReportKeys;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.BranchDetails;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserLeaveDetails;
import com.mip.application.services.BranchService;
import com.mip.application.services.LoginService;
import com.mip.application.services.ReportManagementService;
import com.mip.application.services.UserService;
import com.mip.application.view.UserVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.EncryptException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.TypeUtil;

/**
 * <p>
 * <code>UserController.java</code> contains all the methods pertaining to User
 * Management use case model. User Management use case model includes following
 * use cases : 
 * <table border="1">
 * 	<tr><th width="5%">Sl. No.</th><th>Use Case</th><th>Dispatcher action method signature</th></tr>
 * 	<tr><td>1.</td><td>Add User</td><td>{@link #addUser(HttpServletRequest,HttpServletResponse,UserVO)}</td></tr>
 * 	<tr><td>2.</td><td>List Users</td><td>{@link #listUsers(HttpServletRequest,HttpServletResponse)}</td></tr>
 * 	<tr><td>3.</td><td>View User Details</td><td>{@link #showUserDetails(HttpServletRequest,HttpServletResponse,UserVO)}</td></tr>
 * 	<tr><td>4.</td><td>Search User</td><td>{@link #findUserByUserDetails(HttpServletRequest,HttpServletResponse,UserVO)}</td></tr>
 * 	<tr><td rowspan="2">5.</td><td rowspan="2">Modify User Details</td><td>{@link #showModifyUserDetails(HttpServletRequest,HttpServletResponse,UserVO)}</td></tr>
 * 	<tr><td>{@link #modifyUserDetails(HttpServletRequest,HttpServletResponse,UserVO)}</td></tr>
 * 	<tr><td rowspan="2">6.</td><td rowspan="2">Search User & Reset Password</td><td>{@link #findUserByUserId(HttpServletRequest,HttpServletResponse,UserVO)}</td></tr>
 * 	<tr><td>{@link #resetPassword(HttpServletRequest,HttpServletResponse,UserVO)}</td></tr>
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
public class UserController extends BasePlatformController {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			UserController.class);

	public static Properties props = null;
	
	/**
	 * Set inversion of Control for <code>UserService</code>
	 * <code>LoginService</code> and <code>BranchService</code>
	 */
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	private LoginService loginService;

	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	
	private BranchService branchService;
	
	public void setBranchService(BranchService branchService) {
		this.branchService = branchService;
	}
	
	private ReportManagementService reportManagementService;
	
	public void setReportManagementService(
			ReportManagementService reportManagementService) {
		this.reportManagementService = reportManagementService;
	}

	/**
	 * Gets all the roles to populate the role field in the Add User and 
	 * Modify User use cases.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getRolesAndBranches(HttpServletRequest request,
			HttpServletResponse response) {
		logger.entering("getRolesAndBranches");

		ModelAndView mav = null;
		List<BranchDetails> branchList = null;
		try {
			mav = getRoles(request, response);
			branchList = branchService.listBranchDetails();

			mav.setViewName(MAVPaths.VIEW_USER_ADD);
			mav.addObject(MAVObjects.LIST_BRANCH, branchList);
		} 
		catch(MISPException exception) {			
			logger.error("An exception occured while fetching list of all " +
					"roles/ branches.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_ADD);
		}
		catch (Exception exception){			
			logger.error("An exception occured while fetching list of all " +
					"roles/ branches.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_ADD);
		}	

		logger.exiting("getRolesAndBranches");
		return mav;
	}

	/**
	 * Gets all the roles to populate the role field in the Search User  
	 * use cases.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getRoles(HttpServletRequest request,
			HttpServletResponse response) {
		logger.entering("getRoles");

		ModelAndView mav = null;
		Map<Integer, String> roleMap = null;
		try {
			roleMap = userService.getAllRoles();

			mav = new ModelAndView(MAVPaths.VIEW_USER_SEARCH);
			mav.addObject(MAVObjects.MAP_ROLES, roleMap);
		} 
		catch(MISPException exception) {			
			logger.error("An exception occured while fetching list of all " +
					"roles.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_SEARCH);
		}
		catch (Exception exception){			
			logger.error("An exception occured while fetching list of all " +
					"roles.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_SEARCH);
		}	

		logger.exiting("getRoles");
		return mav;
	}
	
	/**
	 * This method handles the operation of adding a User Details record 
	 * into database. 
	 * 
	 * TODO : Tokenization has to be done, to avoid multiple form submits.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param userVO
	 *            UserVO command object
	 * 
	 * @return upon success redirect to <code>addUser.jsp</code>, and on failure
	 *         to <code>global_error_page.jsp</code>
	 */
	public ModelAndView addUser(HttpServletRequest request,
			HttpServletResponse response, UserVO userVO) {
		Object[] params = {userVO};
		logger.entering("addUser", params);

		ModelAndView mav = null;
		
		HttpSession session = request.getSession();
		UserDetails userDetails = (UserDetails) session
				.getAttribute(SessionKeys.SESSION_USER_DETAILS);
		int loggedInUserId = userDetails.getUserId();
		
		String userUid = null;
		try {
			userUid = userService.registerUser(userVO,loggedInUserId);

			Object[] msgParams = {userUid};
			mav = super.success(SuccessMessages.USER_ADDED, 
					TypeUtil.arrayToCsv(msgParams),
					MAVPaths.JSP_USER_ADD);
			mav.addObject("userUid", userUid);
		} 
		catch(MISPException exception) {			
			logger.error("An exception occured while adding a new  " +
					"user details record.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_ADD);
		}
		catch (Exception exception){						
			logger.error("An exception occured while adding a new  " +
					"user details record.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_ADD);
		}	

		logger.exiting("addUser");
		return mav;
	}

	/**
	 * This method fetches a list of user records from database.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @return upon success redirect to <code>listUser.jsp</code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView listUsers(HttpServletRequest request,
			HttpServletResponse response) {
		
		logger.entering("listUsers");
		
		ModelAndView mav = null;

		HttpSession session = request.getSession();
		UserDetails userDetails = (UserDetails) session
				.getAttribute(SessionKeys.SESSION_USER_DETAILS);
		List<UserDetails> userList = null;
		Map<Integer, Integer> currentYearLeaveMap = new HashMap<Integer, Integer>();
		Map<Integer, Integer> currentMonthLeaveMap =new HashMap<Integer, Integer>();
		List<UserLeaveDetails> getCurrentLeaveList=null;
		try {
			userList = userService.listUserDetails();	
			getCurrentLeaveList=userService.userCurrentLeaveDetails();
			int yearLeave=0;
			int monthLeave=0;
			
			for(UserDetails userDetails1 : userList) {
				yearLeave = 0;
				monthLeave=0;
				for (UserLeaveDetails userLeaveDetails : getCurrentLeaveList) {
					
					if(userDetails1.getUserId() == userLeaveDetails.getUser().getUserId()) {
						
						if (DateUtil.getYear(new Date())== DateUtil.getYear(userLeaveDetails.getLeaveDate())) {
							yearLeave++;
							
							if (DateUtil.getMonth(new Date()) == DateUtil
									.getMonth(userLeaveDetails.getLeaveDate())) {
								monthLeave++;
							}
						}	
					}
				}
				currentYearLeaveMap.put(userDetails1.getUserId(),yearLeave);
				currentMonthLeaveMap.put(userDetails1.getUserId(), monthLeave);
			}
			/*currentYearLeaveMap = userService.userYearLeaveDetails(userList);
			currentMonthLeaveMap = userService.userMonthLeaveDetails(userList);*/
			
			userDetails.setCurrentYearLeaveMap(currentYearLeaveMap);
			userDetails.setCurrentMonthLeaveMap(currentMonthLeaveMap);
			
			mav = new ModelAndView(MAVPaths.VIEW_USER_LIST);
			mav.addObject(MAVObjects.LIST_USER, userList);
			mav.addObject(MAVObjects.MAP_LEAVE_YEAR, currentYearLeaveMap);
			mav.addObject(MAVObjects.MAP_LEAVE_MONTH ,currentMonthLeaveMap);
		} 
		catch (MISPException exception) {		
			logger.error("An exception occured while fetching  " +
					"a list of users.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_LIST);
		}
		catch (Exception exception){			
			logger.error("An exception occured while fetching  " +
					"a list of users.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_LIST);
		}	

		logger.exiting("listUsers");
		return mav;
	}

	/**
	 * This method fetches a particular user details record from database. 
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param userVO
	 *            UserVO command object
	 * 
	 * @return upon success redirect to <code>userDetails.jsp</code>, 
	 * 			and on failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView showUserDetails(HttpServletRequest request,
			HttpServletResponse response, UserVO userVO) {
		Object[] params = {userVO};
		logger.entering("showUserDetails", params);

		ModelAndView mav = null;
		try {
			userVO = userService.getUserDetails(userVO.getUserId());
			
			mav = new ModelAndView(MAVPaths.VIEW_USER_DETAILS);
			mav.addObject(MAVObjects.VO_USER, userVO);
		} 
		catch (MISPException exception) {
			logger.error("An exception occured while fetching a particular " +
					"user details record.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_DETAILS);
		}
		catch (Exception exception){			
			logger.error("An exception occured while fetching a particular " +
					"user details record.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_DETAILS);
		}	

		logger.exiting("showUserDetails");
		return mav;
	}


	/**
	 * This method modifies the user details record as entered by the business
	 * administrator.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param userVO
	 *            UserVO command object
	 * 
	 * @return upon success redirect to <code>searchUser.jsp</code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView modifyUserDetails(HttpServletRequest request,
			HttpServletResponse response, UserVO userVO) {
		
		Object[] params = {userVO};
		logger.entering("modifyUserDetails", params);

		ModelAndView mav = null;

		String userId = request.getParameter("userId");

		HttpSession session = request.getSession();
		int loginUser = ((UserDetails) session
				.getAttribute(SessionKeys.SESSION_USER_DETAILS)).getUserId();

		try {
			userVO.setUserId(userId);
			userService.modifyUserDetails(userVO,loginUser);

			mav = super.success(SuccessMessages.USER_MODIFIED, 
						MAVPaths.JSP_USER_SEARCH);
		} 
		catch (MISPException exception) {
			logger.error("An exception occured while updating " +
					"user details record.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_SEARCH);
		}
		catch (Exception exception){	
			logger.error("An exception occured while updating " +
					"user details record.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_SEARCH);
		}	

		logger.exiting("modifyUserDetails");
		return mav;
	}

	/**
	 * This method fetches user details from DB and populates the screen so that 
	 * the administrator can modify the details of a user from the platform.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param userVO
	 *            UserVO command object
	 * 
	 * @return upon success redirect to <code>searchUser.jsp</code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView showModifyUserDetails(HttpServletRequest request,
			HttpServletResponse response, UserVO userVO) {
		
		Object[] params = {userVO};
		logger.entering("showModifyUserDetails", params);

		ModelAndView mav = null;
		Map<Integer, String> roleMap = null;
		List<BranchDetails> branchList = null;

		String userId = request.getParameter("userId");
		try {
			userVO = userService.getUserDetails(userId);			
			roleMap = userService.getAllRoles();
			branchList = branchService.listBranchDetails();

			mav = new ModelAndView(MAVPaths.VIEW_USER_MODIFY);
			mav.addObject(MAVObjects.VO_USER, userVO);
			mav.addObject(MAVObjects.MAP_ROLES, roleMap);
			mav.addObject(MAVObjects.LIST_BRANCH, branchList);
		} 
		catch (MISPException exception) {
			logger.error("An exception occured while fetching an existing  " +
					"user details record for modifying.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_SEARCH);
		}
		catch (Exception exception){	
			logger.error("An exception occured while fetching an existing  " +
					"user details record for modifying.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_SEARCH);
		}	

		logger.exiting("showModifyUserDetails");
		return mav;
	}
	
	/**
	 * This re-usable method searches for a particular user in database, 
	 * based on different parameters, in case of following 2 use cases: 
	 * 	1) Search User - First Name, Surname and/or MSISDN are passed 
	 * 						as the input parameters.
	 * 	2) Reset Password - User UId is passed as the input parameter.
	 * 
	 * The service method, however, uses Criteria Query to handle both use cases.
	 * 
	 * Hence, this method is declared as a <code>private</code>, so that 
	 * this method can be invoked from actual dispatcher action methods 
	 * to get the job done. 
	 * 
	 * @param userVO
	 *            	UserVO command object
	 * @param viewName
	 * 				View name, which is the output view on success 
	 * 				of the operation.
	 * @param jspName
	 * 				The actual path of JSP, which is given as the link in the 
	 * 				<code>global_error_page.jsp</code> on failure
	 * 				of the operation.
	 * @return 	upon success redirect to a view specified by 
	 * 			<code>viewName</code> variable, and on failure to 
	 * 			<code>global_error_page.jsp</code>
	 */
	private ModelAndView searchUser(UserVO userVO,
			String viewName, String jspName){
		
		Object[] params = {userVO, viewName, jspName};
		logger.entering("searchUser", params);

		ModelAndView mav = null;

		List<UserDetails> userList = null;
		List<UserLeaveDetails> leaveList = null;
		Map<Integer, String> monthLeaveMap = null;
		Map<Integer, String> yearLeaveMap = null;
		Map<String, Map<Integer, String>> leaveMap = null;
		StringBuilder yearLeaves = null;
		StringBuilder monthLeaves = null;
		
		try {
			
			userList = userService.searchUserDetails(userVO);
			
			leaveList = userService.userCurrentLeaveDetails();
			
			monthLeaveMap = new HashMap<Integer, String>();
			yearLeaveMap = new HashMap<Integer, String>();
			
			for(UserDetails userDetails : userService.listUserDetails()) {
				monthLeaves = new StringBuilder("");
				yearLeaves = new StringBuilder("");
				for (UserLeaveDetails userLeaveDetails : leaveList) {
					
					if(userDetails.getUserId() == userLeaveDetails.getUser().getUserId()) {
						
						if (DateUtil.getYear(new Date()) == DateUtil
								.getYear(userLeaveDetails.getLeaveDate())) {
							yearLeaves.append(DateUtil.toDateString(userLeaveDetails.getLeaveDate()));
							yearLeaves.append(", ");
							
							if (DateUtil.getMonth(new Date()) == DateUtil
									.getMonth(userLeaveDetails.getLeaveDate())) {
								monthLeaves.append(DateUtil.toDateString(userLeaveDetails.getLeaveDate()));
								monthLeaves.append(", ");
							}
						}
					}
				}
				// Removes comma from the end
				if(monthLeaves.lastIndexOf(",") != -1)
					monthLeaves.deleteCharAt(monthLeaves.lastIndexOf(","));
				if(yearLeaves.lastIndexOf(",") != -1)
					yearLeaves.deleteCharAt(yearLeaves.lastIndexOf(","));
				
				monthLeaveMap.put(userDetails.getUserId(), monthLeaves.toString());
				yearLeaveMap.put(userDetails.getUserId(), yearLeaves.toString());
				
			}
			
			leaveMap = new HashMap<String, Map<Integer,String>>();
			leaveMap.put(MAVObjects.MAP_LEAVE_MONTH, monthLeaveMap);
			leaveMap.put(MAVObjects.MAP_LEAVE_YEAR, yearLeaveMap);
			
			logger.debug("Leave Map : ", leaveMap);
			
			mav = new ModelAndView(viewName);
			mav.addObject(MAVObjects.LIST_USER, userList);
			mav.addObject(MAVObjects.MAP_LEAVE_MONTH_YEAR, leaveMap);
		} 
		catch (MISPException exception) {
			logger.error("An exception occured while searching for a " +
					"user details record.", exception);			
			mav = super.error("", jspName);
		}
		catch (Exception exception){	
			logger.error("An exception occured while searching for a " +
					"user details record.", exception);			
			mav = super.error("", jspName);
		}
		
		logger.exiting("searchUser");
		return mav;
	}

	/**
	 * This method handles Search User use case and invokes the 
	 * <code>searchUser()</code> method to do the job.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param userVO
	 *            UserVO command object
	 * @return upon success redirect to <code>searchUser.jsp</code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	public ModelAndView findUserByUserDetails(HttpServletRequest request,
			HttpServletResponse response, UserVO userVO){
		
		Object[] params = {userVO};
		logger.entering("findUserByUserDetails",params);

		String viewName = MAVPaths.VIEW_USER_SEARCH;
		String jspName = MAVPaths.JSP_USER_SEARCH;
		
		/**
		 * Added : 24 Feb 2012
		 * Problem with pagination in search user.
		 * NULL check added
		 */
		// Nullifies the role in case no role is selected
		if(null != userVO.getRole() && userVO.getRole().equals("-1"))
			userVO.setRole(null);
		
		ModelAndView roleMav = getRoles(request, response); 
		ModelAndView mav = searchUser(userVO, viewName, jspName);	
		
		@SuppressWarnings("rawtypes")
		Map roleMavModel = roleMav.getModel();
		if(roleMavModel != null){
			@SuppressWarnings("unchecked")
			Map<Integer, String> roleMap = (Map<Integer, String>)
					roleMavModel.get(MAVObjects.MAP_ROLES);
			
			mav.addObject(MAVObjects.MAP_ROLES, roleMap);
		}
		logger.exiting("findUserByUserDetails");
		return mav;
	}

	/**
	 * This method is invoked when the administrator wants to resets the 
	 * password and/or account lock status of a user, 
	 * for which the first step would be to search the user, for which it 
	 * invokes the <code>searchUser()</code> method to do the job.
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param userVO
	 *            UserVO command object
	 * @return upon success redirect to <code>resetPassword.jsp</code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	
	public ModelAndView findUserByUserId(HttpServletRequest request,
			HttpServletResponse response, UserVO userVO){
		
		Object[] params = {userVO};
		logger.entering("findUserByUserId",params);
		
		/**
		 * Just to ensure that, in case multiple searches are done,
		 * 		User Id doesn't get retained. 
		 */
		userVO.setUserId(null);
		/**
		 * To ensure Role doesn't get retained in Reset Password 
		 * use cases.
		 */
		userVO.setRole(null);
		
		String viewName = MAVPaths.VIEW_USER_RESET_PASSWORD;
		String jspName = MAVPaths.JSP_USER_RESET_PASSWORD;
		
		ModelAndView mav = searchUser(userVO, viewName, jspName);
		
		logger.exiting("findUserByUserId");
		return mav;
	}
	

	/**
	 * This method is invoked when the administrator resets the password and 
	 * account lock status of a user, from the platform. 
	 * 
	 * @param request 
	 * 			an instance of <code>HttpServletRequest</code>
	 * 
	 * @param response 
	 * 			an instance of <code>HttpServletResponse</code>
	 * 
	 * @param userVO 
	 * 			UserVO command object
	 * 
	 * @return upon success redirect to <code>resetPassword.jsp</code>, and on
	 *         failure to <code>global_error_page.jsp</code>
	 */
	
	public ModelAndView resetPassword(HttpServletRequest request,
			HttpServletResponse response, UserVO userVO){
		
		Object[] params = {userVO};
		logger.entering("resetPassword",params);
		
		ModelAndView mav = null;
		try{
			loginService.resetPassword(userVO);
			
			mav = super.success(SuccessMessages.PASSWORD_RESET, 
					MAVPaths.JSP_USER_RESET_PASSWORD);
		}
		catch(MISPException exception){			
			logger.error("An exception occured while resetting " +
					"password of the user.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_RESET_PASSWORD);
		}
		catch (Exception exception){					
			logger.error("An exception occured while resetting " +
					"password of the user.", exception);			
			mav = super.error("", MAVPaths.JSP_USER_RESET_PASSWORD);
		}	
		
		logger.exiting("resetPassword");
		return mav;
	}

	/**
	 * This method handles the request and response for downloading user
	 * report.
	 * 
	 * @param httpServletRequest
	 *            an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 *            an instance of HTTPServletRequest
	 * @return <code>ModelAndView</code> object containing the response.
	 */
	public ModelAndView downloadUserReport(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {

		logger.entering("downloadUserReport");

		HttpSession session = httpServletRequest.getSession();
		UserDetails userDetails = (UserDetails) session
				.getAttribute(SessionKeys.SESSION_USER_DETAILS);
		//File reportDirectory = null;
		File userReportFile = null;
		HSSFWorkbook workbook = null;
		FileOutputStream fileOutStream = null;
		ServletOutputStream servletOutStream = null;
		FileInputStream fileInputStream = null;

		try {
			// Creates the file name
			String timeStampFormat = "ddMMyyyyHHmmss";
			String currTimeStamp = DateUtil.toDateString(new Date(), timeStampFormat);
			String reportFileName = ReportKeys.USER_REP_FILE_NAME
					+ currTimeStamp + ReportKeys.REPORT_FILE_TYPE;

			// Creates the directory for generating report
			/*reportDirectory = new File(httpServletRequest.getSession()
					.getServletContext().getRealPath("/")
					+ ReportKeys.REPORTS_FOLDER);
			if (!reportDirectory.exists()) {
				logger.debug("Creating directory");
				reportDirectory.mkdir();
			}*/

			// creates the report file
			userReportFile = new File(//reportDirectory.getAbsolutePath() + File.separator + 
					reportFileName);

			if (!userReportFile.exists()) {
				userReportFile.createNewFile();

			}

			// Writes report data to excel file
			workbook = new HSSFWorkbook();
			reportManagementService.writeUserReport(workbook,userDetails.getCurrentYearLeaveMap(),userDetails.getCurrentMonthLeaveMap());

			fileOutStream = new FileOutputStream(userReportFile);
			workbook.write(fileOutStream);
			fileOutStream.flush();

			// Sets the file for download
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition",
					"attachment; filename=" + reportFileName);
			servletOutStream = httpServletResponse.getOutputStream();
			fileInputStream = new FileInputStream(userReportFile);
			int i;
			while ((i = fileInputStream.read()) != -1) {
				servletOutStream.write(i);
			}

			servletOutStream.flush();

		} catch (IOException e) {
			logger.error(
					"Exception occured while downloading user report", e);
			return null;
		} catch (MISPException e) {
			logger.error(
					"Exception occured while downloading user report", e);
			return null;
		} catch (Exception e) {

			logger.error(
					"Exception occured while downloading user report", e);
			return null;
		} finally {
			// closes all resources
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}

				if (servletOutStream != null) {
					servletOutStream.close();
				}

				if (fileInputStream != null) {
					fileInputStream.close();
				}

				if (userReportFile != null && userReportFile.exists()) {
					userReportFile.delete();
				}
			} catch (Exception e) {
				logger.error("Exception occured while " +
								"closing resources after user report download");
				return null;
			}
		}

		logger.exiting("downloadUserReport");
		// Returns null as response has been set
		return null;
	}
	
	/**
	 * This method controls the loading of Forgot Password page
	 * 
	 * @param request - An instance of HttpServletRequest Object
	 * @param response - An instance of HttpServletResponse Object
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView loadForgotPassword(HttpServletRequest request, 
			HttpServletResponse response) 
	{
		
		logger.entering("loadForgotPassword");
		
		ModelAndView mavObj = null;
		
		try
		{	
			mavObj = new ModelAndView(MAVPaths.VIEW_USER_FORGOT_PASSWORD);
		}
		catch (Exception exception)
		{			
			logger.error(FaultMessages.GENERIC_ERROR,exception);
			
			mavObj = super.error(FaultMessages.GENERIC_ERROR);			
		}
		
		logger.exiting("loadForgotPassword",mavObj);
		
		return mavObj;
	}
	
	/**
	 * This method is used to check if the entered User ID exists in our system
	 * or not
	 * @param httpServletRequest - An instance of HttpServletRequest Object
	 * @param httpServletResponse - An instance of HttpServletResponse Object
	 * @return - Returns the ModelAndView object
	 * @throws EncryptException 
	 */
	public ModelAndView checkForUser (HttpServletRequest httpServletRequest,
							HttpServletResponse httpServletResponse) throws EncryptException {
		
		logger.entering("checkForUser");
		
		// Status value used in jsp to show the response
		boolean status = false;
		
		ModelAndView mav = null;
		
		String userUID = (String) httpServletRequest.getParameter("userUid");
		
		String userIDandEmailExists = userService.checkIfUserExists(userUID);
		
		/**
		 * userIDandEmailExists :
		 * true,false = ID exists but no email for user
		 * false, false = ID and Email does not exist for user
		 * true,true = Both ID and Email exists for user
		 */
		if(userIDandEmailExists.equals("true,false")){
			mav = loadForgotPassword(httpServletRequest, httpServletResponse);
			mav.addObject("message", FaultMessages.FORGOT_PASSWORD_INVALID_EMAIL_ID);
			mav.addObject("status", status);
		}
		else if (userIDandEmailExists.equals("false,false")) {
			mav = loadForgotPassword(httpServletRequest, httpServletResponse);
			mav.addObject("message", FaultMessages.FORGOT_PASSWORD_INVALID_USER);
			mav.addObject("status", status);
		}
		else if (userIDandEmailExists.equals("true,true")){				
			// e-mail to be prepared and sent
			props = new Properties();
        	try {
				props.load(UserController.class
						.getResourceAsStream("/mail.properties"));
			} catch (IOException exception) {
				logger.error("Unable to load mail.properties file",
						exception);
			}			
			if(userService.generateAndSendEmail(userUID)){
				status = true;
				mav = loadForgotPassword(httpServletRequest, httpServletResponse);
				mav.addObject("message", SuccessMessages.FORGOT_PASSWORD_MAIL_SUCCESS);
				mav.addObject("status", status);
			} else {
				logger.error("An exception occured while processing " +
						"password reset request.");			
				mav = super.error(FaultMessages.GENERIC_ERROR, MAVPaths.LOG_IN_URL);
			}
		}
		return mav;
	}
	
	/**
	 * This method is used to reset the password for the user based on the user
	 * hash provided in email link and the hash which we generate
	 * 
	 * @param httpServletRequest - An instance of HttpServletRequest Object
	 * @param httpServletResponse - An instance of HttpServletResponse Object
	 * @return - Returns the ModelAndView object
	 */
	public ModelAndView resetPassUsingURLHash(HttpServletRequest httpServletRequest,
								HttpServletResponse httpServletResponse){
		ModelAndView mav = null;
		try {
			
			String userID =  (String) httpServletRequest.getParameter("id");
			String hashData = httpServletRequest.getQueryString().split("&")[1].substring(10);
			
			Object[] params = {userID, hashData};
			logger.entering("resetPassUsingURLHash", params);
			
			UserVO userVO = new UserVO();
			userVO.setUserId(userID);
			
			boolean status = userService.valUrlInputForPassReset(hashData,userVO);
			
			if (status) {
				mav = super.success(SuccessMessages.PASSWORD_RESET, 
						MAVPaths.LOG_IN_URL);
			} else {
				logger.error(FaultMessages.GENERIC_ERROR);
				
				mav = loadForgotPassword(httpServletRequest, httpServletResponse);
				mav.addObject("message", FaultMessages.RESET_PASSWORD_HASH_MISMATCH);
				mav.addObject("status", false);
			}
		}
		catch (MISPException exception) {
			logger.error("Error occurred while resetting password", exception);
			mav = super.error(FaultMessages.GENERIC_ERROR);
		}
		catch (Exception exception) {
			logger.error("Error occurred while resetting password", exception);
			mav = super.error(FaultMessages.GENERIC_ERROR);
		}
		return mav;		
	}
}
