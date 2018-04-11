package com.mip.framework.security;

import static com.mip.framework.constants.PlatformConstants.AUDIT_LOGGER_APPENDER;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.acegisecurity.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.dao.DataAccessException;

import com.mip.application.view.AdminConfigVO;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * 
 * This class is the Custom implementation for JdbcDaoImpl
 * 
 */
public class CustomMISPJDBCImpl extends JdbcDaoImpl {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			CustomMISPJDBCImpl.class);

	private static MISPLogger auditLogger = LoggerFactory.getInstance()
			.getLogger(AUDIT_LOGGER_APPENDER);

	protected class CustomUsersByUsernameMapping extends
			JdbcDaoImpl.UsersByUsernameMapping {
		/**
		 * @param rs
		 *            , ResultSet containing the user details This method
		 *            overrides the superclass method's default behavior of
		 *            reading boolean from the Resultset.
		 */
		protected Object mapRow(ResultSet rs, int rownum) throws SQLException {
			CustomMISPJDBCImpl.logger.entering("mapRow");

			String username = rs.getString(1);
			String password = rs.getString(2);
			int enabled = rs.getInt(3);
			int accountLocked = rs.getInt(4);
			int attemptCount = rs.getInt(5);

			boolean isEnabled = false;
			boolean isNotLocked = true;

			if (enabled == 1)
				isEnabled = true;

			AdminConfigVO adminConfigVO = new AdminConfigSrvc().
									getAdminConfigVO(getDataSource());
			
			if (accountLocked == 1
					|| attemptCount == adminConfigVO.getMaxLoginAttempts()) {
				isNotLocked = false;
				
				auditLogger.info("Account Locked for User [User ID] : ", 
					username, ". User can not login until password is reset.");
			}

			CustomMISPUser user = new CustomMISPUser(
					username,
					password,
					isEnabled,
					true,
					true,
					isNotLocked,
					new GrantedAuthority[] { new GrantedAuthorityImpl("HOLDER") });

			CustomMISPJDBCImpl.logger.exiting("mapRow");
			return user;
		}

		protected CustomUsersByUsernameMapping(DataSource ds) {
			// call the super class constructor
			super(ds);
		}
	}

	protected class CustomAuthoritiesByUsernameMapping extends
			JdbcDaoImpl.AuthoritiesByUsernameMapping {
		protected CustomAuthoritiesByUsernameMapping(DataSource ds) {
			// call the super class constructor
			super(ds);

		}
	}

	protected void initMappingSqlQueries() {
		usersByUsernameMapping = new CustomUsersByUsernameMapping(
				getDataSource());
		authoritiesByUsernameMapping = new CustomAuthoritiesByUsernameMapping(
				getDataSource());
	}

	/**
	 * @ overridden
	 */
	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

		List<UserDetails> users = null;
		try {
			users = usersByUsernameMapping.execute(username);

		} catch (DataAccessException e) {
			auditLogger.error("Exception occured while executing "
					+ "usersByUsernameMapping query. The network is down. " +
							"[User ID] : " + username);
			logger.error("Exception occured while executing "
					+ "usersByUsernameMapping query.", e);
			throw e;
		}
		if (users.size() == 0) {
			auditLogger.error("User not found [User ID] : " + username);
			throw new UsernameNotFoundException("User not found");
		}

		CustomMISPUser user = (CustomMISPUser) users.get(0);

		List dbAuths = null;
		try {
			dbAuths = authoritiesByUsernameMapping.execute(user.getUsername());
			addCustomAuthorities(user.getUsername(), dbAuths);

		} catch (DataAccessException e) {
			auditLogger.error("Exception occured while executing "
					+ "authoritiesByUsernameMapping query. The network is down. " +
							"[User ID] : "+ username);
			logger.error("Exception occured while executing "
					+ "authoritiesByUsernameMapping query.", e);
			throw e;
		}

		if (dbAuths.size() == 0) {
			auditLogger.error("User has no GrantedAuthority [User ID] : "
					+ username);
			throw new UsernameNotFoundException("User has no GrantedAuthority");
		}

		GrantedAuthority arrayAuths[] = (GrantedAuthority[]) (GrantedAuthority[]) dbAuths
				.toArray(new GrantedAuthority[dbAuths.size()]);

		String returnUsername = user.getUsername();

		if (!isUsernameBasedPrimaryKey())
			returnUsername = username;

		return new CustomMISPUser(returnUsername, user.getPassword(), user
				.isEnabled(), true, true, user.isAccountNonLocked(), arrayAuths);
	}
}
