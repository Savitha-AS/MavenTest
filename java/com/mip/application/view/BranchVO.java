package com.mip.application.view;

public class BranchVO implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -5219429250198794713L;	
	
	private String branchId;
	private String branchCode;
	private String branchName;
	private String branchStreet;
	private String branchRegion;
	private String branchCity;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BranchVO [branchRegion=");
		builder.append(branchRegion);
		builder.append(", branchCity=");
		builder.append(branchCity);
		builder.append(", branchCode=");
		builder.append(branchCode);
		builder.append(", branchId=");
		builder.append(branchId);
		builder.append(", branchName=");
		builder.append(branchName);
		builder.append(", branchStreet=");
		builder.append(branchStreet);
		builder.append("]");
		return builder.toString();
	}

	public String getBranchId() {
		return this.branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchCode() {
		return this.branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return this.branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchStreet() {
		return this.branchStreet;
	}

	public void setBranchStreet(String branchStreet) {
		this.branchStreet = branchStreet;
	}

	public String getBranchRegion() {
		return this.branchRegion;
	}

	public void setBranchRegion(String branchRegion) {
		this.branchRegion = branchRegion;
	}

	public String getBranchCity() {
		return this.branchCity;
	}

	public void setBranchCity(String branchCity) {
		this.branchCity = branchCity;
	}
}
