package com.mip.framework.castor;

import java.util.Map;

import com.mip.application.view.SMSPlaceHolderVO;

public class SMSPlaceHolders {
	
	private Map<String,SMSPlaceHolderVO> placeHolderMap;
    
    public Map<String,SMSPlaceHolderVO> getPlaceHolderMap() {
        return placeHolderMap;
    }
    
    public void setPlaceHolderMap(Map<String,SMSPlaceHolderVO> placeHolderMap) {
        this.placeHolderMap = placeHolderMap;
    }

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SMSPlaceHolders [placeHolderMap=");
		builder.append(placeHolderMap);
		builder.append("]");
		return builder.toString();
	}
}
