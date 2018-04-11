package com.mip.application.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import com.mip.application.constants.FaultMessages;
import com.mip.application.constants.SuccessMessages;
import com.mip.application.dal.managers.DatabaseDrivenFilterInvocationDefinitionSource;
import com.mip.application.model.RoleMaster;
import com.mip.application.services.RoleService;
import com.mip.application.services.UserService;
import com.mip.application.view.RoleVO;
import com.mip.framework.constants.MAVObjects;
import com.mip.framework.constants.MAVPaths;
import com.mip.framework.constants.PlatformConstants;
import com.mip.framework.controllers.BasePlatformController;
import com.mip.framework.logger.LoggerFactory;
import com.mip.framework.logger.MISPLogger;
import com.mip.framework.utils.DateUtil;
import com.mip.framework.utils.TypeUtil;

/**
 * Controller Class that has functionalities related to Role Management module.
 * 
 * @author THBS
 *
 */
public class RoleController extends BasePlatformController {

	/**
	 * Instance of Logger.
	 */
	private MISPLogger logger = LoggerFactory.getInstance().getLogger(
			RoleController.class);
	
	/**
	 * Instance of RoleService.
	 */
	private RoleService roleService;
	
	private UserService userService;
	
	private DatabaseDrivenFilterInvocationDefinitionSource dbFilter;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	public void setDbFilter(
			DatabaseDrivenFilterInvocationDefinitionSource dbFilter) {
		this.dbFilter = dbFilter;
	}
	
	/**
	 * Sets the roleService.
	 * 
	 * @param roleService an instance of <code>RoleService</code> 
	 * 		  representing roleService.
	 */
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	
	/**
	 * Method to add add Role Details.
	 * 
	 * @param request an instance of HttpServletRequest representing request.
	 * 
	 * @param response an instance of HttpServletResponse representing response.
	 * 
	 * @param roleVO an instance of <code>RoleVO</code> representing RoleVO.
	 * 
	 * @return an instance of <code>ModelAndView</code> representing view object.
	 */
	public ModelAndView addRole(HttpServletRequest request, 
			HttpServletResponse response, RoleVO roleVO)
	{
		Object[] params = { roleVO };

		logger.entering("addRole", params);
		
		String roleName = roleVO.getRoleName();
		String roleDescription = new String(PlatformConstants.ROLE_DESCRIPTION_INITIAL);
		String currentDate = DateUtil.getCurrentDateString();
		
		roleDescription = roleDescription.
				concat(roleName.toUpperCase().substring(0,2)).
						concat(roleName.toUpperCase().substring(roleName.length() - 2,roleName.length())).
							concat("_").
								concat(currentDate);
		
		roleVO.setRoleDescription(roleDescription);
		
		ModelAndView mav = null;

		try {
			
			this.roleService.addMenuMappingRoleAccessDetails(roleVO);
			
			dbFilter.setAllRolesAllowedMap(userService.getAllRolesAllowed());
			
			mav = super.success(SuccessMessages.ROLE_ADDED,
					TypeUtil.arrayToCsv(params), MAVPaths.JSP_ROLE_ADD);
		} catch (Exception exception) {
			logger.error("An exception occured while adding a new  "
					+ "role record.", exception);
			mav = super.error("", MAVPaths.JSP_ROLE_ADD);
		}
		logger.exiting("addRole");

		return mav;

	}
	
	/**
	 * Method to get the Menu details for particular role.
	 * 
	 * @param request an instance of HttpServletRequest representing request.
	 * 
	 * @param response an instance of HttpServletResponse representing response.
	 * 
	 * @param roleVO an instance of <code>RoleVO</code> representing RoleVO.
	 * 
	 * @return an instance of <code>ModelAndView</code> representing view object. 
	 */
	public ModelAndView getMenusByRole(HttpServletRequest request, 
			HttpServletResponse response, RoleVO roleVO){
		
		Object[] params = {roleVO};
		logger.entering("getMenusByRole",params);
		
		ModelAndView mav = null;
		RoleVO roleDetailsVO = new RoleVO();
		
		String role = (String) request.getParameter("role");
		
		if(role!=null && !role.equals("")){

			roleDetailsVO = this.roleService.getMenuDetailsForRole(
					Integer.parseInt(role));
		}
		
		
		logger.debug("roleDetailsVO", roleDetailsVO);
		
		mav = viewAndModifyRole(request, response);
		mav.addObject(MAVObjects.VO_ROLE, roleDetailsVO);
		
		logger.exiting("getMenusByRole");
		
		return mav;
	}
	
	/**
	 * Method to get the all the Menu details.
	 * 
	 * @param request an instance of HttpServletRequest representing request.
	 * 
	 * @param response an instance of HttpServletResponse representing response.
	 * 
	 * @param roleVO an instance of <code>RoleVO</code> representing RoleVO.
	 * 
	 * @return an instance of <code>ModelAndView</code> representing view object. 
	 */
	public ModelAndView getAllMenus(HttpServletRequest request, 
			HttpServletResponse response, RoleVO roleVO){
		
		Object[] params = {roleVO};
		logger.entering("getAllMenus",params);
		
		ModelAndView mav = null;
		
		RoleVO roleDetailsVO = this.roleService.getAllMenuDetails();
		
		logger.debug("roleDetailsVO", roleDetailsVO);
		
		mav = viewAndModifyRole(request, response);
		mav.addObject(MAVObjects.VO_ROLE, roleDetailsVO);
		
		logger.exiting("getAllMenus");
		
		return mav;
	}
	
	/**
	 * This method controls the launching of View and modify role page.
	 * 
	 * @param httpServletRequest
	 * 		an instance of HTTPServletRequest
	 * 
	 * @param httpServletResponse
	 * 		an instance of HTTPServletRequest
	 * 
	 * @return 
	 * 		ModelAndView, a view object
	 */
	public ModelAndView viewAndModifyRole(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) 
	{
		logger.entering("viewAndModifyRole");

		ModelAndView mav = null;
		List<RoleMaster> roleList = null;
		Map<Integer, String> roleMap = new HashMap<Integer, String>();
		try {
			
			logger.debug("Roles List : ", roleList);
			roleMap = this.roleService.getRoles();
			
			mav = new ModelAndView(MAVPaths.VIEW_AND_MODIFY_ROLE);
			mav.addObject(MAVObjects.MAP_ROLES, roleMap);
		} 
		catch (Exception exception){			
			logger.error(FaultMessages.GENERIC_ERROR
					, exception);			
			mav = super.error(FaultMessages.GENERIC_ERROR);
		}	

		logger.exiting("viewAndModifyRole", mav);
		return mav;
	}
	
	/**
	 * Method to modify Role Details. 
	 * 
	 * @param request instance of <code>HttpServletRequest</code> 
	 * 		  representing request.
	 * 
	 * @param response instance of <code>HttpServletResponse</code>
	 * 		  representing response.
	 * 
	 * @param roleVO an instance of <code>RoleVO</code> representing RoleVO.
	 * 
	 * @return an instance of <code>ModelAndView</code> representing view object.
	 */
	public ModelAndView modifyRoleDetails(
			HttpServletRequest request, HttpServletResponse response, 
			RoleVO roleVO) 
	{
		Object[] params = {roleVO};
		logger.entering("modifyRoleDetails", params);
		
		ModelAndView mav = null;
		
		this.roleService.modifyMenuMappingRoleAccessDetails(roleVO);
		
		dbFilter.setAllRolesAllowedMap(userService.getAllRolesAllowed());
		
		mav = super.success(SuccessMessages.ROLE_MODIFIED, 
				TypeUtil.arrayToCsv(params), MAVPaths.JSP_ROLE_VIEW_AND_MODIFY);
		
		logger.exiting("modifyRoleDetails");
		
		return mav;
	}
}
