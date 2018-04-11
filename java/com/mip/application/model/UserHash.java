package com.mip.application.model;


public class UserHash extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 3247346595275337010L;

	private int hashId;
	private UserDetails userDetails;
	private String userHash;
	private byte accountLocked;
	private int attemptCount;
	private byte firstLogin;
	private byte loggedIn;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserHash [hashId=");
		builder.append(hashId);
		builder.append(", userDetails=");
		builder.append(userDetails);
		builder.append(", userHash=");
		builder.append(userHash);
		builder.append(", accountLocked=");
		builder.append(accountLocked);
		builder.append(", attemptCount=");
		builder.append(attemptCount);
		builder.append(", firstLogin=");
		builder.append(firstLogin);
		builder.append(", loggedIn=");
		builder.append(loggedIn);
		builder.append("]");
		return builder.toString();
	}

	public int getHashId() {
		return hashId;
	}

	public void setHashId(int hashId) {
		this.hashId = hashId;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetails userDetails) {
		this.userDetails = userDetails;
	}

	public String getUserHash() {
		return userHash;
	}

	public void setUserHash(String userHash) {
		this.userHash = userHash;
	}

	public byte getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(byte accountLocked) {
		this.accountLocked = accountLocked;
	}

	public int getAttemptCount() {
		return attemptCount;
	}

	public void setAttemptCount(int attemptCount) {
		this.attemptCount = attemptCount;
	}

	public byte getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(byte firstLogin) {
		this.firstLogin = firstLogin;
	}

	public byte getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(byte loggedIn) {
		this.loggedIn = loggedIn;
	}
}
