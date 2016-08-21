package com.hagongda.devicebean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Action implements Serializable, ActionType{

	private static final long serialVersionUID = 6949130428132640985L;
	String requestId;
	Integer actionType;
	
	LightInfo info;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public LightInfo getInfo() {
		return info;
	}

	public void setInfo(LightInfo info) {
		this.info = info;
	}

	public int getActionType() {
		return actionType;
	}
	
	
}
