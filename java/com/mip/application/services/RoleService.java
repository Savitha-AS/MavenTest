package com.mip.application.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.mip.application.dal.managers.RoleManager;
import com.mip.application.model.RoleMaster;
import com.mip.application.view.RoleVO;
import com.mip.framework.exceptions.DBException;
import com.mip.framework.exceptions.MISPException;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;


/**
 * <p>
 * <code>RoleService.java</code> contains all the service layer methods 
 * pertaining to Role Management use case model.
 * </p>
 * 
 * @author T H B S
 */
public class RoleService {

	/**
	 * Instance of MISPLogger.
	 */
	private static MISPLogger logger = LoggerFactory.getInstance().getLogger(
			RoleService.class);
		
	/**
	 * Instance of <code>RoleManager</code>.
	 */
	private RoleManager roleManager;
	
	/**
	 * Sets the RoleManager instance.
	 * 
	 * @param roleManager an instance of roleManager to set. 
	 */
	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}
		
	/**
	 * Method to check whether a role exists in DB.
	 * 
	 * @param roleName an instance of <code>String</code> 
	 * 	 	  representing role name.
	 * 
	 * @param roleId an instance of type int 
	 * 		  representing role id.
	 * 
	 * @return an instance of type boolean representing isRoleExist.
	 */
	public boolean checkIfRoleExists(String roleName, String roleId){
		
		Object[] params = {roleName, roleId};
		logger.entering("checkIfRoleExists", params);
		
		boolean isRoleExist = false;
			try {
				isRoleExist = this.roleManager.checkIfRoleAlreadyExists(
						roleName, Integer.valueOf(roleId));
			}
			catch (DBException dbException) {
				logger.error("An error occured while " +
						"validating customer's KTP", dbException);
			}
		
		logger.exiting("checkIfRoleExists");
			
		return isRoleExist;
	}
	
	/**
	 * Method to add Menu Role Access Details.
	 * 
	 * @param roleVO an instance of <code>RoleVO</code> representing roleVO.
	 */
	public void addMenuMappingRoleAccessDetails(RoleVO roleVO){
		
		Object[] params = {roleVO};
		logger.entering("addMenuMappingRoleAccessDetails", params);
		
		this.roleManager.insertRoleMenuAccessDetails(roleVO);
		
		logger.exiting("addMenuMappingRoleAccessDetails");
	}
	
	/**
	 * Method to modify menu role access details.
	 * 
	 * @param roleVO an instance of <code>RoleVO</code> representing roleVO.
	 */
	public void modifyMenuMappingRoleAccessDetails(RoleVO roleVO){
		
		Object[] params = {roleVO};
		logger.entering("modifyMenuMappingRoleAccessDetails", params);
		
		this.roleManager.updateRoleMenuAccessDetails(roleVO);
		
		logger.exiting("modifyMenuMappingRoleAccessDetails");
		
		
	}
	
	/**
	 * Gets all the roles to populate the role field in various
	 * use cases.
	 * 
	 * @return an instance of <code>Map</code> representing all Roles Map.
	 */
	public Map<Integer, String> getRoles() {
		logger.entering("getRoles");

		Map<Integer, String> roleMap = new HashMap<Integer, String>();
		try {
			List<RoleMaster> roleMasterList = roleManager.getRolesList();

			for(RoleMaster roleMaster : roleMasterList){				
				roleMap.put(roleMaster.getRoleId(), roleMaster.getRoleName());
			}
		} 
		catch(MISPException exception) {			
			logger.error("An exception occured while fetching list of all " +
					"roles.", exception);
		}
		catch (Exception exception){			
			logger.error("An exception occured while fetching list of all " +
					"roles.", exception);
		}	

		logger.exiting("getRoles");
		return roleMap;
	}
	
	/**
	 * This Method will give all the roles list. 
	 * 
	 * @return an instance of <code>List</code> of String representing all roles.
	 */
	public List<RoleMaster> getRolesList(){
		
		logger.entering("getRolesList");
		
		List<RoleMaster> roleMasterList = new ArrayList<RoleMaster>();
		try {
			roleMasterList = this.roleManager.getRolesList();
		} catch (DBException dbException) {
			logger.error("DB Exception occured while " +
					"adding role details", dbException);
		}
		
		logger.exiting("getRolesList");
		
		return roleMasterList;
	}
	
	/**
	 * This method will get all the menu details for the given role.
	 * 
	 * @param roleId an instance of int type representing role id.
	 * 
	 * @return an instance of <code>RoleVO</code> representing RoleVO
	 */
	public RoleVO getMenuDetailsForRole(int roleId){
		
		Object[] params = {roleId};
		logger.entering("getMenuDetailsForRole", params);
		
		RoleVO roleVO = new RoleVO();
		
		roleVO = this.roleManager.retrieveMenuDetailsByRoleId(roleId);
		
		String parentMenuString = roleVO.getParentMenuString();
		String childMenuString = roleVO.getChildMenuString();
		
		
		roleVO.setRoleId(""+roleId);
		
		if(parentMenuString != null){
			StringTokenizer parentStringTokenizer = new StringTokenizer(
					roleVO.getParentMenuString(), ",");
			List<Integer> parentMenus = new ArrayList<Integer>();
			while(parentStringTokenizer.hasMoreTokens()){
				parentMenus.add(Integer.parseInt(parentStringTokenizer.nextToken()));
			}
			roleVO.setParentMenus(parentMenus);
		}
		
		if(childMenuString != null){
			StringTokenizer childStringTokenizer = new StringTokenizer(
					roleVO.getChildMenuString(), ",");
			List<Integer> childMenus = new ArrayList<Integer>();
			while(childStringTokenizer.hasMoreTokens()){
				childMenus.add(Integer.parseInt(childStringTokenizer.nextToken()));
			}
			roleVO.setChildMenus(childMenus);
		}
		
		logger.exiting("getMenuDetailsForRole", roleVO);
		
		return roleVO;
	}
	
	/**
	 * This method will get all the menu details for the given role.
	 * 
	 * @return an instance of <code>RoleVO</code> representing RoleVO
	 */
	public RoleVO getAllMenuDetails(){
		
		logger.entering("getAllMenuDetails");
		
		RoleVO roleVO = new RoleVO();
		
		roleVO = this.roleManager.retrieveAllMenuDetails();
		
		String parentMenuString = roleVO.getParentMenuString();
		String childMenuString = roleVO.getChildMenuString();
		
		roleVO.setRoleId(""+1);
		
		if(parentMenuString != null){
			StringTokenizer parentStringTokenizer = new StringTokenizer(
					roleVO.getParentMenuString(), ",");
			List<Integer> parentMenus = new ArrayList<Integer>();
			while(parentStringTokenizer.hasMoreTokens()){
				parentMenus.add(Integer.parseInt(parentStringTokenizer.nextToken()));
			}
			
			roleVO.setParentMenus(parentMenus);
		}
		
		if(childMenuString != null){
			StringTokenizer childStringTokenizer = new StringTokenizer(
					roleVO.getChildMenuString(), ",");
			List<Integer> childMenus = new ArrayList<Integer>();
			while(childStringTokenizer.hasMoreTokens()){
				childMenus.add(Integer.parseInt(childStringTokenizer.nextToken()));
			}
			roleVO.setChildMenus(childMenus);
		}
		
		logger.exiting("getAllMenuDetails", roleVO);
		
		return roleVO;
	}
	
	/**
	 * This method retrieves the child menus for the given parent id.
	 * 
	 * @param parentMenuId an int representing menu parent id.
	 *            
	 * @return instance of <code>RoleVO</code> containing child menu RoleVO.
	 */
	public RoleVO getChildMenusForParentMenuId(int parentMenuId) 
			throws MISPException 
	{
		
		Object[] params = {parentMenuId};
		logger.entering("getChildMenusForParentMenuId", params);
		
		RoleVO childMenuRoleVO = null;
		childMenuRoleVO = roleManager.fetchChildMenuDetailsByParentId(parentMenuId);
		
		logger.exiting("getChildMenusForParentMenuId", childMenuRoleVO);
		
		return childMenuRoleVO;
	}
	
}
