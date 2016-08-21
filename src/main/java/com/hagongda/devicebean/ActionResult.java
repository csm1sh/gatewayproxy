package com.hagongda.devicebean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ActionResult implements Serializable{
	private static final long serialVersionUID = 8406916022491996671L;
	String requestId;
	Boolean isSuccess;
	String errorString;
	LightInfo info;
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrorString() {
		return errorString;
	}
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
	public LightInfo getInfo() {
		return info;
	}
	public void setInfo(LightInfo info) {
		this.info = info;
	}
	
}
