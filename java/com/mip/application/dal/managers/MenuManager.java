package com.mip.application.dal.managers;

import static com.mip.application.constants.DBObjects.TABLE_MENU;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;

import com.mip.application.model.Menu;
import com.mip.application.model.RoleMaster;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>MenuManager.java</code> contains all the data access layer methods 
 * pertaining to Menu model.
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
public class MenuManager extends DataAccessManager<Menu, Integer> {

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			MenuManager.class);

	public MenuManager() {
		super(Menu.class);		
	}
	
	/**
	 * Fetches menu details from the database based on a Role
	 * 
	 * @param roleMaster 
	 * 			RoleMaster based on which, menu details are fetched.
	 * 
	 * @return List of menu items.
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> fetchMenuDetails(RoleMaster roleMaster) 
		throws DBException{
		
		Object[] params = {roleMaster};
		logger.entering("fetchMenuDetails", params);
		
		List<Menu> menuList = null;
		try{			
			StringBuilder queryString = new StringBuilder("");
			queryString.append("SELECT menu ");
			queryString.append("FROM MenuRoleMapping AS mrMapping  ");
			queryString.append("LEFT OUTER JOIN mrMapping.menu AS menu ");
			queryString.append("LEFT OUTER JOIN mrMapping.roleMaster AS role ");
			queryString.append("WHERE role.roleId = ? AND menu.active = ?");		
			queryString.append("ORDER BY menu.menuParentId, menu.menuDisplayOrder ");		
			
			Object[] queryParams = {roleMaster.getRoleId(), 
					PlatformConstants.STATUS_ACTIVE};
			menuList = this.getHibernateTemplate().
						find(queryString.toString(), queryParams);
			
		}catch(DataAccessException e){
			logger.error("Exception occured while retrieving Menu Details.", e);
			throw new DBException(e);
		}
		
		logger.exiting("fetchMenuDetails", menuList);
		return menuList;
	}
	
	
	/**
	 * Fetches menu details from the database based on a Role
	 * 
	 * @param roleMaster 
	 * 			RoleMaster based on which, menu details are fetched.
	 * 
	 * @return List of menu items.
	 * @throws DBException
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> fetchAllMenuDetails() 
		throws DBException{
		
		logger.entering("fetchAllMenuDetails");
		
		List<Menu> menuList = null;
		try{			
			StringBuilder queryString = new StringBuilder("");
			queryString.append("SELECT m ");
			queryString.append("FROM menu  m ");
			queryString.append("WHERE m.active = ? ");		
			queryString.append("ORDER BY m.menuParentId, m.menuDisplayOrder ");		
			
			
			StringBuilder sqlQuery = new StringBuilder("SELECT m.menu_id AS menuId, ")
			.append("m.menu_parent_id AS menuParentId, m.menu_name AS menuName, m.menu_description AS menuDescription,")
			.append(" m.menu_url AS menuUrl, m.menu_tooltip AS menuTooltip, m.menu_display_order AS menuDisplayOrder, m.is_active AS active")
			.append(" FROM ").append(TABLE_MENU).append(" m")
			.append(" WHERE  m.is_active=").append(PlatformConstants.STATUS_ACTIVE)
			.append(" ORDER BY m.menu_parent_id, m.menu_display_order");
			
			menuList = getSession().createSQLQuery(sqlQuery.toString())
					.addScalar("menuId", Hibernate.LONG)
					.addScalar("menuParentId", Hibernate.LONG)
					.addScalar("menuName", Hibernate.STRING)
					.addScalar("menuDescription", Hibernate.STRING)
					.addScalar("menuUrl", Hibernate.STRING)
					.addScalar("menuTooltip", Hibernate.STRING)
					.addScalar("menuDisplayOrder", Hibernate.INTEGER)
					.addScalar("active", Hibernate.BYTE)
					.setResultTransformer(
							Transformers.aliasToBean(Menu.class)).list();
			
			
		}catch(DataAccessException e){
			logger.error("Exception occured while retrieving Menu Details.", e);
			throw new DBException(e);
		}
		
		logger.exiting("fetchAllMenuDetails", menuList);
		return menuList;
	}
	
}
