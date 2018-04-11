package com.mip.application.view;

import java.io.Serializable;

public class ReportDailyNewVO implements Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -8558760299296949289L;

	private String userUid;
	private Integer userId;
	private String regDate;
	private int registeredFree;
	private int confirmedFree;
	private int registeredXL;
	private int confirmedXL;
	private int registeredHP;
	private int confirmedHP;
	private int deductedXL;
	private int deductedHP;
	private int deductedXLandHP;
	private int registeredXLandHP;
	private int registeredXLandIP;
	private int confirmedXLandHP;
	private String leaveReason;
	private int registeredIP;
	private int confirmedIP;
	private int confirmedXLandIP;
	private int deductedXLandIP;
	private int deductedIP;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportDailyNewVO [userUid=");
		builder.append(userUid);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", regDate=");
		builder.append(regDate);
		builder.append(", registeredFree=");
		builder.append(registeredFree);
		builder.append(", confirmedFree=");
		builder.append(confirmedFree);
		builder.append(", registeredXL=");
		builder.append(registeredXL);
		builder.append(", confirmedXL=");
		builder.append(confirmedXL);
		builder.append(", registeredHP=");
		builder.append(registeredHP);
		builder.append(", confirmedHP=");
		builder.append(confirmedHP);
		builder.append(", deductedXL=");
		builder.append(deductedXL);
		builder.append(", deductedHP=");
		builder.append(deductedHP);
		builder.append(", deductedXLandHP=");
		builder.append(deductedXLandHP);
		builder.append(", registeredXLandHP=");
		builder.append(registeredXLandHP);
		builder.append(", registeredXLandIP=");
		builder.append(registeredXLandIP);
		builder.append(", confirmedXLandHP=");
		builder.append(confirmedXLandHP);
		builder.append(", leaveReason=");
		builder.append(leaveReason);
		builder.append(", registeredIP=");
		builder.append(registeredIP);
		builder.append(", confirmedIP=");
		builder.append(confirmedIP);
		builder.append(", confirmedXLandIP=");
		builder.append(confirmedXLandIP);
		builder.append(", deductedXLandIP=");
		builder.append(deductedXLandIP);
		builder.append(", deductedIP=");
		builder.append(deductedIP);
		builder.append("]");
		return builder.toString();
	}

	public int getDeductedXL() {
		return deductedXL;
	}

	public void setDeductedXL(int deductedXL) {
		this.deductedXL = deductedXL;
	}

	public int getDeductedHP() {
		return deductedHP;
	}

	public void setDeductedHP(int deductedHP) {
		this.deductedHP = deductedHP;
	}

	public int getDeductedXLandHP() {
		return deductedXLandHP;
	}

	public void setDeductedXLandHP(int deductedXLandHP) {
		this.deductedXLandHP = deductedXLandHP;
	}

	public String getUserUid() {
		return userUid;
	}

	public void setUserUid(String userUid) {
		this.userUid = userUid;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getRegDate() {
		return regDate;
	}
	
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	public int getRegisteredFree() {
		return registeredFree;
	}
	
	public void setRegisteredFree(int registeredFree) {
		this.registeredFree = registeredFree;
	}
	
	public int getConfirmedFree() {
		return confirmedFree;
	}
	
	public void setConfirmedFree(int confirmedFree) {
		this.confirmedFree = confirmedFree;
	}
	
	public int getRegisteredXL() {
		return registeredXL;
	}

	public void setRegisteredXL(int registeredXL) {
		this.registeredXL = registeredXL;
	}

	public int getConfirmedXL() {
		return confirmedXL;
	}

	public void setConfirmedXL(int confirmedXL) {
		this.confirmedXL = confirmedXL;
	}

	public int getRegisteredHP() {
		return registeredHP;
	}

	public void setRegisteredHP(int registeredHP) {
		this.registeredHP = registeredHP;
	}

	public int getConfirmedHP() {
		return confirmedHP;
	}

	public void setConfirmedHP(int confirmedHP) {
		this.confirmedHP = confirmedHP;
	}

	public int getRegisteredXLandHP() {
		return registeredXLandHP;
	}
	
	public void setRegisteredXLandHP(int registeredXLandHP) {
		this.registeredXLandHP = registeredXLandHP;
	}
	
	
	
	public int getRegisteredXLandIP() {
		return registeredXLandIP;
	}

	public void setRegisteredXLandIP(int registeredXLandIP) {
		this.registeredXLandIP = registeredXLandIP;
	}

	public int getConfirmedXLandHP() {
		return confirmedXLandHP;
	}
	
	public void setConfirmedXLandHP(int confirmedXLandHP) {
		this.confirmedXLandHP = confirmedXLandHP;
	}
	
	public String getLeaveReason() {
		return leaveReason;
	}

	public void setLeaveReason(String leaveReason) {
		this.leaveReason = leaveReason;
	}
	
	
	
	public int getRegisteredIP() {
		return registeredIP;
	}

	public void setRegisteredIP(int registeredIP) {
		this.registeredIP = registeredIP;
	}

	public int getConfirmedIP() {
		return confirmedIP;
	}

	public void setConfirmedIP(int confirmedIP) {
		this.confirmedIP = confirmedIP;
	}
	
	
	
	public int getConfirmedXLandIP() {
		return confirmedXLandIP;
	}

	public void setConfirmedXLandIP(int confirmedXLandIP) {
		this.confirmedXLandIP = confirmedXLandIP;
	}
	
	
	
	public int getDeductedXLandIP() {
		return deductedXLandIP;
	}

	public void setDeductedXLandIP(int deductedXLandIP) {
		this.deductedXLandIP = deductedXLandIP;
	}
	
	

	public int getDeductedIP() {
		return deductedIP;
	}

	public void setDeductedIP(int deductedIP) {
		this.deductedIP = deductedIP;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		ReportDailyNewVO reportDailyNewVO = (ReportDailyNewVO)obj;
		
		if(null != reportDailyNewVO.getUserId() && null != reportDailyNewVO.getRegDate()) {
			if (this.userId.equals(reportDailyNewVO.getUserId()) && 
					this.regDate.equals(reportDailyNewVO.getRegDate()))
				result = true;
			
		}
		return result;
	}
}
