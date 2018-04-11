package com.mip.framework.handlers;

import static com.mip.application.constants.DBObjects.TABLE_USER_DETAILS;
import static com.mip.application.constants.DBObjects.TABLE_USER_HASH;
import static com.mip.framework.constants.PlatformConstants.AUDIT_LOGGER_APPENDER;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.support.ServletContextResource;

import com.mip.application.model.UserDetails;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

public class SessionHandler implements HttpSessionListener{

	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			SessionHandler.class);
	
	private static MISPLogger auditLogger = LoggerFactory.getInstance().
					getLogger(AUDIT_LOGGER_APPENDER);
	
	private static final String PLATFORM_DAO_FILE = "WEB-INF\\platform-dao.xml";
	private static final String DATASOURCE_BEAN = "dataSource";
	
	private Connection connection;
	private PreparedStatement ps;

	public void sessionCreated(HttpSessionEvent event) {
		// TODO Auto-generated method stub		
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		logger.entering("sessionDestroyed");
		
		UserDetails userDetails = (UserDetails) event.
					getSession().getAttribute("userDetails");
		
		String query = 
				"UPDATE " + TABLE_USER_HASH + " " +
				"SET is_logged_in=? " +
				"WHERE user_id=(" +
				"	SELECT ud.user_id " +
				"	FROM "+ TABLE_USER_DETAILS + " ud " +
				"	WHERE ud.user_uid=?" +
				")";
		int updateCount = 0;
		if(null != userDetails){
			try {
				BeanFactory daoBeanFactory = new XmlBeanFactory(new ServletContextResource(event.getSession().getServletContext(), PLATFORM_DAO_FILE));
				DriverManagerDataSource dataSource = (DriverManagerDataSource)daoBeanFactory.getBean(DATASOURCE_BEAN);
				logger.info("dataSource.getUrl()---> ", dataSource.getUrl());
				logger.info("dataSource.getUsername()---> ", dataSource.getUsername());
				logger.info("dataSource.getPassword()---> ", dataSource.getPassword());
				
				connection = (Connection) dataSource.getConnection();
				ps = (PreparedStatement) connection.prepareStatement(query);
				ps.setByte(1, (byte)0);
				ps.setString(2, userDetails.getUserUid());
				updateCount = ps.executeUpdate();
				
			} catch(Exception exception) {
				logger.error("An exception occured while updating loggedIn status.",
						exception);	
			}
			auditLogger.info("Successful User Logout [User ID] : ", 
					userDetails.getUserUid());
		}		
		logger.exiting("sessionDestroyed", updateCount);
	}
}
