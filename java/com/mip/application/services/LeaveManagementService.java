package com.mip.application.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.mip.application.constants.FaultMessages;
import com.mip.application.dal.managers.LeaveManagementManager;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserLeaveDetails;
import com.mip.application.model.mappings.LeaveMappings;
import com.mip.application.view.UserLeaveVO;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;

/**
 * <p>
 * <code>LeaveManagementService.java</code> contains all the service layer methods 
 * pertaining to Leave Management use case model.
 * </p>
 * 
 * @author T H B S
 */
public class LeaveManagementService {

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			LeaveManagementService.class);

	/**
	 * Set inversion of Control for <code>LeaveManagementManager</code>,
	 */
	private LeaveManagementManager leaveManagementManager;

	public void setLeaveManagementManager(
			LeaveManagementManager leaveManagementManager) {
		this.leaveManagementManager = leaveManagementManager;
	}

	
	/**
	 * Persists the user's leave details into database.
	 * 
	 * @param UserLeaveVO 
	 * 				UserLeaveVO containing the data to be added into database.
	 * @param UserDetails 
	 * 				contains user details  of the logged in user like userId,UserName.
	 * @throws MISPException	
	 * 
	 *  @return boolean
	 */
	public boolean applyLeaveForUsers(UserLeaveVO userLeaveVO,
			UserDetails userDetails) throws MISPException{
		
		Object[] params = { userLeaveVO, userDetails };
		logger.entering("applyLeaveForUsers", params);
		//sets the from date of leave
		Calendar start = Calendar.getInstance();
		start.setTime(DateUtil.toDate(userLeaveVO.getFromDate()));
		//sets the to date of leave
		Calendar end = Calendar.getInstance();
		end.setTime(DateUtil.toDate(userLeaveVO.getToDate()));
		Date now;
		boolean isApplied = false;

		// applying leave for a day and more than a day
		while (start.before(end) || start.equals(end)) {
			now = start.getTime();
			userLeaveVO.setLeaveDate(now);
			try {
				// Added : 21 Mar 2012
				// WeekEnd check added to avoid insertion of weekend days in DB. 
				if(!DateUtil.checkForWeekEnd(now)) {
					//maps userLeaveVO to userLeaveDetails 
					UserLeaveDetails userLeaveDetails = LeaveMappings
							.mapUserLeaveVOToUserLeaveDetails(userLeaveVO,
									userDetails);
					leaveManagementManager.applyLeave(userLeaveDetails);
					isApplied = true;
				}
			} catch(DBException exception){
				logger.error("An exception occured while inserting leave record in DB.",
						exception);
				throw new MISPException(exception);
			}
			catch(MISPException exception){
				logger.error("An exception occured while mapping VO to Model.",
						exception);
				throw new MISPException(exception);
			}
			start.add(Calendar.DAY_OF_MONTH, 1);
		}

		logger.entering("applyLeaveForUsers", params);
		return isApplied;
	}
	
	/**
	 * Invoked as a DWR call, this method checks if leave already in the database 
	 * applied for the input dates.
	 * 
	 * @param userId	<code>Integer</code> holding the user id.
	 * @param fromDate	<code>String</code> holding the fromDate.
	 * @param toDate	<code>String</code> holding the toDate.
	 * 
	 * @return Comma separated value of invalid leave dates
	 */
	public String checkForValidLeaveDates(int userId, String fromDate, String toDate) {
		Object[] params = {userId, fromDate, toDate};
		logger.entering("checkForValidLeaveDates", params);

		Date now = null;
		Calendar start = Calendar.getInstance();
		start.setTime(DateUtil.toDate(fromDate));
		Calendar end = Calendar.getInstance();
		end.setTime(DateUtil.toDate(toDate));
		
		boolean isValidDate = false;
		StringBuilder invalidDates = new StringBuilder();
		try {
			for(; !start.after(end); start.add(Calendar.DATE, 1)) {
				now = start.getTime();
				isValidDate = leaveManagementManager.checkIfValidLeaveDate(userId, now);
				if(!isValidDate) {
					invalidDates.append(DateUtil.toDateString(now));
					invalidDates.append(", ");
				}
			}
			// Removing comma from the end.
			if(invalidDates.lastIndexOf(", ") != -1)
				invalidDates = invalidDates.deleteCharAt(invalidDates.lastIndexOf(", "));
		} catch (DBException dbe) {
			logger.error("An error occured while " +
					"validating leave date", dbe);
		}
		catch (Exception e) {
			logger.error("An error occured while " +
					"validating leave date", e);
		}
		logger.exiting("checkForValidLeaveDates");
		return invalidDates.toString();
	}


	/**
	 * This method returns the list of user leaves for the given date range.
	 * 
	 * @param userLeaveVO
	 * 		<code>UserLeaveVO</code> containing the date range details.
	 * 
	 * @return
	 * 		<code>List</code> of user leaves details. 
	 * @throws 
	 * 		MISPException If any Exception occurs
	 */
	public List<UserLeaveDetails> getUserLeaveList(UserLeaveVO userLeaveVO) throws MISPException {
		Object[] params = {userLeaveVO};
		logger.entering("getUserLeaveList", params);
		
		List<UserLeaveDetails> userLeaveList = null;
		
		try {
			userLeaveList = leaveManagementManager.getUserLeaveList(userLeaveVO);
		}
		catch(DBException dbe) {
			logger.error(FaultMessages.VIEW_LEAVE_EXCEPTION, dbe);
			throw new MISPException(dbe);
		}
		catch(Exception e) {
			logger.error(FaultMessages.VIEW_LEAVE_EXCEPTION, e);
			throw new MISPException(e);
		}
		
		logger.exiting("getUserLeaveList");
		return userLeaveList;
	}
}
