package com.mip.application.model;

import java.util.Date;
import java.util.Map;

import com.mip.framework.utils.DateUtil;

public class UserDetails extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -294741501506593619L;

	private int userId;
	private String userUid;
	private String userName;			// for display in UI.
	private String fname;
	private String sname;
	private int age;
	private Date dob;
	private String gender;
	private String msisdn;
	private RoleMaster roleMaster;
	private String emailId;
	private int createdBy;
	private Date createdDate;
	private String createdDateStr;		// for display in UI.
	private int modifiedBy;
	private Date modifiedDate;
	private byte active;
	private Date lastLoggedInDate;
	private BranchDetails branchDetails;
	private Map<Integer ,Integer> currentYearLeaveMap;
	private Map<Integer ,Integer> currentMonthLeaveMap;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserDetails [userId=");
		builder.append(userId);
		builder.append(", userUid=");
		builder.append(userUid);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", fname=");
		builder.append(fname);
		builder.append(", sname=");
		builder.append(sname);
		builder.append(", age=");
		builder.append(age);
		builder.append(", dob=");
		builder.append(dob);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", msisdn=");
		builder.append(msisdn);
		//builder.append(", roleMaster=");
		//builder.append(roleMaster);
		builder.append(", emailId=");
		builder.append(emailId);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", createdDateStr=");
		builder.append(createdDateStr);
		builder.append(", modifiedBy=");
		builder.append(modifiedBy);
		builder.append(", modifiedDate=");
		builder.append(modifiedDate);
		builder.append(", active=");
		builder.append(active);
		builder.append(", lastLoggedInDate=");
		builder.append(lastLoggedInDate);
		builder.append(", branchDetails=");
		builder.append(branchDetails);
		builder.append("]");
		return builder.toString();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
		
		this.setUserName(this.fname + " " + this.sname);
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
		
		this.setUserName(this.fname + " " + this.sname);
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public RoleMaster getRoleMaster() {
		return roleMaster;
	}

	public void setRoleMaster(RoleMaster roleMaster) {
		this.roleMaster = roleMaster;
	}

	public String getEmailId() {
		return emailId;
	}
	
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	
	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
		
		// Generate a String format of the Created Date, for display in UI.
		setCreatedDateStr(DateUtil.toDateTimeMeridianString(this.createdDate));
	}

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	public String getCreatedDateStr() {
		return createdDateStr;
	}

	public void setCreatedDateStr(String createdDateStr) {
		this.createdDateStr = createdDateStr;
	}

	public Date getLastLoggedInDate() {
		return lastLoggedInDate;
	}

	public void setLastLoggedInDate(Date lastLoggedInDate) {
		this.lastLoggedInDate = lastLoggedInDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public BranchDetails getBranchDetails() {
		return branchDetails;
	}

	public void setBranchDetails(BranchDetails branchDetails) {
		this.branchDetails = branchDetails;
	}	
	
	public Map<Integer, Integer> getCurrentYearLeaveMap() {
		return currentYearLeaveMap;
	}
	
	public void setCurrentYearLeaveMap(Map<Integer, Integer> currentYearLeaveMap) {
		this.currentYearLeaveMap = currentYearLeaveMap;
	}
	
	public Map<Integer, Integer> getCurrentMonthLeaveMap() {
		return currentMonthLeaveMap;
	}
	public void setCurrentMonthLeaveMap(
			Map<Integer, Integer> currentMonthLeaveMap) {
		this.currentMonthLeaveMap = currentMonthLeaveMap;
	}
	
}
