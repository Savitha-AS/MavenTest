package com.mip.application.model;

public class HashHistory extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -594095140864935253L;

	private int hashHistoryId;
	private UserDetails userDetails;
	private String userHash;
	private int hashPriority;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HashHistory [hashHistoryId=");
		builder.append(hashHistoryId);
		//builder.append(", userDetails=");
		//builder.append(userDetails);
		builder.append(", userHash=");
		builder.append(userHash);
		builder.append(", hashPriority=");
		builder.append(hashPriority);
		builder.append("]");
		return builder.toString();
	}

	public int getHashHistoryId() {
		return hashHistoryId;
	}

	public void setHashHistoryId(int hashHistoryId) {
		this.hashHistoryId = hashHistoryId;
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

	public int getHashPriority() {
		return hashPriority;
	}

	public void setHashPriority(int hashPriority) {
		this.hashPriority = hashPriority;
	}
}
