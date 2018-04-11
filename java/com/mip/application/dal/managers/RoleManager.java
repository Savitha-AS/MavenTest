package com.mip.application.dal.managers;

import static com.mip.application.constants.DBObjects.TABLE_MENU;
import static com.mip.application.constants.DBObjects.TABLE_MENU_ROLE_MAPPING;
import static com.mip.application.constants.DBObjects.TABLE_ROLE_MASTER;


import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.dao.DataAccessException;

import com.mip.application.model.RoleMaster;
import com.mip.application.view.RoleVO;
import com.mip.framework.dao.impls.DataAccessManager;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;

/**
 * <p>
 * <code>RoleManager.java</code> contains all the data access layer methods 
 * pertaining to RoleMaster model.
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
public class RoleManager extends DataAccessManager<RoleMaster, Integer> {

	





	public RoleManager() {
		super(RoleMaster.class);
	}

	/**
	 * An instance of logger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			RoleManager.class);
	

	/**
	 * Gets a list of active Users.
	 * 
	 * @return List of Users
	 * @throws DBException
	 */
	public List<RoleMaster> getRolesList() 
		throws DBException{
		
		logger.entering("getRolesList");
		
		List<RoleMaster> roleMasterList = null;		
		try{
			roleMasterList = super.fetchAll();
			
		}catch(DataAccessException dae){
			logger.error("Exception occured while fetching list of all roles.",
					dae);
			throw new DBException(dae);
		}
		
		logger.exiting("getRolesList");
		return roleMasterList;
	} 
	
	/**
	 * This method queries the database to check if the input role name already
	 * exists.
	 * 
	 * @param roleName input MSISDN.
	 * 
	 * @return true if exists and false otherwise.
	 * 
	 * @throws DBException in case of any DataAccessException.
	 */
	@SuppressWarnings("unchecked")
	public boolean checkIfRoleAlreadyExists(String roleName, int roleId) 
			throws DBException{
		
		logger.entering("checkIfRoleAlreadyExists",roleName);		
		int rowCount;
		List<RoleMaster> roleList = null;
		try
		{
			
			if(roleId != 0){
				Object[] params = {roleName,roleId};				
				roleList = getHibernateTemplate().find
				("FROM RoleMaster rm where rm.roleName = ? and "
						+"rm.roleId <> ? ",params);
			}
			
			roleList = getHibernateTemplate().find
					("FROM RoleMaster rm where rm.roleName = ? ", roleName);
			
			rowCount = roleList.size();
		}
		catch (DataAccessException exception) {	
			logger.error("Exception occured while validating Role", 
					exception);
			throw new DBException(exception);
		}
		
		logger.exiting("checkIfRoleAlreadyExists");
		
		if (rowCount > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * The method to fetch all the menu details.
	 * 
	 * @param roleId an instance of int representing role id.
	 * 
	 * @return an instance of <code>RoleVO</code> representing roleVO.
	 */
	public RoleVO retrieveAllMenuDetails(){
		
		logger.entering("retrieveAllMenuDetails");
		
		RoleVO roleVO = new RoleVO();
		
		SQLQuery fetchMenusSqlQuery = null;
		
		StringBuilder fetchParentMenuIdsForRoleId = new StringBuilder("SELECT ")
		.append("rm.is_ip_reg as 'isIPReg', ")
		.append("group_concat(DISTINCT(IF(menu_parent_id=0, m.menu_id, ''))) as 'parentMenuString', ")
		.append("group_concat(DISTINCT(IF(menu_parent_id<>0, m.menu_id, ''))) as 'childMenuString' ")
		.append("FROM ").append(TABLE_MENU_ROLE_MAPPING).append(" mrm, ")
		.append(TABLE_ROLE_MASTER).append(" rm, ")
		.append(TABLE_MENU).append(" m ")
		.append("WHERE mrm.menu_id=m.menu_id ")
		.append("AND mrm.role_id=")
		.append(1);
		
		fetchMenusSqlQuery = getSession().createSQLQuery(
				fetchParentMenuIdsForRoleId.toString());
		
		@SuppressWarnings("unchecked")
		List<RoleVO> menuIdsList = fetchMenusSqlQuery
				.addScalar("parentMenuString",Hibernate.STRING)
				.addScalar("childMenuString",Hibernate.STRING)
				.addScalar("isIPReg",Hibernate.BYTE)
				.setResultTransformer(
				Transformers.aliasToBean(RoleVO.class))
				.list();
		
		roleVO = menuIdsList.get(0);
		
		logger.exiting("retrieveAllMenuDetails", roleVO);
		
		return roleVO;
	}
	
	
	/**
	 * The method to fetch the menu details for given role.
	 * 
	 * @param roleId an instance of int representing role id.
	 * 
	 * @return an instance of <code>RoleVO</code> representing roleVO.
	 */
public RoleVO retrieveMenuDetailsByRoleId(int roleId){
		
		Object[] params = {roleId};
		logger.entering("retrieveMenuDetailsByRoleId", params);
		
		RoleVO roleVO = new RoleVO();
		
		SQLQuery fetchMenusSqlQuery = null;
		
		StringBuilder fetchParentMenuIdsForRoleId = new StringBuilder("SELECT ")
		.append("rm.is_ip_reg as 'isIPReg', ")
        .append("group_concat(DISTINCT(IF(menu_parent_id=0, ")
        .append("m.menu_id, ''))) as 'parentMenuString', ")
        .append("group_concat(DISTINCT(IF(menu_parent_id<>0, m.menu_id,''))) as 'childMenuString' ")
        .append("FROM ")
        .append(TABLE_ROLE_MASTER).append(" rm, ")
        .append(TABLE_MENU_ROLE_MAPPING).append(" mrm, ") 
        .append(TABLE_MENU).append(" m ")
        .append("WHERE ")
        .append("mrm.menu_id = m.menu_id ")
        .append("AND rm.role_id = mrm.role_id ")
        .append("AND mrm.role_id = ")
		.append(roleId);
		
		fetchMenusSqlQuery = getSession().createSQLQuery(
				fetchParentMenuIdsForRoleId.toString());
		
		@SuppressWarnings("unchecked")
		List<RoleVO> menuIdsList = fetchMenusSqlQuery
				.addScalar("parentMenuString",Hibernate.STRING)
				.addScalar("childMenuString",Hibernate.STRING)
				.addScalar("isIPReg",Hibernate.BYTE)
				.setResultTransformer(
				Transformers.aliasToBean(RoleVO.class))
				.list();
		
		roleVO = menuIdsList.get(0);
		
		logger.exiting("retrieveMenuDetailsByRoleId", roleVO);
		
		return roleVO;
	}
	
	/**
	 * The method to fetch child menu details for the given parent menu id.
	 * 
	 * @param parentMenuId an instance of type int representing parentMenuId.
	 */
	public RoleVO fetchChildMenuDetailsByParentId(int parentMenuId){
		
		Object[] params = {parentMenuId};
		logger.entering("fetchChildMenuDetailsByParentId", params);
		
		RoleVO roleVO = new RoleVO();
		
		SQLQuery fetchMenusSqlQuery = null;
		
		StringBuilder fetchParentMenuIdsForRoleId = new StringBuilder("SELECT ")
		.append("group_concat(DISTINCT(IF(menu_parent_id<>0, m.menu_id, ''))) as 'childMenuString' ")
		.append("FROM ")
		.append(TABLE_MENU).append(" m ")
		.append("WHERE m.is_active=1 ")
		.append("AND m.menu_parent_id=")
		.append(parentMenuId);
		
		fetchMenusSqlQuery = getSession().createSQLQuery(
				fetchParentMenuIdsForRoleId.toString());
		
		@SuppressWarnings("unchecked")
		List<RoleVO> menuIdsList = fetchMenusSqlQuery
				.addScalar("childMenuString",Hibernate.STRING)
				.setResultTransformer(
				Transformers.aliasToBean(RoleVO.class))
				.list();
		
		roleVO = menuIdsList.get(0);
		
		logger.exiting("fetchChildMenuDetailsByParentId", roleVO);
		
		return roleVO;
	}
	
	/**
	 * The method to insert role menu and access details for a particular role.
	 * 
	 * @param roleVO an instance of <code>RoleVO</code> representing roleVO.
	 */
	public void insertRoleMenuAccessDetails(RoleVO roleVO){
		
		Object[] params = {roleVO};
		logger.entering("insertRoleMenuAccessDetails", params);
		
		String menuString = roleVO.getParentMenuString().
								concat(roleVO.getChildMenuString());
		
		Query query = super
				.getSession()
				.createSQLQuery(
					"CALL ADD_ROLE_MENU_ACCESS_DETAILS(:roleName, :roleDescription, :menuString, :isIPReg)")
					.setParameter("roleName", roleVO.getRoleName())
					.setParameter("roleDescription", roleVO.getRoleDescription())
					.setParameter("menuString", menuString)
					
					.setParameter("isIPReg", roleVO.getIsIPReg());
		
		query.executeUpdate();
		
		logger.exiting("insertRoleMenuAccessDetails");
		
	}
	
	/**
	 * The method to update role menu and access details for a particular role.
	 * 
	 * @param roleVO an instance of <code>RoleVO</code> representing roleVO.
	 */
	public void updateRoleMenuAccessDetails(RoleVO roleVO){
		
		Object[] params = {roleVO};
		logger.entering("updateRoleMenuAccessDetails", params);
		
		String menuString = roleVO.getParentMenuString().
								concat(roleVO.getChildMenuString());
		
		Query query = super
				.getSession()
				.createSQLQuery(
					"CALL MODIFY_ROLE_MENU_ACCESS_DETAILS(:roleId, :menuString, :isIPReg)")
					.setParameter("roleId", roleVO.getRoleId())
					.setParameter("menuString", menuString)
					.setParameter("isIPReg", roleVO.getIsIPReg());
		
		
		query.executeUpdate();
		
		logger.exiting("updateRoleMenuAccessDetails");
		
	}
	
	
	
}
