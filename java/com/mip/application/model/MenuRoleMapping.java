package com.mip.application.model;

public class MenuRoleMapping extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 4840573934705572127L;

	private int roleMenuId;
	private RoleMaster roleMaster;
	private Menu menu;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MenuRoleMapping [roleMenuId=");
		builder.append(roleMenuId);
		//builder.append(", roleMaster=");
		//builder.append(roleMaster);
		//builder.append(", menu=");
		//builder.append(menu);
		builder.append("]");
		return builder.toString();
	}

	public int getRoleMenuId() {
		return roleMenuId;
	}

	public void setRoleMenuId(int roleMenuId) {
		this.roleMenuId = roleMenuId;
	}

	public RoleMaster getRoleMaster() {
		return roleMaster;
	}

	public void setRoleMaster(RoleMaster roleMaster) {
		this.roleMaster = roleMaster;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}
}
