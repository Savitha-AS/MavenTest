package com.mip.application.view;

import java.io.Serializable;
import java.util.Date;

public class UserLeaveVO implements Serializable{
	
	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -7895805443869555872L;
	private int leaveId;
	private int userId;
	private String fromDate;
	private String toDate;
	private String reason;
	private Date leaveDate;
	
	public int getLeaveId() {
		return leaveId;
	}
	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	public Date getLeaveDate() {
		return leaveDate;
	}
	
	public void setLeaveDate(Date leaveDate) {
		this.leaveDate = leaveDate;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserLeaveVO [leaveId=");
		builder.append(leaveId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", fromDate=");
		builder.append(fromDate);
		builder.append(", toDate=");
		builder.append(toDate);
		builder.append(", reason=");
		builder.append(reason);
		builder.append(", leaveDate=");
		builder.append(leaveDate);
		return builder.toString();
	}
	
	
	

}
