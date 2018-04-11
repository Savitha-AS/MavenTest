package com.mip.framework.security;

import static com.mip.framework.constants.PlatformConstants.AUDIT_LOGGER_APPENDER;

import static com.mip.application.constants.DBObjects.TABLE_USER_DETAILS;
import static com.mip.application.constants.DBObjects.TABLE_USER_HASH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.BadCredentialsException;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.DaoAuthenticationProvider;
import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.userdetails.UserDetails;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.mip.application.view.AdminConfigVO;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * This class extends org.acegisecurity.providers.dao.DaoAuthenticationProvider
 * to implement custom security policies.
 * 
 * @author THBS
 * 
 */
public class CustomDAOAuthenticationProvider extends DaoAuthenticationProvider {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomDAOAuthenticationProvider.class);

	private static MISPLogger auditLogger = LoggerFactory.getInstance()
			.getLogger(AUDIT_LOGGER_APPENDER);

	/**
	 * An instance of <code>DriverManagerDataSource</code>.
	 */
	private DriverManagerDataSource driverManagerDataSource;

	/**
	 * 
	 * @param driverManagerDataSource
	 *            setter method for driverManagerDataSource
	 */
	public void setDriverManagerDataSource(
			DriverManagerDataSource driverManagerDataSource) {
		this.driverManagerDataSource = driverManagerDataSource;
	}

	/**
	 * This method is overridden for implementing the Custom security Policies
	 */
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {

		Object salt = null;

		SaltSource saltSource = getSaltSource();

		if (getSaltSource() != null)
			salt = saltSource.getSalt(userDetails);

		if (authentication.getCredentials() == null) {

			auditLogger.info("Bad Credentials - Invalid User Id/Password  "
					+ "[User ID] : ", userDetails.getUsername());

			throw new BadCredentialsException("Invalid User Id/Password",
					isIncludeDetailsObject() ? ((Object) (userDetails)) : null);

		}

		String presentedPassword = authentication.getCredentials() != null ? authentication
				.getCredentials().toString()
				: "";

		if (!getPasswordEncoder().isPasswordValid(userDetails.getPassword(),
				presentedPassword, salt)) {

			auditLogger.info("Bad Credentials - Invalid Password  "
					+ "[User ID] : ", userDetails.getUsername());
			
			try {
				// Check for the maximum number of Invalid passwords
				this.checkforMaxInvalidAttempts(userDetails.getUsername());

			} catch (Exception e) {

				auditLogger.info("Bad Credentials [Exception occured] - "
						+ "Invalid Password  [User ID] : ", userDetails
						.getUsername());

				throw new BadCredentialsException("Invalid Password",
						isIncludeDetailsObject() ? ((Object) (userDetails))
								: null);
			}

			throw new BadCredentialsException("Invalid Password",
					isIncludeDetailsObject() ? ((Object) (userDetails)) : null);

		} else {

			// Update the attempt count
			this.updateInvalidAttemptCount(userDetails.getUsername());
		}

		return;
	}

	/**
	 * This method checks for Maximum number of wrong passwords given by the
	 * user.
	 * 
	 * @param userId
	 *            , <code>String</code> containing the user ID
	 */
	private void checkforMaxInvalidAttempts(String userId) {
		logger.entering("checkforMaxInvalidAttempts", userId);

		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement updatePstmt = null;
		Connection connection = null;

		StringBuilder selectsql = new StringBuilder(
				"SELECT userHash.attempt_count FROM ");
		selectsql.append(TABLE_USER_HASH).append(" userHash, ");
		selectsql.append(TABLE_USER_DETAILS).append(" userDetails ");
		selectsql.append(" WHERE  ");
		selectsql.append(
				" userDetails.user_id = userHash.user_id AND userDetails.user_uid = ?");

		
		StringBuilder updatesql = new StringBuilder("UPDATE ");
		updatesql.append(TABLE_USER_HASH).append(" userHash, ");
		updatesql.append(TABLE_USER_DETAILS).append(" user ");
		updatesql.append(
				" SET userHash.attempt_count=? ,userHash.is_account_locked =? WHERE ");
		updatesql.append(
				" user.user_id = userHash.user_id AND user.user_uid = ?");

		try {
			AdminConfigVO adminConfigVO = new AdminConfigSrvc().
							getAdminConfigVO(driverManagerDataSource);
			int maxLoginAttempts = adminConfigVO.getMaxLoginAttempts();

			connection = this.driverManagerDataSource.getConnection();

			pstmt = connection.prepareStatement(selectsql.toString());

			pstmt.setString(1, userId);

			rs = pstmt.executeQuery();

			int attemptCount = 0;

			while (rs.next()) {
				attemptCount = rs.getInt(1);
			}

			// increment the wrong attempt count
			updatePstmt = connection.prepareStatement(updatesql.toString());
			updatePstmt.setInt(1, attemptCount + 1);

			auditLogger.debug("Invalid Attempt Count incremented to ",
					(attemptCount + 1), " for User [User ID] : ", userId);

			auditLogger.info("No. of attempts left out for the "
					+ "User [User ID] : ", userId, " is ",
					(maxLoginAttempts - (attemptCount + 1)));

			if (maxLoginAttempts == (attemptCount + 1)) {
				updatePstmt.setInt(2, 1);

				auditLogger.info("Account Locked for User [User ID] : ", 
						userId);
			} else {
				updatePstmt.setInt(2, 0);
			}

			updatePstmt.setString(3, userId);

			updatePstmt.executeUpdate();

		} catch (Exception e) {
			auditLogger.error("Exception occured while updating " +
					"Invalid Attempts count. The network is down. [User ID] : "
					+ userId);
			logger.error("Exception occured while updating " +
					"Invalid Attempts count.",e);
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (updatePstmt != null)
					updatePstmt.close();
				if (rs != null)
					rs.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.exiting("checkforMaxInvalidAttempts");
	}

	/**
	 * This method updates the max wrong password attempt count.
	 * 
	 * @param userId
	 *            , <code>String</code> containing the user id.
	 */
	private void updateInvalidAttemptCount(String userId) {
		logger.entering("updateInvalidAttemptCount", userId);

		PreparedStatement pstmt = null;
		Connection connection = null;
		
		StringBuilder updatesql = new StringBuilder("UPDATE ");
		updatesql.append(TABLE_USER_HASH).append(" userHash, ");
		updatesql.append(TABLE_USER_DETAILS).append(" user ");
		updatesql.append(" SET userHash.attempt_count=? ")
			.append(" WHERE user.user_id = userHash.user_id AND user.user_uid = ?");

		try {

			connection = this.driverManagerDataSource.getConnection();

			pstmt = connection.prepareStatement(updatesql.toString());

			pstmt.setInt(1, 0);
			pstmt.setString(2, userId);

			pstmt.executeUpdate();

		} catch (Exception e) {
			auditLogger.error("Exception occured while updating " +
					"Invalid Attempts count. The network is down. [User ID] : "
					+ userId);
			logger.error("Exception occured while updating " +
					"Invalid Attempts count.",e);
		} finally {
			try {

				if (pstmt != null)
					pstmt.close();
				if (connection != null)
					connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logger.exiting("updateInvalidAttemptCount");
	}
}
