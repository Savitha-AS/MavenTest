package com.mip.application.model;

public class Menu extends BaseModel implements java.io.Serializable {

	/**
	 * Generated Serial Version ID
	 */
	private static final long serialVersionUID = 16113188905412473L;

	private long menuId;
	private long menuParentId;
	private String menuName;
	private String menuDescription;
	private String menuUrl;
	private String menuTooltip;
	private int menuDisplayOrder;
	private byte active;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Menu [menuId=");
		builder.append(menuId);
		builder.append(", menuParentId=");
		builder.append(menuParentId);
		builder.append(", menuName=");
		builder.append(menuName);
		builder.append(", menuDescription=");
		builder.append(menuDescription);
		builder.append(", menuUrl=");
		builder.append(menuUrl);
		builder.append(", menuTooltip=");
		builder.append(menuTooltip);
		builder.append(", menuDisplayOrder=");
		builder.append(menuDisplayOrder);
		builder.append(", active=");
		builder.append(active);
		builder.append("]");
		return builder.toString();
	}

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}

	public long getMenuParentId() {
		return menuParentId;
	}

	public void setMenuParentId(long menuParentId) {
		this.menuParentId = menuParentId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuDescription() {
		return menuDescription;
	}

	public void setMenuDescription(String menuDescription) {
		this.menuDescription = menuDescription;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public String getMenuTooltip() {
		return menuTooltip;
	}

	public void setMenuTooltip(String menuTooltip) {
		this.menuTooltip = menuTooltip;
	}

	public int getMenuDisplayOrder() {
		return menuDisplayOrder;
	}
	
	public void setMenuDisplayOrder(int menuDisplayOrder) {
		this.menuDisplayOrder = menuDisplayOrder;
	}

	public byte getActive() {
		return active;
	}

	public void setActive(byte active) {
		this.active = active;
	}
}
