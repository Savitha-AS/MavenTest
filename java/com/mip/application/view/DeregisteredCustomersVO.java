package com.mip.application.view;

import java.io.Serializable;

public class DeregisteredCustomersVO implements Serializable{

	/**
	 * Generated Serial Version UID
	 */
	private static final long serialVersionUID = -8744936863121915799L;
	
	private String deregDate;
	private String freeDeregDate;
	private String XLDeregDate;
	private String HPDeregDate;
	private String IPDeregDate;
	private Integer regBy;
	private Integer XLRegBy;
	private Integer HPRegBy;
	private Integer IPRegBy;
	private Integer deregFree;
	private Integer deregXL;
	private Integer deregHP;
	private Integer deregIP;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeregisteredCustomersVO [deregDate=");
		builder.append(deregDate);
		builder.append(", freeDeregDate=");
		builder.append(freeDeregDate);
		builder.append(", XLDeregDate=");
		builder.append(XLDeregDate);
		builder.append(", HPDeregDate=");
		builder.append(HPDeregDate);
		builder.append(", IPDeregDate=");
		builder.append(IPDeregDate);
		builder.append(", deregFree=");
		builder.append(deregFree);
		builder.append(", deregXL=");
		builder.append(deregXL);
		builder.append(", deregHP=");
		builder.append(deregHP);
		builder.append(", deregIP=");
		builder.append(deregIP);
		builder.append(", regBy=");
		builder.append(regBy);
		builder.append(", XLRegBy=");
		builder.append(XLRegBy);
		builder.append(", HPRegBy=");
		builder.append(HPRegBy);
		builder.append(", IPRegBy=");
		builder.append(IPRegBy);
		builder.append("]");
		return builder.toString();
	}
	
	public String getDeregDate() {
		return deregDate;
	}
	public void setDeregDate(String deregDate) {
		this.deregDate = deregDate;
	}
	public String getFreeDeregDate() {
		return freeDeregDate;
	}
	public void setFreeDeregDate(String freeDeregDate) {
		this.freeDeregDate = freeDeregDate;
	}
	public String getXLDeregDate() {
		return XLDeregDate;
	}
	public void setXLDeregDate(String xLDeregDate) {
		XLDeregDate = xLDeregDate;
	}
	public String getHPDeregDate() {
		return HPDeregDate;
	}
	public void setHPDeregDate(String hPDeregDate) {
		HPDeregDate = hPDeregDate;
	}
	
	
	public String getIPDeregDate() {
		return IPDeregDate;
	}

	public void setIPDeregDate(String iPDeregDate) {
		IPDeregDate = iPDeregDate;
	}

	public Integer getDeregXL() {
		return deregXL;
	}
	public void setDeregXL(Integer deregXL) {
		this.deregXL = deregXL;
	}
	public Integer getRegBy() {
		return regBy;
	}
	public void setRegBy(Integer regBy) {
		this.regBy = regBy;
	}
	public Integer getXLregBy() {
		return XLRegBy;
	}
	public void setXLregBy(Integer XLRegBy) {
		this.XLRegBy = XLRegBy;
	}
	public Integer getHPRegBy() {
		return HPRegBy;
	}
	public void setHPRegBy(Integer hPRegBy) {
		HPRegBy = hPRegBy;
	}
	
	
	public Integer getIPRegBy() {
		return IPRegBy;
	}

	public void setIPRegBy(Integer iPRegBy) {
		IPRegBy = iPRegBy;
	}

	public Integer getDeregFree() {
		return deregFree;
	}
	public void setDeregFree(Integer deregFree) {
		this.deregFree = deregFree;
	}
	public Integer getDeregHP() {
		return deregHP;
	}
	public void setDeregHP(Integer deregHP) {
		this.deregHP = deregHP;
	}
	
	
	
	public Integer getDeregIP() {
		return deregIP;
	}

	public void setDeregIP(Integer deregIP) {
		this.deregIP = deregIP;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		DeregisteredCustomersVO deRegVO = (DeregisteredCustomersVO)obj;
		
		/*if(null != deRegVO.getRegBy() && null != deRegVO.getFreeDeregDate()) {
			if (this.regBy.equals(deRegVO.getRegBy())
					&& this.freeDeregDate.equals(deRegVO.getFreeDeregDate()))
				result = true;
			
		}*/
		if(null != deRegVO.getXLregBy() && null != deRegVO.getXLDeregDate()) {
			if (this.XLRegBy.equals(deRegVO.getXLregBy())
					&& this.XLDeregDate.equals(deRegVO.getXLDeregDate()))
				result = true;
			
		}
		else if(null != deRegVO.getHPRegBy() && null != deRegVO.getHPDeregDate()) {
			if (this.HPRegBy.equals(deRegVO.getHPRegBy())
					&& this.HPDeregDate.equals(deRegVO.getHPDeregDate()))
				result = true;
			
		}
		else if(null != deRegVO.getIPRegBy() && null != deRegVO.getIPDeregDate()) {
			if (this.IPRegBy.equals(deRegVO.getIPRegBy())
					&& this.IPDeregDate.equals(deRegVO.getIPDeregDate()))
				result = true;
			
		}
		return result;
		
//		return true;
	}
}
