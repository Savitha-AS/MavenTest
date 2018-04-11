package com.mip.application.model;

import java.util.Date;

public class UserLeaveDetails extends BaseModel implements
java.io.Serializable  {
	
	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -2616375166351561195L;
	private int leaveId;
	private UserDetails user;
	private Date leaveDate;
	private String reason;
	private UserDetails createdBy;
	private Date createdDate;
	
	public int getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}
	public UserDetails getUser() {
		return user;
	}	
	public void setUser(UserDetails user) {
		this.user = user;
	}
	public Date getLeaveDate() {
		return leaveDate;
	}
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public UserDetails getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserDetails createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserLeaveDetails [leaveId=");
		builder.append(leaveId);
		//builder.append(", user=");
		//builder.append(user);
		builder.append(", leaveDate=");
		builder.append(leaveDate);
		builder.append(", reason=");
		builder.append(reason);
		//builder.append(", createdBy=");
		//builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append("]");
		return builder.toString();
	}
	

	
	

}
