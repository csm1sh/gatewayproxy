package com.hagongda.devicebean;

import java.io.Serializable;
@SuppressWarnings("serial")
public class Alarm implements Serializable{
	private static final long serialVersionUID = 3612477915479662177L;
	//项目网关地址
	String gprs;
	//区域码
	String groupId;
	//路灯编号
	String slaveAddress;
	//路灯类型: 1-太阳能;2-电能  
	String lightType;
	//告警级别： 0-清除；1-告警
	Integer priority;
	//告警时间呢
	Long time;
	//告警内容
	String content;
	public String getGprs() {
		return gprs;
	}
	public void setGprs(String gprs) {
		this.gprs = gprs;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getSlaveAddress() {
		return slaveAddress;
	}
	public void setSlaveAddress(String slaveAddress) {
		this.slaveAddress = slaveAddress;
	}
	public String getLightType() {
		return lightType;
	}
	public void setLightType(String lightType) {
		this.lightType = lightType;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
