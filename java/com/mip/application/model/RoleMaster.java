package com.mip.application.model;

public class RoleMaster extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 2517945053055768179L;

	private int roleId;
	private String roleName;
	private String roleDescription;
	private int isIPReg;
	

	public int getIsIPReg() {
		return isIPReg;
	}

	public void setIsIPReg(int isIPReg) {
		this.isIPReg = isIPReg;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RoleMaster [roleId=");
		builder.append(roleId);
		builder.append(", roleName=");
		builder.append(roleName);
		builder.append(", roleDescription=");
		builder.append(roleDescription);
		builder.append(", isIPReg=");
		builder.append(isIPReg);
		builder.append("]");
		return builder.toString();
	}
}
