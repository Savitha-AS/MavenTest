package com.mip.application.model;

public class CommunicationChannel extends BaseModel implements
		java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = -5719131989170966867L;

	private int chnId;
	private String chnName;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CommunicationChannel [chnId=");
		builder.append(chnId);
		builder.append(", chnName=");
		builder.append(chnName);
		builder.append("]");
		return builder.toString();
	}

	public int getChnId() {
		return chnId;
	}

	public void setChnId(int chnId) {
		this.chnId = chnId;
	}

	public String getChnName() {
		return chnName;
	}

	public void setChnName(String chnName) {
		this.chnName = chnName;
	}
}
