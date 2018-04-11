package com.mip.application.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.SessionKeys;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserLeaveDetails;
import com.mip.application.services.LeaveManagementService;
import com.mip.application.services.UserService;
import com.mip.application.view.UserLeaveVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.StringUtil;


/**
 * <p>
 * <code>LeaveManagementController.java</code> contains methods pertaining to Leave
 * Management use case model. Leave Management use case model includes following
 * use cases : 
 * <table border="1">
 * 	<tr><th width="5%">Sl. No.</th><th>Use Case</th><th>Dispatcher action method signature</th></tr>
 * 	<tr><td>1.</td><td>Apply Leave</td><td>{@link #applyLeaveForUsers(HttpServletRequest,HttpServletResponse,UserLeaveVO)}</td></tr>
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
public class LeaveManagementController extends BasePlatformController {
	
	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			LeaveManagementController.class);

	/**
	 * Set inversion of Control for <code>UserService</code>
	 */
	private UserService userService;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Set inversion of Control for <code>LeaveManagementService</code>
	 */
	private LeaveManagementService leaveManagementService;
	
	public void setLeaveManagementService(
			LeaveManagementService leaveManagementService) {
		this.leaveManagementService = leaveManagementService;
	}
	
	/**
	 * This method handles the operation of applying leave  record 
	 * into database. 
	 * 
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @param UserLeaveVO
	 *            UserLeaveVO command object
	 * 
	 * @return ModelAndView,a view object
	 */
	public ModelAndView applyLeaveForUsers(HttpServletRequest request,
			HttpServletResponse response,UserLeaveVO userLeaveVO ) {

		Object[] params = {userLeaveVO  };
		logger.entering("applyLeaveForUsers", params);
		ModelAndView mav = null;

		HttpSession session = request.getSession();
		UserDetails userDetails = (UserDetails) session
				.getAttribute(SessionKeys.SESSION_USER_DETAILS);
		boolean isLeaveApplied = false;
		try {
			//calls applyLeaveForUsers with userLeaveVO and userDetails objects ,
			  //userDetails contains who's logged in to the system
			isLeaveApplied = leaveManagementService.applyLeaveForUsers(userLeaveVO,
					userDetails);
			if (isLeaveApplied)
				mav = super.success(SuccessMessages.LEAVE_APPLIED); 
					

		} catch (MISPException exception) {
			logger.error("An exception occured while adding a new  "
					+ "leave record.", exception);
			mav = super.error("", MAVPaths.JSP_GLOBAL_ERROR);
		} catch (Exception exception) {
			logger.error("An exception occured while adding a new  "
					+ "leave record.", exception);
			mav = super.error("", MAVPaths.JSP_GLOBAL_ERROR);
		}

		logger.exiting("applyLeaveForUsers");
		return mav;
	}
	
	
	
	/**
	 * Gets all theList of  Users to populate the UserName field in the applyLeave page. 
	 * 
	 * @param request
	 *            an instance of HTTPServletRequest
	 * 
	 * @param response
	 *            an instance of HTTPServletRequest
	 * 
	 * @return 
	 */
	public ModelAndView loadUsers(HttpServletRequest request,
			HttpServletResponse response) {
		logger.entering("applyLeave");

		ModelAndView mav = null;
		List<UserDetails> userList = null;
		try {
			userList = userService.listUserDetails();
			mav = new ModelAndView(MAVPaths.VIEW_USER_AGENT_APPLYLEAVE);
			mav.addObject(MAVObjects.LIST_USER, userList);
			
		} 
		catch (MISPException exception) {
			logger.error("An exception occured while applying leave " 
					, exception);			
			mav = super.error("", MAVPaths.JSP_GLOBAL_ERROR);
		}
		catch (Exception exception){			
			logger.error("An exception occured while fetching a particular " +
					"user details record.", exception);			
			mav = super.error("", MAVPaths.JSP_GLOBAL_ERROR);
		}	

		logger.exiting("applyLeave");
		return mav;
	}

	/**
	 * This method controls the launching of View Leaves page.
	 * 
	 * @param httpServletRequest
	 * 		an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 * 		an instance of HTTPServletRequest
	 * 
	 * @return 
	 * 		ModelAndView, a view object
	 */
	public ModelAndView viewLeaves(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		logger.entering("viewLeaves");

		ModelAndView mav = null;
		UserLeaveVO userLeaveVO = null;
		List<UserLeaveDetails> userLeaveList = null;
		try {
			
			String fromDate = null;
			String toDate = null;
			
			String leaveRangeType = httpServletRequest.getParameter("leaveRange");
			// Handles the initial page load
			if(StringUtil.isEmpty(leaveRangeType))
				leaveRangeType = "1";
			
			if(leaveRangeType.equals("1")) {
				// Today
				fromDate = DateUtil.toDateString(new Date());
				toDate = DateUtil.toDateString(new Date());
			} else if(leaveRangeType.equals("2")) {
				// Current Week
				fromDate = DateUtil.toDateString(DateUtil.getFirstDayOfTheWeek(new Date()));
				toDate = DateUtil.toDateString(DateUtil.getLastDayOfTheWeek(DateUtil.toDate(fromDate)));
			} else if(leaveRangeType.equals("3")) {
				// Current Month
				fromDate = DateUtil.toDateString(DateUtil.getFirstDayOfTheMonth(new Date()));
				toDate = DateUtil.toDateString(DateUtil.getLastDayOfTheMonth(new Date()));
			} else {
				// Date Range
				fromDate = String.valueOf(httpServletRequest.getParameter("fromDate"));
				toDate = String.valueOf(httpServletRequest.getParameter("toDate"));
			}
			
			userLeaveVO = new UserLeaveVO();
			userLeaveVO.setFromDate(fromDate);
			userLeaveVO.setToDate(toDate);
			
			userLeaveList = leaveManagementService.getUserLeaveList(userLeaveVO);
			logger.debug("User Leave List : ", userLeaveList);
			
			mav = new ModelAndView(MAVPaths.VIEW_USER_AGENT_VIEWLEAVES);
			mav.addObject(MAVObjects.LIST_USER_LEAVES, userLeaveList);
			mav.addObject("leaveRangeType", leaveRangeType);
			mav.addObject(MAVObjects.VO_USER_LEAVE, userLeaveVO);
		} 
		catch (MISPException mispException) {
			logger.error(FaultMessages.VIEW_LEAVE_PAGE_LOADING_FAILED 
					, mispException);			
			mav = super.error(FaultMessages.VIEW_LEAVE_PAGE_LOADING_FAILED,
					MAVPaths.URL_VIEW_LEAVE);
		}
		catch (Exception exception){			
			logger.error(FaultMessages.GENERIC_ERROR
					, exception);			
			mav = super.error(FaultMessages.GENERIC_ERROR);
		}	

		logger.exiting("viewLeaves", mav);
		return mav;
	}
}
