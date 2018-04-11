package com.mip.application.model;

/**
 * <code>RoleAccess</code> class representing RoleAccess.
 * 
 * @author THBS
 *
 */
public class RoleAccess extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -7205018707042639745L;
	
	/**
	 * Instance of type int representing roleAccessId.
	 */
	int roleAccessId;
	
	/**
	 * Instance of type int representing parentRoleAccessId.
	 */
	int parentRoleAccessId;
	
	/**
	 * Instance of <code>String</code> representing roleURL.
	 */
	String roleUrl;
	
	/**
	 * Instance of <code>String</code> representing roleAllowed.
	 */
	String roleAllowed;

	/**
	 * Gets the roleAccessId
	 * 
	 * @return the roleAccessId an Instance of type int representing roleAccessId.
	 */
	public int getRoleAccessId() {
		return roleAccessId;
	}

	/**
	 * Sets the roleAccessId
	 * 
	 * @param the roleAccessId an Instance of type int representing roleAccessId.
	 */
	public void setRoleAccessId(int roleAccessId) {
		this.roleAccessId = roleAccessId;
	}

	/**
	 * Gets the parentRoleAccessId
	 * 
	 * @return the roleAccessId an Instance of type int representing parentRoleAccessId.
	 */
	public int getParentRoleAccessId() {
		return parentRoleAccessId;
	}
	
	/**
	 * Sets the parentRoleAccessId
	 * 
	 * @param the roleAccessId an Instance of type int representing parentRoleAccessId.
	 */
	public void setParentRoleAccessId(int parentRoleAccessId) {
		this.parentRoleAccessId = parentRoleAccessId;
	}
	
	/**
	 * Gets the roleUrl.
	 * 
	 * @return the roleUrl an Instance of <code>String</code> representing roleURL.
	 */
	public String getRoleUrl() {
		return roleUrl;
	}

	/**
	 * Sets the roleUrl
	 * 
	 * @param roleUrl an Instance of <code>String</code> representing roleURL.
	 */
	public void setRoleUrl(String roleUrl) {
		this.roleUrl = roleUrl;
	}

	/**
	 * Gets the roleAllowed.
	 * 
	 * @return the roleAllowed an Instance of <code>String</code> representing roleAllowed.
	 */
	public String getRoleAllowed() {
		return roleAllowed;
	}

	/**
	 * Sets the roleAllowed.
	 * 
	 * @param roleAllowed an Instance of <code>String</code> representing roleAllowed.
	 */
	public void setRoleAllowed(String roleAllowed) {
		this.roleAllowed = roleAllowed;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RoleAccess [roleAccessId=");
		builder.append(roleAccessId);
		builder.append(", roleUrl=");
		builder.append(roleUrl);
		builder.append(", roleAllowed=");
		builder.append(roleAllowed);
		builder.append("]");
		
		return super.toString();
	}
	
}
