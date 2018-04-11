package com.mip.application.view;

import java.io.Serializable;

public class UserVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 8752902517896231880L;

	private String userId;
	private String userUid;
	private String fname;	
	private String sname;	
	private String msisdn;
	private String email;
	private String dob;	
	private String age;	
	private String gender;	
	private String role;
	private String roleId;
	private String createdBy;
	private String createdDate;
	private String branch;
	private String branchId;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserVO [age=");
		builder.append(age);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", createdDate=");
		builder.append(createdDate);
		builder.append(", dob=");
		builder.append(dob);
		builder.append(", fname=");
		builder.append(fname);
		builder.append(", gender=");
		builder.append(gender);
		builder.append(", msisdn=");
		builder.append(msisdn);
		builder.append(", email=");
		builder.append(email);
		builder.append(", role=");
		builder.append(role);
		builder.append(", roleId=");
		builder.append(roleId);
		builder.append(", sname=");
		builder.append(sname);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userUid=");
		builder.append(userUid);
		builder.append(", branch=");
		builder.append(branch);
		builder.append(", branchId=");
		builder.append(branchId);
		builder.append("]");
		return builder.toString();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}
}
