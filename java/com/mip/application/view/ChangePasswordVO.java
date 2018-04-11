package com.mip.application.view;

import java.io.Serializable;

public class ChangePasswordVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -675933769611514573L;
	
	private String currentHash;
	private String newHash;
	private String firstLogin;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChangePasswordVO [firstLogin=");
		/*builder.append(currentHash);
		builder.append(", newHash=");
		builder.append(newHash);
		builder.append(", firstLogin=");*/
		builder.append(firstLogin);
		builder.append("]");
		return builder.toString();
	}

	public String getCurrentHash() {
		return currentHash;
	}

	public void setCurrentHash(String currentHash) {
		this.currentHash = currentHash;
	}

	public String getNewHash() {
		return newHash;
	}

	public void setNewHash(String newHash) {
		this.newHash = newHash;
	}

	public String getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(String firstLogin) {
		this.firstLogin = firstLogin;
	}
	
}
