package com.hagongda.devicebean;

import java.io.Serializable;
@SuppressWarnings("serial")
public class LightInfo implements Serializable {
	private static final long serialVersionUID = 4618445764276272106L;

	//项目网关地址
	String gprs;
	//区域码
	int groupId;
	//路灯编号
	int slaveAddress;
	//路灯类型: 1-太阳能;2-电能  
	int lightType;
		
	/** action = onoff 时有效 **/
	// on | off
	//on:1 off:0
	boolean on;
	
	/** action = upgrade 时有效 **/
	String version;
	
	/** action = mode 时有效 **/
	//是否经纬度模式
	Boolean geoEnabled;
	//开灯时间 - 距0点的秒数
	Integer onTime;
	//熄灯时间 - 距0点的秒数
	Integer offTime;
	//延迟时间 - 单位：分钟
	Integer offset;
	
	
	/** action = time 时有效 **/
	Long time;
	
	
	/** action = dimming 时有效 **/
	/** 时间单位是分钟 **/
	Integer t1;
	Integer t2;
	Integer t3;
	Integer v1;
	Integer v2;
	Integer v3;
	Integer v4;
	
	/** 能耗 **/
	Long nenghao;
	
	public String getGprs() {
		return gprs;
	}
	public void setGprs(String gprs) {
		this.gprs = gprs;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getSlaveAddress() {
		return slaveAddress;
	}
	public void setSlaveAddress(int slaveAddress) {
		this.slaveAddress = slaveAddress;
	}
	public int getLightType() {
		return lightType;
	}
	public void setLightType(int lightType) {
		this.lightType = lightType;
	}
	public boolean getOnoff() {
		return on;
	}
	public void setOn(boolean on) {
		this.on = on;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Boolean getGeoEnabled() {
		return geoEnabled;
	}
	public void setGeoEnabled(Boolean geoEnabled) {
		this.geoEnabled = geoEnabled;
	}
	public Integer getOnTime() {
		return onTime;
	}
	public void setOnTime(Integer onTime) {
		this.onTime = onTime;
	}
	public Integer getOffTime() {
		return offTime;
	}
	public void setOffTime(Integer offTime) {
		this.offTime = offTime;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public Integer getT1() {
		return t1;
	}
	public void setT1(Integer t1) {
		this.t1 = t1;
	}
	public Integer getT2() {
		return t2;
	}
	public void setT2(Integer t2) {
		this.t2 = t2;
	}
	public Integer getT3() {
		return t3;
	}
	public void setT3(Integer t3) {
		this.t3 = t3;
	}
	public Integer getV1() {
		return v1;
	}
	public void setV1(Integer v1) {
		this.v1 = v1;
	}
	public Integer getV2() {
		return v2;
	}
	public void setV2(Integer v2) {
		this.v2 = v2;
	}
	public Integer getV3() {
		return v3;
	}
	public void setV3(Integer v3) {
		this.v3 = v3;
	}
	public Integer getV4() {
		return v4;
	}
	public void setV4(Integer v4) {
		this.v4 = v4;
	}
	public Long getNenghao() {
		return nenghao;
	}
	public void setNenghao(Long nenghao) {
		this.nenghao = nenghao;
	}
}
