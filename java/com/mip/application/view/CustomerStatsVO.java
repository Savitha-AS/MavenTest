package com.mip.application.view;

import java.io.Serializable;

public class CustomerStatsVO implements Serializable {
	
	/**
	 * Generated serial Version UID
	 */
	private static final long serialVersionUID = 6465940209495484908L;
	
	private String xlRegByUserConfirmed;
	private String xlRegByUserPending;
	private String xlRegByUserDeducted;
	//adding for GH-50
	private String xlConfirmedByFullyDeducted;
	private String xlConfirmedByPartiallyDeducted;
	private String xlConfirmedInFully_PartiallyDeducted;
	
	private String freeModelRegByUserConfirmed;
	private String freeModelRegByUserPending;
	
	private String hospRegByUserConfirmed;
	private String hospRegByUserPending;
	private String hospRegByUserDeducted;
	
	//adding for GH-50
	private String hospConfirmedByFullyDeducted;
	private String hospConfirmedByPartiallyDeducted;
	private String hospConfirmedInFully_PartiallyDeducted;
	
	private String ipRegByUserConfirmed;
	private String ipRegByUserPending;
	private String ipRegByUserDeducted;
	
	//adding for GH-50
	private String ipConfirmedByFullyDeducted;
	private String ipConfirmedByPartiallyDeducted;
	private String ipConfirmedInFully_PartiallyDeducted;
	
	private String marqueeMessage;
	private String lastUpdateTimeStamp;
	private String lastLoginDate;
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CustomerStatsVO [xlRegByUserConfirmed=");
		builder.append(xlRegByUserConfirmed);
		builder.append(", xlRegByUserPending=");
		builder.append(xlRegByUserPending);
		builder.append(", xlRegByUserDeducted=");
		builder.append(xlRegByUserDeducted);
		builder.append(", xlConfirmedByFullyDeducted=");
		builder.append(xlConfirmedByFullyDeducted);
		builder.append(", xlConfirmedByPartiallyDeducted=");
		builder.append(xlConfirmedByPartiallyDeducted);
		builder.append(", xlConfirmedInFully_PartiallyDeducted=");
		builder.append(xlConfirmedInFully_PartiallyDeducted);
		builder.append(", freeModelRegByUserConfirmed=");
		builder.append(freeModelRegByUserConfirmed);
		builder.append(", freeModelRegByUserPending=");
		builder.append(freeModelRegByUserPending);
		builder.append(", hospRegByUserConfirmed=");
		builder.append(hospRegByUserConfirmed);
		builder.append(", hospRegByUserPending=");
		builder.append(hospRegByUserPending);
		builder.append(", hospRegByUserDeducted=");
		builder.append(hospRegByUserDeducted);
		builder.append(", hospConfirmedByFullyDeducted=");
		builder.append(hospConfirmedByFullyDeducted);
		builder.append(", hospConfirmedByPartiallyDeducted=");
		builder.append(hospConfirmedByPartiallyDeducted);
		builder.append(", hospConfirmedInFully_PartiallyDeducted=");
		builder.append(hospConfirmedInFully_PartiallyDeducted);
		builder.append(", ipRegByUserConfirmed=");
		builder.append(ipRegByUserConfirmed);
		builder.append(", ipRegByUserPending=");
		builder.append(ipRegByUserPending);
		builder.append(", ipRegByUserDeducted=");
		builder.append(ipRegByUserDeducted);
		builder.append(", ipConfirmedByFullyDeducted=");
		builder.append(ipConfirmedByFullyDeducted);
		builder.append(", ipConfirmedByPartiallyDeducted=");
		builder.append(ipConfirmedByPartiallyDeducted);
		builder.append(", ipConfirmedInFully_PartiallyDeducted=");
		builder.append(ipConfirmedInFully_PartiallyDeducted);
		builder.append(", marqueeMessage=");
		builder.append(marqueeMessage);
		builder.append(", lastUpdateTimeStamp=");
		builder.append(lastUpdateTimeStamp);
		builder.append(", lastLoginDate=");
		builder.append(lastLoginDate);
		builder.append("]");
		return builder.toString();
	}

	public String getXlRegByUserDeducted() {
		return xlRegByUserDeducted;
	}

	public void setXlRegByUserDeducted(String xlRegByUserDeducted) {
		this.xlRegByUserDeducted = xlRegByUserDeducted;
	}

	public String getHospRegByUserDeducted() {
		return hospRegByUserDeducted;
	}

	public void setHospRegByUserDeducted(String hospRegByUserDeducted) {
		this.hospRegByUserDeducted = hospRegByUserDeducted;
	}

	public String getXlRegByUserConfirmed() {
		return xlRegByUserConfirmed;
	}

	public void setXlRegByUserConfirmed(String xlRegByUserConfirmed) {
		this.xlRegByUserConfirmed = xlRegByUserConfirmed;
	}

	public String getXlRegByUserPending() {
		return xlRegByUserPending;
	}

	public void setXlRegByUserPending(String xlRegByUserPending) {
		this.xlRegByUserPending = xlRegByUserPending;
	}

	public String getFreeModelRegByUserConfirmed() {
		return freeModelRegByUserConfirmed;
	}

	public void setFreeModelRegByUserConfirmed(String freeModelRegByUserConfirmed) {
		this.freeModelRegByUserConfirmed = freeModelRegByUserConfirmed;
	}

	public String getFreeModelRegByUserPending() {
		return freeModelRegByUserPending;
	}

	public void setFreeModelRegByUserPending(String freeModelRegByUserPending) {
		this.freeModelRegByUserPending = freeModelRegByUserPending;
	}

	public String getMarqueeMessage() {
		return marqueeMessage;
	}
	public void setMarqueeMessage(String marqueeMessage) {
		this.marqueeMessage = marqueeMessage;
	}
	public String getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}
	public void setLastUpdateTimeStamp(String lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getHospRegByUserConfirmed() {
		return hospRegByUserConfirmed;
	}

	public void setHospRegByUserConfirmed(String hospRegByUserConfirmed) {
		this.hospRegByUserConfirmed = hospRegByUserConfirmed;
	}

	public String getHospRegByUserPending() {
		return hospRegByUserPending;
	}

	public void setHospRegByUserPending(String hospRegByUserPending) {
		this.hospRegByUserPending = hospRegByUserPending;
	}

	public String getIpRegByUserConfirmed() {
		return ipRegByUserConfirmed;
	}

	public void setIpRegByUserConfirmed(String ipRegByUserConfirmed) {
		this.ipRegByUserConfirmed = ipRegByUserConfirmed;
	}

	public String getIpRegByUserPending() {
		return ipRegByUserPending;
	}

	public void setIpRegByUserPending(String ipRegByUserPending) {
		this.ipRegByUserPending = ipRegByUserPending;
	}

	public String getIpRegByUserDeducted() {
		return ipRegByUserDeducted;
	}

	public void setIpRegByUserDeducted(String ipRegByUserDeducted) {
		this.ipRegByUserDeducted = ipRegByUserDeducted;
	}

	public String getXlConfirmedByFullyDeducted() {
		return xlConfirmedByFullyDeducted;
	}

	public void setXlConfirmedByFullyDeducted(String xlConfirmedByFullyDeducted) {
		this.xlConfirmedByFullyDeducted = xlConfirmedByFullyDeducted;
	}

	public String getXlConfirmedByPartiallyDeducted() {
		return xlConfirmedByPartiallyDeducted;
	}

	public void setXlConfirmedByPartiallyDeducted(
			String xlConfirmedByPartiallyDeducted) {
		this.xlConfirmedByPartiallyDeducted = xlConfirmedByPartiallyDeducted;
	}

	public String getXlConfirmedInFully_PartiallyDeducted() {
		return xlConfirmedInFully_PartiallyDeducted;
	}

	public void setXlConfirmedInFully_PartiallyDeducted(
			String xlConfirmedInFully_PartiallyDeducted) {
		this.xlConfirmedInFully_PartiallyDeducted = xlConfirmedInFully_PartiallyDeducted;
	}

	public String getHospConfirmedByFullyDeducted() {
		return hospConfirmedByFullyDeducted;
	}

	public void setHospConfirmedByFullyDeducted(String hospConfirmedByFullyDeducted) {
		this.hospConfirmedByFullyDeducted = hospConfirmedByFullyDeducted;
	}

	public String getHospConfirmedByPartiallyDeducted() {
		return hospConfirmedByPartiallyDeducted;
	}

	public void setHospConfirmedByPartiallyDeducted(
			String hospConfirmedByPartiallyDeducted) {
		this.hospConfirmedByPartiallyDeducted = hospConfirmedByPartiallyDeducted;
	}

	public String getHospConfirmedInFully_PartiallyDeducted() {
		return hospConfirmedInFully_PartiallyDeducted;
	}

	public void setHospConfirmedInFully_PartiallyDeducted(
			String hospConfirmedInFully_PartiallyDeducted) {
		this.hospConfirmedInFully_PartiallyDeducted = hospConfirmedInFully_PartiallyDeducted;
	}

	public String getIpConfirmedByFullyDeducted() {
		return ipConfirmedByFullyDeducted;
	}

	public void setIpConfirmedByFullyDeducted(String ipConfirmedByFullyDeducted) {
		this.ipConfirmedByFullyDeducted = ipConfirmedByFullyDeducted;
	}

	public String getIpConfirmedByPartiallyDeducted() {
		return ipConfirmedByPartiallyDeducted;
	}

	public void setIpConfirmedByPartiallyDeducted(
			String ipConfirmedByPartiallyDeducted) {
		this.ipConfirmedByPartiallyDeducted = ipConfirmedByPartiallyDeducted;
	}

	public String getIpConfirmedInFully_PartiallyDeducted() {
		return ipConfirmedInFully_PartiallyDeducted;
	}

	public void setIpConfirmedInFully_PartiallyDeducted(
			String ipConfirmedInFully_PartiallyDeducted) {
		this.ipConfirmedInFully_PartiallyDeducted = ipConfirmedInFully_PartiallyDeducted;
	}
	

}
