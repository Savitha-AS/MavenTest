package com.mip.application.dal.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;

import com.mip.application.model.BranchDetails;
import com.mip.application.model.RoleAccess;
import com.mip.application.model.UserDetails;
import com.mip.application.model.UserHash;
import com.mip.application.model.UserLeaveDetails;
import com.mip.application.view.DeductionReportVO;
import com.mip.application.view.ReportAgentVO;
import com.mip.application.view.ReportDailyNewVO;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.StringUtil;

/**
 * <p>
 * <code>UserManager.java</code> contains all the data access layer methods 
 * pertaining to UserDetails model.
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
public class UserManager extends DataAccessManager<UserDetails, Integer> {
	
	public UserManager(){
		super(UserDetails.class);
	}

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			UserManager.class);
		
	/**
	 * Saves a record of type UserDetails in database.
	 * 
	 * @param userDetails 
	 * 				UserDetails, record to be saved.
	 * @throws DBException
	 */
	public void addUserDetails(UserDetails userDetails)
		throws DBException {
			
		Object[] params = {userDetails};
		logger.entering("addUserDetails", params);
		
		try{
			getHibernateTemplate().save(userDetails);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while saving User Details.", e);
			throw new DBException(e);
		}

		logger.exiting("addUserDetails");
	}	

	/**
	 * Gets a list of active Users.
	 * 
	 * @return List of Users
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<UserDetails> getUserDetailsList() 
		throws DBException{
		
		logger.entering("getUserDetailsList");
		
		List<UserDetails> userDetailsList = null;		
		try{
			Object[] params = {PlatformConstants.USERS_DASHBOARD,
						PlatformConstants.USERS_USSD};
			userDetailsList =
				getHibernateTemplate().find(
						"FROM UserDetails userDetails " +
						"WHERE userDetails.active = 1 " +
						"AND userDetails.userUid NOT IN (?,?) ",params);			
			
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching list of all active "+
					"User details.",dae);
			throw new DBException(dae);
		}
		
		logger.exiting("getUserDetailsList");
		return userDetailsList;
	}

	/**
	 * Gets User details, based on PK.
	 * 
	 * @param userId User Id, Primary Key of the table.  
	 * @return UserDetails, user record.
	 * @throws DBException
	 */
	public UserDetails getUserDetails(Integer userId) 
		throws DBException{
		
		Object[] params = {userId};
		logger.entering("getUserDetails", params);
		
		UserDetails userDetails = null;		
		try{
			userDetails = super.fetch(userId);
			
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching User details " +
					"based on User Id.",dae);
			throw new DBException(dae);
		}
		
		logger.exiting("getUserDetails", userDetails);
		return userDetails;
	}
	
	/**
	 * Gets the MAX(UserId), for generation of User UId.
	 * 
	 * @return MAX(UserId)
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public Integer getMaxUserIdFromDB() throws DBException {
		
		logger.entering("getMaxUserIdFromDB");
		
		List<Integer> userIdList = null;
		Integer userId = 0;
		try{ 
			userIdList = getHibernateTemplate().find(
					 "SELECT MAX(userDetails.userId) " +
					 "FROM UserDetails userDetails");
			 
			if(userIdList != null && userIdList.size() > 0){
				userId = userIdList.get(0);
			}
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching MAX(User Id).",dae);
			throw new DBException(dae);
		}
		logger.exiting("getMaxUserIdFromDB", userId);
		return userId;
	}	
	
	/**
	 * Update a user record.
	 * 
	 * @param userDetails UserDetails, details to be updated.
	 * @throws DBException
	 */
	public void updateUserDetails(UserDetails userDetails) 
		throws DBException{
			
		Object[] params = {userDetails};
		logger.entering("updateUserDetails", params);
		
		try{
			getHibernateTemplate().update(userDetails);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while updating User Details.", e);
			throw new DBException(e);
		}

		logger.exiting("updateUserDetails");
	}
		
	/**
	 * Search user, based on the First name, Surname, MSISDN, and/or User UId.
	 * 
	 * @param userDetails User details containing the search criteria.
	 * @return A list of user records matching the search criteria. 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<UserDetails> searchUser(UserDetails userDetails, 
			boolean isLoginUseCase) throws DBException{
			
		Object[] params = {//userDetails, 
				isLoginUseCase};
		logger.entering("searchUser",params);
		
		List<UserDetails> userList = null;
		try{			
			if(userDetails != null){
				Criteria searchCriteria = getSession().
									createCriteria(UserDetails.class);

				if(!StringUtil.isEmpty(userDetails.getUserUid())){
					searchCriteria.add(Restrictions.eq("userUid", 
								userDetails.getUserUid()));
				}
				if(!StringUtil.isEmpty(userDetails.getFname())){
					searchCriteria.add(Restrictions.ilike("fname", 
								userDetails.getFname(), MatchMode.ANYWHERE));
				}
				if(!StringUtil.isEmpty(userDetails.getSname())){
					searchCriteria.add(Restrictions.ilike("sname", 
								userDetails.getSname(), MatchMode.ANYWHERE));
				}
				if(!StringUtil.isEmpty(userDetails.getMsisdn())){
					searchCriteria.add(Restrictions.eq("msisdn", 
								userDetails.getMsisdn()));
				}
				
				// Restrict inactive users from not getting picked up in 
				// Search User and Reset Password use cases.
				searchCriteria.add(Restrictions.eq("active", 
						PlatformConstants.STATUS_ACTIVE));
				
				if(!isLoginUseCase){	
					// Allow the following users to login to the platform.
					// However, their user details will not be shown in the  
					// listings [Search User or Reset Password use cases].
					Object[] users = {PlatformConstants.USERS_DASHBOARD,
								PlatformConstants.USERS_USSD};				
					searchCriteria.add(Restrictions.not(
							Restrictions.in("userUid",users)));	
					if(userDetails.getRoleMaster() != null){
						searchCriteria.add(Restrictions.eq("roleMaster", 
								userDetails.getRoleMaster()));
					}
				}
				
				userList = searchCriteria.list();
			}
		}catch(HibernateException e){ 
			logger.error("Exception occured while searching a User " +
					"based on User UID, First name, Surname and/or MSISDN.", e);
			throw new DBException(e);
		}
		logger.exiting("searchUser");
		return userList;
	}
	
	/**
	 * This method queries the database to check if the input MSISDN is already
	 * assigned to a user.
	 * 
	 * @param msisdn input MSISDN.
	 * 
	 * @return true if exists and false otherwise.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public boolean checkIfMSISDNExists(String msisdn, int userID) 
			throws DBException{
		
		logger.entering("checkIfMSISDNExists",msisdn,userID);		
		int rowCount;
		List<UserDetails> userList = null;
		try
		{
			if(userID != 0){
				Object[] params = {msisdn,userID};				
				userList = getHibernateTemplate().find
				("FROM UserDetails ud where ud.msisdn = ? and "
						+"ud.userId <> ? ",params);
			}
			else
				userList = getHibernateTemplate().find
					("FROM UserDetails ud where ud.msisdn = ? ",msisdn);
			
			rowCount = userList.size();
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while validating MSISDN", 
					exception);
			throw new DBException(exception);
		}
		
		logger.exiting("checkIfMSISDNExists");
		
		if (rowCount > 0)
			return true;
		else
			return false;
	}
	

	/**
	 * This method queries the database to check if there are any users 
	 * associated with the branch that needs to be de-activated.
	 * 
	 * @param branchDetails input BranchDetails.
	 * 
	 * @return true if exists and false otherwise.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public int checkBeforeBranchDelete(BranchDetails branchDetails) 
			throws DBException{
		
		logger.entering("checkBeforeBranchDelete",branchDetails);		
		int rowCount;
		List<UserDetails> userList = null;
		try
		{
			Object[] params = {branchDetails};				
			userList = getHibernateTemplate().find(
				"FROM UserDetails ud WHERE ud.branchDetails = ? ",
				params
			);			
			
			rowCount = userList.size();
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while validating Users " +
					"in a particular branch.",exception);
			throw new DBException(exception);
		}
		
		logger.exiting("checkBeforeBranchDelete",rowCount);		
		return rowCount;
	}

	public void changeLoggedInStatus(UserHash userHash) throws DBException {
		Object[] params = {userHash};
		logger.entering("changeLoggedInStatus", params);
		int updateCount=0;
		try {
			getHibernateTemplate().update(userHash);
						
		}
		catch(DataAccessException exception) {
			logger.error("An exception occured while changing the logged in status " +
					"of the User.", exception);
			throw new DBException(exception);
		}

		logger.exiting("changeLoggedInStatus", updateCount);		
	}

	/**
	 * Gets the list of User's leaves
	 * 
	 * @return List of User's leaves
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<UserLeaveDetails> getUserLeaveDetails() throws DBException {
		
		logger.entering("getUserLeaveDetails");		
		List<UserLeaveDetails> userLeaveDetailsList = null;
		try
		{
			userLeaveDetailsList = getHibernateTemplate().find(
				"FROM UserLeaveDetails uld");
			
			/*userLeaveDetailsList = getSession().createSQLQuery(
					new StringBuilder("SELECT * FROM ")
					.append("user_leave_details").toString())
					.setResultTransformer(Transformers.aliasToBean(UserLeaveDetails.class))
				.list();*/
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while fetching leave data " 
					,exception);
			throw new DBException(exception);
		}
		
		logger.exiting("getUserLeaveDetails");		
		return userLeaveDetailsList;
	}
	
	/**
	 * Gets the list of active Users in the platform except "DASHBOARD" user. 
     * This method is specifically used for generating "Agent Report" where the
     * user "USSD" is required.
	 * 
	 * @return List of Users
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<UserDetails> getUserDetailsListWithUSSD(ReportAgentVO agentReportVO,int noOfRoles) 
		throws DBException{
		
		logger.entering("getUserDetailsListWithUSSD");
		
		List<UserDetails> userDetailsList = null;		
		try{
			Object[] params = {PlatformConstants.USERS_DASHBOARD};
			if(Integer.valueOf(agentReportVO.getRole()).equals(0)){
				userDetailsList =
						getHibernateTemplate().find(
								"FROM UserDetails userDetails " +
								"WHERE userDetails.active = 1 " +								
								" AND userDetails.userUid NOT IN (?) ",params);
			}else if(Integer.valueOf(agentReportVO.getRole()).equals(noOfRoles)){
				userDetailsList =
						getHibernateTemplate().find(
								"FROM UserDetails userDetails " +
								"WHERE userDetails.active = 1 " +
								"AND roleMaster.roleId IN (3,4) ");
			}else if(!(Integer.valueOf(agentReportVO.getRole()).equals(0) || Integer.valueOf(agentReportVO.getRole()).equals(noOfRoles))){
				userDetailsList =
						getHibernateTemplate().find(
								"FROM UserDetails userDetails " +
								"WHERE userDetails.active = 1 " +
								"AND roleMaster.roleId = " +
								Integer.valueOf(agentReportVO.getRole())+
								" AND userDetails.userUid NOT IN (?) ",params);
			
			}
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching list of all active "+
					"User details.",dae);
			throw new DBException(dae);
		}
		
		logger.exiting("getUserDetailsListWithUSSD");
		return userDetailsList;
	}
	
	/**
	 * This method queries the database to check if the input User UID is already
	 * existing.
	 * 
	 * @param userUID input Login ID.
	 * 
	 * @return true if exists and false otherwise.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public boolean checkIfUserExists(String userUID) 
			throws DBException{
		
		logger.entering("checkIfUserExists",userUID);		
		int rowCount;
		List<UserDetails> userList = null;
		try
		{
			userList = getHibernateTemplate().find
					("FROM UserDetails ud where ud.userUid = ? ",userUID);
			
			rowCount = userList.size();
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while validating User UID", 
					exception);
			throw new DBException(exception);
		}
		
		logger.exiting("checkIfUserExists",rowCount);
		
		if (rowCount > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * This method queries the database to check if the input User UID has an
	 * email saved in his user details.
	 * 
	 * @param userUID input Login ID.
	 * 
	 * @return true if exists and false otherwise.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public boolean checkEmailIDForUser (String userUID)
			throws DBException {
				
		logger.entering("checkEmailIDForUser", userUID);
		
		List<String> emailIDlist;
		String emailID = "";
		
		try {
			emailIDlist = getHibernateTemplate().find
					("SELECT emailId FROM UserDetails ud" +
							" where ud.userUid = ?", userUID);
			if(emailIDlist != null && emailIDlist.size() > 0){
				emailID = emailIDlist.get(0);
			
				if(null != emailID ){								
					if(emailID.length()>0){
						return true;
					}
				}
			}
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while validating User E-mail ID", 
					exception);
			throw new DBException(exception);
		}
		
		logger.exiting("checkEmailIDForUser",emailIDlist);
		return false;
	}
	
	/**
	 * This method queries the database to get the user ID for the provided
	 * user UID.
	 * 
	 * @param userUID input Login ID.
	 * 
	 * @return String user ID.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public String getUserID (String userUID)
			throws DBException {
				
		logger.entering("getUserID", userUID);
		
		List<Integer> userIDlist;
		String userID = "";
		
		try {
			userIDlist = getHibernateTemplate().find
					("SELECT userId FROM UserDetails ud" +
							" where ud.userUid = ?", userUID);
			if(userIDlist != null && userIDlist.size() > 0){
				userID = userIDlist.get(0).toString();
			}
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while fetching User ID", 
					exception);
			throw new DBException(exception);
		}
		
		logger.exiting("getUserID",userID);
		return userID;	
	}
	
	/**
	 * This method queries the database to get the email ID for the provided user.
	 * 
	 * @param userUID input Login ID.
	 * 
	 * @return String email.
	 * 
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public String getEmailIDForUser (String userID)
			throws DBException {
				
		logger.entering("getEmailIDForUser", userID);
		
		List<String> emailIDlist;
		String emailID = "";
		
		try {
			emailIDlist = getHibernateTemplate().find
					("SELECT emailId FROM UserDetails ud" +
							" where ud.userUid = ?", userID);
			if(emailIDlist != null && emailIDlist.size() > 0){
				emailID = emailIDlist.get(0);
			}
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while fetching User E-mail ID", 
					exception);
			throw new DBException(exception);
		}
		
		logger.exiting("getEmailIDForUser",emailID);
		return emailID;	
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<RoleAccess> getAllRoleNamesList() throws DBException {
		List<RoleAccess> roleNamesList = new ArrayList<RoleAccess>();
		
		try {
		
			String queryForRoleNames = "FROM RoleAccess";
			
			logger.debug(queryForRoleNames);

			roleNamesList = getHibernateTemplate().find(
					queryForRoleNames);			
		}
		catch (DataAccessException exception) {
			logger.error("Exception occured while fetching role access details", exception);
			throw new DBException(exception);
		}
		
		logger.exiting("getRoleNamesListByURL", roleNamesList);
		
		return roleNamesList;
	}

	/**
	 * 
	 * @param agentReportVO
	 * @return
	 * @throws DBException
	 * This method will fetch the data of the agents based on there role and branch which they report to.
	 */
	@SuppressWarnings("unchecked")
	public List<UserDetails> getBranchwiseUserDetails(
			ReportAgentVO agentReportVO) throws DBException {
		
		logger.entering("getBranchwiseUserDetails", agentReportVO);
		
		List<UserDetails> userDetails = null;
		
		try{ 
			userDetails =
					getHibernateTemplate().find(
							"FROM UserDetails userDetails WHERE" +
					        " roleMaster.roleId= " +
							Integer.valueOf(agentReportVO.getRole())+" " +
							" AND userDetails.active = 1" +								
							" AND userDetails.userUid NOT IN (1)"  +
					        " ORDER BY branchDetails.branchId ");
			
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching branch wise user details.",dae);
			throw new DBException(dae);
		}
		logger.exiting("getBranchwiseUserDetails", userDetails);
		return userDetails;
	}

	/**
	 * This method will fetch the distinct branch Id list of the users 
	 * based on there role.
	 * @param role
	 * @return
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getDistinctBranchId(String role) throws DBException {

		logger.entering("getDistinctBranchId", role);
		
		List<Integer> distinctBranchId = null;
		StringBuilder query = new StringBuilder();
		
		try{ 
			query.append("select distinct(branchDetails.branchId) from UserDetails where roleMaster.roleId=")
			     .append(Integer.valueOf(role))
			     .append(" AND is_active = 1")
			     .append(" order by branch_id");
			distinctBranchId = getSession().createQuery(query.toString()).list();
					
			
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching distinct branch ID list.",dae);
			throw new DBException(dae);
		}
		logger.exiting("getDistinctBranchId", distinctBranchId);
		return distinctBranchId;
	}

	
	/**
	 * Gets the list of active Users in the platform except "DASHBOARD" user. 
     * This method is specifically used for generating "Agent Report" where the
     * user "USSD" is required.
	 * 
	 * @return List of Users
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<UserDetails> getActiveUserDetailsListForDeductions(DeductionReportVO dedReportVO,int noOfRoles) 
		throws DBException{
		
		logger.entering("getActiveUserDetailsListForDeductions");
		
		List<UserDetails> userDetailsList = null;		
		try{
			Object[] params = {PlatformConstants.USERS_DASHBOARD};
			if(Integer.valueOf(dedReportVO.getRole()).equals(0)){
				userDetailsList =
						getHibernateTemplate().find(
								"FROM UserDetails userDetails " +
								"WHERE userDetails.active = 1 " +								
								" AND userDetails.userUid NOT IN (?) ",params);
			}else if(Integer.valueOf(dedReportVO.getRole()).equals(noOfRoles)){
				userDetailsList =
						getHibernateTemplate().find(
								"FROM UserDetails userDetails " +
								"WHERE userDetails.active = 1 " +
								"AND roleMaster.roleId IN (3,4) ");
			}else if(!(Integer.valueOf(dedReportVO.getRole()).equals(0) || Integer.valueOf(dedReportVO.getRole()).equals(noOfRoles))){
				userDetailsList =
						getHibernateTemplate().find(
								"FROM UserDetails userDetails " +
								"WHERE userDetails.active = 1 " +
								"AND roleMaster.roleId = " +
								Integer.valueOf(dedReportVO.getRole())+
								" AND userDetails.userUid NOT IN (?) ",params);
			
			}
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching list of all active "+
					"User details.",dae);
			throw new DBException(dae);
		}
		
		logger.exiting("getActiveUserDetailsListForDeductions");
		return userDetailsList;
	}
	
}
