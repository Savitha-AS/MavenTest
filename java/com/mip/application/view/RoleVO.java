package com.mip.application.view;

import java.io.Serializable;
import java.util.List;

public class RoleVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8752902517896231880L;

	private String roleId;
	private String roleName;
	private String roleDescription;
	private String menuId;
	private String parentMenuString;
	private String childMenuString;
	private List<Integer> parentMenus;
	private List<Integer> childMenus;
	private int isIPReg;
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RoleVO [roleId=");
		builder.append(roleId);
		builder.append(", roleName=");
		builder.append(roleName);
		builder.append(", roleDescription=");
		builder.append(roleDescription);
		builder.append(", menuId=");
		builder.append(menuId);
		builder.append(", parentMenuString=");
		builder.append(parentMenuString);
		builder.append(", childMenuString=");
		builder.append(childMenuString);
		builder.append(", parentMenus=");
		builder.append(parentMenus);
		builder.append(", childMenus=");
		builder.append(childMenus);
		builder.append(", isIPReg=");
		builder.append(isIPReg);
		builder.append("]");
		return builder.toString();
	}

	public String getRoleId() {
		return roleId;
	}
	
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleDescription() {
		return roleDescription;
	}
	
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}
	
	public String getMenuId() {
		return menuId;
	}
	
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	
	public String getParentMenuString() {
		return parentMenuString;
	}
	
	public void setParentMenuString(String parentMenuString) {
		this.parentMenuString = parentMenuString;
	}
	
	public String getChildMenuString() {
		return childMenuString;
	}
	
	public void setChildMenuString(String childMenuString) {
		this.childMenuString = childMenuString;
	}
	
	public List<Integer> getParentMenus() {
		return parentMenus;
	}
	
	public void setParentMenus(List<Integer> parentMenus) {
		this.parentMenus = parentMenus;
	}
	
	public List<Integer> getChildMenus() {
		return childMenus;
	}
	
	public void setChildMenus(List<Integer> childMenus) {
		this.childMenus = childMenus;
	}

	public int getIsIPReg() {
		return isIPReg;
	}

	public void setIsIPReg(int isIPReg) {
		this.isIPReg = isIPReg;
	}

	
}
