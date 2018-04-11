package com.mip.application.dal.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.dao.DataAccessException;

import com.mip.application.constants.FaultMessages;
import com.mip.application.model.UserLeaveDetails;
import com.mip.application.view.UserLeaveVO;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;

/**
 * <p>
 * <code>LeaveManagementManager.java</code> contains all the data access layer methods 
 * pertaining to UserLeaveDetails model.
 * </p>
 * 	
 * <br/>
 * This class extends the <code>DataAccessManager</code> class of 
 * our MISP framework.
 * </p>
 * 
 * @see com.mip.framework.dao.impls.DataAccessManager
 * 
 * @author T H B S
 * 
 */
public class LeaveManagementManager extends DataAccessManager<UserLeaveDetails, Integer>{

	public LeaveManagementManager(){
		super(UserLeaveDetails.class);
	}

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			LeaveManagementManager.class);
	
	/**
	 * Saves a record of type userLeaveDetails in database.
	 * 
	 * @param userLeaveDetails 
	 * 				userLeaveDetails, record to be saved.
	 * @throws DBException
	 */
	public void applyLeave(UserLeaveDetails userLeaveDetails)
	throws DBException {
		
		Object[] params = {userLeaveDetails};
		logger.entering("applyLeave", params);
		try{
			getHibernateTemplate().save(userLeaveDetails);
			
		}catch(DataAccessException e){
			logger.error(FaultMessages.APPLY_LEAVE_EXCEPTION, e);
			throw new DBException(e);
		}
	
		logger.exiting("applyLeave");
	}	
	
	
	/**
	 * Gets a list of Users leave record details.
	 * 
	 * @return List<UserLeaveDetails>
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<UserLeaveDetails> getLeaveRecordsList()throws DBException{
		
		logger.entering("getLeaveRecordsList");
		
		List<UserLeaveDetails> leaveRecordsList = null;		
		try{
			leaveRecordsList=getHibernateTemplate().find(
			"FROM UserLeaveDetails uld ");
			
		}catch(DataAccessException dae){
			logger.error(FaultMessages.LEAVE_LIST_FETCH_EXCEPTION,
					dae);
			throw new DBException(dae);
		}
		
		logger.exiting("getLeaveRecordsList");
		return leaveRecordsList;
	}

	/**
	 * This method queries the database to check if leave is already applied for 
	 * the input date.
	 * 
	 * @param now an instance of Date
	 * @return false if exists and true otherwise.
	 * @throws <code>DBException</code>, if any database error occurs.
	 */
	public boolean checkIfValidLeaveDate(int userId, Date now) throws DBException {
		Object[] params = {userId, now};
		logger.entering("checkIfValidLeaveDate", params);
		
		String date = DateUtil.toDateString(now);
		int rowCount=0;
		try {
			StringBuilder selectSql = new StringBuilder("SELECT COUNT(*) FROM UserLeaveDetails ld ");
			selectSql.append("WHERE ld.user.userId = :userId AND DATE_FORMAT(ld.leaveDate, '%d/%m/%Y') = :date" );
			
			Query query = super.getSession().createQuery(selectSql.toString());
			query.setParameter("userId", userId);
			query.setParameter("date", date);
			
			List dateList = query.list();
			
			rowCount = Integer.valueOf(dateList.get(0).toString());
		}
		catch (DataAccessException exception) {
			logger.error(FaultMessages.LEAVE_DATES_VALIDATION_EXCEPTION, exception);
			throw new DBException(exception);
		}
		catch (Exception exception) {
			logger.error(FaultMessages.LEAVE_DATES_VALIDATION_EXCEPTION, exception);
		}
		
		logger.exiting("checkIfValidLeaveDate", rowCount);
		return rowCount > 0 ? false : true;
	}


	/**
	 * This method returns the list of all leaves for the given date range.
	 * 
	 * @param userLeaveVO
	 * 		<code>UserLeaveVO</code> containing the date range details.
	 * @return
	 * 		<code>ArrayList</code> of type UserLeaveDetails. 
	 * 
	 * @throws <code>DBException</code>
	 * 		If any {@link DataAccessException} occurs.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<UserLeaveDetails> getUserLeaveList(UserLeaveVO userLeaveVO) throws DBException {
		Object[] params = {userLeaveVO};
		logger.entering("getUserLeaveList", params);
		
		String dateFormat = "yyyy-MM-dd";
		ArrayList<UserLeaveDetails> userLeaveList = null;
		
		String fromDate = DateUtil.toDateString(
				DateUtil.toDate(userLeaveVO.getFromDate()),
				dateFormat) + " 00:00:00";
		String toDate = DateUtil.toDateString(
				DateUtil.toDate(userLeaveVO.getToDate()),
				dateFormat) + " 23:59:59";
		
		try {
			StringBuilder selectSql = new StringBuilder("")
				.append("	FROM UserLeaveDetails ul")
				.append("	WHERE ul.leaveDate >= '")
				.append(fromDate)
				.append("'	AND ul.leaveDate <= '")
				.append(toDate)
				.append("'");
		
			userLeaveList = (ArrayList<UserLeaveDetails>) getHibernateTemplate()
					.find(selectSql.toString());
		}
		catch (DataAccessException dae) {
			logger.error(FaultMessages.VIEW_LEAVE_EXCEPTION, dae);
			throw new DBException(dae);
		}
		catch (Exception e) {
			logger.error(FaultMessages.VIEW_LEAVE_EXCEPTION, e);
			throw new DBException(e);
		}
		
		logger.exiting("getUserLeaveList");
		return userLeaveList;
	}
}
