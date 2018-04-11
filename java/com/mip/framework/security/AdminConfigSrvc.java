package com.mip.framework.security;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.mip.application.dal.managers.AdminConfigManager;
import com.mip.application.services.LoginService;
import com.mip.application.view.AdminConfigVO;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

public class AdminConfigSrvc {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			AdminConfigSrvc.class);

	public AdminConfigVO getAdminConfigVO(DataSource dataSource){

		AdminConfigVO adminConfigVO = null;
		try {
			
			/**
			 * When coming from Login use-case, the Spring framework would
			 * not have instantiated the <code>adminConfigManager</code>
			 * bean.
			 */
			JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	
			AdminConfigManager adminConfigManager = new AdminConfigManager();
			adminConfigManager.setJdbcTemplate(jdbcTemplate);
	
			LoginService loginService = new LoginService();
			loginService.setAdminConfigManager(adminConfigManager);
	
			adminConfigVO = loginService.getConfigDetails();	
		
		} catch (MISPException e) {
			logger.error("Exception occured while "
					+ "retrieving the Admin Config details.", e);
		}
		return adminConfigVO;
	}
}
