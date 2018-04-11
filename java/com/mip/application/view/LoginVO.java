package com.mip.application.view;

import java.io.Serializable;
import java.util.ArrayList;

public class LoginVO implements Serializable
{
	
	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -6676130788051146706L;

	private String loginId;
	
	private String userHash;
	
	private boolean isLocked;
	
	private int attemptCount;
	
	private ArrayList<String> hash;
	
	private int roleId;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoginVO [loginId=");
		builder.append(loginId);
		/*builder.append(", userHash=");
		builder.append(userHash);*/
		builder.append(", isLocked=");
		builder.append(isLocked);
		builder.append(", attemptCount=");
		builder.append(attemptCount);
		builder.append(", hash=");
		builder.append(hash);
		builder.append(", roleId=");
		builder.append(roleId);
		builder.append("]");
		return builder.toString();
	}

	/**
	 * @return the loginId
	 */
	public String getLoginId() {
		return loginId;
	}

	/**
	 * @param loginId the loginId to set
	 */
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	/**
	 * @return the userHash
	 */
	public String getUserHash() {
		return userHash;
	}

	/**
	 * @param userHash the userHash to set
	 */
	public void setUserHash(String userHash) {
		this.userHash = userHash;
	}

	/**
	 * @return the isLocked
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * @param isLocked the isLocked to set
	 */
	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * @return the attemptCount
	 */
	public int getAttemptCount() {
		return attemptCount;
	}

	/**
	 * @param attemptCount the attemptCount to set
	 */
	public void setAttemptCount(int attemptCount) {
		this.attemptCount = attemptCount;
	}

	/**
	 * @return the hash
	 */
	public ArrayList<String> getHash() {
		return hash;
	}

	/**
	 * @param hash the hash to set
	 */
	public void setHash(ArrayList<String> hash) {
		this.hash = hash;
	}

	/**
	 * @return the roleId
	 */
	public int getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
